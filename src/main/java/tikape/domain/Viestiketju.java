package tikape.domain;

/**
 *
 * @author kujuku
 */
public class Viestiketju {
    private int tunnus;
    private int alue;
    private String otsikko;
    private String luomisaika;

    public Viestiketju(int tunnus, int alue, String otsikko, String luomisaika) {
        this.tunnus = tunnus;
        this.alue = alue;
        this.otsikko = otsikko;
        this.luomisaika = luomisaika;
    }

    public int getTunnus() {
        return tunnus;
    }

    public int getAlue() {
        return alue;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public String getLuomisaika() {
        return luomisaika;
    }

    public void setTunnus(int tunnus) {
        this.tunnus = tunnus;
    }

    public void setAlue(int alue) {
        this.alue = alue;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public void setLuomisaika(String luomisaika) {
        this.luomisaika = luomisaika;
    }

    public String getOsoite() {
        return "viestiketju/" + this.tunnus;
    }

    @Override
    public String toString() {
        return this.otsikko;
    }
    
}
