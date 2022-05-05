/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import managedbean.Product1;
import managedbean.User;
import org.apache.log4j.Logger;
import project.entities.Products;

/**
 * This class is for DB related functionalities 
 */
public class DBConnection {
    
    static Logger log = Logger.getLogger(DBConnection.class.getName());

    /**
     * Gets the DB connection
     * 
     * @return Connection
     * @throws Exception 
     */
    public static Connection getConnection() throws Exception {

        Connection c = null;
        Class.forName("com.mysql.jdbc.Driver");
        c = DriverManager.getConnection("jdbc:mysql://localhost:3306/javashop_db", "root", "test123$");
        c.setAutoCommit(false);

        return c;
    }

    /**
     * Gets the user record from users table
     * 
     * @param userId
     * @return User
     * @throws Exception 
     */
    public static User getUserFromDB(String userId) throws Exception {

        User user = new User();
        Connection c = getConnection();
        Statement stmt = null;
        String sql = null;

        stmt = c.createStatement();
        sql = "SELECT * FROM users WHERE userid = '" + userId + "'";

        ResultSet rs = stmt.executeQuery(sql);

        // If data available
        if (rs.next()) {
            String userid = rs.getString("userid");
            String hashed_password = rs.getString("hashed_password");
            String role = rs.getString("role");
            String name = rs.getString("name");
            String message = rs.getString("message");
            String email_id = rs.getString("email_id");
            String address = rs.getString("address");

            // Set the result data in User bean
            user.setUserid(userid);
            user.setHashed_password(hashed_password);
            user.setRole(role);
            user.setName(name);
            user.setMessage(message);
            user.setEmail_id(email_id);
            user.setAddress(address);
        }

        rs.close();
        stmt.close();
        c.close();

        return user;
    }
    
    /**
     * Adds the product in products table
     * 
     * @param product
     * @throws Exception 
     */
    public static void addProduct(Product1 product) throws Exception {
        
        Connection c = getConnection();
		
        PreparedStatement stmt=c.prepareStatement("INSERT INTO products VALUES (?, ?, ?, ?)");  
        stmt.setString(1, product.getProduct_id());
        stmt.setString(2, product.getProduct_name());
        stmt.setInt(3, product.getAvailable_quantity());
        stmt.setDouble(4, product.getPrice());  
  
        int i = stmt.executeUpdate();  
        log.info(i+" record inserted into products");  
        
        c.commit();
        c.close();
        
    }
    
    /**
     * Removes the product record from products table
     * 
     * @param product
     * @return Number of entries deleted
     * @throws Exception 
     */
    public static int removeProduct(Product1 product) throws Exception {
        
        Connection c = getConnection();
		
        PreparedStatement stmt=c.prepareStatement("DELETE FROM products WHERE product_id=?");  
        stmt.setString(1, product.getProduct_id());
        
        int i = stmt.executeUpdate();  
        log.info(i+" records deleted from products");  
        
        c.commit();
        c.close();
        
        return i;
    }
    
    /**
     * Updates the product record in products table
     * 
     * @param productId
     * @param availableQuantity
     * @return Number of entries updated
     * @throws Exception 
     */
    public static int updateQuantity(String productId, int availableQuantity) throws Exception {
        
        Connection c = getConnection();
		
        PreparedStatement stmt=c.prepareStatement("UPDATE products SET available_quantity=? WHERE product_id=?");  
        stmt.setInt(1, availableQuantity);
        stmt.setString(2, productId);
        
        int i = stmt.executeUpdate();  
        log.info(i+" records updated in products");  
        
        c.commit();
        c.close();
        
        return i;
    }

    /**
     * Updates the user record in users table
     * 
     * @param user
     * @throws Exception 
     */
    public static void updateProfile(User user) throws Exception {
        
        Connection c = getConnection();
		
        PreparedStatement stmt=c.prepareStatement("UPDATE users SET name=?, message=?, email_id=?, address=? WHERE userid=?");  
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getMessage());
        stmt.setString(3, user.getEmail_id());
        stmt.setString(4, user.getAddress());
        stmt.setString(5, user.getUserid());
        
        int i = stmt.executeUpdate();  
        log.info(i+" records updated in users");  
        
        c.commit();
        c.close();
        
    }
    
    /**
     * Creates new user record in users table
     * 
     * @param user
     * @throws Exception 
     */
    public static void register(User user) throws Exception {
        
        Connection c = getConnection();
		
        PreparedStatement stmt=c.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)");  
        stmt.setString(1, user.getUserid());
        stmt.setString(2, user.getHashed_password());
        stmt.setString(3, "customer");
        stmt.setString(4, user.getName());
        stmt.setString(5, user.getMessage());
        stmt.setString(6, user.getEmail_id());
        stmt.setString(7, user.getAddress());
        
        int i = stmt.executeUpdate();  
        log.info(i+" record inserted into users");  
        
        c.commit();
        c.close();
        
    }
    
    /**
     * Add the order entry in orders table
     * 
     * @param cartContents
     * @param userId
     * @param totalAmount
     * @throws Exception 
     */
    public static void placeOrder(Map<Products, Integer> cartContents, String userId, double totalAmount) throws Exception {
        
        // Read all the products & check the availability in DB
        for (Map.Entry<Products, Integer> entry : cartContents.entrySet()) {
            Products key = entry.getKey();
            Integer value = entry.getValue();
            int availableQuantity = checkAvailability(key.getProductId());
            
            if (availableQuantity < value) {
                throw new Exception("Sorry, The requested Product is not available now.");
            } else {
                // Updates the new available quantity count in products table
                updateQuantity(key.getProductId(), availableQuantity-value);
            }
        }
        
        Connection c = getConnection();
	
        // Constructs the order details
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Products, Integer> entry : cartContents.entrySet()) {
                Products key = entry.getKey();
                Integer value = entry.getValue();
                
                sb.append("[")
                        .append(key.getProductId())
                        .append("-")
                        .append(value)
                        .append("]");
                
            }
        
        PreparedStatement stmt=c.prepareStatement("INSERT INTO orders VALUES (?, ?, ?, ?, ?, ?)");  
        stmt.setString(1, userId + "$" + (String.valueOf(System.currentTimeMillis())));
        stmt.setString(2, userId);
        stmt.setString(3, sb.toString());
        stmt.setString(4, "success");
        stmt.setString(5, String.valueOf(new Date()));
        stmt.setDouble(6, totalAmount);
        
        int i = stmt.executeUpdate();  
        log.info(i+" record inserted into orders");  
        
        c.commit();
        c.close();
        
    }
    
    /**
     * Checks the product availability in products table
     * 
     * @param productId
     * @return
     * @throws Exception 
     */
    public static int checkAvailability(String productId) throws Exception {
        
        int availableQuantity = 0; 
        Connection c = getConnection();
        Statement stmt = null;
        String sql = null;

        stmt = c.createStatement();
        sql = "SELECT available_quantity FROM products WHERE product_id = '" + productId + "'";

        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            availableQuantity = rs.getInt("available_quantity");
        }

        rs.close();
        stmt.close();
        c.close();
        
        return availableQuantity;
    }
    
    /**
     * Checks whether the product entry is already exist in products table
     * 
     * @param productId
     * @return isProductAlreadyExist
     * @throws Exception 
     */
    public static boolean isProductAlreadyExist(String productId) throws Exception {
        
        boolean isProductAlreadyExist = false; 
        Connection c = getConnection();
        Statement stmt = null;
        String sql = null;

        stmt = c.createStatement();
        sql = "SELECT product_id FROM products WHERE product_id = '" + productId + "'";

        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            String dbResult = rs.getString("product_id");
            if (!Validation.isStringEmpty(dbResult)) {
                isProductAlreadyExist = true; 
            }
        }

        rs.close();
        stmt.close();
        c.close();
        
        return isProductAlreadyExist;
    }
    
    /**
     * Checks whether the user entry is already exist in users table
     * 
     * @param userId
     * @return isUserAlreadyExist
     * @throws Exception 
     */
    public static boolean isUserAlreadyExist(String userId) throws Exception {
        
        boolean isUserAlreadyExist = false; 
        Connection c = getConnection();
        Statement stmt = null;
        String sql = null;

        stmt = c.createStatement();
        sql = "SELECT userid FROM users WHERE userid = '" + userId + "'";

        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            String dbResult = rs.getString("userid");
            if (!Validation.isStringEmpty(dbResult)) {
                isUserAlreadyExist = true; 
            }
        }

        rs.close();
        stmt.close();
        c.close();
        
        return isUserAlreadyExist;
    }



}
