package kursusonline.exception;

public class KuotaPenuhException extends Exception {
    String kodeKursus;
    String namaKursus;
    
    public KuotaPenuhException(String kodeKursus, String namaKursus) {
        super("Kuota untuk kursus " + namaKursus + " (" + kodeKursus + ") sudah penuh!");
        this.kodeKursus = kodeKursus;
        this.namaKursus = namaKursus;
    }
    
    public String getKodeKursus() { return kodeKursus; }
    public String getNamaKursus() { return namaKursus; }
}