/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import project.ejbs.Operations;
import project.entities.Products;

/**
 * This class is the Managed Bean for products table
 */
@ManagedBean
@RequestScoped
public class product implements Serializable {

    /**
     * Creates a new instance of product
     */
    public product() {
    }
    
    @EJB
    private Operations operations = new Operations();
    
    @ManagedProperty(value = "#{cart}")
    cart myCart;

    /**
     * Gets the current cart
     * @return cart
     */
    public cart getMyCart() {
        return myCart;
    }

    /**
     * Sets the cart
     * @param myCart 
     */
    public void setMyCart(cart myCart) {
        this.myCart = myCart;
    }
    
    
    /**
     * Gets the query parameter
     * 
     * @return 
     */
    public String getQuery() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("query");
    }
    
    /**
     * Redirect to error page if product is not found
     * @throws IOException 
     */
    public void checkIfQueryExists() throws IOException {
        if (operations.checkIfQueryExists(getQuery()) == 0) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("error.xhtml");
        }
    }
    
    /**
     * Gets the product for the query
     * @return Products
     */
    public Products getProduct(){
        return operations.returnProduct(getQuery());
    }
    
    /**
     * Adds the product to the cart
     */
    public void addToCart() {
        String query = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("query");
        myCart.add(operations.returnProduct(query));
        //return "cart.xhtml";
    }
    
}
