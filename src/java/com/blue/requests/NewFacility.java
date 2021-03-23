
package com.blue.requests;
/**
 *
 * @author Softcall Communication
 */
import com.blue.database.DatabaseBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class NewFacility {
        public SelectItem[] stakeholders;
        public SelectItem[] districts;
        public SelectItem[] types;
        public SelectItem[] levels;
        
        String facilityId;
        String districtId;
        String stakeholderId;
        String facilityLevelId;
        String facilityTypeId;
        String facilityCode;
        String facilityName;
        String location;
        String sublocation;
        String details;

        public SelectItem[] getStakeholders() {
            String query = "SELECT stakeholder_id, (first_name || ' ' || second_name || ' ' || COALESCE(other_names, '')) as stakename FROM stakeholders ORDER BY stakename";
            List list = new ArrayList();
            
            TreeMap <String, String>tMap = new TreeMap<String, String>();
            try{
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs=db.preparedState(stmt);
		//ResultSet rs = db.query(query);
                int z = 0;
		while(rs.next()){
                    //list.add(new CountryList(rs.getString("sys_country_id").toString(), rs.getString("sys_country_name").toString()));
                    list.add(z++);
                    tMap.put(rs.getString("stakeholder_id").toString(), rs.getString("stakename").toString());
                    
                }
                rs.close();
                db.cleanup();
            }catch(SQLException e){
                System.out.println("Error 1: "+ e);
            }catch(Exception e){
                System.out.println("Error 2: "+ e);
                System.err.println(e);
            }
            if (list.isEmpty()) {
                tMap.put("--".toString(), "<<No Regions>>".toString());
                list.add("---".toString());
            }
            stakeholders = new SelectItem[list.size()];
            int i = 0;

            Set set = tMap.entrySet();
            Iterator ite = set.iterator();

            while(ite.hasNext()) { 
                Map.Entry me = (Map.Entry)ite.next(); 
                stakeholders[i++] = new SelectItem(me.getKey().toString(), me.getValue().toString());
            } 
            return stakeholders;
        }

        public SelectItem[] getDistrics() {
            String query = "SELECT district_id, district_name FROM districts";
            List list = new ArrayList();
            
            TreeMap <String, String>tMap = new TreeMap<String, String>();
            try{
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs=db.preparedState(stmt);
		//ResultSet rs = db.query(query);
                int z = 0;
		while(rs.next()){
                    //list.add(new CountryList(rs.getString("sys_country_id").toString(), rs.getString("sys_country_name").toString()));
                    list.add(z++);
                    tMap.put(rs.getString("district_id").toString(), rs.getString("district_name").toString());
                    
                }
                rs.close();
                db.cleanup();
            }catch(SQLException e){
                System.out.println("Error 1: "+ e);
            }catch(Exception e){
                System.out.println("Error 2: "+ e);
                System.err.println(e);
            }
            if (list.isEmpty()) {
                tMap.put("--".toString(), "<<No Districts>>".toString());
                list.add("---".toString());
            }
            districts = new SelectItem[list.size()];
            int i = 0;

            Set set = tMap.entrySet();
            Iterator ite = set.iterator();

            while(ite.hasNext()) { 
                Map.Entry me = (Map.Entry)ite.next(); 
                districts[i++] = new SelectItem(me.getKey().toString(), me.getValue().toString());
            } 
            
            return districts;
        }
        
        public SelectItem[] getTypes() {
            String query = "SELECT report_point_id, (report_point_code ||':'|| report_point_name) AS rep_point FROM  report_points;";
            //String query = "SELECT facility_type_id, (type_abbre ||':'|| type_name) AS types FROM facility_type";
            List list = new ArrayList();
            
            TreeMap <String, String>tMap = new TreeMap<String, String>();
            try{
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs=db.preparedState(stmt);
		//ResultSet rs = db.query(query);
                int z = 0;
		while(rs.next()){
                    //list.add(new CountryList(rs.getString("sys_country_id").toString(), rs.getString("sys_country_name").toString()));
                    list.add(z++);
                    tMap.put(rs.getString("report_point_id").toString(), rs.getString("rep_point").toString());
                    
                }
                rs.close();
                db.cleanup();
            }catch(SQLException e){
                System.out.println("Error 1: "+ e);
            }catch(Exception e){
                System.out.println("Error 2: "+ e);
                System.err.println(e);
            }
            if (list.isEmpty()) {
                tMap.put("--".toString(), "<<No Reporting Point>>".toString());
                list.add("---".toString());
            }
            types = new SelectItem[list.size()];
            int i = 0;

            Set set = tMap.entrySet();
            Iterator ite = set.iterator();

            while(ite.hasNext()) { 
                Map.Entry me = (Map.Entry)ite.next(); 
                types[i++] = new SelectItem(me.getKey().toString(), me.getValue().toString());
            } 
            
            return types;
        }

        public SelectItem[] getLevels() {
            String query = "SELECT facility_level_id, level_name FROM facility_level";
            List list = new ArrayList();
            
            TreeMap <String, String>tMap = new TreeMap<String, String>();
            try{
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs=db.preparedState(stmt);
		//ResultSet rs = db.query(query);
                int z = 0;
		while(rs.next()){
                    //list.add(new CountryList(rs.getString("sys_country_id").toString(), rs.getString("sys_country_name").toString()));
                    list.add(z++);
                    tMap.put(rs.getString("facility_level_id").toString(), rs.getString("level_name").toString());
                    
                }
                rs.close();
                db.cleanup();
            }catch(SQLException e){
                System.out.println("Error 1: "+ e);
            }catch(Exception e){
                System.out.println("Error 2: "+ e);
                System.err.println(e);
            }
            if (list.isEmpty()) {
                tMap.put("--".toString(), "<<No Districts>>".toString());
                list.add("---".toString());
            }
            levels = new SelectItem[list.size()];
            int i = 0;

            Set set = tMap.entrySet();
            Iterator ite = set.iterator();

            while(ite.hasNext()) { 
                Map.Entry me = (Map.Entry)ite.next(); 
                levels[i++] = new SelectItem(me.getKey().toString(), me.getValue().toString());
            } 
            
            return levels;
        }
        
        public String getFacilityId(){
                return facilityId;
        }
        public String getDistrictId(){
                return districtId;
        }
        public String getStakeholderId(){
                return stakeholderId;
        }
        public String getFacilityLevelId(){
                return facilityLevelId;
        }
        public String getFacilityTypeId(){
                return facilityTypeId;
        }
        public String getFacilityCode(){
                return facilityCode;
        }
        public String getFacilityName(){
                return facilityName;
        }
        public String getFacilityLocation(){
                return location;
        }
        public String getFacilitySubLocation(){
                return sublocation;
        }
        public String getDetails(){
                return details;
        }

        public void setFacilityId(String facilityId){
                this.facilityId = facilityId;
        }
        public void setDistrictId(String districtId){
                this.districtId = districtId;
        }
        public void setStakeholderId(String stakeholderId){
                this.stakeholderId = stakeholderId;
        }
        public void setFacilityLevelId(String facilityLevelId){
                this.facilityLevelId = facilityLevelId;
        }
        public void setFacilityTypeId(String facilityTypeId){
                this.facilityTypeId = facilityTypeId;
        }
        public void setFacilityCode(String facilityCode){
                this.facilityCode = facilityCode;
        }
        public void setFacilityName(String facilityName){
                this.facilityName = facilityName;
        }
        public void setFacilityLocation(String location){
                this.location = location;
        }
        public void setFacilitySubLocation(String sublocation){
                this.sublocation = sublocation;
        }
        public void setDetails(String details){
                this.details = details;
        }
        
        public String saveData(){
                int disId = Integer.parseInt(getDistrictId());
                int stakeId = Integer.parseInt(getStakeholderId());
                int levelId = Integer.parseInt(getFacilityLevelId());
                //int typeId = Integer.parseInt(getFacilityTypeId());
                String mysql = "INSERT INTO facilities (district_id, stakeholder_id, facility_level_id, facility_code, facility_name, location, sublocation, details) VALUES ("+ disId +", "+ stakeId +", "+ levelId +", '"+ getFacilityCode() +"', '"+  getFacilityName() +"', '"+ getFacilityLocation() +"', '"+ getFacilitySubLocation() +"', '"+ getDetails() +"');";

                System.out.println("New Facility SQL:"+ mysql);

                try{
                    DatabaseBean db = new DatabaseBean();
                    db.insert(mysql);
                    db.cleanup();
                }catch(SQLException e){
                    //e.printStackTrace();
                    System.out.println("Error 1: "+ e);
                }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("Error 2: "+ e);
                }        

                return "List_Facilities";
        }
        public String listFacility(){
            return "List_Facilities";
        }
}
