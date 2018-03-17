/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.AdmissionDAO;
import Main.Constants;
import Model.admission;
import com.gluonhq.impl.charm.a.b.b.p;
import java.util.Date;
import java.text.SimpleDateFormat;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class IndoorBillingController extends AnchorPane {

    @FXML
    private Label roomCharge;

    @FXML
    private Label doctorCharge;

    @FXML
    private Label treatCharge;

    @FXML
    private Label advance;

    @FXML
    private Label surgeryCharge;

    @FXML
    private Label due;

    @FXML
    private Label releaseDate;

    @FXML
    private Label admitID;

    @FXML
    private Label stayDuration;

    @FXML
    void paid(ActionEvent event) {
        new Thread(() -> {
            new AdmissionDAO().releasePatient(adID);
        }).start();
        Button bt = (Button) event.getSource();
        Stage curStage = (Stage) bt.getScene().getWindow();
        curStage.close();
    }

    String adID;
    public IndoorBillingController(String adID) {
        this.adID = adID;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/IndoorBilling.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            setTexts(adID);
            releaseDate.setText(new SimpleDateFormat("dd-MMM-yyyy ").format(new Date()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setTexts(String adID) {
        Task<admission> getInfo = new Task<admission>() {
            @Override
            protected admission call() throws Exception {
                return new AdmissionDAO().getReleasePatientDetails(adID);
            }

        };
        getInfo.setOnSucceeded((event) -> {
            admission ad = getInfo.getValue();
            admitID.setText(adID);
            stayDuration.setText(Integer.toString(ad.getDurationOfStay()) +" Days" );
            stayDuration.setText(Integer.toString(ad.getDurationOfStay()));
            roomCharge.setText(Integer.toString(ad.getRoomCharge()));
            doctorCharge.setText(Integer.toString(ad.getDrCharge()));
            treatCharge.setText(Integer.toString(ad.getTreatCharge()));
            surgeryCharge.setText(Integer.toString(ad.getSurgeryCost()));
            advance.setText(Integer.toString(ad.getAdvancePaid()));
            due.setText(Integer.toString(ad.getDue()));

        });
        Thread th = new Thread(getInfo);
        th.start();

    }

}
