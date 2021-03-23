
package com.blue.mails;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
 
@ManagedBean(name="mailbox")
@SessionScoped
public class MailBox implements Serializable {

	//private final long serialVersionUID = 1L;
        public ArrayList<Mail> mails;
        public Mail selectedMail;
        
        FacesContext facesContext;
        HttpSession httpSession;
        
        public MailBox(){
            facesContext = FacesContext.getCurrentInstance();
            httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
            //mails = new ArrayList<Mail>();
        }
        
        public Mail getSelectedMail(){ return selectedMail; }
        public void setSelectedMail(Mail selectedMail){ this.selectedMail = selectedMail; }
        
        public void mailList(){
            String query = "SELECT * FROM mails;"; //ORDER BY facility_name";// WHERE facility_id = ?";// ORDER BY facility_name ASC;";
            mails = new ArrayList<Mail>();
            try {
                    DatabaseBean db = new DatabaseBean();
                    Connection con = db.DBconnect();
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setFetchSize(100);
                    ResultSet rs=db.preparedState(stmt);
                    while(rs.next()){
                        mails.add(new Mail(rs.getString("mail_id"), rs.getString("mail_from"), rs.getString("subject"), rs.getString("mail_date"), rs.getString("mail_time"), rs.getString("mail_body")));
                    }
                    rs.close();
                    db.cleanup();                    
            }catch(SQLException e){

            }catch(Exception e){

            }
        }
        
        public ArrayList<Mail> getMails() { 
            if(mails == null){
                mailList();
            }
            return mails; 
        }
        
        public class Mail implements Serializable  {
            String facilityId, from, subject, date, districtName, stakename; 
            
            public Mail(String facilityId, String from, String subject, String date, String districtName, String stakename){
                this.facilityId = facilityId; 
                this.from = from.trim();
                this.date = date;
                this.subject = subject.trim(); 
                this.districtName = districtName;
                this.stakename = stakename;
            }

            public String getFacilityId(){ return facilityId; }
            public String getFrom(){ return from; }
            public String getSubject(){ return subject; } 
            public String getDate(){ return date; }
            public String getDistrictName(){ return districtName; }
            public String getStakename(){ return stakename; } 
            
            public void setFacilityId(String facilityId){ this.facilityId = facilityId; }
            public void setFrom(String from){ this.from = from; }
            public void setSubject(String subject){ this.subject = subject; } 
            public void setDate(String date){ this.date = date; }
            public void setDistrictName(String districtName){ this.districtName = districtName; }
            public void setStakename(String stakename){ this.stakename = stakename; } 
        }
}
