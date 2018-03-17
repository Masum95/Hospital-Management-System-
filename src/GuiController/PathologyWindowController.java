/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.EmployeeDAO;
import DAO.MedicineDAO;
import Main.Constants;
import Model.DiagnosisTest;
import Model.Employee;
import Model.Surgery;
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
public class PathologyWindowController extends AnchorPane {
    
    @FXML
    private Label name;
    @FXML
    private Button detailsBtn;
    
    @FXML
    private ImageView profilePic;
    
    @FXML
    private Label WindowTitle;
    
    @FXML
    private AnchorPane infoWindow;
    
    @FXML
    private TableView infoTable;
    
    ObservableList<String> mode = FXCollections.observableArrayList("Report Request", "Pending Report", "Report History");
    
    @FXML
    void logout(ActionEvent event) throws IOException {
        Button bt = (Button) event.getSource();
        Stage curstage = (Stage) bt.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_FILEPATH + "/Homepage.fxml"));
        Scene scene = new Scene(root);
        curstage.setScene(scene);
    }
    
    @FXML
    void ReportHistory(ActionEvent event) {
        WindowTitle.setText(mode.get(2));
        detailsBtn.setVisible(false);
        populateReportHistoryTable();
    }
    
    @FXML
    void pendingReport(ActionEvent event) {
        WindowTitle.setText(mode.get(1));
        detailsBtn.setVisible(true);
        detailsBtn.setText("Delivered");
        populatePendingReportTable();
    }
    
    @FXML
    void reportRequest(ActionEvent event) {
        detailsBtn.setVisible(true);
        detailsBtn.setText("Report Done");
        WindowTitle.setText(mode.get(0));
        
        populateReportRequestTable();
    }
    
    @FXML
    void viewDetails(ActionEvent event) {
        DiagnosisTest m = ((DiagnosisTest) infoTable.getSelectionModel().getSelectedItem());
        
        if (WindowTitle.getText().equals(mode.get(0))) {
            new Thread(()->{
                new MedicineDAO().performedReport(m);
            }).start();
        } else if (WindowTitle.getText().equals(mode.get(1))) {
            new Thread(()->{
                new MedicineDAO().deliveredReport(m);
            }).start();
        } 
        DiagnosisTest d = (DiagnosisTest) infoTable.getSelectionModel().getSelectedItem();
        infoTable.getItems().remove(d);
    }
    
    PathologyWindowController(String uId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/PathologyWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getEmployeeInfo(uId);
            populateReportRequestTable();
            WindowTitle.setText(mode.get(0));
        } catch (IOException ex) {
            Logger.getLogger(PatientWindowController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void populateReportRequestTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<DiagnosisTest>> loadData = new Task<List<DiagnosisTest>>() {
            @Override
            protected List<DiagnosisTest> call() throws Exception {
                return new MedicineDAO().getReportRequests();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                
                try {
                    infoTable.setItems(FXCollections.observableArrayList(loadData.getValue()));
                } catch (Exception e) {
                }
                
                String[] colName = {"Test ID", "Test Name", "Patient ID", "Refd By", "Prescription Date"};
                String[] property = {"id", "name", "patientID", "refdBy", "suggestionDate"};
                
                int i = 0;
                for (String col : colName) {
                    if (i == 0) {
                        TableColumn<DiagnosisTest, Integer> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    } else {
                        TableColumn<DiagnosisTest, String> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    }
                    
                    i++;
                }
                
            }
        });
        
        Thread th = new Thread(loadData);
        th.start();
    }
    
    private void populatePendingReportTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<DiagnosisTest>> loadData = new Task<List<DiagnosisTest>>() {
            @Override
            protected List<DiagnosisTest> call() throws Exception {
                return new MedicineDAO().getPendingReports();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                
                try {
                    infoTable.setItems(FXCollections.observableArrayList(loadData.getValue()));
                } catch (Exception e) {
                }
                
                String[] colName = {"Test ID", "Test Name", "Patient ID", "Date Of Performance"};
                String[] property = {"id", "name", "patientID", "dateOfPerformance"};
                
                int i = 0;
                for (String col : colName) {
                    if (i == 0) {
                        TableColumn<DiagnosisTest, Integer> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    } else {
                        TableColumn<DiagnosisTest, String> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    }
                    
                    i++;
                }
                
            }
        });
        
        Thread th = new Thread(loadData);
        th.start();
    }
    
    private void populateReportHistoryTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<DiagnosisTest>> loadData = new Task<List<DiagnosisTest>>() {
            @Override
            protected List<DiagnosisTest> call() throws Exception {
                return new MedicineDAO().getDeliveredReports();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                
                try {
                    infoTable.setItems(FXCollections.observableArrayList(loadData.getValue()));
                } catch (Exception e) {
                }
                
                String[] colName = {"Test ID", "Test Name", "Patient ID", "Date Of Delivery"};
                String[] property = {"id", "name", "patientID", "dateOfDelivery"};
                
                int i = 0;
                for (String col : colName) {
                    if (i == 0) {
                        TableColumn<DiagnosisTest, Integer> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    } else {
                        TableColumn<DiagnosisTest, String> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    }
                    
                    i++;
                }
                
            }
        });
        
        Thread th = new Thread(loadData);
        th.start();
    }
    
}
