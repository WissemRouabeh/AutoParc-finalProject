/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class checkUser {
    Context mContext;
    db_connect dbinstance;
    modeluser user;

    public checkUser(Context mContext) {
        this.mContext = mContext;
        dbinstance = new db_connect();
    }

    public modeluser execute(int id) {
        try {
            Connection cnx = dbinstance.CONN();
            if (cnx == null) {
                Toast.makeText(mContext, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * from utilisateur where id ='" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    user = new modeluser(r.getInt(r.findColumn("id")),
                            r.getString(r.findColumn("nom")),
                            r.getString(r.findColumn("prenom")),
                            r.getString(r.findColumn("pwd")),
                            r.getString(r.findColumn("matricule")),
                            r.getString(r.findColumn("type")),
                            r.getString(r.findColumn("statut")),
                            r.getString(r.findColumn("cin")),
                            r.getDate(r.findColumn("date_recrute")).toString()
                    );
                }

            }
        } catch (Exception ex) {
            Log.e("TAG WISSEM", ex.toString());
        }
        return user;
    }

    public modeluser getid(String matricule) {
        modeluser us=new modeluser() ;
        try {
            Connection cnx = dbinstance.CONN();
            if (cnx == null) {
                Toast.makeText(mContext, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * from utilisateur where matricule ='" + matricule + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    us = new modeluser(r.getInt(r.findColumn("id")),
                            r.getString(r.findColumn("nom")),
                            r.getString(r.findColumn("prenom")),
                            r.getString(r.findColumn("pwd")),
                            r.getString(r.findColumn("matricule")),
                            r.getString(r.findColumn("type")),
                            r.getString(r.findColumn("statut")),
                            r.getString(r.findColumn("cin")),
                            r.getDate(r.findColumn("date_recrute")).toString()
                    );
                }

            }
        } catch (Exception ex) {
            Log.e("TAG WISSEM", ex.toString());
        }
        return us;
    }

    public boolean check_ch(int id) {
        boolean b=false;
        try {
            Connection cnx = dbinstance.CONN();
            if (cnx == null) {
                Toast.makeText(mContext, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * from chauffeur where Id ='" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next())
                    b=true;
            }
        } catch (Exception ex) {
            Log.e("TAG WISSEM", ex.toString());
        }
        return b;

    }
    public int ch(int id) {
        int b=-1;
        try {
            Connection cnx = dbinstance.CONN();
            if (cnx == null) {
                Toast.makeText(mContext, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select matricule from chauffeur where Id ='" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next())
                    b=r.getInt(1);
            }
        } catch (Exception ex) {
            Log.e("TAG WISSEM", ex.toString());
        }
        return b;
    }

    public boolean user_deleted(int id) {
        boolean c = true;
        try {
            Connection cnx = dbinstance.CONN();
            if (cnx == null) {
                Toast.makeText(mContext, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * from utilisateur where id ='" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    c = false;
                    new modeluser(r.getInt(r.findColumn("id")),
                            r.getString(r.findColumn("nom")),
                            r.getString(r.findColumn("prenom")),
                            r.getString(r.findColumn("pwd")),
                            r.getString(r.findColumn("matricule")),
                            r.getString(r.findColumn("type")),
                            r.getString(r.findColumn("statut")),
                            r.getString(r.findColumn("cin")),
                            r.getDate(r.findColumn("date_recrute")).toString()
                    );
                }

            }
        } catch (Exception ex) {
            Log.e("TAG WISSEM", ex.toString());
        }
        return c;
    }

    public HashMap<String, String> privileges(int id) {

        HashMap<String, String> hash = new HashMap<>();
        Connection cnx;
        try {
            cnx = dbinstance.CONN();
            if (cnx == null)
                Toast.makeText(mContext, "Connection non etablit ! ", Toast.LENGTH_SHORT).show();
            else {
                String query = "select * from access where id_user= '" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                while (r.next()) {
                    hash.put(r.getString(r.findColumn("ou")),
                            r.getString(r.findColumn("crud")));
                }
                st.close();
                r.close();
                cnx.close();

            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return hash;
    }


}
