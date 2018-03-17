/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.DoctorDAO;
import Model.Doctor;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class HomepageController implements Initializable {

    @FXML
    private Button doctor;

    @FXML
    private Button patientRegistrar;


    @FXML
    private Button patient;

    @FXML
    private Button admin;

    @FXML
    private Button pathologist;

    @FXML
    private Accordion physicianListAccordion;

    @FXML
    private Label emergencyNumber;

    @FXML
    private Label ambulanceNumber;

    Map<String, String> nameToID;
    List<Doctor> docList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
        emergencyNumber.setText("017-123456" );
        ambulanceNumber.setText("015-123456" );
    }

    void preparePhysicanList() {
        // Problems : how to select if there are duplicate values in listview
        nameToID = new HashMap<>();

        Map<String, List<String>> deptList = new HashMap<>(); // For grouping by doctos deptWise

        for (Doctor d : docList) {

            if (!deptList.containsKey(d.getSpecialisation())) {
                deptList.put(d.getSpecialisation(), new ArrayList<String>());
            }
            deptList.get(d.getSpecialisation()).add(d.getFullName());
            //System.out.println(d.getFullName() );
            nameToID.put(d.getFullName(), d.getUserID());
            //System.out.println(nameToID.get(d.getFullName()));
        }
        System.out.println(nameToID.get("Prof. (Dr.) Ninan Chacko" ));
        List<TitledPane> tp = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, List<String>> tr : deptList.entrySet()) {
            ListView<String> doc = new ListView<>();
            doc.getItems().addAll(tr.getValue());
            TitledPane t = new TitledPane(tr.getKey(), doc);
            t.setMinHeight(100);
            tp.add(t);
        }
        physicianListAccordion.getPanes().addAll(tp);
    }

    @FXML
    void login(ActionEvent event) {
        Button bt = (Button) event.getSource();
        Stage stage = new Stage();
        Stage curStage = (Stage) bt.getScene().getWindow();
        Scene scene = new Scene(new LoginController(curStage, bt.getText()));
        System.out.println(scene.toString());
        stage.setScene(scene);
        stage.setTitle("Login Window");
        stage.show();
    }

    @FXML
    void viewPhysicianDetails(ActionEvent event) {
        ListView<String> nameList = (ListView<String>) physicianListAccordion.getExpandedPane().getContent();
        
        final String empID = nameToID.get(nameList.getSelectionModel().getSelectedItem());
        System.out.println(nameList.getSelectionModel().getSelectedItem());
        System.out.println(empID);
        Stage stage = new Stage();
        Scene scene = new Scene(new DoctorDescriptionConrtroller(empID));
        stage.setScene(scene);
        stage.show();
    }

    void newScene(String path, Node nd, String title) throws IOException {
        Stage stage = (Stage) nd.getScene().getWindow();
        stage.setTitle(title);
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
