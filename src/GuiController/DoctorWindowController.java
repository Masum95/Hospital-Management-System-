/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.DoctorDAO;
import DAO.EmployeeDAO;
import DAO.MedicineDAO;
import DAO.PatientDAO;
import DAO.VacationDAO;
import Main.Constants;
import Model.Employee;
import Model.Surgery;
import Model.Vacation;
import Model.admission;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class DoctorWindowController extends AnchorPane {

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
    private JFXComboBox<String> nextXdays;

    @FXML
    private Label ScheduleForNext;

    @FXML
    private AnchorPane scheduleWindow;

    @FXML
    private TableView operationTable;

    @FXML
    private Label WindowTitle1;

    @FXML
    private Label WindowTitle12;

    @FXML
    private JFXDatePicker fromPicker;

    @FXML
    private JFXDatePicker toPicker;

    @FXML
    private TableView leaveTable;

    @FXML
    private Label WindowTitle11;

    ObservableList<String> mode = FXCollections.observableArrayList("Indoor Patient", "Outdoor Patient", "Other Employees", "Schedule");

    @FXML
    void indoorPatient(ActionEvent event) {
        infoWindow.setVisible(true);
        scheduleWindow.setVisible(false);
        populateIndoorTable();
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
    void outdoorPatient(ActionEvent event) {
        infoWindow.setVisible(true);
        scheduleWindow.setVisible(false);
        populateOutdoorTable();
    }

    @FXML
    void schedule(ActionEvent event) {
        infoWindow.setVisible(false);
        scheduleWindow.setVisible(true);
        populateOperationTable();
        populateLeaveTable();
    }

    @FXML
    void apply(ActionEvent event) {
        Task<String> applyLeave = new Task<String>() {
            @Override
            protected String call() throws Exception {
                String id = docID;
                return new VacationDAO().applyLeave(new Vacation().empId(id).
                        frmDate(DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(fromPicker.getValue())).
                        toDate(DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(fromPicker.getValue())));
            }
        };
        applyLeave.setOnSucceeded((e) -> {
            String ret = applyLeave.getValue();
            populateLeaveTable();
        });

    }
    ObservableList<Vacation> vacList;
    ObservableList<Surgery> operationList;

    @FXML
    void viewDetails(ActionEvent event) {
        admission ad = (admission) infoTable.getSelectionModel().getSelectedItem();
        ad.doctorID(docID);
        Stage newStage = new Stage();
        int presId = new PatientDAO().getPrescriptionID(ad.getAdmissionId());
        if (presId == -1) {
            newStage.setScene(new Scene(new EditPrescriptionController(ad)));
        } else {
            newStage.setScene(new Scene(new EditPrescriptionController(ad, true)));
        }

        newStage.show();
    }

    String docID;

    DoctorWindowController(String uId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/DoctorWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            docID = uId;
            getEmployeeInfo(uId);
            populateIndoorTable();
            WindowTitle.setText(mode.get(0));
        } catch (IOException ex) {
            Logger.getLogger(DoctorWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void populateOutdoorTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<admission>> loadData = new Task<List<admission>>() {
            @Override
            protected List<admission> call() throws Exception {
                return new PatientDAO().getOutdoorPatientsUnderDocotorID(docID);
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<admission> adlist = FXCollections.observableArrayList();
                adlist.addAll(loadData.getValue());
                infoTable.setItems(adlist);

                String[] colName = {"Admission ID", "Patient ID", "Name", "Serial No"};
                String[] property = {"admissionId", "patientID", "patientName", "serial"};

                int i = 0;
                for (String col : colName) {
                    if (i == 3) {
                        continue;
                    }
                    TableColumn<admission, String> col1 = new TableColumn<>(col);
                    col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                    infoTable.getColumns().add(col1);
                    i++;
                }
                TableColumn<admission, Integer> col1 = new TableColumn<>(colName[3]);
                col1.setCellValueFactory(new PropertyValueFactory<>(property[3]));
                infoTable.getColumns().add(col1);

            }
        });

        Thread th = new Thread(loadData);
        th.start();

    }

    private void populateIndoorTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<admission>> loadData = new Task<List<admission>>() {
            @Override
            protected List<admission> call() throws Exception {
                return new PatientDAO().getIndoorPatientsUnderDocotorID(docID);
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<admission> adlist = FXCollections.observableArrayList();
                adlist.addAll(loadData.getValue());
                infoTable.setItems(adlist);

                String[] colName = {"Admission ID", "Patient ID", "Date of Admission", "Room no"};
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

    private void populateOperationTable() {
        operationTable.getItems().clear();
        operationTable.getColumns().clear();
        Task<List<Surgery>> loadData = new Task<List<Surgery>>() {
            @Override
            protected List<Surgery> call() throws Exception {
                return new DoctorDAO().getAllOperationFor(docID);
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    operationList = FXCollections.observableArrayList();
                    operationList.addAll(loadData.getValue());
                    operationTable.setItems(vacList);
                } catch (Exception e) {
                }
                String[] colName = {"Operation ID", "Operation Name", "Patient ID ", "Date"};
                String[] property = {"id", "name", "patientID", "perfomDate"};

                int i = 0;
                for (String col : colName) {
                    if (i == 0) {
                        TableColumn<Surgery, Integer> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        operationTable.getColumns().add(col1);
                    } else {
                        TableColumn<Surgery, String> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        operationTable.getColumns().add(col1);
                    }

                    i++;
                }

            }
        });

        Thread th = new Thread(loadData);
        th.start();
    }

    private void populateLeaveTable() {
        leaveTable.getItems().clear();
        leaveTable.getColumns().clear();
        Task<List<Vacation>> loadData = new Task<List<Vacation>>() {
            @Override
            protected List<Vacation> call() throws Exception {
                return new VacationDAO().getAllVacations(docID);
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    vacList = FXCollections.observableArrayList();
                    vacList.addAll(loadData.getValue());
                    leaveTable.setItems(vacList);
                } catch (Exception e) {
                }
                String[] colName = {"From", "To", "Status"};
                String[] property = {"frmDate", "toDate", "status"};

                int i = 0;
                for (String col : colName) {
                    TableColumn<Vacation, String> col1 = new TableColumn<>(col);
                    col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                    leaveTable.getColumns().add(col1);
                    i++;
                }

            }
        });

        Thread th = new Thread(loadData);
        th.start();

    }
}
