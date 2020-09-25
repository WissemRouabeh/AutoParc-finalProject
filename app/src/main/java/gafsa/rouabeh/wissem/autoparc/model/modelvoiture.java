package gafsa.rouabeh.wissem.autoparc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;

public class modelvoiture implements Parcelable {
    int id;
    String Matricule;
    String Modele;
    String NumChassis;
    int mark;
    Boolean Service;
    String date,marque,matricule,carburant,puissance,sit_act,kilometrage,consommation;


    public modelvoiture(int id, String date, String marque, String matricule, String carburant, String puissance, String sit_act,String kilometrage,String consommation,Boolean service) {
        this.id = id;
        this.date = date;
        this.marque = marque;
        this.matricule = matricule;
        this.carburant = carburant;
        this.kilometrage = kilometrage;
        this.consommation = consommation;
        this.puissance = puissance;
        this.sit_act = sit_act;
        this.Service = service;
    }

    public modelvoiture(int id, String matricule, String modele, String numChassis) {
        Matricule = matricule;
        Modele = modele;
        NumChassis = numChassis;
    }


    public modelvoiture(int id,String matricule, String modele, String numChassis, Boolean service) {
        this.id=id;
        Matricule = matricule;
        Modele = modele;
        NumChassis = numChassis;
        Service = service;
    }

    public modelvoiture(int id, String matricule, String numChassis, int mark, Boolean service) {
        this.id = id;
        Matricule = matricule;
        NumChassis = numChassis;
        this.mark = mark;
        Service = service;
    }

    protected modelvoiture(Parcel in) {
        id = in.readInt();
        Matricule = in.readString();
        Modele = in.readString();
        NumChassis = in.readString();
        mark = in.readInt();
        byte tmpService = in.readByte();
        Service = tmpService == 0 ? null : tmpService == 1;
        date = in.readString();
        marque = in.readString();
        matricule = in.readString();
        carburant = in.readString();
        kilometrage = in.readString();
        consommation = in.readString();
        puissance = in.readString();
        sit_act = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Matricule);
        dest.writeString(Modele);
        dest.writeString(NumChassis);
        dest.writeInt(mark);
        dest.writeByte((byte) (Service == null ? 0 : Service ? 1 : 2));
        dest.writeString(date);
        dest.writeString(marque);
        dest.writeString(matricule);
        dest.writeString(carburant);
        dest.writeString(kilometrage);
        dest.writeString(consommation);
        dest.writeString(puissance);
        dest.writeString(sit_act);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<modelvoiture> CREATOR = new Creator<modelvoiture>() {
        @Override
        public modelvoiture createFromParcel(Parcel in) {
            return new modelvoiture(in);
        }

        @Override
        public modelvoiture[] newArray(int size) {
            return new modelvoiture[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        Matricule = matricule;
    }

    public String getModele() {
        return Modele;
    }

    public void setModele(String modele) {
        Modele = modele;
    }

    public String getNumChassis() {
        return NumChassis;
    }

    public void setNumChassis(String numChassis) {
        NumChassis = numChassis;
    }

    public Boolean getService() {
        return Service;
    }

    public void setService(Boolean service) {
        Service = service;
    }

    public String getConsommation() {
        return consommation;
    }

    public void setConsommation(String consommation) {
        this.consommation = consommation;
    }

    public String getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(String kilometrage) {
        this.kilometrage = kilometrage;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getCarburant() {
        return carburant;
    }

    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }

    public String getPuissance() {
        return puissance;
    }

    public void setPuissance(String puissance) {
        this.puissance = puissance;
    }

    public String getSit_act() {
        return sit_act;
    }

    public void setSit_act(String sit_act) {
        this.sit_act = sit_act;
    }
}
