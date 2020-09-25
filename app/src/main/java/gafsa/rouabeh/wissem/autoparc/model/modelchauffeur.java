package gafsa.rouabeh.wissem.autoparc.model;


import android.os.Parcel;
import android.os.Parcelable;

public class modelchauffeur implements Parcelable {

    int id;
    int matricule;
    String nom,prenom;
    String service;
    String cin;
    String daterecrute;

    public modelchauffeur(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }
    public modelchauffeur(int id,int matricule, String nom, String prenom, String service) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.service = service;
    }

    public modelchauffeur(int id, int matricule, String nom, String prenom, String service, String cin, String daterecrute) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.service = service;
        this.cin = cin;
        this.daterecrute = daterecrute;
    }

    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setDaterecrute(String daterecrute) {
        this.daterecrute = daterecrute;
    }

    public String getCin() {
        return cin;
    }

    public String getDaterecrute() {
        return daterecrute;
    }

    protected modelchauffeur(Parcel in) {
        id = in.readInt();
        matricule = in.readInt();
        nom = in.readString();
        prenom = in.readString();
        service = in.readString();
        cin = in.readString();
        daterecrute = in.readString();
    }

    public static final Creator<modelchauffeur> CREATOR = new Creator<modelchauffeur>() {
        @Override
        public modelchauffeur createFromParcel(Parcel in) {
            return new modelchauffeur(in);
        }

        @Override
        public modelchauffeur[] newArray(int size) {
            return new modelchauffeur[size];
        }
    };

    public int getId() {
        return id;
    }
    public int getMatricule() {
        return matricule;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getService() {
        return service;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
        parcel.writeString(nom);
        parcel.writeString(prenom);
        parcel.writeString(service);
        parcel.writeString(cin);
        parcel.writeString(daterecrute);
    }
}

