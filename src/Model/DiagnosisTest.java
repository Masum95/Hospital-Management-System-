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
public class DiagnosisTest {
    String name,refdBy,patientID,suggestionDate,dateOfPerformance,dateOfDelivery;
    int id,cost;
    
    public DiagnosisTest() {
    }
    
    public DiagnosisTest testName(String name){
        this.name = name;
        return this;
    }
    public DiagnosisTest refdBy(String name){
        this.refdBy = name;
        return this;
    }
    public DiagnosisTest patientID(String name){
        this.patientID = name;
        return this;
    }
    public DiagnosisTest suggestionDate(Date str){
        if (str == null) {
            this.suggestionDate = "Not yet assigned";
        } else {
            this.suggestionDate = new SimpleDateFormat("dd-MMM-yyyy").format(str);
        }

        return this;
    }
    public DiagnosisTest dateOfPerformance(Date str){
        if (str == null) {
            this.dateOfPerformance = "Not yet assigned";
        } else {
            this.dateOfPerformance = new SimpleDateFormat("dd-MMM-yyyy").format(str);
        }

        return this;
    }
    public DiagnosisTest dateOfDelivery(Date str){
        if (str == null) {
            this.dateOfDelivery = "Not yet assigned";
        } else {
            this.dateOfDelivery = new SimpleDateFormat("dd-MMM-yyyy").format(str);
        }

        return this;
    }
    
    
    public DiagnosisTest testID(int id){
        this.id = id;
        return this;
    }
    
    public DiagnosisTest cost(int cost){
        this.cost = cost;
        return this;
    }
    

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public String getRefdBy() {
        return refdBy;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getSuggestionDate() {
        return suggestionDate;
    }

    public String getDateOfPerformance() {
        return dateOfPerformance;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }
    
     
    
    
}
