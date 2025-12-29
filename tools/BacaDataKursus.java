package kursusonline.tools;

import kursusonline.model.Kursus;
import kursusonline.model.KursusBerbayar;
import kursusonline.model.KursusGratis;
import kursusonline.model.Peserta;
import java.io.*;
import java.util.ArrayList;

public class BacaDataKursus {
    public static void main(String[] args) {
        System.out.println("=== MEMBACA DATA KURSUS ===");
        bacaDataKursus();
        
        System.out.println("\n=== MEMBACA DATA PESERTA ===");
        bacaDataPeserta();
    }
    
    public static void bacaDataKursus() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data_kursus.dat"))) {
            @SuppressWarnings("unchecked")
            ArrayList<Kursus> daftarKursus = (ArrayList<Kursus>) ois.readObject();
            
            System.out.println("Total kursus: " + daftarKursus.size());
            System.out.println("====================================");
            
            for (Kursus kursus : daftarKursus) {
                System.out.println("Kode: " + kursus.getKode());
                System.out.println("Nama: " + kursus.getNama());
                System.out.println("Jenis: " + kursus.getJenisKursus());
                System.out.println("Biaya: Rp " + kursus.getBiaya());
                System.out.println("Kuota: " + kursus.getKuota());
                System.out.println("Terdaftar: " + kursus.getPesertaTerdaftar());
                System.out.println("Sisa: " + kursus.getSisaKuota());
                System.out.println("Durasi: " + kursus.getDurasi() + " jam");
                System.out.println("------------------------------------");
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("File data_kursus.dat tidak ditemukan!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error membaca file: " + e.getMessage());
        }
    }
    
    public static void bacaDataPeserta() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data_peserta.dat"))) {
            @SuppressWarnings("unchecked")
            ArrayList<Peserta> daftarPeserta = (ArrayList<Peserta>) ois.readObject();
            
            System.out.println("Total peserta: " + daftarPeserta.size());
            System.out.println("====================================");
            
            for (Peserta peserta : daftarPeserta) {
                System.out.println("ID: " + peserta.getId());
                System.out.println("Nama: " + peserta.getNama());
                System.out.println("Email: " + peserta.getEmail());
                System.out.println("Telepon: " + peserta.getTelepon());
                
                System.out.println("Kursus yang diambil:");
                for (Kursus kursus : peserta.getKursusDidaftar()) {
                    System.out.println("  - " + kursus.getNama() + " (Rp " + kursus.getBiaya() + ")");
                }
                
                System.out.println("Total biaya: Rp " + peserta.getTotalBiaya());
                System.out.println("------------------------------------");
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("File data_peserta.dat tidak ditemukan!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error membaca file: " + e.getMessage());
        }
    }
}