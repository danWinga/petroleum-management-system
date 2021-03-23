package com.blue.orders;

import com.blue.database.DatabaseBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="orders")
@SessionScoped
//@RequestScoped
public class Orders implements Serializable {
	private static final long serialVersionUID = 1L;        
        private static ArrayList<Order> orderList;
        private List<Order> filteredOrder;
        private Order selectedOrder;
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        public Orders(){
                
        }
        
        public List<Order> getFilteredOrder() { return filteredOrder; }
        public void setFilteredOrder(List<Order> filteredOrder) { this.filteredOrder = filteredOrder; }
    
        public Order getSelectedOrder(){ return selectedOrder; }
        public void setSelectedOrder(Order selectedOrder){ this.selectedOrder = selectedOrder; }
        
	public ArrayList<Order> getOrderList() {
                if(orderList == null){
                        getMarList();
                }
                return orderList;
        }
        
        public void refresh(){
                orderList = null;
        }
        public void getMarList() {
                String query = "SELECT * FROM vw_orders ";
                if(!httpSession.getAttribute("role").toString().equals("1")){
                        if(httpSession.getAttribute("marketID").toString().equals("0")){
                                query += "WHERE user_id = "+ httpSession.getAttribute("myID").toString();
                        } else {
                                query += "WHERE marketer_id = "+ httpSession.getAttribute("marketID").toString();// +" AND status_id > 1";
                        }
                        
                }
                query += "ORDER BY order_date DESC, order_time DESC ;";
                orderList = new ArrayList<Order>();

                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                                orderList.add(new Order(rs.getString("order_id"), rs.getDate("order_date"), rs.getTime("order_time"), rs.getString("user_id"), rs.getString("status_id"), rs.getString("price_id"), 
                                                        rs.getString("order_price"), rs.getString("lrts"), rs.getString("order_details"), rs.getString("username"), rs.getString("role_id"), rs.getString("email_name"), 
                                                        rs.getString("phone_number"), rs.getString("address"), rs.getString("user_details"), rs.getString("product_id"), rs.getString("location_id"), 
                                                        rs.getString("marketer_id"), rs.getString("current_price"), rs.getBoolean("instock"), rs.getString("product_code"), rs.getString("product_name"), 
                                                        rs.getString("location_code"), rs.getString("location_name"), rs.getString("location_details"), rs.getString("marketer_code"), 
                                                        rs.getString("marketer_name"), rs.getString("marketer_details"), rs.getString("price_details"), rs.getString("status_name"), rs.getString("full_name")));
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("Error 1: "+ e);
                }catch(Exception e){
                        System.out.println("Error 2: "+ e);
                }
	}
                
        
        public class Order implements Serializable {
                String orderId;
                Date orderDate;
                Date orderTime;
                String userId;
                String statusId;
                String priceId;
                String orderPrice;
                String lrts;
                String orderDetails;
                String username;
                String roleId;
                String emailName;
                String phoneNumber;
                String address;
                String userDetails;
                String productId;
                String locationId;
                String marketerId;
                String currentPrice;
                boolean instock;
                String productCode;
                String productName;
                String locationCode;
                String locationName;
                String locationDetails;
                String marketerCode;
                String marketerName;
                String marketerDetails;
                String priceDetails;
                String orderStatus;
                String flag;
                String tooltip;
                String fullName;
                String reportLink;
                String totalAmount;
                
                public Order(String orderId, Date orderDate, Date orderTime, String userId, String statusId, String priceId, String orderPrice, String lrts,
                        String orderDetails, String username, String roleId, String emailName, String phoneNumber, String address, String userDetails, 
                        String productId, String locationId, String marketerId, String currentPrice, boolean instock, String productCode, String productName,
                        String locationCode, String locationName, String locationDetails, String marketerCode, String marketerName, String marketerDetails, 
                        String priceDetails, String orderStatus, String fullName){
                    
                        this.orderId = orderId;
                        this.orderDate = orderDate;
                        this.orderTime = orderTime;
                        this.userId = userId;
                        this.statusId = statusId;
                        this.priceId = priceId;
                        this.orderPrice = orderPrice;
                        this.lrts = lrts;
                        this.orderDetails = orderDetails;
                        this.username = username;
                        this.roleId = roleId;
                        this.emailName = emailName;
                        this.phoneNumber = phoneNumber;
                        this.address = address;
                        this.userDetails = userDetails;
                        this.productId = productId;
                        this.locationId = locationId;
                        this.marketerId = marketerId;
                        this.currentPrice = currentPrice;
                        this.instock = instock;
                        this.productCode = productCode;
                        this.productName = productName;
                        this.locationCode = locationCode;
                        this.locationName = locationName;
                        this.locationDetails = locationDetails;
                        this.marketerCode = marketerCode;
                        this.marketerName = marketerName;
                        this.marketerDetails = marketerDetails;
                        this.priceDetails = priceDetails;
                        this.orderStatus = orderStatus;
                        this.fullName = fullName;
                        this.reportLink = "PDFReports.pdf?reportName=invoice&fileName="+ fullName +"&id="+ orderId;
                        this.totalAmount = new DecimalFormat("#,##0.00").format((Double.parseDouble(lrts) * Double.parseDouble(orderPrice)));
                }
                
                public String getTotalAmount(){ return totalAmount; }
                public void setTotalAmount(String totalAmount){ this.totalAmount = totalAmount; }

                public String getOrderId() { return orderId; }
                public void setOrderId(String orderId) { this.orderId = orderId; }

                public Date getOrderDate() { return orderDate; }
                public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

                public Date getOrderTime() { return orderTime; }
                public void setOrderTime(Date orderTime) { this.orderTime = orderTime; }

                public String getUserId() { return userId; }
                public void setUserId(String userId) { this.userId = userId; }

                public String getStatusId() { return statusId; }
                public void setStatusId(String statusId) { this.statusId = statusId; }

                public String getPriceId() { return priceId; }
                public void setPriceId(String priceId) { this.priceId = priceId; }

                public String getOrderPrice() { return orderPrice; }
                public void setOrderPrice(String orderPrice) { this.orderPrice = orderPrice; }

                public String getLrts() { return lrts; }
                public void setLrts(String lrts) { this.lrts = lrts; }

                public String getOrderDetails() { return orderDetails; }
                public void setOrderDetails(String orderDetails) { this.orderDetails = orderDetails; }

                public String getUsername() { return username; }
                public void setUsername(String username) { this.username = username; }

                public String getRoleId() { return roleId; }
                public void setRoleId(String roleId) { this.roleId = roleId; }

                public String getEmailName() { return emailName; }
                public void setEmailName(String emailName) { this.emailName = emailName; }

                public String getPhoneNumber() { return phoneNumber; }
                public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

                public String getAddress() { return address; }
                public void setAddress(String address) { this.address = address; }

                public String getUserDetails() { return userDetails; }
                public void setUserDetails(String userDetails) { this.userDetails = userDetails; }

                public String getProductId() { return productId; }
                public void setProductId(String productId) { this.productId = productId; }

                public String getLocationId() { return locationId; }
                public void setLocationId(String locationId) { this.locationId = locationId; }

                public String getMarketerId() { return marketerId; }
                public void setMarketerId(String marketerId) { this.marketerId = marketerId; }

                public String getCurrentPrice() { return currentPrice; }
                public void setCurrentPrice(String currentPrice) { this.currentPrice = currentPrice; }

                public boolean isInstock() { return instock; }
                public void setInstock(boolean instock) { this.instock = instock; }

                public String getProductCode() { return productCode; }
                public void setProductCode(String productCode) { this.productCode = productCode; }

                public String getProductName() { return productName; }
                public void setProductName(String productName) { this.productName = productName; }

                public String getLocationCode() { return locationCode; }
                public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

                public String getLocationName() { return locationName; }
                public void setLocationName(String locationName) { this.locationName = locationName; }

                public String getLocationDetails() { return locationDetails; }
                public void setLocationDetails(String locationDetails) { this.locationDetails = locationDetails; }

                public String getMarketerCode() { return marketerCode; }
                public void setMarketerCode(String marketerCode) { this.marketerCode = marketerCode; }

                public String getMarketerName() { return marketerName; }
                public void setMarketerName(String marketerName) { this.marketerName = marketerName; }

                public String getMarketerDetails() { return marketerDetails; }
                public void setMarketerDetails(String marketerDetails) { this.marketerDetails = marketerDetails; }

                public String getPriceDetails() { return priceDetails; }
                public void setPriceDetails(String priceDetails) { this.priceDetails = priceDetails; }
                
                public String getOrderStatus() { return orderStatus; }
                public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
                
                public void setFlag(String flag){ this.flag = flag;}
                public String getFlag(){
                        if (statusId.equals("1")){
                                flag = "/images/yellow.png";
                                tooltip = "New Order";
                        }else if (statusId.equals("2")){
                                flag = "/images/green.png";
                                tooltip = "Order submitted to marketer";
                        }else if (statusId.equals("3")){
                                flag = "/images/red.png";
                                tooltip = "Order processing by marketer";
                        }
                        return flag;
                }

                public String getFullName() { return fullName; }
                public void setFullName(String fullName) { this.fullName = fullName; }

                public String getReportLink() { return reportLink; }
                public void setReportLink(String reportLink) { this.reportLink = reportLink; }
                
                public void acceptOrder(){
                        FacesContext context = FacesContext.getCurrentInstance(); 
                        if(dbAction("UPDATE orders SET status_id = 2 WHERE order_id = "+ orderId) > 0){
                                context.addMessage(null, new FacesMessage("Bravo,","Order Accepted Successfully"));
                        } else {
                                context.addMessage(null, new FacesMessage("Sorry,","Error Accepting Order"));
                        }
                }
                
                public void declineOrder(){
                        FacesContext context = FacesContext.getCurrentInstance(); 
                        if(dbAction("UPDATE orders SET status_id = 3 WHERE order_id = "+ orderId) > 0){
                                context.addMessage(null, new FacesMessage("Bravo,","Order declined Successfully"));
                        } else {
                                context.addMessage(null, new FacesMessage("Sorry,","Order not declined"));
                        }
                }                
                
                private int dbAction(String mysql){
                        int affected = -1;
                        try { 
                                DatabaseBean db = new DatabaseBean();
                                affected = db.update(mysql);
                                db.cleanup();
                        } catch(Exception e){
                                System.out.println("Orders Update Exception: "+ e);
                        }
                        return affected; 
                }
        }
}
