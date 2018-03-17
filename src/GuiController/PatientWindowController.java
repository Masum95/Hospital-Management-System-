/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.EmployeeDAO;
import DAO.PatientDAO;
import Main.Constants;
import Model.Employee;
import Model.Patient;
import Model.admission;
import Model.prescriptionContent;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
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
public class PatientWindowController extends AnchorPane {

    @FXML
    private Label name;

    @FXML
    private ImageView profilePic;

    @FXML
    private Label WindowTitle;

    @FXML
    private AnchorPane infoWindow;

    @FXML
    private AnchorPane upperPane;
    @FXML
    private AnchorPane welcomePane;
    @FXML
    private TableView infoTable;

    @FXML
    private JFXComboBox<String> historyAs;

    @FXML
    private AnchorPane MyAccountWindow;

    @FXML
    private Label birthDate;

    @FXML
    private Label bloodGrp;

    @FXML
    private Label fullName;

    @FXML
    private Label regiDate;

    ObservableList<String> mode = FXCollections.observableArrayList("History", "My Account");
    ObservableList<String> tableAs = FXCollections.observableArrayList("Indoor Patient", "Outdoor Patient");

    @FXML
    void history(ActionEvent event) {
        MyAccountWindow.setVisible(false);
        infoWindow.setVisible(true);
        WindowTitle.setText(mode.get(0));
    }

    @FXML
    void historyAsCombo(ActionEvent event) {
        if (historyAs.getValue().equals(tableAs.get(0))) {
            populateIndoorTable();
        } else {
            populateOutdoorTable();
        }
    }

    private void populateIndoorTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<admission>> loadData = new Task<List<admission>>() {
            @Override
            protected List<admission> call() throws Exception {
                return new PatientDAO().getInAdmissionHistory(patID);
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<admission> adlist = FXCollections.observableArrayList();
                adlist.addAll(loadData.getValue());
                System.out.println(adlist.size());
                infoTable.setItems(adlist);

                String[] colName = {"Admission ID", "Doctor ID", "Admit Date", "Release Date"};
                String[] property = {"admissionId", "doctorID", "appointmentDate", "releaseDate"};

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

    private void populateOutdoorTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<admission>> loadData = new Task<List<admission>>() {
            @Override
            protected List<admission> call() throws Exception {
                return new PatientDAO().getOutadmissionHistory(patID);
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<admission> adlist = FXCollections.observableArrayList();
                adlist.addAll(loadData.getValue());
                System.out.println(adlist.size());
                infoTable.setItems(adlist);

                String[] colName = {"Admission ID", "Doctor ID", "Appointment Date"};
                String[] property = {"admissionId", "doctorID", "appointmentDate"};

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

    @FXML
    void logout(ActionEvent event) throws IOException {
        Button bt = (Button) event.getSource();
        Stage curstage = (Stage) bt.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_FILEPATH + "/Homepage.fxml"));
        Scene scene = new Scene(root);
        curstage.setScene(scene);
    }

    @FXML
    void myAccount(ActionEvent event) {
        infoWindow.setVisible(false);
        WindowTitle.setText(mode.get(1));
        MyAccountWindow.setVisible(true);
        fullName.setText(name.getText());
        birthDate.setText(p.getBirthDate());
        regiDate.setText(p.getRegDate());
        bloodGrp.setText(p.getBloodGroup());
    }

    @FXML
    void viewDetails(ActionEvent event) {
        Stage newStage = new Stage();
        admission ad = (admission) infoTable.getSelectionModel().getSelectedItem();
        newStage.setScene(new Scene(new EditPrescriptionController(ad, true)));
        newStage.show();
    }

    String patID;

    public PatientWindowController(String uId) {
        patID = uId;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/PatientWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getPatientInfo(uId);
            WindowTitle.setText(mode.get(0));
            historyAs.setItems(tableAs);
        } catch (IOException ex) {
            Logger.getLogger(NurseWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public PatientWindowController(String uId,boolean viewMode) {
        patID = uId;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/PatientWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            upperPane.setVisible(false); welcomePane.setVisible(false);
            historyAs.setItems(tableAs);
        } catch (IOException ex) {
            Logger.getLogger(NurseWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    Patient p;

    private void getPatientInfo(String userID) {
        Task<Patient> getPat = new Task<Patient>() {
            @Override
            protected Patient call() throws Exception {
                return new PatientDAO().getPatientAccount(userID);
            }

        };
        getPat.setOnSucceeded((event) -> {
            p = getPat.getValue();
            name.setText(p.getFirstName() + " " + p.getLastName());
            profilePic.setImage(p.getImage());
            System.out.println(p.toString());

        });
        Thread th = new Thread(getPat);
        th.start();
    }

}
