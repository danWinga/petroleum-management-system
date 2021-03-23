/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.marketers;

/**
 *
 * @author root
 */
import com.blue.database.DatabaseBean;
import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="newmarketers")
@SessionScoped
public class newMarketer implements Serializable {
        private String marketercode;
        private String marketername;
        private String details;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);

        public String getMarketercode(){ return marketercode; }
        public void setMarketercode(String marketercode){ this.marketercode = marketercode; }
	public String getMarketername() { return marketername; }
	public void setMarketername(String marketername) { this.marketername = marketername; }
        public String getDetails(){ return details; }
        public void setDetails(String details){ this.details = details; }
        
        public void newMarket(){             
            String mysql = "INSERT INTO marketers (marketer_code, marketer_name, details) VALUES ('"+ marketercode +"', '"+ marketername +"', '"+ details +"');";         
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
