/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.fiches;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gafsa.rouabeh.wissem.autoparc.R;

public class fiche_bon extends AppCompatActivity {
    ImageView edit;
    TextView viewprix, viewdate, viewkilo;
    EditText changeprix, changedate, changekilo;
    LinearLayout modifier;
    Button delete, submit, annuler;
    AlertDialog popupdelete;
    View popupviewdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_bon);
        initview();

    }

    private void initview() {
        edit=findViewById(R.id.edit);
        viewdate=findViewById(R.id.viewdate);
        viewkilo=findViewById(R.id.viewkilo);
        viewprix=findViewById(R.id.viewprix);
        changedate=findViewById(R.id.changedate);
        changekilo=findViewById(R.id.changekilo);
        changeprix=findViewById(R.id.changeprix);
    }
}
