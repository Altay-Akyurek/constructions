import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.*;

public class Ikincisayfa extends javax.swing.JFrame {
    private String kullaniciAdi = "root";
    private String parola = "";
    private String db_ismi = "i̇nsaat";
    private String host = "localhost";
    private int port = 3306;
    private Connection con = null;
    /**
     * Creates new form proje_odevi2
     */
    public Ikincisayfa() {
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
    private float sayiyiFloataCevir(String text) {
    if (text == null) return 0f;
    text = text.trim().replaceAll("[^\\d,.-]", "");
    // Eğer sayı virgül içeriyorsa ve son virgül noktadan sonra geliyorsa, Türk/Avrupa formatıdır
    if (text.contains(",") && text.lastIndexOf(",") > text.lastIndexOf(".")) {
        text = text.replace(".", ""); // Binlik ayraçları kaldır
        text = text.replace(",", "."); // Virgülü noktaya çevir
    } else {
        text = text.replace(",", ""); // Binlik virgülleri kaldır
    }
    return Float.parseFloat(text);
    }
   public void ekle() {
    String insaat_sahibi;
    Float teklif, demir_fiyati, demir_birim_fiyati, beton_fiyati, beton_birim_fiyati, tugla_fiyati, tugla_birim_fiyati, duvar_alan, metrekare, katsayisi;

    insaat_sahibi = tf22.getText();
    teklif = sayiyiFloataCevir(mm.getText());
    demir_fiyati = sayiyiFloataCevir(t1.getText());
    demir_birim_fiyati = sayiyiFloataCevir(tf7.getText());
    beton_fiyati = sayiyiFloataCevir(t2.getText());
    beton_birim_fiyati = sayiyiFloataCevir(tf8.getText());
    tugla_fiyati = sayiyiFloataCevir(t3.getText());
    tugla_birim_fiyati = sayiyiFloataCevir(tf9.getText());
    duvar_alan = sayiyiFloataCevir(tf11.getText());
    metrekare = sayiyiFloataCevir(tf5.getText());
    katsayisi = sayiyiFloataCevir(tf6.getText());

    String ekleSorgu = "INSERT INTO inşaat_teklif(insaat_sahibi, teklif , demir_fiyati,demir_birim_fiyati ,beton_fiyati,beton_birim_fiyati,tugla_fiyati,tugla_birim_fiyati,duvar_alan,metrekare,katsayisi) VALUES('" 
        + insaat_sahibi + "', " + teklif + "," + demir_fiyati + "," + demir_birim_fiyati + "," + beton_fiyati + "," + beton_birim_fiyati + "," + tugla_fiyati + "," + tugla_birim_fiyati + "," + duvar_alan + "," + metrekare + "," + katsayisi + ")";

    try {
        Statement calistir = con.createStatement();
        calistir.execute(ekleSorgu);
        JOptionPane.showMessageDialog(null, "Teklif kaydedildi.");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Teklif kaydedilmedi.");
        System.out.println(ex);
    }
}

    public void cekme() 
    {
        try {
            // Veritabanı bağlantısı
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/i̇nsaat?useSSL=false&serverTimezone=UTC", "root", "");

            // SQL sorgusunu hazırlıyoruz, en son eklenen veriyi almak için DESC ile sıralıyoruz
            String sql = "SELECT * FROM malzeme ORDER BY id DESC LIMIT 1"; // id ile sıralama yapıyoruz
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Verileri okuma
            if (rs.next()) {
                double beton_serfiyat_katsayisi = rs.getDouble("beton_serfiyat_katsayisi");
                double beton_maliyeti = rs.getDouble("beton_maliyeti");
                double demir_agirligi = rs.getDouble("demir_agirligi");
                double demir_maliyeti = rs.getDouble("demir_maliyeti");
                double tugla_uzunlugu = rs.getDouble("tugla_uzunlugu");
                double tugla_genisligi = rs.getDouble("tugla_genisligi");
                double tugla_maliyeti = rs.getDouble("tugla_maliyeti");
                 
                // Sonuçları yazdırma
                tf8.setText(String.valueOf(beton_maliyeti));
                tf7.setText(String.valueOf(demir_maliyeti));
                tf9.setText(String.valueOf(tugla_maliyeti));
                
                //tuğla işlemleri 
                double tugla_alani=tugla_uzunlugu*tugla_genisligi;
                tugla_m.setText(String.valueOf(tugla_maliyeti));
                tugla_adeti.setText(String.valueOf(tugla_alani));
                
                //beton işlemleri
                beton.setText(String.valueOf(beton_maliyeti));
                beton2.setText(String.valueOf(beton_serfiyat_katsayisi));
                
                //demir işlemeleri
                demir_a.setText(String.valueOf(demir_agirligi));
                demir_m.setText(String.valueOf(demir_maliyeti));
                
                
            } else {
                System.out.println("Veri bulunamadı!");
            }

            // Bağlantıyı kapatıyoruz
            rs.close();
            stmt.close();
            conn.close();
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

        jColorChooser1 = new javax.swing.JColorChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        b1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        t1 = new javax.swing.JTextField();
        t2 = new javax.swing.JTextField();
        t3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tf5 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf6 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tf7 = new javax.swing.JTextField();
        tf8 = new javax.swing.JTextField();
        tf9 = new javax.swing.JTextField();
        l2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        tugla_adeti = new javax.swing.JTextField();
        beton = new javax.swing.JTextField();
        beton2 = new javax.swing.JTextField();
        demir_a = new javax.swing.JTextField();
        demir_m = new javax.swing.JTextField();
        tf11 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tugla_m = new javax.swing.JTextField();
        tf33 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tf22 = new javax.swing.JTextField();
        mm = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 102, 255));
        jButton1.setText("Beton");
        jButton1.setAutoscrolls(true);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusCycleRoot(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 102, 255));
        jButton2.setText("Tuğla");
        jButton2.setAutoscrolls(true);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusCycleRoot(true);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 102, 255));
        jButton4.setText("Demir");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setDefaultCapable(false);
        jButton4.setFocusCycleRoot(true);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        b1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        b1.setForeground(new java.awt.Color(16, 88, 236));
        b1.setText("Hesapla");
        b1.setAutoscrolls(true);
        b1.setFocusCycleRoot(true);
        b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("   Fİyat");
        jLabel1.setAutoscrolls(true);
        jLabel1.setFocusCycleRoot(true);
        jLabel1.setMaximumSize(new java.awt.Dimension(50, 16));

        t1.setForeground(new java.awt.Color(255, 0, 0));
        t1.setText("0,0");
        t1.setFocusCycleRoot(true);

        t2.setForeground(new java.awt.Color(255, 0, 0));
        t2.setText("0,0");
        t2.setFocusCycleRoot(true);
        t2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2ActionPerformed(evt);
            }
        });

        t3.setForeground(new java.awt.Color(255, 0, 0));
        t3.setText("0,0");
        t3.setFocusCycleRoot(true);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 255));
        jLabel2.setText("METREKARE GİRİNİZ");
        jLabel2.setAutoscrolls(true);
        jLabel2.setFocusCycleRoot(true);

        tf5.setFocusCycleRoot(true);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("KAT SAYISINI GİRİNİZ");
        jLabel3.setAutoscrolls(true);
        jLabel3.setFocusCycleRoot(true);

        tf6.setFocusCycleRoot(true);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 255));
        jLabel4.setText("Birim Maliyetler");
        jLabel4.setAutoscrolls(true);
        jLabel4.setFocusCycleRoot(true);

        tf7.setForeground(new java.awt.Color(255, 0, 51));
        tf7.setFocusCycleRoot(true);

        tf8.setForeground(new java.awt.Color(255, 0, 51));
        tf8.setFocusCycleRoot(true);
        tf8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf8ActionPerformed(evt);
            }
        });

        tf9.setForeground(new java.awt.Color(255, 0, 51));
        tf9.setFocusCycleRoot(true);

        l2.setForeground(new java.awt.Color(0, 153, 0));
        l2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database-storage.png"))); // NOI18N

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kopya.PNG"))); // NOI18N
        jButton3.setText("jButton3");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setDefaultCapable(false);
        jButton3.setFocusPainted(false);
        jButton3.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jButton3MouseWheelMoved(evt);
            }
        });
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        tugla_adeti.setEnabled(false);

        beton.setEnabled(false);

        beton2.setEnabled(false);

        demir_a.setText("jTextField1");

        demir_m.setText("jTextField1");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("DUVAR ALANINI GİRİNİZ: m²");

        tugla_m.setText("jTextField1");

        tf33.setForeground(new java.awt.Color(0, 0, 255));
        tf33.setText("MALİYET:");
        tf33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf33ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("İnşaat Sahibi");
        jLabel6.setAutoscrolls(true);

        mm.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(demir_a, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(demir_m, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(69, 69, 69)
                .addComponent(beton, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(beton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t1, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(tugla_m, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tf9, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                .addComponent(tf8)
                                .addComponent(tf7))
                            .addComponent(jLabel4)))
                    .addComponent(l2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(mm, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(71, 71, 71))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(tugla_adeti, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(59, 59, 59))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(t3, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                .addComponent(t2)))))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2)
                                .addComponent(tf5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tf6, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(tf11)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel6))
                            .addComponent(tf22, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tf33, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(74, 74, 74))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(l2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 385, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(beton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(beton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tf22)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf11, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(tf5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tf33, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tf7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(tf9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(t2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(tf8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(8, 8, 8)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(t3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(tugla_m, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(demir_a, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tugla_adeti, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(22, 22, 22))
                                    .addComponent(mm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(demir_m, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void b1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        NumberFormat paraFormat = NumberFormat.getCurrencyInstance(new Locale("tr", "TR"));

        //tuğla maliyeti hesaplama
        double m=Double.parseDouble(tugla_adeti.getText());
        double tugla_alan=Double.parseDouble(tf11.getText())/m;
        double tugla_mm=Double.parseDouble(tugla_m.getText());
        double tugla_maliyeti=tugla_alan*tugla_mm;
        long tugla_maliyeti_long = (long) tugla_maliyeti;
        //sonuc yaz
        String sonuc = paraFormat.format(tugla_maliyeti_long);
        t3.setText(sonuc);
        
        //beton maliyet hesaplama
        double a=Double.parseDouble(beton.getText());
        double b=Double.parseDouble(beton2.getText());
        double beton_metreküpü=b*Double.parseDouble(tf5.getText())* Double.parseDouble(tf6.getText());
        double beton_maliyeti=beton_metreküpü*a;
        long beton_maliyeti_long=(long)beton_maliyeti;
        
        //sonuc ₺ cevirme
        String sonuc1 = paraFormat.format(beton_maliyeti_long);

        // Sonucu yaz
        t2.setText(sonuc1);
        
        //demir maliyet hesaplama
        double c=Double.parseDouble(demir_a.getText());
        double d=Double.parseDouble(demir_m.getText());
        double demir_agirligi=c*Double.parseDouble(tf5.getText())* Double.parseDouble(tf6.getText());
        double demir_maliyeti =demir_agirligi*d;
        long demir_maliyeti_long=(long)demir_maliyeti;
        //sonuc ₺ cevirme
        String sonuc2= paraFormat.format(demir_maliyeti_long);
        
        t1.setText(sonuc2);
        double sonuc3=tugla_maliyeti_long+beton_maliyeti_long+demir_maliyeti_long;
        mm.setText(String.valueOf(sonuc3));
       String sonuc4=paraFormat.format(sonuc3);
       
       tf33.setText(sonuc4);
        
        b1.setBackground(Color.GREEN);
    
        // Yazı rengini değiştir
        b1.setForeground(Color.WHITE);
         ekle();
        
        
    }//GEN-LAST:event_b1ActionPerformed

    private void t2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
         
        Ilksayfa sayfa1 = new Ilksayfa();
        sayfa1.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jButton3MouseWheelMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseWheelMoved

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void tf8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf8ActionPerformed

    private void tf33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf33ActionPerformed

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
            java.util.logging.Logger.getLogger(Ikincisayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ikincisayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ikincisayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ikincisayfa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Ikincisayfa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b1;
    private javax.swing.JTextField beton;
    private javax.swing.JTextField beton2;
    private javax.swing.JTextField demir_a;
    private javax.swing.JTextField demir_m;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel l2;
    private javax.swing.JTextField mm;
    private javax.swing.JTextField t1;
    private javax.swing.JTextField t2;
    private javax.swing.JTextField t3;
    private javax.swing.JTextField tf11;
    private javax.swing.JTextField tf22;
    private javax.swing.JTextField tf33;
    private javax.swing.JTextField tf5;
    private javax.swing.JTextField tf6;
    private javax.swing.JTextField tf7;
    private javax.swing.JTextField tf8;
    private javax.swing.JTextField tf9;
    private javax.swing.JTextField tugla_adeti;
    private javax.swing.JTextField tugla_m;
    // End of variables declaration//GEN-END:variables
}
