package view;

import config.Session;
import config.koneksi;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class formlogin extends JFrame {

    JTextField     txtUsername;
    JPasswordField txtPassword;
    JButton        btnLogin;
    JButton        btnExit;

    public formlogin() {

        setTitle("LOGIN - SISTEM INVENTORY");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // ================= PANEL UTAMA (split 2 bagian) =================
        JPanel panelUtama = new JPanel(new GridLayout(1, 2));

        // ================= PANEL KIRI (BIRU) =================
        JPanel panelKiri = new JPanel(new BorderLayout());
        panelKiri.setBackground(new Color(0, 102, 204));

        // Logo di tengah atas
        JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoWrapper.setBackground(new Color(0, 102, 204));
        logoWrapper.setBorder(BorderFactory.createEmptyBorder(40, 20, 10, 20));

        JLabel lblLogo = new JLabel();
        try {
            java.net.URL imgUrl = getClass().getResource("/images/logoSJR.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage()
                        .getScaledInstance(280, 280, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } else {
                lblLogo.setText("SJR");
                lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 60));
                lblLogo.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            lblLogo.setText("SJR");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 60));
            lblLogo.setForeground(Color.WHITE);
        }
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        logoWrapper.add(lblLogo);

        // Teks bawah panel kiri
        JPanel tekstKiri = new JPanel(new GridLayout(3, 1, 0, 6));
        tekstKiri.setBackground(new Color(0, 102, 204));
        tekstKiri.setBorder(BorderFactory.createEmptyBorder(10, 20, 50, 20));

        JLabel lblNamaToko = new JLabel("SYARIF JAYA RACING", JLabel.CENTER);
        lblNamaToko.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNamaToko.setForeground(Color.WHITE);

        JLabel lblSlogan = new JLabel("DI GASPOL DANGAK-DANGAK", JLabel.CENTER);
        lblSlogan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSlogan.setForeground(new Color(255, 255, 255, 200));

        JPanel subPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        subPanel.setBackground(new Color(0, 102, 204));

        JLabel lblSistem = new JLabel("SISTEM INVENTORY", JLabel.CENTER);
        lblSistem.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSistem.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel(
            "Management Barang & Penjualan Spepart", JLabel.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(255, 255, 255, 180));

        subPanel.add(lblSistem);
        subPanel.add(lblDesc);

        tekstKiri.add(lblNamaToko);
        tekstKiri.add(lblSlogan);
        tekstKiri.add(subPanel);

        panelKiri.add(logoWrapper, BorderLayout.CENTER);
        panelKiri.add(tekstKiri,   BorderLayout.SOUTH);

        // ================= PANEL KANAN (PUTIH - FORM LOGIN) =================
        JPanel panelKanan = new JPanel(new GridBagLayout());
        panelKanan.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets    = new Insets(10, 50, 10, 50);
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1.0;

        // Judul LOGIN
        JLabel lblTitle = new JLabel("LOGIN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTitle.setForeground(new Color(0, 102, 204));
        gbc.insets = new Insets(60, 50, 30, 50);
        panelKanan.add(lblTitle, gbc);

        // Label Username
        gbc.insets = new Insets(10, 50, 4, 50);
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(new Color(50, 50, 50));
        panelKanan.add(lblUsername, gbc);

        // Field Username
        gbc.insets = new Insets(0, 50, 16, 50);
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(350, 45));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        panelKanan.add(txtUsername, gbc);

        // Label Password
        gbc.insets = new Insets(10, 50, 4, 50);
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(new Color(50, 50, 50));
        panelKanan.add(lblPassword, gbc);

        // Field Password
        gbc.insets = new Insets(0, 50, 30, 50);
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(350, 45));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        panelKanan.add(txtPassword, gbc);

        // Panel Tombol LOGIN & EXIT
        gbc.insets = new Insets(0, 50, 20, 50);
        JPanel panelTombol = new JPanel(new GridLayout(1, 2, 16, 0));
        panelTombol.setBackground(Color.WHITE);

        btnLogin = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0, 80, 160));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0, 120, 220));
                } else {
                    g2.setColor(new Color(0, 102, 204));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setOpaque(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(160, 50));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnExit = new JButton("EXIT") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(180, 30, 50));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(220, 60, 80));
                } else {
                    g2.setColor(new Color(200, 40, 60));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExit.setForeground(Color.WHITE);
        btnExit.setOpaque(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.setPreferredSize(new Dimension(160, 50));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelTombol.add(btnLogin);
        panelTombol.add(btnExit);
        panelKanan.add(panelTombol, gbc);

        // ================= GABUNG =================
        panelUtama.add(panelKiri);
        panelUtama.add(panelKanan);
        add(panelUtama);

        // ================= EVENT =================
        btnLogin.addActionListener(e -> login());

        // Enter di password langsung login
        txtPassword.addActionListener(e -> login());

        // Enter di username pindah ke password
        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        btnExit.addActionListener(e -> System.exit(0));
    }

    // ================= METHOD LOGIN =================
    void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "Username dan password tidak boleh kosong!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Connection conn = koneksi.configDB();

            // Query sesuai struktur tb_user di database:
            // kolom: id_user, username, password, nama_lengkap, level, role
            PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM tb_user "
                + "WHERE username=? AND password=?"
            );
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Simpan ke Session sesuai kolom database
                Session.username = rs.getString("username");
                Session.role     = rs.getString("role");
                Session.nama     = rs.getString("nama_lengkap");

                JOptionPane.showMessageDialog(
                    null,
                    "Selamat datang, " + Session.nama
                    + "!\nLogin sebagai: " + Session.role,
                    "Login Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
                );

                new MenuUtama().setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Username atau password salah!",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE
                );
                txtPassword.setText("");
                txtPassword.requestFocus();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Koneksi database gagal!\n" + e.getMessage(),
                "Error Koneksi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new formlogin().setVisible(true);
    }
}