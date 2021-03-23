package com.blue.webservices;

import com.blue.database.DatabaseBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.*;

public class SendSMS implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
            try {
                    SendSMSApp();
            } catch (SQLException ex) {
                    Logger.getLogger(SendSMS.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void SendSMSApp() throws SQLException {
            Statement stmt = null;
            try {
                    DatabaseBean db = new DatabaseBean();
                    Connection con = db.DBconnect();
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    //ResultSet rs = stmt.executeQuery("SELECT folderid, smsid, phonenumber, message, linkid, delivery_status FROM sms WHERE folderid = 2");
                    ResultSet rs = stmt.executeQuery("SELECT * FROM sms WHERE folderid = 2");

                    while (rs.next()) {
                            if(SendSMSServer(rs.getString("phonenumber"), rs.getString("smsid"), rs.getString("message"), rs.getString("linkid")).toUpperCase().startsWith("OK|")){
                                    rs.updateInt("folderid", 3);
                                    rs.updateString("delivery_status", "2");
                                    rs.updateRow();
                            }
                    }
                    db.cleanup();
            } catch (Exception e ) {
                    System.out.println(e);
            } finally {
                    if (stmt != null) { 
                            stmt.close(); 
                    }
            }
    }
    
    public String SendSMSServer(String phoneNum, String smsid, String txtMessage, String linkid) throws Exception {

            String  data = URLEncoder.encode("linkid", "UTF-8") + "=" + URLEncoder.encode(linkid, "UTF-8");
                    data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(phoneNum.trim(), "UTF-8");
                    data += "&" + URLEncoder.encode("oa", "UTF-8") + "=" + URLEncoder.encode(phoneNum.trim(), "UTF-8");
                    data += "&" + URLEncoder.encode("fa", "UTF-8") + "=" + URLEncoder.encode(phoneNum.trim(), "UTF-8");
                    data += "&" + URLEncoder.encode("service_id", "UTF-8") + "=" + URLEncoder.encode("6014382000048607", "UTF-8");
                    data += "&" + URLEncoder.encode("sender_name", "UTF-8") + "=" + URLEncoder.encode("22196", "UTF-8");
                    data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(txtMessage.trim(), "UTF-8");
                    data += "&" + URLEncoder.encode("sent", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8");
                    data += "&" + URLEncoder.encode("idblue", "UTF-8") + "=" + URLEncoder.encode(smsid.trim(), "UTF-8");
                    data += "&" + URLEncoder.encode("linkidblue", "UTF-8") + "=" + URLEncoder.encode(smsid.trim(), "UTF-8");
                    
            System.out.println("URL Post: "+ data);

            URL url = new URL("http://197.248.2.228/bluetech/ReceiveSMS.php");
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
