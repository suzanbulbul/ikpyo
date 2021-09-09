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
import sample.entity.Muhasebeci;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class MuhasebeciController extends PersonelContoller{
    @FXML
    private TextField zimmetEdilenMiktar;
    @FXML
    private TextField odeyecegiMiktar;
    @FXML
    private TextField geriGetirilenMiktar;
    @FXML
    private TableView personelTable;
    @FXML
    private TableColumn<Muhasebeci, Long> tableTC;
    @FXML
    private TableColumn<Muhasebeci, String> tableAdi;
    @FXML
    private TableColumn<Muhasebeci, String> tableSoyadi;
    @FXML
    private TableColumn<Muhasebeci, String> tableDepartmani;
    @FXML
    private TableColumn<Muhasebeci, Date> tableIsegiris;

    private ObservableList<Muhasebeci> list = FXCollections.observableArrayList();

    private Muhasebeci selectedMuhasebeci;

    @Override
    public void personelTabosuGuncelle() {
        //ilk once mevcut tablomuzda ve listemizde herhangi bir veri varsa onlari sifirliyoruz.
        this.getPersonelTable().getItems().clear();
        this.getList().clear();
        try {
            //dosyadaki tum satirlari okuyarak bir arraylist'e atiyoruz.
            List<String> lines = this.getDosyaYazOku().dosyadanOku("muhasebeci");
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
                String ZimmetEdilenMiktar = "";
                String odeyecegiMiktar = "";
                String geriGetirilenTutar = "";
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
                     * satirimizda geri getirilen tutar alani '<' simgesi ile baslayip '|' simgesi ile sona eriyor
                     * satirimizda odeyecegi miktar alani '|' simgesi ile baslayip '(' simgesi ile sona eriyor
                     * satirimizda zimmet edilen miktar alani '(' simgesi ile baslayip '>' simgesi ile sona eriyor
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
                            geriGetirilenTutar += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '|') {
                        j++;
                        while (str.charAt(j) != '(') {
                            odeyecegiMiktar += str.charAt(j++);
                        }
                    }
                    if (str.charAt(j) == '(') {
                        j++;
                        while (str.charAt(j) != '>') {
                            ZimmetEdilenMiktar += str.charAt(j++);
                        }
                    }
                }
                /*
                 * Bir satirin okunma islemi bitti ve personel nesnesi lusturularak tableview listesine
                 * ekleme islemi gerceklestirilecek
                 * */

                Muhasebeci muhasebeci = new Muhasebeci(parseInt(ZimmetEdilenMiktar), parseInt(odeyecegiMiktar), parseInt(geriGetirilenTutar));
                muhasebeci.setAdi(ad);
                muhasebeci.setSoyadi(soyad);
                muhasebeci.setDepartman(depart);
                muhasebeci.setMaas(parseInt(maass));
                muhasebeci.setTCKimlik(Long.valueOf(tckimlik));
                muhasebeci.setIseGiris(LocalDate.parse(isegiris));
                muhasebeci.setFotoName(fotoName);
                this.getList().add(muhasebeci);
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
        this.getTableTC().setCellValueFactory(new PropertyValueFactory<Muhasebeci, Long>("TCKimlik"));
        //Tablomuzun tableAdi sutununa personel nesnemizdeki adi ozelligini aktariyoruz
        this.getTableAdi().setCellValueFactory(new PropertyValueFactory<Muhasebeci, String>("adi"));
        //Tablomuzun tableSoyadi sutununa personel nesnemizdeki soyadi ozelligini aktariyoruz
        this.getTableSoyadi().setCellValueFactory(new PropertyValueFactory<Muhasebeci, String>("soyadi"));
        //Tablomuzun tableDepartmani sutununa personel nesnemizdeki departman ozelligini aktariyoruz
        this.getTableDepartmani().setCellValueFactory(new PropertyValueFactory<Muhasebeci, String>("departman"));
        //Tablomuzun tableIsegiris sutununa personel nesnemizdeki iseGiris ozelligini aktariyoruz
        this.getTableIsegiris().setCellValueFactory(new PropertyValueFactory<Muhasebeci, Date>("iseGiris"));
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
                                                if (this.getDepartman().getText().equals("Muhasebeci")) {
                                                    this.setMuhasebeci(new Muhasebeci(0,0,0));
                                                    this.getMuhasebeci().setAdi(this.getAdi().getText());
                                                    this.getMuhasebeci().setSoyadi(this.getSoyadi().getText());
                                                    this.getMuhasebeci().setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                                    this.getMuhasebeci().setMaas(Integer.parseInt(this.getMaas().getText()));
                                                    this.getMuhasebeci().setDepartman(this.getDepartman().getText());
                                                    this.getMuhasebeci().setFotoName(this.getFotoName().getText());
                                                    this.getMuhasebeci().setIseGiris(this.getIseGirisTarihi().getValue());
                                                    this.personelOlustur((Muhasebeci)this.getMuhasebeci());
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Muhasebeci departmanından eleman ekleyebilirsiniz!");
                                                }
                                            } else {
                                                if (this.getDepartman().getText().equals("Muhasebeci")) {
                                                    this.personelGuncelle();
                                                } else {
                                                    this.getHata().setText("Bu sayfadan sadece Muhasebeci departmanından eleman düzeleyebilirsiniz!");
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
        TableView.TableViewSelectionModel<Muhasebeci> selectionModel = this.getPersonelTable().getSelectionModel();

        // tablodam secilecek maks item sayisinin 1 olmasi gerektigini belirliyoruz
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        /*
         * tablodan secilen itemin bir personel nesnesi oldugu biliniyor ve personel nesnesine cast islemi gercekleserek
         * diger metodlardan da ulasilabilrmasi adina global selectedPersonel nesnesine aktariliyor.
         * */
        this.setSelectedMuhasebeci((Muhasebeci) selectionModel.getSelectedItem());
        this.getZimmetEdilenMiktar().setText(String.valueOf(this.getSelectedMuhasebeci().getZimmetEdilenMiktar()));
        this.getOdeyecegiMiktar().setText(String.valueOf(this.getSelectedMuhasebeci().getOdeyecegiMiktar()));
        this.getGeriGetirilenMiktar().setText(String.valueOf(this.getSelectedMuhasebeci().getGeriGetirilenTutar()));


        /* this.getFormuTemizle().setVisible(true);
        this.getIstenCikar().setVisible(true);
        this.getKisiyiGuncelle_yeniKisiEkle().setText("Personel Güncelle"); */
    }

    @Override
    public void personelGuncelle() {
        if(this.getOdeyecegiMiktar().getText().equals("")){
            this.getHata().setText("Odeyecegi Miktar alanı boş bırakılmaz!");
        }else{
            try{
                parseInt(this.getOdeyecegiMiktar().getText());
                if(this.getZimmetEdilenMiktar().getText().equals("")){
                    this.getHata().setText("Zimmet Edilen Miktar alanı boş bırakılmaz!");
                }else{
                    try{
                        parseInt(this.getOdeyecegiMiktar().getText());
                        if(this.getGeriGetirilenMiktar().getText().equals("")){
                            this.getHata().setText("Geri Getirilen Miktar alanı boş bırakılmaz!");
                        }else{
                            try{
                                parseInt(this.getOdeyecegiMiktar().getText());

                                Muhasebeci muhasebeci = new Muhasebeci(parseInt(this.getZimmetEdilenMiktar().getText()),
                                        parseInt(this.getZimmetEdilenMiktar().getText()),
                                        parseInt(this.getGeriGetirilenMiktar().getText()));
                                muhasebeci.setAdi(this.getAdi().getText());
                                muhasebeci.setSoyadi(this.getSoyadi().getText());
                                muhasebeci.setTCKimlik(Long.valueOf(this.getTcKimlikNo().getText()));
                                muhasebeci.setMaas(Integer.parseInt(this.getMaas().getText()));
                                muhasebeci.setDepartman(this.getDepartman().getText());
                                muhasebeci.setFotoName(this.getFotoName().getText());
                                muhasebeci.setIseGiris(this.getIseGirisTarihi().getValue());
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
                                        if (muhasebeci.getFotoName().equals("")) {
                                            muhasebeci.setFotoName(this.getSelectedPersonel().getFotoName());
                                            string = "!" + muhasebeci.getAdi() + "@" + muhasebeci.getSoyadi() + "%" + muhasebeci.getTCKimlik().toString()
                                                    + "&" + muhasebeci.getMaas() + "?" + muhasebeci.getDepartman() + "*" + muhasebeci.getIseGiris().toString() + "_" +
                                                    muhasebeci.getFotoName() + "<" + muhasebeci.getGeriGetirilenTutar() + "|"+ muhasebeci.getOdeyecegiMiktar() + "("+ muhasebeci.getZimmetEdilenMiktar() + ">";
                                        } else {
                                            muhasebeci.setFotoName(UUID.randomUUID().toString());
                                            string = "!" + muhasebeci.getAdi() + "@" + muhasebeci.getSoyadi() + "%" + muhasebeci.getTCKimlik().toString()
                                                    + "&" + muhasebeci.getMaas() + "?" + muhasebeci.getDepartman() + "*" + muhasebeci.getIseGiris().toString() + "_" +
                                                    muhasebeci.getFotoName() +  ".jpg<" + muhasebeci.getGeriGetirilenTutar() + "|"+ muhasebeci.getOdeyecegiMiktar() + "("+ muhasebeci.getZimmetEdilenMiktar() + ">";
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
                                                if (tckimlik.equals(muhasebeci.getTCKimlik().toString())) {
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
                                                        this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), muhasebeci.getFotoName());
                                                        System.out.println(23);
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


                                        lines = this.getDosyaYazOku().dosyadanOku("muhasebeci");
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
                                                if (tckimlik.equals(muhasebeci.getTCKimlik().toString())) {
                                                    //guncellenecek satir siliniyoe
                                                    lines.remove(i);
                                                    //yeni bilgi listeye ekleniyor
                                                    lines.add(string);
                                                    //yeni bilgilerle dosyaya yazma islemi gerceklestiriliyor.
                                                    this.getDosyaYazOku().dosyaGuncelle("muhasebeci", lines);
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
                                    this.getHata().setText("Bir hata ile karşılaşıldı!");
                                    System.out.println(ex.getMessage());
                                }
                            }catch(Exception e){
                                this.getHata().setText("Geri Getirilen Miktar alanına numerik ifadeler girin!");
                                System.out.println(e.getMessage());
                            }
                        }
                    }catch(Exception e){
                        this.getHata().setText("Zimmet Edilen Miktar alanına numerik ifadeler girin!");
                        System.out.println(e.getMessage());
                    }
                }
            }catch(Exception e){
                this.getHata().setText("Odeyecegi Miktar alanına numerik ifadeler girin!");
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void personelOlustur(Muhasebeci muhasebeci) {
        if(this.getOdeyecegiMiktar().getText().equals("")){
            this.getHata().setText("Odeyecegi Miktar alanı boş bırakılmaz!");
        }else{
            try{
                parseInt(this.getOdeyecegiMiktar().getText());
                if(this.getZimmetEdilenMiktar().getText().equals("")){
                    this.getHata().setText("Zimmet Edilen Miktar alanı boş bırakılmaz!");
                }else{
                    try{
                        parseInt(this.getOdeyecegiMiktar().getText());
                        if(this.getGeriGetirilenMiktar().getText().equals("")){
                            this.getHata().setText("Geri Getirilen Miktar alanı boş bırakılmaz!");
                        }else{
                            try{
                                parseInt(this.getOdeyecegiMiktar().getText());
                                String string;
                                muhasebeci.setGeriGetirilenTutar(parseInt(this.getGeriGetirilenMiktar().getText()));
                                muhasebeci.setOdeyecegiMiktar(parseInt(this.getOdeyecegiMiktar().getText()));
                                muhasebeci.setZimmetEdilenMiktar(parseInt(this.getZimmetEdilenMiktar().getText()));
                                if (muhasebeci.getFotoName().equals("")) {
                                    muhasebeci.setFotoName("unnamed.jpg");
                                    string = "!" + muhasebeci.getAdi() + "@" + muhasebeci.getSoyadi() + "%" + muhasebeci.getTCKimlik().toString()
                                            + "&" + muhasebeci.getMaas() + "?" + muhasebeci.getDepartman() + "*" + muhasebeci.getIseGiris().toString()
                                            + "_" + muhasebeci.getFotoName() + "<" + muhasebeci.getGeriGetirilenTutar() + "|" + muhasebeci.getOdeyecegiMiktar() + "(" + muhasebeci.getZimmetEdilenMiktar() + ">";
                                } else {
                                    muhasebeci.setFotoName(UUID.randomUUID().toString());
                                    string = "!" + muhasebeci.getAdi() + "@" + muhasebeci.getSoyadi() + "%" + muhasebeci.getTCKimlik().toString()
                                            + "&" + muhasebeci.getMaas() + "?" + muhasebeci.getDepartman() + "*" + muhasebeci.getIseGiris().toString() + "_"
                                            + muhasebeci.getFotoName() + ".jpg<" + muhasebeci.getGeriGetirilenTutar() + "|" + muhasebeci.getOdeyecegiMiktar() + "(" + muhasebeci.getZimmetEdilenMiktar() + ">";
                                    this.getDosyaYazOku().dosyaKopyala(this.getFotoUrl(), muhasebeci.getFotoName());
                                }

                                this.getDosyaYazOku().setYazilacakDeger(string);
                                try {
                                    this.getDosyaYazOku().dosyayaYaz("personel");
                                    this.getDosyaYazOku().dosyayaYaz("muhasebeci");
                                    this.personelTabosuGuncelle();
                                    this.formTemizle();
                                    this.getHata().setText("");
                                } catch (IOException e) {
                                    this.getHata().setText("Bir hata ile karşılaşıldı!");
                                    e.printStackTrace();
                                }
                            }catch(Exception e){
                                this.getHata().setText("Geri Getirilen Miktar alanına numerik ifadeler girin!");
                                System.out.println(e.getMessage());
                            }
                        }
                    }catch(Exception e){
                        this.getHata().setText("Zimmet Edilen Miktar alanına numerik ifadeler girin!");
                        System.out.println(e.getMessage());
                    }
                }
            }catch(Exception e){
                this.getHata().setText("Odeyecegi Miktar alanına numerik ifadeler girin!");
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void formTemizle() {
        super.formTemizle();
        this.getZimmetEdilenMiktar().setText("");
        this.getOdeyecegiMiktar().setText("");
        this.getGeriGetirilenMiktar().setText("");
    }

    //SET-GET

    public TextField getZimmetEdilenMiktar() {
        return zimmetEdilenMiktar;
    }

    public TextField getOdeyecegiMiktar() {
        return odeyecegiMiktar;
    }

    public TextField getGeriGetirilenMiktar() {
        return geriGetirilenMiktar;
    }

    public TableView getPersonelTable() {
        return personelTable;
    }

    public void setPersonelTable(TableView personelTable) {
        this.personelTable = personelTable;
    }

    public TableColumn<Muhasebeci, Long> getTableTC() {
        return tableTC;
    }

    public void setTableTC(TableColumn<Muhasebeci, Long> tableTC) {
        this.tableTC = tableTC;
    }

    public TableColumn<Muhasebeci, String> getTableAdi() {
        return tableAdi;
    }

    public void setTableAdi(TableColumn<Muhasebeci, String> tableAdi) {
        this.tableAdi = tableAdi;
    }

    public TableColumn<Muhasebeci, String> getTableSoyadi() {
        return tableSoyadi;
    }

    public void setTableSoyadi(TableColumn<Muhasebeci, String> tableSoyadi) {
        this.tableSoyadi = tableSoyadi;
    }

    public TableColumn<Muhasebeci, String> getTableDepartmani() {
        return tableDepartmani;
    }

    public void setTableDepartmani(TableColumn<Muhasebeci, String> tableDepartmani) {
        this.tableDepartmani = tableDepartmani;
    }

    public TableColumn<Muhasebeci, Date> getTableIsegiris() {
        return tableIsegiris;
    }

    public void setTableIsegiris(TableColumn<Muhasebeci, Date> tableIsegiris) {
        this.tableIsegiris = tableIsegiris;
    }

    public ObservableList<Muhasebeci> getList() {
        return list;
    }

    public void setList(ObservableList<Muhasebeci> list) {
        this.list = list;
    }

    public Muhasebeci getSelectedMuhasebeci() {
        return selectedMuhasebeci;
    }

    public void setSelectedMuhasebeci(Muhasebeci selectedMuhasebeci) {
        this.selectedMuhasebeci = selectedMuhasebeci;
    }
}
