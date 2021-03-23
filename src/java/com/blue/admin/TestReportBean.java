/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Benjamin
 */
@ManagedBean(name = "reportPDF")
@ViewScoped
public class TestReportBean {

    @PostConstruct
    public void init() {
        
    }
List<Person> datasource = new ArrayList<Person>();

    public List<Person> getDatasource() {
        return datasource;
    }

    public void setDatasource(List<Person> datasource) {
        this.datasource = datasource;
    }

    public void printPDF() throws JRException, IOException {
        System.out.println("****on report PDF**");
        List<Person> datasource = new ArrayList<Person>();
        datasource.add(new Person("Daniel","Winga"));
        datasource.add(new Person("James","Kamau"));
        datasource.add(new Person("Tony","Morishu"));
        String filename = "names.pdf";
        String jasperPath = "/ireport/report.jasper";
        this.PDF(null, jasperPath, datasource, filename);
    }

    public void PDF(Map<String, Object> params, String jasperPath, List<?> dataSource, String fileName) throws JRException, IOException {
        System.out.println("****in report function**");
        String relativeWebPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(jasperPath);
        File file = new File(relativeWebPath);
        JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dataSource, false);
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params, source);
         HttpServletResponse response=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
         response.addHeader("Content-disposition", "attachment;filename"+ fileName);
         ServletOutputStream stream=response.getOutputStream();
         JasperExportManager.exportReportToPdfStream(print, stream);
          FacesContext.getCurrentInstance().responseComplete();
    }

}
