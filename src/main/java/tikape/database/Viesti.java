package tikape.database;

/**
 *
 * @author kujuku
 */
public class Viesti {
    private String ketju;
    private String kayttaja;
    private String luomisaika;
    private String sisalto;

    public Viesti(String ketju, String kayttaja, String luomisaika, String sisalto) {
        this.ketju = ketju;
        this.kayttaja = kayttaja;
        this.luomisaika = luomisaika;
        this.sisalto = sisalto;
    }

    public String getKetju() {
        return ketju;
    }

    public String getKayttaja() {
        return kayttaja;
    }

    public String getLuomisaika() {
        return luomisaika;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setKetju(String ketju) {
        this.ketju = ketju;
    }

    public void setKayttaja(String kayttaja) {
        this.kayttaja = kayttaja;
    }

    public void setLuomisaika(String luomisaika) {
        this.luomisaika = luomisaika;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

    @Override
    public String toString() {
        return this.kayttaja + ": " + this.sisalto;
    }
    
    
}
