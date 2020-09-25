/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.insertdata;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.checkUser;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;
import gafsa.rouabeh.wissem.autoparc.utilisateurs;

public class utilisateur extends AppCompatActivity {
    EditText matricule, cin;
    EditText nom;
    EditText prenom;
    String output;
    TextView resultat, mdate;
    LinearLayout pr;
    Spinner type;
    Switch c, v, m, s;
    RadioButton cr, ca, vr, va, mr, ma;
    Button btnajout;
    db_connect connect;
    HashMap<String, String> privileges;

    DatePickerDialog dated;
    private int annee, mois, jour;
    private StringBuilder Date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateur);
        initview();
        setdefaultdate();

        cr.setEnabled(false);
        ca.setEnabled(false);
        vr.setEnabled(false);
        va.setEnabled(false);
        mr.setEnabled(false);
        ma.setEnabled(false);


        setprivileges();
        List list = new ArrayList<>();
        list.add("Personnel");
        list.add("Administrateur");
        list.add("Chauffeur");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
        type.setAdapter(adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapter.getItem(i);
                if (!s.equals("Personnel"))
                    pr.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatetime().show();
            }
        });
        btnajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                privileges = getprivileges();
                if (matricule.getText().toString().equals(""))
                    matricule.setError("Champ obligatoire");
                else if (nom.getText().toString().equals(""))
                    matricule.setError("Champ obligatoire");
                else if (prenom.getText().toString().equals(""))
                    matricule.setError("Champ obligatoire");
                else if(String.valueOf(matricule.getText().toString().charAt(0)).equals("0"))
                    showoutput("N° matricule ne doit jamais commencée par 0!", "red");
                else if (type.getSelectedItem().toString().equals("Personnel") && privileges.isEmpty())
                    //resultat.setText("");
                    showoutput("Personnel doit avoir au moins un accès a l'un des modules!", "red");
                else {
                    if (exist(matricule.getText().toString())) {
                        if (cin.getText().toString().length() == 8) {

                            if (type.getSelectedItem().toString().equals("Chauffeur")) {

                                insertch(matricule.getText().toString(),
                                        nom.getText().toString(),
                                        prenom.getText().toString(),
                                        cin.getText().toString(),
                                        mdate.getText().toString()
                                );
                                insertuser(matricule.getText().toString(), matricule.getText().toString(), type.getSelectedItem().toString(),
                                        nom.getText().toString(), prenom.getText().toString(),
                                        cin.getText().toString(),
                                        mdate.getText().toString()
                                );

                            } else {
                                insertuser(matricule.getText().toString(), matricule.getText().toString(), type.getSelectedItem().toString(),
                                        nom.getText().toString(), prenom.getText().toString(),
                                        cin.getText().toString(),
                                        mdate.getText().toString()
                                );

                            }

                        } else
                            showoutput("Le n° cin doit être egale à 8 chiffres!", "red");
                        //TODO: zid verif 3la awel char lazem 0 ou 1
                        //TODO:Matricule ne commencée pas par 0

                    }

                }
            }
        });
    }

    private void initview() {
        type = findViewById(R.id.type);
        matricule = findViewById(R.id.matricule);
        prenom = findViewById(R.id.prenom);
        nom = findViewById(R.id.nom);
        mdate = findViewById(R.id.mdate);
        cin = findViewById(R.id.cin);
        btnajout = findViewById(R.id.btnajout);
        resultat = findViewById(R.id.resultat);
        pr = findViewById(R.id.pr);
        c = findViewById(R.id.C);
        v = findViewById(R.id.V);
        m = findViewById(R.id.M);
        cr = findViewById(R.id.cr);
        ca = findViewById(R.id.ca);
        vr = findViewById(R.id.vr);
        va = findViewById(R.id.va);
        ma = findViewById(R.id.ma);
        mr = findViewById(R.id.mr);
        connect = new db_connect();
    }

    public void setprivileges() {
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cr.setEnabled(true);
                    ca.setEnabled(true);
                } else {
                    cr.setEnabled(false);
                    ca.setEnabled(false);
                }
            }
        });
        m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    mr.setEnabled(true);
                    ma.setEnabled(true);
                } else {
                    mr.setEnabled(false);
                    ma.setEnabled(false);
                }
            }
        });
        v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    vr.setEnabled(true);
                    va.setEnabled(true);
                } else {
                    vr.setEnabled(false);
                    va.setEnabled(false);
                }
            }
        });


    }


    protected HashMap<String, String> getprivileges() {
        HashMap<String, String> hashmap = new HashMap<>();
        if (c.isChecked()) {
            if (ca.isChecked())
                hashmap.put("C", "ALL");
            if (cr.isChecked())
                hashmap.put("C", "READ");
        }
        if (v.isChecked()) {
            if (va.isChecked())
                hashmap.put("V", "ALL");
            if (vr.isChecked())
                hashmap.put("V", "READ");
        }
        if (m.isChecked()) {
            if (ma.isChecked())
                hashmap.put("M", "ALL");
            if (mr.isChecked())
                hashmap.put("M", "READ");
        }

        return hashmap;
    }

    protected void insertuser(String matricule, String pwd, String type, String nom, String prenom, String cin, String date) {

        Connection cnx;
        try {
            cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "Insert into utilisateur" +
                        " (matricule,cin,pwd,nom,prenom,type,date_recrute) values ('" + matricule +
                        "','" +
                        cin + "','" +
                        pwd + "','" +
                        nom + "','" +
                        prenom + "','"
                        + type + "','" +
                        date + "')";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                if (r > 0) {
                    insertaccess(matricule);
                    startActivity(new Intent(getApplicationContext(), utilisateurs.class));
                    finish();
                } else {
                    output = "Erreur lors de l'insertion !";
                    showoutput(output, "red");
                }

            }
        } catch (Exception e) {
            if (e.toString().contains("cin"))
                showoutput("N° cin déja existe!", "red");

        }
    }

    protected void insertch(String matricule, String nom, String prenom, String cin, String date) {

        Connection cnx;
        try {
            cnx = connect.CONN();
            if (cnx == null) {
                Toast.makeText(this, "Connection non etablit !", Toast.LENGTH_SHORT).show();
            } else {
                String query = "Insert into chauffeur (matricule,Nom,Prenom,cin,date_recrute) values ('" + matricule + "' , '" + nom + "' , '" + prenom +
                        "' , '" + cin + "' , '" + date + "')";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                if (r > 0) {
                    output = "Nouveau chauffeur a été ajouté avec succés.";
                    showoutput(output, "green");
                } else {
                    output = "Erreur lors de l'insertion !";
                    showoutput(output, "red");
                }


            }
        } catch (Exception e) {

            if (e.toString().contains("cin")) {

                output = "N° cin déja existe!";
                showoutput(output, "red");
            }
        }
    }

    protected void insertaccess(String matricule) {
        checkUser checkUser = new checkUser(getApplicationContext());
        modeluser user = checkUser.getid(matricule);
        if (user != null) {
            int id = user.getId();

            Connection cnx;
            try {
                cnx = connect.CONN();
                if (cnx == null) {
                    Toast.makeText(this, "Connection non etablit !", Toast.LENGTH_SHORT).show();
                } else {
                    String query = "Insert into access (id_user,ou,crud) values (?,?,?) ";
                    PreparedStatement st = cnx.prepareStatement(query);
                    for (Map.Entry<String, String> entry : privileges.entrySet()) {
                        st.setInt(1, id);
                        st.setString(2, entry.getKey());
                        st.setString(3, entry.getValue());
                        st.executeUpdate();
                    }
                    output = "Nouveau utilisateur a été ajouté avec succés.";
                    showoutput(output, "green");
                    st.close();
                    cnx.close();

                }
            } catch (Exception e) {
                resultat.setText("");
            }

        }
    }

    private boolean exist(String mt) {
        connect = new db_connect();
        boolean check = true;
        try {
            Connection cnx = connect.CONN();
            if (cnx == null) {
                output = "Connection non etablit";
                showoutput(output, "red");
            } else {
                String query = "select * from utilisateur where matricule='" + mt + "'";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                if (r.next()) {
                    check = false;
                    output = "Utilisateur déja existe!\n" +
                            "Matricule:" + mt + " déja existe!";
                    matricule.setText("");
                    showoutput(output, "red");
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    private void setdefaultdate() {
        Calendar cal = Calendar.getInstance();
        int yyyy = cal.get(Calendar.YEAR);
        int MM = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        String datein = yyyy + "-" + MM + "-" + dd;
        mdate.setText(datein);
    }

    protected void showoutput(String output, String color) {
        resultat.setVisibility(View.VISIBLE);
        if (color == "red")
            resultat.setTextColor(getResources().getColor(R.color.red));
        if (color == "green")
            resultat.setTextColor(getResources().getColor(R.color.green));

        resultat.setText(output);
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

                mdate.setText(Date);

            }
        };
        Calendar c = Calendar.getInstance();
        int aa = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        dated = new DatePickerDialog(this, dateListener, aa, mm, dd);
        return dated;

    }


}
