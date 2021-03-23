package com.blue.iReport;

import com.blue.database.DatabaseBean;
import java.awt.Container;
import java.sql.Connection;
import java.util.HashMap;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public class Report { 

	HashMap hm = null;
	Connection con = null;
	String reportName;

	public Report() {

	}

	public Report(HashMap map) {
		this.hm = map;
	}

	public Report(HashMap map, Connection con) {
		this.hm = map;
		this.con = con;
	}

	public void setReportName(String rptName) {
		this.reportName = rptName;
	}

	public void callReport(Container c) {
		JasperPrint jasperPrint = generateReport();
		JRViewer viewer = new JRViewer(jasperPrint);
		c.add(viewer);
	}
        
	public void callConnectionLessReport(Container c) {
		JasperPrint jasperPrint = generateEmptyDataSourceReport();
		JRViewer viewer = new JRViewer(jasperPrint);
		c.add(viewer);
	}

	public void closeReport() {

	}
        
	/** this method will call the report from data source*/
	public JasperPrint generateReport() {
		try {
			if (con == null) {
				try {
					DatabaseBean db = new DatabaseBean();
					con = db.getConnection();
				} catch (Exception ex) {
                                        System.out.println("Exception:"+ ex);
				}
			}
			JasperPrint jasperPrint = null;
			if (hm == null) {
				hm = new HashMap();
			}
			try {
                                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/ireport/"+ reportName +".jasper");
				jasperPrint = JasperFillManager.fillReport(reportPath, hm, con);
                                //System.out.println("Report Path:"+ reportPath);
			} catch (JRException e) {
                                System.out.println("JRException:"+ e);
			}
			return jasperPrint;
		} catch (Exception ex) {
                        System.out.println("Exception:"+ ex);
			return null;
		}
	}

	/** call this method when your report has an empty data source*/
	public JasperPrint generateEmptyDataSourceReport() {
		try {
			JasperPrint jasperPrint = null;
			if (hm == null) {
				hm = new HashMap();
			}
			try {
				jasperPrint = JasperFillManager.fillReport("ireport/"+ reportName + ".jasper", hm, new JREmptyDataSource());
			} catch (JRException e) {
                                System.out.println("JRException:"+ e);
			}
			return jasperPrint;
		} catch (Exception ex) {
                        System.out.println("Exception:"+ ex);
			return null;
		}
	}
}