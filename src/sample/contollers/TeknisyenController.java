package sample.contollers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.entity.Teknisyen;

import static java.lang.Integer.parseInt;

public class TeknisyenController extends PersonelContoller {
    @FXML
    private TextField bolum;
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<Teknisyen, Long> tableTC;
    @FXML
    private TableColumn<Teknisyen, String> tableAdi;
    @FXML
    private TableColumn<Teknisyen, String> tableSoyadi;
    @FXML
    private TableColumn<Teknisyen, String> tableDepartmani;
    @FXML
    private TableColumn<Teknisyen, Date> tableIsegiris;

    private ObservableList<Teknisyen> list = FXCollections.observableArrayList();

    private Teknisyen selectedTeknisyen;

    @Override
    public void personelTabosuGuncelle() {
        //ilk once mevcut tablomuzda ve listemizde herhangi bir veri varsa onlari sifirliyoruz.
        this.getPersonelTable().getItems().clear();
        this.getList().clear();
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("teknisyen");
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
                String bolum = "";
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
                     * satirimizda bolum alani '<' simgesi ile baslayip '|' simgesi ile sona eriyor
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
                            bolum += str.charAt(j++);
                        }
                    }
                }
                /*
                 * Bir satirin okunma islemi bitti ve personel nesnesi lusturularak tableview listesine
                 * ekleme islemi gerceklestirilecek
                 * */
                Teknisyen teknisyen = new Teknisyen(bolum);
                teknisyen.setAdi(ad);
                teknisyen.setSoyadi(soyad);
                teknisyen.setDepartman(depart);
                teknisyen.setMaas(parseInt(maass));
                teknisyen.setTCKimlik(Long.valueOf(tckimlik));
                teknisyen.setIseGiris(LocalDate.parse(isegiris));
                teknisyen.setFotoName(fotoName);
                this.getList().add(teknisyen);
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
        this.getTableTC().setCellValueFactory(new PropertyValueFactory<Teknisyen, Long>("TCKimlik"));
        //Tablomuzun tableAdi sutununa personel nesnemizdeki adi ozelligini aktariyoruz
        this.getTableAdi().setCellValueFactory(new PropertyValueFactory<Teknisyen, String>("adi"));
        //Tablomuzun tableSoyadi sutununa personel nesnemizdeki soyadi ozelligini aktariyoruz
        this.getTableSoyadi().setCellValueFactory(new PropertyValueFactory<Teknisyen, String>("soyadi"));
        //Tablomuzun tableDepartmani sutununa personel nesnemizdeki departman ozelligini aktariyoruz
        this.getTableDepartmani().setCellValueFactory(new PropertyValueFactory<Teknisyen, String>("departman"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableIsegiris().setCellValueFactory(new PropertyValueFactory<Teknisyen, Date>("iseGiris"));
    }

    @Override
    public void personelBilgiGuncelleEkle(ActionEvent e) {
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
                                                if (this.getDepartman().getText().equals("Teknisyen")) {
                                                    this.setTeknisyen(new Teknisyen());
                                                    this.getTeknisyen().setAdi(this.getAdi().getText());
                                                    this.getTeknisyen().setSoyadi(this.getSoyadi().getText());
                                                    this.getTeknisyen().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getTeknisyen().setMaas(parseInt(this.getMaas().getText()));
                                                    this.getTeknisyen().setDepartman(this.getDepartman().getText());
                                                    this.getTeknisyen().setFotoName(this.getFotoName().getText());
                                                    this.getTeknisyen().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Teknisyen) this.getTeknisyen());
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Teknisyen departmanından eleman ekleyebilirsiniz!");
                                                }
                                            } else {
                                                if (this.getDepartman().getText().equals("Teknisyen")) {
                                                    this.personelGuncelle();
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Teknisyen departmanından eleman düzeleyebilirsiniz!");
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

    @Override
    public void personelDetay(MouseEvent e) {
        super.personelDetay(e);
        // tablomuzda secilen verinin alinmiyor
        TableView.TableViewSelectionModel<Teknisyen> selectionModel = this.getPersonelTable().getSelectionModel();

        // tablodam secilecek maks item sayisinin 1 olmasi gerektigini belirliyoruz
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        /*
         * tablodan secilen itemin bir personel nesnesi oldugu biliniyor ve personel nesnesine cast islemi gercekleserek
         * diger metodlardan da ulasilabilrmasi adina global selectedPersonel nesnesine aktariliyor.
         * */
        this.setSelectedTeknisyen((Teknisyen) selectionModel.getSelectedItem());
        this.getBolum().setText(String.valueOf(this.getSelectedTeknisyen().getBolum()));
    }

    @Override
    public void personelGuncelle() {
        if (this.getBolum().getText().equals("")) {
            this.getHata().setText("Bölüm alanı boş bırakılamaz!");
        } else {
            if (!this.personelKontrol()) {
                this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
            } else {
                Teknisyen teknisyen = new Teknisyen(this.getBolum().getText());
                teknisyen.setAdi(this.getAdi().getText());
                teknisyen.setSoyadi(this.getSoyadi().getText());
                teknisyen.setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                teknisyen.setMaas(parseInt(this.getMaas().getText()));
                teknisyen.setDepartman(this.getDepartman().getText());
                teknisyen.setFotoName(this.getFotoName().getText());
                teknisyen.setIseGiris(this.getIseGirisTarihi().getValue());
                try {
                    /*
                     *  string degiskeni, dosyaya yazilacak yaziyi, name dagiskenini ise personelin fotograf isminin benzersiz
                     *  olmasi icin random bir dosya ismi belirlememizde kullancagiz.
                     * */
                    String string;
                    /*
                     * fotograf olarak herhangi bir dosya secilmediyse mevcuttaki fotograf adininin degistirilmeden kullanilmasini
                     * saglamak icin dosyaya bu fotograf adiyla kaydediyoruz
                     * */
                    boolean a = false;
                    if (teknisyen.getFotoName().equals("")) {
                        teknisyen.setFotoName(this.getSelectedPersonel().getFotoName());
                        string = "!" + teknisyen.getAdi() + "@" + teknisyen.getSoyadi() + "%" + teknisyen.getTCKimlik().toString()
                                + "&" + teknisyen.getMaas() + "?" + teknisyen.getDepartman() + "*" + teknisyen.getIseGiris().toString() + "_" +
                                teknisyen.getFotoName() + "<" + teknisyen.getBolum() + "|";
                    } else {
                        teknisyen.setFotoName(UUID.randomUUID().toString());
                        string = "!" + teknisyen.getAdi() + "@" + teknisyen.getSoyadi() + "%" + teknisyen.getTCKimlik().toString()
                                + "&" + teknisyen.getMaas() + "?" + teknisyen.getDepartman() + "*" + teknisyen.getIseGiris().toString() + "_" +
                                teknisyen.getFotoName() + ".jpg<" + teknisyen.getBolum() + "|";
                        a = true;
                    }
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
                            if (tckimlik.equals(teknisyen.getTCKimlik().toString())) {
                                /*
                                 * eger aaa degiskeni true ise bu personelin kisisel fotografi var demektir ve secilen
                                 * fotograf bizim images klasorumuze kaydedilecek.
                                 * */
                                if (a) {
                                    /*
                                     * a degiskeni, mevcut fotograf dosyada varsa eger onu silip yenisini yuklemek amaciyla
                                     * kontrol etmemize yariyor
                                     * */
                                    boolean aa = this.getDosyaYazOku().dosyaAra(this.getSelectedPersonel().getFotoName());
                                    // dosya adi unnamed degilse silme islemi gerceklesecek.
                                    if (aa && !this.getSelectedPersonel().getFotoName().equals("unnamed.jpg")) {
                                        this.getDosyaYazOku().dosyaSil(this.getSelectedPersonel().getFotoName());
                                    }
                                    this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), teknisyen.getFotoName());
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


                    lines = this.getDosyaYazOku().dosyadanOku("teknisyen");
                    size = lines.size();
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
                            if (tckimlik.equals(teknisyen.getTCKimlik().toString())) {
                                //guncellenecek satir siliniyoe
                                lines.remove(i);
                                //yeni bilgi listeye ekleniyor
                                lines.add(string);
                                //yeni bilgilerle dosyaya yazma islemi gerceklestiriliyor.
                                this.getDosyaYazOku().dosyaGuncelle("teknisyen", lines);
                                //daha fazla islem yapilip zaman harcanmamasi adina i size'a esitlenerek diger satirlarin kontrol edilmesi engelleniyor
                                i = size;
                            }
                        }
                    }
                    this.personelTabosuGuncelle();
                    this.formTemizle();
                    this.getHata().setText("");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    @Override
    public void personelOlustur(Teknisyen teknisyen) {
        boolean check = false;
        if (this.getBolum().getText().equals("")) {
            this.getHata().setText("Bölüm alanı boş bırakılamaz!");
        } else {
            if (this.personelKontrol()) {
                this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
            } else {
                String string;
                teknisyen.setBolum(this.getBolum().getText());
                if (teknisyen.getFotoName().equals("")) {
                    teknisyen.setFotoName("unnamed.jpg");
                    string = "!" + teknisyen.getAdi() + "@" + teknisyen.getSoyadi() + "%" + teknisyen.getTCKimlik().toString()
                            + "&" + teknisyen.getMaas() + "?" + teknisyen.getDepartman() + "*" + teknisyen.getIseGiris().toString()
                            + "_" + teknisyen.getFotoName() + "<" + teknisyen.getBolum() + "|";
                } else {
                    teknisyen.setFotoName(UUID.randomUUID().toString());
                    string = "!" + teknisyen.getAdi() + "@" + teknisyen.getSoyadi() + "%" + teknisyen.getTCKimlik().toString()
                            + "&" + teknisyen.getMaas() + "?" + teknisyen.getDepartman() + "*" + teknisyen.getIseGiris().toString()
                            + "_" + teknisyen.getFotoName() + ".jpg<" + teknisyen.getBolum() + "|";

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
                this.getHata().setText("");

            }
        }
    }

    @Override
    public void formTemizle() {
        super.formTemizle();
        this.getBolum().setText("");
    }

    public TextField getBolum() {
        return bolum;
    }

    public TableView getPersonelTable() {
        return personelTable;
    }

    public TableColumn<Teknisyen, Long> getTableTC() {
        return tableTC;
    }

    public TableColumn<Teknisyen, String> getTableAdi() {
        return tableAdi;
    }

    public TableColumn<Teknisyen, String> getTableSoyadi() {
        return tableSoyadi;
    }

    public TableColumn<Teknisyen, String> getTableDepartmani() {
        return tableDepartmani;
    }

    public TableColumn<Teknisyen, Date> getTableIsegiris() {
        return tableIsegiris;
    }

    public ObservableList<Teknisyen> getList() {
        return list;
    }

    public Teknisyen getSelectedTeknisyen() {
        return selectedTeknisyen;
    }

    public void setSelectedTeknisyen(Teknisyen selectedTeknisyen) {
        this.selectedTeknisyen = selectedTeknisyen;
    }
}
