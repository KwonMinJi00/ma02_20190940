package ddwu.mobile.finalproject.ma02_20190940;

public class PlantDTO {
    private int _id;
    private String name;
    private String cono;

    public PlantDTO() {};

    public PlantDTO (String name, String cono) {
        this.name = name;
        this.cono = cono;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCono() {
        return cono;
    }

    public void setCono(String cono) {
        this.cono = cono;
    }

    public String toString() {
        return "식물 이름: " + name;
    }
}
