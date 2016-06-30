package tikape.domain;

/**
 *
 * @author kujuku
 */
public class Viesti {
    private int tunnus;
    private int ketju;
    private String kayttaja;
    private String luomisaika;
    private String sisalto;

    public Viesti(int tunnus, int ketju, String kayttaja, String sisalto, String luomisaika) {
        this.tunnus = tunnus;
        this.ketju = ketju;
        this.kayttaja = kayttaja;
        this.luomisaika = luomisaika;
        this.sisalto = sisalto;
    }

    public int getTunnus() {
        return tunnus;
    }

    public int getKetju() {
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

    public void setTunnus(int tunnus) {
        this.tunnus = tunnus;
    }

    public void setKetju(int ketju) {
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
        return this.sisalto;
    }
    
    
}
