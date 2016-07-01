package tikape.database;

import tikape.domain.Viesti;
import java.util.*;
import java.sql.*;

/**
 *
 * @author kujuku
 */
public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
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
        int ketju = rs.getInt("alue");
        String kayttaja = rs.getString("otsikko");
        String luomisaika = rs.getString("luomisaika");
        String sisalto = rs.getString("sisalto");

        Viesti v = new Viesti(tunnus, ketju, kayttaja, luomisaika, sisalto);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * "
                + "FROM Viesti "
                + "ORDER BY luomisaika DESC;"
        );

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();

        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int ketju = rs.getInt("ketju");
            String kayttaja = rs.getString("kayttaja");
            String sisalto = rs.getString("sisalto");
            String luomisaika = rs.getString("luomisaika");

            viestit.add(new Viesti(tunnus, ketju, kayttaja, sisalto, luomisaika));
        }

        rs.close();
        stmt.close();
        connection.close();

        Collections.reverse(viestit);
        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM Viesti "
                + "WHERE tunnus = ?;"
        );

        stmt.setObject(1, key);
        stmt.execute();

        stmt.close();
        connection.close();
    }

    public void insert(int ketju, String kayttaja, String sisalto) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt;

        if (!this.database.onPostgre()) {
            stmt = connection.prepareStatement(
                    "INSERT INTO Viesti (ketju, kayttaja, sisalto, luomisaika)"
                    + "VALUES (?, ?, ?, DATETIME('now','localtime'));"
            );
        } else {
            stmt = connection.prepareStatement(
                    "INSERT INTO Viesti (ketju, kayttaja, sisalto, luomisaika)"
                    + "VALUES (?, ?, ?, current_timestamp(1));"
            );
        }

        stmt.setObject(1, ketju);
        stmt.setObject(2, kayttaja);
        stmt.setObject(3, sisalto);
        stmt.execute();

        stmt.close();
        connection.close();
    }

    public int findLargestTunnus() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT MAX(tunnus) FROM Viesti;"
        );

        ResultSet rs = stmt.executeQuery();
        int suurin = rs.getInt("MAX(tunnus)");

        stmt.close();
        connection.close();

        return suurin;
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

        String luomisaika = rs.getString("luomisaika");

        rs.close();
        stmt.close();
        connection.close();

        return luomisaika;
    }
}
