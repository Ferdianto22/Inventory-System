import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BarangFrame extends JFrame {
    private JTextField txtNama, txtHarga;
    private JButton btnTambah, btnHapus, btnTampilkan;
    private JTable table;

    public BarangFrame() {
        setTitle("CRUD Barang");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        txtNama = new JTextField(15);
        txtHarga = new JTextField(10);
        btnTambah = new JButton("Tambah");
        btnHapus = new JButton("Hapus");
        btnTampilkan = new JButton("Tampilkan");

        add(new JLabel("Nama Barang:"));
        add(txtNama);
        add(new JLabel("Harga:"));
        add(txtHarga);
        add(btnTambah);
        add(btnHapus);
        add(btnTampilkan);

        btnTambah.addActionListener(e -> tambahBarang());
        btnHapus.addActionListener(e -> hapusBarang());
        btnTampilkan.addActionListener(e -> tampilkanBarang());

        setVisible(true);
    }

    private void tambahBarang() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO barang (nama, harga) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setDouble(2, Double.parseDouble(txtHarga.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void hapusBarang() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM barang WHERE nama = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tampilkanBarang() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM barang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID Barang", "Nama", "Harga"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("idbarang"), rs.getString("nama"), rs.getDouble("harga")});
            }

            table = new JTable(model);
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "Data Barang", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BarangFrame();
    }
}
