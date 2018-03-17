/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author Masum
 */
public class admission {

    String admissionId, patientID, doctorID, appointmentDate, patientName, refdBy, releaseDate;
    int serial, advancePaid;
    String roomNo;
    int roomCharge,drCharge,treatCharge,surgeryCost,due , durationOfStay;
    public admission() {
    }

    public admission admissionID(String str) {
        this.admissionId = str;
        return this;
    }

    public admission patientID(String str) {
        this.patientID = str;
        return this;
    }

    public admission refdBy(String str) {
        this.refdBy = str;
        return this;
    }

    public admission releaseDate(Date str) {
        if (str == null) {
            this.releaseDate = "Not yet assigned";
        } else {
            this.releaseDate = new SimpleDateFormat("dd-MMM-yyyy").format(str);
        }

        return this;
    }

    public admission doctorID(String str) {
        this.doctorID = str;
        return this;
    }

    public admission roomNo(String str) {
        this.roomNo = str;
        return this;
    }

    public admission appointmentDate(String str) {

        this.appointmentDate = str;
        return this;
    }

    public admission patientName(String str) {
        this.patientName = str;
        return this;
    }

    public admission serial(int sl) {
        this.serial = sl;
        return this;
    }
    public admission durationOfStay(int sl) {
        this.durationOfStay = sl;
        return this;
    }

    public admission advancePaid(int sl) {
        this.advancePaid = sl;
        return this;
    }
    public admission roomCharge(int sl) {
        this.roomCharge = sl;
        return this;
    }
    public admission doctorCharge(int sl) {
        this.drCharge = sl;
        return this;
    }
    public admission treatmentCharge(int sl) {
        this.treatCharge = sl;
        return this;
    }
    public admission surgeryCost(int sl) {
        this.surgeryCost = sl;
        return this;
    }
    

    public String getAdmissionId() {
        return admissionId;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getSerial() {
        return serial;
    }

    public String getRefdBy() {
        return refdBy;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getAdvancePaid() {
        return advancePaid;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public int getRoomCharge() {
        return roomCharge;
    }

    public int getDrCharge() {
        return drCharge;
    }

    public int getTreatCharge() {
        return treatCharge;
    }

    public int getSurgeryCost() {
        return surgeryCost;
    }

    public int getDue() {
        return Integer.max(roomCharge + drCharge + treatCharge + surgeryCost - advancePaid, 0);
    }

    public int getDurationOfStay() {
        return durationOfStay;
    }
    

}
