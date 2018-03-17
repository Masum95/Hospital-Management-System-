/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.AccountDAO;
import DAO.AdmissionDAO;
import Main.Constants;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Masum
 */
public class MakeAppointmentController extends AnchorPane {

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private Text serial;

    @FXML
    private JFXButton appointment;

    @FXML
    private TextField patientID;

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    @FXML
    void makeAppointment(ActionEvent event) throws SQLException {
        String ret = new AccountDAO(true).userValidation(patientID.getText().trim(), username.getText().trim(), password.getText().trim());

        if (ret.length() > 0) {
            String df = DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(datePicker.getValue());
            Task<String> getSl = new Task< String>() {
                @Override
                protected String call() throws Exception {
                    return new AdmissionDAO().makeAppointment(docID, df, patientID.getText().trim());
                }

            };
            getSl.setOnSucceeded((e) -> {
                Stage newStage = new Stage();
                Scene scene = new Scene(new PopUpWindowController(getSl.getValue(), ""));
                newStage.setScene(scene);
                newStage.show();
            });
            Thread th = new Thread(getSl);
            th.start();
        } else {
            Stage newStage = new Stage();
            Scene scene = new Scene(new PopUpWindowController("Invalid Userid or Password", ""));
            newStage.setScene(scene);
            newStage.show();
        }
    }

    @FXML
    void dateSelected(ActionEvent event) {
        String df = DateTimeFormatter.ofPattern("dd-MMM-yyyy").format(datePicker.getValue());
        Task<String> getSl = new Task< String>() {
            @Override
            protected String call() throws Exception {
                return new AdmissionDAO().getSerialNo("E10105", df);
            }

        };
        getSl.setOnSucceeded((e) -> {
            serial.setText(getSl.getValue());
        });
        Thread th = new Thread(getSl);
        th.start();
    }

    String docID;

    public MakeAppointmentController(String docID) {
        this.docID = docID;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/MakeAppointment.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    }

}
