/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;

import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class Changeinfos extends AppCompatActivity {
    modeluser user;
    TextView nom, prenom, cin, type, infos, pwd, matricule, date;
    Button appliq, annul;
    DatePickerDialog dated;
    db_connect connect;
    private StringBuilder Date;
    private int annee, mois, jour;
    TextView resultat;
    String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfos);
        initview();
        user = getIntent().getParcelableExtra("user");
        setviews();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatetime().show();
            }
        });
        appliq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verification())
                update();
                else
                    showoutput("Tous les champs sont obligatoires.\n Verifiez vos champs!", "red");
//TODO: rigel les verif 3al les inputs..
            }
        });
        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });


    }

    private boolean verification() {
        boolean v = true;
        if(nom.getText().toString().equals(""))
            v=false;
        if(prenom.getText().toString().equals(""))
            v=false;
        if(date.getText().toString().equals(""))
            v=false;
        if (cin.getText().toString().length() < 8){
            v=false;
            showoutput("Le n° cin doit être egale à 8 chiffres!", "red");
        }
        return v;
    }

    private void update() {
        connect=new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (!(cnx == null)) {
               /* String query = "update utilisateur set nom = '" + nom.getText().toString() + "',prenom='" + prenom.getText().toString() +
                        "',cin='" + cin.getText().toString() + "',date_recrute='" + date.getText().toString() +
                        "'where matricule='" + "06549" + "'";
                Statement st = cnx.createStatement();*/

                String querye="update utilisateur set nom=?, prenom=?, cin=?, date_recrute=? where matricule= ?";
                PreparedStatement stt = cnx.prepareStatement(querye);
                stt.setString(1,nom.getText().toString());
                stt.setString(2,prenom.getText().toString());
                stt.setString(3,cin.getText().toString());
                stt.setString(4,date.getText().toString());
                stt.setString(5,user.getMatricule());
                int r = stt.executeUpdate();
                if (r != 0) {
                    showoutput("Modification a été effectuée avec succés.", "green");
                    //Toast.makeText(this, "Modification bien effectués", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Changeinfos.this,Profile.class));
                    finish();
                }else
                    showoutput("Erreur lors de la modification!", "red");


            }

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    private void setviews() {
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        cin.setText(user.getCin());
        date.setText(user.getDate());
    }

    private void initview() {
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        cin = findViewById(R.id.cin);
        date = findViewById(R.id.date);
        appliq = findViewById(R.id.appliq);
        annul = findViewById(R.id.annul);

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

                date.setText(Date);

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
