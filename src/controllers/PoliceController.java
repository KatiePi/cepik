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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.*;
import java.util.*;

public class PoliceController {


    public ObservableList<ObservableList> data;

    @FXML
    public TableView referralTV;


    public LinkedList<String> referralTVcolNames = new LinkedList<String>(Arrays.asList("Typ dokumentu","Numer VIN","Data wystawienia","Data ważności","Opis"));

    @FXML
    public Tab tab;

    MainPanelController mainController;
    public void setMainController(MainPanelController mainController) {
        this.mainController = mainController;
    }










    public void onMouseClicked() {
//        Tab referralTab = (Tab)event.getTarget();

       // if(referralTab.isSelected()) {

            referralTV.getColumns().clear();
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "admin");
                data = FXCollections.observableArrayList();
                Statement statement = connection.createStatement();
                //ResultSet rs = statement.executeQuery("SELECT v.ProductionYear, v.REgistrationNumber, v.VIN, m.Name FROM vehicle as v JOIN mark as m ON v.mark_idMark = m.idMark ");
                ResultSet rs = statement.executeQuery("SELECT dt.Name, v.VIN, d.startDate, d.ExpireDate, d.description FROM documents as d left JOIN vehicle as v ON d.vehicle_idVehicle = v.idVehicle left join documentType as dt on dt.idDocumentType = d.documentType_idDocumentType where dt.idDocumentType = 2");


                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    col.setText(referralTVcolNames.get(i));
                    referralTV.getColumns().addAll(col);
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
                referralTV.setItems(data);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }



        FXMLLoader mainFXMLLoader = new FXMLLoader(getClass().getResource("../resources/MainPanel.fxml"));
        System.out.println("Main fxmlloader tutaj: " + mainFXMLLoader.toString());
        System.out.println(mainFXMLLoader.<MainPanelController>getController());
    }
  //  }
    TabPane tabPane;


}
