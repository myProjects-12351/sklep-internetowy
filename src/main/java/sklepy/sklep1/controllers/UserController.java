package sklepy.sklep1.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sklepy.sklep1.DatabaseOperations;
import sklepy.sklep1.entities.Product;
import sklepy.sklep1.entities.User;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    DatabaseOperations databaseOperations;

    @GetMapping("/user")
    public String user(Model model, HttpSession session) {
        if (session != null){
            User user = databaseOperations.getUserByLogin((String) session.getAttribute("userLogin"));
            model.addAttribute("user", user);
            List<Product> products = databaseOperations.getProductsByLogin((String) session.getAttribute("userLogin"));
            model.addAttribute("products", products);
            return "user";
        }
        return "index";
    }
}
