/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.InventoryDAO;
import Main.Constants;
import Model.Inventory;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author User
 */

public class AddInventoryController extends AnchorPane {

    @FXML
    private TableView infoTable;

    @FXML
    private TextField txtFld1;

    @FXML
    private TextField txtFld2;

    @FXML
    private TextField txtFld3;

    @FXML
    void add(ActionEvent event) {
        if (ambulanceMode) {
            Inventory m = new Inventory().ID(txtFld1.getText().trim()).driverName(txtFld2.getText().trim()).driverCntct(txtFld3.getText().trim());
            adlist.add(m);
            new Thread(()
                    -> {
                new InventoryDAO().addAmbulance(m);
            }).start();
            infoTable.setItems(adlist);

        } else {
            Inventory m = new Inventory().ID(txtFld1.getText().trim()).name(txtFld2.getText().trim()).cost(Integer.parseInt(txtFld3.getText().trim()));
            adlist.add(m);
            new Thread(()
                    -> {
                new InventoryDAO().addEquipment(m);
            }).start();
            infoTable.setItems(adlist);
        }
        txtFld1.clear();
        txtFld2.clear();
        txtFld3.clear();
    }

    
    boolean ambulanceMode;

    public AddInventoryController(boolean ambulanceMode) {
        this.ambulanceMode = ambulanceMode;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/AddInventory.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            setPromptText(ambulanceMode);
            populateInfoTable();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPromptText(boolean ambulanceMode) {
        if (ambulanceMode) {
            txtFld1.setPromptText("Ambulance ID");
            txtFld2.setPromptText("Driver Name");
            txtFld3.setPromptText("Contact No");
        } else {
            txtFld1.setPromptText("Equipment ID");
            txtFld2.setPromptText("Eqipment Name");
            txtFld3.setPromptText("Cost");
        }
    }

    ObservableList<Inventory> adlist;

    private void populateInfoTable() {
        infoTable.getItems().clear();
        infoTable.getColumns().clear();
        Task<List<Inventory>> loadData = new Task<List<Inventory>>() {
            @Override
            protected List<Inventory> call() throws Exception {
                if (ambulanceMode) {
                    return new InventoryDAO().getAmbulanceList();
                } else {
                    return new InventoryDAO().getEquipmentList();
                }

            }
        };
        loadData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent event) {

                try {
                    adlist = FXCollections.observableArrayList();
                    adlist.addAll(loadData.getValue());
                    infoTable.setItems(adlist);
                } catch (Exception ex) {
                }

                String[] colName;
                String[] property;

                if (ambulanceMode) {
                    colName = new String[]{"Ambulance ID", "Driver Name", "Contact No", "Buy Date"};
                    property = new String[]{"id", "driverName", "driverCntct", "buyDate"};
                } else {
                    colName = new String[]{"Equipment ID", "Equipment Name", "Buy Date", "Cost"};
                    property = new String[]{"id", "name", "buyDate", "cost"};
                }

                int i = 0;
                for (String col : colName) {
                    if (!ambulanceMode && i == 3) {
                        TableColumn<Inventory, Integer> col1 = new TableColumn<>(col);
                        col1.setCellValueFactory(new PropertyValueFactory<>(property[i]));
                        infoTable.getColumns().add(col1);
                    } else {
                        TableColumn<Inventory, String> col1 = new TableColumn<>(col);
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
}
