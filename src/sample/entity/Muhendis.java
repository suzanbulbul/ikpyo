package sample.entity;


public class Muhendis extends Personel {
    private String uzmanlik;

    public Muhendis() {
    }

    public Muhendis(String uzmanlik) {
        this.uzmanlik = uzmanlik;
    }

    public String getUzmanlik() {
        return uzmanlik;
    }

    public void setUzmanlik(String uzmanlik) {
        this.uzmanlik = uzmanlik;
    }
}
