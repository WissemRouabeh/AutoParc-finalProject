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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteuruser;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_chauffeur;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_utilisateur;
import gafsa.rouabeh.wissem.autoparc.insertdata.mission;
import gafsa.rouabeh.wissem.autoparc.insertdata.utilisateur;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class utilisateurs extends AppCompatActivity {
    ListView lst;
    List list;
    session session;
    db_connect connect;
    EditText cherche;
    TextView sort;
    TextView rzlt;
    adapteuruser adapter;
    View popupviewsort;
    AlertDialog popupsort;
    BottomNavigationView bottombar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_utilisateurs);

        /*de stackoverflow pour eviter le clavier lors de louverture l'activité */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initview();
        checkpermession();
        data_fetch();
        adapter = new adapteuruser(list, getApplicationContext());
        getTotale();
        lst.setAdapter(adapter);
        cherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                utilisateurs.this.adapter.filtre(input);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupsorted();
                adapter.notifyDataSetChanged();
            }
        });
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                modeluser us = adapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), fiche_utilisateur.class);
                intent.putExtra("us", us);
                startActivity(intent);
            }
        });
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.it1):
                        startActivity(new Intent(utilisateurs.this, dashboard_view.class));
                        break;
                    case (R.id.it2):
                        startActivity(new Intent(utilisateurs.this, utilisateur.class));
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
        if(!chku.execute(id).getType().equals("Administrateur")){
                bottombar.setVisibility(View.GONE);
        }
    }
    private void data_fetch() {
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                String s = "Connection non etablit";
                rzlt.setText(s);
            } else {
                String query = "select * from utilisateur where id<>' "+session.loggedid()+"'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                while (r.next()) {
                    list.add(new modeluser(r.getInt(r.findColumn("id")),
                            r.getString(r.findColumn("nom")),
                            r.getString(r.findColumn("prenom")),
                            r.getString(r.findColumn("pwd")),
                            r.getString(r.findColumn("matricule")),
                            r.getString(r.findColumn("type")),
                            r.getString(r.findColumn("statut")),
                            r.getString(r.findColumn("cin")),
                            r.getDate(r.findColumn("date_recrute")).toString()
                    ));
                }
                cnx.close();
                st.close();
                r.close();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    protected void initview() {
        connect = new db_connect();
        session=new session(getApplicationContext());
        rzlt =  findViewById(R.id.resultat);
        lst =  findViewById(R.id.listch);
        sort = findViewById(R.id.sort);
        cherche = findViewById(R.id.searchbar);
        list = new ArrayList<modeluser>();
        bottombar =  findViewById(R.id.bottomnavbar);
    }

    protected void popupsorted() {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewsort = inflater.inflate(R.layout.popsortus, null);
        popupsort = new AlertDialog.Builder(this).create();
        popupsort.setView(popupviewsort);
        RadioGroup group = popupviewsort.findViewById(R.id.group);
        popupsort.show();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.Administrateur:
                        utilisateurs.this.adapter.sorted("type", "Administrateur");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.Personnel:
                        utilisateurs.this.adapter.sorted("type", "Personnel");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.PersonnelF:
                        utilisateurs.this.adapter.sorted("Fonctionne", "Personnel");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.PersonnelS:
                        utilisateurs.this.adapter.sorted("Suspendu", "Personnel");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.Chauffeur:
                        utilisateurs.this.adapter.sorted("type", "Chauffeur");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.ChauffeurF:
                        utilisateurs.this.adapter.sorted("Fonctionne", "Chauffeur");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.ChauffeurS:
                        utilisateurs.this.adapter.sorted("Suspendu", "Chauffeur");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.Fonctionne:
                        utilisateurs.this.adapter.sorted("statut", "Fonctionne");
                        utilisateurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.Suspendu:
                        utilisateurs.this.adapter.sorted("statut", "Suspendu");
                        utilisateurs.this.adapter.notifyDataSetChanged();
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
