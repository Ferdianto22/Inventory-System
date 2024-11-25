import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TransaksiFrame extends JFrame {
    private JComboBox<String> cbKonsumen, cbBarang;
    private JButton btnTambah, btnTampilkan;
    private JTable table;

    public TransaksiFrame() {
        setTitle("CRUD Transaksi");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        cbKonsumen = new JComboBox<>();
        cbBarang = new JComboBox<>();
        btnTambah = new JButton("Tambah");
        btnTampilkan = new JButton("Tampilkan");

        add(new JLabel("Pilih Konsumen:"));
        add(cbKonsumen);
        add(new JLabel("Pilih Barang:"));
        add(cbBarang);
        add(btnTambah);
        add(btnTampilkan);

        // Load data for ComboBoxes
        loadKonsumen();
        loadBarang();

        btnTambah.addActionListener(e -> tambahTransaksi());
        btnTampilkan.addActionListener(e -> tampilkanTransaksi());

        setVisible(true);
    }

    private void loadKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT idkonsumen, nama FROM konsumen";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cbKonsumen.addItem(rs.getInt("idkonsumen") + " - " + rs.getString("nama"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadBarang() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT idbarang, nama FROM barang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cbBarang.addItem(rs.getInt("idbarang") + " - " + rs.getString("nama"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tambahTransaksi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String selectedKonsumen = (String) cbKonsumen.getSelectedItem();
            String selectedBarang = (String) cbBarang.getSelectedItem();

            // Extract IDs from selected items
            int idKonsumen = Integer.parseInt(selectedKonsumen.split(" - ")[0]);
            int idBarang = Integer.parseInt(selectedBarang.split(" - ")[0]);

            String sql = "INSERT INTO transaksi (idkonsumen, idbarang) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idKonsumen);
            stmt.setInt(2, idBarang);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tampilkanTransaksi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT t.idtransaksi, k.nama AS konsumen, b.nama AS barang " +
                         "FROM transaksi t " +
                         "JOIN konsumen k ON t.idkonsumen = k.idkonsumen " +
                         "JOIN barang b ON t.idbarang = b.idbarang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID Transaksi", "Konsumen", "Barang"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("idtransaksi"), rs.getString("konsumen"), rs.getString("barang")});
            }

            table = new JTable(model);
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "Data Transaksi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TransaksiFrame();
    }
}
