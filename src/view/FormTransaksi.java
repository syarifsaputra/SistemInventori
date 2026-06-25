package view;

import config.Session;
import config.koneksi;
import java.awt.*;
import java.awt.print.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormTransaksi extends JFrame {

    JComboBox<String> cmbCustomer, cmbBarang;
    JTextField txtKategori, txtHarga, txtJumlah, txtCari;
    JTextField txtBayar, txtKembalian; // BARU: field bayar & kembalian

    JButton btnTambahItem, btnHapusItem, btnSimpan, btnBatal,
            btnCetak, btnMenu, btnReset, btnHapusTrx, btnHitung;

    JTable tblKeranjang, tblRiwayat;
    DefaultTableModel modelKeranjang, modelRiwayat;
    JLabel lblTotalBayar;

    String lastIdJual = "", lastTanggal = "", lastCustomer = "", idJualDipilih = "";

    public FormTransaksi() {
        setTitle("FORM TRANSAKSI PENJUALAN");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(240, 243, 248));
        root.add(buatHeader(), BorderLayout.NORTH);
        root.add(buatBody(),   BorderLayout.CENTER);
        root.add(buatFooter(), BorderLayout.SOUTH);
        add(root);

        tampilCustomer();
        tampilBarang();
        tampilRiwayat();
        pasangEvent();
    }

    // ── HEADER ──────────────────────────────────────────────
    JPanel buatHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(0, 102, 204));
        p.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JLabel lTtl = new JLabel("TRANSAKSI PENJUALAN");
        lTtl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lTtl.setForeground(Color.WHITE);

        JLabel lUsr = new JLabel(
            "Login: " + Session.nama + "  |  " + Session.role);
        lUsr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lUsr.setForeground(new Color(255, 255, 255, 200));
        lUsr.setHorizontalAlignment(JLabel.RIGHT);

        p.add(lTtl, BorderLayout.WEST);
        p.add(lUsr, BorderLayout.EAST);
        return p;
    }

    // ── FOOTER ──────────────────────────────────────────────
    JLabel buatFooter() {
        JLabel l = new JLabel(
            "  💡  Pilih Barang → Isi Jumlah → '+ Tambah ke Keranjang' "
            + "→ Isi Jumlah Bayar → HITUNG → SIMPAN");
        l.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 11));
        l.setForeground(new Color(100, 110, 130));
        l.setBackground(Color.WHITE);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return l;
    }

    // ── BODY ────────────────────────────────────────────────
    JPanel buatBody() {

    JPanel body = new JPanel(new BorderLayout(12, 0));
    body.setBackground(new Color(240, 243, 248));
    body.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel kiri = buatPanelKiri();
    JPanel kanan = buatPanelKanan();

    kiri.setPreferredSize(new Dimension(900, 0));
    kanan.setPreferredSize(new Dimension(330, 0));

    body.add(kiri, BorderLayout.CENTER);
    body.add(kanan, BorderLayout.EAST);

    return body;
}
    // ── PANEL KIRI ──────────────────────────────────────────
    JPanel buatPanelKiri() {

        // ---- Card Input Barang ----
        JPanel cardInput = buatCard();
        cardInput.setLayout(new BorderLayout(0, 10));
        cardInput.add(label("Input Transaksi", 14, true,
            new Color(0, 102, 204)), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(5, 2, 8, 8));
        grid.setOpaque(false);

        cmbCustomer = new JComboBox<>();
        cmbBarang   = new JComboBox<>();
        txtKategori = new JTextField(); txtKategori.setEditable(false);
        txtHarga    = new JTextField(); txtHarga.setEditable(false);
        txtJumlah   = new JTextField("1");

        styleField(txtKategori); styleField(txtHarga); styleField(txtJumlah);
        styleCombo(cmbCustomer); styleCombo(cmbBarang);

        grid.add(label("Customer"));    grid.add(cmbCustomer);
        grid.add(label("Barang"));      grid.add(cmbBarang);
        grid.add(label("Kategori"));    grid.add(txtKategori);
        grid.add(label("Harga"));       grid.add(txtHarga);
        grid.add(label("Jumlah Beli")); grid.add(txtJumlah);

        btnTambahItem = tombol("+ Tambah ke Keranjang", new Color(0, 153, 76));
        btnTambahItem.setPreferredSize(new Dimension(0, 25));

        cardInput.add(grid,          BorderLayout.CENTER);
        cardInput.add(btnTambahItem, BorderLayout.SOUTH);

        // ---- Card Keranjang ----
        JPanel cardKeranjang = buatCard();
        cardKeranjang.setLayout(new BorderLayout(0, 6));

        JPanel hdrKrj = new JPanel(new BorderLayout());
        hdrKrj.setOpaque(false);
        hdrKrj.add(label(" Keranjang Belanja", 13, true,
            new Color(0, 102, 204)), BorderLayout.WEST);
        btnHapusItem = tombol("✕ Hapus Item", new Color(220, 53, 69));
        btnHapusItem.setPreferredSize(new Dimension(110, 28));
        btnHapusItem.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        hdrKrj.add(btnHapusItem, BorderLayout.EAST);

        modelKeranjang = mdl("Barang","Kategori","Harga Satuan","Jumlah","Subtotal");
        tblKeranjang   = tbl(modelKeranjang);
        int[] wKrj = {220,120,140,80,160};
        for (int i=0;i<wKrj.length;i++)
            tblKeranjang.getColumnModel().getColumn(i).setPreferredWidth(wKrj[i]);

        JScrollPane scKrj = new JScrollPane(tblKeranjang);
        scKrj.setBorder(BorderFactory.createEmptyBorder());
        scKrj.setPreferredSize(new Dimension(0, 350));

        // Panel total bayar
        JPanel pTotal = new JPanel(new BorderLayout());
        pTotal.setBackground(new Color(0, 102, 204));
        pTotal.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        JLabel lTTxt = new JLabel("TOTAL BAYAR");
        lTTxt.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        lTTxt.setForeground(Color.WHITE);
        lblTotalBayar = new JLabel("Rp 0");
        lblTotalBayar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        lblTotalBayar.setForeground(new Color(255, 220, 80));
        lblTotalBayar.setHorizontalAlignment(JLabel.RIGHT);
        pTotal.add(lTTxt,         BorderLayout.WEST);
        pTotal.add(lblTotalBayar, BorderLayout.EAST);

        cardKeranjang.add(hdrKrj,  BorderLayout.NORTH);
        cardKeranjang.add(scKrj,   BorderLayout.CENTER);
        cardKeranjang.add(pTotal,  BorderLayout.SOUTH);

        // ---- Card Kembalian (BARU) ----
        JPanel cardKembalian = buatCard();
        cardKembalian.setLayout(new BorderLayout(0, 8));
        cardKembalian.add(label(" Pembayaran & Kembalian", 13, true,
            new Color(0, 102, 204)), BorderLayout.NORTH);

        JPanel gridKembalian = new JPanel(new GridLayout(2, 3, 8, 6));
        gridKembalian.setOpaque(false);

        txtBayar      = new JTextField();
        txtKembalian  = new JTextField();
        txtKembalian.setEditable(false);
        txtKembalian.setBackground(new Color(240, 250, 240));

        styleField(txtBayar);
        styleField(txtKembalian);

        btnHitung = tombol("HITUNG KEMBALIAN", new Color(0, 102, 204));
        btnHitung.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));

        gridKembalian.add(label("Jumlah Bayar (Rp)"));
        gridKembalian.add(txtBayar);
        gridKembalian.add(btnHitung);
        gridKembalian.add(label("Kembalian (Rp)"));
        gridKembalian.add(txtKembalian);
        gridKembalian.add(new JLabel()); // spacer

        cardKembalian.add(gridKembalian, BorderLayout.CENTER);

        // ---- Tombol Aksi Kiri Bawah ----
        btnSimpan = tombol("💾 SIMPAN", new Color(0, 102, 204));
        btnBatal  = tombol("↺ BATAL",  new Color(108, 117, 125));
        btnCetak  = tombol("🖨 CETAK",  new Color(111, 66, 193));
        btnCetak.setEnabled(false);

        // Ukuran font tombol lebih kecil
        btnSimpan.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        btnBatal.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        btnCetak.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));

        JPanel aksiKiri = new JPanel(new GridLayout(1, 3, 6, 0));
        aksiKiri.setBackground(new Color(240, 243, 248));
        aksiKiri.setPreferredSize(new Dimension(0, 36));
        aksiKiri.add(btnSimpan);
        aksiKiri.add(btnBatal);
        aksiKiri.add(btnCetak);

        // Gabung kiri
        JPanel tengah = new JPanel(new BorderLayout(0, 12));
        tengah.setBackground(new Color(240, 243, 248));
        tengah.add(cardKeranjang,  BorderLayout.CENTER);
        tengah.add(cardKembalian,  BorderLayout.SOUTH);

        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(new Color(240, 243, 248));
        cardInput.setPreferredSize(new Dimension(0, 220));
        p.add(cardInput, BorderLayout.NORTH);
        p.add(tengah,    BorderLayout.CENTER);
        p.add(aksiKiri,  BorderLayout.SOUTH);
        return p;
    }

    // ── PANEL KANAN ─────────────────────────────────────────
    JPanel buatPanelKanan() {
        // Search bar
        JLabel lIkon = new JLabel("🔍");
        lIkon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        lIkon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

        txtCari = new JTextField();
        txtCari.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        txtCari.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 195, 220), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnReset.setBackground(new Color(108, 117, 125));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false); btnReset.setBorderPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.setPreferredSize(new Dimension(65, 30));

        JPanel pSearch = new JPanel(new BorderLayout(5, 0));
        pSearch.setOpaque(false);
        pSearch.add(lIkon,   BorderLayout.WEST);
        pSearch.add(txtCari, BorderLayout.CENTER);
        pSearch.add(btnReset, BorderLayout.EAST);

        JPanel topRwt = new JPanel(new BorderLayout(0, 6));
        topRwt.setOpaque(false);
        topRwt.add(label(" Riwayat Transaksi", 14, true,
            new Color(0, 102, 204)), BorderLayout.NORTH);
        topRwt.add(pSearch, BorderLayout.SOUTH);

        modelRiwayat = mdl("ID","Tanggal","Customer","Barang","Qty","Total");
        tblRiwayat   = tbl(modelRiwayat);
        int[] wRwt   = {40,90,120,120,40,100};
        for (int i=0;i<wRwt.length;i++)
            tblRiwayat.getColumnModel().getColumn(i).setPreferredWidth(wRwt[i]);

        JScrollPane scRwt = new JScrollPane(tblRiwayat);
        scRwt.setBorder(BorderFactory.createEmptyBorder());

        JPanel cardRwt = buatCard();
        cardRwt.setLayout(new BorderLayout(0, 8));
        cardRwt.add(topRwt, BorderLayout.NORTH);
        cardRwt.add(scRwt,  BorderLayout.CENTER);

        // Tombol kanan bawah — lebih kecil
        btnHapusTrx = tombol("🗑 HAPUS TRANSAKSI", new Color(220, 53, 69));
        btnMenu     = tombol("⬅ MENU",             new Color(52, 58, 64));
        btnHapusTrx.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));
        btnMenu.setFont(new Font("Segoe UI Emoji", Font.BOLD, 11));

        if (Session.role.equals("Kasir")) btnHapusTrx.setVisible(false);

        JPanel aksiKanan = new JPanel(new GridLayout(1, 2, 6, 0));
        aksiKanan.setBackground(new Color(240, 243, 248));
        aksiKanan.setPreferredSize(new Dimension(0, 36));
        aksiKanan.add(btnHapusTrx);
        aksiKanan.add(btnMenu);

        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(new Color(240, 243, 248));
        p.add(cardRwt,   BorderLayout.CENTER);
        p.add(aksiKanan, BorderLayout.SOUTH);
        return p;
    }

    // ── EVENTS ──────────────────────────────────────────────
    void pasangEvent() {
        cmbBarang.addActionListener(e -> tampilHarga());
        btnTambahItem.addActionListener(e -> tambahKeKeranjang());
        btnHapusItem.addActionListener(e -> hapusItemKeranjang());
        btnHitung.addActionListener(e -> hitungKembalian()); // BARU
        btnSimpan.addActionListener(e -> simpanTransaksi());
        btnBatal.addActionListener(e -> batalKeranjang());
        btnCetak.addActionListener(e -> cetakStruk());
        btnHapusTrx.addActionListener(e -> hapusTransaksi());
        btnMenu.addActionListener(e -> { new MenuUtama().setVisible(true); dispose(); });
        btnReset.addActionListener(e -> { txtCari.setText(""); tampilRiwayat(); });

        // Hitung kembalian otomatis saat mengetik di txtBayar
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                hitungKembalian();
            }
        });

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { cariRiwayat(); }
        });

        tblRiwayat.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = tblRiwayat.getSelectedRow();
                if (r >= 0) {
                    lastIdJual    = modelRiwayat.getValueAt(r, 0).toString();
                    idJualDipilih = lastIdJual;
                    lastTanggal   = modelRiwayat.getValueAt(r, 1).toString();
                    lastCustomer  = modelRiwayat.getValueAt(r, 2).toString();
                    btnCetak.setEnabled(true);
                }
            }
        });
    }

    // ── HITUNG KEMBALIAN (BARU) ─────────────────────────────
    void hitungKembalian() {
        try {
            // Ambil total dari label (hapus "Rp " dan titik)
            String totalStr = lblTotalBayar.getText()
                    .replace("Rp ", "").replace(".", "").trim();
            if (totalStr.equals("0") || totalStr.isEmpty()) {
                txtKembalian.setText("");
                txtKembalian.setForeground(new Color(60, 65, 75));
                return;
            }

            long total = Long.parseLong(totalStr);
            String bayarStr = txtBayar.getText()
                    .replace(".", "").replace(",", "").trim();

            if (bayarStr.isEmpty()) {
                txtKembalian.setText("");
                return;
            }

            long bayar = Long.parseLong(bayarStr);
            long kembalian = bayar - total;

            if (kembalian < 0) {
                // Bayar kurang
                txtKembalian.setText("Kurang: Rp " +
                    String.format("%,d", Math.abs(kembalian)).replace(",", "."));
                txtKembalian.setForeground(new Color(220, 53, 69));
                txtKembalian.setBackground(new Color(255, 240, 240));
            } else {
                // Kembalian
                txtKembalian.setText("Rp " +
                    String.format("%,d", kembalian).replace(",", "."));
                txtKembalian.setForeground(new Color(0, 130, 60));
                txtKembalian.setBackground(new Color(240, 255, 245));
            }
        } catch (NumberFormatException ex) {
            txtKembalian.setText("Input tidak valid");
            txtKembalian.setForeground(new Color(220, 53, 69));
        }
    }

    // ── DB METHODS ──────────────────────────────────────────
    void tampilCustomer() {
        try {
            ResultSet rs = koneksi.configDB().createStatement()
                .executeQuery("SELECT * FROM tb_customer ORDER BY nama_customer");
            while (rs.next()) cmbCustomer.addItem(rs.getString("nama_customer"));
        } catch (Exception e) { pesan(e.getMessage()); }
    }

    void tampilBarang() {
        try {
            ResultSet rs = koneksi.configDB().createStatement()
                .executeQuery("SELECT * FROM tb_barang ORDER BY nama_barang");
            while (rs.next()) cmbBarang.addItem(rs.getString("nama_barang"));
        } catch (Exception e) { pesan(e.getMessage()); }
    }

    void tampilHarga() {
        if (cmbBarang.getSelectedItem() == null) return;
        try {
            PreparedStatement p = koneksi.configDB().prepareStatement(
                "SELECT b.*,k.nama_kategori FROM tb_barang b "
                + "JOIN tb_kategori k ON b.id_kategori=k.id_kategori "
                + "WHERE nama_barang=?");
            p.setString(1, cmbBarang.getSelectedItem().toString());
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                txtHarga.setText(rp(rs.getInt("harga_jual")));
                txtKategori.setText(rs.getString("nama_kategori"));
            }
        } catch (Exception e) { pesan(e.getMessage()); }
    }

    void tambahKeKeranjang() {
        String nama = cmbBarang.getSelectedItem().toString();
        String hStr = txtHarga.getText().replace(".", "").replace(",", "");
        if (hStr.isEmpty()) { pesan("Pilih barang terlebih dahulu!"); return; }
        int harga, jml;
        try {
            harga = Integer.parseInt(hStr);
            jml   = Integer.parseInt(txtJumlah.getText().trim());
            if (jml <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            pesan("Jumlah harus angka positif!"); return;
        }
        try {
            PreparedStatement p = koneksi.configDB().prepareStatement(
                "SELECT stok FROM tb_barang WHERE nama_barang=?");
            p.setString(1, nama); ResultSet rs = p.executeQuery();
            int stok = rs.next() ? rs.getInt("stok") : 0;
            int sdKrj = 0;
            for (int i = 0; i < modelKeranjang.getRowCount(); i++)
                if (modelKeranjang.getValueAt(i, 0).toString().equals(nama))
                    sdKrj += Integer.parseInt(modelKeranjang.getValueAt(i, 3).toString());
            if (jml + sdKrj > stok) {
                pesan("Stok tidak mencukupi!\nTersedia:" + stok
                    + " | Keranjang:" + sdKrj + " | Diminta:" + jml); return;
            }
            long sub = (long) harga * jml;
            modelKeranjang.addRow(new Object[]{
                nama, txtKategori.getText(),
                "Rp " + rp(harga), jml, "Rp " + rp(sub) });
            hitungTotal();
            txtJumlah.setText("1");
            // Reset kembalian saat keranjang berubah
            txtBayar.setText("");
            txtKembalian.setText("");
        } catch (Exception ex) { pesan(ex.getMessage()); }
    }

    void hitungTotal() {
        long t = 0;
        for (int i = 0; i < modelKeranjang.getRowCount(); i++)
            t += Long.parseLong(modelKeranjang.getValueAt(i, 4)
                .toString().replace("Rp ", "").replace(".", ""));
        lblTotalBayar.setText("Rp " + rp(t));
        // Auto hitung kembalian jika bayar sudah diisi
        if (!txtBayar.getText().isEmpty()) hitungKembalian();
    }

    void hapusItemKeranjang() {
        int r = tblKeranjang.getSelectedRow();
        if (r == -1) { pesan("Pilih item di keranjang!"); return; }
        modelKeranjang.removeRow(r);
        hitungTotal();
        txtBayar.setText(""); txtKembalian.setText("");
    }

    void simpanTransaksi() {
        if (modelKeranjang.getRowCount() == 0) {
            pesan("Keranjang masih kosong!"); return;
        }
        // Validasi pembayaran
        if (txtBayar.getText().isEmpty()) {
            pesan("Isi jumlah bayar terlebih dahulu!"); return;
        }
        try {
            long totalVal = Long.parseLong(lblTotalBayar.getText()
                .replace("Rp ", "").replace(".", ""));
            long bayarVal = Long.parseLong(txtBayar.getText()
                .replace(".", "").replace(",", ""));
            if (bayarVal < totalVal) {
                pesan("Jumlah bayar kurang!\nTotal: Rp " + rp(totalVal)
                    + "\nBayar: Rp " + rp(bayarVal)); return;
            }
        } catch (NumberFormatException ex) {
            pesan("Jumlah bayar tidak valid!"); return;
        }

        String cust = cmbCustomer.getSelectedItem().toString();
        if (JOptionPane.showConfirmDialog(null,
            "Simpan untuk: " + cust + "\n"
            + "Total: " + lblTotalBayar.getText() + "\n"
            + "Bayar: Rp " + txtBayar.getText() + "\n"
            + "Kembalian: " + txtKembalian.getText(),
            "Konfirmasi Simpan", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        try {
            Connection conn = koneksi.configDB();
            PreparedStatement pC = conn.prepareStatement(
                "SELECT id_customer FROM tb_customer WHERE nama_customer=?");
            pC.setString(1, cust); ResultSet rC = pC.executeQuery();
            String idC = rC.next() ? rC.getString(1) : "";

            PreparedStatement pU = conn.prepareStatement(
                "SELECT id_user FROM tb_user WHERE username=?");
            pU.setString(1, Session.username); ResultSet rU = pU.executeQuery();
            int idU = rU.next() ? rU.getInt(1) : 1;

            int ok = 0;
            for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                String nb  = modelKeranjang.getValueAt(i, 0).toString();
                int    jml = Integer.parseInt(modelKeranjang.getValueAt(i, 3).toString());
                double sub = Double.parseDouble(modelKeranjang.getValueAt(i, 4)
                    .toString().replace("Rp ", "").replace(".", ""));

                PreparedStatement pB = conn.prepareStatement(
                    "SELECT id_barang,stok FROM tb_barang WHERE nama_barang=?");
                pB.setString(1, nb); ResultSet rB = pB.executeQuery();
                if (!rB.next()) continue;
                String idB = rB.getString(1); int stok = rB.getInt(2);
                if (jml > stok) { pesan("Stok " + nb + " tidak cukup ("+stok+")"); continue; }

                PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO tb_penjualan(tgl_transaksi,id_customer,"
                    + "id_barang,jumlah_beli,total_bayar,id_user)"
                    + "VALUES(CURDATE(),?,?,?,?,?)");
                ins.setString(1,idC); ins.setString(2,idB);
                ins.setInt(3,jml); ins.setDouble(4,sub); ins.setInt(5,idU);
                ins.executeUpdate();

                PreparedStatement upd = conn.prepareStatement(
                    "UPDATE tb_barang SET stok=stok-? WHERE id_barang=?");
                upd.setInt(1,jml); upd.setString(2,idB); upd.executeUpdate();
                ok++;
            }

            ResultSet rL = conn.createStatement().executeQuery(
                "SELECT id_jual,tgl_transaksi FROM tb_penjualan "
                + "ORDER BY id_jual DESC LIMIT 1");
            if (rL.next()) {
                lastIdJual   = rL.getString(1);
                lastTanggal  = rL.getString(2);
                lastCustomer = cust;
            }

            JOptionPane.showMessageDialog(null,
                "✅ " + ok + " item berhasil disimpan!\n"
                + "Customer  : " + cust + "\n"
                + "Total     : " + lblTotalBayar.getText() + "\n"
                + "Bayar     : Rp " + rp(Long.parseLong(
                    txtBayar.getText().replace(".", "").replace(",", ""))) + "\n"
                + "Kembalian : " + txtKembalian.getText(),
                "Transaksi Berhasil", JOptionPane.INFORMATION_MESSAGE);

            modelKeranjang.setRowCount(0);
            hitungTotal();
            txtBayar.setText(""); txtKembalian.setText("");
            txtKembalian.setBackground(new Color(240, 250, 240));
            tampilRiwayat();
            btnCetak.setEnabled(true);

        } catch (Exception e) { pesan(e.getMessage()); }
    }

    void batalKeranjang() {
        if (modelKeranjang.getRowCount() == 0) return;
        if (JOptionPane.showConfirmDialog(null, "Kosongkan keranjang belanja?",
            "Konfirmasi Batal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            modelKeranjang.setRowCount(0);
            hitungTotal();
            txtJumlah.setText("1");
            txtBayar.setText(""); txtKembalian.setText("");
            txtKembalian.setBackground(new Color(240, 250, 240));
            txtKembalian.setForeground(new Color(60, 65, 75));
        }
    }

    void hapusTransaksi() {
        int r = tblRiwayat.getSelectedRow();
        if (r == -1) { pesan("Pilih transaksi di riwayat!"); return; }
        String id = modelRiwayat.getValueAt(r, 0).toString();
        if (JOptionPane.showConfirmDialog(null, "Hapus transaksi ID:" + id + "?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        try {
            Connection c = koneksi.configDB();
            PreparedStatement pG = c.prepareStatement(
                "SELECT id_barang,jumlah_beli FROM tb_penjualan WHERE id_jual=?");
            pG.setString(1, id); ResultSet rG = pG.executeQuery();
            if (rG.next()) {
                PreparedStatement pS = c.prepareStatement(
                    "UPDATE tb_barang SET stok=stok+? WHERE id_barang=?");
                pS.setInt(1, rG.getInt(2)); pS.setString(2, rG.getString(1));
                pS.executeUpdate();
            }
            c.prepareStatement("DELETE FROM tb_penjualan WHERE id_jual=?")
             .executeUpdate(); // note: need setString
            PreparedStatement pD = c.prepareStatement(
                "DELETE FROM tb_penjualan WHERE id_jual=?");
            pD.setString(1, id); pD.executeUpdate();

            pesan("Transaksi berhasil dihapus!");
            tampilRiwayat(); btnCetak.setEnabled(false);
            lastIdJual = ""; idJualDipilih = "";
        } catch (Exception e) { pesan(e.getMessage()); }
    }

    void tampilRiwayat() {
        modelRiwayat.setRowCount(0);
        try {
            ResultSet rs = koneksi.configDB().createStatement().executeQuery(
                "SELECT p.id_jual,p.tgl_transaksi,c.nama_customer,"
                + "b.nama_barang,p.jumlah_beli,p.total_bayar "
                + "FROM tb_penjualan p "
                + "JOIN tb_customer c ON p.id_customer=c.id_customer "
                + "JOIN tb_barang b ON p.id_barang=b.id_barang "
                + "ORDER BY p.id_jual DESC");
            while (rs.next()) modelRiwayat.addRow(new Object[]{
                rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5),
                "Rp " + rp(rs.getLong(6)) });
        } catch (Exception e) { pesan(e.getMessage()); }
    }

    void cariRiwayat() {
        modelRiwayat.setRowCount(0);
        try {
            String kw = "%" + txtCari.getText() + "%";
            PreparedStatement p = koneksi.configDB().prepareStatement(
                "SELECT p.id_jual,p.tgl_transaksi,c.nama_customer,"
                + "b.nama_barang,p.jumlah_beli,p.total_bayar "
                + "FROM tb_penjualan p "
                + "JOIN tb_customer c ON p.id_customer=c.id_customer "
                + "JOIN tb_barang b ON p.id_barang=b.id_barang "
                + "WHERE c.nama_customer LIKE ? OR b.nama_barang LIKE ? "
                + "OR p.tgl_transaksi LIKE ? OR CAST(p.id_jual AS CHAR) LIKE ? "
                + "ORDER BY p.id_jual DESC");
            p.setString(1,kw); p.setString(2,kw);
            p.setString(3,kw); p.setString(4,kw);
            ResultSet rs = p.executeQuery();
            while (rs.next()) modelRiwayat.addRow(new Object[]{
                rs.getString(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getString(5),
                "Rp " + rp(rs.getLong(6)) });
        } catch (Exception e) { pesan(e.getMessage()); }
    }

    // ── CETAK STRUK ─────────────────────────────────────────
    void cetakStruk() {
        if (lastIdJual.isEmpty()) {
            pesan("Pilih transaksi di riwayat terlebih dahulu!"); return;
        }
        java.util.List<String[]> items = new java.util.ArrayList<>();
        long grand = 0;
        try {
            PreparedStatement p = koneksi.configDB().prepareStatement(
                "SELECT b.nama_barang,p.jumlah_beli,b.harga_jual,p.total_bayar "
                + "FROM tb_penjualan p JOIN tb_barang b ON p.id_barang=b.id_barang "
                + "JOIN tb_customer c ON p.id_customer=c.id_customer "
                + "WHERE c.nama_customer=? AND p.tgl_transaksi=? ORDER BY p.id_jual");
            p.setString(1, lastCustomer); p.setString(2, lastTanggal);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                items.add(new String[]{rs.getString(1), rs.getString(2),
                    String.valueOf(rs.getLong(3)), String.valueOf(rs.getLong(4))});
                grand += rs.getLong(4);
            }
        } catch (Exception e) { pesan(e.getMessage()); return; }

        final long totalAkhir = grand;
        final java.util.List<String[]> list = items;

        // Ambil nilai bayar & kembalian untuk struk
        final String strBayar = txtBayar.getText().isEmpty()
            ? "-" : "Rp " + rp(Long.parseLong(
                txtBayar.getText().replace(".", "").replace(",", "")));
        final String strKembalian = txtKembalian.getText().isEmpty()
            ? "-" : txtKembalian.getText();

        JPanel struk = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w=getWidth(), pad=20, y=0; float[] dash={4f,3f};

                // Header
                g2.setColor(new Color(0,102,204)); g2.fillRect(0,0,w,92);
                g2.setColor(Color.WHITE);
                tengah(g2,"TOKO SYARIF JAYA RACING",new Font("Segoe UI",Font.BOLD,15),w,24);
                tengah(g2,"Di Gaspol Dangak-Dangak",new Font("Segoe UI",Font.PLAIN,10),w,40);
                tengah(g2,"Jl. Racing No.88 | 0812-3456-7890",new Font("Segoe UI",Font.PLAIN,10),w,55);
                g2.setColor(new Color(255,255,255,80)); g2.setStroke(new BasicStroke(0.5f));
                g2.drawLine(pad,66,w-pad,66);
                g2.setColor(Color.WHITE);
                tengah(g2,"No:"+lastIdJual+"  Tgl:"+lastTanggal,
                    new Font("Segoe UI",Font.BOLD,10),w,82);
                y=102;

                // Info
                y=garisP(g2,pad,w,y,dash)+13;
                g2.setStroke(new BasicStroke(1));
                g2.setFont(new Font("Segoe UI",Font.PLAIN,11));
                g2.setColor(new Color(55,60,70));
                g2.drawString("Customer : "+lastCustomer,pad,y); y+=15;
                g2.drawString("Kasir    : "+Session.nama,pad,y); y+=9;

                // Detail barang
                y=garisP(g2,pad,w,y,dash)+13;
                g2.setStroke(new BasicStroke(1));
                g2.setFont(new Font("Segoe UI",Font.BOLD,11));
                g2.setColor(new Color(30,30,30));
                g2.drawString("DETAIL PEMBELIAN",pad,y); y+=5;
                g2.setColor(new Color(200,200,200));
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawLine(pad,y,w-pad,y); y+=12;

                for (String[] it : list) {
                    String sub="Rp "+rp(Long.parseLong(it[3]));
                    String hst="Rp "+rp(Long.parseLong(it[2]));
                    g2.setStroke(new BasicStroke(1));
                    g2.setFont(new Font("Segoe UI",Font.BOLD,11));
                    g2.setColor(new Color(30,30,30));
                    g2.drawString(it[0],pad,y); y+=14;
                    g2.setFont(new Font("Segoe UI",Font.PLAIN,10));
                    g2.setColor(new Color(100,105,115));
                    g2.drawString(it[1]+" x "+hst,pad+4,y);
                    g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                    g2.setColor(new Color(0,102,204));
                    FontMetrics fm=g2.getFontMetrics();
                    g2.drawString(sub,w-pad-fm.stringWidth(sub),y); y+=13;
                    g2.setColor(new Color(235,235,235));
                    g2.setStroke(new BasicStroke(0.5f));
                    g2.drawLine(pad,y,w-pad,y); y+=8;
                }

                // Ringkasan
                String ts="Rp "+rp(totalAkhir);
                y=garisP(g2,pad,w,y,dash)+13;
                g2.setStroke(new BasicStroke(1));
                g2.setFont(new Font("Segoe UI",Font.PLAIN,11));
                g2.setColor(new Color(80,85,95));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString("Subtotal",pad,y);
                g2.drawString(ts,w-pad-fm.stringWidth(ts),y); y+=14;
                g2.drawString("Diskon",pad,y);
                g2.drawString("Rp 0",w-pad-fm.stringWidth("Rp 0"),y); y+=10;

                // Kotak total
                g2.setColor(new Color(0,102,204));
                g2.fillRoundRect(pad,y,w-pad*2,32,8,8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI",Font.BOLD,12));
                g2.drawString("TOTAL BAYAR",pad+10,y+21);
                fm=g2.getFontMetrics();
                g2.drawString(ts,w-pad-10-fm.stringWidth(ts),y+21); y+=40;

                // Bayar & Kembalian (BARU di struk)
                g2.setStroke(new BasicStroke(1));
                g2.setFont(new Font("Segoe UI",Font.PLAIN,11));
                g2.setColor(new Color(80,85,95));
                fm=g2.getFontMetrics();
                g2.drawString("Bayar",pad,y);
                g2.drawString(strBayar,w-pad-fm.stringWidth(strBayar),y); y+=15;

                g2.setFont(new Font("Segoe UI",Font.BOLD,11));
                g2.setColor(new Color(0,130,60));
                g2.drawString("Kembalian",pad,y);
                fm=g2.getFontMetrics();
                g2.drawString(strKembalian,w-pad-fm.stringWidth(strKembalian),y); y+=10;

                // Footer
                y=garisP(g2,pad,w,y,dash)+14;
                g2.setStroke(new BasicStroke(1));
                g2.setFont(new Font("Segoe UI",Font.ITALIC,10));
                g2.setColor(new Color(120,125,135));
                tengah(g2,"Terima kasih telah berbelanja!",g2.getFont(),w,y); y+=14;
                tengah(g2,"~ Syarif Jaya Racing ~",g2.getFont(),w,y);
            }
            public Dimension getPreferredSize() {
                return new Dimension(320, Math.max(520, 220+list.size()*42));
            }
        };
        struk.setBackground(Color.WHITE);

        JButton btnPrint = tombol("🖨  PRINT STRUK", new Color(0,102,204));
        btnPrint.addActionListener(ev -> {
            try {
                PrinterJob job = PrinterJob.getPrinterJob();
                Paper paper = new Paper(); double pt=72.0/25.4;
                paper.setSize(80*pt,300*pt);
                paper.setImageableArea(5,5,80*pt-10,300*pt-10);
                PageFormat pf=job.defaultPage(); pf.setPaper(paper);
                pf.setOrientation(PageFormat.PORTRAIT);
                job.setPrintable((gr,pf2,pi) -> {
                    if(pi>0) return Printable.NO_SUCH_PAGE;
                    Graphics2D g2d=(Graphics2D)gr;
                    g2d.translate(pf2.getImageableX(),pf2.getImageableY());
                    double sc=Math.min(
                        pf2.getImageableWidth()/struk.getPreferredSize().width,
                        pf2.getImageableHeight()/struk.getPreferredSize().height);
                    g2d.scale(sc,sc); struk.setSize(struk.getPreferredSize());
                    struk.printAll(g2d); return Printable.PAGE_EXISTS; },pf);
                if(job.printDialog()) { job.print(); pesan("Struk berhasil dicetak!"); }
            } catch(Exception ex) { pesan("Gagal: "+ex.getMessage()); }
        });

        JButton btnTutup = new JButton("Tutup");
        btnTutup.setFont(new Font("Segoe UI",Font.PLAIN,12));
        btnTutup.setFocusPainted(false);

        JDialog dlg = new JDialog(this,"Preview Struk",true);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(Color.WHITE);
        JLabel lPrev = new JLabel("Preview Struk",JLabel.CENTER);
        lPrev.setFont(new Font("Segoe UI",Font.BOLD,14));
        lPrev.setForeground(new Color(0,102,204));
        lPrev.setBorder(BorderFactory.createEmptyBorder(10,0,8,0));
        JScrollPane sc = new JScrollPane(struk);
        sc.setBorder(BorderFactory.createEmptyBorder(0,14,0,14));
        sc.getViewport().setBackground(new Color(235,238,245));
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER,10,8));
        bot.setBackground(Color.WHITE);
        bot.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(220,220,220)));
        bot.add(btnPrint); bot.add(btnTutup);
        btnTutup.addActionListener(ev -> dlg.dispose());
        dlg.add(lPrev,BorderLayout.NORTH);
        dlg.add(sc,BorderLayout.CENTER);
        dlg.add(bot,BorderLayout.SOUTH);
        dlg.setSize(380,620);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    // ── HELPERS UI ──────────────────────────────────────────
    JPanel buatCard() {
        JPanel p = new JPanel(); p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215,222,235),1,true),
            BorderFactory.createEmptyBorder(12,14,12,14)));
        return p;
    }

    JLabel label(String t) { return label(t,13,false,new Color(60,65,75)); }
    JLabel label(String t,int sz,boolean bold,Color c) {
        JLabel l=new JLabel(t);
        l.setFont(new Font("Segoe UI",bold?Font.BOLD:Font.PLAIN,sz));
        l.setForeground(c); return l;
    }

    JButton tombol(String t,Color w) {
        JButton b = new JButton(t) {
            public void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(!isEnabled()?new Color(180,180,185):
                    getModel().isPressed()?w.darker().darker():
                    getModel().isRollover()?w.darker():w);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI",Font.BOLD,12));
        b.setForeground(Color.WHITE); b.setOpaque(false);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI",Font.PLAIN,13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,208,220),1,true),
            BorderFactory.createEmptyBorder(4,8,4,8)));
    }

    void styleCombo(JComboBox cb) { cb.setFont(new Font("Segoe UI",Font.PLAIN,13)); }

    void styleTable(JTable t) {
        t.setFont(new Font("Segoe UI",Font.PLAIN,12)); t.setRowHeight(32);
        t.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12));
        t.getTableHeader().setBackground(new Color(0,102,204));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setReorderingAllowed(false);
        t.setSelectionBackground(new Color(210,228,255));
        t.setSelectionForeground(new Color(0,55,120));
        t.setGridColor(new Color(232,236,245));
        t.setShowHorizontalLines(true); t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0,1));
    }

    DefaultTableModel mdl(String... cols) {
        DefaultTableModel m = new DefaultTableModel() {
            public boolean isCellEditable(int r,int c){return false;}
        };
        for (String c:cols) m.addColumn(c);
        return m;
    }

    JTable tbl(DefaultTableModel m) {
        JTable t = new JTable(m); styleTable(t); return t;
    }

    // ── STRUK HELPERS ───────────────────────────────────────
    void tengah(Graphics2D g,String t,Font f,int w,int y) {
        g.setFont(f); FontMetrics fm=g.getFontMetrics();
        g.drawString(t,(w-fm.stringWidth(t))/2,y);
    }

    int garisP(Graphics2D g,int pad,int w,int y,float[] dash) {
        g.setColor(new Color(180,180,180));
        g.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,10f,dash,0f));
        g.drawLine(pad,y,w-pad,y); return y;
    }

    String rp(long n) { return String.format("%,d",n).replace(",","."); }

    void pesan(String msg) { JOptionPane.showMessageDialog(null,msg); }

    public static void main(String[] args) { new FormTransaksi().setVisible(true); }
}