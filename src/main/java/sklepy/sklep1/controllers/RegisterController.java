package sklepy.sklep1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sklepy.sklep1.DatabaseOperations;
import sklepy.sklep1.Encryptor;
import sklepy.sklep1.entities.User;

@Controller
public class RegisterController {
    @Autowired
    private DatabaseOperations databaseOperations;
    private Encryptor encryptor;

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/canRegister")
    public String canRegister(@ModelAttribute User user, Model model){
        System.out.println(user.toString());
        model.addAttribute("login", user.getLogin());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());

        if (!databaseOperations.isUserExist(user.getLogin()))
            databaseOperations.registerToDatabase(user);
        else{
            return "register";
        }
        return "redirect:/login";
    }
}
