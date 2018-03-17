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
public class Employee implements DataModel{
    private String userID;
    private String fullName,birthDate,bloodGroup,joinDate,firstName,lastName; //? Should date be date type
    private String bankName,ACno,departmentName;
    private int locationID,deptID,salary;
    Image img;
    
    
    public Employee(){}
    
    public Employee fullName(String fname){
        this.fullName = fname;
        return  this;
    }
    public Employee firstName(String fname){
        this.firstName = fname;
        return  this;
    }
    public Employee lastName(String fname){
        this.lastName = fname;
        return  this;
    }
    
    
    
    public Employee birthDate(String bdate){
        this.birthDate = bdate;
        return  this;
    }
    
    public Employee bloodGroup(String bg){
        this.bloodGroup = bg;
        return  this;
    }
    
    public Employee joinDate(String rdate){
        this.joinDate = rdate;
        return  this;
    }
    
    public Employee employeeID(String userID){
        this.userID = userID;
        return  this;
    }
    
    public Employee locationID(int lid){
        this.locationID = lid;
        return  this;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Image getImg() {
        return img;
    }
    
    public Employee salary(int salary){
        this.salary = salary;
        return  this;
    }
    public Employee bankACNo(String ac){
        this.ACno = ac;
        return  this;
    }
    public Employee bankName(String bank){
        this.bankName = bank;
        return  this;
    }
    public Employee image(Image img){
        this.img = img;
        return  this;
    }
    public Employee departmentName(String name){
        this.departmentName = name;
        return  this;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public String getBankName() {
        return bankName;
    }

    public String getACno() {
        return ACno;
    }

    public int getLocationID() {
        return locationID;
    }

    public int getDeptID() {
        return deptID;
    }

    public int getSalary() {
        return salary;
    }
    public Image getImage() {
        return img;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    
    
    

    
    
}
