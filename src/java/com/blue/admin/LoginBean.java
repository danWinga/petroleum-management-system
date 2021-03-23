package com.blue.admin;

public class LoginBean {
        public String loginname;
        public String stakeholdername;
        public String adminProgram;
        public String error;
        public String password;

        public LoginBean(){

        }

        /**
        * @param loginname
        * @param password
        */
        public LoginBean(String loginname, String password) { this.loginname = loginname; }
        public String getStakeholder(){ return stakeholdername; }

        public String getLoginname(){ return loginname; }
        public final void setLoginname(String loginname){ this.loginname = loginname; }

        public String getPassword(){ return password; }
        public void setPassword(String password){ this.password = password; }

        public String getError(){ return error; }
        public void setError(String error){ this.error = error; }
}