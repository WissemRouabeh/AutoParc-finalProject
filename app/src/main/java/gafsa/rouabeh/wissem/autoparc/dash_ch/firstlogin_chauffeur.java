/*
 * *all code wrote by wissem rouabeh**
 */

/*
 * *all code wrote by wissem rouabeh**
 */

/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.dash_ch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Statement;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.db_connect;

public class firstlogin_chauffeur extends AppCompatActivity {
    EditText nom, prenom;
    Button ajouter;
    TextView resultat;
    db_connect connect;
    String output;
    int matricule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chauffeur_dashboard);
        initview();
        matricule = getIntent().getIntExtra("matricule",0000000);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on principe on fait la verification de l'xistance avant l'insertion
                //checkfirst(matricule.getText().toString(),nom.getText().toString() , prenom.getText().toString());
                if (nom.getText().toString().equals(""))
                    nom.setError("Champ obligatoire");
                else if (prenom.getText().toString().equals(""))
                    prenom.setError("Champ obligatoire");
                else {
                    insert( nom.getText().toString() , prenom.getText().toString(),String.valueOf(matricule));
                    final int seconds = 2;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(firstlogin_chauffeur.this, chauffeur_dashboard.class));
                            finish();
                        }
                    }, seconds * 100);
                }
            }
        });

    }
    private void insert(String nom, String prenom, String matricule) {

        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "no connection";
                showoutput(output, "red");
            } else {
                String query = " insert into chauffeur (Nom,Prenom,matricule) Values('" + nom + "','" + prenom + "','" + matricule + "')";
                Statement st1 = cnx.createStatement();
                int result = st1.executeUpdate(query);

                if (!(result == 0)) {
                    output = "Chauffeur Activé !";
                    showoutput(output, "green");
                } else {
                    output = "Erreur lors de l'activation !";
                    showoutput(output, "red");
                }
            }
        } catch (Exception ex) {
            output = ex.toString();
            showoutput(output, "red");
        }

    }
    protected void initview() {
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ajouter =  findViewById(R.id.btnajout);
        resultat =  findViewById(R.id.resultat);
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
