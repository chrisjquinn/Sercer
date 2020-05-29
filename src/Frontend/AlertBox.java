package Frontend;

import javafx.scene.control.Label;

/*          AlertBox.java
     The simplest window in the whole software, this is
     used as a pop-up for when the user has done something
     out of constraints (e.g. put number of casualties = 0)
*/

//@FXML Document    ->      alertBox.fxml

public class AlertBox {

    public Label alertText;

    public void display(String s){
        alertText.setText(s);
    }
}
