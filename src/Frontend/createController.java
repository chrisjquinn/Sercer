package Frontend;

import Backend.*;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/*         CreateController.java
    The create window actions are handled here.

    Specifically, the user can draw the SERCObjects into the canvas
    with whatever scale they would like. This is then converted into
    a SERCObject grid and a SERC is made
*/

//@FXML Document       ->       createWindow.fxml

public class createController {
    //The controller passed from main screen
    private MainController mainController;

    // All of the UI variables have to be public in order to be referenced via communication with FXML
    public Canvas createCanvas;
    public Color currentColour;
    public ToggleGroup paintToggleGroup;
    public Slider paintSlider;
    public Label paintSliderLabel;
    public BorderPane createPane;

    public int paintSliderValue;
    public double canvasX;
    public double canvasY;

    /*  init(MainController m)

        stores the main controller so that the SERC can be passed back when this is finished.
        Then, listeners are added to the slider and the canvas for drawing

     */
    public void init(MainController m){
        mainController = m;
        paintSliderValue = 20;      //Just before the user changes it
        currentColour = Color.BLACK;
        paintSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            paintSliderLabel.setText(String.valueOf(newValue.intValue()));
            paintSliderValue = (int) newValue.doubleValue();
            clearButtonClicked();
        });

        createCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        canvasX =  e.getX();
                        canvasY =  e.getY();
                    }
                });

        createCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        canvasX =  e.getX();
                        canvasY =  e.getY();
                    }
                });
        renderGrid();

    }

    /*    canvasClicked()

          The user has pressed down on the mouse click on the canvas, fill the cell
          that the mouse is at with the current paint the mouse has.
     */
    public void canvasClicked(){
        GraphicsContext gc = createCanvas.getGraphicsContext2D();
        gc.setFill(currentColour);
        int convertedX = ((int) canvasX / paintSliderValue) * paintSliderValue;
        int convertedY = ((int) canvasY / paintSliderValue) * paintSliderValue;
        gc.fillRect(convertedX,convertedY,paintSliderValue,paintSliderValue);
        renderGrid();
    }

    /*    canvasDragged()

          The user has pressed down on the mouse and is now dragging with the mouse
          button pressed down. This will then create a stream / path of paint.
          However, this will not be a path if the user tries to do it with casualties
          or aids. As these are meant to be limited and not abundant
     */
    public void canvasDragged(){
        if(!(currentColour.equals(Color.ORANGE) || currentColour.equals(Color.GREEN))){
            GraphicsContext gc = createCanvas.getGraphicsContext2D();
            gc.setFill(currentColour);
            int convertedX =  ((int) canvasX / paintSliderValue) * paintSliderValue;
            int convertedY =  ((int) canvasY / paintSliderValue) * paintSliderValue;
            gc.fillRect(convertedX,convertedY,paintSliderValue,paintSliderValue);
            renderGrid();
        }
    }

    //When paint buttons clicked on the side of the pane, set the colour of the painter
    public void wallRadioButtonClicked(){currentColour = Color.BLACK;}
    public void dangerRadioButtonClicked(){currentColour = Color.RED;}
    public void roadRadioButtonClicked(){currentColour = Color.GRAY;}
    public void waterRadioButtonClicked(){currentColour = Color.BLUE;}
    public void casualtyRadioButtonClicked(){currentColour = Color.ORANGE;}
    public void aidRadioButtonClicked(){currentColour = Color.GREEN;}
    public void eraserRadioButtonClicked(){currentColour = Color.WHITE;}

    //Clear the canvas and draw the grid lines back
    public void clearButtonClicked(){
        GraphicsContext gc = createCanvas.getGraphicsContext2D();
        gc.clearRect(0,0,createCanvas.getWidth(), createCanvas.getHeight());
        renderGrid();
    }

    //Draws grid lines so the user can see the cells, removed when converted to a SERC
    public void renderGrid(){
        GraphicsContext gc = createCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(0.5);
        for(int i = 0; i <=  createCanvas.getWidth(); i = i + paintSliderValue){
            gc.strokeLine(i,0, i , createCanvas.getHeight());
        }
        for(int j = 0; j <=  createCanvas.getHeight(); j = j + paintSliderValue){
            gc.strokeLine(0,j, createCanvas.getWidth() , j);
        }


    }

    /*      convertButtonClicked()

            The user now wants to convert the map drawn out into a SERC, a pixel reader
            is used to see what is in the canvas. Then, a SERC grid is made from each cell
            sample and then passed into a SERC class.
     */
    public void convertButtonClicked(){
        WritableImage writableImage = new WritableImage((int) createCanvas.getWidth(), (int) createCanvas.getHeight());
        WritableImage snapshot = createCanvas.snapshot(new SnapshotParameters(), writableImage);

        PixelReader pixelReader = snapshot.getPixelReader();
        SERCObject[][] convertingGrid = new SERCObject[(int) Math.ceil(snapshot.getWidth() / paintSliderValue)][(int) Math.ceil(snapshot.getHeight() / paintSliderValue) ];

        for(int i = 0; i < Math.ceil(snapshot.getWidth() / paintSliderValue); i = i + 1){
            for(int j = 0; j < Math.ceil(snapshot.getWidth() / paintSliderValue); j = j + 1){
                Color currentPixel = pixelReader.getColor((i * paintSliderValue) + 1,(j*paintSliderValue) + 1);
                Point currentPoint = new Point(i,j);

                if (currentPixel.equals(Color.WHITE)){convertingGrid[i][j] = new Empty(currentPoint);}
                else if (currentPixel.equals(Color.BLACK)){convertingGrid[i][j] = new Wall(currentPoint);}
                else if (currentPixel.equals(Color.BLUE)){convertingGrid[i][j] = new Water(currentPoint);}
                else if (currentPixel.equals(Color.GRAY)){convertingGrid[i][j] = new Road(currentPoint);}
                else if (currentPixel.equals(Color.GREEN)){convertingGrid[i][j] = new Aid(currentPoint);}
                else if (currentPixel.equals(Color.ORANGE)){convertingGrid[i][j] = new Casualty(currentPoint);}
                else if (currentPixel.equals(Color.RED)){convertingGrid[i][j] = new OutOfBounds(currentPoint);}
                else{convertingGrid[i][j] = new Empty(currentPoint);}
            }
        }

        //Now to get the current time and put this into the log
        LocalDateTime current = LocalDateTime.now();
        // to print in a particular format
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        SERC output = new SERC(convertingGrid);
        output.setWasCreated(true);
        output.setTimeOfCreation("----- SERC Created on "+current.format(format)+" -----");
        mainController.setCurrentSERC(output);
        mainController.showSERC();
        mainController.showSERCSummary();
    }

    //The user is finished, convert the SERC and pass back to the main window
    public void saveAndClose(){
        convertButtonClicked();
        Stage window = (Stage) createPane.getScene().getWindow();
        window.close();

    }

    //Same method from the main controller, opens the about box
    public void openAboutBox(){
        try {
            FXMLLoader aboutLoader = new FXMLLoader(getClass().getResource("/Frontend/aboutBox.fxml"));
            Parent root = (Parent) aboutLoader.load();

            //Get the controller of generateWindow
            AboutBox aboutBox = aboutLoader.getController();
            aboutBox.init();

            Stage aboutStage = new Stage();
            aboutStage.initModality(Modality.APPLICATION_MODAL);
            aboutStage.setScene(new Scene(root, 700, 600));
            aboutStage.setTitle("About");
            aboutStage.show();
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

} //END of createController
