package tikape;

import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.database.*;

public class Main {

    public static void main(String[] args) throws Exception {
//        testi2();
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        String jdbcOsoite = "jdbc:sqlite:keskustelupalsta.db";

        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }

        Database database = new Database(jdbcOsoite);
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        AlueDao alueDao = new AlueDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        Sovellus sovellus = new Sovellus(database, viestiketjuDao, alueDao, viestiDao);

        sovellus.kaynnista();

//        ville();
//        kuunteleOsoitteetAlueille();
    }

//    public static void ville() throws Exception {
//        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
//        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
//        AlueDao alueDao = new AlueDao(database);
//        ViestiDao viestiDao = new ViestiDao(database);
//        get("/viestiketju", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("viestit", viestiDao.findAll());
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());
//
//        post("/viestiketju", (req, res) -> {
//            String kayttaja = req.queryParams("kayttaja");
//            String sisalto = req.queryParams("sisalto");
//            viestiDao.insert(1, kayttaja, sisalto);
//
//            HashMap map = new HashMap<>();
//            map.put("viestit", viestiDao.findAll());
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());
//
//        get("/alueet", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("alueet", alueDao.findAll());
//
//            return new ModelAndView(map, "Alueet");
//        }, new ThymeleafTemplateEngine());
//
//        post("/alueet", (req, res) -> {
//            String alueenNimi = req.queryParams("alueNimi");
//            alueDao.insert(alueenNimi);
//            kuunteleUusiOsoite();
//
//            HashMap map = new HashMap<>();
//            map.put("viesti", "Lisätty alue: " + alueenNimi);
//            map.put("alueet", alueDao.findAll());
//
//            return new ModelAndView(map, "Alueet");
//        }, new ThymeleafTemplateEngine());
//    }
//
//    public static void kuunteleUusiOsoite() throws Exception {
//        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
//        AlueDao alueDao = new AlueDao(database);
//        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
//        int uusiAlue = alueDao.findSuurinTunnus();
//        get("/alue/" + uusiAlue, (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("aluenimi", alueDao.findOne(uusiAlue).getNimi());
//            map.put("viestiketjut", viestiketjuDao.findAll(uusiAlue));
//            return new ModelAndView(map, "Alue");
//        }, new ThymeleafTemplateEngine());
//    }
//
//    public static void kuunteleOsoitteetAlueille() throws Exception {
//        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
//        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
//        AlueDao alueDao = new AlueDao(database);
//        ViestiDao viestiDao = new ViestiDao(database);
//        for (Viesti viesti : viestiDao.findAll()) {
//            get("/viestiketju/" + , (req, res) -> {
//                HashMap map = new HashMap<>();
//                map.put("viestit", viestiDao.findAll());
//                return new ModelAndView(map, "index");
//            }, new ThymeleafTemplateEngine());
//
//        }
//        for (Alue alue : alueDao.findAll()) {
//            int alueTunnus = alue.getTunnus();
//            get("/alue/" + alueTunnus, (req, res) -> {
//                HashMap map = new HashMap<>();
//                map.put("aluenimi", alue.getNimi());
//                map.put("viestiketjut", viestiketjuDao.findAll(alueTunnus));
//                return new ModelAndView(map, "Alue");
//            }, new ThymeleafTemplateEngine());
//        }
//    }
//
//    public static void testi() throws Exception {
//        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
//        AlueDao alueDao = new AlueDao(database);
//        List<Viestiketju> ketjut = alueDao.findKetjut(2);
//
//        for (Viestiketju vk : ketjut) {
//            System.out.println(vk);
//        }
//    }
//
//    public static void testi2() throws Exception {
//        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
//        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
//        System.out.println(viestiketjuDao.findUusimmanViestinAjankohta(1));
//    }
//
//    public static void esimerkki() throws Exception {
//        Database database = new Database("jdbc:sqlite:opiskelijat.db");
//        database.init();
//
//        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
//
//        get("/", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("viesti", "tervehdys");
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelijat", opiskelijaDao.findAll());
//
//            return new ModelAndView(map, "opiskelijat");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "opiskelija");
//        }, new ThymeleafTemplateEngine());
//    }
}
