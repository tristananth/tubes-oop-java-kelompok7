package kursusonline.model;

import java.io.*;
import java.util.ArrayList;
import kursusonline.exception.KuotaPenuhException;

public class Pendataan {
    ArrayList<Kursus> daftarKursus;
    ArrayList<Peserta> daftarPeserta;
    static final String FILE_KURSUS = "data_kursus.dat";
    static final String FILE_PESERTA = "data_peserta.dat";
    
    public Pendataan() {
        daftarKursus = new ArrayList<>();
        daftarPeserta = new ArrayList<>();
        inisialisasiDataContoh();
    }
    
    void inisialisasiDataContoh() {
        // Tambah contoh kursus
        daftarKursus.add(new KursusBerbayar("Java Programming", "JAVA101", 30, 40, 500000));
        daftarKursus.add(new KursusBerbayar("Web Development", "WEB201", 25, 35, 750000));
        daftarKursus.add(new KursusGratis("Introduction to Python", "PYT001", 50, 20));
        daftarKursus.add(new KursusBerbayar("Data Science", "DS301", 20, 60, 1200000));
        daftarKursus.add(new KursusGratis("Git Fundamentals", "GIT001", 40, 15));
    }
    
    public void tambahKursus(Kursus kursus) {
        daftarKursus.add(kursus);
    }
    
    public Kursus getKursusByKode(String kode) {
        for (Kursus kursus : daftarKursus) {
            if (kursus.getKode().equals(kode)) {
                return kursus;
            }
        }
        return null;
    }
    
    public boolean daftarkanPeserta(Peserta peserta, String kodeKursus) throws KuotaPenuhException {
        Kursus kursus = getKursusByKode(kodeKursus);
        if (kursus == null) {
            return false;
        }
        
        boolean sukses = peserta.daftarKursus(kursus);
        if (sukses) {
            // Cek apakah peserta sudah ada di daftar
            boolean pesertaSudahAda = false;
            for (Peserta p : daftarPeserta) {
                if (p.getId().equals(peserta.getId())) {
                    pesertaSudahAda = true;
                    break;
                }
            }
            
            if (!pesertaSudahAda) {
                daftarPeserta.add(peserta);
            }
            return true;
        }
        return false;
    }
    
    public void simpanData() {
        try (ObjectOutputStream oosKursus = new ObjectOutputStream(new FileOutputStream(FILE_KURSUS));
             ObjectOutputStream oosPeserta = new ObjectOutputStream(new FileOutputStream(FILE_PESERTA))) {
            
            oosKursus.writeObject(daftarKursus);
            oosPeserta.writeObject(daftarPeserta);
            
            System.out.println("Data berhasil disimpan ke file.");
            
        } catch (IOException e) {
            System.err.println("Error menyimpan data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void muatData() {
        try (ObjectInputStream oisKursus = new ObjectInputStream(new FileInputStream(FILE_KURSUS));
             ObjectInputStream oisPeserta = new ObjectInputStream(new FileInputStream(FILE_PESERTA))) {
            
            daftarKursus = (ArrayList<Kursus>) oisKursus.readObject();
            daftarPeserta = (ArrayList<Peserta>) oisPeserta.readObject();
            
            System.out.println("Data berhasil dimuat dari file.");
            System.out.println("Jumlah kursus: " + daftarKursus.size());
            System.out.println("Jumlah peserta: " + daftarPeserta.size());
            
        } catch (FileNotFoundException e) {
            System.out.println("File data tidak ditemukan, menggunakan data contoh.");
            // Inisialisasi ulang data contoh
            daftarKursus.clear();
            daftarPeserta.clear();
            inisialisasiDataContoh();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error memuat data: " + e.getMessage());
            e.printStackTrace();
            // Inisialisasi ulang data contoh jika error
            daftarKursus.clear();
            daftarPeserta.clear();
            inisialisasiDataContoh();
        }
    }
    
    public ArrayList<Kursus> getDaftarKursus() {
        return daftarKursus;
    }
    
    public ArrayList<Peserta> getDaftarPeserta() {
        return daftarPeserta;
    }
}