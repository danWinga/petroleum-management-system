package com.blue.prices;

import com.blue.database.DatabaseBean;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
 
@ManagedBean(name="prices")
@SessionScoped
public class Prices implements Serializable {
        public SelectItem[] reportType;
	private final long serialVersionUID = 1L;
        private ArrayList<Price> priceList;
        private ArrayList<Price> filteredPrice;
        private Price selectedPrice;
        private String id;
        
        FacesContext facesContext;
        HttpSession httpSession;

        
        public Prices(){
                facesContext = FacesContext.getCurrentInstance();
                httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
                
                try {
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS howmany FROM orders WHERE user_id = "+ httpSession.getAttribute("myID").toString());
                        ResultSet rs=db.preparedState(stmt);

                        while(rs.next()){
                                //orderNumber = "<font-color=\"red\">"+ rs.getInt("howmany") + "</p>";
                                if(rs.getInt("howmany") > 0){
                                        orderNumber = "("+ rs.getInt("howmany") + ")";
                                } else {
                                        orderNumber = "";
                                }
                        }
                        rs.close();
                        db.cleanup();                    
                }catch(SQLException e){
                        System.out.println("Error :"+ e);
                }catch(Exception e){

                }
        }
                
        public Price getSelectedPrice(){ return selectedPrice; }
        public void setSelectedPrice(Price selectedPrice){ this.selectedPrice = selectedPrice; }
        
        public void przList(String query){
                priceList = new ArrayList<Price>();
                try {
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        stmt.setFetchSize(100);
                        ResultSet rs=db.preparedState(stmt);

                        while(rs.next()){
                                priceList.add(new Price(rs.getString("price_id"), rs.getString("product_id"), 
                                                    rs.getString("location_id"), rs.getString("marketer_id"), 
                                                    rs.getString("price"), rs.getBoolean("instock"), 
                                                    rs.getString("product_code"), rs.getString("product_name"), 
                                                    rs.getString("location_code"), rs.getString("location_name"), 
                                                    rs.getString("marketer_code"), rs.getString("marketer_name"), 
                                                    rs.getString("price_details")));
                        }
                        rs.close();
                        db.cleanup();                    
                }catch(SQLException e){
                        System.out.println("Error :"+ e);
                }catch(Exception e){

                }
        }
        
        private String marketerId;
        public String getMarketerId() { return marketerId; }
        public void setMarketerId(String marketerId) { this.marketerId = marketerId; }
        
        private String productId;
        private String locationId;

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public String getLocationId() { return locationId; }
        public void setLocationId(String locationId) { this.locationId = locationId; }
        
        public void productFilter(){
                priceList = null;
                String sql = "SELECT * FROM vw_prices WHERE marketer_id = "+ marketerId;
                if(!productId.equals("0"))  sql += " AND product_id = "+ productId;
                if(!locationId.equals("0")) sql += " AND location_id = "+ locationId;
                przList(sql);
        }

        public void productPrices(){
                String markId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("marketerid");
                this.marketerId = markId;
                priceList = null;
                String sql = "SELECT * FROM vw_prices WHERE marketer_id = "+ marketerId;
                przList(sql);
        }
        
        public ArrayList<Price> getPriceList() { 
                String query = "SELECT * FROM vw_prices "; 
                if(!"0".equals(httpSession.getAttribute("marketID").toString())){
                        query += " WHERE marketer_id = '"+ httpSession.getAttribute("marketID").toString() +"'";
                }
                query += ";";
                if(priceList == null){
                        przList(query);
                }
                return priceList; 
        }
        
        public void refresh(){
                priceList = null;
        }
        
        public void onEdit(RowEditEvent event) {  
                String stock = ((Price) event.getObject()).getInstock() ? "In Stock":"Out of Stock";
                try {
                        DatabaseBean db = new DatabaseBean();
                        db.insert("UPDATE prices SET price = "+ Double.parseDouble(((Price) event.getObject()).getPrice()) +", instock = "+ ((Price) event.getObject()).getInstock() +" WHERE price_id = "+ ((Price) event.getObject()).getPriceId() +";");
                        db.cleanup();
                        FacesMessage msg = new FacesMessage(((Price) event.getObject()).getProductname() +",  "+ ((Price) event.getObject()).getLocationname(), "New Price is KShs."+ ((Price) event.getObject()).getPrice() +"/= and it is "+ stock);  
                        FacesContext.getCurrentInstance().addMessage(null, msg); 
                } catch(SQLException e){
                        System.out.println("SQL Error In Facilities: "+ e);
                        FacesMessage msg = new FacesMessage("Sorry! ", "New Price update failed");  
                        FacesContext.getCurrentInstance().addMessage(null, msg); 
                } catch(Exception e){
                        System.out.println("Exception Error In Facilities: "+ e);
                        FacesMessage msg = new FacesMessage("Sorry! ", "New Price update failed");  
                        FacesContext.getCurrentInstance().addMessage(null, msg); 
                }
        }  
      
        public void onCancel(RowEditEvent event) {  
                FacesMessage msg = new FacesMessage(((Price) event.getObject()).getProductname() +",  "+ ((Price) event.getObject()).getLocationname(), "Price update cancelled");  
                FacesContext.getCurrentInstance().addMessage(null, msg);  
                //System.out.println("Price ID: "+ ((Price) event.getObject()).getPriceId() +"\t "+ ((Price) event.getObject()).getPrice());
        }
        
        public ArrayList<Price> getFilteredPrice() {  
                return filteredPrice;  
        }  
  
        public void setFilteredPrice(ArrayList<Price> filteredPrice) {  
                this.filteredPrice = filteredPrice;  
        }  
        
        private String orderNumber;
        public void setOrderNumber(String orderNumber){ this.orderNumber = orderNumber; }
        public String getOrderNumber(){ return orderNumber; }

        public class Price implements Serializable  {
                String priceId, productId, locationId, marketerId, price, productcode, productname, locationcode, locationname, marketercode, marketername, details;
                boolean instock;
                String stocked;
                String litres;
                String amount;
                String notes;
                
                public Price(String priceId, String productId, String locationId, String marketerId, String price, boolean instock, String productcode, String productname, String locationcode, String locationname, String marketercode, String marketername, String details){
                        this.priceId = priceId; 
                        this.productId = productId.trim();
                        this.locationId = locationId;
                        this.marketerId = marketerId.trim(); 
                        this.price = price;
                        this.instock = instock;
                        this.productcode = productcode;
                        this.productname = productname;
                        this.locationcode = locationcode;
                        this.locationname = locationname;
                        this.marketercode = marketercode;
                        this.marketername = marketername;
                        this.details = details;
                }

                public String getPriceId(){ return priceId; }
                public String getProductId(){ return productId; }
                public String getMarketerId(){ return marketerId; } 
                public String getLocationId(){ return locationId; }
                public String getPrice(){ return price; }
                public boolean getInstock(){ return instock; }
                public String getProductcode(){ return productcode; }
                public String getProductname(){ return productname; }
                public String getLocationcode(){ return locationcode; }
                public String getLocationname(){ return locationname; } 
                public String getMarketercode(){ return marketercode; }
                public String getMarketername(){ return marketername; }
                public String getDetails(){ return details; }
                public String getStocked(){ return getInstock() ? "Yes":"No"; }

                public void setPriceId(String priceId){ this.priceId = priceId; }
                public void setProductId(String productId){ this.productId = productId; }
                public void setMarketerId(String marketerId){ this.marketerId = marketerId; } 
                public void setLocationId(String locationId){ this.locationId = locationId; }
                public void setPrice(String price){ this.price = price; }
                public void setInstock(boolean instock){ this.instock = instock; }
                public void setProductcode(String productcode){ this.productcode = productcode; }
                public void setProductname(String productname){ this.productname = productname; }
                public void setLevelName(String locationcode){ this.locationcode = locationcode; }
                public void setLocationname(String locationname){ this.locationname = locationname; } 
                public void setMarketercode(String marketercode){ this.marketercode = marketercode; }
                public void setMarketername(String marketername){ this.marketername = marketername; }
                public void setDetails(String details){ this.details = details; }
                public void setStocked(String stocked){ this.stocked = stocked; }

                public String getLitres() { return litres; }
                public void setLitres(String litres) { this.litres = litres; }
                
                public String getNotes(){ return notes; }
                public void setNotes(String notes){ this.notes = notes; }
                
                public String getAmount(){ return amount; }
                public void setAmount(String amount){ this.amount = amount; }
                public void increment() { 
                        if(litres.trim().length() > 0){
                                double tot = (Double.parseDouble(litres) * 1000 * Double.parseDouble(price)); 
                                amount = new DecimalFormat("#,##0.00").format(tot);
                        } else {
                                amount = new DecimalFormat("#,##0.00").format(0);
                        }
                }
                
                public void updateData(ActionEvent actionevent){
                        String mysql = "UPDATE prices SET price = "+ price.trim() +", details = '"+ details.trim() +"' WHERE price_id = "+ Integer.parseInt(priceId.trim()) +";";
                        dbAction(mysql);
                }
                
                public void placeOrder(){
                        String userId = httpSession.getAttribute("myID").toString();
                        String mysql = "INSERT INTO orders (user_id, price_id, price, lrts, order_number, details) VALUES ("+ userId +", "+ priceId +", '"+ price +"','"+ (Double.parseDouble(litres) * 1000) +"', (SELECT order_numbering('"+ priceId +"')), '"+ notes.trim() +"');";
                        if(litres.trim().length() > 0){ 
                                FacesContext context = FacesContext.getCurrentInstance(); 
                                if(dbAction(mysql) > 0){
                                        context.addMessage(null, new FacesMessage("Bravo,","Your Order was created Successfully"));
                                } else {
                                        context.addMessage(null, new FacesMessage("Sorry,","System encountered a problem trying to create your Order. Try again later"));
                                }
                                
                        }
                }
                
                public String onFlowProcess(FlowEvent event) {
                        return event.getNewStep();
                }
                
                public void updateOrder(){
                        String mysql = "UPDATE orders SET details = '"+ notes.trim() +"', status_id = 2 WHERE order_id = "+ httpSession.getAttribute("orderId").toString() +";";
                        dbUpdate(mysql);
                        getUrl("PDFReports.pdf?reportName=invoice&fileName="+ new Date().toString() +"&id="+ httpSession.getAttribute("orderId").toString());
                }
                
                public void getUrl(String pageName){
                        FacesContext context = FacesContext.getCurrentInstance();
                        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest(); 
                        try {
                                context.getExternalContext().redirect(request.getContextPath() + "/"+ pageName);
                        } catch(IOException ex){ }
                }

                public int dbAction(String mysql){
                        int affected = -1;
                        try{
                                DatabaseBean db = new DatabaseBean();
                                affected = db.insert(mysql);
                                //ResultSet rs = db.query(mysql);
                                //while(rs.next()){
                                //        affected = rs.getInt("order_id");
                                //}
                                db.cleanup();
                        }catch(SQLException e){
                                System.out.println("SQL Error In Facilities: "+ e);
                        }catch(Exception e){
                                System.out.println("Exception Error In Facilities: "+ e);
                        }
                        return affected;
                }
                
                public int dbUpdate(String mysql){
                        int affected = -1;
                        try{
                                DatabaseBean db = new DatabaseBean();
                                affected = db.insert(mysql);
                                db.cleanup();
                        }catch(SQLException e){
                                System.out.println("SQL Error In dbUpdate: "+ e);
                        }catch(Exception e){
                                System.out.println("Exception Error In dbUpdate: "+ e);
                        }
                        return affected;
                }
        }
}
