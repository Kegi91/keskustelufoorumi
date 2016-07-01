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

    public void kaynnista() throws Exception {
        kuuntelePaasivunOsoite();
        kuunteleOsoitteetAlueille();
        kuunteleOsoitteetViestiketjuille();
    }

    public void kuuntelePaasivunOsoite() throws Exception {
        get("/alueet", (req, res) -> {
            
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());
            map.put("alueDao", alueDao);
            
            return new ModelAndView(map, "Alueet");
        }, new ThymeleafTemplateEngine());

        post("/alueet", (req, res) -> {
            String uudenAlueenNimi = req.queryParams("alueNimi");
            HashMap map = new HashMap<>();

            if (!uudenAlueenNimi.isEmpty()) {
                alueDao.insert(uudenAlueenNimi);
                map.put("viestiKayttajalle", "Lisätty alue: \"" + uudenAlueenNimi + "\"");
                kuunteleOsoitteetAlueille();
            } else {
                map.put("eiTyhjaaAluetta", "Alueen nimi ei saa olla tyhjä");
            }

            map.put("alueet", alueDao.findAll());
            map.put("alueDao", alueDao);
            
            return new ModelAndView(map, "Alueet");
        }, new ThymeleafTemplateEngine());
    }

    private void muodostaAlueenHashmap(Alue alue, HashMap map) throws Exception {
        map.put("aluenimi", "Alue: " + alue.getNimi());
        map.put("alueosoite", alue.getTunnus());
        map.put("viestiketjut", viestiketjuDao.findAll(alue.getTunnus()));
        map.put("viestiketjuDao", viestiketjuDao);
    }

    public void kuunteleOsoitteetAlueille() throws Exception {
        for (Alue alue : alueDao.findAll()) {
            get("/alue/" + alue.getTunnus(), (req, res) -> {
                HashMap map = new HashMap<>();
                muodostaAlueenHashmap(alue, map);
                return new ModelAndView(map, "Alue");
            }, new ThymeleafTemplateEngine());

            post("/alue/" + alue.getTunnus(), (req, res) -> {
                String viestiketjuNimi = req.queryParams("viestiketjuNimi");
                HashMap map = new HashMap<>();

                if (!viestiketjuNimi.isEmpty()) {
                    viestiketjuDao.insert(alue.getTunnus(), viestiketjuNimi);
                    map.put("viestiKayttajalle", "Lisätty viestiketju: \"" + viestiketjuNimi + "\"");
                    kuunteleOsoitteetViestiketjuille();
                } else {
                    map.put("eiTyhjaaKetjua", "Viestiketjun nimi ei saa olla tyhjä");
                }

                muodostaAlueenHashmap(alue, map);
                return new ModelAndView(map, "Alue");
            }, new ThymeleafTemplateEngine());
        }
    }

    private void muodostaViestiketjunHashmap(int alue, Viestiketju ketju, HashMap map) throws Exception {
        map.put("aluenimi", "Alue: " + alueDao.findOne(alue).getNimi());
        map.put("ketjunimi", "Viestiketju: " + ketju.getOtsikko());
        map.put("ketjuosoite", ketju.getTunnus());
        map.put("viestit", viestiketjuDao.findViestit(ketju.getTunnus()));
        map.put("viestiDao", viestiDao);
        map.put("alueosoite", "/alue/" + alue);
    }

    public void kuunteleOsoitteetViestiketjuille() throws Exception {
        for (Viestiketju viestiketju : viestiketjuDao.findAll()) {
            int viestiketjunTunnus = viestiketju.getTunnus();
            int ketjunAlueTunnus = viestiketju.getAlue();
     
            get("/alue/" + ketjunAlueTunnus + "/viestiketju/" + viestiketjunTunnus, (req, res) -> {
                HashMap map = new HashMap<>();
                muodostaViestiketjunHashmap(ketjunAlueTunnus, viestiketju, map);
            
                return new ModelAndView(map, "Viestiketju");
            }, new ThymeleafTemplateEngine());

            post("/alue/" + ketjunAlueTunnus + "/viestiketju/" + viestiketjunTunnus, (req, res) -> {
                String kayttajaNimi = req.queryParams("kayttajaNimi");
                String sisalto = req.queryParams("sisalto");
                
                viestiDao.insert(viestiketjunTunnus, kayttajaNimi, sisalto);

                HashMap map = new HashMap<>();
                muodostaViestiketjunHashmap(ketjunAlueTunnus, viestiketju, map);
                
                return new ModelAndView(map, "Viestiketju");
            }, new ThymeleafTemplateEngine());
        }
    }
}