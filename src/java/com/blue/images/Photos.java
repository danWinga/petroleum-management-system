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

@ManagedBean(name="photos")
@SessionScoped
public class Photos implements Serializable {
	private static final long serialVersionUID = 1L;        
        private ArrayList<Photo> photoList;
        private Photo selectedPhoto;
        private String hostelId;
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        public Photos(){

        }
        
        public void printing(){
                photoList = null;
        }
        
        public void deletePhoto(){
                System.out.println("Selected photo: ");//+ selectedPhoto.picId);
        }
        
        public String getHostelId(){ return hostelId; }
        public void setHostelId(String hostelId){
                photoList = null;
                this.hostelId = hostelId; 
        }
        
        public Photo getSelectedPhoto(){ return selectedPhoto; }
        public void setSelectedPhoto(Photo selectedPhoto){ this.selectedPhoto = selectedPhoto; }
        
	public ArrayList<Photo> getPhotoList() {
                if(photoList == null){
                        getList();
                }
                return photoList;
        }
        public void setPhotoList(ArrayList<Photo> photoList){ this.photoList = photoList; }
        
        public void getList() {
                String query = "SELECT pic_id, hostel_id, file_name, profile_pic, islocation, details FROM pics WHERE (1=1) AND hostel_id ="+ hostelId;

                photoList = new ArrayList<Photo>();

                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                                photoList.add(new Photo(rs.getString("pic_id"), rs.getString("hostel_id"), rs.getString("file_name"), rs.getBoolean("profile_pic"), rs.getBoolean("islocation"), rs.getString("details")));
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("SQL Exception while getting Hostels: "+ e);
                }catch(Exception e){
                        System.out.println("Exception while getting Hostels: "+ e);
                }
	}
                
        public class Photo implements Serializable { 
                String picId;
                String hostelId;
                String fileName; 
                boolean profilePic;
                boolean locate;
                String details;
                String description;

                public Photo(String picId, String hostelId, String fileName, boolean profilePic, boolean locate, String details){
                        this.picId = picId;
                        this.hostelId = hostelId;
                        this.fileName = fileName;
                        this.profilePic = profilePic;
                        this.locate = locate;
                        this.details = details;
                }
                
                public String getPicId(){ return picId; }
                public void setPicId(String picId){ this.picId = picId; }
                
                public String getHostelId(){ return hostelId; }
                public void SetHostelId(String hostelId){ this.hostelId = hostelId; }
                
                public String getFileName(){ return fileName; }
                public void setFileName(String fileName){ this.fileName = fileName; }
                
                public boolean getProfilePic(){ return profilePic; }
                public void setProfilePic(boolean profilePic){ this.profilePic = profilePic; }
                
                public boolean getLocate(){ return locate; }
                public void setLocate(boolean locate){ this.locate = locate; }
                
                public String getDetails(){ return details; }
                public void setDetails(String details){ this.details = details; }
                
                public String getDescription(){
                        if(profilePic){
                                return "Profile";
                        } else if(locate) {
                                return "Map";
                        } else {
                                return "";
                        }
                }
                public void setDescription(String description){ this.description = description; }
        }
}
