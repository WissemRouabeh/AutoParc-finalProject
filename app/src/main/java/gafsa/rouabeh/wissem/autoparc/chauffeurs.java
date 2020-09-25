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

import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistechauffeur;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_chauffeur;
import gafsa.rouabeh.wissem.autoparc.insertdata.chauffeur;
import gafsa.rouabeh.wissem.autoparc.model.modelchauffeur;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class chauffeurs extends AppCompatActivity {
    ListView lst;
    List list;
    db_connect connect;
    EditText cherche;
    TextView sort;
    TextView rzlt;
    String output;
    adapteurlistechauffeur adapter;
    View popupviewdelete;
    View popupviewsort;
    AlertDialog popupdelete;
    AlertDialog popupsort;
    BottomNavigationView bottombar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chauffeurs);

        /*de stackoverflow pour eviter le clavier lors de louverture l'activité */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initview();
        checkpermession();
        data_fetch();

        adapter = new adapteurlistechauffeur(list, getApplicationContext());
        getTotale();

        lst.setAdapter(adapter);

        cherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String input = charSequence.toString();
                chauffeurs.this.adapter.filtre(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                modelchauffeur ch = adapter.getItem(position);
                Intent i = new Intent(getApplicationContext(), fiche_chauffeur.class);
                i.putExtra("ch", ch);
                startActivity(i);
            }
        });

        /*lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                modelchauffeur ch = adapter.getItem(position);
                popupdelete(valueOf(ch.getId()).toString(), ch.getNom(), ch.getPrenom());
                popupdelete.show();
                return true;
            }
        });*/

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
                        startActivity(new Intent(chauffeurs.this, dashboard_view.class));
                        break;
                    case (R.id.it2):
                        startActivity(new Intent(chauffeurs.this, chauffeur.class));
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
            HashMap<String,String> h = chku.privileges(id);
            if(h.containsKey("C")){
                if(h.get("C").equals("READ"))
                    bottombar.setVisibility(View.GONE);
            }
        }
    }
    private void data_fetch() {

        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "no connection";
                rzlt.setText(output);

            } else {

                //String query = "select chauffeur.Id,chauffeur.Nom,chauffeur.Prenom,mission.Date_debut,mission.Date_fin from chauffeur,mission where chauffeur.Id = mission.Id_chauffeur";
                //on ne peut pas utiliser la requetet precedente car nous somme on besoin aussi de chauffeurs qui non pas affecter a aucune mission
                String query = " select * from chauffeur";
                Statement st1 = cnx.createStatement();
                ResultSet result = st1.executeQuery(query);
                while (result.next()) {
                    String query2 = "select * from mission where Id_chauffeur = '" + result.getInt(result.findColumn("Id")) + "'";
                    Statement st2 = cnx.createStatement();
                    ResultSet resultmission = st2.executeQuery(query2);

                    String color = "green";
                    boolean bool = true; //disponible

                  /*  if (resultmission.next()) {
                        if (comparedate(resultmission.getTimestamp(resultmission.findColumn("Date_debut")),
                                resultmission.getTimestamp(resultmission.findColumn("Date_fin")))) {
                            //SimpleDateFormat datetime1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            //Date date=resultmission.getTimestamp(resultmission.findColumn("Date_debut"));
                            //String ss=datetime1.format(date);
                            //rzlt.setVisibility(View.VISIBLE);
                            //rzlt.setText(ss);
                            color = "blue";
                        } else
                            color = "green";
                    } else {
                        color = "grey";
                    }*/

                    if (!resultmission.isBeforeFirst())
                        color = "grey";
                    else
                        while (resultmission.next() && bool) {
                            if (comparedate(resultmission.getTimestamp(resultmission.findColumn("Date_debut")),
                                    resultmission.getTimestamp(resultmission.findColumn("Date_fin")))) {
                                color = "blue";
                                bool = false;
                            }
                        }

                    list.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                            result.getInt((result.findColumn("matricule"))),
                            result.getString((result.findColumn("Nom"))),
                            result.getString((result.findColumn("Prenom"))),
                            color,
                            String.valueOf(result.getInt(result.findColumn("cin"))),
                            result.getDate(result.findColumn("date_recrute")).toString()));

                }

                cnx.close();
                st1.close();
                result.close();
            }
        } catch (Exception ex) {
            output = ex.toString();
            rzlt.setVisibility(View.VISIBLE);
            rzlt.setText(output);
        }

    }
/*
    private void data_fetch() {

        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "no connection";
                rzlt.setText(output);

            } else {

                //String query = "select chauffeur.Id,chauffeur.Nom,chauffeur.Prenom,mission.Date_debut,mission.Date_fin from chauffeur,mission where chauffeur.Id = mission.Id_chauffeur";
                //on ne peut pas utiliser la requetet precedente car nous somme on besoin aussi de chauffeurs qui non pas affecter a aucune mission
                String query = " select * from chauffeur";
                Statement st1 = cnx.createStatement();
                ResultSet result = st1.executeQuery(query);
                String color="";
                while (result.next()) {
                    if(result.getInt(result.findColumn("service"))==1)
                        color="blue";

                    if(result.getInt(result.findColumn("service"))==0)
                        color="green";

                    if(result.getInt(result.findColumn("service"))==-1)
                        color="grey";
                    list.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                            result.getString(result.findColumn("Nom")),
                            result.getString(result.findColumn("Prenom")),color));

                }

            }
        } catch (Exception ex) {
            output = ex.toString();
            rzlt.setVisibility(View.VISIBLE);
            rzlt.setText(output);
        }

    }
*/

    @Override
    public void onBackPressed() {
        startActivity(new Intent(chauffeurs.this, dashboard.class));
    }

    @NonNull
    private Boolean comparedate(Date debut, Date fin) {
        Date currentdt = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String current = datetime.format(cal.getTime());
        String s = debut.toString();
        try {
            currentdt = datetime.parse(current);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentdt.before(fin) && currentdt.after(debut)) {
            return true;
        } else {
            return false;
        }
    }

    //ce principe de faire un dialog apater a un layout precis pour la confirmation est depuis stackoverflow..
    protected void popupdelete(final String id, final String nom, final String prenom) {
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
                msg.setText("id:" + id + " | Nom:" + nom + " | prenom:" + prenom);
                try {
                    Connection cnx = connect.CONN();
                    if (!(cnx == null)) {
                        String query = " delete from chauffeur where id = '" + id + "'";
                        Statement st1 = cnx.createStatement();
                        int result = st1.executeUpdate(query);
                        if (!(result == 0)) {
                            startActivity(new Intent(chauffeurs.this, chauffeurs.class));
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
        popupviewsort = inflater.inflate(R.layout.popupsort, null);
        popupsort = new AlertDialog.Builder(this).create();
        popupsort.setView(popupviewsort);
        RadioGroup group = popupviewsort.findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.green:
                        chauffeurs.this.adapter.sorted("green");
                        chauffeurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.blue:
                        chauffeurs.this.adapter.sorted("blue");
                        chauffeurs.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.grey:
                        chauffeurs.this.adapter.sorted("grey");
                        chauffeurs.this.adapter.notifyDataSetChanged();
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

    protected void initview() {
        rzlt = (TextView) findViewById(R.id.resultat);
        lst = (ListView) findViewById(R.id.listview);
        lst.setHeaderDividersEnabled(false);
        sort = (TextView) findViewById(R.id.sort);
        cherche = (EditText) findViewById(R.id.searchbar);
        list = new ArrayList<modelchauffeur>();
        bottombar = (BottomNavigationView) findViewById(R.id.bottomnavbar);
        connect = new db_connect();

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                //donothing
            } else {
                String query = "select count(*) nbrtotale from chauffeur";
                Statement st = cnx.createStatement();
                ResultSet resultat = st.executeQuery(query);
                if (resultat.next())
                    Toast.makeText(chauffeurs.this, "Totale: "+String.valueOf(resultat.getInt(resultat.findColumn("nbrtotale"))), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            //on na pas obligé dafficher le message derreur puisque sil ya un erreur sa sera afficher lors de de laffichage de la liste
            //puisque cette method sera declancher apres le method oncreate;
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getTotale() {
        TextView sum=findViewById(R.id.sum);
        sum.setText("Totale: "+String.valueOf(adapter.getCount()));
    }
}
