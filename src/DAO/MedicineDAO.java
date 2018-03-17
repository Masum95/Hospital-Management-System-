/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Model.DiagnosisTest;
import Model.Doctor;
import Model.Medicine;
import Model.Surgery;
import Model.prescriptionContent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Masum
 */
public class MedicineDAO {

    public MedicineDAO() {
    }

    public List<Medicine> getAllMedicine() {
        List<Medicine> medList = new ArrayList<>();
        String SQL = "select brand_name,medicine_id from medicine_info";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                medList.add(new Medicine().brandName(rs.getString("brand_name")).MedicineID(rs.getInt("medicine_id")));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return medList;
    }

    public List<Surgery> getAllSurgerys() {
        List<Surgery> surgeryList = new ArrayList<>();
        String SQL = "select * from SURGERY_INFO";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                surgeryList.add(new Surgery().surgeryName(rs.getString("surgery_name")).department(rs.getString("department")).surgeryCost(rs.getInt("cost")).surgeryID(rs.getInt("SURGERY_INFO_PK")));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return surgeryList;
    }

    public List<DiagnosisTest> getAllDiagnosisTests() {
        List<DiagnosisTest> testList = new ArrayList<>();
        String SQL = "select * from DIAGNOSIS_TEST_INFO";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getString("test_name" ));
                testList.add(new DiagnosisTest().testName(rs.getString("test_name")).testID(rs.getInt("test_id")).cost(rs.getInt("cost")));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return testList;
    }

    public int addPrescriptionContent(prescriptionContent p) {
        int id = 0;
        String SQL = "{?= call insert_in_prescription ( ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();) {
            try (CallableStatement cst = connection.prepareCall(SQL);) {

                cst.registerOutParameter(1, Types.INTEGER);
                cst.setString(2, p.getAdmitID());
                cst.setInt(3, p.getBphigh());
                cst.setInt(4, p.getBplow());
                cst.setInt(5, p.getPulse());
                cst.setInt(6, p.getWeight());
                cst.setInt(7, p.getGlucose());
                cst.executeQuery();
                id = cst.getInt(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public void addMedicineContent(int pid, List<Medicine> mlist) {
        int id = 0;
        String SQL = "{ call insert_in_med_content ( ?, ?, ?, ?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();) {
            try (CallableStatement cst = connection.prepareCall(SQL);) {
                for (Medicine m : mlist) {

                    cst.setInt(1, m.getId());
                    cst.setInt(2, pid);
                    cst.setString(3, m.getDosageInstruction());
                    cst.setString(4, m.getComments());
                    cst.setInt(5, m.getUpto());
                    cst.executeQuery();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addSurgeryContent(String adID, List<Surgery> m) {
        String SQL = "{call insert_in_operation ( ?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();) {
            try (CallableStatement cst = connection.prepareCall(SQL);) {
                for (Surgery t : m) {

                    cst.setInt(1, t.getId());
                    cst.setString(2, adID);

                    cst.executeQuery();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addDiagnosisContent(int pid, List<DiagnosisTest> tlist) {

        String SQL = "{call insert_in_diagnosis_content ( ?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();) {
            try (CallableStatement cst = connection.prepareCall(SQL);) {
                for (DiagnosisTest t : tlist) {

                    cst.setString(1, t.getName());
                    cst.setInt(2, pid);

                    cst.executeQuery();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public prescriptionContent getPrescription(String adID) throws SQLException {
        boolean outdoor = adID.charAt(0) == 'O';
        System.out.println(adID);
        prescriptionContent ps = null;
        List<DiagnosisTest> testList = new ArrayList<>();
        List<Medicine> medList = new ArrayList<>();
        List<Surgery> surlist = new ArrayList<>();
        String SQL = "select * from PRESCRIPTION where ADMISSION_ID = ?";

        try (Connection connection = DBUtil.getDataSource().getConnection();) {

            try (PreparedStatement pst = connection.prepareStatement(SQL);) {
                pst.setString(1, adID);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    ps = new prescriptionContent().bpHigh(rs.getInt("bphi")).bpLow(rs.getInt("bplo")).pulse(rs.getInt("pulse")).weight(rs.getInt("weight"));
                    ps = ps.glucose(rs.getInt("glucose")).dateOfPrescription(new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("prescription_date")));

                }
                rs.close();
            } catch (Exception e) {
            }
            // get medicine content 
            SQL = "SELECT\n"
                    + "    mc.*, mi.brand_name\n"
                    + "FROM\n"
                    + "    medicine_content mc\n"
                    + "    JOIN medicine_info mi ON (\n"
                    + "        mc.medicine_id = mi.medicine_id\n"
                    + "    )\n"
                    + "    JOIN prescription p ON (\n"
                    + "        mc.prescription_id = p.prescription_id\n"
                    + "    )\n"
                    + "WHERE\n"
                    + "    admission_id = ?";
            try (PreparedStatement pst = connection.prepareStatement(SQL);) {
                pst.setString(1, adID);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Medicine m = new Medicine().brandName(rs.getString("brand_name")).dosageInstruction(rs.getString("dosage_instruction")).comments(rs.getString("comments")).upto(rs.getInt("upto"));
                    System.out.println(m.getBrandName());
                    medList.add(m);
                }
                ps = ps.medicineList(medList);
                rs.close();
            } catch (Exception e) {
            }
            // get diagnosis content
            SQL = "SELECT\n"
                    + "    dc.*,\n"
                    + "    di.test_name,\n"
                    + "    di.COST\n"
                    + "FROM\n"
                    + "    diagnosis_content dc\n"
                    + "    JOIN prescription p ON (\n"
                    + "        dc.prescription_id = p.prescription_id\n"
                    + "    )\n"
                    + "    JOIN diagnosis_test_info di ON (\n"
                    + "        dc.test_id = di.test_id\n"
                    + "    )\n"
                    + "WHERE\n"
                    + "    p.admission_id = ?";
            try (PreparedStatement pst = connection.prepareStatement(SQL);) {
                pst.setString(1, adID);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getInt("test_id"));
                    DiagnosisTest d = new DiagnosisTest().testID(rs.getInt("test_id")).testName(rs.getString("test_name")).cost(rs.getInt("cost"));
                    System.out.println(d.getName());
                    testList.add(d);
                }
                ps = ps.diagnosisList(testList);
                rs.close();
            } catch (Exception e) {
            }
            // get surgery content 
            SQL = "SELECT\n"
                    + "    *\n"
                    + "FROM\n"
                    + "    operation\n"
                    + "    JOIN surgery_info ON (\n"
                    + "        surgery_info_pk = surgery_id\n"
                    + "    )\n"
                    + "WHERE\n"
                    + "    admission_id = ?";
            try (PreparedStatement pst = connection.prepareStatement(SQL);) {
                pst.setString(1, adID);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("surgery_name"));
                    Surgery s = new Surgery().surgeryName(rs.getString("surgery_name")).operationID(rs.getInt("operation_id")).DateOfPerformance(rs.getDate("operation_date"));
                    s = s.surgeryID(rs.getInt("surgery_id")).surgeryCost(rs.getInt("cost"));
                    System.out.println(s.getName());
                    surlist.add(s);
                }
                ps = ps.surgeryList(surlist);
                rs.close();
            } catch (Exception e) {
            }
            // get patient id
            if (outdoor) {
                SQL = "SELECT\n"
                        + "    patients_id\n"
                        + "FROM\n"
                        + "    patients_info\n"
                        + "    JOIN out_admission USING ( patients_id )\n"
                        + "WHERE\n"
                        + "    admission_id = ?";
            } else {
                SQL = "SELECT\n"
                        + "    patients_id\n"
                        + "FROM\n"
                        + "    patients_info\n"
                        + "    JOIN in_admission USING ( patients_id )\n"
                        + "WHERE\n"
                        + "    admission_id = ?";
            }

            try (PreparedStatement pst = connection.prepareStatement(SQL);) {
                pst.setString(1, adID);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    ps = ps.patientID(rs.getString("patients_id"));
                }
                rs.close();
            } catch (Exception e) {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ps;
    }

    public List<Surgery> getAllOperations() {
        List<Surgery> surgeryList = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    operation_id,\n"
                + "    patients_id,\n"
                + "    consulting_doctor,\n"
                + "    surgery_name\n"
                + "FROM\n"
                + "    operation o,\n"
                + "    in_admission ia,\n"
                + "    surgery_info si\n"
                + "WHERE\n"
                + "        o.admission_id = ia.admission_id\n"
                + "    AND\n"
                + "        o.surgery_id = si.surgery_info_pk";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Surgery s = new Surgery().operationID(rs.getInt("operation_id")).surgeryName(rs.getString("surgery_name"));
                s = s.patientID(rs.getString("patients_id")).requestedBy(rs.getString("consulting_doctor"));
                System.out.println("---" + s.getName());
                surgeryList.add(s);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return surgeryList;
    }

    

    public List<Doctor> getDoctorsForOperation(int opID) throws SQLException {
        List<Doctor> docList = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    employees_id,\n"
                + "    first_name||' '||last_name fullName ,\n"
                + "    specialization\n"
                + "from employees_info join doctors_info using(employees_id) ,\n"
                + "    (\n"
                + "    select department from surgery_info join operation on(surgery_id = surgery_info_pk) where operation_id = ?\n"
                + "        \n"
                + "    ) surInfo\n"
                + "    where upper(TRIM((REPLACE(surinfo.department,' ','')) )) = upper(TRIM(REPLACE(specialization,' ','')) ) ";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            pst.setInt(1, opID);
            ResultSet rs = pst.executeQuery();
            System.out.println("----");
            while (rs.next()) {
                System.out.println(rs.getString("fullName"));
                Doctor d = new Doctor().employeeID(rs.getString("employees_id")).fullName(rs.getString("fullName")).specialisation(rs.getString("specialization"));
                docList.add(d);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return docList;
    }

    public List<DiagnosisTest> getReportRequests() {
        List<DiagnosisTest> testList = new ArrayList<>();
        String SQL = "SELECT \n"
                + "    dc.diagnosis_content_id,ia.patients_id,di.test_name,p.prescription_date\n"
                + "FROM\n"
                + "    diagnosis_content dc,\n"
                + "    diagnosis_test_info di,\n"
                + "    prescription p,\n"
                + "    in_admission ia\n"
                + "WHERE\n"
                + "        date_of_performance IS NULL\n"
                + "    AND\n"
                + "        dc.test_id = di.test_id\n"
                + "    AND\n"
                + "        dc.prescription_id = p.prescription_id\n"
                + "    AND \n"
                + "        ia.admission_id = p.admission_id\n"
                + "    \n"
                + "UNION\n"
                + "SELECT \n"
                + "    dc.diagnosis_content_id, oa.PATIENTS_ID,di.test_name, p.prescription_date\n"
                + "FROM\n"
                + "    diagnosis_content dc,\n"
                + "    diagnosis_test_info di,\n"
                + "    prescription p,\n"
                + "    out_admission oa\n"
                + "WHERE\n"
                + "        date_of_performance IS NULL\n"
                + "    AND\n"
                + "        dc.test_id = di.test_id\n"
                + "    AND\n"
                + "        dc.prescription_id = p.prescription_id\n"
                + "    AND \n"
                + "        oa.admission_id = p.admission_id";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                testList.add(new DiagnosisTest().testName(rs.getString("test_name")).testID(rs.getInt("diagnosis_content_id")).patientID(rs.getString("Patients_id")).suggestionDate(rs.getDate("prescription_date")));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return testList;
    }

    // to be edited
    public List<DiagnosisTest> getPendingReports() {
        List<DiagnosisTest> testList = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    dc.diagnosis_content_id,ia.patients_id,di.test_name,dc.date_of_performance\n"
                + "FROM\n"
                + "    diagnosis_content dc,\n"
                + "    diagnosis_test_info di,\n"
                + "    prescription p,\n"
                + "    in_admission ia\n"
                + "WHERE\n"
                + "        date_of_delivery IS NULL AND date_of_performance IS NOT NULL\n"
                + "    AND\n"
                + "        dc.test_id = di.test_id\n"
                + "    AND\n"
                + "        dc.prescription_id = p.prescription_id\n"
                + "    AND\n"
                + "        ia.admission_id = p.admission_id\n"
                + "UNION\n"
                + "SELECT\n"
                + "    dc.diagnosis_content_id,oa.patients_id,di.test_name,dc.date_of_performance\n"
                + "FROM\n"
                + "    diagnosis_content dc,diagnosis_test_info di,prescription p, out_admission oa\n"
                + "WHERE\n"
                + "        date_of_delivery IS  NULL AND date_of_performance IS NOT NULL\n"
                + "    AND\n"
                + "        dc.test_id = di.test_id\n"
                + "    AND\n"
                + "        dc.prescription_id = p.prescription_id\n"
                + "    AND\n"
                + "        oa.admission_id = p.admission_id";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DiagnosisTest d = new DiagnosisTest().testName(rs.getString("test_name")).testID(rs.getInt("diagnosis_content_id")).patientID(rs.getString("patients_id"));
                d = d.dateOfPerformance(rs.getDate("date_of_performance"));
                System.out.println(d.getName());
                testList.add(d);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return testList;
    }

    //To be edited
    public List<DiagnosisTest> getDeliveredReports() {
        List<DiagnosisTest> testList = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    dc.diagnosis_content_id,ia.patients_id,di.test_name,dc.date_of_delivery\n"
                + "FROM\n"
                + "    diagnosis_content dc,\n"
                + "    diagnosis_test_info di,\n"
                + "    prescription p,\n"
                + "    in_admission ia\n"
                + "WHERE\n"
                + "        date_of_delivery IS not NULL AND date_of_performance IS NOT NULL\n"
                + "    AND\n"
                + "        dc.test_id = di.test_id\n"
                + "    AND\n"
                + "        dc.prescription_id = p.prescription_id\n"
                + "    AND\n"
                + "        ia.admission_id = p.admission_id\n"
                + "UNION\n"
                + "SELECT\n"
                + "    dc.diagnosis_content_id,\n"
                + "    oa.patients_id,\n"
                + "    di.test_name,\n"
                + "    dc.date_of_delivery\n"
                + "FROM\n"
                + "    diagnosis_content dc,diagnosis_test_info di,prescription p, out_admission oa\n"
                + "WHERE\n"
                + "        date_of_delivery IS not NULL AND date_of_performance IS NOT NULL\n"
                + "    AND\n"
                + "        dc.test_id = di.test_id\n"
                + "    AND\n"
                + "        dc.prescription_id = p.prescription_id\n"
                + "    AND\n"
                + "        oa.admission_id = p.admission_id";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("-----------");
                DiagnosisTest d = new DiagnosisTest().testName(rs.getString("test_name")).testID(rs.getInt("diagnosis_content_id")).patientID(rs.getString("patients_id"));
                d = d.dateOfDelivery(rs.getDate("date_of_delivery"));
                System.out.println(d.getName());
                testList.add(d);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return testList;
    }

    public void performedReport(DiagnosisTest m) {
        String SQL = "update DIAGNOSIS_CONTENT set date_of_performance =  to_date(to_char(sysdate,'dd-Mon-yyyy'),'dd-Mon-yyyy') where diagnosis_content_id = ?";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            pst.setInt(1, m.getId());
            ResultSet rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deliveredReport(DiagnosisTest m) {
        String SQL = "update DIAGNOSIS_CONTENT set date_of_delivery =  to_date(to_char(sysdate,'dd-Mon-yyyy'),'dd-Mon-yyyy') where diagnosis_content_id = ?";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            pst.setInt(1, m.getId());
            ResultSet rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Doctor> getDoctorsForOperationWithDate(int opID, String dat) {
        List<Doctor> docList = new ArrayList<>();
        String SQL = "SELECT\n" +
                    "    ei.employees_id,\n" +
                    "    first_name || ' ' || last_name fullname,\n" +
                    "    specialization\n" +
                    "FROM\n" +
                    "    employees_info ei\n" +
                    "    JOIN doctors_info di ON (\n" +
                    "        di.employees_id = ei.employees_id\n" +
                    "    )\n" +
                    "    LEFT OUTER JOIN (\n" +
                    "        SELECT\n" +
                    "            employees_id,\n" +
                    "            COUNT(*) vacation_count\n" +
                    "        FROM\n" +
                    "            vacation\n" +
                    "        WHERE\n" +
                    "                verified = 'yes'\n" +
                    "            AND\n" +
                    "                TO_DATE(?,'dd-mon-yyyy') BETWEEN fromdate AND todate\n" +
                    "        GROUP BY\n" +
                    "            employees_id\n" +
                    "        HAVING\n" +
                    "            COUNT(*) > 0\n" +
                    "    ) vactable ON (\n" +
                    "        vactable.employees_id <> ei.employees_id\n" +
                    "    ),\n" +
                    "    (\n" +
                    "        SELECT\n" +
                    "            department\n" +
                    "        FROM\n" +
                    "            surgery_info\n" +
                    "            JOIN operation ON (\n" +
                    "                surgery_id = surgery_info_pk\n" +
                    "            )\n" +
                    "        WHERE\n" +
                    "            operation_id = ?\n" +
                    "    ) surinfo\n" +
                    "WHERE\n" +
                    "    upper(TRIM( (replace(surinfo.department, ' ','') ) ) ) = upper(TRIM(replace(specialization,' ','') ) )";

        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            pst.setInt(2, opID);
            pst.setString(1, dat);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Doctor d = new Doctor().employeeID(rs.getString("employees_id")).fullName(rs.getString("fullName")).specialisation(rs.getString("specialization"));
                docList.add(d);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return docList;
    }

    public String assign_operation(int opID,ArrayList<Doctor> tlist,String date) {
        String SQL = "{?= call insert_in_operation_assignment ( ?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();) {
            
            try (CallableStatement cst = connection.prepareCall(SQL);) {
                for (Doctor t : tlist) {
                    cst.registerOutParameter(1, Types.VARCHAR);
                    cst.setInt(2, opID);
                    cst.setString(3, t.getUserID());

                    cst.executeQuery();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
            SQL = "update operation set operation_date = to_date(?,'dd-Mon-yyyy') where operation_id = ?";
            try (PreparedStatement pst = connection.prepareStatement(SQL);) {
                pst.setString(1, date);
                pst.setInt(2, opID);
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return "Success";
    }

}
