/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khanzahmsanjungan;

import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;

/**
 *
 * @author windiartonugroho
 */
public class DlgLoketAntrian extends javax.swing.JFrame {
    private final Connection koneksi=koneksiDB.condb();
    private final validasi Valid=new validasi();
    private final sekuel Sequel=new sekuel();
    private PreparedStatement ps;
    private ResultSet rs;
    /**
     * Creates new form frmUtama
     */
    public DlgLoketAntrian() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        
        jam();
    }

    DlgLoketAntrian(Object object, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        BtnCetak = new widget.Button();
        LabelNomor = new widget.Label();
        LabelTanggal = new widget.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIMKES Khanza Cetak Antrian Loket");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setWarnaAtas(new java.awt.Color(0, 50, 0));
        internalFrame1.setWarnaBawah(new java.awt.Color(0, 130, 0));
        internalFrame1.setLayout(new java.awt.BorderLayout());

        BtnCetak.setForeground(new java.awt.Color(255, 255, 51));
        BtnCetak.setText("Cetak");
        BtnCetak.setFont(new java.awt.Font("Tahoma", 1, 80)); // NOI18N
        BtnCetak.setGlassColor(new java.awt.Color(255, 255, 204));
        BtnCetak.setPreferredSize(new java.awt.Dimension(158, 125));
        BtnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCetakActionPerformed(evt);
            }
        });
        internalFrame1.add(BtnCetak, java.awt.BorderLayout.PAGE_END);

        LabelNomor.setForeground(new java.awt.Color(255, 255, 255));
        LabelNomor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelNomor.setText("001");
        LabelNomor.setFont(new java.awt.Font("Tahoma", 0, 350)); // NOI18N
        internalFrame1.add(LabelNomor, java.awt.BorderLayout.CENTER);

        LabelTanggal.setForeground(new java.awt.Color(255, 255, 51));
        LabelTanggal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelTanggal.setText("Antrian Loket Pendaftaran Tanggal :");
        LabelTanggal.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        internalFrame1.add(LabelTanggal, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        autonomer();
        try{            
            ps=koneksi.prepareStatement("select nama_instansi, alamat_instansi, kabupaten, propinsi, aktifkan, wallpaper,kontak,email,logo from setting");
            try {
                rs=ps.executeQuery();
                while(rs.next()){
                    akses.setnamars(rs.getString("nama_instansi"));
                    akses.setalamatrs(rs.getString("alamat_instansi"));
                    akses.setkabupatenrs(rs.getString("kabupaten"));
                    akses.setpropinsirs(rs.getString("propinsi"));
                    akses.setkontakrs(rs.getString("kontak"));
                    akses.setemailrs(rs.getString("email"));
                }  
            } catch (Exception e) {
                System.out.println(e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                 
        }catch(Exception e){
            System.out.println("Notifikasi : Silahkan Set Aplikasi "+e);
        }
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DlgLoketAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgLoketAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgLoketAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgLoketAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DlgLoketAntrian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnCetak;
    private widget.Label LabelNomor;
    private widget.Label LabelTanggal;
    private widget.InternalFrame internalFrame1;
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
    
    private void autonomer(){
        Valid.autoNomer3("select ifnull(MAX(CONVERT(antriloketcetak.nomor,signed)),0) from antriloketcetak where antriloketcetak.tanggal=current_date()","",3,LabelNomor);
    }
}
