package com.blue.marketers;

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

@ManagedBean(name="marketers")
@SessionScoped
public class Marketers implements Serializable{
	private static final long serialVersionUID = 1L;        
        private static ArrayList<Marketer> marketerList;
        private Marketer selectedMarketer;
        
        private String marketercode;
        private String marketername;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        public Marketers(){

        }

        public String getMarketercode(){ return marketercode; }
        public void setMarketercode(String marketercode){ this.marketercode = marketercode; }
	public String getMarketername() { return marketername; }
	public void setMarketername(String marketername) { this.marketername = marketername; }
        public void newMarket(){ 
                new Marketer().dbAction("INSERT INTO marketers (marketer_code, marketer_name) VALUES ('"+ marketercode +"', '"+ marketername +"');"); 
        }
        
        public Marketer getSelectedMarketer(){ return selectedMarketer; }
        public void setSelectedMarketer(Marketer selectedMarketer){ this.selectedMarketer = selectedMarketer; }
        
	public ArrayList<Marketer> getMarketerList() {
                if(marketerList == null){
                        getMarList();
                }
                return marketerList;
        }
        public void getMarList() {
                String query = "SELECT marketer_id, marketer_code, marketer_name, details FROM marketers;";
                marketerList = new ArrayList<Marketer>();

                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                            marketerList.add(new Marketer(rs.getString("marketer_id"), rs.getString("marketer_code"), rs.getString("marketer_name"), rs.getString("details")));
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("Error 1: "+ e);
                }catch(Exception e){
                        System.out.println("Error 2: "+ e);
                }

                //return marketerList;
	}

        public class Marketer implements Serializable {
		String marketerId;
		String marketercode;
                String marketername;
                String details;
                
		public Marketer(String marketerId, String marketercode, String marketername, String details) {
                        this.marketerId = marketerId;
			this.marketercode = marketercode;
                        this.marketername = marketername;
                        this.details = details;
		}
                public Marketer(){}
		
		public String getMarketerId() { return marketerId; }
                public void setMarketerId(String marketerId){ this.marketerId = marketerId; }
                public String getMarketercode(){ return marketercode; }
                public void setMarketercode(String marketercode){ this.marketercode = marketercode; }
		public String getMarketername() { return marketername; }
		public void setMarketername(String marketername) { this.marketername = marketername; }
                public String getDetails(){ return details; }
                public void setDetails(String details){ this.details = details; }
                public void updateMarketer(){
                        dbAction("UPDATE marketers SET marketer_code = '"+ marketercode +"', marketer_name = '"+ marketername +"', details = '"+ details +"' WHERE marketer_id = "+ Integer.parseInt(marketerId.trim()) +";");
                }
                public void deleteMarketer(){
                        dbAction("DELETE FROM marketers WHERE marketer_id = "+ Integer.parseInt(marketerId.trim()) +";");
                }
                public void sessionMarketer(){
                        httpSession.setAttribute("marketerid", marketerId.trim());
                }
                public void dbAction(String mysql){
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
