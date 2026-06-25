package view;

import config.koneksi;
import config.Session;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class FormBarang extends JFrame {

    JTextField txtIdBarang, txtNamaBarang, txtSatuan, txtHarga, txtStok, txtCari;
    JComboBox<String> cmbKategori;
    JButton btnSimpan, btnEdit, btnHapus, btnBatal, btnMenu;
    JTable table;
    DefaultTableModel model;

    public FormBarang() {
        // ================= SETUP FRAME =================
        setTitle("FORM DATA BARANG - Sistem Inventory");
        setSize(1200, 700); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        Font fontLabel = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

        // ================= HEADER BIRU =================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(0, 102, 204));
        pnlHeader.setPreferredSize(new Dimension(getWidth(), 70));
        pnlHeader.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitleHeader = new JLabel("DATA BARANG");
        lblTitleHeader.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitleHeader.setForeground(Color.WHITE);
        pnlHeader.add(lblTitleHeader, BorderLayout.WEST);

        // Menggunakan Session.username dan Session.role
        String userLogin = Session.username != null ? Session.username : "User";
        String roleLogin = Session.role != null ? Session.role : "Admin";
        JLabel lblUserInfo = new JLabel("Login: " + userLogin + "  |  " + roleLogin);
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUserInfo.setForeground(Color.WHITE);
        pnlHeader.add(lblUserInfo, BorderLayout.EAST);

        // ================= PANEL UTAMA =================
        JPanel pnlUtama = new JPanel(new BorderLayout(20, 20));
        pnlUtama.setBackground(new Color(245, 248, 250)); 
        pnlUtama.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ================= PANEL KIRI (INPUT) =================
        JPanel pnlKiri = new JPanel(new BorderLayout(0, 20));
        pnlKiri.setBackground(Color.WHITE);
        pnlKiri.setPreferredSize(new Dimension(450, 0));
        pnlKiri.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblJudulKiri = new JLabel("Input Data Barang");
        lblJudulKiri.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudulKiri.setForeground(new Color(0, 102, 204));
        pnlKiri.add(lblJudulKiri, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 15));
        pnlForm.setBackground(Color.WHITE);

        txtIdBarang = new JTextField(); txtIdBarang.setEditable(false);
        cmbKategori = new JComboBox<>();
        txtNamaBarang = new JTextField();
        txtSatuan = new JTextField();
        txtHarga = new JTextField();
        txtStok = new JTextField();

        JTextField[] fields = {txtIdBarang, txtNamaBarang, txtSatuan, txtHarga, txtStok};
        for (JTextField f : fields) {
            f.setFont(fontInput);
            f.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200)),
                    new EmptyBorder(5, 8, 5, 8)));
        }
        cmbKategori.setFont(fontInput);

        String[] labels = {"ID Barang", "Kategori", "Nama Barang", "Satuan", "Harga Jual (Rp)", "Stok Tersedia"};
        JComponent[] inputs = {txtIdBarang, cmbKategori, txtNamaBarang, txtSatuan, txtHarga, txtStok};
        
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(fontLabel);
            lbl.setForeground(Color.DARK_GRAY);
            pnlForm.add(lbl);
            pnlForm.add(inputs[i]);
        }
        pnlKiri.add(pnlForm, BorderLayout.CENTER);

        // --- REVISI WARNA & EMOJI BUTTON GROUP ---
        JPanel pnlButtonKiri = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtonKiri.setBackground(Color.WHITE);

        // PERBAIKAN: Menggunakan karakter emoji yang sama persis dengan FormCustomer
        btnSimpan = new JButton("💾  SIMPAN");
        btnEdit = new JButton("✏  EDIT");
        btnHapus = new JButton("🗑  HAPUS");
        btnBatal = new JButton("↺  BATAL");
        btnMenu = new JButton("☰  MENU");

        btnSimpan.setBackground(new Color(0, 102, 204)); 
        btnEdit.setBackground(new Color(40, 167, 69));   
        btnHapus.setBackground(new Color(220, 53, 69));  
        btnBatal.setBackground(new Color(240, 173, 78)); 
        btnMenu.setBackground(new Color(52, 58, 64));    

        JButton[] btns = {btnSimpan, btnEdit, btnHapus, btnBatal, btnMenu};
        for (JButton b : btns) {
            b.setForeground(Color.WHITE);
            // Pastikan font menggunakan Segoe UI Emoji
            b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

        pnlButtonKiri.add(btnSimpan);
        pnlButtonKiri.add(btnBatal);
        pnlButtonKiri.add(btnEdit);
        pnlButtonKiri.add(btnHapus);
        
        JPanel pnlMenuWrap = new JPanel(new BorderLayout());
        pnlMenuWrap.setBackground(Color.WHITE);
        pnlMenuWrap.add(btnMenu, BorderLayout.CENTER);
        
        JPanel pnlBawahKiri = new JPanel(new BorderLayout(0, 10));
        pnlBawahKiri.setBackground(Color.WHITE);
        pnlBawahKiri.add(pnlButtonKiri, BorderLayout.CENTER);
        pnlBawahKiri.add(pnlMenuWrap, BorderLayout.SOUTH);
        
        pnlKiri.add(pnlBawahKiri, BorderLayout.SOUTH);

        // ================= PANEL KANAN (DATA) =================
        JPanel pnlKanan = new JPanel(new BorderLayout(0, 15));
        pnlKanan.setBackground(Color.WHITE);
        pnlKanan.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel pnlAtasKanan = new JPanel(new BorderLayout(10, 10));
        pnlAtasKanan.setBackground(Color.WHITE);

        // PERBAIKAN: Mengganti ikon agar senada dengan FormCustomer
        JLabel lblJudulKanan = new JLabel("📋 Data Inventori Barang");
        lblJudulKanan.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        lblJudulKanan.setForeground(new Color(0, 102, 204));

        JPanel pnlCari = new JPanel(new BorderLayout(5, 0));
        pnlCari.setBackground(Color.WHITE);
        
        JLabel lblIconCari = new JLabel(" 🔍 ");
        lblIconCari.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        txtCari = new JTextField();
        txtCari.setPreferredSize(new Dimension(280, 30)); 
        txtCari.setFont(fontInput);
        txtCari.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 102, 204), 1),
                new EmptyBorder(5, 8, 5, 8)));
        
        JButton btnReset = new JButton("Reset");
        btnReset.setBackground(new Color(108, 117, 125));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnReset.setFocusPainted(false);

        pnlCari.add(lblIconCari, BorderLayout.WEST);
        pnlCari.add(txtCari, BorderLayout.CENTER);
        pnlCari.add(btnReset, BorderLayout.EAST);

        pnlAtasKanan.add(lblJudulKanan, BorderLayout.WEST);
        pnlAtasKanan.add(pnlCari, BorderLayout.EAST);
        pnlKanan.add(pnlAtasKanan, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Kategori");
        model.addColumn("Nama Barang");
        model.addColumn("Satuan");
        model.addColumn("Harga");
        model.addColumn("Stok");

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setOpaque(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));
        pnlKanan.add(scroll, BorderLayout.CENTER);

        pnlUtama.add(pnlKiri, BorderLayout.WEST);
        pnlUtama.add(pnlKanan, BorderLayout.CENTER);

        add(pnlHeader, BorderLayout.NORTH);
        add(pnlUtama, BorderLayout.CENTER);

        // Proteksi Hak Akses Role Kasir
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            btnSimpan.setEnabled(false); 
            btnEdit.setVisible(false);   
            btnHapus.setVisible(false);  
        }

        tampilKategori();
        tampilData();
        autoID();

        btnSimpan.addActionListener(e -> simpan());
        btnEdit.addActionListener(e -> edit());
        btnHapus.addActionListener(e -> hapus());
        btnBatal.addActionListener(e -> batal());
        btnMenu.addActionListener(e -> {
            new MenuUtama().setVisible(true);
            dispose();
        });

        btnReset.addActionListener(e -> {
            txtCari.setText("");
            tampilData(); 
            txtCari.requestFocus();
        });

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariData(); 
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                klikTabel();
            }
        });

        txtHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formatRupiah();
            }
        });
    }

    // ====================== METHOD LOGIKA BACKEND ======================

    void tampilKategori() {
        try {
            Connection conn = koneksi.configDB();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tb_kategori");
            while (rs.next()) {
                cmbKategori.addItem(rs.getString("nama_kategori"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void tampilData() {
        model.setRowCount(0);
        try {
            Connection conn = koneksi.configDB();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT tb_barang.*, tb_kategori.nama_kategori "
                + "FROM tb_barang "
                + "JOIN tb_kategori "
                + "ON tb_barang.id_kategori = tb_kategori.id_kategori"
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getString("nama_kategori"),
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    "Rp " + String.format("%,d", rs.getInt("harga_jual")).replace(",", "."),
                    rs.getString("stok")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void cariData() {
        model.setRowCount(0);
        try {
            Connection conn = koneksi.configDB();
            String sql =
                "SELECT tb_barang.*, tb_kategori.nama_kategori "
                + "FROM tb_barang "
                + "JOIN tb_kategori "
                + "ON tb_barang.id_kategori = tb_kategori.id_kategori "
                + "WHERE nama_barang LIKE ? "
                + "OR tb_kategori.nama_kategori LIKE ? "
                + "OR tb_barang.id_barang LIKE ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            String keyword = "%" + txtCari.getText() + "%";
            pst.setString(1, keyword);
            pst.setString(2, keyword);
            pst.setString(3, keyword);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getString("nama_kategori"),
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    "Rp " + String.format("%,d", rs.getInt("harga_jual")).replace(",", "."),
                    rs.getString("stok")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void simpan() {
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            JOptionPane.showMessageDialog(null, "Akses ditolak! Kasir tidak bisa menambah data.");
            return;
        }
        try {
            if (txtNamaBarang.getText().equals("") || txtHarga.getText().equals("") || txtStok.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Data belum lengkap!");
                return;
            }

            Connection conn = koneksi.configDB();
            String sql = "INSERT INTO tb_barang VALUES(?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            String harga = txtHarga.getText().replace(".", "");

            pst.setString(1, txtIdBarang.getText());
            pst.setString(2, String.valueOf(cmbKategori.getSelectedIndex() + 1));
            pst.setString(3, txtNamaBarang.getText());
            pst.setString(4, txtSatuan.getText());
            pst.setInt(5, Integer.parseInt(harga));
            pst.setString(6, txtStok.getText());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
            tampilData();
            kosong();
            autoID();
            
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void edit() {
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            JOptionPane.showMessageDialog(null, "Akses ditolak! Kasir tidak bisa mengedit data.");
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data dulu di tabel!");
            return;
        }

        String id = table.getValueAt(row, 0).toString();

        try {
            Connection conn = koneksi.configDB();
            String sql =
                "UPDATE tb_barang SET "
                + "id_kategori=?, nama_barang=?, "
                + "satuan=?, harga_jual=?, stok=? "
                + "WHERE id_barang=?";

            PreparedStatement pst = conn.prepareStatement(sql);
            String harga = txtHarga.getText().replace(".", "");

            pst.setString(1, String.valueOf(cmbKategori.getSelectedIndex() + 1));
            pst.setString(2, txtNamaBarang.getText());
            pst.setString(3, txtSatuan.getText());
            pst.setInt(4, Integer.parseInt(harga));
            pst.setString(5, txtStok.getText());
            pst.setString(6, id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
            tampilData();
            kosong();
            autoID();
            
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void hapus() {
        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            JOptionPane.showMessageDialog(null, "Akses ditolak! Kasir tidak bisa menghapus data.");
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data dulu di tabel!");
            return;
        }

        int jawab = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION) {
            String id = table.getValueAt(row, 0).toString();
            try {
                Connection conn = koneksi.configDB();
                PreparedStatement pst = conn.prepareStatement("DELETE FROM tb_barang WHERE id_barang=?");
                pst.setString(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                tampilData();
                kosong();
                autoID();
                
                btnSimpan.setEnabled(true);
                btnEdit.setEnabled(false);
                btnHapus.setEnabled(false);
            } catch (Exception e) {
                if (e.getMessage().contains("foreign key constraint")) {
                    JOptionPane.showMessageDialog(null, "Gagal: Barang sudah tercatat dalam histori transaksi!");
                } else {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        }
    }

    void klikTabel() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtIdBarang.setText(table.getValueAt(row, 0).toString());
        cmbKategori.setSelectedItem(table.getValueAt(row, 1).toString());
        txtNamaBarang.setText(table.getValueAt(row, 2).toString());
        txtSatuan.setText(table.getValueAt(row, 3).toString());

        String harga = table.getValueAt(row, 4).toString().replace("Rp", "").replace(".", "").trim();
        txtHarga.setText(harga);
        txtStok.setText(table.getValueAt(row, 5).toString());

        if (Session.role != null && Session.role.equalsIgnoreCase("Kasir")) {
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            btnSimpan.setEnabled(false);
        } else {
            btnEdit.setEnabled(true);
            btnHapus.setEnabled(true);
            btnSimpan.setEnabled(false);
        }
    }

    void kosong() {
        txtNamaBarang.setText("");
        txtSatuan.setText("");
        txtHarga.setText("");
        txtStok.setText("");
    }

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
    }

    void autoID() {
        try {
            Connection conn = koneksi.configDB();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_barang FROM tb_barang ORDER BY id_barang DESC LIMIT 1");
            if (rs.next()) {
                String id = rs.getString("id_barang").substring(3);
                int nomor = Integer.parseInt(id) + 1;
                String nol = nomor < 10 ? "00" : (nomor < 100 ? "0" : "");
                txtIdBarang.setText("BRG" + nol + nomor);
            } else {
                txtIdBarang.setText("BRG001");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void formatRupiah() {
        try {
            String angka = txtHarga.getText().replace(".", "").replace(",", "");
            if (!angka.equals("")) {
                long nilai = Long.parseLong(angka);
                DecimalFormat format = new DecimalFormat("#,###");
                DecimalFormatSymbols simbol = new DecimalFormatSymbols();
                simbol.setGroupingSeparator('.');
                format.setDecimalFormatSymbols(simbol);
                txtHarga.setText(format.format(nilai));
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        new FormBarang().setVisible(true);
    }
}