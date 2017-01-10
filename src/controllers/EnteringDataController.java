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
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnteringDataController {
    @FXML TextField ownerNameTF;
    @FXML TextField ownerLastnameTF;
    @FXML TextField ownerPeselTF;
    @FXML TextField ownerCityTF;

    @FXML TextField vehicleProdYearTF;
    @FXML TextField vehicleRegNumTF;
    @FXML TextField vehicleMarkTF;
    @FXML TextField vehicleVinTF;

    @FXML TextField docStartDateTF;
    @FXML TextField docExpiryDateTF;
    @FXML TextField docDescTF;

    @FXML TextField ownershipStartDateTF;

    @FXML ComboBox docTypeCB;
    @FXML ComboBox docVehicleCB;
    @FXML ComboBox ownershipOwnerCB;
    @FXML ComboBox ownershipVehicleCB;

    private Map<Integer, String> documentTypes;
    private Map<Integer, String> vehicles;
    private Map<Integer, String> owners;
    private int vehicleMarkId = -1;


    @FXML
    void addOwnersClick(MouseEvent event) {
        String name = ownerNameTF.getText();
        String lastname = ownerLastnameTF.getText();
        String pesel = ownerPeselTF.getText();
        String city = ownerCityTF.getText();

        if(name != "" && lastname != "" && pesel != "" && city != "") {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "admin");
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO owner VALUES  (DEFAULT, '"+name+"', '"+lastname+"', "+pesel+", '"+city+"')");
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ownerNameTF.clear();
            ownerLastnameTF.clear();
            ownerPeselTF.clear();
            ownerCityTF.clear();
        }
    }

    @FXML
    void addVechilesClick(MouseEvent event) {
        String prodYear = vehicleProdYearTF.getText();
        String RegNum = vehicleRegNumTF.getText();
        String Mark = vehicleMarkTF.getText();
        String Vin = vehicleVinTF.getText();


        if(prodYear != "" && RegNum != "" && Mark != "" && Vin != "") {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "admin");
                Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery("select * from mark");
                while(result.next()){
                    if(result.getString("Name").equals(Mark))
                        vehicleMarkId = result.getInt("idMark");
                }

                System.out.print("MArkaaa is" + vehicleMarkId);

                statement.executeUpdate("INSERT INTO vehicle VALUES  (DEFAULT, '"+prodYear+"', '"+RegNum+"', "+vehicleMarkId+", '"+Vin+"')");
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            vehicleProdYearTF.clear();
            vehicleRegNumTF.clear();
            vehicleMarkTF.clear();
            vehicleVinTF.clear();
        }
    }

    @FXML
    void addDocumentsClick(MouseEvent event) {
        String docType = (String)docTypeCB.getValue();
        String vehicle = (String)docVehicleCB.getValue();
        String startDate = docStartDateTF.getText();
        String expiryDate = docExpiryDateTF.getText();
        String desc = docDescTF.getText();

        if(docType != "" && vehicle != "" && startDate != "" && expiryDate != "" && desc != "") {
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
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "admin");
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
        }
    }

    @FXML
    void addOwnershipsClick(MouseEvent event) {
        String owner = (String)ownershipOwnerCB.getValue();
        String vehicle = (String)ownershipVehicleCB.getValue();
        String startDate = ownershipStartDateTF.getText();

        if(owner != "" && vehicle != "" && startDate != "") {
            int ownerId = -1;
            int vehicleId = -1;

            for(Map.Entry<Integer, String> e : owners.entrySet()) {
                if(e.getValue() == owner)
                    ownerId = e.getKey();
            }

            for(Map.Entry<Integer, String> e : vehicles.entrySet()) {
                if(e.getValue() == vehicle)
                    vehicleId = e.getKey();
            }

            if(ownerId != -1 && vehicleId != -1) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "admin");
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("INSERT INTO ownership VALUES (DEFAULT, '"+startDate+"', + '', +  "+vehicleId+", "+ownerId+") ");
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            setOwnershipTabComboBoxes();

            ownershipStartDateTF.clear();
        }
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

    @FXML
    void ownershipTabSelected(Event event) {
        Tab ownershipTab = (Tab)event.getTarget();
        owners = new HashMap<>();
        vehicles = new HashMap<>();

        if(ownershipTab.isSelected()) {
            setOwnershipTabComboBoxes();
        }
    }

    void setDocumentsTabComboBoxes() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "admin");
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from documenttype");
            while(result.next())
                documentTypes.put(result.getInt("idDocumentType"), result.getString("Name"));

            result = statement.executeQuery("select * from vehicle");

            while(result.next())
                vehicles.put(result.getInt("idVehicle"), result.getString("VIN"));

            docTypeCB.getItems().clear();

            for(int i = 1; i <= documentTypes.size(); i++)
                docTypeCB.getItems().add(documentTypes.get(i));


            docVehicleCB.getItems().clear();

            for(int i = 1; i <= vehicles.size(); i++)
                docVehicleCB.getItems().add(vehicles.get(i));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setOwnershipTabComboBoxes() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "admin");
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from owner");

            while(result.next())
                owners.put(result.getInt("idOwner"), result.getString("Firstname") + " " + result.getString("Lastname") + " " + result.getString("Pesel") );

            result = statement.executeQuery("select * from vehicle");

            while(result.next())
                vehicles.put(result.getInt("idVehicle"), result.getString("VIN"));

            ownershipOwnerCB.getItems().clear();

            for(int i = 1; i <= owners.size(); i++)
                ownershipOwnerCB.getItems().add(owners.get(i));


            ownershipVehicleCB.getItems().clear();

            for(int i = 1; i <= vehicles.size(); i++)
                ownershipVehicleCB.getItems().add(vehicles.get(i));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
