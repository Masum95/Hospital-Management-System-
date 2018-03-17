/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Masum
 */
public class SeatInfo {
    String roomNo,buildingNo,roomType,wardType;
    int floorNo,wardNo , price;

    public SeatInfo() {
    }
    
    public SeatInfo roomNo(String str){
        this.roomNo = str;
        return  this;
    }
    public SeatInfo buildingNo(String str){
       this.buildingNo = str;
        return  this;
    }
    public SeatInfo roomType(String str){
       this.roomType = str;
        return  this;
    }
    public SeatInfo wardType(String str){
       this.wardType = str;
        return  this;
    }
    public SeatInfo floorNo(int str){
       this.floorNo = str;
        return  this;
    }
    public SeatInfo wardNo(int str){
        this.wardNo = str;
        return  this;
    }
    public SeatInfo price(int str){
        this.price = str;
        return  this;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public int getWardNo() {
        return wardNo;
    }

    public int getPrice() {
        return price;
    }

    public String getWardType() {
        return wardType;
    }
    
}
