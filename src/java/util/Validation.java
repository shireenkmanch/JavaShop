/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * This class holds the validation methods for general business
 */
public class Validation {
    
    /**
     * Checks whether the string is empty or not
     * 
     * @param str
     * @return true if string is empty, false otherwise
     */
    public static boolean isStringEmpty(String str) {
        if (str == null || str.trim().length() <= 0) {
            return true;
        } else {
            return false;
        }
    }
    
}
