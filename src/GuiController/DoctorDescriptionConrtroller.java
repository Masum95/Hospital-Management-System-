/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.DoctorDAO;
import Main.Constants;
import Model.Doctor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class DoctorDescriptionConrtroller extends AnchorPane {

    @FXML
    private ImageView image;

    @FXML
    private Label name;

    @FXML
    private Label isOnLeave;

    @FXML
    private Label Designation;

    @FXML
    private Label qualification;

    @FXML
    private Label Department;
    Doctor d;

    DoctorDescriptionConrtroller(String empID) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/DoctorDescription.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Task getDoctor = new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println(empID);
                d = new DoctorDAO().getDoctorDetails(empID);
                d = d.employeeID(empID);
                System.out.println("here");
                System.out.println(d.toString());
                return null;

            }

        };

        getDoctor.setOnSucceeded((event) -> {
            name.setText(d.getFullName());
            //this.isOnLeave.setText(isOnLeave);
            Designation.setText(d.getDesignation());
            qualification.setText(d.getQualification());
            Department.setText(d.getSpecialisation());
            image.setImage(d.getImage());
        });

        Thread th = new Thread(getDoctor);
        th.start();
    }

    @FXML
    void makeAppointment(ActionEvent event) {
        Button bt = (Button) event.getSource();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(new MakeAppointmentController(d.getUserID())));
        newStage.show();
    }

    void newScene(String path, Node nd, String title) throws IOException {
        Stage stage = (Stage) nd.getScene().getWindow();
        stage.setTitle(title);
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
