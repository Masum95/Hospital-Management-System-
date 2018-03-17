/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;

/**
 *
 * @author User
 */
public class prescriptionContent {

    String patientID, doctorID, dateOfPrescription, dateOfPerformance;
    String admitID ;
    int prescriptionID;
    int bphigh,bplow,pulse,weight,glucose;
    List<Medicine> medList;
    List<DiagnosisTest> testList;
    List<Surgery> surgreytList;
    
    public prescriptionContent() {
    }

    public prescriptionContent patientID(String str) {
        this.patientID = str;
        return this;
    }

    public prescriptionContent doctorID(String str) {
        this.doctorID = str;
        return this;
    }
    public prescriptionContent admissionID(String str) {
        this.admitID = str;
        return this;
    }
    public prescriptionContent prescriptonID(int str) {
        this.prescriptionID = str;
        return this;
    }


    public prescriptionContent dateOfPrescription(String str) {
        this.dateOfPrescription = str;
        return this;
    }

    public prescriptionContent dateOfPerformance(String str) {
        this.dateOfPerformance = str;
        return this;
    }
    public prescriptionContent bpHigh(int str) {
        this.bphigh = str;
        return this;
    }
    public prescriptionContent bpLow(int str) {
        this.bplow = str;
        return this;
    }
    public prescriptionContent weight(int str) {
        this.weight = str;
        return this;
    }
    public prescriptionContent pulse(int str) {
        this.pulse = str;
        return this;
    }
    public prescriptionContent glucose(int str) {
        this.glucose = str;
        return this;
    }
    public prescriptionContent medicineList(List<Medicine> str) {
        this.medList = str;
        return this;
    }
    public prescriptionContent diagnosisList(List<DiagnosisTest> str) {
        this.testList = str;
        return this;
    }
    public prescriptionContent surgeryList(List<Surgery> str) {
        this.surgreytList = str;
        return this;
    }

    
    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getDateOfPrescription() {
        return dateOfPrescription;
    }

    public String getDateOfPerformance() {
        return dateOfPerformance;
    }

    public int getBplow() {
        return bplow;
    }

    public int getPulse() {
        return pulse;
    }

    public int getWeight() {
        return weight;
    }
    public String getAdmitID() {
        return admitID;
    }

    public int getBphigh() {
        return bphigh;
    }

    public int getGlucose() {
        return glucose;
    }

    public int getPrescriptionID() {
        return prescriptionID;
    }

    public List<Medicine> getMedList() {
        return medList;
    }

    public List<DiagnosisTest> getTestList() {
        return testList;
    }

    public List<Surgery> getSurgreytList() {
        return surgreytList;
    }
    
    
    

}
