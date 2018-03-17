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
public class Medicine {

    String brandName, contains, drug_for, dosage_form, manufacturer, drug_class;
    String dosageInstruction, comments;
    int id , upto;

    public Medicine() {
    }

    public Medicine brandName(String name) {
        this.brandName = name;
        return this;
    }
    
    public Medicine contians(String str) {
        this.contains = str;
        return this;
    }
    
    public Medicine drugFor(String str) {
        this.drug_for = str;
        return this;
    }
    
    public Medicine dosageForm(String str) {
        this.dosage_form = str;
        return this;
    }
        
    public Medicine drugClass(String str) {
        this.drug_class = str;
        return this;
    }
    
    public Medicine dosageInstruction(String str) {
        this.dosageInstruction = str;
        return this;
    }
    
    public Medicine comments(String str) {
        this.comments = str;
        return this;
    }
    
    public Medicine upto(int upto) {
        this.upto = upto;
        return this;
    }
    
    public Medicine MedicineID(int id) {
        this.id = id;
        return this;
    }
    

    public String getBrandName() {
        return brandName;
    }

    public String getContains() {
        return contains;
    }

    public String getDrug_for() {
        return drug_for;
    }

    public String getDosage_form() {
        return dosage_form;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getDrug_class() {
        return drug_class;
    }

    public String getDosageInstruction() {
        return dosageInstruction;
    }

    public String getComments() {
        return comments;
    }

    public int getUpto() {
        return upto;
    }

    public int getId() {
        return id;
    }

}
