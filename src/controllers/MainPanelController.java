package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.Event;
/**
 * Created by Kasia on 2017-01-03.
 */

public class MainPanelController {

    PoliceController policeController;
    public FXMLLoader policeFXMLLoader;

    private void initialize() {
        policeController.setMainController(this);
        policeFXMLLoader = new FXMLLoader(getClass().getResource("Police.fxml"));
    }

    @FXML
    void policeTabOnSelectionChanged(){
        System.out.println("DZIALA TYLKO GDY NACISNIESZ NA TAB");
        System.out.println("DZIALA TYLKO GDY NACISNIESZ NA TAB");
        System.out.println("DZIALA TYLKO GDY NACISNIESZ NA TAB");
      // policeController.onMouseClicked();
        System.out.println( policeController);

    }


    public void setMainController(PoliceController policeController) {
        this.policeController = policeController;
    }
}

