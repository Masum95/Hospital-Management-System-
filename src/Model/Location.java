/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import DBConnection.DBUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Location {
    int id;
    String division,district,thana;

    public Location(int id, String division, String district, String thana) {
        this.id = id;
        this.division = division;
        this.district = district;
        this.thana = thana;
    }
    public Location( String division, String district, String thana) {
        this.division = division;
        this.district = district;
        this.thana = thana;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }
    
    
    
    public int getId() {
        return id;
    }

    public String getDivision() {
        return division;
    }

    public String getDistrict() {
        return district;
    }

    public String getThana() {
        return thana;
    }

    
    
    public static  List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            String SQL = "SELECT\n" +
                                "    *\n" +
                                "FROM\n" +
                                "    locations_info";
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                int lid = rs.getInt("LOCATION_ID" );
                String div = rs.getString("division");
                String dis = rs.getString("district");
                String thn = rs.getString("thana");
                locationList.add(new Location(lid, div, dis, thn));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationList;

    }
    
    
}
