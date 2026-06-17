package com.mycompany.javacrud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class FormSiswa extends JFrame {

    private JTextField txtNIS;
    private JTextField txtNama;
    private JComboBox<String> cmbJurusan;
    private JComboBox<String> cmbJK;
    private JTextArea txtAlamat;

    private JButton btnSimpan;
    private JButton btnHapus;
    private JButton btnUpdate;
    private JButton btnReset;

    private JTable table;
    private DefaultTableModel tableModel;

    private int selectedRow = -1;

    private static final int COL_NIS     = 0;
    private static final int COL_NAMA    = 1;
    private static final int COL_JURUSAN = 2;
    private static final int COL_JK      = 3;
    private static final int COL_ALAMAT  = 4;

    public FormSiswa() {
        super("Form Siswa");
        buildUI();
        attachListeners();
        loadTableFromDB();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Koneksi.closeConnection();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setMinimumSize(new Dimension(750, 380));
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(16, 0));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.add(buildFormPanel(), BorderLayout.WEST);
        root.add(buildTablePanel(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildFormPanel() {
        txtNIS  = new JTextField();
        txtNama = new JTextField();

        cmbJurusan = new JComboBox<>(new String[]{
                "Rekayasa Perangkat Lunak",
                "Teknik Komputer dan Jaringan",
                "Multimedia",
                "Akuntansi"
        });

        cmbJK = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});

        txtAlamat = new JTextArea(4, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);

        btnSimpan = new JButton("Simpan");
        btnHapus  = new JButton("Hapus");
        btnUpdate = new JButton("Update");
        btnReset  = new JButton("Reset");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 8);
        g.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel("Form Siswa");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.fill = GridBagConstraints.NONE;
        panel.add(lblTitle, g);
        g.gridwidth = 1;

        g.gridx = 0; g.gridy = 1; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        panel.add(new JLabel("NIS"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        txtNIS.setPreferredSize(new Dimension(220, 26));
        panel.add(txtNIS, g);

        g.gridx = 0; g.gridy = 2; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        panel.add(new JLabel("Nama"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        txtNama.setPreferredSize(new Dimension(220, 26));
        panel.add(txtNama, g);

        g.gridx = 0; g.gridy = 3; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        panel.add(new JLabel("Jurusan"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        panel.add(cmbJurusan, g);

        g.gridx = 0; g.gridy = 4; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        panel.add(new JLabel("JK"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        panel.add(cmbJK, g);

        g.gridx = 0; g.gridy = 5; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        g.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Alamat"), g);
        g.gridx = 1; g.fill = GridBagConstraints.BOTH; g.weightx = 1.0; g.weighty = 1.0;
        scrollAlamat.setPreferredSize(new Dimension(220, 90));
        panel.add(scrollAlamat, g);
        g.anchor = GridBagConstraints.WEST;
        g.weighty = 0;

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 6));
        btnPanel.add(btnSimpan);
        btnPanel.add(btnHapus);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnReset);

        g.gridx = 0; g.gridy = 6; g.gridwidth = 2;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(14, 4, 4, 8);
        panel.add(btnPanel, g);

        return panel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
                new String[]{"NIS", "Nama", "Jurusan", "JK", "Alamat"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(460, 300));
        return scroll;
    }

    private void attachListeners() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) fillFormFromRow(selectedRow);
            }
        });

        btnSimpan.addActionListener(e -> onSimpan());
        btnHapus.addActionListener(e -> onHapus());
        btnUpdate.addActionListener(e -> onUpdate());
        btnReset.addActionListener(e -> {
            clearForm();
            table.clearSelection();
            selectedRow = -1;
        });
    }

    private void loadTableFromDB() {
        tableModel.setRowCount(0);
        Connection conn = Koneksi.getConnection();
        if (conn == null) return;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nis, nama, jurusan, jk, alamat FROM students ORDER BY nis")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("nis"),
                        rs.getString("nama"),
                        rs.getString("jurusan"),
                        rs.getString("jk"),
                        rs.getString("alamat")
                });
            }
        } catch (SQLException e) {
            showError("Gagal memuat data: " + e.getMessage());
        }
    }

    private void onSimpan() {
        if (!validateForm()) return;

        int nis = Integer.parseInt(txtNIS.getText().trim());
        String nama    = txtNama.getText().trim();
        String jurusan = cmbJurusan.getSelectedItem().toString();
        String jk      = cmbJK.getSelectedItem().toString();
        String alamat  = txtAlamat.getText().trim();

        Connection conn = Koneksi.getConnection();
        if (conn == null) return;

        String sql = "INSERT INTO students (nis, nama, jurusan, jk, alamat) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nis);
            ps.setString(2, nama);
            ps.setString(3, jurusan);
            ps.setString(4, jk);
            ps.setString(5, alamat);
            ps.executeUpdate();
            loadTableFromDB();
            clearForm();
        } catch (SQLIntegrityConstraintViolationException e) {
            showWarning("NIS " + nis + " sudah terdaftar!");
        } catch (SQLException e) {
            showError("Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void onHapus() {
        if (selectedRow < 0) {
            showWarning("Pilih baris data yang ingin dihapus terlebih dahulu.");
            return;
        }

        int nis = Integer.parseInt(tableModel.getValueAt(selectedRow, COL_NIS).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus data siswa NIS: " + nis + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        Connection conn = Koneksi.getConnection();
        if (conn == null) return;

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE nis = ?")) {
            ps.setInt(1, nis);
            ps.executeUpdate();
            loadTableFromDB();
            clearForm();
            selectedRow = -1;
        } catch (SQLException e) {
            showError("Gagal menghapus data: " + e.getMessage());
        }
    }

    private void onUpdate() {
        if (selectedRow < 0) {
            showWarning("Pilih baris data yang ingin diupdate terlebih dahulu.");
            return;
        }
        if (!validateForm()) return;

        int nis        = Integer.parseInt(txtNIS.getText().trim());
        String nama    = txtNama.getText().trim();
        String jurusan = cmbJurusan.getSelectedItem().toString();
        String jk      = cmbJK.getSelectedItem().toString();
        String alamat  = txtAlamat.getText().trim();

        Connection conn = Koneksi.getConnection();
        if (conn == null) return;

        String sql = "UPDATE students SET nama = ?, jurusan = ?, jk = ?, alamat = ? WHERE nis = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, jurusan);
            ps.setString(3, jk);
            ps.setString(4, alamat);
            ps.setInt(5, nis);
            ps.executeUpdate();
            loadTableFromDB();
            clearForm();
            selectedRow = -1;
        } catch (SQLException e) {
            showError("Gagal mengupdate data: " + e.getMessage());
        }
    }

    private void fillFormFromRow(int row) {
        txtNIS.setText(tableModel.getValueAt(row, COL_NIS).toString());
        txtNama.setText(tableModel.getValueAt(row, COL_NAMA).toString());
        cmbJurusan.setSelectedItem(tableModel.getValueAt(row, COL_JURUSAN).toString());
        cmbJK.setSelectedItem(tableModel.getValueAt(row, COL_JK).toString());
        txtAlamat.setText(tableModel.getValueAt(row, COL_ALAMAT).toString());
    }

    private boolean validateForm() {
        String nisText = txtNIS.getText().trim();
        if (nisText.isEmpty()) {
            showWarning("NIS tidak boleh kosong!");
            txtNIS.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(nisText);
        } catch (NumberFormatException e) {
            showWarning("NIS harus berupa angka!");
            txtNIS.requestFocus();
            return false;
        }
        if (txtNama.getText().trim().isEmpty()) {
            showWarning("Nama tidak boleh kosong!");
            txtNama.requestFocus();
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtNIS.setText("");
        txtNama.setText("");
        cmbJurusan.setSelectedIndex(0);
        cmbJK.setSelectedIndex(0);
        txtAlamat.setText("");
        txtNIS.requestFocus();
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}