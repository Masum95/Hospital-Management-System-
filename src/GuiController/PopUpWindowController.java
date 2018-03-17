/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import Main.Constants;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PopUpWindowController extends AnchorPane{

    @FXML
    private Text descriptionOf;

    @FXML
    private Text description;  


    PopUpWindowController(String descriptionOf, String description) {
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/PopUpWindow.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
        }
        this.descriptionOf.setText(descriptionOf); 
        this.description.setText(description);
    }
    @FXML
    void close(ActionEvent event) {
        Button bt = (Button) event.getSource();
        Stage stage = (Stage) bt.getScene().getWindow();
        stage.close();
    }
    
    
    
}
