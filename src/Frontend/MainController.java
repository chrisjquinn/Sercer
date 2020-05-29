package Frontend;

//Objects from backend
import Backend.Aid;
import Backend.Casualty;
import Backend.SERC;
import Backend.SERCGenerator;

//Exporting as PDF
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

//JavaFX imports for rendering, buttons, etc.
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

//writing out and loading in
import java.io.*;

/*          MainController.java
    The main window events are handled here.
    From the main window, the generate and create widow can
    be accessed. SERCs are displayed, saved and loaded here.

    The main window also stores the current SERC and (if
    applicable) the SERCGenerator along with it in case the
    user wishes to re-load
*/

//@FXML Document       ->       mainWindow.fxml

public class MainController {
    public Stage primaryStage;

    /*          Variables of the window, buttons, items etc.            */

    //Buttons for when we are pressing things
    public Button createButton;
    public Button generateButton;
    public Button saveButton;
    public Button loadButton;
    public TextArea notesTextArea;
    public TextArea logTextArea;
    public TextArea briefTextArea;

    //these are the clickable things in the menu
    public MenuItem fileCloseButton;
    public MenuItem fileSaveButton;
    public MenuItem fileSaveAsPDFButton;

    //The canvas that displays the SERC
    public Canvas sercCanvas;



    //Variables for the SERC generation and management
    public SERCGenerator currentGenerator;
    public SERC currentSERC;

    public void setCurrentGenerator(SERCGenerator gen){this.currentGenerator = gen;}
    public void setCurrentSERC(SERC s){this.currentSERC = s;}


    /*  init(Stage stage)

        When the main window is called, a few items need to be disabled and some listeners
        need to be set. init() will disable buttons that only work when a SERC is loaded
        and also start listening to the notes text for changes.
    */
    public void init(Stage stage){
        //Add a listener to the text area for when the user is putting information in.
        notesTextArea.textProperty().addListener((observable, oldValue, newValue)  -> {
            if(!(currentSERC == null)){
                currentSERC.setNotes(notesTextArea.getText());
            }
        });
        loadButton.setDisable(true);

        GraphicsContext gc = sercCanvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(
                "No current SERC loaded",
                Math.round(sercCanvas.getWidth()  / 2),
                Math.round(sercCanvas.getHeight() / 2)
        );

        primaryStage = stage;
        fileSaveButton.setDisable(true);
        fileSaveAsPDFButton.setDisable(true);
        saveButton.setDisable(true);

    }

    /*  openCreateWindow()

        Calls the createWindow FXML document along with its controller,
        opens it on top of the main window. The stage is passed over so
        the windows are linked and can communicate
    */
    public void openCreateWindow(){
        try{
            FXMLLoader createLoader = new FXMLLoader(getClass().getResource("/Frontend/createWindow.fxml"));
            Parent root = (Parent) createLoader.load();

            //Get the controller of createWindow
            createController createcontroller = createLoader.getController();
            //Tell the controller to do some stuff before shown to the user
            createcontroller.init(this);

            Stage newStage = new Stage();
            newStage.setResizable(false);
            newStage.setScene(new Scene(root, 730, 570));
            newStage.setTitle("Create SERC");
            newStage.show();

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /*  openGenerateWindow()

        Calls the generateWindow FXML document along with its controller,
        opens it on top of the main window. The stage is passed over so
        the windows are linked and can communicate
    */
    public void openGenerateWindow() {
        try {
            FXMLLoader genLoader = new FXMLLoader(getClass().getResource("/Frontend/generateWindow.fxml"));
            Parent root = (Parent) genLoader.load();

            //Get the controller of generateWindow
            generateController generatecontroller = genLoader.getController();
            generatecontroller.init(this);

            Stage generateStage = new Stage();
            generateStage.setResizable(false);
            generateStage.setScene(new Scene(root, 700, 572));
            generateStage.setTitle("Generate SERC");
            generateStage.show();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /*  openAboutBox()

        Calls the aboutBox FXML document along with its controller,
        opens it on top of the main window. Communication not needed
        between main window and this one, but the about box needs to be
        modal.
    */
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

    /*  loadSERC()

        This will open a file browser so the user can load a SERC
        (of type .serc) into the software. The serialized object is
        read in and converted back to its SERC form.

        If the SERC that was loaded in was generated, then a generator is
        made so that re-loads are allowed.
    */
    public void loadSERC(){
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SERC Files (*.serc)","*.serc"));
        File selectedFile = filechooser.showOpenDialog(primaryStage);

        if(selectedFile != null){
            //try and get the serc
            try{
                FileInputStream fis = new FileInputStream(selectedFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                SERC deSerializedSERC = (SERC) ois.readObject();
                setCurrentSERC(deSerializedSERC);
                if(!deSerializedSERC.getWasCreated()) {
                    SERCGenerator newGenerator = new SERCGenerator(deSerializedSERC);
                    setCurrentGenerator(newGenerator);
                }
                showSERC();
                showSERCSummary();
                ois.close();
            } catch(Exception e){createAlert(e.getMessage());}
        }
        else{
            createAlert("File selected is empty");
        }
    }

    /*  saveSERC()

        When the user wishes to save the SERC as a .serc file
        into the location of their choice.
    */
    public void saveSERC(){
        currentSERC.setBrief(briefTextArea.getText());
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SERC Files (*.serc)", "*.serc"));
        File file = fileChooser.showSaveDialog(primaryStage);
        try{
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(currentSERC);
            objectOut.close();
        } catch(Exception ignored){}
    }

    /*  saveSERCasPDF()

        This will save the SERC as PDF with the map and descriptions.
        the IText library is used here to create a layout and put the
        pieces together.

        If the SERC was generated, then another page is made with the brief
        along with the casualties and aids removed.
    */
    public void saveSERCasPDF() {
        currentSERC.setBrief(briefTextArea.getText());
        //Get the save directory of where we want to save it
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(primaryStage);
        File fullSerc = new File(file.getAbsolutePath().replace(".pdf", "1.png"));


        WritableImage firstImage = new WritableImage((int) sercCanvas.getWidth(), (int) sercCanvas.getHeight());
        final WritableImage snapshot = sercCanvas.snapshot(new SnapshotParameters(), firstImage);

        //This will write the image to the local directory as a png
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", fullSerc);
        } catch (IOException e) {
            createAlert("Snapshot could not be taken: "+e);
        }

        Font courier = FontFactory.getFont(FontFactory.COURIER);
        Font helv = FontFactory.getFont(FontFactory.HELVETICA);
        Font helvBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        try{
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file.getCanonicalPath()));
            document.open();

            //Add the time of generation / creation
            document.add(new Paragraph(currentSERC.getTimeOfCreation(), courier));

            //Get the image back in the Image object made for writing onto PDF
            Image iTextImageFullSerc = Image.getInstance(fullSerc.getAbsolutePath());

            if (currentSERC.getWasCreated()) {
                //We just put the serc onto one page along with the brief
                document.add(iTextImageFullSerc);
                document.add(new Paragraph("Brief:", helvBold));
                document.add(new Paragraph(briefTextArea.getText(), helv));

                    //Delete the image of the serc as we have now added it to the PDF
                boolean deleteFullSerc = fullSerc.delete();
            } else {
                File sercLayout = new File(file.getAbsolutePath().replace(".pdf", "2.png"));
                showSERCLayout();
                WritableImage secondImage = new WritableImage((int) sercCanvas.getWidth(), (int) sercCanvas.getHeight());
                final WritableImage snapshot2 = sercCanvas.snapshot(new SnapshotParameters(), secondImage);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot2, null), "png", sercLayout);
                } catch (IOException e) {
                    createAlert("Snapshot could not be taken: "+e);
                }
                showSERC();

                document.add(iTextImageFullSerc);
                document.add(new Paragraph("Metadata:", helvBold));
                document.add(new Paragraph("Location: " + currentSERC.getLocation() + "           Time: " + currentSERC.getTimeOfSERC() + "           Weather: " + currentSERC.getWeather(), helv));
                

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.addCell(getCell("\nCasualties:", helvBold, PdfPCell.ALIGN_LEFT));
                table.addCell(getCell("Aids:",helvBold, PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(currentSERC.getCasualtiesDescription(), helv,  PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(currentSERC.getAidsDescription(),helv, PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell("", helvBold, PdfPCell.ALIGN_LEFT));
                table.addCell(getCell("Notes:",helvBold, PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell("", helv,  PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(currentSERC.getNotes(),helv, PdfPCell.ALIGN_RIGHT));

                document.add(table);

                document.newPage();

                Image iTextSercLayout = Image.getInstance(sercLayout.getAbsolutePath());
                document.add(iTextSercLayout);
                document.add(new Paragraph("Brief:", helvBold));
                document.add(new Paragraph(briefTextArea.getText(), helv));

                //Delete the image of the serc as we have now added it to the PDF
                boolean deleteFullSerc = fullSerc.delete();
                boolean deleteSercLayout = sercLayout.delete();
            }
            document.close();
            writer.close();
            System.out.println("Serc has been saved as PDF");
        } catch (Exception ignored){}

    }

    //Needed to create a cell in the PDF table such that text can be formatted
    public PdfPCell getCell(String text, Font f, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, f));
        cell.setPadding(0);

        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }


    /*  reload()

        When the user wishes to keep the casualties and aids the same, but
        change the map. Re-load will make the current SERCGenerator make a new
        SERC but keep the casualties and aids.
     */
    public void reload(){
        currentSERC = currentGenerator.generateNewSERC();
        showSERC();
        showSERCSummary();
    }

    /*  close()

        exits the program
    */
    public void close(){
        //This is when the user has finished.
        System.exit(0);

    }

    /*  showSERC()

        This is rendering of SERCs onto the main canvas. The grid is utilized
        here along with the SERCObject int representations. Colour per SERCObject
    */
    public void showSERC(){
        loadButton.setDisable(currentSERC.getWasCreated());
        fileSaveButton.setDisable(false);
        fileSaveAsPDFButton.setDisable(false);
        saveButton.setDisable(false);
        GraphicsContext gc = sercCanvas.getGraphicsContext2D();
        int scaleValueX = (int) Math.ceil(sercCanvas.getWidth() / currentSERC.getWidth());
        int scaleValueY = (int) Math.ceil(sercCanvas.getHeight() / currentSERC.getLength());


        for(int i = 0; i  < sercCanvas.getWidth(); i = i + scaleValueX){
            for(int j = 0; j < sercCanvas.getHeight(); j = j + scaleValueY){
                gc.setFill(setColor(currentSERC.grid[i / scaleValueX][j / scaleValueY].getIntValue()));
                gc.fillRect(i,j,scaleValueX,scaleValueY);       //Doesn't seem to be enlarging the rectangle
            }
        }

        if(!currentSERC.getWasCreated()){
            //Put the casualties onto the map
            for(int i = 0; i < currentSERC.getCasualties().size(); i++){
                Casualty currentCasualty = currentSERC.getCasualties().get(i);
                gc.setFill(setColor(2)); //Orange - Casualty
                gc.fillRect(currentCasualty.getLocation().x * scaleValueX, currentCasualty.getLocation().y * scaleValueY, scaleValueX, scaleValueY);
                gc.setFill(setColor(1));
                gc.fillText(String.valueOf(i+1), currentCasualty.getLocation().x * scaleValueX + (int) Math.floor(scaleValueX/3), currentCasualty.getLocation().y * scaleValueY + (int) Math.floor(scaleValueY/3), scaleValueX);
            }

            //Now put the aids onto the map
            for(int i = 0; i < currentSERC.getAids().size(); i++){
                Aid aid = currentSERC.getAids().get(i);
                gc.setFill(setColor(3)); //Green - Aid
                gc.fillRect(aid.getLocation().x * scaleValueX, aid.getLocation().y * scaleValueY, scaleValueX, scaleValueY);
                gc.setFill(setColor(0));
                gc.fillText(String.valueOf(i+1), aid.getLocation().x * scaleValueX + (int) Math.floor(scaleValueX/3), aid.getLocation().y * scaleValueY + (int) Math.floor(scaleValueY/3), scaleValueX);

            }
        }


    }
    /*  showSERCLayout()

        similar to showSERC, simply renders the SERC map without casualties and aids.
     */
    public void showSERCLayout(){
        GraphicsContext gc = sercCanvas.getGraphicsContext2D();
        int scaleValueX = (int) Math.ceil(sercCanvas.getWidth() / currentSERC.getWidth());
        int scaleValueY = (int) Math.ceil(sercCanvas.getHeight() / currentSERC.getLength());

        for(int i = 0; i  < sercCanvas.getWidth(); i = i + scaleValueX){
            for(int j = 0; j < sercCanvas.getHeight(); j = j + scaleValueY){
                gc.setFill(setColor(currentSERC.grid[i / scaleValueX][j / scaleValueY].getIntValue()));
                gc.fillRect(i,j,scaleValueX,scaleValueY);
            }
        }
    }

    /*  showSERCSummary()

        Puts the metadata into the tabs on the right hand side of the window.
     */
    public void showSERCSummary(){
        logTextArea.setText(currentSERC.getFullSERCSummary());
        notesTextArea.setText(currentSERC.getNotes());
        briefTextArea.setText(currentSERC.getBrief());
    }

    /*  setColour(int i)

        returns a Colour object for the rendering of SERCs, this is where the
        switching of int i comes useful.
     */
    public Color setColor(int i) {
        switch (i) {
            case 0: return Color.WHITE;     //Empty
            case 1: return Color.BLACK;     //Wall
            case 2: return Color.ORANGE;    //Casualty
            case 3: return Color.GREEN;     //Aids
            case 4: return Color.RED;       //Danger / Out of Bounds
            case 5: return Color.BLUE;      //Water
            case 6: return Color.GRAY;      //Roads
            case 7: return Color.BEIGE;
            //Any more?
            default: return Color.PURPLE; //For when something goes horribly wrong
        }
    }

    /*  createAlert(String message)

        creates an alert box with the message passed into this method.
     */
    public void createAlert(String message){
        try{
            FXMLLoader alertLoader = new FXMLLoader(getClass().getResource("/Frontend/alertBox.fxml"));
            Parent root = (Parent) alertLoader.load();

            //Get the controller of createWindow
            AlertBox alertBox = alertLoader.getController();
            //Tell the controller to do some stuff before shown to the user
            alertBox.display(message);

            Stage newStage = new Stage();
            newStage.setTitle("Alert");
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setScene(new Scene(root, 500,100));
            newStage.showAndWait();

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }



}
