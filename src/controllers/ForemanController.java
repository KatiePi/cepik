package controllers;

/**
 * Created by Kasia on 2017-01-03.
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.DateValidator;
import utils.PeselValidator;
import utils.VinValidator;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ForemanController {
    @FXML private Label incorrectDataLabel3;
    @FXML private Label incorrectDataLabel2;
    @FXML private Label incorrectDataLabel1;


    @FXML TextField docStartDateTF;
    @FXML TextField docExpiryDateTF;
    @FXML TextField docDescTF;

    @FXML TextField ownershipStartDateTF;

    @FXML ComboBox docTypeCB;
    @FXML ComboBox docVehicleCB;
    @FXML ComboBox ownershipOwnerCB;
    @FXML ComboBox ownershipVehicleCB;
    @FXML
    private Label incorrectDataLabel;

    private Map<Integer, String> documentTypes;
    private Map<Integer, String> vehicles;
    private HashMap<Integer, String> owners;
    private int vehicleMarkId = -1;

    private VinValidator vinValidator;
    private DateValidator dateValidator;
    private DateValidator dateValidator2;


    public void initialize() {
        // TODO Auto-generated method stub

        incorrectDataLabel2.setVisible(false);

    }



    @FXML
    void addDocumentsClick(MouseEvent event) {
        String docType = (String)docTypeCB.getValue();
        String vehicle = (String)docVehicleCB.getValue();
        String startDate = docStartDateTF.getText();
        String expiryDate = docExpiryDateTF.getText();
        String desc = docDescTF.getText();

        dateValidator = new DateValidator(startDate);
        dateValidator2 = new DateValidator(expiryDate);
        if(docType.isEmpty() != true && vehicle.isEmpty() != true && dateValidator.isValid() == true && dateValidator2.isValid()==true && desc.isEmpty() != true) {
            int docTypeId = -1;
            int vehicleId = -1;

            for(Map.Entry<Integer, String> e : documentTypes.entrySet()) {
                if(e.getValue() == docType)
                    docTypeId = e.getKey();
            }

            for(Map.Entry<Integer, String> e : vehicles.entrySet()) {
                if(e.getValue() == vehicle)
                    vehicleId = e.getKey();
            }

            if(docTypeId != -1 && vehicleId != -1) {
                try {
                    incorrectDataLabel2.setVisible(false);
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "newuser", "rozPass_123.");
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("INSERT INTO documents VALUES (DEFAULT, "+docTypeId+", '"+expiryDate+"', '"+startDate+"', '', '"+desc+"', "+vehicleId+")");
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            setDocumentsTabComboBoxes();

            docStartDateTF.clear();
            docExpiryDateTF.clear();
            docDescTF.clear();
        }        else incorrectDataLabel2.setVisible(true);

    }


    @FXML
    void documentsTabSelected(Event event) {
        Tab documentsTab = (Tab)event.getTarget();
        documentTypes = new HashMap<>();
        vehicles = new HashMap<>();

        if(documentsTab.isSelected()) {
            setDocumentsTabComboBoxes();
        }
    }



    void setDocumentsTabComboBoxes() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "newuser", "rozPass_123.");
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from documenttype where documenttype.name='Skierowanie na badanie techniczne'");
            while(result.next())
                documentTypes.put(result.getInt("idDocumentType"), result.getString("Name"));

            result = statement.executeQuery("select * from vehicle");

            while(result.next())
                vehicles.put(result.getInt("idVehicle"), result.getString("VIN"));

            docTypeCB.getItems().clear();

//            for(int i = 1; i <= documentTypes.size(); i++)
//                docTypeCB.getItems().add(documentTypes.get(i));
//
            for (int key : documentTypes.keySet())
                docTypeCB.getItems().add(documentTypes.get(key));


            docVehicleCB.getItems().clear();



            for (int key : vehicles.keySet())
                docVehicleCB.getItems().add(vehicles.get(key));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
