package sklepy.sklep1.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sklepy.sklep1.DatabaseOperations;
import sklepy.sklep1.entities.Comment;
import sklepy.sklep1.entities.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {
    @Autowired
    DatabaseOperations databaseOperations;

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        return "addProduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("amount") BigDecimal amount,
            Model model, HttpSession session) {

        String imagePath = saveImage(imageFile);

        Date today = Date.from(Instant.now());

        Product product = new Product();
        product.setOwner_id(databaseOperations.getUserIdByLogin((String) session.getAttribute("userLogin")));
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price.floatValue());
        product.setImagePath(imagePath);
        product.setAmount(amount.intValue());
        product.setCreation_date(today);
        product.setModification_date(today);
        product.setProductUrl("/");
        databaseOperations.addProductToDatabase(product);

        return "redirect:/user";
    }

    private String saveImage(MultipartFile imageFile) {
        String uploadDir = "src/main/resources/static/images/uploads/";
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        String originalFilename = imageFile.getOriginalFilename();
        String filePath = uploadPath.resolve(UUID.randomUUID() + "_" + originalFilename).toString();

        try {
            Files.copy(imageFile.getInputStream(), Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // /images/uploads/nazwapliku;
        String[] st = filePath.split("/");
        return "/images/uploads/" + st[st.length - 1];
    }

    @PostMapping("/product/{id}/add-comment")
    public String addComment(@PathVariable("id") Long product_id,
                             @RequestParam("comment") String comment,
                             HttpSession session){

        databaseOperations.addCommentToDatabase((String) session.getAttribute("userLogin"), product_id, comment);

        return ("redirect:/products/"+product_id);
    }

    @GetMapping("/products/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model){
        Product product = databaseOperations.getProductByProductId(id);
        model.addAttribute("product", product);
        List<Comment> comments = databaseOperations.getCommentsByProductId(id);
        model.addAttribute("comments", comments);
        return "product";
    }


    @PostMapping("/cart/add-item")
    public String addItemToCart(@RequestParam("product_id") Long product_id, HttpSession session){
        databaseOperations.addPoductToCart((String) session.getAttribute("userLogin"), product_id);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model){
        List<Product> products = databaseOperations.getUserProductsInCart((String) session.getAttribute("userLogin"));
        model.addAttribute("cartItems", products);
        return "cart";
    }
}

