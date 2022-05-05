/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import util.Authentication;
import util.DBConnection;
import util.Validation;

/**
 * This class is the Managed Bean for Product
 * This will hold all the properties related to products table
 */
@ManagedBean
@RequestScoped
public class Product1 {
    
    static Logger log = Logger.getLogger(Product1.class.getName());
    
    private String product_id;
    private String product_name;
    private int available_quantity;
    private double price;
    private String message;
    private Part file;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(int available_quantity) {
        this.available_quantity = available_quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    
    
    
    /**
     * Adds a new product with user entered data from JSF page
     * 
     * @return next message
     * @throws Exception 
     */
    public String addProduct() throws Exception {
        
        try {
            
            // Check if the user entered product id is already exist in products table
            boolean isProductAlreadyExist = DBConnection.isProductAlreadyExist(this.product_id);
            if (isProductAlreadyExist) {
                log.info("Product already exist. Product ID: " + product_id);
                this.setMessage("Product already exist. Product ID: " + product_id);
                return "message.xhtml"; 
            }
            
            // Add the requested product in products table
            DBConnection.addProduct(this);
            
            log.info("Product added successfully. Product ID: " + product_id);
            this.setMessage("Product added successfully.");
            
            // Product image storage path in server
            String uploadsPath = "D:\\Work\\Dev\\NetBeansGroup\\NetBeansProjects\\JavaShop\\web\\images";
            String filename = this.product_id + ".jpg";
            
            // Copy the user uploaded image file into the server
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, new File(uploadsPath, filename).toPath());
            }
            catch (IOException e) {
                log.error(e.getMessage());
            }
            
            return "message.xhtml"; 
            
        } catch (Exception e) {
            String logMessage = "Error: " + e.toString();
            log.error(logMessage);
            //setCustom_message(logMessage);
            this.setMessage(logMessage);
            return "error.xhtml"; 
        }
        
    }
    
    /**
     * Removes the product for user entered product id from JSF page
     * 
     * @return next message
     * @throws Exception 
     */
    public String removeProduct() throws Exception {
        
        try {
            
            // Remove the product record from products table
            int i = DBConnection.removeProduct(this);
            
            // Send exception message if there is no product available
            if (i == 0) {
                throw new Exception("No Product found.");
            }
            
            log.info("Product removed successfully. Product ID: " + product_id);
            this.setMessage("Product removed successfully.");
            return "message.xhtml"; 
            
        } catch (Exception e) {
            String logMessage = "Error: " + e.toString();
            log.error(logMessage);
            //setCustom_message(logMessage);
            this.setMessage(logMessage);
            return "error.xhtml"; 
        }
        
    }
    
    /**
     * Updates the product with user entered data from JSF page
     * 
     * @return next message
     * @throws Exception 
     */
    public String updateQuantity() throws Exception {
        
        try {
            
            // Update the product details in products table
            int i = DBConnection.updateQuantity(this.product_id, this.available_quantity);
            
            // Send exception message if there is no product available
            if (i == 0) {
                throw new Exception("No Product found.");
            }
            
            log.info("Available Quantity is updated successfully. Product ID: " + product_id);
            this.setMessage("Available Quantity is updated successfully.");
            return "message.xhtml"; 
            
        } catch (Exception e) {
            String logMessage = "Error: " + e.toString();
            log.error(logMessage);
            //setCustom_message(logMessage);
            this.setMessage(logMessage);
            return "error.xhtml"; 
            
            
        }
        
    }
    
    
    
}
