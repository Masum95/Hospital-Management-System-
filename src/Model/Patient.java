/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.image.Image;

/**
 *
 * @author User
 */
public class Patient implements DataModel {

    private String userID;
    private String firstName, lastName, birthDate, bloodGroup, regDate;
    private int locationID,age;
    Image img;

    public Patient() {
    }

    public Patient firstName(String fname) {
        this.firstName = fname;
        return this;
    }

    public Patient lastName(String lname) {
        this.lastName = lname;
        return this;
    }

    public Patient birthDate(String bdate) {
        this.birthDate = bdate;
        return this;
    }

    public Patient bloodGroup(String bg) {
        this.bloodGroup = bg;
        return this;
    }
    public Patient age(int age) {
        this.age = age;
        return this;
    }

    public Patient registrationDate(String rdate) {
        this.regDate = rdate;
        return this;
    }

    public Patient patientID(String userID) {
        this.userID = userID;
        return this;
    }

    public Patient locationID(int lid) {
        this.locationID = lid;
        return this;
    }

    public Patient image(Image img) {
        this.img = img;
        return this;
    }

    public String getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public int getAge() {
        return age;
    }

    public Image getImg() {
        return img;
    }
    

    public String getRegDate() {
        return regDate;
    }

    public int getLocationID() {
        return locationID;
    }

    public Image getImage() {
        return img;
    }

}
