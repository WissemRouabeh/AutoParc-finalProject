/*
 * *all code wrote by wissem rouabeh**
 */

package gafsa.rouabeh.wissem.autoparc.model;

public class modeldash {
    String titre,desc;
    int img;

    public modeldash() {
    }

    public modeldash(String titre, String desc, int img) {
        this.titre = titre;
        this.desc = desc;
        this.img = img;
    }

    public String getTitre() {
        return titre;
    }

    public String getDesc() {
        return desc;
    }

    public int getImg() {
        return img;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
