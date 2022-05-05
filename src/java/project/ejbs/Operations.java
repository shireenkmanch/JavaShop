/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.ejbs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import project.entities.Products;
import project.entities.Users;

/**
 * This class holds the DB operations related queries & methods
 */
@Stateless
public class Operations {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaShopPU");
    EntityManager em = emf.createEntityManager();

    /**
     * Gets all products from products table
     * @return List of Products
     */
    public List<Products> retrieveProducts() {
        return em.createQuery("SELECT p FROM Products p").getResultList();
    }
    
    /**
     * Checks if the product is exist in products table
     * @param query
     * @return Number of entries exist
     */
    public int checkIfQueryExists(String query) {
        List<Products> products = em.createQuery("SELECT p FROM Products p WHERE p.productId = :productId")
                                    .setParameter("productId", query).getResultList();
        return products.size();
    }
    
    /**
     * Gets the specific product
     * @param query
     * @return Products
     */
    public Products returnProduct(String query) {
        Products product = (Products)em.createQuery("SELECT p FROM Products p WHERE p.productId = :productId")
                .setParameter("productId", query).getSingleResult();
        return product;
    }
    
    /**
     * Gets the Search Results By Product Id
     * @param query
     * @return List of Products
     */
    public List<Products> retrieveSearchResultsById(String query) {
        return em.createQuery("SELECT p FROM Products p WHERE p.productId LIKE '%" + query + "%'").getResultList();
    }
    
    /**
     * Gets the Search Results By Product Name
     * @param query
     * @return List of Products
     */
    public List<Products> retrieveSearchResultsByName(String query) {
        return em.createQuery("SELECT p FROM Products p WHERE p.productName LIKE '%" + query + "%'").getResultList();
    }
    
    /**
     * Gets the Search Results By User Id
     * @param query
     * @return List of Users
     */
    public List<Users> retrieveSearchResultsByUserId(String query) {
        return em.createQuery("SELECT u FROM Users u WHERE u.userid LIKE '%" + query + "%'").getResultList();
    }
    
    /**
     * Gets the Search Results By User Name
     * @param query
     * @return List of Users
     */
    public List<Users> retrieveSearchResultsByUserName(String query) {
        return em.createQuery("SELECT u FROM Users u WHERE u.userid LIKE '%" + query + "%'").getResultList();
    }
    
    
    /**
     * Sample method to connect persistence DB
     * @param object 
     */
    public void persist(Object object) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    
}
