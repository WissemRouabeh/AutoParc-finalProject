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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.model.modelbon;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class adapteurbon extends BaseAdapter {
    List<modelbon> mdata;
    List<modelbon> Fdata;
    Context mContext;

    public adapteurbon(List<modelbon> mdata, Context mContext) {
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
    public modelbon getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mdata.get(i).getNum_bon();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from((mContext));
            view = inflater.inflate(R.layout.itembon, parent, false);
        }
        TextView matricule = view.findViewById(R.id.matricule);
        TextView nomprenom = view.findViewById(R.id.nomprenom);
        TextView type = view.findViewById(R.id.type);
        TextView fct = view.findViewById(R.id.statut);
        TextView date = view.findViewById(R.id.date);
        modelbon us = mdata.get(i);
        matricule.setText(us.getMarque() +" | "+us.getSerie());
        String np = us.getKilo_ancien() + " | Kilométrage coulé( " + us.getKilo_cons() + " )";
        nomprenom.setText(np);
        type.setText(us.getId_voiture());
        fct.setText(us.getStatut());
        date.setText(us.getDate_bon());
        return view;
    }

    public void filtre(String chaine) {
        chaine = chaine.toLowerCase(Locale.getDefault());
        mdata.clear();
        if (chaine.length() == 0) {
            mdata.addAll(Fdata);
        } else {

            for (modelbon us : Fdata) {
                if (us.getStatut().toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getDate_bon().toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getId_voiture().toLowerCase(Locale.getDefault()).contains(chaine)) {

                    mdata.add(us);
                }
            }
        }
        notifyDataSetChanged();
    }


}
