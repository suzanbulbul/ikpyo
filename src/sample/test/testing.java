package sample.test;

import org.junit.Assert;
import org.junit.Test;
import sample.contollers.*;
import sample.entity.*;

import java.time.LocalDate;


public class testing {

    private Isci isci = new Isci();
    private IsciController ic = new IsciController();

    @Test
    public void isciOlusturTest(){
        isci.setAdi("Deneme Adi");
        isci.setSoyadi("Deneme Soyadi");
        isci.setTCKimlik(99966685858L);
        isci.setDepartman("Isci");
        isci.setIseGiris(LocalDate.now());
        isci.setMaas(222);
        ic.getAylikHedef().setText("12");
        ic.getToplamUretim().setText("1");
        ic.personelOlustur(isci);
    }

}
