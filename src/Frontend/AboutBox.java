package Frontend;

import javafx.scene.text.Text;

/*          AboutBox.java
    This is a simple pop-up window that will display to the user
    a guide to the software. This uses text boxes to display this
    and is kept the same every time, hence the text being written
    here.
*/

// FXML Document -> aboutBox.fxml

public class AboutBox {

    public Text mainWindowText;
    public Text createWindowText;
    public Text generateWindowText;
    public Text otherStuffText;

    public void init(){
        //Simply just set the text of each part, everything else is done

        mainWindowText.setText("The main window is where all further actions come from. Here you can load, save, create and auto-generate SERCs.\n" +
                                "Pressing the 'create' or 'generate' buttons will open a separate window for each of these actions, it is recommended\n" +
                                "that you don't close the main window at all, as the windows communicate with each other. You can open/close all other\n" +
                                "windows as much as you want.\n\n"+

                                "The 'Messages/Log' tab is not editable, this is for the software to put metadata such as when the SERC was made (etc.)\n" +
                                "To make any notes, put them in the 'Notes' tab. Pressing enter will start a new line and the text should wrap around,\n"+
                                "the text will save automatically to the corresponding SERC displayed."

        );

        createWindowText.setText("The create window is so that you can draw your own SERC, the idea behind this is because you already know what you\n" +
                                "want to be in the SERC, this is simply a tool for you to draw out quickly as a lot of SERCs take a while to draw out. \n\n" +

                                "Pressing 'convert' will remove the boarder around the cells and pass to the main window. It is also kept in the create\n" +
                                "window so that more changes can be made as you go along.\n\n"+

                                "Sadly, re-sizing the window has been disabled, this is because the actual canvas in which you paint the SERC will not\n"+
                                "re-size along with it, it will stay the same size. This is where the slider comes in, setting a smaller value on the \n"+
                                "slider allows smaller cells, but can be harder to create what you want. Be warned; changing the slider when you already\n"+
                                "have work made will remove all your work, convert it first.\n\n"+

                                "Simple paint colours are used to represent common items in SERCS; Casualties, aids, water, walls, blank spaces (etc).\n" +
                                "You won't be able to create multiple casualties or aids through dragging the mouse when clicked down, this is disabled as\n"+
                                "it would be mis-representative of the object at hand."

        );

        generateWindowText.setText("The generate window is the real interesting part. Here, SERCs can be generated with a specified amount of degree.\n" +
                                    "A lot of flexibility has been put into this, allowing only a few constraints you have to satisfy:\n\n" +

                                    "You are perfectly able to not enter anything and allow a completely random SERC to be created, this will be shown\n" +
                                    "back onto the main window. The 'Re-load' button will have become enabled now as well, as the type of SERC and number\n"+
                                    "of casualties has been set, pressing re-load will simply change:\n\n"+

                                    "1. The layout of the type of SERC (i.e. the surroundings will look different)\n"+
                                    "2. The placement of casualties and aids\n\n"+

                                    "If you wish to make a new random SERC, open the generate window and press generate. \n\n" +

                                    "If you put a number into the number of casualties, the algorithm will map all of the conditions you select to this number\n"+
                                    "this does allow 1 casualty to have every condition possible. It is down to you to decide what seems too unrealistic.\n"+
                                    "If you set a number of casualties and only put some conditions, then the algorithm will then allocate random conditions\n"+
                                    "for the remaining casualties. This could mean there are casualties with repeated conditions, and some casualties can be\n"+
                                    "simply not suffering.\n\n"+

                                    "If you have made a selection and wish to start again, simply close the window and press 'generate' on the main window\n"+
                                    "again."
        );

        otherStuffText.setText("Please see the usage guide for a more detailed walk-through on how to use. Any moe specific questions, please email:\n\n"+
                                "C.Quinn@warwick.ac.uk");
    }
}
