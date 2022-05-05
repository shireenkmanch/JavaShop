/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.web;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.bean.RequestScoped;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import project.ejbs.Operations;
import project.entities.Products;
import project.entities.Users;

/**
 * This class is the connector to query from Operations Class
 */
@ManagedBean
@RequestScoped
public class index implements Serializable {

    /**
     * Creates a new instance of index1
     */
    public index () {
    }
    
    @EJB
    private Operations operations = new Operations();
    
    /**
     * Gets all products from products table
     * @return List of Products
     */
    public List<Products> getProducts() {
        return operations.retrieveProducts();
    }
    
    private String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
    
    /**
     * Gets the Search Results By Product Id
     * @param query
     * @return List of Products
     */
    public List<Products> getSearchResultsById() {
        return operations.retrieveSearchResultsById(searchKey);
    }
    
    /**
     * Gets the Search Results By Product Name
     * @param query
     * @return List of Products
     */
    public List<Products> getSearchResultsByName() {
        return operations.retrieveSearchResultsByName(searchKey);
    }
    
    /**
     * Gets the Search Results By User Id
     * @param query
     * @return List of Users
     */
    public List<Users> getSearchResultsByUserId() {
        return operations.retrieveSearchResultsByUserId(searchKey);
    }
    
    /**
     * Gets the Search Results By User Name
     * @param query
     * @return List of Users
     */
    public List<Users> getSearchResultsByUserName() {
        return operations.retrieveSearchResultsByUserName(searchKey);
    }
    
}
