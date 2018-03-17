/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Model.SeatInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Masum
 */
public class SeatDAO {

    public SeatDAO() {
    }

    public List<SeatInfo> getSeatInfo() {
        List<SeatInfo> seatList = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    si.*,\n"
                + "    r.price\n"
                + "FROM\n"
                + "    seat_info si,\n"
                + "    room_type r\n"
                + "WHERE\n"
                + "    si.room_type = r.room_type";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                SeatInfo s = new SeatInfo().buildingNo(rs.getString("building_no")).wardType(rs.getString("ward_type")).roomType(rs.getString("room_type"));
                s = s.floorNo(rs.getInt("floor_no")).price(rs.getInt("price")).roomNo(rs.getString("room_no")).wardNo(rs.getInt("ward_no"));
                seatList.add(s);

            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seatList;
    }

    public List<SeatInfo> getRoomPrice() {
        List<SeatInfo> adlist = new ArrayList<>();
        String SQL = "select * from room_type";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                SeatInfo s = new SeatInfo().roomType(rs.getString("room_type")).price(rs.getInt("price"));
                adlist.add(s);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adlist;
    }

    public void updateRoomCost(SeatInfo m) {
        String SQL = "update room_type set price = ?  where upper(room_type) = upper(?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            pst.setInt(1, m.getPrice());
            pst.setString(2, m.getRoomType());
            pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
