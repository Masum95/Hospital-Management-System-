/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Model.admission;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Masum
 */
public class AdmissionDAO {

    public AdmissionDAO() {
    }

    public String getSerialNo(String doctorID, String date) {
        String SQL = "{?= call find_serial  ( ? ,? )";
        String ret = null;
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, Types.INTEGER);
            cst.setString(2, doctorID);
            cst.setString(3, date);

            cst.executeQuery();
            int tmp = cst.getInt(1);
            System.out.println(tmp);
            if (tmp == -2) {
                ret = "Serial Not Available";
            }
            if (tmp == -1) {
                ret = "Doctor On Leave";
            } else {
                ret = "Serial available: " + Integer.toString(tmp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public String makeAppointment(String doctorID, String date, String patID) {
        String SQL = "{?= call make_an_appointment  ( ? ,? ,? )";
        String ret = "";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, Types.VARCHAR);
            System.out.println(doctorID);
            cst.setString(2, patID);
            cst.setString(3, doctorID);
            cst.setString(4, date);

            cst.executeQuery();
            ret = cst.getString(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public admission getReleasePatientDetails(String adID) {
        admission ad = null;
        String SQL = "begin ? := get_indoor_billing(?); end;";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setString(2, adID);
            
            
            cst.executeQuery();
            ResultSet rs = (ResultSet) cst.getObject(1);
            while (rs.next()) {
                ad = new admission().durationOfStay(rs.getInt("duration_of_stay")).roomCharge(rs.getInt("roomprice"));
                ad = ad.advancePaid(rs.getInt("advance")).surgeryCost(rs.getInt("surgery_cost")).doctorCharge(rs.getInt("doctorcharge"));
                ad = ad.treatmentCharge(rs.getInt("treatmentCharge"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ad;
    }
 
    public void releasePatient(String adID) {
        String SQL = "{call release_patient  ( ? )";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.setString(1, adID);
            cst.executeQuery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
}
