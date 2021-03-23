/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.products;

/**
 *
 * @author root
 */
import com.blue.locations.*;
import com.blue.database.DatabaseBean;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="newproduct")
@SessionScoped
public class NewProduct {
    
    String productId;
    String productCode;
    String productName;
    String details;
                
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
    public String getProductId(){ return productId; }
    public void setProductId(String productId){ this.productId = productId; }
    public String getProductCode(){ return productCode; }
    public void setProductCode(String productCode){ this.productCode = productCode; }
    public String getProductName(){ return productName; }
    public void setProductName(String productName){ this.productName = productName; }
    public String getDetails(){ return details; }
    public void setDetails(String details){ this.details = details; }
                
    public void newProduct(){
        String mysql = "INSERT INTO products (product_code, product_name, details) VALUES ('"+ getProductCode() +"', '"+ getProductName() +"', '"+ getDetails() +"');";         
        System.out.print(mysql);
        try{
            DatabaseBean db = new DatabaseBean();
            db.insert(mysql);
            db.cleanup();
        }catch(SQLException e){
            System.out.println("SQL Error In Facilities: "+ e);
        }catch(Exception e){
            System.out.println("Exception Error In Facilities: "+ e);
        }        
    }
}
