package com.blue.products;

import com.blue.database.DatabaseBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="products")
@SessionScoped
public class Products implements Serializable{
	private final long serialVersionUID = 1L;        
        private ArrayList<Product> productList;
        private Product selectedProduct;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
	public ArrayList<Product> getProductStatus() {
            String query = "SELECT status_name, count(status_name) AS count FROM vw_orders GROUP BY status_name";
            productList = new ArrayList<Product>();

            try{
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs=db.preparedState(stmt);
		while(rs.next()){
                        productList.add(new Product(rs.getString("status_name"), rs.getString("count")));
		}
                rs.close();
                db.cleanup();
            }catch(SQLException e){
                System.out.println("Error 1: "+ e);
            }catch(Exception e){
                System.out.println("Error 2: "+ e);
            }
            return productList;
	}

	public ArrayList<Product> getProductList() {
            String query = "SELECT product_id, product_code, product_name, details FROM products";
            productList = new ArrayList<Product>();

            try{
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs=db.preparedState(stmt);
		while(rs.next()){
                        productList.add(new Product(rs.getString("product_id"), rs.getString("product_code"), rs.getString("product_name"), rs.getString("details")));
		}
                rs.close();
                db.cleanup();
            }catch(SQLException e){
                System.out.println("Error 1: "+ e);
            }catch(Exception e){
                System.out.println("Error 2: "+ e);
            }
            return productList;
	}
        
        public Product getSelectedProduct(){ return selectedProduct; }
        public void setSelectedProduct(Product selectedProduct){ this.selectedProduct = selectedProduct; }
        
        public class Product {
                String productId;
                String productCode;
                String productName;
                String details;
                
		public Product(String productId, String productCode, String productName, String details) {
                        this.productId = productId;
			this.productCode = productCode;
                        this.productName = productName;
                        this.details = details;
		}
                
		public Product(String productCode, String productName) {
			this.productCode = productCode;
                        this.productName = productName;
		}
		
                public String getProductId(){ return productId; }
                public void setProductId(String productId){ this.productId = productId; }
                public String getProductCode(){ return productCode; }
                public void setProductCode(String productCode){ this.productCode = productCode; }
                public String getProductName(){ return productName; }
                public void setProductName(String productName){ this.productName = productName; }
                public String getDetails(){ return details; }
                public void setDetails(String details){ this.details = details; }
                
                public void updateProduct(){
                        dbAction("UPDATE products SET product_code = '"+ getProductCode() +"', product_name = '"+ getProductName() +"', details = '"+ details +"' WHERE product_id = '"+ getProductId() +"';");
                }
                public void deleteProduct(){
                        dbAction("DELETE FROM products WHERE product_id = '"+ getProductId() +"';");
                }
                public void dbAction(String mysql){
                        System.out.println(mysql);
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

}
