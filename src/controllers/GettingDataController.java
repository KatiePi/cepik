package controllers;

/**
 * Created by Kasia on 2017-01-03.
 */
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.*;
import java.util.*;

public class GettingDataController {

    private ObservableList<ObservableList> data;

    @FXML
    private TableView ownerTV;
    @FXML
    private TableView vehicleTV;
    @FXML
    private TableView documentTV;
    @FXML
    private TableView ownershipTV;
    @FXML
    private TableView updateOwnerTV;
    @FXML
    private TableView updateVehicleTV;
    @FXML
    private TableView updateDocumentTV;
    @FXML
    private TableView updateOwnershipTV;

    private String user = "root";
    private String password = "admin";

    private LinkedList<String> ownerTVcolNames = new LinkedList<String>(Arrays.asList("Imię","Nazwisko","Pesel","Miasto"));
    private LinkedList<String> vehicleTVcolNames = new LinkedList<String>(Arrays.asList("Rok produkcji","Numer rejestracyjny","Numer VIN","Marka"));
    private LinkedList<String> documentTVcolNames = new LinkedList<String>(Arrays.asList("Typ dokumentu","Numer VIN","Data wystawienia","Data ważności","Opis"));
    private LinkedList<String> ownershipTVcolNames = new LinkedList<String>(Arrays.asList("Imię","Nazwisko","Numer Vin","Data wystawienia"));
    private LinkedList<String> updateOwnerTVcolNames = new LinkedList<String>(Arrays.asList("Imię","Nazwisko","Pesel","Miasto", "Numer identyfikacyjny ", "Data modyfikacji"));
    private LinkedList<String> updateVehicleTVcolNames = new LinkedList<String>(Arrays.asList("Numer rejestracyjny", "Rok produkcji" ,"VIN","Marka","Numer identyfikacyjny","Data modyfikacji"));
    private LinkedList<String> updateDocumentTVcolNames = new LinkedList<String>(Arrays.asList("Typ dokumentu", "Pojazd" ,"Numer identyfikacyjny","Opis","Data modyfikacji", "Data wystawienia", "Data wygaśnięcia"));
    private LinkedList<String> updateOwnershipTVcolNames = new LinkedList<String>(Arrays.asList("Nazwisko właściciela", "Imię właściciela" ,"Pojazd" ,"Numer identyfikacyjny","Data wystawienia", "Data modyfikacji"));
    @FXML
    void ownerTabSelected(Event event) {
        Tab ownerTab = (Tab)event.getTarget();

        if(ownerTab.isSelected()) {
            ownerTV.getColumns().clear();

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT firstname, lastname, pesel, city FROM owner ");

                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                  //  System.out.println("Hello " + col.getText());
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    col.setText(ownerTVcolNames.get(i));
                    ownerTV.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        //Iterate Column
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);

                }
                //FINALLY ADDED TO TableView
                ownerTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void vehicleTabSelected(Event event) {
        Tab vehicleTab = (Tab)event.getTarget();

        if(vehicleTab.isSelected()) {
            vehicleTV.getColumns().clear();
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT v.ProductionYear, v.REgistrationNumber, v.VIN, m.Name FROM vehicle as v JOIN mark as m ON v.mark_idMark = m.idMark ");
                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    col.setText(vehicleTVcolNames.get(i));
                    vehicleTV.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        //Iterate Column
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);

                }
                //FINALLY ADDED TO TableView
                vehicleTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void documentsTabSelected(Event event) {
        Tab documentsTab = (Tab)event.getTarget();

        if(documentsTab.isSelected()) {

            documentTV.getColumns().clear();

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT dt.Name, v.VIN, d.startDate, d.ExpireDate, d.description FROM documents as d left JOIN vehicle as v ON d.vehicle_idVehicle = v.idVehicle left join documentType as dt on dt.idDocumentType = d.documentType_idDocumentType");
                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    col.setText(documentTVcolNames.get(i));
                    documentTV.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        //Iterate Column
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);

                }
                //FINALLY ADDED TO TableView
                documentTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void ownershipTabSelected(Event event) {
        Tab ownershipTab = (Tab) event.getTarget();

        if (ownershipTab.isSelected()) {
            ownershipTV.getColumns().clear();

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT o.lastname, o.firstname, v.VIN, os.startDate from ownership as os join owner as o on os.Owner_idOwner = o.idOwner join vehicle as v on v.idVehicle = os.Vehicle_idVehicle");
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    col.setText(ownershipTVcolNames.get(i));
                    ownershipTV.getColumns().addAll(col);
                    System.out.println("Column [" + i + "] ");
                }

                while (rs.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    data.add(row);

                }
                //FINALLY ADDED TO TableView
                ownershipTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void updateOwnerTabSelected(Event event) {
        Tab ownerHistory = (Tab)event.getTarget();

        if(ownerHistory.isSelected()) {
            updateOwnerTV.getColumns().clear();

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT firstname, lastname, pesel, city, Owner_idOwner, DateOfUpdate FROM owner_history ");

                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    col.setText(updateOwnerTVcolNames.get(i));
                    updateOwnerTV.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);

                }
                updateOwnerTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void updateVehicleTabSelected(Event event) {
        Tab vehicleHistory = (Tab)event.getTarget();

        if(vehicleHistory.isSelected()) {
            updateVehicleTV.getColumns().clear();

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT v.REgistrationNumber, v.ProductionYear, v.VIN, m.Name, v.idVehicle_history,  v.DateOfUpdate FROM vehicle_history as v JOIN mark as m ON v.mark_idMark = m.idMark ");

                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    col.setText(updateVehicleTVcolNames.get(i));
                    updateVehicleTV.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);

                }
                updateVehicleTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void updateDocumentTabSelected(Event event) {
        Tab documentHistory = (Tab)event.getTarget();

        if(documentHistory.isSelected()) {
            updateDocumentTV.getColumns().clear();

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("    SELECT dt.Name, v.VIN, d.idDocuments_history, d.description, d.dateOfUpdate,d.startDate, d.ExpireDate " +
                                                         "FROM documents_history as d left JOIN vehicle as v ON d.vehicle_idVehicle = v.idVehicle " +
                                                        "left join documentType as dt on dt.idDocumentType = d.documentType_idDocumentType");

                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    col.setText(updateDocumentTVcolNames.get(i));
                    updateDocumentTV.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);

                }
                updateDocumentTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void updateOwnershipTabSelected(Event event) {
        Tab ownershipHistory = (Tab)event.getTarget();

        if(ownershipHistory.isSelected()) {
            updateOwnershipTV.getColumns().clear();

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", user, password);
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT o.lastname, o.firstname, v.VIN, os.idOwnership_history, os.startDate, os.dateOfUpdate " +
                                                         "from ownership_history as os join owner as o on os.Owner_idOwner = o.idOwner join vehicle as v on v.idVehicle = os.Vehicle_idVehicle");

                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                    col.setText(updateOwnershipTVcolNames.get(i));
                    updateOwnershipTV.getColumns().addAll(col);
                    System.out.println("Column ["+i+"] ");
                }

                while(rs.next()){
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                        row.add(rs.getString(i));
                        System.out.println(rs.getString(i));
                    }
                    System.out.println("Row [1] added "+row );
                    data.add(row);

                }
                updateOwnershipTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
