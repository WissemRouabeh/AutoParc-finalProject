/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.dash_ch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.Missions;
import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistemissions;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_mission;
import gafsa.rouabeh.wissem.autoparc.model.modelmission;
import gafsa.rouabeh.wissem.autoparc.session;
import gafsa.rouabeh.wissem.autoparc.utilisateurs;

public class listm extends AppCompatActivity {

    List<modelmission> list;
    db_connect connect;
    TextView resultat, sort;
    EditText searchbar;
    ListView listview;
    adapteurlistemissions adapter;
    String output;
    AlertDialog popupsort;
    View popupviewsort;
    session session;
    int matid;
    int id;
    BottomNavigationView btm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);
        session = new session(getApplicationContext());
        connect=new db_connect();
        matid=getIntent().getIntExtra("matid",-1);
        getmatid();

        /*de stackoverflow pour eviter le clavier lors de louverture l'activité */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getmatid();
        getid();
        initview();
        data_fetch();
        adapter = new adapteurlistemissions(getApplicationContext(), list);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                modelmission ms = adapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), fiche_mission.class);
                intent.putExtra("ms", ms);
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
                listm.this.adapter.filtre(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

sort.setVisibility(View.GONE);

    }
    private void getmatid(){
        Connection cnx = connect.CONN();
        if(cnx!=null){
            try {
                String queri="select matricule from utilisateur where id='"+session.loggedid()+"'";
                Statement st = cnx.createStatement();

                ResultSet r=st.executeQuery(queri);
                if(r.next())
                    matid=r.getInt(1);

            } catch (SQLException e) {
            }

        }
    }
    private void getid(){
        Connection cnx = connect.CONN();
        if(cnx!=null){
            try {
                String queri="select Id from chauffeur where matricule='"+matid+"'";
                Statement st = cnx.createStatement();

                ResultSet r=st.executeQuery(queri);
                if(r.next())
                    id=r.getInt(1);

            } catch (SQLException e) {
            }

        }
    }
    private void initview() {
        list = new ArrayList<>();
        resultat = findViewById(R.id.resultat);
        sort = findViewById(R.id.sort);
        searchbar = findViewById(R.id.searchbar);
        listview = findViewById(R.id.listview);
        btm=findViewById(R.id.bottomnavbar);
        btm.setVisibility(View.GONE);
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
                String query1 = "select mission.ID,mission.Id_chauffeur,mission.Id_voiture,nom, prenom,matricule voiture_mar, trajet, num_serie,Date_debut,Date_fin from mission left join chauffeur on mission.Id_chauffeur = chauffeur.Id left join voiture on mission.Id_voiture = voiture_id where Id_chauffeur='" + id + "'";
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
                        list.add(new modelmission(result.getInt(result.findColumn("ID")),
                                result.getString(result.findColumn("nom")),
                                result.getString(result.findColumn("prenom")),
                                result.getString(result.findColumn("voiture_mar")),
                                result.getString(result.findColumn("num_serie")),
                                result.getString(result.findColumn("trajet")),
                                datedebut,
                                datefin,
                                service,
                                result.getInt(result.findColumn("Id_chauffeur")),
                                result.getInt(result.findColumn("matricule")),
                                result.getInt(result.findColumn("Id_voiture"))
                        ));
                    }
                    else if (when.equals("soon")) {
                        service="blue";
                        list.add(new modelmission(result.getInt(result.findColumn("ID")),
                                result.getString(result.findColumn("nom")),
                                result.getString(result.findColumn("prenom")),
                                result.getString(result.findColumn("voiture_mar")),
                                result.getString(result.findColumn("num_serie")),
                                result.getString(result.findColumn("trajet")),
                                datedebut,
                                datefin,
                                service,
                                result.getInt(result.findColumn("Id_chauffeur")),
                                result.getInt(result.findColumn("matricule")),
                                result.getInt(result.findColumn("Id_voiture"))
                        ));

                    } else if(when.equals("recent")){
                        service="brown";
                        String terminer = terminer(result.getTimestamp(result.findColumn("Date_fin")));
                        if(terminer.equals("terminer")){
                            service="grey";

                        }
                        list.add(new modelmission(result.getInt(result.findColumn("ID")),
                                result.getString(result.findColumn("nom")),
                                result.getString(result.findColumn("prenom")),
                                result.getString(result.findColumn("voiture_mar")),
                                result.getString(result.findColumn("num_serie")),
                                result.getString(result.findColumn("trajet")),
                                datedebut,
                                datefin,
                                service,
                                result.getInt(result.findColumn("Id_chauffeur")),
                                result.getInt(result.findColumn("matricule")),
                                result.getInt(result.findColumn("Id_voiture"))
                        ));

                    }

                }
            }
        } catch (Exception ex) {
            ///output
            output = ex.toString();
            showoutput(output, "red");

        }
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
        SimpleDateFormat datetime = new SimpleDateFormat("dd-MM-yyyy");
        String current = datetime.format(cal.getTime());
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
                        listm.this.adapter.sorted("green");
                        listm.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.blue:
                        listm.this.adapter.sorted("blue");
                        listm.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.grey:
                        listm.this.adapter.sorted("grey");
                        listm.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;

                    default:
                        break;
                }
            }
        });

    }

}
