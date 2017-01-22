package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javafx.event.Event;
/**
 * Created by Kasia on 2017-01-03.
 */

public class MainPanelController {

    @FXML Tab foremanTab;

    @FXML
    public void foremanOnSelectionChanged(Event event){
    }

    @FXML
    public void policemanOnSelectionChanged(){
    }

    @FXML
    public void ABWOnSelectionChanged(){
    }

    @FXML
    public void borderGuardsOnSelectionChanged(){
    }

}

