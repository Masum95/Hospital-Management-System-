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
public class Inventory {

    String id,driverName,driverCntct ,buyDate , name;
    int cost;
    public Inventory ID(String str)
    {
        this.id = str;
        return  this;
    }
    public Inventory driverName(String str)
    {
        this.driverName = str;
        return  this;
    }
    public Inventory driverCntct(String str)
    {
        this.driverCntct = str;
        return  this;
    }
    public Inventory buyDate(String str)
    {
        this.buyDate = str;
        return  this;
    }
    public Inventory name(String str)
    {
        this.name = str;
        return  this;
    }
    public Inventory cost(int str)
    {
        this.cost = str;
        return  this;
    }

    public String getId() {
        return id;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverCntct() {
        return driverCntct;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }
    
    
}
