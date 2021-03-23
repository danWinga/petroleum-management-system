package com.blue.navigation;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "reportnavigation")
@SessionScoped
public class ReportNavigation implements Serializable {
        private final long serialVersionUID = 1L;
        FacesContext facesContext;
        HttpSession httpSession;
        private Date startDate, endDate;

        private String graphName = "/reports/0";

        public ReportNavigation() { 
                facesContext = FacesContext.getCurrentInstance();
                httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
        }
        

        public void doNav() {
                String repName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("graph");
                this.graphName = repName;
        }
        
        public String getGraphName(){ return graphName; }
        public void setGraphName(String graphName){ this.graphName = graphName; }
        
        public Date getStartDate(){ return startDate; }
        public void setStartDate(Date startDate){ this.startDate = startDate; }
        
        public Date getEndDate(){ return endDate; }
        public void setEndDate(Date endDate){ this.endDate = endDate; }

        public void getUrl(){
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest(); 
                try {
                        context.getExternalContext().redirect(request.getContextPath() + "/");
                } catch(IOException ex){ }
        }
        public void setReportName(){
                System.out.println(graphName +"\t"+ startDate +"\t"+ endDate);
        }
}
