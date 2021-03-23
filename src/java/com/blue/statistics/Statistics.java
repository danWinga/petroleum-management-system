
package com.blue.statistics;
/**
 *
 * @author Softcall Communication
 */
import com.blue.database.DatabaseBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
 
@ManagedBean(name="statistics")
@SessionScoped
public class Statistics implements Serializable {
	private final long serialVersionUID = 1L;
        private List<Stats> statsList;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        public Statistics(){
                statsList = null;
                reqList();
        }
        
        public void printing(){
                statsList = null;
                reqList();
        }
        
        public List<Stats> getStatsList() { return statsList; }
        
        private void reqList(){
            String query = "SELECT marketer_name, product_name, location_name, smsdate, sms_count FROM vw_report ";
            if(!"0".equals(httpSession.getAttribute("role").toString())){
                    query += " WHERE marketer_name = '"+ marketerName(httpSession.getAttribute("role").toString()) +"'";
            }
            query += " ORDER BY smsdate DESC;";
            if(statsList == null){
                statsList = new ArrayList<Stats>();
                try {
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                                statsList.add(new Stats(rs.getString("marketer_name"), rs.getString("product_name"), rs.getString("location_name"), rs.getString("smsdate"), rs.getString("sms_count")));
                        }
                        rs.close();
                        db.cleanup();                    
                }catch(SQLException e){
                        System.out.println("SQLException  caught: "+ e);
                }catch(Exception e){
                        System.out.println("Exception caught: "+ e);
                }
            }
        }
        
        public String marketerName(String marketerId){
            String marName = "";
            try {
                    DatabaseBean db = new DatabaseBean();
                    Connection con = db.DBconnect();
                    PreparedStatement stmt = con.prepareStatement("SELECT marketer_name FROM marketers WHERE marketer_id = "+ marketerId +" Limit 1");
                    ResultSet rs=db.preparedState(stmt);
                    while(rs.next()){
                            marName = rs.getString("marketer_name");
                    }
                    rs.close();
                    db.cleanup();                    
            }catch(SQLException e){
                    System.out.println("SQLException  caught: "+ e);
            }catch(Exception e){
                    System.out.println("Exception caught: "+ e);
            }
            return marName;
        }
        
        public class Stats  {
                String marName, prodName, locName, smsDate, smsCount;
                public Stats(String marName, String prodName, String locName, String smsDate, String smsCount){
                        this.marName = marName;
                        this.prodName = prodName; 
                        this.locName = locName; 
                        this.smsDate = smsDate; 
                        this.smsCount = smsCount; 
                }

                public String getMarName(){ return marName; }
                public String getProdName(){ return prodName; } 
                public String getLocName(){ return locName; }
                public String getSmsDate(){ return smsDate; }
                public String getSmsCount(){ return smsCount; }

                public void setMarName(String marName){ this.marName = marName; }
                public void setProdName(String prodName){ this.prodName = prodName; } 
                public void setLocName(String locName){ this.locName = locName; }
                public void setSmsDate(String smsDate){ this.smsDate = smsDate; }
                public void setSmsCount(String smsCount){ this.smsCount = smsCount; }
        }
}
