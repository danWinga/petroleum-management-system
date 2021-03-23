/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.users;

/**
 *
 * @author root
 */
import com.blue.requests.*;
import com.blue.database.DatabaseBean;
import java.io.Serializable;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
 
@ManagedBean(name="newusers")
@SessionScoped
public class NewUser {
        int userid;
        public SelectItem[] marketers;
        boolean active;
        String username, password, marketer, emailname, roleId, phonenum, address, details;


        public int getUserId(){ return userid; }
        public String getUsername(){ return username; } 
        public String getPassword(){ return password; }
        public String getMarketer(){ return marketer; }
        public String getEmailname(){ return emailname; }
        public String getRoleId(){ return roleId; }
        public boolean getActive(){ return active; }
        public String getPhonenum(){ return phonenum; }
        public String getAddress(){ return address; }
        public String getDetails(){ return details; }

        public void setUserId(int userid){ this.userid = userid; }
        public void setUsername(String username){ this.username = username; } 
        public void setPassword(String password){ this.password = password; }
        public void setMarketer(String marketer){ this.marketer = marketer; }
        public void setEmailname(String emailname){ this.emailname = emailname; }
        public void setRoleId(String roleId){ this.roleId = roleId; }
        public void setActive(boolean active){ this.active = active; }
        public void setPhonenum(String phonenum){ this.phonenum = phonenum; }
        public void setAddress(String address){ this.address = address; }
        public void setDetails(String details){ this.details = details; }
        
        public SelectItem[] getMarketers() {
            String query = "SELECT marketer_id, marketer_name AS marketer FROM marketers;";
            List list = new ArrayList();            
            TreeMap <String, String>tMap = new TreeMap<String, String>();
            try{
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs=db.preparedState(stmt);
                int z = 0;
                
                list.add(z++);
                tMap.put("0", "Blue technologies Admin");
                list.add(z++);
                //tMap.put(xx +"", "Petrol Station Owner");
                tMap.put("x", "Petrol Station Owner");
                
		while(rs.next()){
                        list.add(z++);
                        tMap.put(rs.getString("marketer_id").toString(), rs.getString("marketer").toString());
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
                tMap.put("--".toString(), "<<No Marketer>>".toString());
                list.add("---".toString());
            }
            marketers = new SelectItem[list.size()];
            int i = 0;
            Set set = tMap.entrySet();
            Iterator ite = set.iterator();
            while(ite.hasNext()) { 
                Map.Entry me = (Map.Entry)ite.next(); 
                marketers[i++] = new SelectItem(me.getKey().toString(), me.getValue().toString());
            } 
            return marketers;
        }

                
        public void updateData(ActionEvent actionevent){
                if(marketer.equals("x")){
                    roleId = "3";
                    marketer = "0";
                } else if(marketer.equals("0")){
                    roleId = "1";
                    marketer = "0";
                } else {
                    roleId = "2";
                }
                String mysql = "INSERT INTO users (username, psword, marketer, email_name, role_id, active, phone_number, address, details)"
                    +"VALUES ('"+ username +"', '"+ password +"', '"+ marketer +"', '"+ emailname +"', '"+ roleId +"', '"+ active +"', '"+ phonenum +"', '"+ address +"', '"+ details +"');";

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