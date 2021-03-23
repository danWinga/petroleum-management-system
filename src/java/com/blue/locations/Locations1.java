package com.blue.locations;

import com.blue.products.Products;
import com.blue.database.DatabaseBean;
import com.blue.products.Products.Product;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean(name="locations1")
@SessionScoped
public class Locations1 implements Serializable{
	private final long serialVersionUID = 1L;        
        private ArrayList<Location> locationList;
        private Location selectedLocation;
        
        public Location getSelectedLocation(){ return selectedLocation; }
        public void setSelectedLocation(Location selectedLocation){ this.selectedLocation = selectedLocation; }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        
        
        public ArrayList<Location> getLocationList() {
                if(locationList == null){
                        LoadList();
                }                    
                return locationList;
        }
        
        public void refresh(){
                locationList = null;
        }
        
        public void LoadList() {
                String query = "SELECT location_id, location_code, location_name, details FROM locations ORDER BY location_id";
                locationList = new ArrayList<Location>();

                try{
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        int x = 0;
                        while(rs.next()){
                                locationList.add(new Location(rs.getString("location_id"), rs.getString("location_code"), rs.getString("location_name"), rs.getString("details"), createCategoryModel(rs.getString("location_code"), rs.getString("location_id"))));
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("Error 1: "+ e);
                }catch(Exception e){
                        System.out.println("Error 2: "+ e);
                }
	}
        
        public BarChartModel createCategoryModel(String locationCode, String locationId) {  
                BarChartModel categoryModel = new BarChartModel();
                
                Products products = new Products();
                for(Product prod : products.getProductList()){
                        ChartSeries p = new ChartSeries();
                        p.setLabel(prod.getProductCode());
                        //p.set(locationCode, getRequests(locationCode, prod.getProductCode()));
                        p.set(locationCode, getRequests(locationId, prod.getProductId()));
                        categoryModel.addSeries(p);
                }
                return categoryModel;
        }
        
        private CartesianChartModel combinedModel;
        private void createCombinedModel() {
                combinedModel = new BarChartModel();
 
                BarChartSeries boys = new BarChartSeries();
                boys.setLabel("Boys");
 
                boys.set("2004", 120);
                boys.set("2005", 100);
                boys.set("2006", 44);
                boys.set("2007", 150);
                boys.set("2008", 25);
 
                LineChartSeries girls = new LineChartSeries();
                girls.setLabel("Girls");
 
                girls.set("2004", 52);
                girls.set("2005", 60);
                girls.set("2006", 110);
                girls.set("2007", 135);
                girls.set("2008", 120);
 
                combinedModel.addSeries(boys);
                combinedModel.addSeries(girls);
         
                combinedModel.setTitle("Bar and Line");
                combinedModel.setLegendPosition("ne");
                combinedModel.setMouseoverHighlight(false);
                combinedModel.setShowDatatip(false);
                combinedModel.setShowPointLabels(true);
                Axis yAxis = combinedModel.getAxis(AxisType.Y);
                yAxis.setMin(0);
                yAxis.setMax(200);
        }
        
        private int getRequests(String locationId, String productId){
                int reqst = 0;
                String marketID = httpSession.getAttribute("marketID").toString();
                String query = "SELECT * FROM vw_prices WHERE location_id = "+ locationId +" AND product_id = "+ productId +" ";
                query += " AND marketer_id = "+ marketID +"";
                query += " ORDER BY price_id DESC LIMIT 1";
                try {
                        DatabaseBean db = new DatabaseBean();
                        Connection con = db.DBconnect();
                        PreparedStatement stmt = con.prepareStatement(query);
                        ResultSet rs=db.preparedState(stmt);
                        while(rs.next()){
                                reqst = rs.getInt("price");
                        }
                        rs.close();
                        db.cleanup();
                }catch(SQLException e){
                        System.out.println("SQLException  caught: "+ e);
                }catch(Exception e){
                        System.out.println("Exception caught: "+ e);
                }
                return reqst;
        }
        
        public static class Location {
                String locationId;
                String locationCode;
                String locationName;
                String details;
                BarChartModel categoryModel;
                
		public Location(String locationId, String locationCode, String locationName, String details, BarChartModel categoryModel) {
                        this.locationId = locationId;
			this.locationCode = locationCode;
                        this.locationName = locationName;
                        this.details = details;
                        this.categoryModel = categoryModel;
		}
		
		public Location(String locationId, String locationCode, String locationName, String details) {
                        this.locationId = locationId;
			this.locationCode = locationCode;
                        this.locationName = locationName;
                        this.details = details;
		}
                
                public String getLocationId(){ return locationId; }
                public void setLocationId(String locationId){ this.locationId = locationId; }
                public String getLocationCode(){ return locationCode; }
                public void setLocationCode(String locationCode){ this.locationCode = locationCode; }
                public String getLocationName(){ return locationName; }
                public void setLocationName(String locationName){ this.locationName = locationName; }
                public String getDetails(){ return details; }
                public void setDetails(String details){ this.details = details; }
                public BarChartModel getCategoryModel() { return categoryModel; }
                public void setCategoryModel(BarChartModel categoryModel) { this.categoryModel = categoryModel; }
                
                public void updateLocation(){
                        dbAction("UPDATE locations SET location_code = '"+ locationCode +"', location_name = '"+ locationName +"', details = '"+ details +"' WHERE location_id = "+ Integer.parseInt(locationId.trim()) +";");
                }
                public void deleteLocation(){
                        dbAction("DELETE FROM locations WHERE location_id = "+ Integer.parseInt(locationId.trim()) +";");
                }
                public void dbAction(String mysql){
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
}