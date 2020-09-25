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

import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.model.modelvoiture;

public class adapteurlistevoiture extends BaseAdapter {

    List<modelvoiture> mdata; /*hethi el bch nfiltrouha eli feha donnees lkol*/
    List<modelvoiture> Fdata; /*hethi bech nesta3mlouha fel filtrage*/
    List<modelvoiture> Sdata; /*hethi bech nesta3mlouha fel ordrement*/

    Context mContext;

    public adapteurlistevoiture(List<modelvoiture> data, Context context) {
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
    public modelvoiture getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from((mContext));
            view = inflater.inflate(R.layout.itemv, parent, false);
        }
        TextView model = view.findViewById(R.id.model);
        TextView matricule = view.findViewById(R.id.matric);
        TextView date = view.findViewById(R.id.date);
        TextView situation = view.findViewById(R.id.situation);
        TextView info = view.findViewById(R.id.info);
        ImageView srvc = view.findViewById(R.id.srvc);

        modelvoiture vh = getItem(position);

        model.setText(vh.getMarque());
        matricule.setText(vh.getmatricule());

        if(!StringUtils.isNullOrEmpty(vh.getDate()))
        date.setText(vh.getDate().replace(".0",""));


        situation.setText(vh.getSit_act());



        info.setText(vh.getCarburant()+" "+vh.getPuissance());


        if (vh.getService() == true)
            srvc.setColorFilter(mContext.getResources().getColor(R.color.blue));
        else
            srvc.setColorFilter(mContext.getResources().getColor(R.color.green));

        if(!StringUtils.isNullOrEmpty(vh.getSit_act())){

            if(vh.getSit_act().contains("circulation"))
                situation.setTextColor(mContext.getResources().getColor(R.color.green));

            if(vh.getSit_act().contains("En cours")){
                situation.setTextColor(mContext.getResources().getColor(R.color.blue));
                srvc.setColorFilter(mContext.getResources().getColor(R.color.greytransparent));
            }

            if(vh.getSit_act().contains("rébutée")){
                situation.setTextColor(mContext.getResources().getColor(R.color.red));
                srvc.setColorFilter(mContext.getResources().getColor(R.color.greytransparent));
            }


        }

        return view;
    }

    public void filtre(String chaine) {
        chaine = chaine.toLowerCase(Locale.getDefault());
        mdata.clear();
        if (chaine.length() == 0) {
            mdata.addAll(Fdata);
        } else {

            for (modelvoiture vh : Fdata) {
                if (vh.getMarque().toLowerCase(Locale.getDefault()).contains(chaine)
                        || vh.getmatricule().toLowerCase(Locale.getDefault()).contains(chaine)
                        || vh.getDate().toLowerCase(Locale.getDefault()).contains(chaine)
                        || vh.getPuissance().toLowerCase(Locale.getDefault()).contains(chaine)
                        || vh.getSit_act().toLowerCase(Locale.getDefault()).contains(chaine))
                    {
                    mdata.add(vh);
                    }
            }
        }
        notifyDataSetChanged();
    }


    public void sorteddispo() {
        //green disponible et grey nouveau ma3mal hata mission
        mdata.clear();
        int i = Sdata.size();
        for (modelvoiture vh : Sdata) {
            if (vh.getService() == false)
                mdata.add(vh);
        }

        notifyDataSetChanged();
    }

    public void sorted(boolean b) {
        mdata.clear();
        for (modelvoiture vh : Fdata) {
            if (vh.getService() == b)
                mdata.add(vh);
        }
        for (modelvoiture vh : Fdata) {
            if ((!mdata.contains(vh)))
                mdata.add(vh);
        }
//TODO:zid condition tri: hors service.
    }


    public void refresh() {
        notifyDataSetChanged();
    }

}
