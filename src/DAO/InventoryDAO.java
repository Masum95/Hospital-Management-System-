/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Model.Inventory;
import Model.SeatInfo;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author User
 */
public class InventoryDAO {

    public InventoryDAO() {
    }
    
    public List<Inventory> getAmbulanceList()
    {
        List<Inventory> ambList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            String SQL = "select * from ambulances";
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                Inventory i = new Inventory().ID(rs.getString("AMBULANCE_ID" )).driverName(rs.getString("DRIVER_NAME" ));
                i = i.buyDate(new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("BUY_DATE" )));
                i = i.driverCntct(rs.getString("DRIVER_CONTACT_NO" ));
                ambList.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ambList;
    }
    public List<Inventory> getEquipmentList()
    {
        List<Inventory> eqpList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            String SQL = "select * from equipments";
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                Inventory i = new Inventory().ID(rs.getString("EQUIPMENT_ID" )).name(rs.getString("EQUIPMENT_NAME" ));
                i = i.buyDate(new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("BUY_DATE" )));
                i = i.cost(rs.getInt("COST" ));
                eqpList.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eqpList;
    }
    public List<SeatInfo> getRoomPriceList()
    {
        List<SeatInfo> slist = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            String SQL = "select * from room_type";
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                SeatInfo s = new SeatInfo().price(rs.getInt("price" )).roomType(rs.getString("room_type" ));
                slist.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slist;
    }
    
    public void addAmbulance(Inventory in){
        String id = null;
        String SQL = "{call add_ambulance ( ? ,?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.setString(1, in.getId());
            cst.setString(2, in.getDriverName());
            cst.setString(3, in.getDriverCntct());
            
            cst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void addEquipment(Inventory in){
        String id = null;
        String SQL = "{call add_equipment ( ? ,?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.setString(1, in.getId());
            cst.setString(2, in.getName());
            cst.setInt(3, in.getCost());
            
            cst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
