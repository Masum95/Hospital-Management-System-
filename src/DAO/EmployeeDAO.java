/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Model.Account;
import Model.Doctor;
import Model.Employee;
import Model.Location;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author User
 */
public class EmployeeDAO {

    public EmployeeDAO() {
    }

    public Employee getEmployee(String userId) {
        String SQL = "SELECT\n"
                + "    einfo.*,\n"
                + "    eacnt.image_link\n"
                + "FROM\n"
                + "    employees_info einfo,\n"
                + "    employees_account eacnt\n"
                + "WHERE\n"
                + "        einfo.employees_id = ?  AND\n"
                + "        einfo.employees_id = eacnt.employees_id";
        Employee e = null;
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String empId = rs.getString("employees_id");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String bdate = new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("birth_date"));

                String bg = rs.getString("BLOOD_GROUP");
                String bank = rs.getString("BANK_NAME");
                String bnkAC = rs.getString("BANK_ACCOUNT_NO");
                String imgPath = new File(rs.getString("image_link")).getAbsolutePath();
                Image img = new Image(new FileInputStream(new File(imgPath)));
                System.out.println(img);
                e = new Employee().fullName(fName + " " + lName).birthDate(bdate).bloodGroup(bg).employeeID(userId).bankName(bank).bankACNo(bnkAC).image(img);
                e = e.salary(rs.getInt("salary" )).joinDate(new SimpleDateFormat("dd-MMM-yy" ).format(rs.getDate("hire_date" )));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> empList = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    employees_id,\n"
                + "    first_name || ' ' || last_name fullname,\n"
                + "    hire_date,\n"
                + "    salary,\n"
                + "    department_name\n"
                + "FROM\n"
                + "    employees_info\n"
                + "    JOIN departments_info USING ( department_id )\n"
                + "WHERE\n"
                + "    department_id != (\n"
                + "        SELECT\n"
                + "            department_id\n"
                + "        FROM\n"
                + "            departments_info\n"
                + "        WHERE\n"
                + "            department_name LIKE '%Doctor%'\n"
                + "    )";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                Statement st = connection.createStatement();) {

            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                Doctor d;
                String empId = rs.getString("employees_id");
                String fName = rs.getString("fullName");
                String hd = new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("hire_date"));
                int salary = rs.getInt("salary");
                String deptName = rs.getString("department_name");

                Employee e = new Employee().employeeID(empId).fullName(fName).joinDate(hd).salary(salary).departmentName(deptName);
                empList.add(e);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empList;

    }

    public String addEmployee(Employee p, Doctor d, Location l,Account ac,String ind) {
        String id = null;
        String SQL = "{?= call insert_in_employees_info ( ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ? , ?,? , ?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, Types.VARCHAR);
            cst.setString(2, p.getFirstName());
            cst.setString(3, p.getLastName());
            cst.setString(4, p.getBirthDate());
            cst.setString(5, p.getBloodGroup());
            cst.setString(6, l.getDivision());
            cst.setString(7, l.getDistrict());
            cst.setString(8, l.getThana());
            cst.setString(9, p.getDepartmentName());
            cst.setInt(10, p.getSalary());
            cst.setString(11, p.getBankName());
            cst.setString(12, p.getACno());
            cst.setString(13, ind);
            if (d != null) {
                cst.setString(14, d.getTitle());
                cst.setString(15, d.getDesignation());
                cst.setString(16, d.getSpecialisation());
                cst.setString(17, d.getQualification());
            }
            else{
                cst.setString(14, "");
                cst.setString(15, "");
                cst.setString(16, "");
                cst.setString(17, "");
            }
            cst.setString(18, ac.getPassword());
            cst.executeQuery();
            id = cst.getString(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public String increaseSalary(String userID, int sal) {
        String SQL = "UPDATE employees_info\n" +
                    "    SET\n" +
                    "        salary = ?\n" +
                    "WHERE\n" +
                    "    employees_id = ?";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            pst.setInt(1, sal);
            pst.setString(2, userID);
            try {
                 pst.executeQuery();
                 return "succes";
            } catch (Exception e) {
                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public void testFunction(int depId) {
        String SQL = "begin ? := get_employees(?); end;";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.setInt(2, depId);
            
            
            cst.executeQuery();
            ResultSet rs = (ResultSet) cst.getObject(1);
            System.out.println("-----");
            while(rs.next())
            {
                System.out.println(rs.getString("first_name" ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
