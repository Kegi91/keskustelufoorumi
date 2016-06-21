package tikape.database;

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

        String alue;
        int alue_tunnus = rs.getInt("tunnus");
        String nimi = rs.getString("nimi");

        Alue a = new Alue(alue_tunnus, nimi);

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
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Alue (nimi)"
                + "VALUES (?);"
        );

        stmt.setObject(1, nimi);
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
}
