package sample.entity;

public class Pazarlamaci extends Personel {
    private int aylikHedef;
    private int toplamSatis;

    public Pazarlamaci() {
    }

    public Pazarlamaci(int aylikHedef, int toplamSatis) {
        this.aylikHedef = aylikHedef;
        this.toplamSatis = toplamSatis;
    }

    public int getAylikHedef() {
        return aylikHedef;
    }

    public void setAylikHedef(int aylikHedef) {
        this.aylikHedef = aylikHedef;
    }

    public int getToplamSatis() {
        return toplamSatis;
    }

    public void setToplamSatis(int toplamSatis) {
        this.toplamSatis = toplamSatis;
    }
}
