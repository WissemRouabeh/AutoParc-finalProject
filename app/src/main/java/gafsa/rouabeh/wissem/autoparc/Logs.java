/*
 ** all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlog;
import gafsa.rouabeh.wissem.autoparc.model.modellog;

public class Logs extends AppCompatActivity {

    EditText searchbar;
    ListView listview;
    adapteurlog adapter;
    List<modellog> list;
    db_connect connect;
    session session;
    View viewpopup;
    AlertDialog logpopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        session = new session(getApplicationContext());
        connect = new db_connect();
        initview();
        fetchdata();
        adapter = new adapteurlog(list, getApplicationContext());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                affiche(adapter.getItem(position));
            }
        });

    }

    private void affiche(modellog item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        viewpopup = inflater.inflate(R.layout.popuplog, null);
        logpopup = new AlertDialog.Builder(this).create();
        logpopup.setView(viewpopup);
        Button oui = viewpopup.findViewById(R.id.oui);
        TextView msg = viewpopup.findViewById(R.id.msg);
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logpopup.dismiss();
            }
        });

        if (item.getType().equals("Voiture")) {

            if (item.getAction().equals("UPDATE")) {
                String[] ancien = item.getAncien().replace("|", "&").split("&");
                String[] nouveau = item.getNouveau().replace("|", "&").split("&");
                StringBuilder itemv = new StringBuilder();
                itemv.append("Voiture:\n");

                itemv.append("Matricule:\n");
                itemv.append("ancien: " + ancien[0] + " nouveau:" + nouveau[0] + "\n");

                itemv.append("Marque:\n");
                itemv.append("ancien: " + ancien[1] + " nouveau:" + nouveau[1] + "\n");

                itemv.append("Kilometrage:\n");
                itemv.append("ancien: " + ancien[2] + " nouveau:" + nouveau[2] + "\n");

                itemv.append("Carburant:\n");
                itemv.append("ancien: " + ancien[3] + " nouveau:" + nouveau[3] + "\n");

                itemv.append("Consommation:\n");
                itemv.append("ancien: " + ancien[4] + " nouveau:" + nouveau[4] + "\n");

                itemv.append("Puissance:\n");
                itemv.append("ancien: " + ancien[5] + " nouveau:" + nouveau[5] + "\n");

                itemv.append("Date mise en circulation:\n");
                itemv.append("ancien: " + ancien[6] + " nouveau:" + nouveau[6] + "\n");

                itemv.append("Situation actuel:\n");
                itemv.append("ancien: " + ancien[7] + " nouveau:" + nouveau[7] + "\n");

                msg.setText(itemv);
            }
            if (item.getAction().equals("DELETE")) {
                String[] ancien = item.getAncien().replace("|", "&").split("&");
                StringBuilder itemv = new StringBuilder();
                itemv.append("Voiture:\n");

                itemv.append("Matricule:\n");
                itemv.append("ancien: " + ancien[0] + "\n");

                itemv.append("Marque:\n");
                itemv.append("ancien: " + ancien[1] + "\n");

                itemv.append("Kilometrage:\n");
                itemv.append("ancien: " + ancien[2] + "\n");

                itemv.append("Carburant:\n");
                itemv.append("ancien: " + ancien[3] + "\n");

                itemv.append("Consommation:\n");
                itemv.append("ancien: " + ancien[4] + "\n");

                itemv.append("Puissance:\n");
                itemv.append("ancien: " + ancien[5] + "\n");

                itemv.append("Date mise en circulation:\n");
                itemv.append("ancien: " + ancien[6] + "\n");

                itemv.append("Situation actuel:\n");
                itemv.append("ancien: " + ancien[7] + "\n");

                msg.setText(itemv);
            }


        }
        if (item.getType().equals("Mission")) {
            if (item.getAction().equals("UPDATE")) {

                String ancien[] = item.getAncien().replace("|", "&").split("&");
                String nouveau[] = item.getNouveau().replace("|", "&").split("&");
                StringBuilder itemv = new StringBuilder();
                itemv.append("Mission:\n");
                itemv.append("Trajet:\n ");
                itemv.append("ancien: " + ancien[3] + " nouveau:" + nouveau[2] + "\n");

                itemv.append("Chauffeur:\n ");
                itemv.append("ancien: " + ancien[4] + " nouveau:" + nouveau[3] + "\n");

                itemv.append("Voiture:\n ");
                itemv.append("ancien: " + ancien[5] + " nouveau:" + nouveau[4] + "\n");

                itemv.append("Date Debut:\n ");
                itemv.append("ancien: " + ancien[1] + " nouveau:" + nouveau[0] + "\n");
                if (!ancien[2].contains("2050")) {
                    itemv.append("Date Fin:\n ");
                    itemv.append("ancien: " + ancien[2] + " nouveau:" + nouveau[1] + "\n");

                }
                msg.setText(itemv);
            }
            if (item.getAction().equals("DELETE")) {

                String ancien[] = item.getAncien().replace("|", "&").split("&");
                StringBuilder itemv = new StringBuilder();
                itemv.append("Mission:\n");
                itemv.append("Trajet: \n");
                itemv.append("ancien: " + ancien[3] + "\n");

                itemv.append("Chauffeur: \n");
                itemv.append("ancien: " + ancien[4] + "\n");

                itemv.append("Voiture: \n");
                itemv.append("ancien: " + ancien[5] + "\n");

                itemv.append("Date Debut:\n ");
                itemv.append("ancien: " + ancien[1] + "\n");

                if (!ancien[2].contains("2050")) {
                    itemv.append("Date Fin: \n");
                    itemv.append("ancien: " + ancien[2] + "\n");

                }
                msg.setText(itemv);
            }


        }
        if (item.getType().equals("Chauffeur")) {
            if (item.getAction().equals("UPDATE")) {
                String ancien[] = item.getAncien().replace("|", "&").split("&");
                String nouveau[] = item.getNouveau().replace("|", "&").split("&");

                StringBuilder itemv = new StringBuilder();
                itemv.append("Chauffeur:\n");
                itemv.append("Matricule:\n ");
                itemv.append("ancien: " + ancien[0] + " nouveau:" + nouveau[0] + "\n");

                itemv.append("Cin:\n ");
                itemv.append("ancien: " + ancien[1] + " nouveau:" + nouveau[1] + "\n");

                itemv.append("Nom:\n ");
                itemv.append("ancien: " + ancien[2] + " nouveau:" + nouveau[2] + "\n");

                itemv.append("Prenom:\n ");
                itemv.append("ancien: " + ancien[3] + " nouveau:" + nouveau[3] + "\n");

                itemv.append("Date Recrutement:\n ");
                itemv.append("ancien: " + ancien[4] + " nouveau:" + nouveau[4] + "\n");

                msg.setText(itemv);
            }
            if (item.getAction().equals("DELETE")) {
                String ancien[] = item.getAncien().replace("|", "&").split("&");

                StringBuilder itemv = new StringBuilder();
                itemv.append("Chauffeur:\n");
                itemv.append("Matricule:\n ");
                itemv.append("ancien: " + ancien[0] + "\n");

                itemv.append("Cin:\n ");
                itemv.append("ancien: " + ancien[1] + "\n");

                itemv.append("Nom:\n ");
                itemv.append("ancien: " + ancien[2] + "\n");

                itemv.append("Prenom:\n ");
                itemv.append("ancien: " + ancien[3] + "\n");

                itemv.append("Date Recrutement: \n");
                itemv.append("ancien: " + ancien[4] + "\n");

                msg.setText(itemv);
            }


        }


        logpopup.show();
    }


    private void initview() {
        searchbar = findViewById(R.id.searchbar);
        listview = findViewById(R.id.listview);
        list = new ArrayList<>();
    }

    private void fetchdata() {
        Connection cnx;
        checkUser chku = new checkUser(getApplicationContext());
        String query;
        try {
            cnx = connect.CONN();
            if (cnx != null) {
                if (chku.execute(session.loggedid()).getType().equals("Administrateur")) {
                    query = "select * from logs";
                } else
                    query = "select * from logs where id_user='" + session.loggedid() + "'";

                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);

                while (r.next()) {
                    int id = r.getInt(r.findColumn("id"));
                    int u_id = r.getInt(r.findColumn("id_user"));
                    String date = r.getTimestamp(r.findColumn("date")).toString();
                    String type = r.getString(r.findColumn("object_type"));
                    String action = r.getString(r.findColumn("action"));
                    String ancien = r.getString(r.findColumn("niveau"));
                    String nouveau = r.getString(r.findColumn("nouveau"));
                    String getnp = "select nom,prenom from utilisateur where id='" + String.valueOf(u_id) + "'";
                    ResultSet r2 = cnx.createStatement().executeQuery(getnp);
                    while (r2.next()) {
                        String np = r2.getString(1) + " " + r2.getString(2);
                        list.add(new modellog(id, u_id, type, ancien, nouveau, action, np, date));
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            searchbar.setText(e.toString());
        }
    }

}
