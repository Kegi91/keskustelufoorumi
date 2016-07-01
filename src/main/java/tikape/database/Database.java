package tikape.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;

        init();
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = null;

        if (this.onPostgre()) {
            lauseet = postgreLauseet();
        }

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE Alue ("
                + "tunnus SERIAL PRIMARY KEY, "
                + "nimi varchar(50)"
                + ");");
        lista.add("CREATE TABLE Viestiketju ("
                + "tunnus SERIAL PRIMARY KEY, "
                + "alue INTEGER REFERENCES Alue (tunnus), "
                + "otsikko varchar(50), "
                + "luomisaika TIMESTAMP"
                + ");");
        lista.add("CREATE TABLE Viesti ("
                + "tunnus SERIAL PRIMARY KEY, "
                + "ketju INTEGER REFERENCES Viestiketju (tunnus), "
                + "kayttaja varchar(50), "
                + "luomisaika TIMESTAMP, sisalto varchar(500)"
                + ");");

        lista.add("INSERT INTO Alue (nimi) VALUES ('Ohjelmointi');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Musiikki');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Elokuvat');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Muut');");

        lista.add("INSERT INTO Viestiketju (alue, otsikko, luomisaika) "
                + "VALUES (1, 'Java', current_timestamp(0));");

        lista.add("INSERT INTO Viesti (ketju, kayttaja, sisalto, luomisaika) "
                + "VALUES (1, 'Kegi', 'Java ei oo kivaa vaan siedettävää', current_timestamp(0));");

        return lista;
    }
    
    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE Alue ("
                + "tunnus integer PRIMARY KEY, "
                + "nimi varchar(50)"
                + ");");
        lista.add("CREATE TABLE Viestiketju ("
                + "tunnus integer PRIMARY KEY, "
                + "alue INTEGER, "
                + "ostikko varchar(50), "
                + "luomisaika DATETIME, "
                + "FOREIGN KEY(alue) REFERENCES Alue(tunnus)"
                + ");");
        lista.add("CREATE TABLE Viesti ("
                + "tunnus SERIAL PRIMARY KEY, "
                + "ketju INTEGER, "
                + "kayttaja varchar(50), "
                + "luomisaika DATETIME, "
                + "sisalto varchar(500), "
                + "FOREIGN KEY ketju REFERENCES Viestiketju(tunnus)"
                + ");");

        lista.add("INSERT INTO Alue (nimi) VALUES ('Ohjelmointi');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Musiikki');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Elokuvat');");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Muut');");

        lista.add("INSERT INTO Viestiketju(alue, otsikko, luomisaika) "
                + "VALUES (1, 'Java', DATETIME('now','local'));");

        lista.add("INSERT INTO Viesti (ketju, kayttaja, sisalto, luomisaika) "
                + "VALUES (1, 'Kegi', 'Java ei oo kivaa vaan siedettävää', DATETIME('now', 'local'));");

        return lista;
    }

    public boolean onPostgre() {
        return this.databaseAddress.contains("postgre");
    }
}
