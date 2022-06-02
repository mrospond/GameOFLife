/*
    Klasa obsługująca widok aplikacji, jest kontrolerem widoku main-view.fxml
    Obsługuje działanie przycisków oraz współpracuje z kontrolerem modelu, aby widok zawsze odzwierciedlał stan modelu
 */

package com.gol.gameoflife.view;

import com.gol.gameoflife.controller.Controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;
import javafx.util.Duration;

//import javafx.event.ActionEvent;

public  class ViewController{

    protected Controller controller;

    private Boolean isRunning = false;

    private Timeline timeline = new Timeline();

    private int genNumber = 0;

    private double simulationSpeed = 0.5;

    ConfirmBox confirmBox = new ConfirmBox();

    @FXML
    private VBox rootPane;

    @FXML
    private Button playButton;

    @FXML
    private Button resetButton;

    @FXML
    private Label genNumberLabel;

    @FXML
    private GridPane boardPane;

    @FXML
    private Button resizeButton;

    @FXML
    private TextField sizeInput;

    @FXML
    private Button saveFileButton;

    @FXML
    private Button readFileButton;

    @FXML
    private Slider slider;


    /*
        Funkcja obsługująca zachowanie suwaka odpowiedzialnego za czas symulacji
     */

    @FXML
     void sliderDragged(MouseEvent event) {// MouseEvent by default
        class Actions{
            void action() {
                simulationSpeed = 0.1 * slider.getValue();

                if (isRunning) {
                    timeline.stop();
                    isRunning = false;
                    playButton.setText("Stop");
                    play();
                }
//                System.out.println(simulationSpeed);
            }
        }
        Actions a = new Actions();

        slider.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            a.action();
        });
        slider.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            a.action();
        });
    }


    /*
        Wyświetla popup z możliwością wybrou pliku do wczytania
        Funkcja wywoływana przez readFileButton
     */


    @FXML
    public void readFile(){

        Popup popup = new Popup();

        // popupBox to dodatkowy kontener, który umożliwia odpowiednie ułożenie węzłów-dzieci w popupie

        VBox popupBox = new VBox(0);
        popupBox.setAlignment(Pos.CENTER);

        Button loadButton = new Button("Load");
        Button exitButton = new Button("Exit");

        // Utworzenie combo boxa z listą dostępnych plików

        ComboBox<String> fileList = new ComboBox<String>();
        fileList.setValue("Choose a file");
        String [] fileNames = controller.getFileHandler().listFiles();

        for(int i = 0; i < fileNames.length; i++){
            fileList.getItems().add(i, fileNames[i]);
        }


        // Ustawia położenie popupu w miejscu przycisku wczytywania pliku

        Bounds boundsInScreen = readFileButton.localToScreen(readFileButton.getBoundsInLocal());
        double layoutX = boundsInScreen.getMinX();
        double layoutY = boundsInScreen.getMinY();

        // Style przycisków i tła

        loadButton.prefWidthProperty().bind(fileList.widthProperty());
        exitButton.prefWidthProperty().bind(fileList.widthProperty());
        popupBox.setBackground(new Background( new BackgroundFill(Paint.valueOf("GAINSBORO"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Dodaje węzły-dzieci do popupui wyświetla je

        popupBox.getChildren().addAll(fileList, loadButton, exitButton);

        popup.getContent().addAll(popupBox);

        popup.show(saveFileButton.getScene().getWindow(),layoutX,layoutY);

        // Obsługa przycisków Load i Exit

        loadButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if(fileList.getValue() != "Choose a file") {
                boardPane.getChildren().clear();
                controller.readConfig(fileList.getValue());
                drawBoard();
                popup.hide();
                sizeInput.setText(String.valueOf(controller.getSimulation().getBoard().getWidth()));
                timeline.stop();
                isRunning = false;
                playButton.setText("Play");
                genNumber = 0;
                genNumberLabel.setText("Generation: " + String.valueOf(genNumber));
            }

        });

        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            popup.hide();
        });


    }


    /*
        Wyświetla popup umożliwiający zapis pliku
        Funckja wywoływana przez saveFileButton
     */
    @FXML
    private void saveFile(){
        Popup popup = new Popup();

        // Ustawia położenie popupu w miejscu przycisku zapisu pliku

        Bounds boundsInScreen = saveFileButton.localToScreen(saveFileButton.getBoundsInLocal());
        double layoutX = boundsInScreen.getMinX();
        double layoutY = boundsInScreen.getMinY();


        // popupBox to dodatkowy kontener, który umożliwia odpowiednie ułożenie węzłów-dzieci w popupie

        HBox popupBox = new HBox(5);

        // Utworzenie węzłów-dzieci popupu

        TextField input = new TextField();
        input.setPrefSize(100,-1);
        Button saveBtn = new Button("Save");
        Button exitBtn = new Button("Exit");
        Label inputName = new Label();
        inputName.setText("File name: ");


        popupBox.setPrefSize(250, saveFileButton.getHeight());
        popupBox.setAlignment(Pos.CENTER);
        popupBox.setBackground(new Background( new BackgroundFill(Paint.valueOf("GAINSBORO"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Dodanie węzłów-dzieci do popupu oraz ich wyświetlanie

        popupBox.getChildren().addAll(inputName, input, saveBtn, exitBtn);
        popup.getContent().addAll(popupBox);

        popup.show(saveFileButton.getScene().getWindow(),layoutX,layoutY);

        saveBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            if(input.getText().trim().equals("")) {
                AlertBox.display("Alert!", "Podana nazwa pliku jest niepoprawna!");
            }
            else if (controller.getFileHandler().alreadyExists(input.getText())) {
                confirmBox.display("Alert!", "   Plik o podanej nazwie już istnieje. Czy chcesz go nadpisać?   ");
                if (confirmBox.getAnswer()) { // confirmBox.getAnswer() == true
                    controller.saveConfig(input.getText());
                    popup.hide();
                }
            }
            else {
                controller.saveConfig(input.getText());
                popup.hide();
            }
        });

        exitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            popup.hide();
        });

        readFileButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            popup.hide();
        });


    }

    // Funkcja do poprania następnego stanu symulacji i jego wyświetlenia

    private void nextStep(){
        controller.nextStep();
        drawBoard();
        genNumber++;
        genNumberLabel.setText("Generation: " + String.valueOf(genNumber));
    }


    /*
     Funkcja rozpoczynająca ciągłą symulację następnych stanów gry
     Funkcja wywoływana przez playButton
     */

    @FXML
    private void play(){
        if (!isRunning) {
            playButton.setText("Stop");

            /* Co pół sekundy pobierany i wyświetlany jest następny krok symulacji
               Ilość cykli INDEFINITE zapewnia ciągłe działanie, aż do momentu przerwania poprzez naciśnięcie przycisku stop
            */

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(simulationSpeed), e -> nextStep())
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
            isRunning = true;

        } else {
            playButton.setText("Play");
            timeline.stop();
            isRunning = false;
            boardPane.getChildren().clear();
            drawBoard();
        }

    }


    /*
      Resetuje symulację - wszystkie pola stają się martwe
      Funkcja wywoływana przez przycisk resetButton
    */

    @FXML
    private void reset(){
        boardPane.getChildren().clear();
        controller.resetSimulation();
        drawBoard();
        genNumber = 0;
        genNumberLabel.setText("Generation: " + String.valueOf(genNumber));
        if(isRunning) {
            isRunning = false;
            playButton.setText("Play");
            timeline.stop();
        }
    }

    //Funkcja do zmiany rozmiaru planszy, wywoływana przez resizeButton

    @FXML
    private void resizeBoard(){

        int currentSize = controller.getSimulation().getBoard().getWidth();
        int size = Integer.valueOf(sizeInput.getText());
        if(size != currentSize) {
            if(size > 0 && size < 51) {
                isRunning = false;
                playButton.setText("Play");
                timeline.stop();
                boardPane.getChildren().clear();
                controller.resizeSimulation(size, size);
                drawBoard();
                sizeInput.setPromptText(String.valueOf(size));
            }
            else {
                AlertBox.display("Alert!", "Wprowadź wartość 1-50 !!!");
            }
        }
    }


    public void setModelController(Controller controller) {
        this.controller = controller;
    }

    // Funckja do tworzenia planszy, korzysta z funckji addField() do dodania kolejnych pól

    public void drawBoard() {
        boardPane.getChildren().clear();
        int rows = controller.getSimulation().getBoard().getWidth();
        int cols = controller.getSimulation().getBoard().getHeight();

        boardPane.prefWidthProperty().bind(rootPane.widthProperty());
        boardPane.prefWidthProperty().bind(rootPane.heightProperty());


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                addField(i, j);
            }
        }
    }


    //Funkcja służąca do dodania pojedynczego pola planszy

    private void addField(int row, int col) {
        StackPane field = new StackPane();

        field.prefWidthProperty().bind(rootPane.widthProperty().divide(controller.getSimulation().getBoard().getWidth()));
        field.prefHeightProperty().bind(rootPane.heightProperty().divide(controller.getSimulation().getBoard().getWidth()));

        field.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // Dodanie pola do planszy, w odpowiendim kolorze w zależności od stanu

        if(controller.getSimulation().getBoard().getCell(row, col).isAlive()) field.setStyle("-fx-background-color: crimson");
        else field.setStyle("-fx-background-color: antiquewhite");
        boardPane.add(field, row, col);

        // Dodaje obsługę zdarzenia "naciśnięcia i przytrzymania" myszy

        field.addEventHandler(MouseDragEvent.DRAG_DETECTED, event -> {
            if(!isRunning) {
                field.startFullDrag();
            }
        });

        /*
         Jeśli zostało wcześniej rozpoczęte zdarzenie DRAG_DETECTED, to pole zmieni stan
         przy wejściu przyciśniętej myszy na dane pole
        */

        field.addEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, event -> {
            if(event.isPrimaryButtonDown()) {
                controller.getSimulation().getBoard().getCell(row, col).changeState();
                if (controller.getSimulation().getBoard().getCell(row, col).isAlive()) field.setStyle("-fx-background-color: crimson");
                else field.setStyle("-fx-background-color: antiquewhite");
            }
        });

        // Obsluga zdarzenia pojedynczego kliknięcia myszką. Zmienia stan pola.

        field.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(!isRunning) {
                controller.getSimulation().getBoard().getCell(row, col).changeState();
                if (controller.getSimulation().getBoard().getCell(row, col).isAlive())
                    field.setStyle("-fx-background-color: crimson");
                else field.setStyle("-fx-background-color: antiquewhite");
            }
        });
    }

}
