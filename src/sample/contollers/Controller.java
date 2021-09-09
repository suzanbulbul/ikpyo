package sample.contollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import sample.Main;

import java.io.IOException;

public class Controller {

    private Main main = new Main();


    public void girisCikis(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/girisCikisTakip.fxml");
        this.getMain().setTitle("Giriş Çıkış Takip");
    }
    public void personelListesi(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/personel.fxml");
        this.getMain().setTitle("Personel");
    }
    public void isciler(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/isci.fxml");
        this.getMain().setTitle("İşçi");
    }
    public void pazarlamacilar(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/pazarlamaci.fxml");
        this.getMain().setTitle("Pazarlamacı");
    }
    public void itElemani(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/ITElemani.fxml");
        this.getMain().setTitle("IT Elemanı");
    }
    public void muhasebeci(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/Muhasebeci.fxml");
        this.getMain().setTitle("Muhasebeci");
    }
    public void muhendis(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/Muhendis.fxml");
        this.getMain().setTitle("Mühendis");
    }
    public void satislar(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/satislar.fxml");
        this.getMain().setTitle("Satışlar");
    }
    public void uretim(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/uretilenler.fxml");
        this.getMain().setTitle("Üretilenler");
    }
    public void teknisyen(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/teknisyen.fxml");
        this.getMain().setTitle("Teknisyen");
    }
    public void teknikEleman(ActionEvent e) throws Exception {
        this.getMain().goToPage("/sample/arayuzler/TeknikEleman.fxml");
        this.getMain().setTitle("Teknik Eleman");
    }

    public Main getMain() {
        if(this.main == null)
            this.main = new Main();
        return main;
    }
}
