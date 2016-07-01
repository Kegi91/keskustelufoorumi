package tikape.database;

import tikape.domain.Viestiketju;
import tikape.domain.Viesti;
import tikape.domain.Alue;
import java.util.*;
import java.sql.*;

/**
 *
 * @author kujuku
 */
public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * "
                + "FROM Alue "
                + "WHERE tunnus = ?;"
        );

        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        int tunnus = rs.getInt("tunnus");
        String nimi = rs.getString("nimi");

        Alue a = new Alue(tunnus, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * "
                + "FROM Alue;"
        );

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();

        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            String nimi = rs.getString("nimi");

            alueet.add(new Alue(tunnus, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM Alue "
                + "WHERE tunnus = ?;"
        );

        stmt.setObject(1, key);
        stmt.execute();

        stmt.close();
        connection.close();
    }

    public void insert(String nimi) throws SQLException {
        int uusiTunnus = findSuurinTunnus() + 1;
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Alue VALUES (?, ?);"
        );

        stmt.setObject(1, uusiTunnus);
        stmt.setObject(2, nimi);

        stmt.execute();
        stmt.close();
        connection.close();
    }

    public List<Viestiketju> findKetjut(int alue) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * "
                + "FROM Viestiketju "
                + "WHERE alue = ?;"
        );

        stmt.setObject(1, alue);
        ResultSet rs = stmt.executeQuery();

        List<Viestiketju> viestiketjut = new ArrayList<>();

        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            String otsikko = rs.getString("otsikko");
            String luomisaika = rs.getString("luomisaika");

            viestiketjut.add(new Viestiketju(tunnus, alue, otsikko, luomisaika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestiketjut;
    }

    public int findKetjujenMaara(int alue) throws SQLException {
        return findKetjut(alue).size();
    }

    public int findSuurinTunnus() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT MAX(tunnus) "
                + "FROM Alue;"
        );

        ResultSet rs = stmt.executeQuery();

        int suurin = 0;
        if (rs.next()) {
            suurin = rs.getInt("MAX(tunnus)");
        }

        rs.close();
        stmt.close();
        connection.close();

        return suurin;
    }

    public List<Viesti> findViestit(int alue) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT v.tunnus, v.ketju, v.kayttaja, v.luomisaika, v.sisalto "
                + "FROM Alue a, Viesti v, Viestiketju vk "
                + "WHERE a.tunnus = vk.alue "
                + "AND vk.tunnus = v.ketju "
                + "AND a.tunnus = ? "
                + "ORDER BY v.luomisaika DESC;"
        );

        stmt.setObject(1, alue);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();

        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int ketju = rs.getInt("ketju");
            String kayttaja = rs.getString("kayttaja");
            String luomisaika = rs.getString("luomisaika");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(tunnus, ketju, kayttaja, sisalto, luomisaika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    public int findViestienMaara(int alue) throws SQLException {
        return this.findViestit(alue).size();
    }

    public String findViimeisimmanViestinAika(int alue) throws SQLException {
        if (this.findViestit(alue).size() > 0) {
            return this.findViestit(alue).get(0).getLuomisaika();
        }

        return "-";
    }

    public String findViimeisimmanViestinKayttaja(int alue) throws SQLException {
        if (this.findViestit(alue).size() > 0) {
            return this.findViestit(alue).get(0).getKayttaja();
        }

        return "-";
    }

    public String findViimeisimmanViestinKetju(int alue) throws SQLException {
        String palautettava;
        int ketju;
        ViestiketjuDao dao = new ViestiketjuDao(this.database);
        
        if (this.findViestit(alue).size() == 0) {
            return "-";
        }
        
        ketju = this.findViestit(alue).get(0).getKetju();

        return dao.findOne(ketju).getOtsikko();
    }
}
