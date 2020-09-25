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

import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.model.modelmission;

public class adapteurlistemissions extends BaseAdapter {


    Context mContext;
    List<modelmission> mdata;
    List<modelmission> Fdata; /*hethi bech nesta3mlouha fel filtrage*/
    List<modelmission> Sdata; /*hethi bech nesta3mlouha fel ordrement*/

    public adapteurlistemissions(Context mContext, List<modelmission> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;

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
    public modelmission getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.itemm, parent, false);
        }

        TextView ch = view.findViewById(R.id.id_miss);
        TextView vt = view.findViewById(R.id.vt);
        TextView vhh = view.findViewById(R.id.vh);
        TextView v = view.findViewById(R.id.trajet);
        TextView date = view.findViewById(R.id.dt_deb);
        ImageView srvc = view.findViewById(R.id.srvc);
        TextView srvc1 = view.findViewById(R.id.srvc1);

        modelmission ms = getItem(position);
        String chr;
        if(!StringUtils.isNullOrEmpty(ms.getNom_ch())||!StringUtils.isNullOrEmpty(ms.getNom_ch()))
            ch.setText(ms.getNom_ch() + " " + ms.getPrenom_ch());

        v.setText(ms.getTrajet());
        String ss = ms.getV_marq() + " " + ms.getV_serie();
        if (!ss.equals("null null"))
            vt.setText(ss);
        srvc.setColorFilter(getcolor(ms.getService()));
        if (ms.getDate_fin().contains("2050")) {
            chr = "";
            chr = ms.getDate_debut();
            //srvc.setColorFilter(mContext.getResources().getColor(R.color.brown)); //ga3da temchi
            date.setText(chr.replace(".0", ""));
        } else {
            chr = "";
            chr = ms.getDate_debut() + " " + ms.getDate_fin();
            date.setText(chr.replace(".0", ""));

        }
        srvc1.setTextColor(getcolor(ms.getService()));

        switch (ms.getService()) {
            case "green":
                srvc1.setText("Aujourd'hui");
                break;
            case "blue":
                srvc1.setText("Pas encore commencée");
                break;
            case "grey":
                srvc1.setText("Terminée");
                break;
            case "brown":
                srvc1.setText("Déja commencée");
                break;
            default:
                break;
        }

        return view;

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
            case "brown":
                color = mContext.getResources().getColor(R.color.brown);
                break;
            default:
                break;
        }
        return color;
    }


    public void filtre(String chaine) {
        chaine = chaine.toLowerCase(Locale.getDefault());
        mdata.clear();
        if (chaine.length() == 0) {
            mdata.addAll(Fdata);
        } else {
            for (modelmission ms : Fdata) {
                if (ms.getTrajet().toLowerCase(Locale.getDefault()).contains(chaine)
                        || String.valueOf(ms.getId_ch()).contains(chaine)
                        || String.valueOf(ms.getId()).contains(chaine)
                        || String.valueOf(ms.getNom_ch()).contains(chaine)
                        || String.valueOf(ms.getPrenom_ch()).contains(chaine)
                        || String.valueOf(ms.getId_vh()).contains(chaine))
                    mdata.add(ms);
            }

        }
        notifyDataSetChanged();
    }

    public void sorted(String chaine) {
        mdata.clear();
        int i = Sdata.size();
        for (modelmission ms : Sdata) {
            if (ms.getService().equals(chaine))
                mdata.add(ms);
        }
        for (modelmission ms : Sdata) {
            while (!(mdata.contains(ms))) {
                mdata.add(ms);
            }
        }

        notifyDataSetChanged();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void sorteddispo() {
        //green disponible et grey nouveau ma3mal hata mission
        mdata.clear();
        int i = Sdata.size();
        for (modelmission ms : Sdata) {
            if (ms.getService().equals("green") || ms.getService().equals("blue"))
                mdata.add(ms);
        }

        notifyDataSetChanged();
    }
}
