package gafsa.rouabeh.wissem.autoparc.dash_ch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

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

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistevoiture;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_voiture;
import gafsa.rouabeh.wissem.autoparc.model.modelvoiture;
import gafsa.rouabeh.wissem.autoparc.session;

public class listv extends AppCompatActivity {
    ListView listview;
    EditText cherche;
    TextView sort;
    TextView resultat;
    adapteurlistevoiture adapter;
    List list;
    db_connect connect;
    String rzlt;
    View popupviewsort;
    AlertDialog popupsort;
    session session;
    int matid,id;
    BottomNavigationView btm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voitures);
        session = new session(getApplicationContext());
        connect=new db_connect();

        getmatid();
        getid();
        initview();
        fetchdata();
        adapter = new adapteurlistevoiture(list, this);
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
        cherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String input = charSequence.toString();
                listv.this.adapter.filtre(input);
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
        sort.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, chauffeur_dashboard.class));
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

    protected void initview() {

        list = new ArrayList<>();
        resultat = findViewById(R.id.resultat);
        listview = findViewById(R.id.listvh);
        sort = findViewById(R.id.sort);
        cherche = findViewById(R.id.searchbar);
        list = new ArrayList<modelvoiture>();

        btm=findViewById(R.id.bottomnavbar);
        btm.setVisibility(View.GONE);

    }

    protected void fetchdata() {
        connect = new db_connect();
        try {
            Connection connection = connect.CONN();
            if (connection == null) {
                rzlt = "no connection";
            } else {
                String query = "select * from voiture ";
                Statement st = connection.createStatement();
                ResultSet resultat = st.executeQuery(query);
                while (resultat.next()) {
                    String query2 = "select * from mission where Id_voiture = '" + resultat.getInt(resultat.findColumn("voiture_id")) + "' and Id_chauffeur ='" + session.loggedid() + "'";
                    Statement st2 = connection.createStatement();
                    ResultSet resultatmissions = st2.executeQuery(query2);
                    boolean work = false;
                    while (resultatmissions.next() && work == false) {
                        work = comparedate(resultatmissions.getTimestamp(resultatmissions.findColumn("Date_debut")),
                                resultatmissions.getTimestamp(resultatmissions.findColumn("Date_fin")));

                        //doub mayalga wahda yekhdem feha fel wa9t lhali yegleb true beli houwa
                        // yekhdem w automatiuement yokhrej mel boucle//
                    }
                    list.add(new modelvoiture(resultat.getInt(resultat.findColumn("voiture_id")),
                            resultat.getString(resultat.findColumn("num_serie")),
                            resultat.getString(resultat.findColumn("voiture_mar")),
                            resultat.getString(resultat.findColumn("num_chasses")),
                            work));
                }

                connection.close();
                st.close();
                resultat.close();
            }
        } catch (Exception ex) {
            rzlt = ex.toString();
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
                        listv.this.adapter.sorted(true);//on service tekhdem
                        listv.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    case R.id.disponible:
                        listv.this.adapter.sorted(false);//matekhdemch
                        listv.this.adapter.notifyDataSetChanged();
                        popupsort.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
