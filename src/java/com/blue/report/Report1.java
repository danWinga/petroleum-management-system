package com.blue.report;

import com.blue.database.DatabaseBean;
import com.blue.locations.Locations;
import com.blue.locations.Locations.Location;
import com.blue.products.Products;
import com.blue.products.Products.Product;
import java.io.Serializable;  
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.chart.CartesianChartModel;  
import org.primefaces.model.chart.ChartSeries;


@ManagedBean(name="reports")
@SessionScoped
public class Report1 implements Serializable {  
        private String height;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
    
        private CartesianChartModel categoryModel;  
        public Report1() {  
                createCategoryModel();  
        }  
  
        public CartesianChartModel getCategoryModel() {  
                return categoryModel;  
        }  
  
        private void createCategoryModel() {  
                categoryModel = new CartesianChartModel();  
                ArrayList<Product> productList = new Products().getProductList();
                
                ChartSeries products[] = new ChartSeries[productList.size()];
                int x = 0;
                for(Product prodList : productList){
                        products[x] = new ChartSeries();
                        products[x].setLabel(prodList.getProductName());
                        //System.out.println(prodList.getProductName());
                        try {
                                DatabaseBean db = new DatabaseBean();
                                //String query = "SELECT location_name, SUM(sms_count) AS sms_count FROM vw_report"
                                //        + " WHERE product_id = "+ prodList.getProductId() 
                                //        + " GROUP BY location_name";
                                String query = "SELECT location_code, COUNT(location_code) AS sms_count FROM vw_orders "
                                        +   " WHERE product_code ilike '"+ prodList.getProductCode() +"' ";
                                        
                                        if(!httpSession.getAttribute("role").toString().equals("1")){
                                                if(httpSession.getAttribute("marketID").toString().equals("0")){
                                                        query += "AND user_id = "+ httpSession.getAttribute("myID").toString();
                                                } else {
                                                        query += "AND marketer_id = "+ httpSession.getAttribute("marketID").toString() +" AND status_id > 1";
                                                }
                                        }
                //query += ";";
//                if(!role.equals("0")){
//                    query += " AND marketer_code ilike (SELECT marketer_code FROM marketers WHERE marketer_id = "+ role +")";
//                }
                                        query += " GROUP BY location_code;";
                                
                                ResultSet rs = db.query(query);
                                //System.out.println("SQL: "+ query);
                                products[x].setLabel(prodList.getProductName());
                                while(rs.next()){
                                        products[x].set(rs.getString("location_code").toUpperCase(), rs.getInt("sms_count"));  
                                        //System.out.println(rs.getString("location_name") +"\t"+ rs.getInt("sms_count"));
                                }
                                rs.close();
                                db.cleanup();
                        } catch(Exception e){
                                System.out.println("Exception: "+ e);
                        }
                        categoryModel.addSeries(products[x]); 
                        x++;
                }           
        }  
        public String getHeight(){ 
                //ArrayList<Product> productList = new Products().getProductList();
                ArrayList<Location> locationList = new Locations().getLocationList();
                //height = (productList.size() * 100) +"px";
                height = (locationList.size() * 50) +"px";
                return height; 
        }
}
