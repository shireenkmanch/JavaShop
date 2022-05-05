/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import project.ejbs.Operations;
import project.entities.Products;
import project.web.index;
import util.Authentication;
import util.DBConnection;
import util.Validation;

/**
 * This class is the Managed Bean for User
 * This will hold all the properties related to users table
 */
@ManagedBean
@SessionScoped
public class User {
    
    static Logger log = Logger.getLogger(User.class.getName());
    
    private String userid;
    private String hashed_password;
    private String role;
    private String name;
    private String message;
    private String email_id;
    private String address;
    private String password;
    private String custom_message;

    
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Checks whether the logged in user has administrator access rights
     * 
     * @return true if administrator user, false otherwise.
     */
    public boolean isAdmin() {
        if ("administrator".equals(this.role)) {
            return true;
        } else {
            return false;
        }
    }
    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustom_message() {
        return custom_message;
    }

    public void setCustom_message(String custom_message) {
        this.custom_message = custom_message;
    }
    
    
    /**
     * Authenticates the user
     * Invoked in Login page
     * 
     * @return next page
     * @throws Exception 
     */
    public String authenticate() throws Exception {
        
        boolean isAuthenticated = false;
        try {
            
            if (Validation.isStringEmpty(userid)) {
                throw new Exception("userid is empty");
            }
            
            if (Validation.isStringEmpty(password)) {
                throw new Exception("password is empty");
            }
            
            log.info("Authentication attempt happened. userid: " + userid);
            
            // Get user data from DB
            User user = null;
            user = DBConnection.getUserFromDB(userid);
            
            // Check the user id's presence in DB
            if ((!Validation.isStringEmpty(user.getUserid())) && user.getUserid().equalsIgnoreCase(userid)) {
                isAuthenticated = Authentication.checkPassword(password, user.getHashed_password());
            }
            
            // If authenticated, log & redirect to home page. Redirect to login page again if authentication failed.
            if (isAuthenticated) {
                log.info("Authentication attempt succeeded. userid: " + userid);
                setCustom_message(null);
                setUserValues(user);
                return "home.xhtml"; 
            } else {
                log.info("Authentication attempt failed. userid: " + userid);
                setCustom_message("User ID or Password is invalid. Please try again.");
                return "loginWithMessage.xhtml"; 
            }
            
        } catch (Exception e) {
            String logMessage = "Error: " + e.toString();
            log.error(logMessage);
            setCustom_message(logMessage);
            return "error.xhtml"; 
        }
        
        
    }
    
    /**
     * Override the DB returned values on managed bean
     * @param user 
     */
    public void setUserValues(User user) {
        this.setUserid(user.getUserid());
        this.setHashed_password(user.getHashed_password());
        this.setRole(user.getRole());
        this.setName(user.getName());
        this.setMessage(user.getMessage());
        this.setEmail_id(user.getEmail_id());
        this.setAddress(user.getAddress());
    }
    
    /**
     * Updates the user profile data
     * 
     * @return next page
     * @throws Exception 
     */
    public String updateProfile() throws Exception {
        
        try {
            
            // Update the user in DB with user entered data from JSF page
            DBConnection.updateProfile(this);
            
            log.info("Profile is updated successfully. userid: " + userid);
            setCustom_message("Profile is updated successfully.");
            return "updateProfile.xhtml"; 
            
        } catch (Exception e) {
            String logMessage = "Error: " + e.toString();
            log.error(logMessage);
            setCustom_message(logMessage);
            return "error.xhtml"; 
        }
        
        
    }

    /**
     * Checks whether the logged in user has administrator access rights
     * 
     * @throws IOException 
     */
    public void accessCheck() throws IOException {
        if (!isAdmin()) {
            String logMessage = "Access Denied";
            log.error(logMessage);
            setCustom_message(logMessage);
            // Redirect to error page if not an admin user
            FacesContext.getCurrentInstance().getExternalContext().redirect("error.xhtml");
        }
    }
    
    /**
     * Clears the session while logout
     * 
     * @throws IOException 
     */
    public void logOut() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
    
    /**
     * Creates new account in users table with user entered data from JSF page
     * 
     * @return next page
     * @throws Exception 
     */
    public String register() throws Exception {
        
        try {
            
            if (Validation.isStringEmpty(userid)) {
                throw new Exception("userid is empty");
            }
            
            if (Validation.isStringEmpty(password)) {
                throw new Exception("password is empty");
            }
            
            if (Validation.isStringEmpty(name)) {
                throw new Exception("name is empty");
            }
            if (Validation.isStringEmpty(email_id)) {
                throw new Exception("email_id is empty");
            }
            if (Validation.isStringEmpty(address)) {
                throw new Exception("address is empty");
            }
            if (Validation.isStringEmpty(message)) {
                throw new Exception("message is empty");
            }
            
            // Get the hashed string for the password
            String saltedHash = Authentication.getSaltedHash(password);
            this.setHashed_password(saltedHash);
            
            // Check if the requested user id already exist or not
            boolean isUserAlreadyExist = DBConnection.isUserAlreadyExist(this.userid);
            if (isUserAlreadyExist) {
                // Log the error message & redirect to register page again if user id is already taken
                log.info("User already exist. User ID: " + userid);
                setCustom_message("User already exist. Please try another userid.");
                return "register.xhtml"; 
            }
            
            // Create a new account in users table
            DBConnection.register(this);
            
            log.info("Account is created successfully. userid: " + userid);
            setCustom_message("Your account is created successfully. Please login to continue.");
            return "loginWithMessage.xhtml"; 
            
        } catch (Exception e) {
            String logMessage = "Error: " + e.toString();
            log.error(logMessage);
            setCustom_message(logMessage);
            return "error.xhtml"; 
        }
        
        
    }
    
    
    /**
     * Gets the List of products available from the DB
     * 
     * @return List of products
     * @throws Exception 
     */
    public List<Products> getProducts() throws Exception {
        
        index ind = new index();
        
        List<Products> list = ind.getProducts();
        
        return list;
    }
    

    
    
}
