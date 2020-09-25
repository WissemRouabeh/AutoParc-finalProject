/*
 * *all code wrote by wissem rouabeh**
 */

/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.dash_ch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import gafsa.rouabeh.wissem.autoparc.Login;
import gafsa.rouabeh.wissem.autoparc.Profile;
import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistemissions;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_mission;
import gafsa.rouabeh.wissem.autoparc.model.modelmission;
import gafsa.rouabeh.wissem.autoparc.session;

public class chauffeur_dashboard extends AppCompatActivity {
    session session;
    db_connect connect;
    List list;
    TextView resultat;
    ListView listview;
    BottomNavigationView bottombar;
    adapteurlistemissions adapter;
    String output;
    CardView m,v,c;
    LinearLayout toolbar_deconexion;
    int matid;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chauffeur_dashboard);

        session = new session(getApplicationContext());
        connect = new db_connect();


        getmatid();
        getid();
        initview();
        data_fetch();
        adapter = new adapteurlistemissions(getApplicationContext(), list);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                modelmission ms=adapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(),fiche_mission.class);
                intent.putExtra("ms",ms);
                startActivity(intent);

            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),listm.class));
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),listv.class));
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });

        toolbar_deconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void initview() {

        list = new ArrayList<modelmission>();
        resultat = findViewById(R.id.resultat);
        listview = findViewById(R.id.listview1);
        m =  findViewById(R.id.m);
        v = findViewById(R.id.v);
        c = findViewById(R.id.c);
        bottombar = findViewById(R.id.bottomnavbar);
        toolbar_deconexion = findViewById(R.id.toolbar);
    }


    private void logout() {
        session.setLoggedin(false, -1);
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        finish();
    }
    private void data_fetch() {
        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {

                output = "Erreur lors de la connexion";
                showoutput(output, "red");
///output
            } else {
                //String query = "select chauffeur.Id,chauffeur.Nom,chauffeur.Prenom,mission.Date_debut,mission.Date_fin from chauffeur,mission where chauffeur.Id = mission.Id_chauffeur";
                //on ne peut pas utiliser la requetet precedente car nous somme on besoin aussi de chauffeurs qui non pas affecter a aucune mission
                //String query = " select * from mission";
                String query1 = "select mission.ID,mission.Id_chauffeur,mission.Id_voiture,nom, prenom,matricule, voiture_mar, trajet, num_serie,Date_debut,Date_fin from mission left join chauffeur on mission.Id_chauffeur = chauffeur.Id left join voiture on mission.Id_voiture = voiture_id where Id_chauffeur='"+id+"'";
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
                }
            }
        } catch (Exception ex) {
            ///output
            output = ex.toString();
            showoutput(output, "red");

        }
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
                Log.e("tag",e.toString());
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
                Log.e("tag",e.toString());

            }

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

}
