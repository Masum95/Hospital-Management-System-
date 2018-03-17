/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.DoctorDAO;
import DAO.EmployeeDAO;
import DAO.PatientDAO;
import DAO.SeatDAO;
import Main.Constants;
import Model.Doctor;
import Model.Employee;
import Model.SeatInfo;
import Model.admission;
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PatientRegistrarWindowController extends AnchorPane {

    @FXML
    private Label name;

    @FXML
    private ImageView profilePic;

    @FXML
    private Label WindowTitle;

    @FXML
    private AnchorPane infoWindow;

    @FXML
    private TableView infoTable;

    @FXML
    private AnchorPane registerPatientWindow;

    @FXML
    private JFXComboBox<String> building;

    @FXML
    private JFXComboBox<String> roomType;

    @FXML
    private JFXComboBox<String> roomNo;
    @FXML
    private JFXComboBox<Integer> floorNo;

    @FXML
    private JFXComboBox<Integer> wardNo;

    @FXML
    private JFXComboBox<String> department;

    @FXML
    private TextField patientID;

    @FXML
    private JFXComboBox<String> wardType;

    @FXML
    private Label price;

    @FXML
    private TextField referredBy;

    @FXML
    private JFXComboBox<String> consultingDoctor;

    @FXML
    private TextField advanced;

    ObservableList<String> mode = FXCollections.observableArrayList("Current Patient", "Register Patient");

    @FXML
    void allocateBed(ActionEvent event) {
        final admission ad = new admission().refdBy(referredBy.getText().trim()).advancePaid(Integer.parseInt(advanced.getText().trim()))
                .doctorID(nameToID.get(consultingDoctor.getValue())).roomNo(roomNo.getValue()).patientID(patientID.getText().trim());
        new Thread(
                () -> {
                    new PatientDAO().addIndoorPatient(ad);
                }).start();
    }

    @FXML
    void billing(ActionEvent event) {
        admission ad = (admission) infoTable.getSelectionModel().getSelectedItem();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(new IndoorBillingController(ad.getAdmissionId())));
        newStage.show();
    }

    @FXML
    void buildingCombo(ActionEvent event) {
        Set<Integer> fno = new HashSet<>();
        for (SeatInfo s : seatList) {
            if (s.getBuildingNo().equals(building.getValue())) {
                fno.add(s.getFloorNo());
            }
        }
        floorNo.setItems(FXCollections.observableArrayList(fno));
    }

    @FXML
    void currentPaitent(ActionEvent event) {
        WindowTitle.setText(mode.get(0));
        registerPatientWindow.setVisible(false);
        infoWindow.setVisible(true);
        populateIndoorTable();
    }

    @FXML
    void deptCombo(ActionEvent event) {
        consultingDoctor.setItems(FXCollections.observableArrayList(deptList.get(department.getValue())));
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Button bt = (Button) event.getSource();
        Stage curstage = (Stage) bt.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_FILEPATH + "/Homepage.fxml"));
        Scene scene = new Scene(root);
        curstage.setScene(scene);
    }

    @FXML
    void registerPatient(ActionEvent event) {
        WindowTitle.setText(mode.get(1));
        infoWindow.setVisible(false);
        registerPatientWindow.setVisible(true);
    }

    @FXML
    void remove(ActionEvent event) {
        admission ad = (admission) infoTable.getSelectionModel().getSelectedItem();
        infoTable.getItems().remove(ad);
    }

    @FXML
    void floorNoCombo(ActionEvent event) {
        Set<Integer> wno = new HashSet<>();
        for (SeatInfo s : seatList) {
            if (s.getFloorNo() == floorNo.getValue() && s.getBuildingNo().equals(building.getValue()) && s.getWardType().equals(wardType.getValue())) {
                wno.add(s.getWardNo());
            }
        }
        wardNo.setItems(FXCollections.observableArrayList(wno));
    }

    @FXML
    void roomNoCombo(ActionEvent event) {

    }

    @FXML
    void roomTypeCombo(ActionEvent event) {
        Set<String> rn = new HashSet<>();
        for (SeatInfo s : seatList) {
            if (s.getRoomType().equals(roomType.getValue()) && s.getWardNo() == wardNo.getValue() && s.getFloorNo() == floorNo.getValue() && s.getBuildingNo().equals(building.getValue()) && s.getWardType().equals(wardType.getValue())) {
                rn.add(s.getRoomNo());
                price.setText(Integer.toString(s.getPrice()));
            }
        }
        roomNo.setItems(FXCollections.observableArrayList(rn));
    }

    @FXML
    void wardNoCombo(ActionEvent event) {
        Set<String> rt = new HashSet<>();
        for (SeatInfo s : seatList) {
            if (s.getWardNo() == wardNo.getValue() && s.getFloorNo() == floorNo.getValue() && s.getBuildingNo().equals(building.getValue()) && s.getWardType().equals(wardType.getValue())) {
                rt.add(s.getRoomType());
            }
        }
        roomType.setItems(FXCollections.observableArrayList(rt));
    }

    @FXML
    void wardTypeCombo(ActionEvent event) {
    }

    List<SeatInfo> seatList;

    private void getSeatInfo() {
        Task<List<SeatInfo>> getSeat = new Task<List<SeatInfo>>() {
            @Override
            protected List<SeatInfo> call() throws Exception {
                return new SeatDAO().getSeatInfo();
            }

        };
        getSeat.setOnSucceeded((event) -> {
            seatList = getSeat.getValue();
            Set<String> bno = new HashSet<>();
            for (SeatInfo s : seatList) {
                bno.add(s.getBuildingNo());
            }
            building.setItems(FXCollections.observableArrayList(bno));
            wardType.setItems(FXCollections.observableArrayList("Male", "Female"));
        });
        Thread th = new Thread(getSeat);
        th.start();
    }

    Map<String, String> nameToID;
    List<Doctor> docList;

    private void getDoctorList() {
        Task getDoctor = new Task() {
            @Override
            protected Object call() throws Exception {
                docList = new DoctorDAO().getAllDoctors();
                return null;
            }
        };
        getDoctor.setOnSucceeded((event) -> {
            preparePhysicanList();
        });
        Thread th = new Thread(getDoctor);
        th.setDaemon(true);
        th.start();
    }

    Map<String, List<String>> deptList;

    void preparePhysicanList() {
        // Problems : how to select if there are duplicate values in listview
        nameToID = new HashMap<>();

        deptList = new HashMap<>(); // For grouping by doctos deptWise
        List<String> doclist = new ArrayList<>();
        for (Doctor d : docList) {

            if (!deptList.containsKey(d.getSpecialisation())) {
                deptList.put(d.getSpecialisation(), new ArrayList<String>());
            }
            deptList.get(d.getSpecialisation()).add(d.getFullName());
            //System.out.println(d.getFullName() );
            nameToID.put(d.getFullName(), d.getUserID());
            doclist.add(d.getFullName());
            //System.out.println(nameToID.get(d.getFullName()));
        }

        TextFields.bindAutoCompletion(referredBy, doclist);

        int i = 0;
        ObservableList<String> departmentList = FXCollections.observableArrayList();
        for (Map.Entry<String, List<String>> tr : deptList.entrySet()) {
            ListView<String> doc = new ListView<>();
            doc.getItems().addAll(tr.getValue());
            departmentList.add(tr.getKey());
        }
        department.setItems(departmentList);
    }

    PatientRegistrarWindowController(String uId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/PatientRegistrarWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getDoctorList();
            getSeatInfo();
            getEmployeeInfo(uId);
            populateIndoorTable();
            WindowTitle.setText(mode.get(0));
        } catch (IOException ex) {
            Logger.getLogger(PatientRegistrarWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void populateIndoorTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<admission>> loadData = new Task<List<admission>>() {
            @Override
            protected List<admission> call() throws Exception {
                return new PatientDAO().getAllCurrentIndoorPatient();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<admission> adlist = FXCollections.observableArrayList();
                adlist.addAll(loadData.getValue());
                infoTable.setItems(adlist);

                String[] colName = {"Admission ID", "Patient ID", "Date Of Admission", "Room No"};
                String[] property = {"admissionId", "patientID", "appointmentDate", "roomNo"};

                int i = 0;
                for (String col : colName) {

                    TableColumn<admission, String> col1 = new TableColumn<>(col);
                    col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                    infoTable.getColumns().add(col1);
                    i++;
                }

            }
        });

        Thread th = new Thread(loadData);
        th.start();

    }

    private void getEmployeeInfo(String userID) {
        Task<Employee> getEmp = new Task<Employee>() {
            @Override
            protected Employee call() throws Exception {
                return new EmployeeDAO().getEmployee(userID);
            }
        };
        getEmp.setOnSucceeded((event) -> {
            Employee e = getEmp.getValue();
            name.setText(e.getFullName());
            profilePic.setImage(e.getImage());
        });
        Thread th = new Thread(getEmp);

        th.start();
    }
}
