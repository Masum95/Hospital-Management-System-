/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author Masum
 */
public class Surgery {
    String name,dept,suggestiondate,requestedBy,patientID,perfomDate;
    int id,cost;
    int operationID;
    List<String> doctorList;
    public Surgery() {
        
    }
    
    public Surgery surgeryName(String str)
    {
        this.name = str;
        return this;
    }
    public Surgery department(String str)
    {
        this.dept = str;
        return this;
    }
    public Surgery patientID(String str)
    {
        this.patientID = str;
        return this;
    }
    public Surgery requestedBy(String str)
    {
        this.requestedBy  = str;
        return this;
    }
    public Surgery doctorList(List<String> str)
    {
        this.doctorList  = str;
        return this;
    }
    
    public Surgery surgeryID(int id)
    {
        this.id = id;
        return this;
    }
    public Surgery operationID(int id)
    {
        this.operationID = id;
        return this;
    }
    public Surgery surgeryCost(int cost)
    {
        this.cost = cost;
        return this;
    }
    
    public Surgery suggestionDate(Date str)
    {

         if (str == null) {
            this.suggestiondate = "Not yet assigned";
        } else {
            this.suggestiondate = new SimpleDateFormat("dd-MMM-yyyy").format(str);
        }
         return this;
    }
    public Surgery DateOfPerformance(Date str)
    {

         if (str == null) {
            this.perfomDate = "Not yet assigned";
        } else {
            this.perfomDate = new SimpleDateFormat("dd-MMM-yyyy").format(str);
        }
         return this;
    }

    
    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public int getOperationID() {
        return operationID;
    }

    public String getSuggestiondate() {
        return suggestiondate;
    }

    public String getPerfomDate() {
        return perfomDate;
    }

    

    public String getRequestedBy() {
        return requestedBy;
    }

    public String getPatientID() {
        return patientID;
    }

    public List<String> getDoctorList() {
        return doctorList;
    }
    
    
}
