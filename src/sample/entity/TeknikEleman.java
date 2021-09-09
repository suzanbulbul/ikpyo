package sample.entity;

public class TeknikEleman extends Personel {
    private int tecrube;
    private String Alan;

    public TeknikEleman() {
    }

    public TeknikEleman(int tecrube, String alan) {
        this.tecrube = tecrube;
        Alan = alan;
    }

    public int getTecrube() {
        return tecrube;
    }

    public void setTecrube(int tecrube) {
        this.tecrube = tecrube;
    }

    public String getAlan() {
        return Alan;
    }

    public void setAlan(String alan) {
        Alan = alan;
    }

    public void tecrubeGuncelle(int tecrube){
        this.setTecrube(tecrube);;
    }
}
