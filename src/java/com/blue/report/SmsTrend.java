/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.report;

/**
 *
 * @author root
 */
import com.blue.database.DatabaseBean;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean(name="smstrends")
@SessionScoped
public class SmsTrend implements Serializable {
        private CartesianChartModel inboxModel;
        private CartesianChartModel outboxModel;

        public SmsTrend() {
                createInboxModel();
                createOutboxModel();
        }

        public CartesianChartModel getInboxModel() {
                return inboxModel;
        }
        
        public CartesianChartModel getOutboxModel() {
                return outboxModel;
        }

        private void createInboxModel() {
                inboxModel = new CartesianChartModel();
                LineChartSeries series1 = new LineChartSeries();
                series1.setLabel("Inbox SMS Trends");
                ArrayList<Graph> dataList = getData("1");
                for(int x = (dataList.size() - 1); x > -1; x--){
                        series1.set(dataList.get(x).date, dataList.get(x).smsCount);
                }
                inboxModel.addSeries(series1);
        }
        
        private void createOutboxModel() {
                outboxModel = new CartesianChartModel();
                LineChartSeries series1 = new LineChartSeries();
                series1.setLabel("Outbox SMS Trends");
                ArrayList<Graph> dataList = getData("2");
                for(int x = (dataList.size() - 1); x > -1; x--){
                        series1.set(dataList.get(x).date, dataList.get(x).smsCount);
                }
                outboxModel.addSeries(series1);
        }
        
        private ArrayList<Graph> getData(String folderId){
                ArrayList<Graph> dataList = new ArrayList<Graph>();
                try {
                        DatabaseBean db = new DatabaseBean();
                        String query = "SELECT smsdate, count(message) AS sms FROM sms WHERE folderid = "+ folderId +" GROUP BY smsdate ORDER BY smsdate DESC LIMIT 7; ";
                        ResultSet rs = db.query(query);
                        while(rs.next()){
                                dataList.add(new Graph(rs.getString("smsdate"), rs.getInt("sms")));
                        }        
                        db.cleanup();
                } catch(Exception e){
                
                }
                return dataList;
        }
        
        public class Graph {
                String date;
                int smsCount;
                public Graph(String date, int smsCount){
                        this.date = date;
                        this.smsCount = smsCount;
                }
                public String getDate(){ return date; }
                public void setDate(String date){ this.date = date; }
                public int getSmsCount(){ return smsCount; }
                public void setSmsCount(int smsCount){ this.smsCount = smsCount; }
        }
}
