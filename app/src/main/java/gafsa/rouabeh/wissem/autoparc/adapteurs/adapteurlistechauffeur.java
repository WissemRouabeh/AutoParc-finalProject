package gafsa.rouabeh.wissem.autoparc.adapteurs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import gafsa.rouabeh.wissem.autoparc.model.modelchauffeur;

public class adapteurlistechauffeur extends BaseAdapter {

    List<modelchauffeur> mdata; /*hethi el bch nfiltrouha eli feha donnees lkol*/
    List<modelchauffeur> Fdata; /*hethi bech nesta3mlouha fel filtrage*/
    List<modelchauffeur> Sdata; /*hethi bech nesta3mlouha fel ordrement*/

    Context mContext;


    public adapteurlistechauffeur(List<modelchauffeur> data, Context context) {
        this.mdata = data;
        this.mContext = context;
        this.Fdata = new ArrayList<>();
        this.Fdata.addAll(mdata);
        this.Sdata = new ArrayList<>();
        this.Sdata.addAll(mdata);

    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Nullable
    @Override
    public modelchauffeur getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from((mContext));
            view = inflater.inflate(R.layout.item, parent, false);
        }
        TextView nom = view.findViewById(R.id.nom);
        TextView prenom = view.findViewById(R.id.prenom);
        ImageView srvc = view.findViewById(R.id.userimg);
        TextView matricule = view.findViewById(R.id.matricule);

        modelchauffeur chauffeur = getItem(position);
        matricule.setText("M°"+String.valueOf(chauffeur.getMatricule())+" C°"+chauffeur.getCin());
        nom.setText(chauffeur.getNom());
        prenom.setText(chauffeur.getPrenom());
        srvc.setColorFilter(getcolor(chauffeur.getService()));
        return view;
    }

    public void filtre(String chaine) {
        chaine = chaine.toLowerCase(Locale.getDefault());
        mdata.clear();
        if (chaine.length() == 0) {
            mdata.addAll(Fdata);
        } else {

            for (modelchauffeur CH : Fdata) {
                if (CH.getNom().toLowerCase(Locale.getDefault()).contains(chaine) || CH.getPrenom().toLowerCase(Locale.getDefault()).contains(chaine)) {
                    mdata.add(CH);
                }
            }
        }
        notifyDataSetChanged();
    }

    private int getcolor(String couleur) {
        int color = mContext.getResources().getColor(R.color.grey);
        switch (couleur) {
            case "green":
                color = mContext.getResources().getColor(R.color.green);
                break;
            case "blue":
                color = mContext.getResources().getColor(R.color.blue);
                break;
            case "grey":
                color = mContext.getResources().getColor(R.color.grey);
                break;

            default:
                break;
        }
        return color;
    }

    public void sorted(String chaine) {
        mdata.clear();
        int i = Sdata.size();
        for (modelchauffeur ch : Sdata) {
            if (ch.getService().equals(chaine))
                mdata.add(ch);
        }
        for (modelchauffeur ch : Sdata) {
            while (!(mdata.contains(ch))) {
                mdata.add(ch);
            }
        }

        notifyDataSetChanged();
    }
    public void refresh(){
        notifyDataSetChanged();
    }

    public void sorteddispo() {
        //green disponible et grey nouveau ma3mal hata mission
        mdata.clear();
        for (modelchauffeur ch : Sdata) {
            if (ch.getService().equals("green")||ch.getService().equals("grey")){
                mdata.add(ch);
            notifyDataSetChanged();}
            notifyDataSetChanged();

        }

        notifyDataSetChanged();
    }

}
