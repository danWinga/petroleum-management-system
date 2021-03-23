package com.blue.prices;

import com.blue.database.DatabaseBean;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
 
@ManagedBean(name="newprices")
@SessionScoped
public class NewPrice implements Serializable  {
        String priceId, productId, locationId, marketerId, price, productcode, productname, locationcode, locationname, marketercode, marketername, details;
        
        public String getPriceId(){ return priceId; }
        public String getProductId(){ return productId; }
        public String getMarketerId(){ return marketerId; } 
        public String getLocationId(){ return locationId; }
        public String getPrice(){ return price; }
        public String getProductcode(){ return productcode; }
        public String getProductname(){ return productname; }
        public String getLocationcode(){ return locationcode; }
        public String getLocationname(){ return locationname; } 
        public String getMarketercode(){ return marketercode; }
        public String getMarketername(){ return marketername; }
        public String getDetails(){ return details; }

        public void setPriceId(String priceId){ this.priceId = priceId; }
        public void setProductId(String productId){ this.productId = productId; }
        public void setMarketerId(String marketerId){ this.marketerId = marketerId; } 
        public void setLocationId(String locationId){ this.locationId = locationId; }
        public void setPrice(String price){ this.price = price; }
        public void setProductcode(String productcode){ this.productcode = productcode; }
        public void setProductname(String productname){ this.productname = productname; }
        public void setLevelName(String locationcode){ this.locationcode = locationcode; }
        public void setLocationname(String locationname){ this.locationname = locationname; } 
        public void setMarketercode(String marketercode){ this.marketercode = marketercode; }
        public void setMarketername(String marketername){ this.marketername = marketername; }
        public void setDetails(String details){ this.details = details; }
                                
        public void updateData(ActionEvent actionevent){
                //String mysql = "INSERT INTO prices (product_id, location_id, marketer_id, price, details) VALUES ("+ Integer.parseInt(productId) +", "+ Integer.parseInt(locationId) +", "+ Integer.parseInt(marketerId) +",  '"+ price.trim() +"', '"+ details.trim() +"');";
                String mysql = "INSERT INTO prices (product_id, location_id, marketer_id, price, details) "
                        + "SELECT "+ productId +", "+ locationId +", "+ marketerId +",  '"+ price.trim() +"', '"+ details.trim() +"' "
                        + "WHERE "+ productId +" NOT IN (SELECT product_id FROM prices WHERE product_id = "+ productId +" AND location_id = "+ locationId +" AND marketer_id = "+  marketerId +");";

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
