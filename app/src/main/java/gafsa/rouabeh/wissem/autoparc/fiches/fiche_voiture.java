/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.fiches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.Voitures;
import gafsa.rouabeh.wissem.autoparc.checkUser;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.insertdata.voiture_carburant;
import gafsa.rouabeh.wissem.autoparc.model.modelvoiture;
import gafsa.rouabeh.wissem.autoparc.session;

public class fiche_voiture extends AppCompatActivity {
    int id;
    int mcount, ccount, bcount;
    db_connect connect;
    ImageView edit;
    TextView modele, input_nbr_ms, input_nbr_ch, input_nbr_bn;
    TextView viewmodele, viewserie, viewcarb, viewpuiss, viewsit, viewkilo, viewcons, mdate;
    EditText changemodele, changeserie, changkilo, changcons;
    Spinner changcarb, changpuiss, changsit;
    LinearLayout modifier;
    Button delete, submit, annuler;
    AlertDialog popupdelete;
    View popupviewdelete;
    CardView bon_vh;
    session session;
    String cons;
    String kilom;
    ArrayAdapter<String> adapter1, adapter2, adapter3;
    modelvoiture vh;
    TextView resultat;
    TextView edittxt;
    String output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_voiture);
        connect = new db_connect();
        session = new session(getApplicationContext());
        vh = getIntent().getParcelableExtra("vh");

        id = vh.getId();
        Toast.makeText(this, vh.getmatricule(), Toast.LENGTH_SHORT).show();
        getmv();
        getch();
        getbn();
        getcons();
        initview();


        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.puissance));
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.carburant));
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.sit_act));

        changsit.setAdapter(adapter3);
        changcarb.setAdapter(adapter2);
        changpuiss.setAdapter(adapter1);

        checkpermession();

        input_nbr_ms.setText(String.valueOf(mcount));
        input_nbr_ch.setText(String.valueOf(ccount));
        input_nbr_bn.setText(String.valueOf(bcount));

        modele.setText(vh.getMarque());
        viewmodele.setText(vh.getMarque());
        viewserie.setText(vh.getmatricule());
        viewcarb.setText(vh.getCarburant());
        viewcons.setText(vh.getConsommation());
        viewkilo.setText(vh.getKilometrage());
        mdate.setText(vh.getDate());
        viewpuiss.setText(vh.getPuissance());
        viewsit.setText(vh.getSit_act());

        changemodele.setText(vh.getMarque());
        changeserie.setText(vh.getmatricule().substring(3, vh.getmatricule().length()).replace("-", ""));
        changcarb.setSelection(adapter2.getPosition(vh.getCarburant()));
        changcons.setText(vh.getConsommation());
        changkilo.setText(vh.getKilometrage());
        changpuiss.setSelection(adapter1.getPosition(vh.getPuissance()));
        changsit.setSelection(adapter3.getPosition(vh.getSit_act()));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modifier.getVisibility() == View.VISIBLE) {

                    modifier.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);

                    viewmodele.setVisibility(View.VISIBLE);
                    viewserie.setVisibility(View.VISIBLE);
                    viewcarb.setVisibility(View.VISIBLE);
                    viewcons.setVisibility(View.VISIBLE);
                    viewkilo.setVisibility(View.VISIBLE);
                    mdate.setClickable(false);
                    viewpuiss.setVisibility(View.VISIBLE);
                    viewsit.setVisibility(View.VISIBLE);


                    changemodele.setVisibility(View.GONE);
                    changeserie.setVisibility(View.GONE);
                    changcarb.setVisibility(View.GONE);
                    changcons.setVisibility(View.GONE);
                    changkilo.setVisibility(View.GONE);
                    changpuiss.setVisibility(View.GONE);
                    changsit.setVisibility(View.GONE);
                } else {
                    modifier.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);

                    viewmodele.setVisibility(View.GONE);
                    viewserie.setVisibility(View.GONE);
                    viewcarb.setVisibility(View.GONE);
                    viewcons.setVisibility(View.GONE);
                    viewkilo.setVisibility(View.GONE);
                    mdate.setClickable(true);
                    viewpuiss.setVisibility(View.GONE);
                    viewsit.setVisibility(View.GONE);

                    changemodele.setVisibility(View.VISIBLE);
                    changeserie.setVisibility(View.VISIBLE);
                    changcarb.setVisibility(View.VISIBLE);
                    changcons.setVisibility(View.VISIBLE);
                    changkilo.setVisibility(View.VISIBLE);
                    changpuiss.setVisibility(View.VISIBLE);
                    changsit.setVisibility(View.VISIBLE);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete(id);

            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeview();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.setMarque(changemodele.getText().toString());
                vh.setMatricule("09-" + changeserie.getText().toString());
                vh.setConsommation(changcons.getText().toString());
                vh.setKilometrage(changkilo.getText().toString());
                vh.setSit_act(changsit.getSelectedItem().toString());
                vh.setDate(mdate.getText().toString());
                vh.setCarburant(changcarb.getSelectedItem().toString());
                vh.setPuissance(changpuiss.getSelectedItem().toString());
                popupedit();

            }
        });



    }

    private void update(modelvoiture vh) {
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {

                String query = "Delete from vehicule where id ='" + id + "'";
                String query1 = "INSERT INTO vehicule(id,marque,matricule,carburant,puissance,situation,kilometrage,consommation,date_ms) " +
                        "values ('" + vh.getId() + "','"
                        + vh.getMarque() + "','"
                        + vh.getmatricule() + "','"
                        + vh.getCarburant() + "','"
                        + vh.getPuissance() + "','"
                        + vh.getSit_act() + "','"
                        + vh.getKilometrage() + "','"
                        + vh.getConsommation() + "','"
                        + vh.getDate() + "')";
                Statement st = cnx.createStatement();
                st.executeUpdate(query);
                int r = st.executeUpdate(query1);
                if (r != 0) {
                    //Toast.makeText(getBaseContext(), "Voiture modifiés", Toast.LENGTH_SHORT).show();

                    output = "Modification a été effectuée avec succés.";
                    showoutput(output, "green");

                    changeview();

                    insertlogs(session.loggedid(), "Voiture",
                            viewserie.getText().toString() + "|" + viewmodele.getText().toString() + "|" + viewkilo.getText().toString() + "|" + viewcarb.getText().toString() + "|" + viewcons.getText().toString() + "|" + viewpuiss.getText().toString() + "|" + mdate.getText().toString() + "|" + viewsit.getText().toString(),
                            vh.getmatricule() + "|" + vh.getMarque() + "|" + vh.getKilometrage() + "|" + vh.getCarburant() + "|" + vh.getConsommation() + "|" + vh.getPuissance() + "|" + vh.getDate() + "|" + vh.getSit_act(),
                            "UPDATE");

                    startActivity(new Intent(getApplicationContext(), Voitures.class));

                } else {
                    //Toast.makeText(getBaseContext(), "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                    output = "Erreur lors de la modification.";
                    showoutput(output, "red");
                }
            }

        } catch (Exception e) {
            if (e.toString().contains("duplicate"))
                Toast.makeText(this, "Matricule déja existe !", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkpermession() {
        session session = new session(getApplicationContext());
        checkUser chku = new checkUser(getApplicationContext());
        int id = session.loggedid();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap = chku.privileges(id);
        if (!chku.execute(id).getType().equals("Admnistrateur")) {
            if (hashMap.containsKey("V")) {
                if (hashMap.get("V").contains("READ")) {
                    edit.setVisibility(View.GONE);
                    edittxt.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
            }

        }
    }

    private void initview() {
        modele = findViewById(R.id.Modele);

        viewmodele = findViewById(R.id.viewmodele);
        viewserie = findViewById(R.id.viewserie);
        viewcarb = findViewById(R.id.viewcarb);
        viewcons = findViewById(R.id.viewcons);
        viewkilo = findViewById(R.id.viewkilo);
        viewpuiss = findViewById(R.id.viewpuiss);
        viewsit = findViewById(R.id.viewsit);
        edittxt = findViewById(R.id.edittxt);

        changemodele = findViewById(R.id.changemodele);
        changeserie = findViewById(R.id.changeserie);
        changcarb = findViewById(R.id.changcarb);
        changcons = findViewById(R.id.changcons);
        changpuiss = findViewById(R.id.changpuiss);
        changsit = findViewById(R.id.changsit);
        mdate = findViewById(R.id.mdate);
        changkilo = findViewById(R.id.changkilo);

        input_nbr_ms = findViewById(R.id.input_nbr_ms);
        input_nbr_ch = findViewById(R.id.input_nbr_ch);
        input_nbr_bn = findViewById(R.id.input_nbr_bon);
        edit = findViewById(R.id.edit);
        submit = findViewById(R.id.submit_update);
        delete = findViewById(R.id.delete);
        annuler = findViewById(R.id.annuler);
        modifier = findViewById(R.id.modifier);
        bon_vh = findViewById(R.id.bon_vh);
        resultat = findViewById(R.id.resultat);
    }


    private void getmv() {

        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select count(*) nm from mission where Id_voiture ='" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    mcount = r.getInt(r.findColumn("nm"));
                } else {
                    mcount = 0;

                }

            }


        } catch (Exception e) {
        }
    }

    private void getcons() {

        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select cons_moy,kilom from voiture where voiture_id ='" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    cons = String.valueOf(r.getDouble(r.findColumn("cons_moy")));
                    kilom = String.valueOf(r.getInt(r.findColumn("kilom")));
                } else {
                    mcount = 0;

                }

            }


        } catch (Exception e) {
        }
    }

    private void getch() {

        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select count(DISTINCT mission.Id_chauffeur) nv from mission where Id_voiture = '" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    ccount = r.getInt(r.findColumn("nv"));
                } else {
                    ccount = 0;
                }

            }


        } catch (Exception e) {
        }
    }

    private void getbn() {

        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select count(*) nv from bons where id_voiture = '" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    bcount = r.getInt(r.findColumn("nv"));
                } else {
                    bcount = 0;
                }

            }


        } catch (Exception e) {

        }
    }

    private void supprimer(int id) {
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "Delete from vehicule where id ='" + id + "'";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                if (r != 0) {
                    //Toast.makeText(getBaseContext(), "Voiture est supprimé", Toast.LENGTH_SHORT).show();

                    output = "Une voiture a été supprimée avec succés.";
                    showoutput(output, "green");

                    insertlogs(session.loggedid(), "Voiture",
                            viewserie.getText().toString() + "|" + viewmodele.getText().toString() + "|" + viewkilo.getText().toString() + "|" + viewcarb.getText().toString() + "|" + viewcons.getText().toString() + "|" + viewpuiss.getText().toString() + "|" + mdate.getText().toString() + "|" + viewsit.getText().toString(),
                            "vide", "DELETE");
                    startActivity(new Intent(getApplicationContext(), Voitures.class));
                } else {
                    //Toast.makeText(getBaseContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    output = "Erreur lors de la suppression.";
                    showoutput(output, "red");
                }

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    protected void popupdelete(final int id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupdeletevt, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = popupviewdelete.findViewById(R.id.oui);
        Button non = popupviewdelete.findViewById(R.id.non);
        popupdelete.show();
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supprimer(id);
                popupdelete.dismiss();
                onResume();
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete.dismiss();
            }
        });
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
    }*/

    private void changeview() {
        modifier.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);

        viewmodele.setVisibility(View.VISIBLE);
        viewserie.setVisibility(View.VISIBLE);
        viewcarb.setVisibility(View.VISIBLE);
        viewcons.setVisibility(View.VISIBLE);
        viewkilo.setVisibility(View.VISIBLE);
        mdate.setClickable(false);
        viewpuiss.setVisibility(View.VISIBLE);
        viewsit.setVisibility(View.VISIBLE);

        changemodele.setVisibility(View.GONE);
        changeserie.setVisibility(View.GONE);
        changcarb.setVisibility(View.GONE);
        changcons.setVisibility(View.GONE);
        changkilo.setVisibility(View.GONE);
        changpuiss.setVisibility(View.GONE);
        changsit.setVisibility(View.GONE);
    }

    protected void insertlogs(int id, String type, String niveau, String nouveau, String action) {
        Connection cnx;
        try {
            cnx = connect.CONN();
            String query = "insert into logs (id_user,object_type,niveau,nouveau,action)" +
                    "values ('" + String.valueOf(id) + "','" + type + "','" + niveau + "','" + nouveau + "','" + action + "')";
            Statement st = cnx.createStatement();
            st.executeUpdate(query);

        } catch (Exception e) {
            Toast.makeText(this, "wisseme" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

   /* private boolean checkinputchange() {
        boolean b = true;
        if (changemodele.getText().toString().equals(viewmodele.getText().toString()) &&
                changechass.getText().toString().equals(viewchass.getText().toString()) &&
                changeserie.getText().toString().equals(viewserie.getText().toString())
                )
            b = false;
        return b;
    }*/
   protected void showoutput(String output, String color) {
       resultat.setVisibility(View.VISIBLE);
       if (color == "red")
           resultat.setTextColor(getResources().getColor(R.color.red));
       if (color == "green")
           resultat.setTextColor(getResources().getColor(R.color.green));

       resultat.setText(output);
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
                update(vh);
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
}
