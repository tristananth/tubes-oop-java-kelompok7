package kursusonline.model;

import kursusonline.exception.KuotaPenuhException;
import java.io.Serializable;

public abstract class Kursus implements Serializable {
    static final long serialVersionUID = 1L;
    
    String nama;
    String kode;
    int kuota;
    int pesertaTerdaftar;
    int durasi; // dalam jam
    
    public Kursus(String nama, String kode, int kuota, int durasi) {
        this.nama = nama;
        this.kode = kode;
        this.kuota = kuota;
        this.durasi = durasi;
        this.pesertaTerdaftar = 0;
    }
    
    public boolean daftarPeserta() throws KuotaPenuhException {
        if (pesertaTerdaftar >= kuota) {
            throw new KuotaPenuhException(kode, nama);
        }
        pesertaTerdaftar++;
        return true;
    }
    
    // Abstract methods
    public abstract double getBiaya();
    public abstract String getJenisKursus();
    
    // Getter methods
    public String getNama() { return nama; }
    public String getKode() { return kode; }
    public int getKuota() { return kuota; }
    public int getPesertaTerdaftar() { return pesertaTerdaftar; }
    public int getDurasi() { return durasi; }
    public int getSisaKuota() { return kuota - pesertaTerdaftar; }
    
    @Override
    public String toString() {
        return kode + " - " + nama + " (" + getJenisKursus() + ")";
    }
}