package com.blue.locations;

import com.blue.database.DatabaseBean;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="newlocations")
@SessionScoped
public class NewLocation {
    
    String locationCode;
    String locationName;
    String details;
                
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
    public String getLocationCode(){ return locationCode; }
    public void setLocationCode(String locationCode){ this.locationCode = locationCode; }
    public String getLocationName(){ return locationName; }
    public void setLocationName(String locationName){ this.locationName = locationName; }
    public String getDetails(){ return details; }
    public void setDetails(String details){ this.details = details; }
    public void newLocation(){             
        String mysql = "INSERT INTO locations (location_code, location_name, details) VALUES ('"+ locationCode +"', '"+ locationName +"', '"+ details +"');";         
        System.out.print(mysql);
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
