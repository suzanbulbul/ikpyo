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
import sample.entity.Pazarlamaci;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class PazarlamaciController extends PersonelContoller {
    @FXML
    private TextField aylikHedef;
    @FXML
    private TextField toplamSatis;
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<Pazarlamaci, Long> tableTC;
    @FXML
    private TableColumn<Pazarlamaci, String> tableAdi;
    @FXML
    private TableColumn<Pazarlamaci, String> tableSoyadi;
    @FXML
    private TableColumn<Pazarlamaci, String> tableDepartmani;
    @FXML
    private TableColumn<Pazarlamaci, Date> tableIsegiris;

    private ObservableList<Pazarlamaci> list = FXCollections.observableArrayList();

    private Pazarlamaci selectedPazarlamaci;

    @Override
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
                    }
                    if (str.charAt(j) == '|') {
                        j++;
                        while (str.charAt(j) != '>') {
                            aylikHedef += str.charAt(j++);
                        }
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
        this.getTableDepartmani().setCellValueFactory(new PropertyValueFactory<Pazarlamaci, String>("departman"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableIsegiris().setCellValueFactory(new PropertyValueFactory<Pazarlamaci, Date>("iseGiris"));
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
                                                if (this.getDepartman().getText().equals("Pazarlamaci")) {
                                                    this.setPazarlamaci(new Pazarlamaci());
                                                    this.getPazarlamaci().setAdi(this.getAdi().getText());
                                                    this.getPazarlamaci().setSoyadi(this.getSoyadi().getText());
                                                    this.getPazarlamaci().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getPazarlamaci().setMaas(parseInt(this.getMaas().getText()));
                                                    this.getPazarlamaci().setDepartman(this.getDepartman().getText());
                                                    this.getPazarlamaci().setFotoName(this.getFotoName().getText());
                                                    this.getPazarlamaci().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Pazarlamaci) this.getPazarlamaci());
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Pazarlamaci departmanından eleman ekleyebilirsiniz!");
                                                }
                                            } else {
                                                if (this.getDepartman().getText().equals("Pazarlamaci")) {
                                                    this.personelGuncelle();
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Pazarlamaci departmanından eleman düzeleyebilirsiniz!");
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
        TableView.TableViewSelectionModel<Pazarlamaci> selectionModel = this.getPersonelTable().getSelectionModel();

        // tablodam secilecek maks item sayisinin 1 olmasi gerektigini belirliyoruz
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        /*
         * tablodan secilen itemin bir personel nesnesi oldugu biliniyor ve personel nesnesine cast islemi gercekleserek
         * diger metodlardan da ulasilabilrmasi adina global selectedPersonel nesnesine aktariliyor.
         * */
        this.setSelectedPazarlamaci((Pazarlamaci) selectionModel.getSelectedItem());
        this.getAylikHedef().setText(String.valueOf(this.getSelectedPazarlamaci().getAylikHedef()));
        this.getToplamSatis().setText(String.valueOf(this.getSelectedPazarlamaci().getToplamSatis()));
    }

    @Override
    public void personelGuncelle() {
        if (this.getAylikHedef().getText().equals("")) {
            this.getHata().setText("Aylık Hedef alanı boş bırakılamaz!");
        } else {
            try {
                parseInt(this.getAylikHedef().getText());
                if (this.getToplamSatis().getText().equals("")) {
                    this.getHata().setText("Toplam Satış alanı boş bırakılamaz!");
                } else {
                    try {
                        parseInt(this.getToplamSatis().getText());
                        if (!this.personelKontrol()) {
                            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
                        } else {
                            Pazarlamaci pazarlamaci = new Pazarlamaci(parseInt(this.getAylikHedef().getText()), parseInt(this.getToplamSatis().getText()));
                            pazarlamaci.setAdi(this.getAdi().getText());
                            pazarlamaci.setSoyadi(this.getSoyadi().getText());
                            pazarlamaci.setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                            pazarlamaci.setMaas(parseInt(this.getMaas().getText()));
                            pazarlamaci.setDepartman(this.getDepartman().getText());
                            pazarlamaci.setFotoName(this.getFotoName().getText());
                            pazarlamaci.setIseGiris(this.getIseGirisTarihi().getValue());
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
                                if (pazarlamaci.getFotoName().equals("")) {
                                    pazarlamaci.setFotoName(this.getSelectedPersonel().getFotoName());
                                    string = "!" + pazarlamaci.getAdi() + "@" + pazarlamaci.getSoyadi() + "%" + pazarlamaci.getTCKimlik().toString()
                                            + "&" + pazarlamaci.getMaas() + "?" + pazarlamaci.getDepartman() + "*" + pazarlamaci.getIseGiris().toString() + "_" +
                                            pazarlamaci.getFotoName() + "<" + pazarlamaci.getToplamSatis() + "|" + pazarlamaci.getAylikHedef() + ">";
                                } else {
                                    pazarlamaci.setFotoName(UUID.randomUUID().toString());
                                    string = "!" + pazarlamaci.getAdi() + "@" + pazarlamaci.getSoyadi() + "%" + pazarlamaci.getTCKimlik().toString()
                                            + "&" + pazarlamaci.getMaas() + "?" + pazarlamaci.getDepartman() + "*" + pazarlamaci.getIseGiris().toString() + "_" +
                                            pazarlamaci.getFotoName() + ".jpg<" + pazarlamaci.getToplamSatis() + "|" + pazarlamaci.getAylikHedef() + ">";
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
                                        if (tckimlik.equals(pazarlamaci.getTCKimlik().toString())) {
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
                                                this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), pazarlamaci.getFotoName());
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


                                lines = this.getDosyaYazOku().dosyadanOku("pazarlamaci");
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
                                        if (tckimlik.equals(pazarlamaci.getTCKimlik().toString())) {
                                            //guncellenecek satir siliniyoe
                                            lines.remove(i);
                                            //yeni bilgi listeye ekleniyor
                                            lines.add(string);
                                            //yeni bilgilerle dosyaya yazma islemi gerceklestiriliyor.
                                            this.getDosyaYazOku().dosyaGuncelle("pazarlamaci", lines);
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
                    } catch (Exception e) {
                        this.getHata().setText("Toplam Satış alanına numerik ifadeler girin!");
                        System.out.println(e.getMessage());
                    }
                }
            } catch (Exception e) {
                this.getHata().setText("Aylık Hedef alanına numerik ifadeler girin!");
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void personelOlustur(Pazarlamaci pazarlamaci) {
        if (this.getAylikHedef().getText().equals("")) {
            this.getHata().setText("Aylık Hedef alanı boş bırakılamaz!");
        } else {
            try {
                parseInt(this.getAylikHedef().getText());
                if (this.getToplamSatis().getText().equals("")) {
                    this.getHata().setText("Toplam Satış alanı boş bırakılamaz!");
                } else {
                    try {
                        parseInt(this.getToplamSatis().getText());
                        if (this.personelKontrol()) {
                            this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
                        } else {
                            String string;
                            pazarlamaci.setToplamSatis(parseInt(this.getToplamSatis().getText()));
                            pazarlamaci.setAylikHedef(parseInt(this.getAylikHedef().getText()));
                            if (pazarlamaci.getFotoName().equals("")) {
                                pazarlamaci.setFotoName("unnamed.jpg");
                                string = "!" + pazarlamaci.getAdi() + "@" + pazarlamaci.getSoyadi() + "%" + pazarlamaci.getTCKimlik().toString()
                                        + "&" + pazarlamaci.getMaas() + "?" + pazarlamaci.getDepartman() + "*" + pazarlamaci.getIseGiris().toString()
                                        + "_" + pazarlamaci.getFotoName() + "<" + pazarlamaci.getToplamSatis() + "|" + pazarlamaci.getAylikHedef() + ">";
                            } else {
                                pazarlamaci.setFotoName(UUID.randomUUID().toString());
                                string = "!" + pazarlamaci.getAdi() + "@" + pazarlamaci.getSoyadi() + "%" + pazarlamaci.getTCKimlik().toString()
                                        + "&" + pazarlamaci.getMaas() + "?" + pazarlamaci.getDepartman() + "*" + pazarlamaci.getIseGiris().toString() + "_"
                                        + pazarlamaci.getFotoName() + ".jpg<" + pazarlamaci.getToplamSatis() + "|" + pazarlamaci.getAylikHedef() + ">";
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
                            this.getHata().setText("");

                        }
                    } catch (Exception e) {
                        this.getHata().setText("Toplam Satış alanına numerik ifadeler girin!");
                        System.out.println(e.getMessage());
                    }
                }
            } catch (Exception e) {
                this.getHata().setText("Aylık Hedef alanına numerik ifadeler girin!");
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void formTemizle() {
        super.formTemizle();
        this.getToplamSatis().setText("");
        this.getAylikHedef().setText("");
    }


    public TextField getAylikHedef() {
        return aylikHedef;
    }

    public void setAylikHedef(TextField aylikHedef) {
        this.aylikHedef = aylikHedef;
    }

    public TextField getToplamSatis() {
        return toplamSatis;
    }

    public void setToplamSatis(TextField toplamSatis) {
        this.toplamSatis = toplamSatis;
    }

    public TableView getPersonelTable() {
        return personelTable;
    }

    public void setPersonelTable(TableView personelTable) {
        this.personelTable = personelTable;
    }

    public TableColumn<Pazarlamaci, Long> getTableTC() {
        return tableTC;
    }

    public void setTableTC(TableColumn<Pazarlamaci, Long> tableTC) {
        this.tableTC = tableTC;
    }

    public TableColumn<Pazarlamaci, String> getTableAdi() {
        return tableAdi;
    }

    public void setTableAdi(TableColumn<Pazarlamaci, String> tableAdi) {
        this.tableAdi = tableAdi;
    }

    public TableColumn<Pazarlamaci, String> getTableSoyadi() {
        return tableSoyadi;
    }

    public void setTableSoyadi(TableColumn<Pazarlamaci, String> tableSoyadi) {
        this.tableSoyadi = tableSoyadi;
    }

    public TableColumn<Pazarlamaci, String> getTableDepartmani() {
        return tableDepartmani;
    }

    public void setTableDepartmani(TableColumn<Pazarlamaci, String> tableDepartmani) {
        this.tableDepartmani = tableDepartmani;
    }

    public TableColumn<Pazarlamaci, Date> getTableIsegiris() {
        return tableIsegiris;
    }

    public void setTableIsegiris(TableColumn<Pazarlamaci, Date> tableIsegiris) {
        this.tableIsegiris = tableIsegiris;
    }

    public ObservableList<Pazarlamaci> getList() {
        return list;
    }

    public void setList(ObservableList<Pazarlamaci> list) {
        this.list = list;
    }

    public Pazarlamaci getSelectedPazarlamaci() {
        return selectedPazarlamaci;
    }

    public void setSelectedPazarlamaci(Pazarlamaci selectedPazarlamaci) {
        this.selectedPazarlamaci = selectedPazarlamaci;
    }
}
