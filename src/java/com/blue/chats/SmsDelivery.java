/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.chats;

/**
 *
 * @author root
 */
import com.blue.database.DatabaseBean;
import com.blue.products.Products;
import com.blue.products.Products.Product;
import java.io.Serializable;  
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;  
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;


@ManagedBean(name="smsdelivery")
//@SessionScoped
@RequestScoped
public class SmsDelivery implements Serializable {  
    
        private PieChartModel pieModel;  
        
        public SmsDelivery() {  
                createPieModel();  
        }
          
        public PieChartModel getPieModel() {  
                return pieModel;  
        }
  
        private void createPieModel() {  
                pieModel = new PieChartModel(); 
                String query = "SELECT (SELECT count(*) FROM sms WHERE folderid = 3 AND delivery_status = '3') AS delivered, (SELECT count(*) FROM sms WHERE folderid = 3 AND delivery_status <> '3') AS pending;";
                try {
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                                pieModel.set("Delivered", rs.getInt("delivered"));
                                pieModel.set("Pending", rs.getInt("pending"));
                        }
                        rs.close();
                        db.cleanup();                    
                }catch(SQLException e){
                        System.out.println("SQLException  caught: "+ e);
                }catch(Exception e){
                        System.out.println("Exception caught: "+ e);
                }
                
                //pieModel.set("Brand 1", 540);  
                //pieModel.set("Brand 2", 325);  
                //pieModel.set("Brand 3", 702);  
                //pieModel.set("Brand 4", 421);  
        }
}  