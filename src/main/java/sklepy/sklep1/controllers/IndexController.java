package sklepy.sklep1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sklepy.sklep1.DatabaseOperations;
import sklepy.sklep1.entities.Product;

import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    DatabaseOperations databaseOperations;

    @GetMapping("/index")
    public String index(Model model) {
        List<String> items = Arrays.asList("item1", "item2", "item3", "item1", "item2", "item3", "item1");
        List<Product> products = databaseOperations.getXProducts(100, 100000000);
        model.addAttribute("items", products);
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("search") String name,
                         @RequestParam("category") String category) {



        return null;
    }

    @GetMapping("/index/filtruj")
    public String filter(@RequestParam("price") float price,
                         Model model) {

        if (price > 0) {
            List<Product> products = databaseOperations.getXProducts(100, price);
            model.addAttribute("items", products);
        }
        return "/index";
    }
}
