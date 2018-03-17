/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.MedicineDAO;
import Main.Constants;
import Model.Doctor;
import Model.Surgery;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author User
 */
public class OperationController extends AnchorPane {

    @FXML
    private Label suggestedBy;

    @FXML
    private Label patientID;

    @FXML
    private Label operationID;

    @FXML
    private TableView availableTable;

    @FXML
    private TableColumn<Doctor, String> availableDoctorColm;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private TableView selectedDoctorTable;

    @FXML
    private TableColumn<Doctor, String> selectedDoctorColm;

    @FXML
    void confirm(ActionEvent event) {
        Task<String> operation_assign = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return new MedicineDAO().assign_operation(op.getOperationID(), new ArrayList<Doctor>(selectedDocList),DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(datePicker.getValue()));
            }
        };
        operation_assign.setOnSucceeded((ev) -> {
            // popup to be opened
        });
        new Thread(operation_assign).start();
    }

    @FXML
    void dateSelected(ActionEvent event) {
        getAvailableDoctors(true, Date.valueOf(datePicker.getValue()));
        prepareSelectTable();
    }

    @FXML
    void select(ActionEvent event) {
        try {
            Doctor d = (Doctor) availableTable.getSelectionModel().getSelectedItem();
            availableTable.getItems().remove(d);
            availDocList.remove(d);
            selectedDocList.add(d);
            selectedDoctorTable.setItems(selectedDocList);
        } catch (Exception e) {
        }

    }

    @FXML
    void deselect(ActionEvent event) {
        try {
            Doctor d = (Doctor) selectedDoctorTable.getSelectionModel().getSelectedItem();
            selectedDoctorTable.getItems().remove(d);
            selectedDocList.remove(d);
            availDocList.add(d);
            availableTable.setItems(availDocList);
        } catch (Exception e) {
        }

    }

    Surgery op;

    public OperationController(Surgery op) {
        this.op = op;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/operation.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getOperationInfo();
            getAvailableDoctors(false, null);
            prepareSelectTable();
            
            final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(
                                LocalDate.now()
                        )) {
                            setDisable(true);
                            setStyle("-fx-background-color: #808080;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void getOperationInfo() {

        operationID.setText(Integer.toString(op.getOperationID()));
        patientID.setText(op.getPatientID());
        suggestedBy.setText(op.getRequestedBy());

    }
    Map<String, Doctor> nameToDoctor;
    ObservableList<Doctor> availDocList;
    ObservableList<Doctor> selectedDocList;

    private void getAvailableDoctors(boolean isDate, Date dat) {
        availableTable.getItems().clear();
        Task<List<Doctor>> getDoctors = new Task<List<Doctor>>() {
            @Override
            protected List<Doctor> call() throws Exception {
                if (isDate) {
                    return new MedicineDAO().getDoctorsForOperationWithDate(op.getOperationID(), DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(datePicker.getValue()));
                } else {
                    return new MedicineDAO().getDoctorsForOperation(op.getOperationID());
                }

            }

        };
        getDoctors.setOnSucceeded((event) -> {
            try {
                nameToDoctor = new HashMap<>();
                for (Doctor d : getDoctors.getValue()) {
                    nameToDoctor.put(d.getFullName(), d);
                }
                availDocList = FXCollections.observableArrayList();
                availDocList.addAll(getDoctors.getValue());

                availableTable.setItems(availDocList);
                System.out.println(availDocList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

            availableDoctorColm.setCellValueFactory(new PropertyValueFactory<>("userID"));
             System.out.println(availDocList.size());
        });
        Thread th = new Thread(getDoctors);
        th.start();
    }

    private void prepareSelectTable() {
        selectedDocList = FXCollections.observableArrayList();
        selectedDoctorTable.getItems().clear();
        selectedDoctorColm.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }
}
