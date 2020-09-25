/*
 * *all code wrote by wissem rouabeh**
 */

/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.adapteurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.model.modellog;

public class adapteurlog extends BaseAdapter {
    List<modellog> mdata;
    List<modellog> Fdata;


    Context mContext;

    public adapteurlog(List<modellog> mdata, Context mContext) {
        this.mdata = mdata;
        this.Fdata = new ArrayList<>();
        this.Fdata.addAll(mdata);
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public modellog getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mdata.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from((mContext));
            view = inflater.inflate(R.layout.itemlog, parent, false);
        }

        RelativeLayout r = view.findViewById(R.id.r);
        TextView id_user = view.findViewById(R.id.id_user);
        TextView type = view.findViewById(R.id.type);
        LinearLayout qq = view.findViewById(R.id.qq);
        TextView action = view.findViewById(R.id.action);
        TextView npuser = view.findViewById(R.id.npuser);
        TextView don1 = view.findViewById(R.id.don1);
        TextView date = view.findViewById(R.id.date);
        modellog us = mdata.get(i);
        id_user.setText(String.valueOf(us.getId_user()));

        String[] values = us.getAncien().replace("|", "&").split("&");
        if (us.getType().equals("Voiture"))
            if (us.getAction().equals("UPDATE"))
                don1.setText("Mise a jour au niveau de voiture ayant la matricule:" + values[0]);
            else
                don1.setText("Suppresion de voiture ayant la matricule:" + values[0]);

        if (us.getType().equals("Chauffeur"))
            if (us.getAction().equals("UPDATE"))
                don1.setText("Mise a jour au niveau de chauffeur ayant la matricule:" + values[0]);
            else
                don1.setText("Suppresion de chauffeur ayant la matricule:" + values[0]);

        if (us.getType().equals("Mission"))
            if (us.getAction().equals("UPDATE"))
                don1.setText("Mise a jour d'une mission");
            else
                don1.setText("Suppresion d'une mission");


        if (us.getAction().toLowerCase(Locale.getDefault()).equals("delete")) {
            action.setText("Suppression");
            action.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        if (us.getAction().toLowerCase(Locale.getDefault()).equals("update")) {
            action.setText("Mise à jour");

            action.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }

        npuser.setText(us.getNomprenom_user());
        date.setText(us.getDate().replace(".0", ""));
        type.setText(us.getType());

        return view;
    }

    public void filtre(String chaine) {
        chaine = chaine.toLowerCase(Locale.getDefault());
        mdata.clear();
        if (chaine.length() == 0) {
            mdata.addAll(Fdata);
        } else {
            for (modellog us : Fdata) {
                if (String.valueOf(us.getId_user()).toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getNomprenom_user().toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getDate().toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getAction().toLowerCase(Locale.getDefault()).contains(chaine)) {
                    mdata.add(us);
                }
            }
        }
        notifyDataSetChanged();
    }

}
