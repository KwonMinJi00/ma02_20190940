package ddwu.mobile.finalproject.ma02_20190940;

import java.io.Serializable;

public class DetailDTO implements Serializable {

    private int _id;
    private String cono;
    private String wsp;
    private String wsu;
    private String wa;
    private String ww;
    private String soil;
    private String frtlzr;
    private String prpgt;
    private String grwt;
    private String winterLwTp;

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getFrtlzr() {
        return frtlzr;
    }

    public void setFrtlzr(String frtlzr) {
        this.frtlzr = frtlzr;
    }

    public String getPrpgt() {
        return prpgt;
    }

    public void setPrpgt(String prpgt) {
        this.prpgt = prpgt;
    }

    public String getGrwt() {
        return grwt;
    }

    public void setGrwt(String grwt) {
        this.grwt = grwt;
    }

    public String getWinterLwTp() {
        return winterLwTp;
    }

    public void setWinterLwTp(String winterLwTp) {
        this.winterLwTp = winterLwTp;
    }

    public DetailDTO() {};

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCono() {
        return cono;
    }

    public void setCono(String cono) {
        this.cono = cono;
    }

    public String getWsp() {
        return wsp;
    }

    public void setWsp(String wsp) {
        this.wsp = wsp;
    }

    public String getWsu() {
        return wsu;
    }

    public void setWsu(String wsu) {
        this.wsu = wsu;
    }

    public String getWa() {
        return wa;
    }

    public void setWa(String wa) {
        this.wa = wa;
    }

    public String getWw() {
        return ww;
    }

    public void setWw(String ww) {
        this.ww = ww;
    }
}
