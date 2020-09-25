package gafsa.rouabeh.wissem.autoparc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistevoiture;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_voiture;
import gafsa.rouabeh.wissem.autoparc.insertdata.voiture;
import gafsa.rouabeh.wissem.autoparc.model.modelvoiture;

public class Voitures extends AppCompatActivity {
    ListView listview;
    EditText cherche;
    TextView sort;
    TextView resultat;
    adapteurlistevoiture adapter;
    List list;
    db_connect connect;
    String rzlt;
    View popupviewdelete, popupviewsort;
    AlertDialog popupdelete, popupsort;
    BottomNavigationView bottombar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_voitures);

        /*de stackoverflow pour eviter le clavier lors de louverture l'activité */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initview();

        checkpermession();
        fetchdata();
        adapter = new adapteurlistevoiture(list, this);
        getTotale();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                modelvoiture vh = adapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), fiche_voiture.class);
                intent.putExtra("vh", vh);
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long longueur) {
                modelvoiture vh = adapter.getItem(position);
                popupdelete(String.valueOf(vh.getId()), vh.getMarque(), vh.getmatricule(), vh.getSit_act());
                popupdelete.show();
                return false;
            }
        });
        cherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String input = charSequence.toString();
                Voitures.this.adapter.filtre(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupsorted();
                popupsort.show();
                adapter.notifyDataSetChanged();
            }
        });

        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.it1):
                        startActivity(new Intent(Voitures.this, dashboard_view.class));
                        break;
                    case (R.id.it2):
                        startActivity(new Intent(Voitures.this, voiture.class));
                        break;
                }
                return false;
            }
        });
    }

    private void checkpermession() {
        session session = new session(getApplicationContext());
        checkUser chku = new checkUser(getApplicationContext());
        int id = session.loggedid();
        if (!chku.execute(id).getType().equals("Administrateur")) {
            HashMap<String, String> h = chku.privileges(id);
            if (h.containsKey("V")) {
                if (h.get("V").equals("READ"))
                    bottombar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Voitures.this, dashboard.class));
    }


    protected void initview() {

        list = new ArrayList<>();
        resultat = (TextView) findViewById(R.id.resultat);
        listview = (ListView) findViewById(R.id.listvh);
        sort = (TextView) findViewById(R.id.sort);
        cherche = (EditText) findViewById(R.id.searchbar);
        list = new ArrayList<modelvoiture>();
        bottombar = findViewById(R.id.bottomnavbar);
        //bottombar = (BottomNavigationView) findViewById(R.id.bottomnavbar);

    }

    protected void fetchdata() {
        connect = new db_connect();
        try {
            Connection connection = connect.CONN();
            if (connection == null) {
                rzlt = "no connection";
            } else {
                String query = "select * from vehicule";
                Statement st = connection.createStatement();
                ResultSet resultat = st.executeQuery(query);
                while (resultat.next()) {
                    String query2 = "select * from mission where Id_voiture = '" + resultat.getInt(resultat.findColumn("id")) + "'";
                    Statement st2 = connection.createStatement();
                    ResultSet resultatmissions = st2.executeQuery(query2);
                    boolean work = false;
                    while (resultatmissions.next() && work == false) {
                        work = comparedate(resultatmissions.getTimestamp(resultatmissions.findColumn("Date_debut")),
                                resultatmissions.getTimestamp(resultatmissions.findColumn("Date_fin")));

                        //doub mayalga wahda yekhdem feha fel wa9t lhali yegleb true beli houwa
                        // yekhdem w automatiuement yokhrej mel boucle//
                    }


//int id, String date, String marque, String matricule, String carburant, String puissance, String sit_act,String kilometrage,String consommation
                    list.add(new modelvoiture(
                            resultat.getInt(resultat.findColumn("id")),
                            resultat.getDate(resultat.findColumn("date_ms")).toString(),
                            resultat.getString(resultat.findColumn("marque")),
                            resultat.getString(resultat.findColumn("matricule")),
                            resultat.getString(resultat.findColumn("carburant")),
                            resultat.getString(resultat.findColumn("puissance")),
                            resultat.getString(resultat.findColumn("situation")),
                            resultat.getString(resultat.findColumn("kilometrage")),
                            String.valueOf(resultat.getDouble(resultat.findColumn("consommation"))),
                            work));

                }

                connection.close();
                st.close();
                resultat.close();
            }
        } catch (Exception ex) {
            rzlt = ex.toString();
            //Toast.makeText(getApplicationContext(), rzlt, Toast.LENGTH_SHORT).show();
            resultat.setVisibility(View.VISIBLE);
            resultat.setText(rzlt);
        }

    }


    private boolean comparedate(Date debut, Date fin) {
        Date currentdt = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String current = datetime.format(cal.getTime());
        try {
            currentdt = datetime.parse(current);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentdt.after(debut) && currentdt.before(fin))
            return true;
        else
            return false;


    }

    protected void popupdelete(final String id, final String marque, final String matricule, final String situation) {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupdeletevt, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = (Button) popupviewdelete.findViewById(R.id.oui);
        Button non = (Button) popupviewdelete.findViewById(R.id.non);
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView msg = (TextView) popupviewdelete.findViewById(R.id.msg);
                msg.setText("id:" + id + " | Marque:" + marque + " | Matricule:" + matricule + " | Situation:" + situation);
                try {
                    Connection cnx = connect.CONN();
                    if (!(cnx == null)) {
                        String query = " delete from vehicule where id = '" + id + "'";
                        Statement st1 = cnx.createStatement();
                        int result = st1.executeUpdate(query);
                        if (!(result == 0)) {
                            startActivity(new Intent(Voitures.this, Voitures.class));
                            finish();
                            /*ici l'ajout de l'id de l'utilisateur pour remplir le log a fin d'introduire les droits d'access*/
                            popupdelete.dismiss();
                        }

                    } else {
                        Toast.makeText(Voitures.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                        popupdelete.dismiss();
                    }
                } catch (Exception ex) {
                    rzlt = ex.toString();
                    resultat.setVisibility(View.VISIBLE);
                    resultat.getText();
                }
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete.dismiss();
            }
        });

    }

    protected void popupsorted() {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewsort = inflater.inflate(R.layout.popsortvh, null);
        popupsort = new AlertDialog.Builder(this).create();
        popupsort.setView(popupviewsort);
        RadioGroup group = popupviewsort.findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.sortit:
                        Voitures.this.adapter.sorted(true);//on service tekhdem
                        Voitures.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.disponible:
                        Voitures.this.adapter.sorted(false);//matekhdemch
                        Voitures.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void getTotale() {
        TextView sum=findViewById(R.id.sum);
        sum.setText("Totale: "+String.valueOf(adapter.getCount()));
    }
}
