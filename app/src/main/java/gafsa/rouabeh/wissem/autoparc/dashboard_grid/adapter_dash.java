/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.dashboard_grid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gafsa.rouabeh.wissem.autoparc.Logs;
import gafsa.rouabeh.wissem.autoparc.Missions;
import gafsa.rouabeh.wissem.autoparc.Profile;
import gafsa.rouabeh.wissem.autoparc.R;
import gafsa.rouabeh.wissem.autoparc.Voitures;
import gafsa.rouabeh.wissem.autoparc.chauffeurs;
import gafsa.rouabeh.wissem.autoparc.model.modeldash;
import gafsa.rouabeh.wissem.autoparc.utilisateurs;

public class adapter_dash extends RecyclerView.Adapter<adapter_dash.view_holder> {
    Context mContext;
    List<modeldash> data;
    ItemClickListener listeneritem;

    public adapter_dash(Context mContext, List<modeldash> data) {
        this.mContext = mContext;
        this.data = data;
    }
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(ItemClickListener clickListener) {
        listeneritem = clickListener;
    }

    @NonNull
    @Override
    public adapter_dash.view_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell,null);
        return new view_holder(view,listeneritem);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_dash.view_holder holder,final int i) {
    holder.titre.setText(data.get(i).getTitre());
    holder.desc.setText(data.get(i).getDesc());
    holder.img.setImageResource(data.get(i).getImg());

    holder.card.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (data.get(i).getTitre()) {
                case "Chauffeurs":
                    mContext.startActivity(new Intent(mContext, chauffeurs.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "Missions":
                    mContext.startActivity(new Intent(mContext, Missions.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "Voitures":
                    mContext.startActivity(new Intent(mContext, Voitures.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "Utilisateurs":
                    mContext.startActivity(new Intent(mContext, utilisateurs.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "Journaux":
                    mContext.startActivity(new Intent(mContext, Logs.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case "Compte":
                    mContext.startActivity(new Intent(mContext, Profile.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                default:
                    break;
            }
        }
    });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class view_holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView titre;
        TextView desc;
        CardView card;
        public view_holder(@NonNull View itemView,final ItemClickListener listen) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            titre=itemView.findViewById(R.id.titre);
            desc=itemView.findViewById(R.id.desc);
            card=itemView.findViewById(R.id.card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listen!=null) {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION) {
                            listen.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
