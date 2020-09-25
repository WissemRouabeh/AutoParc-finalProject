/*
 * *wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.insertdata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.Missions;
import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistechauffeur;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistevoiture;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.model.modelchauffeur;
import gafsa.rouabeh.wissem.autoparc.model.modelvoiture;

public class mission extends AppCompatActivity {
    /*protected static*/ EditText mtitre;
    TextView mvoiture, mchauffeur, mdate, resultat;
    Spinner spinnerde, spinnervers;
    Button btnajout;
    TextView rzlt;
    Date insertdate;
    adapteurlistechauffeur adapterch;
    adapteurlistevoiture adaptervh;
    TimePickerDialog timed;
    DatePickerDialog dated;
    db_connect connect;
    String output;
    List listch, listvh;
    ListView listviewch, listViewvh;
    AlertDialog chauffeurdialog, voituredialog;
    String titre;
    String datein;
    String dateinvh;
    String trajet;
    int idch;
    int idvh;
    Date debut;
    Date fin;
    List<String> list;
    ArrayAdapter<String> adapter;
    Button ajouter_tr;
    private int annee, mois, jour, min, heure;
    private StringBuilder Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initview();
        list = new ArrayList<>();
        fetchtrj();

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
        spinnerde.setAdapter(adapter);
        spinnervers.setAdapter(adapter);
        ajouter_tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), trajet.class), 1);
            }
        });
        mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mdate.setText(null);
                showdatetime().show();

            }
        });
        mchauffeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCH();
                adapterch = new adapteurlistechauffeur(listch, getApplicationContext());
                dialogchauffeur();


            }
        });
        mvoiture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchVH();
                adaptervh = new adapteurlistevoiture(listvh, getApplicationContext());
                dialogvoiture();

            }

        });

        btnajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String de = spinnerde.getSelectedItem().toString();
                String vers = spinnervers.getSelectedItem().toString();
                trajet = "de " + de + " à " + vers;
                Log.e("", datein);
                Toast.makeText(getApplicationContext(), datein, Toast.LENGTH_LONG).show();
                insert(idch, idvh, datein, trajet);
            }
        });

    }

    private void fetchtrj() {

        connect = new db_connect();

        try {
            Connection cnx = connect.CONN();
            if (cnx != null) {
                String query = "select * from trajet";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                while (r.next()) {
                    list.add(r.getString(2));
                }
            }
        } catch (Exception e) {
            resultat.setText(e.toString());
        }
    }

    private void dialogvoiture() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View voitureview = inflater.inflate(R.layout.dialogvoiture, null);
        voituredialog = new AlertDialog.Builder(this).create();
        voituredialog.setView(voitureview);
        EditText searchbar = (EditText) voitureview.findViewById(R.id.searchbar);
        rzlt = (TextView) voitureview.findViewById(R.id.resultat);
        listViewvh = (ListView) voitureview.findViewById(R.id.listview);
        listViewvh.setAdapter(adaptervh);
        listViewvh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                modelvoiture vh = mission.this.adaptervh.getItem(position);
                idvh = vh.getId();
                mission.this.mvoiture.setText(vh.getModele() + "|" + vh.getmatricule() + "|" + vh.getPuissance());
                voituredialog.dismiss();
            }
        });
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String input = charSequence.toString();
                mission.this.adaptervh.filtre(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        voituredialog.show();
    }

    private void dialogchauffeur() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View chauffeurview = inflater.inflate(R.layout.dialogchauffeur, null);
        chauffeurdialog = new AlertDialog.Builder(this).create();
        chauffeurdialog.setView(chauffeurview);
        EditText searchbar = (EditText) chauffeurview.findViewById(R.id.searchbar);
        rzlt = (TextView) chauffeurview.findViewById(R.id.resultat);
        listviewch = (ListView) chauffeurview.findViewById(R.id.listview);
        //mission.this.adaptervh.sorteddispo();
        listviewch.setAdapter(adapterch);
        listviewch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                modelchauffeur ch = mission.this.adapterch.getItem(position);
                idch = ch.getId();
                mission.this.mchauffeur.setText(ch.getNom() + " " + ch.getPrenom());
                chauffeurdialog.dismiss();
            }
        });
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String input = charSequence.toString();
                mission.this.adapterch.filtre(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        chauffeurdialog.show();

    }

    /* private void fetchCH() {
         connect = new db_connect();
         try {
             Connection cnx = connect.CONN();
             if (cnx == null) {
                 output = "no connection";
                 showoutput(output, "red");

             } else {

                 //String query = "select chauffeur.Id,chauffeur.Nom,chauffeur.Prenom,mission.Date_debut,mission.Date_fin from chauffeur,mission where chauffeur.Id = mission.Id_chauffeur";
                 //on ne peut pas utiliser la requetet precedente car nous somme on besoin aussi de chauffeurs qui non pas affecter a aucune mission
                 String query = " select * from chauffeur";
                 Statement st1 = cnx.createStatement();
                 ResultSet result = st1.executeQuery(query);
                 String color = null;
                 mission.this.listch = new ArrayList<modelchauffeur>();
                 while (result.next()) {
                     String query2 = "select * from mission where Id_chauffeur = '" + result.getInt(result.findColumn("Id")) + "'";
                     Statement st2 = cnx.createStatement();
                     ResultSet resultmission = st2.executeQuery(query2);
                     if (resultmission.next()) {
                         SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                         debut = resultmission.getTimestamp(resultmission.findColumn("Date_debut"));
                         fin = resultmission.getTimestamp(resultmission.findColumn("Date_fin"));
                         debut = datetime.parse(debut.toString());
                         fin = datetime.parse(fin.toString());
                         if (datein == null) {
                             Calendar cal = Calendar.getInstance();
                             int ss = cal.get(Calendar.SECOND);
                             int mm = cal.get(Calendar.MINUTE);
                             int hh = cal.get(Calendar.HOUR_OF_DAY);
                             int yyyy = cal.get(Calendar.YEAR);
                             int MM = cal.get(Calendar.MONTH);
                             int dd = cal.get(Calendar.DAY_OF_MONTH);
                             datein = yyyy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss;
                         }
                         Date dt = datetime.parse(datein);
                         String deb = datetime.format(debut);
                         String fn = datetime.format(fin);
                         String dtt = datetime.format(dt);
                         boolean compare=false;
                         boolean comparenull;
                         if (!(fn.contains("2050-01-01 00:00:00.0"))){
                             comparenull = true;
                             compare = datetime.parse(dtt).before(datetime.parse(fn))
                                     && datetime.parse(dtt).after(datetime.parse(deb));
                         }
                         else
                             comparenull = false;

                         if ((comparenull)&& ((!compare)))
  {
                                 listch.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                                         result.getString((result.findColumn("Nom"))),
                                         result.getString((result.findColumn("Prenom"))),
                                         "green"));


                         }
                     }else {
                         listch.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                                 result.getString((result.findColumn("Nom"))),
                                 result.getString((result.findColumn("Prenom"))),
                                 "grey"));
                     }


                 }
             }
         } catch (Exception ex) {
             output = ex.toString();
             showoutput(output, "red");

         }
     }*/
    private void fetchCH() {
        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "Echec lors de la connection";
                showoutput(output, "red");

            } else {

                String query = " select * from chauffeur";
                Statement st0 = cnx.createStatement();
                ResultSet result = st0.executeQuery(query);
                mission.this.listch = new ArrayList<modelchauffeur>();
                while (result.next()) {
                    Statement st1 = cnx.createStatement();
                    String query2 = "select * from mission where Id_chauffeur = '" + result.getInt(result.findColumn("Id")) + "'";
                    ResultSet resultmission = st1.executeQuery(query2);
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
                    if (color.equals("grey") || color.equals("green"))
                        listch.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                                result.getInt((result.findColumn("matricule"))),
                                result.getString((result.findColumn("Nom"))),
                                result.getString((result.findColumn("Prenom"))),
                                color,
                                String.valueOf(result.getInt(result.findColumn("cin"))),
                                result.getDate(result.findColumn("date_recrute")).toString()));




                        /*
                        boolean compare = false;
                        boolean comparenull;*/
                        /*if (!(fn.contains("2050-01-01 00:00:00.0"))) {
                            comparenull = true;
                            compare = datetime.parse(dtt).before(datetime.parse(fn))
                                    && datetime.parse(dtt).after(datetime.parse(deb));
                        } else
                            comparenull = false;*/

                        /*if ((comparenull) && ((!compare))) {
                            listch.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                                    result.getString((result.findColumn("Nom"))),
                                    result.getString((result.findColumn("Prenom"))),
                                    "green"));


                        }*/
                     /*else {
                        listch.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                                result.getString((result.findColumn("Nom"))),
                                result.getString((result.findColumn("Prenom"))),
                                "grey"));
                    }*/
resultmission.close();
st1.close();
                }
                st0.close();
                result.close();
            }
            cnx.close();
        } catch (Exception ex) {
            output = ex.toString();
            showoutput(output, "red");
        }
    }

    private void fetchVH() {
        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "Echec lors de la  connection";
                showoutput(output, "red");


            } else {

                //String query = "select chauffeur.Id,chauffeur.Nom,chauffeur.Prenom,mission.Date_debut,mission.Date_fin from chauffeur,mission where chauffeur.Id = mission.Id_chauffeur";
                //on ne peut pas utiliser la requetet precedente car nous somme on besoin aussi de chauffeurs qui non pas affecter a aucune mission
                String query = " select * from vehicule where situation='" + "En circulation" + "'";
                Statement st3 = cnx.createStatement();
                ResultSet resultat = st3.executeQuery(query);
                mission.this.listvh = new ArrayList<modelvoiture>();
                while (resultat.next()) {
                    Statement st4 = cnx.createStatement();
                    String query2 = "select * from mission where Id_voiture = '" + resultat.getInt(resultat.findColumn("id")) + "'";
                    ResultSet resultatmissions = st4.executeQuery(query2);

                    boolean work = false;
                    while (resultatmissions.next() && work == false) {
                        work = comparedate(resultatmissions.getTimestamp(resultatmissions.findColumn("Date_debut")),
                                resultatmissions.getTimestamp(resultatmissions.findColumn("Date_fin")));

                        //doub mayalga wahda yekhdem feha fel wa9t lhali yegleb true beli houwa
                        // yekhdem w automatiuement yokhrej mel boucle//
                    }


//int id, String date, String marque, String matricule, String carburant, String puissance, String sit_act,String kilometrage,String consommation
                    if (!work) {
                        listvh.add(new modelvoiture(
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
                    st4.close();
                    resultatmissions.close();
                }
                st3.close();
                resultat.close();
            }
            cnx.close();


        } catch (Exception ex) {
            output = ex.toString();
            showoutput(output, "red");
        }
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


    private void insert(int idch, int idvh, String date_debut, String trajet) {

        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "no connection";
                showoutput(output, "red");
            } else {
                //hna nbadlou el requete
                String query = " insert into mission (Id_chauffeur,Id_voiture,Date_debut,Trajet) Values('" + String.valueOf(idch).toString() + "','" + String.valueOf(idvh).toString() + "','" + date_debut + "','" + trajet + "')";
                Statement st1 = cnx.createStatement();
                int result = st1.executeUpdate(query);

                if (!(result == 0)) {
                    output = "Nouvelle mission a été deposée avec succés.";
                    showoutput(output, "green");
                    startActivity(new Intent(getApplicationContext(), Missions.class));
                } else {
                    output = "Erreur lors de l'insertion !";
                    showoutput(output, "red");
                }


            }
        } catch (Exception ex) {
            output = ex.toString();
            resultat.setVisibility(View.VISIBLE);
            resultat.setText(output);
        }

    }

    private List<String> fetchtr() {
        List<String> listtrajet = new ArrayList<>();
        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = " Echec lors de la connection";
                showoutput(output, "red");
            } else {
                //requete
                String query = "select * from trajet";
                Statement st1 = cnx.createStatement();
                ResultSet result = st1.executeQuery(query);

                while (result.next()) {
                    listtrajet.add(result.getString(result.findColumn("trajet")));
                }

            }
        } catch (Exception ex) {
            output = ex.toString();
            resultat.setVisibility(View.VISIBLE);
            resultat.setText(output);
        }
        return listtrajet;
    }

    protected void initview() {
        Date = new StringBuilder();
        mvoiture = findViewById(R.id.mvoiture);
        mchauffeur = findViewById(R.id.mchauffeur);
        resultat = findViewById(R.id.resultat);
        mdate = findViewById(R.id.mdate);
        spinnerde = findViewById(R.id.spinnerde);
        spinnervers = findViewById(R.id.spinnervers);
        btnajout = findViewById(R.id.btnajout);
        ajouter_tr = findViewById(R.id.ajouter_tr);


    }

    private Date convert2Date(String input) {
        Date date = null;
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0");
        try {
            date = datetime.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    private DatePickerDialog showdatetime() {

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int aa, int mm,
                                  int jj) {
                annee = aa;
                mois = mm;
                jour = jj;
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

                time();
                timed.show();
            }
        };
        Calendar c = Calendar.getInstance();

        int aa = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        dated = new DatePickerDialog(this, dateListener, aa, mm, dd);

        return dated;


    }

    private void time() {
        TimePickerDialog.OnTimeSetListener timeListener =
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hh, int mm) {
                        heure = hh;
                        min = mm;
                        Date.append(" ");
                        if (heure < 10)
                            Date.append("0").append(heure);
                        else
                            Date.append(heure);

                        Date.append(":");
                        if (min < 10)

                            Date.append("0").append(min);
                        else
                            Date.append(min);
                        Date.append(":");
                        Date.append("00");

                        mdate.setText(Date.toString());
                        datein = Date.toString();
                        dateinvh = Date.toString();
                        insertdate = convert2Date(mdate.getText().toString());
                    }

                };
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR);
        int mm = c.get(Calendar.MINUTE);
        timed = new TimePickerDialog(this, timeListener, hh, mm, true);
    }

    protected void showoutput(String output, String color) {
        resultat.setVisibility(View.VISIBLE);
        if (color == "red")
            resultat.setTextColor(getResources().getColor(R.color.red));
        if (color == "green")
            resultat.setTextColor(getResources().getColor(R.color.green));
        resultat.setText(output);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String trajet = data.getStringExtra("trajet");
                list.add(1, trajet);
                adapter.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED) {
                //mata3melchay
                Log.e("wissem", "temchi bch tjib data testana trajet");
            }
        }
    }

}

