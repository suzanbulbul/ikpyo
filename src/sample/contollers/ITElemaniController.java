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
import sample.contollers.PersonelContoller;
import sample.entity.ITElemani;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static java.lang.Integer.parseInt;


public class ITElemaniController extends PersonelContoller {
    @FXML
    private TextField uzmalik;
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<ITElemani, Long> tableTC;
    @FXML
    private TableColumn<ITElemani, String> tableAdi;
    @FXML
    private TableColumn<ITElemani, String> tableSoyadi;
    @FXML
    private TableColumn<ITElemani, String> tableDepartmani;
    @FXML
    private TableColumn<ITElemani, Date> tableIsegiris;

    private ObservableList<ITElemani> list = FXCollections.observableArrayList();

    private ITElemani selectedITElemani;


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
                                                if (this.getDepartman().getText().equals("IT Elemani")) {
                                                    this.getITElemani().setAdi(this.getAdi().getText());
                                                    this.getITElemani().setSoyadi(this.getSoyadi().getText());
                                                    this.getITElemani().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getITElemani().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getITElemani().setDepartman(this.getDepartman().getText());
                                                    this.getITElemani().setFotoName(this.getFotoName().getText());
                                                    this.getITElemani().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((ITElemani) this.getITElemani());
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece IT Elemani departmanından eleman ekleyebilirsiniz!");
                                                }
                                            } else {
                                                if (this.getDepartman().getText().equals("IT Elemani")) {
                                                    this.personelGuncelle();
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece IT Elemani departmanından eleman düzeleyebilirsiniz!");
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
    public void personelTabosuGuncelle() {
        //ilk once mevcut tablomuzda ve listemizde herhangi bir veri varsa onlari sifirliyoruz.
        this.getPersonelTable().getItems().clear();
        this.getList().clear();
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("itelemani");
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
                ITElemani itElemani = new ITElemani(uzmanlik);
                itElemani.setAdi(ad);
                itElemani.setSoyadi(soyad);
                itElemani.setDepartman(depart);
                itElemani.setMaas(parseInt(maass));
                itElemani.setTCKimlik(Long.valueOf(tckimlik));
                itElemani.setIseGiris(LocalDate.parse(isegiris));
                itElemani.setFotoName(fotoName);
                this.getList().add(itElemani);
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
        this.getTableTC().setCellValueFactory(new PropertyValueFactory<ITElemani, Long>("TCKimlik"));
        //Tablomuzun tableAdi sutununa personel nesnemizdeki adi ozelligini aktariyoruz
        this.getTableAdi().setCellValueFactory(new PropertyValueFactory<ITElemani, String>("adi"));
        //Tablomuzun tableSoyadi sutununa personel nesnemizdeki soyadi ozelligini aktariyoruz
        this.getTableSoyadi().setCellValueFactory(new PropertyValueFactory<ITElemani, String>("soyadi"));
        //Tablomuzun tableDepartmani sutununa personel nesnemizdeki departman ozelligini aktariyoruz
        this.getTableDepartmani().setCellValueFactory(new PropertyValueFactory<ITElemani, String>("departman"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableIsegiris().setCellValueFactory(new PropertyValueFactory<ITElemani, Date>("iseGiris"));
    }

    @Override
    public void personelDetay(MouseEvent e) {
        super.personelDetay(e);
        // tablomuzda secilen verinin alinmiyor
        TableView.TableViewSelectionModel<ITElemani> selectionModel = this.getPersonelTable().getSelectionModel();

        // tablodam secilecek maks item sayisinin 1 olmasi gerektigini belirliyoruz
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        /*
         * tablodan secilen itemin bir personel nesnesi oldugu biliniyor ve personel nesnesine cast islemi gercekleserek
         * diger metodlardan da ulasilabilrmasi adina global selectedPersonel nesnesine aktariliyor.
         * */
        this.setSelectedITElemani((ITElemani) selectionModel.getSelectedItem());
        this.getUzmalik().setText(String.valueOf(this.getSelectedITElemani().getUzmanlik()));
    }

    @Override
    public void personelGuncelle() {
        if(this.getUzmalik().getText().equals("")){
            this.getHata().setText("Uzmanlık alanı boş olamaz!");
        }else {
            if (!this.personelKontrol()) {
                this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
            } else {
                ITElemani itElemani = new ITElemani();
                itElemani.setAdi(this.getAdi().getText());
                itElemani.setSoyadi(this.getSoyadi().getText());
                itElemani.setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                itElemani.setMaas(Integer.parseInt(this.getMaas().getText()));
                itElemani.setDepartman(this.getDepartman().getText());
                itElemani.setFotoName(this.getFotoName().getText());
                itElemani.setIseGiris(this.getIseGirisTarihi().getValue());
                itElemani.setUzmanlik(this.getUzmalik().getText());
                try {
                    if (this.personelKontrol()) {
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
                        if (itElemani.getFotoName().equals("")) {
                            itElemani.setFotoName(this.getSelectedPersonel().getFotoName());
                            string = "!" + itElemani.getAdi() + "@" + itElemani.getSoyadi() + "%" + itElemani.getTCKimlik().toString()
                                    + "&" + itElemani.getMaas() + "?" + itElemani.getDepartman() + "*" + itElemani.getIseGiris().toString() + "_" +
                                    itElemani.getFotoName() + "<" + itElemani.getUzmanlik() + "|";
                        } else {
                            itElemani.setFotoName(UUID.randomUUID().toString());
                            string = "!" + itElemani.getAdi() + "@" + itElemani.getSoyadi() + "%" + itElemani.getTCKimlik().toString()
                                    + "&" + itElemani.getMaas() + "?" + itElemani.getDepartman() + "*" + itElemani.getIseGiris().toString() + "_" +
                                    itElemani.getFotoName() + ".jpg<" + itElemani.getUzmanlik() + "|";
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
                                if (tckimlik.equals(itElemani.getTCKimlik().toString())) {
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
                                        this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), itElemani.getFotoName());
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


                        lines = this.getDosyaYazOku().dosyadanOku("itelemani");
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
                                if (tckimlik.equals(itElemani.getTCKimlik().toString())) {
                                    //guncellenecek satir siliniyoe
                                    lines.remove(i);
                                    //yeni bilgi listeye ekleniyor
                                    lines.add(string);
                                    //yeni bilgilerle dosyaya yazma islemi gerceklestiriliyor.
                                    this.getDosyaYazOku().dosyaGuncelle("itelemani", lines);
                                    //daha fazla islem yapilip zaman harcanmamasi adina i size'a esitlenerek diger satirlarin kontrol edilmesi engelleniyor
                                    i = size;
                                }
                            }
                        }
                        this.personelTabosuGuncelle();
                        this.formTemizle();
                        this.getHata().setText("");
                    }else{
                        this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel yok!");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    @Override
    public void personelOlustur(ITElemani itElemani) {
        if(this.getUzmalik().getText().equals("")){
            this.getHata().setText("Uzmanlık alanı boş olamaz!");
        }else {
            if (this.personelKontrol()) {
                this.getHata().setText("Bu kimlik numarasıyla kayıtlı bir personel zaten var!");
            } else {
                String string;
                itElemani.setUzmanlik(this.getUzmalik().getText());
                if (itElemani.getFotoName().equals("")) {
                    itElemani.setFotoName("unnamed.jpg");
                    string = "!" + itElemani.getAdi() + "@" + itElemani.getSoyadi() + "%" + itElemani.getTCKimlik().toString()
                            + "&" + itElemani.getMaas() + "?" + itElemani.getDepartman() + "*" + itElemani.getIseGiris().toString()
                            + "_" + itElemani.getFotoName() + "<" + itElemani.getUzmanlik() + "|";
                } else {
                    itElemani.setFotoName(UUID.randomUUID().toString());
                    string = "!" + itElemani.getAdi() + "@" + itElemani.getSoyadi() + "%" + itElemani.getTCKimlik().toString()
                            + "&" + itElemani.getMaas() + "?" + itElemani.getDepartman() + "*" + itElemani.getIseGiris().toString()
                            + "_" + itElemani.getFotoName() + "<" + itElemani.getUzmanlik() + "|";
                    File source = new File(this.getFotoUrl());
                    File dest = new File(Paths.get("").toString() + "\\src\\images\\" + itElemani.getFotoName() + ".jpg");
                    try {
                        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
    }

    @Override
    public void formTemizle() {
        super.formTemizle();
        this.getUzmalik().setText("");
    }

    /*Get - Set Metodları*/

    public sample.entity.ITElemani getSelectedITElemani() {
        return selectedITElemani;
    }

    public void setSelectedITElemani(sample.entity.ITElemani selectedITElemani) {
        this.selectedITElemani = selectedITElemani;
    }

    public TextField getUzmalik() {
        return uzmalik;
    }

    public void setUzmalik(TextField uzmalik) {
        this.uzmalik = uzmalik;
    }

    public TableView getPersonelTable() {
        return personelTable;
    }

    public void setPersonelTable(TableView personelTable) {
        this.personelTable = personelTable;
    }

    public TableColumn<sample.entity.ITElemani, Long> getTableTC() {
        return tableTC;
    }

    public void setTableTC(TableColumn<sample.entity.ITElemani, Long> tableTC) {
        this.tableTC = tableTC;
    }

    public TableColumn<sample.entity.ITElemani, String> getTableAdi() {
        return tableAdi;
    }

    public void setTableAdi(TableColumn<sample.entity.ITElemani, String> tableAdi) {
        this.tableAdi = tableAdi;
    }

    public TableColumn<sample.entity.ITElemani, String> getTableSoyadi() {
        return tableSoyadi;
    }

    public void setTableSoyadi(TableColumn<sample.entity.ITElemani, String> tableSoyadi) {
        this.tableSoyadi = tableSoyadi;
    }

    public TableColumn<sample.entity.ITElemani, String> getTableDepartmani() {
        return tableDepartmani;
    }

    public void setTableDepartmani(TableColumn<sample.entity.ITElemani, String> tableDepartmani) {
        this.tableDepartmani = tableDepartmani;
    }

    public TableColumn<sample.entity.ITElemani, Date> getTableIsegiris() {
        return tableIsegiris;
    }

    public void setTableIsegiris(TableColumn<sample.entity.ITElemani, Date> tableIsegiris) {
        this.tableIsegiris = tableIsegiris;
    }

    public ObservableList<sample.entity.ITElemani> getList() {
        return list;
    }

    public void setList(ObservableList<sample.entity.ITElemani> list) {
        this.list = list;
    }
}
