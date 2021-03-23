/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blue.navigation;

import java.io.Serializable;
import java.util.ArrayList;  
import java.util.List;  
import javax.annotation.PostConstruct;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
  
@ManagedBean(name="GalleriaBean")
@ViewScoped
public class GalleriaBean implements Serializable {  
        private static List<String> images;
  
        @PostConstruct  
        public void init() {  
                FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                images = new ArrayList<String>();  
                for(int i=1;i<=12;i++) { 
                        images.add(i + ".jpg");  
                }
        }  
  
        public List<String> getImages() {  
                return images;  
        }  
}  