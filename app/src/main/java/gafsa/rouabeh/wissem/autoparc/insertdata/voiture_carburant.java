/*
 * *all code wrote by wissem rouabeh**
 */

/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.insertdata;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurbon;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.model.modelbon;

public class voiture_carburant extends AppCompatActivity {
    EditText qt, kilo, num_bon;
    TextView dt;
    LinearLayout block_ajout;
    RelativeLayout ajoutbtn;
    Button submit, annuler;
    ListView listview;
    db_connect connect;
    int id;
    int k, cons;
    List<modelbon> list;
    adapteurbon adapter;
    StringBuilder Date;
    DatePickerDialog dated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiture_carburant);
        id = getIntent().getIntExtra("idd", 0);

        initview();

        ajoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                block_ajout.setVisibility(View.VISIBLE);
                ajoutbtn.setVisibility(View.GONE);
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dt.setText("");
                qt.setText("");
                kilo.setText("");
                num_bon.setText("");
                block_ajout.setVisibility(View.GONE);
                ajoutbtn.setVisibility(View.VISIBLE);
                startActivity(getIntent());
                finish();
            }
        });
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showgetdate();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:check les inputs
                if (checkinput()) {
                    get_carburant();
                    int q = Integer.valueOf(qt.getText().toString());
                    int ki = Integer.valueOf(kilo.getText().toString());
                    int num = Integer.valueOf(num_bon.getText().toString());
                    if (check_kilo(ki, num)) {
                        set_bon(Integer.valueOf(num_bon.getText().toString()), dt.getText().toString(), q, ki, id);
                        update_voiture(id, ki);
                    } else
                        submit.setError("Verifiez vos données s'ils existe déja !!");
                }
            }
        });

        fetchdata();
        adapter = new adapteurbon(list, this);
        listview.setAdapter(adapter);

    }

    protected void fetchdata() {
        connect = new db_connect();
        try {
            Connection connection = connect.CONN();
            if (connection == null) {

            } else {
                String query = "select * from bons where id_voiture='" + id + "' Order by Date_bon DESC";
                Statement st = connection.createStatement();
                ResultSet resultat = st.executeQuery(query);
                while (resultat.next()) {
                    String query2 = "select * from voiture where voiture_id='" + id + "'";
                    Statement st2 = connection.createStatement();
                    ResultSet re = st2.executeQuery(query2);
                    while (re.next()) {
                        list.add(new modelbon(resultat.getInt(1),
                                String.valueOf(resultat.getInt(2)),
                                String.valueOf(resultat.getInt(3)),
                                String.valueOf(resultat.getInt(4)),
                                String.valueOf(resultat.getInt(5)),
                                String.valueOf(resultat.getDate(6)),
                                String.valueOf(resultat.getDouble(7)),
                                String.valueOf(resultat.getInt(8)),
                                re.getString(2),
                                re.getString(3),
                                resultat.getString(9)
                        ));
                    }

                    re.close();
                    st2.close();

                }

                connection.close();
                st.close();
                resultat.close();
            }
        } catch (Exception ex) {

        }

    }

    private void initview() {
        connect = new db_connect();
        dt = findViewById(R.id.date);
        qt = findViewById(R.id.qt);
        kilo = findViewById(R.id.kilo);
        submit = findViewById(R.id.submit);
        annuler = findViewById(R.id.annuler);
        block_ajout = findViewById(R.id.block_ajout_bon);
        ajoutbtn = findViewById(R.id.ajout);
        num_bon = findViewById(R.id.num);
        listview = findViewById(R.id.listview);
        list = new ArrayList<>();
    }

    protected void get_carburant() {
        Connection cnx = connect.CONN();
        try {
            if (cnx != null) {
                String query = "select kilom,cons_moy from voiture where voiture_id='" + id + "'";
                ResultSet r = cnx.createStatement().executeQuery(query);
                if (r.next()) {
                    k = r.getInt(1);
                    cons = r.getInt(2);
                }
            }
        } catch (Exception e) {
        }

    }

    protected void set_bon(int num_bon, String d, int q, int ka, int id_voiture) {
        //d=date_inseré q=qt_insere ka=kilo_inseré //
        Connection cnx = connect.CONN();
        int prix = q;
        int qt = prix / 2;
        try {
            if (cnx != null) {
                String query = "insert into bons(num_bon,Date_bon,qt_carburant,Prix,kilo_ancien,Id_voiture)" +
                        "values ('" + num_bon + "','" + d + "','" + qt + "','" + prix + "','" + ka + "','" + id_voiture + "')";
                int r = cnx.createStatement().executeUpdate(query);
                if (r != 0) {
                    String que = "select num_bon,kilo_ancien,qt_carburant from bons where statut='encours' and id_voiture='" + id_voiture + "'";
                    ResultSet rr = cnx.createStatement().executeQuery(que);
                    if (rr.next()) {
                        int i = rr.getInt(1);
                        int kilo_ancien = rr.getInt(rr.findColumn("kilo_ancien"));
                        int qt_b_ancien = rr.getInt(rr.findColumn("qt_carburant"));
                        int kilo_cons = ka - kilo_ancien;

                        double cons = qt_b_ancien * 100 / kilo_cons;
                        cons = (double) Math.round(cons * 100) / 100;
                        if (cons > 30)
                            cons = 30;
                        if (cons < 0)
                            cons = 0;
                        String update = "UPDATE bons set kilo_cons='" + String.valueOf(kilo_cons) + "',pourcentage_cons='" + String.valueOf(cons) + "',statut='consommee' " +
                                "where id_voiture='" + id_voiture + "' and num_bon='" + i + "' LIMIT 1";
                        cnx.createStatement().executeUpdate(update);
                    }
                }
            }
        } catch (Exception e) {
            if (e.toString().contains("divide")) {
                Toast.makeText(this, "C'est le premier bon deposé pour cette voiture", Toast.LENGTH_SHORT).show();
            }
        }

    }

    protected void update_voiture(int id_voiture, int kilom) {
        Connection cnx = connect.CONN();
        try {
            if (cnx != null) {
                String getcons = "select AVG(pourcentage_cons),count(*) from bons where id_voiture='" + id_voiture + "'";
                ResultSet r = cnx.createStatement().executeQuery(getcons);
                Double cons;
                int count;
                if (r.next()) {
                    cons = r.getDouble(1);
                    count = r.getInt(2);
                    String setcons = "update voiture set total_bons='" + String.valueOf(count) + "',cons_moy='" + String.valueOf(cons) + "',kilom='" + String.valueOf(kilom) + "' where voiture_id='" + id_voiture + "'";
                    int rr = cnx.createStatement().executeUpdate(setcons);
                    if (rr != 0) {
                        Toast.makeText(this, "Bon bien deposé!", Toast.LENGTH_SHORT).show();
                        block_ajout.setVisibility(View.GONE);
                        ajoutbtn.setVisibility(View.VISIBLE);
                        finish();
                        startActivity(getIntent());
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    protected boolean check_kilo(int i, int num) {
        boolean check = true;

        Connection cnx = connect.CONN();

        try {
            if (cnx != null) {
                String query = "select MAX(kilo_ancien),num_bon from bons where id_voiture='" + id + "'";
                ResultSet r = cnx.createStatement().executeQuery(query);
                int max;
                if (r.next()) {
                    max = r.getInt(1);
                    if (max >= i)
                        check = false;
                    if (num == r.getInt(2))
                        check = false;

                }

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return check;
    }

    private void showgetdate() {

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int aa, int mm,
                                  int jj) {
                int annee = aa;
                int mois = mm;
                int jour = jj;
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

                dt.setText(Date.toString());


            }
        };

        Calendar c = Calendar.getInstance();
        int aa = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        dated = new DatePickerDialog(this, dateListener, aa, mm, dd);
        dated.show();


    }

    protected boolean checkinput() {
        Boolean check = true;
        if (dt.getText().equals("")) {
            dt.setError("Champ obligatoire !");
            check = false;
        }
        if (qt.getText().toString().equals("")) {
            qt.setError("Champ obligatoire !");
            check = false;
        }else if (Integer.parseInt(qt.getText().toString())<10) {
            qt.setError("Montant doit étre superieur a 10 TND !");
            check = false;
        }

        if (kilo.getText().toString().equals("")) {
            check = false;
            kilo.setError("Champ obligatoire !");
        }
        if (num_bon.getText().toString().equals("")) {
            check = false;
            num_bon.setError("Champ obligatoire !");
        }

        return check;
    }

}
