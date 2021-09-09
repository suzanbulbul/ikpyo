package sample.entity;

public class Teknisyen extends Personel {
    private String bolum;

    public Teknisyen() {
    }

    public Teknisyen(String bolum) {
        this.bolum = bolum;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }
}
