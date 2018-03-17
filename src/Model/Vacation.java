/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author User
 */
public class Vacation {

    private String empID, frmDate, toDate,status;

    public Vacation() {
    }

    public Vacation empId(String userid) {
        this.empID = userid;
        return this;
    }
    public Vacation status(String str) {
        if(str.equalsIgnoreCase("Yes" )){
            this.status = "Accepted" ;
        }else{
            this.status = "Pending";
        }
        
        return this;
    }

    public Vacation frmDate(String str) {
        if (str == null) {
            this.frmDate = "Not yet assigned";
        } else {
            this.frmDate = str;
        }

        return this;
    }

    public Vacation toDate(String str) {
        if (str == null) {
            this.toDate = "Not yet assigned";
        } else {
            this.toDate = str;
        }

        return this;
    }

    public String getEmpID() {
        return empID;
    }

    public String getFrmDate() {
        return frmDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getStatus() {
        return status;
    }
    
}
