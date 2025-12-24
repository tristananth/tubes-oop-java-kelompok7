package kursusonline.model;

public class KursusGratis extends Kursus {
    static final long serialVersionUID = 3L;
    
    public KursusGratis(String nama, String kode, int kuota, int durasi) {
        super(nama, kode, kuota, durasi);
    }
    
    @Override
    public double getBiaya() {
        return 0;
    }
    
    @Override
    public String getJenisKursus() {
        return "Gratis";
    }
}