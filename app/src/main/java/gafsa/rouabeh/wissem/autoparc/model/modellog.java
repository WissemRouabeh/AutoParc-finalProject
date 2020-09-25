/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.model;

public class modellog {
    int id;
    int id_user;
    String type;
    String ancien;
    String nouveau;
    String action;
    String nomprenom_user;
    String date;

    public modellog(int id, int id_user, String type, String ancien, String nouveau, String action, String nomprenom_user, String date) {
        this.id = id;
        this.id_user = id_user;
        this.type = type;
        this.ancien = ancien;
        this.nouveau = nouveau;
        this.action = action;
        this.nomprenom_user = nomprenom_user;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getId_user() {
        return id_user;
    }

    public String getType() {
        return type;
    }

    public String getAncien() {
        return ancien;
    }

    public String getNouveau() {
        return nouveau;
    }

    public String getAction() {
        return action;
    }

    public String getNomprenom_user() {
        return nomprenom_user;
    }

    public String getDate() {
        return date;
    }
}
