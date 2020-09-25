/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.adapteurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.model.modeluser;

public class adapteuruser extends BaseAdapter {
    List<modeluser> mdata;
    List<modeluser> Fdata;
    List<modeluser> Sdata;
    Context mContext;

    public adapteuruser(List<modeluser> mdata, Context mContext) {
        this.mdata = mdata;
        this.mContext = mContext;
        this.Fdata = new ArrayList<>();
        this.Fdata.addAll(mdata);
        this.Sdata = new ArrayList<>();
        this.Sdata.addAll(mdata);
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public modeluser getItem(int i) {
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
            view = inflater.inflate(R.layout.itemuser, parent, false);
        }
        TextView matricule = view.findViewById(R.id.matricule);
        TextView nomprenom = view.findViewById(R.id.nomprenom);
        TextView type = view.findViewById(R.id.type);
        TextView fct = view.findViewById(R.id.statut);
        ImageView img=view.findViewById(R.id.icon);

        modeluser us = mdata.get(i);

        matricule.setText("M°"+us.getMatricule()+" C°"+us.getCin());
        String np = us.getNom() + " " + us.getPrenom();
        nomprenom.setText(np);
        type.setText(us.getType());
        if(us.getStatut().equals("Fonctionne")){
            fct.setTextColor(mContext.getResources().getColor(R.color.green));
            fct.setText("Actif");
            img.setColorFilter(mContext.getResources().getColor(R.color.green));
        }else{
            fct.setTextColor(mContext.getResources().getColor(R.color.grey));
            fct.setText("Suspendu");
            img.setColorFilter(mContext.getResources().getColor(R.color.grey));
        }

        return view;
    }


    public void sorted(String type, String chaine) {
        mdata.clear();
        if (type.equals("statut")) {
            for (modeluser us : Sdata) {
                if (us.getStatut().equals(chaine))
                    mdata.add(us);
            }
        }
        if (type.equals("type")) {
            for (modeluser us : Sdata) {
                if (us.getType().equals(chaine))
                    mdata.add(us);
            }
        }
        if (type.equals("suspendu")) {
            for (modeluser us : Sdata) {
                if (us.getType().equals(chaine) && us.getStatut().equals("Suspendu"))
                    mdata.add(us);
            }
        }if (type.equals("fonctionne")) {
            for (modeluser us : Sdata) {
                if (us.getType().equals(chaine) && us.getStatut().equals("Fonctionne"))
                    mdata.add(us);
            }
        }
        for (modeluser us : Sdata) {
            while (!(mdata.contains(us))) {
                mdata.add(us);
            }
        }

        notifyDataSetChanged();
    }


    public void filtre(String chaine) {
        chaine = chaine.toLowerCase(Locale.getDefault());
        mdata.clear();
        if (chaine.length() == 0) {
            mdata.addAll(Fdata);
        } else {

            for (modeluser us : Fdata) {
                if (us.getNom().toLowerCase(Locale.getDefault()).contains(chaine) || us.getPrenom().toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getType().toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getStatut().toLowerCase(Locale.getDefault()).contains(chaine)
                        || us.getMatricule().toLowerCase(Locale.getDefault()).contains(chaine)) {
                    mdata.add(us);
                }
            }
        }
        notifyDataSetChanged();
    }


}
