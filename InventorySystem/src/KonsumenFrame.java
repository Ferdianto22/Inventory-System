import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class KonsumenFrame extends JFrame {
    private JTextField txtNama;
    private JButton btnTambah, btnHapus, btnTampilkan;
    private JTable table;

    public KonsumenFrame() {
        setTitle("CRUD Konsumen");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        txtNama = new JTextField(15);
        btnTambah = new JButton("Tambah");
        btnHapus = new JButton("Hapus");
        btnTampilkan = new JButton("Tampilkan");

        add(new JLabel("Nama Konsumen:"));
        add(txtNama);
        add(btnTambah);
        add(btnHapus);
        add(btnTampilkan);

        btnTambah.addActionListener(e -> tambahKonsumen());
        btnHapus.addActionListener(e -> hapusKonsumen());
        btnTampilkan.addActionListener(e -> tampilkanKonsumen());

        setVisible(true);
    }

    private void tambahKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO konsumen (nama) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void hapusKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM konsumen WHERE nama = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void tampilkanKonsumen() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM konsumen";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nama"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("idkonsumen"), rs.getString("nama")});
            }

            table = new JTable(model);
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "Data Konsumen", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new KonsumenFrame();
    }
}
