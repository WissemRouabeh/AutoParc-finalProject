/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.fiches;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.checkUser;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;
import gafsa.rouabeh.wissem.autoparc.session;
import gafsa.rouabeh.wissem.autoparc.utilisateurs;

public class fiche_utilisateur extends AppCompatActivity {
    TextView privileges, delete, suspendu, fonctionne;
    ImageView edit;
    TextView type, viewnom, viewprenom, matricule, viewpwd, mdate, cin;
    TextView changenom, changepr, changemat, changepwd, changecin;
    Switch c, v, m, s;
    RadioButton cr, ca, vr, va, mr, ma;
    int id;
    modeluser us;
    db_connect connect;
    HashMap<String, String> hashmap_view;
    HashMap<String, String> hashmap_update;
    LinearLayout pr, modifier, sup_sus;
    Button submit, annuler;
    View popupviewdelete;
    AlertDialog popupdelete;
    DatePickerDialog dated;
    TextView resultat;
    TextView edittxt;
    String output;
    private int annee, mois, jour;
    private StringBuilder Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_utilisateur);

        us = getIntent().getParcelableExtra("us");
        id = us.getId();

        initview();
        checkpermession();
        if (us.getStatut().equals("Fonctionne")) {

            fonctionne.setVisibility(View.GONE);
            suspendu.setVisibility(View.VISIBLE);
        }
        if (us.getStatut().equals("Suspendu")) {

            suspendu.setVisibility(View.GONE);
            fonctionne.setVisibility(View.VISIBLE);
        }
        mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatetime().show();
            }
        });
        pr.setVisibility(View.GONE);
        switcher_enabled(false);
        radiogroups_enabled(false);

        viewnom.setText(us.getNom());
        viewprenom.setText(us.getPrenom());
        viewpwd.setText(us.getPwd());
        cin.setText(us.getCin());
        mdate.setText(us.getDate());
        matricule.setText(us.getMatricule());
        type.setText(us.getType());


        changepr.setText(us.getPrenom());
        changecin.setText(us.getCin());
        changemat.setText(us.getMatricule());
        changenom.setText(us.getNom());
        changepwd.setText(us.getPwd());

        hashmap_view = privileges();
        setpr();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdeletem();
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setpr();
                radiogroups_enabled(false);
                switcher_enabled(false);
                modifier.setVisibility(View.GONE);
                sup_sus.setVisibility(View.VISIBLE);

                viewnom.setVisibility(View.VISIBLE);
                viewnom.setVisibility(View.VISIBLE);
                viewpwd.setVisibility(View.VISIBLE);

                cin.setVisibility(View.VISIBLE);
                mdate.setClickable(false);
                matricule.setVisibility(View.VISIBLE);

                changenom.setVisibility(View.GONE);
                changepr.setVisibility(View.GONE);
                changepwd.setVisibility(View.GONE);
                changecin.setVisibility(View.GONE);
                changemat.setVisibility(View.GONE);
                pr.setVisibility(View.GONE);
            }
        });
        privileges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pr.getVisibility() == View.VISIBLE) {
                    pr.setVisibility(View.GONE);
                    setpr();
                    radiogroups_enabled(false);
                    switcher_enabled(false);
                    modifier.setVisibility(View.GONE);
                    sup_sus.setVisibility(View.VISIBLE);

                } else
                    pr.setVisibility(View.VISIBLE);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        fonctionne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_statut("Fonctionne");
                us.setStatut("Fonctionne");
            }
        });
        suspendu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_statut("Suspendu");
                us.setStatut("Suspendu");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashmap_update = getprivileges();
                update_privileges(hashmap_update);
                us.setNom(changenom.getText().toString());
                us.setPrenom(changepr.getText().toString());
                us.setDate(mdate.getText().toString());
                us.setPwd(changepwd.getText().toString());
                us.setCin(changecin.getText().toString());
                us.setMatricule(changemat.getText().toString());

                if (exist(changemat.getText().toString()))
                    if (changecin.getText().toString().length() == 8)
                        popupedit();
                    else {
                        output = "Le n° cin doit être egale a 8 chiffres.";
                        showoutput(output, "red");
                    }
                changepr.setText(us.getPrenom());
                changecin.setText(us.getCin());
                changemat.setText(us.getMatricule());
                changenom.setText(us.getNom());
                changepwd.setText(us.getPwd());


            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!c.isEnabled()) {
                    switcher_enabled(true);
                    setprivileges();
                    setpr();
                    modifier.setVisibility(View.VISIBLE);
                    sup_sus.setVisibility(View.GONE);

                    viewnom.setVisibility(View.GONE);
                    viewprenom.setVisibility(View.GONE);
                    cin.setVisibility(View.GONE);
                    mdate.setClickable(true);
                    matricule.setVisibility(View.GONE);
                    viewpwd.setVisibility(View.GONE);

                    changenom.setVisibility(View.VISIBLE);
                    changepr.setVisibility(View.VISIBLE);
                    changepwd.setVisibility(View.VISIBLE);
                    changecin.setVisibility(View.VISIBLE);
                    changemat.setVisibility(View.VISIBLE);
                    changepwd.setVisibility(View.VISIBLE);

                } else {
                    setpr();
                    radiogroups_enabled(false);
                    switcher_enabled(false);
                    modifier.setVisibility(View.GONE);
                    sup_sus.setVisibility(View.VISIBLE);

                    viewnom.setVisibility(View.VISIBLE);
                    viewnom.setVisibility(View.VISIBLE);
                    cin.setVisibility(View.VISIBLE);
                    mdate.setClickable(false);
                    matricule.setVisibility(View.VISIBLE);

                    changenom.setVisibility(View.GONE);
                    changepr.setVisibility(View.GONE);
                    changepwd.setVisibility(View.GONE);
                    changecin.setVisibility(View.GONE);
                    changemat.setVisibility(View.GONE);

                }

            }
        });
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ca.setEnabled(true);
                    cr.setEnabled(true);
                } else {
                    ca.setEnabled(false);
                    cr.setEnabled(false);
                    cr.setChecked(false);
                    cr.setChecked(false);
                }
            }

        });
        m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ma.setEnabled(true);
                    mr.setEnabled(true);
                } else {
                    ma.setEnabled(false);
                    mr.setEnabled(false);
                    ma.setChecked(false);
                    mr.setChecked(false);
                }
            }

        });
        v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    va.setEnabled(true);
                    vr.setEnabled(true);
                } else {
                    va.setEnabled(false);
                    vr.setEnabled(false);
                    va.setChecked(false);
                    vr.setChecked(false);
                }
            }

        });

    }

    private void update(modeluser us) {
        Connection cnx;
        try {
            cnx = connect.CONN();
            if (!(cnx == null)) {
                String query = "Update utilisateur set nom = '" + us.getNom() + "',prenom='" + us.getPrenom() +
                        "',cin='" + us.getCin() + "',date_recrute='" + us.getDate() + "',pwd='" + us.getPwd() + "',matricule='" + us.getMatricule()
                        +
                        "' where matricule='" + matricule.getText().toString() + "'";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);

               if(us.getType().equals("Chauffeur")){
                   String query1 = "Update Chauffeur set Nom = '" + us.getNom() + "',Prenom='" + us.getPrenom() +
                           "',cin='" + us.getCin() + "',date_recrute='" + us.getDate() + "',matricule='" + us.getMatricule()
                           +
                           "' where matricule='" + matricule.getText().toString() + "'";
                   Statement st1 = cnx.createStatement();
                   st1.executeUpdate(query1);
               }

                if ( r > 0) {
                    //Toast.makeText(this, "Utilisateur bien modifié", Toast.LENGTH_SHORT).show();

                    output = "Modification a été effectuée avec succés.";
                    showoutput(output, "green");
                    startActivity(new Intent(getApplicationContext(),utilisateurs.class));
                    finish();
                } else {
                    //Toast.makeText(getBaseContext(), "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                    output = "Erreur lors de la modification.";
                    showoutput(output, "red");
                }

            }

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean exist(String mt) {
        connect = new db_connect();
        boolean check = true;
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Connection non etablit", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * from utilisateur where matricule='" + mt + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    if (!matricule.getText().toString().equals(mt)) {
                        check = false;
                        //Toast.makeText(this, "Matricule:" + mt + " déja existe!", Toast.LENGTH_SHORT).show();

                        output = "Matricule:" + mt + " déja existe!";
                        showoutput(output, "red");
                        changemat.setText(matricule.getText().toString());
                    }

                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return check;
    }


    private void checkpermession() {

        session session = new session(getApplicationContext());
        checkUser chku = new checkUser(getApplicationContext());
        int id = session.loggedid();
        modeluser current = chku.execute(id);
        if (!current.getType().equals("Administrateur")) {
            edit.setVisibility(View.GONE);
            edittxt.setVisibility(View.GONE);
            suspendu.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
    }

    private void initview() {
        privileges = findViewById(R.id.privileges);
        suspendu = findViewById(R.id.suspendu);
        edittxt = findViewById(R.id.edittxt);
        fonctionne = findViewById(R.id.Fonctionne);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        type = findViewById(R.id.type);
        viewnom = findViewById(R.id.viewnom);
        viewprenom = findViewById(R.id.viewprenom);
        matricule = findViewById(R.id.Matricule);
        viewpwd = findViewById(R.id.viewpwd);
        pr = findViewById(R.id.pr);
        changepr = findViewById(R.id.changepr);
        changecin = findViewById(R.id.changecin);
        changemat = findViewById(R.id.changemat);
        changenom = findViewById(R.id.changenom);
        changepwd = findViewById(R.id.changepwd);
        cin = findViewById(R.id.cin);
        mdate = findViewById(R.id.daterecrute);
        submit = findViewById(R.id.submit_update);
        annuler = findViewById(R.id.annuler);
        modifier = findViewById(R.id.modifier);
        resultat = findViewById(R.id.resultat);
        sup_sus = findViewById(R.id.sup_sus);
        c = findViewById(R.id.C);
        v = findViewById(R.id.V);
        m = findViewById(R.id.M);
        cr = findViewById(R.id.cr);
        ca = findViewById(R.id.ca);
        vr = findViewById(R.id.vr);
        va = findViewById(R.id.va);
        ma = findViewById(R.id.ma);
        mr = findViewById(R.id.mr);
        connect = new db_connect();

    }


    private void setpr() {
        if (hashmap_view.containsKey("M")) {
            m.setChecked(true);
            if (hashmap_view.get("M").equals("ALL"))
                ma.setChecked(true);
            else mr.setChecked(true);
        } else {
            m.setChecked(false);
            ma.setChecked(false);
            mr.setChecked(false);
        }
        if (hashmap_view.containsKey("C")) {
            c.setChecked(true);
            if (hashmap_view.get("C").equals("ALL"))
                ca.setChecked(true);
            else cr.setChecked(true);
        } else {
            c.setChecked(false);
            ca.setChecked(false);
            cr.setChecked(false);
        }
        if (hashmap_view.containsKey("V")) {
            v.setChecked(true);
            if (hashmap_view.get("V").equals("ALL"))
                va.setChecked(true);
            else vr.setChecked(true);
        } else {
            v.setChecked(false);
            va.setChecked(false);
            vr.setChecked(false);
        }

    }

    private HashMap<String, String> privileges() {
        HashMap<String, String> hash = new HashMap<>();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null)
                Toast.makeText(this, "Connection non etablit ! ", Toast.LENGTH_SHORT).show();
            else {
                String query = "select * from access where id_user= '" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                while (r.next()) {
                    hash.put(r.getString(r.findColumn("ou")),
                            r.getString(r.findColumn("crud")));
                }

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return hash;
    }

    public void switcher_enabled(boolean b) {
        c.setEnabled(b);
        v.setEnabled(b);
        m.setEnabled(b);
    }

    public void radiogroups_enabled(boolean b) {
        cr.setEnabled(b);
        ca.setEnabled(b);
        vr.setEnabled(b);
        va.setEnabled(b);
        mr.setEnabled(b);
        ma.setEnabled(b);
    }


    public void setprivileges() {
        c.setClickable(true);
        v.setClickable(true);
        m.setClickable(true);
        if (c.isChecked()) {
            cr.setEnabled(true);
            ca.setEnabled(true);
        }
        if (m.isChecked()) {
            mr.setEnabled(true);
            ma.setEnabled(true);
        }
        if (v.isChecked()) {
            vr.setEnabled(true);
            va.setEnabled(true);
        }

    }

    protected void update_statut(String s) {
        Connection cnx;
        try {
            cnx = connect.CONN();
            if (!(cnx == null)) {
                String query = "Update utilisateur set statut = '" + s + "' where id='" + id + "'";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                if (r > 0)
                    if (s.equals("Fonctionne")) {
                        suspendu.setVisibility(View.VISIBLE);
                        fonctionne.setVisibility(View.GONE);
                    }
                if (s.equals("Suspendu")) {
                    suspendu.setVisibility(View.GONE);
                    fonctionne.setVisibility(View.VISIBLE);
                }
                output = "Modification a été effectuée avec succés.";
                showoutput(output, "green");
            }

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void update_privileges(HashMap<String, String> update) {
        HashMap<String, String> hashmap = update;
        Connection cnx;
        try {
            cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                Statement delete = cnx.createStatement();

                Statement check = cnx.createStatement();
                ResultSet r = check.executeQuery("select * from access where id_user = '" + id + "'");
                if (r.next())
                    delete.executeUpdate("Delete from access where id_user = '" + id + "'");

                String query = "Insert into access (id_user,ou,crud) values (?,?,?) ";
                PreparedStatement st = cnx.prepareStatement(query);
                for (Map.Entry<String, String> entry : hashmap.entrySet()) {
                    st.setInt(1, id);
                    st.setString(2, entry.getKey());
                    st.setString(3, entry.getValue());
                    st.executeUpdate();
                }

                r.close();
                check.close();
                st.close();
                delete.close();
                cnx.close();

                hashmap_view = privileges();
                setpr();
                radiogroups_enabled(false);
                switcher_enabled(false);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    protected HashMap<String, String> getprivileges() {
        HashMap<String, String> hashmap = new HashMap<>();
        if (c.isChecked()) {
            if (ca.isChecked())
                hashmap.put("C", "ALL");
            if (cr.isChecked())
                hashmap.put("C", "READ");
        }
        if (v.isChecked()) {
            if (va.isChecked())
                hashmap.put("V", "ALL");
            if (vr.isChecked())
                hashmap.put("V", "READ");
        }
        if (m.isChecked()) {
            if (ma.isChecked())
                hashmap.put("M", "ALL");
            if (mr.isChecked())
                hashmap.put("M", "READ");
        }

        return hashmap;
    }

    protected void delete() {
        Connection cnx;
        try {
            cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                Statement deleteaccess = cnx.createStatement();
                Statement deleteuser = cnx.createStatement();
                Statement check = cnx.createStatement();
                ResultSet r = check.executeQuery("select * from access where id_user = '" + id + "'");
                if (r.next())
                    deleteaccess.executeUpdate("Delete from access where id_user = '" + id + "'");
                int v = deleteuser.executeUpdate("delete from utilisateur where id='" + id + "'");
                if (v > 0) {
                    //Toast.makeText(this, "Utilisateur supprimé ! ", Toast.LENGTH_SHORT).show();

                    output = "Un Utilisateur a été supprimé avec succés.";
                    showoutput(output, "green");

                } else {
                    output = "Erreur lors de la suppression.";
                    showoutput(output, "red");
                }
                //Toast.makeText(this, "Echec lors de la supprission", Toast.LENGTH_SHORT).show();


                cnx.close();
                deleteaccess.close();
                deleteuser.close();
                r.close();
                check.close();

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    protected void popupdeletem() {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupdeleteuser, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = popupviewdelete.findViewById(R.id.oui);
        Button non = popupviewdelete.findViewById(R.id.non);
        popupdelete.show();
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
                popupdelete.dismiss();
                startActivity(new Intent(fiche_utilisateur.this, utilisateurs.class));
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete.dismiss();
            }
        });
    }

    protected void popupedit() {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupedit, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = popupviewdelete.findViewById(R.id.oui);
        Button non = popupviewdelete.findViewById(R.id.non);
        popupdelete.show();
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(us);
                popupdelete.dismiss();
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete.dismiss();
            }
        });
    }


    private DatePickerDialog showdatetime() {

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int aa, int mm,
                                  int jj) {
                annee = aa;
                mois = mm;
                jour = jj;
                Date = new StringBuilder();
                Date.append(annee).append("-");
                if (mois < 10)
                    Date.append("0").append(mois + 1).append("-");
                else
                    Date.append(mois + 1).append("-");

                if (jour < 10)
                    Date.append("0").append(jour);
                else
                    Date.append(jour);

                mdate.setText(Date);

            }
        };
        Calendar c = Calendar.getInstance();
        int aa = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        dated = new DatePickerDialog(this, dateListener, aa, mm, dd);
        return dated;


    }

    protected void showoutput(String output, String color) {
        resultat.setVisibility(View.VISIBLE);
        if (color == "red")
            resultat.setTextColor(getResources().getColor(R.color.red));
        if (color == "green")
            resultat.setTextColor(getResources().getColor(R.color.green));

        resultat.setText(output);
    }

}
