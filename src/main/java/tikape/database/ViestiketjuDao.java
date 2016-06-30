package tikape.database;

import tikape.domain.Viestiketju;
import tikape.domain.Viesti;
import java.util.*;
import java.sql.*;

/**
 *
 * @author kujuku
 */
public class ViestiketjuDao {

    private Database database;

    public ViestiketjuDao(Database database) {
        this.database = database;
    }

//    @Override
    public Viestiketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * "
                + "FROM Viestiketju "
                + "WHERE tunnus = ?;"
        );

        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

        int tunnus = rs.getInt("tunnus");
        int alue = rs.getInt("alue");
        String otsikko = rs.getString("otsikko");
        String luomisaika = rs.getString("luomisaika");

        Viestiketju v = new Viestiketju(tunnus, alue, otsikko, luomisaika);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

//    @Override
    public List<Viestiketju> findAll(int a) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * "
                + "FROM Viestiketju "
                + "WHERE alue = ?;"
        );
        
        stmt.setObject(1, a);

        ResultSet rs = stmt.executeQuery();
        List<Viestiketju> viestiketjut = new ArrayList<>();

        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int alue = rs.getInt("alue");
            String otsikko = rs.getString("otsikko");
            String luomisaika = rs.getString("luomisaika");

            viestiketjut.add(new Viestiketju(tunnus, alue, otsikko, luomisaika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestiketjut;
    }

//    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM Viestiketju "
                + "WHERE tunnus = ?;"
        );

        stmt.setObject(1, key);
        stmt.execute();

        stmt.close();
        connection.close();
    }

    public void insert(int alue, String otsikko) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO "
                + "Viestiketju (alue, otsikko, luomisaika)"
                + "VALUES (?, ?, DATETIME('now','localtime'));"
        );

        stmt.setObject(1, alue);
        stmt.setObject(2, otsikko);
        stmt.execute();

        stmt.close();
        connection.close();
    }

    public int findLargestTunnus() throws SQLException {
//        Connection connection = database.getConnection();
//        PreparedStatement stmt = connection.prepareStatement(
//                "SELECT MAX(tunnus) FROM Viestiketju;"
//        );
//
//        stmt.setObject(1, alue);
//        stmt.setObject(2, otsikko);
//        stmt.execute();
//
//        stmt.close();
//        connection.close();

        return 0;
    }

    public List<Viesti> findViestit(int ketju) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * "
                + "FROM Viesti WHERE ketju = ? "
                + "ORDER BY luomisaika DESC;"
        );

        stmt.setObject(1, ketju);
        ResultSet rs = stmt.executeQuery();

        List<Viesti> viestit = new ArrayList<>();

        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            String luomisaika = rs.getString("luomisaika");
            String kayttaja = rs.getString("kayttaja");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(tunnus, ketju, kayttaja, sisalto, luomisaika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    public int findViestienMaara(int ketju) throws SQLException {
        return findViestit(ketju).size();
    }

    public String findUusimmanViestinAjankohta(int ketju) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT luomisaika "
                + "FROM Viesti "
                + "WHERE ketju = ? "
                + "ORDER BY luomisaika DESC "
                + "LIMIT 1;"
        );

        stmt.setObject(1, ketju);
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            return "-";
        }

        String luomisaika = rs.getString("luomisaika");

        rs.close();
        stmt.close();
        connection.close();

        return luomisaika;
    }
}
