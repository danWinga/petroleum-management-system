package com.blue.admin;

import java.sql.ResultSet;
import com.blue.database.DatabaseBean;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "simplelogin")
public class SimpleLogin implements Serializable {
        private String username;
	private String password;
        public boolean display;
        
        public String SESSION_KEY_NAME = "mySessionKeyName";
        
        FacesContext facesContext;// = FacesContext.getCurrentInstance();
        HttpSession httpSession;// = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        FacesContext context; // = FacesContext.getCurrentInstance();
        HttpServletRequest request; // = (HttpServletRequest) context.getExternalContext().getRequest(); 
        
        public SimpleLogin(){
                facesContext = FacesContext.getCurrentInstance();
                httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
                context = FacesContext.getCurrentInstance();
                request = (HttpServletRequest) context.getExternalContext().getRequest(); 
        }
        
        public String getSessionKeyName() { return SESSION_KEY_NAME; }
        public void setDisplay(boolean display){ this.display = display;  }
        
        public boolean getDisplay(){ 
                if(httpSession.getAttribute("display") != null){
                        this.display = "yes".equals(httpSession.getAttribute("display").toString()) ? true : false;
                } else {
                        this.display = false;
                }
                return display; 
        }
        
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public void login() {  
                //RequestContext context = RequestContext.getCurrentInstance();  
                FacesMessage msg = null; 
                String result = null;
                String query = "SELECT user_id, username, psword, marketer, role_id, email_name, phone_number, address, details FROM users "
                        + "WHERE username = '"+ username.trim() +"' AND psword = '"+ password.trim() +"' LIMIT 1;";

                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);//, ResultSet.TYPE_SCROLL_INSENSITIVE);
                        ResultSet rs=db.preparedState(stmt);

                        while(rs.next()){
                                result = rs.getString("username");
                                httpSession.setAttribute("username", username);
                                httpSession.setAttribute("role", rs.getString("role_id"));
                                httpSession.setAttribute("marketID", rs.getString("marketer"));
                                httpSession.setAttribute("myID", rs.getString("user_id"));
                        }
                        stmt.close();
                        db.insert("INSERT INTO qrcode (qr_name, details) VALUES ('"+ username +"','"+ username +"') ;");
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("SQLException on Login:"+ e.getMessage());
                }catch(Exception e){
                        System.out.println("Exception on Login:"+ e.getMessage());
                }

                boolean loggedIn = false;  

                //if(username != null  && username.equals("admin") && password != null  && password.equals("admin")) {  
                if(result != null){
                        loggedIn = true;  
                        display = true;
                        httpSession.setAttribute("display", "yes");
                        try {
                                //context.getExternalContext().redirect(request.getContextPath() + "/technologies/main.xhtml");
                                context.getExternalContext().redirect(request.getContextPath() + "/");
                        } catch(IOException ex){ }
                } else {  
                        loggedIn = false;  
                        display = false;                
                        httpSession.setAttribute("display", "no");
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials"));  
                }
        }
        public void logout() {
            httpSession.setAttribute("display", "no");
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }
            try {
                //context.getExternalContext().redirect(request.getContextPath() + "/technologies/main.xhtml");
                context.getExternalContext().redirect(request.getContextPath() + "/");
            } catch(IOException ex){ }
        }
}