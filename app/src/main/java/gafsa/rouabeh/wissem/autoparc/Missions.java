/*
 * *all code wrote by wissem rouabeh**
 */

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

import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistemissions;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_mission;
import gafsa.rouabeh.wissem.autoparc.insertdata.mission;
import gafsa.rouabeh.wissem.autoparc.model.modelmission;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class Missions extends AppCompatActivity {
    List list;
    db_connect connect;
    TextView resultat, sort;
    EditText searchbar;
    ListView listview;
    BottomNavigationView bottombar;
    adapteurlistemissions adapter;
    String output;
    AlertDialog popupdelete;
    AlertDialog popupsort;
    View popupviewdelete;
    View popupviewsort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_missions);

        /*de stackoverflow pour eviter le clavier lors de louverture l'activité */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initview();
        checkpermession();
        data_fetch();

        adapter = new adapteurlistemissions(getApplicationContext(), list);
        getTotale();
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);

        /*listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                modelmission ms = adapter.getItem(position);
                popupdelete(String.valueOf(ms.getId()),
                        ms.getNom_ch(),
                        ms.getPrenom_ch(),
                        ms.getTrajet()
                );
                popupdelete.show();
                return true;
            }
        });*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                modelmission ms=adapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(),fiche_mission.class);
                intent.putExtra("ms",ms);
                startActivity(intent);
            }
        });
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String input = charSequence.toString();
                Missions.this.adapter.filtre(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.it1):
                        startActivity(new Intent(Missions.this, dashboard_view.class));
                        break;
                    case (R.id.it2):
                        startActivity(new Intent(Missions.this, mission.class));
                        break;
                }
                return false;
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupsorted();
                adapter.notifyDataSetChanged();
            }
        });


    }
    private void checkpermession() {
        session session = new session(getApplicationContext());
        checkUser chku = new checkUser(getApplicationContext());
        int id = session.loggedid();
        if(!chku.execute(id).getType().equals("Administrateur")){
            HashMap<String,String> h = chku.privileges(id);
            if(h.get("M").equals("READ"))
                bottombar.setVisibility(View.GONE);
        }
    }
    private void initview() {
        list = new ArrayList<modelmission>();
        resultat = (TextView) findViewById(R.id.resultat);
        sort = (TextView) findViewById(R.id.sort);
        searchbar = (EditText) findViewById(R.id.searchbar);
        listview = (ListView) findViewById(R.id.listview);
        bottombar = (BottomNavigationView) findViewById(R.id.bottomnavbar);
    }

    private void data_fetch() {

        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {

                output = "no connection";
                showoutput(output, "red");
///output
            } else {
                //String query = "select chauffeur.Id,chauffeur.Nom,chauffeur.Prenom,mission.Date_debut,mission.Date_fin from chauffeur,mission where chauffeur.Id = mission.Id_chauffeur";
                //on ne peut pas utiliser la requetet precedente car nous somme on besoin aussi de chauffeurs qui non pas affecter a aucune mission
                //String query = " select * from mission";
                String query1 = "select mission.ID,mission.Id_chauffeur,mission.Id_voiture,nom, prenom, marque, trajet,vehicule.matricule,Date_debut,Date_fin from mission left join chauffeur on mission.Id_chauffeur = chauffeur.Id left join vehicule on mission.Id_voiture = vehicule.id ";
                Statement st1 = cnx.createStatement();
                ResultSet result = st1.executeQuery(query1);
                while (result.next()) {
                    //SimpleDateFormat datetimeformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String datedebut = result.getTimestamp(result.findColumn("Date_debut")).toString();
                    String datefin = result.getTimestamp(result.findColumn("Date_fin")).toString();
                    //ou juste on ajoute tostring();
                    String service="";
                    String when = comparedate(result.getTimestamp(result.findColumn("Date_debut")));
                    if (when.equals("today")){
                        service="green";
                    }
                    else if (when.equals("soon"))
                        service="blue";
                    else if(when.equals("recent")){
                        service="brown";
                        String terminer = terminer(result.getTimestamp(result.findColumn("Date_fin")));
                        if(terminer.equals("terminer")){
                            service="grey";
                        }
                    }



                    list.add(new modelmission(result.getInt(result.findColumn("ID")),
                            result.getString(result.findColumn("nom")),
                            result.getString(result.findColumn("prenom")),
                            result.getString(result.findColumn("marque")),
                            result.getString(result.findColumn("matricule")),
                            result.getString(result.findColumn("trajet")),
                            datedebut,
                            datefin,
                            service,
                            result.getInt(result.findColumn("Id_chauffeur")),0,
                            result.getInt(result.findColumn("Id_voiture"))
                    ));

                }
            }
        } catch (Exception ex) {
            ///output
            output = ex.toString();
            showoutput(output, "red");

        }
    }

    protected void popupdelete(final String id, final String trajet, final String nom, final String prenom) {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupdelete, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = (Button) popupviewdelete.findViewById(R.id.oui);
        Button non = (Button) popupviewdelete.findViewById(R.id.non);
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView msg = (TextView) popupviewdelete.findViewById(R.id.msg);
                msg.setText("id:" + id + " | Nom:" + nom + " | prenom:" + prenom + " | trajet:" + trajet);
                try {
                    Connection cnx1 = connect.CONN();
                    if (!(cnx1 == null)) {
                        String query = " delete from mission where id = '" + id + "'";
                        Statement st1 = cnx1.createStatement();
                        int result = st1.executeUpdate(query);
                        if (!(result == 0)) {
                            startActivity(new Intent(Missions.this, Missions.class));
                            finish();
                            /*ici l'ajout de l'id de l'utilisateur pour remplir le log a fin d'introduire les droits d'access*/
                            popupdelete.dismiss();
                        }
                    }
                } catch (Exception ex) {
                    output = ex.toString();
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
        popupviewsort = inflater.inflate(R.layout.popsortms, null);
        popupsort = new AlertDialog.Builder(this).create();
        popupsort.setView(popupviewsort);
        RadioGroup group = popupviewsort.findViewById(R.id.group);
        popupsort.show();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.green:
                        Missions.this.adapter.sorted("green");
                        Missions.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.blue:
                        Missions.this.adapter.sorted("blue");
                        Missions.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.grey:
                        Missions.this.adapter.sorted("grey");
                        Missions.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;

                    default:
                        break;
                }
            }
        });

        //if(group.getCheckedRadioButtonId()==R.id.green)
        //  chauffeurs.this.adapter.sorted("green");
    }


    @NonNull
    private String comparedate(Date debut) {
        Date currentdt = null;
        String service = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat datetime = new SimpleDateFormat("dd-MM-yyyy");
        String current = datetime.format(cal.getTime());
        String db = datetime.format(debut);
        try {
            currentdt = datetime.parse(current);
            debut = datetime.parse(db);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentdt.after(debut)) {
            service = "recent";
        } else if (currentdt.before(debut)) {
            service = "soon";
        } else if (currentdt.compareTo(debut) == 0) {
            service = "today";
        }

        return service;

    }
    private String terminer(Date fin) {
        Date currentdt = null;
        String service = "t";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String current = datetime.format(cal.getTime());
        current.replace(".0","");
        String db = datetime.format(fin);
        try {
            currentdt = datetime.parse(current);
            fin = datetime.parse(db);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Boolean i = currentdt.after(fin);
        if (i) {
            service = "terminer";
        }

        return service;

    }

    protected void showoutput(String output, String color) {
        resultat.setVisibility(View.VISIBLE);
        if (color == "red")
            resultat.setTextColor(getResources().getColor(R.color.red));
        if (color == "green")
            resultat.setTextColor(getResources().getColor(R.color.green));
        resultat.setText(output);
    }

    public void getTotale() {
        TextView sum=findViewById(R.id.sum);
        sum.setText("Totale: "+String.valueOf(adapter.getCount()));
    }
}