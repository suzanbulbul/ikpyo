package sample.contollers;

import static java.lang.Integer.parseInt;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import sample.Main;
import sample.entity.*;
import sample.util.DosyaYazma;

public class SatislarController implements Initializable {
    private DosyaYazma dosyaYazOku;
    @FXML
    private TableView personelListele;
    @FXML
    private Label toplamSatis;
    @FXML
    private Label aylikHedef;
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<Pazarlamaci, Long> tableTC;
    @FXML
    private TableColumn<Pazarlamaci, String> tableAdi;
    @FXML
    private TableColumn<Pazarlamaci, String> tableSoyadi;
    @FXML
    private TableColumn<Pazarlamaci, Long> tableToplamSatis;
    @FXML
    private TableColumn<Pazarlamaci, Long> tableAylikHedef;


    private ObservableList<Pazarlamaci> list = FXCollections.observableArrayList();


    int ts;
    int ah;


    private Main main = new Main();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.personelTabosuGuncelle();
    }

    public void geri(ActionEvent e) throws Exception {
        /*Anasayfa arayuzune geciliyor...*/
        this.getMain().goToPage("/sample/arayuzler/anasayfa.fxml");
        /*
         * pencerenin basliginin guncellenmesi gerek. static olarak olusturdugumuz stage penceremizin basligini kendi
         * olusturdugumuz setTitle metodu ile gunvelliyoruz
         * */
        this.getMain().setTitle("Anasayfa");
    }

    public void personelTabosuGuncelle() {
        //ilk once mevcut tablomuzda ve listemizde herhangi bir veri varsa onlari sifirliyoruz.
        this.getPersonelTable().getItems().clear();
        this.getList().clear();
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("pazarlamaci");
            int size = lines.size();//satir sayimizi aliyoruz.
            /*
             * Bu for dongusunde her bir satir icin islem gerceklesecektir. i degiskeni txt dosyamizdaki 1 satiri ifade ediyor.
             * */
            for (int i = 0; i < size; i++) {
                //satirdaki yaziyi arraylist degiskeninden string degiskenine aktariyoruz
                String str = lines.get(i);
                //satirin uzunlugunu aliyoruz.
                int strsize = str.length();
                /*
                 * Buradak degiskenler her bir satirdaki ad, soyad vb. bilgileri bir degiskene karakter karakter
                 * yazabilmemiz icin gereklidir
                 * */
                String ad = "";
                String soyad = "";
                String tckimlik = "";
                String maass = "";
                String depart = "";
                String isegiris = "";
                String fotoName = "";
                String aylikHedef = "";
                String toplamSatis = "";
                /*
                 * Bu for dongusunde bir satirdaki her bir karakter icin islem gerceklesecektir. her j degiskeni
                 * satirdaki 1 karakteri ifade ediyor.
                 * */
                for (int j = 0; j < strsize; j++) {
                    /*
                     * her bir sart bir ozelligi belirliyor
                     * satirimizda isim alani '!' simgesi ile baslayip '@' simgesi ile sona eriyor
                     * satirimizda soyisim alani '@' simgesi ile baslayip '%' simgesi ile sona eriyor
                     * satirimizda tckimlik alani '%' simgesi ile baslayip '&' simgesi ile sona eriyor
                     * satirimizda maas alani '&' simgesi ile baslayip '?' simgesi ile sona eriyor
                     * satirimizda departman alani '?' simgesi ile baslayip '*' simgesi ile sona eriyor
                     * satirimizda isegiristarihi alani '*' simgesi ile baslayip '_' simgesi ile sona eriyor
                     * satirimizda fotograf adi alani '_' simgesi ile baslayip '<' simgesi ile sona eriyor
                     * satirimizda toplam satis adi alani '<' simgesi ile baslayip '|' simgesi ile sona eriyor
                     * satirimizda aylik hedef adi alani '|' simgesi ile baslayip '>' simgesi ile sona eriyor
                     * bunlara gore de gerekli atama islemleri yapilarak personel nesnesinin olusmasi saglaniyor...
                     * */
                    if (str.charAt(j) == '!') {
                        j++;
                        while (str.charAt(j) != '@') {
                            ad += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '@') {
                        j++;
                        while (str.charAt(j) != '%') {
                            soyad += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '%') {
                        j++;
                        while (str.charAt(j) != '&') {
                            tckimlik += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '&') {
                        j++;
                        while (str.charAt(j) != '?') {
                            maass += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '?') {
                        j++;
                        while (str.charAt(j) != '*') {
                            depart += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '*') {
                        j++;
                        while (str.charAt(j) != '_') {
                            isegiris += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '_') {
                        j++;
                        while (str.charAt(j) != '<') {
                            fotoName += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '<') {
                        j++;
                        while (str.charAt(j) != '|') {
                            toplamSatis += str.charAt(j++);
                        }
                        ts += Integer.parseInt(toplamSatis);

                    }
                    if (str.charAt(j) == '|') {
                        j++;
                        while (str.charAt(j) != '>') {
                            aylikHedef += str.charAt(j++);
                        }

                        ah += Integer.parseInt(aylikHedef);
                    }
                }
                /*
                 * Bir satirin okunma islemi bitti ve personel nesnesi lusturularak tableview listesine
                 * ekleme islemi gerceklestirilecek
                 * */
                Pazarlamaci pazarlamaci = new Pazarlamaci(parseInt(aylikHedef), parseInt(toplamSatis));
                pazarlamaci.setAdi(ad);
                pazarlamaci.setSoyadi(soyad);
                pazarlamaci.setDepartman(depart);
                pazarlamaci.setMaas(parseInt(maass));
                pazarlamaci.setTCKimlik(Long.valueOf(tckimlik));
                pazarlamaci.setIseGiris(LocalDate.parse(isegiris));
                pazarlamaci.setFotoName(fotoName);
                this.getList().add(pazarlamaci);
            }
        } catch (Exception e) {
            //olasi bir hata durumunda karsilasilan hatayi konsol ekranina yazma islemi gerceklesecek
            e.printStackTrace();
        }
        /*
         * dosyadan okunarak nesneye donusturulen personel bilgilerinin tablomuza aktarma islemlerini gerceklestirelim
         * tablomuza liste degiskenini ekleyelim
         *  */
        this.getPersonelTable().setItems(this.getList());
        //Tablomuzun tableTC sutununa personel nesnemizdeki TCKimlik ozelligini aktariyoruz
        this.getTableTC().setCellValueFactory(new PropertyValueFactory<Pazarlamaci, Long>("TCKimlik"));
        //Tablomuzun tableAdi sutununa personel nesnemizdeki adi ozelligini aktariyoruz
        this.getTableAdi().setCellValueFactory(new PropertyValueFactory<Pazarlamaci, String>("adi"));
        //Tablomuzun tableSoyadi sutununa personel nesnemizdeki soyadi ozelligini aktariyoruz
        this.getTableSoyadi().setCellValueFactory(new PropertyValueFactory<Pazarlamaci, String>("soyadi"));
        //Tablomuzun tableDepartmani sutununa personel nesnemizdeki departman ozelligini aktariyoruz
        this.getTableToplamSatis().setCellValueFactory(new PropertyValueFactory<Pazarlamaci, Long>("toplamSatis"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableAylikHedef().setCellValueFactory(new PropertyValueFactory<Pazarlamaci, Long>("aylikHedef"));
        aylikHedef.setText(String.valueOf(ah));
        toplamSatis.setText(String.valueOf(ts));

    }

    public DosyaYazma getDosyaYazOku() {
        if (this.dosyaYazOku == null) {
            this.dosyaYazOku = new DosyaYazma();
        }
        return dosyaYazOku;
    }

    public TableView getPersonelTable() {
        return personelTable;
    }

    public TableColumn<Pazarlamaci, Long> getTableTC() {
        return tableTC;
    }

    public TableColumn<Pazarlamaci, String> getTableAdi() {
        return tableAdi;
    }

    public TableColumn<Pazarlamaci, String> getTableSoyadi() {
        return tableSoyadi;
    }

    public TableColumn<Pazarlamaci, Long> getTableToplamSatis() {
        return tableToplamSatis;
    }

    public TableColumn<Pazarlamaci, Long> getTableAylikHedef() {
        return tableAylikHedef;
    }

    public ObservableList<Pazarlamaci> getList() {
        return list;
    }

    public Main getMain() {
        return main;
    }


}
