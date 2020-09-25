package gafsa.rouabeh.wissem.autoparc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class splash extends AppCompatActivity {
    session session;
    TextView titre, check,message;
    ImageView imagesplsah;
    db_connect connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        titre = findViewById(R.id.titre);
        check = findViewById(R.id.check);
        message = findViewById(R.id.resultat);
        imagesplsah = findViewById(R.id.imagesplash);

        titre.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_fade));
        imagesplsah.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_out));
        session = new session(getApplicationContext());
        message.setText("Connexion encours..");
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getIntent());
                finish();
            }
        });
            connect = new db_connect();
            try {
                Connection cnx = connect.CONN();
                if (cnx == null) {
                    check.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                } else {
                    check.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                    message.setText("Connexion etablit.");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkgoto(session.logged());
                            finish();
                        }
                    }, 2000);
                }
            } catch (Exception e) {
                Log.e("wissem","123...321");

            }

    }

    private void checkgoto(boolean session1) {
        if (session1) {
                startActivity(new Intent(splash.this, dashboard_view.class));

        } else {
            startActivity(new Intent(splash.this, Login.class));
        }

    }

}

