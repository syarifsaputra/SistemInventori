package view;

import config.Session;
import config.koneksi;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormCustomer extends JFrame {

    JTextField txtIdCustomer;
    JTextField txtNamaCustomer;
    JTextArea  txtAlamat;
    JTextField txtTelepon;
    JTextField txtCari;

    JButton btnSimpan;
    JButton btnEdit;
    JButton btnHapus;
    JButton btnBatal;
    JButton btnMenu;
    JButton btnReset;

    JTable         table;
    DefaultTableModel model;

    public FormCustomer() {

        setTitle("FORM DATA CUSTOMER");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // =====================================================
        // ROOT
        // =====================================================
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(240, 243, 248));

        // =====================================================
        // HEADER
        // =====================================================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 204));
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel lblTitle = new JLabel("DATA CUSTOMER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel(
            "Login: " + Session.username + "  |  " + Session.role
        );
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUser.setForeground(new Color(255, 255, 255, 200));
        lblUser.setHorizontalAlignment(JLabel.RIGHT);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblUser,  BorderLayout.EAST);

        // =====================================================
        // BODY — kiri (input) + kanan (tabel)
        // =====================================================
        JPanel body = new JPanel(new GridLayout(1, 2, 12, 0));
        body.setBackground(new Color(240, 243, 248));
        body.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // =====================================================
        // PANEL KIRI — Form Input
        // =====================================================
        JPanel panelKiri = new JPanel(new BorderLayout(0, 10));
        panelKiri.setBackground(new Color(240, 243, 248));

        // --- Card Input ---
        JPanel cardInput = buatCard();
        cardInput.setLayout(new BorderLayout(0, 14));

        JLabel lblInputJudul = new JLabel("Input Data Customer");
        lblInputJudul.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblInputJudul.setForeground(new Color(0, 102, 204));

        // Grid form 4 baris
        JPanel gridInput = new JPanel(new GridLayout(4, 2, 10, 12));
        gridInput.setOpaque(false);

        txtIdCustomer   = new JTextField();
        txtNamaCustomer = new JTextField();
        txtAlamat       = new JTextArea(3, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtTelepon      = new JTextField();

        txtIdCustomer.setEditable(false);
        txtIdCustomer.setBackground(new Color(245, 246, 248));

        styleField(txtIdCustomer);
        styleField(txtNamaCustomer);
        styleField(txtTelepon);

        // =====================================================
        // VALIDASI INPUT: Nama hanya huruf, Telepon hanya angka
        // =====================================================
        txtNamaCustomer.addKeyListener(hanyaHuruf());
        txtTelepon.addKeyListener(hanyaAngka());

        txtAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtAlamat.setBorder(BorderFactory.createEmptyBorder(5, 9, 5, 9));

        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.setBorder(BorderFactory.createLineBorder(
            new Color(200, 208, 220), 1, true));

        gridInput.add(buatLabel("ID Customer"));   gridInput.add(txtIdCustomer);
        gridInput.add(buatLabel("Nama Customer")); gridInput.add(txtNamaCustomer);
        gridInput.add(buatLabel("Alamat"));        gridInput.add(scrollAlamat);
        gridInput.add(buatLabel("Telepon"));       gridInput.add(txtTelepon);

        cardInput.add(lblInputJudul, BorderLayout.NORTH);
        cardInput.add(gridInput,     BorderLayout.CENTER);

        // --- Card Info Hak Akses ---
        JPanel cardInfo = buatCard();
        cardInfo.setLayout(new BorderLayout());

        String infoText = Session.role != null && Session.role.equalsIgnoreCase("Kasir")
            ? "ℹ  Mode Kasir: Hanya dapat menambah customer baru"
            : "ℹ  Mode Admin: Akses penuh CRUD";
        JLabel lblInfo = new JLabel(infoText);
        lblInfo.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 12));
        lblInfo.setForeground(Session.role != null && Session.role.equalsIgnoreCase("Kasir")
            ? new Color(130, 80, 0)
            : new Color(0, 100, 50));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        cardInfo.add(lblInfo, BorderLayout.CENTER);
        cardInfo.setBackground(Session.role != null && Session.role.equalsIgnoreCase("Kasir")
            ? new Color(255, 243, 220)
            : new Color(220, 245, 230));
        cardInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Session.role != null && Session.role.equalsIgnoreCase("Kasir")
                ? new Color(255, 200, 100)
                : new Color(100, 200, 130), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // --- Tombol Aksi ---
        JPanel panelAksi = new JPanel(new GridLayout(1, 4, 8, 0));
        panelAksi.setBackground(new Color(240, 243, 248));
        panelAksi.setPreferredSize(new Dimension(0, 44));

        btnSimpan = buatTombol("💾  SIMPAN", new Color(0, 153, 76));
        btnEdit   = buatTombol("✏  EDIT",   new Color(255, 153, 0));
        btnHapus  = buatTombol("🗑  HAPUS",  new Color(220, 53, 69));
        btnBatal  = buatTombol("↺  BATAL",  new Color(108, 117, 125));

        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBatal.setEnabled(false);

        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            btnEdit.setVisible(false);
            btnHapus.setVisible(false);
        }

        panelAksi.add(btnSimpan);
        panelAksi.add(btnEdit);
        panelAksi.add(btnHapus);
        panelAksi.add(btnBatal);

        // Gabung kiri
        JPanel kiriTengah = new JPanel(new BorderLayout(0, 10));
        kiriTengah.setBackground(new Color(240, 243, 248));
        kiriTengah.add(cardInput, BorderLayout.CENTER);
        kiriTengah.add(cardInfo,  BorderLayout.SOUTH);

        panelKiri.add(kiriTengah, BorderLayout.CENTER);
        panelKiri.add(panelAksi,  BorderLayout.SOUTH);

        // =====================================================
        // PANEL KANAN — Tabel Data Customer
        // =====================================================
        JPanel panelKanan = new JPanel(new BorderLayout(0, 10));
        panelKanan.setBackground(new Color(240, 243, 248));

        // --- Card Tabel ---
        JPanel cardTabel = buatCard();
        cardTabel.setLayout(new BorderLayout(0, 10));

        // Header tabel + search
        JPanel topTabel = new JPanel(new BorderLayout(0, 6));
        topTabel.setOpaque(false);

        JLabel lblTabelJudul = new JLabel("📋  Daftar Customer");
        lblTabelJudul.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        lblTabelJudul.setForeground(new Color(0, 102, 204));

        // Search bar
        JPanel panelSearch = new JPanel(new BorderLayout(6, 0));
        panelSearch.setOpaque(false);

        JLabel lblIkon = new JLabel("🔍");
        lblIkon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        lblIkon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

        txtCari = new JTextField();
        txtCari.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCari.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 195, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnReset.setBackground(new Color(108, 117, 125));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        btnReset.setBorderPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.setPreferredSize(new Dimension(70, 32));

        panelSearch.add(lblIkon,  BorderLayout.WEST);
        panelSearch.add(txtCari,  BorderLayout.CENTER);
        panelSearch.add(btnReset, BorderLayout.EAST);

        topTabel.add(lblTabelJudul, BorderLayout.NORTH);
        topTabel.add(panelSearch,   BorderLayout.SOUTH);

        // Tabel
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        model.addColumn("ID Customer");
        model.addColumn("Nama Customer");
        model.addColumn("Alamat");
        model.addColumn("Telepon");

        table = new JTable(model);
        styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(220);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);

        JScrollPane scrollTabel = new JScrollPane(table);
        scrollTabel.setBorder(BorderFactory.createEmptyBorder());

        // Statistik bawah tabel
        JPanel panelStat = new JPanel(new BorderLayout());
        panelStat.setBackground(new Color(0, 102, 204));
        panelStat.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        JLabel lblStatTxt = new JLabel("Total Customer Terdaftar");
        lblStatTxt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatTxt.setForeground(Color.WHITE);

        JLabel lblStatVal = new JLabel("0 Customer");
        lblStatVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatVal.setForeground(new Color(255, 220, 80));
        lblStatVal.setHorizontalAlignment(JLabel.RIGHT);

        panelStat.add(lblStatTxt, BorderLayout.WEST);
        panelStat.add(lblStatVal, BorderLayout.EAST);

        cardTabel.add(topTabel,    BorderLayout.NORTH);
        cardTabel.add(scrollTabel, BorderLayout.CENTER);
        cardTabel.add(panelStat,   BorderLayout.SOUTH);

        // Tombol kanan bawah
        JPanel panelKananBawah = new JPanel(new GridLayout(1, 1, 8, 0));
        panelKananBawah.setBackground(new Color(240, 243, 248));
        panelKananBawah.setPreferredSize(new Dimension(0, 44));

        btnMenu = buatTombol("⬅  MENU", new Color(52, 58, 64));
        panelKananBawah.add(btnMenu);

        panelKanan.add(cardTabel,       BorderLayout.CENTER);
        panelKanan.add(panelKananBawah, BorderLayout.SOUTH);

        // =====================================================
        // FOOTER
        // =====================================================
        JLabel lblFooter = new JLabel(
            "  💡  Nama: huruf saja | Telepon: angka saja | Klik baris tabel untuk Edit/Hapus"
        );
        lblFooter.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 11));
        lblFooter.setForeground(new Color(100, 110, 130));
        lblFooter.setBackground(Color.WHITE);
        lblFooter.setOpaque(true);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));

        // =====================================================
        // GABUNG
        // =====================================================
        body.add(panelKiri);
        body.add(panelKanan);

        root.add(header,    BorderLayout.NORTH);
        root.add(body,      BorderLayout.CENTER);
        root.add(lblFooter, BorderLayout.SOUTH);
        add(root);

        // =====================================================
        // LOAD DATA
        // =====================================================
        tampilData(lblStatVal);
        autoID();

        // =====================================================
        // EVENTS
        // =====================================================
        btnSimpan.addActionListener(e -> simpan(lblStatVal));
        btnEdit.addActionListener(e -> edit(lblStatVal));
        btnHapus.addActionListener(e -> hapus(lblStatVal));
        btnBatal.addActionListener(e -> batal());

        btnMenu.addActionListener(e -> {
            new MenuUtama().setVisible(true);
            dispose();
        });

        btnReset.addActionListener(e -> {
            txtCari.setText("");
            tampilData(lblStatVal);
        });

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                cariData(lblStatVal);
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                klikTabel();
            }
        });
    }

    // =====================================================
    // VALIDASI: Hanya Huruf dan Spasi (Nama Customer)
    // =====================================================
    java.awt.event.KeyAdapter hanyaHuruf() {
        return new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                // Izinkan: huruf (a-z, A-Z), spasi, dan karakter kontrol (backspace, delete, dll)
                if (!Character.isLetter(c) && c != ' ' && !Character.isISOControl(c)) {
                    e.consume(); // Blokir karakter yang tidak sesuai
                    Toolkit.getDefaultToolkit().beep(); // Bunyi peringatan
                }
            }
        };
    }

    // =====================================================
    // VALIDASI: Hanya Angka (Telepon)
    // =====================================================
    java.awt.event.KeyAdapter hanyaAngka() {
        return new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                // Izinkan: angka 0-9 dan karakter kontrol (backspace, delete, dll)
                if (!Character.isDigit(c) && !Character.isISOControl(c)) {
                    e.consume(); // Blokir karakter yang tidak sesuai
                    Toolkit.getDefaultToolkit().beep(); // Bunyi peringatan
                }
            }
        };
    }

    // =====================================================
    // HELPERS UI
    // =====================================================
    JPanel buatCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 222, 235), 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        return card;
    }

    JLabel buatLabel(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 65, 75));
        return lbl;
    }

    JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (!isEnabled()) {
                    g2.setColor(new Color(180, 180, 185));
                } else if (getModel().isPressed()) {
                    g2.setColor(warna.darker().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(warna.darker());
                } else {
                    g2.setColor(warna);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 208, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 9, 5, 9)
        ));
    }

    void styleTable(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(27);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(0, 102, 204));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setReorderingAllowed(false);
        t.setSelectionBackground(new Color(210, 228, 255));
        t.setSelectionForeground(new Color(0, 55, 120));
        t.setGridColor(new Color(232, 236, 245));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
    }

    // =====================================================
    // TAMPIL DATA
    // =====================================================
    void tampilData(JLabel lblStat) {
        model.setRowCount(0);
        try {
            Connection conn = koneksi.configDB();
            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT * FROM tb_customer ORDER BY nama_customer");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_customer"),
                    rs.getString("nama_customer"),
                    rs.getString("alamat"),
                    rs.getString("telepon")
                });
            }
            lblStat.setText(model.getRowCount() + " Customer");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // =====================================================
    // CARI DATA
    // =====================================================
    void cariData(JLabel lblStat) {
        model.setRowCount(0);
        try {
            Connection conn = koneksi.configDB();
            PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM tb_customer "
                + "WHERE nama_customer LIKE ? "
                + "OR id_customer LIKE ? "
                + "OR telepon LIKE ? "
                + "OR alamat LIKE ? "
                + "ORDER BY nama_customer"
            );
            String kw = "%" + txtCari.getText() + "%";
            pst.setString(1, kw); pst.setString(2, kw);
            pst.setString(3, kw); pst.setString(4, kw);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_customer"),
                    rs.getString("nama_customer"),
                    rs.getString("alamat"),
                    rs.getString("telepon")
                });
            }
            lblStat.setText(model.getRowCount() + " Customer");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // =====================================================
    // SIMPAN
    // =====================================================
    void simpan(JLabel lblStat) {
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            JOptionPane.showMessageDialog(null, "Akses ditolak! Kasir tidak bisa menambah data.");
            return;
        }
        if (txtNamaCustomer.getText().trim().isEmpty()
                || txtAlamat.getText().trim().isEmpty()
                || txtTelepon.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Data belum lengkap!", "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Validasi tambahan saat simpan (double-check)
        if (!txtNamaCustomer.getText().matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null,
                "Nama Customer hanya boleh berisi huruf dan spasi!",
                "Format Salah", JOptionPane.WARNING_MESSAGE);
            txtNamaCustomer.requestFocus();
            return;
        }
        if (!txtTelepon.getText().matches("[0-9]+")) {
            JOptionPane.showMessageDialog(null,
                "Nomor Telepon hanya boleh berisi angka!",
                "Format Salah", JOptionPane.WARNING_MESSAGE);
            txtTelepon.requestFocus();
            return;
        }
        try {
            Connection conn = koneksi.configDB();
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO tb_customer VALUES(?,?,?,?)"
            );
            pst.setString(1, txtIdCustomer.getText());
            pst.setString(2, txtNamaCustomer.getText());
            pst.setString(3, txtAlamat.getText());
            pst.setString(4, txtTelepon.getText());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,
                "✅ Data customer berhasil disimpan!",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            tampilData(lblStat);
            kosong();
            autoID();
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            btnBatal.setEnabled(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // =====================================================
    // EDIT
    // =====================================================
    void edit(JLabel lblStat) {
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            JOptionPane.showMessageDialog(null, "Akses ditolak! Kasir tidak bisa mengedit data.");
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data dulu!");
            return;
        }
        // Validasi tambahan saat edit (double-check)
        if (!txtNamaCustomer.getText().matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null,
                "Nama Customer hanya boleh berisi huruf dan spasi!",
                "Format Salah", JOptionPane.WARNING_MESSAGE);
            txtNamaCustomer.requestFocus();
            return;
        }
        if (!txtTelepon.getText().matches("[0-9]+")) {
            JOptionPane.showMessageDialog(null,
                "Nomor Telepon hanya boleh berisi angka!",
                "Format Salah", JOptionPane.WARNING_MESSAGE);
            txtTelepon.requestFocus();
            return;
        }
        String id = table.getValueAt(row, 0).toString();
        try {
            Connection conn = koneksi.configDB();
            PreparedStatement pst = conn.prepareStatement(
                "UPDATE tb_customer SET "
                + "nama_customer=?, alamat=?, telepon=? "
                + "WHERE id_customer=?"
            );
            pst.setString(1, txtNamaCustomer.getText());
            pst.setString(2, txtAlamat.getText());
            pst.setString(3, txtTelepon.getText());
            pst.setString(4, id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,
                "✅ Data berhasil diupdate!",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            tampilData(lblStat);
            kosong();
            autoID();
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            btnBatal.setEnabled(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // =====================================================
    // HAPUS
    // =====================================================
    void hapus(JLabel lblStat) {
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            JOptionPane.showMessageDialog(null, "Akses ditolak! Kasir tidak bisa menghapus data.");
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data dulu!");
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(null,
            "Yakin ingin menghapus customer ini?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (jawab != JOptionPane.YES_OPTION) return;

        String id = table.getValueAt(row, 0).toString();
        try {
            Connection conn = koneksi.configDB();
            PreparedStatement pst = conn.prepareStatement(
                "DELETE FROM tb_customer WHERE id_customer=?"
            );
            pst.setString(1, id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "✅ Data berhasil dihapus!");
            tampilData(lblStat);
            kosong();
            autoID();
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            btnBatal.setEnabled(false);

        } catch (Exception e) {
            if (e.getMessage().contains("foreign key constraint")) {
                JOptionPane.showMessageDialog(null,
                    "⚠ Customer sudah digunakan dalam transaksi!",
                    "Tidak Bisa Dihapus", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    // =====================================================
    // KLIK TABEL
    // =====================================================
    void klikTabel() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtIdCustomer.setText(table.getValueAt(row, 0).toString());
        txtNamaCustomer.setText(table.getValueAt(row, 1).toString());
        txtAlamat.setText(table.getValueAt(row, 2).toString());
        txtTelepon.setText(table.getValueAt(row, 3).toString());

        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            btnBatal.setEnabled(true);
            btnSimpan.setEnabled(false);
        } else {
            btnEdit.setEnabled(true);
            btnHapus.setEnabled(true);
            btnBatal.setEnabled(true);
            btnSimpan.setEnabled(false);
        }
    }

    // =====================================================
    // KOSONG
    // =====================================================
    void kosong() {
        txtNamaCustomer.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
    }

    // =====================================================
    // BATAL
    // =====================================================
    void batal() {
        kosong();
        autoID();
        table.clearSelection();
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            btnSimpan.setEnabled(false);
        } else {
            btnSimpan.setEnabled(true);
        }
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBatal.setEnabled(false);
    }

    // =====================================================
    // AUTO ID
    // =====================================================
    void autoID() {
        try {
            Connection conn = koneksi.configDB();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id_customer FROM tb_customer "
                + "ORDER BY id_customer DESC LIMIT 1"
            );
            if (rs.next()) {
                String id  = rs.getString("id_customer").substring(3);
                int nomor  = Integer.parseInt(id) + 1;
                String nol = nomor < 10 ? "00"
                           : nomor < 100 ? "0" : "";
                txtIdCustomer.setText("CST" + nol + nomor);
            } else {
                txtIdCustomer.setText("CST001");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void main(String[] args) {
        new FormCustomer().setVisible(true);
    }
}