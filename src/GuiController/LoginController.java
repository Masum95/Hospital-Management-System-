/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.AccountDAO;
import Main.Constants;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class LoginController extends AnchorPane {

    Stage prevStage;
    String loginMode;

    @FXML
    private TextField userID;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Label loginStatus;

    List<String> department = Arrays.asList("Patient Registrar", "Nursing", "Pathologist", "Admin", "Doctor");

    public LoginController(Stage prevStage, String loginMode) {
        this.prevStage = prevStage;
        this.loginMode = loginMode;
        System.out.println(loginMode);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/Login.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            loginStatus.setText("");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        String uName = userName.getText().trim();
        String pass = password.getText().trim();
        String uId = userID.getText().trim();
        if (checkEdit(uName, pass, uId)) {
            Button bt = (Button) event.getSource();
            Stage curStage = (Stage) bt.getScene().getWindow();
            try {
                if (loginMode.equalsIgnoreCase("Patient")) {
                    String ret = new AccountDAO(true).userValidation(uId, uName, pass);
                    if (ret.length() > 0) {
                        loginStatus.setText("Login Success");
                        Scene scene = new Scene(new PatientWindowController(uId));
                        prevStage.setScene(scene);
                        
                        curStage.close();
                    } else {
                        loginStatus.setText("Login Failed");
                    }

                } else {
                    String ret = new AccountDAO(false).userValidation(uId, uName, pass);
                    if (ret.length() > 0) {
                        Scene scene = null;
                        if (ret.equalsIgnoreCase(department.get(0))) {
                            scene = new Scene(new PatientRegistrarWindowController(uId));

                        } else if (ret.equalsIgnoreCase(department.get(1))) {

                            scene = new Scene(new NurseWindowController(uId));

                        } else if (ret.equalsIgnoreCase(department.get(2))) {

                            scene = new Scene(new PathologyWindowController(uId));

                        } else if (ret.equalsIgnoreCase(department.get(3))) {

                            scene = new Scene(new AdminWindowController(uId));

                        } else if (ret.equalsIgnoreCase(department.get(4))) {
                            scene = new Scene(new DoctorWindowController(uId));
                        }

                        curStage.close();
                        prevStage.setScene(scene);
                    } else {
                        loginStatus.setText("Login Failed");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            loginStatus.setText("Field(s) not Filled");
        }

    }

    @FXML
    void register(ActionEvent event) throws IOException {
        Button bt = (Button) event.getSource();
        Stage curStage = (Stage) bt.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_FILEPATH + "/RegistrationForm.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        curStage.close();
    }

    void newScene(String path, Node nd, String title) throws IOException {
        Stage stage = (Stage) nd.getScene().getWindow();
        stage.setTitle(title);
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    private boolean checkEdit(String uName, String pass, String uId) {
        return uName.length() > 0 && pass.length() > 0 && uId.length() > 0;
    }

}
