package sample.contollers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.entity.Muhendis;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class MuhendisController extends PersonelContoller {
    @FXML
    private TextField uzmanlik;
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<Muhendis, Long> tableTC;
    @FXML
    private TableColumn<Muhendis, String> tableAdi;
    @FXML
    private TableColumn<Muhendis, String> tableSoyadi;
    @FXML
    private TableColumn<Muhendis, String> tableDepartmani;
    @FXML
    private TableColumn<Muhendis, Date> tableIsegiris;

    private ObservableList<Muhendis> list = FXCollections.observableArrayList();

    private Muhendis selectedMuhendis;


    @Override
    public void personelTabosuGuncelle() {
        //ilk once mevcut tablomuzda ve listemizde herhangi bir veri varsa onlari sifirliyoruz.
        this.getPersonelTable().getItems().clear();
        this.getList().clear();
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("muhendis");
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
                String uzmanlik = "";
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
                     * satirimizda uzmanlik adi alani '<' simgesi ile baslayip '|' simgesi ile sona eriyor
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
                            uzmanlik += str.charAt(j++);
                        }
                    }


                }
                /*
                 * Bir satirin okunma islemi bitti ve personel nesnesi lusturularak tableview listesine
                 * ekleme islemi gerceklestirilecek
                 * */

                Muhendis muhendis = new Muhendis(uzmanlik);
                muhendis.setAdi(ad);
                muhendis.setSoyadi(soyad);
                muhendis.setDepartman(depart);
                muhendis.setMaas(parseInt(maass));
                muhendis.setTCKimlik(Long.valueOf(tckimlik));
                muhendis.setIseGiris(LocalDate.parse(isegiris));
                muhendis.setFotoName(fotoName);
                this.getList().add(muhendis);
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
        this.getTableTC().setCellValueFactory(new PropertyValueFactory<Muhendis, Long>("TCKimlik"));
        //Tablomuzun tableAdi sutununa personel nesnemizdeki adi ozelligini aktariyoruz
        this.getTableAdi().setCellValueFactory(new PropertyValueFactory<Muhendis, String>("adi"));
        //Tablomuzun tableSoyadi sutununa personel nesnemizdeki soyadi ozelligini aktariyoruz
        this.getTableSoyadi().setCellValueFactory(new PropertyValueFactory<Muhendis, String>("soyadi"));
        //Tablomuzun tableDepartmani sutununa personel nesnemizdeki departman ozelligini aktariyoruz
        this.getTableDepartmani().setCellValueFactory(new PropertyValueFactory<Muhendis, String>("departman"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableIsegiris().setCellValueFactory(new PropertyValueFactory<Muhendis, Date>("iseGiris"));
    }

    @Override
    public void personelDetay(MouseEvent e) {
        super.personelDetay(e);
        // tablomuzda secilen verinin alinmiyor
        TableView.TableViewSelectionModel<Muhendis> selectionModel = this.getPersonelTable().getSelectionModel();

        // tablodam secilecek maks item sayisinin 1 olmasi gerektigini belirliyoruz
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        /*
         * tablodan secilen itemin bir personel nesnesi oldugu biliniyor ve personel nesnesine cast islemi gercekleserek
         * diger metodlardan da ulasilabilrmasi adina global selectedPersonel nesnesine aktariliyor.
         * */
        this.setSelectedMuhendis((Muhendis) selectionModel.getSelectedItem());
        this.getUzmanlik().setText(String.valueOf(this.getSelectedMuhendis().getUzmanlik()));
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
                                                if (this.getDepartman().getText().equals("Muhendis")) {
                                                    this.setMuhendis(new Muhendis());
                                                    this.getMuhendis().setAdi(this.getAdi().getText());
                                                    this.getMuhendis().setSoyadi(this.getSoyadi().getText());
                                                    this.getMuhendis().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getMuhendis().setMaas(parseInt(this.getMaas().getText()));
                                                    this.getMuhendis().setDepartman(this.getDepartman().getText());
                                                    this.getMuhendis().setFotoName(this.getFotoName().getText());
                                                    this.getMuhendis().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Muhendis) this.getMuhendis());
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Muhendis departmanından eleman ekleyebilirsiniz!");
                                                }
                                            } else {
                                                if (this.getDepartman().getText().equals("Muhendis")) {
                                                    this.personelGuncelle();
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Muhendis departmanından eleman düzeleyebilirsiniz!");
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
    public void personelGuncelle() {
        if (this.getUzmanlik().getText().equals("")) {
            this.getHata().setText("Uzmanlık alanı boş bırakılamaz!");
        } else {
            if (!this.personelKontrol()) {
                this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
            } else {
                Muhendis muhendis = new Muhendis();
                muhendis.setAdi(this.getAdi().getText());
                muhendis.setSoyadi(this.getSoyadi().getText());
                muhendis.setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                muhendis.setMaas(parseInt(this.getMaas().getText()));
                muhendis.setDepartman(this.getDepartman().getText());
                muhendis.setFotoName(this.getFotoName().getText());
                muhendis.setIseGiris(this.getIseGirisTarihi().getValue());
                muhendis.setUzmanlik(this.getUzmanlik().getText());

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
                    if (muhendis.getFotoName().equals("")) {
                        muhendis.setFotoName(this.getSelectedPersonel().getFotoName());
                        string = "!" + muhendis.getAdi() + "@" + muhendis.getSoyadi() + "%" + muhendis.getTCKimlik().toString()
                                + "&" + muhendis.getMaas() + "?" + muhendis.getDepartman() + "*" + muhendis.getIseGiris().toString() + "_" +
                                muhendis.getFotoName() + "<" + muhendis.getUzmanlik() + "|";
                    } else {
                        muhendis.setFotoName(UUID.randomUUID().toString());
                        string = "!" + muhendis.getAdi() + "@" + muhendis.getSoyadi() + "%" + muhendis.getTCKimlik().toString()
                                + "&" + muhendis.getMaas() + "?" + muhendis.getDepartman() + "*" + muhendis.getIseGiris().toString() + "_" +
                                muhendis.getFotoName() + ".jpg<" + muhendis.getUzmanlik() + "|";
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
                            if (tckimlik.equals(muhendis.getTCKimlik().toString())) {
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
                                    this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), muhendis.getFotoName());
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


                    lines = this.getDosyaYazOku().dosyadanOku("muhendis");
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
                            if (tckimlik.equals(muhendis.getTCKimlik().toString())) {
                                //guncellenecek satir siliniyoe
                                lines.remove(i);
                                //yeni bilgi listeye ekleniyor
                                lines.add(string);
                                //yeni bilgilerle dosyaya yazma islemi gerceklestiriliyor.
                                this.getDosyaYazOku().dosyaGuncelle("muhendis", lines);
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
    public void personelOlustur(Muhendis muhendis) {
        if (this.getUzmanlik().getText().equals("")) {
            this.getHata().setText("Uzmanlık alanı boş bırakılamaz!");
        } else {
            if (this.personelKontrol()) {
                this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
            } else {
                String string;
                muhendis.setUzmanlik(this.getUzmanlik().getText());
                if (muhendis.getFotoName().equals("")) {
                    muhendis.setFotoName("unnamed.jpg");
                    string = "!" + muhendis.getAdi() + "@" + muhendis.getSoyadi() + "%" + muhendis.getTCKimlik().toString()
                            + "&" + muhendis.getMaas() + "?" + muhendis.getDepartman() + "*" + muhendis.getIseGiris().toString()
                            + "_" + muhendis.getFotoName() + "<" + muhendis.getUzmanlik() + "|";
                } else {
                    muhendis.setFotoName(UUID.randomUUID().toString());
                    string = "!" + muhendis.getAdi() + "@" + muhendis.getSoyadi() + "%" + muhendis.getTCKimlik().toString()
                            + "&" + muhendis.getMaas() + "?" + muhendis.getDepartman() + "*" + muhendis.getIseGiris().toString()
                            + "_" + muhendis.getFotoName() + ".jpg<" + muhendis.getUzmanlik() + "|";
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
    }

    @Override
    public void formTemizle() {
        super.formTemizle();
        this.getUzmanlik().setText("");
    }


    //SET-GET METOD


    public TextField getUzmanlik() {
        return uzmanlik;
    }

    public TableView getPersonelTable() {
        return personelTable;
    }

    public TableColumn<Muhendis, Long> getTableTC() {
        return tableTC;
    }

    public TableColumn<Muhendis, String> getTableAdi() {
        return tableAdi;
    }

    public TableColumn<Muhendis, String> getTableSoyadi() {
        return tableSoyadi;
    }

    public TableColumn<Muhendis, String> getTableDepartmani() {
        return tableDepartmani;
    }

    public TableColumn<Muhendis, Date> getTableIsegiris() {
        return tableIsegiris;
    }

    public ObservableList<Muhendis> getList() {
        return list;
    }

    public Muhendis getSelectedMuhendis() {
        return selectedMuhendis;
    }

    public void setSelectedMuhendis(Muhendis selectedMuhendis) {
        this.selectedMuhendis = selectedMuhendis;
    }
}