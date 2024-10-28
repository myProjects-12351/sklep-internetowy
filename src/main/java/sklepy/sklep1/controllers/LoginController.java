package sklepy.sklep1.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sklepy.sklep1.DatabaseOperations;

@Controller
public class LoginController {
    @Autowired
    DatabaseOperations databaseOperations;

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping("/canLogin")
    public String canLogin(@RequestParam("login") String login, @RequestParam("password") String password, HttpSession session){
        if (databaseOperations.canUserLogin(login, password)) {
            session.setAttribute("session", session);
            session.setAttribute("userLogin", login);
            return "redirect:/index";
        } else {
            return "redirect:/login";
        }
    }

}
