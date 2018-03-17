/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GuiController;

import DAO.EmployeeDAO;
import Main.Constants;
import Model.Employee;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class EmployeeDescriptionController extends AnchorPane {

    @FXML
    private Label fullName;
    @FXML
    private Label joinDate;
    @FXML
    private Label curSalary;

    @FXML
    private JFXTextField salary;

    @FXML
    private Label empID;

    @FXML
    void remove(ActionEvent event) {

    }

    int stringToInt(String txt) {
        try {
            return Integer.parseInt(txt.trim());
        } catch (Exception ex) {
            return Integer.MIN_VALUE;
        }
    }

    @FXML
    void update(ActionEvent event) {
        if (stringToInt(salary.getText()) != Integer.MIN_VALUE) {
            int sal = stringToInt(salary.getText());
            Task<String> incSal = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return  new EmployeeDAO().increaseSalary(e.getUserID(),e.getSalary() + sal);
                }
            };
            incSal.setOnSucceeded((ev) -> {
                 
                 String ret = incSal.getValue();
                 if(ret.length()>0)
                 {
                     salary.clear();
                     getEmployeeInfo(e.getUserID());
                 }
                 
            });
                    
            new Thread(incSal).start();
            
        }
    }

    public EmployeeDescriptionController(String empID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_FILEPATH + "/EmployeeDescription.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            getEmployeeInfo(empID);
        } catch (IOException ex) {
        }
    }
    Employee e;

    private void getEmployeeInfo(String userID) {
        Task<Employee> getEmp = new Task<Employee>() {
            @Override
            protected Employee call() throws Exception {
                return new EmployeeDAO().getEmployee(userID);
            }
        };
        getEmp.setOnSucceeded((event) -> {
            e = getEmp.getValue();
            fullName.setText(e.getFullName());
            joinDate.setText(e.getJoinDate());
            empID.setText(e.getUserID());
            curSalary.setText(Integer.toString(e.getSalary()));
        });
        Thread th = new Thread(getEmp);

        th.start();
    }

}
