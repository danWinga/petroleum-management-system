package com.blue.navigation;

import com.blue.database.DatabaseBean;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "navigation")
@SessionScoped
public class NavigationBean implements Serializable {
        private final long serialVersionUID = 1L;
        FacesContext facesContext;
        HttpSession httpSession;

        private String pageName = "/templete/board";
        //private String graphName = "/requests/smsdelivery";
        private String graphName = "/reports/products";
        private boolean admin, marketer;
        private String marketerLogo;
        private boolean roleID;

        public NavigationBean() { 
                facesContext = FacesContext.getCurrentInstance();
                httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        }
        
        public String getMarketerLogo(){ return (String)httpSession.getAttribute("marketID"); }
        public void setMarketerLogo(String marketerLogo){ this.marketerLogo = marketerLogo; }
        
        public boolean getRoleID(){ return httpSession.getAttribute("role").toString().equals("2") ? true:false; }
        public void setRoleID(boolean roleID){ this.roleID = roleID; }

        public void doNav() {
                String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("test");
                this.pageName = str;
                String graphName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("graph");
                this.graphName = graphName;
        }

        public String getPageName() { return pageName; }
        public void setPageName(String pageName) { this.pageName = pageName; }
        
        public String getGraphName(){ return graphName; }
        public void setGraphName(String graphName){ this.graphName = graphName; }

        public boolean getAdmin(){ 
                admin = "1".equals(httpSession.getAttribute("role").toString()) ? true : false;
                return admin; 
        }
        
        public boolean getMarketer(){
                marketer = (("2".equals(httpSession.getAttribute("role").toString())) || ("1".equals(httpSession.getAttribute("role").toString()))) ? true : false;
                return marketer; 
        }

        public void getUrl(){
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest(); 
                try {
                        context.getExternalContext().redirect(request.getContextPath() + "/");
                        //context.getExternalContext().redirect(request.getContextPath() + "/technologies/main.xhtml");
                } catch(IOException ex){ }
        }
        
        public void getUrl(String pageName){
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest(); 
                try {
                        context.getExternalContext().redirect(request.getContextPath() + "/"+ pageName);
                } catch(IOException ex){ }
        }
        
        public int getOrderNumber(int statusId){
                int orders = 0;
                String query = "SELECT COUNT(*) AS orders FROM vw_orders WHERE status_id = ? ";
                if(httpSession.getAttribute("marketID").toString().equals("0")){
                        query += "AND user_id = "+ httpSession.getAttribute("myID").toString();
                } else {
                        query += "AND marketer_id = "+ httpSession.getAttribute("marketID").toString();
                }
                query += ";";
                
                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        stmt.setInt(1, statusId);
                        ResultSet rs=db.preparedState(stmt);
                        int x = 0;
                        while(rs.next()){
                                orders = rs.getInt("orders");
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("Error 1: "+ e);
                }catch(Exception e){
                        System.out.println("Error 2: "+ e);
                }
                return orders;
        }
}
