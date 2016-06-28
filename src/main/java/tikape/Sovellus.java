package tikape;

import tikape.database.*;
import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Sovellus {
    private Database database;
    private ViestiketjuDao viestiketjuDao;
    private AlueDao alueDao;
    private ViestiDao viestiDao;
    
    public Sovellus(Database database, ViestiketjuDao viestiketjuDao, AlueDao alueDao, ViestiDao viestiDao) {
        this.database = database;
        this.viestiketjuDao = viestiketjuDao;
        this.alueDao = alueDao;
        this.viestiDao = viestiDao;
    }
    
    public void alueet() throws Exception {
         get("/alueet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "Alueet");
        }, new ThymeleafTemplateEngine());

        post("/alueet", (req, res) -> {
            String alueenNimi = req.queryParams("alueNimi");
            alueDao.insert(alueenNimi);
            kuunteleUusiOsoite();

            HashMap map = new HashMap<>();
            map.put("viesti", "Lisätty alue: " + alueenNimi);
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "Alueet");
        }, new ThymeleafTemplateEngine());
        
        this.kuunteleOsoitteetAlueille();
    }
    
    public void kuunteleUusiOsoite() throws Exception {
        int uusiAlue = alueDao.findSuurinTunnus();
        get("/alue/" + uusiAlue, (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aluenimi", alueDao.findOne(uusiAlue).getNimi());
            map.put("viestiketjut", viestiketjuDao.findAll(uusiAlue));
            return new ModelAndView(map, "Alue");
        }, new ThymeleafTemplateEngine());
    }
    
    public void kuunteleOsoitteetAlueille() throws Exception {
//        for (Viesti viesti : viestiDao.findAll()) {
//            get("/viestiketju/" + , (req, res) -> {
//                HashMap map = new HashMap<>();
//                map.put("viestit", viestiDao.findAll());
//                return new ModelAndView(map, "index");
//            }, new ThymeleafTemplateEngine());
//
//        }
        for (Alue alue : alueDao.findAll()) {
            int alueTunnus = alue.getTunnus();
            get("/alue/" + alueTunnus, (req, res) -> {
                HashMap map = new HashMap<>();
                map.put("aluenimi", alue.getNimi());
                map.put("viestiketjut", viestiketjuDao.findAll(alueTunnus));
                return new ModelAndView(map, "Alue");
            }, new ThymeleafTemplateEngine());
        }
    }
}
