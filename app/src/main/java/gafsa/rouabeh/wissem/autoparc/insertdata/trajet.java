/*
 * *all code wrote by wissem rouabeh**
 */

/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.insertdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.db_connect;
import gafsa.rouabeh.wissem.autoparc.fiches.fiche_utilisateur;
import gafsa.rouabeh.wissem.autoparc.insertdata.mission;
import gafsa.rouabeh.wissem.autoparc.utilisateurs;

public class trajet extends AppCompatActivity {
    ListView listtr;
    TextView resultat;
    EditText input;
    Button btnajout;
    Button btretour;
    db_connect connect;
    List<String> list;
    ArrayAdapter<String> adapter;
    View popupviewdelete;
    AlertDialog popupdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajet);
        initview();
        fetchlist();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listtr.setAdapter(adapter);
        btnajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!list.contains(input.getText().toString()))
                    insert();
                else
                    resultat.setText("Trajet déja existe ! ");
            }
        });
        listtr.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String [] s=list.get(i).replace(" ","%").split("%");
                popupdelete(Integer.parseInt(s[0]));
                return false;
            }
        });
        btretour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),mission.class);
                setResult(RESULT_CANCELED,i);
                finish();
            }
        });
    }

    private void insert() {
        Connection cnx = connect.CONN();
        try {
            if (cnx != null) {
                String query = "insert into trajet(libelle) values('" + input.getText().toString() + "')";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                Intent i = new Intent(getApplicationContext(),mission.class);
                if(r>0){
                    i.putExtra("trajet", input.getText().toString());
                    setResult(RESULT_OK,i);
                    finish();
                }
                st.close();

            }
            cnx.close();
        } catch (Exception e) {
            resultat.setText(e.toString());
        }
    }
    private void delete(int id) {
        Connection cnx = connect.CONN();
        try {
            if (cnx != null) {
                String query = "delete from trajet where id='" + String.valueOf(id) + "'";
                Statement st = cnx.createStatement();
                int r = st.executeUpdate(query);
                if(r>0){
                    resultat.setVisibility(View.VISIBLE);
                    resultat.setTextColor(getResources().getColor(R.color.green));
                    resultat.setText("Trajet a été supprimé avec succés!");
                    startActivity(getIntent());
                }
                st.close();

            }
            cnx.close();
        } catch (Exception e) {
            resultat.setText(e.toString());
        }
    }
    protected void popupdelete(final int idd) {

        LayoutInflater inflater = LayoutInflater.from(this);
        popupviewdelete = inflater.inflate(R.layout.popupdeletet, null);
        popupdelete = new AlertDialog.Builder(this).create();
        popupdelete.setView(popupviewdelete);
        Button oui = popupviewdelete.findViewById(R.id.oui);
        Button non = popupviewdelete.findViewById(R.id.non);
        popupdelete.show();
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(idd);
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

    private void fetchlist() {
        Connection cnx = connect.CONN();
        try {
            if (cnx != null) {
                String query = "select * from trajet";
                Statement st = cnx.createStatement();
                ResultSet r = st.executeQuery(query);
                while (r.next()) {
                    list.add(r.getString(1)+" " +r.getString(2));
                }
            }
        } catch (Exception e) {
            resultat.setText(e.toString());
        }
    }

    private void initview() {
        listtr = findViewById(R.id.listtr);
        resultat = findViewById(R.id.resultat);
        input = findViewById(R.id.input);
        btnajout = findViewById(R.id.btnajout);
        btretour = findViewById(R.id.btretour);
        connect = new db_connect();
        list=new ArrayList<>();
    }
}
