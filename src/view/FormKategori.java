package view;

import config.koneksi;
import config.Session;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class FormKategori extends JFrame {

    // ===== KOMPONEN FORM =====
    private JTextField txtNamaKategori;
    private JTextField txtSearch;

    // ===== TOMBOL =====
    private JButton btnSimpan;
    private JButton btnEdit;
    private JButton btnHapus;
    private JButton btnBatal;
    private JButton btnMenu;
    private JButton btnReset;

    // ===== TABEL =====
    private JTable tabelKategori;
    private DefaultTableModel modelTabel;

    // ===== STATE =====
    private String idKategori = "";

    // ===== LABEL JUMLAH =====
    private JLabel lblJumlahKategori;

    // ===== WARNA TEMA =====
    private final Color BIRU_UTAMA = new Color(0, 102, 204);
    private final Color BIRU_MUDA  = new Color(230, 241, 255);
    private final Color HIJAU      = new Color(0, 153, 76);
    private final Color MERAH      = new Color(220, 53, 69);
    private final Color ABU_TUA    = new Color(80, 80, 80);
    private final Color BORDER_CLR = new Color(210, 220, 235);

    public FormKategori() {
        setTitle("FORM DATA KATEGORI");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(Color.WHITE);

        panelUtama.add(buatHeader(),      BorderLayout.NORTH);
        panelUtama.add(buatBodyPanel(),   BorderLayout.CENTER);
        panelUtama.add(buatPanelTombol(), BorderLayout.SOUTH);

        add(panelUtama);

        pasangEvents();
        tampilData();
        setModeDefault();
    }

    // ================================================================
    // HEADER
    // ================================================================
    private JPanel buatHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BIRU_UTAMA);
        header.setPreferredSize(new Dimension(1000, 80));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JLabel lblJudul = new JLabel("DATA KATEGORI");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblJudul.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel(
            "Login: " + Session.username + "  |  " + Session.role
        );
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(220, 235, 255));

        JPanel headerWrapper = new JPanel(new GridBagLayout());
        headerWrapper.setBackground(BIRU_UTAMA);
        headerWrapper.setPreferredSize(new Dimension(1000, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        header.add(lblJudul, BorderLayout.WEST);
        header.add(lblUser,  BorderLayout.EAST);
        headerWrapper.add(header, gbc);
        return headerWrapper;
    }

    // ================================================================
    // BODY — Split kiri + kanan
    // ================================================================
    private JSplitPane buatBodyPanel() {
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buatPanelKiri(),
                buatPanelKanan()
        );
        split.setDividerLocation(400);
        split.setDividerSize(1);
        split.setBorder(null);
        return split;
    }

    // ================================================================
    // PANEL KIRI — Form Input
    // ================================================================
    private JPanel buatPanelKiri() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel lblSeksi = new JLabel("Input Data Kategori");
        lblSeksi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeksi.setForeground(BIRU_UTAMA);
        lblSeksi.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        txtNamaKategori = buatTextField(true);

        // Row 0: Nama Kategori
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.38;
        formPanel.add(buatLabel("Nama Kategori"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.62;
        formPanel.add(txtNamaKategori, gbc);

        // Info bar mode akses
        JPanel infoBar = buatInfoBar();

        JPanel northGroup = new JPanel(new BorderLayout(0, 10));
        northGroup.setBackground(Color.WHITE);
        northGroup.add(lblSeksi,   BorderLayout.NORTH);
        northGroup.add(formPanel,  BorderLayout.CENTER);
        northGroup.add(infoBar,    BorderLayout.SOUTH);

        panel.add(northGroup, BorderLayout.NORTH);
        return panel;
    }

    // ================================================================
    // PANEL KANAN — Tabel
    // ================================================================
    private JPanel buatPanelKanan() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 8, 16, 16));

        JLabel lblDaftar = new JLabel("  \uD83D\uDCC2  Daftar Kategori");
        lblDaftar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDaftar.setForeground(BIRU_UTAMA);
        lblDaftar.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(6, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel lblSearch = new JLabel("\uD83D\uDD0D");
        lblSearch.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnReset.setBackground(new Color(100, 100, 100));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        btnReset.setBorderPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.setPreferredSize(new Dimension(68, 32));

        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnReset,  BorderLayout.EAST);

        // Tabel
        String[] kolom = {"ID Kategori", "Nama Kategori"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelKategori = new JTable(modelTabel);
        tabelKategori.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        tabelKategori.setRowHeight(30);
        tabelKategori.setSelectionBackground(BIRU_MUDA);
        tabelKategori.setSelectionForeground(Color.BLACK);
        tabelKategori.setGridColor(new Color(230, 235, 245));
        tabelKategori.setShowGrid(true);
        tabelKategori.setFocusable(false);
        tabelKategori.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = tabelKategori.getTableHeader();
        tableHeader.setBackground(BIRU_UTAMA);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        tableHeader.setPreferredSize(new Dimension(0, 36));
        tableHeader.setReorderingAllowed(false);

        tabelKategori.getColumnModel().getColumn(0).setPreferredWidth(110);
        tabelKategori.getColumnModel().getColumn(1).setPreferredWidth(310);

        // Alternating row color
        tabelKategori.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE
                                                     : new Color(248, 251, 255));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(tabelKategori);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        scroll.getViewport().setBackground(Color.WHITE);

        // Footer total
        JPanel footerTabel = new JPanel(new BorderLayout());
        footerTabel.setBackground(BIRU_UTAMA);
        footerTabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        footerTabel.setPreferredSize(new Dimension(0, 42));

        JLabel lblTotal = new JLabel("Total Kategori Terdaftar");
        lblTotal.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblTotal.setForeground(Color.WHITE);

        lblJumlahKategori = new JLabel("0 Kategori");
        lblJumlahKategori.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblJumlahKategori.setForeground(new Color(255, 220, 100));

        footerTabel.add(lblTotal,          BorderLayout.WEST);
        footerTabel.add(lblJumlahKategori, BorderLayout.EAST);

        JPanel tengah = new JPanel(new BorderLayout(0, 4));
        tengah.setBackground(Color.WHITE);
        tengah.add(searchPanel, BorderLayout.NORTH);
        tengah.add(scroll,      BorderLayout.CENTER);
        tengah.add(footerTabel, BorderLayout.SOUTH);

        panel.add(lblDaftar, BorderLayout.NORTH);
        panel.add(tengah,    BorderLayout.CENTER);
        return panel;
    }

    // ================================================================
    // PANEL TOMBOL BAWAH
    // ================================================================
    private JPanel buatPanelTombol() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);

        // Kiri: CRUD
        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        kiri.setBackground(Color.WHITE);

        btnSimpan = buatTombolAksi("  \uD83D\uDCBE  SIMPAN", HIJAU,                150, 44);
        btnEdit   = buatTombolAksi("  \u270F  EDIT",         new Color(255,153,0), 130, 44);
        btnHapus  = buatTombolAksi("  \uD83D\uDDD1  HAPUS",  MERAH,               130, 44);
        btnBatal  = buatTombolAksi("  \u21BA  BATAL",        new Color(108,117,125), 120, 44);

        kiri.add(btnSimpan);
        kiri.add(btnEdit);
        kiri.add(btnHapus);
        kiri.add(btnBatal);

        // Hak akses Kasir
        if ("Kasir".equalsIgnoreCase(Session.role)) {
            btnEdit.setVisible(false);
            btnHapus.setVisible(false);
        }

        // Kanan: MENU
        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
        kanan.setBackground(new Color(50, 50, 50));
        kanan.setPreferredSize(new Dimension(185, 60));

        btnMenu = buatTombolAksi("  \u2B50  MENU", new Color(50, 50, 50), 160, 44);
        btnMenu.setForeground(Color.WHITE);
        kanan.add(btnMenu);

        JPanel tombolBar = new JPanel(new BorderLayout());
        tombolBar.setBackground(Color.WHITE);
        tombolBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));
        tombolBar.add(kiri,  BorderLayout.WEST);
        tombolBar.add(kanan, BorderLayout.EAST);

        // Hint
        JPanel hintBar = new JPanel(new BorderLayout());
        hintBar.setBackground(Color.WHITE);
        hintBar.setBorder(BorderFactory.createEmptyBorder(2, 14, 4, 14));
        JLabel lblHint = new JLabel(
            "\u25A1  Klik baris tabel untuk memilih kategori \u2192 Edit atau Hapus"
        );
        lblHint.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        lblHint.setForeground(new Color(150, 150, 150));
        hintBar.add(lblHint, BorderLayout.WEST);

        wrapper.add(tombolBar, BorderLayout.CENTER);
        wrapper.add(hintBar,   BorderLayout.SOUTH);
        return wrapper;
    }

    // ================================================================
    // INFO BAR
    // ================================================================
    private JPanel buatInfoBar() {
        boolean isAdmin = "Admin".equalsIgnoreCase(Session.role);
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        isAdmin ? new Color(59,109,17,60) : new Color(180,130,0,60),
                        1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        bar.setBackground(isAdmin ? new Color(240,250,235) : new Color(255,248,220));

        String teks  = isAdmin ? "\u24D8  Mode Admin: Akses penuh CRUD"
                               : "\u24D8  Mode Kasir: Lihat data saja";
        Color  warna = isAdmin ? new Color(40,100,10) : new Color(120,80,10);

        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI Emoji ", Font.PLAIN, 12));
        lbl.setForeground(warna);
        bar.add(lbl);
        return bar;
    }

    // ================================================================
    // HELPERS UI
    // ================================================================
    private JLabel buatLabel(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(ABU_TUA);
        return lbl;
    }

    private JTextField buatTextField(boolean editable) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setEditable(editable);
        tf.setBackground(editable ? Color.WHITE : new Color(240, 240, 240));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        tf.setPreferredSize(new Dimension(0, 38));
        return tf;
    }

    private JButton buatTombolAksi(String label, Color bg, int w, int h) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isEnabled()
                        ? (getModel().isPressed() ? bg.darker() : bg)
                        : new Color(180, 180, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    // ================================================================
    // MODE TOMBOL
    // ================================================================
    private void setModeDefault() {
        btnSimpan.setEnabled(true);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBatal.setEnabled(false);
    }

    private void setModePilih() {
        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
        btnBatal.setEnabled(true);
    }

    // ================================================================
    // LOAD DATA — SESUAI STRUKTUR ASLI: tb_kategori
    // ================================================================
    void tampilData() {
        tampilData("");
    }

    void tampilData(String keyword) {
        modelTabel.setRowCount(0);
        try {
            Connection conn = koneksi.configDB();
            String sql = "SELECT id_kategori, nama_kategori "
                       + "FROM tb_kategori "
                       + "WHERE id_kategori   LIKE ? "
                       + "   OR nama_kategori LIKE ? "
                       + "ORDER BY id_kategori";
            PreparedStatement pst = conn.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            pst.setString(1, kw);
            pst.setString(2, kw);
            ResultSet rs = pst.executeQuery();
            int jml = 0;
            while (rs.next()) {
                modelTabel.addRow(new Object[]{
                    rs.getString("id_kategori"),
                    rs.getString("nama_kategori")
                });
                jml++;
            }
            lblJumlahKategori.setText(jml + " Kategori");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // ================================================================
    // SIMPAN — INSERT ke tb_kategori (id auto increment)
    // ================================================================
    void simpan() {
        String nama = txtNamaKategori.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Nama kategori masih kosong!");
            txtNamaKategori.requestFocus();
            return;
        }
        try {
            Connection conn = koneksi.configDB();
            String sql = "INSERT INTO tb_kategori (nama_kategori) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,
                    "Data kategori berhasil disimpan!");
            kosong();
            tampilData();
            setModeDefault();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // ================================================================
    // EDIT — UPDATE tb_kategori
    // ================================================================
    void edit() {
        if (idKategori.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data dulu!");
            return;
        }
        try {
            Connection conn = koneksi.configDB();
            String sql = "UPDATE tb_kategori SET nama_kategori=? "
                       + "WHERE id_kategori=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNamaKategori.getText().trim());
            pst.setString(2, idKategori);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,
                    "Data berhasil diupdate!");
            kosong();
            tampilData();
            setModeDefault();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // ================================================================
    // HAPUS — DELETE dari tb_kategori
    // ================================================================
    void hapus() {
        if (idKategori.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data dulu!");
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(null,
                "Yakin ingin menghapus kategori ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (jawab != JOptionPane.YES_OPTION) return;

        try {
            Connection conn = koneksi.configDB();
            String sql = "DELETE FROM tb_kategori WHERE id_kategori=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idKategori);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
            kosong();
            tampilData();
            setModeDefault();
        } catch (Exception e) {
            if (e.getMessage().contains("foreign key constraint")) {
                JOptionPane.showMessageDialog(null,
                        "Kategori sudah digunakan di data barang!");
            } else {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    // ================================================================
    // KLIK TABEL
    // ================================================================
    void klikTabel() {
        int row = tabelKategori.getSelectedRow();
        if (row < 0) return;
        idKategori = modelTabel.getValueAt(row, 0).toString();
        txtNamaKategori.setText(modelTabel.getValueAt(row, 1).toString());
        setModePilih();
    }

    // ================================================================
    // KOSONG / BATAL
    // ================================================================
    void kosong() {
        txtNamaKategori.setText("");
        idKategori = "";
        tabelKategori.clearSelection();
    }

    void batal() {
        kosong();
        tampilData();
        setModeDefault();
    }

    // ================================================================
    // PASANG EVENTS
    // ================================================================
    private void pasangEvents() {

        // Klik baris tabel
        tabelKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                klikTabel();
            }
        });

        // Search real-time
        txtSearch.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
                public void insertUpdate (javax.swing.event.DocumentEvent e) { cari(); }
                public void removeUpdate (javax.swing.event.DocumentEvent e) { cari(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { cari(); }
                private void cari() { tampilData(txtSearch.getText().trim()); }
            }
        );

        btnReset.addActionListener(e -> {
            txtSearch.setText("");
            tampilData();
        });

        btnSimpan.addActionListener(e -> simpan());
        btnEdit.addActionListener(e -> edit());
        btnHapus.addActionListener(e -> hapus());
        btnBatal.addActionListener(e -> batal());

        btnMenu.addActionListener(e -> {
            new MenuUtama().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormKategori().setVisible(true));
    }
}