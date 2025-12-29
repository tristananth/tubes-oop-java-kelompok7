package kursusonline.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import kursusonline.model.*;
import kursusonline.exception.KuotaPenuhException;
import java.io.*;
import java.util.Comparator;

public class MainFrame extends JFrame {
    Pendataan pendataan;
    JPanel mainPanel;
    CardLayout cardLayout;
    
    // Komponen untuk form pendaftaran
    JTextField txtNama, txtEmail, txtTelepon;
    JComboBox<String> comboKursus;
    JLabel lblBiaya, lblKuota;
    JButton btnDaftar;
    
    // Komponen untuk tabel data
    JTable tableData;
    DefaultTableModel tableModel;
    TableRowSorter<DefaultTableModel> tableSorter;
    
    public MainFrame() {
        pendataan = new Pendataan();
        
        setTitle("Sistem Pendaftaran Kursus Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        initUI();
        
        // Muat data SETELAH UI diinisialisasi
        pendataan.muatData();
        refreshComboKursus(); // Refresh combo box setelah data dimuat
    }
    
    void initUI() {
        // Setup CardLayout untuk navigasi
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Panel Utama (Dashboard)
        JPanel dashboardPanel = createDashboardPanel();
        
        // Panel Pendaftaran
        JPanel pendaftaranPanel = createPendaftaranPanel();
        
        // Panel Data
        JPanel dataPanel = createDataPanel();
        
        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(pendaftaranPanel, "pendaftaran");
        mainPanel.add(dataPanel, "data");
        
        add(mainPanel);
        
        // Menu Bar
        createMenuBar();
    }
    
    JPanel createDashboardPanel() {
        // Panel utama dengan warna background #FF6D1F
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 109, 31)); // Warna #FF6D1F
        
        // Header - Judul dengan teks hitam
        JLabel lblTitle = new JLabel("Sistem Pendaftaran Kursus Online", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(50, 0, 60, 0));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Center Panel untuk tombol-tombol
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(255, 109, 31)); // Warna #FF6D1F
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 150));
        
        // Tombol 1: Pendaftaran Kursus Baru
        JButton btnPendaftaran = new JButton("Pendaftaran Kursus Baru");
        btnPendaftaran.setFont(new Font("Arial", Font.BOLD, 16));
        btnPendaftaran.setBackground(Color.WHITE);
        btnPendaftaran.setForeground(Color.BLACK);
        btnPendaftaran.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPendaftaran.setMaximumSize(new Dimension(400, 60));
        btnPendaftaran.setMinimumSize(new Dimension(400, 60));
        btnPendaftaran.setPreferredSize(new Dimension(400, 60));
        btnPendaftaran.setFocusPainted(false);
        btnPendaftaran.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        btnPendaftaran.addActionListener(e -> {
            // Refresh combo box sebelum menampilkan panel pendaftaran
            refreshComboKursus();
            cardLayout.show(mainPanel, "pendaftaran");
        });
        
        // Tombol 2: Lihat Data Pendaftaran
        JButton btnData = new JButton("Lihat Data Pendaftaran");
        btnData.setFont(new Font("Arial", Font.BOLD, 16));
        btnData.setBackground(Color.WHITE);
        btnData.setForeground(Color.BLACK);
        btnData.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnData.setMaximumSize(new Dimension(400, 60));
        btnData.setMinimumSize(new Dimension(400, 60));
        btnData.setPreferredSize(new Dimension(400, 60));
        btnData.setFocusPainted(false);
        btnData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        btnData.addActionListener(e -> {
            refreshDataTable();
            cardLayout.show(mainPanel, "data");
        });
        
        // Tombol 3: Keluar Aplikasi
        JButton btnKeluar = new JButton("Keluar Aplikasi");
        btnKeluar.setFont(new Font("Arial", Font.BOLD, 16));
        btnKeluar.setBackground(Color.WHITE);
        btnKeluar.setForeground(Color.BLACK);
        btnKeluar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnKeluar.setMaximumSize(new Dimension(400, 60));
        btnKeluar.setMinimumSize(new Dimension(400, 60));
        btnKeluar.setPreferredSize(new Dimension(400, 60));
        btnKeluar.setFocusPainted(false);
        btnKeluar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                MainFrame.this, 
                "Apakah Anda yakin ingin keluar? Data akan disimpan otomatis.",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                pendataan.simpanData();
                System.exit(0);
            }
        });
        
        // Tambahkan tombol ke center panel dengan spacing
        centerPanel.add(btnPendaftaran);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacing
        centerPanel.add(btnData);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacing
        centerPanel.add(btnKeluar);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer dengan info statistik
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel lblStats = new JLabel("Status: " + 
            "Kursus Tersedia: " + pendataan.getDaftarKursus().size() + " | " +
            "Total Peserta: " + pendataan.getDaftarPeserta().size(), 
            SwingConstants.CENTER
        );
        lblStats.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStats.setForeground(Color.BLACK);
        
        JLabel lblFooter = new JLabel("© 2024 Sistem Kursus Online", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 12));
        lblFooter.setForeground(Color.BLACK);
        
        footerPanel.add(lblStats, BorderLayout.NORTH);
        footerPanel.add(lblFooter, BorderLayout.SOUTH);
        
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    JPanel createPendaftaranPanel() {
        // Panel utama dengan background putih
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Judul Form - dengan font besar dan teks hitam
        JLabel lblTitle = new JLabel("Form Pendaftaran Kursus", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Panel utama untuk form
        JPanel formContainer = new JPanel();
        formContainer.setBackground(Color.WHITE);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(BorderFactory.createEmptyBorder(10, 150, 10, 150));
        
        // 1. Nama Lengkap
        JLabel lblNama = new JLabel("Nama Lengkap");
        lblNama.setFont(new Font("Arial", Font.BOLD, 14));
        lblNama.setForeground(Color.BLACK);
        lblNama.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblNama.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        txtNama = new JTextField();
        txtNama.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNama.setForeground(Color.BLACK);
        txtNama.setMaximumSize(new Dimension(600, 40));
        txtNama.setPreferredSize(new Dimension(600, 40));
        txtNama.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtNama.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 2. Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        lblEmail.setForeground(Color.BLACK);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblEmail.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setForeground(Color.BLACK);
        txtEmail.setMaximumSize(new Dimension(600, 40));
        txtEmail.setPreferredSize(new Dimension(600, 40));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 3. Nomor Telepon
        JLabel lblTelepon = new JLabel("Nomor Telepon (harus dimulai dengan 0)");
        lblTelepon.setFont(new Font("Arial", Font.BOLD, 14));
        lblTelepon.setForeground(Color.BLACK);
        lblTelepon.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTelepon.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        txtTelepon = new JTextField();
        txtTelepon.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTelepon.setForeground(Color.BLACK);
        txtTelepon.setMaximumSize(new Dimension(600, 40));
        txtTelepon.setPreferredSize(new Dimension(600, 40));
        txtTelepon.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtTelepon.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 4. Pilih Kursus
        JLabel lblKursus = new JLabel("Pilih Kursus");
        lblKursus.setFont(new Font("Arial", Font.BOLD, 14));
        lblKursus.setForeground(Color.BLACK);
        lblKursus.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblKursus.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        comboKursus = new JComboBox<>();
        comboKursus.setFont(new Font("Arial", Font.PLAIN, 14));
        comboKursus.setForeground(Color.BLACK);
        comboKursus.setBackground(Color.WHITE);
        comboKursus.setMaximumSize(new Dimension(600, 40));
        comboKursus.setPreferredSize(new Dimension(600, 40));
        comboKursus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        comboKursus.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboKursus.addActionListener(new ComboKursusListener());
        
        // 5. Biaya Kursus
        JLabel lblBiayaTitle = new JLabel("Biaya Kursus");
        lblBiayaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblBiayaTitle.setForeground(Color.BLACK);
        lblBiayaTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBiayaTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        lblBiaya = new JLabel("Rp 0");
        lblBiaya.setFont(new Font("Arial", Font.PLAIN, 14));
        lblBiaya.setForeground(Color.BLACK);
        lblBiaya.setMaximumSize(new Dimension(600, 40));
        lblBiaya.setPreferredSize(new Dimension(600, 40));
        lblBiaya.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        lblBiaya.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBiaya.setOpaque(true);
        lblBiaya.setBackground(Color.WHITE);
        
        // 6. Sisa Kuota
        JLabel lblKuotaTitle = new JLabel("Sisa Kuota");
        lblKuotaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblKuotaTitle.setForeground(Color.BLACK);
        lblKuotaTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblKuotaTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        lblKuota = new JLabel("0");
        lblKuota.setFont(new Font("Arial", Font.PLAIN, 14));
        lblKuota.setForeground(Color.BLACK);
        lblKuota.setMaximumSize(new Dimension(600, 40));
        lblKuota.setPreferredSize(new Dimension(600, 40));
        lblKuota.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        lblKuota.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblKuota.setOpaque(true);
        lblKuota.setBackground(Color.WHITE);
        
        // Tambahkan semua komponen ke form container
        formContainer.add(lblNama);
        formContainer.add(txtNama);
        formContainer.add(lblEmail);
        formContainer.add(txtEmail);
        formContainer.add(lblTelepon);
        formContainer.add(txtTelepon);
        formContainer.add(lblKursus);
        formContainer.add(comboKursus);
        formContainer.add(lblBiayaTitle);
        formContainer.add(lblBiaya);
        formContainer.add(lblKuotaTitle);
        formContainer.add(lblKuota);
        
        // Panel untuk tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        // Tombol Daftar Sekarang
        btnDaftar = new JButton("Daftar Sekarang");
        btnDaftar.setFont(new Font("Arial", Font.BOLD, 16));
        btnDaftar.setBackground(new Color(34, 139, 34)); // Hijau
        btnDaftar.setForeground(Color.BLACK);
        btnDaftar.setPreferredSize(new Dimension(180, 45));
        btnDaftar.setFocusPainted(false);
        btnDaftar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnDaftar.addActionListener(new DaftarButtonListener());
        
        // Tombol Kembali ke Dashboard
        JButton btnKembali = new JButton("Kembali ke Dashboard");
        btnKembali.setFont(new Font("Arial", Font.BOLD, 16));
        btnKembali.setBackground(new Color(220, 20, 60)); // Merah
        btnKembali.setForeground(Color.BLACK);
        btnKembali.setPreferredSize(new Dimension(220, 45));
        btnKembali.setFocusPainted(false);
        btnKembali.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnKembali.addActionListener(e -> {
            clearForm();
            cardLayout.show(mainPanel, "dashboard");
        });
        
        buttonPanel.add(btnDaftar);
        buttonPanel.add(btnKembali);
        
        // Panel untuk form dan tombol
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(formContainer, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Tambahkan panel tengah ke panel utama
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    JPanel createDataPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Header
        JLabel lblTitle = new JLabel("Data Pendaftaran Kursus", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Tabel Data dengan sorting
        String[] columnNames = {"Kode", "Nama Kursus", "Jenis", "Biaya", "Kuota", "Terdaftar", "Sisa"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak bisa diedit
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Untuk sorting yang lebih baik berdasarkan tipe data
                if (columnIndex == 3) return Double.class; // Biaya
                if (columnIndex == 4 || columnIndex == 5 || columnIndex == 6) return Integer.class; // Kuota, Terdaftar, Sisa
                return String.class; // Kode, Nama, Jenis
            }
        };
        
        tableData = new JTable(tableModel);
        tableData.setFont(new Font("Arial", Font.PLAIN, 12));
        tableData.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableData.setRowHeight(25);
        
        // Setup TableRowSorter untuk sorting
        tableSorter = new TableRowSorter<>(tableModel);
        tableData.setRowSorter(tableSorter);
        
        // Tambahkan MouseListener untuk sorting ketika header diklik
        tableData.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tableData.columnAtPoint(e.getPoint());
                if (column >= 0) {
                    // Toggle antara ascending dan descending
                    SortOrder sortOrder = getNextSortOrder(column);
                    tableSorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(column, sortOrder)));
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel dengan tombol sorting
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> refreshDataTable());
        
        JButton btnExport = new JButton("Export ke CSV");
        btnExport.addActionListener(e -> exportToCSV());
        
        // Tombol untuk sorting ascending
        JButton btnSortAsc = new JButton("Sortir A-Z");
        btnSortAsc.addActionListener(e -> {
            int column = tableData.getSelectedColumn();
            if (column == -1) column = 0; // Default ke kolom pertama
            tableSorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(column, SortOrder.ASCENDING)));
        });
        
        // Tombol untuk sorting descending
        JButton btnSortDesc = new JButton("Sortir Z-A");
        btnSortDesc.addActionListener(e -> {
            int column = tableData.getSelectedColumn();
            if (column == -1) column = 0; // Default ke kolom pertama
            tableSorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(column, SortOrder.DESCENDING)));
        });
        
        JButton btnKembali = new JButton("Kembali ke Dashboard");
        btnKembali.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnSortAsc);
        buttonPanel.add(btnSortDesc);
        buttonPanel.add(btnKembali);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Helper method untuk mendapatkan sort order berikutnya
    SortOrder getNextSortOrder(int column) {
        java.util.List<? extends RowSorter.SortKey> sortKeys = tableSorter.getSortKeys();
        if (!sortKeys.isEmpty() && sortKeys.get(0).getColumn() == column) {
            SortOrder currentOrder = sortKeys.get(0).getSortOrder();
            return currentOrder == SortOrder.ASCENDING ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        }
        return SortOrder.ASCENDING; // Default ascending
    }
    
    void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuFile = new JMenu("File");
        JMenuItem menuDashboard = new JMenuItem("Dashboard");
        menuDashboard.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        JMenuItem menuSave = new JMenuItem("Simpan Data");
        menuSave.addActionListener(e -> {
            pendataan.simpanData();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        });
        
        JMenuItem menuExit = new JMenuItem("Keluar");
        menuExit.addActionListener(e -> {
            pendataan.simpanData();
            System.exit(0);
        });
        
        menuFile.add(menuDashboard);
        menuFile.add(menuSave);
        menuFile.addSeparator();
        menuFile.add(menuExit);
        
        JMenu menuData = new JMenu("Data");
        JMenuItem menuLihatData = new JMenuItem("Lihat Data Kursus");
        menuLihatData.addActionListener(e -> {
            refreshDataTable();
            cardLayout.show(mainPanel, "data");
        });
        
        JMenuItem menuReset = new JMenuItem("Reset Data");
        menuReset.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin mereset data? Data akan dikembalikan ke contoh awal.",
                "Konfirmasi Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                pendataan = new Pendataan();
                refreshComboKursus();
                refreshDataTable();
                JOptionPane.showMessageDialog(this, "Data telah direset ke contoh awal.");
            }
        });
        
        menuData.add(menuLihatData);
        menuData.add(menuReset);
        
        JMenu menuHelp = new JMenu("Bantuan");
        JMenuItem menuAbout = new JMenuItem("Tentang");
        menuAbout.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Sistem Pendaftaran Kursus Online\nVersi 1.0\n\n" +
            "Fitur:\n" +
            "- Pendaftaran kursus berbayar/gratis\n" +
            "- Validasi kuota\n" +
            "- Penyimpanan data ke file\n" +
            "- GUI dengan Java Swing\n\n" +
            "File data disimpan di:\n" +
            "- data_kursus.dat (data kursus)\n" +
            "- data_peserta.dat (data peserta)\n" +
            "- data_export.csv (data ekspor)",
            "Tentang Aplikasi",
            JOptionPane.INFORMATION_MESSAGE));
        
        menuHelp.add(menuAbout);
        
        menuBar.add(menuFile);
        menuBar.add(menuData);
        menuBar.add(menuHelp);
        
        setJMenuBar(menuBar);
    }
    
    // Inner Class untuk Event Listener ComboBox
    class ComboKursusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateKursusInfo();
        }
    }
    
    // Inner Class untuk Event Listener Tombol Daftar
    class DaftarButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Validasi input
                if (txtNama.getText().trim().isEmpty() ||
                    txtEmail.getText().trim().isEmpty() ||
                    txtTelepon.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Harap isi semua field!",
                        "Validasi Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validasi email format sederhana
                String email = txtEmail.getText().trim();
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Format email tidak valid!",
                        "Validasi Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validasi nomor telepon harus dimulai dengan 0 (Indonesia)
                String telepon = txtTelepon.getText().trim();
                if (!telepon.startsWith("0")) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Nomor telepon harus dimulai dengan angka 0!",
                        "Validasi Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validasi nomor telepon hanya boleh berisi angka
                if (!telepon.matches("\\d+")) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Nomor telepon hanya boleh berisi angka!",
                        "Validasi Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Ambil data kursus yang dipilih
                String selected = (String) comboKursus.getSelectedItem();
                if (selected == null || selected.isEmpty() || selected.equals("-")) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Pilih kursus terlebih dahulu!",
                        "Validasi Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String kodeKursus = selected.split(" - ")[0];
                Kursus kursus = pendataan.getKursusByKode(kodeKursus);
                
                if (kursus == null) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Kursus tidak ditemukan!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Buat peserta baru
                String idPeserta = "P" + System.currentTimeMillis();
                Peserta peserta = new Peserta(
                    idPeserta,
                    txtNama.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelepon.getText().trim()
                );
                
                // Coba daftarkan
                boolean sukses = pendataan.daftarkanPeserta(peserta, kodeKursus);
                
                if (sukses) {
                    // Untuk kursus berbayar, tampilkan dialog pembayaran
                    if (kursus.getBiaya() > 0) {
                        String[] options = {"Bayar Sekarang", "Nanti"};
                        int choice = JOptionPane.showOptionDialog(MainFrame.this,
                            String.format("Kursus %s memerlukan pembayaran Rp %,.0f\nApakah Anda ingin membayar sekarang?", 
                                kursus.getNama(), kursus.getBiaya()),
                            "Konfirmasi Pembayaran",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                        
                        if (choice == 0) {
                            JOptionPane.showMessageDialog(MainFrame.this,
                                "Pembayaran berhasil! Terima kasih.",
                                "Pembayaran Sukses",
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    
                    // Tampilkan konfirmasi sukses
                    JOptionPane.showMessageDialog(MainFrame.this,
                        String.format("Pendaftaran berhasil!\n\n" +
                            "ID Peserta: %s\n" +
                            "Nama: %s\n" +
                            "Kursus: %s\n" +
                            "Total Biaya: Rp %,.0f\n\n" +
                            "Sisa kuota kursus: %d dari %d",
                            peserta.getId(),
                            peserta.getNama(),
                            kursus.getNama(),
                            peserta.getTotalBiaya(),
                            kursus.getSisaKuota(),
                            kursus.getKuota()),
                        "Pendaftaran Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    clearForm();
                    pendataan.simpanData(); // Simpan perubahan ke file
                    refreshComboKursus(); // Refresh combo box
                    refreshDataTable(); // Refresh tabel data
                    
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Pendaftaran gagal!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (KuotaPenuhException ex) {
                JOptionPane.showMessageDialog(MainFrame.this,
                    ex.getMessage() + "\n\nSilakan pilih kursus lain.",
                    "Kuota Penuh",
                    JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Terjadi kesalahan: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    // Method helper untuk memperbarui info kursus
    void updateKursusInfo() {
        if (comboKursus == null || lblBiaya == null || lblKuota == null) {
            return; // Komponen belum diinisialisasi
        }
        
        String selected = (String) comboKursus.getSelectedItem();
        if (selected != null && !selected.isEmpty() && !selected.equals("-")) {
            String kode = selected.split(" - ")[0];
            Kursus kursus = pendataan.getKursusByKode(kode);
            if (kursus != null) {
                lblBiaya.setText(String.format("Rp %,.0f", kursus.getBiaya()));
                lblKuota.setText(kursus.getSisaKuota() + " / " + kursus.getKuota());
                
                // Update warna label kuota
                if (kursus.getSisaKuota() <= 5) {
                    lblKuota.setForeground(Color.RED);
                } else if (kursus.getSisaKuota() <= 10) {
                    lblKuota.setForeground(Color.ORANGE);
                } else {
                    lblKuota.setForeground(Color.BLACK);
                }
            }
        } else {
            // Reset ke default jika memilih "-" atau null
            lblBiaya.setText("Rp 0");
            lblKuota.setText("0");
            lblKuota.setForeground(Color.BLACK);
        }
    }
    
    // Method helper untuk refresh combo box kursus
    void refreshComboKursus() {
        if (comboKursus == null) {
            return; // Komponen belum diinisialisasi
        }
        
        comboKursus.removeAllItems();
        comboKursus.addItem("-"); // Item default/null
        
        for (Kursus kursus : pendataan.getDaftarKursus()) {
            if (kursus.getSisaKuota() > 0) {
                comboKursus.addItem(kursus.toString());
            }
        }
        
        comboKursus.setSelectedIndex(0); // Set ke item default "-"
        updateKursusInfo(); // Perbarui info kursus setelah refresh
    }
    
    void refreshDataTable() {
        if (tableModel == null) {
            return; // Komponen belum diinisialisasi
        }
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add new data
        for (Kursus kursus : pendataan.getDaftarKursus()) {
            Object[] row = {
                kursus.getKode(),
                kursus.getNama(),
                kursus.getJenisKursus(),
                kursus.getBiaya(), // Simpan sebagai double untuk sorting yang benar
                kursus.getKuota(),
                kursus.getPesertaTerdaftar(),
                kursus.getSisaKuota()
            };
            tableModel.addRow(row);
        }
        
        // Update table UI
        if (tableData != null) {
            tableData.revalidate();
            tableData.repaint();
        }
    }
    
    void exportToCSV() {
        try {
            File file = new File("data_export.csv");
            FileWriter writer = new FileWriter(file);
            
            // Write header
            writer.write("Kode,Nama Kursus,Jenis,Biaya,Kuota,Terdaftar,Sisa\n");
            
            // Write data
            for (Kursus kursus : pendataan.getDaftarKursus()) {
                writer.write(String.format("%s,%s,%s,%.0f,%d,%d,%d\n",
                    kursus.getKode(),
                    kursus.getNama(),
                    kursus.getJenisKursus(),
                    kursus.getBiaya(),
                    kursus.getKuota(),
                    kursus.getPesertaTerdaftar(),
                    kursus.getSisaKuota()
                ));
            }
            
            writer.close();
            
            JOptionPane.showMessageDialog(this,
                "Data berhasil diexport ke: " + file.getAbsolutePath(),
                "Export Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error exporting data: " + e.getMessage(),
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void clearForm() {
        if (txtNama != null) txtNama.setText("");
        if (txtEmail != null) txtEmail.setText("");
        if (txtTelepon != null) txtTelepon.setText("");
        refreshComboKursus();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Create and show the GUI
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}