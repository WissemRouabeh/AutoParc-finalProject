package gafsa.rouabeh.wissem.autoparc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.dash_ch.chauffeur_dashboard;
import gafsa.rouabeh.wissem.autoparc.dashboard_grid.adapter_dash;
import gafsa.rouabeh.wissem.autoparc.model.modeldash;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class dashboard_view extends AppCompatActivity {
    RecyclerView grid;
    LinearLayout toolbar_deconexion;
    session session;
    TextView welcome;
    modeluser user;
    db_connect connect;
    HashMap<String, String> hash;
    List<modeldash> data;
    adapter_dash adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_recycler);
        data = new ArrayList<>();
        initview();
        checkUser check = new checkUser(getApplicationContext());
        user = getuser();

            hash = new HashMap<>();
            hash = check.privileges(session.loggedid());
            setvisibility(hash);

            welcome.setText("Bienvenue " + user.getPrenom() + user.getNom() + " !");

        //Toast.makeText(this, String.valueOf(session.loggedid()), Toast.LENGTH_SHORT).show();
        //definition de button deconextion
        toolbar_deconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logout();
                logout("default");
                // default si la deconnexion est
                // non pas la resultat d'une erreur
                // sinon le message d'erreur qui sera afficher lors de louverture de lactivité login
            }
        });

    }


    private void logout(String msg_erreur) {
        session.setLoggedin(false, -1);
        Intent i = new Intent(dashboard_view.this, Login.class);
        i.putExtra("msg_erreur", msg_erreur);
        startActivity(i);
        finish();
    }

    private void initview() {
        toolbar_deconexion = findViewById(R.id.logout);
        welcome = findViewById(R.id.welcome);
        grid = findViewById(R.id.recyler);
        grid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        adapter = new adapter_dash(getApplicationContext(), data);
        grid.setAdapter(adapter);
        session = new session(getApplicationContext());
        connect=new db_connect();
    }

    private void setvisibility(HashMap<String, String> hashmap) {
        if (user.getType().equals("Personnel")) {
            data.add(new modeldash("Compte", "Gérer mon compte", R.drawable.a001));
            data.add(new modeldash("Journaux", "Gérer les journaux", R.drawable.logsicon));
            if (hashmap.containsKey("M")) {
                data.add(new modeldash("Missions", "Gérer les missions", R.drawable.a005));

            }
            if (hashmap.containsKey("C")) {
                data.add(new modeldash("Chauffeurs", "Gérer les chauffeurs", R.drawable.a006));

            }
            if (hashmap.containsKey("V")) {
                data.add(new modeldash("Voitures", "Gérer les voitures", R.drawable.a003));

            }
            /*if (hashmap.containsKey("S")) {
                data.add(new modeldash("Statistiques", "Consulter les statistiques", R.drawable.charticon));

            }*/

            adapter = new adapter_dash(getApplicationContext(), data);
            adapter.notifyDataSetChanged();

        } else if (user.getType().equals("Chauffeur")) {
            Intent i = new Intent(getApplicationContext(), chauffeur_dashboard.class);
            startActivity(i);
        } else {
            setAllviews();
            adapter = new adapter_dash(getApplicationContext(), data);
            adapter.notifyDataSetChanged();

        }


    }

    /*private HashMap<String, String> privileges() {

        HashMap<String, String> hash = new HashMap<>();
        Connection cnx;
        try {
            cnx = connect.CONN();
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
                st.close();
                r.close();
                cnx.close();

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return hash;
    }*/

    protected void setAllviews() {
        data.add(new modeldash("Chauffeurs", "Gérer les chauffeurs", R.drawable.a006));
        data.add(new modeldash("Missions", "Gérer les missions", R.drawable.a005));
        data.add(new modeldash("Voitures", "Gérer les voitures", R.drawable.a003));
        data.add(new modeldash("Utilisateurs", "Gérer les utilisateurs", R.drawable.a002));
        data.add(new modeldash("Journaux", "Gérer les journaux", R.drawable.logsicon));
        //data.add(new modeldash("Statistiques", "Gérer les consulter", R.drawable.charticon));
        data.add(new modeldash("Compte", "Mon compte", R.drawable.a001));
    }

    public modeluser getuser() {
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(getApplicationContext(), "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * from utilisateur where id ='" + session.loggedid() + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                   user = new modeluser(r.getInt(r.findColumn("id")),
                            r.getString(r.findColumn("nom")),
                            r.getString(r.findColumn("prenom")),
                            r.getString(r.findColumn("pwd")),
                            r.getString(r.findColumn("matricule")),
                            r.getString(r.findColumn("type")),
                            r.getString(r.findColumn("statut")),
                            String.valueOf(r.getInt(r.findColumn("cin"))),
                            r.getDate(r.findColumn("date_recrute")).toString()
                    );
                }

            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
        return user;
    }
}


