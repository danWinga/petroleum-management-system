
package com.blue.users;
/**
 *
 * @author Softcall Communication
 */
import com.blue.requests.*;
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
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
 
@ManagedBean(name="users")
@SessionScoped
public class Users implements Serializable {
        public SelectItem[] stakeholders;
        public SelectItem[] districts;
        public SelectItem[] types;
        public SelectItem[] levels;
	private final long serialVersionUID = 1L;
        
        private List<User> usersList;
        private User selectedUser;
        private List<User> filteredUser;
        
        public Users(){
            usersList = new ArrayList<User>();
            reqList();
        }
        
	public List<User> getFilteredUser(){
            return  filteredUser;
	}
	public void setFilteredUser(List<User> filteredUser){
		this.filteredUser = filteredUser;
	}

        public List<User> getUsersList() { 
            return usersList;
        }
        private void reqList(){
            String query = "SELECT user_id, username, psword, email_name, role_id, active, phone_number, address, "
                    +"  CASE "
                    +"  WHEN marketer = 0 THEN 'Blue Technologies Admin'"
                    +"  WHEN marketer <> 0 THEN (SELECT marketer_name FROM marketers WHERE marketer_id = marketer::integer) "
                    +"  END AS marketer "
                    + "FROM users";
            try {
                    DatabaseBean db = new DatabaseBean();
                    Connection con = db.DBconnect();
                    PreparedStatement stmt = con.prepareStatement(query);
                    ResultSet rs=db.preparedState(stmt);
                    while(rs.next()){
                        usersList.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("psword"), rs.getString("marketer"), 
                                rs.getString("email_name"), rs.getString("role_id"), rs.getBoolean("active"), rs.getString("phone_number"), rs.getString("address")));
                    }
                    rs.close();
                    db.cleanup();                    
            }catch(SQLException e){
                System.out.println("SQLException  caught: "+ e);
            }catch(Exception e){
                System.out.println("Exception caught: "+ e);
            }
        }
        
                        
        public class User  {
                int userid;
                boolean active;
                String username, password, marketer, emailname, roleId, phonenum, address, isActive;

                public User(int userid, String username, String password, String marketer, String emailname, String roleId, Boolean active, String phonenum, String address){
                        this.userid = userid;
                        this.username = username; 
                        this.password = password; 
                        this.marketer = marketer; 
                        this.emailname = emailname; 
                        this.roleId = roleId;
                        this.active = active; 
                        this.phonenum = phonenum; 
                        this.address = address;
                }

                public int getUserId(){ return userid; }
                public String getUsername(){ return username; } 
                public String getPassword(){ return password; }
                public String getMarketer(){ return marketer; }
                public String getEmailname(){ return emailname; }
                public String getRoleId(){ return roleId; }
                public boolean getActive(){ return active; }
                public String getPhonenum(){ return phonenum; }
                public String getAddress(){ return address; }
                public String getIsActive(){ return getActive() ? "Yes":"No"; }

                public void setUserId(int userid){ this.userid = userid; }
                public void setUsername(String username){ this.username = username; } 
                public void setPassword(String password){ this.password = password; }
                public void setMarketer(String marketer){ this.marketer = marketer; }
                public void setEmailname(String emailname){ this.emailname = emailname; }
                public void setRoleId(String roleId){ this.roleId = roleId; }
                public void setActive(boolean active){ this.active = active; }
                public void setPhonenum(String phonenum){ this.phonenum = phonenum; }
                public void setAddress(String address){ this.address = address; }
                public void setIsActive(String isActive){ this.isActive = isActive; }
        }
}
