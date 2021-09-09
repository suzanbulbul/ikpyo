package sample.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class Personel {

    private String adi;
    private String soyadi;
    private Long TCKimlik;
    private LocalDate iseGiris;
    private String departman;
    private int maas;
    private String fotoName;


    public String getAdi() {
        if(this.adi == null)
            this.adi = "Adı Belirtilmemiş!";
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSoyadi() {
        if(this.soyadi == null)
            this.soyadi = "Soyadı Belirtilmemiş!";
        return soyadi;
    }

    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }

    public Long getTCKimlik() {
        if(this.TCKimlik == 0)
            this.TCKimlik = 1000000000L;
        return TCKimlik;
    }

    public void setTCKimlik(Long TCKimlik) {
        this.TCKimlik = TCKimlik;
    }

    public LocalDate getIseGiris() {
        return iseGiris;
    }

    public void setIseGiris(LocalDate iseGiris) {
        this.iseGiris = iseGiris;
    }

    public String getDepartman() {
        if(this.departman == null)
            this.departman = "Departman Belirtilmemiş!";
        return departman;
    }

    public void setDepartman(String departman) {
        this.departman = departman;
    }

    public int getMaas() {
        if(this.maas == 0)
            this.maas = 2350;
        return maas;
    }

    public void setMaas(int maas) {
        this.maas = maas;
    }

    public String getFotoName() {
        return fotoName;
    }

    public void setFotoName(String fotoName) {
        this.fotoName = fotoName;
    }

    @Override
    public String toString() {
        return "Personel{" +
                "adi='" + adi + '\'' +
                ", soyadi='" + soyadi + '\'' +
                ", TCKimlik=" + TCKimlik +
                ", iseGiris=" + iseGiris +
                ", departman='" + departman + '\'' +
                ", maas=" + maas +
                ", fotoName='" + fotoName + '\'' +
                '}';
    }
}
