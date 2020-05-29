package Frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*

        SERCER - Software for creating SERCS

        Author - Chris Quinn, May 2020. 



 */
/*      Main entry point for the software
   Here, the software starts at main(String[] args).
   A main window is loaded by calling the fxml document for mainWindow.fxml
   along with its controller, MainController.java. From here, the program runs.

   All main window interaction is kept onto MainController
*/

//(This has no FXML document linked)

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Frontend/mainWindow.fxml"));
        Parent root = (Parent) mainLoader.load();
        primaryStage.setTitle("SERCER 2020");
        primaryStage.setScene(new Scene(root, 817, 573));


        //Get the controller of mainWindow
        MainController maincontroller = mainLoader.getController();
        maincontroller.init(primaryStage);
        //Methods to the MainController go here
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
