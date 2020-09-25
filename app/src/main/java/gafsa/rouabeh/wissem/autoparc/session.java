/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc;

import android.content.Context;
import android.content.SharedPreferences;

public class session {
    SharedPreferences peref;
    SharedPreferences.Editor editeur;
    Context context;

    public session(Context context) {
        this.context = context;
        peref = context.getSharedPreferences("application_parcauto", Context.MODE_PRIVATE);
        editeur = peref.edit();
    }

    public void setLoggedin(boolean check_login, int id) {
        editeur.putBoolean("session", check_login);
        editeur.putInt("id", id);
        editeur.apply();
    }

    public boolean logged() {
        return peref.getBoolean("session", false);
    }

    public int loggedid() {
        return peref.getInt("id",-1);
    }

    public void logout(){
        editeur.clear();
        editeur.commit();
    }
    /*
        instanciation Session sess= new Session(this)
        en login : sesson.setloggedin(true)
        en cas de check
        if(session.loggedin()==true
        intent to dashboard (c deja conecté onecté)
        en cas de logout juste methode logout
          void
            session.setloggedin(false)
            finish
            intent
          appelle en clique en le bouton logout
        */
}

