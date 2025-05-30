
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

// Diğer importlar ve class başı...

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Monster
 */
public class dorduncusayfa extends javax.swing.JFrame {
    private String kullaniciAdi = "root";
    private String parola = "";
    private String db_ismi = "i̇nsaat";
    private String host = "localhost";
    private int port = 3306;
    private Connection con = null;

    /**
     * Creates new form dorduncusayfa
     */
    public dorduncusayfa() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ikincisayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        setTitle("Maliyet Hesaplama <Me-AK>");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JFrame'in içerik bölmesinin arka plan rengini ayarlayın
        getContentPane().setBackground(new Color(240, 240, 240));
        
        initComponents();
        baglanti();
        cekme();
    }
     public void baglanti() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db_ismi;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            // l1.setText("Driver Bulunamadı");  // l1 varsa bu satırı açabilirsiniz
            l2.setText("Veritabanı Bağlanti Basarisiz");
        }

        try {
            con = DriverManager.getConnection(url, kullaniciAdi, parola);
            Timer timer = new Timer(500, e -> {
                
                if(l2.getText().isEmpty())
                {
                    l2.setText("Veritabanı Bağlanti Başarili");
                }else
                {
                    l2.setText("");
                }
            });
            timer.start();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ucuncusayfa.class.getName()).log(Level.SEVERE, null, ex);
            Timer timer = new Timer(500, e -> {
                
                if(l2.getText().isEmpty())
                {
                    l2.setText("Veritabanı Bağlanti Basarisiz");
                    l2.setForeground(new Color(255, 0, 0));
                }else
                {
                    
                    l2.setText("");
                }
            });
            timer.start();
            
        }
    }
public void cekme() 
{
    try {
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/i̇nsaat?useSSL=false&serverTimezone=UTC", "root", "");

        String sql = "SELECT * FROM inşaat_teklif";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        while (rs.next()) {
            String isim = rs.getString("insaat_sahibi");
            Float teklif = rs.getFloat("teklif");
            Float demir_fiyati = rs.getFloat("demir_fiyati");
            Float demir_birim_fiyati = rs.getFloat("demir_birim_fiyati");
            Float beton_fiyati = rs.getFloat("beton_fiyati");
            Float beton_birim_fiyati = rs.getFloat("beton_birim_fiyati");
            Float tugla_fiyati = rs.getFloat("tugla_fiyati");
            Float tugla_birim_fiyati = rs.getFloat("tugla_birim_fiyati");
            Float duvar_alan = rs.getFloat("duvar_alan");
            Float metrekare = rs.getFloat("metrekare");
            Float katsayisi = rs.getFloat("katsayisi");

            Object[] bilgiler = {
                isim,
                teklif,
                demir_fiyati,
                demir_birim_fiyati,
                beton_fiyati,
                beton_birim_fiyati,
                tugla_fiyati,
                tugla_birim_fiyati,
                duvar_alan,
                metrekare,
                katsayisi
            };

            model.addRow(bilgiler);
        }

    } catch (Exception e) {
        e.printStackTrace();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        l2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(1000, 500));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "İsim", "Teklif", "Demir Fiyatı", "Demir Birim Fİyati", "Beton Fiyatı", "Beton Birim Fiyatı", "Tuğla Fİyatı", "Tuğla Birim Fiyatı", "Duvar Alanı", "Metrekare", "Kat Sayısı"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        l2.setForeground(new java.awt.Color(0, 153, 0));
        l2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database-storage.png"))); // NOI18N

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(l2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 876, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 76, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(l2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Ilksayfa sayfa1 = new Ilksayfa();
        sayfa1.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(dorduncusayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dorduncusayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dorduncusayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dorduncusayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dorduncusayfa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel l2;
    // End of variables declaration//GEN-END:variables
}
