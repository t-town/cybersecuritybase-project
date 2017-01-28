package sec.project.controller;

import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;
import org.springframework.ui.Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    @PersistenceContext
    private EntityManager em;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String loadDone(Model model, @RequestParam(required = false) String admin) {
        if (admin != null) {
            model.addAttribute("admin", true);
        }
        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("list", signups);
        return "done";
    }

    @RequestMapping(value = "/form1", method = RequestMethod.POST)
    public String submitForm(HttpServletResponse response, Model model, @RequestParam String name, @RequestParam String address, @RequestParam String password, @RequestParam String pledge) {
        Signup s = new Signup(name, address, password, pledge);
        signupRepository.save(s);
        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("list", signups);
        Cookie session = new Cookie("sessionId", s.getId().toString());
        response.addCookie(session);
        model.addAttribute("name", s.getName());
        return "done";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletResponse response, Model model, @RequestParam String name, @RequestParam String password) {
        Query q = em.createNativeQuery("SELECT ID from signup WHERE name = '" + name + "' AND password = '" + password + "'");
        if (q.getResultList().isEmpty()) {
            return "login";
        }
        BigInteger id = (BigInteger) q.getResultList().get(0);
        q = em.createNativeQuery("SELECT name from signup WHERE name = '" + name + "' AND password = '" + password + "'");

        Cookie session = new Cookie("sessionId", id.toString());
        response.addCookie(session);
        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("list", signups);
        model.addAttribute("name", q.getResultList().get(0).toString());
        return "done";
    }

    @RequestMapping(value = "/changePledge", method = RequestMethod.POST)
    public String changePledge(HttpServletRequest request, Model model, @RequestParam String newPledge) {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                System.out.println(cookies[i].getName());
                if (cookies[i].getName().equals("sessionId")) {
                    cookie = cookies[i];
                }
            }
        }
        if (cookie == null) {
            return "login";
        }
        Signup sign = signupRepository.findOne(Long.parseLong(cookie.getValue()));
        sign.changePledge(newPledge);
        signupRepository.save(sign);
        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("list", signups);
        model.addAttribute("name", sign.getName());
        return "done";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request, Model model, @RequestParam String user) {
        Query q = em.createNativeQuery("SELECT ID from Signup WHERE name = '" + user + "'");
        if (q.getResultList().isEmpty()) {
            List<Signup> signups = signupRepository.findAll();
            model.addAttribute("list", signups);

            return "done";
        }
        BigInteger s = (BigInteger) q.getResultList().get(0);
        signupRepository.delete(s.longValue());
        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("list", signups);

        return "done";
    }

}
