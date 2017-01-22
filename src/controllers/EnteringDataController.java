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

public class EnteringDataController {
    @FXML private Label incorrectDataLabel3;
    @FXML private Label incorrectDataLabel2;
    @FXML private Label incorrectDataLabel1;
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

    @FXML TextField updateOwnerNameTF;
    @FXML TextField updateOwnerLastnameTF;
    @FXML TextField updateOwnerPeselTF;
    @FXML TextField updateOwnerCityTF;

    @FXML TextField updateProductionYearTF;
    @FXML TextField updateRegistrationNumberTF;
    @FXML TextField updateVINnumberTF;
    @FXML TextField updateMarkTF;

    @FXML TextField updateDocTypeTF;
    @FXML TextField updateDocStartDateTF;
    @FXML TextField updateDocExpireDateTF;
    @FXML TextField updateDocDescriptionTF;
    @FXML TextField updateDocVehicleTF;

    @FXML TextField ownershipStartDateTF;

    @FXML ComboBox docTypeCB;
    @FXML ComboBox docVehicleCB;
    @FXML ComboBox ownershipOwnerCB;
    @FXML ComboBox ownershipVehicleCB;
    @FXML ComboBox updateOwnerCB;
    @FXML ComboBox updateVehicleCB;
    @FXML ComboBox updateDocumentCB;
    @FXML ComboBox updateOwnershipCB;

    @FXML private Label incorrectDataLabel;

    private Map<Integer, String> documentTypes;
    private Map<Integer, String> vehicles;
    private HashMap<Integer, String> owners;
    private int vehicleMarkId = -1;
    private PeselValidator peselValidator;
    private VinValidator vinValidator;
    private DateValidator dateValidator;
    private DateValidator dateValidator2;
    private String user = "root";
    private String password = "admin";
    private int updateOwnerId;
    private int updateVehicleId;


    public void initialize() {
        // TODO Auto-generated method stub
        incorrectDataLabel.setVisible(false);
        incorrectDataLabel1.setVisible(false);
        incorrectDataLabel2.setVisible(false);
        incorrectDataLabel3.setVisible(false);
    }


    @FXML
    void addOwnersClick(MouseEvent event) {
        String name = ownerNameTF.getText();
        String lastname = ownerLastnameTF.getText();
        String pesel = ownerPeselTF.getText();
        peselValidator = new PeselValidator(pesel);
        System.out.println(peselValidator.isValid());
        String city = ownerCityTF.getText();

        if(name.isEmpty() == false && lastname.isEmpty() == false && peselValidator.isValid() == true && city.isEmpty() == false) {

            try {
                incorrectDataLabel.setVisible(false);
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",  user, password);
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
        else incorrectDataLabel.setVisible(true);
    }

    @FXML
    void addVechilesClick(MouseEvent event) {
        String prodYear = vehicleProdYearTF.getText();
        String RegNum = vehicleRegNumTF.getText();
        String Mark = vehicleMarkTF.getText();
        String Vin = vehicleVinTF.getText();
        vinValidator = new VinValidator(Vin);
        System.out.println(vinValidator.isValid());


        if(prodYear.isEmpty() != true && RegNum.isEmpty() != true && Mark.isEmpty() != true && vinValidator.isValid() == true) {
            try {
                incorrectDataLabel1.setVisible(false);
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
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
        }        else incorrectDataLabel1.setVisible(true);

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
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("INSERT INTO documents VALUES (DEFAULT, "+docTypeId+", '"+expiryDate+"', '"+startDate+"', '"+desc+"', "+vehicleId+")");
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
    void addOwnershipsClick(MouseEvent event) {
        String owner = (String)ownershipOwnerCB.getValue();
        String vehicle = (String)ownershipVehicleCB.getValue();
        String startDate = ownershipStartDateTF.getText();
        dateValidator = new DateValidator(startDate);
        if(owner.isEmpty() != true && vehicle.isEmpty() != true && dateValidator.isValid() == true) {
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
                    incorrectDataLabel3.setVisible(false);
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("INSERT INTO ownership VALUES (DEFAULT, '"+startDate+"', +  "+vehicleId+", "+ownerId+") ");
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            setOwnershipTabComboBoxes();

            ownershipStartDateTF.clear();
        }        else incorrectDataLabel3.setVisible(true);

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

    @FXML
    void updateOwnerTabSelected(Event event) {
        Tab updateOwnerTab = (Tab)event.getTarget();
        owners = new HashMap<>();

        if(updateOwnerTab.isSelected()) {
            setUpdateOwnerComboBoxes();
        }
    }

    @FXML
    void updateVehicleTabSelected(Event event) {
        Tab updateVehicleTab = (Tab)event.getTarget();
        vehicles = new HashMap<>();

        if(updateVehicleTab.isSelected()) {
            setUpdateVehicleComboBoxes();
        }
    }

    @FXML
    void updateDocumentTabSelected(Event event) {
        /*Tab updateVehicleTab = (Tab)event.getTarget();
        vehicles = new HashMap<>();

        if(updateVehicleTab.isSelected()) {
            setUpdateVehicleComboBoxes();
        }*/
    }

    @FXML
    void updateOwnershipTabSelected(Event event) {
        /*Tab updateVehicleTab = (Tab)event.getTarget();
        vehicles = new HashMap<>();

        if(updateVehicleTab.isSelected()) {
            setUpdateVehicleComboBoxes();
        }*/
    }

    void setDocumentsTabComboBoxes() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from documenttype");
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

    void setOwnershipTabComboBoxes() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from owner");
            while(result.next())
                owners.put(result.getInt("idOwner"), result.getString("Firstname") + " " + result.getString("Lastname") + " " + result.getString("Pesel"));

            result = statement.executeQuery("select * from vehicle");

            while(result.next())
                vehicles.put(result.getInt("idVehicle"), result.getString("VIN"));

            ownershipOwnerCB.getItems().clear();
            for (int key : owners.keySet())
                ownershipOwnerCB.getItems().add(owners.get(key));

            ownershipVehicleCB.getItems().clear();

    //        for(int i = 1; i <= vehicles.size(); i++)
    //            ownershipVehicleCB.getItems().add(vehicles.get(i));

            for (int key : vehicles.keySet())
                ownershipVehicleCB.getItems().add(vehicles.get(key));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpdateOwnerComboBoxes(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from owner");
            while(result.next())
                owners.put(result.getInt("idOwner"), result.getString("Firstname") + ' ' +  result.getString("Lastname") + ' ' + result.getString("City") + ' ' + result.getString("Pesel"));

            updateOwnerCB.getItems().clear();
            for (int key : owners.keySet())
                updateOwnerCB.getItems().add(owners.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setDataOwnerLabels(){
        updateOwnerId = -1;
        String owner = (String)updateOwnerCB.getValue();
        for(Map.Entry<Integer, String> e : owners.entrySet()) {
            if(e.getValue() == owner)
                updateOwnerId = e.getKey();
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from owner where owner.idOwner = " + updateOwnerId);
            while(result.next()){
                updateOwnerNameTF.setText(result.getString("Firstname"));
                updateOwnerLastnameTF.setText(result.getString("Lastname"));
                updateOwnerPeselTF.setText(result.getString("PESEL"));
                updateOwnerCityTF.setText(result.getString("City"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateOwnerOnMouseClicked(MouseEvent event){
        String name = updateOwnerNameTF.getText();
        String lastname = updateOwnerLastnameTF.getText();
        String pesel = updateOwnerPeselTF.getText();
        peselValidator = new PeselValidator(pesel);
        String city = updateOwnerCityTF.getText();
        if(name.isEmpty() == false && lastname.isEmpty() == false && peselValidator.isValid() == true && city.isEmpty() == false) {

            try {
                incorrectDataLabel.setVisible(false);
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",  user, password);
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE owner set firstname ='"+name+"', lastname ='"+lastname+"', PESEL ="+pesel+", city ='"+city+ "' where idOwner =" + updateOwnerId);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateOwnerNameTF.clear();
            updateOwnerLastnameTF.clear();
            updateOwnerPeselTF.clear();
            updateOwnerCityTF.clear();
        }
        else incorrectDataLabel.setVisible(true);
        setUpdateOwnerComboBoxes();
    }

    @FXML
    public void updateOwnerCBOnAction(){
        setDataOwnerLabels();
    }

    public void setUpdateVehicleComboBoxes(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from vehicle");

            while(result.next())
                vehicles.put(result.getInt("idVehicle"), result.getString("RegistrationNumber") + ' ' +  result.getString("VIN") +  result.getString("ProductionYear"));

            updateVehicleCB.getItems().clear();
            for (int key : vehicles.keySet())
                updateVehicleCB.getItems().add(vehicles.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setDataVehicleLabels(){
        updateVehicleId = -1;
        String vehicle = (String)updateVehicleCB.getValue();
        for(Map.Entry<Integer, String> e : vehicles.entrySet()) {
            if(e.getValue() == vehicle)
                updateVehicleId = e.getKey();
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("select * from vehicle where vehicle.idVehicle =" + updateVehicleId);
            while(result.next()){
                updateProductionYearTF.setText(result.getString("ProductionYear"));
                updateRegistrationNumberTF.setText(result.getString("RegistrationNumber"));
                updateVINnumberTF.setText(result.getString("VIN"));
                updateMarkTF.setText(result.getString("Mark"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateVehicleButtonOnAction(MouseEvent event){
        String productionYear = updateProductionYearTF.getText();
        String registrationNumber = updateRegistrationNumberTF.getText();
        String vin = updateVINnumberTF.getText();
        vinValidator = new VinValidator(vin);
        String mark = updateMarkTF.getText();
        if(productionYear.isEmpty() == false && registrationNumber.isEmpty() == false && vinValidator.isValid() == true && mark.isEmpty() == false) {

            try {
                incorrectDataLabel.setVisible(false);
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",  user, password);
                Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery("select * from mark");
                while(result.next()){
                    if(result.getString("Name").equals(mark))
                        vehicleMarkId = result.getInt("idMark");
                }

                statement.executeUpdate("UPDATE vehicle set productionYear ='"+productionYear+"', registrationNumber ='"+registrationNumber+"', vin ="+vin+", mark ='"+vehicleMarkId+ "' where idVehicle =" + updateVehicleId);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateProductionYearTF.clear();
            updateRegistrationNumberTF.clear();
            updateVINnumberTF.clear();
            updateMarkTF.clear();
        }
        else incorrectDataLabel.setVisible(true);
        setUpdateVehicleComboBoxes();
    }

    @FXML
    public void updateVehicleCBOnAction(){
        setDataVehicleLabels();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setUpdateDocumentComboBoxes(){
       /* try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from vehicle");
            while(result.next())
                vehicles.put(result.getInt("idVehicle"), result.getString("RegistrationNumber") + ' ' +  result.getString("VIN") + ' ' + result.getString("Mark") + ' ' + result.getString("ProductionYear"));

            updateVehicleCB.getItems().clear();
            for (int key : vehicles.keySet())
                updateVehicleCB.getItems().add(vehicles.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setDataDocumentLabels(){
        updateVehicleId = -1;
        String vehicle = (String)updateVehicleCB.getValue();
        for(Map.Entry<Integer, String> e : vehicles.entrySet()) {
            if(e.getValue() == vehicle)
                updateVehicleId = e.getKey();
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from vehicle where vehicle.idVehicle = " + updateVehicleId);
            while(result.next()){
                updateProductionYearTF.setText(result.getString("ProductionYear"));
                updateRegistrationNumberTF.setText(result.getString("RegistrationNumber"));
                updateVINnumberTF.setText(result.getString("VIN"));
                updateMarkTF.setText(result.getString("Mark"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void updateDocumentButtonOnAction(MouseEvent event){
       /* String productionYear = updateProductionYearTF.getText();
        String registrationNumber = updateRegistrationNumberTF.getText();
        String vin = updateVINnumberTF.getText();
        vinValidator = new VinValidator(vin);
        String mark = updateMarkTF.getText();
        if(productionYear.isEmpty() == false && registrationNumber.isEmpty() == false && vinValidator.isValid() == true && mark.isEmpty() == false) {

            try {
                incorrectDataLabel.setVisible(false);
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",  user, password);
                Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery("select * from mark");
                while(result.next()){
                    if(result.getString("Name").equals(mark))
                        vehicleMarkId = result.getInt("idMark");
                }

                statement.executeUpdate("UPDATE vehicle set productionYear ='"+productionYear+"', registrationNumber ='"+registrationNumber+"', vin ="+vin+", mark ='"+vehicleMarkId+ "' where idVehicle =" + updateVehicleId);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateProductionYearTF.clear();
            updateRegistrationNumberTF.clear();
            updateVINnumberTF.clear();
            updateMarkTF.clear();
        }
        else incorrectDataLabel.setVisible(true);
        setUpdateVehicleComboBoxes();*/
    }

    @FXML
    public void updateDocumentCBOnAction(){
        //setDataDocumentLabels();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setUpdateOwnershipComboBoxes(){
       /* try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from vehicle");
            while(result.next())
                vehicles.put(result.getInt("idVehicle"), result.getString("RegistrationNumber") + ' ' +  result.getString("VIN") + ' ' + result.getString("Mark") + ' ' + result.getString("ProductionYear"));

            updateVehicleCB.getItems().clear();
            for (int key : vehicles.keySet())
                updateVehicleCB.getItems().add(vehicles.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    public void setDataOwnershipLabels(){
       /* updateVehicleId = -1;
        String vehicle = (String)updateVehicleCB.getValue();
        for(Map.Entry<Integer, String> e : vehicles.entrySet()) {
            if(e.getValue() == vehicle)
                updateVehicleId = e.getKey();
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from vehicle where vehicle.idVehicle = " + updateVehicleId);
            while(result.next()){
                updateProductionYearTF.setText(result.getString("ProductionYear"));
                updateRegistrationNumberTF.setText(result.getString("RegistrationNumber"));
                updateVINnumberTF.setText(result.getString("VIN"));
                updateMarkTF.setText(result.getString("Mark"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void updateOwnershipButtonOnAction(MouseEvent event){
       /* String productionYear = updateProductionYearTF.getText();
        String registrationNumber = updateRegistrationNumberTF.getText();
        String vin = updateVINnumberTF.getText();
        vinValidator = new VinValidator(vin);
        String mark = updateMarkTF.getText();
        if(productionYear.isEmpty() == false && registrationNumber.isEmpty() == false && vinValidator.isValid() == true && mark.isEmpty() == false) {

            try {
                incorrectDataLabel.setVisible(false);
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",  user, password);
                Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery("select * from mark");
                while(result.next()){
                    if(result.getString("Name").equals(mark))
                        vehicleMarkId = result.getInt("idMark");
                }

                statement.executeUpdate("UPDATE vehicle set productionYear ='"+productionYear+"', registrationNumber ='"+registrationNumber+"', vin ="+vin+", mark ='"+vehicleMarkId+ "' where idVehicle =" + updateVehicleId);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateProductionYearTF.clear();
            updateRegistrationNumberTF.clear();
            updateVINnumberTF.clear();
            updateMarkTF.clear();
        }
        else incorrectDataLabel.setVisible(true);
        setUpdateVehicleComboBoxes();*/
    }

    @FXML
    public void updateOwnershipCBOnAction(){
       // setDataOwnershipLabels();
    }
}
