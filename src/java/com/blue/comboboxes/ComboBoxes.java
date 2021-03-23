package com.blue.comboboxes;

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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
 
@ManagedBean(name="comboboxes")
@SessionScoped
public class ComboBoxes implements Serializable {
        public SelectItem[] marketers;
        public SelectItem[] locations;
        public SelectItem[] products;
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        public SelectItem[] getMarketers() {
                String query = "SELECT marketer_id, (marketer_code ||' : '|| marketer_name) AS marketer FROM marketers ";
                if(!"0".equals(httpSession.getAttribute("marketID").toString())){
                        query += " WHERE marketer_id = '"+ httpSession.getAttribute("marketID").toString() +"'";
                }
                query += ";";

                List list = new ArrayList();            
                TreeMap <String, String>tMap = new TreeMap<String, String>();
                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        int z = 0;
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

        public SelectItem[] getLocations() {
                String query = "SELECT location_id, (location_code ||' : '|| location_name) AS location FROM locations";
                List list = new ArrayList();
                TreeMap <String, String>tMap = new TreeMap<String, String>();
                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        int z = 0;
                        while(rs.next()){
                                list.add(z++);
                                tMap.put(rs.getString("location_id").toString(), rs.getString("location").toString());
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
                        tMap.put("--".toString(), "<<No Location>>".toString());
                        list.add("---".toString());
                }
                locations = new SelectItem[list.size()];
                int i = 0;
                Set set = tMap.entrySet();
                Iterator ite = set.iterator();
                while(ite.hasNext()) { 
                        Map.Entry me = (Map.Entry)ite.next(); 
                        locations[i++] = new SelectItem(me.getKey().toString(), me.getValue().toString());
                } 
                return locations;
        }
        
        public SelectItem[] getProducts() {
                String query = "SELECT product_id, (product_code ||':'|| product_name) AS product FROM products;";
                List list = new ArrayList();
                TreeMap <String, String>tMap = new TreeMap<String, String>();
                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        int z = 0;
                        while(rs.next()){
                                list.add(z++);
                                tMap.put(rs.getString("product_id").toString(), rs.getString("product").toString());
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("Error 1: "+ e);
                }catch(Exception e){
                        System.out.println("Error 2: "+ e);
                }
                if (list.isEmpty()) {
                        tMap.put("--".toString(), "<<No Products>>".toString());
                        list.add("---".toString());
                }
                products = new SelectItem[list.size()];
                int i = 0;
                Set set = tMap.entrySet();
                Iterator ite = set.iterator();
                while(ite.hasNext()) { 
                        Map.Entry me = (Map.Entry)ite.next(); 
                        products[i++] = new SelectItem(me.getKey().toString(), me.getValue().toString());
                } 
                return products;
        }
}
