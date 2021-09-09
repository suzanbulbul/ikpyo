package sample.contollers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.*;
import sample.entity.*;
import sample.util.DosyaYazma;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Integer.parseInt;

public class PersonelContoller implements Initializable {
    private DosyaYazma dosyaYazOku;//dosya yazma/okuma nesnemiz.
    @FXML
    private TextField tcKimlikNo;
    @FXML
    private TextField adi;
    @FXML
    private TextField soyadi;
    @FXML
    private DatePicker iseGirisTarihi;
    @FXML
    private TextField maas;
    @FXML
    private TextField departman;
    @FXML
    private ListView iseGirisSaatleri;
    @FXML
    private ListView istenCikisSaatleri;
    @FXML
    private Button kisiyiGuncelle_yeniKisiEkle;
    @FXML
    private Button istenCikar;
    @FXML
    private Button formuTemizle;
    @FXML
    private ImageView fotograf;
    /*tableview attribute*/
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<Personel, Long> tableTC;
    @FXML
    private TableColumn<Personel, String> tableAdi;
    @FXML
    private TableColumn<Personel, String> tableSoyadi;
    @FXML
    private TableColumn<Personel, String> tableDepartmani;
    @FXML
    private TableColumn<Personel, Date> tableIsegiris;
    /*end tableview attribute*/
    @FXML
    /*
      secilen fotografin adini kullaniciya arayuzde kullaniciya gosterecek label. Ayrica dosyanin secilip secilmedigini
      kontrol edebilmemiz icin bize yardimci olacaktir.
    */
    private Label FotoName;
    @FXML
    private Label hata;

    // kullanicinin personel icin sectigi fotografin yolunu barindiracak string degiskeni
    private String fotoUrl;
    // tableview uzerinde sectigimiz personeli temsil eder.
    private Personel selectedPersonel;

    // bu main nesnemiz, pencereler arasi gecislerimizde pencere title'ını degistirmemiz icin gereklidir.
    private Main main = new Main();

    // tableview icin bilgilerin tutuldugu liste
    private ObservableList<Personel> list = FXCollections.observableArrayList();

    private String name;

    // polymorphism
    private Personel isci = new Isci();
    private Personel ITElemani = new ITElemani();
    private Personel muhasebeci;
    private Personel muhendis;
    private Personel pazarlamaci;
    private Personel teknikEleman;
    private Personel teknisyen;



    /*Metodlar*/


    // Personel Listesi sayfasina ilk girdigimizde arayüzümüzdeki birimlere ilk deger atamasini gerceklestirecek kod blogu
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.personelTabosuGuncelle();
        this.getIseGirisTarihi().setValue(LocalDate.now());
        this.getFotograf().setImage(new Image("/images/unnamed.jpg"));
        /*
         * form bos oldugu zaman form temizle ve isten cikar butonları gereksiz olacagindan bu butonlari default olarak
         * gorunmez yapiyoruz...
         * */
        this.getFormuTemizle().setVisible(false);
        this.getIstenCikar().setVisible(false);
    }

    // personelin fotografinin secilmesi icin fotograf sec butonuna basilince gerceklesecek islemlerin oldugu blok

    public void fotografSec(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        Stage secondaryStage = new Stage();
        //Secilecek dosyanın turunu belirleme islemi saglayan kod parcalari.
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        fileChooser.getExtensionFilters().add(fileExtensions);
        //dosya secim ekraninin acilip secilen dosyanin file nesnesine atanmasi islemi gerceklesiyor
        File selectedFile = fileChooser.showOpenDialog(secondaryStage);
        //secilen dosyanın adini diger metodlarda ulasabilmek icin global label degiskenine ve dosya yolunu da string degiskenine atiyoruz.
        this.getFotoName().setText(selectedFile.getName());
        this.setFotoUrl(selectedFile.getPath());
    }

    // personel bilgilerinin yer aldigi tableview'e bilgilerin yuklenmesini gerceklestirecek kod blogu
    public void personelTabosuGuncelle() {
        //ilk once mevcut tablomuzda ve listemizde herhangi bir veri varsa onlari sifirliyoruz.
        this.getPersonelTable().getItems().clear();
        this.getList().clear();
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("personel");
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
                     * satirimizda fotorgraf adi alani '_' simgesi ile baslayip '<' simgesi ile sona eriyor
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
                }
                /*
                 * Bir satirin okunma islemi bitti ve personel nesnesi lusturularak tableview listesine
                 * ekleme islemi gerceklestirilecek
                 * */
                Personel p = new Personel();
                p.setAdi(ad);
                p.setSoyadi(soyad);
                p.setDepartman(depart);
                p.setMaas(parseInt(maass));
                p.setTCKimlik(Long.valueOf(tckimlik));
                p.setIseGiris(LocalDate.parse(isegiris));
                p.setFotoName(fotoName);
                this.getList().add(p);
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
        this.getTableTC().setCellValueFactory(new PropertyValueFactory<Personel, Long>("TCKimlik"));
        //Tablomuzun tableAdi sutununa personel nesnemizdeki adi ozelligini aktariyoruz
        this.getTableAdi().setCellValueFactory(new PropertyValueFactory<Personel, String>("adi"));
        //Tablomuzun tableSoyadi sutununa personel nesnemizdeki soyadi ozelligini aktariyoruz
        this.getTableSoyadi().setCellValueFactory(new PropertyValueFactory<Personel, String>("soyadi"));
        //Tablomuzun tableDepartmani sutununa personel nesnemizdeki departman ozelligini aktariyoruz
        this.getTableDepartmani().setCellValueFactory(new PropertyValueFactory<Personel, String>("departman"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableIsegiris().setCellValueFactory(new PropertyValueFactory<Personel, Date>("iseGiris"));
    }

    // Geri butonunun tıklandığında bir önceki sayfaya geçmesini sağlayacak kod parçası.

    public void geri(ActionEvent e) throws Exception {
        /*Anasayfa arayuzune geciliyor...*/
        this.getMain().goToPage("/sample/arayuzler/anasayfa.fxml");
        /*
         * pencerenin basliginin guncellenmesi gerek. static olarak olusturdugumuz stage penceremizin basligini kendi
         * olusturdugumuz setTitle metodu ile gunvelliyoruz
         * */
        this.getMain().setTitle("Anasayfa");
    }

    // tableview'da bir personele tiklandiginda personel bigilerinin form uzerine aktarilmasini saglayacak kod blogu

    public void personelDetay(MouseEvent e) {

        // tablomuzda secilen verinin alinmiyor
        TableView.TableViewSelectionModel<Personel> selectionModel = this.getPersonelTable().getSelectionModel();

        // tablodam secilecek maks item sayisinin 1 olmasi gerektigini belirliyoruz
        selectionModel.setSelectionMode(SelectionMode.SINGLE);


        /*
         * tablodan secilen itemin bir personel nesnesi oldugu biliniyor ve personel nesnesine cast islemi gercekleserek
         * diger metodlardan da ulasilabilrmasi adina global selectedPersonel nesnesine aktariliyor.
         * */
        this.setSelectedPersonel((Personel) selectionModel.getSelectedItem());

        this.getIseGirisSaatleri().getItems().clear();
        this.getIstenCikisSaatleri().getItems().clear();

        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("personelGiris");
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
                String tckimlik = "";
                String girisTarihi = "";
                String girisSaati = "";
                /*
                 * Bu for dongusunde bir satirdaki her bir karakter icin islem gerceklesecektir. her j degiskeni
                 * satirdaki 1 karakteri ifade ediyor.
                 * */
                for (int j = 0; j < strsize; j++) {
                    /*
                     * her bir sart bir ozelligi belirliyor
                     * satirimizda tcKimlik alani '!' simgesi ile baslayip '@' simgesi ile sona eriyor
                     * satirimizda girisTarihi alani '@' simgesi ile baslayip '&' simgesi ile sona eriyor
                     * satirimizda girisSaati alani '&' simgesi ile baslayip '.' simgesi ile sona eriyor
                     * bunlara gore de gerekli atama islemleri yapilarak personel nesnesinin olusmasi saglaniyor...
                     * */
                    if (str.charAt(j) == '!') {
                        j++;
                        while (str.charAt(j) != '@') {
                            tckimlik += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '@') {
                        j++;
                        while (str.charAt(j) != '&') {
                            girisTarihi += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '&') {
                        j++;
                        while (str.charAt(j) != '.') {
                            girisSaati += str.charAt(j++);
                        }
                    }
                }

                if (this.getSelectedPersonel().getTCKimlik().toString().equals(tckimlik)) {
                    this.getIseGirisSaatleri().getItems().add(girisTarihi + " --- " + girisSaati);
                }
            }

            lines = this.getDosyaYazOku().dosyadanOku("personelCikis");
            size = lines.size();//satir sayimizi aliyoruz.
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
                String tckimlik = "";
                String girisTarihi = "";
                String girisSaati = "";
                /*
                 * Bu for dongusunde bir satirdaki her bir karakter icin islem gerceklesecektir. her j degiskeni
                 * satirdaki 1 karakteri ifade ediyor.
                 * */
                for (int j = 0; j < strsize; j++) {
                    /*
                     * her bir sart bir ozelligi belirliyor
                     * satirimizda tcKimlik alani '!' simgesi ile baslayip '@' simgesi ile sona eriyor
                     * satirimizda girisTarihi alani '@' simgesi ile baslayip '&' simgesi ile sona eriyor
                     * satirimizda girisSaati alani '&' simgesi ile baslayip '.' simgesi ile sona eriyor
                     * bunlara gore de gerekli atama islemleri yapilarak personel nesnesinin olusmasi saglaniyor...
                     * */
                    if (str.charAt(j) == '!') {
                        j++;
                        while (str.charAt(j) != '@') {
                            tckimlik += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '@') {
                        j++;
                        while (str.charAt(j) != '&') {
                            girisTarihi += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '&') {
                        j++;
                        while (str.charAt(j) != '.') {
                            girisSaati += str.charAt(j++);
                        }
                    }
                }

                if (this.getSelectedPersonel().getTCKimlik().toString().equals(tckimlik)) {
                    this.getIstenCikisSaatleri().getItems().add(girisTarihi + " --- " + girisSaati);
                }
            }

        } catch (Exception ex) {
            //olasi bir hata durumunda karsilasilan hatayi konsol ekranina yazma islemi gerceklesecek
            ex.printStackTrace();
        }

        // form ozelliklerine nesne ozellikleri aktarilarak formlar dolduruluyor...
        this.getTcKimlikNo().setText(this.getSelectedPersonel().getTCKimlik().toString());
        this.getAdi().setText(this.getSelectedPersonel().getAdi());
        this.getSoyadi().setText(this.getSelectedPersonel().getSoyadi());
        this.getDepartman().setText(this.getSelectedPersonel().getDepartman());
        this.getMaas().setText(String.valueOf(this.getSelectedPersonel().getMaas()));
        this.getIseGirisTarihi().setValue(this.getSelectedPersonel().getIseGiris());
        File file = new File(Paths.get("").toAbsolutePath().toString() + "\\src\\images\\" + this.selectedPersonel.getFotoName());
        try {
            this.getFotograf().setImage(new Image(file.toURI().toURL().toString()));
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }

        /*
         * default olarak gorunmez yaptigimiz butonlarımizi gorunur hale getiriyoruz ve daha onceform bos oldugu icin
         * yeni eleman ekle yazan butonumuz personel guncelle olarak guncelleniyor.
         * */
        this.getFormuTemizle().setVisible(true);
        this.getIstenCikar().setVisible(true);
        this.getKisiyiGuncelle_yeniKisiEkle().setText("Personel Güncelle");
    }

    /*Yeni bir personel eklemek istedigimizde Yeni personel ekle butonuna bastigimizda veya var olan personelin
      bilgilerini guncellemek istedigimizde persoel guncelle butonuna basıldiginda gerceklesecek islemlerin
      bulundugu kod blogu
    */

    public void personelBilgiGuncelleEkle(ActionEvent e) {
        if (this.getTcKimlikNo().getText().equals("")) {
            this.getHata().setText("T.C. Kimlik Numarası alanı boş olamaz!");
        } else {
            try {
                Long l = Long.valueOf(this.getTcKimlikNo().getText());
                if (l.longValue() < 10000000000L || l.longValue() > 99999999998L) {
                    this.getHata().setText("T.C. Kimlik Numarası 11 Haneli Olmalı!");
                } else {
                    if (l % 2 != 0) {
                        this.getHata().setText("T.C. Kimlik Numarası tek olamaz!");
                    } else {
                        if (this.getAdi().getText().equals("")) {
                            this.getHata().setText("İsim alanı boş olamaz!");
                        } else {
                            if (this.getSoyadi().getText().equals("")) {
                                this.getHata().setText("Soyisim alanı boş olamaz!");
                            } else {
                                if (this.getMaas().getText().equals("")) {
                                    this.getHata().setText("Maaş alanı boş olamaz!");
                                } else {
                                    try {
                                        parseInt(this.getMaas().getText());
                                        if (this.getDepartman().getText().equals("")) {
                                            this.getHata().setText("Departman alanı boş olamaz!");
                                        } else {
                                            if (this.getKisiyiGuncelle_yeniKisiEkle().getText().equals("Yeni Personel Ekle")) {
                                                /*
                                                 * her personel kategorisine ozel personel olusturucu metodu olusturulacaktır. ve her calisan kendi personel
                                                 * olusturucusuyla dosyaya yazilacaktir.
                                                 * */
                                                if (this.getDepartman().getText().equals("Isci")) {
                                                    this.setIsci(new Isci(100, 0));
                                                    this.getIsci().setAdi(this.getAdi().getText());
                                                    this.getIsci().setSoyadi(this.getSoyadi().getText());
                                                    this.getIsci().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getIsci().setMaas(parseInt(this.getMaas().getText()));
                                                    this.getIsci().setDepartman(this.getDepartman().getText());
                                                    this.getIsci().setFotoName(this.getFotoName().getText());
                                                    this.getIsci().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Isci) this.getIsci());
                                                } else if (this.getDepartman().getText().equals("IT Elemani")) {
                                                    this.setITElemani(new ITElemani(""));
                                                    this.getITElemani().setAdi(this.getAdi().getText());
                                                    this.getITElemani().setSoyadi(this.getSoyadi().getText());
                                                    this.getITElemani().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getITElemani().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getITElemani().setDepartman(this.getDepartman().getText());
                                                    this.getITElemani().setFotoName(this.getFotoName().getText());
                                                    this.getITElemani().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((ITElemani) this.getITElemani());
                                                } else if (this.getDepartman().getText().equals("Muhasebeci")) {
                                                    this.setMuhasebeci(new Muhasebeci(0,0,0));
                                                    this.getMuhasebeci().setAdi(this.getAdi().getText());
                                                    this.getMuhasebeci().setSoyadi(this.getSoyadi().getText());
                                                    this.getMuhasebeci().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getMuhasebeci().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getMuhasebeci().setDepartman(this.getDepartman().getText());
                                                    this.getMuhasebeci().setFotoName(this.getFotoName().getText());
                                                    this.getMuhasebeci().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Muhasebeci) this.getMuhasebeci());
                                                } else if (this.getDepartman().getText().equals("Muhendis")) {
                                                    this.setMuhendis(new Muhendis());
                                                    this.getMuhendis().setAdi(this.getAdi().getText());
                                                    this.getMuhendis().setSoyadi(this.getSoyadi().getText());
                                                    this.getMuhendis().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getMuhendis().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getMuhendis().setDepartman(this.getDepartman().getText());
                                                    this.getMuhendis().setFotoName(this.getFotoName().getText());
                                                    this.getMuhendis().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Muhendis) this.getMuhendis());
                                                } else if (this.getDepartman().getText().equals("Pazarlamaci")) {
                                                    this.setPazarlamaci(new Pazarlamaci());
                                                    this.getPazarlamaci().setAdi(this.getAdi().getText());
                                                    this.getPazarlamaci().setSoyadi(this.getSoyadi().getText());
                                                    this.getPazarlamaci().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getPazarlamaci().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getPazarlamaci().setDepartman(this.getDepartman().getText());
                                                    this.getPazarlamaci().setFotoName(this.getFotoName().getText());
                                                    this.getPazarlamaci().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Pazarlamaci) this.getPazarlamaci());
                                                } else if (this.getDepartman().getText().equals("Teknisyen")) {
                                                    this.setTeknisyen(new Teknisyen());
                                                    this.getTeknisyen().setAdi(this.getAdi().getText());
                                                    this.getTeknisyen().setSoyadi(this.getSoyadi().getText());
                                                    this.getTeknisyen().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getTeknisyen().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getTeknisyen().setDepartman(this.getDepartman().getText());
                                                    this.getTeknisyen().setFotoName(this.getFotoName().getText());
                                                    this.getTeknisyen().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Teknisyen) this.getTeknisyen());
                                                } else if (this.getDepartman().getText().equals("Teknik Eleman")) {
                                                    this.setTeknikEleman(new TeknikEleman());
                                                    this.getTeknikEleman().setAdi(this.getAdi().getText());
                                                    this.getTeknikEleman().setSoyadi(this.getSoyadi().getText());
                                                    this.getTeknikEleman().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getTeknikEleman().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getTeknikEleman().setDepartman(this.getDepartman().getText());
                                                    this.getTeknikEleman().setFotoName(this.getFotoName().getText());
                                                    this.getTeknikEleman().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((TeknikEleman) this.getTeknikEleman());
                                                } else {
                                                    this.personelOlustur();//personel sinifi belirlenemeyen bir sinifsa default personel dosyasina kayit edilecek
                                                }
                                            } else {
                                                /*
                                                 * her personel kategorisine ozel personel guncelleme metodu olusturulacaktır. ve her calisan kendi personel
                                                 * guncelleme metoduyla dosyada guncellenecektir
                                                 * */
                                                if (this.getDepartman().getText().equals("Isci")) {
                                                    this.getHata().setText("Bu personelin bilgisini buradan guncelleyemezsiniz.");
                                                } else if (this.getDepartman().getText().equals("IT Elemani")) {
                                                    this.getHata().setText("Bu personelin bilgisini buradan guncelleyemezsiniz.");
                                                } else if (this.getDepartman().getText().equals("Muhasebeci")) {
                                                    this.getHata().setText("Bu personelin bilgisini buradan guncelleyemezsiniz.");
                                                } else if (this.getDepartman().getText().equals("Muhendis")) {
                                                    this.getHata().setText("Bu personelin bilgisini buradan guncelleyemezsiniz.");
                                                } else if (this.getDepartman().getText().equals("Pazarlamaci")) {
                                                    this.getHata().setText("Bu personelin bilgisini buradan guncelleyemezsiniz.");
                                                } else if (this.getDepartman().getText().equals("Teknisyen")) {
                                                    this.getHata().setText("Bu personelin bilgisini buradan guncelleyemezsiniz.");
                                                } else if (this.getDepartman().getText().equals("Teknik Eleman")) {
                                                    this.getHata().setText("Bu personelin bilgisini buradan guncelleyemezsiniz.");
                                                } else {
                                                    this.personelGuncelle();//personel sinifi belirlenemeyen bir sinifsa default personel dosyasindan guncellenecek
                                                }
                                            }
                                        }
                                    } catch (Exception ex) {
                                        this.getHata().setText("Maas alanına numerik ifadeler girin!");
                                        System.out.println(ex.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                this.getHata().setText("T.C. Kimlik alanına numerik ifadeler girin!");
                System.out.println(ex.getMessage());
            }
        }
    }

    // Yeni personel ekleme islemleri
    public void personelOlustur() {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (this.getFotoName().getText().equals("")) {
                string = "!" + this.getAdi().getText() + "@" + this.getSoyadi().getText() + "%" + this.getTcKimlikNo().getText()
                        + "&" + this.getMaas().getText() + "?" + this.getDepartman().getText() + "*" + this.getIseGirisTarihi().getValue() + "_unnamed.jpg<";
            } else {
                this.setName(UUID.randomUUID().toString());
                string = "!" + this.getAdi().getText() + "@" + this.getSoyadi().getText() + "%" + this.getTcKimlikNo().getText()
                        + "&" + this.getMaas().getText() + "?" + this.getDepartman().getText() + "*" + this.getIseGirisTarihi().getValue() + "_" + this.getName() + ".jpg<";
                File source = new File(this.getFotoUrl());
                File dest = new File(Paths.get("").toAbsolutePath().toString() + "\\src\\images\\" + this.getName() + ".jpg");
                try {
                    Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.personelTabosuGuncelle();
            this.formTemizle();
            this.getHata().setText("");
        }
    }

    public void personelOlustur(Isci isci) {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (isci.getFotoName().equals("")) {
                isci.setFotoName("unnamed.jpg");
                string = "!" + isci.getAdi() + "@" + isci.getSoyadi() + "%" + isci.getTCKimlik().toString()
                        + "&" + isci.getMaas() + "?" + isci.getDepartman() + "*" + isci.getIseGiris().toString()
                        + "_" + isci.getFotoName() + "<" + isci.getToplamUretim() + "|" + isci.getAylikHedef() + ">";
            } else {
                isci.setFotoName(UUID.randomUUID().toString());
                string = "!" + isci.getAdi() + "@" + isci.getSoyadi() + "%" + isci.getTCKimlik().toString()
                        + "&" + isci.getMaas() + "?" + isci.getDepartman() + "*" + isci.getIseGiris().toString() + "_"
                        + isci.getFotoName() + ".jpg<" + isci.getToplamUretim() + "|" + isci.getAylikHedef() + ">";
                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), this.getName());
                this.getMain();
            }

            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
                this.getDosyaYazOku().dosyayaYaz("isci");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.personelTabosuGuncelle();
            this.formTemizle();
            this.getHata().setText("");

        }
    }

    public void personelOlustur(ITElemani itElemani) {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (itElemani.getFotoName().equals("")) {
                itElemani.setFotoName("unnamed.jpg");
                string = "!" + itElemani.getAdi() + "@" + itElemani.getSoyadi() + "%" + itElemani.getTCKimlik().toString()
                        + "&" + itElemani.getMaas() + "?" + itElemani.getDepartman() + "*" + itElemani.getIseGiris().toString()
                        + "_" + itElemani.getFotoName() + "<" + itElemani.getUzmanlik() + "|";
            } else {
                itElemani.setFotoName(UUID.randomUUID().toString());
                string = "!" + itElemani.getAdi() + "@" + itElemani.getSoyadi() + "%" + itElemani.getTCKimlik().toString()
                        + "&" + itElemani.getMaas() + "?" + itElemani.getDepartman() + "*" + itElemani.getIseGiris().toString() + "_"
                        + itElemani.getFotoName() + ".jpg<" + itElemani.getUzmanlik() + "|";
                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), itElemani.getFotoName());
            }

            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
                this.getDosyaYazOku().dosyayaYaz("itelemani");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.personelTabosuGuncelle();
            this.formTemizle();
            this.getHata().setText("");
        }
    }

    public void personelOlustur(Muhasebeci muhasebeci) {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (muhasebeci.getFotoName().equals("")) {
                muhasebeci.setFotoName("unnamed.jpg");
                string = "!" + muhasebeci.getAdi() + "@" + muhasebeci.getSoyadi() + "%" + muhasebeci.getTCKimlik().toString()
                        + "&" + muhasebeci.getMaas() + "?" + muhasebeci.getDepartman() + "*" + muhasebeci.getIseGiris().toString()
                        + "_" + muhasebeci.getFotoName() + "<" + muhasebeci.getZimmetEdilenMiktar() + "|" + muhasebeci.getOdeyecegiMiktar() + "(" + muhasebeci.getGeriGetirilenTutar() + ">";
            } else {
                muhasebeci.setFotoName(UUID.randomUUID().toString());
                string = "!" + muhasebeci.getAdi() + "@" + muhasebeci.getSoyadi() + "%" + muhasebeci.getTCKimlik().toString()
                        + "&" + muhasebeci.getMaas() + "?" + muhasebeci.getDepartman() + "*" + muhasebeci.getIseGiris().toString() + "_"
                        + muhasebeci.getFotoName() + ".jpg<" + muhasebeci.getZimmetEdilenMiktar() + "|" + muhasebeci.getOdeyecegiMiktar() + "(" + muhasebeci.getGeriGetirilenTutar() + ">";
                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), muhasebeci.getFotoName());
            }

            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
                this.getDosyaYazOku().dosyayaYaz("muhasebeci");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.personelTabosuGuncelle();
            this.formTemizle();
        }
    }

    public void personelOlustur(Muhendis muhendis) {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (muhendis.getFotoName().equals("")) {
                muhendis.setFotoName("unnamed.jpg");
                string = "!" + muhendis.getAdi() + "@" + muhendis.getSoyadi() + "%" + muhendis.getTCKimlik().toString()
                        + "&" + muhendis.getMaas() + "?" + muhendis.getDepartman() + "*" + muhendis.getIseGiris().toString()
                        + "_" + muhendis.getFotoName() + "<" + muhendis.getUzmanlik() + "|";
            } else {
                muhendis.setFotoName(UUID.randomUUID().toString());
                string = "!" + muhendis.getAdi() + "@" + muhendis.getSoyadi() + "%" + muhendis.getTCKimlik().toString()
                        + "&" + muhendis.getMaas() + "?" + muhendis.getDepartman() + "*" + muhendis.getIseGiris().toString() + "_"
                        + muhendis.getFotoName() + ".jpg<" + muhendis.getUzmanlik() + "|";
                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), muhendis.getFotoName());
            }

            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
                this.getDosyaYazOku().dosyayaYaz("muhendis");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.personelTabosuGuncelle();
            this.formTemizle();
        }
    }

    public void personelOlustur(Pazarlamaci pazarlamaci) {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (pazarlamaci.getFotoName().equals("")) {
                pazarlamaci.setFotoName("unnamed.jpg");
                string = "!" + pazarlamaci.getAdi() + "@" + pazarlamaci.getSoyadi() + "%" + pazarlamaci.getTCKimlik().toString()
                        + "&" + pazarlamaci.getMaas() + "?" + pazarlamaci.getDepartman() + "*" + pazarlamaci.getIseGiris().toString()
                        + "_" + pazarlamaci.getFotoName() + "<" + pazarlamaci.getAylikHedef() + "|" + pazarlamaci.getToplamSatis() + ">";
            } else {
                pazarlamaci.setFotoName(UUID.randomUUID().toString());
                string = "!" + pazarlamaci.getAdi() + "@" + pazarlamaci.getSoyadi() + "%" + pazarlamaci.getTCKimlik().toString()
                        + "&" + pazarlamaci.getMaas() + "?" + pazarlamaci.getDepartman() + "*" + pazarlamaci.getIseGiris().toString() + "_"
                        + pazarlamaci.getFotoName() + ".jpg<" + pazarlamaci.getAylikHedef() + "|" + pazarlamaci.getToplamSatis() + ">";
                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), pazarlamaci.getFotoName());
            }

            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
                this.getDosyaYazOku().dosyayaYaz("pazarlamaci");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.personelTabosuGuncelle();
            this.formTemizle();
        }
    }

    public void personelOlustur(Teknisyen teknisyen) {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (teknisyen.getFotoName().equals("")) {
                teknisyen.setFotoName("unnamed.jpg");
                string = "!" + teknisyen.getAdi() + "@" + teknisyen.getSoyadi() + "%" + teknisyen.getTCKimlik().toString()
                        + "&" + teknisyen.getMaas() + "?" + teknisyen.getDepartman() + "*" + teknisyen.getIseGiris().toString()
                        + "_" + teknisyen.getFotoName() + "<" + teknisyen.getBolum() + "|";
            } else {
                teknisyen.setFotoName(UUID.randomUUID().toString());
                string = "!" + teknisyen.getAdi() + "@" + teknisyen.getSoyadi() + "%" + teknisyen.getTCKimlik().toString()
                        + "&" + teknisyen.getMaas() + "?" + teknisyen.getBolum() + "*" + teknisyen.getIseGiris().toString() + "_"
                        + teknisyen.getFotoName() + ".jpg<" +  teknisyen.getBolum() + "|";
                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), teknisyen.getFotoName());
            }

            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
                this.getDosyaYazOku().dosyayaYaz("teknisyen");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.personelTabosuGuncelle();
            this.formTemizle();
        }
    }

    public void personelOlustur(TeknikEleman teknikEleman) {
        if (this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
        } else {
            String string;
            if (teknikEleman.getFotoName().equals("")) {
                teknikEleman.setFotoName("unnamed.jpg");
                string = "!" + teknikEleman.getAdi() + "@" + teknikEleman.getSoyadi() + "%" + teknikEleman.getTCKimlik().toString()
                        + "&" + teknikEleman.getMaas() + "?" + teknikEleman.getDepartman() + "*" + teknikEleman.getIseGiris().toString()
                        + "_" + teknikEleman.getFotoName() + "<" + teknikEleman.getTecrube() + "|" + teknikEleman.getAlan() + ">";
            } else {
                teknikEleman.setFotoName(UUID.randomUUID().toString());
                string = "!" + teknikEleman.getAdi() + "@" + teknikEleman.getSoyadi() + "%" + teknikEleman.getTCKimlik().toString()
                        + "&" + teknikEleman.getMaas() + "?" + teknikEleman.getDepartman() + "*" + teknikEleman.getIseGiris().toString() + "_"
                        + teknikEleman.getFotoName() + ".jpg<" + teknikEleman.getTecrube() + "|" + teknikEleman.getAlan() + ">";
                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), teknikEleman.getFotoName());
            }

            this.getDosyaYazOku().setYazilacakDeger(string);
            try {
                this.getDosyaYazOku().dosyayaYaz("personel");
                this.getDosyaYazOku().dosyayaYaz("teknikEleman");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.personelTabosuGuncelle();
            this.formTemizle();
        }
    }

    // Bilgi Güncelleme islemleri
    public void personelGuncelle() {
        if (!this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
        } else {
            /*
             *  string degiskeni, dosyaya yazilacak yaziyi, name dagiskenini ise personelin fotograf isminin benzersiz
             *  olmasi icin random bir dosya ismi belirlememizde kullancagiz.
             * */
            String string;
            this.setName(UUID.randomUUID().toString());
            /*
             * aaa boolean degiskeni, bir personelin bilgisi guncellenirken fotograf olarak mevcut fotografi degistirdiysek
             * yeni dosya adiyla yeni fotografin kullanilmasi icin true degerini alacak ve buna bagli olarak kisinin kendine ozel
             * olarak tanimlanan name fotograf adiyla dosyaya yeniden kaydedilmesini saglayacaktir.
             * */
            boolean aaa = false;
            /*
             * fotograf olarak herhangi bir dosya secilmediyse mevcuttaki fotograf adininin degistirilmeden kullanilmasini
             * saglamak icin dosyaya bu fotograf adiyla kaydediyoruz
             * */
            if (this.getFotoName().getText().equals("")) {
                string = "!" + this.getAdi().getText() + "@" + this.getSoyadi().getText() + "%" + this.getTcKimlikNo().getText()
                        + "&" + this.getMaas().getText() + "?" + this.getDepartman().getText() + "*" + this.getIseGirisTarihi().getValue() + "_" + this.getSelectedPersonel().getFotoName() + "<";
            } else {
                string = "!" + this.getAdi().getText() + "@" + this.getSoyadi().getText() + "%" + this.getTcKimlikNo().getText()
                        + "&" + this.getMaas().getText() + "?" + this.getDepartman().getText() + "*" + this.getIseGirisTarihi().getValue() + "_" + this.getName() + ".jpg<";
                aaa = true;
            }
            try {
                /*
                 * baslangicta dosyadaki satirlar okunuyor
                 * */
                List<String> lines = this.getDosyaYazOku().dosyadanOku("personel");
                int size = lines.size();
                for (int i = 0; i < size; i++) {
                    String str = lines.get(i);
                    int strsize = str.length();
                    for (int j = 0; j < strsize; j++) {
                        String tckimlik = "";
                        /*
                         * formdaki kimlik numarasinin dosyada var olup olmadigini kontrol etmek amaciyla her satirdaki
                         * kimlik numaralari okunuyor
                         * */
                        if (str.charAt(j) == '%') {
                            j++;
                            while (str.charAt(j) != '&') {
                                tckimlik += str.charAt(j++);
                            }
                        }
                        /*
                         * formdaki kimlik numarasi bu satirdaki okunan kimlik numarasi ile eslesiyorsa bu sart icindeki
                         * islemler gerceklesecek
                         * */
                        if (tckimlik.equals(this.getTcKimlikNo().getText())) {
                            /*
                             * eger aaa degiskeni true ise bu personelin kisisel fotografi var demektir ve secilen
                             * fotograf bizim images klasorumuze kaydedilecek.
                             * */
                            if (aaa) {
                                /*
                                 * a degiskeni, mevcut fotograf dosyada varsa eger onu silip yenisini yuklemek amaciyla
                                 * kontrol etmemize yariyor
                                 * */
                                boolean a = this.getDosyaYazOku().dosyaAra(this.getSelectedPersonel().getFotoName());
                                // dosya adi unnamed degilse silme islemi gerceklesecek.
                                if (a && !this.getSelectedPersonel().getFotoName().equals("unnamed.jpg")) {
                                    this.getDosyaYazOku().dosyaSil(this.getSelectedPersonel().getFotoName());
                                }
                                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), this.getName());
                            }
                            //guncellenecek satir siliniyoe
                            lines.remove(i);
                            //yeni bilgi listeye ekleniyor
                            lines.add(string);
                            //yeni bilgilerle dosyaya yazma islemi gerceklestiriliyor.
                            this.getDosyaYazOku().dosyaGuncelle("personel", lines);
                            //daha fazla islem yapilip zaman harcanmamasi adina i size'a esitlenerek diger satirlarin kontrol edilmesi engelleniyor
                            i = size;
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //tablo guncelleme ve form temizleme islemleri gerceklesiyor...
            this.personelTabosuGuncelle();
            this.formTemizle();
            this.getHata().setText("");
        }
    }

    // personeli isten cikarmak icin isten cikar butonuna bastigimizda calisacak kod blogu
    public void personelIstenCikar(ActionEvent e) {
        if (this.getTcKimlikNo().getText().equals("")) {
            this.getHata().setText("T.C. Kimlik Numarası alanı boş olamaz!");
        } else {
            try {
                Long l = Long.valueOf(this.getTcKimlikNo().getText());
                if (l < 10000000000L || l > 99999999998L) {
                    this.getHata().setText("T.C. Kimlik Numarası 11 Haneli Olmalı!");
                } else {
                    if (l % 2 != 0) {
                        this.getHata().setText("T.C. Kimlik Numarası tek olamaz!");
                    } else {
                        if (this.getDepartman().getText().equals("Isci")) {
                            this.istenCikar("isci");
                        } else if (this.getDepartman().getText().equals("IT Elemani")) {
                            this.istenCikar("itelemani");
                        } else if (this.getDepartman().getText().equals("Muhasebeci")) {
                            this.istenCikar("muhasebeci");
                        } else if (this.getDepartman().getText().equals("Muhendis")) {
                            this.istenCikar("muhendis");
                        } else if (this.getDepartman().getText().equals("Pazarlamaci")) {
                            this.istenCikar("pazarlamaci");
                        } else if (this.getDepartman().getText().equals("Teknisyen")) {
                            this.istenCikar("teknisyen");
                        } else if (this.getDepartman().getText().equals("Teknik Eleman")) {
                            this.istenCikar("teknikEleman");
                        } else {
                            this.istenCikar();//personel olusturma islemleri farkli bir sinifta gerceklesiyor...
                        }
                    }
                }
            } catch (Exception ex) {
                this.getHata().setText("T.C. Kimlik alanına numerik ifadeler girin!");
                System.out.println(ex.getMessage());
            }
        }
    }

    public boolean istenCikar() {
        boolean check = false;
        if (!this.personelKontrol()) {
            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
        } else {
            try {
                List<String> lines = this.getDosyaYazOku().dosyadanOku("personel");
                int size = lines.size();
                for (int i = 0; i < size; i++) {
                    String str = lines.get(i);
                    int strsize = str.length();
                    for (int j = 0; j < strsize; j++) {
                        String tckimlik = "";
                        if (str.charAt(j) == '%') {
                            j++;
                            while (str.charAt(j) != '&') {
                                tckimlik += str.charAt(j++);
                            }
                        }
                        if (tckimlik.equals(this.getTcKimlikNo().getText())) {
                            boolean a = this.getDosyaYazOku().dosyaAra(this.getSelectedPersonel().getFotoName());
                            if (a && !this.getSelectedPersonel().getFotoName().equals("unnamed.jpg")) {
                                this.getDosyaYazOku().dosyaSil(this.getSelectedPersonel().getFotoName());
                            }
                            lines.remove(i);
                            this.getDosyaYazOku().dosyaGuncelle("personel", lines);
                            check = true;
                            i = size;
                        }
                    }
                }
                lines = this.getDosyaYazOku().dosyadanOku("personelGiris");
                size = lines.size();
                for (int i = 0; i < size; i++) {
                    String str = lines.get(i);
                    int strsize = str.length();
                    for (int j = 0; j < strsize; j++) {
                        String tckimlik = "";
                        if (str.charAt(j) == '!') {
                            j++;
                            while (str.charAt(j) != '@') {
                                tckimlik += str.charAt(j++);
                            }
                        }
                        if (tckimlik.equals(this.getTcKimlikNo().getText())) {
                            lines.remove(i);
                            i = 0;
                            size--;
                            this.getDosyaYazOku().dosyaGuncelle("personelGiris", lines);
                        }
                    }
                }
                lines = this.getDosyaYazOku().dosyadanOku("personelCikis");
                size = lines.size();
                for (int i = 0; i < size; i++) {
                    String str = lines.get(i);
                    int strsize = str.length();
                    for (int j = 0; j < strsize; j++) {
                        String tckimlik = "";
                        if (str.charAt(j) == '!') {
                            j++;
                            while (str.charAt(j) != '@') {
                                tckimlik += str.charAt(j++);
                            }
                        }
                        if (tckimlik.equals(this.getTcKimlikNo().getText())) {
                            lines.remove(i);
                            i = 0;
                            size--;
                            this.getDosyaYazOku().dosyaGuncelle("personelCikis", lines);
                        }
                    }
                }
                this.personelTabosuGuncelle();
                this.formTemizle();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return check;
    }

    public void istenCikar(String dosya) {
        if (this.istenCikar()) {
            try {
                int i;
                List<String> lines = this.getDosyaYazOku().dosyadanOku(dosya);
                int size = lines.size();
                for (i = 0; i < size; i++) {
                    String str = lines.get(i);
                    int strsize = str.length();
                    for (int j = 0; j < strsize; j++) {
                        String tckimlik = "";
                        if (str.charAt(j) == '%') {
                            j++;
                            while (str.charAt(j) != '&') {
                                tckimlik += str.charAt(j++);
                            }
                        }
                        if (tckimlik.equals(this.getSelectedPersonel().getTCKimlik().toString())) {
                            lines.remove(i);
                            this.getDosyaYazOku().dosyaGuncelle(dosya, lines);
                            i = size;
                        }
                    }
                }
                this.personelTabosuGuncelle();
                this.formTemizle();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    // form uzerindeki bilgileri temizlemek istedigimizde form temizleme metodunun calismasini saglamak icin ilk bu blok ziyaret edilecektir
    public void formuTemizle(ActionEvent e) {
        this.formTemizle();
    }

    /*form temizleme islemi sadece form temizle butonu araciligiyla degil personel ekle, guncelle, isten cikar
      islemlerinden sonrada gerceklesecegi icin sadece actioneventli bir metodla olmamasi adına ve diger metodlar uzerinden
      de ulasmak istedigimizde rahatca ulasabilmek icin bu metos yazilmistir
    */
    public void formTemizle() {
        this.getTcKimlikNo().setText("");
        this.getAdi().setText("");
        this.getSoyadi().setText("");
        this.getMaas().setText("");
        this.getDepartman().setText("");
        this.getIseGirisTarihi().setValue(LocalDate.now());
        this.getFotograf().setImage(new Image("/images/unnamed.jpg"));
        this.getKisiyiGuncelle_yeniKisiEkle().setText("Yeni Personel Ekle");
        this.getFormuTemizle().setVisible(false);
        this.getIstenCikar().setVisible(false);
        this.getPersonelTable().getSelectionModel().clearSelection();
        this.getFotoName().setText("");
        this.getIseGirisSaatleri().getItems().clear();
        this.getIstenCikisSaatleri().getItems().clear();
    }

    public boolean personelKontrol() {
        boolean check = false;
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("personel");
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
                String tckimlik = "";
                /*
                 * Bu for dongusunde bir satirdaki her bir karakter icin islem gerceklesecektir. her j degiskeni
                 * satirdaki 1 karakteri ifade ediyor.
                 * */
                for (int j = 0; j < strsize; j++) {
                    /*
                     * her bir sart bir ozelligi belirliyor
                     * satirimizda tcKimlik alani '!' simgesi ile baslayip '@' simgesi ile sona eriyor
                     * satirimizda girisTarihi alani '@' simgesi ile baslayip '&' simgesi ile sona eriyor
                     * satirimizda girisSaati alani '&' simgesi ile baslayip '.' simgesi ile sona eriyor
                     * bunlara gore de gerekli atama islemleri yapilarak personel nesnesinin olusmasi saglaniyor...
                     * */
                    if (str.charAt(j) == '%') {
                        j++;
                        while (str.charAt(j) != '&') {
                            tckimlik += str.charAt(j++);
                        }
                    }
                }
                if (this.getTcKimlikNo().getText().equals(tckimlik)) {
                    check = true;
                }
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return check;
    }

    /*Get - Set Metodlari*/


    public DosyaYazma getDosyaYazOku() {
        if (this.dosyaYazOku == null)
            this.dosyaYazOku = new DosyaYazma();
        return dosyaYazOku;
    }

    public TextField getTcKimlikNo() {
        return tcKimlikNo;
    }

    public TextField getAdi() {
        return adi;
    }

    public TextField getSoyadi() {
        return soyadi;
    }

    public DatePicker getIseGirisTarihi() {
        return iseGirisTarihi;
    }

    public TextField getMaas() {
        return maas;
    }

    public TextField getDepartman() {
        return departman;
    }

    public Button getKisiyiGuncelle_yeniKisiEkle() {
        return kisiyiGuncelle_yeniKisiEkle;
    }

    public Button getIstenCikar() {
        return istenCikar;
    }

    public Button getFormuTemizle() {
        return formuTemizle;
    }

    public ImageView getFotograf() {
        return fotograf;
    }

    public Main getMain() {
        if (this.main == null)
            this.main = new Main();
        return main;
    }

    public Personel getIsci() {
        if (this.isci == null)
            this.isci = new Isci(150, 100);
        return isci;
    }

    public void setIsci(Personel isci) {
        this.isci = isci;
    }

    public Personel getITElemani() {
        if (this.ITElemani == null)
            this.ITElemani = new ITElemani();
        return ITElemani;
    }

    public void setITElemani(Personel ITElemani) {
        this.ITElemani = ITElemani;
    }

    public Personel getMuhasebeci() {
        if (this.muhasebeci == null)
            this.muhasebeci = new Muhasebeci();
        return muhasebeci;
    }

    public void setMuhasebeci(Personel muhasebeci) {
        this.muhasebeci = muhasebeci;
    }

    public Personel getMuhendis() {
        if (this.muhendis == null)
            this.muhendis = new Muhendis();
        return muhendis;
    }

    public void setMuhendis(Personel muhendis) {
        this.muhendis = muhendis;
    }

    public Personel getPazarlamaci() {
        if (this.pazarlamaci == null)
            this.pazarlamaci = new Pazarlamaci(0, 0);
        return pazarlamaci;
    }

    public void setPazarlamaci(Personel pazarlamaci) {
        this.pazarlamaci = pazarlamaci;
    }

    public Personel getTeknikEleman() {
        if (this.teknikEleman == null)
            this.teknikEleman = new TeknikEleman();
        return teknikEleman;
    }

    public void setTeknikEleman(Personel teknikEleman) {
        this.teknikEleman = teknikEleman;
    }

    public Personel getTeknisyen() {
        if (this.teknisyen == null)
            this.teknisyen = new Teknisyen();
        return teknisyen;
    }

    public void setTeknisyen(Personel teknisyen) {
        this.teknisyen = teknisyen;
    }

    private TableView getPersonelTable() {
        return personelTable;
    }

    private TableColumn<Personel, Long> getTableTC() {
        return tableTC;
    }

    private TableColumn<Personel, String> getTableAdi() {
        return tableAdi;
    }

    private TableColumn<Personel, String> getTableSoyadi() {
        return tableSoyadi;
    }

    private TableColumn<Personel, String> getTableDepartmani() {
        return tableDepartmani;
    }

    private TableColumn<Personel, Date> getTableIsegiris() {
        return tableIsegiris;
    }

    private ObservableList<Personel> getList() {
        return list;
    }

    public Label getFotoName() {
        return FotoName;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Personel getSelectedPersonel() {
        return selectedPersonel;
    }

    public void setSelectedPersonel(Personel selectedPersonel) {
        this.selectedPersonel = selectedPersonel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListView getIseGirisSaatleri() {
        return iseGirisSaatleri;
    }

    public ListView getIstenCikisSaatleri() {
        return istenCikisSaatleri;
    }

    public Label getHata() {
        return hata;
    }
}