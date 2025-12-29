package kursusonline.model;

import java.util.ArrayList;
import kursusonline.exception.KuotaPenuhException;
import java.io.Serializable;

public class Peserta implements Serializable {
    static final long serialVersionUID = 4L;
    
    String id;
    String nama;
    String email;
    String telepon;
    ArrayList<Kursus> kursusDidaftar;
    
    public Peserta(String id, String nama, String email, String telepon) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
        this.kursusDidaftar = new ArrayList<>();
    }
    
    public boolean daftarKursus(Kursus kursus) throws KuotaPenuhException {
        if (kursus.daftarPeserta()) {
            kursusDidaftar.add(kursus);
            return true;
        }
        return false;
    }
    
    public double getTotalBiaya() {
        double total = 0;
        for (Kursus kursus : kursusDidaftar) {
            total += kursus.getBiaya();
        }
        return total;
    }
    
    // Getter methods
    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getTelepon() { return telepon; }
    public ArrayList<Kursus> getKursusDidaftar() { return kursusDidaftar; }
    
    @Override
    public String toString() {
        return id + " - " + nama + " (" + email + ")";
    }
}