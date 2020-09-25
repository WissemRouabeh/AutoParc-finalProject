/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class recuper_pwd extends AppCompatActivity {

    int progressStatus = 0;
    Handler handler = new Handler();
    Button btn, btn2;
    EditText matricule, cin;
    TextView pwd, pwd_verif, rzlt;
    ProgressBar progressbar;
    db_connect connect;
    String mat;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuper_pwd);

        initview();
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                check_input();


            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_input2()) {
                    update();
                    progressthread(2);
                } else {
                    rzlt.setText("Les champs doivent être identiques et de 8 caractères minimum");
                }

            }
        });
    }

    private void progressthread(final int i) {
        progressStatus = 0;
        rzlt.setText("");
        progressbar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setProgress(progressStatus);

                            if (progressStatus == 100) {

                                if (i == 1) {
                                    matricule.setText("");
                                    cin.setText("");
                                    progressbar.setVisibility(View.GONE);
                                    logo.setText("Réintialiser le mot de passe");
                                    pwd.setText("Nouveau mot de passe");
                                    pwd_verif.setText("Confirmer le mot de passe");
                                    matricule.requestFocus();
                                    matricule.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    cin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    btn2.setVisibility(View.VISIBLE);
                                }

                                if (i == 2) {
                                    btn2.setVisibility(View.GONE);
                                    rzlt.setTextColor(getResources().getColor(R.color.green));
                                    rzlt.setText("Mot de passe a été réintialisé avec succés");
                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                    finish();

                                }

                            }
                        }
                    });
                }
            }
        }).start();
    }

    private boolean check_input2() {
        boolean check = true;
        if (matricule.getText().toString().length() < 8 || !matricule.getText().toString().equals(cin.getText().toString())) {
            check = false;
            matricule.setText("");
            cin.setText("");
        }
        return check;
    }

    private void check_input() {
        try {
            Connection cnx = connect.CONN();
            if (cnx != null) {
                String query = "select matricule,pwd from utilisateur where matricule='" + matricule.getText().toString() + "'";
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    mat = matricule.getText().toString();
                    if (rs.getString(rs.findColumn("cin")).equals(cin.getText().toString())) {
                        progressthread(1);
                        btn.setVisibility(View.GONE);
                    } else
                        rzlt.setText("Verifiez les champs");

                } else {
                    rzlt.setText("Verifiez les champs");
                }

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void update() {
        try {
            Connection cnx = connect.CONN();
            if (cnx != null) {
                String query = "update utilisateur set pwd='" + matricule.getText().toString() + "'" +
                        "where matricule='" + mat + "'";
                Statement st = cnx.createStatement();
                st.executeUpdate(query);

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("qsd", e.toString());

        }
    }


    private void initview() {
        btn = findViewById(R.id.btn);
        btn2 = findViewById(R.id.btn_app);
        pwd = findViewById(R.id.chang_pwd);
        pwd_verif = findViewById(R.id.chang_pwd_verif);
        matricule = findViewById(R.id.matricule);
        logo = findViewById(R.id.logo);
        cin = findViewById(R.id.cin);
        rzlt = findViewById(R.id.rzlt);
        progressbar = findViewById(R.id.progress);
        connect = new db_connect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }
}