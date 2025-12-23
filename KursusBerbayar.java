package kursusonline.model;

public class KursusBerbayar extends Kursus {
    static final long serialVersionUID = 2L;
    double biaya;
    
    public KursusBerbayar(String nama, String kode, int kuota, int durasi, double biaya) {
        super(nama, kode, kuota, durasi);
        this.biaya = biaya;
    }
    
    @Override
    public double getBiaya() {
        return biaya;
    }
    
    @Override
    public String getJenisKursus() {
        return "Berbayar";
    }
    
    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }
}