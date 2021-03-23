/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.images;

/**
 *
 * @author root
 */
import com.blue.database.DatabaseBean;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
 
@ManagedBean(name="uploadImage")
@SessionScoped
public class UploadImage implements Serializable {
    
        private static final long serialVersionUID = 1L;
        private UploadedFile file;
        private String marketerId;
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        public UploadImage(){

        }
        
        public void setMarketerId(String marketerId){ this.marketerId = marketerId; 
                System.out.println("Marketer: "+ marketerId);
        }
        public String getMarketerId(){ return marketerId; }
 
        public UploadedFile getFile() {
                return file;
        }
 
        public void setFile(UploadedFile file) {
                this.file = file;
        }

        public void upload() {
            
        }
                
        public void locationMap(FileUploadEvent event){
                String sql = "UPDATE marketers SET logo = ? WHERE marketer_id = "+ (String)httpSession.getAttribute("marketerid");
                //System.out.println("Sql:"+ sql);
                handleFileUpload(event, sql);
        }
        
        public void handleFileUpload(FileUploadEvent event, String sql) {  
                try {
                        InputStream fin2 =  event.getFile().getInputstream(); 
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement pre = con.prepareStatement(sql);
                        pre.setBinaryStream(1, fin2, (int) event.getFile().getSize());
                        pre.executeUpdate();
                        //System.out.println("Inserting Successfully!");
                        pre.close();
                        FacesMessage msg = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                }catch (SQLException sqlex){
                        System.out.println("Image Display SQL Exception: "+ sqlex);
                } catch (Exception e) {
                        //        System.out.println("Exception-File Upload." + e.getMessage());
                } 
        }
}