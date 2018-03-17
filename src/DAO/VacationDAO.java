/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Model.Surgery;
import Model.Vacation;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class VacationDAO {

    public VacationDAO() {
    }

    public String applyLeave(Vacation v) {
        String ret = null;
        String SQL = "{?= call insert_in_vacation ( ? ,?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, Types.VARCHAR);
            cst.setString(2, v.getEmpID());
            cst.setString(3, v.getFrmDate());
            cst.setString(4, v.getToDate());
            
            
            cst.executeQuery();
            ret = cst.getString(1);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return ret;
    }

    public List<Vacation> getAllVacations(String docID) {
        String SQL = "select * from vacation where employees_id = ?";
        List<Vacation> adList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, docID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vacation a = new Vacation().empId(rs.getString("Employees_id" )).frmDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("FromDate" )));
                a = a.toDate( new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("todate" ))).status(rs.getString("verified" ));
                adList.add(a);

            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adList;
    }
}
