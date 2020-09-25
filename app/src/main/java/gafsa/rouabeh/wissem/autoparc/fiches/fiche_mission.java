/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.fiches;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.Missions;
import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistechauffeur;
import gafsa.rouabeh.wissem.autoparc.adapteurs.adapteurlistevoiture;
import gafsa.rouabeh.wissem.autoparc.checkUser;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.model.modelchauffeur;
import gafsa.rouabeh.wissem.autoparc.model.modelmission;
import gafsa.rouabeh.wissem.autoparc.model.modelvoiture;
import gafsa.rouabeh.wissem.autoparc.session;

public class fiche_mission extends AppCompatActivity {
    TextView chauffeur, voiture, trajet, datedebut, datefin, ch, vh, db, df, mfini;
    LinearLayout tr;
    ImageView edit;
    Button delete, submit, annuler;
    LinearLayout modifier;
    db_connect connect;
    modelmission ms;
    modelmission up;
    StringBuilder Date;
    TimePickerDialog timed;
    DatePickerDialog dated;
    AlertDialog voituredialog, chauffeurdialog;
    ListView listviewch;
    ListView listViewvh;
    List listch, listvh;
    LinearLayout df_view;
    TextView mission_id;
    TextView edittxt;
    String datein;
    adapteurlistechauffeur adapterch;
    adapteurlistevoiture adaptervh;
    int i;
    View popupviewdelete;
    AlertDialog popupdelete;
    String c_input;
    String v_input;
    session session;
    String matricule;
    String immat;
    Spinner spinnerde, spinnervers;
    List list;
    ArrayAdapter<String> adapter;
    private int annee, mois, jour, min, heure;
    TextView resultat;
    String output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_mission);
        session = new session(getApplicationContext());
        ms = getIntent().getParcelableExtra("ms");

        initview();

        list = new ArrayList<>();
        fetchtrj();

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
        spinnerde.setAdapter(adapter);
        spinnervers.setAdapter(adapter);

        mfini.setVisibility(View.GONE);

        if (ms.getService().equals("brown"))
            mfini.setVisibility(View.VISIBLE);

        if (ms.getService().equals("grey")) {
            df_view.setVisibility(View.VISIBLE);
            mfini.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            modifier.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            edittxt.setVisibility(View.GONE);
        }
        setviews(ms);
        checkpermession();
        up = new modelmission();
        up = ms;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modifier.getVisibility() == View.GONE) {
                    modifier.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    ch.setVisibility(View.VISIBLE);
                    vh.setVisibility(View.VISIBLE);
                    tr.setVisibility(View.VISIBLE);
                    db.setVisibility(View.VISIBLE);
                    df.setVisibility(View.VISIBLE);
                    chauffeur.setVisibility(View.GONE);
                    voiture.setVisibility(View.GONE);
                    trajet.setVisibility(View.GONE);
                    datedebut.setVisibility(View.GONE);
                    datefin.setVisibility(View.GONE);
                    setviews_up(ms);
                } else {
                    modifier.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                    c_input = chauffeur.getText().toString();
                    v_input = voiture.getText().toString();
                    ch.setVisibility(View.GONE);
                    vh.setVisibility(View.GONE);
                    tr.setVisibility(View.GONE);
                    db.setVisibility(View.GONE);
                    df.setVisibility(View.GONE);
                    chauffeur.setVisibility(View.VISIBLE);
                    voiture.setVisibility(View.VISIBLE);
                    trajet.setVisibility(View.VISIBLE);
                    datedebut.setVisibility(View.VISIBLE);
                    datefin.setVisibility(View.VISIBLE);
                    if (ms.getService().equals("grey")) {
                        df_view.setVisibility(View.VISIBLE);
                        mfini.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                        modifier.setVisibility(View.GONE);
                    }
                }


            }
        });
        mfini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fini_mission();
                startActivity(new Intent(getApplicationContext(), Missions.class));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdeletem();
            }
        });
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatetime().show();
            }
        });
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCH();
                adapterch = new adapteurlistechauffeur(listch, getApplicationContext());
                dialogchauffeur();
            }
        });
        vh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchVH();
                adaptervh = new adapteurlistevoiture(listvh, getApplicationContext());
                dialogvoiture();

            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifier.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                c_input = chauffeur.getText().toString();
                v_input = voiture.getText().toString();
                ch.setVisibility(View.GONE);
                vh.setVisibility(View.GONE);
                tr.setVisibility(View.GONE);
                db.setVisibility(View.GONE);
                df.setVisibility(View.GONE);
                chauffeur.setVisibility(View.VISIBLE);
                voiture.setVisibility(View.VISIBLE);
                trajet.setVisibility(View.VISIBLE);
                datedebut.setVisibility(View.VISIBLE);
                datefin.setVisibility(View.VISIBLE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update();
                popupedit();
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
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void fini_mission() {
        up = ms;
        Calendar cal = Calendar.getInstance();
        int ss = cal.get(Calendar.SECOND);
        int mm = cal.get(Calendar.MINUTE);
        int hh = cal.get(Calendar.HOUR_OF_DAY);
        int yyyy = cal.get(Calendar.YEAR);
        int MM = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        MM++;

        String fin = yyyy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss;
        Toast.makeText(this, fin, Toast.LENGTH_LONG).show();
        up.setDate_fin(fin);
        up.setService("grey");
        update();

    }

    private void setviews(modelmission ms) {

        chauffeur.setText(String.valueOf(ms.getId_ch()) + "-" + ms.getNom_ch() + " " + ms.getPrenom_ch());
        trajet.setText(ms.getTrajet());
        datedebut.setText(ms.getDate_debut().replace(".0", ""));
        datefin.setText(ms.getDate_fin().replace(".0", ""));
        voiture.setText(ms.getV_marq() + " " + ms.getV_serie());
    }

    private void setviews_up(modelmission ms) {

        ch.setText(String.valueOf(ms.getId_ch()) + "-" + ms.getNom_ch() + " " + ms.getPrenom_ch());

        String de = spinnerde.getSelectedItem().toString();
        String vers = spinnervers.getSelectedItem().toString();

        db.setText(ms.getDate_debut().replace(".0", ""));
        df.setText(ms.getDate_fin().replace(".0", ""));

        vh.setText(ms.getV_marq() + " " + ms.getV_serie());

    }

    protected void supp() {
        Connection cnx = connect.CONN();
        try {
            if (cnx != null) {
                String query = "delete from mission where ID='" + ms.getId() + "'";
                int r = cnx.createStatement().executeUpdate(query);
                if (r > 0) {
                    insertlogs(session.loggedid(), "Mission",
                            ms.getId() + "|" + datedebut.getText().toString() + "|" + datefin.getText().toString() + "|" + trajet.getText().toString() + "|" + chauffeur.getText().toString() + "|" + voiture.getText().toString(),
                            "vide",
                            "DELETE");
                    //Toast.makeText(this, "Mission a été supprimée avec succés", Toast.LENGTH_SHORT).show();

                    output = "Une mission a été supprimée avec succés.";
                    showoutput(output, "green");

                } else{
                    output = "Erreur lors de la suppression.";
                    showoutput(output, "red");
                }
                    //Toast.makeText(this, "Echec lors de la suppression !", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkpermession() {
        session session = new session(getApplicationContext());
        checkUser chku = new checkUser(getApplicationContext());
        int id = session.loggedid();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap = chku.privileges(id);
        if (!chku.execute(id).getType().equals("Administrateur")) {
            if (hashMap.containsKey("M")) {
                if (hashMap.get("M").contains("READ")) {
                    edit.setVisibility(View.GONE);
                    edittxt.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initview() {
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        edittxt = findViewById(R.id.edittxt);
        chauffeur = findViewById(R.id.viewchauffeur);
        voiture = findViewById(R.id.viewVoiture);
        trajet = findViewById(R.id.viewtrajet);
        datedebut = findViewById(R.id.viewdate);
        datefin = findViewById(R.id.viewdatef);
        ch = findViewById(R.id.changecha);
        tr = findViewById(R.id.changtrajet);
        vh = findViewById(R.id.changevoiture);
        db = findViewById(R.id.changedate);
        df = findViewById(R.id.changedatef);
        spinnerde = findViewById(R.id.spinnerde);
        spinnervers = findViewById(R.id.spinnervers);

        submit = findViewById(R.id.submit_update);
        annuler = findViewById(R.id.annuler);
        modifier = findViewById(R.id.modifier);
        df_view = findViewById(R.id.datefin);
        mfini = findViewById(R.id.mfini);
        resultat = findViewById(R.id.resultat);
        mission_id = findViewById(R.id.mission_id);
        mission_id.setText(String.valueOf(ms.getId()));
        connect = new db_connect();
    }

    private void dialogvoiture() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View voitureview = inflater.inflate(R.layout.dialogvoiture, null);
        voituredialog = new AlertDialog.Builder(this).create();
        voituredialog.setView(voitureview);
        EditText searchbar = voitureview.findViewById(R.id.searchbar);
        listViewvh = voitureview.findViewById(R.id.listview);
        listViewvh.setAdapter(adaptervh);
        listViewvh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                modelvoiture vh1 = fiche_mission.this.adaptervh.getItem(position);
                v_input = vh1.getModele() + " " + vh1.getmatricule();
                immat = vh1.getmatricule();
                vh.setText(v_input);
                up.setId_vh(vh1.getId());
                up.setV_marq(vh1.getModele());
                up.setV_serie(vh1.getmatricule());
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
                fiche_mission.this.adaptervh.filtre(input);
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
        EditText searchbar = chauffeurview.findViewById(R.id.searchbar);
        listviewch = chauffeurview.findViewById(R.id.listview);
        listviewch.setAdapter(adapterch);
        listviewch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                modelchauffeur ch1 = fiche_mission.this.adapterch.getItem(position);
                c_input = String.valueOf(ch1.getId()) + " " + ch1.getNom() + " " + ch1.getPrenom();
                matricule = String.valueOf(ch1.getMatricule());
                ch.setText(c_input);
                up.setId_ch(ch1.getId());
                up.setNom_ch(ch1.getNom());
                up.setPrenom_ch(ch1.getPrenom());
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
                fiche_mission.this.adapterch.filtre(input);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        chauffeurdialog.show();

    }

    protected void update() {
        String de = spinnerde.getSelectedItem().toString();
        String vers = spinnervers.getSelectedItem().toString();

        up.setTrajet("de " + de + " à " + vers);

        Connection cnx = connect.CONN();
        try {
            if (cnx != null) {
                String query = "Update mission set Id_chauffeur='" + up.getId_ch() + "'," +
                        "Id_voiture='" + up.getId_vh() + "'," +
                        "Date_debut='" + up.getDate_debut() + "'," +
                        "Date_fin='" + up.getDate_fin() + "'," +
                        "Trajet='" + up.getTrajet() + "'" +
                        "where ID='" + ms.getId() + "'";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                if (r > 0) {

                    //Toast.makeText(this, "Modifiaction a été effectuée avec succés: Mission.id=" + String.valueOf(up.getId()), Toast.LENGTH_SHORT).show();

                    output = "Modification a été effectuée avec succés.";
                    showoutput(output, "green");

                    insertlogs(session.loggedid(), "Mission",
                            ms.getId() + "|" + datedebut.getText().toString() + "|" + datefin.getText().toString() + "|" + trajet.getText().toString() + "|" + chauffeur.getText().toString() + "|" + voiture.getText().toString(),
                            up.getDate_debut() + "|" + up.getDate_fin() + "|" + up.getTrajet() + "|" + ch.getText().toString() + "|" + vh.getText().toString(),
                            "UPDATE");

                    //Toast.makeText(this, "Mission bien modifiée !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Missions.class));
                } else
                {
                    //Toast.makeText(getBaseContext(), "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                    output = "Erreur lors de la modification.";
                    showoutput(output, "red");
                }


            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchVH() {
        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = " select * from vehicule";
                Statement st1 = cnx.createStatement();
                ResultSet result = st1.executeQuery(query);
                fiche_mission.this.listvh = new ArrayList<modelvoiture>();
                while (result.next()) {
                    String query2 = "select * from mission where Id_voiture = '" + result.getInt(result.findColumn("id")) + "'";
                    Statement st2 = cnx.createStatement();
                    ResultSet resultmission = st2.executeQuery(query2);

                    boolean bool = true;//disponible

                    if (!resultmission.isBeforeFirst())
                        listvh.add(new modelvoiture(
                                result.getInt(result.findColumn("id")),
                                result.getDate(result.findColumn("date_ms")).toString(),
                                result.getString(result.findColumn("marque")),
                                result.getString(result.findColumn("matricule")),
                                result.getString(result.findColumn("carburant")),
                                result.getString(result.findColumn("puissance")),
                                result.getString(result.findColumn("situation")),
                                result.getString(result.findColumn("kilometrage")),
                                String.valueOf(result.getDouble(result.findColumn("consommation"))),
                                false));
                    else {
                        while (resultmission.next() && bool) {
                            SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date debut = resultmission.getTimestamp(resultmission.findColumn("Date_debut"));
                            Date fin = resultmission.getTimestamp(resultmission.findColumn("Date_fin"));
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

                            boolean cmp = datetime.parse(dtt).before(datetime.parse(fn))
                                    && datetime.parse(dtt).after(datetime.parse(deb));
                            if (cmp && result.getString(result.findColumn("situation")).contains("rébutée"))
                                bool = false;

                        }
                        if (bool)
                            listvh.add(new modelvoiture(
                                    result.getInt(result.findColumn("id")),
                                    result.getDate(result.findColumn("date_ms")).toString(),
                                    result.getString(result.findColumn("marque")),
                                    result.getString(result.findColumn("matricule")),
                                    result.getString(result.findColumn("carburant")),
                                    result.getString(result.findColumn("puissance")),
                                    result.getString(result.findColumn("situation")),
                                    result.getString(result.findColumn("kilometrage")),
                                    String.valueOf(result.getDouble(result.findColumn("consommation"))),
                                    true));
                    }
                }

            }
        } catch (Exception ex) {
            Log.e("TAG WISSEM", ex.toString());
        }
    }

    private void fetchCH() {
        connect = new db_connect();
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                Log.d("TAG WISSEM", "cnx invalide");


            } else {

                String query = " select * from chauffeur";
                Statement st1 = cnx.createStatement();
                ResultSet result = st1.executeQuery(query);
                String color = null;
                fiche_mission.this.listch = new ArrayList<modelchauffeur>();
                while (result.next()) {
                    String query2 = "select * from mission where Id_chauffeur = '" + result.getInt(result.findColumn("Id")) + "'";
                    Statement st2 = cnx.createStatement();
                    ResultSet resultmission = st2.executeQuery(query2);


                    boolean bool = true;//disponible

                    if (!resultmission.isBeforeFirst())

                        listch.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                                result.getInt((result.findColumn("matricule"))),
                                result.getString((result.findColumn("Nom"))),
                                result.getString((result.findColumn("Prenom"))),
                                "grey"));
                    else {
                        while (resultmission.next() && bool) {
                            SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date debut = resultmission.getTimestamp(resultmission.findColumn("Date_debut"));
                            Date fin = resultmission.getTimestamp(resultmission.findColumn("Date_fin"));
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

                            boolean cmp = datetime.parse(dtt).before(datetime.parse(fn))
                                    && datetime.parse(dtt).after(datetime.parse(deb));
                            if (cmp)
                                bool = false;
                        }
                        if (bool)
                            listch.add(new modelchauffeur(result.getInt(result.findColumn("Id")),
                                    result.getInt((result.findColumn("matricule"))),
                                    result.getString((result.findColumn("Nom"))),
                                    result.getString((result.findColumn("Prenom"))),
                                    "green"));
                    }

                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
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

                        up.setDate_debut(Date.toString());
                        datedebut.setText(Date.toString());
                        db.setText(Date.toString());


                    }

                };

        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR);
        int mm = c.get(Calendar.MINUTE);
        timed = new TimePickerDialog(this, timeListener, hh, mm, true);
    }

    protected void popupdeletem() {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupdeletem, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = popupviewdelete.findViewById(R.id.oui);
        Button non = popupviewdelete.findViewById(R.id.non);
        popupdelete.show();
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supp();
                popupdelete.dismiss();
                startActivity(new Intent(fiche_mission.this, Missions.class));
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete.dismiss();
            }
        });
    }

    protected void insertlogs(int id, String type, String niveau, String nouveau, String action) {
        Connection cnx;
        try {
            cnx = connect.CONN();
            String query = "insert into logs (id_user,object_type,niveau,nouveau,action)" +
                    "values ('" + String.valueOf(id) + "','" + type + "','" + niveau + "','" + nouveau + "','" + action + "')";
            Statement st = cnx.createStatement();
            st.executeUpdate(query);

        } catch (Exception e) {
            Toast.makeText(this, "aze"+ e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    protected void showoutput(String output, String color) {
        resultat.setVisibility(View.VISIBLE);
        if (color == "red")
            resultat.setTextColor(getResources().getColor(R.color.red));
        if (color == "green")
            resultat.setTextColor(getResources().getColor(R.color.green));

        resultat.setText(output);
    }

    protected void popupedit() {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupedit, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = popupviewdelete.findViewById(R.id.oui);
        Button non = popupviewdelete.findViewById(R.id.non);
        popupdelete.show();
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                popupdelete.dismiss();
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdelete.dismiss();
            }
        });
    }
}
