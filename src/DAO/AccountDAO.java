/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Main.Constants;
import Model.Account;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

/**
 *
 * @author User
 */
public class AccountDAO {

    Boolean isPatient;

    public AccountDAO(boolean isPatient) {
        this.isPatient = isPatient;
    }

    public String userValidation(String userID, String userName, String password) throws SQLException {
        String dept = "";
        if (isPatient) {
            String SQL = prepareQuery("patients_account", "patients_id");
            try (Connection connection = DBUtil.getDataSource().getConnection();
                    PreparedStatement pst = connection.prepareStatement(SQL);) {

                pst.setString(1, userName);
                pst.setString(2, password);
                pst.setString(3, userID);
                System.out.println(pst.toString());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    dept = "Patient";
                }
            }
        } else {
            String SQL = prepareQuery("employees_account", "employees_id");
            try (Connection connection = DBUtil.getDataSource().getConnection();
                    PreparedStatement pst = connection.prepareStatement(SQL);) {

                pst.setString(1, userName);
                pst.setString(2, password);
                pst.setString(3, userID);
                System.out.println(pst.toString());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String sql2 = "SELECT\n"
                            + "    department_name\n"
                            + "FROM\n"
                            + "    employees_info e,\n"
                            + "    departments_info d\n"
                            + "WHERE\n"
                            + "        e.employees_id = ? \n"
                            + "    AND\n"
                            + "        e.department_id = d.department_id";
                    PreparedStatement pst2 = connection.prepareStatement(sql2);
                    pst2.setString(1, userID);
                    ResultSet rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        dept = rs2.getString("DEPARTMENT_NAME");
                    }

                }
            }
        }
        return dept;
    }

    private String prepareQuery(String tableName, String idField) {
        String ret = "SELECT\n"
                + "    *\n"
                + "FROM\n"
                + tableName
                + " WHERE\n"
                + "        username = ?\n"
                + "    AND\n"
                + "        password = ?\n"
                + "    AND\n"
                + idField + " = ?";
        return ret;
    }

    public String createEmployeeAccount(Account ac) {
        String SQL = "{?= call insert_in_employees_account  ( ? ,? ,? ,?, ? )";
        String ret = null;
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, Types.VARCHAR);
            cst.setString(2, ac.getUserID());
            cst.setString(3, ac.getUserName());
            cst.setString(4, ac.getPassword());
            cst.setString(5, ac.getEmail());
            cst.setString(6, ac.getPrevPassword());
            cst.executeQuery();
            ret = cst.getString(1);
            //System.out.println(id);
            BufferedImage bi =  SwingFXUtils.fromFXImage(ac.getImage(), null);
            ImageIO.write(bi, "jpg" , new File(Constants.employeeImgPath + ac.getUserID() + ".jpg"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
