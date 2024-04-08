import java.time.LocalDate;

public class Budova {
    private int id;
    private String nazev;
    private boolean hotovo;
    private LocalDate datumPostaveni;

    public Budova(int id, String nazev, boolean hotovo, LocalDate datumPostaveni) {
        this.id = id;
        this.nazev = nazev;
        this.hotovo = hotovo;
        this.datumPostaveni = datumPostaveni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public boolean isHotovo() {
        return hotovo;
    }

    public void setHotovo(boolean hotovo) {
        this.hotovo = hotovo;
    }

    public LocalDate getDatumPostaveni() {
        return datumPostaveni;
    }

    public void setDatumPostaveni(LocalDate datumPostaveni) {
        this.datumPostaveni = datumPostaveni;
    }
}