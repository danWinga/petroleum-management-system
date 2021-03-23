package com.blue.images;

import com.blue.database.DatabaseBean;
import java.awt.Image;
import javax.servlet.annotation.WebServlet;
import java.sql.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
 
@WebServlet(name = "DisplayImage", urlPatterns = {"/DisplayImage"})
public class DisplayImage extends HttpServlet {
        private static final long serialVersionUID = 4593558495041379082L;

        
        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

                InputStream sImage;
                try {
                        String id = "";
                        
                        if(request.getParameter("id") != null) id = request.getParameter("id");
                        
                        if(id.length() > 0){
                                String query = "SELECT logo FROM marketers WHERE marketer_id ='" + id + "' "; // AND islocation = false ";
                                DatabaseBean db = new DatabaseBean();
                                Connection con = db.DBconnect();
                                PreparedStatement stmt = con.prepareStatement(query);
                                ResultSet rs=db.preparedState(stmt);

                                if (rs.next()) {
                                        byte[] bytearray = new byte[1048576];
                                        int size = 0;
                                        sImage = rs.getBinaryStream(1);
                                        response.reset();
                                        response.setContentType("image/jpeg");
                                        while ((size = sImage.read(bytearray)) != -1) {
                                                response.getOutputStream().
                                                write(bytearray, 0, size);
                                        }
                                        
                                        
                                }
                        }  
                                
                } catch (SQLException sqlex){
                        System.out.println("Image Display SQL Exception: "+ sqlex);
                } catch (Exception e) {
                        System.out.println("Image Display Exception: "+ e);
                }
        }
        

        @Override
        public String getServletInfo() {
                return "Servet to desplay image from database";
        }
        
        
        public Image readImage(Connection conn, int id) throws SQLException, IOException {
                PreparedStatement pstmt = null;
                try {
                        pstmt = conn.prepareStatement("SELECT contents FROM image WHERE id = ?");
                        pstmt.setInt(1, id); 
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                                InputStream is = rs.getBinaryStream(1);
                                return ImageIO.read(is);
                        } else {
                                return null;
                        }
                } finally {
                        if (pstmt != null) {
                                try {
                                        pstmt.close();
                                } catch (SQLException ignored) {
                                }
                        }
                }
        }
}