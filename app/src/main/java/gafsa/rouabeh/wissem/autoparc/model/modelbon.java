/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.model;

public class modelbon {
    int num_bon;
    String qt_carburant;
    String Prix;
    String kilo_ancien;
    String kilo_cons;
    String Date_bon;
    String pourcentage_cons;
    String id_voiture;
    String Serie;
    String Marque;
    String statut;

    public modelbon(int num_bon, String qt_carburant, String prix, String kilo_ancien, String kilo_cons, String date_bon, String pourcentage_cons, String id_voiture, String statut) {
        this.num_bon = num_bon;
        this.qt_carburant = qt_carburant;
        Prix = prix;
        this.kilo_ancien = kilo_ancien;
        this.kilo_cons = kilo_cons;
        Date_bon = date_bon;
        this.pourcentage_cons = pourcentage_cons;
        this.id_voiture = id_voiture;
        this.statut = statut;
    }

    public modelbon(int num_bon, String qt_carburant, String prix, String kilo_ancien, String kilo_cons, String date_bon, String pourcentage_cons, String id_voiture, String serie, String marque, String statut) {
        this.num_bon = num_bon;
        this.qt_carburant = qt_carburant;
        Prix = prix;
        this.kilo_ancien = kilo_ancien;
        this.kilo_cons = kilo_cons;
        Date_bon = date_bon;
        this.pourcentage_cons = pourcentage_cons;
        this.id_voiture = id_voiture;
        Serie = serie;
        Marque = marque;
        this.statut = statut;
    }

    public String getSerie() {
        return Serie;
    }

    public String getMarque() {
        return Marque;
    }

    public int getNum_bon() {
        return num_bon;
    }

    public String getQt_carburant() {
        return qt_carburant;
    }

    public String getPrix() {
        return Prix;
    }

    public String getKilo_ancien() {
        return kilo_ancien;
    }

    public String getKilo_cons() {
        return kilo_cons;
    }

    public String getDate_bon() {
        return Date_bon;
    }

    public String getPourcentage_cons() {
        return pourcentage_cons;
    }

    public String getId_voiture() {
        return id_voiture;
    }

    public String getStatut() {
        return statut;
    }
}
