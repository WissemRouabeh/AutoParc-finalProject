package gafsa.rouabeh.wissem.autoparc;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class Profile extends AppCompatActivity {
    Bundle id;
    Button logout;
    TextView nom, prenom, cin, type, infos, pwd, matricule, date, clear;
    session session;
    private modeluser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initview();
        checkUser check = new checkUser(getApplicationContext());
        user = check.execute(session.loggedid());
        setviews();
        //TODO:button deconnexion codage avec longclicklistener to clear cache: trimCache(getBaseContext());
        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                trimCache(getBaseContext());
                return false;
            }
        });
        infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Changeinfos.class).putExtra("user", user));
                //startActivityForResult(new Intent(getApplicationContext(),Changeinfos.class).putExtra("user",user),1);
            }
        });

    }
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] file = dir.list();
            for (String ch : file) {
                boolean success = deleteDir(new File(dir, ch));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    private void setviews() {
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        cin.setText(user.getCin());
        date.setText(user.getDate());
        matricule.setText(user.getMatricule());
        type.setText(user.getType());
    }


    private void initview() {
        session = new session(getApplicationContext());
        nom = findViewById(R.id.nom);
        logout = findViewById(R.id.decnx);
        prenom = findViewById(R.id.prenom);
        cin = findViewById(R.id.cin);
        type = findViewById(R.id.type);
        infos = findViewById(R.id.infos);
        pwd = findViewById(R.id.pwd);
        matricule = findViewById(R.id.matricule);
        date = findViewById(R.id.date);
        clear = findViewById(R.id.clear);
    }



/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
            user=data.getParcelableExtra("user");
            setviews();
                Toast.makeText(this, "Modification effectuée!", Toast.LENGTH_SHORT).show();
            }
            if(resultCode==RESULT_CANCELED){
                Log.e("wissem","temchi bch tjib data wtarja3 testana reponse");
            }
        }
    }
*/


}
