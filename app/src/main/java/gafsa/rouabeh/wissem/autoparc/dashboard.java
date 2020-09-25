package gafsa.rouabeh.wissem.autoparc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class dashboard extends AppCompatActivity {

    CardView cMissions, cChauffeurs, cVoitures, cUsers, cLogs, cStatistic;
    RelativeLayout toolbar_deconexion;
    LinearLayout l1, l2, l3;
    session session;
    CardView cProfile;
    TextView welcome;
    modeluser user;
    db_connect connect;
    HashMap<String, String> hash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        session = new session(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "id num:" + String.valueOf(session.loggedid()), Toast.LENGTH_LONG).show();
        checkUser check = new checkUser(getApplicationContext());
        user = check.execute(session.loggedid());
        if ((user == null)) {
            logout("Utilisateur non disponible");
        }
        check.user_deleted(session.loggedid());
        if (!(user == null)) {
            initview();
            hash = new HashMap<>();
            hash = privileges();
            setvisibility(hash);
            welcome.setText("Bienvenue @" + user.getPrenom() + user.getNom() + "!");
        }

        //definition de button deconextion
        toolbar_deconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout("default");
                // default si la deconnexion est
                // non pas la resultat d'une erreur
                // sinon le message d'erreur qui sera afficher lors de louverture de lactivité login
            }
        });

        cMissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, Missions.class));
            }
        });
        cChauffeurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, chauffeurs.class));
                //int i = session.loggedid();

            }
        });
        cVoitures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, Voitures.class));
            }
        });
        cUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, Voitures.class));
            }
        });
        cLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, Profile.class);
                startActivity(i);
            }
        });
    }



    private void logout(String msg_erreur) {
        session.setLoggedin(false, -1);
        Intent i = new Intent(dashboard.this, Login.class);
        i.putExtra("msg_erreur", msg_erreur);
        startActivity(i);
        finish();
    }

    private void initview() {
        cMissions = findViewById(R.id.missions);
        cChauffeurs = findViewById(R.id.chauffeurs);
        cVoitures = findViewById(R.id.voitures);
        cUsers = findViewById(R.id.utilisateurs);
        cLogs = findViewById(R.id.logs);
        cStatistic = findViewById(R.id.statistic);
        cProfile = findViewById(R.id.Profile);
        toolbar_deconexion = findViewById(R.id.logout);
        welcome = findViewById(R.id.welcome);
    }

    private void setvisibility(HashMap<String, String> hashmap) {
        if (user.getType().equals("Personnel")) {
            cMissions.setVisibility(View.GONE);
            cVoitures.setVisibility(View.GONE);
            cChauffeurs.setVisibility(View.GONE);
            cStatistic.setVisibility(View.GONE);
            cLogs.setVisibility(View.GONE);
            cUsers.setVisibility(View.GONE);
            cProfile.setVisibility(View.VISIBLE);
            if (hashmap.containsKey("C"))
                cChauffeurs.setVisibility(View.VISIBLE);

            if (hashmap.containsKey("V"))
                cVoitures.setVisibility(View.VISIBLE);

            if (hashmap.containsKey("M"))
                cMissions.setVisibility(View.VISIBLE);

            if (hashmap.containsKey("S"))
                cStatistic.setVisibility(View.VISIBLE);

        }
    }

    private HashMap<String, String> privileges() {

        HashMap<String, String> hash = new HashMap<>();
        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null)
                Toast.makeText(this, "Connection non etablit ! ", Toast.LENGTH_SHORT).show();
            else {
                String query = "select * from access where id_user= '" + user.getId() + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                while (r.next()) {
                    hash.put(r.getString(r.findColumn("ou")),
                            r.getString(r.findColumn("crud")));
                }

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return hash;
    }


}


