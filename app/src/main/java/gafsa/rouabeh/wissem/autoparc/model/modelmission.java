/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class modelmission implements Parcelable{

    int id;
    int id_ch;
    int matricule;
    int id_vh;
    int id_tr;
    String nom_ch;
    String prenom_ch;
    String v_marq;
    String v_serie;
    String v_num_chass;
    String trajet;
    String date_debut;
    String date_fin;
    String service;

    public modelmission() {
    }

    public modelmission(int id, String nom_ch, String prenom_ch, String v_marq, String v_serie, String trajet, String date_debut, String date_fin, String service, int idch, int matricule, int idvh) {
        this.id = id;
        this.nom_ch = nom_ch;
        this.prenom_ch = prenom_ch;
        this.v_marq = v_marq;
        this.v_serie = v_serie;
        this.trajet = trajet;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.service = service;
        this.id_ch = idch;
        this.matricule = matricule;
        this.id_vh = idvh;
    }


    public modelmission(int id, int id_ch, int id_vh, String trajet, String date_debut, String date_fin, String service) {
        this.id = id;
        this.id_ch = id_ch;
        this.id_vh = id_vh;
        this.trajet = trajet;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.service = service;
    }

    public modelmission(int id, int id_ch, int id_vh, String trajet, String date_debut, String date_fin) {
        this.id = id;
        this.id_ch = id_ch;
        this.id_vh = id_vh;
        this.trajet = trajet;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }


    protected modelmission(Parcel in) {
        id = in.readInt();
        matricule = in.readInt();
        id_ch = in.readInt();
        id_vh = in.readInt();
        id_tr = in.readInt();
        nom_ch = in.readString();
        prenom_ch = in.readString();
        v_marq = in.readString();
        v_serie = in.readString();
        v_num_chass = in.readString();
        trajet = in.readString();
        date_debut = in.readString();
        date_fin = in.readString();
        service = in.readString();
    }

    public static final Creator<modelmission> CREATOR = new Creator<modelmission>() {
        @Override
        public modelmission createFromParcel(Parcel in) {
            return new modelmission(in);
        }

        @Override
        public modelmission[] newArray(int size) {
            return new modelmission[size];
        }
    };

    public int getId_tr() {
        return id_tr;
    }
    public int getMatricule() {
        return matricule;
    }

    public void setId_tr(int id_tr) {
        this.id_tr = id_tr;
    }

    public String getNom_ch() {
        return nom_ch;
    }

    public void setNom_ch(String nom_ch) {
        this.nom_ch = nom_ch;
    }

    public String getPrenom_ch() {
        return prenom_ch;
    }

    public void setPrenom_ch(String prenom_ch) {
        this.prenom_ch = prenom_ch;
    }

    public String getV_marq() {
        return v_marq;
    }

    public void setV_marq(String v_marq) {
        this.v_marq = v_marq;
    }

    public String getV_serie() {
        return v_serie;
    }

    public void setV_serie(String v_serie) {
        this.v_serie = v_serie;
    }

    public String getV_num_chass() {
        return v_num_chass;
    }

    public void setV_num_chass(String v_num_chass) {
        this.v_num_chass = v_num_chass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_ch() {
        return id_ch;
    }

    public void setId_ch(int id_ch) {
        this.id_ch = id_ch;
    }

    public int getId_vh() {
        return id_vh;
    }

    public void setId_vh(int id_vh) {
        this.id_vh = id_vh;
    }

    public String getTrajet() {
        return trajet;
    }

    public void setTrajet(String trajet) {
        this.trajet = trajet;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(matricule);
        parcel.writeInt(id_ch);
        parcel.writeInt(id_vh);
        parcel.writeInt(id_tr);
        parcel.writeString(nom_ch);
        parcel.writeString(prenom_ch);
        parcel.writeString(v_marq);
        parcel.writeString(v_serie);
        parcel.writeString(v_num_chass);
        parcel.writeString(trajet);
        parcel.writeString(date_debut);
        parcel.writeString(date_fin);
        parcel.writeString(service);
    }
}
