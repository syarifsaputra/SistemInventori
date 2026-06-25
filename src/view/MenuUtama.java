package view;

import config.koneksi;
import config.Session;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class MenuUtama extends JFrame {

    JButton btnKategori;
    JButton btnBarang;
    JButton btnCustomer;
    JButton btnTransaksi;
    JButton btnLogout;

    // ===== LABEL STATS REAL-TIME =====
    private JLabel valKategori;
    private JLabel valBarang;
    private JLabel valCustomer;
    private JLabel valTransaksi;

    // ===== TIMER REAL-TIME =====
    private javax.swing.Timer timerRefresh;

    public MenuUtama() {

        setTitle("Sistem Inventory - Syarif Jaya Racing");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(Color.WHITE);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 204));
        header.setPreferredSize(new Dimension(1000, 110));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        // ---- KIRI: Logo + Brand ----
        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        headerLeft.setOpaque(false);

        JLabel logoPanel = new JLabel();
        try {
            java.net.URL imgUrl = getClass().getResource("/images/logoSJR.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(62, 62, Image.SCALE_SMOOTH);
                logoPanel.setIcon(new ImageIcon(img));
            } else {
                logoPanel.setText("SJR");
                logoPanel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                logoPanel.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            logoPanel.setText("SJR");
            logoPanel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            logoPanel.setForeground(Color.WHITE);
        }
        logoPanel.setPreferredSize(new Dimension(62, 62));
        logoPanel.setHorizontalAlignment(JLabel.CENTER);

        JPanel brandPanel = new JPanel(new GridLayout(3, 1, 0, 2));
        brandPanel.setOpaque(false);

        JLabel lblJudul = new JLabel("TOKO SYARIF JAYA RACING");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(Color.WHITE);

        JLabel lblSlogan = new JLabel("Di Gaspol Dangak-Dangak  —  Sistem Inventory");
        lblSlogan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSlogan.setForeground(new Color(255, 255, 255, 180));

        JLabel lblUserInfo = new JLabel(
            "Login sebagai: " + Session.role + "  |  " + Session.nama
        );
        lblUserInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUserInfo.setForeground(new Color(255, 220, 100));

        brandPanel.add(lblJudul);
        brandPanel.add(lblSlogan);
        brandPanel.add(lblUserInfo);

        headerLeft.add(logoPanel);
        headerLeft.add(brandPanel);

        // ---- KANAN: Tombol Logout ----
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headerRight.setOpaque(false);

        btnLogout = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(255, 255, 255, 60));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40));
                } else {
                    g2.setColor(new Color(255, 255, 255, 20));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(255, 255, 255, 150));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setOpaque(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(110, 38));
        btnLogout.setHorizontalAlignment(SwingConstants.CENTER);

        headerRight.add(btnLogout);

        header.add(headerLeft,  BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        JPanel headerWrapper = new JPanel(new GridBagLayout());
        headerWrapper.setBackground(new Color(0, 102, 204));
        headerWrapper.setPreferredSize(new Dimension(1000, 110));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        headerWrapper.add(header, gbc);

        // ================= STATS BAR REAL-TIME =================
        JPanel statsBar = new JPanel(new GridLayout(1, 4, 0, 0));
        statsBar.setBackground(new Color(248, 250, 255));
        statsBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 25)),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        statsBar.setPreferredSize(new Dimension(1000, 65));

        // Buat panel stat dengan referensi label value
        JPanel[] statPanels = new JPanel[4];
        statPanels[0] = buatStatItemRealtime("\uD83D\uDCC2", "Kategori",
                new Color(230, 241, 251), "kategori");
        statPanels[1] = buatStatItemRealtime("\uD83D\uDCE6", "Barang",
                new Color(234, 243, 222), "barang");
        statPanels[2] = buatStatItemRealtime("\uD83D\uDC64", "Customer",
                new Color(250, 238, 218), "customer");
        statPanels[3] = buatStatItemRealtime("\uD83E\uDDFE", "Transaksi",
                new Color(250, 236, 231), "transaksi");

        for (JPanel p : statPanels) statsBar.add(p);

        // ================= CONTENT =================
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(24, 30, 20, 30));

        JLabel sectionLabel = new JLabel("MENU UTAMA");
        sectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sectionLabel.setForeground(new Color(140, 140, 140));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel gridMenu = new JPanel(new GridLayout(2, 2, 14, 14));
        gridMenu.setBackground(Color.WHITE);

        btnKategori  = buatMenuCard("\uD83D\uDCC2", "Data Kategori",
                "Kelola kategori produk spare part",
                new Color(0, 102, 204));
        btnBarang    = buatMenuCard("\uD83D\uDCE6", "Data Barang",
                "Tambah, edit, dan kelola stok produk",
                new Color(59, 109, 17));
        btnCustomer  = buatMenuCard("\uD83D\uDC64", "Data Customer",
                "Kelola data pelanggan toko",
                new Color(186, 117, 23));
        btnTransaksi = buatMenuCard("\uD83E\uDDFE", "Data Transaksi",
                "Catat penjualan dan riwayat transaksi",
                new Color(216, 90, 48));

        gridMenu.add(btnKategori);
        gridMenu.add(btnBarang);
        gridMenu.add(btnCustomer);
        gridMenu.add(btnTransaksi);

        // ---- Footer ----
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0, 0, 0, 20)),
                BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));

        JLabel lblFooterL = new JLabel("\u25CF Sistem berjalan normal");
        lblFooterL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooterL.setForeground(new Color(59, 109, 17));

        JLabel lblFooterR = new JLabel("Syarif Jaya Racing \u00A9 2026");
        lblFooterR.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooterR.setForeground(new Color(160, 160, 160));

        footer.add(lblFooterL, BorderLayout.WEST);
        footer.add(lblFooterR, BorderLayout.EAST);

        content.add(sectionLabel, BorderLayout.NORTH);
        content.add(gridMenu,     BorderLayout.CENTER);
        content.add(footer,       BorderLayout.SOUTH);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(headerWrapper, BorderLayout.NORTH);
        northPanel.add(statsBar,      BorderLayout.SOUTH);

        panelUtama.add(northPanel, BorderLayout.NORTH);
        panelUtama.add(content,    BorderLayout.CENTER);
        add(panelUtama);

        // ================= EVENTS =================
        btnKategori.addActionListener(e -> {
            new FormKategori().setVisible(true);
            refreshStats(); // refresh setelah buka form
        });

        btnBarang.addActionListener(e -> {
            new FormBarang().setVisible(true);
            refreshStats();
        });

        btnCustomer.addActionListener(e -> {
            new FormCustomer().setVisible(true);
            refreshStats();
        });

        btnTransaksi.addActionListener(e -> {
            new FormTransaksi().setVisible(true);
            refreshStats();
        });

        btnLogout.addActionListener(e -> {
            int jawab = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin banget nih, logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (jawab == JOptionPane.YES_OPTION) {
                stopTimer();
                Session.username = "";
                Session.role     = "";
                Session.nama     = "";
                new formlogin().setVisible(true);
                dispose();
            }
        });

        // ================= TIMER REAL-TIME (setiap 5 detik) =================
        timerRefresh = new javax.swing.Timer(5000, e -> refreshStats());
        timerRefresh.start();

        // Load pertama kali langsung
        refreshStats();

        // Stop timer saat window ditutup
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopTimer();
            }
        });
    }

    // ================================================================
    // AMBIL DATA DARI DB — QUERY COUNT
    // ================================================================
    private int queryCount(String sql) {
        try {
            Connection conn = koneksi.configDB();
            Statement st    = conn.createStatement();
            ResultSet rs    = st.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            // silent — tetap tampil 0 jika DB error
        }
        return 0;
    }

    // Khusus transaksi: pakai tb_penjualan sesuai database
    private int queryCountTransaksi() {
        return queryCount("SELECT COUNT(*) FROM tb_penjualan");
    }

    // ================================================================
    // REFRESH SEMUA STATS DARI DATABASE
    // ================================================================
    private void refreshStats() {
        SwingUtilities.invokeLater(() -> {
            int jmlKategori  = queryCount("SELECT COUNT(*) FROM tb_kategori");
            int jmlBarang    = queryCount("SELECT COUNT(*) FROM tb_barang");
            int jmlCustomer  = queryCount("SELECT COUNT(*) FROM tb_customer");
            int jmlTransaksi = queryCountTransaksi();

            if (valKategori  != null) valKategori.setText(jmlKategori  + " Data");
            if (valBarang    != null) valBarang.setText(jmlBarang    + " Item");
            if (valCustomer  != null) valCustomer.setText(jmlCustomer  + " Orang");
            if (valTransaksi != null) valTransaksi.setText(jmlTransaksi + " Transaksi");
        });
    }

    private void stopTimer() {
        if (timerRefresh != null && timerRefresh.isRunning()) {
            timerRefresh.stop();
        }
    }

    // ================================================================
    // HELPER: STAT ITEM DENGAN LABEL REAL-TIME
    // ================================================================
    JPanel buatStatItemRealtime(String emoji, String label,
                                Color bgColor, String tipe) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(new Color(248, 250, 255));

        // Icon box
        JPanel iconBox = new JPanel(new BorderLayout());
        iconBox.setPreferredSize(new Dimension(36, 36));
        iconBox.setBackground(bgColor);
        iconBox.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JLabel ico = new JLabel(emoji, JLabel.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconBox.add(ico);

        // Text box
        JPanel textBox = new JPanel(new GridLayout(2, 1, 0, 1));
        textBox.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(130, 130, 130));

        // Value label yang akan diupdate real-time
        JLabel val = new JLabel("...");
        val.setFont(new Font("Segoe UI", Font.BOLD, 15));
        val.setForeground(new Color(30, 30, 30));

        // Simpan referensi sesuai tipe
        switch (tipe) {
            case "kategori":  valKategori  = val; break;
            case "barang":    valBarang    = val; break;
            case "customer":  valCustomer  = val; break;
            case "transaksi": valTransaksi = val; break;
        }

        textBox.add(lbl);
        textBox.add(val);

        panel.add(iconBox);
        panel.add(textBox);
        return panel;
    }

    // ================================================================
    // HELPER: MENU CARD
    // ================================================================
    JButton buatMenuCard(String emoji, String title,
                         String desc, Color accentColor) {

        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setLayout(new BorderLayout(0, 8));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        JLabel lblIcon = new JLabel(emoji);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel txtPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        txtPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 30, 30));

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(130, 130, 130));

        txtPanel.add(lblTitle);
        txtPanel.add(lblDesc);

        JLabel lblArrow = new JLabel("\u2192");
        lblArrow.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblArrow.setForeground(new Color(180, 180, 180));
        lblArrow.setHorizontalAlignment(JLabel.RIGHT);

        btn.add(lblIcon,  BorderLayout.NORTH);
        btn.add(txtPanel, BorderLayout.CENTER);
        btn.add(lblArrow, BorderLayout.EAST);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accentColor, 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                lblArrow.setForeground(accentColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 220, 235), 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                lblArrow.setForeground(new Color(180, 180, 180));
            }
        });

        return btn;
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new MenuUtama().setVisible(true);
    }
}