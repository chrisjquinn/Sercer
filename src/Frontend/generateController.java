package Frontend;//Package for all the SERC class structure
import Backend.*;

//JavaFX imports
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

//ArrayList used for conditions and casualties
import java.util.ArrayList;


/*          generateController.java
    The generate window is controlled here. A lot of GUI buttons
    are on this window and take up most of the file.

    Mapping of conditions to casualties is also done here.
*/

//@FXML Document       ->       generateWindow.fxml


public class generateController {

    //The maincontroller is stored so the generate controller can pass info back
    private MainController mainController;


    //buttons at the top of the generate window
    public MenuButton wetSERCMenuButton;
    public MenuButton drySERCMenuButton;
    public MenuButton phoneMenuButton;
    public MenuButton firstAidMenuButton;
    public TextField numberOfCasualtiesArea;

    //buttons and fields at the bottom of the generate window
    public Button finalGenerateButton;
    public TextField locationTextField;
    public TextField timeTextField;
    public TextField weatherTextField;

    //variables that store conditions, casualties, SERC items
    private ArrayList<ConditionType> conditionsArray;
    private ArrayList<String> conditionsInfoArray;
    private ArrayList<Casualty> casualties;
    private ArrayList<Aid> aids;
    private WaterType bodyOfWater;
    private DryType drySERCType;
    private PhoneFaType phoneType;
    private PhoneFaType faType;
    private boolean isWaterBased;

    //These are a bunch of tick boxes that are in the casualty main box, there are a lot
    /*              BREATHING PANE          */
    public CheckBox breathingAna;               public TextArea breathingAnaText;
    public CheckBox breathingChockFull;         public TextArea breathingChockFullText;
    public CheckBox breathingChockPart;         public TextArea breathingChockPartText;
    public CheckBox breathingHypervent;         public TextArea breathingHyerventText;
    public CheckBox breathingAsthma;            public TextArea breathingAsthmaText;

    /*              COUNCIOUS PANE           */
    public CheckBox conBreathing;               public TextArea conBreathingText;
    public CheckBox conNotBreathing;            public TextArea conNonBreathingText;
    public CheckBox conFaint;                   public TextArea conFaintText;
    public CheckBox conSeizure;                 public TextArea conSeizureText;

    /*              BREAKS / DISLOCATION PANE   */
    public CheckBox breakArm;                   public TextArea breakArmText;
    public CheckBox breakLeg;                   public TextArea breakLegText;
    public CheckBox breakOther;                 public TextArea breakOtherText;
    public CheckBox dislocKnee;                 public TextArea dislocKneeText;
    public CheckBox dislocAnk;                  public TextArea dislocAnkText;
    public CheckBox dislocHip;                  public TextArea dislocHipText;
    public CheckBox dislocOther;                public TextArea dislocOtherText;

    /*              SPRAIN / STRAIN PANE        */
    public CheckBox sprainElbow;                public TextArea sprainElbowText;
    public CheckBox sprainAnk;                  public TextArea sprainAnkText;
    public CheckBox sprainWrist;                public TextArea sprainWristText;
    public CheckBox sprainKnee;                 public TextArea sprainKneeText;
    public CheckBox sprainOther;                public TextArea sprainOtherText;

    /*              MEDICAL CONDITION PANE      */
    public CheckBox medicalEpilepsy;            public TextArea medicalEpilepsyText;
    public CheckBox medicalAsthma;              public TextArea medicalAsthmaText;
    public CheckBox medicalAllergies;           public TextArea medicalAllergiesText;
    public CheckBox medicalDiabetes;            public TextArea medicalDiabetesText;
    public CheckBox medicalAngina;              public TextArea medicalAnginaText;
    public CheckBox medicalOther;               public TextArea medicalOtherText;

    /*              BLEEDS PANE             */
    public CheckBox bleedChest;                 public TextArea bleedChestText;
    public CheckBox bleedHead;                  public TextArea bleedHeadText;
    public CheckBox bleedEmbed;                 public TextArea bleedEmbedText;
    public CheckBox bleedArm;                   public TextArea bleedArmText;
    public CheckBox bleedLeg;                   public TextArea bleedLegText;
    public CheckBox bleedOther;                 public TextArea bleedOtherText;


    /*              DROWNING PANE           */
    public Tab drowningTab;
    public CheckBox drownWeak;                  public TextArea drownWeakText;
    public CheckBox drownNon;                   public TextArea drownNonText;
    public CheckBox downLocked;                 public TextArea drownLockedText;
    public CheckBox drownOther;                 public TextArea downOtherText;


    /*              OTHERS PANE             */
    public CheckBox otherHeat;                  public TextArea otherHeatText;
    public CheckBox otherHypotherm;             public TextArea otherHypothermText;
    public CheckBox otherSpinal;                public TextArea otherSpinalText;
    public CheckBox otherCrush;                 public TextArea otherCrushText;
    public CheckBox otherSting;                 public TextArea otherStingText;
    public CheckBox otherNotSuffering;          public TextArea otherNotSufferingText;
    public CheckBox otherOther;                 public TextArea otherOtherText;


    //End of all the checkboxes


    /*  init(MainController m)

        stores the main controller so the SERC can be passed back to the main window.
        Also starts listening to the casualties area so that only numbers can be input.
        Finally disables the drowning tab in-case the user picks a dry SERC
     */
    public void init(MainController m){
        numberOfCasualtiesArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberOfCasualtiesArea.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        mainController = m;
        drowningTab.setDisable(true);
    }

    //Drop-Down menu buttons for phone sets the type when the SERCGenerator is called
    public void noPrefPhone(){phoneMenuButton.setText("No Pref."); phoneType = randomPhoneFAType();}
    public void inSERCPhone(){phoneMenuButton.setText("In SERC"); phoneType = PhoneFaType.INSERC;}
    public void outOfSERCPhone(){phoneMenuButton.setText("Out of Bounds"); phoneType = PhoneFaType.OUTOFBOUNDS;}
    public void noPhone(){phoneMenuButton.setText("None"); phoneType = PhoneFaType.NONE;}

    //Drop-down menu buttons for the first aid kit.
    public void noPrefFA(){firstAidMenuButton.setText("No Pref."); faType = randomPhoneFAType();}
    public void inSERCFA(){firstAidMenuButton.setText("In SERC"); faType = PhoneFaType.INSERC;}
    public void outOfSERCFA(){firstAidMenuButton.setText("Out of Bounds"); faType = PhoneFaType.OUTOFBOUNDS;}
    public void noFA(){firstAidMenuButton.setText("None"); faType = PhoneFaType.NONE;}

    //Drop-down menu items for a dry-type of SERC
    public void noPrefDrySERC(){
        wetSERCMenuButton.setDisable(true);
        isWaterBased = false; drySERCType = randomDrySERC();
        drySERCMenuButton.setText("No Pref.");
        drowningTab.setDisable(true);
    }
    public void indoorSERCPressed(){
        wetSERCMenuButton.setDisable(true);
        isWaterBased = false; drySERCType = DryType.INDOOR;
        drySERCMenuButton.setText("Indoor");
        drowningTab.setDisable(true);
    }
    public void multiRoomSERCPressed(){
        wetSERCMenuButton.setDisable(true);
        isWaterBased = false; drySERCType = DryType.MULTIROOM;
        drySERCMenuButton.setText("Multi Room");
        drowningTab.setDisable(true);
    }
    public void outdoorSERCPressed(){
        wetSERCMenuButton.setDisable(true);
        isWaterBased = false; drySERCType = DryType.OUTDOOR;
        drySERCMenuButton.setText("Outdoor");
        drowningTab.setDisable(true);
    }
    public void roadSERCPressed(){
        wetSERCMenuButton.setDisable(true);
        isWaterBased = false; drySERCType = DryType.ROAD;
        drySERCMenuButton.setText("Road");
        drowningTab.setDisable(true);
    }

    //Drop-down menu items for a wet type of SERC
    public void noPrefWetSERC(){
        drySERCMenuButton.setDisable(true);
        bodyOfWater = randomWetSERC(); isWaterBased = true;
        wetSERCMenuButton.setText("No Pref.");
        drowningTab.setDisable(false);
    }
    public void lakeSERCPressed(){
        drySERCMenuButton.setDisable(true);
        bodyOfWater = WaterType.LAKE; isWaterBased = true;
        wetSERCMenuButton.setText("Lake");
        drowningTab.setDisable(false);

    }
    public void poolSERCPressed(){
        drySERCMenuButton.setDisable(true);
        bodyOfWater = WaterType.POOL; isWaterBased = true;
        wetSERCMenuButton.setText("Pool");
        drowningTab.setDisable(false);

    }
    public void beachSERCPressed(){
        drySERCMenuButton.setDisable(true);
        bodyOfWater = WaterType.BEACH; isWaterBased = true;
        wetSERCMenuButton.setText("Beach");
        drowningTab.setDisable(false);

    }
    public void riverSERCPressed(){
        drySERCMenuButton.setDisable(true);
        bodyOfWater = WaterType.CANAL; isWaterBased = true;
        wetSERCMenuButton.setText("River");
        drowningTab.setDisable(false);

    }
    public void oceanSERCPressed(){
        drySERCMenuButton.setDisable(true);
        bodyOfWater = WaterType.OCEAN; isWaterBased = true;
        wetSERCMenuButton.setText("Ocean");
        drowningTab.setDisable(false);

    }

    /*  generateButtonPressed()

        The big method, the user is ready to generate a SERC.

        This method will then check what SERC is to be used, map the conditions to casualties. Check the aids.
        Then make the SERCGenerator, generate a SERC, pass it all to the main window and close

     */
    public void generateButtonPressed(){
        checkSERCType();
        compileConditions();
        checkAids();
        try {
            mapConditionsToCasualties(); //This is what throws the exception
            //Create the generator and set its params
            SERCGenerator generator = new SERCGenerator((int) mainController.sercCanvas.getWidth() / 10, (int) mainController.sercCanvas.getHeight() / 10);

            if(isWaterBased){generator.setWetSERCType(bodyOfWater);}
            else{generator.setDrySERCType(drySERCType);}
            //Then we need to add the casualties and aids
            generator.setCasualties(casualties);
            generator.setAids(aids);

            //Add the metadata to the generator, it will add it to the serc later
            RandomMetadataGenerator metadataGenerator;
            if(isWaterBased){metadataGenerator = new RandomMetadataGenerator(bodyOfWater);}
            else{metadataGenerator = new RandomMetadataGenerator(drySERCType);}

            if(locationTextField.getText().equals("")){generator.setLocation(metadataGenerator.getLocation());}
            else{generator.setLocation(locationTextField.getText());}

            if(timeTextField.getText().equals("")){generator.setTimeOfSERC(metadataGenerator.getTime());}
            else{generator.setTimeOfSERC(timeTextField.getText());}

            if(weatherTextField.getText().equals("")){generator.setWeather(metadataGenerator.getWeather());}
            else{generator.setWeather(weatherTextField.getText());}

            //Create a simple brief (that can be changed)
            String briefText = "";
            if(!(generator.getLocation().equals(""))){briefText += "You are at "+generator.getLocation()+".";}
            if(!generator.getTimeOfSERC().equals("")){briefText += "It is "+generator.getTimeOfSERC();}
            if(!generator.getWeather().equals("")){briefText += " and the weather is "+generator.getWeather();}
            generator.setBrief(briefText);

            //Now create a serc, pass these to the maincontroller
            SERC serc = generator.generateNewSERC();

            mainController.setCurrentGenerator(generator);
            mainController.setCurrentSERC(serc);
            mainController.showSERC();
            mainController.showSERCSummary();
            Stage window = (Stage) finalGenerateButton.getScene().getWindow();
            window.close();

        } catch (Exception e){
            e.printStackTrace();
            createAlert(e.getMessage());
            numberOfCasualtiesArea.clear();
        }
    }

    public void checkSERCType(){
        if(!drySERCMenuButton.isDisabled() && !wetSERCMenuButton.isDisabled()){
            //One of the two buttons aren't disabled, that means the user doesn't care what type of SERC
            boolean chanceADry = Math.random() < 0.5;
            if(chanceADry){
                drySERCType = randomDrySERC();
                isWaterBased = false;
            }
            else{
                bodyOfWater = randomWetSERC();
                isWaterBased = true;
            }
        }
    }

    //User does not care what type of phone, or they have already chosen and it needs adding
    public void checkPhone(){
        if (phoneMenuButton.getText().equals("Phone")){
            phoneType = randomPhoneFAType();
        }
        if(!(phoneType.equals(PhoneFaType.NONE))){aids.add(new Aid(phoneType, "Phone"));}
    }
    //User does not care what type of first aid kit, or they have already chosen and it needs adding
    public void checkFA(){
        if(firstAidMenuButton.getText().equals("FA")){
            faType = randomPhoneFAType();
        }
        if(!(faType.equals(PhoneFaType.NONE))){aids.add(new Aid(faType, "First Aid Kit"));}
    }

    public void checkAids(){
        //Check the phone, check the first aid kit and other aids that may be needed
        aids = new ArrayList<>();
        checkPhone();
        checkFA();

    }

    public void mapConditionsToCasualties() throws Exception{
        //First we will check the number of casualties that has been given
        int numberOfCasualties;
        try{
            numberOfCasualties = Integer.parseInt(numberOfCasualtiesArea.getCharacters().toString());

            //Checks for when the user is pushing the constraints
            if((conditionsArray.contains(ConditionType.SPINAL)) && (numberOfCasualties > 10)){throw new Exception("SERC will be hard with casualties greater than 10 including a spinal");}
            if(conditionsArray.contains(ConditionType.LOCKED) && numberOfCasualties < 2){throw new Exception("Minimum of two casualties is needed when having locked swimmers");}
        } catch (NumberFormatException e){
            //User has violated a constraint, need to pick a random number of casualties.
            if (conditionsArray.contains(ConditionType.SPINAL) && conditionsArray.contains(ConditionType.LOCKED)){
                numberOfCasualties = (int) Math.floor(Math.random() * 10) + 3;
            }
            else if(conditionsArray.contains(ConditionType.SPINAL)){
                numberOfCasualties = (int) Math.floor(Math.random() * 10) + 1;
            }
            else if(conditionsArray.contains(ConditionType.LOCKED)){
                //We need a minimum of two casualties
                numberOfCasualties = (int) Math.floor(Math.random() * 14) + 2;
            }
            else{
                numberOfCasualties = (int) Math.floor(Math.random() * 15) + 1;
            }
        }

        //User has done too much or too little number of casualties
        if(numberOfCasualties > 15){throw new Exception("Number of Casualties is quite large");}
        if(numberOfCasualties == 0){throw new Exception("Number of Casualties cannot be zero");}

        //init the casualty array for mapping
        casualties = new ArrayList<Casualty>();
        for(int i = 0; i < numberOfCasualties; i ++){
            casualties.add(new Casualty());
        }


        //We have more conditions ticked than we do casualties
        if(conditionsArray.size() > numberOfCasualties){
            System.out.println("Number of conditions greater than the number of casualties");
            for(Casualty casualty : casualties){
                int indexForRandomCondition = (int) Math.floor(Math.random() * conditionsArray.size());
                ConditionType randomCondition = conditionsArray.get(indexForRandomCondition);
                String randomConditionString = conditionsInfoArray.get(indexForRandomCondition);
                casualty.addCondition(randomCondition, randomConditionString);
                conditionsArray.remove(indexForRandomCondition);
                conditionsInfoArray.remove(indexForRandomCondition);
            }
            //We have more conditions than we do casualties, lets add them
            do{
                int indexForRandomCondition = (int) Math.floor(Math.random() * conditionsArray.size());
                ConditionType randomCondition = conditionsArray.get(indexForRandomCondition);
                String randomConditionString = conditionsInfoArray.get(indexForRandomCondition);
                int indexForRandomCasualty = (int) Math.floor(Math.random() * numberOfCasualties);
                Casualty casualtyAtIndex = casualties.get(indexForRandomCasualty);
                casualtyAtIndex.addCondition(randomCondition, randomConditionString);
                casualties.set(indexForRandomCasualty, casualtyAtIndex);
                conditionsArray.remove(indexForRandomCondition);
                conditionsInfoArray.remove(indexForRandomCondition);
            }while(!(conditionsArray.isEmpty()));
        }
        //We have equal number of conditions to casualties
        else if(conditionsArray.size() == numberOfCasualties){
            System.out.println("Number of conditions and number of casualties are the same");
            //array blank, just add the conditions to each casualty in order
            for(int i = 0; i < numberOfCasualties; i++){
                Casualty casualtyWithCondition = casualties.get(i);
                casualtyWithCondition.addCondition(conditionsArray.get(i), conditionsInfoArray.get(i));
            }
        }

        else /*(conditionsArray.size() < numberOfCasualties)*/{
            System.out.println("Number of casualties greater than the number of conditions");
            int numberOfConditions = conditionsArray.size();
            for(int i = 0; i < conditionsArray.size(); i++){
                Casualty casualty = casualties.get(i);
                casualty.addCondition(conditionsArray.get(i),conditionsInfoArray.get(i));
            }
            for(int i = numberOfConditions; i < numberOfCasualties; i++){
                Casualty casualty = casualties.get(i);
                ConditionType cond = randomCondition();
                casualty.addCondition(cond, "");
            }
        }
    }

    /*      compileConditions()

            Checks all the checkboxes in the middle section if they are ticked, then adds them
            to the conditions array. This is done before being mapped to the casualties.
     */
    public void compileConditions(){
        conditionsArray = new ArrayList<ConditionType>();
        conditionsInfoArray = new ArrayList<String>();
        //this method is going to go through all the casualties and format into a nice list for us to use
        /*              BREATHING PANE          */
        if(breathingAna.isSelected()){conditionsArray.add(ConditionType.ANAPHYL); conditionsInfoArray.add(breathingAnaText.getText());}
        if(breathingChockFull.isSelected()){conditionsArray.add(ConditionType.CHOCKFULL); conditionsInfoArray.add(breathingChockFullText.getText());}
        if(breathingChockPart.isSelected()){conditionsArray.add(ConditionType.CHOCKPART); conditionsInfoArray.add(breathingChockPartText.getText());}
        if(breathingHypervent.isSelected()){conditionsArray.add(ConditionType.HYPERVENT); conditionsInfoArray.add(breathingHyerventText.getText());}
        if(breathingAsthma.isSelected()){conditionsArray.add(ConditionType.ASTHMA); conditionsInfoArray.add(breathingAsthmaText.getText());}

        /*              COUNCIOUS PANE           */
        if(conBreathing.isSelected()){conditionsArray.add(ConditionType.UB); conditionsInfoArray.add(conBreathingText.getText());}
        if(conNotBreathing.isSelected()){conditionsArray.add(ConditionType.UNB); conditionsInfoArray.add(conNonBreathingText.getText());}
        if(conFaint.isSelected()){conditionsArray.add(ConditionType.FAINT); conditionsInfoArray.add(conFaintText.getText());}
        if(conSeizure.isSelected()){conditionsArray.add(ConditionType.SEIZURE); conditionsInfoArray.add(conSeizureText.getText());}


        /*              BREAKS / DISLOCATION PANE   */
        if(breakArm.isSelected()){conditionsArray.add(ConditionType.BROKEN); conditionsInfoArray.add(" Arm " + breakArmText.getText());}
        if(breakLeg.isSelected()){conditionsArray.add(ConditionType.BROKEN); conditionsInfoArray.add(" Leg " + breakLegText.getText());}
        if(breakOther.isSelected()){conditionsArray.add(ConditionType.BROKEN); conditionsInfoArray.add(breakOtherText.getText());}
        if(dislocKnee.isSelected()){conditionsArray.add(ConditionType.DISLOCATION); conditionsInfoArray.add(" Knee " + dislocKneeText.getText());}
        if(dislocAnk.isSelected()){conditionsArray.add(ConditionType.DISLOCATION); conditionsInfoArray.add(" Ankle " + dislocAnkText.getText());}
        if(dislocHip.isSelected()){conditionsArray.add(ConditionType.DISLOCATION); conditionsInfoArray.add(" Hip " + dislocHipText.getText());}
        if(dislocOther.isSelected()){conditionsArray.add(ConditionType.DISLOCATION); conditionsInfoArray.add(dislocOtherText.getText());}


        /*              SPRAIN / STRAIN PANE        */
        if(sprainElbow.isSelected()){conditionsArray.add(ConditionType.SPRAIN); conditionsInfoArray.add(" Elbow " + sprainElbowText.getText());}
        if(sprainAnk.isSelected()){conditionsArray.add(ConditionType.SPRAIN); conditionsInfoArray.add(" Ankle " + sprainAnkText.getText());}
        if(sprainWrist.isSelected()){conditionsArray.add(ConditionType.SPRAIN); conditionsInfoArray.add(" Wrist " + sprainWristText.getText());}
        if(sprainKnee.isSelected()){conditionsArray.add(ConditionType.SPRAIN); conditionsInfoArray.add(" Knee " + sprainKneeText.getText());}
        if(sprainOther.isSelected()){conditionsArray.add(ConditionType.SPRAIN); conditionsInfoArray.add(sprainOtherText.getText());}

        /*              MEDICAL CONDITION PANE      */
        if(medicalEpilepsy.isSelected()){conditionsArray.add(ConditionType.EPILEPSY); conditionsInfoArray.add(" - Suffering " + medicalEpilepsyText.getText());}
        else if(medicalEpilepsy.isIndeterminate()){conditionsArray.add(ConditionType.EPILEPSY); conditionsInfoArray.add(" - Not Suffering " + medicalEpilepsyText.getText());}

        if(medicalAsthma.isSelected()){conditionsArray.add(ConditionType.ASTHMA); conditionsInfoArray.add(" - Suffering " + medicalAsthmaText.getText());}
        else if(medicalAsthma.isIndeterminate()){conditionsArray.add(ConditionType.ASTHMA); conditionsInfoArray.add(" - Not Suffering " + medicalAsthmaText.getText());}

        if(medicalAllergies.isSelected()){conditionsArray.add(ConditionType.ALLERGIES); conditionsInfoArray.add(" - Suffering " + medicalAllergiesText.getText());}
        else if(medicalAllergies.isIndeterminate()){conditionsArray.add(ConditionType.ALLERGIES); conditionsInfoArray.add(" - Not Suffering " + medicalAllergiesText.getText());}

        if(medicalDiabetes.isSelected()){conditionsArray.add(ConditionType.DIABETIC); conditionsInfoArray.add(" - Suffering " + medicalDiabetesText.getText());}
        else if(medicalDiabetes.isIndeterminate()){conditionsArray.add(ConditionType.DIABETIC); conditionsInfoArray.add(" - Not Suffering " + medicalDiabetesText.getText());}

        if(medicalAngina.isSelected()){conditionsArray.add(ConditionType.ANGINA); conditionsInfoArray.add(" - Suffering " + medicalAnginaText.getText());}
        else if(medicalAngina.isIndeterminate()){conditionsArray.add(ConditionType.ANGINA); conditionsInfoArray.add("- Not Suffering " + medicalAnginaText.getText());}

        if(medicalOther.isSelected()){conditionsArray.add(ConditionType.MEDICOTHER); conditionsInfoArray.add(" - Suffering " + medicalOtherText.getText());}
        else if(medicalOther.isIndeterminate()){conditionsArray.add(ConditionType.MEDICOTHER); conditionsInfoArray.add(" - Not Suffering " + medicalOtherText.getText());}

        /*              BLEEDS PANE             */
        if(bleedChest.isSelected()){conditionsArray.add(ConditionType.BLEED); conditionsInfoArray.add(" - Chest " + bleedChestText.getText());}
        if(bleedHead.isSelected()){conditionsArray.add(ConditionType.BLEED); conditionsInfoArray.add(" - Head " + bleedHeadText.getText());}
        if(bleedEmbed.isSelected()){conditionsArray.add(ConditionType.BLEED); conditionsInfoArray.add(" - Embedded " + bleedEmbedText.getText());}
        if(bleedArm.isSelected()){conditionsArray.add(ConditionType.BLEED); conditionsInfoArray.add(" - Arm " + bleedArmText.getText());}
        if(bleedOther.isSelected()){conditionsArray.add(ConditionType.BLEED); conditionsInfoArray.add(" " + bleedOtherText.getText());}


        /*              DROWNING PANE           */
        if(drownWeak.isSelected()){conditionsArray.add(ConditionType.WEAKSWIM); conditionsInfoArray.add(" " + drownWeakText.getText());}
        if(drownNon.isSelected()){conditionsArray.add(ConditionType.NONSWIM); conditionsInfoArray.add(" " + drownNonText.getText());}
        if(downLocked.isSelected()){conditionsArray.add(ConditionType.LOCKED); conditionsInfoArray.add(" " + drownLockedText.getText()); conditionsArray.add(ConditionType.LOCKED); conditionsInfoArray.add(" " + drownLockedText.getText());}
        if(drownOther.isSelected()){conditionsArray.add(ConditionType.DROWNOTH); conditionsInfoArray.add(" " + downOtherText.getText());}

        /*              OTHERS PANE             */
        if(otherHeat.isSelected()){conditionsArray.add(ConditionType.HEATSTROK); conditionsInfoArray.add(" " + otherHeatText.getText());}
        if(otherHypotherm.isSelected()){conditionsArray.add(ConditionType.HYPOTHERM); conditionsInfoArray.add(" " + otherHypothermText.getText());}
        if(otherSpinal.isSelected()){conditionsArray.add(ConditionType.SPINAL); conditionsInfoArray.add(" " + otherSpinalText.getText());}
        if(otherCrush.isSelected()){conditionsArray.add(ConditionType.CRUSH); conditionsInfoArray.add(" " + otherCrushText.getText());}
        if(otherSting.isSelected()){conditionsArray.add(ConditionType.STING); conditionsInfoArray.add(" " + otherStingText.getText());}
        if(otherNotSuffering.isSelected()){conditionsArray.add(ConditionType.FINE); conditionsInfoArray.add(" " + otherNotSufferingText.getText());}
        if(otherOther.isSelected()){conditionsArray.add(ConditionType.OTHER); conditionsInfoArray.add(" " + otherOtherText.getText());}


    }

    // Random variable methods

    /*  randomCondition()

        give out a random condition, depending if the SERC is water based or not.
     */
    public ConditionType randomCondition(){
        int rand;
        ConditionType condition = ConditionType.OTHER;

        if(isWaterBased){rand = (int) Math.floor(Math.random() * 27); }
        else{ rand = (int) Math.floor(Math.random() * 24); }
        switch (rand){
            case 0: condition = ConditionType.CHOCKFULL; break;
            case 1: condition = ConditionType.CHOCKPART; break;
            case 2: condition = ConditionType.HYPERVENT; break;
            case 3: condition = ConditionType.ASTHMA; break;
            case 4: condition = ConditionType.ANAPHYL; break;
            case 5: condition = ConditionType.UB; break;
            case 6: condition = ConditionType.UNB; break;
            case 7: condition = ConditionType.FAINT; break;
            case 8: condition = ConditionType.SEIZURE; break;
            case 9: condition = ConditionType.BROKEN; break;
            case 10:condition = ConditionType.DISLOCATION; break;
            case 11:condition = ConditionType.SPRAIN; break;
            case 12:condition = ConditionType.MEDICOTHER; break;
            case 13:condition = ConditionType.EPILEPSY; break;
            case 14:condition = ConditionType.ALLERGIES; break;
            case 15:condition = ConditionType.DIABETIC; break;
            case 16:condition = ConditionType.ANGINA; break;
            case 17:condition = ConditionType.BLEED; break;
            case 18:condition = ConditionType.STING; break;
            case 19:condition = ConditionType.FINE; break;
            case 20:condition = ConditionType.SPINAL; break;
            case 21:condition = ConditionType.CRUSH; break;
            case 22:condition = ConditionType.HYPOTHERM; break;
            case 23:condition = ConditionType.HEATSTROK; break;
            //Water ones below, we do not allow locked
            case 24:condition = ConditionType.DROWNOTH; break;
            case 25:condition = ConditionType.WEAKSWIM; break;
            case 26:condition = ConditionType.NONSWIM; break;
        }
        return condition;
    }

    //Give a random wet SERC type
    public WaterType randomWetSERC(){
        //There are 5 possible types of wet serc possible
        int rand = (int) Math.floor(Math.random() * 5);
        switch (rand){
            case 0: return WaterType.POOL;
            case 1: return WaterType.BEACH;
            case 2: return WaterType.CANAL;
            case 3: return WaterType.LAKE;
            case 4: return WaterType.OCEAN;
        }
        return WaterType.POOL;
    }

    //Give a random dry SERC type
    public DryType randomDrySERC(){
        //There are 5 possible types of wet serc possible
        int rand = (int) Math.floor(Math.random() * 4);
        switch (rand){
            case 0: return DryType.INDOOR;
            case 1: return DryType.MULTIROOM;
            case 2: return DryType.OUTDOOR;
            case 3: return DryType.ROAD;
        }
        return DryType.INDOOR;
    }

    //Give a random Aid type
    public PhoneFaType randomPhoneFAType(){
        int rand = (int) Math.floor(Math.random() * 3);
        switch (rand){
            case 0: return PhoneFaType.INSERC;
            case 1: return PhoneFaType.OUTOFBOUNDS;
            case 2: return PhoneFaType.NONE;
        }
        return PhoneFaType.OUTOFBOUNDS;
    }

    //Something has happened, create an alert and display it modal (same method in main window has been used here)
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

        } catch (Exception ex){
            ex.printStackTrace();
        }
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
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }


} //END of generateController.java
