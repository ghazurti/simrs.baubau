/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgAdmin.java
 *
 * Created on 04 Des 13, 12:59:34
 */
package khanzahmsanjungan;

import bridging.BPJSCekRujukanKartuPCare;
import fungsi.koneksiDB;
import fungsi.akses;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;

/**
 *
 * @author Kode
 */
public class DlgAntrian extends javax.swing.JDialog {
    
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
    private String umur = "0", sttsumur = "Th";
    private String status = "Baru", BASENOREG = "", URUTNOREG = "", aktifjadwal = "";
    private Properties prop = new Properties();
    private int lebar = 0, tinggi = 0;

    /**
     * Creates new form DlgAdmin
     *
     * @param parent
     * @param id
     */
    public DlgAntrian(java.awt.Frame parent, boolean id) {
        super(parent, id);
        initComponents();
        
        try {
            ps = koneksi.prepareStatement(
                    "select nm_pasien,concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) asal,"
                    + "namakeluarga,keluarga,pasien.kd_pj,penjab.png_jawab,if(tgl_daftar=?,'Baru','Lama') as daftar, "
                    + "TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) as tahun, "
                    + "(TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12)) as bulan, "
                    + "TIMESTAMPDIFF(DAY, DATE_ADD(DATE_ADD(tgl_lahir,INTERVAL TIMESTAMPDIFF(YEAR, tgl_lahir, CURDATE()) YEAR), INTERVAL TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) - ((TIMESTAMPDIFF(MONTH, tgl_lahir, CURDATE()) div 12) * 12) MONTH), CURDATE()) as hari from pasien "
                    + "inner join kelurahan inner join kecamatan inner join kabupaten inner join penjab "
                    + "on pasien.kd_kel=kelurahan.kd_kel and pasien.kd_pj=penjab.kd_pj "
                    + "and pasien.kd_kec=kecamatan.kd_kec and pasien.kd_kab=kabupaten.kd_kab "
                    + "where pasien.no_rkm_medis=?");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        try {
            prop.loadFromXML(new FileInputStream("setting/database.xml"));
            aktifjadwal = prop.getProperty("JADWALDOKTERDIREGISTRASI");
            URUTNOREG = prop.getProperty("URUTNOREG");
            BASENOREG = prop.getProperty("BASENOREG");
        } catch (Exception ex) {
            aktifjadwal = "";
            URUTNOREG = "";
            BASENOREG = "";
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LblKdPoli = new component.Label();
        LblKdDokter = new component.Label();
        NoReg = new component.TextBox();
        NoRawat = new component.TextBox();
        Biaya = new component.TextBox();
        jPanel1 = new component.Panel();
        BtnCetak = new widget.Button();
        LabelNomor = new widget.Label();
        LabelTanggal = new widget.Label();
        BtnClose = new widget.ButtonBig();

        LblKdPoli.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LblKdPoli.setText("Norm");
        LblKdPoli.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LblKdPoli.setPreferredSize(new java.awt.Dimension(20, 14));

        LblKdDokter.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LblKdDokter.setText("Norm");
        LblKdDokter.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LblKdDokter.setPreferredSize(new java.awt.Dimension(20, 14));

        NoReg.setPreferredSize(new java.awt.Dimension(320, 30));
        NoReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoRegActionPerformed(evt);
            }
        });
        NoReg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRegKeyPressed(evt);
            }
        });

        NoRawat.setPreferredSize(new java.awt.Dimension(320, 30));
        NoRawat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoRawatActionPerformed(evt);
            }
        });
        NoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKeyPressed(evt);
            }
        });

        Biaya.setPreferredSize(new java.awt.Dimension(320, 30));
        Biaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BiayaActionPerformed(evt);
            }
        });
        Biaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BiayaKeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(1, 1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 215, 255)), "::[ Pasien Baru Ambil antrian Loket!!! ]::", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Poppins", 0, 24), new java.awt.Color(0, 131, 62))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(0, 131, 62));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 70));
        jPanel1.setLayout(new java.awt.BorderLayout());

        BtnCetak.setForeground(new java.awt.Color(0, 153, 102));
        BtnCetak.setText("Cetak");
        BtnCetak.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        BtnCetak.setGlassColor(new java.awt.Color(51, 255, 153));
        BtnCetak.setPreferredSize(new java.awt.Dimension(158, 125));
        BtnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetakActionPerformed(evt);
            }
        });
        jPanel1.add(BtnCetak, java.awt.BorderLayout.PAGE_END);

        LabelNomor.setForeground(new java.awt.Color(0, 153, 153));
        LabelNomor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelNomor.setText("001");
        LabelNomor.setFont(new java.awt.Font("Tahoma", 0, 350)); // NOI18N
        LabelNomor.setPreferredSize(new java.awt.Dimension(474, 324));
        jPanel1.add(LabelNomor, java.awt.BorderLayout.CENTER);

        LabelTanggal.setForeground(new java.awt.Color(0, 153, 102));
        LabelTanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelTanggal.setText("Antrian Loket Pendaftaran Tanggal :");
        LabelTanggal.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jPanel1.add(LabelTanggal, java.awt.BorderLayout.PAGE_START);

        BtnClose.setBackground(new java.awt.Color(255, 255, 255));
        BtnClose.setForeground(new java.awt.Color(51, 51, 51));
        BtnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/exit.png"))); // NOI18N
        BtnClose.setMnemonic('U');
        BtnClose.setToolTipText("Alt+U");
        BtnClose.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        BtnClose.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
        BtnClose.setIconTextGap(2);
        BtnClose.setMargin(new java.awt.Insets(0, 0, 0, 0));
        BtnClose.setPreferredSize(new java.awt.Dimension(100, 75));
        BtnClose.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        BtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(BtnClose, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened
    
    private void NoRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRegActionPerformed
    
    private void NoRegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRegKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRegKeyPressed
    
    private void NoRawatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoRawatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRawatActionPerformed
    
    private void NoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoRawatKeyPressed
    
    private void BiayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BiayaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BiayaActionPerformed
    
    private void BiayaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BiayaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BiayaKeyPressed

    private void BtnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCetakActionPerformed
        if(Sequel.menyimpantf("antriloketcetak","current_date(),current_time(),'"+LabelNomor.getText()+"'","Nomor Antrian")==true){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            Valid.MyReportqry("rptAntriLoket.jasper","report","::[ Antrian Loket ]::",
                "select date_format(antriloketcetak.tanggal,'%d-%m-%Y') as tanggal,antriloketcetak.nomor,antriloketcetak.jam from antriloketcetak where antriloketcetak.tanggal=current_date and antriloketcetak.nomor='"+LabelNomor.getText()+"' ",param);
            this.setCursor(Cursor.getDefaultCursor());
            autonomer();
        }else{
            autonomer();
            if(Sequel.menyimpantf("antriloketcetak","current_date(),current_time(),'"+LabelNomor.getText()+"'","Nomor Antrian")==true){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Map<String, Object> param = new HashMap<>();
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());
                Valid.MyReportqry("rptAntriLoket.jasper","report","::[ Antrian Loket ]::",
                    "select date_format(antriloketcetak.tanggal,'%d-%m-%Y') as tanggal,antriloketcetak.nomor,antriloketcetak.jam from antriloketcetak where antriloketcetak.tanggal=current_date and antriloketcetak.nomor='"+LabelNomor.getText()+"' ",param);
                this.setCursor(Cursor.getDefaultCursor());
                autonomer();
            }else{
                autonomer();
                if(Sequel.menyimpantf("antriloketcetak","current_date(),current_time(),'"+LabelNomor.getText()+"'","Nomor Antrian")==true){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars",akses.getnamars());
                    param.put("alamatrs",akses.getalamatrs());
                    param.put("kotars",akses.getkabupatenrs());
                    param.put("propinsirs",akses.getpropinsirs());
                    param.put("kontakrs",akses.getkontakrs());
                    param.put("emailrs",akses.getemailrs());
                    Valid.MyReportqry("rptAntriLoket.jasper","report","::[ Antrian Loket ]::",
                        "select date_format(antriloketcetak.tanggal,'%d-%m-%Y') as tanggal,antriloketcetak.nomor,antriloketcetak.jam from antriloketcetak where antriloketcetak.tanggal=current_date and antriloketcetak.nomor='"+LabelNomor.getText()+"' ",param);
                    this.setCursor(Cursor.getDefaultCursor());
                    autonomer();
                }
            }
        }
    }//GEN-LAST:event_BtnCetakActionPerformed

    private void BtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseActionPerformed

        dispose();
    }//GEN-LAST:event_BtnCloseActionPerformed
                
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgAntrian dialog = new DlgAntrian(new javax.swing.JFrame(), true);
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
    private component.TextBox Biaya;
    private widget.Button BtnCetak;
    private widget.ButtonBig BtnClose;
    private widget.Label LabelNomor;
    private widget.Label LabelTanggal;
    private component.Label LblKdDokter;
    private component.Label LblKdPoli;
    private component.TextBox NoRawat;
    private component.TextBox NoReg;
    private component.Panel jPanel1;
    // End of variables declaration//GEN-END:variables

    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                Date now = Calendar.getInstance().getTime();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
                LocalDateTime sekarang = LocalDateTime.now();  
                LabelTanggal.setText("Antrian Loket Pendaftaran Tanggal : "+dtf.format(sekarang)); 
                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                nilai_jam = now.getHours();
                nilai_menit = now.getMinutes();
                nilai_detik = now.getSeconds();
                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                if(menit.equals("01")&&detik.equals("01")){
                    autonomer();
                }
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
    public void setPasien(String norm, String kodepoli, String kddokter) {
    }
    
    private void UpdateUmur() {
        
    }
    
    private void isNumber() {
    }
    
    private void autonomer(){
        Valid.autoNomer3("select ifnull(MAX(CONVERT(antriloketcetak.nomor,signed)),0) from antriloketcetak where antriloketcetak.tanggal=current_date()","",3,LabelNomor);
    }
}
