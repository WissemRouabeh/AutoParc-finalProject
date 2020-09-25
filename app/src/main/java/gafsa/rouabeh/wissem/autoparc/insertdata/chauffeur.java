package gafsa.rouabeh.wissem.autoparc.insertdata;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.chauffeurs;
import gafsa.rouabeh.wissem.autoparc.db_connect;

public class chauffeur extends AppCompatActivity {
    EditText nom, prenom, matricule, cin;
    Button ajouter;
    TextView mdate;
    db_connect connect;
    TextView resultat;
    String output;
    DatePickerDialog dated;
    private int annee, mois, jour;
    private StringBuilder Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chauffeur);
        initview();
        setdefaultdate();
        mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatetime().show();
            }
        });
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on principe on fait la verification de l'xistance avant l'insertion
                //exist(matricule.getText().toString());
                if (nom.getText().toString().equals(""))
                    showoutput("Tous les champs sont obligatoires.\n Verifiez vos champs!", "red");
                else if (prenom.getText().toString().equals(""))
                    showoutput("Tous les champs sont obligatoires.\n Verifiez vos champs!", "red");
                else if (matricule.getText().toString().equals(""))
                    showoutput("Tous les champs sont obligatoires.\n Verifiez vos champs!", "red");
                else if (cin.getText().toString().equals(""))
                    showoutput("Tous les champs sont obligatoires.\n Verifiez vos champs!", "red");
                else if (cin.getText().toString().length() < 8)
                    showoutput("Le n° cin doit être egale à 8 chiffres!", "red");
                //TODO: zid verif 3la awel char lazem 0 ou 1

                else if(String.valueOf(matricule.getText().toString().charAt(0)).equals("0"))
                    showoutput("N° matricule ne doit jamais commencée par 0!", "red");
                    //TODO:Matricule ne commencée pas par 0

                else {
                    if (exist(matricule.getText().toString()))
                        insert(matricule.getText().toString(), nom.getText().toString(), prenom.getText().toString(),
                                cin.getText().toString(),
                                mdate.getText().toString()
                        );

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
                output = "Connection non etablit";
                showoutput(output, "red");
            } else {
                String query = "select * from utilisateur where matricule='" + mt + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    check = false;
                    output = "Chauffeur déja existe!\n" +
                            "Matricule:" + mt + " déja existe!";
                    matricule.setText("");
                    showoutput(output, "red");
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            showoutput(e.toString(), "red");

        }
        return check;
    }

    private void setdefaultdate() {
        Calendar cal = Calendar.getInstance();
        int yyyy = cal.get(Calendar.YEAR);
        int MM = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        String datein = yyyy + "-" + MM + "-" + dd;
        mdate.setText(datein);
    }


    protected void initview() {
        matricule = findViewById(R.id.matricule);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ajouter = findViewById(R.id.btnajout);
        resultat = findViewById(R.id.resultat);
        cin = findViewById(R.id.cin);
        mdate = findViewById(R.id.mdate);
    }

    protected void showoutput(String output, String color) {
        resultat.setVisibility(View.VISIBLE);
        if (color == "red")
            resultat.setTextColor(getResources().getColor(R.color.red));
        if (color == "green")
            resultat.setTextColor(getResources().getColor(R.color.green));

        resultat.setText(output);
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

    private void insert(String matricule, String nom, String prenom, String cin, String date) {

        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "Connection non etablit";
                showoutput(output, "red");
            } else {
                String query = "Insert into chauffeur (matricule,Nom,Prenom,cin,date_recrute) values ('" + matricule + "' , '" + nom + "' , '" + prenom +
                        "' , '" + cin + "' , '" + date + "')";
                Statement st1 = cnx.createStatement();
                int result = st1.executeUpdate(query);

                String query1 = "Insert into utilisateur" +
                        " (matricule,cin,pwd,nom,prenom,type,date_recrute) values ('" + matricule +
                        "','" + matricule + "','" +
                        cin + "','" +
                        nom + "','" +
                        prenom + "','"
                        + "Chauffeur" + "','" +
                        date + "')";

                Statement st11 = cnx.createStatement();
                int result1 = st11.executeUpdate(query1);

                if (!(result == 0) || !(result1 == 0)) {
                    output = "Nouveau chauffeur a été ajouté avec succés.";
                    showoutput(output, "green");
                    startActivity(new Intent(chauffeur.this, chauffeurs.class));
                    finish();

                } else {
                    output = "Erreur lors de l'insertion !";
                    showoutput(output, "red");
                }

            }

        } catch (Exception ex) {
            if (ex.toString().contains("matricule")) {
                showoutput("Chauffeur avec matricule= " + matricule + " déja existe !", "red");
            }
            if (ex.toString().contains("cin")) {
                showoutput("N° cin déja existe ! ", "red");
            }



        }
    }


}
