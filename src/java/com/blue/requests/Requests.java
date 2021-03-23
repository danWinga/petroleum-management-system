package com.blue.requests;

import com.blue.database.DatabaseBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
 
@ManagedBean(name="requests")
//@SessionScoped
@RequestScoped
public class Requests implements Serializable {
	private final long serialVersionUID = 1L;
        
        private List<Request> inboxList;
        private Request selectedInbox;
        
        private List<Request> outboxList;
        private Request selectedOutbox;
        
        private List<Request> sentList;
        private Request selectedSent;
        
        private List<Request> filteredList;
        
        public Requests(){
                inboxList = reqList("1");
                outboxList = reqList("2");
                sentList = reqList("3");
        }
        
	public List<Request> getFilteredList(){ return  filteredList; }
	public void setFilteredList(List<Request> filteredList){ this.filteredList = filteredList; }

        public List<Request> getInboxList() { 
                if(inboxList == null){ reqList("1"); }
                return inboxList;
        }
        
        public List<Request> getOutboxList() { 
                if(outboxList == null){ reqList("2"); }
                return outboxList;
        }
        
        public List<Request> getSentList() { 
                if(sentList == null){ reqList("3"); }
                return sentList;
        }
        
        public void printing(){
                inboxList = null;
                inboxList = reqList("1");
                outboxList = null;
                outboxList = reqList("2");
                sentList = null;
                sentList = reqList("3");
        }
        
        public void refreshSent(){

        }
        
        private List<Request> reqList(String folderId){
                String query = "SELECT smsId, phonenumber, folderid, message, smsdate, TO_CHAR((smstime || ' second')::interval, 'HH24:MI') AS smstime, delivery_status FROM sms WHERE folderid = "+ folderId +" ORDER BY smsId DESC";
                List smslist = new ArrayList<Request>();
                try {
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                                String delivery = rs.getString("delivery_status");
                                if(delivery == null) delivery = "2";
                                smslist.add(new Request(rs.getString("smsId"), rs.getString("phonenumber"), rs.getString("folderid"), rs.getString("message"), rs.getString("smsdate"), rs.getString("smstime"), delivery));
                        }
                        rs.close();
                        db.cleanup();                    
                }catch(SQLException e){
                        System.out.println("SQLException  caught: "+ e);
                }catch(Exception e){
                        System.out.println("Exception caught: "+ e);
                }
                return smslist;
        }
                        
        public class Request  {
                String smsId, phonenumber, folderid, message, smsdate, smstime, delivery;

                public Request(String smsId, String phonenumber, String folderid, String message, String smsdate, String smstime, String delivery){
                        this.smsId = smsId;
                        this.phonenumber = phonenumber; 
                        this.folderid = folderid; 
                        this.message = message; 
                        this.smsdate = smsdate; 
                        this.smstime = smstime;
                        this.delivery = delivery;
                }

                public String getSmsId(){ return smsId; }
                public String getPhonenumber(){ return phonenumber; } 
                public String getFolderId(){ return folderid; }
                public String getMessage(){ return message; }
                public String getSmsdate(){ return smsdate; }
                public String getSmstime(){ return smstime; }
                public String getDelivery(){ 
                        return delivery.equals("3") ? "Delivered":"Pending";
                }

                public void setSmsId(String smsId){ this.smsId = smsId; }
                public void setPhonenumber(String phonenumber){ this.phonenumber = phonenumber; } 
                public void setFolderId(String folderid){ this.folderid = folderid; }
                public void setMessage(String message){ this.message = message; }
                public void setSmsdate(String smsdate){ this.smsdate = smsdate; }
                public void setSmstime(String smstime){ this.smstime = smstime; }
                public void setDelivery(String delivery){ this.delivery = delivery; }
        }
}
