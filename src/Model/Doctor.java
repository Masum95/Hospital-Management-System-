/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.image.Image;

/**
 *
 * @author Masum
 */
public class Doctor implements DataModel{
    String userID;
    String fullName;
    String title,designation,specialisation,qualification;
    Image img ;
    public Doctor(){}
    
    public Doctor fullName(String fname){
        this.fullName = fname;
        return  this;
    }

    public Doctor employeeID(String empId){
        this.userID = empId;
        return  this;
    }
    public Doctor title(String title){
        this.title = title;
        return  this;
    }
    public Doctor designation(String des){
        this.designation = des;
        return  this;
    }
    public Doctor specialisation(String sp){
        this.specialisation = sp;
        return  this;
    }
    public Doctor qualification(String qf){
        this.qualification = qf;
        return  this;
    }
    public Doctor image(Image img){
        this.img = img;
        return  this;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTitle() {
        return title;
    }

    public String getDesignation() {
        return designation;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public String getQualification() {
        return qualification;
    }
    public Image getImage() {
        return img;
    }
    
    @Override
    public String toString() {
        return "Doctor{" + "userID=" + userID + ", fullName=" + fullName + ", title=" + title + ", designation=" + designation + ", specialisation=" + specialisation + ", qualification=" + qualification + '}';
    }
    
    


    
}
