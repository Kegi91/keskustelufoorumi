package tikape;

import tikape.domain.*;
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
            map.put("alueDao", alueDao);
            return new ModelAndView(map, "Alueet");
        }, new ThymeleafTemplateEngine());

        post("/alueet", (req, res) -> {
            String alueNimi = req.queryParams("alueNimi");
            if (!alueNimi.isEmpty()) {
                alueDao.insert(alueNimi);
            }
            kuunteleOsoiteUudelleAlueelle();

            HashMap map = new HashMap<>();
            map.put("viestiKayttajalle", "Lisätty alue: " + alueNimi);
            map.put("alueet", alueDao.findAll());
            map.put("alueDao", alueDao);
            return new ModelAndView(map, "Alueet");
        }, new ThymeleafTemplateEngine());

        this.kuunteleOsoitteetAlueille();
        this.kuunteleOsoitteetViestiketjuille();
    }

    public void kuunteleOsoiteUudelleAlueelle() throws Exception {
        int uudenAlueenTunnus = alueDao.findSuurinTunnus();
        get("/alue/" + uudenAlueenTunnus, (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aluenimi", alueDao.findOne(uudenAlueenTunnus).getNimi());
            map.put("alueosoite", uudenAlueenTunnus);
            map.put("viestiketjut", viestiketjuDao.findAll(uudenAlueenTunnus));
            map.put("viestiketjuDao", viestiketjuDao);
            return new ModelAndView(map, "Alue");
        }, new ThymeleafTemplateEngine());

        post("/alue/" + uudenAlueenTunnus, (req, res) -> {
            String viestiketjuNimi = req.queryParams("viestiketjuNimi");
            int uudenViestiketjunTunnus = viestiketjuDao.findSuurinTunnus();
            viestiketjuDao.insert(uudenAlueenTunnus, viestiketjuNimi);
            Alue viestiketjunAlue = alueDao.findOne(uudenAlueenTunnus);
            kuunteleOsoitteetViestiketjuille();

            HashMap map = new HashMap<>();
            map.put("aluenimi", viestiketjunAlue.getNimi());
            map.put("alueosoite", uudenAlueenTunnus);
            map.put("viestiketjut", viestiketjuDao.findAll(uudenAlueenTunnus));
            map.put("viestiketjuDao", viestiketjuDao);
            return new ModelAndView(map, "Alue");
        }, new ThymeleafTemplateEngine());
    }

    public void kuunteleOsoitteetAlueille() throws Exception {
        for (Alue alue : alueDao.findAll()) {
            int alueTunnus = alue.getTunnus();
            get("/alue/" + alueTunnus, (req, res) -> {
                HashMap map = new HashMap<>();
                map.put("aluenimi", alue.getNimi());
                map.put("alueosoite", alueTunnus);
                map.put("viestiketjut", viestiketjuDao.findAll(alueTunnus));
                map.put("viestiketjuDao", viestiketjuDao);
                return new ModelAndView(map, "Alue");
            }, new ThymeleafTemplateEngine());

            post("/alue/" + alueTunnus, (req, res) -> {
                String viestiketjuNimi = req.queryParams("viestiketjuNimi");
                int uudenViestiketjunTunnus = viestiketjuDao.findSuurinTunnus();
                viestiketjuDao.insert(alueTunnus, viestiketjuNimi);
                kuunteleOsoitteetViestiketjuille();

                HashMap map = new HashMap<>();
                map.put("aluenimi", alue.getNimi());
                map.put("alueosoite", alueTunnus);
                map.put("viestiketjut", viestiketjuDao.findAll(alueTunnus));
                map.put("viestiketjuDao", viestiketjuDao);
                return new ModelAndView(map, "Alue");
            }, new ThymeleafTemplateEngine());
        }
    }

    public void kuunteleOsoitteetViestiketjuille() throws Exception {
        for (Viestiketju viestiketju : viestiketjuDao.findAll()) {
            int ketju = viestiketju.getTunnus();
            int alue = viestiketju.getAlue();
            get("/alue/" + alue + "/viestiketju/" + ketju, (req, res) -> {
                HashMap map = new HashMap<>();
                map.put("aluenimi", alueDao.findOne(alue).getNimi());
                map.put("ketjunimi", viestiketju.getOtsikko());
                map.put("ketjuosoite", ketju);
                map.put("viestit", viestiketjuDao.findViestit(ketju));
                map.put("viestiDao", viestiDao);
                return new ModelAndView(map, "Viestiketju");
            }, new ThymeleafTemplateEngine());

            post("/alue/" + alue + "/viestiketju/" + ketju, (req, res) -> {
                String kayttajaNimi = req.queryParams("kayttajaNimi");
                String sisalto = req.queryParams("sisalto");
                viestiDao.insert(ketju, kayttajaNimi, sisalto);

                HashMap map = new HashMap<>();
                map.put("aluenimi", alueDao.findOne(alue).getNimi());
                map.put("ketjunimi", viestiketju.getOtsikko());
                map.put("ketjuosoite", ketju);
                map.put("viestit", viestiketjuDao.findViestit(ketju));
                map.put("viestiDao", viestiDao);
                return new ModelAndView(map, "Viestiketju");
            }, new ThymeleafTemplateEngine());
        }
    }
}
