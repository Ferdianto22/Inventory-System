import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Sistem CRUD Inventory");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton btnBarang = new JButton("Kelola Barang");
        JButton btnKonsumen = new JButton("Kelola Konsumen");
        JButton btnTransaksi = new JButton("Kelola Transaksi");

        btnBarang.addActionListener(e -> new BarangFrame());
        btnKonsumen.addActionListener(e -> new KonsumenFrame());
        btnTransaksi.addActionListener(e -> new TransaksiFrame());

        add(new JLabel("Pilih modul untuk dijalankan:"));
        add(btnBarang);
        add(btnKonsumen);
        add(btnTransaksi);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
