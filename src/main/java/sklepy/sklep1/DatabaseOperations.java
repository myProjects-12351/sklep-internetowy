package sklepy.sklep1;

import org.springframework.stereotype.Component;
import sklepy.sklep1.entities.Address;
import sklepy.sklep1.entities.Comment;
import sklepy.sklep1.entities.Product;
import sklepy.sklep1.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseOperations {
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Encryptor encryptor = new Encryptor();
    private String sql;
    private Connection connection;
    Random random = new Random();

    private Connection connectToDatabase() {
        String dbURL = "jdbc:mysql://192.168.56.99:3306/shop1";
        String username = "shop_user";
        String userpassword = "password";

        try {
            return DriverManager.getConnection(dbURL, username, userpassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canUserLogin(String login, String password) {
        sql = "SELECT * FROM users WHERE login = ? AND password = ?";

        String hashedPass = encryptor.encryptString(password);
        System.out.println("HASHED PASSWORD: " + hashedPass);
        System.out.println("NORMAL PASS: " + password);
        connection = connectToDatabase();

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, hashedPass);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserByLogin(String login) {
        User user = new User();
        sql = "SELECT * FROM users WHERE login = ?";

        try (Connection connection = connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setUser_id(resultSet.getLong("user_id"));
                user.setLogin(resultSet.getString("login"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("POBRANO ID: "+ user.toString());
        return user;
    }

    public boolean isUserExist(String login){
        sql = "SELECT * FROM users WHERE login = ?";
        Connection connection = connectToDatabase();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void registerToDatabase(User user){
        user.setPassword(encryptor.encryptString(user.getPassword()));
        try {
            Connection connection = connectToDatabase();

            sql = "INSERT INTO users (login, password, email, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            int status1 = statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Address> getAddressesByLogin(String user){
        List<Address> addresses = new ArrayList<>();
        long id = getUserIdByLogin(user);

        try {
            Connection connection = connectToDatabase();

            sql = "SELECT * FROM addresses WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Address address = new Address();
                address.setTask_id(resultSet.getLong("address_id"));
                address.setUser_id(resultSet.getLong("user_id"));
                address.setFname(resultSet.getString("fname"));
                address.setLname(resultSet.getString("lname"));
                address.setCountry(resultSet.getString("country"));
                address.setCity(resultSet.getString("city"));
                address.setRoad(resultSet.getString("road"));
                address.setPostal_code(resultSet.getString("postal_code"));

                addresses.add(address);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return addresses;
    }


    public long getUserIdByLogin(String user){
        Long id = null;
        try {
            Connection connection = connectToDatabase();

            sql = "SELECT user_id FROM users WHERE login = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("user_id");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public List<Product> getProductsByLogin(String user){
        List<Product> products = new ArrayList<>();
        long id = getUserIdByLogin(user);
        try {
            Connection connection = connectToDatabase();
            sql = "SELECT * FROM products WHERE owner_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProduct_id(resultSet.getLong("id_products"));
                product.setOwner_id(resultSet.getLong("owner_id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getFloat("price"));
                product.setDescription(resultSet.getString("description"));
                product.setImagePath(resultSet.getString("imagePaths"));

                products.add(product);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public void addProductToDatabase(Product product) {
        try {
            Connection connection = connectToDatabase();

            sql = "INSERT INTO products (owner_id, name, price, description, imagePaths, amount, creation_date, mod_date, url) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, product.getOwner_id());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setFloat(3, product.getPrice());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setString(5, product.getImagePath());
            preparedStatement.setInt(6, product.getAmount());
            preparedStatement.setDate(7, (new Date(product.getCreation_date().getTime())));
            preparedStatement.setDate(8, (new Date(product.getModification_date().getTime())));
            preparedStatement.setString(9, product.getProductUrl());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getXProducts(int amountOfProducts, float price) {
        List<Product> products = new ArrayList<>();

        try {
            Connection connection = connectToDatabase();

            sql = "SELECT * FROM products WHERE price < ? LIMIT ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setFloat(1, price);
            preparedStatement.setInt(2, amountOfProducts);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Product product = new Product();
                product.setProduct_id(resultSet.getLong("id_products"));
                product.setOwner_id(resultSet.getLong("owner_id"));
                product.setName(resultSet.getString("name"));
                product.setAmount(resultSet.getInt("amount"));
                product.setImagePath(resultSet.getString("imagePaths"));
                product.setPrice(resultSet.getFloat("price"));
                product.setDescription(resultSet.getString("description"));
                product.setCreation_date(resultSet.getDate("creation_date"));
                product.setModification_date(resultSet.getDate("mod_date"));
                product.setProductUrl(resultSet.getString("url"));
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return products;
    }

    public void addCommentToDatabase(String user, Long id_product, String text) {
        Long uid = getUserIdByLogin(user);

        try {
            Connection connection = connectToDatabase();
            sql = "INSERT INTO comments (product_id, user_id, author, text) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id_product);
            preparedStatement.setLong(2, uid);
            preparedStatement.setString(3, user);
            preparedStatement.setString(4, text);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Comment> getCommentsByProductId(Long id_product) {
        List<Comment> comments = new ArrayList<>();
        try {
            Connection connection = connectToDatabase();
            sql = "SELECT * FROM comments WHERE product_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id_product);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setComment_id(resultSet.getLong("id_comment"));
                comment.setProduct_id(resultSet.getLong("product_id"));
                comment.setUser_id(resultSet.getLong("user_id"));
                comment.setText(resultSet.getString("text"));
                comment.setAuthor(resultSet.getString("author"));
                comments.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    public Product getProductByProductId(Long id){
        Product product = new Product();
        try {
            Connection connection = connectToDatabase();
            sql = "SELECT * FROM products WHERE id_products = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product.setProduct_id(resultSet.getLong("id_products"));
                product.setOwner_id(resultSet.getLong("owner_id"));
                product.setName(resultSet.getString("name"));
                product.setAmount(resultSet.getInt("amount"));
                product.setImagePath(resultSet.getString("imagePaths"));
                product.setPrice(resultSet.getFloat("price"));
                product.setDescription(resultSet.getString("description"));
                product.setCreation_date(resultSet.getDate("creation_date"));
                product.setModification_date(resultSet.getDate("mod_date"));
                product.setProductUrl(resultSet.getString("url"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public void addPoductToCart(String user, Long product_id) {
        try {
            Connection connection = connectToDatabase();
            sql = "INSERT INTO cart (user_id, product_id, amount) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, getUserIdByLogin(user));
            preparedStatement.setLong(2, product_id);
            preparedStatement.setLong(3,1);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Product> getUserProductsInCart(String user) {
        List<Product> products = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        List<Long> amounts = new ArrayList<>();

        try {
            Connection connection = connectToDatabase();
            sql = "SELECT * FROM cart WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, getUserIdByLogin(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getLong("product_id"));
                amounts.add(resultSet.getLong("amount"));
            }

            for(int i=0; i<ids.size(); i++) {
                Product product = getProductByProductId(ids.get(i));
                product.setAmountL(amounts.get(i));
                products.add(product);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return products;
    }

}
