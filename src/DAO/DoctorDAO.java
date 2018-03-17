/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Model.Doctor;
import Model.Surgery;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Masum
 */
public class DoctorDAO {

    public DoctorDAO() {
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> docList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            String SQL = "SELECT\n"
                    + "    d.employees_id,\n"
                    + "    d.specialization,\n"
                    + "    first_name,\n"
                    + "    last_name\n"
                    + "FROM\n"
                    + "    doctors_info d,\n"
                    + "    employees_info e\n"
                    + "WHERE\n"
                    + "    d.employees_id = e.employees_id";
            //System.out.println(SQL);
            ResultSet rs = st.executeQuery(SQL);
            //System.out.println(rs);
            while (rs.next()) {
                String empId = rs.getString("employees_id");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                lName = rs.wasNull() ? "" : lName;
                String specialisation = rs.getString("Specialization");
                Doctor d = new Doctor().employeeID(empId).specialisation(specialisation);
                d = d.fullName(fName + " " + lName);
                docList.add(d);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docList;

    }

    public List<String> getAllDepartments() {
        List<String> deptList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            String SQL = "select distinct(specialization) from doctors_info";
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                String dep = rs.getString("specialization");
                System.out.println("---" + dep);
                deptList.add(dep);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deptList;

    }

    public Doctor getDoctorDetails(String userID) {
        System.out.println("here");
        Doctor d = null;
        String SQL = "SELECT\n"
                + "    d.*,\n"
                + "    first_name,\n"
                + "    last_name,\n"
                + "    image_link\n"
                + "FROM\n"
                + "    doctors_info d,\n"
                + "    employees_info einfo,\n"
                + "    employees_account eaccount\n"
                + "WHERE\n"
                + "        d.employees_id = ? AND\n"
                + "        d.employees_id = einfo.employees_id AND\n"
                + "        d.employees_id = eaccount.employees_id";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, userID);
            System.out.println(userID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String empId = rs.getString("employees_id");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String title = rs.getString("title");

                String specialisation = rs.getString("Specialization");
                String designation = rs.getString("designation");
                String qualification = rs.getString("qualification");
                String imgPath = new File(rs.getString("image_link")).getAbsolutePath();
                Image img = new Image(new FileInputStream(new File(imgPath)));
                d = new Doctor().employeeID(empId).specialisation(specialisation).qualification(qualification).designation(designation).title(title);
                d = d.fullName(fName + " " + lName).image(img);
                System.out.println(d.getFullName());
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;

    }

    public List<Doctor> getAllDoctorForAdmin() {
        List<Doctor> docList = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    d.*,\n"
                + "    first_name,\n"
                + "    last_name\n"
                + "FROM\n"
                + "    DOCTORS_INFO d,\n"
                + "    employees_info e\n"
                + "WHERE\n"
                + "    D.EMPLOYEES_ID = E.EMPLOYEES_ID";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {
            System.out.println(SQL);
            ResultSet rs = st.executeQuery(SQL);
            System.out.println(rs.toString());
            while (rs.next()) {
                Doctor d;
                String empId = rs.getString("employees_id");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String title = rs.getString("title");

                String specialisation = rs.getString("Specialization");
                String designation = rs.getString("designation");
                String qualification = rs.getString("qualification");
                d = new Doctor().employeeID(empId).specialisation(specialisation).qualification(qualification).designation(designation).title(title);
                d = d.fullName(fName + " " + lName);
                //System.out.println(d);
                docList.add(d);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docList;

    }

    public List<Surgery> getAllOperationFor(String docID) {
        String SQL = "SELECT\n"
                + "    ia.patients_id,o.operation_date, o.operation_id\n"
                + "FROM\n"
                + "    operation_assignment oa,\n"
                + "    operation o,\n"
                + "    in_admission ia\n"
                + "WHERE\n"
                + "        oa.operation_id = o.operation_id\n"
                + "    AND\n"
                + "        ia.admission_id = o.admission_id\n"
                + "    AND\n"
                + "        employee_id = ?\n"
                + "    AND (\n"
                + "            operation_date IS NOT NULL\n"
                + "        AND\n"
                + "            operation_date >= SYSDATE\n"
                + "    )";
        List<Surgery> adList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, docID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Surgery a = new Surgery().operationID(rs.getInt("operation_id")).DateOfPerformance(rs.getDate("operation_date"));
                a = a.patientID(rs.getString("patients_id"));
                adList.add(a);

            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adList;
    }

}
