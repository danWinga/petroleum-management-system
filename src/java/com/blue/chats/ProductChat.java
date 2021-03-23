package com.blue.chats;

import com.blue.database.DatabaseBean;
import com.blue.products.Products;
import com.blue.products.Products.Product;
import java.io.Serializable;  
import java.lang.String;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.BarChartModel;

@ManagedBean(name="productchat")
//@SessionScoped
//@RequestScoped
public final class ProductChat implements Serializable {  
  
        private BarChartModel categoryModel, statusModel;
        int arraySize = 0;
        String marketerId = "";
        String marketerName = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        String role = "";

        public ProductChat() {  
                role = httpSession.getAttribute("role").toString();
                marketerId = httpSession.getAttribute("marketID").toString();
                marketerName = getMarketer(marketerId);                
        }  

        public BarChartModel getCategoryModel() { 
                createCategoryModel(marketerName, marketerId);
                return categoryModel;  
        }  
        
        public void createCategoryModel(String marketerName, String marketerId) { 
                categoryModel = new BarChartModel();
                Products products = new Products();
                for(Product prod : products.getProductList()){
                        ChartSeries p = new ChartSeries();
                        p.setLabel(prod.getProductCode());
                        p.set(marketerName, getRequests(marketerId, prod.getProductId()));
                        categoryModel.addSeries(p);
                }
                categoryModel.setLegendPosition("nw");
                categoryModel.setLegendRows(1);
        }
        
        
        public CartesianChartModel getCategoryStatus() { 
                createCategoryModelStatus();
                return statusModel;  
        }  
        
        public void createCategoryModelStatus() { 
                statusModel = new BarChartModel();
                Products products = new Products();
                for(Product prod : products.getProductStatus()){
                        ChartSeries p = new ChartSeries();
                        p.setLabel(prod.getProductCode());
                        p.set(prod.getProductCode(), Integer.parseInt(prod.getProductName()));
                        statusModel.addSeries(p);
                }
        }
        
        private int getRequests(String marketerId, String productId){
                int reqst = 0;
                String query;
                if(!marketerId.equals("0")){
                        query = "SELECT product_id, marketer_id, AVG(price::real) AS requests FROM prices WHERE marketer_id = "+ marketerId +" AND product_id = "+ productId +" GROUP BY product_id, marketer_id;"; 
                } else {
                        query = "SELECT product_id, AVG(price::real) AS requests FROM prices WHERE product_id = "+ productId  +" GROUP BY product_id;"; 
                }
                try {
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                                reqst = rs.getInt("requests");
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("SQLException  caught: "+ e);
                }catch(Exception e){
                        System.out.println("Exception caught: "+ e);
                }
                return reqst;
        }
        
        public String getMarketer(String marketerId){
                String marketName = "";
                if(marketerId.equals("0")){ 
                        return "General Average Prices";
                } else {
                        String query = "SELECT marketer_name FROM marketers WHERE marketer_id = "+ marketerId;
                        try {
                                DatabaseBean db = new DatabaseBean();
                                Connection con = db.DBconnect();
                                PreparedStatement stmt = con.prepareStatement(query);
                                ResultSet rs=db.preparedState(stmt);
                                while(rs.next()){
                                        marketName = rs.getString("marketer_name");
                                }
                                rs.close();
                                db.cleanup();
                        }catch(SQLException e){
                                System.out.println("SQLException  caught: "+ e);
                        }catch(Exception e){
                                System.out.println("Exception caught: "+ e);
                        }
                        return marketName;
                }
        }
}
