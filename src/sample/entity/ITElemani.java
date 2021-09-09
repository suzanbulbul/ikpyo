package sample.entity;

public class ITElemani  extends Personel{

    private String uzmanlik;

    public ITElemani() {
    }

    public ITElemani(String uzmalik) {
        this.uzmanlik = uzmalik;
    }

    public String getUzmanlik() {
        return uzmanlik;
    }

    public void setUzmanlik(String uzmanlik) {
        this.uzmanlik = uzmanlik;
    }
}
