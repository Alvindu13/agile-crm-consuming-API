package com.agilecrm;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class GUIInterface extends Application {


    private Button buttonAllEventsCharged;
    private Button buttonEventsByDates;





    @Override
    public void start(Stage stage) {

        buttonAllEventsCharged = new Button("Charger tous les évènements");
        buttonEventsByDates = new Button("Générer le fichier excel");
        final LocalDate zeroDate = LocalDate.of(1970, 12, 1);
        final LocalDate infiniteDate = LocalDate.of(2040, 4, 25);


        DatePicker datePickerStart = new DatePicker();
        DatePicker datePickerEnd = new DatePicker();
        LocalDate ldt = LocalDate.now();

        VBox root = new VBox();
        Scene scene = new Scene(root, 600, 250);

        root.setSpacing(10);
        root.setPadding(new Insets(15,20, 10,10));

        datePickerStart.setValue(ldt);
        datePickerStart.setShowWeekNumbers(true);
        datePickerEnd.setValue(ldt);
        datePickerEnd.setShowWeekNumbers(true);

        //recup l'entree de l'utilisateur
        System.out.println(datePickerStart.getValue());
        System.out.println(datePickerEnd.getValue());

        Label labelStart = new Label("Début : ");
        Label labelEnd = new Label("Fin : ");

        root.getChildren().addAll(labelStart, datePickerStart);
        root.getChildren().addAll(labelEnd, datePickerEnd);
        root.getChildren().addAll(buttonEventsByDates);
        root.getChildren().add(addHBoxGenerateAllEvents());

        stage.setTitle("Agile Evenements");
        stage.setScene(scene);
        stage.show();

        buttonEventsByDates.setOnAction((event) -> {
            try {
                CreateExcelDemo.generateFile(datePickerStart.getValue(), datePickerEnd.getValue());
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        });

        buttonAllEventsCharged.setOnAction(event -> {
            try {
                System.out.println(zeroDate);
                System.out.println(infiniteDate);
                CreateExcelDemo.generateFile(zeroDate, infiniteDate);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        });

    }

    public void runInterfaceGUI(){
        // JavaFX should be initialized
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JFXPanel(); // initializes JavaFX environment
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Application.launch();
    }

    public HBox addHBoxGenerateAllEvents() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");
        buttonAllEventsCharged.setPrefSize(200, 20);
        buttonAllEventsCharged.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(buttonAllEventsCharged);
        return hbox;
    }


    /*public static void main(String[] args) {
        Application.launch(args);
    }*/
}
