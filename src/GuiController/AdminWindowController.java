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
import Main.Constants;
import Model.Account;
import Model.Doctor;
import Model.Employee;
import Model.Location;
import Model.Surgery;
import Model.admission;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AdminWindowController extends AnchorPane {

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
    private AnchorPane registrationWindow;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private ComboBox<String> division;

    @FXML
    private ComboBox<String> district;

    @FXML
    private ComboBox<String> thana;

    @FXML
    private TextField salary;

    @FXML
    private JFXDatePicker birthDate;

    @FXML
    private JFXComboBox<String> bloodGroup;

    @FXML
    private JFXComboBox<String> bankName;

    @FXML
    private TextField acNo;

    @FXML
    private JFXComboBox<String> department;

    @FXML
    private Label errorMsgEmployee;

    @FXML
    private Tab DoctorRegiWindow;

    @FXML
    private TextField designation;

    @FXML
    private JFXComboBox<String> specialisation;

    @FXML
    private TextField qualification;

    @FXML
    private JFXComboBox<String> title;

    @FXML
    private Label errorMsgDoctor;
    @FXML
    private Button viewDetailsBt;

    @FXML
    private AnchorPane monitorHospitalWindow;

    ObservableList<String> mode = FXCollections.observableArrayList("Doctor", "Patient", "Other Employees", "Add Employees", "Monitor Hospital", "Operation Request");
    ObservableList<String> deptList = FXCollections.observableArrayList("Patient Registrar", "Nursing", "Pathologist", "Admin", "Doctor");
    ObservableList<String> bgroup = FXCollections.observableArrayList("A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-");
    ObservableList<String> bnkList = FXCollections.observableArrayList("Sonali Bank", "Agrani Bank", "Janata Bank", "Pubali Bank", "Rupali Bank");
    ObservableList<String> titleList = FXCollections.observableArrayList("Professor", "Doctor");
    Map<String, Set<String>> divisionToDistrict = new HashMap<>();
    Map<String, Set<String>> districtToThana = new HashMap<>();

    @FXML
    void addEmployees(ActionEvent event) {
        infoWindow.setVisible(false);
        monitorHospitalWindow.setVisible(false);
        registrationWindow.setVisible(true);
        WindowTitle.setText(mode.get(3));
        department.setItems(deptList);
        bloodGroup.setItems(bgroup);
        bankName.setItems(bnkList);
        DoctorRegiWindow.setDisable(true);
        prepareSpecialisation();

    }

    @FXML
    void operationRequest(ActionEvent event) {
        infoWindow.setVisible(true);
        monitorHospitalWindow.setVisible(false);
        registrationWindow.setVisible(false);
        WindowTitle.setText(mode.get(5));
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<Surgery>> loadData = new Task<List<Surgery>>() {
            @Override
            protected List<Surgery> call() throws Exception {
                return new MedicineDAO().getAllOperations();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<Surgery> surlist = FXCollections.observableArrayList();
                surlist.addAll(loadData.getValue());
                infoTable.setItems(surlist);

                String[] colName = {"Operation ID", "Surgery Name", "Requested By", "Patient ID"};
                String[] property = {"operationID", "name", "requestedBy", "patientID"};

                int i = 0;
                for (String col : colName) {
                    TableColumn<Surgery, String> col1 = new TableColumn<>(col);
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
    void ambulance(ActionEvent event) {
        Stage newStage = new Stage();
        newStage.setScene(new Scene(new AddInventoryController(true)));
        newStage.show();
    }

    @FXML
    void doctorInfo(ActionEvent event) {
        viewDetailsBt.setVisible(true);
        monitorHospitalWindow.setVisible(false);
        registrationWindow.setVisible(false);
        infoWindow.setVisible(true);
        WindowTitle.setText(mode.get(0));
        populateDoctorTable();
    }

    @FXML
    void employeesInfo(ActionEvent event) {
        viewDetailsBt.setVisible(true);
        monitorHospitalWindow.setVisible(false);
        registrationWindow.setVisible(false);
        infoWindow.setVisible(true);
        WindowTitle.setText(mode.get(2));
        populateEmployeeTable();
    }

    @FXML
    void enrollEmployee(ActionEvent event) {
        System.out.println("here in enroll ");
        Button bt = (Button) event.getSource();
        Stage curStage = (Stage) bt.getScene().getWindow();
        if (checkEdit()) {
            String df = DateTimeFormatter.ofPattern("dd-MMM-yy").format(birthDate.getValue());
            Stage newStage = new Stage();
            Scene scene;
            Employee e = new Employee().firstName(firstName.getText().trim()).lastName(lastName.getText().trim()).birthDate(df).bloodGroup(bloodGroup.getValue()).salary(Integer.parseInt(salary.getText().trim()));
            e = e.departmentName(department.getValue()).bankName(bankName.getValue()).bankACNo(acNo.getText().trim());
            String pass = getPassword();
            String id = new EmployeeDAO().addEmployee(e, null, new Location(division.getValue(), district.getValue(), thana.getValue()), new Account().password(pass), "N");
            scene = new Scene(new PopUpWindowController("Your Employee ID is " + id, "Temporary Password " + pass));

            newStage.setScene(scene);
            newStage.show();
        } else {
            errorMsgEmployee.setText("Field(s) not properly Filled");
        }
    }

    @FXML
    void enrollDoctor(ActionEvent event) {
        Button bt = (Button) event.getSource();
        Stage curStage = (Stage) bt.getScene().getWindow();
        if (checkEdit()) {
            String df = DateTimeFormatter.ofPattern("dd-MMM-yy").format(birthDate.getValue());
            Stage newStage = new Stage();
            Scene scene;
            Employee e = new Employee().firstName(firstName.getText().trim()).lastName(lastName.getText().trim()).birthDate(df).bloodGroup(bloodGroup.getValue()).salary(Integer.parseInt(salary.getText().trim()));
            e = e.departmentName(department.getValue()).bankName(bankName.getValue()).bankACNo(acNo.getText().trim());
            Doctor d = new Doctor().designation(designation.getText().trim()).qualification(qualification.getText().trim()).specialisation(specialisation.getValue()).title(title.getValue());
            String pass = getPassword();
            String id = new EmployeeDAO().addEmployee(e, d, new Location(division.getValue(), district.getValue(), thana.getValue()), new Account().password(pass), "D");
            scene = new Scene(new PopUpWindowController("Your Employee ID is " + id, "Temporary Password " + pass));

            newStage.setScene(scene);
            newStage.show();
        } else {
            errorMsgDoctor.setText("Field(s) not properly Filled");

        }
    }

    @FXML
    void equipment(ActionEvent event) {
        Stage newStage = new Stage();
        newStage.setScene(new Scene(new AddInventoryController(false)));
        newStage.show();
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
    void manageWard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_FILEPATH + "/WardManagement.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    @FXML
    void monitorHospital(ActionEvent event) {
        infoWindow.setVisible(false);
        registrationWindow.setVisible(false);
        monitorHospitalWindow.setVisible(true);
        WindowTitle.setText(mode.get(3));
    }

    @FXML
    void patientInfo(ActionEvent event) {
        monitorHospitalWindow.setVisible(false);
        registrationWindow.setVisible(false);
        infoWindow.setVisible(true);
        WindowTitle.setText(mode.get(1));
        populateIndoorTable();
        viewDetailsBt.setVisible(false);
    }

    

    @FXML
    void viewDetails(ActionEvent event) {
        Stage newStage = new Stage();
        if (!infoTable.getSelectionModel().isEmpty()) {
            if (WindowTitle.getText().equals(mode.get(0))) {
                Doctor d = (Doctor) infoTable.getSelectionModel().getSelectedItem();
                newStage.setScene(new Scene(new EmployeeDescriptionController(d.getUserID())));
            } else if (WindowTitle.getText().equals(mode.get(2))) {
                Employee d = (Employee) infoTable.getSelectionModel().getSelectedItem();
                newStage.setScene(new Scene(new EmployeeDescriptionController(d.getUserID())));
            } else if (WindowTitle.getText().equals(mode.get(5))) {
                Surgery d = (Surgery) infoTable.getSelectionModel().getSelectedItem();
                newStage.setScene(new Scene(new OperationController(d)));
            }
        }

        newStage.show();
    }

    private void populateDoctorTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        System.out.println("here");
        Task<List<Doctor>> loadData = new Task<List<Doctor>>() {
            @Override
            protected List<Doctor> call() throws Exception {
                return new DoctorDAO().getAllDoctorForAdmin();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<Doctor> doclist = FXCollections.observableArrayList();
                doclist.addAll(loadData.getValue());
                infoTable.setItems(doclist);

                String[] colName = {"Employee ID", "Name", "Designation", "Specilization"};
                String[] property = {"userID", "fullName", "designation", "specialisation"};

                int i = 0;
                for (String col : colName) {
                    TableColumn<Doctor, String> col1 = new TableColumn<>(col);
                    col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                    infoTable.getColumns().add(col1);
                    i++;
                }

            }
        });

        Thread th = new Thread(loadData);
        th.start();

    }

    public AdminWindowController(String userID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/AdminWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            populateDoctorTable();
            getEmployeeInfo(userID);
            prepareLocations();
            WindowTitle.setText(mode.get(0));

        } catch (IOException ex) {
            Logger.getLogger(AdminWindowController.class
                    .getName()).log(Level.SEVERE, null, ex);
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

    private void populateEmployeeTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<Employee>> loadData = new Task<List<Employee>>() {
            @Override
            protected List<Employee> call() throws Exception {
                return new EmployeeDAO().getAllEmployees();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                ObservableList<Employee> empList = FXCollections.observableArrayList();
                empList.addAll(loadData.getValue());

                infoTable.setItems(empList);

                String[] colName = {"Employee ID", "Name", "Department", "Hire Date", "Salary"};
                String[] property = {"userID", "fullName", "departmentName", "joinDate", "salary"};

                int i = 0;
                for (String col : colName) {
                    if (property[i].equals("salary")) {
                        System.out.println("in salry");
                        TableColumn<Employee, Integer> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    } else {
                        TableColumn<Employee, String> col1 = new TableColumn<>(col);
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

    private boolean checkEdit() {
        boolean success = false;
        success = firstName.getText().length() > 0 && lastName.getText().length() > 0 && !department.getSelectionModel().isEmpty() && !bloodGroup.getSelectionModel().isEmpty();
        success = !division.getSelectionModel().isEmpty() && !district.getSelectionModel().isEmpty() && !thana.getSelectionModel().isEmpty() && birthDate.getValue() != null;
        success = salary.getText().length() > 0 && !bankName.getSelectionModel().isEmpty() && acNo.getText().length() > 0;
        if (!department.getSelectionModel().isEmpty()) {
            if (department.getValue().equals(deptList.get(4))) {
                success = designation.getText().length() > 0 && !specialisation.getSelectionModel().isEmpty() && qualification.getText().length() > 0;
                success = !title.getSelectionModel().isEmpty();
                if (!success) {
                    errorMsgDoctor.setText("Field(s) Not Filled Properly");
                }
            }
        }

        if (!success && errorMsgEmployee.getText().length() == 0) {
            errorMsgEmployee.setText("Field(s) Not Filled Properly");
        }
        return success;
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
    void departmentCombo(ActionEvent event) {
        if (department.getValue().equals(deptList.get(4))) {
            title.setItems(titleList);
            DoctorRegiWindow.setDisable(false);
        } else {
            DoctorRegiWindow.setDisable(true);
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

    private void prepareSpecialisation() {

        Task<List<String>> getSpecialisation = new Task<List<String>>() {
            @Override
            protected List<String> call() throws Exception {
                return new DoctorDAO().getAllDepartments();
            }
        };
        getSpecialisation.setOnSucceeded((event) -> {

            List<String> dept = getSpecialisation.getValue();
            System.out.println(dept.size());
            specialisation.setItems(FXCollections.observableArrayList(dept));
        });
        Thread th = new Thread(getSpecialisation);
        th.start();
    }

    String getPassword() {
        int len = 7;
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String alphanum = upper + lower + digits;
        String pass = "";
        for (int i = 0; i < len; i++) {
            //pass.concat(alphanum[((int)(Math.random()*1000))]);
            pass += alphanum.charAt(((int) (Math.random() * 1000)) % alphanum.length());
        }
        return pass;
    }
}
