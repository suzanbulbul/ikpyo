package sample.entity;

public class Isci extends Personel{
    private int aylikHedef;
    private int ToplamUretim;

    public Isci() {
    }

    public Isci(int aylikHedef, int ToplamUretim){
        this.aylikHedef = aylikHedef;
        this.ToplamUretim = ToplamUretim;
    }

    public int getAylikHedef() {
        return aylikHedef;
    }

    public void setAylikHedef(int aylikHedef) {
        this.aylikHedef = aylikHedef;
    }

    public int getToplamUretim() {
        return ToplamUretim;
    }

    public void setToplamUretim(int toplamUretim) {
        ToplamUretim = toplamUretim;
    }
}
