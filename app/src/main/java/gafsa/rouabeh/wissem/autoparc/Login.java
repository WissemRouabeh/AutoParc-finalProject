package gafsa.rouabeh.wissem.autoparc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    EditText user, pass;
    Button btn_login;
    String output;
    db_connect connect;
    TextView mOutput;
    TextView forgotpass;
    session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String matricule = user.getText().toString().trim();
                String mdp = pass.getText().toString().trim();
                if (champcheck(matricule, mdp)) {
                    connect = new db_connect();
                    try {
                        Connection cnx = connect.CONN();
                        if (connect.CONN() == null) {
                            output = "no connection";
                            mOutput.setText(output);
                        } else {
                            String query = "select * from utilisateur where matricule='" + matricule + "'";
                            Statement st1 = cnx.createStatement();
                            ResultSet result = st1.executeQuery(query);
                            if (result.next()) {
                                String truepwd = result.getString(result.findColumn("pwd"));
                                if (truepwd.equals(mdp)) {
                                    if (result.getString(result.findColumn("statut")).equals("Fonctionne")) {
                                        Intent i = new Intent(Login.this, dashboard_view.class);
                                        logged(result.getInt(result.findColumn("id")));
                                        startActivity(i);
                                        finish();
                                    } else {
                                        mOutput.setText("Compte suspendu pour le moment.");
                                    }
                                } else
                                    mOutput.setText("Mot de passe incorrect.");
                            } else {
                                mOutput.setText("Utilisateur indisponible.");
                            }

                        }
                    } catch (Exception ex) {
                        mOutput.setText(ex.toString());
                    }
                } else
                    mOutput.setText("La saisie est incomplète.");
            }

        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), recuper_pwd.class));
            }
        });

    }

    private boolean champcheck(String s1, String s2) {
        boolean check = false;
        if (!((s1.equals("")) || (s2.equals("")))) {
            check = true;
        }
        return check;
    }

    protected void logged(int i) {
        session = new session(getApplicationContext());
        session.setLoggedin(true, i);
    }
    protected void initview() {
        btn_login = findViewById(R.id.btn);
        mOutput = findViewById(R.id.rzlt);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        forgotpass = findViewById(R.id.forgotpass);
    }
}

