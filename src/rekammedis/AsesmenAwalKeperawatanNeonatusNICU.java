package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;


/**
 *
 * @author perpustakaan
 */
public final class AsesmenAwalKeperawatanNeonatusNICU extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabModeMasalah,tabModeDetailMasalah,tabModeRencana,tabModeDetailRencana;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0,jml=0,index=0;
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private StringBuilder htmlContent;
    private String pilihan="";
    private boolean[] pilih; 
    private String[] kode,masalah;
    private String masalahkeperawatan="",finger=""; 
    private File file;
    private FileWriter fileWriter;
    private String iyem;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private FileReader myObj;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public AsesmenAwalKeperawatanNeonatusNICU(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Tgl.Lahir","J.K.","NIP Pengkaji","Nama Pengkaji",
            "Tgl.Asuhan",
            "Anamnesis","KetAnamnesis","AsalPasien","KeluhanUtama","DiagnosisMasuk","RiwayatKeluhanSaatIni","RiwayatPenggunaanObat","RiwayatKehamilanIbu","LahirDi","ProsesPersalinan","DitolongOleh","DitolongOlehLainLain","UsiaGestasi","Umur","BBL","PBL","RiwayatKesehatanSebelumnya","OpnameRS","SelamaHariOpname","RiwayatPembedahan","OperasiHari","RiwayatAlergi","SebutAlergi","Makanan","Imunisasi","RisikoGizi","BBLGizi","BBS","PBTB","LLA","LK","LD","LP","BentukDada","PernafasanSpontan","Frekuensi","SaturasiO2","Sesak","Retraksi","Sianosis","WarnaLendir","ccLendir","Wheezing","Ronchi","WSD","ccWSD","Suction","ccSuction","AlatBantuNafas","RespirasiJenisPernafasan","HasilRespirasi","KeteranganRespirasi","BunyiJantung","Pendarahan","JenisPendarahan","ccPendarahan","FemoralisKanan","FemoralisKiri","KeadaanSaatIni","Nadi","Ekstremitas","Kapiler","HasilLab","Intavena","HariIntavena","BeratBadan","PersenBeratBadan","Lendir","CaraMinum","ReflexIsap","WarnaKulit","Lidah","ReflexTelan","Abdomen","KeadaanSekarang","TurgorKulit","UbunUbun","Muntah","HasilLaboratorium","KeteranganHasilLab","Residu","Kesadaran","Kepala","ReflexTampak","Tangisan","KetTangisan","LingkarKepala","Kejang","PupilKiri","PupilKanan","JenisPupil","Gerakan","KetNeurosensori","BAK","ProduksiUrine","KeadaanReproduksi","AlatBantu","Vagina","Menstruasi","Kateter","Prominen","Ambigus","Preputium","Hipospadia","BAB","ccBAB","Feses","Anus","WarnaBAB","KondisiBAB","FrekuensiBAB","Mekonium","Suhu","Dehidrasi","Integritas","KondisiKebersihanDiri","WarnaKulitBersihDiri","SuhuBersihDiri","DerajatSuhu","KepalaBersihDiri","Umbilical","KukuBersihDiri","MataBersihDiri","TaliPusat","AbdomenKebersihanDIri","JenisBersihDiri","LokasiBersihDiri","BanyakTidur","KeteranganTidur","PolaTidur","StatusAnak","Kongenital","Lokasi","SebutKongenital","StatusGestasi","Berkunjung","KontakMata","Menyentuh","Berbicara","Menggendong","YangMerawat","LainLainRawat","RencanaPulang","LainLainDischargePlaning","SkalaResiko1","NilaiResiko1","SkalaResiko2","NilaiResiko2","SkalaResiko3","NilaiResiko3","SkalaResiko4","NilaiResiko4","SkalaResiko5","NilaiResiko5","SkalaResiko6","NilaiResiko6","NilaiResikoTotal","SkalaSydney1","NilaiSydney1","SkalaSydney2","NilaiSydney2","SkalaSydney3","NilaiSydney3","SkalaSydney4","NilaiSydney4","SkalaSydney5","NilaiSydney5","NilaiSydneyTotal","Rencana","Reflex Palmar Grasp","Reflex Babinski", "Reflex Plantar Grasp", "Reflex Moro", "Perawatan Diri (Mandi, BAB, BAK)", "Pemantauan pemberian obat", "Pemberian ASI", "Perawatan Metode Kangguru", "Perawatan luka", "Pendampingan tenaga khusus di rumah", "Kremer"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 188; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(160);
            }else if(i==3){
                column.setPreferredWidth(65);
            }else if(i==4){
                column.setPreferredWidth(25);
            }else if(i==5){
                column.setPreferredWidth(85);
            }else if(i==6){
                column.setPreferredWidth(150);
            }else if(i==7){
                column.setPreferredWidth(85);
            }else if(i==8){
                column.setPreferredWidth(150);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setPreferredWidth(150);
            }else if(i==11){
                column.setPreferredWidth(117);
            }else if(i==12){
                column.setPreferredWidth(78);
            }else if(i==13){
                column.setPreferredWidth(150);
            }else if(i==14){
                column.setPreferredWidth(110);
            }else if(i==15){
                column.setPreferredWidth(70);
            }else if(i==16){
                column.setPreferredWidth(220);
            }else if(i==17){
                column.setPreferredWidth(170);
            }else if(i==18){
                column.setPreferredWidth(170);
            }else if(i==19){
                column.setPreferredWidth(170);
            }else if(i==20){
                column.setPreferredWidth(150);
            }else if(i==21){
                column.setPreferredWidth(150);
            }else if(i==22){
                column.setPreferredWidth(125);
            }else if(i==23){
                column.setPreferredWidth(210);
            }else if(i==24){
                column.setPreferredWidth(130);
            }else if(i==25){
                column.setPreferredWidth(130);
            }else if(i==26){
                column.setPreferredWidth(48);
            }else if(i==27){
                column.setPreferredWidth(65);
            }else if(i==28){
                column.setPreferredWidth(44);
            }else if(i==29){
                column.setPreferredWidth(59);
            }else if(i==30){
                column.setPreferredWidth(61);
            }else if(i==31){
                column.setPreferredWidth(59);
            }else if(i==32){
                column.setPreferredWidth(120);
            }else if(i==33){
                column.setPreferredWidth(85);
            }else if(i==34){
                column.setPreferredWidth(64);
            }else if(i==35){
                column.setPreferredWidth(60);
            }else if(i==36){
                column.setPreferredWidth(74);
            }else if(i==37){
                column.setPreferredWidth(67);
            }else if(i==38){
                column.setPreferredWidth(52);
            }else if(i==39){
                column.setPreferredWidth(52);
            }else if(i==40){
                column.setPreferredWidth(44);
            }else if(i==41){
                column.setPreferredWidth(44);
            }else if(i==42){
                column.setPreferredWidth(150);
            }else if(i==43){
                column.setPreferredWidth(150);
            }else if(i==44){
                column.setPreferredWidth(106);
            }else if(i==45){
                column.setPreferredWidth(130);
            }else if(i==46){
                column.setPreferredWidth(65);
            }else if(i==47){
                column.setPreferredWidth(50);
            }else if(i==48){
                column.setPreferredWidth(130);
            }else if(i==49){
                column.setPreferredWidth(72);
            }else if(i==50){
                column.setPreferredWidth(54);
            }else if(i==51){
                column.setPreferredWidth(63);
            }else if(i==52){
                column.setPreferredWidth(69);
            }else if(i==53){
                column.setPreferredWidth(97);
            }else if(i==54){
                column.setPreferredWidth(75);
            }else if(i==55){
                column.setPreferredWidth(170);
            }else if(i==56){
                column.setPreferredWidth(70);
            }else if(i==57){
                column.setPreferredWidth(140);
            }else if(i==58){
                column.setPreferredWidth(140);
            }else if(i==59){
                column.setPreferredWidth(140);
            }else if(i==60){
                column.setPreferredWidth(140);
            }else if(i==61){
                column.setPreferredWidth(140);
            }else if(i==62){
                column.setPreferredWidth(111);
            }else if(i==63){
                column.setPreferredWidth(60);
            }else if(i==64){
                column.setPreferredWidth(60);
            }else if(i==65){
                column.setPreferredWidth(140);
            }else if(i==66){
                column.setPreferredWidth(119);
            }else if(i==67){
                column.setPreferredWidth(65);
            }else if(i==68){
                column.setPreferredWidth(74);
            }else if(i==69){
                column.setPreferredWidth(140);
            }else if(i==70){
                column.setPreferredWidth(41);
            }else if(i==71){
                column.setPreferredWidth(91);
            }else if(i==72){
                column.setPreferredWidth(66);
            }else if(i==73){
                column.setPreferredWidth(44);
            }else if(i==74){
                column.setPreferredWidth(159);
            }else if(i==75){
                column.setPreferredWidth(140);
            }else if(i==76){
                column.setPreferredWidth(94);
            }else if(i==77){
                column.setPreferredWidth(79);
            }else if(i==78){
                column.setPreferredWidth(140);
            }else if(i==79){
                column.setPreferredWidth(140);
            }else if(i==80){
                column.setPreferredWidth(79);
            }else if(i==81){
                column.setPreferredWidth(80);
            }else if(i==82){
                column.setPreferredWidth(85);
            }else if(i==83){
                column.setPreferredWidth(80);
            }else if(i==84){
                column.setPreferredWidth(79);
            }else if(i==85){
                column.setPreferredWidth(80);
            }else if(i==86){
                column.setPreferredWidth(80);
            }else if(i==87){
                column.setPreferredWidth(80);
            }else if(i==88){
                column.setPreferredWidth(103);
            }else if(i==89){
                column.setPreferredWidth(103);
            }else if(i==90){
                column.setPreferredWidth(103);
            }else if(i==91){
                column.setPreferredWidth(103);
            }else if(i==92){
                column.setPreferredWidth(103);
            }else if(i==93){
                column.setPreferredWidth(68);
            }else if(i==94){
                column.setPreferredWidth(90);
            }else if(i==95){
                column.setPreferredWidth(140);
            }else if(i==96){
                column.setPreferredWidth(65);
            }else if(i==97){
                column.setPreferredWidth(108);
            }else if(i==98){
                column.setPreferredWidth(120);
            }else if(i==99){
                column.setPreferredWidth(180);
            }else if(i==100){
                column.setPreferredWidth(67);
            }else if(i==101){
                column.setPreferredWidth(104);
            }else if(i==102){
                column.setPreferredWidth(140);
            }else if(i==103){
                column.setPreferredWidth(140);
            }else if(i==104){
                column.setPreferredWidth(170);
            }else if(i==105){
                column.setPreferredWidth(170);
            }else if(i==106){
                column.setPreferredWidth(161);
            }else if(i==107){
                column.setPreferredWidth(106);
            }else if(i==108){
                column.setPreferredWidth(250);
            }else if(i==109){
                column.setPreferredWidth(157);
            }else if(i==110){
                column.setPreferredWidth(105);
            }else if(i==111){
                column.setPreferredWidth(55);
            }else if(i==112){
                column.setPreferredWidth(140);
            }else if(i==113){
                column.setPreferredWidth(90);
            }else if(i==114){
                column.setPreferredWidth(90);
            }else if(i==115){
                column.setPreferredWidth(150);
            }else if(i==116){
                column.setPreferredWidth(110);
            }else if(i==117){
                column.setPreferredWidth(110);
            }else if(i==118){
                column.setPreferredWidth(95);
            }else if(i==119){
                column.setPreferredWidth(150);
            }else if(i==120){
                column.setPreferredWidth(80);
            }else if(i==121){
                column.setPreferredWidth(140);
            }else if(i==122){
                column.setPreferredWidth(140);
            }else if(i==123){
                column.setPreferredWidth(100);
            }else if(i==124){
                column.setPreferredWidth(85);
            }else if(i==125){
                column.setPreferredWidth(65);
            }else if(i==126){
                column.setPreferredWidth(80);
            }else if(i==127){
                column.setPreferredWidth(140);
            }else if(i==128){
                column.setPreferredWidth(140);
            }else if(i==129){
                column.setPreferredWidth(77);
            }else if(i==130){
                column.setPreferredWidth(40);
            }else if(i==131){
                column.setPreferredWidth(77);
            }else if(i==132){
                column.setPreferredWidth(40);
            }else if(i==133){
                column.setPreferredWidth(177);
            }else if(i==134){
                column.setPreferredWidth(40);
            }else if(i==135){
                column.setPreferredWidth(77);
            }else if(i==136){
                column.setPreferredWidth(40);
            }else if(i==137){
                column.setPreferredWidth(162);
            }else if(i==138){
                column.setPreferredWidth(40);
            }else if(i==139){
                column.setPreferredWidth(162);
            }else if(i==140){
                column.setPreferredWidth(40);
            }else if(i==141){
                column.setPreferredWidth(40);
            }else if(i==142){
                column.setPreferredWidth(82);
            }else if(i==143){
                column.setPreferredWidth(40);
            }else if(i==144){
                column.setPreferredWidth(82);
            }else if(i==145){
                column.setPreferredWidth(40);
            }else if(i==146){
                column.setPreferredWidth(82);
            }else if(i==147){
                column.setPreferredWidth(40);
            }else if(i==148){
                column.setPreferredWidth(82);
            }else if(i==149){
                column.setPreferredWidth(40);
            }else if(i==150){
                column.setPreferredWidth(82);
            }else if(i==151){
                column.setPreferredWidth(40);
            }else if(i==152){
                column.setPreferredWidth(82);
            }else if(i==153){
                column.setPreferredWidth(40);
            }else if(i==154){
                column.setPreferredWidth(82);
            }else if(i==155){
                column.setPreferredWidth(40);
            }else if(i==156){
                column.setPreferredWidth(82);
            }else if(i==157){
                column.setPreferredWidth(40);
            }else if(i==158){
                column.setPreferredWidth(82);
            }else if(i==159){
                column.setPreferredWidth(40);
            }else if(i==160){
                column.setPreferredWidth(86);
            }else if(i==161){
                column.setPreferredWidth(44);
            }else if(i==162){
                column.setPreferredWidth(86);
            }else if(i==163){
                column.setPreferredWidth(44);
            }else if(i==164){
                column.setPreferredWidth(44);
            }else if(i==165){
                column.setPreferredWidth(380);
            
            }else if(i==166){
                column.setPreferredWidth(317);
           
            
            }else if(i==167){
                column.setPreferredWidth(165);
            }else if(i==168){
                column.setPreferredWidth(149);
            }else if(i==169){
                column.setPreferredWidth(209);
            }else if(i==170){
                column.setPreferredWidth(107);
            }else if(i==171){
                column.setPreferredWidth(200);
            }else if(i==172){
                column.setPreferredWidth(200);
            }else if(i==173){
                column.setPreferredWidth(200);
            }else if(i==174){
                column.setPreferredWidth(200);
            }else if(i==175){
                column.setPreferredWidth(200);
            }else if(i==176){
                column.setPreferredWidth(200);
            }else if(i==177){
                column.setPreferredWidth(200);
//Penambahan
            }else if(i==178){
                column.setPreferredWidth(200);
            }else if(i==179){
                column.setPreferredWidth(200);
            }else if(i==180){
                column.setPreferredWidth(200);
            }else if(i==181){
                column.setPreferredWidth(200);
            }else if(i==182){
                column.setPreferredWidth(200);
            }else if(i==183){
                column.setPreferredWidth(200);
            }else if(i==184){
                column.setPreferredWidth(200);
            }else if(i==185){
                column.setPreferredWidth(200);
            }else if(i==186){
                column.setPreferredWidth(200);
            }else if(i==187){
                column.setPreferredWidth(200);
            }
            
            
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        KetAnamnesis.setDocument(new batasInput((int)3000).getKata(KetAnamnesis));
        KeluhanUtama.setDocument(new batasInput((int)3000).getKata(KeluhanUtama));
        DiagnosisMasuk.setDocument(new batasInput((int)3000).getKata(DiagnosisMasuk));
        RiwayatKeluhanSaatIni.setDocument(new batasInput((int)3000).getKata(RiwayatKeluhanSaatIni));
        RiwayatPenggunaanObat.setDocument(new batasInput((int)3000).getKata(RiwayatPenggunaanObat));
        RiwayatKehamilanIbu.setDocument(new batasInput((int)1000).getKata(RiwayatKehamilanIbu));
        DitolongOlehLainLain.setDocument(new batasInput((int)1000).getKata(DitolongOlehLainLain));
        OpnameRS.setDocument(new batasInput((int)100).getKata(OpnameRS));
        SelamaHariOpname.setDocument(new batasInput((int)100).getKata(SelamaHariOpname));
        UsiaGestasi.setDocument(new batasInput((int)100).getKata(UsiaGestasi));
        Umur.setDocument(new batasInput((int)100).getKata(Umur));
        LD.setDocument(new batasInput((int)100).getKata(LD));
        BBL.setDocument(new batasInput((int)100).getKata(BBL));
        PBL.setDocument(new batasInput((int)100).getKata(PBL));
        OperasiHari.setDocument(new batasInput((int)100).getKata(OperasiHari));
        SebutAlergi.setDocument(new batasInput((int)100).getKata(SebutAlergi));
        BBLGizi.setDocument(new batasInput((int)100).getKata(BBLGizi));
        BBS.setDocument(new batasInput((int)100).getKata(BBS));
        PBTB.setDocument(new batasInput((int)100).getKata(PBTB));
        LLA.setDocument(new batasInput((int)100).getKata(LLA));
        LK.setDocument(new batasInput((int)100).getKata(LK));
        LD.setDocument(new batasInput((int)100).getKata(LD));
        LP.setDocument(new batasInput((int)100).getKata(LP));
        SaturasiO2.setDocument(new batasInput((int)100).getKata(SaturasiO2));
        Frekuensi.setDocument(new batasInput((int)100).getKata(Frekuensi));
        WarnaLendir.setDocument(new batasInput((int)100).getKata(WarnaLendir));
        ccLendir.setDocument(new batasInput((int)100).getKata(ccLendir));
        ccWSD.setDocument(new batasInput((int)100).getKata(ccWSD));
        ccSuction.setDocument(new batasInput((int)100).getKata(ccSuction));
        HasilRespirasi.setDocument(new batasInput((int)100).getKata(HasilRespirasi));
        KeteranganRespirasi.setDocument(new batasInput((int)100).getKata(KeteranganRespirasi));
        JenisPendarahan.setDocument(new batasInput((int)100).getKata(JenisPendarahan));
        ccPendarahan.setDocument(new batasInput((int)100).getKata(ccPendarahan));
        Nadi.setDocument(new batasInput((int)100).getKata(Nadi));
        Kapiler.setDocument(new batasInput((int)100).getKata(Kapiler));
        HariIntavena.setDocument(new batasInput((int)100).getKata(HariIntavena));
        persenBeratBadan.setDocument(new batasInput((int)100).getKata(persenBeratBadan));
        Muntah.setDocument(new batasInput((int)100).getKata(Muntah));
        Residu.setDocument(new batasInput((int)100).getKata(Residu));
        Kremer.setDocument(new batasInput((int)100).getKata(Kremer));
        LingkarKepala.setDocument(new batasInput((int)100).getKata(LingkarKepala));
        JenisBersihDiri.setDocument(new batasInput((int)100).getKata(JenisBersihDiri));
        LokasiBersihDiri.setDocument(new batasInput((int)100).getKata(LokasiBersihDiri));
        PupilKiri.setDocument(new batasInput((int)100).getKata(PupilKiri));
        PupilKanan.setDocument(new batasInput((int)100).getKata(PupilKanan));
        KetNeurosensori.setDocument(new batasInput((int)100).getKata(KetNeurosensori));
        FrekuensiBAB.setDocument(new batasInput((int)100).getKata(FrekuensiBAB));        
        BAK.setDocument(new batasInput((int)100).getKata(BAK));
        ProduksiUrine.setDocument(new batasInput((int)100).getKata(ProduksiUrine));
        WarnaBAB.setDocument(new batasInput((int)100).getKata(WarnaBAB));
        Suhu.setDocument(new batasInput((int)100).getKata(Suhu));
        ccBAB.setDocument(new batasInput((int)100).getKata(ccBAB));
        FrekuensiBAB.setDocument(new batasInput((int)100).getKata(FrekuensiBAB));
        Mekonium.setDocument(new batasInput((int)100).getKata(Mekonium));
        Dehidrasi.setDocument(new batasInput((int)100).getKata(Dehidrasi));
        DerajatSuhu.setDocument(new batasInput((int)100).getKata(DerajatSuhu));
        KeteranganTidur.setDocument(new batasInput((int)100).getKata(KeteranganTidur));
        Lokasi.setDocument(new batasInput((int)100).getKata(Lokasi));
        SebutKongenital.setDocument(new batasInput((int)100).getKata(SebutKongenital));
        LainLainRawat.setDocument(new batasInput((int)100).getKata(LainLainRawat));
        LainLainDischargePlaning.setDocument(new batasInput((int)100).getKata(LainLainDischargePlaning));
        Rencana.setDocument(new batasInput((int)1000).getKata(Rencana));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){ 
                    if(i==1){
                        KdPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                        NmPetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());  
                     
                    }
                         
                }              
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        tabModeMasalah=new DefaultTableModel(null,new Object[]{
                "P","KODE","MASALAH KEPERAWATAN"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbMasalahKeperawatan.setModel(tabModeMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahKeperawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbMasalahKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (i = 0; i < 3; i++) {
            TableColumn column = tbMasalahKeperawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==2){
                column.setPreferredWidth(350);
            }
        }
        tbMasalahKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeRencana=new DefaultTableModel(null,new Object[]{
                "P","KODE","RENCANA KEPERAWATAN"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRencanaKeperawatan.setModel(tabModeRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaKeperawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRencanaKeperawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (i = 0; i < 3; i++) {
            TableColumn column = tbRencanaKeperawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==2){
                column.setPreferredWidth(350);
            }
        }
        tbRencanaKeperawatan.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDetailMasalah=new DefaultTableModel(null,new Object[]{
                "Kode","Masalah Keperawatan"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbMasalahDetail.setModel(tabModeDetailMasalah);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbMasalahDetail.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbMasalahDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbMasalahDetail.getColumnModel().getColumn(i);
            if(i==0){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(750);
            }
        }
        tbMasalahDetail.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDetailRencana=new DefaultTableModel(null,new Object[]{
                "Kode","Rencana Keperawatan"
            }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbRencanaDetail.setModel(tabModeDetailRencana);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbRencanaDetail.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRencanaDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 2; i++) {
            TableColumn column = tbRencanaDetail.getColumnModel().getColumn(i);
            if(i==0){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==1){
                column.setPreferredWidth(420);
            }
        }
        tbRencanaDetail.setDefaultRenderer(Object.class, new WarnaTable());
        
        ChkAccor.setSelected(false);
        isMenu();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        label14 = new widget.Label();
        KdPetugas = new widget.TextBox();
        NmPetugas = new widget.TextBox();
        BtnPetugas = new widget.Button();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        Jk = new widget.TextBox();
        jLabel10 = new widget.Label();
        label11 = new widget.Label();
        jLabel11 = new widget.Label();
        jLabel36 = new widget.Label();
        Anamnesis = new widget.ComboBox();
        TglAsuhan = new widget.Tanggal();
        AsalPasien = new widget.ComboBox();
        jLabel38 = new widget.Label();
        jLabel9 = new widget.Label();
        scrollPane1 = new widget.ScrollPane();
        KeluhanUtama = new widget.TextArea();
        jLabel30 = new widget.Label();
        scrollPane2 = new widget.ScrollPane();
        RiwayatKeluhanSaatIni = new widget.TextArea();
        jLabel31 = new widget.Label();
        scrollPane3 = new widget.ScrollPane();
        DiagnosisMasuk = new widget.TextArea();
        jLabel32 = new widget.Label();
        scrollPane4 = new widget.ScrollPane();
        RiwayatPenggunaanObat = new widget.TextArea();
        jSeparator2 = new javax.swing.JSeparator();
        KetAnamnesis = new widget.TextBox();
        DitolongOlehLainLain = new widget.TextBox();
        RiwayatKehamilanIbu = new widget.TextBox();
        jLabel42 = new widget.Label();
        jLabel43 = new widget.Label();
        LahirDi = new widget.ComboBox();
        ProsesPersalinan = new widget.ComboBox();
        jLabel44 = new widget.Label();
        jLabel124 = new widget.Label();
        RiwayatKesehatanSebelumnya = new widget.ComboBox();
        OpnameRS = new widget.TextBox();
        jLabel125 = new widget.Label();
        RiwayatAlergi = new widget.ComboBox();
        SelamaHariOpname = new widget.TextBox();
        jLabel127 = new widget.Label();
        Makanan = new widget.ComboBox();
        jLabel128 = new widget.Label();
        jLabel129 = new widget.Label();
        Imunisasi = new widget.ComboBox();
        jLabel47 = new widget.Label();
        UsiaGestasi = new widget.TextBox();
        jLabel28 = new widget.Label();
        Umur = new widget.TextBox();
        jLabel22 = new widget.Label();
        LD = new widget.TextBox();
        jLabel23 = new widget.Label();
        jLabel17 = new widget.Label();
        jLabel26 = new widget.Label();
        BBLGizi = new widget.TextBox();
        jLabel25 = new widget.Label();
        jLabel18 = new widget.Label();
        BBS = new widget.TextBox();
        jLabel20 = new widget.Label();
        jLabel24 = new widget.Label();
        PBTB = new widget.TextBox();
        jLabel29 = new widget.Label();
        jLabel12 = new widget.Label();
        LLA = new widget.TextBox();
        jLabel13 = new widget.Label();
        jLabel15 = new widget.Label();
        LK = new widget.TextBox();
        jLabel48 = new widget.Label();
        jLabel27 = new widget.Label();
        jLabel131 = new widget.Label();
        BentukDada = new widget.ComboBox();
        SaturasiO2 = new widget.TextBox();
        jLabel132 = new widget.Label();
        PernafasanSpontan = new widget.ComboBox();
        Frekuensi = new widget.TextBox();
        jLabel139 = new widget.Label();
        Sesak = new widget.ComboBox();
        AlatBantuNafas = new widget.ComboBox();
        jLabel140 = new widget.Label();
        jLabel141 = new widget.Label();
        Ekstremitas = new widget.ComboBox();
        jLabel142 = new widget.Label();
        KeadaanSaatIni = new widget.ComboBox();
        jLabel143 = new widget.Label();
        BunyiJantung = new widget.ComboBox();
        jLabel144 = new widget.Label();
        RespirasiJenisPernafasan = new widget.ComboBox();
        WarnaLendir = new widget.TextBox();
        jLabel145 = new widget.Label();
        Pendarahan = new widget.ComboBox();
        jLabel146 = new widget.Label();
        FemoralisKanan = new widget.ComboBox();
        Kapiler = new widget.TextBox();
        BeratBadan = new widget.ComboBox();
        jLabel148 = new widget.Label();
        jLabel149 = new widget.Label();
        CaraMinum = new widget.ComboBox();
        jLabel147 = new widget.Label();
        FemoralisKiri = new widget.ComboBox();
        jLabel150 = new widget.Label();
        HasilLab = new widget.ComboBox();
        jLabel151 = new widget.Label();
        Intavena = new widget.ComboBox();
        HariIntavena = new widget.TextBox();
        jLabel152 = new widget.Label();
        KeadaanSekarang = new widget.ComboBox();
        jLabel153 = new widget.Label();
        TurgorKulit = new widget.ComboBox();
        jLabel154 = new widget.Label();
        UbunUbun = new widget.ComboBox();
        jLabel155 = new widget.Label();
        Lendir = new widget.ComboBox();
        jLabel156 = new widget.Label();
        Abdomen = new widget.ComboBox();
        Muntah = new widget.TextBox();
        jLabel157 = new widget.Label();
        Lidah = new widget.ComboBox();
        jLabel158 = new widget.Label();
        HasilLaboratorium = new widget.ComboBox();
        jLabel159 = new widget.Label();
        WarnaKulit = new widget.ComboBox();
        Residu = new widget.TextBox();
        jLabel164 = new widget.Label();
        Kesadaran = new widget.ComboBox();
        Kremer = new widget.TextBox();
        ProduksiUrine = new widget.TextBox();
        Tangisan = new widget.ComboBox();
        jLabel165 = new widget.Label();
        jLabel166 = new widget.Label();
        Kepala = new widget.ComboBox();
        KetTangisan = new widget.ComboBox();
        JenisPupil = new widget.ComboBox();
        jLabel113 = new widget.Label();
        LingkarKepala = new widget.TextBox();
        jLabel114 = new widget.Label();
        PupilKiri = new widget.TextBox();
        jLabel115 = new widget.Label();
        PupilKanan = new widget.TextBox();
        jLabel117 = new widget.Label();
        BAK = new widget.TextBox();
        jLabel118 = new widget.Label();
        jLabel119 = new widget.Label();
        KetNeurosensori = new widget.TextBox();
        jLabel170 = new widget.Label();
        Kejang = new widget.ComboBox();
        jLabel171 = new widget.Label();
        jLabel172 = new widget.Label();
        jLabel173 = new widget.Label();
        jLabel174 = new widget.Label();
        Gerakan = new widget.ComboBox();
        ReflexTampak = new widget.ComboBox();
        KeadaanReproduksi = new widget.ComboBox();
        Vagina = new widget.ComboBox();
        AlatBantu = new widget.ComboBox();
        jLabel179 = new widget.Label();
        Menstruasi = new widget.ComboBox();
        jLabel181 = new widget.Label();
        BAB = new widget.ComboBox();
        WarnaBAB = new widget.TextBox();
        jLabel182 = new widget.Label();
        Preputium = new widget.ComboBox();
        jLabel183 = new widget.Label();
        KondisiBAB = new widget.ComboBox();
        jLabel184 = new widget.Label();
        Feses = new widget.ComboBox();
        Suhu = new widget.TextBox();
        Hipospadia = new widget.ComboBox();
        ccBAB = new widget.TextBox();
        jLabel186 = new widget.Label();
        Anus = new widget.ComboBox();
        FrekuensiBAB = new widget.TextBox();
        jLabel187 = new widget.Label();
        Berkunjung = new widget.ComboBox();
        Mekonium = new widget.TextBox();
        jLabel188 = new widget.Label();
        Kongenital = new widget.ComboBox();
        jLabel189 = new widget.Label();
        KondisiKebersihanDiri = new widget.ComboBox();
        jLabel191 = new widget.Label();
        Integritas = new widget.ComboBox();
        Dehidrasi = new widget.TextBox();
        KukuBersihDiri = new widget.ComboBox();
        jLabel193 = new widget.Label();
        TaliPusat = new widget.ComboBox();
        LainLainRawat = new widget.TextBox();
        WarnaKulitBersihDiri = new widget.ComboBox();
        DerajatSuhu = new widget.TextBox();
        jLabel196 = new widget.Label();
        jLabel197 = new widget.Label();
        LokasiBersihDiri = new widget.TextBox();
        SuhuBersihDiri = new widget.ComboBox();
        KeteranganTidur = new widget.TextBox();
        jLabel199 = new widget.Label();
        JenisBersihDiri = new widget.TextBox();
        SebutKongenital = new widget.TextBox();
        jLabel201 = new widget.Label();
        KepalaBersihDiri = new widget.ComboBox();
        jLabel202 = new widget.Label();
        MataBersihDiri = new widget.ComboBox();
        Umbilical = new widget.ComboBox();
        AbdomenKebersihanDIri = new widget.ComboBox();
        jLabel205 = new widget.Label();
        PolaTidur = new widget.ComboBox();
        Lokasi = new widget.TextBox();
        jLabel208 = new widget.Label();
        KontakMata = new widget.ComboBox();
        jLabel209 = new widget.Label();
        jLabel210 = new widget.Label();
        StatusGestasi = new widget.ComboBox();
        jLabel213 = new widget.Label();
        BanyakTidur = new widget.ComboBox();
        jLabel214 = new widget.Label();
        StatusAnak = new widget.ComboBox();
        jLabel217 = new widget.Label();
        SkalaResiko1 = new widget.ComboBox();
        jLabel218 = new widget.Label();
        NilaiResiko1 = new widget.TextBox();
        jLabel219 = new widget.Label();
        jLabel220 = new widget.Label();
        jLabel221 = new widget.Label();
        SkalaResiko2 = new widget.ComboBox();
        jLabel222 = new widget.Label();
        NilaiResiko2 = new widget.TextBox();
        jLabel223 = new widget.Label();
        jLabel224 = new widget.Label();
        SkalaResiko3 = new widget.ComboBox();
        jLabel225 = new widget.Label();
        NilaiResiko3 = new widget.TextBox();
        jLabel226 = new widget.Label();
        jLabel227 = new widget.Label();
        SkalaResiko4 = new widget.ComboBox();
        jLabel228 = new widget.Label();
        NilaiResiko4 = new widget.TextBox();
        jLabel229 = new widget.Label();
        jLabel230 = new widget.Label();
        SkalaResiko5 = new widget.ComboBox();
        jLabel231 = new widget.Label();
        NilaiResiko5 = new widget.TextBox();
        jLabel232 = new widget.Label();
        jLabel233 = new widget.Label();
        SkalaResiko6 = new widget.ComboBox();
        jLabel234 = new widget.Label();
        NilaiResiko6 = new widget.TextBox();
        jLabel235 = new widget.Label();
        NilaiResikoTotal = new widget.TextBox();
        jLabel236 = new widget.Label();
        jLabel237 = new widget.Label();
        SkalaSydney1 = new widget.ComboBox();
        jLabel238 = new widget.Label();
        NilaiSydney1 = new widget.TextBox();
        jLabel239 = new widget.Label();
        jLabel240 = new widget.Label();
        SkalaSydney2 = new widget.ComboBox();
        jLabel241 = new widget.Label();
        NilaiSydney2 = new widget.TextBox();
        jLabel242 = new widget.Label();
        jLabel243 = new widget.Label();
        SkalaSydney3 = new widget.ComboBox();
        jLabel244 = new widget.Label();
        NilaiSydney3 = new widget.TextBox();
        jLabel245 = new widget.Label();
        jLabel246 = new widget.Label();
        SkalaSydney4 = new widget.ComboBox();
        jLabel247 = new widget.Label();
        NilaiSydney4 = new widget.TextBox();
        jLabel248 = new widget.Label();
        jLabel249 = new widget.Label();
        SkalaSydney5 = new widget.ComboBox();
        jLabel250 = new widget.Label();
        NilaiSydney5 = new widget.TextBox();
        NilaiSydneyTotal = new widget.TextBox();
        jLabel270 = new widget.Label();
        jSeparator12 = new javax.swing.JSeparator();
        Scroll6 = new widget.ScrollPane();
        tbMasalahKeperawatan = new widget.Table();
        TabRencanaKeperawatan = new javax.swing.JTabbedPane();
        panelBiasa1 = new widget.PanelBiasa();
        Scroll8 = new widget.ScrollPane();
        tbRencanaKeperawatan = new widget.Table();
        scrollPane5 = new widget.ScrollPane();
        Rencana = new widget.TextArea();
        BtnTambahMasalah = new widget.Button();
        BtnAllMasalah = new widget.Button();
        BtnCariMasalah = new widget.Button();
        TCariMasalah = new widget.TextBox();
        BtnTambahRencana = new widget.Button();
        BtnAllRencana = new widget.Button();
        BtnCariRencana = new widget.Button();
        label13 = new widget.Label();
        TCariRencana = new widget.TextBox();
        label12 = new widget.Label();
        jLabel284 = new widget.Label();
        jLabel285 = new widget.Label();
        jLabel286 = new widget.Label();
        jLabel288 = new widget.Label();
        jLabel59 = new widget.Label();
        DitolongOleh = new widget.ComboBox();
        jLabel290 = new widget.Label();
        jLabel37 = new widget.Label();
        BBL = new widget.TextBox();
        jLabel291 = new widget.Label();
        jLabel40 = new widget.Label();
        PBL = new widget.TextBox();
        jLabel292 = new widget.Label();
        jLabel293 = new widget.Label();
        RiwayatPembedahan = new widget.ComboBox();
        OperasiHari = new widget.TextBox();
        SebutAlergi = new widget.TextBox();
        RisikoGizi = new widget.ComboBox();
        jLabel39 = new widget.Label();
        jLabel41 = new widget.Label();
        LP = new widget.TextBox();
        jLabel45 = new widget.Label();
        jLabel289 = new widget.Label();
        jLabel46 = new widget.Label();
        jLabel294 = new widget.Label();
        jLabel60 = new widget.Label();
        jLabel61 = new widget.Label();
        jLabel295 = new widget.Label();
        Retraksi = new widget.ComboBox();
        jLabel296 = new widget.Label();
        Sianosis = new widget.ComboBox();
        HasilRespirasi = new widget.TextBox();
        jLabel133 = new widget.Label();
        ccLendir = new widget.TextBox();
        jLabel63 = new widget.Label();
        jLabel297 = new widget.Label();
        Wheezing = new widget.ComboBox();
        jLabel298 = new widget.Label();
        Ronchi = new widget.ComboBox();
        jLabel134 = new widget.Label();
        ccWSD = new widget.TextBox();
        jLabel64 = new widget.Label();
        WSD = new widget.ComboBox();
        ccSuction = new widget.TextBox();
        jLabel135 = new widget.Label();
        Suction = new widget.ComboBox();
        jLabel65 = new widget.Label();
        jLabel136 = new widget.Label();
        jLabel137 = new widget.Label();
        KeteranganRespirasi = new widget.TextBox();
        jLabel33 = new widget.Label();
        jLabel62 = new widget.Label();
        jLabel251 = new widget.Label();
        jLabel252 = new widget.Label();
        jLabel66 = new widget.Label();
        jLabel253 = new widget.Label();
        Nadi = new widget.TextBox();
        jLabel67 = new widget.Label();
        jLabel254 = new widget.Label();
        JenisPendarahan = new widget.TextBox();
        ccPendarahan = new widget.TextBox();
        jLabel69 = new widget.Label();
        jLabel255 = new widget.Label();
        jLabel35 = new widget.Label();
        persenBeratBadan = new widget.TextBox();
        jLabel70 = new widget.Label();
        jLabel256 = new widget.Label();
        ReflexIsap = new widget.ComboBox();
        jLabel257 = new widget.Label();
        ReflexTelan = new widget.ComboBox();
        jLabel68 = new widget.Label();
        jLabel258 = new widget.Label();
        jLabel71 = new widget.Label();
        jLabel259 = new widget.Label();
        jLabel260 = new widget.Label();
        jLabel49 = new widget.Label();
        jLabel72 = new widget.Label();
        jLabel126 = new widget.Label();
        jLabel50 = new widget.Label();
        jLabel130 = new widget.Label();
        jLabel138 = new widget.Label();
        jLabel160 = new widget.Label();
        jLabel161 = new widget.Label();
        jLabel180 = new widget.Label();
        Kateter = new widget.ComboBox();
        jLabel261 = new widget.Label();
        Prominen = new widget.ComboBox();
        jLabel262 = new widget.Label();
        Ambigus = new widget.ComboBox();
        jLabel162 = new widget.Label();
        jLabel263 = new widget.Label();
        jLabel120 = new widget.Label();
        jLabel121 = new widget.Label();
        jLabel163 = new widget.Label();
        jLabel264 = new widget.Label();
        jLabel185 = new widget.Label();
        jLabel167 = new widget.Label();
        jLabel265 = new widget.Label();
        jLabel168 = new widget.Label();
        jLabel169 = new widget.Label();
        jLabel266 = new widget.Label();
        jLabel190 = new widget.Label();
        jLabel267 = new widget.Label();
        jLabel175 = new widget.Label();
        jLabel192 = new widget.Label();
        jLabel203 = new widget.Label();
        jLabel195 = new widget.Label();
        jLabel268 = new widget.Label();
        jLabel269 = new widget.Label();
        Menyentuh = new widget.ComboBox();
        jLabel280 = new widget.Label();
        Berbicara = new widget.ComboBox();
        jLabel281 = new widget.Label();
        Menggendong = new widget.ComboBox();
        jLabel282 = new widget.Label();
        YangMerawat = new widget.ComboBox();
        jLabel194 = new widget.Label();
        jLabel283 = new widget.Label();
        PerawatanDiri = new widget.ComboBox();
        LainLainDischargePlaning = new widget.TextBox();
        jLabel198 = new widget.Label();
        jLabel200 = new widget.Label();
        ReflexMoro = new javax.swing.JComboBox<>();
        jLabel176 = new widget.Label();
        ReflexBabinski = new javax.swing.JComboBox<>();
        jLabel177 = new widget.Label();
        ReflexPlantarGrasp = new javax.swing.JComboBox<>();
        jLabel178 = new widget.Label();
        reflexpalmargrasp = new javax.swing.JComboBox<>();
        jLabel204 = new widget.Label();
        PanelWall1 = new usu.widget.glass.PanelGlass();
        jLabel287 = new widget.Label();
        PemantauanPemberianObat = new widget.ComboBox();
        jLabel299 = new widget.Label();
        PemberianASI = new widget.ComboBox();
        jLabel300 = new widget.Label();
        PerawatanMetodeKangguru = new widget.ComboBox();
        jLabel301 = new widget.Label();
        PerawatanLuka = new widget.ComboBox();
        jLabel302 = new widget.Label();
        PendampinganTenagaKhususDiRumah = new widget.ComboBox();
        jLabel303 = new widget.Label();
        jLabel304 = new widget.Label();
        KeteranganHasilLab1 = new widget.TextBox();
        jLabel206 = new widget.Label();
        internalFrame3 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        FormMenu = new widget.PanelBiasa();
        jLabel34 = new widget.Label();
        TNoRM1 = new widget.TextBox();
        TPasien1 = new widget.TextBox();
        BtnPrint1 = new widget.Button();
        FormMasalahRencana = new widget.PanelBiasa();
        Scroll7 = new widget.ScrollPane();
        tbMasalahDetail = new widget.Table();
        Scroll9 = new widget.ScrollPane();
        tbRencanaDetail = new widget.Table();
        scrollPane6 = new widget.ScrollPane();
        DetailRencana = new widget.TextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Penilaian Awal Keperawatan NICU ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnAll);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        internalFrame1.add(panelGlass8, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(254, 255, 254));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(255, 255, 255));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(870, 3000));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(74, 10, 131, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        FormInput.add(TPasien);
        TPasien.setBounds(309, 10, 260, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        FormInput.add(TNoRM);
        TNoRM.setBounds(207, 10, 100, 23);

        label14.setText("Pengkaji :");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 70, 23);

        KdPetugas.setEditable(false);
        KdPetugas.setName("KdPetugas"); // NOI18N
        KdPetugas.setPreferredSize(new java.awt.Dimension(80, 23));
        KdPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPetugasKeyPressed(evt);
            }
        });
        FormInput.add(KdPetugas);
        KdPetugas.setBounds(74, 40, 110, 23);

        NmPetugas.setEditable(false);
        NmPetugas.setName("NmPetugas"); // NOI18N
        NmPetugas.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NmPetugas);
        NmPetugas.setBounds(190, 40, 230, 23);

        BtnPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPetugas.setMnemonic('2');
        BtnPetugas.setToolTipText("Alt+2");
        BtnPetugas.setName("BtnPetugas"); // NOI18N
        BtnPetugas.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPetugasActionPerformed(evt);
            }
        });
        BtnPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPetugasKeyPressed(evt);
            }
        });
        FormInput.add(BtnPetugas);
        BtnPetugas.setBounds(420, 40, 28, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(580, 10, 60, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(644, 10, 80, 23);

        Jk.setEditable(false);
        Jk.setHighlighter(null);
        Jk.setName("Jk"); // NOI18N
        FormInput.add(Jk);
        Jk.setBounds(774, 10, 80, 23);

        jLabel10.setText("No.Rawat :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput.add(jLabel10);
        jLabel10.setBounds(0, 10, 70, 23);

        label11.setText("Tanggal :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label11);
        label11.setBounds(450, 40, 70, 23);

        jLabel11.setText("J.K. :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(740, 10, 30, 23);

        jLabel36.setText("Anamnesis :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(0, 70, 70, 23);

        Anamnesis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Orang Tua", "Orang Lain" }));
        Anamnesis.setName("Anamnesis"); // NOI18N
        Anamnesis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AnamnesisKeyPressed(evt);
            }
        });
        FormInput.add(Anamnesis);
        Anamnesis.setBounds(80, 70, 120, 23);

        TglAsuhan.setForeground(new java.awt.Color(50, 70, 50));
        TglAsuhan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-12-2023 09:50:49" }));
        TglAsuhan.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        TglAsuhan.setName("TglAsuhan"); // NOI18N
        TglAsuhan.setOpaque(false);
        TglAsuhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TglAsuhanKeyPressed(evt);
            }
        });
        FormInput.add(TglAsuhan);
        TglAsuhan.setBounds(530, 40, 140, 23);

        AsalPasien.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Poliklinik", "IGD", "K. Bersalin", "R. Gabung", "OK Obgyn" }));
        AsalPasien.setName("AsalPasien"); // NOI18N
        AsalPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AsalPasienKeyPressed(evt);
            }
        });
        FormInput.add(AsalPasien);
        AsalPasien.setBounds(530, 70, 140, 23);

        jLabel38.setText("Asal Pasien :");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(450, 70, 70, 23);

        jLabel9.setText("Riwayat Penggunaan Obat :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput.add(jLabel9);
        jLabel9.setBounds(450, 200, 150, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setName("scrollPane1"); // NOI18N

        KeluhanUtama.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        KeluhanUtama.setColumns(20);
        KeluhanUtama.setRows(5);
        KeluhanUtama.setName("KeluhanUtama"); // NOI18N
        KeluhanUtama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeluhanUtamaKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(KeluhanUtama);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(179, 150, 280, 43);

        jLabel30.setText("Keluhan Utama :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(0, 150, 175, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        RiwayatKeluhanSaatIni.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RiwayatKeluhanSaatIni.setColumns(20);
        RiwayatKeluhanSaatIni.setRows(5);
        RiwayatKeluhanSaatIni.setName("RiwayatKeluhanSaatIni"); // NOI18N
        RiwayatKeluhanSaatIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatKeluhanSaatIniKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(RiwayatKeluhanSaatIni);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(180, 200, 280, 43);

        jLabel31.setText("Riwayat Keluhan Saat ini :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(0, 200, 170, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        DiagnosisMasuk.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        DiagnosisMasuk.setColumns(20);
        DiagnosisMasuk.setRows(5);
        DiagnosisMasuk.setName("DiagnosisMasuk"); // NOI18N
        DiagnosisMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiagnosisMasukKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(DiagnosisMasuk);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(604, 150, 250, 43);

        jLabel32.setText("Diagnosis Masuk :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(460, 150, 140, 23);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane4.setName("scrollPane4"); // NOI18N

        RiwayatPenggunaanObat.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RiwayatPenggunaanObat.setColumns(20);
        RiwayatPenggunaanObat.setRows(5);
        RiwayatPenggunaanObat.setName("RiwayatPenggunaanObat"); // NOI18N
        RiwayatPenggunaanObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatPenggunaanObatKeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(RiwayatPenggunaanObat);

        FormInput.add(scrollPane4);
        scrollPane4.setBounds(604, 200, 250, 70);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(0, 101, 880, 3);

        KetAnamnesis.setFocusTraversalPolicyProvider(true);
        KetAnamnesis.setName("KetAnamnesis"); // NOI18N
        KetAnamnesis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetAnamnesisKeyPressed(evt);
            }
        });
        FormInput.add(KetAnamnesis);
        KetAnamnesis.setBounds(210, 70, 210, 23);

        DitolongOlehLainLain.setFocusTraversalPolicyProvider(true);
        DitolongOlehLainLain.setName("DitolongOlehLainLain"); // NOI18N
        DitolongOlehLainLain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DitolongOlehLainLainKeyPressed(evt);
            }
        });
        FormInput.add(DitolongOlehLainLain);
        DitolongOlehLainLain.setBounds(690, 280, 160, 23);

        RiwayatKehamilanIbu.setFocusTraversalPolicyProvider(true);
        RiwayatKehamilanIbu.setName("RiwayatKehamilanIbu"); // NOI18N
        RiwayatKehamilanIbu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatKehamilanIbuKeyPressed(evt);
            }
        });
        FormInput.add(RiwayatKehamilanIbu);
        RiwayatKehamilanIbu.setBounds(180, 250, 280, 23);

        jLabel42.setText("Riwayat Kehamilan Ibu :");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(10, 250, 160, 23);

        jLabel43.setText("Lahir Di :");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(0, 280, 168, 23);

        LahirDi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rumah Bersalin", "Bidan Praktik Mandiri", "Rumah", "Rumah Sakit", "Fasilitas Kesehatan Lainnya" }));
        LahirDi.setName("LahirDi"); // NOI18N
        LahirDi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LahirDiKeyPressed(evt);
            }
        });
        FormInput.add(LahirDi);
        LahirDi.setBounds(172, 280, 140, 23);

        ProsesPersalinan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SC", "Spontan" }));
        ProsesPersalinan.setName("ProsesPersalinan"); // NOI18N
        ProsesPersalinan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProsesPersalinanKeyPressed(evt);
            }
        });
        FormInput.add(ProsesPersalinan);
        ProsesPersalinan.setBounds(430, 280, 80, 23);

        jLabel44.setText("Proses Persalinan :");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(320, 280, 100, 23);

        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel124.setText("Hari");
        jLabel124.setName("jLabel124"); // NOI18N
        FormInput.add(jLabel124);
        jLabel124.setBounds(460, 310, 20, 23);

        RiwayatKesehatanSebelumnya.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Pernah Opname", "Pernah Opname" }));
        RiwayatKesehatanSebelumnya.setName("RiwayatKesehatanSebelumnya"); // NOI18N
        RiwayatKesehatanSebelumnya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatKesehatanSebelumnyaKeyPressed(evt);
            }
        });
        FormInput.add(RiwayatKesehatanSebelumnya);
        RiwayatKesehatanSebelumnya.setBounds(170, 340, 140, 23);

        OpnameRS.setFocusTraversalPolicyProvider(true);
        OpnameRS.setName("OpnameRS"); // NOI18N
        OpnameRS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OpnameRSKeyPressed(evt);
            }
        });
        FormInput.add(OpnameRS);
        OpnameRS.setBounds(320, 340, 140, 23);

        jLabel125.setText("Riwayat Kesehatan Sebelum :");
        jLabel125.setName("jLabel125"); // NOI18N
        FormInput.add(jLabel125);
        jLabel125.setBounds(10, 340, 150, 23);

        RiwayatAlergi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        RiwayatAlergi.setName("RiwayatAlergi"); // NOI18N
        RiwayatAlergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatAlergiKeyPressed(evt);
            }
        });
        FormInput.add(RiwayatAlergi);
        RiwayatAlergi.setBounds(170, 370, 140, 23);

        SelamaHariOpname.setFocusTraversalPolicyProvider(true);
        SelamaHariOpname.setName("SelamaHariOpname"); // NOI18N
        SelamaHariOpname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SelamaHariOpnameKeyPressed(evt);
            }
        });
        FormInput.add(SelamaHariOpname);
        SelamaHariOpname.setBounds(460, 340, 50, 23);

        jLabel127.setText("Riwayat Alergi :");
        jLabel127.setName("jLabel127"); // NOI18N
        FormInput.add(jLabel127);
        jLabel127.setBounds(70, 370, 90, 23);

        Makanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ASI", "ASI dan Susu Formula", "Susu Formula" }));
        Makanan.setName("Makanan"); // NOI18N
        Makanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MakananKeyPressed(evt);
            }
        });
        FormInput.add(Makanan);
        Makanan.setBounds(580, 370, 200, 23);

        jLabel128.setText("Makanan :");
        jLabel128.setName("jLabel128"); // NOI18N
        FormInput.add(jLabel128);
        jLabel128.setBounds(500, 370, 70, 23);

        jLabel129.setText("Riwayat Imunisasi :");
        jLabel129.setName("jLabel129"); // NOI18N
        FormInput.add(jLabel129);
        jLabel129.setBounds(60, 400, 100, 23);

        Imunisasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Belum", "Hepatitis B", "Polio" }));
        Imunisasi.setName("Imunisasi"); // NOI18N
        Imunisasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ImunisasiKeyPressed(evt);
            }
        });
        FormInput.add(Imunisasi);
        Imunisasi.setBounds(170, 400, 140, 23);

        jLabel47.setText("Usia Gestasi :");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput.add(jLabel47);
        jLabel47.setBounds(30, 310, 138, 23);

        UsiaGestasi.setFocusTraversalPolicyProvider(true);
        UsiaGestasi.setName("UsiaGestasi"); // NOI18N
        UsiaGestasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsiaGestasiKeyPressed(evt);
            }
        });
        FormInput.add(UsiaGestasi);
        UsiaGestasi.setBounds(170, 310, 90, 23);

        jLabel28.setText("Umur :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(310, 310, 40, 23);

        Umur.setFocusTraversalPolicyProvider(true);
        Umur.setName("Umur"); // NOI18N
        Umur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UmurKeyPressed(evt);
            }
        });
        FormInput.add(Umur);
        Umur.setBounds(360, 310, 100, 23);

        jLabel22.setText("LD :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(650, 460, 20, 23);

        LD.setFocusTraversalPolicyProvider(true);
        LD.setName("LD"); // NOI18N
        LD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LDKeyPressed(evt);
            }
        });
        FormInput.add(LD);
        LD.setBounds(670, 460, 50, 23);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("x/menit");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(510, 510, 40, 23);

        jLabel17.setText("Risiko Gizi :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(500, 400, 73, 23);

        jLabel26.setText("BBL :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(60, 460, 50, 23);

        BBLGizi.setFocusTraversalPolicyProvider(true);
        BBLGizi.setName("BBLGizi"); // NOI18N
        BBLGizi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BBLGiziKeyPressed(evt);
            }
        });
        FormInput.add(BBLGizi);
        BBLGizi.setBounds(110, 460, 50, 23);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("gram");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(160, 460, 30, 23);

        jLabel18.setText("BBS :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(200, 460, 30, 23);

        BBS.setFocusTraversalPolicyProvider(true);
        BBS.setName("BBS"); // NOI18N
        BBS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BBSKeyPressed(evt);
            }
        });
        FormInput.add(BBS);
        BBS.setBounds(230, 460, 50, 23);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("gram");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(280, 460, 30, 23);

        jLabel24.setText("PB/TB :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(320, 460, 40, 23);

        PBTB.setFocusTraversalPolicyProvider(true);
        PBTB.setName("PBTB"); // NOI18N
        PBTB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PBTBKeyPressed(evt);
            }
        });
        FormInput.add(PBTB);
        PBTB.setBounds(360, 460, 50, 23);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("cm");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(410, 460, 20, 23);

        jLabel12.setText("LLA :");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(440, 460, 30, 23);

        LLA.setFocusTraversalPolicyProvider(true);
        LLA.setName("LLA"); // NOI18N
        LLA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LLAKeyPressed(evt);
            }
        });
        FormInput.add(LLA);
        LLA.setBounds(470, 460, 50, 23);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("cm");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(520, 460, 20, 23);

        jLabel15.setText("LK :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(550, 460, 20, 23);

        LK.setFocusTraversalPolicyProvider(true);
        LK.setName("LK"); // NOI18N
        LK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LKKeyPressed(evt);
            }
        });
        FormInput.add(LK);
        LK.setBounds(570, 460, 50, 23);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("cm");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(620, 460, 20, 23);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("REPRODUKSI, SEKSUALITAS DAN ELIMINASI");
        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(20, 1160, 260, 23);

        jLabel131.setText("Bentuk Dada :");
        jLabel131.setName("jLabel131"); // NOI18N
        FormInput.add(jLabel131);
        jLabel131.setBounds(0, 510, 109, 23);

        BentukDada.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simetris", "Asimetris", "Lain-Lain" }));
        BentukDada.setName("BentukDada"); // NOI18N
        BentukDada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BentukDadaKeyPressed(evt);
            }
        });
        FormInput.add(BentukDada);
        BentukDada.setBounds(120, 510, 90, 23);

        SaturasiO2.setActionCommand("<Not Set>");
        SaturasiO2.setFocusTraversalPolicyProvider(true);
        SaturasiO2.setName("SaturasiO2"); // NOI18N
        SaturasiO2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SaturasiO2KeyPressed(evt);
            }
        });
        FormInput.add(SaturasiO2);
        SaturasiO2.setBounds(670, 510, 50, 23);

        jLabel132.setText("Warna Lendir :");
        jLabel132.setName("jLabel132"); // NOI18N
        FormInput.add(jLabel132);
        jLabel132.setBounds(580, 540, 72, 23);

        PernafasanSpontan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        PernafasanSpontan.setName("PernafasanSpontan"); // NOI18N
        PernafasanSpontan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PernafasanSpontanKeyPressed(evt);
            }
        });
        FormInput.add(PernafasanSpontan);
        PernafasanSpontan.setBounds(290, 510, 80, 23);

        Frekuensi.setFocusTraversalPolicyProvider(true);
        Frekuensi.setName("Frekuensi"); // NOI18N
        Frekuensi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FrekuensiKeyPressed(evt);
            }
        });
        FormInput.add(Frekuensi);
        Frekuensi.setBounds(460, 510, 50, 23);

        jLabel139.setText("Sesak :");
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(0, 540, 109, 23);

        Sesak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Sesak.setName("Sesak"); // NOI18N
        Sesak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SesakKeyPressed(evt);
            }
        });
        FormInput.add(Sesak);
        Sesak.setBounds(120, 540, 90, 23);

        AlatBantuNafas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "ETT", "Ventilator", "CPAP", "Nasal" }));
        AlatBantuNafas.setName("AlatBantuNafas"); // NOI18N
        AlatBantuNafas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlatBantuNafasKeyPressed(evt);
            }
        });
        FormInput.add(AlatBantuNafas);
        AlatBantuNafas.setBounds(120, 600, 90, 23);

        jLabel140.setText("Alat Bantu Nafas :");
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(20, 600, 90, 23);

        jLabel141.setText("Ekstremitas :");
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(340, 710, 70, 23);

        Ekstremitas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hangat", "Dingin", "Sianosis" }));
        Ekstremitas.setName("Ekstremitas"); // NOI18N
        Ekstremitas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EkstremitasKeyPressed(evt);
            }
        });
        FormInput.add(Ekstremitas);
        Ekstremitas.setBounds(420, 710, 120, 23);

        jLabel142.setText("Keadaan Saat Ini :");
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput.add(jLabel142);
        jLabel142.setBounds(320, 680, 90, 23);

        KeadaanSaatIni.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Edema", "Lemah", "Pucat" }));
        KeadaanSaatIni.setName("KeadaanSaatIni"); // NOI18N
        KeadaanSaatIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeadaanSaatIniKeyPressed(evt);
            }
        });
        FormInput.add(KeadaanSaatIni);
        KeadaanSaatIni.setBounds(420, 680, 120, 23);

        jLabel143.setText("Bunyi Jantung :");
        jLabel143.setName("jLabel143"); // NOI18N
        FormInput.add(jLabel143);
        jLabel143.setBounds(0, 650, 109, 23);

        BunyiJantung.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Regular", "Irregular" }));
        BunyiJantung.setName("BunyiJantung"); // NOI18N
        BunyiJantung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BunyiJantungKeyPressed(evt);
            }
        });
        FormInput.add(BunyiJantung);
        BunyiJantung.setBounds(120, 650, 120, 23);

        jLabel144.setText("AGD :");
        jLabel144.setName("jLabel144"); // NOI18N
        FormInput.add(jLabel144);
        jLabel144.setBounds(250, 600, 30, 23);

        RespirasiJenisPernafasan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        RespirasiJenisPernafasan.setName("RespirasiJenisPernafasan"); // NOI18N
        RespirasiJenisPernafasan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RespirasiJenisPernafasanKeyPressed(evt);
            }
        });
        FormInput.add(RespirasiJenisPernafasan);
        RespirasiJenisPernafasan.setBounds(290, 600, 80, 23);

        WarnaLendir.setFocusTraversalPolicyProvider(true);
        WarnaLendir.setName("WarnaLendir"); // NOI18N
        WarnaLendir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WarnaLendirKeyPressed(evt);
            }
        });
        FormInput.add(WarnaLendir);
        WarnaLendir.setBounds(660, 540, 120, 23);

        jLabel145.setText("Pendarahan :");
        jLabel145.setName("jLabel145"); // NOI18N
        FormInput.add(jLabel145);
        jLabel145.setBounds(340, 650, 70, 23);

        Pendarahan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Pendarahan.setName("Pendarahan"); // NOI18N
        Pendarahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PendarahanKeyPressed(evt);
            }
        });
        FormInput.add(Pendarahan);
        Pendarahan.setBounds(420, 650, 120, 23);

        jLabel146.setText("Arteri Femoralis :");
        jLabel146.setName("jLabel146"); // NOI18N
        FormInput.add(jLabel146);
        jLabel146.setBounds(0, 680, 109, 23);

        FemoralisKanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kuat", "Lemah" }));
        FemoralisKanan.setName("FemoralisKanan"); // NOI18N
        FemoralisKanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FemoralisKananKeyPressed(evt);
            }
        });
        FormInput.add(FemoralisKanan);
        FemoralisKanan.setBounds(120, 680, 120, 23);

        Kapiler.setFocusTraversalPolicyProvider(true);
        Kapiler.setName("Kapiler"); // NOI18N
        Kapiler.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KapilerKeyPressed(evt);
            }
        });
        FormInput.add(Kapiler);
        Kapiler.setBounds(670, 710, 70, 23);

        BeratBadan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BB Naik", "BB Tidak Berubah", "BB Turun" }));
        BeratBadan.setName("BeratBadan"); // NOI18N
        BeratBadan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BeratBadanKeyPressed(evt);
            }
        });
        FormInput.add(BeratBadan);
        BeratBadan.setBounds(120, 790, 150, 23);

        jLabel148.setText("Berat Badan :");
        jLabel148.setName("jLabel148"); // NOI18N
        FormInput.add(jLabel148);
        jLabel148.setBounds(0, 790, 109, 23);

        jLabel149.setText("Kremer :");
        jLabel149.setName("jLabel149"); // NOI18N
        FormInput.add(jLabel149);
        jLabel149.setBounds(570, 820, 80, 23);

        CaraMinum.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dot", "Sonde Lambung", "Direct Breast Feeding", "Cup Feeding" }));
        CaraMinum.setName("CaraMinum"); // NOI18N
        CaraMinum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CaraMinumKeyPressed(evt);
            }
        });
        FormInput.add(CaraMinum);
        CaraMinum.setBounds(670, 790, 100, 23);

        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel147.setText("B. Kiri");
        jLabel147.setName("jLabel147"); // NOI18N
        FormInput.add(jLabel147);
        jLabel147.setBounds(250, 710, 50, 23);

        FemoralisKiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kuat", "Lemah" }));
        FemoralisKiri.setName("FemoralisKiri"); // NOI18N
        FemoralisKiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FemoralisKiriKeyPressed(evt);
            }
        });
        FormInput.add(FemoralisKiri);
        FemoralisKiri.setBounds(120, 710, 120, 23);

        jLabel150.setText("Hasil Laboratorium :");
        jLabel150.setName("jLabel150"); // NOI18N
        FormInput.add(jLabel150);
        jLabel150.setBounds(0, 740, 110, 23);

        HasilLab.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Anemia", "Trombositopenia", "Leukositosis", "Hipoproteinemia" }));
        HasilLab.setName("HasilLab"); // NOI18N
        HasilLab.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilLabKeyPressed(evt);
            }
        });
        FormInput.add(HasilLab);
        HasilLab.setBounds(120, 740, 120, 23);

        jLabel151.setText("Akses Intavena Via :");
        jLabel151.setName("jLabel151"); // NOI18N
        FormInput.add(jLabel151);
        jLabel151.setBounds(300, 740, 110, 23);

        Intavena.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "UVC", "Perifer", "Vena Central" }));
        Intavena.setName("Intavena"); // NOI18N
        Intavena.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntavenaKeyPressed(evt);
            }
        });
        FormInput.add(Intavena);
        Intavena.setBounds(420, 740, 120, 23);

        HariIntavena.setFocusTraversalPolicyProvider(true);
        HariIntavena.setName("HariIntavena"); // NOI18N
        HariIntavena.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HariIntavenaKeyPressed(evt);
            }
        });
        FormInput.add(HariIntavena);
        HariIntavena.setBounds(540, 740, 40, 23);

        jLabel152.setText("Residu :");
        jLabel152.setName("jLabel152"); // NOI18N
        FormInput.add(jLabel152);
        jLabel152.setBounds(770, 820, 50, 23);

        KeadaanSekarang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Minum", "Labioschizis", "Palatoschizis", "Genatoschizis", "Labio Palate Genatoschizis", "Puasa" }));
        KeadaanSekarang.setName("KeadaanSekarang"); // NOI18N
        KeadaanSekarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeadaanSekarangKeyPressed(evt);
            }
        });
        FormInput.add(KeadaanSekarang);
        KeadaanSekarang.setBounds(670, 880, 150, 23);

        jLabel153.setText("Turgor Kulit :");
        jLabel153.setName("jLabel153"); // NOI18N
        FormInput.add(jLabel153);
        jLabel153.setBounds(40, 880, 70, 23);

        TurgorKulit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Sedang", "Buruk" }));
        TurgorKulit.setName("TurgorKulit"); // NOI18N
        TurgorKulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TurgorKulitKeyPressed(evt);
            }
        });
        FormInput.add(TurgorKulit);
        TurgorKulit.setBounds(120, 880, 150, 23);

        jLabel154.setText("Keadaan Saat Ini :");
        jLabel154.setName("jLabel154"); // NOI18N
        FormInput.add(jLabel154);
        jLabel154.setBounds(550, 880, 109, 23);

        UbunUbun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cekung", "Datar", "Cembung" }));
        UbunUbun.setName("UbunUbun"); // NOI18N
        UbunUbun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UbunUbunKeyPressed(evt);
            }
        });
        FormInput.add(UbunUbun);
        UbunUbun.setBounds(420, 880, 140, 23);

        jLabel155.setText("Selaput Lendir :");
        jLabel155.setName("jLabel155"); // NOI18N
        FormInput.add(jLabel155);
        jLabel155.setBounds(320, 790, 90, 23);

        Lendir.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lembab", "Kering", "Kotor", "Lain-Lain", " " }));
        Lendir.setName("Lendir"); // NOI18N
        Lendir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LendirKeyPressed(evt);
            }
        });
        FormInput.add(Lendir);
        Lendir.setBounds(420, 790, 140, 23);

        jLabel156.setText("Abdomen :");
        jLabel156.setName("jLabel156"); // NOI18N
        FormInput.add(jLabel156);
        jLabel156.setBounds(330, 850, 80, 23);

        Abdomen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Supel", "Kembung", "Tegang" }));
        Abdomen.setName("Abdomen"); // NOI18N
        Abdomen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AbdomenKeyPressed(evt);
            }
        });
        FormInput.add(Abdomen);
        Abdomen.setBounds(420, 850, 140, 23);

        Muntah.setFocusTraversalPolicyProvider(true);
        Muntah.setName("Muntah"); // NOI18N
        Muntah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MuntahKeyPressed(evt);
            }
        });
        FormInput.add(Muntah);
        Muntah.setBounds(830, 790, 70, 23);

        jLabel157.setText("Lidah :");
        jLabel157.setName("jLabel157"); // NOI18N
        FormInput.add(jLabel157);
        jLabel157.setBounds(590, 850, 70, 23);

        Lidah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lembab", "Kering", "Kotor", "Lain-Lain" }));
        Lidah.setName("Lidah"); // NOI18N
        Lidah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LidahKeyPressed(evt);
            }
        });
        FormInput.add(Lidah);
        Lidah.setBounds(670, 850, 150, 23);

        jLabel158.setText("Hasil Laboratorium :");
        jLabel158.setName("jLabel158"); // NOI18N
        FormInput.add(jLabel158);
        jLabel158.setBounds(0, 910, 110, 23);

        HasilLaboratorium.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Hipoproteinnemia", "Hipoalbuminemia", "Asidosis Metabolic", "Hipokalsemia", "Alkalosis Metabolic", "Hiponatremia", "Hipokalsemia", "Hipoglikemia", "Hiperbilirubinemia" }));
        HasilLaboratorium.setName("HasilLaboratorium"); // NOI18N
        HasilLaboratorium.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilLaboratoriumKeyPressed(evt);
            }
        });
        FormInput.add(HasilLaboratorium);
        HasilLaboratorium.setBounds(120, 910, 150, 23);

        jLabel159.setText("Warna Kulit :");
        jLabel159.setName("jLabel159"); // NOI18N
        FormInput.add(jLabel159);
        jLabel159.setBounds(330, 820, 80, 23);

        WarnaKulit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kemerahan", "Pucat", "Sianosis", "Ikterik" }));
        WarnaKulit.setName("WarnaKulit"); // NOI18N
        WarnaKulit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WarnaKulitKeyPressed(evt);
            }
        });
        FormInput.add(WarnaKulit);
        WarnaKulit.setBounds(420, 820, 140, 23);

        Residu.setFocusTraversalPolicyProvider(true);
        Residu.setName("Residu"); // NOI18N
        Residu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ResiduKeyPressed(evt);
            }
        });
        FormInput.add(Residu);
        Residu.setBounds(830, 820, 70, 23);

        jLabel164.setText("Kesadaran :");
        jLabel164.setName("jLabel164"); // NOI18N
        FormInput.add(jLabel164);
        jLabel164.setBounds(20, 960, 109, 23);

        Kesadaran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Respon Terhadap Nyeri", "Tidak Respon Terhadap Nyeri" }));
        Kesadaran.setName("Kesadaran"); // NOI18N
        Kesadaran.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KesadaranKeyPressed(evt);
            }
        });
        FormInput.add(Kesadaran);
        Kesadaran.setBounds(140, 960, 160, 23);

        Kremer.setFocusTraversalPolicyProvider(true);
        Kremer.setName("Kremer"); // NOI18N
        Kremer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KremerKeyPressed(evt);
            }
        });
        FormInput.add(Kremer);
        Kremer.setBounds(660, 820, 100, 23);

        ProduksiUrine.setFocusTraversalPolicyProvider(true);
        ProduksiUrine.setName("ProduksiUrine"); // NOI18N
        ProduksiUrine.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProduksiUrineKeyPressed(evt);
            }
        });
        FormInput.add(ProduksiUrine);
        ProduksiUrine.setBounds(320, 1210, 60, 23);

        Tangisan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        Tangisan.setName("Tangisan"); // NOI18N
        Tangisan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TangisanKeyPressed(evt);
            }
        });
        FormInput.add(Tangisan);
        Tangisan.setBounds(140, 1030, 90, 23);

        jLabel165.setText("Tangisan :");
        jLabel165.setName("jLabel165"); // NOI18N
        FormInput.add(jLabel165);
        jLabel165.setBounds(20, 1030, 109, 23);

        jLabel166.setText("Kepala :");
        jLabel166.setName("jLabel166"); // NOI18N
        FormInput.add(jLabel166);
        jLabel166.setBounds(270, 960, 80, 23);

        Kepala.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "An Ancephal", "Tidak Ada Kelainan", "Hydrocephalus", "Ceptal Hematoma", "Perdarahan Ventrikuler", "Caput Succadeneum" }));
        Kepala.setName("Kepala"); // NOI18N
        Kepala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KepalaKeyPressed(evt);
            }
        });
        FormInput.add(Kepala);
        Kepala.setBounds(360, 960, 120, 23);

        KetTangisan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Kuat", "Kurang Kuat", "Melengking", "Merintih" }));
        KetTangisan.setName("KetTangisan"); // NOI18N
        KetTangisan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetTangisanKeyPressed(evt);
            }
        });
        FormInput.add(KetTangisan);
        KetTangisan.setBounds(240, 1030, 90, 23);

        JenisPupil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "An Isokor", "Tidak Bereaksi Terhadap Cahaya", "Isokor", "Lemah", "Dilatasi" }));
        JenisPupil.setName("JenisPupil"); // NOI18N
        JenisPupil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JenisPupilKeyPressed(evt);
            }
        });
        FormInput.add(JenisPupil);
        JenisPupil.setBounds(240, 1060, 90, 23);

        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel113.setText("mm");
        jLabel113.setName("jLabel113"); // NOI18N
        FormInput.add(jLabel113);
        jLabel113.setBounds(210, 1090, 18, 23);

        LingkarKepala.setHighlighter(null);
        LingkarKepala.setName("LingkarKepala"); // NOI18N
        LingkarKepala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LingkarKepalaKeyPressed(evt);
            }
        });
        FormInput.add(LingkarKepala);
        LingkarKepala.setBounds(460, 1030, 60, 23);

        jLabel114.setText("Lingkar Kepala :");
        jLabel114.setName("jLabel114"); // NOI18N
        FormInput.add(jLabel114);
        jLabel114.setBounds(360, 1030, 90, 23);

        PupilKiri.setHighlighter(null);
        PupilKiri.setName("PupilKiri"); // NOI18N
        PupilKiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PupilKiriKeyPressed(evt);
            }
        });
        FormInput.add(PupilKiri);
        PupilKiri.setBounds(140, 1060, 70, 23);

        jLabel115.setText("Alat Bantu :");
        jLabel115.setName("jLabel115"); // NOI18N
        FormInput.add(jLabel115);
        jLabel115.setBounds(700, 1210, 60, 23);

        PupilKanan.setHighlighter(null);
        PupilKanan.setName("PupilKanan"); // NOI18N
        PupilKanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PupilKananKeyPressed(evt);
            }
        });
        FormInput.add(PupilKanan);
        PupilKanan.setBounds(140, 1090, 70, 23);

        jLabel117.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel117.setText("A. Buang Air Besar");
        jLabel117.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel117.setName("jLabel117"); // NOI18N
        FormInput.add(jLabel117);
        jLabel117.setBounds(20, 1340, 110, 23);

        BAK.setHighlighter(null);
        BAK.setName("BAK"); // NOI18N
        BAK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BAKKeyPressed(evt);
            }
        });
        FormInput.add(BAK);
        BAK.setBounds(120, 1210, 60, 23);

        jLabel118.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel118.setText("cc/kgbb/jam");
        jLabel118.setName("jLabel118"); // NOI18N
        FormInput.add(jLabel118);
        jLabel118.setBounds(380, 1210, 60, 23);

        jLabel119.setText("Keterangan :");
        jLabel119.setName("jLabel119"); // NOI18N
        FormInput.add(jLabel119);
        jLabel119.setBounds(610, 1060, 70, 23);

        KetNeurosensori.setHighlighter(null);
        KetNeurosensori.setName("KetNeurosensori"); // NOI18N
        KetNeurosensori.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KetNeurosensoriKeyPressed(evt);
            }
        });
        FormInput.add(KetNeurosensori);
        KetNeurosensori.setBounds(690, 1060, 170, 23);

        jLabel170.setText("Gerakan :");
        jLabel170.setName("jLabel170"); // NOI18N
        FormInput.add(jLabel170);
        jLabel170.setBounds(340, 1060, 109, 23);

        Kejang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Tonik", "Klonik" }));
        Kejang.setName("Kejang"); // NOI18N
        Kejang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KejangKeyPressed(evt);
            }
        });
        FormInput.add(Kejang);
        Kejang.setBounds(690, 1030, 150, 23);

        jLabel171.setText("Reflex Rooting :");
        jLabel171.setName("jLabel171"); // NOI18N
        FormInput.add(jLabel171);
        jLabel171.setBounds(460, 960, 120, 23);

        jLabel172.setText("Kondisi Sekarang :");
        jLabel172.setName("jLabel172"); // NOI18N
        FormInput.add(jLabel172);
        jLabel172.setBounds(440, 1210, 100, 23);

        jLabel173.setText("Kejang :");
        jLabel173.setName("jLabel173"); // NOI18N
        FormInput.add(jLabel173);
        jLabel173.setBounds(570, 1030, 109, 23);

        jLabel174.setText("Vagina :");
        jLabel174.setName("jLabel174"); // NOI18N
        FormInput.add(jLabel174);
        jLabel174.setBounds(10, 1260, 100, 23);

        Gerakan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Paralisis", "Aktif", "Subtle" }));
        Gerakan.setName("Gerakan"); // NOI18N
        Gerakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GerakanKeyPressed(evt);
            }
        });
        FormInput.add(Gerakan);
        Gerakan.setBounds(460, 1060, 120, 23);

        ReflexTampak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        ReflexTampak.setName("ReflexTampak"); // NOI18N
        ReflexTampak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ReflexTampakKeyPressed(evt);
            }
        });
        FormInput.add(ReflexTampak);
        ReflexTampak.setBounds(590, 960, 80, 23);

        KeadaanReproduksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Retensio Urine", "Inkontinentia", "Jernih", "Pekat", "Hematuria" }));
        KeadaanReproduksi.setName("KeadaanReproduksi"); // NOI18N
        KeadaanReproduksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeadaanReproduksiKeyPressed(evt);
            }
        });
        FormInput.add(KeadaanReproduksi);
        KeadaanReproduksi.setBounds(550, 1210, 130, 23);

        Vagina.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bersih", "Kotor" }));
        Vagina.setName("Vagina"); // NOI18N
        Vagina.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VaginaKeyPressed(evt);
            }
        });
        FormInput.add(Vagina);
        Vagina.setBounds(120, 1260, 110, 23);

        AlatBantu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ada" }));
        AlatBantu.setName("AlatBantu"); // NOI18N
        AlatBantu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AlatBantuKeyPressed(evt);
            }
        });
        FormInput.add(AlatBantu);
        AlatBantu.setBounds(770, 1210, 90, 23);

        jLabel179.setText("Kateter :");
        jLabel179.setName("jLabel179"); // NOI18N
        FormInput.add(jLabel179);
        jLabel179.setBounds(380, 1260, 50, 23);

        Menstruasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Menstruasi.setName("Menstruasi"); // NOI18N
        Menstruasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenstruasiKeyPressed(evt);
            }
        });
        FormInput.add(Menstruasi);
        Menstruasi.setBounds(290, 1260, 80, 23);

        jLabel181.setText("Warna :");
        jLabel181.setName("jLabel181"); // NOI18N
        FormInput.add(jLabel181);
        jLabel181.setBounds(30, 1400, 120, 23);

        BAB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        BAB.setName("BAB"); // NOI18N
        BAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BABKeyPressed(evt);
            }
        });
        FormInput.add(BAB);
        BAB.setBounds(160, 1370, 90, 23);

        WarnaBAB.setFocusTraversalPolicyProvider(true);
        WarnaBAB.setName("WarnaBAB"); // NOI18N
        WarnaBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WarnaBABKeyPressed(evt);
            }
        });
        FormInput.add(WarnaBAB);
        WarnaBAB.setBounds(160, 1400, 150, 23);

        jLabel182.setText("Hipospadia :");
        jLabel182.setName("jLabel182"); // NOI18N
        FormInput.add(jLabel182);
        jLabel182.setBounds(360, 1310, 70, 23);

        Preputium.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bersih", "Kotor" }));
        Preputium.setName("Preputium"); // NOI18N
        Preputium.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PreputiumKeyPressed(evt);
            }
        });
        FormInput.add(Preputium);
        Preputium.setBounds(120, 1310, 110, 23);

        jLabel183.setText("Frekuensi BAB :");
        jLabel183.setName("jLabel183"); // NOI18N
        FormInput.add(jLabel183);
        jLabel183.setBounds(640, 1400, 100, 23);

        KondisiBAB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Kembung", "Konstipasi", "Ileustomi", "Kolostomi", "Lain-Lain" }));
        KondisiBAB.setName("KondisiBAB"); // NOI18N
        KondisiBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiBABKeyPressed(evt);
            }
        });
        FormInput.add(KondisiBAB);
        KondisiBAB.setBounds(480, 1400, 140, 23);

        jLabel184.setText("Konsistensi Feses :");
        jLabel184.setName("jLabel184"); // NOI18N
        FormInput.add(jLabel184);
        jLabel184.setBounds(370, 1370, 100, 23);

        Feses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lembek", "Cair Berampas", "Cair Tanpa Ampas" }));
        Feses.setName("Feses"); // NOI18N
        Feses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FesesKeyPressed(evt);
            }
        });
        FormInput.add(Feses);
        Feses.setBounds(480, 1370, 140, 23);

        Suhu.setFocusTraversalPolicyProvider(true);
        Suhu.setName("Suhu"); // NOI18N
        Suhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuKeyPressed(evt);
            }
        });
        FormInput.add(Suhu);
        Suhu.setBounds(250, 1530, 50, 23);

        Hipospadia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Hipospadia.setName("Hipospadia"); // NOI18N
        Hipospadia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HipospadiaKeyPressed(evt);
            }
        });
        FormInput.add(Hipospadia);
        Hipospadia.setBounds(440, 1310, 80, 23);

        ccBAB.setFocusTraversalPolicyProvider(true);
        ccBAB.setName("ccBAB"); // NOI18N
        ccBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ccBABKeyPressed(evt);
            }
        });
        FormInput.add(ccBAB);
        ccBAB.setBounds(250, 1370, 50, 23);

        jLabel186.setText("Anus :");
        jLabel186.setName("jLabel186"); // NOI18N
        FormInput.add(jLabel186);
        jLabel186.setBounds(700, 1370, 40, 23);

        Anus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        Anus.setName("Anus"); // NOI18N
        Anus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AnusKeyPressed(evt);
            }
        });
        FormInput.add(Anus);
        Anus.setBounds(750, 1370, 110, 23);

        FrekuensiBAB.setFocusTraversalPolicyProvider(true);
        FrekuensiBAB.setName("FrekuensiBAB"); // NOI18N
        FrekuensiBAB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FrekuensiBABKeyPressed(evt);
            }
        });
        FormInput.add(FrekuensiBAB);
        FrekuensiBAB.setBounds(750, 1400, 50, 23);

        jLabel187.setText("Keadaan Saat Ini :");
        jLabel187.setName("jLabel187"); // NOI18N
        FormInput.add(jLabel187);
        jLabel187.setBounds(90, 1600, 90, 23);

        Berkunjung.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Berkunjung.setName("Berkunjung"); // NOI18N
        Berkunjung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BerkunjungKeyPressed(evt);
            }
        });
        FormInput.add(Berkunjung);
        Berkunjung.setBounds(130, 1980, 110, 23);

        Mekonium.setFocusTraversalPolicyProvider(true);
        Mekonium.setName("Mekonium"); // NOI18N
        Mekonium.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MekoniumKeyPressed(evt);
            }
        });
        FormInput.add(Mekonium);
        Mekonium.setBounds(160, 1430, 50, 23);

        jLabel188.setText("Kelainan Kongenital :");
        jLabel188.setName("jLabel188"); // NOI18N
        FormInput.add(jLabel188);
        jLabel188.setBounds(480, 1900, 110, 23);

        Kongenital.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        Kongenital.setName("Kongenital"); // NOI18N
        Kongenital.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KongenitalKeyPressed(evt);
            }
        });
        FormInput.add(Kongenital);
        Kongenital.setBounds(600, 1900, 80, 23);

        jLabel189.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel189.setText("DOWN SCORE");
        jLabel189.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel189.setName("jLabel189"); // NOI18N
        FormInput.add(jLabel189);
        jLabel189.setBounds(20, 2470, 170, 23);

        KondisiKebersihanDiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Baik", "Lemah", "Kejang", "Paralisie", "Gerakan Terbatas", "Kelemahan Otot" }));
        KondisiKebersihanDiri.setName("KondisiKebersihanDiri"); // NOI18N
        KondisiKebersihanDiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KondisiKebersihanDiriKeyPressed(evt);
            }
        });
        FormInput.add(KondisiKebersihanDiri);
        KondisiKebersihanDiri.setBounds(190, 1600, 110, 23);

        jLabel191.setText("Derajat Dehidrasi :");
        jLabel191.setName("jLabel191"); // NOI18N
        FormInput.add(jLabel191);
        jLabel191.setBounds(140, 1500, 100, 23);

        Integritas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kering", "Rash", "Bullae", "Utuh", "Mengelupas", "Kemerahan", "Pustule", "Ptechiae", "Lesi" }));
        Integritas.setName("Integritas"); // NOI18N
        Integritas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntegritasKeyPressed(evt);
            }
        });
        FormInput.add(Integritas);
        Integritas.setBounds(190, 1570, 110, 23);

        Dehidrasi.setFocusTraversalPolicyProvider(true);
        Dehidrasi.setName("Dehidrasi"); // NOI18N
        Dehidrasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DehidrasiKeyPressed(evt);
            }
        });
        FormInput.add(Dehidrasi);
        Dehidrasi.setBounds(250, 1500, 50, 23);

        KukuBersihDiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        KukuBersihDiri.setName("KukuBersihDiri"); // NOI18N
        KukuBersihDiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KukuBersihDiriKeyPressed(evt);
            }
        });
        FormInput.add(KukuBersihDiri);
        KukuBersihDiri.setBounds(110, 1720, 130, 23);

        jLabel193.setText("Tali Pusat :");
        jLabel193.setName("jLabel193"); // NOI18N
        FormInput.add(jLabel193);
        jLabel193.setBounds(460, 1720, 60, 23);

        TaliPusat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        TaliPusat.setName("TaliPusat"); // NOI18N
        TaliPusat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TaliPusatKeyPressed(evt);
            }
        });
        FormInput.add(TaliPusat);
        TaliPusat.setBounds(530, 1720, 120, 23);

        LainLainRawat.setEditable(false);
        LainLainRawat.setFocusTraversalPolicyProvider(true);
        LainLainRawat.setName("LainLainRawat"); // NOI18N
        FormInput.add(LainLainRawat);
        LainLainRawat.setBounds(450, 2010, 120, 23);

        WarnaKulitBersihDiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kemerahan", "Pucat" }));
        WarnaKulitBersihDiri.setName("WarnaKulitBersihDiri"); // NOI18N
        WarnaKulitBersihDiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WarnaKulitBersihDiriKeyPressed(evt);
            }
        });
        FormInput.add(WarnaKulitBersihDiri);
        WarnaKulitBersihDiri.setBounds(110, 1690, 130, 23);

        DerajatSuhu.setFocusTraversalPolicyProvider(true);
        DerajatSuhu.setName("DerajatSuhu"); // NOI18N
        DerajatSuhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DerajatSuhuKeyPressed(evt);
            }
        });
        FormInput.add(DerajatSuhu);
        DerajatSuhu.setBounds(400, 1690, 50, 23);

        jLabel196.setText("Suhu :");
        jLabel196.setName("jLabel196"); // NOI18N
        FormInput.add(jLabel196);
        jLabel196.setBounds(240, 1690, 40, 23);

        jLabel197.setText("Punting Umbilical :");
        jLabel197.setName("jLabel197"); // NOI18N
        FormInput.add(jLabel197);
        jLabel197.setBounds(660, 1690, 100, 23);

        LokasiBersihDiri.setEditable(false);
        LokasiBersihDiri.setFocusTraversalPolicyProvider(true);
        LokasiBersihDiri.setName("LokasiBersihDiri"); // NOI18N
        FormInput.add(LokasiBersihDiri);
        LokasiBersihDiri.setBounds(530, 1750, 120, 23);

        SuhuBersihDiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hangat", "Dingin", "Ikterus", "Baik", "Buruk" }));
        SuhuBersihDiri.setName("SuhuBersihDiri"); // NOI18N
        SuhuBersihDiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuBersihDiriKeyPressed(evt);
            }
        });
        FormInput.add(SuhuBersihDiri);
        SuhuBersihDiri.setBounds(290, 1690, 110, 23);

        KeteranganTidur.setFocusTraversalPolicyProvider(true);
        KeteranganTidur.setName("KeteranganTidur"); // NOI18N
        KeteranganTidur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganTidurKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganTidur);
        KeteranganTidur.setBounds(350, 1820, 160, 23);

        jLabel199.setText("Lokasi :");
        jLabel199.setName("jLabel199"); // NOI18N
        FormInput.add(jLabel199);
        jLabel199.setBounds(470, 1750, 50, 23);

        JenisBersihDiri.setEditable(false);
        JenisBersihDiri.setFocusTraversalPolicyProvider(true);
        JenisBersihDiri.setName("JenisBersihDiri"); // NOI18N
        FormInput.add(JenisBersihDiri);
        JenisBersihDiri.setBounds(340, 1750, 110, 23);

        SebutKongenital.setEditable(false);
        SebutKongenital.setFocusTraversalPolicyProvider(true);
        SebutKongenital.setName("SebutKongenital"); // NOI18N
        FormInput.add(SebutKongenital);
        SebutKongenital.setBounds(770, 1900, 90, 23);

        jLabel201.setText("Kepala :");
        jLabel201.setName("jLabel201"); // NOI18N
        FormInput.add(jLabel201);
        jLabel201.setBounds(470, 1690, 50, 23);

        KepalaBersihDiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bersih", "Kotor", "Bau" }));
        KepalaBersihDiri.setName("KepalaBersihDiri"); // NOI18N
        KepalaBersihDiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KepalaBersihDiriKeyPressed(evt);
            }
        });
        FormInput.add(KepalaBersihDiri);
        KepalaBersihDiri.setBounds(530, 1690, 120, 23);

        jLabel202.setText("Keterangan :");
        jLabel202.setName("jLabel202"); // NOI18N
        FormInput.add(jLabel202);
        jLabel202.setBounds(250, 1820, 90, 23);

        MataBersihDiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Tidak Ada" }));
        MataBersihDiri.setName("MataBersihDiri"); // NOI18N
        MataBersihDiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MataBersihDiriKeyPressed(evt);
            }
        });
        FormInput.add(MataBersihDiri);
        MataBersihDiri.setBounds(340, 1720, 110, 23);

        Umbilical.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kering", "Basah", "Bau", "Kemerahan", "Pus" }));
        Umbilical.setName("Umbilical"); // NOI18N
        Umbilical.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UmbilicalKeyPressed(evt);
            }
        });
        FormInput.add(Umbilical);
        Umbilical.setBounds(770, 1690, 110, 23);

        AbdomenKebersihanDIri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada Kelainan", "Kolostomi", "Luka Operasi" }));
        AbdomenKebersihanDIri.setName("AbdomenKebersihanDIri"); // NOI18N
        AbdomenKebersihanDIri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AbdomenKebersihanDIriKeyPressed(evt);
            }
        });
        FormInput.add(AbdomenKebersihanDIri);
        AbdomenKebersihanDIri.setBounds(110, 1750, 130, 23);

        jLabel205.setText("Pola Tidur :");
        jLabel205.setName("jLabel205"); // NOI18N
        FormInput.add(jLabel205);
        jLabel205.setBounds(50, 1900, 70, 23);

        PolaTidur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Terganggu", "Teratur" }));
        PolaTidur.setName("PolaTidur"); // NOI18N
        PolaTidur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PolaTidurKeyPressed(evt);
            }
        });
        FormInput.add(PolaTidur);
        PolaTidur.setBounds(130, 1900, 120, 23);

        Lokasi.setFocusTraversalPolicyProvider(true);
        Lokasi.setName("Lokasi"); // NOI18N
        Lokasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LokasiKeyPressed(evt);
            }
        });
        FormInput.add(Lokasi);
        Lokasi.setBounds(680, 1900, 90, 23);

        jLabel208.setText("Kontak Mata :");
        jLabel208.setName("jLabel208"); // NOI18N
        FormInput.add(jLabel208);
        jLabel208.setBounds(240, 1980, 79, 23);

        KontakMata.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        KontakMata.setName("KontakMata"); // NOI18N
        KontakMata.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KontakMataKeyPressed(evt);
            }
        });
        FormInput.add(KontakMata);
        KontakMata.setBounds(330, 1980, 120, 23);

        jLabel209.setText("Berkunjung :");
        jLabel209.setName("jLabel209"); // NOI18N
        FormInput.add(jLabel209);
        jLabel209.setBounds(40, 1980, 80, 23);

        jLabel210.setText("Orang Tua");
        jLabel210.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel210.setName("jLabel210"); // NOI18N
        FormInput.add(jLabel210);
        jLabel210.setBounds(40, 1960, 80, 23);

        StatusGestasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Prematur", "Matur" }));
        StatusGestasi.setName("StatusGestasi"); // NOI18N
        StatusGestasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusGestasiKeyPressed(evt);
            }
        });
        FormInput.add(StatusGestasi);
        StatusGestasi.setBounds(130, 1930, 120, 23);

        jLabel213.setText("Lebih Banyak Tidur :");
        jLabel213.setName("jLabel213"); // NOI18N
        FormInput.add(jLabel213);
        jLabel213.setBounds(20, 1820, 100, 23);

        BanyakTidur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Siang Hari", "Malam Hari", "Tidak Tidur", "Menangis", "Tidur" }));
        BanyakTidur.setName("BanyakTidur"); // NOI18N
        BanyakTidur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BanyakTidurKeyPressed(evt);
            }
        });
        FormInput.add(BanyakTidur);
        BanyakTidur.setBounds(130, 1820, 120, 23);

        jLabel214.setText("Status Anak :");
        jLabel214.setName("jLabel214"); // NOI18N
        FormInput.add(jLabel214);
        jLabel214.setBounds(270, 1900, 70, 23);

        StatusAnak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Diinginkan", "Tidak Diinginkan", "Direncanakan", "Tidak Direncanakan", "Anak Adopsi" }));
        StatusAnak.setName("StatusAnak"); // NOI18N
        StatusAnak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusAnakKeyPressed(evt);
            }
        });
        FormInput.add(StatusAnak);
        StatusAnak.setBounds(350, 1900, 120, 23);

        jLabel217.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel217.setText("1. Ekspresi Wajah");
        jLabel217.setName("jLabel217"); // NOI18N
        FormInput.add(jLabel217);
        jLabel217.setBounds(70, 2260, 300, 23);

        SkalaResiko1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Otot-Otot Relaks", "Meringis" }));
        SkalaResiko1.setName("SkalaResiko1"); // NOI18N
        SkalaResiko1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko1ItemStateChanged(evt);
            }
        });
        SkalaResiko1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko1KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko1);
        SkalaResiko1.setBounds(430, 2260, 280, 23);

        jLabel218.setText("Nilai :");
        jLabel218.setName("jLabel218"); // NOI18N
        FormInput.add(jLabel218);
        jLabel218.setBounds(720, 2260, 75, 23);

        NilaiResiko1.setEditable(false);
        NilaiResiko1.setFocusTraversalPolicyProvider(true);
        NilaiResiko1.setName("NilaiResiko1"); // NOI18N
        FormInput.add(NilaiResiko1);
        NilaiResiko1.setBounds(800, 2260, 60, 23);

        jLabel219.setText("Skala :");
        jLabel219.setName("jLabel219"); // NOI18N
        FormInput.add(jLabel219);
        jLabel219.setBounds(340, 2260, 80, 23);

        jLabel220.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel220.setText("2. Menangis");
        jLabel220.setName("jLabel220"); // NOI18N
        FormInput.add(jLabel220);
        jLabel220.setBounds(70, 2290, 300, 23);

        jLabel221.setText("Skala :");
        jLabel221.setName("jLabel221"); // NOI18N
        FormInput.add(jLabel221);
        jLabel221.setBounds(340, 2290, 80, 23);

        SkalaResiko2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Menangis", "Mengerang", "Menangis Keras" }));
        SkalaResiko2.setName("SkalaResiko2"); // NOI18N
        SkalaResiko2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko2ItemStateChanged(evt);
            }
        });
        SkalaResiko2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko2KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko2);
        SkalaResiko2.setBounds(430, 2290, 280, 23);

        jLabel222.setText("Nilai :");
        jLabel222.setName("jLabel222"); // NOI18N
        FormInput.add(jLabel222);
        jLabel222.setBounds(720, 2290, 75, 23);

        NilaiResiko2.setEditable(false);
        NilaiResiko2.setFocusTraversalPolicyProvider(true);
        NilaiResiko2.setName("NilaiResiko2"); // NOI18N
        FormInput.add(NilaiResiko2);
        NilaiResiko2.setBounds(800, 2290, 60, 23);

        jLabel223.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel223.setText("3. Pola Pernafasan");
        jLabel223.setName("jLabel223"); // NOI18N
        FormInput.add(jLabel223);
        jLabel223.setBounds(70, 2320, 300, 23);

        jLabel224.setText("Skala :");
        jLabel224.setName("jLabel224"); // NOI18N
        FormInput.add(jLabel224);
        jLabel224.setBounds(340, 2320, 80, 23);

        SkalaResiko3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bernafas Relaks", "Perubahan Pola Nafas" }));
        SkalaResiko3.setName("SkalaResiko3"); // NOI18N
        SkalaResiko3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko3ItemStateChanged(evt);
            }
        });
        SkalaResiko3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko3KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko3);
        SkalaResiko3.setBounds(430, 2320, 280, 23);

        jLabel225.setText("Nilai :");
        jLabel225.setName("jLabel225"); // NOI18N
        FormInput.add(jLabel225);
        jLabel225.setBounds(720, 2320, 75, 23);

        NilaiResiko3.setEditable(false);
        NilaiResiko3.setFocusTraversalPolicyProvider(true);
        NilaiResiko3.setName("NilaiResiko3"); // NOI18N
        FormInput.add(NilaiResiko3);
        NilaiResiko3.setBounds(800, 2320, 60, 23);

        jLabel226.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel226.setText("4. Lengan");
        jLabel226.setName("jLabel226"); // NOI18N
        FormInput.add(jLabel226);
        jLabel226.setBounds(70, 2350, 300, 23);

        jLabel227.setText("Skala :");
        jLabel227.setName("jLabel227"); // NOI18N
        FormInput.add(jLabel227);
        jLabel227.setBounds(340, 2350, 80, 23);

        SkalaResiko4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Relaks/Terikat", "Fleksi/Ektensi" }));
        SkalaResiko4.setName("SkalaResiko4"); // NOI18N
        SkalaResiko4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko4ItemStateChanged(evt);
            }
        });
        SkalaResiko4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko4KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko4);
        SkalaResiko4.setBounds(430, 2350, 280, 23);

        jLabel228.setText("Nilai :");
        jLabel228.setName("jLabel228"); // NOI18N
        FormInput.add(jLabel228);
        jLabel228.setBounds(720, 2350, 75, 23);

        NilaiResiko4.setEditable(false);
        NilaiResiko4.setFocusTraversalPolicyProvider(true);
        NilaiResiko4.setName("NilaiResiko4"); // NOI18N
        FormInput.add(NilaiResiko4);
        NilaiResiko4.setBounds(800, 2350, 60, 23);

        jLabel229.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel229.setText("5. Kaki");
        jLabel229.setName("jLabel229"); // NOI18N
        FormInput.add(jLabel229);
        jLabel229.setBounds(70, 2380, 300, 23);

        jLabel230.setText("Skala :");
        jLabel230.setName("jLabel230"); // NOI18N
        FormInput.add(jLabel230);
        jLabel230.setBounds(340, 2380, 80, 23);

        SkalaResiko5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Relaks/Terikat", "Fleksi/Ektensi" }));
        SkalaResiko5.setName("SkalaResiko5"); // NOI18N
        SkalaResiko5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko5ItemStateChanged(evt);
            }
        });
        SkalaResiko5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko5KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko5);
        SkalaResiko5.setBounds(430, 2380, 280, 23);

        jLabel231.setText("Nilai :");
        jLabel231.setName("jLabel231"); // NOI18N
        FormInput.add(jLabel231);
        jLabel231.setBounds(720, 2380, 75, 23);

        NilaiResiko5.setEditable(false);
        NilaiResiko5.setFocusTraversalPolicyProvider(true);
        NilaiResiko5.setName("NilaiResiko5"); // NOI18N
        FormInput.add(NilaiResiko5);
        NilaiResiko5.setBounds(800, 2380, 60, 23);

        jLabel232.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel232.setText("6. Keadaan Kesadaran");
        jLabel232.setName("jLabel232"); // NOI18N
        FormInput.add(jLabel232);
        jLabel232.setBounds(70, 2410, 300, 23);

        jLabel233.setText("Skala :");
        jLabel233.setName("jLabel233"); // NOI18N
        FormInput.add(jLabel233);
        jLabel233.setBounds(340, 2410, 80, 23);

        SkalaResiko6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidur/Terjaga", "Rewel" }));
        SkalaResiko6.setName("SkalaResiko6"); // NOI18N
        SkalaResiko6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaResiko6ItemStateChanged(evt);
            }
        });
        SkalaResiko6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaResiko6KeyPressed(evt);
            }
        });
        FormInput.add(SkalaResiko6);
        SkalaResiko6.setBounds(430, 2410, 280, 23);

        jLabel234.setText("Nilai :");
        jLabel234.setName("jLabel234"); // NOI18N
        FormInput.add(jLabel234);
        jLabel234.setBounds(720, 2410, 75, 23);

        NilaiResiko6.setEditable(false);
        NilaiResiko6.setFocusTraversalPolicyProvider(true);
        NilaiResiko6.setName("NilaiResiko6"); // NOI18N
        FormInput.add(NilaiResiko6);
        NilaiResiko6.setBounds(800, 2410, 60, 23);

        jLabel235.setText("Total :");
        jLabel235.setName("jLabel235"); // NOI18N
        FormInput.add(jLabel235);
        jLabel235.setBounds(720, 2440, 75, 23);

        NilaiResikoTotal.setEditable(false);
        NilaiResikoTotal.setFocusTraversalPolicyProvider(true);
        NilaiResikoTotal.setName("NilaiResikoTotal"); // NOI18N
        FormInput.add(NilaiResikoTotal);
        NilaiResikoTotal.setBounds(800, 2440, 60, 23);

        jLabel236.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel236.setText("1. Pernafasan");
        jLabel236.setName("jLabel236"); // NOI18N
        FormInput.add(jLabel236);
        jLabel236.setBounds(70, 2500, 450, 23);

        jLabel237.setText("Skala :");
        jLabel237.setName("jLabel237"); // NOI18N
        FormInput.add(jLabel237);
        jLabel237.setBounds(450, 2500, 80, 23);

        SkalaSydney1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<60x/mnt", "60-80x/mnt", ">80x/mnt" }));
        SkalaSydney1.setName("SkalaSydney1"); // NOI18N
        SkalaSydney1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney1ItemStateChanged(evt);
            }
        });
        SkalaSydney1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney1KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney1);
        SkalaSydney1.setBounds(540, 2500, 170, 23);

        jLabel238.setText("Nilai :");
        jLabel238.setName("jLabel238"); // NOI18N
        FormInput.add(jLabel238);
        jLabel238.setBounds(720, 2500, 75, 23);

        NilaiSydney1.setEditable(false);
        NilaiSydney1.setFocusTraversalPolicyProvider(true);
        NilaiSydney1.setName("NilaiSydney1"); // NOI18N
        FormInput.add(NilaiSydney1);
        NilaiSydney1.setBounds(800, 2500, 60, 23);

        jLabel239.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel239.setText("2. Retraksi");
        jLabel239.setName("jLabel239"); // NOI18N
        FormInput.add(jLabel239);
        jLabel239.setBounds(70, 2530, 450, 23);

        jLabel240.setText("Skala :");
        jLabel240.setName("jLabel240"); // NOI18N
        FormInput.add(jLabel240);
        jLabel240.setBounds(450, 2530, 80, 23);

        SkalaSydney2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Ringan", "Berat" }));
        SkalaSydney2.setName("SkalaSydney2"); // NOI18N
        SkalaSydney2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney2ItemStateChanged(evt);
            }
        });
        SkalaSydney2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney2KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney2);
        SkalaSydney2.setBounds(540, 2530, 170, 23);

        jLabel241.setText("Nilai :");
        jLabel241.setName("jLabel241"); // NOI18N
        FormInput.add(jLabel241);
        jLabel241.setBounds(720, 2530, 75, 23);

        NilaiSydney2.setEditable(false);
        NilaiSydney2.setFocusTraversalPolicyProvider(true);
        NilaiSydney2.setName("NilaiSydney2"); // NOI18N
        FormInput.add(NilaiSydney2);
        NilaiSydney2.setBounds(800, 2530, 60, 23);

        jLabel242.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel242.setText("3. Sianosis");
        jLabel242.setName("jLabel242"); // NOI18N
        FormInput.add(jLabel242);
        jLabel242.setBounds(70, 2560, 450, 23);

        jLabel243.setText("Skala :");
        jLabel243.setName("jLabel243"); // NOI18N
        FormInput.add(jLabel243);
        jLabel243.setBounds(450, 2560, 80, 23);

        SkalaSydney3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Ada", "Hilang dengan O2", "Menetap dengan O2" }));
        SkalaSydney3.setName("SkalaSydney3"); // NOI18N
        SkalaSydney3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney3ItemStateChanged(evt);
            }
        });
        SkalaSydney3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney3KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney3);
        SkalaSydney3.setBounds(540, 2560, 170, 23);

        jLabel244.setText("Nilai :");
        jLabel244.setName("jLabel244"); // NOI18N
        FormInput.add(jLabel244);
        jLabel244.setBounds(720, 2560, 75, 23);

        NilaiSydney3.setEditable(false);
        NilaiSydney3.setFocusTraversalPolicyProvider(true);
        NilaiSydney3.setName("NilaiSydney3"); // NOI18N
        FormInput.add(NilaiSydney3);
        NilaiSydney3.setBounds(800, 2560, 60, 23);

        jLabel245.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel245.setText("4. Air Entry");
        jLabel245.setName("jLabel245"); // NOI18N
        FormInput.add(jLabel245);
        jLabel245.setBounds(70, 2590, 450, 23);

        jLabel246.setText("Skala :");
        jLabel246.setName("jLabel246"); // NOI18N
        FormInput.add(jLabel246);
        jLabel246.setBounds(450, 2590, 80, 23);

        SkalaSydney4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ada", "Menurun", "Tidak Terdengar" }));
        SkalaSydney4.setName("SkalaSydney4"); // NOI18N
        SkalaSydney4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney4ItemStateChanged(evt);
            }
        });
        SkalaSydney4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney4KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney4);
        SkalaSydney4.setBounds(540, 2590, 170, 23);

        jLabel247.setText("Nilai :");
        jLabel247.setName("jLabel247"); // NOI18N
        FormInput.add(jLabel247);
        jLabel247.setBounds(720, 2590, 75, 23);

        NilaiSydney4.setEditable(false);
        NilaiSydney4.setFocusTraversalPolicyProvider(true);
        NilaiSydney4.setName("NilaiSydney4"); // NOI18N
        FormInput.add(NilaiSydney4);
        NilaiSydney4.setBounds(800, 2590, 60, 23);

        jLabel248.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel248.setText("5. Merintih");
        jLabel248.setName("jLabel248"); // NOI18N
        FormInput.add(jLabel248);
        jLabel248.setBounds(70, 2620, 450, 23);

        jLabel249.setText("Skala :");
        jLabel249.setName("jLabel249"); // NOI18N
        FormInput.add(jLabel249);
        jLabel249.setBounds(450, 2620, 80, 23);

        SkalaSydney5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Merintih", "Terdengar Dengan Stetoskop", "Terdengar Tanpa Alat Bantu" }));
        SkalaSydney5.setName("SkalaSydney5"); // NOI18N
        SkalaSydney5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                SkalaSydney5ItemStateChanged(evt);
            }
        });
        SkalaSydney5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SkalaSydney5KeyPressed(evt);
            }
        });
        FormInput.add(SkalaSydney5);
        SkalaSydney5.setBounds(540, 2620, 170, 23);

        jLabel250.setText("Nilai :");
        jLabel250.setName("jLabel250"); // NOI18N
        FormInput.add(jLabel250);
        jLabel250.setBounds(720, 2620, 75, 23);

        NilaiSydney5.setEditable(false);
        NilaiSydney5.setFocusTraversalPolicyProvider(true);
        NilaiSydney5.setName("NilaiSydney5"); // NOI18N
        FormInput.add(NilaiSydney5);
        NilaiSydney5.setBounds(800, 2620, 60, 23);

        NilaiSydneyTotal.setEditable(false);
        NilaiSydneyTotal.setFocusTraversalPolicyProvider(true);
        NilaiSydneyTotal.setName("NilaiSydneyTotal"); // NOI18N
        FormInput.add(NilaiSydneyTotal);
        NilaiSydneyTotal.setBounds(800, 2650, 60, 23);

        jLabel270.setText("Total :");
        jLabel270.setName("jLabel270"); // NOI18N
        FormInput.add(jLabel270);
        jLabel270.setBounds(720, 2650, 75, 23);

        jSeparator12.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator12.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator12.setName("jSeparator12"); // NOI18N
        FormInput.add(jSeparator12);
        jSeparator12.setBounds(20, 2710, 880, 1);

        Scroll6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tbMasalahKeperawatan.setName("tbMasalahKeperawatan"); // NOI18N
        tbMasalahKeperawatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbMasalahKeperawatanMouseClicked(evt);
            }
        });
        tbMasalahKeperawatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbMasalahKeperawatanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbMasalahKeperawatanKeyReleased(evt);
            }
        });
        Scroll6.setViewportView(tbMasalahKeperawatan);

        FormInput.add(Scroll6);
        Scroll6.setBounds(30, 2720, 400, 143);

        TabRencanaKeperawatan.setBackground(new java.awt.Color(255, 255, 254));
        TabRencanaKeperawatan.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TabRencanaKeperawatan.setForeground(new java.awt.Color(50, 50, 50));
        TabRencanaKeperawatan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRencanaKeperawatan.setName("TabRencanaKeperawatan"); // NOI18N

        panelBiasa1.setName("panelBiasa1"); // NOI18N
        panelBiasa1.setLayout(new java.awt.BorderLayout());

        Scroll8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 253)));
        Scroll8.setName("Scroll8"); // NOI18N
        Scroll8.setOpaque(true);

        tbRencanaKeperawatan.setName("tbRencanaKeperawatan"); // NOI18N
        Scroll8.setViewportView(tbRencanaKeperawatan);

        panelBiasa1.add(Scroll8, java.awt.BorderLayout.CENTER);

        TabRencanaKeperawatan.addTab("Rencana Keperawatan", panelBiasa1);

        scrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane5.setName("scrollPane5"); // NOI18N

        Rencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Rencana.setColumns(20);
        Rencana.setRows(5);
        Rencana.setName("Rencana"); // NOI18N
        Rencana.setOpaque(true);
        Rencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(Rencana);

        TabRencanaKeperawatan.addTab("Rencana Keperawatan Lainnya", scrollPane5);

        FormInput.add(TabRencanaKeperawatan);
        TabRencanaKeperawatan.setBounds(450, 2720, 610, 143);

        BtnTambahMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahMasalah.setMnemonic('3');
        BtnTambahMasalah.setToolTipText("Alt+3");
        BtnTambahMasalah.setName("BtnTambahMasalah"); // NOI18N
        BtnTambahMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahMasalahActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahMasalah);
        BtnTambahMasalah.setBounds(380, 2870, 28, 23);

        BtnAllMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllMasalah.setMnemonic('2');
        BtnAllMasalah.setToolTipText("2Alt+2");
        BtnAllMasalah.setName("BtnAllMasalah"); // NOI18N
        BtnAllMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllMasalahActionPerformed(evt);
            }
        });
        BtnAllMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllMasalahKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllMasalah);
        BtnAllMasalah.setBounds(350, 2870, 28, 23);

        BtnCariMasalah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariMasalah.setMnemonic('1');
        BtnCariMasalah.setToolTipText("Alt+1");
        BtnCariMasalah.setName("BtnCariMasalah"); // NOI18N
        BtnCariMasalah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariMasalah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariMasalahActionPerformed(evt);
            }
        });
        BtnCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariMasalah);
        BtnCariMasalah.setBounds(320, 2870, 28, 23);

        TCariMasalah.setToolTipText("Alt+C");
        TCariMasalah.setName("TCariMasalah"); // NOI18N
        TCariMasalah.setPreferredSize(new java.awt.Dimension(140, 23));
        TCariMasalah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariMasalahKeyPressed(evt);
            }
        });
        FormInput.add(TCariMasalah);
        TCariMasalah.setBounds(100, 2870, 215, 23);

        BtnTambahRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambahRencana.setMnemonic('3');
        BtnTambahRencana.setToolTipText("Alt+3");
        BtnTambahRencana.setName("BtnTambahRencana"); // NOI18N
        BtnTambahRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambahRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahRencanaActionPerformed(evt);
            }
        });
        FormInput.add(BtnTambahRencana);
        BtnTambahRencana.setBounds(830, 2870, 28, 23);

        BtnAllRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAllRencana.setMnemonic('2');
        BtnAllRencana.setToolTipText("2Alt+2");
        BtnAllRencana.setName("BtnAllRencana"); // NOI18N
        BtnAllRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAllRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllRencanaActionPerformed(evt);
            }
        });
        BtnAllRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllRencanaKeyPressed(evt);
            }
        });
        FormInput.add(BtnAllRencana);
        BtnAllRencana.setBounds(800, 2870, 28, 23);

        BtnCariRencana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariRencana.setMnemonic('1');
        BtnCariRencana.setToolTipText("Alt+1");
        BtnCariRencana.setName("BtnCariRencana"); // NOI18N
        BtnCariRencana.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariRencana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariRencanaActionPerformed(evt);
            }
        });
        BtnCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(BtnCariRencana);
        BtnCariRencana.setBounds(760, 2870, 28, 23);

        label13.setText("Key Word :");
        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label13);
        label13.setBounds(460, 2870, 60, 23);

        TCariRencana.setToolTipText("Alt+C");
        TCariRencana.setName("TCariRencana"); // NOI18N
        TCariRencana.setPreferredSize(new java.awt.Dimension(215, 23));
        TCariRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariRencanaKeyPressed(evt);
            }
        });
        FormInput.add(TCariRencana);
        TCariRencana.setBounds(520, 2870, 235, 23);

        label12.setText("Key Word :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(60, 23));
        FormInput.add(label12);
        label12.setBounds(40, 2870, 60, 23);

        jLabel284.setText("Suhu :");
        jLabel284.setName("jLabel284"); // NOI18N
        FormInput.add(jLabel284);
        jLabel284.setBounds(200, 1530, 40, 23);

        jLabel285.setText("Kuku :");
        jLabel285.setName("jLabel285"); // NOI18N
        FormInput.add(jLabel285);
        jLabel285.setBounds(60, 1720, 40, 23);

        jLabel286.setText("Warna Kulit :");
        jLabel286.setName("jLabel286"); // NOI18N
        FormInput.add(jLabel286);
        jLabel286.setBounds(30, 1690, 70, 23);

        jLabel288.setText("Abdomen :");
        jLabel288.setName("jLabel288"); // NOI18N
        FormInput.add(jLabel288);
        jLabel288.setBounds(40, 1750, 60, 23);

        jLabel59.setText("Ditolong Oleh :");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(520, 280, 80, 23);

        DitolongOleh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dokter", "Bidan", "Dukun", "Lain-Lain" }));
        DitolongOleh.setName("DitolongOleh"); // NOI18N
        DitolongOleh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DitolongOlehKeyPressed(evt);
            }
        });
        FormInput.add(DitolongOleh);
        DitolongOleh.setBounds(600, 280, 80, 23);

        jLabel290.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel290.setText("Minggu");
        jLabel290.setName("jLabel290"); // NOI18N
        FormInput.add(jLabel290);
        jLabel290.setBounds(260, 310, 40, 23);

        jLabel37.setText("BBL :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(490, 310, 40, 23);

        BBL.setFocusTraversalPolicyProvider(true);
        BBL.setName("BBL"); // NOI18N
        BBL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BBLKeyPressed(evt);
            }
        });
        FormInput.add(BBL);
        BBL.setBounds(540, 310, 90, 23);

        jLabel291.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel291.setText("gram");
        jLabel291.setName("jLabel291"); // NOI18N
        FormInput.add(jLabel291);
        jLabel291.setBounds(630, 310, 30, 23);

        jLabel40.setText("PBL :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(680, 310, 40, 23);

        PBL.setFocusTraversalPolicyProvider(true);
        PBL.setName("PBL"); // NOI18N
        PBL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PBLKeyPressed(evt);
            }
        });
        FormInput.add(PBL);
        PBL.setBounds(730, 310, 90, 23);

        jLabel292.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel292.setText("cm");
        jLabel292.setName("jLabel292"); // NOI18N
        FormInput.add(jLabel292);
        jLabel292.setBounds(820, 310, 20, 23);

        jLabel293.setText("Riwayat Pembedahan :");
        jLabel293.setName("jLabel293"); // NOI18N
        FormInput.add(jLabel293);
        jLabel293.setBounds(510, 340, 120, 23);

        RiwayatPembedahan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak Pernah Operasi", "Pernah Operasi" }));
        RiwayatPembedahan.setName("RiwayatPembedahan"); // NOI18N
        RiwayatPembedahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RiwayatPembedahanKeyPressed(evt);
            }
        });
        FormInput.add(RiwayatPembedahan);
        RiwayatPembedahan.setBounds(640, 340, 140, 23);

        OperasiHari.setFocusTraversalPolicyProvider(true);
        OperasiHari.setName("OperasiHari"); // NOI18N
        OperasiHari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OperasiHariKeyPressed(evt);
            }
        });
        FormInput.add(OperasiHari);
        OperasiHari.setBounds(790, 340, 60, 23);

        SebutAlergi.setFocusTraversalPolicyProvider(true);
        SebutAlergi.setName("SebutAlergi"); // NOI18N
        SebutAlergi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SebutAlergiKeyPressed(evt);
            }
        });
        FormInput.add(SebutAlergi);
        SebutAlergi.setBounds(320, 370, 140, 23);

        RisikoGizi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kecil Masa Kehamilan (KMK)", "Sesuai Masa Kehamilan (SMK)", "Besar Masa Kehamilan (BMK)" }));
        RisikoGizi.setName("RisikoGizi"); // NOI18N
        RisikoGizi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RisikoGiziKeyPressed(evt);
            }
        });
        FormInput.add(RisikoGizi);
        RisikoGizi.setBounds(580, 400, 200, 23);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel39.setText("( BBL : Berat Badan Lahir, BBS : Berat Badan Sekarang, LK : Lingkar Kepala, LD : Lingkar Dada, LP : Lingkar Perut )");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(120, 430, 560, 23);

        jLabel41.setText("LP :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(750, 460, 20, 23);

        LP.setFocusTraversalPolicyProvider(true);
        LP.setName("LP"); // NOI18N
        LP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LPKeyPressed(evt);
            }
        });
        FormInput.add(LP);
        LP.setBounds(770, 460, 50, 23);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("%");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput.add(jLabel45);
        jLabel45.setBounds(720, 510, 20, 23);

        jLabel289.setText("Spontan :");
        jLabel289.setName("jLabel289"); // NOI18N
        FormInput.add(jLabel289);
        jLabel289.setBounds(230, 510, 50, 23);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("cm");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(720, 460, 20, 23);

        jLabel294.setText("Frekuensi :");
        jLabel294.setName("jLabel294"); // NOI18N
        FormInput.add(jLabel294);
        jLabel294.setBounds(390, 510, 60, 23);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel60.setText("cm");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(820, 460, 20, 23);

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel61.setText("RIWAYAT KESEHATAN");
        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(20, 130, 130, 23);

        jLabel295.setText("Retraksi :");
        jLabel295.setName("jLabel295"); // NOI18N
        FormInput.add(jLabel295);
        jLabel295.setBounds(230, 540, 50, 23);

        Retraksi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Retraksi.setName("Retraksi"); // NOI18N
        Retraksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RetraksiKeyPressed(evt);
            }
        });
        FormInput.add(Retraksi);
        Retraksi.setBounds(290, 540, 80, 23);

        jLabel296.setText("Sianosis :");
        jLabel296.setName("jLabel296"); // NOI18N
        FormInput.add(jLabel296);
        jLabel296.setBounds(400, 540, 50, 23);

        Sianosis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Sianosis.setName("Sianosis"); // NOI18N
        Sianosis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SianosisKeyPressed(evt);
            }
        });
        FormInput.add(Sianosis);
        Sianosis.setBounds(460, 540, 80, 23);

        HasilRespirasi.setFocusTraversalPolicyProvider(true);
        HasilRespirasi.setName("HasilRespirasi"); // NOI18N
        HasilRespirasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HasilRespirasiKeyPressed(evt);
            }
        });
        FormInput.add(HasilRespirasi);
        HasilRespirasi.setBounds(460, 600, 135, 23);

        jLabel133.setText("Saturasi O2 :");
        jLabel133.setName("jLabel133"); // NOI18N
        FormInput.add(jLabel133);
        jLabel133.setBounds(600, 510, 64, 23);

        ccLendir.setFocusTraversalPolicyProvider(true);
        ccLendir.setName("ccLendir"); // NOI18N
        ccLendir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ccLendirKeyPressed(evt);
            }
        });
        FormInput.add(ccLendir);
        ccLendir.setBounds(780, 540, 50, 23);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel63.setText("cc");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput.add(jLabel63);
        jLabel63.setBounds(830, 540, 20, 23);

        jLabel297.setText("Wheezing :");
        jLabel297.setName("jLabel297"); // NOI18N
        FormInput.add(jLabel297);
        jLabel297.setBounds(0, 570, 109, 23);

        Wheezing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Wheezing.setName("Wheezing"); // NOI18N
        Wheezing.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WheezingKeyPressed(evt);
            }
        });
        FormInput.add(Wheezing);
        Wheezing.setBounds(120, 570, 90, 23);

        jLabel298.setText("Ronchi :");
        jLabel298.setName("jLabel298"); // NOI18N
        FormInput.add(jLabel298);
        jLabel298.setBounds(230, 570, 50, 23);

        Ronchi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Ronchi.setName("Ronchi"); // NOI18N
        Ronchi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RonchiKeyPressed(evt);
            }
        });
        FormInput.add(Ronchi);
        Ronchi.setBounds(290, 570, 80, 23);

        jLabel134.setText("Hasil :");
        jLabel134.setName("jLabel134"); // NOI18N
        FormInput.add(jLabel134);
        jLabel134.setBounds(410, 600, 40, 23);

        ccWSD.setFocusTraversalPolicyProvider(true);
        ccWSD.setName("ccWSD"); // NOI18N
        ccWSD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ccWSDKeyPressed(evt);
            }
        });
        FormInput.add(ccWSD);
        ccWSD.setBounds(540, 570, 40, 23);

        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel64.setText("cc");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput.add(jLabel64);
        jLabel64.setBounds(580, 570, 20, 23);

        WSD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        WSD.setName("WSD"); // NOI18N
        WSD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WSDKeyPressed(evt);
            }
        });
        FormInput.add(WSD);
        WSD.setBounds(460, 570, 80, 23);

        ccSuction.setFocusTraversalPolicyProvider(true);
        ccSuction.setName("ccSuction"); // NOI18N
        ccSuction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ccSuctionKeyPressed(evt);
            }
        });
        FormInput.add(ccSuction);
        ccSuction.setBounds(790, 570, 40, 23);

        jLabel135.setText("Continous Suction :");
        jLabel135.setName("jLabel135"); // NOI18N
        FormInput.add(jLabel135);
        jLabel135.setBounds(600, 570, 100, 23);

        Suction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Suction.setName("Suction"); // NOI18N
        Suction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuctionKeyPressed(evt);
            }
        });
        FormInput.add(Suction);
        Suction.setBounds(710, 570, 80, 23);

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel65.setText("cc");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput.add(jLabel65);
        jLabel65.setBounds(900, 790, 20, 23);

        jLabel136.setText("WSD :");
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput.add(jLabel136);
        jLabel136.setBounds(420, 570, 30, 23);

        jLabel137.setText("Keterangan :");
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput.add(jLabel137);
        jLabel137.setBounds(610, 600, 70, 23);

        KeteranganRespirasi.setFocusTraversalPolicyProvider(true);
        KeteranganRespirasi.setName("KeteranganRespirasi"); // NOI18N
        KeteranganRespirasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganRespirasiKeyPressed(evt);
            }
        });
        FormInput.add(KeteranganRespirasi);
        KeteranganRespirasi.setBounds(685, 600, 160, 23);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setText("RESPIRASI");
        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(20, 490, 90, 23);

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel62.setText("SKRINING GIZI ");
        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput.add(jLabel62);
        jLabel62.setBounds(20, 430, 90, 23);

        jLabel251.setText("Pengisian Kapiler :");
        jLabel251.setName("jLabel251"); // NOI18N
        FormInput.add(jLabel251);
        jLabel251.setBounds(560, 710, 100, 23);

        jLabel252.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel252.setText("A. Kanan");
        jLabel252.setName("jLabel252"); // NOI18N
        FormInput.add(jLabel252);
        jLabel252.setBounds(250, 680, 50, 23);

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel66.setText("jam");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(830, 570, 20, 23);

        jLabel253.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel253.setText("Hari Ke");
        jLabel253.setName("jLabel253"); // NOI18N
        FormInput.add(jLabel253);
        jLabel253.setBounds(580, 740, 50, 23);

        Nadi.setFocusTraversalPolicyProvider(true);
        Nadi.setName("Nadi"); // NOI18N
        Nadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NadiKeyPressed(evt);
            }
        });
        FormInput.add(Nadi);
        Nadi.setBounds(670, 680, 70, 23);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel67.setText("x/mnt");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput.add(jLabel67);
        jLabel67.setBounds(740, 680, 40, 23);

        jLabel254.setText("Jenis Pendarahan :");
        jLabel254.setName("jLabel254"); // NOI18N
        FormInput.add(jLabel254);
        jLabel254.setBounds(560, 650, 100, 23);

        JenisPendarahan.setFocusTraversalPolicyProvider(true);
        JenisPendarahan.setName("JenisPendarahan"); // NOI18N
        JenisPendarahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JenisPendarahanKeyPressed(evt);
            }
        });
        FormInput.add(JenisPendarahan);
        JenisPendarahan.setBounds(670, 650, 130, 23);

        ccPendarahan.setFocusTraversalPolicyProvider(true);
        ccPendarahan.setName("ccPendarahan"); // NOI18N
        ccPendarahan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ccPendarahanKeyPressed(evt);
            }
        });
        FormInput.add(ccPendarahan);
        ccPendarahan.setBounds(800, 650, 40, 23);

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel69.setText("cm");
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput.add(jLabel69);
        jLabel69.setBounds(520, 1030, 20, 23);

        jLabel255.setText("Nadi :");
        jLabel255.setName("jLabel255"); // NOI18N
        FormInput.add(jLabel255);
        jLabel255.setBounds(620, 680, 40, 23);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("SIRKULASI");
        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(20, 630, 90, 23);

        persenBeratBadan.setFocusTraversalPolicyProvider(true);
        persenBeratBadan.setName("persenBeratBadan"); // NOI18N
        persenBeratBadan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                persenBeratBadanKeyPressed(evt);
            }
        });
        FormInput.add(persenBeratBadan);
        persenBeratBadan.setBounds(270, 790, 40, 23);

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel70.setText("%");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput.add(jLabel70);
        jLabel70.setBounds(900, 820, 20, 23);

        jLabel256.setText("Reflex Isap :");
        jLabel256.setName("jLabel256"); // NOI18N
        FormInput.add(jLabel256);
        jLabel256.setBounds(40, 820, 70, 23);

        ReflexIsap.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kuat", "Lemah", "Tidak Ada", "Belum Ada" }));
        ReflexIsap.setName("ReflexIsap"); // NOI18N
        ReflexIsap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ReflexIsapKeyPressed(evt);
            }
        });
        FormInput.add(ReflexIsap);
        ReflexIsap.setBounds(120, 820, 150, 23);

        jLabel257.setText("Reflex Telan :");
        jLabel257.setName("jLabel257"); // NOI18N
        FormInput.add(jLabel257);
        jLabel257.setBounds(40, 850, 70, 23);

        ReflexTelan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kuat", "Lemah", "Tidak Ada", "Belum Ada" }));
        ReflexTelan.setName("ReflexTelan"); // NOI18N
        ReflexTelan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ReflexTelanKeyPressed(evt);
            }
        });
        FormInput.add(ReflexTelan);
        ReflexTelan.setBounds(120, 850, 150, 23);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel68.setText("dtk");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(740, 710, 20, 23);

        jLabel258.setText("Muntah :");
        jLabel258.setName("jLabel258"); // NOI18N
        FormInput.add(jLabel258);
        jLabel258.setBounds(770, 790, 50, 23);

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel71.setText("%");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(310, 790, 20, 23);

        jLabel259.setText("Keterangan :");
        jLabel259.setName("jLabel259"); // NOI18N
        FormInput.add(jLabel259);
        jLabel259.setBounds(340, 910, 70, 23);

        jLabel260.setText("Ubun Ubun :");
        jLabel260.setName("jLabel260"); // NOI18N
        FormInput.add(jLabel260);
        jLabel260.setBounds(300, 880, 109, 23);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel49.setText("NUTRISI DAN CAIRAN");
        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput.add(jLabel49);
        jLabel49.setBounds(20, 770, 130, 23);

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText("cc");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(840, 650, 20, 23);

        jLabel126.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel126.setText("mm");
        jLabel126.setName("jLabel126"); // NOI18N
        FormInput.add(jLabel126);
        jLabel126.setBounds(210, 1060, 18, 23);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("NEUROSENSORI");
        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput.add(jLabel50);
        jLabel50.setBounds(20, 940, 130, 23);

        jLabel130.setText("Pupil :");
        jLabel130.setName("jLabel130"); // NOI18N
        FormInput.add(jLabel130);
        jLabel130.setBounds(90, 1060, 40, 23);

        jLabel138.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel138.setText("C");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(450, 1690, 10, 23);

        jLabel160.setText("LAKI-LAKI");
        jLabel160.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel160.setName("jLabel160"); // NOI18N
        FormInput.add(jLabel160);
        jLabel160.setBounds(50, 1290, 60, 23);

        jLabel161.setText("Produksi Urine :");
        jLabel161.setName("jLabel161"); // NOI18N
        FormInput.add(jLabel161);
        jLabel161.setBounds(230, 1210, 80, 23);

        jLabel180.setText("Pseudo :");
        jLabel180.setName("jLabel180"); // NOI18N
        FormInput.add(jLabel180);
        jLabel180.setBounds(230, 1260, 50, 23);

        Kateter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Kateter.setName("Kateter"); // NOI18N
        Kateter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KateterKeyPressed(evt);
            }
        });
        FormInput.add(Kateter);
        Kateter.setBounds(440, 1260, 80, 23);

        jLabel261.setText("Prominen :");
        jLabel261.setName("jLabel261"); // NOI18N
        FormInput.add(jLabel261);
        jLabel261.setBounds(530, 1260, 60, 23);

        Prominen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Prominen.setName("Prominen"); // NOI18N
        Prominen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProminenKeyPressed(evt);
            }
        });
        FormInput.add(Prominen);
        Prominen.setBounds(600, 1260, 80, 23);

        jLabel262.setText("Ambigus :");
        jLabel262.setName("jLabel262"); // NOI18N
        FormInput.add(jLabel262);
        jLabel262.setBounds(700, 1260, 60, 23);

        Ambigus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Ambigus.setName("Ambigus"); // NOI18N
        Ambigus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AmbigusKeyPressed(evt);
            }
        });
        FormInput.add(Ambigus);
        Ambigus.setBounds(770, 1260, 90, 23);

        jLabel162.setText("PEREMPUAN");
        jLabel162.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel162.setName("jLabel162"); // NOI18N
        FormInput.add(jLabel162);
        jLabel162.setBounds(30, 1240, 80, 23);

        jLabel263.setText("Preputium :");
        jLabel263.setName("jLabel263"); // NOI18N
        FormInput.add(jLabel263);
        jLabel263.setBounds(50, 1310, 60, 23);

        jLabel120.setText("Frekuensi BAK :");
        jLabel120.setName("jLabel120"); // NOI18N
        FormInput.add(jLabel120);
        jLabel120.setBounds(20, 1210, 90, 23);

        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel121.setText("A. Buang Air Kecil");
        jLabel121.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel121.setName("jLabel121"); // NOI18N
        FormInput.add(jLabel121);
        jLabel121.setBounds(20, 1180, 110, 23);

        jLabel163.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel163.setText("x/hari");
        jLabel163.setName("jLabel163"); // NOI18N
        FormInput.add(jLabel163);
        jLabel163.setBounds(180, 1210, 30, 23);

        jLabel264.setText("Keluar Mekonium :");
        jLabel264.setName("jLabel264"); // NOI18N
        FormInput.add(jLabel264);
        jLabel264.setBounds(30, 1430, 120, 23);

        jLabel185.setText("Keadaan Saat Ini :");
        jLabel185.setName("jLabel185"); // NOI18N
        FormInput.add(jLabel185);
        jLabel185.setBounds(370, 1400, 100, 23);

        jLabel167.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel167.setText("cc");
        jLabel167.setName("jLabel167"); // NOI18N
        FormInput.add(jLabel167);
        jLabel167.setBounds(300, 1370, 20, 23);

        jLabel265.setText("Buang Air Besar (BAB) :");
        jLabel265.setName("jLabel265"); // NOI18N
        FormInput.add(jLabel265);
        jLabel265.setBounds(30, 1370, 120, 23);

        jLabel168.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel168.setText("x/hari");
        jLabel168.setName("jLabel168"); // NOI18N
        FormInput.add(jLabel168);
        jLabel168.setBounds(800, 1400, 30, 23);

        jLabel169.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel169.setText("jam setelah lahir");
        jLabel169.setName("jLabel169"); // NOI18N
        FormInput.add(jLabel169);
        jLabel169.setBounds(210, 1430, 100, 23);

        jLabel266.setText("Integritas :");
        jLabel266.setName("jLabel266"); // NOI18N
        FormInput.add(jLabel266);
        jLabel266.setBounds(120, 1570, 60, 23);

        jLabel190.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel190.setText("KEAMANAN DAN PROTEKSI");
        jLabel190.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel190.setName("jLabel190"); // NOI18N
        FormInput.add(jLabel190);
        jLabel190.setBounds(20, 1460, 160, 23);

        jLabel267.setText("Mata/Secret :");
        jLabel267.setName("jLabel267"); // NOI18N
        FormInput.add(jLabel267);
        jLabel267.setBounds(260, 1720, 70, 23);

        jLabel175.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel175.setText("C");
        jLabel175.setName("jLabel175"); // NOI18N
        FormInput.add(jLabel175);
        jLabel175.setBounds(300, 1530, 10, 23);

        jLabel192.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel192.setText("KEBERSIHAN DIRI");
        jLabel192.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel192.setName("jLabel192"); // NOI18N
        FormInput.add(jLabel192);
        jLabel192.setBounds(20, 1660, 160, 23);

        jLabel203.setText("Kondisi/Jenis :");
        jLabel203.setName("jLabel203"); // NOI18N
        FormInput.add(jLabel203);
        jLabel203.setBounds(240, 1750, 90, 23);

        jLabel195.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel195.setText("TIDUR DAN ISTIRAHAT");
        jLabel195.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel195.setName("jLabel195"); // NOI18N
        FormInput.add(jLabel195);
        jLabel195.setBounds(20, 1790, 130, 23);

        jLabel268.setText("Status Gestasi :");
        jLabel268.setName("jLabel268"); // NOI18N
        FormInput.add(jLabel268);
        jLabel268.setBounds(40, 1930, 77, 23);

        jLabel269.setText("Menyentuh :");
        jLabel269.setName("jLabel269"); // NOI18N
        FormInput.add(jLabel269);
        jLabel269.setBounds(470, 1980, 79, 23);

        Menyentuh.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Menyentuh.setName("Menyentuh"); // NOI18N
        Menyentuh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenyentuhKeyPressed(evt);
            }
        });
        FormInput.add(Menyentuh);
        Menyentuh.setBounds(560, 1980, 110, 23);

        jLabel280.setText("Berbicara :");
        jLabel280.setName("jLabel280"); // NOI18N
        FormInput.add(jLabel280);
        jLabel280.setBounds(660, 1980, 79, 23);

        Berbicara.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        Berbicara.setName("Berbicara"); // NOI18N
        Berbicara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BerbicaraKeyPressed(evt);
            }
        });
        FormInput.add(Berbicara);
        Berbicara.setBounds(750, 1980, 110, 23);

        jLabel281.setText("Menggendong :");
        jLabel281.setName("jLabel281"); // NOI18N
        FormInput.add(jLabel281);
        jLabel281.setBounds(40, 2010, 80, 23);

        Menggendong.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ya", "Tidak" }));
        Menggendong.setName("Menggendong"); // NOI18N
        Menggendong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenggendongKeyPressed(evt);
            }
        });
        FormInput.add(Menggendong);
        Menggendong.setBounds(130, 2010, 110, 23);

        jLabel282.setText("Yang Merawat :");
        jLabel282.setName("jLabel282"); // NOI18N
        FormInput.add(jLabel282);
        jLabel282.setBounds(240, 2010, 80, 23);

        YangMerawat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ibu", "Ayah", "Nenek", "Pengasuh", "Lain-Lain" }));
        YangMerawat.setName("YangMerawat"); // NOI18N
        YangMerawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                YangMerawatKeyPressed(evt);
            }
        });
        FormInput.add(YangMerawat);
        YangMerawat.setBounds(330, 2010, 120, 23);

        jLabel194.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel194.setText("PSIKOSOSIAL");
        jLabel194.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel194.setName("jLabel194"); // NOI18N
        FormInput.add(jLabel194);
        jLabel194.setBounds(20, 1870, 130, 23);

        jLabel283.setText("Perawatan Diri (Mandi, BAB, BAK) :");
        jLabel283.setName("jLabel283"); // NOI18N
        FormInput.add(jLabel283);
        jLabel283.setBounds(50, 2110, 170, 23);

        PerawatanDiri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PerawatanDiri.setName("PerawatanDiri"); // NOI18N
        PerawatanDiri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerawatanDiriKeyPressed(evt);
            }
        });
        FormInput.add(PerawatanDiri);
        PerawatanDiri.setBounds(230, 2110, 110, 23);

        LainLainDischargePlaning.setFocusTraversalPolicyProvider(true);
        LainLainDischargePlaning.setName("LainLainDischargePlaning"); // NOI18N
        FormInput.add(LainLainDischargePlaning);
        LainLainDischargePlaning.setBounds(730, 2140, 120, 24);

        jLabel198.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel198.setText("PERENCANAAN PEMULANGAN");
        jLabel198.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel198.setName("jLabel198"); // NOI18N
        FormInput.add(jLabel198);
        jLabel198.setBounds(20, 2060, 170, 23);

        jLabel200.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel200.setText("PENILAIAN SKALA NYERI");
        jLabel200.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel200.setName("jLabel200"); // NOI18N
        FormInput.add(jLabel200);
        jLabel200.setBounds(20, 2230, 170, 23);

        ReflexMoro.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ReflexMoro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tidak", "Ya" }));
        ReflexMoro.setName("ReflexMoro"); // NOI18N
        FormInput.add(ReflexMoro);
        ReflexMoro.setBounds(590, 990, 80, 24);

        jLabel176.setText("Reflex Moro :");
        jLabel176.setName("jLabel176"); // NOI18N
        FormInput.add(jLabel176);
        jLabel176.setBounds(460, 990, 120, 23);

        ReflexBabinski.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ReflexBabinski.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tidak", "Ya" }));
        ReflexBabinski.setName("ReflexBabinski"); // NOI18N
        FormInput.add(ReflexBabinski);
        ReflexBabinski.setBounds(360, 990, 70, 24);

        jLabel177.setText("Reflex Babinski :");
        jLabel177.setName("jLabel177"); // NOI18N
        FormInput.add(jLabel177);
        jLabel177.setBounds(230, 990, 120, 23);

        ReflexPlantarGrasp.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ReflexPlantarGrasp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tidak", "Ya" }));
        ReflexPlantarGrasp.setName("ReflexPlantarGrasp"); // NOI18N
        FormInput.add(ReflexPlantarGrasp);
        ReflexPlantarGrasp.setBounds(830, 960, 70, 24);

        jLabel178.setText("Reflex Plantar Grasp :");
        jLabel178.setName("jLabel178"); // NOI18N
        FormInput.add(jLabel178);
        jLabel178.setBounds(700, 960, 120, 23);

        reflexpalmargrasp.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        reflexpalmargrasp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tidak", "Ya" }));
        reflexpalmargrasp.setName("reflexpalmargrasp"); // NOI18N
        FormInput.add(reflexpalmargrasp);
        reflexpalmargrasp.setBounds(140, 990, 70, 24);

        jLabel204.setText("Reflex Palmar Grasp :");
        jLabel204.setName("jLabel204"); // NOI18N
        FormInput.add(jLabel204);
        jLabel204.setBounds(10, 990, 120, 23);

        PanelWall1.setBackground(new java.awt.Color(255, 255, 255));
        PanelWall1.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/Tabel Derajat Dehidrasi.png"))); // NOI18N
        PanelWall1.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall1.setOpaque(true);
        PanelWall1.setPreferredSize(new java.awt.Dimension(200, 200));
        PanelWall1.setRound(false);
        PanelWall1.setWarna(new java.awt.Color(110, 110, 110));
        PanelWall1.setLayout(null);
        FormInput.add(PanelWall1);
        PanelWall1.setBounds(320, 1480, 360, 180);

        jLabel287.setText("Pemantauan pemberian obat :");
        jLabel287.setName("jLabel287"); // NOI18N
        FormInput.add(jLabel287);
        jLabel287.setBounds(50, 2140, 170, 23);

        PemantauanPemberianObat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PemantauanPemberianObat.setName("PemantauanPemberianObat"); // NOI18N
        PemantauanPemberianObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemantauanPemberianObatKeyPressed(evt);
            }
        });
        FormInput.add(PemantauanPemberianObat);
        PemantauanPemberianObat.setBounds(230, 2140, 110, 23);

        jLabel299.setText("Pemberian ASI :");
        jLabel299.setName("jLabel299"); // NOI18N
        FormInput.add(jLabel299);
        jLabel299.setBounds(50, 2170, 170, 23);

        PemberianASI.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PemberianASI.setName("PemberianASI"); // NOI18N
        PemberianASI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PemberianASIKeyPressed(evt);
            }
        });
        FormInput.add(PemberianASI);
        PemberianASI.setBounds(230, 2170, 110, 23);

        jLabel300.setText("Lain-Lain");
        jLabel300.setName("jLabel300"); // NOI18N
        FormInput.add(jLabel300);
        jLabel300.setBounds(660, 2140, 60, 23);

        PerawatanMetodeKangguru.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PerawatanMetodeKangguru.setName("PerawatanMetodeKangguru"); // NOI18N
        PerawatanMetodeKangguru.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerawatanMetodeKangguruKeyPressed(evt);
            }
        });
        FormInput.add(PerawatanMetodeKangguru);
        PerawatanMetodeKangguru.setBounds(560, 2110, 110, 23);

        jLabel301.setText("Perawatan luka :");
        jLabel301.setName("jLabel301"); // NOI18N
        FormInput.add(jLabel301);
        jLabel301.setBounds(310, 2140, 240, 23);

        PerawatanLuka.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PerawatanLuka.setName("PerawatanLuka"); // NOI18N
        PerawatanLuka.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PerawatanLukaKeyPressed(evt);
            }
        });
        FormInput.add(PerawatanLuka);
        PerawatanLuka.setBounds(560, 2140, 110, 23);

        jLabel302.setText("Pendampingan tenaga khusus di rumah :");
        jLabel302.setName("jLabel302"); // NOI18N
        FormInput.add(jLabel302);
        jLabel302.setBounds(310, 2170, 240, 23);

        PendampinganTenagaKhususDiRumah.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tidak", "Ya" }));
        PendampinganTenagaKhususDiRumah.setName("PendampinganTenagaKhususDiRumah"); // NOI18N
        PendampinganTenagaKhususDiRumah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PendampinganTenagaKhususDiRumahKeyPressed(evt);
            }
        });
        FormInput.add(PendampinganTenagaKhususDiRumah);
        PendampinganTenagaKhususDiRumah.setBounds(560, 2170, 110, 23);

        jLabel303.setText("Perencanaan Pemulangan/Discharge Planning :");
        jLabel303.setName("jLabel303"); // NOI18N
        FormInput.add(jLabel303);
        jLabel303.setBounds(40, 2080, 240, 23);

        jLabel304.setText("Perawatan Metode Kangguru :");
        jLabel304.setName("jLabel304"); // NOI18N
        FormInput.add(jLabel304);
        jLabel304.setBounds(310, 2110, 240, 23);

        KeteranganHasilLab1.setFocusTraversalPolicyProvider(true);
        KeteranganHasilLab1.setName("KeteranganHasilLab1"); // NOI18N
        KeteranganHasilLab1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KeteranganHasilLab1KeyPressed(evt);
            }
        });
        FormInput.add(KeteranganHasilLab1);
        KeteranganHasilLab1.setBounds(420, 910, 220, 23);

        jLabel206.setText("Cara Minum :");
        jLabel206.setName("jLabel206"); // NOI18N
        FormInput.add(jLabel206);
        jLabel206.setBounds(580, 790, 80, 23);

        scrollInput.setViewportView(FormInput);

        internalFrame2.add(scrollInput, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Input Penilaian", internalFrame2);

        internalFrame3.setBorder(null);
        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame3.add(Scroll, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Asuhan :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-12-2023" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "04-12-2023" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(195, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(LCount);

        internalFrame3.add(panelGlass9, java.awt.BorderLayout.PAGE_END);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(470, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout(1, 1));

        ChkAccor.setBackground(new java.awt.Color(255, 250, 248));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelected(true);
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.WEST);

        FormMenu.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu.setBorder(null);
        FormMenu.setName("FormMenu"); // NOI18N
        FormMenu.setPreferredSize(new java.awt.Dimension(115, 43));
        FormMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel34.setText("Pasien :");
        jLabel34.setName("jLabel34"); // NOI18N
        jLabel34.setPreferredSize(new java.awt.Dimension(55, 23));
        FormMenu.add(jLabel34);

        TNoRM1.setEditable(false);
        TNoRM1.setHighlighter(null);
        TNoRM1.setName("TNoRM1"); // NOI18N
        TNoRM1.setPreferredSize(new java.awt.Dimension(100, 23));
        FormMenu.add(TNoRM1);

        TPasien1.setEditable(false);
        TPasien1.setBackground(new java.awt.Color(245, 250, 240));
        TPasien1.setHighlighter(null);
        TPasien1.setName("TPasien1"); // NOI18N
        TPasien1.setPreferredSize(new java.awt.Dimension(250, 23));
        FormMenu.add(TPasien1);

        BtnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item (copy).png"))); // NOI18N
        BtnPrint1.setMnemonic('T');
        BtnPrint1.setToolTipText("Alt+T");
        BtnPrint1.setName("BtnPrint1"); // NOI18N
        BtnPrint1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrint1ActionPerformed(evt);
            }
        });
        FormMenu.add(BtnPrint1);

        PanelAccor.add(FormMenu, java.awt.BorderLayout.NORTH);

        FormMasalahRencana.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        FormMasalahRencana.setName("FormMasalahRencana"); // NOI18N
        FormMasalahRencana.setLayout(new java.awt.GridLayout(3, 0, 1, 1));

        Scroll7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll7.setName("Scroll7"); // NOI18N
        Scroll7.setOpaque(true);

        tbMasalahDetail.setName("tbMasalahDetail"); // NOI18N
        Scroll7.setViewportView(tbMasalahDetail);

        FormMasalahRencana.add(Scroll7);

        Scroll9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)));
        Scroll9.setName("Scroll9"); // NOI18N
        Scroll9.setOpaque(true);

        tbRencanaDetail.setName("tbRencanaDetail"); // NOI18N
        Scroll9.setViewportView(tbRencanaDetail);

        FormMasalahRencana.add(Scroll9);

        scrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 254)), "Rencana Keperawatan Lainnya :", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        scrollPane6.setName("scrollPane6"); // NOI18N

        DetailRencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        DetailRencana.setColumns(20);
        DetailRencana.setRows(5);
        DetailRencana.setName("DetailRencana"); // NOI18N
        DetailRencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetailRencanaKeyPressed(evt);
            }
        });
        scrollPane6.setViewportView(DetailRencana);

        FormMasalahRencana.add(scrollPane6);

        PanelAccor.add(FormMasalahRencana, java.awt.BorderLayout.CENTER);

        internalFrame3.add(PanelAccor, java.awt.BorderLayout.EAST);

        TabRawat.addTab("Data Penilaian", internalFrame3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(BtnPetugas,"Pengkaji 1");
        }else{
            if(Sequel.menyimpantf("asesmen_awal_keperawatan_neonatus_nicu","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Rawat",183,new String[]{
//                    TNoRw.getText(),
//                    Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
//                    Anamnesis.getSelectedItem().toString(),
//                    KetAnamnesis.getText(), 
//                    AsalPasien.getSelectedItem().toString(),
//                    KeluhanUtama.getText(),
//                    DiagnosisMasuk.getText(),
//                    RiwayatKeluhanSaatIni.getText(),
//                    RiwayatPenggunaanObat.getText(),
//                    RiwayatKehamilanIbu.getText(),
//                    DitolongOlehLainLain.getText(),
//                    LahirDi.getSelectedItem().toString(),
//                    ProsesPersalinan.getSelectedItem().toString(), 
//                    RiwayatKesehatanSebelumnya.getSelectedItem().toString(),
//                    OpnameRS.getText(),RiwayatAlergi.getSelectedItem().toString(),SelamaHariOpname.getText(),Makanan.getSelectedItem().toString(),Imunisasi.getSelectedItem().toString(),UsiaGestasi.getText(), 
//                    Umur.getText(),LD.getText(),Nadi.getText(),BBLGizi.getText(),BBS.getText(),PBTB.getText(),LLA.getText(),LK.getText(),BentukDada.getSelectedItem().toString(),SaturasiO2.getText(),PernafasanSpontan.getSelectedItem().toString(), 
//                    Frekuensi.getText(), 
//                    AlatBantuNafas.getSelectedItem().toString(),Sesak.getSelectedItem().toString(),Ekstremitas.getSelectedItem().toString(),BunyiJantung.getSelectedItem().toString(),
//                    RespirasiJenisPernafasan.getSelectedItem().toString(),WarnaLendir.getText(),KeadaanSaatIni.getSelectedItem().toString(),Pendarahan.getSelectedItem().toString(),FemoralisKanan.getSelectedItem().toString(),Kapiler.getText(),
//                    BeratBadan.getSelectedItem().toString(),FemoralisKiri.getSelectedItem().toString(),HasilLab.getSelectedItem().toString(), 
//                    Intavena.getSelectedItem().toString(),HariIntavena.getText(),KeadaanSekarang.getSelectedItem().toString(),CaraMinum.getSelectedItem().toString(),Abdomen.getSelectedItem().toString(),Muntah.getText(), 
//                    Lidah.getSelectedItem().toString(),HasilLaboratorium.getSelectedItem().toString(),WarnaKulit.getSelectedItem().toString(),Residu.getText(),TurgorKulit.getSelectedItem().toString(),UbunUbun.getSelectedItem().toString(), 
//                    Lendir.getSelectedItem().toString(),KetTangisan.getSelectedItem().toString(), 
//                    JenisPupil.getSelectedItem().toString(),Kepala.getSelectedItem().toString(),Kesadaran.getSelectedItem().toString(),KeteranganHasilLab.getText(),Tangisan.getSelectedItem().toString(), 
//                    ProduksiUrine.getText(),PupilKiri.getText(),PupilKanan.getText(),LingkarKepala.getText(),BAK.getText(),KetNeurosensori.getText(),ReflexTampak.getSelectedItem().toString(),Gerakan.getSelectedItem().toString(),Kejang.getSelectedItem().toString(), 
//                    KeadaanReproduksi.getSelectedItem().toString(),Vagina.getSelectedItem().toString(),AlatBantu.getSelectedItem().toString(),Menstruasi.getSelectedItem().toString(), 
//                    Preputium.getSelectedItem().toString(),BAB.getSelectedItem().toString(),WarnaBAB.getText(),KondisiBAB.getSelectedItem().toString(),Feses.getSelectedItem().toString(),Suhu.getText(),Hipospadia.getSelectedItem().toString(),
//                    ccBAB.getText(),Anus.getSelectedItem().toString(),FrekuensiBAB.getText(),Berkunjung.getSelectedItem().toString(),Mekonium.getText(),Kongenital.getSelectedItem().toString(),
//                    KondisiKebersihanDiri.getSelectedItem().toString(),KukuBersihDiri.getSelectedItem().toString(),Integritas.getSelectedItem().toString(),Dehidrasi.getText(),TaliPusat.getSelectedItem().toString(),WarnaKulitBersihDiri.getSelectedItem().toString(),DerajatSuhu.getText(),
//                    SuhuBersihDiri.getSelectedItem().toString(),KeteranganTidur.getText(),KepalaBersihDiri.getSelectedItem().toString(),MataBersihDiri.getSelectedItem().toString(),Umbilical.getSelectedItem().toString(),AbdomenKebersihanDIri.getSelectedItem().toString(), 
//                    PolaTidur.getSelectedItem().toString(),Lokasi.getText(),KontakMata.getSelectedItem().toString(),StatusGestasi.getSelectedItem().toString(),BanyakTidur.getSelectedItem().toString(),StatusAnak.getSelectedItem().toString(), 
//                    SkalaResiko1.getSelectedItem().toString(),NilaiResiko1.getText(),SkalaResiko2.getSelectedItem().toString(),NilaiResiko2.getText(),SkalaResiko3.getSelectedItem().toString(),NilaiResiko3.getText(),SkalaResiko4.getSelectedItem().toString(),NilaiResiko4.getText(), 
//                    SkalaResiko5.getSelectedItem().toString(),NilaiResiko5.getText(),SkalaResiko6.getSelectedItem().toString(),NilaiResiko6.getText(),NilaiResikoTotal.getText(),SkalaSydney1.getSelectedItem().toString(),NilaiSydney1.getText(),SkalaSydney2.getSelectedItem().toString(),NilaiSydney2.getText(),
//                    SkalaSydney3.getSelectedItem().toString(),NilaiSydney3.getText(),SkalaSydney4.getSelectedItem().toString(),NilaiSydney4.getText(),SkalaSydney5.getSelectedItem().toString(),NilaiSydney5.getText(), 
//                    
//                    NilaiSydneyTotal.getText(),
//                    Rencana.getText(),KdPetugas.getText()
                
                    TNoRw.getText(),
                    Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
                    Anamnesis.getSelectedItem().toString(),
                    KetAnamnesis.getText(), 
                    AsalPasien.getSelectedItem().toString(),
                    KeluhanUtama.getText(),
                    DiagnosisMasuk.getText(),
                    RiwayatKeluhanSaatIni.getText(),
                    RiwayatPenggunaanObat.getText(),
                    RiwayatKehamilanIbu.getText(),
                    LahirDi.getSelectedItem().toString(),
                    ProsesPersalinan.getSelectedItem().toString(),
                    DitolongOleh.getSelectedItem().toString(),
                    DitolongOlehLainLain.getText(),
                    UsiaGestasi.getText(),
                    Umur.getText(),
                    BBL.getText(),
                    PBL.getText(),
                    RiwayatKesehatanSebelumnya.getSelectedItem().toString(),
                    OpnameRS.getText(),
                    SelamaHariOpname.getText(),
                    RiwayatPembedahan.getSelectedItem().toString(),
                    OperasiHari.getText(),
                    RiwayatAlergi.getSelectedItem().toString(),
                    SebutAlergi.getText(),
                    Makanan.getSelectedItem().toString(),
                    Imunisasi.getSelectedItem().toString(),
                    RisikoGizi.getSelectedItem().toString(), 
                    BBLGizi.getText(),
                    BBS.getText(),
                    PBTB.getText(),
                    LLA.getText(),
                    LK.getText(),
                    LD.getText(),
                    LP.getText(),
                    BentukDada.getSelectedItem().toString(),
                    PernafasanSpontan.getSelectedItem().toString(),
                    Frekuensi.getText(),
                    SaturasiO2.getText(),
                    Sesak.getSelectedItem().toString(),
                    Retraksi.getSelectedItem().toString(),
                    Sianosis.getSelectedItem().toString(),
                    WarnaLendir.getText(),
                    ccLendir.getText(),
                    Wheezing.getSelectedItem().toString(),
                    Ronchi.getSelectedItem().toString(),
                    WSD.getSelectedItem().toString(),
                    ccWSD.getText(),
                    Suction.getSelectedItem().toString(),
                    ccSuction.getText(),
                    AlatBantuNafas.getSelectedItem().toString(),
                    RespirasiJenisPernafasan.getSelectedItem().toString(),
                    HasilRespirasi.getText(),
                    KeteranganRespirasi.getText(),
                    BunyiJantung.getSelectedItem().toString(),
                    Pendarahan.getSelectedItem().toString(),
                    JenisPendarahan.getText(),
                    ccPendarahan.getText(),
                    FemoralisKanan.getSelectedItem().toString(),
                    FemoralisKiri.getSelectedItem().toString(),
                    KeadaanSaatIni.getSelectedItem().toString(),
                    Nadi.getText(), 
                    Ekstremitas.getSelectedItem().toString(),
                    Kapiler.getText(),
                    HasilLab.getSelectedItem().toString(),
                    Intavena.getSelectedItem().toString(),
                    HariIntavena.getText(),
                    BeratBadan.getSelectedItem().toString(),
                    persenBeratBadan.getText(),
                    Lendir.getSelectedItem().toString(),
                    CaraMinum.getSelectedItem().toString(),
                    ReflexIsap.getSelectedItem().toString(),
                    WarnaKulit.getSelectedItem().toString(),
                    Lidah.getSelectedItem().toString(),
                    ReflexTelan.getSelectedItem().toString(),
                    Abdomen.getSelectedItem().toString(),
                    KeadaanSekarang.getSelectedItem().toString(),
                    TurgorKulit.getSelectedItem().toString(),
                    UbunUbun.getSelectedItem().toString(),
                    Muntah.getText(), 
                    HasilLaboratorium.getSelectedItem().toString(),
                    Kremer.getText(),
                    Residu.getText(),
                    Kesadaran.getSelectedItem().toString(),
                    Kepala.getSelectedItem().toString(),
                    ReflexTampak.getSelectedItem().toString(),
                    Tangisan.getSelectedItem().toString(),
                    KetTangisan.getSelectedItem().toString(),
                    LingkarKepala.getText(),
                    Kejang.getSelectedItem().toString(),
                    PupilKiri.getText(),
                    PupilKanan.getText(),
                    JenisPupil.getSelectedItem().toString(),
                    Gerakan.getSelectedItem().toString(),
                    KetNeurosensori.getText(),
                    BAK.getText(),
                    ProduksiUrine.getText(),
                    KeadaanReproduksi.getSelectedItem().toString(),
                    AlatBantu.getSelectedItem().toString(),
                    Vagina.getSelectedItem().toString(),
                    Menstruasi.getSelectedItem().toString(),
                    Kateter.getSelectedItem().toString(),
                    Prominen.getSelectedItem().toString(),
                    Ambigus.getSelectedItem().toString(),
                    Preputium.getSelectedItem().toString(),
                    Hipospadia.getSelectedItem().toString(),
                    BAB.getSelectedItem().toString(),
                    ccBAB.getText(),
                    Feses.getSelectedItem().toString(),
                    Anus.getSelectedItem().toString(),
                    WarnaBAB.getText(),
                    KondisiBAB.getSelectedItem().toString(),
                    FrekuensiBAB.getText(),
                    Mekonium.getText(),
                    Suhu.getText(),
                    Dehidrasi.getText(),
                    Integritas.getSelectedItem().toString(),
                    KondisiKebersihanDiri.getSelectedItem().toString(),
                    WarnaKulitBersihDiri.getSelectedItem().toString(),
                    SuhuBersihDiri.getSelectedItem().toString(),
                    DerajatSuhu.getText(),
                    KepalaBersihDiri.getSelectedItem().toString(),
                    Umbilical.getSelectedItem().toString(),
                    KukuBersihDiri.getSelectedItem().toString(),
                    MataBersihDiri.getSelectedItem().toString(),
                    TaliPusat.getSelectedItem().toString(),
                    AbdomenKebersihanDIri.getSelectedItem().toString(),
                    JenisBersihDiri.getText(),
                    LokasiBersihDiri.getText(),
                    BanyakTidur.getSelectedItem().toString(),
                    KeteranganTidur.getText(),
                    PolaTidur.getSelectedItem().toString(),
                    StatusAnak.getSelectedItem().toString(),
                    Kongenital.getSelectedItem().toString(),
                    Lokasi.getText(),
                    SebutKongenital.getText(),
                    StatusGestasi.getSelectedItem().toString(),
                    Berkunjung.getSelectedItem().toString(),
                    KontakMata.getSelectedItem().toString(),
                    Menyentuh.getSelectedItem().toString(),
                    Berbicara.getSelectedItem().toString(),
                    Menggendong.getSelectedItem().toString(),
                    YangMerawat.getSelectedItem().toString(),
                    LainLainRawat.getText(),
                    PerawatanDiri.getSelectedItem().toString(),
                    LainLainDischargePlaning.getText(),             
                    SkalaResiko1.getSelectedItem().toString(),
                    NilaiResiko1.getText(),
                    SkalaResiko2.getSelectedItem().toString(),
                    NilaiResiko2.getText(),
                    SkalaResiko3.getSelectedItem().toString(),
                    NilaiResiko3.getText(),
                    SkalaResiko4.getSelectedItem().toString(),
                    NilaiResiko4.getText(),
                    SkalaResiko5.getSelectedItem().toString(),
                    NilaiResiko5.getText(),
                    SkalaResiko6.getSelectedItem().toString(),
                    NilaiResiko6.getText(),
                    NilaiResikoTotal.getText(),
                    SkalaSydney1.getSelectedItem().toString(),
                    NilaiSydney1.getText(),
                    SkalaSydney2.getSelectedItem().toString(),
                    NilaiSydney2.getText(),
                    SkalaSydney3.getSelectedItem().toString(),
                    NilaiSydney3.getText(),
                    SkalaSydney4.getSelectedItem().toString(),
                    NilaiSydney4.getText(),
                    SkalaSydney5.getSelectedItem().toString(),
                    NilaiSydney5.getText(), 
                    NilaiSydneyTotal.getText(),
                    Rencana.getText(),
                    KdPetugas.getText(),
                    reflexpalmargrasp.getSelectedItem().toString(),
                    ReflexBabinski.getSelectedItem().toString(),
                    ReflexPlantarGrasp.getSelectedItem().toString(),
                    ReflexMoro.getSelectedItem().toString(),
                    PerawatanDiri.getSelectedItem().toString(),
                    PemantauanPemberianObat.getSelectedItem().toString(),
                    PemberianASI.getSelectedItem().toString(),
                    PerawatanMetodeKangguru.getSelectedItem().toString(),
                    PerawatanLuka.getSelectedItem().toString(),
                    PendampinganTenagaKhususDiRumah.getSelectedItem().toString(),
                    Kremer.getText()
                })==true){
                    for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                        if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_masalah","?,?",2,new String[]{TNoRw.getText(),tbMasalahKeperawatan.getValueAt(i,1).toString()});
                        }
                    }
                    for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                        if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_rencana","?,?",2,new String[]{TNoRw.getText(),tbRencanaKeperawatan.getValueAt(i,1).toString()});
                        }
                    }
                    emptTeks();
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,Rencana,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }            
            
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRM.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"Nama Pasien");
        }else if(KdPetugas.getText().trim().equals("")||NmPetugas.getText().trim().equals("")){
            Valid.textKosong(BtnPetugas,"Pengkaji 1");

        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KdPetugas.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh petugas yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }  
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
//        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        if(tabMode.getRowCount()==0){
//            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
//            BtnBatal.requestFocus();
//        }else if(tabMode.getRowCount()!=0){
//            try{
//                File g = new File("file2.css");            
//                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
//                bg.write(
//                        ".isi td{border-right: 1px solid #e2e7dd;font: 11px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                        ".isi2 td{font: 11px tahoma;height:12px;background: #ffffff;color:#323232;}"+                    
//                        ".isi3 td{border-right: 1px solid #e2e7dd;font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
//                        ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
//                );
//                bg.close();
//
//                File f;            
//                BufferedWriter bw; 
//                
//                ps=koneksi.prepareStatement(
//                    "select asesmen_awal_keperawatan_neonatus_nicu.no_rawat,asesmen_awal_keperawatan_neonatus_nicu.tanggal,asesmen_awal_keperawatan_neonatus_nicu.informasi,asesmen_awal_keperawatan_neonatus_nicu.ket_informasi,asesmen_awal_keperawatan_neonatus_nicu.tiba_diruang_rawat,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.kasus_trauma,asesmen_awal_keperawatan_neonatus_nicu.cara_masuk,asesmen_awal_keperawatan_neonatus_nicu.rps,asesmen_awal_keperawatan_neonatus_nicu.rpd,asesmen_awal_keperawatan_neonatus_nicu.rpk,asesmen_awal_keperawatan_neonatus_nicu.rpo,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_pembedahan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_dirawat_dirs,asesmen_awal_keperawatan_neonatus_nicu.alat_bantu_dipakai,asesmen_awal_keperawatan_neonatus_nicu.riwayat_kehamilan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_kehamilan_perkiraan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_tranfusi,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alergi,asesmen_awal_keperawatan_neonatus_nicu.riwayat_merokok,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_merokok_jumlah,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alkohol,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alkohol_jumlah,asesmen_awal_keperawatan_neonatus_nicu.riwayat_narkoba,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_olahraga,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_mental,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_keadaan_umum,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gcs,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_td,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_nadi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_rr,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_suhu,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_spo2,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_bb,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_tb,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kepala,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kepala_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_wajah,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_wajah_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_leher,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kejang,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kejang_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_sensorik,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_denyut_nadi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_sirkulasi,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_sirkulasi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_pulsasi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_pola_nafas,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_retraksi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_suara_nafas,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_volume_pernafasan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_jenis_pernafasan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_jenis_pernafasan_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_irama_nafas,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_batuk,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_mulut,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_mulut_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_gigi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_gigi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_lidah,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_lidah_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_tenggorokan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_tenggorokan_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_abdomen,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_abdomen_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_peistatik_usus,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_anus,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pengelihatan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pengelihatan_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_alat_bantu_penglihatan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pendengaran,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_bicara,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_bicara_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_sensorik,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_motorik,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_kekuatan_otot,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_warnakulit,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_turgor,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_kulit,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_dekubitas,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_pergerakan_sendi,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_kekauatan_otot,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_nyeri_sendi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_nyeri_sendi_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_oedema,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_oedema_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_fraktur,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_fraktur_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_frekuensi_jumlah,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_frekuensi_durasi,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_konsistensi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_warna,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_frekuensi_jumlah,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_frekuensi_durasi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_warna,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_lainlain,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_makanminum,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_mandi,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_eliminasi,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_berpakaian,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_berpindah,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_frekuesi_makan,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_jenis_makanan,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_porsi_makan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pola_tidur_lama_tidur,asesmen_awal_keperawatan_neonatus_nicu.pola_tidur_gangguan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_kemampuan_sehari,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_aktifitas,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_berjalan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_berjalan_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ambulasi,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_atas,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_atas_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_bawah,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_bawah_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_menggenggam,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_menggenggam_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_koordinasi,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_koordinasi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_kesimpulan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_kondisi_psiko,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_gangguan_jiwa,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_perilaku,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_perilaku_keterangan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_hubungan_keluarga,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_tinggal,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_tinggal_keterangan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_nilai_kepercayaan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_nilai_kepercayaan_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_pendidikan_pj,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_edukasi_diberikan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_edukasi_diberikan_keterangan,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_penyebab,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_penyebab,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_kualitas,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_kualitas,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_lokasi,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_menyebar,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_skala,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_waktu,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_hilang,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_hilang,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_diberitahukan_dokter,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_jam_diberitahukan_dokter,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala1,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai1,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala3,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala4,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai4,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala6,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_totalnilai,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala1,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai1,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai2,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala4,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai4,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai5,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala7,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai7,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala8,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai8,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala9,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai9,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala10,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai10,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala11,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai11,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_totalnilai,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi1,asesmen_awal_keperawatan_neonatus_nicu.nilai_gizi1,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi2,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.nilai_gizi2,asesmen_awal_keperawatan_neonatus_nicu.nilai_total_gizi,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_diagnosa_khusus,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_ket_diagnosa_khusus,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_diketahui_dietisen,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_jam_diketahui_dietisen,asesmen_awal_keperawatan_neonatus_nicu.rencana,asesmen_awal_keperawatan_neonatus_nicu.nip1,"+
//                    "asesmen_awal_keperawatan_neonatus_nicu.nip2,asesmen_awal_keperawatan_neonatus_nicu.kd_dokter,pasien.tgl_lahir,pasien.jk,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter,"+
//                    "reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.agama,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,bahasa_pasien.nama_bahasa "+
//                    "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                    "inner join asesmen_awal_keperawatan_neonatus_nicu on reg_periksa.no_rawat=asesmen_awal_keperawatan_neonatus_nicu.no_rawat "+
//                    "inner join petugas as pengkaji1 on asesmen_awal_keperawatan_neonatus_nicu.nip1=pengkaji1.nip "+
//                    "inner join petugas as pengkaji2 on asesmen_awal_keperawatan_neonatus_nicu.nip2=pengkaji2.nip "+
//                    "inner join dokter on asesmen_awal_keperawatan_neonatus_nicu.kd_dokter=dokter.kd_dokter "+
//                    "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
//                    "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where "+
//                    "asesmen_awal_keperawatan_neonatus_nicu.tanggal between ? and ? "+
//                    (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or asesmen_awal_keperawatan_neonatus_nicu.nip1 like ? or "+
//                    "pengkaji1.nama like ? or asesmen_awal_keperawatan_neonatus_nicu.kd_dokter like ? or dokter.nm_dokter like ?)")+
//                    " order by asesmen_awal_keperawatan_neonatus_nicu.tanggal");
//
//                try {
//                    ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
//                    ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
//                    if(!TCari.getText().equals("")){
//                        ps.setString(3,"%"+TCari.getText()+"%");
//                        ps.setString(4,"%"+TCari.getText()+"%");
//                        ps.setString(5,"%"+TCari.getText()+"%");
//                        ps.setString(6,"%"+TCari.getText()+"%");
//                        ps.setString(7,"%"+TCari.getText()+"%");
//                        ps.setString(8,"%"+TCari.getText()+"%");
//                        ps.setString(9,"%"+TCari.getText()+"%");
//                    }   
//                    rs=ps.executeQuery();
//                    pilihan = (String)JOptionPane.showInputDialog(null,"Silahkan pilih laporan..!","Pilihan Cetak",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Laporan 1 (HTML)","Laporan 2 (WPS)","Laporan 3 (CSV)"},"Laporan 1 (HTML)");
//                    switch (pilihan) {
//                        case "Laporan 1 (HTML)":
//                                htmlContent = new StringBuilder();
//                                htmlContent.append(                             
//                                    "<tr class='isi'>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='105px'>No.Rawat</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>No.RM</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pasien</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tgl.Lahir</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='25px'>J.K.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kode DPJP</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama DPJP</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='117px'>Tgl.Asuhan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='78px'>Macam Kasus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anamnesis</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='110px'>Tiba Di Ruang Rawat</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Cara Masuk</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='220px'>Riwayat Penyakit Saat Ini</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Dahulu</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Keluarga</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penggunaan Obat</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Pembedahan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Dirawat Di RS</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Yang Dipakai</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='210px'>Dalam Keadaan Hamil/Sedang Menyusui</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Transfusi Darah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Alergi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='48px'>Merokok</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Batang/Hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>Alkohol</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Gelas/Hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='61px'>Obat Tidur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Olah Raga</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kesadaran Mental</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keadaan Umum</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='64px'>GCS(E,V,M)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='60px'>TD(mmHg)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='74px'>Nadi(x/menit)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='67px'>RR(x/menit)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>Suhu(°C)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>SpO2(%)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>BB(Kg)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>TB(cm)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kepala</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Wajah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='106px'>Leher</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kejang</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='50px'>Pulsasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sirkulasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='72px'>Denyut Nadi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='54px'>Retraksi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='63px'>Pola Nafas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='69px'>Suara Nafas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='97px'>Batuk & Sekresi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='75px'>Volume</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Pernafasaan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Irama</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mulut</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lidah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gigi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tenggorokan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Abdomen</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Peistatik Usus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penglihatan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Penglihatan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Motorik</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pendengaran</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Bicara</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Otot</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kulit</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna Kulit</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Turgor</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Resiko Decubitas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Oedema</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pergerakan Sendi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kekuatan Otot</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Fraktur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Sendi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAB</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Konsistensi BAB</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAB</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAK</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAK</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lain-lain BAK</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mandi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Makan/Minum</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpakaian</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Eliminasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpindah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Porsi Makan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi Makan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Makanan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lama Tidur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gangguan Tidur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Aktifitas Sehari-hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Berjalan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Aktifitas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Alat Ambulasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Ekstremitas Atas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Ekstremitas Bawah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Kemampuan Menggenggam</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Kemampuan Koordinasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Kesimpulan Gangguan Fungsi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Kondisi Psikologis</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Adakah Perilaku</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Gangguan Jiwa di Masa Lalu</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Hubungan Pasien</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Agama</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Tinggal Dengan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Pekerjaan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Pembayaran</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Nilai-nilai Kepercayaan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>j. Bahasa Sehari-hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>k. Pendidikan Pasien</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>l. Pendidikan P.J.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>m. Edukasi Diberikan Kepada</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penyebab Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kualitas Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lokasi Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Menyebar</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Waktu / Durasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Hilang Bila</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Diberitahukan Pada Dokter</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.M.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 7</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 7</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 8</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 8</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 9</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 9</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 10</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 10</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 11</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 11</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.S.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>2. Apakah asupan makan berkurang karena tidak nafsu makan ?</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Total Skor</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pasien dengan diagnosis khusus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keterangan Diagnosa Khusus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sudah dibaca dan diketahui oleh Dietisen</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jam Dibaca Dietisen</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Rencana Keperawatan Lainnya</td>"+
//                                    "</tr>"
//                                );
//                                while(rs.next()){
//                                    htmlContent.append(
//                                        "<tr class='isi'>"+
//                                            "<td valign='top'>"+rs.getString("no_rawat")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nm_pasien")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("jk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nip1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkaji1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nip2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkaji2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("kd_dokter")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nm_dokter")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("tanggal")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("kasus_trauma")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("informasi")+", "+rs.getString("ket_informasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("tiba_diruang_rawat")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("cara_masuk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rps")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rpd")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rpk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rpo")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_pembedahan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_dirawat_dirs")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("alat_bantu_dipakai")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_kehamilan")+", "+rs.getString("riwayat_kehamilan_perkiraan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_tranfusi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_alergi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_merokok")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_merokok_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_alkohol")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_alkohol_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_narkoba")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_olahraga")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_mental")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_keadaan_umum")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gcs")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_td")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_nadi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_rr")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_suhu")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_spo2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_bb")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_tb")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kepala")+", "+rs.getString("pemeriksaan_susunan_kepala_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_wajah")+", "+rs.getString("pemeriksaan_susunan_wajah_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_leher")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kejang")+", "+rs.getString("pemeriksaan_susunan_kejang_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_sensorik")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_pulsasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi")+", "+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_denyut_nadi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_retraksi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_pola_nafas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_suara_nafas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_batuk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_volume_pernafasan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_jenis_pernafasan")+", "+rs.getString("pemeriksaan_respirasi_jenis_pernafasan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_irama_nafas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_mulut")+", "+rs.getString("pemeriksaan_gastrointestinal_mulut_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_lidah")+", "+rs.getString("pemeriksaan_gastrointestinal_lidah_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_gigi")+", "+rs.getString("pemeriksaan_gastrointestinal_gigi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_tenggorokan")+", "+rs.getString("pemeriksaan_gastrointestinal_tenggorokan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_abdomen")+", "+rs.getString("pemeriksaan_gastrointestinal_abdomen_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_peistatik_usus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_anus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_sensorik")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pengelihatan")+", "+rs.getString("pemeriksaan_neurologi_pengelihatan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_alat_bantu_penglihatan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_motorik")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pendengaran")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_bicara")+", "+rs.getString("pemeriksaan_neurologi_bicara_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_kekuatan_otot")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_kulit")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_warnakulit")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_turgor")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_dekubitas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_oedema")+", "+rs.getString("pemeriksaan_muskuloskletal_oedema_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_pergerakan_sendi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_kekauatan_otot")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_fraktur")+", "+rs.getString("pemeriksaan_muskuloskletal_fraktur_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi")+", "+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_durasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_konsistensi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_warna")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_durasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_warna")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_lainlain")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_mandi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_makanminum")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpakaian")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_eliminasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpindah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_nutrisi_porsi_makan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_nutrisi_frekuesi_makan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_nutrisi_jenis_makanan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_tidur_lama_tidur")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_tidur_gangguan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kemampuan_sehari")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_berjalan")+", "+rs.getString("pengkajian_fungsi_berjalan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_aktifitas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ambulasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_atas")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_atas_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_bawah")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_bawah_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_menggenggam")+", "+rs.getString("pengkajian_fungsi_menggenggam_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_koordinasi")+", "+rs.getString("pengkajian_fungsi_koordinasi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kesimpulan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_kondisi_psiko")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_perilaku")+", "+rs.getString("riwayat_psiko_perilaku_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_gangguan_jiwa")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_hubungan_keluarga")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("agama")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_tinggal")+", "+rs.getString("riwayat_psiko_tinggal_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pekerjaan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("png_jawab")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_nilai_kepercayaan")+", "+rs.getString("riwayat_psiko_nilai_kepercayaan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nama_bahasa")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pnd")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_pendidikan_pj")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_edukasi_diberikan")+", "+rs.getString("riwayat_psiko_edukasi_diberikan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_penyebab")+", "+rs.getString("penilaian_nyeri_ket_penyebab")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_kualitas")+", "+rs.getString("penilaian_nyeri_ket_kualitas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_lokasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_menyebar")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_skala")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_waktu")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_hilang")+", "+rs.getString("penilaian_nyeri_ket_hilang")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_diberitahukan_dokter")+", "+rs.getString("penilaian_nyeri_jam_diberitahukan_dokter")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_totalnilai")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala7")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai7")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala8")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai8")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala9")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai9")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala10")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai10")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala11")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai11")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_totalnilai")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nilai_gizi1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nilai_gizi2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nilai_total_gizi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_diagnosa_khusus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_ket_diagnosa_khusus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_diketahui_dietisen")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_jam_diketahui_dietisen")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rencana")+"</td>"+
//                                        "</tr>"
//                                    );
//                                }
//                                f = new File("RMPenilaianAwalKeperawatanRanap.html");            
//                                bw = new BufferedWriter(new FileWriter(f));            
//                                bw.write("<html>"+
//                                            "<head><link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" /></head>"+
//                                            "<body>"+
//                                                "<table width='18500px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
//                                                    htmlContent.toString()+
//                                                "</table>"+
//                                            "</body>"+                   
//                                         "</html>"
//                                );
//
//                                bw.close();                         
//                                Desktop.getDesktop().browse(f.toURI());
//                            break;
//                        case "Laporan 2 (WPS)":
//                                htmlContent = new StringBuilder();
//                                htmlContent.append(                             
//                                    "<tr class='isi'>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='105px'>No.Rawat</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>No.RM</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pasien</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tgl.Lahir</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='25px'>J.K.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>NIP Pengkaji 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama Pengkaji 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kode DPJP</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nama DPJP</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='117px'>Tgl.Asuhan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='78px'>Macam Kasus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anamnesis</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='110px'>Tiba Di Ruang Rawat</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Cara Masuk</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='220px'>Riwayat Penyakit Saat Ini</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Dahulu</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penyakit Keluarga</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Penggunaan Obat</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Pembedahan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Dirawat Di RS</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Yang Dipakai</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='210px'>Dalam Keadaan Hamil/Sedang Menyusui</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Transfusi Darah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Riwayat Alergi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='48px'>Merokok</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Batang/Hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>Alkohol</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Gelas/Hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='61px'>Obat Tidur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='59px'>Olah Raga</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kesadaran Mental</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keadaan Umum</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='64px'>GCS(E,V,M)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='60px'>TD(mmHg)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='74px'>Nadi(x/menit)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='67px'>RR(x/menit)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>Suhu(°C)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='52px'>SpO2(%)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>BB(Kg)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='44px'>TB(cm)</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kepala</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Wajah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='106px'>Leher</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kejang</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='50px'>Pulsasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sirkulasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='72px'>Denyut Nadi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='54px'>Retraksi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='63px'>Pola Nafas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='69px'>Suara Nafas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='97px'>Batuk & Sekresi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center' width='75px'>Volume</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Pernafasaan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Irama</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mulut</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lidah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gigi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Tenggorokan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Abdomen</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Peistatik Usus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Anus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sensorik</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penglihatan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Alat Bantu Penglihatan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Motorik</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pendengaran</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Bicara</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Otot</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kulit</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna Kulit</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Turgor</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Resiko Decubitas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Oedema</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pergerakan Sendi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kekuatan Otot</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Fraktur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Sendi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAB</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Konsistensi BAB</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAB</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi BAK</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>x/</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Warna BAK</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lain-lain BAK</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Mandi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Makan/Minum</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpakaian</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Eliminasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Berpindah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Porsi Makan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Frekuensi Makan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jenis Makanan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lama Tidur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Gangguan Tidur</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Aktifitas Sehari-hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Berjalan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Aktifitas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Alat Ambulasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Ekstremitas Atas</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Ekstremitas Bawah</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Kemampuan Menggenggam</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Kemampuan Koordinasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Kesimpulan Gangguan Fungsi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>a. Kondisi Psikologis</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>b. Adakah Perilaku</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>c. Gangguan Jiwa di Masa Lalu</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>d. Hubungan Pasien</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>e. Agama</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>f. Tinggal Dengan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>g. Pekerjaan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>h. Pembayaran</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>i. Nilai-nilai Kepercayaan</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>j. Bahasa Sehari-hari</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>k. Pendidikan Pasien</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>l. Pendidikan P.J.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>m. Edukasi Diberikan Kepada</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Penyebab Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Kualitas Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Lokasi Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Menyebar</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Nyeri</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Waktu / Durasi</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Nyeri Hilang Bila</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Diberitahukan Pada Dokter</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Morse 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.M. 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.M.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 3</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 4</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 5</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 6</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 7</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 7</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 8</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 8</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 9</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 9</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 10</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 10</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skala Sydney 11</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>N.S. 11</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>T.S.</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 1</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>2. Apakah asupan makan berkurang karena tidak nafsu makan ?</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Skor 2</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Total Skor</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Pasien dengan diagnosis khusus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Keterangan Diagnosa Khusus</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Sudah dibaca dan diketahui oleh Dietisen</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Jam Dibaca Dietisen</td>"+
//                                        "<td valign='middle' bgcolor='#FFFAF8' align='center'>Rencana Keperawatan Lainnya</td>"+
//                                    "</tr>"
//                                );
//                                while(rs.next()){
//                                    htmlContent.append(
//                                        "<tr class='isi'>"+
//                                            "<td valign='top'>"+rs.getString("no_rawat")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nm_pasien")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("jk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nip1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkaji1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nip2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkaji2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("kd_dokter")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nm_dokter")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("tanggal")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("kasus_trauma")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("informasi")+", "+rs.getString("ket_informasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("tiba_diruang_rawat")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("cara_masuk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rps")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rpd")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rpk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rpo")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_pembedahan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_dirawat_dirs")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("alat_bantu_dipakai")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_kehamilan")+", "+rs.getString("riwayat_kehamilan_perkiraan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_tranfusi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_alergi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_merokok")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_merokok_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_alkohol")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_alkohol_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_narkoba")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_olahraga")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_mental")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_keadaan_umum")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gcs")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_td")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_nadi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_rr")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_suhu")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_spo2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_bb")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_tb")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kepala")+", "+rs.getString("pemeriksaan_susunan_kepala_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_wajah")+", "+rs.getString("pemeriksaan_susunan_wajah_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_leher")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_kejang")+", "+rs.getString("pemeriksaan_susunan_kejang_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_susunan_sensorik")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_pulsasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi")+", "+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_kardiovaskuler_denyut_nadi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_retraksi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_pola_nafas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_suara_nafas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_batuk")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_volume_pernafasan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_jenis_pernafasan")+", "+rs.getString("pemeriksaan_respirasi_jenis_pernafasan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_respirasi_irama_nafas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_mulut")+", "+rs.getString("pemeriksaan_gastrointestinal_mulut_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_lidah")+", "+rs.getString("pemeriksaan_gastrointestinal_lidah_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_gigi")+", "+rs.getString("pemeriksaan_gastrointestinal_gigi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_tenggorokan")+", "+rs.getString("pemeriksaan_gastrointestinal_tenggorokan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_abdomen")+", "+rs.getString("pemeriksaan_gastrointestinal_abdomen_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_peistatik_usus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_gastrointestinal_anus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_sensorik")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pengelihatan")+", "+rs.getString("pemeriksaan_neurologi_pengelihatan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_alat_bantu_penglihatan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_motorik")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_pendengaran")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_bicara")+", "+rs.getString("pemeriksaan_neurologi_bicara_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_neurologi_kekuatan_otot")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_kulit")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_warnakulit")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_turgor")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_integument_dekubitas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_oedema")+", "+rs.getString("pemeriksaan_muskuloskletal_oedema_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_pergerakan_sendi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_kekauatan_otot")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_fraktur")+", "+rs.getString("pemeriksaan_muskuloskletal_fraktur_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi")+", "+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_durasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_konsistensi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bab_warna")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_jumlah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_durasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_warna")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pemeriksaan_eliminasi_bak_lainlain")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_mandi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_makanminum")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpakaian")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_eliminasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_aktifitas_berpindah")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_nutrisi_porsi_makan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_nutrisi_frekuesi_makan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_nutrisi_jenis_makanan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_tidur_lama_tidur")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pola_tidur_gangguan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kemampuan_sehari")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_berjalan")+", "+rs.getString("pengkajian_fungsi_berjalan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_aktifitas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ambulasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_atas")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_atas_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_ekstrimitas_bawah")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_bawah_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_menggenggam")+", "+rs.getString("pengkajian_fungsi_menggenggam_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_koordinasi")+", "+rs.getString("pengkajian_fungsi_koordinasi_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pengkajian_fungsi_kesimpulan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_kondisi_psiko")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_perilaku")+", "+rs.getString("riwayat_psiko_perilaku_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_gangguan_jiwa")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_hubungan_keluarga")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("agama")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_tinggal")+", "+rs.getString("riwayat_psiko_tinggal_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pekerjaan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("png_jawab")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_nilai_kepercayaan")+", "+rs.getString("riwayat_psiko_nilai_kepercayaan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nama_bahasa")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("pnd")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_pendidikan_pj")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("riwayat_psiko_edukasi_diberikan")+", "+rs.getString("riwayat_psiko_edukasi_diberikan_keterangan")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_penyebab")+", "+rs.getString("penilaian_nyeri_ket_penyebab")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_kualitas")+", "+rs.getString("penilaian_nyeri_ket_kualitas")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_lokasi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_menyebar")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_skala")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_waktu")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_hilang")+", "+rs.getString("penilaian_nyeri_ket_hilang")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_nyeri_diberitahukan_dokter")+", "+rs.getString("penilaian_nyeri_jam_diberitahukan_dokter")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_skala6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_nilai6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhmorse_totalnilai")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai3")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai4")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai5")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai6")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala7")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai7")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala8")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai8")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala9")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai9")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala10")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai10")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_skala11")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_nilai11")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("penilaian_jatuhsydney_totalnilai")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nilai_gizi1")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nilai_gizi2")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("nilai_total_gizi")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_diagnosa_khusus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_ket_diagnosa_khusus")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_diketahui_dietisen")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("skrining_gizi_jam_diketahui_dietisen")+"</td>"+
//                                            "<td valign='top'>"+rs.getString("rencana")+"</td>"+
//                                        "</tr>"
//                                    );
//                                }
//                                f = new File("RMPenilaianAwalKeperawatanRanap.wps");            
//                                bw = new BufferedWriter(new FileWriter(f));            
//                                bw.write("<html>"+
//                                            "<head><link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" /></head>"+
//                                            "<body>"+
//                                                "<table width='18500px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
//                                                    htmlContent.toString()+
//                                                "</table>"+
//                                            "</body>"+                   
//                                         "</html>"
//                                );
//
//                                bw.close();                         
//                                Desktop.getDesktop().browse(f.toURI());
//                            break;
//                        case "Laporan 3 (CSV)":
//                                htmlContent = new StringBuilder();
//                                htmlContent.append(                             
//                                    "\"No.Rawat\";\"No.RM\";\"Nama Pasien\";\"Tgl.Lahir\";\"J.K.\";\"NIP Pengkaji 1\";\"Nama Pengkaji 1\";\"NIP Pengkaji 2\";\"Nama Pengkaji 2\";\"Kode DPJP\";\"Nama DPJP\";\"Tgl.Asuhan\";\"Macam Kasus\";\"Anamnesis\";\"Tiba Di Ruang Rawat\";\"Cara Masuk\";\"Riwayat Penyakit Saat Ini\";\"Riwayat Penyakit Dahulu\";\"Riwayat Penyakit Keluarga\";\"Riwayat Penggunaan Obat\";\"Riwayat Pembedahan\";\"Riwayat Dirawat Di RS\";\"Alat Bantu Yang Dipakai\";\"Dalam Keadaan Hamil/Sedang Menyusui\";\"Riwayat Transfusi Darah\";\"Riwayat Alergi\";\"Merokok\";\"Batang/Hari\";\"Alkohol\";\"Gelas/Hari\";\"Obat Tidur\";\"Olah Raga\";\"Kesadaran Mental\";\"Keadaan Umum\";\"GCS(E,V,M)\";\"TD(mmHg)\";\"Nadi(x/menit)\";\"RR(x/menit)\";\"Suhu(°C)\";\"SpO2(%)\";\"BB(Kg)\";\"TB(cm)\";\"Kepala\";\"Wajah\";\"Leher\";\"Kejang\";\"Sensorik\";\"Pulsasi\";\"Sirkulasi\";\"Denyut Nadi\";\"Retraksi\";\"Pola Nafas\";\"Suara Nafas\";\"Batuk & Sekresi\";\"Volume\";\"Jenis Pernafasaan\";\"Irama\";\"Mulut\";\"Lidah\";\"Gigi\";\"Tenggorokan\";\"Abdomen\";\"Peistatik Usus\";\"Anus\";\"Sensorik\";\"Penglihatan\";\"Alat Bantu Penglihatan\";\"Motorik\";\"Pendengaran\";\"Bicara\";\"Otot\";\"Kulit\";\"Warna Kulit\";\"Turgor\";\"Resiko Decubitas\";\"Oedema\";\"Pergerakan Sendi\";\"Kekuatan Otot\";\"Fraktur\";\"Nyeri Sendi\";\"Frekuensi BAB\";\"x/\";\"Konsistensi BAB\";\"Warna BAB\";\"Frekuensi BAK\";\"x/\";\"Warna BAK\";\"Lain-lain BAK\";\"Mandi\";\"Makan/Minum\";\"Berpakaian\";\"Eliminasi\";\"Berpindah\";\"Porsi Makan\";\"Frekuensi Makan\";\"Jenis Makanan\";\"Lama Tidur\";\"Gangguan Tidur\";\"a. Aktifitas Sehari-hari\";\"b. Berjalan\";\"c. Aktifitas\";\"d. Alat Ambulasi\";\"e. Ekstremitas Atas\";\"f. Ekstremitas Bawah\";\"g. Kemampuan Menggenggam\";\"h. Kemampuan Koordinasi\";\"i. Kesimpulan Gangguan Fungsi\";\"a. Kondisi Psikologis\";\"b. Adakah Perilaku\";\"c. Gangguan Jiwa di Masa Lalu\";\"d. Hubungan Pasien\";\"e. Agama\";\"f. Tinggal Dengan\";\"g. Pekerjaan\";\"h. Pembayaran\";\"i. Nilai-nilai Kepercayaan\";\"j. Bahasa Sehari-hari\";\"k. Pendidikan Pasien\";\"l. Pendidikan P.J.\";\"m. Edukasi Diberikan Kepada\";\"Nyeri\";\"Penyebab Nyeri\";\"Kualitas Nyeri\";\"Lokasi Nyeri\";\"Nyeri Menyebar\";\"Skala Nyeri\";\"Waktu / Durasi\";\"Nyeri Hilang Bila\";\"Diberitahukan Pada Dokter\";\"Skala Morse 1\";\"N.M. 1\";\"Skala Morse 2\";\"N.M. 2\";\"Skala Morse 3\";\"N.M. 3\";\"Skala Morse 4\";\"N.M. 4\";\"Skala Morse 5\";\"N.M. 5\";\"Skala Morse 6\";\"N.M. 6\";\"T.M.\";\"Skala Sydney 1\";\"N.S. 1\";\"Skala Sydney 2\";\"N.S. 2\";\"Skala Sydney 3\";\"N.S. 3\";\"Skala Sydney 4\";\"N.S. 4\";\"Skala Sydney 5\";\"N.S. 5\";\"Skala Sydney 6\";\"N.S. 6\";\"Skala Sydney 7\";\"N.S. 7\";\"Skala Sydney 8\";\"N.S. 8\";\"Skala Sydney 9\";\"N.S. 9\";\"Skala Sydney 10\";\"N.S. 10\";\"Skala Sydney 11\";\"N.S. 11\";\"T.S.\";\"1. Apakah ada penurunan BB yang tidak diinginkan selama 6 bulan terakhir ?\";\"Skor 1\";\"2. Apakah asupan makan berkurang karena tidak nafsu makan ?\";\"Skor 2\";\"Total Skor\";\"Pasien dengan diagnosis khusus\";\"Keterangan Diagnosa Khusus\";\"Sudah dibaca dan diketahui oleh Dietisen\";\"Jam Dibaca Dietisen\";\"Rencana Keperawatan Lainnya\"\n"
//                                ); 
//                                while(rs.next()){
//                                    htmlContent.append(
//                                        "\""+rs.getString("no_rawat")+"\";\""+rs.getString("no_rkm_medis")+"\";\""+rs.getString("nm_pasien")+"\";\""+rs.getString("tgl_lahir")+"\";\""+rs.getString("jk")+"\";\""+rs.getString("nip1")+"\";\""+rs.getString("pengkaji1")+"\";\""+rs.getString("nip2")+"\";\""+rs.getString("pengkaji2")+"\";\""+rs.getString("kd_dokter")+"\";\""+rs.getString("nm_dokter")+"\";\""+rs.getString("tanggal")+"\";\""+rs.getString("kasus_trauma")+"\";\""+rs.getString("informasi")+", "+rs.getString("ket_informasi")+"\";\""+rs.getString("tiba_diruang_rawat")+"\";\""+rs.getString("cara_masuk")+"\";\""+rs.getString("rps")+"\";\""+rs.getString("rpd")+"\";\""+rs.getString("rpk")+"\";\""+rs.getString("rpo")+"\";\""+rs.getString("riwayat_pembedahan")+"\";\""+rs.getString("riwayat_dirawat_dirs")+"\";\""+rs.getString("alat_bantu_dipakai")+"\";\""+rs.getString("riwayat_kehamilan")+", "+rs.getString("riwayat_kehamilan_perkiraan")+"\";\""+rs.getString("riwayat_tranfusi")+"\";\""+rs.getString("riwayat_alergi")+"\";\""+rs.getString("riwayat_merokok")+"\";\""+rs.getString("riwayat_merokok_jumlah")+"\";\""+rs.getString("riwayat_alkohol")+"\";\""+rs.getString("riwayat_alkohol_jumlah")+"\";\""+rs.getString("riwayat_narkoba")+"\";\""+rs.getString("riwayat_olahraga")+"\";\""+rs.getString("pemeriksaan_mental")+"\";\""+rs.getString("pemeriksaan_keadaan_umum")+"\";\""+rs.getString("pemeriksaan_gcs")+"\";\""+rs.getString("pemeriksaan_td")+"\";\""+rs.getString("pemeriksaan_nadi")+"\";\""+rs.getString("pemeriksaan_rr")+"\";\""+rs.getString("pemeriksaan_suhu")+"\";\""+rs.getString("pemeriksaan_spo2")+"\";\""+rs.getString("pemeriksaan_bb")+"\";\""+rs.getString("pemeriksaan_tb")+"\";\""+rs.getString("pemeriksaan_susunan_kepala")+", "+rs.getString("pemeriksaan_susunan_kepala_keterangan")+"\";\""+rs.getString("pemeriksaan_susunan_wajah")+", "+rs.getString("pemeriksaan_susunan_wajah_keterangan")+"\";\""+rs.getString("pemeriksaan_susunan_leher")+"\";\""+rs.getString("pemeriksaan_susunan_kejang")+", "+rs.getString("pemeriksaan_susunan_kejang_keterangan")+"\";\""+rs.getString("pemeriksaan_susunan_sensorik")+"\";\""+rs.getString("pemeriksaan_kardiovaskuler_pulsasi")+"\";\""+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi")+", "+rs.getString("pemeriksaan_kardiovaskuler_sirkulasi_keterangan")+"\";\""+rs.getString("pemeriksaan_kardiovaskuler_denyut_nadi")+"\";\""+rs.getString("pemeriksaan_respirasi_retraksi")+"\";\""+rs.getString("pemeriksaan_respirasi_pola_nafas")+"\";\""+rs.getString("pemeriksaan_respirasi_suara_nafas")+"\";\""+rs.getString("pemeriksaan_respirasi_batuk")+"\";\""+rs.getString("pemeriksaan_respirasi_volume_pernafasan")+"\";\""+rs.getString("pemeriksaan_respirasi_jenis_pernafasan")+", "+rs.getString("pemeriksaan_respirasi_jenis_pernafasan_keterangan")+"\";\""+rs.getString("pemeriksaan_respirasi_irama_nafas")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_mulut")+", "+rs.getString("pemeriksaan_gastrointestinal_mulut_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_lidah")+", "+rs.getString("pemeriksaan_gastrointestinal_lidah_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_gigi")+", "+rs.getString("pemeriksaan_gastrointestinal_gigi_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_tenggorokan")+", "+rs.getString("pemeriksaan_gastrointestinal_tenggorokan_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_abdomen")+", "+rs.getString("pemeriksaan_gastrointestinal_abdomen_keterangan")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_peistatik_usus")+"\";\""+rs.getString("pemeriksaan_gastrointestinal_anus")+"\";\""+rs.getString("pemeriksaan_neurologi_sensorik")+"\";\""+rs.getString("pemeriksaan_neurologi_pengelihatan")+", "+rs.getString("pemeriksaan_neurologi_pengelihatan_keterangan")+"\";\""+rs.getString("pemeriksaan_neurologi_alat_bantu_penglihatan")+"\";\""+rs.getString("pemeriksaan_neurologi_motorik")+"\";\""+rs.getString("pemeriksaan_neurologi_pendengaran")+"\";\""+rs.getString("pemeriksaan_neurologi_bicara")+", "+rs.getString("pemeriksaan_neurologi_bicara_keterangan")+"\";\""+rs.getString("pemeriksaan_neurologi_kekuatan_otot")+"\";\""+rs.getString("pemeriksaan_integument_kulit")+"\";\""+rs.getString("pemeriksaan_integument_warnakulit")+"\";\""+rs.getString("pemeriksaan_integument_turgor")+"\";\""+rs.getString("pemeriksaan_integument_dekubitas")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_oedema")+", "+rs.getString("pemeriksaan_muskuloskletal_oedema_keterangan")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_pergerakan_sendi")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_kekauatan_otot")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_fraktur")+", "+rs.getString("pemeriksaan_muskuloskletal_fraktur_keterangan")+"\";\""+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi")+", "+rs.getString("pemeriksaan_muskuloskletal_nyeri_sendi_keterangan")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_jumlah")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_frekuensi_durasi")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_konsistensi")+"\";\""+rs.getString("pemeriksaan_eliminasi_bab_warna")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_jumlah")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_frekuensi_durasi")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_warna")+"\";\""+rs.getString("pemeriksaan_eliminasi_bak_lainlain")+"\";\""+rs.getString("pola_aktifitas_mandi")+"\";\""+rs.getString("pola_aktifitas_makanminum")+"\";\""+rs.getString("pola_aktifitas_berpakaian")+"\";\""+rs.getString("pola_aktifitas_eliminasi")+"\";\""+rs.getString("pola_aktifitas_berpindah")+"\";\""+rs.getString("pola_nutrisi_porsi_makan")+"\";\""+rs.getString("pola_nutrisi_frekuesi_makan")+"\";\""+rs.getString("pola_nutrisi_jenis_makanan")+"\";\""+rs.getString("pola_tidur_lama_tidur")+"\";\""+rs.getString("pola_tidur_gangguan")+"\";\""+rs.getString("pengkajian_fungsi_kemampuan_sehari")+"\";\""+rs.getString("pengkajian_fungsi_berjalan")+", "+rs.getString("pengkajian_fungsi_berjalan_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_aktifitas")+"\";\""+rs.getString("pengkajian_fungsi_ambulasi")+"\";\""+rs.getString("pengkajian_fungsi_ekstrimitas_atas")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_atas_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_ekstrimitas_bawah")+", "+rs.getString("pengkajian_fungsi_ekstrimitas_bawah_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_menggenggam")+", "+rs.getString("pengkajian_fungsi_menggenggam_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_koordinasi")+", "+rs.getString("pengkajian_fungsi_koordinasi_keterangan")+"\";\""+rs.getString("pengkajian_fungsi_kesimpulan")+"\";\""+rs.getString("riwayat_psiko_kondisi_psiko")+"\";\""+rs.getString("riwayat_psiko_perilaku")+", "+rs.getString("riwayat_psiko_perilaku_keterangan")+"\";\""+rs.getString("riwayat_psiko_gangguan_jiwa")+"\";\""+rs.getString("riwayat_psiko_hubungan_keluarga")+"\";\""+rs.getString("agama")+"\";\""+rs.getString("riwayat_psiko_tinggal")+", "+rs.getString("riwayat_psiko_tinggal_keterangan")+"\";\""+rs.getString("pekerjaan")+"\";\""+rs.getString("png_jawab")+"\";\""+rs.getString("riwayat_psiko_nilai_kepercayaan")+", "+rs.getString("riwayat_psiko_nilai_kepercayaan_keterangan")+"\";\""+rs.getString("nama_bahasa")+"\";\""+rs.getString("pnd")+"\";\""+rs.getString("riwayat_psiko_pendidikan_pj")+"\";\""+rs.getString("riwayat_psiko_edukasi_diberikan")+", "+rs.getString("riwayat_psiko_edukasi_diberikan_keterangan")+"\";\""+rs.getString("penilaian_nyeri")+"\";\""+rs.getString("penilaian_nyeri_penyebab")+", "+rs.getString("penilaian_nyeri_ket_penyebab")+"\";\""+rs.getString("penilaian_nyeri_kualitas")+", "+rs.getString("penilaian_nyeri_ket_kualitas")+"\";\""+rs.getString("penilaian_nyeri_lokasi")+"\";\""+rs.getString("penilaian_nyeri_menyebar")+"\";\""+rs.getString("penilaian_nyeri_skala")+"\";\""+rs.getString("penilaian_nyeri_waktu")+"\";\""+rs.getString("penilaian_nyeri_hilang")+", "+rs.getString("penilaian_nyeri_ket_hilang")+"\";\""+rs.getString("penilaian_nyeri_diberitahukan_dokter")+", "+rs.getString("penilaian_nyeri_jam_diberitahukan_dokter")+"\";\""+rs.getString("penilaian_jatuhmorse_skala1")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai1")+"\";\""+rs.getString("penilaian_jatuhmorse_skala2")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai2")+"\";\""+rs.getString("penilaian_jatuhmorse_skala3")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai3")+"\";\""+rs.getString("penilaian_jatuhmorse_skala4")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai4")+"\";\""+rs.getString("penilaian_jatuhmorse_skala5")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai5")+"\";\""+rs.getString("penilaian_jatuhmorse_skala6")+"\";\""+rs.getString("penilaian_jatuhmorse_nilai6")+"\";\""+rs.getString("penilaian_jatuhmorse_totalnilai")+"\";\""+rs.getString("penilaian_jatuhsydney_skala1")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai1")+"\";\""+rs.getString("penilaian_jatuhsydney_skala2")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai2")+"\";\""+rs.getString("penilaian_jatuhsydney_skala3")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai3")+"\";\""+rs.getString("penilaian_jatuhsydney_skala4")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai4")+"\";\""+rs.getString("penilaian_jatuhsydney_skala5")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai5")+"\";\""+rs.getString("penilaian_jatuhsydney_skala6")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai6")+"\";\""+rs.getString("penilaian_jatuhsydney_skala7")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai7")+"\";\""+rs.getString("penilaian_jatuhsydney_skala8")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai8")+"\";\""+rs.getString("penilaian_jatuhsydney_skala9")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai9")+"\";\""+rs.getString("penilaian_jatuhsydney_skala10")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai10")+"\";\""+rs.getString("penilaian_jatuhsydney_skala11")+"\";\""+rs.getString("penilaian_jatuhsydney_nilai11")+"\";\""+rs.getString("penilaian_jatuhsydney_totalnilai")+"\";\""+rs.getString("skrining_gizi1")+"\";\""+rs.getString("nilai_gizi1")+"\";\""+rs.getString("skrining_gizi2")+"\";\""+rs.getString("nilai_gizi2")+"\";\""+rs.getString("nilai_total_gizi")+"\";\""+rs.getString("skrining_gizi_diagnosa_khusus")+"\";\""+rs.getString("skrining_gizi_ket_diagnosa_khusus")+"\";\""+rs.getString("skrining_gizi_diketahui_dietisen")+"\";\""+rs.getString("skrining_gizi_jam_diketahui_dietisen")+"\";\""+rs.getString("rencana")+"\"\n"
//                                    );
//                                }
//                                f = new File("RMPenilaianAwalKeperawatanRanap.csv");            
//                                bw = new BufferedWriter(new FileWriter(f));            
//                                bw.write(htmlContent.toString());
//
//                                bw.close();                         
//                                Desktop.getDesktop().browse(f.toURI());
//                            break; 
//                    }           
//                } catch (Exception e) {
//                    System.out.println("Notif : "+e);
//                } finally{
//                    if(rs!=null){
//                        rs.close();
//                    }
//                    if(ps!=null){
//                        ps.close();
//                    }
//                }
//            }catch(Exception e){
//                System.out.println("Notifikasi : "+e);
//            }
//        }
//        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            TCari.setText("");
            tampil();
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                ChkAccor.setSelected(true);
                isMenu();
                getMasalah();
            } catch (java.lang.NullPointerException e) {
            }
            if((evt.getClickCount()==2)&&(tbObat.getSelectedColumn()==0)){
                getData();
                TabRawat.setSelectedIndex(0);
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    ChkAccor.setSelected(true);
                    isMenu();
                    getMasalah();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    getData();
                    TabRawat.setSelectedIndex(0);
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==1){
            tampil();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            if(Valid.daysOld("./cache/masalahkeperawatan.iyem")<8){
                tampilMasalah2();
            }else{
                tampilMasalah();
            }
        } catch (Exception e) {
        }
        
        try {
            if(Valid.daysOld("./cache/rencanakeperawatan.iyem")>=7){
                tampilRencana();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowOpened

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        if(tbObat.getSelectedRow()!= -1){
            isMenu();
        }else{
            ChkAccor.setSelected(false);
            JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data yang mau ditampilkan...!!!!");
        }
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
//        if(tbObat.getSelectedRow()>-1){
//            Map<String, Object> param = new HashMap<>();    
//            param.put("namars",akses.getnamars());
//            param.put("alamatrs",akses.getalamatrs());
//            param.put("kotars",akses.getkabupatenrs());
//            param.put("propinsirs",akses.getpropinsirs());
//            param.put("kontakrs",akses.getkontakrs());
//            param.put("emailrs",akses.getemailrs());          
//            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
//            param.put("nyeri",Sequel.cariGambar("select gambar.nyeri from gambar")); 
//            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
//            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),6).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),5).toString():finger)+"\n"+Valid.SetTgl3(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString())); 
//            
//            try {
//                masalahkeperawatan="";
//                ps=koneksi.prepareStatement(
//                    "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "+
//                    "inner join penilaian_awal_keperawatan_ranap_masalah on penilaian_awal_keperawatan_ranap_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "+
//                    "where penilaian_awal_keperawatan_ranap_masalah.no_rawat=? order by penilaian_awal_keperawatan_ranap_masalah.kode_masalah");
//                try {
//                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
//                    rs=ps.executeQuery();
//                    while(rs.next()){
//                        masalahkeperawatan=rs.getString("nama_masalah")+", "+masalahkeperawatan;
//                    }
//                } catch (Exception e) {
//                    System.out.println("Notif : "+e);
//                } finally{
//                    if(rs!=null){
//                        rs.close();
//                    }
//                    if(ps!=null){
//                        ps.close();
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Notif : "+e);
//            }
//            param.put("masalah",masalahkeperawatan);  
//            try {
//                masalahkeperawatan="";
//                ps=koneksi.prepareStatement(
//                    "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "+
//                    "inner join penilaian_awal_keperawatan_ranap_rencana on penilaian_awal_keperawatan_ranap_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "+
//                    "where penilaian_awal_keperawatan_ranap_rencana.no_rawat=? order by penilaian_awal_keperawatan_ranap_rencana.kode_rencana");
//                try {
//                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
//                    rs=ps.executeQuery();
//                    while(rs.next()){
//                        masalahkeperawatan=rs.getString("rencana_keperawatan")+", "+masalahkeperawatan;
//                    }
//                } catch (Exception e) {
//                    System.out.println("Notif : "+e);
//                } finally{
//                    if(rs!=null){
//                        rs.close();
//                    }
//                    if(ps!=null){
//                        ps.close();
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Notif : "+e);
//            }
//            param.put("rencana",masalahkeperawatan); 
//            Valid.MyReportqry("rptCetakPenilaianAwalKeperawatanRanap.jasper","report","::[ Laporan Penilaian Awal Keperawatan Rawat Inap ]::",
//                "select asesmen_awal_keperawatan_neonatus_nicu.no_rawat,asesmen_awal_keperawatan_neonatus_nicu.tanggal,asesmen_awal_keperawatan_neonatus_nicu.informasi,asesmen_awal_keperawatan_neonatus_nicu.ket_informasi,asesmen_awal_keperawatan_neonatus_nicu.tiba_diruang_rawat,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.kasus_trauma,asesmen_awal_keperawatan_neonatus_nicu.cara_masuk,asesmen_awal_keperawatan_neonatus_nicu.rps,asesmen_awal_keperawatan_neonatus_nicu.rpd,asesmen_awal_keperawatan_neonatus_nicu.rpk,asesmen_awal_keperawatan_neonatus_nicu.rpo,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_pembedahan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_dirawat_dirs,asesmen_awal_keperawatan_neonatus_nicu.alat_bantu_dipakai,asesmen_awal_keperawatan_neonatus_nicu.riwayat_kehamilan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_kehamilan_perkiraan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_tranfusi,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alergi,asesmen_awal_keperawatan_neonatus_nicu.riwayat_merokok,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_merokok_jumlah,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alkohol,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alkohol_jumlah,asesmen_awal_keperawatan_neonatus_nicu.riwayat_narkoba,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_olahraga,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_mental,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_keadaan_umum,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gcs,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_td,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_nadi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_rr,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_suhu,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_spo2,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_bb,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_tb,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kepala,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kepala_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_wajah,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_wajah_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_leher,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kejang,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kejang_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_sensorik,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_denyut_nadi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_sirkulasi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_sirkulasi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_pulsasi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_pola_nafas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_retraksi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_suara_nafas,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_volume_pernafasan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_jenis_pernafasan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_jenis_pernafasan_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_irama_nafas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_batuk,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_mulut,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_mulut_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_gigi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_gigi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_lidah,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_lidah_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_tenggorokan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_tenggorokan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_abdomen,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_abdomen_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_peistatik_usus,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_anus,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pengelihatan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pengelihatan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_alat_bantu_penglihatan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pendengaran,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_bicara,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_bicara_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_sensorik,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_motorik,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_kekuatan_otot,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_warnakulit,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_turgor,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_kulit,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_dekubitas,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_pergerakan_sendi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_kekauatan_otot,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_nyeri_sendi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_nyeri_sendi_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_oedema,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_oedema_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_fraktur,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_fraktur_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_frekuensi_jumlah,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_frekuensi_durasi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_konsistensi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_warna,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_frekuensi_jumlah,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_frekuensi_durasi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_warna,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_lainlain,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_makanminum,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_mandi,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_eliminasi,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_berpakaian,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_berpindah,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_frekuesi_makan,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_jenis_makanan,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_porsi_makan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pola_tidur_lama_tidur,asesmen_awal_keperawatan_neonatus_nicu.pola_tidur_gangguan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_kemampuan_sehari,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_aktifitas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_berjalan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_berjalan_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ambulasi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_atas,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_atas_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_bawah,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_bawah_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_menggenggam,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_menggenggam_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_koordinasi,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_koordinasi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_kesimpulan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_kondisi_psiko,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_gangguan_jiwa,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_perilaku,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_perilaku_keterangan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_hubungan_keluarga,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_tinggal,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_tinggal_keterangan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_nilai_kepercayaan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_nilai_kepercayaan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_pendidikan_pj,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_edukasi_diberikan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_edukasi_diberikan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_penyebab,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_penyebab,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_kualitas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_kualitas,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_lokasi,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_menyebar,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_skala,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_waktu,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_hilang,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_hilang,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_diberitahukan_dokter,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_jam_diberitahukan_dokter,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala1,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai1,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala3,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala4,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai4,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala6,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_totalnilai,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala1,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai1,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai2,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala4,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai4,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai5,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala7,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai7,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala8,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai8,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala9,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai9,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala10,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai10,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala11,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai11,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_totalnilai,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi1,asesmen_awal_keperawatan_neonatus_nicu.nilai_gizi1,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi2,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.nilai_gizi2,asesmen_awal_keperawatan_neonatus_nicu.nilai_total_gizi,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_diagnosa_khusus,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_ket_diagnosa_khusus,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_diketahui_dietisen,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_jam_diketahui_dietisen,asesmen_awal_keperawatan_neonatus_nicu.rencana,asesmen_awal_keperawatan_neonatus_nicu.nip1,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.nip2,asesmen_awal_keperawatan_neonatus_nicu.kd_dokter,pasien.tgl_lahir,pasien.jk,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter,"+
//                "reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.agama,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,bahasa_pasien.nama_bahasa "+
//                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                "inner join asesmen_awal_keperawatan_neonatus_nicu on reg_periksa.no_rawat=asesmen_awal_keperawatan_neonatus_nicu.no_rawat "+
//                "inner join petugas as pengkaji1 on asesmen_awal_keperawatan_neonatus_nicu.nip1=pengkaji1.nip "+
//                "inner join petugas as pengkaji2 on asesmen_awal_keperawatan_neonatus_nicu.nip2=pengkaji2.nip "+
//                "inner join dokter on asesmen_awal_keperawatan_neonatus_nicu.kd_dokter=dokter.kd_dokter "+
//                "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
//                "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
//            
//            Valid.MyReportqry("rptCetakPenilaianAwalKeperawatanRanap2.jasper","report","::[ Laporan Penilaian Awal Keperawatan Rawat Inap ]::",
//                "select asesmen_awal_keperawatan_neonatus_nicu.no_rawat,asesmen_awal_keperawatan_neonatus_nicu.tanggal,asesmen_awal_keperawatan_neonatus_nicu.informasi,asesmen_awal_keperawatan_neonatus_nicu.ket_informasi,asesmen_awal_keperawatan_neonatus_nicu.tiba_diruang_rawat,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.kasus_trauma,asesmen_awal_keperawatan_neonatus_nicu.cara_masuk,asesmen_awal_keperawatan_neonatus_nicu.rps,asesmen_awal_keperawatan_neonatus_nicu.rpd,asesmen_awal_keperawatan_neonatus_nicu.rpk,asesmen_awal_keperawatan_neonatus_nicu.rpo,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_pembedahan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_dirawat_dirs,asesmen_awal_keperawatan_neonatus_nicu.alat_bantu_dipakai,asesmen_awal_keperawatan_neonatus_nicu.riwayat_kehamilan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_kehamilan_perkiraan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_tranfusi,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alergi,asesmen_awal_keperawatan_neonatus_nicu.riwayat_merokok,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_merokok_jumlah,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alkohol,asesmen_awal_keperawatan_neonatus_nicu.riwayat_alkohol_jumlah,asesmen_awal_keperawatan_neonatus_nicu.riwayat_narkoba,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_olahraga,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_mental,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_keadaan_umum,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gcs,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_td,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_nadi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_rr,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_suhu,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_spo2,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_bb,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_tb,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kepala,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kepala_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_wajah,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_wajah_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_leher,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kejang,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_kejang_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_susunan_sensorik,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_denyut_nadi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_sirkulasi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_sirkulasi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_kardiovaskuler_pulsasi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_pola_nafas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_retraksi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_suara_nafas,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_volume_pernafasan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_jenis_pernafasan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_jenis_pernafasan_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_irama_nafas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_respirasi_batuk,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_mulut,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_mulut_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_gigi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_gigi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_lidah,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_lidah_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_tenggorokan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_tenggorokan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_abdomen,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_abdomen_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_peistatik_usus,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_gastrointestinal_anus,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pengelihatan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pengelihatan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_alat_bantu_penglihatan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_pendengaran,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_bicara,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_bicara_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_sensorik,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_motorik,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_neurologi_kekuatan_otot,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_warnakulit,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_turgor,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_kulit,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_integument_dekubitas,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_pergerakan_sendi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_kekauatan_otot,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_nyeri_sendi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_nyeri_sendi_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_oedema,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_oedema_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_fraktur,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_muskuloskletal_fraktur_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_frekuensi_jumlah,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_frekuensi_durasi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_konsistensi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bab_warna,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_frekuensi_jumlah,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_frekuensi_durasi,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_warna,asesmen_awal_keperawatan_neonatus_nicu.pemeriksaan_eliminasi_bak_lainlain,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_makanminum,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_mandi,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_eliminasi,asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_berpakaian,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pola_aktifitas_berpindah,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_frekuesi_makan,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_jenis_makanan,asesmen_awal_keperawatan_neonatus_nicu.pola_nutrisi_porsi_makan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pola_tidur_lama_tidur,asesmen_awal_keperawatan_neonatus_nicu.pola_tidur_gangguan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_kemampuan_sehari,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_aktifitas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_berjalan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_berjalan_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ambulasi,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_atas,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_atas_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_bawah,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_ekstrimitas_bawah_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_menggenggam,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_menggenggam_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_koordinasi,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_koordinasi_keterangan,asesmen_awal_keperawatan_neonatus_nicu.pengkajian_fungsi_kesimpulan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_kondisi_psiko,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_gangguan_jiwa,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_perilaku,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_perilaku_keterangan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_hubungan_keluarga,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_tinggal,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_tinggal_keterangan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_nilai_kepercayaan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_nilai_kepercayaan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_pendidikan_pj,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_edukasi_diberikan,asesmen_awal_keperawatan_neonatus_nicu.riwayat_psiko_edukasi_diberikan_keterangan,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_penyebab,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_penyebab,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_kualitas,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_kualitas,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_lokasi,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_menyebar,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_skala,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_waktu,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_hilang,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_ket_hilang,asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_diberitahukan_dokter,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_nyeri_jam_diberitahukan_dokter,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala1,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai1,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala3,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala4,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai4,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_skala6,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_nilai6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhmorse_totalnilai,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala1,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai1,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala2,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai2,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai3,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala4,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai4,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala5,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai5,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai6,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala7,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai7,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala8,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai8,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala9,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai9,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala10,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai10,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_skala11,asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_nilai11,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.penilaian_jatuhsydney_totalnilai,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi1,asesmen_awal_keperawatan_neonatus_nicu.nilai_gizi1,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi2,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.nilai_gizi2,asesmen_awal_keperawatan_neonatus_nicu.nilai_total_gizi,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_diagnosa_khusus,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_ket_diagnosa_khusus,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_diketahui_dietisen,asesmen_awal_keperawatan_neonatus_nicu.skrining_gizi_jam_diketahui_dietisen,asesmen_awal_keperawatan_neonatus_nicu.rencana,asesmen_awal_keperawatan_neonatus_nicu.nip1,"+
//                "asesmen_awal_keperawatan_neonatus_nicu.nip2,asesmen_awal_keperawatan_neonatus_nicu.kd_dokter,pasien.tgl_lahir,pasien.jk,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter,"+
//                "reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.agama,pasien.pekerjaan,pasien.pnd,penjab.png_jawab,bahasa_pasien.nama_bahasa "+
//                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
//                "inner join asesmen_awal_keperawatan_neonatus_nicu on reg_periksa.no_rawat=asesmen_awal_keperawatan_neonatus_nicu.no_rawat "+
//                "inner join petugas as pengkaji1 on asesmen_awal_keperawatan_neonatus_nicu.nip1=pengkaji1.nip "+
//                "inner join petugas as pengkaji2 on asesmen_awal_keperawatan_neonatus_nicu.nip2=pengkaji2.nip "+
//                "inner join dokter on asesmen_awal_keperawatan_neonatus_nicu.kd_dokter=dokter.kd_dokter "+
//                "inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
//                "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where reg_periksa.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"'",param);
//        }else{
//            JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data terlebih dahulu..!!!!");
//        }  
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void TglAsuhanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TglAsuhanKeyPressed
//        Valid.pindah(evt,BtnDPJP,MacamKasus);
    }//GEN-LAST:event_TglAsuhanKeyPressed

    private void AnamnesisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AnamnesisKeyPressed
//        Valid.pindah(evt,MacamKasus,KetAnamnesis);
    }//GEN-LAST:event_AnamnesisKeyPressed

    private void BtnPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPetugasKeyPressed
        Valid.pindah(evt,BtnSimpan,BtnPetugas);
    }//GEN-LAST:event_BtnPetugasKeyPressed

    private void BtnPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPetugasActionPerformed
        i=1;
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setAlwaysOnTop(false);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnPetugasActionPerformed

    private void KdPetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPetugasKeyPressed

    }//GEN-LAST:event_KdPetugasKeyPressed

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{
            Valid.pindah(evt,TCari,BtnPetugas);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void AsalPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AsalPasienKeyPressed
//        Valid.pindah(evt,TibadiRuang,KeluhanUtama);
    }//GEN-LAST:event_AsalPasienKeyPressed

    private void KeluhanUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeluhanUtamaKeyPressed
        Valid.pindah2(evt,AsalPasien,DiagnosisMasuk);
    }//GEN-LAST:event_KeluhanUtamaKeyPressed

    private void RiwayatKeluhanSaatIniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatKeluhanSaatIniKeyPressed
        Valid.pindah2(evt,DiagnosisMasuk,RiwayatPenggunaanObat);
    }//GEN-LAST:event_RiwayatKeluhanSaatIniKeyPressed

    private void DiagnosisMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosisMasukKeyPressed
        Valid.pindah2(evt,KeluhanUtama,RiwayatKeluhanSaatIni);
    }//GEN-LAST:event_DiagnosisMasukKeyPressed

    private void RiwayatPenggunaanObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatPenggunaanObatKeyPressed
        Valid.pindah2(evt,RiwayatKeluhanSaatIni,RiwayatKehamilanIbu);
    }//GEN-LAST:event_RiwayatPenggunaanObatKeyPressed

    private void DetailRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetailRencanaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DetailRencanaKeyPressed

    private void KetAnamnesisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetAnamnesisKeyPressed
//        Valid.pindah(evt,Anamnesis,TibadiRuang);
    }//GEN-LAST:event_KetAnamnesisKeyPressed

    private void DitolongOlehLainLainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DitolongOlehLainLainKeyPressed
        Valid.pindah(evt,RiwayatKehamilanIbu,LahirDi);
    }//GEN-LAST:event_DitolongOlehLainLainKeyPressed

    private void RiwayatKehamilanIbuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatKehamilanIbuKeyPressed
        Valid.pindah(evt,RiwayatPenggunaanObat,DitolongOlehLainLain);
    }//GEN-LAST:event_RiwayatKehamilanIbuKeyPressed

    private void LahirDiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LahirDiKeyPressed
        Valid.pindah(evt,DitolongOlehLainLain,ProsesPersalinan);
    }//GEN-LAST:event_LahirDiKeyPressed

    private void ProsesPersalinanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsesPersalinanKeyPressed
//        Valid.pindah(evt,LahirDi,KetSedangMenyusui);
    }//GEN-LAST:event_ProsesPersalinanKeyPressed

    private void RiwayatKesehatanSebelumnyaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatKesehatanSebelumnyaKeyPressed
//        Valid.pindah(evt,Alergi,OpnameRS);
    }//GEN-LAST:event_RiwayatKesehatanSebelumnyaKeyPressed

    private void OpnameRSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OpnameRSKeyPressed
        Valid.pindah(evt,RiwayatKesehatanSebelumnya,RiwayatAlergi);
    }//GEN-LAST:event_OpnameRSKeyPressed

    private void RiwayatAlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatAlergiKeyPressed
        Valid.pindah(evt,OpnameRS,SelamaHariOpname);
    }//GEN-LAST:event_RiwayatAlergiKeyPressed

    private void SelamaHariOpnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelamaHariOpnameKeyPressed
        Valid.pindah(evt,RiwayatAlergi,Makanan);
    }//GEN-LAST:event_SelamaHariOpnameKeyPressed

    private void MakananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MakananKeyPressed
        Valid.pindah(evt,SelamaHariOpname,Imunisasi);
    }//GEN-LAST:event_MakananKeyPressed

    private void ImunisasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ImunisasiKeyPressed
        Valid.pindah(evt,Makanan,UsiaGestasi);
    }//GEN-LAST:event_ImunisasiKeyPressed

    private void UsiaGestasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsiaGestasiKeyPressed
//        Valid.pindah(evt,Imunisasi,KeadaanMentalUmum);
    }//GEN-LAST:event_UsiaGestasiKeyPressed

    private void UmurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UmurKeyPressed
//        Valid.pindah(evt,KeadaanMentalUmum,LD);
    }//GEN-LAST:event_UmurKeyPressed

    private void LDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LDKeyPressed
        Valid.pindah(evt,Umur,Nadi);
    }//GEN-LAST:event_LDKeyPressed

    private void BBLGiziKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BBLGiziKeyPressed
        Valid.pindah(evt,Nadi,BBS);
    }//GEN-LAST:event_BBLGiziKeyPressed

    private void BBSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BBSKeyPressed
        Valid.pindah(evt,BBLGizi,PBTB);
    }//GEN-LAST:event_BBSKeyPressed

    private void PBTBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PBTBKeyPressed
        Valid.pindah(evt,BBS,LLA);
    }//GEN-LAST:event_PBTBKeyPressed

    private void LLAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LLAKeyPressed
        Valid.pindah(evt,PBTB,LK);
    }//GEN-LAST:event_LLAKeyPressed

    private void LKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LKKeyPressed
        Valid.pindah(evt,LLA,BentukDada);
    }//GEN-LAST:event_LKKeyPressed

    private void BentukDadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BentukDadaKeyPressed
        Valid.pindah(evt,LK,SaturasiO2);
    }//GEN-LAST:event_BentukDadaKeyPressed

    private void SaturasiO2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SaturasiO2KeyPressed
        Valid.pindah(evt,BentukDada,PernafasanSpontan);
    }//GEN-LAST:event_SaturasiO2KeyPressed

    private void PernafasanSpontanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PernafasanSpontanKeyPressed
        Valid.pindah(evt,SaturasiO2,Frekuensi);
    }//GEN-LAST:event_PernafasanSpontanKeyPressed

    private void FrekuensiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FrekuensiKeyPressed
//        Valid.pindah(evt,PernafasanSpontan,SistemSarafLeher);
    }//GEN-LAST:event_FrekuensiKeyPressed

    private void SesakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SesakKeyPressed
//        Valid.pindah(evt,KardiovaskularDenyutNadi,AlatBantuNafas);
    }//GEN-LAST:event_SesakKeyPressed

    private void AlatBantuNafasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlatBantuNafasKeyPressed
        Valid.pindah(evt,Sesak,Ekstremitas);
    }//GEN-LAST:event_AlatBantuNafasKeyPressed

    private void EkstremitasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EkstremitasKeyPressed
        Valid.pindah(evt,AlatBantuNafas,Pendarahan);
    }//GEN-LAST:event_EkstremitasKeyPressed

    private void KeadaanSaatIniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeadaanSaatIniKeyPressed
        Valid.pindah(evt,WarnaLendir,FemoralisKanan);
    }//GEN-LAST:event_KeadaanSaatIniKeyPressed

    private void BunyiJantungKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BunyiJantungKeyPressed
        Valid.pindah(evt,Pendarahan,RespirasiJenisPernafasan);
    }//GEN-LAST:event_BunyiJantungKeyPressed

    private void RespirasiJenisPernafasanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RespirasiJenisPernafasanKeyPressed
        Valid.pindah(evt,BunyiJantung,WarnaLendir);
    }//GEN-LAST:event_RespirasiJenisPernafasanKeyPressed

    private void WarnaLendirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WarnaLendirKeyPressed
        Valid.pindah(evt,RespirasiJenisPernafasan,KeadaanSaatIni);
    }//GEN-LAST:event_WarnaLendirKeyPressed

    private void PendarahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PendarahanKeyPressed
        Valid.pindah(evt,Ekstremitas,BunyiJantung);
    }//GEN-LAST:event_PendarahanKeyPressed

    private void FemoralisKananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FemoralisKananKeyPressed
        Valid.pindah(evt,KeadaanSaatIni,Kapiler);
    }//GEN-LAST:event_FemoralisKananKeyPressed

    private void CaraMinumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraMinumKeyPressed
        Valid.pindah(evt,KeadaanSekarang,TurgorKulit);
    }//GEN-LAST:event_CaraMinumKeyPressed

    private void BeratBadanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BeratBadanKeyPressed
//        Valid.pindah(evt,KetGastrointestinalLidah,KetGastrointestinalGigi);
    }//GEN-LAST:event_BeratBadanKeyPressed

    private void KapilerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KapilerKeyPressed
        Valid.pindah(evt,FemoralisKanan,FemoralisKiri);
    }//GEN-LAST:event_KapilerKeyPressed

    private void FemoralisKiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FemoralisKiriKeyPressed
//        Valid.pindah(evt,Kapiler,KetGastrointestinalLidah);
    }//GEN-LAST:event_FemoralisKiriKeyPressed

    private void HasilLabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilLabKeyPressed
        
    }//GEN-LAST:event_HasilLabKeyPressed

    private void IntavenaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntavenaKeyPressed
        
    }//GEN-LAST:event_IntavenaKeyPressed

    private void HariIntavenaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HariIntavenaKeyPressed
        Valid.pindah(evt,Intavena,KeadaanSekarang);
    }//GEN-LAST:event_HariIntavenaKeyPressed

    private void KeadaanSekarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeadaanSekarangKeyPressed
//        Valid.pindah(evt,KetGastrointestinalGigi,CaraMinum);
    }//GEN-LAST:event_KeadaanSekarangKeyPressed

    private void TurgorKulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TurgorKulitKeyPressed
        Valid.pindah(evt,CaraMinum,Abdomen);
    }//GEN-LAST:event_TurgorKulitKeyPressed

    private void UbunUbunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UbunUbunKeyPressed
        Valid.pindah(evt,Lidah,HasilLaboratorium);
    }//GEN-LAST:event_UbunUbunKeyPressed

    private void LendirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LendirKeyPressed
//        Valid.pindah(evt,Residu,IntegumentKulit);
    }//GEN-LAST:event_LendirKeyPressed

    private void AbdomenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AbdomenKeyPressed
        Valid.pindah(evt,TurgorKulit,Muntah);
    }//GEN-LAST:event_AbdomenKeyPressed

    private void MuntahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MuntahKeyPressed
        Valid.pindah(evt,Abdomen,Lidah);
    }//GEN-LAST:event_MuntahKeyPressed

    private void LidahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LidahKeyPressed
        Valid.pindah(evt,Muntah,UbunUbun);
    }//GEN-LAST:event_LidahKeyPressed

    private void HasilLaboratoriumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilLaboratoriumKeyPressed
        Valid.pindah(evt,UbunUbun,WarnaKulit);
    }//GEN-LAST:event_HasilLaboratoriumKeyPressed

    private void WarnaKulitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WarnaKulitKeyPressed
        Valid.pindah(evt,HasilLaboratorium,Residu);
    }//GEN-LAST:event_WarnaKulitKeyPressed

    private void ResiduKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ResiduKeyPressed
        Valid.pindah(evt,WarnaKulit,Lendir);
    }//GEN-LAST:event_ResiduKeyPressed

    private void KesadaranKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KesadaranKeyPressed
//        Valid.pindah(evt,IntegumentDecubitus,KeteranganHasilLab);
    }//GEN-LAST:event_KesadaranKeyPressed

    private void KremerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KremerKeyPressed
        Valid.pindah(evt,Kesadaran,KetTangisan);
    }//GEN-LAST:event_KremerKeyPressed

    private void ProduksiUrineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProduksiUrineKeyPressed
        Valid.pindah(evt,Tangisan,Kepala);
    }//GEN-LAST:event_ProduksiUrineKeyPressed

    private void TangisanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TangisanKeyPressed
        Valid.pindah(evt,JenisPupil,ProduksiUrine);
    }//GEN-LAST:event_TangisanKeyPressed

    private void KepalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KepalaKeyPressed
//        Valid.pindah(evt,KetMuskuloskletalFraktur,KetMuskuloskletalNyeriSendi);
    }//GEN-LAST:event_KepalaKeyPressed

    private void KetTangisanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetTangisanKeyPressed
        Valid.pindah(evt,Kremer,JenisPupil);
    }//GEN-LAST:event_KetTangisanKeyPressed

    private void JenisPupilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JenisPupilKeyPressed
        Valid.pindah(evt,KetTangisan,Tangisan);
    }//GEN-LAST:event_JenisPupilKeyPressed

    private void LingkarKepalaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LingkarKepalaKeyPressed
//        Valid.pindah(evt,PupilKanan,WBAB);
    }//GEN-LAST:event_LingkarKepalaKeyPressed

    private void PupilKiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PupilKiriKeyPressed
//        Valid.pindah(evt,KetMuskuloskletalNyeriSendi,PupilKanan);
    }//GEN-LAST:event_PupilKiriKeyPressed

    private void PupilKananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PupilKananKeyPressed
        Valid.pindah(evt,PupilKiri,LingkarKepala);
    }//GEN-LAST:event_PupilKananKeyPressed

    private void BAKKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BAKKeyPressed
//        Valid.pindah(evt,WBAB,XBAK);
    }//GEN-LAST:event_BAKKeyPressed

    private void KetNeurosensoriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetNeurosensoriKeyPressed
//        Valid.pindah(evt,XBAK,LBAK);
    }//GEN-LAST:event_KetNeurosensoriKeyPressed

    private void KejangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KejangKeyPressed
        Valid.pindah(evt,KeadaanReproduksi,Vagina);
    }//GEN-LAST:event_KejangKeyPressed

    private void GerakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GerakanKeyPressed
//        Valid.pindah(evt,LBAK,ReflexTampak);
    }//GEN-LAST:event_GerakanKeyPressed

    private void ReflexTampakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ReflexTampakKeyPressed
        Valid.pindah(evt,Gerakan,KeadaanReproduksi);
    }//GEN-LAST:event_ReflexTampakKeyPressed

    private void KeadaanReproduksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeadaanReproduksiKeyPressed
        Valid.pindah(evt,ReflexTampak,Kejang);
    }//GEN-LAST:event_KeadaanReproduksiKeyPressed

    private void VaginaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VaginaKeyPressed
//        Valid.pindah(evt,Kejang,PolaNutrisiPorsi);
    }//GEN-LAST:event_VaginaKeyPressed

    private void AlatBantuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlatBantuKeyPressed
//        Valid.pindah(evt,PolaTidurLama,Menstruasi);
    }//GEN-LAST:event_AlatBantuKeyPressed

    private void MenstruasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenstruasiKeyPressed
        Valid.pindah(evt,AlatBantu,BAB);
    }//GEN-LAST:event_MenstruasiKeyPressed

    private void BABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BABKeyPressed
        Valid.pindah(evt,Menstruasi,WarnaBAB);
    }//GEN-LAST:event_BABKeyPressed

    private void WarnaBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WarnaBABKeyPressed
        Valid.pindah(evt,BAB,Preputium);
    }//GEN-LAST:event_WarnaBABKeyPressed

    private void PreputiumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PreputiumKeyPressed
        Valid.pindah(evt,WarnaBAB,KondisiBAB);
    }//GEN-LAST:event_PreputiumKeyPressed

    private void KondisiBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiBABKeyPressed
        Valid.pindah(evt,Preputium,Feses);
    }//GEN-LAST:event_KondisiBABKeyPressed

    private void FesesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FesesKeyPressed
        Valid.pindah(evt,KondisiBAB,Suhu);
    }//GEN-LAST:event_FesesKeyPressed

    private void HipospadiaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HipospadiaKeyPressed
        Valid.pindah(evt,Suhu,ccBAB);
    }//GEN-LAST:event_HipospadiaKeyPressed

    private void ccBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ccBABKeyPressed
        Valid.pindah(evt,Hipospadia,Anus);
    }//GEN-LAST:event_ccBABKeyPressed

    private void AnusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AnusKeyPressed
        Valid.pindah(evt,ccBAB,FrekuensiBAB);
    }//GEN-LAST:event_AnusKeyPressed

    private void BerkunjungKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BerkunjungKeyPressed
        Valid.pindah(evt,FrekuensiBAB,Mekonium);
    }//GEN-LAST:event_BerkunjungKeyPressed

    private void MekoniumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MekoniumKeyPressed
        Valid.pindah(evt,Berkunjung,Kongenital);
    }//GEN-LAST:event_MekoniumKeyPressed

    private void KongenitalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KongenitalKeyPressed
        Valid.pindah(evt,Mekonium,KondisiKebersihanDiri);
    }//GEN-LAST:event_KongenitalKeyPressed

    private void KondisiKebersihanDiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KondisiKebersihanDiriKeyPressed
        Valid.pindah(evt,Kongenital,Integritas);
    }//GEN-LAST:event_KondisiKebersihanDiriKeyPressed

    private void IntegritasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntegritasKeyPressed
        Valid.pindah(evt,KondisiKebersihanDiri,Dehidrasi);
    }//GEN-LAST:event_IntegritasKeyPressed

    private void DehidrasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DehidrasiKeyPressed
        Valid.pindah(evt,Integritas,KukuBersihDiri);
    }//GEN-LAST:event_DehidrasiKeyPressed

    private void KukuBersihDiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KukuBersihDiriKeyPressed
        Valid.pindah(evt,Dehidrasi,TaliPusat);
    }//GEN-LAST:event_KukuBersihDiriKeyPressed

    private void TaliPusatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TaliPusatKeyPressed
        Valid.pindah(evt,KukuBersihDiri,WarnaKulitBersihDiri);
    }//GEN-LAST:event_TaliPusatKeyPressed

    private void WarnaKulitBersihDiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WarnaKulitBersihDiriKeyPressed
        Valid.pindah(evt,TaliPusat,DerajatSuhu);
    }//GEN-LAST:event_WarnaKulitBersihDiriKeyPressed

    private void DerajatSuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DerajatSuhuKeyPressed
        Valid.pindah(evt,WarnaKulitBersihDiri,SuhuBersihDiri);
    }//GEN-LAST:event_DerajatSuhuKeyPressed

    private void SuhuBersihDiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuBersihDiriKeyPressed
        Valid.pindah(evt,DerajatSuhu,KeteranganTidur);
    }//GEN-LAST:event_SuhuBersihDiriKeyPressed

    private void KeteranganTidurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganTidurKeyPressed
        Valid.pindah(evt,SuhuBersihDiri,KepalaBersihDiri);
    }//GEN-LAST:event_KeteranganTidurKeyPressed

    private void KepalaBersihDiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KepalaBersihDiriKeyPressed
        Valid.pindah(evt,KeteranganTidur,MataBersihDiri);
    }//GEN-LAST:event_KepalaBersihDiriKeyPressed

    private void MataBersihDiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MataBersihDiriKeyPressed
//        Valid.pindah(evt,KepalaBersihDiri,KeteranganEdukasiPsikologis);
    }//GEN-LAST:event_MataBersihDiriKeyPressed

    private void UmbilicalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UmbilicalKeyPressed
//        Valid.pindah(evt,KeteranganEdukasiPsikologis,Provokes);
    }//GEN-LAST:event_UmbilicalKeyPressed

    private void AbdomenKebersihanDIriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AbdomenKebersihanDIriKeyPressed
//        Valid.pindah(evt,Umbilical,KetProvokes);
    }//GEN-LAST:event_AbdomenKebersihanDIriKeyPressed

    private void PolaTidurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PolaTidurKeyPressed
//        Valid.pindah(evt,KetProvokes,KetQuality);
    }//GEN-LAST:event_PolaTidurKeyPressed

    private void LokasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LokasiKeyPressed
//        Valid.pindah(evt,KetQuality,KontakMata);
    }//GEN-LAST:event_LokasiKeyPressed

    private void KontakMataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KontakMataKeyPressed
        Valid.pindah(evt,Lokasi,StatusGestasi);
    }//GEN-LAST:event_KontakMataKeyPressed

    private void StatusGestasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusGestasiKeyPressed
//        Valid.pindah(evt,KontakMata,Durasi);
    }//GEN-LAST:event_StatusGestasiKeyPressed

    private void BanyakTidurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BanyakTidurKeyPressed
//        Valid.pindah(evt,Durasi,KetNyeri);
    }//GEN-LAST:event_BanyakTidurKeyPressed

    private void StatusAnakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusAnakKeyPressed
//        Valid.pindah(evt,KetNyeri,KetPadaDokter);
    }//GEN-LAST:event_StatusAnakKeyPressed

    private void SkalaResiko1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko1ItemStateChanged
        if(SkalaResiko1.getSelectedIndex()==0){
            NilaiResiko1.setText("0");
        }else{
            NilaiResiko1.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko1ItemStateChanged

    private void SkalaResiko1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko1KeyPressed
//        Valid.pindah(evt,KetPadaDokter,SkalaResiko2);
    }//GEN-LAST:event_SkalaResiko1KeyPressed

    private void SkalaResiko2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko2ItemStateChanged
        if(SkalaResiko2.getSelectedIndex()==0){
            NilaiResiko2.setText("0");
        }else if(SkalaResiko2.getSelectedIndex()==1){
            NilaiResiko2.setText("1");
        }else{
            NilaiResiko2.setText("2");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko2ItemStateChanged

    private void SkalaResiko2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko2KeyPressed
        Valid.pindah(evt,SkalaResiko1,SkalaResiko3);
    }//GEN-LAST:event_SkalaResiko2KeyPressed

    private void SkalaResiko3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko3ItemStateChanged
        if(SkalaResiko3.getSelectedIndex()==0){
            NilaiResiko3.setText("0");
        }else if(SkalaResiko3.getSelectedIndex()==1){
            NilaiResiko3.setText("1");
        }else{
            NilaiResiko3.setText("2");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko3ItemStateChanged

    private void SkalaResiko3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko3KeyPressed
        Valid.pindah(evt,SkalaResiko2,SkalaResiko4);
    }//GEN-LAST:event_SkalaResiko3KeyPressed

    private void SkalaResiko4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko4ItemStateChanged
        if(SkalaResiko4.getSelectedIndex()==0){
            NilaiResiko4.setText("0");
        }else{
            NilaiResiko4.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko4ItemStateChanged

    private void SkalaResiko4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko4KeyPressed
        Valid.pindah(evt,SkalaResiko3,SkalaResiko5);
    }//GEN-LAST:event_SkalaResiko4KeyPressed

    private void SkalaResiko5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko5ItemStateChanged
        if(SkalaResiko5.getSelectedIndex()==0){
            NilaiResiko5.setText("0");
        }else if(SkalaResiko5.getSelectedIndex()==1){
            NilaiResiko5.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko5ItemStateChanged

    private void SkalaResiko5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko5KeyPressed
        Valid.pindah(evt,SkalaResiko4,SkalaResiko6);
    }//GEN-LAST:event_SkalaResiko5KeyPressed

    private void SkalaResiko6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaResiko6ItemStateChanged
        if(SkalaResiko6.getSelectedIndex()==0){
            NilaiResiko6.setText("0");
        }else{
            NilaiResiko6.setText("1");
        }
        isTotalResikoJatuh();
    }//GEN-LAST:event_SkalaResiko6ItemStateChanged

    private void SkalaResiko6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaResiko6KeyPressed
        Valid.pindah(evt,SkalaResiko5,SkalaSydney1);
    }//GEN-LAST:event_SkalaResiko6KeyPressed

    private void SkalaSydney1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney1ItemStateChanged
        if(SkalaSydney1.getSelectedIndex()==0){
            NilaiSydney1.setText("0");
        }else if(SkalaSydney1.getSelectedIndex()==1){
            NilaiSydney1.setText("1");
        }else{
            NilaiSydney1.setText("2");
        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney1ItemStateChanged

    private void SkalaSydney1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney1KeyPressed
        Valid.pindah(evt,SkalaResiko6,SkalaSydney2);
    }//GEN-LAST:event_SkalaSydney1KeyPressed

    private void SkalaSydney2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney2ItemStateChanged
        if(SkalaSydney2.getSelectedIndex()==0){
            NilaiSydney2.setText("0");
        }else if(SkalaSydney2.getSelectedIndex()==1){
            NilaiSydney2.setText("1");
        }else{
            NilaiSydney2.setText("2");
        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney2ItemStateChanged

    private void SkalaSydney2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney2KeyPressed
        Valid.pindah(evt,SkalaSydney1,SkalaSydney3);
    }//GEN-LAST:event_SkalaSydney2KeyPressed

    private void SkalaSydney3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney3ItemStateChanged
        if(SkalaSydney3.getSelectedIndex()==0){
            NilaiSydney3.setText("0");
        }else if(SkalaSydney3.getSelectedIndex()==1){
            NilaiSydney3.setText("1");
        }else{
            NilaiSydney3.setText("2");
        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney3ItemStateChanged

    private void SkalaSydney3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney3KeyPressed
        Valid.pindah(evt,SkalaSydney2,SkalaSydney4);
    }//GEN-LAST:event_SkalaSydney3KeyPressed

    private void SkalaSydney4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney4ItemStateChanged
        if(SkalaSydney4.getSelectedIndex()==0){
            NilaiSydney4.setText("0");
        }else if(SkalaSydney4.getSelectedIndex()==1){
            NilaiSydney4.setText("1");
        }else{
            NilaiSydney4.setText("2");
        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney4ItemStateChanged

    private void SkalaSydney4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney4KeyPressed
        Valid.pindah(evt,SkalaSydney3,SkalaSydney5);
    }//GEN-LAST:event_SkalaSydney4KeyPressed

    private void SkalaSydney5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_SkalaSydney5ItemStateChanged
        if(SkalaSydney5.getSelectedIndex()==0){
            NilaiSydney5.setText("0");
        }else if(SkalaSydney5.getSelectedIndex()==1){
            NilaiSydney5.setText("1");
        }else{
            NilaiSydney5.setText("2");
        }
        isTotalResikoSydney();
    }//GEN-LAST:event_SkalaSydney5ItemStateChanged

    private void SkalaSydney5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SkalaSydney5KeyPressed
//        Valid.pindah(evt,SkalaSydney4,SkalaSydney6);
    }//GEN-LAST:event_SkalaSydney5KeyPressed

    private void tbMasalahKeperawatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanMouseClicked
        if(tabModeMasalah.getRowCount()!=0){
            try {
                tampilRencana2();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanMouseClicked

    private void tbMasalahKeperawatanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyPressed
        if(tabModeMasalah.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCariMasalah.setText("");
                TCariMasalah.requestFocus();
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyPressed

    private void tbMasalahKeperawatanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMasalahKeperawatanKeyReleased
        if(tabModeMasalah.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    tampilRencana2();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbMasalahKeperawatanKeyReleased

    private void RencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaKeyPressed
        Valid.pindah2(evt,TCariMasalah,BtnSimpan);
    }//GEN-LAST:event_RencanaKeyPressed

    private void BtnTambahMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahMasalahActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterMasalahKeperawatan form=new MasterMasalahKeperawatan(null,false);
        form.isCek();
        form.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahMasalahActionPerformed

    private void BtnAllMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllMasalahActionPerformed
        TCari.setText("");
        tampilMasalah();
    }//GEN-LAST:event_BtnAllMasalahActionPerformed

    private void BtnAllMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllMasalahKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllMasalahActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCariMasalah, TCariMasalah);
        }
    }//GEN-LAST:event_BtnAllMasalahKeyPressed

    private void BtnCariMasalahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariMasalahActionPerformed
        tampilMasalah2();
    }//GEN-LAST:event_BtnCariMasalahActionPerformed

    private void BtnCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariMasalahKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilMasalah2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            Rencana.requestFocus();
        }
    }//GEN-LAST:event_BtnCariMasalahKeyPressed

    private void TCariMasalahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariMasalahKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilMasalah2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            Rencana.requestFocus();
        }
    }//GEN-LAST:event_TCariMasalahKeyPressed

    private void BtnTambahRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahRencanaActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MasterRencanaKeperawatan form=new MasterRencanaKeperawatan(null,false);
        form.isCek();
        form.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        form.setLocationRelativeTo(internalFrame1);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnTambahRencanaActionPerformed

    private void BtnAllRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllRencanaActionPerformed
        TCariRencana.setText("");
        tampilRencana();
        tampilRencana2();
    }//GEN-LAST:event_BtnAllRencanaActionPerformed

    private void BtnAllRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllRencanaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllRencanaActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCariRencana, TCariRencana);
        }
    }//GEN-LAST:event_BtnAllRencanaKeyPressed

    private void BtnCariRencanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariRencanaActionPerformed
        tampilRencana2();
    }//GEN-LAST:event_BtnCariRencanaActionPerformed

    private void BtnCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariRencanaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilRencana2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            BtnSimpan.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            TCariRencana.requestFocus();
        }
    }//GEN-LAST:event_BtnCariRencanaKeyPressed

    private void TCariRencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariRencanaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            tampilRencana2();
        }else if((evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN)||(evt.getKeyCode()==KeyEvent.VK_TAB)){
            BtnCariRencana.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            TCariMasalah.requestFocus();
        }
    }//GEN-LAST:event_TCariRencanaKeyPressed

    private void DitolongOlehKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DitolongOlehKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DitolongOlehKeyPressed

    private void BBLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BBLKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BBLKeyPressed

    private void PBLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PBLKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PBLKeyPressed

    private void RiwayatPembedahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RiwayatPembedahanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RiwayatPembedahanKeyPressed

    private void OperasiHariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OperasiHariKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_OperasiHariKeyPressed

    private void SebutAlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SebutAlergiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SebutAlergiKeyPressed

    private void RisikoGiziKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RisikoGiziKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RisikoGiziKeyPressed

    private void LPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_LPKeyPressed

    private void RetraksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RetraksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RetraksiKeyPressed

    private void SianosisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SianosisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SianosisKeyPressed

    private void HasilRespirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilRespirasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HasilRespirasiKeyPressed

    private void ccLendirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ccLendirKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ccLendirKeyPressed

    private void WheezingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WheezingKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_WheezingKeyPressed

    private void RonchiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RonchiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RonchiKeyPressed

    private void ccWSDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ccWSDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ccWSDKeyPressed

    private void WSDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WSDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_WSDKeyPressed

    private void ccSuctionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ccSuctionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ccSuctionKeyPressed

    private void SuctionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuctionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SuctionKeyPressed

    private void KeteranganRespirasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganRespirasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KeteranganRespirasiKeyPressed

    private void NadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NadiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NadiKeyPressed

    private void JenisPendarahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JenisPendarahanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JenisPendarahanKeyPressed

    private void ccPendarahanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ccPendarahanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ccPendarahanKeyPressed

    private void persenBeratBadanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_persenBeratBadanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_persenBeratBadanKeyPressed

    private void ReflexIsapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ReflexIsapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReflexIsapKeyPressed

    private void ReflexTelanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ReflexTelanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ReflexTelanKeyPressed

    private void KateterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KateterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KateterKeyPressed

    private void ProminenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProminenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProminenKeyPressed

    private void AmbigusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AmbigusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AmbigusKeyPressed

    private void FrekuensiBABKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FrekuensiBABKeyPressed
        Valid.pindah(evt,Anus,Berkunjung);
    }//GEN-LAST:event_FrekuensiBABKeyPressed

    private void SuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuKeyPressed
        Valid.pindah(evt,Feses,Hipospadia);
    }//GEN-LAST:event_SuhuKeyPressed

    private void MenyentuhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenyentuhKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenyentuhKeyPressed

    private void BerbicaraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BerbicaraKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BerbicaraKeyPressed

    private void MenggendongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenggendongKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenggendongKeyPressed

    private void YangMerawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_YangMerawatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_YangMerawatKeyPressed

    private void PerawatanDiriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerawatanDiriKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerawatanDiriKeyPressed

    private void PemantauanPemberianObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemantauanPemberianObatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PemantauanPemberianObatKeyPressed

    private void PemberianASIKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemberianASIKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PemberianASIKeyPressed

    private void PerawatanMetodeKangguruKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerawatanMetodeKangguruKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerawatanMetodeKangguruKeyPressed

    private void PerawatanLukaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PerawatanLukaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PerawatanLukaKeyPressed

    private void PendampinganTenagaKhususDiRumahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PendampinganTenagaKhususDiRumahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PendampinganTenagaKhususDiRumahKeyPressed

    private void KeteranganHasilLab1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeteranganHasilLab1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KeteranganHasilLab1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            AsesmenAwalKeperawatanNeonatusNICU dialog = new AsesmenAwalKeperawatanNeonatusNICU(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.ComboBox Abdomen;
    private widget.ComboBox AbdomenKebersihanDIri;
    private widget.ComboBox AlatBantu;
    private widget.ComboBox AlatBantuNafas;
    private widget.ComboBox Ambigus;
    private widget.ComboBox Anamnesis;
    private widget.ComboBox Anus;
    private widget.ComboBox AsalPasien;
    private widget.ComboBox BAB;
    private widget.TextBox BAK;
    private widget.TextBox BBL;
    private widget.TextBox BBLGizi;
    private widget.TextBox BBS;
    private widget.ComboBox BanyakTidur;
    private widget.ComboBox BentukDada;
    private widget.ComboBox BeratBadan;
    private widget.ComboBox Berbicara;
    private widget.ComboBox Berkunjung;
    private widget.Button BtnAll;
    private widget.Button BtnAllMasalah;
    private widget.Button BtnAllRencana;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCariMasalah;
    private widget.Button BtnCariRencana;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPetugas;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambahMasalah;
    private widget.Button BtnTambahRencana;
    private widget.ComboBox BunyiJantung;
    private widget.ComboBox CaraMinum;
    private widget.CekBox ChkAccor;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox Dehidrasi;
    private widget.TextBox DerajatSuhu;
    private widget.TextArea DetailRencana;
    private widget.TextArea DiagnosisMasuk;
    private widget.ComboBox DitolongOleh;
    private widget.TextBox DitolongOlehLainLain;
    private widget.ComboBox Ekstremitas;
    private widget.ComboBox FemoralisKanan;
    private widget.ComboBox FemoralisKiri;
    private widget.ComboBox Feses;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormMasalahRencana;
    private widget.PanelBiasa FormMenu;
    private widget.TextBox Frekuensi;
    private widget.TextBox FrekuensiBAB;
    private widget.ComboBox Gerakan;
    private widget.TextBox HariIntavena;
    private widget.ComboBox HasilLab;
    private widget.ComboBox HasilLaboratorium;
    private widget.TextBox HasilRespirasi;
    private widget.ComboBox Hipospadia;
    private widget.ComboBox Imunisasi;
    private widget.ComboBox Intavena;
    private widget.ComboBox Integritas;
    private widget.TextBox JenisBersihDiri;
    private widget.TextBox JenisPendarahan;
    private widget.ComboBox JenisPupil;
    private widget.TextBox Jk;
    private widget.TextBox Kapiler;
    private widget.ComboBox Kateter;
    private widget.TextBox KdPetugas;
    private widget.ComboBox KeadaanReproduksi;
    private widget.ComboBox KeadaanSaatIni;
    private widget.ComboBox KeadaanSekarang;
    private widget.ComboBox Kejang;
    private widget.TextArea KeluhanUtama;
    private widget.ComboBox Kepala;
    private widget.ComboBox KepalaBersihDiri;
    private widget.ComboBox Kesadaran;
    private widget.TextBox KetAnamnesis;
    private widget.TextBox KetNeurosensori;
    private widget.ComboBox KetTangisan;
    private widget.TextBox KeteranganHasilLab1;
    private widget.TextBox KeteranganRespirasi;
    private widget.TextBox KeteranganTidur;
    private widget.ComboBox KondisiBAB;
    private widget.ComboBox KondisiKebersihanDiri;
    private widget.ComboBox Kongenital;
    private widget.ComboBox KontakMata;
    private widget.TextBox Kremer;
    private widget.ComboBox KukuBersihDiri;
    private widget.Label LCount;
    private widget.TextBox LD;
    private widget.TextBox LK;
    private widget.TextBox LLA;
    private widget.TextBox LP;
    private widget.ComboBox LahirDi;
    private widget.TextBox LainLainDischargePlaning;
    private widget.TextBox LainLainRawat;
    private widget.ComboBox Lendir;
    private widget.ComboBox Lidah;
    private widget.TextBox LingkarKepala;
    private widget.TextBox Lokasi;
    private widget.TextBox LokasiBersihDiri;
    private widget.ComboBox Makanan;
    private widget.ComboBox MataBersihDiri;
    private widget.TextBox Mekonium;
    private widget.ComboBox Menggendong;
    private widget.ComboBox Menstruasi;
    private widget.ComboBox Menyentuh;
    private widget.TextBox Muntah;
    private widget.TextBox Nadi;
    private widget.TextBox NilaiResiko1;
    private widget.TextBox NilaiResiko2;
    private widget.TextBox NilaiResiko3;
    private widget.TextBox NilaiResiko4;
    private widget.TextBox NilaiResiko5;
    private widget.TextBox NilaiResiko6;
    private widget.TextBox NilaiResikoTotal;
    private widget.TextBox NilaiSydney1;
    private widget.TextBox NilaiSydney2;
    private widget.TextBox NilaiSydney3;
    private widget.TextBox NilaiSydney4;
    private widget.TextBox NilaiSydney5;
    private widget.TextBox NilaiSydneyTotal;
    private widget.TextBox NmPetugas;
    private widget.TextBox OperasiHari;
    private widget.TextBox OpnameRS;
    private widget.TextBox PBL;
    private widget.TextBox PBTB;
    private widget.PanelBiasa PanelAccor;
    private usu.widget.glass.PanelGlass PanelWall1;
    private widget.ComboBox PemantauanPemberianObat;
    private widget.ComboBox PemberianASI;
    private widget.ComboBox PendampinganTenagaKhususDiRumah;
    private widget.ComboBox Pendarahan;
    private widget.ComboBox PerawatanDiri;
    private widget.ComboBox PerawatanLuka;
    private widget.ComboBox PerawatanMetodeKangguru;
    private widget.ComboBox PernafasanSpontan;
    private widget.ComboBox PolaTidur;
    private widget.ComboBox Preputium;
    private widget.TextBox ProduksiUrine;
    private widget.ComboBox Prominen;
    private widget.ComboBox ProsesPersalinan;
    private widget.TextBox PupilKanan;
    private widget.TextBox PupilKiri;
    private javax.swing.JComboBox<String> ReflexBabinski;
    private widget.ComboBox ReflexIsap;
    private javax.swing.JComboBox<String> ReflexMoro;
    private javax.swing.JComboBox<String> ReflexPlantarGrasp;
    private widget.ComboBox ReflexTampak;
    private widget.ComboBox ReflexTelan;
    private widget.TextArea Rencana;
    private widget.TextBox Residu;
    private widget.ComboBox RespirasiJenisPernafasan;
    private widget.ComboBox Retraksi;
    private widget.ComboBox RisikoGizi;
    private widget.ComboBox RiwayatAlergi;
    private widget.TextBox RiwayatKehamilanIbu;
    private widget.TextArea RiwayatKeluhanSaatIni;
    private widget.ComboBox RiwayatKesehatanSebelumnya;
    private widget.ComboBox RiwayatPembedahan;
    private widget.TextArea RiwayatPenggunaanObat;
    private widget.ComboBox Ronchi;
    private widget.TextBox SaturasiO2;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll6;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.ScrollPane Scroll9;
    private widget.TextBox SebutAlergi;
    private widget.TextBox SebutKongenital;
    private widget.TextBox SelamaHariOpname;
    private widget.ComboBox Sesak;
    private widget.ComboBox Sianosis;
    private widget.ComboBox SkalaResiko1;
    private widget.ComboBox SkalaResiko2;
    private widget.ComboBox SkalaResiko3;
    private widget.ComboBox SkalaResiko4;
    private widget.ComboBox SkalaResiko5;
    private widget.ComboBox SkalaResiko6;
    private widget.ComboBox SkalaSydney1;
    private widget.ComboBox SkalaSydney2;
    private widget.ComboBox SkalaSydney3;
    private widget.ComboBox SkalaSydney4;
    private widget.ComboBox SkalaSydney5;
    private widget.ComboBox StatusAnak;
    private widget.ComboBox StatusGestasi;
    private widget.ComboBox Suction;
    private widget.TextBox Suhu;
    private widget.ComboBox SuhuBersihDiri;
    private widget.TextBox TCari;
    private widget.TextBox TCariMasalah;
    private widget.TextBox TCariRencana;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRM1;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPasien1;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JTabbedPane TabRencanaKeperawatan;
    private widget.ComboBox TaliPusat;
    private widget.ComboBox Tangisan;
    private widget.Tanggal TglAsuhan;
    private widget.TextBox TglLahir;
    private widget.ComboBox TurgorKulit;
    private widget.ComboBox UbunUbun;
    private widget.ComboBox Umbilical;
    private widget.TextBox Umur;
    private widget.TextBox UsiaGestasi;
    private widget.ComboBox Vagina;
    private widget.ComboBox WSD;
    private widget.TextBox WarnaBAB;
    private widget.ComboBox WarnaKulit;
    private widget.ComboBox WarnaKulitBersihDiri;
    private widget.TextBox WarnaLendir;
    private widget.ComboBox Wheezing;
    private widget.ComboBox YangMerawat;
    private widget.TextBox ccBAB;
    private widget.TextBox ccLendir;
    private widget.TextBox ccPendarahan;
    private widget.TextBox ccSuction;
    private widget.TextBox ccWSD;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel113;
    private widget.Label jLabel114;
    private widget.Label jLabel115;
    private widget.Label jLabel117;
    private widget.Label jLabel118;
    private widget.Label jLabel119;
    private widget.Label jLabel12;
    private widget.Label jLabel120;
    private widget.Label jLabel121;
    private widget.Label jLabel124;
    private widget.Label jLabel125;
    private widget.Label jLabel126;
    private widget.Label jLabel127;
    private widget.Label jLabel128;
    private widget.Label jLabel129;
    private widget.Label jLabel13;
    private widget.Label jLabel130;
    private widget.Label jLabel131;
    private widget.Label jLabel132;
    private widget.Label jLabel133;
    private widget.Label jLabel134;
    private widget.Label jLabel135;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel142;
    private widget.Label jLabel143;
    private widget.Label jLabel144;
    private widget.Label jLabel145;
    private widget.Label jLabel146;
    private widget.Label jLabel147;
    private widget.Label jLabel148;
    private widget.Label jLabel149;
    private widget.Label jLabel15;
    private widget.Label jLabel150;
    private widget.Label jLabel151;
    private widget.Label jLabel152;
    private widget.Label jLabel153;
    private widget.Label jLabel154;
    private widget.Label jLabel155;
    private widget.Label jLabel156;
    private widget.Label jLabel157;
    private widget.Label jLabel158;
    private widget.Label jLabel159;
    private widget.Label jLabel160;
    private widget.Label jLabel161;
    private widget.Label jLabel162;
    private widget.Label jLabel163;
    private widget.Label jLabel164;
    private widget.Label jLabel165;
    private widget.Label jLabel166;
    private widget.Label jLabel167;
    private widget.Label jLabel168;
    private widget.Label jLabel169;
    private widget.Label jLabel17;
    private widget.Label jLabel170;
    private widget.Label jLabel171;
    private widget.Label jLabel172;
    private widget.Label jLabel173;
    private widget.Label jLabel174;
    private widget.Label jLabel175;
    private widget.Label jLabel176;
    private widget.Label jLabel177;
    private widget.Label jLabel178;
    private widget.Label jLabel179;
    private widget.Label jLabel18;
    private widget.Label jLabel180;
    private widget.Label jLabel181;
    private widget.Label jLabel182;
    private widget.Label jLabel183;
    private widget.Label jLabel184;
    private widget.Label jLabel185;
    private widget.Label jLabel186;
    private widget.Label jLabel187;
    private widget.Label jLabel188;
    private widget.Label jLabel189;
    private widget.Label jLabel19;
    private widget.Label jLabel190;
    private widget.Label jLabel191;
    private widget.Label jLabel192;
    private widget.Label jLabel193;
    private widget.Label jLabel194;
    private widget.Label jLabel195;
    private widget.Label jLabel196;
    private widget.Label jLabel197;
    private widget.Label jLabel198;
    private widget.Label jLabel199;
    private widget.Label jLabel20;
    private widget.Label jLabel200;
    private widget.Label jLabel201;
    private widget.Label jLabel202;
    private widget.Label jLabel203;
    private widget.Label jLabel204;
    private widget.Label jLabel205;
    private widget.Label jLabel206;
    private widget.Label jLabel208;
    private widget.Label jLabel209;
    private widget.Label jLabel21;
    private widget.Label jLabel210;
    private widget.Label jLabel213;
    private widget.Label jLabel214;
    private widget.Label jLabel217;
    private widget.Label jLabel218;
    private widget.Label jLabel219;
    private widget.Label jLabel22;
    private widget.Label jLabel220;
    private widget.Label jLabel221;
    private widget.Label jLabel222;
    private widget.Label jLabel223;
    private widget.Label jLabel224;
    private widget.Label jLabel225;
    private widget.Label jLabel226;
    private widget.Label jLabel227;
    private widget.Label jLabel228;
    private widget.Label jLabel229;
    private widget.Label jLabel23;
    private widget.Label jLabel230;
    private widget.Label jLabel231;
    private widget.Label jLabel232;
    private widget.Label jLabel233;
    private widget.Label jLabel234;
    private widget.Label jLabel235;
    private widget.Label jLabel236;
    private widget.Label jLabel237;
    private widget.Label jLabel238;
    private widget.Label jLabel239;
    private widget.Label jLabel24;
    private widget.Label jLabel240;
    private widget.Label jLabel241;
    private widget.Label jLabel242;
    private widget.Label jLabel243;
    private widget.Label jLabel244;
    private widget.Label jLabel245;
    private widget.Label jLabel246;
    private widget.Label jLabel247;
    private widget.Label jLabel248;
    private widget.Label jLabel249;
    private widget.Label jLabel25;
    private widget.Label jLabel250;
    private widget.Label jLabel251;
    private widget.Label jLabel252;
    private widget.Label jLabel253;
    private widget.Label jLabel254;
    private widget.Label jLabel255;
    private widget.Label jLabel256;
    private widget.Label jLabel257;
    private widget.Label jLabel258;
    private widget.Label jLabel259;
    private widget.Label jLabel26;
    private widget.Label jLabel260;
    private widget.Label jLabel261;
    private widget.Label jLabel262;
    private widget.Label jLabel263;
    private widget.Label jLabel264;
    private widget.Label jLabel265;
    private widget.Label jLabel266;
    private widget.Label jLabel267;
    private widget.Label jLabel268;
    private widget.Label jLabel269;
    private widget.Label jLabel27;
    private widget.Label jLabel270;
    private widget.Label jLabel28;
    private widget.Label jLabel280;
    private widget.Label jLabel281;
    private widget.Label jLabel282;
    private widget.Label jLabel283;
    private widget.Label jLabel284;
    private widget.Label jLabel285;
    private widget.Label jLabel286;
    private widget.Label jLabel287;
    private widget.Label jLabel288;
    private widget.Label jLabel289;
    private widget.Label jLabel29;
    private widget.Label jLabel290;
    private widget.Label jLabel291;
    private widget.Label jLabel292;
    private widget.Label jLabel293;
    private widget.Label jLabel294;
    private widget.Label jLabel295;
    private widget.Label jLabel296;
    private widget.Label jLabel297;
    private widget.Label jLabel298;
    private widget.Label jLabel299;
    private widget.Label jLabel30;
    private widget.Label jLabel300;
    private widget.Label jLabel301;
    private widget.Label jLabel302;
    private widget.Label jLabel303;
    private widget.Label jLabel304;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private widget.Label label11;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label14;
    private widget.PanelBiasa panelBiasa1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.TextBox persenBeratBadan;
    private javax.swing.JComboBox<String> reflexpalmargrasp;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.ScrollPane scrollPane6;
    private widget.Table tbMasalahDetail;
    private widget.Table tbMasalahKeperawatan;
    private widget.Table tbObat;
    private widget.Table tbRencanaDetail;
    private widget.Table tbRencanaKeperawatan;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
                "select asesmen_awal_keperawatan_neonatus_nicu.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,DATE_FORMAT(pasien.tgl_lahir, '%d-%m-%Y') as tgl_lahir,pasien.jk,petugas.nip,petugas.nama,asesmen_awal_keperawatan_neonatus_nicu.tanggal,asesmen_awal_keperawatan_neonatus_nicu.Anamnesis,asesmen_awal_keperawatan_neonatus_nicu.KetAnamnesis,asesmen_awal_keperawatan_neonatus_nicu.AsalPasien,asesmen_awal_keperawatan_neonatus_nicu.KeluhanUtama,asesmen_awal_keperawatan_neonatus_nicu.DiagnosisMasuk,asesmen_awal_keperawatan_neonatus_nicu.RiwayatKeluhanSaatIni,asesmen_awal_keperawatan_neonatus_nicu.RiwayatPenggunaanObat,asesmen_awal_keperawatan_neonatus_nicu.RiwayatKehamilanIbu,asesmen_awal_keperawatan_neonatus_nicu.LahirDi,asesmen_awal_keperawatan_neonatus_nicu.ProsesPersalinan,asesmen_awal_keperawatan_neonatus_nicu.DitolongOleh,asesmen_awal_keperawatan_neonatus_nicu.DitolongOlehLainLain,asesmen_awal_keperawatan_neonatus_nicu.UsiaGestasi,asesmen_awal_keperawatan_neonatus_nicu.Umur,asesmen_awal_keperawatan_neonatus_nicu.BBL,asesmen_awal_keperawatan_neonatus_nicu.PBL,asesmen_awal_keperawatan_neonatus_nicu.RiwayatKesehatanSebelumnya,asesmen_awal_keperawatan_neonatus_nicu.OpnameRS,asesmen_awal_keperawatan_neonatus_nicu.SelamaHariOpname,asesmen_awal_keperawatan_neonatus_nicu.RiwayatPembedahan,asesmen_awal_keperawatan_neonatus_nicu.OperasiHari,asesmen_awal_keperawatan_neonatus_nicu.RiwayatAlergi,asesmen_awal_keperawatan_neonatus_nicu.SebutAlergi,asesmen_awal_keperawatan_neonatus_nicu.Makanan,asesmen_awal_keperawatan_neonatus_nicu.Imunisasi,asesmen_awal_keperawatan_neonatus_nicu.RisikoGizi,asesmen_awal_keperawatan_neonatus_nicu.BBLGizi,asesmen_awal_keperawatan_neonatus_nicu.BBS,asesmen_awal_keperawatan_neonatus_nicu.PBTB,asesmen_awal_keperawatan_neonatus_nicu.LLA,asesmen_awal_keperawatan_neonatus_nicu.LK,asesmen_awal_keperawatan_neonatus_nicu.LD,asesmen_awal_keperawatan_neonatus_nicu.LP,asesmen_awal_keperawatan_neonatus_nicu.BentukDada,asesmen_awal_keperawatan_neonatus_nicu.PernafasanSpontan,asesmen_awal_keperawatan_neonatus_nicu.Frekuensi,asesmen_awal_keperawatan_neonatus_nicu.SaturasiO2,asesmen_awal_keperawatan_neonatus_nicu.Sesak,asesmen_awal_keperawatan_neonatus_nicu.Retraksi,asesmen_awal_keperawatan_neonatus_nicu.Sianosis,asesmen_awal_keperawatan_neonatus_nicu.WarnaLendir,asesmen_awal_keperawatan_neonatus_nicu.ccLendir,asesmen_awal_keperawatan_neonatus_nicu.Wheezing,asesmen_awal_keperawatan_neonatus_nicu.Ronchi,asesmen_awal_keperawatan_neonatus_nicu.WSD,asesmen_awal_keperawatan_neonatus_nicu.ccWSD,asesmen_awal_keperawatan_neonatus_nicu.Suction,asesmen_awal_keperawatan_neonatus_nicu.ccSuction,asesmen_awal_keperawatan_neonatus_nicu.AlatBantuNafas,asesmen_awal_keperawatan_neonatus_nicu.RespirasiJenisPernafasan,asesmen_awal_keperawatan_neonatus_nicu.HasilRespirasi,asesmen_awal_keperawatan_neonatus_nicu.KeteranganRespirasi,asesmen_awal_keperawatan_neonatus_nicu.BunyiJantung,asesmen_awal_keperawatan_neonatus_nicu.Pendarahan,asesmen_awal_keperawatan_neonatus_nicu.JenisPendarahan,asesmen_awal_keperawatan_neonatus_nicu.ccPendarahan,asesmen_awal_keperawatan_neonatus_nicu.FemoralisKanan,asesmen_awal_keperawatan_neonatus_nicu.FemoralisKiri,asesmen_awal_keperawatan_neonatus_nicu.KeadaanSaatIni,asesmen_awal_keperawatan_neonatus_nicu.Nadi,asesmen_awal_keperawatan_neonatus_nicu.Ekstremitas,asesmen_awal_keperawatan_neonatus_nicu.Kapiler,asesmen_awal_keperawatan_neonatus_nicu.HasilLab,asesmen_awal_keperawatan_neonatus_nicu.Intavena,asesmen_awal_keperawatan_neonatus_nicu.HariIntavena,asesmen_awal_keperawatan_neonatus_nicu.BeratBadan,asesmen_awal_keperawatan_neonatus_nicu.persenBeratBadan,asesmen_awal_keperawatan_neonatus_nicu.Lendir,asesmen_awal_keperawatan_neonatus_nicu.CaraMinum,asesmen_awal_keperawatan_neonatus_nicu.ReflexIsap,asesmen_awal_keperawatan_neonatus_nicu.WarnaKulit,asesmen_awal_keperawatan_neonatus_nicu.Lidah,asesmen_awal_keperawatan_neonatus_nicu.ReflexTelan,asesmen_awal_keperawatan_neonatus_nicu.Abdomen,asesmen_awal_keperawatan_neonatus_nicu.KeadaanSekarang,asesmen_awal_keperawatan_neonatus_nicu.TurgorKulit,asesmen_awal_keperawatan_neonatus_nicu.UbunUbun,asesmen_awal_keperawatan_neonatus_nicu.Muntah,asesmen_awal_keperawatan_neonatus_nicu.HasilLaboratorium,asesmen_awal_keperawatan_neonatus_nicu.KeteranganHasilLab,asesmen_awal_keperawatan_neonatus_nicu.Residu,asesmen_awal_keperawatan_neonatus_nicu.Kesadaran,asesmen_awal_keperawatan_neonatus_nicu.Kepala,asesmen_awal_keperawatan_neonatus_nicu.ReflexTampak,asesmen_awal_keperawatan_neonatus_nicu.Tangisan,asesmen_awal_keperawatan_neonatus_nicu.KetTangisan,asesmen_awal_keperawatan_neonatus_nicu.LingkarKepala,asesmen_awal_keperawatan_neonatus_nicu.Kejang,asesmen_awal_keperawatan_neonatus_nicu.PupilKiri,asesmen_awal_keperawatan_neonatus_nicu.PupilKanan,asesmen_awal_keperawatan_neonatus_nicu.JenisPupil,asesmen_awal_keperawatan_neonatus_nicu.Gerakan,asesmen_awal_keperawatan_neonatus_nicu.KetNeurosensori,asesmen_awal_keperawatan_neonatus_nicu.BAK,asesmen_awal_keperawatan_neonatus_nicu.ProduksiUrine,asesmen_awal_keperawatan_neonatus_nicu.KeadaanReproduksi,asesmen_awal_keperawatan_neonatus_nicu.AlatBantu,asesmen_awal_keperawatan_neonatus_nicu.Vagina,asesmen_awal_keperawatan_neonatus_nicu.Menstruasi,asesmen_awal_keperawatan_neonatus_nicu.Kateter,asesmen_awal_keperawatan_neonatus_nicu.Prominen,asesmen_awal_keperawatan_neonatus_nicu.Ambigus,asesmen_awal_keperawatan_neonatus_nicu.Preputium,asesmen_awal_keperawatan_neonatus_nicu.Hipospadia,asesmen_awal_keperawatan_neonatus_nicu.BAB,asesmen_awal_keperawatan_neonatus_nicu.ccBAB,asesmen_awal_keperawatan_neonatus_nicu.Feses,asesmen_awal_keperawatan_neonatus_nicu.Anus,asesmen_awal_keperawatan_neonatus_nicu.WarnaBAB,asesmen_awal_keperawatan_neonatus_nicu.KondisiBAB,asesmen_awal_keperawatan_neonatus_nicu.FrekuensiBAB,asesmen_awal_keperawatan_neonatus_nicu.Mekonium,asesmen_awal_keperawatan_neonatus_nicu.Suhu,asesmen_awal_keperawatan_neonatus_nicu.Dehidrasi,asesmen_awal_keperawatan_neonatus_nicu.Integritas,asesmen_awal_keperawatan_neonatus_nicu.KondisiKebersihanDiri,asesmen_awal_keperawatan_neonatus_nicu.WarnaKulitBersihDiri,asesmen_awal_keperawatan_neonatus_nicu.SuhuBersihDiri,asesmen_awal_keperawatan_neonatus_nicu.DerajatSuhu,asesmen_awal_keperawatan_neonatus_nicu.KepalaBersihDiri,asesmen_awal_keperawatan_neonatus_nicu.Umbilical,asesmen_awal_keperawatan_neonatus_nicu.KukuBersihDiri,asesmen_awal_keperawatan_neonatus_nicu.MataBersihDiri,asesmen_awal_keperawatan_neonatus_nicu.TaliPusat,asesmen_awal_keperawatan_neonatus_nicu.AbdomenKebersihanDIri,asesmen_awal_keperawatan_neonatus_nicu.JenisBersihDiri,asesmen_awal_keperawatan_neonatus_nicu.LokasiBersihDiri,asesmen_awal_keperawatan_neonatus_nicu.BanyakTidur,asesmen_awal_keperawatan_neonatus_nicu.KeteranganTidur,asesmen_awal_keperawatan_neonatus_nicu.PolaTidur,asesmen_awal_keperawatan_neonatus_nicu.StatusAnak,asesmen_awal_keperawatan_neonatus_nicu.Kongenital,asesmen_awal_keperawatan_neonatus_nicu.Lokasi,asesmen_awal_keperawatan_neonatus_nicu.SebutKongenital,asesmen_awal_keperawatan_neonatus_nicu.StatusGestasi,asesmen_awal_keperawatan_neonatus_nicu.Berkunjung,asesmen_awal_keperawatan_neonatus_nicu.KontakMata,asesmen_awal_keperawatan_neonatus_nicu.Menyentuh,asesmen_awal_keperawatan_neonatus_nicu.Berbicara,asesmen_awal_keperawatan_neonatus_nicu.Menggendong,asesmen_awal_keperawatan_neonatus_nicu.YangMerawat,asesmen_awal_keperawatan_neonatus_nicu.LainLainRawat,asesmen_awal_keperawatan_neonatus_nicu.RencanaPulang,asesmen_awal_keperawatan_neonatus_nicu.LainLainDischargePlaning,asesmen_awal_keperawatan_neonatus_nicu.SkalaResiko1,asesmen_awal_keperawatan_neonatus_nicu.NilaiResiko1,asesmen_awal_keperawatan_neonatus_nicu.SkalaResiko2,asesmen_awal_keperawatan_neonatus_nicu.NilaiResiko2,asesmen_awal_keperawatan_neonatus_nicu.SkalaResiko3,asesmen_awal_keperawatan_neonatus_nicu.NilaiResiko3,asesmen_awal_keperawatan_neonatus_nicu.SkalaResiko4,asesmen_awal_keperawatan_neonatus_nicu.NilaiResiko4,asesmen_awal_keperawatan_neonatus_nicu.SkalaResiko5,asesmen_awal_keperawatan_neonatus_nicu.NilaiResiko5,asesmen_awal_keperawatan_neonatus_nicu.SkalaResiko6,asesmen_awal_keperawatan_neonatus_nicu.NilaiResiko6,asesmen_awal_keperawatan_neonatus_nicu.NilaiResikoTotal,asesmen_awal_keperawatan_neonatus_nicu.SkalaSydney1,asesmen_awal_keperawatan_neonatus_nicu.NilaiSydney1,asesmen_awal_keperawatan_neonatus_nicu.SkalaSydney2,asesmen_awal_keperawatan_neonatus_nicu.NilaiSydney2,asesmen_awal_keperawatan_neonatus_nicu.SkalaSydney3,asesmen_awal_keperawatan_neonatus_nicu.NilaiSydney3,asesmen_awal_keperawatan_neonatus_nicu.SkalaSydney4,asesmen_awal_keperawatan_neonatus_nicu.NilaiSydney4,asesmen_awal_keperawatan_neonatus_nicu.SkalaSydney5,asesmen_awal_keperawatan_neonatus_nicu.NilaiSydney5,asesmen_awal_keperawatan_neonatus_nicu.NilaiSydneyTotal,asesmen_awal_keperawatan_neonatus_nicu.Rencana,asesmen_awal_keperawatan_neonatus_nicu.nip as KdPetugas,asesmen_awal_keperawatan_neonatus_nicu.reflexpalmargrasp, " +
                        "asesmen_awal_keperawatan_neonatus_nicu.ReflexBabinski,asesmen_awal_keperawatan_neonatus_nicu.ReflexPlantarGrasp,asesmen_awal_keperawatan_neonatus_nicu.ReflexMoro,asesmen_awal_keperawatan_neonatus_nicu.PerawatanDiri,asesmen_awal_keperawatan_neonatus_nicu.PemantauanPemberianObat,asesmen_awal_keperawatan_neonatus_nicu.PemberianASI,asesmen_awal_keperawatan_neonatus_nicu.PerawatanMetodeKangguru,asesmen_awal_keperawatan_neonatus_nicu.PerawatanLuka,asesmen_awal_keperawatan_neonatus_nicu.PendampinganTenagaKhususDiRumah,asesmen_awal_keperawatan_neonatus_nicu.Kremer "+
                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                "inner join asesmen_awal_keperawatan_neonatus_nicu on reg_periksa.no_rawat=asesmen_awal_keperawatan_neonatus_nicu.no_rawat "+
                "inner join petugas on asesmen_awal_keperawatan_neonatus_nicu.nip=petugas.nip "+
                "where asesmen_awal_keperawatan_neonatus_nicu.tanggal between ? and ? "+
                (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or pasien.nm_pasien like ? or asesmen_awal_keperawatan_neonatus_nicu.nip like ? or "+
                "petugas.nama like ?)")+
                "order by asesmen_awal_keperawatan_neonatus_nicu.tanggal");
            
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                if(!TCari.getText().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
//                    ps.setString(8,"%"+TCari.getText()+"%");
//                    ps.setString(9,"%"+TCari.getText()+"%");
                }   
                rs=ps.executeQuery();
                while(rs.next()){
                    tabMode.addRow(new String[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("tgl_lahir"),rs.getString("jk"),rs.getString("nip"),rs.getString("nama"),rs.getString("tanggal"),rs.getString("Anamnesis"),rs.getString("KetAnamnesis"),rs.getString("AsalPasien"),rs.getString("KeluhanUtama"),rs.getString("DiagnosisMasuk"),rs.getString("RiwayatKeluhanSaatIni"),rs.getString("RiwayatPenggunaanObat"),rs.getString("RiwayatKehamilanIbu"),rs.getString("LahirDi"),rs.getString("ProsesPersalinan"),rs.getString("DitolongOleh"),rs.getString("DitolongOlehLainLain"),rs.getString("UsiaGestasi"),rs.getString("Umur"),rs.getString("BBL"),rs.getString("PBL"),rs.getString("RiwayatKesehatanSebelumnya"),rs.getString("OpnameRS"),rs.getString("SelamaHariOpname"),rs.getString("RiwayatPembedahan"),rs.getString("OperasiHari"),rs.getString("RiwayatAlergi"),rs.getString("SebutAlergi"),rs.getString("Makanan"),rs.getString("Imunisasi"),rs.getString("RisikoGizi"),rs.getString("BBLGizi"),rs.getString("BBS"),rs.getString("PBTB"),rs.getString("LLA"),rs.getString("LK"),rs.getString("LD"),rs.getString("LP"),rs.getString("BentukDada"),rs.getString("PernafasanSpontan"),rs.getString("Frekuensi"),rs.getString("SaturasiO2"),rs.getString("Sesak"),rs.getString("Retraksi"),rs.getString("Sianosis"),rs.getString("WarnaLendir"),rs.getString("ccLendir"),rs.getString("Wheezing"),rs.getString("Ronchi"),rs.getString("WSD"),rs.getString("ccWSD"),rs.getString("Suction"),rs.getString("ccSuction"),rs.getString("AlatBantuNafas"),rs.getString("RespirasiJenisPernafasan"),rs.getString("HasilRespirasi"),rs.getString("KeteranganRespirasi"),rs.getString("BunyiJantung"),rs.getString("Pendarahan"),rs.getString("JenisPendarahan"),rs.getString("ccPendarahan"),rs.getString("FemoralisKanan"),rs.getString("FemoralisKiri"),rs.getString("KeadaanSaatIni"),rs.getString("Nadi"),rs.getString("Ekstremitas"),rs.getString("Kapiler"),rs.getString("HasilLab"),rs.getString("Intavena"),rs.getString("HariIntavena"),rs.getString("BeratBadan"),rs.getString("persenBeratBadan"),rs.getString("Lendir"),rs.getString("CaraMinum"),rs.getString("ReflexIsap"),rs.getString("WarnaKulit"),rs.getString("Lidah"),rs.getString("ReflexTelan"),rs.getString("Abdomen"),rs.getString("KeadaanSekarang"),rs.getString("TurgorKulit"),rs.getString("UbunUbun"),rs.getString("Muntah"),rs.getString("HasilLaboratorium"),rs.getString("KeteranganHasilLab"),rs.getString("Residu"),rs.getString("Kesadaran"),rs.getString("Kepala"),rs.getString("ReflexTampak"),rs.getString("Tangisan"),rs.getString("KetTangisan"),rs.getString("LingkarKepala"),rs.getString("Kejang"),rs.getString("PupilKiri"),rs.getString("PupilKanan"),rs.getString("JenisPupil"),rs.getString("Gerakan"),rs.getString("KetNeurosensori"),rs.getString("BAK"),rs.getString("ProduksiUrine"),rs.getString("KeadaanReproduksi"),rs.getString("AlatBantu"),rs.getString("Vagina"),rs.getString("Menstruasi"),rs.getString("Kateter"),rs.getString("Prominen"),rs.getString("Ambigus"),rs.getString("Preputium"),rs.getString("Hipospadia"),rs.getString("BAB"),rs.getString("ccBAB"),rs.getString("Feses"),rs.getString("Anus"),rs.getString("WarnaBAB"),rs.getString("KondisiBAB"),rs.getString("FrekuensiBAB"),rs.getString("Mekonium"),rs.getString("Suhu"),rs.getString("Dehidrasi"),rs.getString("Integritas"),rs.getString("KondisiKebersihanDiri"),rs.getString("WarnaKulitBersihDiri"),rs.getString("SuhuBersihDiri"),rs.getString("DerajatSuhu"),rs.getString("KepalaBersihDiri"),rs.getString("Umbilical"),rs.getString("KukuBersihDiri"),rs.getString("MataBersihDiri"),rs.getString("TaliPusat"),rs.getString("AbdomenKebersihanDIri"),rs.getString("JenisBersihDiri"),rs.getString("LokasiBersihDiri"),rs.getString("BanyakTidur"),rs.getString("KeteranganTidur"),rs.getString("PolaTidur"),rs.getString("StatusAnak"),rs.getString("Kongenital"),rs.getString("Lokasi"),rs.getString("SebutKongenital"),rs.getString("StatusGestasi"),rs.getString("Berkunjung"),rs.getString("KontakMata"),rs.getString("Menyentuh"),rs.getString("Berbicara"),rs.getString("Menggendong"),rs.getString("YangMerawat"),rs.getString("LainLainRawat"),rs.getString("RencanaPulang"),rs.getString("LainLainDischargePlaning"),rs.getString("SkalaResiko1"),rs.getString("NilaiResiko1"),rs.getString("SkalaResiko2"),rs.getString("NilaiResiko2"),rs.getString("SkalaResiko3"),rs.getString("NilaiResiko3"),rs.getString("SkalaResiko4"),rs.getString("NilaiResiko4"),rs.getString("SkalaResiko5"),rs.getString("NilaiResiko5"),rs.getString("SkalaResiko6"),rs.getString("NilaiResiko6"),rs.getString("NilaiResikoTotal"),rs.getString("SkalaSydney1"),rs.getString("NilaiSydney1"),rs.getString("SkalaSydney2"),rs.getString("NilaiSydney2"),rs.getString("SkalaSydney3"),rs.getString("NilaiSydney3"),rs.getString("SkalaSydney4"),rs.getString("NilaiSydney4"),rs.getString("SkalaSydney5"),rs.getString("NilaiSydney5"),rs.getString("NilaiSydneyTotal"),rs.getString("Rencana"),rs.getString("reflexpalmargrasp"),rs.getString("ReflexBabinski"),rs.getString("ReflexPlantarGrasp"),rs.getString("ReflexMoro"),rs.getString("PerawatanDiri"),rs.getString("PemantauanPemberianObat"),rs.getString("PemberianASI"),rs.getString("PerawatanMetodeKangguru"),rs.getString("PerawatanLuka"),rs.getString("PendampinganTenagaKhususDiRumah"),rs.getString("Kremer")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        TglAsuhan.setDate(new Date());
        Anamnesis.setSelectedIndex(0);
        KetAnamnesis.setText("");
        
        AsalPasien.setSelectedIndex(0);
        
        KeluhanUtama.setText("");
        DiagnosisMasuk.setText("");
        RiwayatKeluhanSaatIni.setText("");
        RiwayatPenggunaanObat.setText("");
        RiwayatKehamilanIbu.setText("");
        DitolongOlehLainLain.setText("");
        LahirDi.setSelectedIndex(0);
        ProsesPersalinan.setSelectedIndex(0);
        
        RiwayatKesehatanSebelumnya.setSelectedIndex(0);
        OpnameRS.setText("");
        RiwayatAlergi.setSelectedIndex(0);
        SelamaHariOpname.setText("");
        Makanan.setSelectedIndex(0);
        Imunisasi.setSelectedIndex(0);
        UsiaGestasi.setText("");
        
        Umur.setText("");
        LD.setText("");
        Nadi.setText("");
        BBLGizi.setText("");
        BBS.setText("");
        PBTB.setText("");
        LLA.setText("");
        LK.setText("");
        BentukDada.setSelectedIndex(0);
        SaturasiO2.setText("");
        PernafasanSpontan.setSelectedIndex(0);
        Frekuensi.setText("");
        
        Sesak.setSelectedIndex(0);
        AlatBantuNafas.setSelectedIndex(0);
        Ekstremitas.setSelectedIndex(0);
        Pendarahan.setSelectedIndex(0);
        BunyiJantung.setSelectedIndex(0);
        RespirasiJenisPernafasan.setSelectedIndex(0);
        WarnaLendir.setText("");
        KeadaanSaatIni.setSelectedIndex(0);
        FemoralisKanan.setSelectedIndex(0);
        Kapiler.setText("");
        FemoralisKiri.setSelectedIndex(0);
        
        BeratBadan.setSelectedIndex(0);
        
        HasilLab.setSelectedIndex(0);
        
        Intavena.setSelectedIndex(0);
        HariIntavena.setText("");
        KeadaanSekarang.setSelectedIndex(0);
        CaraMinum.setSelectedIndex(0);
        TurgorKulit.setSelectedIndex(0);
        Abdomen.setSelectedIndex(0);
        Muntah.setText("");
        Lidah.setSelectedIndex(0);
        UbunUbun.setSelectedIndex(0);
        HasilLaboratorium.setSelectedIndex(0);
        WarnaKulit.setSelectedIndex(0);
        Residu.setText("");
        Lendir.setSelectedIndex(0);
        
        Kesadaran.setSelectedIndex(0);
        Kremer.setText("");
        KetTangisan.setSelectedIndex(0);
        JenisPupil.setSelectedIndex(0);
        Tangisan.setSelectedIndex(0);
        ProduksiUrine.setText("");
        Kepala.setSelectedIndex(0);
        
        PupilKiri.setText("");
        PupilKanan.setText("");
        LingkarKepala.setText("");
        
        BAK.setText("");
        
        KetNeurosensori.setText("");
        
        Gerakan.setSelectedIndex(0);
        ReflexTampak.setSelectedIndex(0);
        KeadaanReproduksi.setSelectedIndex(0);
        Kejang.setSelectedIndex(0);
        Vagina.setSelectedIndex(0);
        
        AlatBantu.setSelectedIndex(0);
        Menstruasi.setSelectedIndex(0);
        BAB.setSelectedIndex(0);
        WarnaBAB.setText("");
        Preputium.setSelectedIndex(0);
        KondisiBAB.setSelectedIndex(0);
        Feses.setSelectedIndex(0);
        Suhu.setText("");
        Hipospadia.setSelectedIndex(0);
        ccBAB.setText("");
        Anus.setSelectedIndex(0);
        FrekuensiBAB.setText("");
        Berkunjung.setSelectedIndex(0);
        Mekonium.setText("");
        Kongenital.setSelectedIndex(0);
        KondisiKebersihanDiri.setSelectedIndex(0);
        Integritas.setSelectedIndex(0);
        Dehidrasi.setText("");
        KukuBersihDiri.setSelectedIndex(0);
        TaliPusat.setSelectedIndex(0);
        WarnaKulitBersihDiri.setSelectedIndex(0);
        DerajatSuhu.setText("");
        SuhuBersihDiri.setSelectedIndex(0);
        KeteranganTidur.setText("");
        KepalaBersihDiri.setSelectedIndex(0);
        MataBersihDiri.setSelectedIndex(0);
        
        Umbilical.setSelectedIndex(0);
        AbdomenKebersihanDIri.setSelectedIndex(0);
        
        PolaTidur.setSelectedIndex(0);
        
        Lokasi.setText("");
        KontakMata.setSelectedIndex(0);
        StatusGestasi.setSelectedIndex(0);
        
        BanyakTidur.setSelectedIndex(0);
        
        StatusAnak.setSelectedIndex(0);
        
        SkalaResiko1.setSelectedIndex(0);
        NilaiResiko1.setText("0");
        SkalaResiko2.setSelectedIndex(0);
        NilaiResiko2.setText("0");
        SkalaResiko3.setSelectedIndex(0);
        NilaiResiko3.setText("0");
        SkalaResiko4.setSelectedIndex(0);
        NilaiResiko4.setText("0");
        SkalaResiko5.setSelectedIndex(0);
        NilaiResiko5.setText("0");
        SkalaResiko6.setSelectedIndex(0);
        NilaiResiko6.setText("0");
        NilaiResikoTotal.setText("0");
//        TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (0-24), Tindakan : Intervensi pencegahan risiko jatuh standar");
        SkalaSydney1.setSelectedIndex(0);
        NilaiSydney1.setText("0");
        SkalaSydney2.setSelectedIndex(0);
        NilaiSydney2.setText("0");
        SkalaSydney3.setSelectedIndex(0);
        NilaiSydney3.setText("0");
        SkalaSydney4.setSelectedIndex(0);
        NilaiSydney4.setText("0");
        SkalaSydney5.setSelectedIndex(0);
        NilaiSydney5.setText("0");
        
        NilaiSydneyTotal.setText("0");
//        TingkatSydney.setText("Tingkat Resiko : Risiko Rendah (1-3), Tindakan : Intervensi pencegahan risiko jatuh standar");
        
        Rencana.setText("");
        for (i = 0; i < tabModeMasalah.getRowCount(); i++) {
            tabModeMasalah.setValueAt(false,i,0);
        }
        Valid.tabelKosong(tabModeRencana);
        TabRawat.setSelectedIndex(0);
//        MacamKasus.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()); 
            TglLahir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
            Jk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            KdPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            NmPetugas.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());
            Valid.SetTgl2(TglAsuhan,tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());
            
            Anamnesis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            KetAnamnesis.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());
            AsalPasien.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());
            KeluhanUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
            DiagnosisMasuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            RiwayatKeluhanSaatIni.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            RiwayatPenggunaanObat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            RiwayatKehamilanIbu.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            LahirDi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            ProsesPersalinan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            DitolongOleh.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());
            DitolongOlehLainLain.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
            UsiaGestasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            Umur.setText(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());
            BBL.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());
            PBL.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());
            RiwayatKesehatanSebelumnya.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());
            OpnameRS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());
            SelamaHariOpname.setText(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());
            RiwayatPembedahan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());
            OperasiHari.setText(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());
            RiwayatAlergi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString());
            SebutAlergi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());
            Makanan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString());
            Imunisasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString());
            RisikoGizi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),33).toString());
            BBLGizi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());
            BBS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());
            PBTB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),36).toString());
            LLA.setText(tbObat.getValueAt(tbObat.getSelectedRow(),37).toString());
            LK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),38).toString());
            LD.setText(tbObat.getValueAt(tbObat.getSelectedRow(),39).toString());
            LP.setText(tbObat.getValueAt(tbObat.getSelectedRow(),40).toString());
            BentukDada.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),41).toString());
            PernafasanSpontan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),42).toString());
            Frekuensi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),43).toString());
            SaturasiO2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),44).toString());
            Sesak.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),45).toString());
            Retraksi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),46).toString());
            Sianosis.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),47).toString());
            WarnaLendir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),48).toString());
            ccLendir.setText(tbObat.getValueAt(tbObat.getSelectedRow(),49).toString());
            Wheezing.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),50).toString());
            Ronchi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),51).toString());
            WSD.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),52).toString());
            ccWSD.setText(tbObat.getValueAt(tbObat.getSelectedRow(),53).toString());
            Suction.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),54).toString());
            ccSuction.setText(tbObat.getValueAt(tbObat.getSelectedRow(),55).toString());
            AlatBantuNafas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),56).toString());
            RespirasiJenisPernafasan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),57).toString());
            HasilRespirasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),58).toString());
            KeteranganRespirasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),59).toString());
            BunyiJantung.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),60).toString());
            Pendarahan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),61).toString());
            JenisPendarahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),62).toString());
            ccPendarahan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),63).toString());
            FemoralisKanan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),64).toString());
            FemoralisKiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),65).toString());
            KeadaanSaatIni.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),66).toString());
            Nadi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),67).toString());
            Ekstremitas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),68).toString());
            Kapiler.setText(tbObat.getValueAt(tbObat.getSelectedRow(),69).toString());
            HasilLab.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),70).toString());
            Intavena.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),71).toString());
            HariIntavena.setText(tbObat.getValueAt(tbObat.getSelectedRow(),72).toString());
            BeratBadan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),73).toString());
            persenBeratBadan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),74).toString());
            Lendir.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),75).toString());
            CaraMinum.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),76).toString());
            ReflexIsap.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),77).toString());
            WarnaKulit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),78).toString());
            Lidah.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),79).toString());
            ReflexTelan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),80).toString());
            Abdomen.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),81).toString());
            KeadaanSekarang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),82).toString());
            TurgorKulit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),83).toString());
            UbunUbun.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),84).toString());
            Muntah.setText(tbObat.getValueAt(tbObat.getSelectedRow(),85).toString());
            HasilLaboratorium.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),86).toString());
            Kremer.setText(tbObat.getValueAt(tbObat.getSelectedRow(),87).toString());
            Residu.setText(tbObat.getValueAt(tbObat.getSelectedRow(),88).toString());
            Kesadaran.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),89).toString());
            Kepala.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),90).toString());
            ReflexTampak.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),91).toString());
            ReflexMoro.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),177).toString());
            Tangisan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),92).toString());
            KetTangisan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),93).toString());
            LingkarKepala.setText(tbObat.getValueAt(tbObat.getSelectedRow(),94).toString());
            Kejang.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),95).toString());
            PupilKiri.setText(tbObat.getValueAt(tbObat.getSelectedRow(),96).toString());
            PupilKanan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),97).toString());
            JenisPupil.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),98).toString());
            Gerakan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),99).toString());
            KetNeurosensori.setText(tbObat.getValueAt(tbObat.getSelectedRow(),100).toString());
            BAK.setText(tbObat.getValueAt(tbObat.getSelectedRow(),101).toString());
            ProduksiUrine.setText(tbObat.getValueAt(tbObat.getSelectedRow(),102).toString());
            KeadaanReproduksi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),103).toString());
            AlatBantu.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),104).toString());
            Vagina.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),105).toString());
            Menstruasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),106).toString());
            Kateter.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),107).toString());
            Prominen.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),108).toString());
            Ambigus.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),109).toString());
            Preputium.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),110).toString());
            Hipospadia.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),111).toString());
            BAB.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),112).toString());
            ccBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),113).toString());
            Feses.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),114).toString());
            Anus.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),115).toString());
            WarnaBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),116).toString());
            KondisiBAB.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),117).toString());
            FrekuensiBAB.setText(tbObat.getValueAt(tbObat.getSelectedRow(),118).toString());
            Mekonium.setText(tbObat.getValueAt(tbObat.getSelectedRow(),119).toString());
            Suhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(),120).toString());
            Dehidrasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),121).toString());
            Integritas.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),122).toString());
            KondisiKebersihanDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),123).toString());
            WarnaKulitBersihDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),124).toString());
            SuhuBersihDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),125).toString());
            DerajatSuhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(),126).toString());
            KepalaBersihDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),127).toString());
            Umbilical.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),128).toString());
            KukuBersihDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),129).toString());
            MataBersihDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),130).toString());
            TaliPusat.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),131).toString());
            AbdomenKebersihanDIri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),132).toString());
            JenisBersihDiri.setText(tbObat.getValueAt(tbObat.getSelectedRow(),133).toString());
            LokasiBersihDiri.setText(tbObat.getValueAt(tbObat.getSelectedRow(),134).toString());
            BanyakTidur.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),135).toString());
            KeteranganTidur.setText(tbObat.getValueAt(tbObat.getSelectedRow(),136).toString());
            PolaTidur.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),137).toString());
            StatusAnak.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),138).toString());
            Kongenital.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),139).toString());
            Lokasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),140).toString());
            SebutKongenital.setText(tbObat.getValueAt(tbObat.getSelectedRow(),141).toString());
            StatusGestasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),142).toString());
            Berkunjung.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),143).toString());
            KontakMata.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),144).toString());
            Menyentuh.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),145).toString());
            Berbicara.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),146).toString());
            Menggendong.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),147).toString());
            YangMerawat.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),148).toString());
            LainLainRawat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),149).toString());
            PerawatanDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),150).toString());
            LainLainDischargePlaning.setText(tbObat.getValueAt(tbObat.getSelectedRow(),151).toString());
            SkalaResiko1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),152).toString());
            NilaiResiko1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),153).toString());
            SkalaResiko2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),154).toString());
            NilaiResiko2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),155).toString());
            SkalaResiko3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),156).toString());
            NilaiResiko3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),157).toString());
            SkalaResiko4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),158).toString());
            NilaiResiko4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),159).toString());
            SkalaResiko5.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),160).toString());
            NilaiResiko5.setText(tbObat.getValueAt(tbObat.getSelectedRow(),161).toString());
            SkalaResiko6.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),162).toString());
            NilaiResiko6.setText(tbObat.getValueAt(tbObat.getSelectedRow(),163).toString());
            NilaiResikoTotal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),164).toString());
            SkalaSydney1.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),165).toString());
            NilaiSydney1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),166).toString());
            SkalaSydney2.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),167).toString());
            NilaiSydney2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),168).toString());
            SkalaSydney3.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),169).toString());
            NilaiSydney3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),170).toString());
            SkalaSydney4.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),171).toString());
            NilaiSydney4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),172).toString());
            SkalaSydney5.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),173).toString());
            NilaiSydney5.setText(tbObat.getValueAt(tbObat.getSelectedRow(),174).toString());
            NilaiSydneyTotal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),175).toString());
            Rencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(),176).toString());
            reflexpalmargrasp.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),177).toString());
            ReflexBabinski.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),178).toString());
            ReflexPlantarGrasp.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),179).toString());
            ReflexMoro.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),180).toString());
            PerawatanDiri.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),181).toString());
            PemantauanPemberianObat.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),182).toString());
            PemberianASI.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),183).toString());
            PerawatanMetodeKangguru.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),184).toString());
            PerawatanLuka.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),185).toString());
            PendampinganTenagaKhususDiRumah.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),186).toString());
            Kremer.setText(tbObat.getValueAt(tbObat.getSelectedRow(),187).toString());
            try {
                Valid.tabelKosong(tabModeMasalah);
                
                ps=koneksi.prepareStatement(
                        "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_masalah on penilaian_awal_keperawatan_ranap_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "+
                        "where penilaian_awal_keperawatan_ranap_masalah.no_rawat=? order by penilaian_awal_keperawatan_ranap_masalah.kode_masalah");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeMasalah.addRow(new Object[]{true,rs.getString(1),rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
            
            try {
                Valid.tabelKosong(tabModeRencana);
                
                ps=koneksi.prepareStatement(
                        "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_rencana on penilaian_awal_keperawatan_ranap_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "+
                        "where penilaian_awal_keperawatan_ranap_rencana.no_rawat=? order by penilaian_awal_keperawatan_ranap_rencana.kode_rencana");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeRencana.addRow(new Object[]{true,rs.getString(1),rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
            
            Valid.SetTgl2(TglAsuhan,tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());
        }
    }

    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select pasien.nm_pasien, if(pasien.jk='L','Laki-Laki','Perempuan') as jk,pasien.tgl_lahir,pasien.agama,"+
                    "bahasa_pasien.nama_bahasa,pasien.pnd,pasien.pekerjaan "+
                    "from pasien inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "where pasien.no_rkm_medis=?");
            try {
                ps.setString(1,TNoRM.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    TPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    TglLahir.setText(rs.getString("tgl_lahir"));
//                    LainLainRawat.setText(rs.getString("agama"));
//                    JenisBersihDiri.setText(rs.getString("nama_bahasa"));
//                    SebutKongenital.setText(rs.getString("pnd"));
                    
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
    
    public void setNoRm(String norwt, Date tgl2,String carabayar,String norm) {
        TNoRw.setText(norwt);
        TNoRM.setText(norm);
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
//        LokasiBersihDiri.setText(carabayar);
        DTPCari2.setDate(tgl2);    
        isRawat(); 
    }
    
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getpenilaian_awal_keperawatan_ranap());
        BtnHapus.setEnabled(akses.getpenilaian_awal_keperawatan_ranap());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_ranap());
        BtnEdit.setEnabled(akses.getpenilaian_awal_keperawatan_ranap()); 
        BtnTambahMasalah.setEnabled(akses.getmaster_masalah_keperawatan()); 
        BtnTambahRencana.setEnabled(akses.getmaster_rencana_keperawatan()); 
        if(akses.getjml2()>=1){
            KdPetugas.setEditable(false);
            BtnPetugas.setEnabled(false);
            KdPetugas.setText(akses.getkode());
            Sequel.cariIsi("select petugas.nama from petugas where petugas.nip=?", NmPetugas,KdPetugas.getText());
            if(NmPetugas.getText().equals("")){
                KdPetugas.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan petugas...!!");
            }
        }            
    }

    public void setTampil(){
       TabRawat.setSelectedIndex(1);
       tampil();
    }
    
    private void isMenu(){
        if(ChkAccor.isSelected()==true){
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(470,HEIGHT));
            FormMenu.setVisible(true);  
            FormMasalahRencana.setVisible(true);
            ChkAccor.setVisible(true);
        }else if(ChkAccor.isSelected()==false){    
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15,HEIGHT));
            FormMenu.setVisible(false);  
            FormMasalahRencana.setVisible(false);  
            ChkAccor.setVisible(true);
        }
    }

    private void getMasalah() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRM1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
            TPasien1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            DetailRencana.setText(tbObat.getValueAt(tbObat.getSelectedRow(),174).toString());
            
            try {
                Valid.tabelKosong(tabModeDetailMasalah);
                ps=koneksi.prepareStatement(
                        "select master_masalah_keperawatan.kode_masalah,master_masalah_keperawatan.nama_masalah from master_masalah_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_masalah on penilaian_awal_keperawatan_ranap_masalah.kode_masalah=master_masalah_keperawatan.kode_masalah "+
                        "where penilaian_awal_keperawatan_ranap_masalah.no_rawat=? order by penilaian_awal_keperawatan_ranap_masalah.kode_masalah");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeDetailMasalah.addRow(new Object[]{rs.getString(1),rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
            
            try {
                Valid.tabelKosong(tabModeDetailRencana);
                ps=koneksi.prepareStatement(
                        "select master_rencana_keperawatan.kode_rencana,master_rencana_keperawatan.rencana_keperawatan from master_rencana_keperawatan "+
                        "inner join penilaian_awal_keperawatan_ranap_rencana on penilaian_awal_keperawatan_ranap_rencana.kode_rencana=master_rencana_keperawatan.kode_rencana "+
                        "where penilaian_awal_keperawatan_ranap_rencana.no_rawat=? order by penilaian_awal_keperawatan_ranap_rencana.kode_rencana");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    rs=ps.executeQuery();
                    while(rs.next()){
                        tabModeDetailRencana.addRow(new Object[]{rs.getString(1),rs.getString(2)});
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
        }
    }
   
    

    private void hapus() {
        if(Sequel.queryu2tf("delete from asesmen_awal_keperawatan_neonatus_nicu where no_rawat=? and tanggal=?",2,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString(),
            tbObat.getValueAt(tbObat.getSelectedRow(),7).toString()
        })==true){
            TNoRM1.setText("");
            TPasien1.setText("");
            ChkAccor.setSelected(false);
            isMenu();
            tampil();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }

    private void ganti() {
        if(Sequel.mengedittf("asesmen_awal_keperawatan_neonatus_nicu","no_rawat=?","no_rawat=?,tanggal=?,Anamnesis=?,KetAnamnesis=?,AsalPasien=?,KeluhanUtama=?,DiagnosisMasuk=?,RiwayatKeluhanSaatIni=?,RiwayatPenggunaanObat=?,RiwayatKehamilanIbu=?,LahirDi=?,ProsesPersalinan=?,DitolongOleh=?,DitolongOlehLainLain=?,UsiaGestasi=?,Umur=?,BBL=?,PBL=?,RiwayatKesehatanSebelumnya=?,OpnameRS=?,SelamaHariOpname=?,RiwayatPembedahan=?,OperasiHari=?,RiwayatAlergi=?,SebutAlergi=?,Makanan=?,Imunisasi=?,RisikoGizi=?,BBLGizi=?,BBS=?,PBTB=?,LLA=?,LK=?,LD=?,LP=?,BentukDada=?,PernafasanSpontan=?,Frekuensi=?,SaturasiO2=?,Sesak=?,Retraksi=?,Sianosis=?,WarnaLendir=?,ccLendir=?,Wheezing=?,Ronchi=?,WSD=?,ccWSD=?,Suction=?,ccSuction=?,AlatBantuNafas=?,RespirasiJenisPernafasan=?,HasilRespirasi=?,KeteranganRespirasi=?,BunyiJantung=?,Pendarahan=?,JenisPendarahan=?,ccPendarahan=?,FemoralisKanan=?,FemoralisKiri=?,KeadaanSaatIni=?,Nadi=?,Ekstremitas=?,Kapiler=?,HasilLab=?,Intavena=?,HariIntavena=?,BeratBadan=?,persenBeratBadan=?,Lendir=?,CaraMinum=?,ReflexIsap=?,WarnaKulit=?,Lidah=?,ReflexTelan=?,Abdomen=?,KeadaanSekarang=?,TurgorKulit=?,UbunUbun=?,Muntah=?,HasilLaboratorium=?,KeteranganHasilLab=?,Residu=?,Kesadaran=?,Kepala=?,ReflexTampak=?,Tangisan=?,KetTangisan=?,LingkarKepala=?,Kejang=?,PupilKiri=?,PupilKanan=?,JenisPupil=?,Gerakan=?,KetNeurosensori=?,BAK=?,ProduksiUrine=?,KeadaanReproduksi=?,AlatBantu=?,Vagina=?,Menstruasi=?,Kateter=?,Prominen=?,Ambigus=?,Preputium=?,Hipospadia=?,BAB=?,ccBAB=?,Feses=?,Anus=?,WarnaBAB=?,KondisiBAB=?,FrekuensiBAB=?,Mekonium=?,Suhu=?,Dehidrasi=?,Integritas=?,KondisiKebersihanDiri=?,WarnaKulitBersihDiri=?,SuhuBersihDiri=?,DerajatSuhu=?,KepalaBersihDiri=?,Umbilical=?,KukuBersihDiri=?,MataBersihDiri=?,TaliPusat=?,AbdomenKebersihanDIri=?,JenisBersihDiri=?,LokasiBersihDiri=?,BanyakTidur=?,KeteranganTidur=?,PolaTidur=?,StatusAnak=?,Kongenital=?,Lokasi=?,SebutKongenital=?,StatusGestasi=?,Berkunjung=?,KontakMata=?,Menyentuh=?,Berbicara=?,Menggendong=?,YangMerawat=?,LainLainRawat=?,RencanaPulang=?,LainLainDischargePlaning=?,SkalaResiko1=?,NilaiResiko1=?,SkalaResiko2=?,NilaiResiko2=?,SkalaResiko3=?,NilaiResiko3=?,SkalaResiko4=?,NilaiResiko4=?,SkalaResiko5=?,NilaiResiko5=?,SkalaResiko6=?,NilaiResiko6=?,NilaiResikoTotal=?,SkalaSydney1=?,NilaiSydney1=?,SkalaSydney2=?,NilaiSydney2=?,SkalaSydney3=?,NilaiSydney3=?,SkalaSydney4=?,NilaiSydney4=?,SkalaSydney5=?,NilaiSydney5=?,NilaiSydneyTotal=?,Rencana=?,nip=?,reflexpalmargrasp=?,ReflexBabinski=?,ReflexPlantarGrasp=?,ReflexMoro=?,PerawatanDiri=?,PemantauanPemberianObat=?,PemberianASI=?,PerawatanMetodeKangguru=?,PerawatanLuka=?,PendampinganTenagaKhususDiRumah=?,Kremer=?",184,new String[]{
                TNoRw.getText(),Valid.SetTgl(TglAsuhan.getSelectedItem()+"")+" "+TglAsuhan.getSelectedItem().toString().substring(11,19),
//                Anamnesis.getSelectedItem().toString(),KetAnamnesis.getText(),
//                AsalPasien.getSelectedItem().toString(),KeluhanUtama.getText(),DiagnosisMasuk.getText(),RiwayatKeluhanSaatIni.getText(),RiwayatPenggunaanObat.getText(),RiwayatKehamilanIbu.getText(),LahirDi.getSelectedItem().toString(),ProsesPersalinan.getSelectedItem().toString(),DitolongOleh.getSelectedItem().toString(),DitolongOlehLainLain.getText(), 
//                UsiaGestasi.getText(),Umur.getText(),PBL.getText(),BBL.getText(),RiwayatKesehatanSebelumnya.getSelectedItem().toString(),OpnameRS.getText(),SelamaHariOpname.getText(),RiwayatPembedahan.getSelectedItem().toString(),OperasiHari.getText(),RiwayatAlergi.getSelectedItem().toString(),SebutAlergi.getText(),Makanan.getSelectedItem().toString(),Imunisasi.getSelectedItem().toString(),RisikoGizi.getSelectedItem().toString(),BBLGizi.getText(),BBS.getText(),PBTB.getText(),LLA.getText(),LK.getText(),LD.getText(),LP.getText(),BentukDada.getSelectedItem().toString(),PernafasanSpontan.getSelectedItem().toString(),Frekuensi.getText(),SaturasiO2.getText(),Sesak.getSelectedItem().toString(),Retraksi.getSelectedItem().toString(),Sianosis.getSelectedItem().toString(),WarnaLendir.getText(),ccLendir.getText(),Wheezing.getSelectedItem().toString(),Ronchi.getSelectedItem().toString(),WSD.getSelectedItem().toString(),ccWSD.getText(),Suction.getSelectedItem().toString(),ccSuction.getText(),AlatBantuNafas.getSelectedItem().toString(),RespirasiJenisPernafasan.getSelectedItem().toString(),HasilRespirasi.getText(),KeteranganRespirasi.getText(),BunyiJantung.getSelectedItem().toString(),Pendarahan.getSelectedItem().toString(),ccPendarahan.getText(),FemoralisKanan.getSelectedItem().toString(),FemoralisKiri.getSelectedItem().toString(),
//                KeadaanSaatIni.getSelectedItem().toString(),Nadi.getText(),Ekstremitas.getSelectedItem().toString(),Kapiler.getText(),HasilLab.getSelectedItem().toString(),Intavena.getSelectedItem().toString(),HariIntavena.getText(),BeratBadan.getSelectedItem().toString(),persenBeratBadan.getText(), Lendir.getSelectedItem().toString(),CaraMinum.getSelectedItem().toString(),ReflexIsap.getSelectedItem().toString(),WarnaKulit.getSelectedItem().toString(),Lidah.getSelectedItem().toString(),ReflexTelan.getSelectedItem().toString(),Abdomen.getSelectedItem().toString(),KeadaanSekarang.getSelectedItem().toString(),TurgorKulit.getSelectedItem().toString(),UbunUbun.getSelectedItem().toString(),Muntah.getText(),HasilLaboratorium.getSelectedItem().toString(),KeteranganHasilLab.getText(),Residu.getText(),Kesadaran.getSelectedItem().toString(),Kepala.getSelectedItem().toString(),ReflexTampak.getSelectedItem().toString(),Tangisan.getSelectedItem().toString(),KetTangisan.getSelectedItem().toString(),LingkarKepala.getText(),Kejang.getSelectedItem().toString(),PupilKiri.getText(),PupilKanan.getText(),JenisPupil.getSelectedItem().toString(),Gerakan.getSelectedItem().toString(),KetNeurosensori.getText(),BAK.getText(),ProduksiUrine.getText(),KeadaanReproduksi.getSelectedItem().toString(),AlatBantu.getSelectedItem().toString(),Vagina.getSelectedItem().toString(),Menstruasi.getSelectedItem().toString(),Kateter.getSelectedItem().toString(),Prominen.getSelectedItem().toString(),Ambigus.getSelectedItem().toString(),Preputium.getSelectedItem().toString(),Hipospadia.getSelectedItem().toString(),BAB.getSelectedItem().toString(),ccBAB.getText(),Feses.getSelectedItem().toString(),Anus.getSelectedItem().toString(),WarnaBAB.getText(),KondisiBAB.getSelectedItem().toString(),FrekuensiBAB.getText(),Mekonium.getText(),Suhu.getText(),Dehidrasi.getText(),Integritas.getSelectedItem().toString(),KondisiKebersihanDiri.getSelectedItem().toString(),WarnaKulitBersihDiri.getSelectedItem().toString(),SuhuBersihDiri.getSelectedItem().toString(),DerajatSuhu.getText(),KepalaBersihDiri.getSelectedItem().toString(),Umbilical.getSelectedItem().toString(),KukuBersihDiri.getSelectedItem().toString(),MataBersihDiri.getSelectedItem().toString(),TaliPusat.getSelectedItem().toString(),AbdomenKebersihanDIri.getSelectedItem().toString(),JenisBersihDiri.getText(),LokasiBersihDiri.getText(),BanyakTidur.getSelectedItem().toString(),KeteranganTidur.getText(),PolaTidur.getSelectedItem().toString(),StatusAnak.getSelectedItem().toString(),Kongenital.getSelectedItem().toString(),Lokasi.getText(),SebutKongenital.getText(),StatusGestasi.getSelectedItem().toString(),Berkunjung.getSelectedItem().toString(),KontakMata.getSelectedItem().toString(),Menyentuh.getSelectedItem().toString(),Berbicara.getSelectedItem().toString(),Menggendong.getSelectedItem().toString(),YangMerawat.getSelectedItem().toString(),LainLainRawat.getText(),RencanaPulang.getSelectedItem().toString(),LainLainDischargePlaning.getText(),
//                SkalaResiko1.getSelectedItem().toString(),NilaiResiko1.getText(),SkalaResiko2.getSelectedItem().toString(),NilaiResiko2.getText(),SkalaResiko3.getSelectedItem().toString(),NilaiResiko3.getText(),SkalaResiko4.getSelectedItem().toString(),NilaiResiko4.getText(),
//                SkalaResiko5.getSelectedItem().toString(),NilaiResiko5.getText(),SkalaResiko6.getSelectedItem().toString(),NilaiResiko6.getText(),NilaiResikoTotal.getText(),SkalaSydney1.getSelectedItem().toString(),NilaiSydney1.getText(),SkalaSydney2.getSelectedItem().toString(),NilaiSydney2.getText(),
//                SkalaSydney3.getSelectedItem().toString(),NilaiSydney3.getText(),SkalaSydney4.getSelectedItem().toString(),NilaiSydney4.getText(),SkalaSydney5.getSelectedItem().toString(),NilaiSydney5.getText(),NilaiSydneyTotal.getText(),
//                Rencana.getText(),KdPetugas.getText(),
//                tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
                Anamnesis.getSelectedItem().toString(),KetAnamnesis.getText(),AsalPasien.getSelectedItem().toString(),KeluhanUtama.getText(),DiagnosisMasuk.getText(),RiwayatKeluhanSaatIni.getText(),RiwayatPenggunaanObat.getText(),RiwayatKehamilanIbu.getText(),LahirDi.getSelectedItem().toString(),ProsesPersalinan.getSelectedItem().toString(),DitolongOleh.getSelectedItem().toString(),DitolongOlehLainLain.getText(),UsiaGestasi.getText(),Umur.getText(),BBL.getText(),PBL.getText(),RiwayatKesehatanSebelumnya.getSelectedItem().toString(),OpnameRS.getText(),SelamaHariOpname.getText(),RiwayatPembedahan.getSelectedItem().toString(),OperasiHari.getText(),RiwayatAlergi.getSelectedItem().toString(),SebutAlergi.getText(),Makanan.getSelectedItem().toString(),Imunisasi.getSelectedItem().toString(),RisikoGizi.getSelectedItem().toString(),BBLGizi.getText(),BBS.getText(),PBTB.getText(),LLA.getText(),LK.getText(),LD.getText(),LP.getText(),BentukDada.getSelectedItem().toString(),PernafasanSpontan.getSelectedItem().toString(),Frekuensi.getText(),SaturasiO2.getText(),Sesak.getSelectedItem().toString(),Retraksi.getSelectedItem().toString(),Sianosis.getSelectedItem().toString(),WarnaLendir.getText(),ccLendir.getText(),Wheezing.getSelectedItem().toString(),Ronchi.getSelectedItem().toString(),WSD.getSelectedItem().toString(),ccWSD.getText(),Suction.getSelectedItem().toString(),ccSuction.getText(),AlatBantuNafas.getSelectedItem().toString(),RespirasiJenisPernafasan.getSelectedItem().toString(),HasilRespirasi.getText(),KeteranganRespirasi.getText(),BunyiJantung.getSelectedItem().toString(),Pendarahan.getSelectedItem().toString(),JenisPendarahan.getText(),ccPendarahan.getText(),FemoralisKanan.getSelectedItem().toString(),FemoralisKiri.getSelectedItem().toString(),KeadaanSaatIni.getSelectedItem().toString(),Nadi.getText(),Ekstremitas.getSelectedItem().toString(),Kapiler.getText(),HasilLab.getSelectedItem().toString(),Intavena.getSelectedItem().toString(),HariIntavena.getText(),BeratBadan.getSelectedItem().toString(),persenBeratBadan.getText(),Lendir.getSelectedItem().toString(),CaraMinum.getSelectedItem().toString(),ReflexIsap.getSelectedItem().toString(),WarnaKulit.getSelectedItem().toString(),Lidah.getSelectedItem().toString(),ReflexTelan.getSelectedItem().toString(),Abdomen.getSelectedItem().toString(),KeadaanSekarang.getSelectedItem().toString(),TurgorKulit.getSelectedItem().toString(),UbunUbun.getSelectedItem().toString(),Muntah.getText(),HasilLaboratorium.getSelectedItem().toString(),Kremer.getText(),Residu.getText(),Kesadaran.getSelectedItem().toString(),Kepala.getSelectedItem().toString(),ReflexTampak.getSelectedItem().toString(),Tangisan.getSelectedItem().toString(),KetTangisan.getSelectedItem().toString(),LingkarKepala.getText(),Kejang.getSelectedItem().toString(),PupilKiri.getText(),PupilKanan.getText(),JenisPupil.getSelectedItem().toString(),Gerakan.getSelectedItem().toString(),KetNeurosensori.getText(),BAK.getText(),ProduksiUrine.getText(),KeadaanReproduksi.getSelectedItem().toString(),AlatBantu.getSelectedItem().toString(),Vagina.getSelectedItem().toString(),Menstruasi.getSelectedItem().toString(),Kateter.getSelectedItem().toString(),Prominen.getSelectedItem().toString(),Ambigus.getSelectedItem().toString(),Preputium.getSelectedItem().toString(),Hipospadia.getSelectedItem().toString(),BAB.getSelectedItem().toString(),ccBAB.getText(),Feses.getSelectedItem().toString(),Anus.getSelectedItem().toString(),WarnaBAB.getText(),KondisiBAB.getSelectedItem().toString(),FrekuensiBAB.getText(),Mekonium.getText(),Suhu.getText(),Dehidrasi.getText(),Integritas.getSelectedItem().toString(),KondisiKebersihanDiri.getSelectedItem().toString(),WarnaKulitBersihDiri.getSelectedItem().toString(),SuhuBersihDiri.getSelectedItem().toString(),DerajatSuhu.getText(),KepalaBersihDiri.getSelectedItem().toString(),Umbilical.getSelectedItem().toString(),KukuBersihDiri.getSelectedItem().toString(),MataBersihDiri.getSelectedItem().toString(),TaliPusat.getSelectedItem().toString(),AbdomenKebersihanDIri.getSelectedItem().toString(),JenisBersihDiri.getText(),LokasiBersihDiri.getText(),BanyakTidur.getSelectedItem().toString(),KeteranganTidur.getText(),PolaTidur.getSelectedItem().toString(),StatusAnak.getSelectedItem().toString(),Kongenital.getSelectedItem().toString(),Lokasi.getText(),SebutKongenital.getText(),StatusGestasi.getSelectedItem().toString(),Berkunjung.getSelectedItem().toString(),KontakMata.getSelectedItem().toString(),Menyentuh.getSelectedItem().toString(),Berbicara.getSelectedItem().toString(),Menggendong.getSelectedItem().toString(),YangMerawat.getSelectedItem().toString(),LainLainRawat.getText(),PerawatanDiri.getSelectedItem().toString(),LainLainDischargePlaning.getText(),SkalaResiko1.getSelectedItem().toString(),NilaiResiko1.getText(),SkalaResiko2.getSelectedItem().toString(),NilaiResiko2.getText(),SkalaResiko3.getSelectedItem().toString(),NilaiResiko3.getText(),SkalaResiko4.getSelectedItem().toString(),NilaiResiko4.getText(),SkalaResiko5.getSelectedItem().toString(),NilaiResiko5.getText(),SkalaResiko6.getSelectedItem().toString(),NilaiResiko6.getText(),NilaiResikoTotal.getText(),SkalaSydney1.getSelectedItem().toString(),NilaiSydney1.getText(),SkalaSydney2.getSelectedItem().toString(),NilaiSydney2.getText(),SkalaSydney3.getSelectedItem().toString(),NilaiSydney3.getText(),SkalaSydney4.getSelectedItem().toString(),NilaiSydney4.getText(),SkalaSydney5.getSelectedItem().toString(),NilaiSydney5.getText(),NilaiSydneyTotal.getText(),Rencana.getText(),KdPetugas.getText(),
                reflexpalmargrasp.getSelectedItem().toString(),ReflexBabinski.getSelectedItem().toString(),ReflexPlantarGrasp.getSelectedItem().toString(),ReflexMoro.getSelectedItem().toString(),PerawatanDiri.getSelectedItem().toString(),PemantauanPemberianObat.getSelectedItem().toString(),PemberianASI.getSelectedItem().toString(),PerawatanMetodeKangguru.getSelectedItem().toString(),PerawatanLuka.getSelectedItem().toString(),PendampinganTenagaKhususDiRumah.getSelectedItem().toString(),Kremer.getText(),
                tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
             })==true){            
                Sequel.meghapus("penilaian_awal_keperawatan_ranap_masalah","no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                for (i = 0; i < tbMasalahKeperawatan.getRowCount(); i++) {
                    if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                        Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_masalah","?,?",2,new String[]{TNoRw.getText(),tbMasalahKeperawatan.getValueAt(i,1).toString()});
                    }
                }
                Sequel.meghapus("penilaian_awal_keperawatan_ranap_rencana","no_rawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                for (i = 0; i < tbRencanaKeperawatan.getRowCount(); i++) {
                    if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                        Sequel.menyimpan2("penilaian_awal_keperawatan_ranap_rencana","?,?",2,new String[]{TNoRw.getText(),tbRencanaKeperawatan.getValueAt(i,1).toString()});
                    }
                }
                getMasalah();
                tampil();
                DetailRencana.setText(Rencana.getText());
                emptTeks();
                TabRawat.setSelectedIndex(1);
        }
    }
    
    private void isTotalResikoJatuh(){
        try {
            NilaiResikoTotal.setText((Integer.parseInt(NilaiResiko1.getText())+Integer.parseInt(NilaiResiko2.getText())+Integer.parseInt(NilaiResiko3.getText())+Integer.parseInt(NilaiResiko4.getText())+Integer.parseInt(NilaiResiko5.getText())+Integer.parseInt(NilaiResiko6.getText()))+"");
            if(Integer.parseInt(NilaiResikoTotal.getText())<25){
//                TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (0-24), Tindakan : Intervensi pencegahan risiko jatuh standar");
            }else if(Integer.parseInt(NilaiResikoTotal.getText())<45){
//                TingkatResiko.setText("Tingkat Resiko : Risiko Sedang (25-44), Tindakan : Intervensi pencegahan risiko jatuh standar");
            }else if(Integer.parseInt(NilaiResikoTotal.getText())>=45){
//                TingkatResiko.setText("Tingkat Resiko : Risiko Tinggi (> 45), Tindakan : Intervensi pencegahan risiko jatuh standar dan Intervensi risiko jatuh tinggi");
            }
        } catch (Exception e) {
            NilaiResikoTotal.setText("0");
//            TingkatResiko.setText("Tingkat Resiko : Risiko Rendah (0-24), Tindakan : Intervensi pencegahan risiko jatuh standar");
        }
    }
    
    
    
    private void tampilMasalah() {
        try{
            Valid.tabelKosong(tabModeMasalah);
            file=new File("./cache/masalahkeperawatan.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem="";
            ps=koneksi.prepareStatement("select * from master_masalah_keperawatan order by master_masalah_keperawatan.kode_masalah");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeMasalah.addRow(new Object[]{false,rs.getString(1),rs.getString(2)});
                    iyem=iyem+"{\"KodeMasalah\":\""+rs.getString(1)+"\",\"NamaMasalah\":\""+rs.getString(2)+"\"},";
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            fileWriter.write("{\"masalahkeperawatan\":["+iyem.substring(0,iyem.length()-1)+"]}");
            fileWriter.flush();
            fileWriter.close();
            iyem=null;
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilMasalah2() {
        try{
            jml=0;
            for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
                if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            masalah=null;
            masalah=new String[jml];

            index=0;        
            for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
                if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=tbMasalahKeperawatan.getValueAt(i,1).toString();
                    masalah[index]=tbMasalahKeperawatan.getValueAt(i,2).toString();
                    index++;
                }
            } 

            Valid.tabelKosong(tabModeMasalah);

            for(i=0;i<jml;i++){
                tabModeMasalah.addRow(new Object[] {
                    pilih[i],kode[i],masalah[i]
                });
            }
            
            myObj = new FileReader("./cache/masalahkeperawatan.iyem");
            root = mapper.readTree(myObj);
            response = root.path("masalahkeperawatan");
            if(response.isArray()){
                for(JsonNode list:response){
                    if(list.path("KodeMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())||list.path("NamaMasalah").asText().toLowerCase().contains(TCariMasalah.getText().toLowerCase())){
                        tabModeMasalah.addRow(new Object[]{
                            false,list.path("KodeMasalah").asText(),list.path("NamaMasalah").asText()
                        });                    
                    }
                }
            }
            myObj.close();
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilRencana() {
        try{
            file=new File("./cache/rencanakeperawatan.iyem");
            file.createNewFile();
            fileWriter = new FileWriter(file);
            iyem="";
            ps=koneksi.prepareStatement("select * from master_rencana_keperawatan order by master_rencana_keperawatan.kode_rencana");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    iyem=iyem+"{\"KodeMasalah\":\""+rs.getString(1)+"\",\"KodeRencana\":\""+rs.getString(2)+"\",\"NamaRencana\":\""+rs.getString(3)+"\"},";
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
            fileWriter.write("{\"rencanakeperawatan\":["+iyem.substring(0,iyem.length()-1)+"]}");
            fileWriter.flush();
            fileWriter.close();
            iyem=null;
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    private void tampilRencana2() {
        try{
            jml=0;
            for(i=0;i<tbRencanaKeperawatan.getRowCount();i++){
                if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            masalah=null;
            masalah=new String[jml];

            index=0;        
            for(i=0;i<tbRencanaKeperawatan.getRowCount();i++){
                if(tbRencanaKeperawatan.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=tbRencanaKeperawatan.getValueAt(i,1).toString();
                    masalah[index]=tbRencanaKeperawatan.getValueAt(i,2).toString();
                    index++;
                }
            } 

            Valid.tabelKosong(tabModeRencana);

            for(i=0;i<jml;i++){
                tabModeRencana.addRow(new Object[] {
                    pilih[i],kode[i],masalah[i]
                });
            }

            myObj = new FileReader("./cache/rencanakeperawatan.iyem");
            root = mapper.readTree(myObj);
            response = root.path("rencanakeperawatan");
            if(response.isArray()){
                for(i=0;i<tbMasalahKeperawatan.getRowCount();i++){
                    if(tbMasalahKeperawatan.getValueAt(i,0).toString().equals("true")){
                        for(JsonNode list:response){
                            if(list.path("KodeMasalah").asText().toLowerCase().equals(tbMasalahKeperawatan.getValueAt(i,1).toString())&&
                                    list.path("NamaRencana").asText().toLowerCase().contains(TCariRencana.getText().toLowerCase())){
                                tabModeRencana.addRow(new Object[]{
                                    false,list.path("KodeRencana").asText(),list.path("NamaRencana").asText()
                                });                    
                            }
                        }
                    }
                }
            }
            myObj.close();
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }

    private void isTotalResikoSydney() {
        try {
            NilaiSydneyTotal.setText((Integer.parseInt(NilaiSydney1.getText())+Integer.parseInt(NilaiSydney2.getText())+Integer.parseInt(NilaiSydney3.getText())+Integer.parseInt(NilaiSydney4.getText())+Integer.parseInt(NilaiSydney5.getText()))+"");
            if(Integer.parseInt(NilaiSydneyTotal.getText())<4){
//                TingkatSydney.setText("Tingkat Resiko : Risiko Rendah (1-3), Tindakan : Intervensi pencegahan risiko standar");
            }else if(Integer.parseInt(NilaiSydneyTotal.getText())>=4){
//                TingkatSydney.setText("Tingkat Resiko : Risiko Sedang (> 4), Tindakan : Intervensi pencegahan risiko tinggi");
            }
        } catch (Exception e) {
            NilaiSydneyTotal.setText("0");
//            TingkatSydney.setText("Tingkat Resiko : Risiko Rendah (1-3), Tindakan : Intervensi pencegahan risiko standar");
        }
    }
}