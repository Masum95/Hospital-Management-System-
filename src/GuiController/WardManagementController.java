/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.InventoryDAO;
import DAO.SeatDAO;
import Model.Inventory;
import Model.SeatInfo;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author User
 */
public class WardManagementController implements Initializable {
    
    @FXML
    private TextField price;
    
    @FXML
    private Label roomType;
    
    @FXML
    private TableView infoTable;
    @FXML
    private TableColumn<SeatInfo, String> roomTypeColm;
    
    @FXML
    private TableColumn<SeatInfo, Integer> priceColm;
    
    @FXML
    void update(ActionEvent event) {
        SeatInfo m = ((SeatInfo) infoTable.getSelectionModel().getSelectedItem()).price(Integer.parseInt(price.getText().trim()));
        Task updt = new Task() {
            @Override
            protected Object call() throws Exception {
                new SeatDAO().updateRoomCost(m);
                return null;
            }
        };
        updt.setOnSucceeded((e) -> {
            populateInfoTable();
        });
        new Thread(updt).start();
    }
    
    ObservableList<SeatInfo> adlist;
    
    private void populateInfoTable() {
        infoTable.getItems().clear();
        Task<List<SeatInfo>> loadData = new Task<List<SeatInfo>>() {
            @Override
            protected List<SeatInfo> call() throws Exception {
                
                return new InventoryDAO().getRoomPriceList();
            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent event) {
                
                try {
                    adlist = FXCollections.observableArrayList();
                    adlist.addAll(loadData.getValue());
                    System.out.println(adlist.size());
                    infoTable.setItems(adlist);
                } catch (Exception ex) {
                }
                
                String[] property = new String[]{"roomType", "price"};
                
                roomTypeColm.setCellValueFactory(new PropertyValueFactory<>(property[0]));
                priceColm.setCellValueFactory(new PropertyValueFactory<>(property[1]));
            }
            
        });
        
        Thread th = new Thread(loadData);
        
        th.start();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateInfoTable();
        infoTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                SeatInfo s = (SeatInfo) infoTable.getSelectionModel().getSelectedItem();
                roomType.setText(s.getRoomType());
                price.setText(Integer.toString(s.getPrice()));
            }
        });
    }
    
}
