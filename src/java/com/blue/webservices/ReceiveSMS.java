/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.webservices;

import com.blue.database.DatabaseBean;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author root
 */
@WebServlet(name = "ReceiveSMS", urlPatterns = {"/WEBService/ReceiveSMS"})
public class ReceiveSMS extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String messages = request.getParameter("messages");
        String resp = "";

        PrintWriter out = response.getWriter();
        try {
                DatabaseBean db = new DatabaseBean();
                Connection con = db.DBconnect();
                con.setAutoCommit(false);
                DatabaseMetaData dbmData = con.getMetaData();
                if(dbmData.supportsBatchUpdates()){
                        PreparedStatement stmt1 = con.prepareStatement("INSERT INTO sms (phonenumber, linkid, folderid, message) VALUES (?, ?, ?, ?);");
                        JSONArray array = new JSONArray(messages);
                        System.out.println(array.toString(2));

                        for(int i = 0; i < array.length(); i ++) {
                                String phonum = array.getJSONObject(i).getString("msisdn").trim();
                                String sms = array.getJSONObject(i).getString("message").trim();
                                String linkid = array.getJSONObject(i).getString("linkid").trim();
                                
                                stmt1.setString(1, phonum);
                                stmt1.setString(2, linkid);
                                stmt1.setInt(3, 1);
                                stmt1.setString(4, sms);
                                System.out.println("INSERT INTO sms (phonenumber, linkid, folderid, message) VALUES ('"+ phonum +"', '"+  linkid +"', 3, '"+ sms +"')");
                                
                                if(sms.contains("#")){
                                    String[] smsTokens =  sms.split("#");
                                    String sql = "INSERT INTO requests (phonenumber, linkid, marketer_code, product_code, location_code) VALUES ('"+ phonum+ "', '"+ linkid +"', '"+ smsTokens[0] +"','"+ smsTokens[1] +"','"+ smsTokens[2] +"')";
                                    System.out.println(sql);
                                    db.insert(sql);
                                }else if(sms.contains(" ")){
                                    String[] smsTokens = sms.split(" ");
                                    String sql = "INSERT INTO requests (phonenumber, linkid, marketer_code, product_code, location_code) VALUES ('"+ phonum+ "', '"+ linkid +"', '"+ smsTokens[0] +"','"+ smsTokens[1] +"','"+ smsTokens[2] +"')";
                                    System.out.println(sql);
                                    db.insert(sql);
                                }else if(sms.contains("*")){
                                    String[] smsTokens = sms.split("*");
                                    String sql = "INSERT INTO requests (phonenumber, linkid, marketer_code, product_code, location_code) VALUES ('"+ phonum+ "', '"+ linkid +"', '"+ smsTokens[0] +"','"+ smsTokens[1] +"','"+ smsTokens[2] +"')";
                                    System.out.println(sql);
                                    db.insert(sql);
                                }else if(sms.contains("-")){
                                    String[] smsTokens =  sms.split("-");
                                    String sql = "INSERT INTO requests (phonenumber, linkid, marketer_code, product_code, location_code) VALUES ('"+ phonum+ "', '"+ linkid +"', '"+ smsTokens[0] +"','"+ smsTokens[1] +"','"+ smsTokens[2] +"')";
                                    System.out.println(sql);
                                    db.insert(sql);
                                }

                                stmt1.addBatch();
                        }
                        int []c1 = stmt1.executeBatch();
                        con.commit();
                        resp = "OK|Message Received";
                } else {
                        resp = "FAIL|Server encountered internal error";
                        System.out.println("Your drivers dont support batch updates");
                }       
                db.cleanup();
        }catch(BatchUpdateException e){
                System.out.println("BatchUpdateException: "+ e.getNextException());
                resp = "FAIL|Data format error";
        }catch(SQLException e){
                System.out.println("SQL Error: "+ e);
                resp = "FAIL|Server encountered internal error";
        }catch(Exception e){
                System.out.println("Exception Error: "+ e.getMessage());
                resp = "FAIL|Server encountered internal error";
        }
        out.println(resp);
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
