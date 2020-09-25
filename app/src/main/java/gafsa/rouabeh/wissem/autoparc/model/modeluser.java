/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class modeluser implements Parcelable {
    public static final Creator<modeluser> CREATOR = new Creator<modeluser>() {
        @Override
        public modeluser createFromParcel(Parcel in) {
            return new modeluser(in);
        }

        @Override
        public modeluser[] newArray(int size) {
            return new modeluser[size];
        }
    };
    int id;
    String nom;
    String prenom;
    String pwd;
    String type;
    String statut;
    String matricule;
    String cin;
    String date;

    public modeluser(int id, String nom, String prenom, String pwd, String matricule, String type, String statut, String cin, String date) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.pwd = pwd;
        this.type = type;
        this.statut = statut;
        this.matricule = matricule;
        this.date = date;
        this.cin = cin;
    }

    public modeluser(int id, String nom, String prenom, String pwd, String type, String statut, String matricule) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.pwd = pwd;
        this.type = type;
        this.statut = statut;
        this.matricule = matricule;
    }

    public modeluser() {
    }

    protected modeluser(Parcel in) {
        id = in.readInt();
        nom = in.readString();
        prenom = in.readString();
        pwd = in.readString();
        type = in.readString();
        statut = in.readString();
        matricule = in.readString();
        cin = in.readString();
        date = in.readString();
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public String getDate() {
        return date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nom);
        parcel.writeString(prenom);
        parcel.writeString(pwd);
        parcel.writeString(type);
        parcel.writeString(statut);
        parcel.writeString(matricule);
        parcel.writeString(cin);
        parcel.writeString(date);
    }
}
