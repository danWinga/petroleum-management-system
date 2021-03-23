/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.webservices;
import com.blue.database.DatabaseBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.quartz.*;
import org.json.*;
/**
 *
 * @author root
 */
public class DeliveryNotes implements Job {
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
            try {
                    JSONArray dbArray = dataFromDB();           
                    if(dbArray != null){
                            updateDB(new JSONArray(SendToServer(dbArray.toString())));
                    }
            } catch (JSONException ex) {
                    System.out.println("JSONException: "+ ex);
            } catch (Exception e){
                    System.out.println("Exception: "+ e);
            }
    }
    
    public void updateDB(JSONArray dbArray)  {
            String  sql = "UPDATE sms SET delivery_status = ? WHERE smsid = ?";
            try{
                    DatabaseBean db = new DatabaseBean();
                    Connection con = db.DBconnect();
                    con.setAutoCommit(false);
                    DatabaseMetaData dbmData = con.getMetaData();
                        
                    if(dbmData.supportsBatchUpdates()){
                            PreparedStatement stmt = con.prepareStatement(sql);

                            for(int i = 0; i < dbArray.length(); i ++) {
                                    //stmt.setString(1, dbArray.getJSONObject(i).getString("delivery_status"));
                                    stmt.setString(1, dbArray.getJSONObject(i).getString("sent"));
                                    //2:pending 3:delivered
                                    stmt.setInt(2, dbArray.getJSONObject(i).getInt("linkid"));
                                    stmt.addBatch();
                            }
                            int []c1 = stmt.executeBatch();
                            con.commit();
                    } else {
                            System.out.println("Your drivers dont support batch updates");
                    }
                    db.cleanup();
            }catch(BatchUpdateException e){
                    System.out.println("BatchUpdateException: "+ e.getNextException());
            }catch (JSONException e){
                    System.out.println("Exception: "+ e);
            }catch(Exception e){
                    System.out.println("Exception: "+ e);
            }
    }
    
    public JSONArray dataFromDB() {
            JSONArray jArray = null;
            String query = "SELECT smsid FROM sms WHERE folderid = 3 AND delivery_status ='2' AND smsdate > now()::date - integer '2'";
            try {
                    jArray = new JSONArray();
                    DatabaseBean db = new DatabaseBean();
                    ResultSet rs = db.query(query);
                    while(rs.next()){  
                            JSONObject linesJ = new JSONObject();
                            linesJ.put("linkid", rs.getInt("smsid"));
                            jArray.put(linesJ);
                    }
                    db.cleanup();
            }catch(SQLException e){
                    System.out.println("1SQLException: "+ e);
            }catch(Exception e){
                    System.out.println("1Exception: "+ e);
            }
            return jArray;
    }
    
    public String SendToServer(String links) throws Exception {
            String  data = URLEncoder.encode("links", "UTF-8") + "=" + URLEncoder.encode(links, "UTF-8");

            URL url = new URL("http://197.248.2.228/bluetech/DeliveryStatus.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line = null;
            String response = "";
            while ((line = rd.readLine()) != null) {
                    response += line;
            }
            System.out.println(response);
            return response;
    }
}
