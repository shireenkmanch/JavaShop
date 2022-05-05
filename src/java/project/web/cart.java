/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import project.entities.Products;
import util.DBConnection;

/**
 * This class is the Managed Bean for Cart
 */
@ManagedBean
@SessionScoped
public class cart implements Serializable {
    
    static Logger log = Logger.getLogger(cart.class.getName());

    private List<Products> products = new ArrayList<>();
    private String custom_message;
    /**
     * Creates a new instance of cart
     */
    public cart() {
    }
    
    /**
     * Adds the product to the cart
     * @param p 
     */
    public void add(Products p) {
        products.add(p);
    }
    
    /**
     * Removes the product from the cart
     * @param p 
     */
    public void remove(Products p) {
        products.remove(p);
    }
    
    /**
     * Gets cart size
     * @return 
     */
    public int getCartCount() {
        return products.size();
    }

    /**
     * Gets the dynamic message content
     * @return custom_message
     */
    public String getCustom_message() {
        return custom_message;
    }

    /**
     * Sets the dynamic message content
     * @param custom_message 
     */
    public void setCustom_message(String custom_message) {
        this.custom_message = custom_message;
    }
    
    
    /**
     * Gets the cart contents as a map
     * @return Products Map
     */
    public Map<Products, Integer> getCartContents() {
        Map<Products, Integer> cartContents = new HashMap<>();
        for (Products obj : products) {
            if (cartContents.containsKey(obj)) {
                cartContents.put(obj, cartContents.get(obj)+1);
            } else {
                cartContents.put(obj, 1);
            }
        }
        return cartContents;
    }
    
    /**
     * Calculates the total amount
     * @return total
     */
    public double getCartTotal() {
        double total = 0;
        Map<Products, Integer> cartContents = new HashMap<>();
        for (Products obj : products) {
            if (cartContents.containsKey(obj)) {
                cartContents.put(obj, cartContents.get(obj)+1);
                total = total + obj.getPrice();
            } else {
                cartContents.put(obj, 1);
                total = total + obj.getPrice();
            }
        }
        return total;
    }
    
    /**
     * Resets the cart
     */
    public void getCleaner() {
        products = new ArrayList<>();
        setCustom_message("");
    }
    
    /**
     * Places the order
     * @param userId 
     */
    public void checkOut(String userId) {
        
        try {
            
            Map<Products, Integer> cartContents = getCartContents();
            
            // Place the order in orders table
            DBConnection.placeOrder(cartContents, userId, getCartTotal());
            
            log.info("Check out is completed successfully.");
            setCustom_message("Your order is placed successfully.");
            
            products = new ArrayList<>();
            
        } catch (Exception e) {
            String logMessage = "Error: " + e.toString();
            log.error(logMessage);
            setCustom_message(e.getMessage());
            
        }
        
        
    }
    
    
}
