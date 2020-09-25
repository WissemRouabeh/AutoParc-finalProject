package gafsa.rouabeh.wissem.autoparc.insertdata;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.Voitures;
import gafsa.rouabeh.wissem.autoparc.db_connect;

public class voiture extends AppCompatActivity {
    EditText model;
    EditText immat;
    EditText kilometrage;
    EditText consommation;
    Button ajouter;
    TextView resultat, mdate;
    db_connect connect;
    String output;
    String Matricule;
    Spinner puissance, carburant, sit_act;
    DatePickerDialog dated;
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<String> adapter3;
    private int annee, mois, jour;
    private StringBuilder Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiture);
        initview();

        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.puissance));
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.carburant));
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.sit_act));

        puissance.setAdapter(adapter1);
        carburant.setAdapter(adapter2);
        sit_act.setAdapter(adapter3);


        mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatetime().show();
            }
        });

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultat.setVisibility(View.GONE);

                if (immat.getText().toString().length() == 6 || model.getText().toString().equals("")) {

                    Matricule = "09-"+immat.getText().toString().substring(0,3)+"-"+immat.getText().toString().substring(3,6);
                    insert(model.getText().toString(),
                            Matricule,
                            carburant.getSelectedItem().toString(),
                            puissance.getSelectedItem().toString(),
                            sit_act.getSelectedItem().toString(),
                            kilometrage.getText().toString(),
                            consommation.getText().toString(),
                            mdate.getText().toString()
                    );


                } else {

                    if (model.getText().toString().equals(""))
                        output = "Champ marque n'est pas valide !";
                    else
                        output = "Champ de 6 chiffres obligatoire ";
                    showoutput(output, "red");

                }

            }
        });

    }


    private void insert(String mrq, String mat, String carb, String puiss, String sit,String kilometrage,String consommation, String date) {

        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "no connection";
                showoutput(output, "red");
            } else {
                //String query = " insert into voiture (voiture_mar,num_serie,num_chasses) Values('" + model + "','" + matricule + "','" + numchassis + "')";
                String query1 = "INSERT INTO vehicule(marque,matricule,carburant,puissance,situation,kilometrage,consommation,date_ms) " +
                        "values ('" + mrq + "','"
                        + mat + "','"
                        + carb + "','"
                        + puiss + "','"
                        + sit + "','"
                        + kilometrage + "','"
                        + consommation + "','"
                        + date + "')";

                Statement st1 = cnx.createStatement();

                int result = st1.executeUpdate(query1);

                if (!(result == 0)) {
                    output = "Nouvelle voiture a été ajoutée avec succés.";
                    showoutput(output, "green");
                    startActivity(new Intent(getApplicationContext(), Voitures.class));
                } else {
                    output = "Erreur lors de l'insertion !";
                    showoutput(output, "red");
                }


            }
        } catch (Exception ex) {
            output = ex.toString();
            resultat.setVisibility(View.VISIBLE);
            resultat.setText(output);
        }

    }

    protected void initview() {
        model = findViewById(R.id.model);
        immat = findViewById(R.id.numserie);
        ajouter = findViewById(R.id.btnajout);
        resultat = findViewById(R.id.resultat);
        puissance = findViewById(R.id.puissance);
        carburant = findViewById(R.id.carburant);
        sit_act = findViewById(R.id.sit_act);
        mdate = findViewById(R.id.mdate);
        consommation = findViewById(R.id.consommation);
        kilometrage = findViewById(R.id.kilometrage);

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
