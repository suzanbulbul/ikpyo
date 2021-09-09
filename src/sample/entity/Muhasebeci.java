package sample.entity;

public class Muhasebeci extends Personel {
    private int ZimmetEdilenMiktar;
    private int odeyecegiMiktar;
    private int geriGetirilenTutar;

    public Muhasebeci() {
    }

    public Muhasebeci(int zimmetEdilenMiktar, int odeyecegiMiktar, int geriGetirilenTutar) {
        this.ZimmetEdilenMiktar = zimmetEdilenMiktar;
        this.odeyecegiMiktar = odeyecegiMiktar;
        this.geriGetirilenTutar = geriGetirilenTutar;
    }

    public int getZimmetEdilenMiktar() {
        return ZimmetEdilenMiktar;
    }

    public void setZimmetEdilenMiktar(int zimmetEdilenMiktar) {
        ZimmetEdilenMiktar = zimmetEdilenMiktar;
    }

    public int getOdeyecegiMiktar() {
        return odeyecegiMiktar;
    }

    public void setOdeyecegiMiktar(int odeyecegiMiktar) {
        this.odeyecegiMiktar = odeyecegiMiktar;
    }

    public int getGeriGetirilenTutar() {
        return geriGetirilenTutar;
    }

    public void setGeriGetirilenTutar(int geriGetirilenTutar) {
        this.geriGetirilenTutar = geriGetirilenTutar;
    }
}
