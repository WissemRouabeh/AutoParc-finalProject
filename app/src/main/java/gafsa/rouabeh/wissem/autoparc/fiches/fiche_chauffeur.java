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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.chauffeurs;
import gafsa.rouabeh.wissem.autoparc.checkUser;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.model.modelchauffeur;
import gafsa.rouabeh.wissem.autoparc.session;

public class fiche_chauffeur extends AppCompatActivity {
    int id;
    int mcount, vcount;
    db_connect connect;
    ImageView edit;
    TextView nom, prenom, input_nbr_ms, input_nbr_vh, cin, matricule, datein;
    TextView viewnom, viewprenom;
    EditText changenom, changeprenom;
    EditText changecin, changemat;
    LinearLayout modifier;
    Button delete, submit, annuler;
    AlertDialog popupdelete;
    View popupviewdelete;
    session session;
    modelchauffeur ch;
    DatePickerDialog dated;
    TextView resultat;
    TextView edittxt;
    String output;
    private int annee, mois, jour;
    private StringBuilder Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_chauffeur);
        connect = new db_connect();
        session = new session(getApplicationContext());
        ch = getIntent().getParcelableExtra("ch");
        id = ch.getId();

        initview();
        checkpermession();

        getmv();
        getvh();

        input_nbr_ms.setText(String.valueOf(mcount));
        input_nbr_vh.setText(String.valueOf(vcount));

        changenom.setText(ch.getNom());
        nom.setText(ch.getNom());
        changeprenom.setText(ch.getPrenom());
        prenom.setText(ch.getPrenom());
        cin.setText(ch.getCin());
        changecin.setText(ch.getCin());
        matricule.setText(String.valueOf(ch.getMatricule()));
        changemat.setText(String.valueOf(ch.getMatricule()));
        datein.setText(ch.getDaterecrute());


        viewprenom.setText(ch.getPrenom());
        viewnom.setText(ch.getNom());
        datein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatetime().show();
            }
        });
        datein.setClickable(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modifier.getVisibility() == View.VISIBLE) {
                    modifier.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);

                    viewnom.setVisibility(View.VISIBLE);
                    viewprenom.setVisibility(View.VISIBLE);
                    matricule.setVisibility(View.VISIBLE);
                    cin.setVisibility(View.VISIBLE);
                    datein.setClickable(false);

                    changenom.setVisibility(View.GONE);
                    changeprenom.setVisibility(View.GONE);
                    changecin.setVisibility(View.GONE);
                    changemat.setVisibility(View.GONE);

                } else {
                    modifier.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);

                    viewnom.setVisibility(View.GONE);
                    viewprenom.setVisibility(View.GONE);
                    matricule.setVisibility(View.GONE);
                    cin.setVisibility(View.GONE);

                    changenom.setVisibility(View.VISIBLE);
                    changeprenom.setVisibility(View.VISIBLE);
                    datein.setClickable(true);
                    changemat.setVisibility(View.VISIBLE);
                    changecin.setVisibility(View.VISIBLE);
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


                if (changenom.getText().toString().equals("")) {
                    changenom.setError("Champ est vide");
                } else if (changeprenom.getText().toString().equals("")) {
                    changeprenom.setError("Champ est vide");
                } else if (changecin.getText().toString().equals("")) {
                    changecin.setError("Champ est vide");
                } else if (changemat.getText().toString().equals("")) {
                    changemat.setError("Champ est vide");
                } else {

                    if (exist(matricule.getText().toString())) {
                        if (changecin.getText().toString().length() == 8) {
                            ch.setNom(changenom.getText().toString());
                            ch.setPrenom(changeprenom.getText().toString());
                            ch.setMatricule(Integer.parseInt(changemat.getText().toString()));
                            ch.setDaterecrute(datein.getText().toString());
                            ch.setCin(changecin.getText().toString());
                            popupedit();

                        } else{
                            output = "Le n° cin doit être egale a 8 chiffres.";
                            showoutput(output, "red");
                        }
                            //Toast.makeText(fiche_chauffeur.this, "Le n° cin doit etre egale a 8 chiffres", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
                        Toast.makeText(this, "Matricule:" + mt + " déja existe!", Toast.LENGTH_SHORT).show();
                        changemat.setText(matricule.getText().toString());
                    }

                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    private void changeview() {

        modifier.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);

        viewnom.setVisibility(View.VISIBLE);
        viewnom.setVisibility(View.VISIBLE);
        matricule.setVisibility(View.VISIBLE);
        cin.setVisibility(View.VISIBLE);

        changenom.setVisibility(View.GONE);
        changenom.setVisibility(View.GONE);
        changemat.setVisibility(View.GONE);
        changecin.setVisibility(View.GONE);
        datein.setClickable(false);
    }

    private void initview() {

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);

        viewnom = findViewById(R.id.viewnom);
        viewprenom = findViewById(R.id.viewprenom);
        matricule = findViewById(R.id.matricule);
        cin = findViewById(R.id.cin);
        datein = findViewById(R.id.datein);

        changenom = findViewById(R.id.changenom);
        changeprenom = findViewById(R.id.changeprenom);
        changecin = findViewById(R.id.changecin);
        changemat = findViewById(R.id.changemat);

        input_nbr_ms = findViewById(R.id.input_nbr_ms);
        input_nbr_vh = findViewById(R.id.input_nbr_vh);
        edit = findViewById(R.id.edit);
        resultat = findViewById(R.id.resultat);
        submit = findViewById(R.id.submit_update);
        delete = findViewById(R.id.delete);
        annuler = findViewById(R.id.annuler);
        modifier = findViewById(R.id.modifier);
        edittxt = findViewById(R.id.edittxt);
        datein.setClickable(false);
    }

    private void getmv() {

        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select count(*) nm from mission where Id_chauffeur ='" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    mcount = r.getInt(r.findColumn("nm"));
                } else {
                    mcount = 0;

                }

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "97846513" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getvh() {

        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select count(DISTINCT mission.Id_voiture) nv from mission where Id_chauffeur = '" + id + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    vcount = r.getInt(r.findColumn("nv"));
                } else {
                    vcount = 0;
                }

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "aze" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void supprimer(int id) {
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "Delete from chauffeur where Id ='" + id + "'";
                String query1 = "Delete from utilisateur where matricule ='" + String.valueOf(ch.getMatricule()) + "'";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                st.executeUpdate(query1);

                if (r != 0) {
                    insertlogs(session.loggedid(), "Chauffeur",
                            matricule.getText().toString() + "|" + cin.getText().toString() + "|" + viewnom.getText().toString() + "|" + viewprenom.getText().toString() + "|" + datein.getText().toString(),
                            "vide", "DELETE");
                    //Toast.makeText(getBaseContext(), "Un chauffeur a été supprimé avec succés", Toast.LENGTH_SHORT).show();

                    output = "Un chauffeur a été supprimé avec succés.";
                    showoutput(output, "green");

                    startActivity(new Intent(getApplicationContext(), chauffeurs.class));
                } else {
                    output = "Erreur lors de la suppression.";
                    showoutput(output, "red");
                }

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void upp(modelchauffeur chg) {
        Connection cnx;
        try {
            cnx = connect.CONN();
            if (!(cnx == null)) {
                String query = "Update utilisateur set nom = '" + ch.getNom() + "',prenom='" + ch.getPrenom() +
                        "',cin='" + ch.getCin() + "',date_recrute='" + ch.getDaterecrute() + "',matricule='" + String.valueOf(ch.getMatricule())
                        +
                        "' where matricule='" + matricule.getText().toString() + "'";
                Statement st = cnx.createStatement();
                st.executeUpdate(query);
                String query1 = "Update Chauffeur set Nom = '" + ch.getNom() + "',Prenom='" + ch.getPrenom() +
                        "',cin='" + ch.getCin() + "',date_recrute='" + ch.getDaterecrute() + "',matricule='" + String.valueOf(ch.getMatricule())
                        +
                        "' where matricule='" + matricule.getText().toString() + "'";
                int r1 = st.executeUpdate(query1);
                if (r1 != 0 ) {

                    output = "Modification a été effectuée avec succés.";
                    showoutput(output, "green");

                    insertlogs(session.loggedid(), "Chauffeur",
                            matricule.getText().toString() + "|" + cin.getText().toString() + "|" + viewnom.getText().toString() + "|" + viewprenom.getText().toString() + "|" + datein.getText().toString(),
                            String.valueOf(ch.getMatricule()) + "|" + ch.getCin() + "|" + ch.getNom() + "|" + ch.getPrenom() + "|" + ch.getDaterecrute(),
                            "UPDATE");
                    startActivity(new Intent(getApplicationContext(), chauffeurs.class));
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

    private void modif(String n, String p) {
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Echec lors de connexion", Toast.LENGTH_SHORT).show();
            } else {
                String query = "Update chauffeur " +
                        "Set nom='" + n + "',prenom='" + p + "'" +
                        "where Id ='" + id + "'";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                if (r != 0) {
                    if (checkinputchange()) {
                        Toast.makeText(getBaseContext(), "Modification effectué", Toast.LENGTH_SHORT).show();
                        insertlogs(session.loggedid(), "Chauffeur",
                                String.valueOf(ch.getMatricule()) + "|" + viewnom.getText().toString() + "|" + viewprenom.getText().toString(),
                                String.valueOf(ch.getMatricule()) + "|" + changenom.getText().toString() + "|" + changeprenom.getText().toString(),
                                "UPDATE");
                    }

                    /*changenom.setText(n);
                    changeprenom.setText(p);
                    viewprenom.setText(n);
                    viewnom.setText(p);
                    nom.setText(n);
                    prenom.setText(p);
                    changeview();*/

                    startActivity(new Intent(getApplicationContext(), chauffeurs.class));
                    finish();

                } else {
                    Toast.makeText(getBaseContext(), "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void popupdelete(final int id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupdelete, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        popupdelete.show();
        Button oui = popupviewdelete.findViewById(R.id.oui);
        Button non = popupviewdelete.findViewById(R.id.non);
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supprimer(id);
                popupdelete.dismiss();
                startActivity(new Intent(fiche_chauffeur.this, chauffeurs.class));
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete.dismiss();
            }
        });
    }

    private void checkpermession() {
        session session = new session(getApplicationContext());
        checkUser chku = new checkUser(getApplicationContext());
        int id = session.loggedid();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap = chku.privileges(id);
        if (!chku.execute(id).getType().equals("Administrateur")) {
            if (hashMap.containsKey("C")) {
                if (hashMap.get("C").contains("READ")) {
                    edit.setVisibility(View.GONE);
                    edittxt.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
            }

        }
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
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkinputchange() {
        boolean b = true;
        if (changenom.getText().toString().equals(viewnom.getText().toString()) && changeprenom.getText().toString().equals(viewprenom.getText().toString()))
            b = false;
        return b;
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

                datein.setText(Date);

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
                upp(ch);
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
