package com.blue.iReport;

import com.blue.database.DatabaseBean;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;


@WebServlet(name = "PDFReports", urlPatterns = {"/PDFReports.pdf"})
public class PDFReports extends HttpServlet {
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String reportName = request.getParameter("reportName");
                String fileName = request.getParameter("fileName");
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename="+ fileName +".pdf");
		ServletOutputStream out = response.getOutputStream();
                System.out.println("Report:"+ reportName +"\treportId:"+ request.getParameter("id"));
		try {
                        
                        DatabaseBean db = new DatabaseBean();
                        Connection cn = db.getConnection();
			JasperReport report = (JasperReport) JRLoader.loadObject(getServletContext().getRealPath("/ireport/"+ reportName +".jasper"));

			Map param = new HashMap();
			param.put("orderId", request.getParameter("id"));
                        //param.put("imageURL", "http://localhost:8080/Blue/DisplayImage?id");
                        String hostAddress = request.getLocalAddr();
                        hostAddress = (request.getLocalAddr().equals("0:0:0:0:0:0:0:1")) ? "localhost":request.getLocalAddr();
                        param.put("imageURL", "http://"+ hostAddress +":"+ request.getLocalPort() + request.getContextPath() +"/DisplayImage?id");
                        //System.out.println("http://"+ request.getLocalAddr() +":"+ request.getLocalPort() + request.getContextPath() +"/DisplayImage?id");
                        //System.out.println(request.getLocalAddr());
                        //System.out.println(request.getServerName());
                        //System.out.println(request.getLocalPort());
                        //System.out.println(request.getServerPort());
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, cn);
                        
                        
                        
                        //JRXlsExporter exporterXLS = new JRXlsExporter();

			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			exporter.exportReport();
                        db.cleanup();
		} catch (Exception e) {
			
                        System.out.println("PDFReports Report File:"+ reportName);
                        //System.out.println("PDFReports Exception on :"+ e);
                        e.printStackTrace();
		}
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
