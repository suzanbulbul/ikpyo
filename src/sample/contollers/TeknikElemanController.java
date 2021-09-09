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
import sample.entity.TeknikEleman;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class TeknikElemanController extends PersonelContoller {
    @FXML
    private TextField tecrube;
    @FXML
    private TextField alan;
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<TeknikEleman, Long> tableTC;
    @FXML
    private TableColumn<TeknikEleman, String> tableAdi;
    @FXML
    private TableColumn<TeknikEleman, String> tableSoyadi;
    @FXML
    private TableColumn<TeknikEleman, String> tableDepartmani;
    @FXML
    private TableColumn<TeknikEleman, Date> tableIsegiris;

    private ObservableList<TeknikEleman> list = FXCollections.observableArrayList();

    private TeknikEleman selectedTeknikEleman;

    @Override
    public void personelTabosuGuncelle() {
        //ilk once mevcut tablomuzda ve listemizde herhangi bir veri varsa onlari sifirliyoruz.
        this.getPersonelTable().getItems().clear();
        this.getList().clear();
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("teknikEleman");
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
                String tecrube = "";
                String alan = "";
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
                     * satirimizda tecrube alani '<' simgesi ile baslayip '|' simgesi ile sona eriyor
                     * satirimizda alan alani '|' simgesi ile baslayip '>' simgesi ile sona eriyor
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
                            tecrube += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '|') {
                        j++;
                        while (str.charAt(j) != '>') {
                            alan += str.charAt(j++);
                        }
                    }
                }
                /*
                 * Bir satirin okunma islemi bitti ve personel nesnesi lusturularak tableview listesine
                 * ekleme islemi gerceklestirilecek
                 * */
                TeknikEleman teknikEleman = new TeknikEleman(parseInt(tecrube), alan);
                teknikEleman.setAdi(ad);
                teknikEleman.setSoyadi(soyad);
                teknikEleman.setDepartman(depart);
                teknikEleman.setMaas(parseInt(maass));
                teknikEleman.setTCKimlik(Long.valueOf(tckimlik));
                teknikEleman.setIseGiris(LocalDate.parse(isegiris));
                teknikEleman.setFotoName(fotoName);
                this.getList().add(teknikEleman);
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
        this.getTableTC().setCellValueFactory(new PropertyValueFactory<TeknikEleman, Long>("TCKimlik"));
        //Tablomuzun tableAdi sutununa personel nesnemizdeki adi ozelligini aktariyoruz
        this.getTableAdi().setCellValueFactory(new PropertyValueFactory<TeknikEleman, String>("adi"));
        //Tablomuzun tableSoyadi sutununa personel nesnemizdeki soyadi ozelligini aktariyoruz
        this.getTableSoyadi().setCellValueFactory(new PropertyValueFactory<TeknikEleman, String>("soyadi"));
        //Tablomuzun tableDepartmani sutununa personel nesnemizdeki departman ozelligini aktariyoruz
        this.getTableDepartmani().setCellValueFactory(new PropertyValueFactory<TeknikEleman, String>("departman"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableIsegiris().setCellValueFactory(new PropertyValueFactory<TeknikEleman, Date>("iseGiris"));
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
                                                if (this.getDepartman().getText().equals("Teknik Eleman")) {
                                                    this.setTeknikEleman(new TeknikEleman());
                                                    this.getTeknikEleman().setAdi(this.getAdi().getText());
                                                    this.getTeknikEleman().setSoyadi(this.getSoyadi().getText());
                                                    this.getTeknikEleman().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getTeknikEleman().setMaas(parseInt(this.getMaas().getText()));
                                                    this.getTeknikEleman().setDepartman(this.getDepartman().getText());
                                                    this.getTeknikEleman().setFotoName(this.getFotoName().getText());
                                                    this.getTeknikEleman().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((TeknikEleman) this.getTeknikEleman());
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Teknik Eleman departmanından eleman ekleyebilirsiniz!");
                                                }
                                            } else {
                                                if (this.getDepartman().getText().equals("Teknik Eleman")) {
                                                    this.personelGuncelle();
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Teknik Eleman departmanından eleman düzeleyebilirsiniz!");
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
        TableView.TableViewSelectionModel<TeknikEleman> selectionModel = this.getPersonelTable().getSelectionModel();

        // tablodam secilecek maks item sayisinin 1 olmasi gerektigini belirliyoruz
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        /*
         * tablodan secilen itemin bir personel nesnesi oldugu biliniyor ve personel nesnesine cast islemi gercekleserek
         * diger metodlardan da ulasilabilrmasi adina global selectedPersonel nesnesine aktariliyor.
         * */
        this.setSelectedTeknikEleman((TeknikEleman) selectionModel.getSelectedItem());
        this.getTecrube().setText(String.valueOf(this.getSelectedTeknikEleman().getTecrube()));
        this.getAlan().setText(String.valueOf(this.getSelectedTeknikEleman().getAlan()));
    }

    @Override
    public void personelGuncelle() {
        if (this.getTecrube().getText().equals("")) {
            this.getHata().setText("Tecrube alanı boş bırakılamaz!");
        } else {
            try {
                parseInt(this.getTecrube().getText());
                if (this.getAlan().getText().equals("")) {
                    this.getHata().setText("Alan bölümü boş bırakılamaz!");
                } else {
                    if (!this.personelKontrol()) {
                        this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
                    } else {
                        TeknikEleman teknikEleman = new TeknikEleman(parseInt(this.getTecrube().getText()), this.getAlan().getText());
                        teknikEleman.setAdi(this.getAdi().getText());
                        teknikEleman.setSoyadi(this.getSoyadi().getText());
                        teknikEleman.setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                        teknikEleman.setMaas(parseInt(this.getMaas().getText()));
                        teknikEleman.setDepartman(this.getDepartman().getText());
                        teknikEleman.setFotoName(this.getFotoName().getText());
                        teknikEleman.setIseGiris(this.getIseGirisTarihi().getValue());
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
                            if (teknikEleman.getFotoName().equals("")) {
                                teknikEleman.setFotoName(this.getSelectedPersonel().getFotoName());
                                string = "!" + teknikEleman.getAdi() + "@" + teknikEleman.getSoyadi() + "%" + teknikEleman.getTCKimlik().toString()
                                        + "&" + teknikEleman.getMaas() + "?" + teknikEleman.getDepartman() + "*" + teknikEleman.getIseGiris().toString() + "_" +
                                        teknikEleman.getFotoName() + "<" + teknikEleman.getTecrube() + "|" + teknikEleman.getAlan() + ">";
                            } else {
                                teknikEleman.setFotoName(UUID.randomUUID().toString());
                                string = "!" + teknikEleman.getAdi() + "@" + teknikEleman.getSoyadi() + "%" + teknikEleman.getTCKimlik().toString()
                                        + "&" + teknikEleman.getMaas() + "?" + teknikEleman.getDepartman() + "*" + teknikEleman.getIseGiris().toString() + "_" +
                                        teknikEleman.getFotoName() + ".jpg<" + teknikEleman.getTecrube() + "|" + teknikEleman.getAlan() + ">";
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
                                    if (tckimlik.equals(teknikEleman.getTCKimlik().toString())) {
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
                                            this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), teknikEleman.getFotoName());
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


                            lines = this.getDosyaYazOku().dosyadanOku("teknikEleman");
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
                                    if (tckimlik.equals(teknikEleman.getTCKimlik().toString())) {
                                        //guncellenecek satir siliniyoe
                                        lines.remove(i);
                                        //yeni bilgi listeye ekleniyor
                                        lines.add(string);
                                        //yeni bilgilerle dosyaya yazma islemi gerceklestiriliyor.
                                        this.getDosyaYazOku().dosyaGuncelle("teknikEleman", lines);
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
            } catch (Exception e) {
                this.getHata().setText("Tecrube alanına numerik ifadeler girin!");
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void personelOlustur(TeknikEleman teknikEleman) {
        if (this.getTecrube().getText().equals("")) {
            this.getHata().setText("Tecrube alanı boş bırakılamaz!");
        } else {
            try {
                parseInt(this.getTecrube().getText());
                if (this.getAlan().getText().equals("")) {
                    this.getHata().setText("Alan bölümü boş bırakılamaz!");
                } else {
                    if (this.personelKontrol()) {
                        this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
                    } else {
                        String string;
                        teknikEleman.setTecrube(parseInt(this.getTecrube().getText()));
                        teknikEleman.setAlan(this.getAlan().getText());
                        if (teknikEleman.getFotoName().equals("")) {
                            teknikEleman.setFotoName("unnamed.jpg");
                            string = "!" + teknikEleman.getAdi() + "@" + teknikEleman.getSoyadi() + "%" + teknikEleman.getTCKimlik().toString()
                                    + "&" + teknikEleman.getMaas() + "?" + teknikEleman.getDepartman() + "*" + teknikEleman.getIseGiris().toString()
                                    + "_" + teknikEleman.getFotoName() + "<" + teknikEleman.getTecrube() + "|" + teknikEleman.getAlan() + ">";
                        } else {
                            teknikEleman.setFotoName(UUID.randomUUID().toString());
                            string = "!" + teknikEleman.getAdi() + "@" + teknikEleman.getSoyadi() + "%" + teknikEleman.getTCKimlik().toString()
                                    + "&" + teknikEleman.getMaas() + "?" + teknikEleman.getDepartman() + "*" + teknikEleman.getIseGiris().toString()
                                    + "_" + teknikEleman.getFotoName() + ".jpg<" + teknikEleman.getTecrube() + "|" + teknikEleman.getAlan() + ">";

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
                        this.getHata().setText("");

                    }
                }
            } catch (Exception e) {
                this.getHata().setText("Tecrube alanına numerik ifadeler girin!");
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void formTemizle() {
        super.formTemizle();
        this.getTecrube().setText("");
        this.getAlan().setText("");
    }


    public TextField getTecrube() {
        return tecrube;
    }

    public void setTecrube(TextField tecrube) {
        this.tecrube = tecrube;
    }

    public TextField getAlan() {
        return alan;
    }

    public void setAlan(TextField alan) {
        this.alan = alan;
    }

    public TableView getPersonelTable() {
        return personelTable;
    }

    public void setPersonelTable(TableView personelTable) {
        this.personelTable = personelTable;
    }

    public TableColumn<TeknikEleman, Long> getTableTC() {
        return tableTC;
    }

    public void setTableTC(TableColumn<TeknikEleman, Long> tableTC) {
        this.tableTC = tableTC;
    }

    public TableColumn<TeknikEleman, String> getTableAdi() {
        return tableAdi;
    }

    public void setTableAdi(TableColumn<TeknikEleman, String> tableAdi) {
        this.tableAdi = tableAdi;
    }

    public TableColumn<TeknikEleman, String> getTableSoyadi() {
        return tableSoyadi;
    }

    public void setTableSoyadi(TableColumn<TeknikEleman, String> tableSoyadi) {
        this.tableSoyadi = tableSoyadi;
    }

    public TableColumn<TeknikEleman, String> getTableDepartmani() {
        return tableDepartmani;
    }

    public void setTableDepartmani(TableColumn<TeknikEleman, String> tableDepartmani) {
        this.tableDepartmani = tableDepartmani;
    }

    public TableColumn<TeknikEleman, Date> getTableIsegiris() {
        return tableIsegiris;
    }

    public void setTableIsegiris(TableColumn<TeknikEleman, Date> tableIsegiris) {
        this.tableIsegiris = tableIsegiris;
    }

    public ObservableList<TeknikEleman> getList() {
        return list;
    }

    public void setList(ObservableList<TeknikEleman> list) {
        this.list = list;
    }

    public TeknikEleman getSelectedTeknikEleman() {
        return selectedTeknikEleman;
    }

    public void setSelectedTeknikEleman(TeknikEleman selectedTeknikEleman) {
        this.selectedTeknikEleman = selectedTeknikEleman;
    }
}
