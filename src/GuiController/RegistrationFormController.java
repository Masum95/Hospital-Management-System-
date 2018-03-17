/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.AccountDAO;
import DAO.PatientDAO;
import Model.Account;
import Model.Location;
import Model.Patient;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class RegistrationFormController implements Initializable {

    @FXML
    private JFXComboBox<String> accountAs;

    @FXML
    private Label errorMsg;

    @FXML
    private AnchorPane patientWindow;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField pateintUserName;

    @FXML
    private ComboBox<String> division;

    @FXML
    private ComboBox<String> district;

    @FXML
    private ComboBox<String> thana;

    @FXML
    private JFXDatePicker birthDate;

    @FXML
    private Button patientImage;

    @FXML
    private Label patImageName;
    @FXML
    private TextField patientEmail;
    @FXML
    private JFXComboBox<String> bloodGroup;

    @FXML
    private PasswordField patientConfPassword;

    @FXML
    private PasswordField patientPassword;

    @FXML
    private AnchorPane employeeWindow;

    @FXML
    private TextField empID;

    @FXML
    private TextField empUserName;

    @FXML
    private PasswordField empPrevPassword;
    
    @FXML
    private PasswordField empPassword;

    @FXML
    private PasswordField empConfPassword;

    @FXML
    private Button empImage;

    @FXML
    private Label empImgName;

    @FXML
    private TextField empEmail;

    private boolean isPatient;
    Image img;
    ObservableList<String> acntAs = FXCollections.observableArrayList("Patient", "Employee");
    ObservableList<String> bgroup = FXCollections.observableArrayList("A+", "B+","AB+","O+", "A-","B-","AB-","O-" );
    Map<String, Set<String>> divisionToDistrict = new HashMap<>();
    Map<String, Set<String>> districtToThana = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountAs.setItems(acntAs);
        bloodGroup.setItems(bgroup);
        accountAs.getSelectionModel().selectFirst();
        isPatient = true;
        prepareLocations();

    }

    @FXML
    void register(ActionEvent event) throws IOException {
        Button bt = (Button) event.getSource();
        Stage curStage = (Stage) bt.getScene().getWindow();

        if (checkEdit()) {
            
            Stage newStage = new Stage();
            Scene scene;

            if (isPatient) {
                String df = DateTimeFormatter.ofPattern("dd-MMM-yy").format(birthDate.getValue());
                Patient pt = new Patient().birthDate(df).bloodGroup(bloodGroup.getValue()).firstName(firstName.getText().trim()).lastName(lastName.getText().trim());
                Account ac = new Account().email(patientEmail.getText().trim()).password(patientPassword.getText().trim()).userName(pateintUserName.getText().trim()).image(img);

                String id = new PatientDAO().addPatient(pt, ac,new Location(division.getValue(), district.getValue(), thana.getValue()));
                scene = new Scene(new PopUpWindowController( "Your Paitent ID is", id));

            } else {
                Account ac = new Account().email(empEmail.getText().trim()).password(empPassword.getText().trim()).userID(empID.getText().trim()).userName(empUserName.getText().trim()).image(img).prevPassword(empPrevPassword.getText());
                String success = new AccountDAO(false).createEmployeeAccount(ac);
                scene = new Scene(new PopUpWindowController( success, "" ));
            }

            newStage.setScene(scene);
            newStage.show();
        }
    }

    private boolean checkEdit() {
        boolean success = false;
        if (isPatient) {
            success = firstName.getText().length() > 0 && lastName.getText().length() > 0 && patientPassword.getText().length() > 0 && patientConfPassword.getText().length() > 0;
            success = !division.getSelectionModel().isEmpty() && !district.getSelectionModel().isEmpty() && !thana.getSelectionModel().isEmpty() && birthDate.getValue() != null;
            success = patImageName.getText().length() > 0 && patientEmail.getText().length() > 0;

            if (!patientConfPassword.getText().trim().equals(patientPassword.getText().trim())) {
                errorMsg.setText("Password Doesn't Match");
                success = false;
            } else {
                errorMsg.setText("");
            }
        } else {
            success = empID.getText().length() > 0 && empUserName.getText().length() > 0 && empPassword.getText().length() > 0 && empConfPassword.getText().length() > 0 &&  empPrevPassword.getText().length() > 0;
            success = empImgName.getText().length() > 0 && empEmail.getText().length() > 0;

            if (!empPassword.getText().trim().equals(empConfPassword.getText().trim())) {
                errorMsg.setText("Password Doesn't Match");
                success = false;
            } else {
                errorMsg.setText("");
            }
        }

        if (!success && errorMsg.getText().length() == 0) {
            errorMsg.setText("Field(s) Not Filled Properly");
        }
        return success;
    }

    @FXML
    void ImageUpload(ActionEvent event) {
        FileChooser fc = new FileChooser();
        List<String> imgExtension = new ArrayList<>();
        imgExtension.addAll(Arrays.asList("*.jpg", "*jpeg", "*.png", "*.bmp"));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", imgExtension));
        File file = fc.showOpenDialog(null);

        String url = file.getAbsolutePath();
        try {
            img = new Image(new FileInputStream(new File(url)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RegistrationFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (isPatient) {
            patImageName.setText(file.getName());
        } else {
            empImgName.setText(file.getName());
        }

    }

    @FXML
    void districtCombo(ActionEvent event) {
        thana.getItems().clear();
        if (!district.getSelectionModel().isEmpty()) {
            thana.setItems(FXCollections.observableArrayList(districtToThana.get(district.getValue())));
        }

    }

    @FXML
    void divisionCombo(ActionEvent event) {
        district.getItems().clear();
        district.setItems(FXCollections.observableArrayList(divisionToDistrict.get(division.getValue())));
    }

    @FXML
    void comboChanged(ActionEvent event) {

        if (accountAs.getValue().equals("Patient")) {
            System.out.println(accountAs.getValue());
            isPatient = true;
            employeeWindow.setVisible(false);
            patientWindow.setVisible(true);
        } else {
            System.out.println(accountAs.getValue());
            isPatient = false;
            patientWindow.setVisible(false);
            employeeWindow.setVisible(true);
        }

    }

    Set<String> div = new HashSet<>();

    private void prepareLocations() {
        divisionToDistrict = new HashMap<>();
        districtToThana = new HashMap<>();

        Task<List<Location>> getLocation = new Task<List<Location>>() {
            @Override
            protected List<Location> call() throws Exception {
                return Location.getAllLocations();
            }
        };
        getLocation.setOnSucceeded((event) -> {

            List<Location> loc = getLocation.getValue();
            for (Location l : loc) {
                div.add(l.getDivision());
                if (!divisionToDistrict.containsKey(l.getDivision())) {
                    divisionToDistrict.put(l.getDivision(), new HashSet<String>());
                }
                divisionToDistrict.get(l.getDivision()).add(l.getDistrict());

                if (!districtToThana.containsKey(l.getDistrict())) {
                    districtToThana.put(l.getDistrict(), new HashSet<>());
                }
                districtToThana.get(l.getDistrict()).add(l.getThana());
            }
            division.setItems(FXCollections.observableArrayList(div));
        });
        Thread th = new Thread(getLocation);
        th.start();
    }

}
