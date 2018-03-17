/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBConnection.DBUtil;
import Main.Constants;
import Model.Account;
import Model.Location;
import Model.Patient;
import Model.admission;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class PatientDAO {

    public PatientDAO() {
    }

    public List<Patient> getAllPatients() {
        return null;
    }

    public void RemoveCurrentPatient(Patient p) {

    }

    public String addPatient(Patient p, Account ac, Location l) {
        String id = null;
        String SQL = "{?= call insert_in_patient ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            cst.setString(9, ac.getUserName());
            cst.setString(10, ac.getPassword());
            cst.setString(11, ac.getEmail());
            cst.executeQuery();
            id = cst.getString(1);
            //System.out.println(id);
            BufferedImage bi = SwingFXUtils.fromFXImage(ac.getImage(), null);
            ImageIO.write(bi, "jpg", new File(Constants.patientImgPath + id + ".jpg"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }
    public String addIndoorPatient(admission ad) {
        String id = null;
        String SQL = "{?= call insert_in_in_admission ( ?, ?, ?, ?, ?)";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                CallableStatement cst = connection.prepareCall(SQL);) {

            cst.registerOutParameter(1, Types.VARCHAR);
            cst.setString(2, ad.getRefdBy());
            cst.setString(3, ad.getDoctorID());
            cst.setString(4, ad.getPatientID());
            cst.setInt(5, ad.getAdvancePaid());
            cst.setString(6, ad.getRoomNo());
            cst.executeQuery();
            System.out.println("--###########");
            System.out.println(cst.getString(1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public Patient getPatientAccount(String userId) {
        String SQL = "SELECT\n"
                + "    pi.*,\n"
                + "    pa.IMAGE_LINK\n"
                + "FROM\n"
                + "    patients_info pi,\n"
                + "    patients_account pa\n"
                + "WHERE\n"
                + "        pi.PATIENTS_ID = ? \n"
                + "    AND\n"
                + "        pi.PATIENTS_ID = pa.PATIENTS_ID";
        Patient p = null;
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String patId = rs.getString("PATIENTS_ID");
                String fName = rs.getString("first_name");
                String lName = rs.getString("last_name");
                String bdate = new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("birth_date"));
                String bg = rs.getString("BLOOD_GROUP");
                String regiDate = new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("REGISTRATION_DATE"));
                String imgPath = new File(rs.getString("image_link")).getAbsolutePath();
                Image img = new Image(new FileInputStream(new File(imgPath)));
                p = new Patient().birthDate(bdate).bloodGroup(bg).firstName(fName).lastName(lName).image(img).patientID(userId).registrationDate(regiDate);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p;
    }

    public List<admission> getOutdoorPatientsUnderDocotorID(String docID) {
        String SQL = "SELECT\n"
                + "    admission_id,\n"
                + "    o.patients_id,\n"
                + "    ( first_name || ' ' || last_name ) fullname,\n"
                + "    serial_no\n"
                + "FROM\n"
                + "    out_admission o, patients_info p\n"
                + "WHERE\n"
                + "        appointment_date = trunc(SYSDATE)\n"
                + "    AND\n"
                + "        employees_id =  ? \n"
                + "    AND\n"
                + "        o.patients_id = p.patients_id";
        List<admission> adList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, docID);
            ResultSet rs = pst.executeQuery();
            System.out.println("here in function");
            while (rs.next()) {
                String adid = rs.getString("admission_id");
                String fName = rs.getString("fullName");
                String pid = rs.getString("patients_id");
                int sl = rs.getInt("serial_no");
                //String bdate = new SimpleDateFormat("dd-MMM-yy").format(rs.getDate("birth_date"));

                admission a = new admission().admissionID(adid).patientID(pid).patientName(fName).serial(sl);
                System.out.println(a.getAdmissionId());
                adList.add(a);

            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adList;
    }

    public List<admission> getIndoorPatientsUnderDocotorID(String docID) {
        String SQL = "SELECT\n" +
                            "    *\n" +
                            "FROM\n" +
                            "    in_admission\n" +
                            "WHERE\n" +
                            "        consulting_doctor = ?\n" +
                            "    AND\n" +
                            "        release_date IS NULL";
        List<admission> adList = new ArrayList<>();
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, docID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String adid = rs.getString("admission_id");
                String pid = rs.getString("patients_id");

                admission a = new admission().admissionID(adid).patientID(pid).appointmentDate( new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("admit_date")));
                a = a.roomNo(rs.getString("room_no" ));
                adList.add(a);

            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adList;
    }

    public Patient getPatientDetails(String userID) {
        Patient p = null ;
        String SQL = "SELECT\n"
                + "    p.*,\n"
                + "    trunc(months_between(SYSDATE,birth_date) / 12) age\n"
                + "FROM\n"
                + "    patients_info p\n"
                + "WHERE\n"
                + "    patients_id = ?";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            
            pst.setString(1, userID);
            System.out.println(userID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                p = new Patient().age(rs.getInt("age")).firstName(rs.getString("first_name" )).lastName(rs.getString("last_name" )).patientID(userID);
                System.out.println(p.getFirstName());
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p;
    }

    public List<admission> getOutadmissionHistory(String patId) {
        List<admission> adlist = new ArrayList<>();
        String SQL = "SELECT\n"
                + "    *\n"
                + "FROM\n"
                + "    out_admission\n"
                + "WHERE\n"
                + "    patients_id = ?";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, patId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                admission ad = new admission().admissionID(rs.getString("admission_id")).doctorID(rs.getString("Employees_id"));
                ad = ad.appointmentDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("appointment_date")));
                adlist.add(ad);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adlist;
    }

    public List<admission> getInAdmissionHistory(String patID) {
        List<admission> adlist = new ArrayList<>();
        String SQL = "SELECT\n" +
                        "    *\n" +
                        "FROM\n" +
                        "    in_admission\n" +
                        "WHERE\n" +
                        "    patients_id = ?";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            pst.setString(1, patID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                admission ad = new admission().admissionID(rs.getString("admission_id")).doctorID(rs.getString("consulting_doctor")).roomNo(rs.getString("room_no" ));
                ad = ad.appointmentDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("admit_date"))).releaseDate(rs.getDate("release_date"));
                ad = ad.patientID(rs.getString("patients_id" ));
                adlist.add(ad);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adlist;
    }

    public List<admission> getAllCurrentIndoorPatient() {
         List<admission> adlist = new ArrayList<>();
        String SQL = "SELECT\n" +
                        "    *\n" +
                        "FROM\n" +
                        "    in_admission\n" +
                        "WHERE\n" +
                        "    release_date IS NULL";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                admission ad = new admission().admissionID(rs.getString("admission_id")).doctorID(rs.getString("consulting_doctor"));
                ad = ad.appointmentDate(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate("admit_date"))).roomNo(rs.getString("room_no" ));
                ad = ad.patientID(rs.getString("patients_id" ));
                adlist.add(ad);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adlist;
    }

    public int getPrescriptionID(String admissionID){
        int ret = -1;
        String SQL = "SELECT\n" +
                        "    prescription_id\n" +
                        "FROM\n" +
                        "    prescription\n" +
                        "WHERE\n" +
                        "    admission_id = ?";
        try (Connection connection = DBUtil.getDataSource().getConnection();
                PreparedStatement pst = connection.prepareStatement(SQL);) {
            pst.setString(1, admissionID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ret = rs.getInt("prescription_id" );
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
    

}
