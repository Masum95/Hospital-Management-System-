/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.DoctorDAO;
import DAO.MedicineDAO;
import DAO.PatientDAO;
import Main.Constants;
import Model.DiagnosisTest;
import Model.Doctor;
import Model.Medicine;
import Model.Patient;
import Model.Surgery;
import Model.admission;
import Model.prescriptionContent;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author User
 */
public class EditPrescriptionController extends AnchorPane {

    @FXML
    private Text doctorName;

    @FXML
    private Text qualification;

    @FXML
    private Text date;

    @FXML
    private Label patientName;

    @FXML
    private Label patientID;

    @FXML
    private Label age;

    @FXML
    private TextField pulse;

    @FXML
    private TextField bpLow;

    @FXML
    private TextField bpHigh;

    @FXML
    private TextField glucoseLevel;

    @FXML
    private TextField weight;

    @FXML
    private TextField medicineName;

    @FXML
    private TextField morning;

    @FXML
    private TextField noon;

    @FXML
    private TextField night;

    @FXML
    private TextField comment;

    @FXML
    private TextField upto;

    @FXML
    private TableView<Medicine> medicineTable;

    @FXML
    private TableColumn<Medicine, String> medicineNameCol;

    @FXML
    private TableColumn<Medicine, String> dosageInstructionCol;

    @FXML
    private TableColumn<Medicine, String> dosageCmntsCol;

    @FXML
    private TableColumn<String, Integer> drugUptoCol;

    @FXML
    private TextField diagnosisTestName;

    @FXML
    private TableView<DiagnosisTest> diagnosisTable;

    @FXML
    private TableColumn<DiagnosisTest, String> diagnosisTestNameCol;

    @FXML
    private TableColumn<DiagnosisTest, Integer> diagnosisTestIDCol;

    @FXML
    private TableColumn<DiagnosisTest, Integer> diagnosisTestCostCol;

    @FXML
    private TableView<Surgery> surgeryTable;

    @FXML
    private TableColumn<Surgery, String> surgeryNameCol;

    @FXML
    private TableColumn<Surgery, Integer> surgeryIDCol;

    @FXML
    private TableColumn<Surgery, String> surgeryDeptCol;

    @FXML
    private TextField surgeryName;

    @FXML
    private GridPane medicineGrid;
    @FXML
    private GridPane testGrid;
    @FXML
    private GridPane surgeryGrid;
    @FXML
    private Button history;

    ObservableList<DiagnosisTest> prescribedDiagnosisTests;
    
    private int txtFldToInt(String num)
    {
        if(num.length()==0) return -1;
        else return Integer.parseInt(num.trim());
    }
    private String intToTxtfld(int num)
    {
        if(num==-1) return "" ;
        else return Integer.toString(num);
    }
    @FXML
    void addDiagnosisTest(ActionEvent event) {
        DiagnosisTest d = new DiagnosisTest().testName(diagnosisTestName.getText().trim());
        d = d.testID(testNameToTest.get(d.getName()).getId()).cost(testNameToTest.get(d.getName()).getCost());
        prescribedDiagnosisTests.add(d);
        diagnosisTable.setItems(prescribedDiagnosisTests);
        diagnosisTestName.clear();
    }

    ObservableList<Medicine> prescribedMedicines;

    @FXML
    void addMedicine(ActionEvent event) {
        Medicine m = new Medicine().brandName(medicineName.getText().trim()).comments(comment.getText().trim()).upto(Integer.parseInt(upto.getText().trim()));
        String dosage = morning.getText().trim() + "+" + noon.getText().trim() + "+" + night.getText().trim();
        m = m.MedicineID(medicineNameToID.get(m.getBrandName())).dosageInstruction(dosage);
        prescribedMedicines.add(m);
        medicineTable.setItems(prescribedMedicines);
        medicineName.clear();
        comment.clear();
        upto.clear();
        morning.clear();
        noon.clear();
        night.clear();
    }

    ObservableList<Surgery> prescribedSurgerys;

    @FXML
    void addSurgery(ActionEvent event) {
        Surgery s = new Surgery().surgeryName(surgeryName.getText().trim());
        s = s.surgeryID(surgeryNameToSurgery.get(s.getName()).getId()).department(surgeryNameToSurgery.get(s.getName()).getDept());

        prescribedSurgerys.add(s);
        surgeryTable.setItems(prescribedSurgerys);
        surgeryName.clear();
    }

    @FXML
    void showPatientHistory(ActionEvent event) {
        Stage newStage = new Stage();
        newStage.setScene(new Scene(new PatientWindowController(ad.getPatientID(), true)));
        newStage.show();
    }

    @FXML
    void viewSurgeryDetails(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        final prescriptionContent p = new prescriptionContent().admissionID(ad.getAdmissionId()).bpHigh(txtFldToInt(bpHigh.getText())).bpLow(txtFldToInt(bpLow.getText())).
                dateOfPrescription(new SimpleDateFormat("dd-MMM-yyyy ").format(new Date())).doctorID(ad.getDoctorID()).patientID(ad.getPatientID())
                .glucose(txtFldToInt(glucoseLevel.getText())).pulse(txtFldToInt(pulse.getText())).weight(txtFldToInt(weight.getText()));
        final List<Medicine> medilist = new ArrayList<>(prescribedMedicines);
        final List<DiagnosisTest> testlist = new ArrayList<>(prescribedDiagnosisTests);
        final List<Surgery> surlist = new ArrayList<>(prescribedSurgerys);

        Task<Integer> getPresID = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return new MedicineDAO().addPrescriptionContent(p);
            }
        };
        getPresID.setOnSucceeded((e) -> {
            int id = getPresID.getValue();
            new Thread(() -> {
                new MedicineDAO().addMedicineContent(id, medilist);
            }).start();
            new Thread(() -> {
                new MedicineDAO().addSurgeryContent(p.getAdmitID(), surlist);
            }).start();
            new Thread(() -> {
                new MedicineDAO().addDiagnosisContent(id, testlist);
            }).start();
        });
        new Thread(getPresID).start();
        Button bt = (Button) event.getSource();
        Stage curStage = (Stage) bt.getScene().getWindow();
        curStage.close();

    }

    admission ad;

    public EditPrescriptionController(admission ad) {
        this.ad = ad;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/EditPrescription.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getEmployeeInfo(ad.getDoctorID());

            getPatientInfo(ad.getPatientID());
            prepareMedicineSuggestion();
            prepareDiagnosisSuggestion();
            prepareSurgerySuggestion();
            date.setText(new SimpleDateFormat("dd-MMM-yyyy ").format(new Date()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getEmployeeInfo(String userID) {
        Task<Doctor> getEmp = new Task<Doctor>() {
            @Override
            protected Doctor call() throws Exception {
                return new DoctorDAO().getDoctorDetails(userID);
            }
        };
        getEmp.setOnSucceeded((event) -> {
            Doctor e = getEmp.getValue();
            System.out.println(e.getFullName());
            qualification.setText(e.getQualification());
            doctorName.setText(e.getFullName());
        });
        Thread th = new Thread(getEmp);

        th.start();
    }

    private void getPatientInfo(String patID) {
        System.out.println("----++" + ad.getPatientID());
        Task<Patient> getPat = new Task<Patient>() {
            @Override
            protected Patient call() throws Exception {
                return new PatientDAO().getPatientDetails(patID);
            }
        };
        getPat.setOnSucceeded((event) -> {
            Patient p = getPat.getValue();
            age.setText(Integer.toString(p.getAge()) + " Years");
            patientID.setText(p.getUserID());
            patientName.setText(p.getFirstName() + " " + p.getLastName());

        });
        Thread th = new Thread(getPat);
        th.start();
    }

    Map<String, Integer> medicineNameToID;

    private void prepareMedicineSuggestion() {
        Task<List<Medicine>> getMed = new Task<List<Medicine>>() {
            @Override
            protected List<Medicine> call() throws Exception {
                return new MedicineDAO().getAllMedicine();
            }

        };
        getMed.setOnSucceeded((event) -> {
            List<Medicine> medlist = getMed.getValue();
            List<String> medNameList = new ArrayList<>();
            medicineNameToID = new HashMap<>();
            for (Medicine m : medlist) {
                medNameList.add(m.getBrandName());
                medicineNameToID.put(m.getBrandName(), m.getId());
            }
            TextFields.bindAutoCompletion(medicineName, medNameList);
            prescribedMedicines = FXCollections.observableArrayList();

            medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("brandName"));
            dosageInstructionCol.setCellValueFactory(new PropertyValueFactory<>("dosageInstruction"));
            dosageCmntsCol.setCellValueFactory(new PropertyValueFactory<>("comments"));
            drugUptoCol.setCellValueFactory(new PropertyValueFactory<>("upto"));

        });
        Thread th = new Thread(getMed);
        th.start();
    }

    Map<String, DiagnosisTest> testNameToTest;

    private void prepareDiagnosisSuggestion() {
        Task<List<DiagnosisTest>> getTest = new Task<List<DiagnosisTest>>() {
            @Override
            protected List<DiagnosisTest> call() throws Exception {
                return new MedicineDAO().getAllDiagnosisTests();
            }
        };
        getTest.setOnSucceeded((event) -> {
            List<DiagnosisTest> testList = getTest.getValue();
            List<String> testNameList = new ArrayList<>();
            testNameToTest = new HashMap<>();
            for (DiagnosisTest m : testList) {
                testNameList.add(m.getName());
                testNameToTest.put(m.getName(), m);
            }
            TextFields.bindAutoCompletion(diagnosisTestName, testNameList);
            prescribedDiagnosisTests = FXCollections.observableArrayList();

            diagnosisTestNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            diagnosisTestIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            diagnosisTestCostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));

        });
        Thread th = new Thread(getTest);
        th.start();
    }

    Map<String, Surgery> surgeryNameToSurgery;

    private void prepareSurgerySuggestion() {
        Task<List<Surgery>> getSurgery = new Task<List<Surgery>>() {
            @Override
            protected List<Surgery> call() throws Exception {
                return new MedicineDAO().getAllSurgerys();
            }
        };
        getSurgery.setOnSucceeded((event) -> {
            List<Surgery> surList = getSurgery.getValue();
            List<String> surNameList = new ArrayList<>();
            surgeryNameToSurgery = new HashMap<>();
            for (Surgery s : surList) {
                surNameList.add(s.getName());
                surgeryNameToSurgery.put(s.getName(), s);
            }
            TextFields.bindAutoCompletion(surgeryName, surNameList);
            prescribedSurgerys = FXCollections.observableArrayList();

            surgeryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            surgeryIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            surgeryDeptCol.setCellValueFactory(new PropertyValueFactory<>("dept"));

        });
        Thread th = new Thread(getSurgery);
        th.start();
    }

    public EditPrescriptionController(admission ad, boolean viewMode) {
        this.ad = ad;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/EditPrescription.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getEmployeeInfo(ad.getDoctorID());

            date.setText(ad.getAppointmentDate());
            medicineGrid.setVisible(false);
            surgeryGrid.setVisible(false);
            testGrid.setVisible(false);
            history.setVisible(false);
            getPrescriptionContent();
        } catch (Exception ex) {
        }
    }

    private void prepareViewMode(prescriptionContent p) {
        try {
            bpHigh.setText(intToTxtfld(p.getBphigh()));
            bpLow.setText(intToTxtfld(p.getBplow()));
            weight.setText(intToTxtfld(p.getWeight()));
            pulse.setText(intToTxtfld(p.getPulse()));
            glucoseLevel.setText(intToTxtfld(p.getGlucose()));
        } catch (Exception ex) {
        }

        bpHigh.setEditable(false);
        bpLow.setEditable(false);
        pulse.setEditable(false);
        weight.setEditable(false);
        history.setVisible(false);
        glucoseLevel.setEditable(false);

    }

    private void getPrescriptionContent() {
        Task<prescriptionContent> getPres = new Task<prescriptionContent>() {
            @Override
            protected prescriptionContent call() throws Exception {
                return new MedicineDAO().getPrescription(ad.getAdmissionId());
            }
        };
        getPres.setOnSucceeded((WorkerStateEvent event) -> {
            prescriptionContent p = getPres.getValue();
            try {
                getPatientInfo(p.getPatientID());
            } catch (Exception ex) {
            }

            // ------------- prepare Medicine list
            try {
                prescribedMedicines = FXCollections.observableArrayList(p.getMedList());
                medicineTable.setItems(prescribedMedicines);
            } catch (Exception ex) {
            }
            
            // ------------- prepare Diagnosis TestList
            try {
                prescribedDiagnosisTests = FXCollections.observableArrayList(p.getTestList());
                diagnosisTable.setItems(prescribedDiagnosisTests);
            } catch (Exception ex) {
            }
            try {
                // prepare Surgery List
                prescribedSurgerys = FXCollections.observableArrayList(p.getSurgreytList());
                surgeryTable.setItems(prescribedSurgerys);
            } catch (Exception ex) {
            }
            medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("brandName"));
            dosageInstructionCol.setCellValueFactory(new PropertyValueFactory<>("dosageInstruction"));
            dosageCmntsCol.setCellValueFactory(new PropertyValueFactory<>("comments"));
            drugUptoCol.setCellValueFactory(new PropertyValueFactory<>("upto"));

            diagnosisTestNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            diagnosisTestIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            diagnosisTestCostCol.setCellValueFactory(new PropertyValueFactory<>("cost"));

            surgeryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            surgeryIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            surgeryDeptCol.setCellValueFactory(new PropertyValueFactory<>("dept"));
            prepareViewMode(p);

        });

        new Thread(getPres).start();

    }

}
