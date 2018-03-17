/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.EmployeeDAO;
import Main.Constants;
import Model.Employee;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class NurseWindowController extends AnchorPane {

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
    
    ObservableList<String> mode = FXCollections.observableArrayList("Patients", "Schedule");
    
    @FXML
    void logout(ActionEvent event) throws IOException {
        Button bt = (Button) event.getSource();
        Stage curstage = (Stage) bt.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_FILEPATH + "/Homepage.fxml"));
        Scene scene = new Scene(root);
        curstage.setScene(scene);
    }

    @FXML
    void patient(ActionEvent event) {

    }

    @FXML
    void schedule(ActionEvent event) {

    }

    @FXML
    void viewDetails(ActionEvent event) {

    }

    NurseWindowController(String uId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/NurseWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getEmployeeInfo(uId);
            WindowTitle.setText(mode.get(0));
        } catch (IOException ex) {
            Logger.getLogger(NurseWindowController.class.getName()).log(Level.SEVERE, null, ex);
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

}
