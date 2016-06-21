package tikape;

import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.database.*;

public class Main {

    public static void main(String[] args) throws Exception {
        testi2();
    }

    public static void testi() throws Exception {
        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
        AlueDao alueDao = new AlueDao(database);
        List<Viestiketju> ketjut = alueDao.findKetjut(2);
        
        for (Viestiketju vk : ketjut) {
            System.out.println(vk);
        }
    }
    
    public static void testi2() throws Exception {
        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        System.out.println(viestiketjuDao.findUusimmanViestinAjankohta(1));        
    }
    
    public static void esimerkki() throws Exception {
        Database database = new Database("jdbc:sqlite:opiskelijat.db");
        database.init();

        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
    }
}
