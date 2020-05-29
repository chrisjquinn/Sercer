package Backend;

import java.util.ArrayList;

import java.io.Serializable;

public class SERC implements Serializable{

	private static final long serialVersionUID = 1L;


	/* 				SERC.java
*		Author: Chris Quinn
*		Date: March 2020
* 		Notes: 	This is the SERC object that is output by SERCGenerator.java
*				and is saved as a .serc file by the software
*
*		Structure:
*		- Main variables
*		- Constructors
*		- Setters & Getters
*		- Print function
*/

	/* 					MAIN VARIABLES			*/
	/*	These are variables so that we can query the SERC, display its properites and maybe find new ones */

	private boolean wasCreated;

	private int numberOfCasualties;
	private int numberOfAids;

	//SERC properties, what type it is
	private boolean isWaterBased;		
	private WaterType bodyOfWater; 		
	private DryType drySERCType;

	//SERC grid, casualties and aids
	private int length; 
	private int width;
	private ArrayList<Casualty> casualties;
	private ArrayList<Aid> aids;
	public SERCObject[][] grid;	
	/* where each element is a SERCObject, of type (with int representations):
		0 	-	EMPTY
		1	-	WALL
		2	-	CASUALTY
		3	-	AID
		4	-	DANGER / OUT OF BOUNDS
		5	-	WATER (Body of water)
		6 	- 	ROAD 
	*/


	//Metadata of the SERC
	private String timeOfCreation;
	private String timeOfSERC;
	private String location;
	private String weather;
	private String notes;
	private String brief;



	/*		-------------------  CONSTRUCTORS   -------------------- 			 	*/	

	public SERC(){}

	//Construct SERC object once a grid has been given
	public SERC(SERCObject[][] input){
		this.grid = input;
		this.width = grid[0].length;
		this.length = grid[1].length;
	}

	//Construct SERC object once we have grid, casualties and aids (used by generateController.java in Frontend)
	public SERC(SERCObject[][] gridIn, ArrayList<Casualty> casIn, ArrayList<Aid> aidIn){
		this.grid = gridIn;
		this.width = grid[0].length;
		this.length = grid[1].length;
		this.casualties = casIn;
		this.aids = aidIn;
	}


	/*			----------------   GETTERS    --------------------------			*/
	//Simple getters
	public boolean getWasCreated(){return this.wasCreated;}
	public int getWidth(){return this.width;}
	public int getLength(){return this.length;}
	public SERCObject getCell(Point p){return this.grid[p.x][p.y];}
	public SERCObject getCell(int x, int y){return this.grid[x][y];}
	public ArrayList<Casualty> getCasualties(){return this.casualties;}
	public ArrayList<Aid> getAids(){return this.aids;}
	public WaterType getBodyOfWater(){return this.bodyOfWater;}
	public boolean getIsWaterBased(){return this.isWaterBased;}
	public DryType getDrySERCType(){return this.drySERCType;}
	public String getLocation(){return this.location;}
	public String getWeather(){return this.weather;}
	public String getTimeOfSERC(){return this.timeOfSERC;}
	public String getTimeOfCreation(){return this.timeOfCreation;}
	public String getNotes(){return this.notes;}
	public String getBrief(){return this.brief;}
	
	/* 	getCasualtiesDescription()
	*
	*	returns a full description of all the casualties in the SERC.
	*/
	public String getCasualtiesDescription(){
		String fullDescription = new String();
		if (casualties == null){fullDescription = "";}
		else{
			for(int i = 0; i < casualties.size(); i++){
				fullDescription += "Casualty " + (i+1) + " "+casualties.get(i).getCasualtyDescription() + "\n";
			}
		}
		return fullDescription;
	}

	/* 	getAidsDescription()
	*
	*	returns a full description of all the casualties in the SERC.
	*/
	public String getAidsDescription(){
		String fullDescription = new String();
		if(aids == null){fullDescription = "";}
		else{
			for(int i = 0; i < aids.size(); i++){
				fullDescription += "Aid " + (i+1) + " "+aids.get(i).getDescription() + "\n";
			}
		}
		return fullDescription;
	}

	/* 	getFullSERCSummary()
	*
	*	returns a full description of the SERC. Casualties, aids, metadta.
	*/
	public String getFullSERCSummary(){
		String fullSummary;
		fullSummary = "";
		fullSummary += timeOfCreation + "\n\n";

		if(!(getWasCreated())){
			if (!(location.equals(""))) {fullSummary += "Location: " + location + "\n";}
			if (!(timeOfSERC.equals(""))) {fullSummary += "Time: "+ timeOfSERC + "\n";}
			if (!(weather.equals(""))) {fullSummary += "Weather: "+ weather + "\n\n";}
			fullSummary += "---------   	Casualty Descriptions   	---------\n" + getCasualtiesDescription() + "\n";
			fullSummary += "---------   			Aids  	         	---------\n" + getAidsDescription();
		}
		return fullSummary;
	}

	/*			----------------   SETTERS    --------------------------			*/

	public void setWasCreated(boolean b){this.wasCreated = b;}
	public void setNumberOfCasualties(int num){this.numberOfCasualties = num;}
	public void setNumberOfAids(int aids){this.numberOfAids = aids;}
	public void setWaterBody(WaterType wt){this.isWaterBased = true; bodyOfWater = wt;}
	public void setDrySERCType(DryType dt){this.drySERCType = dt;}
	public void setLocation(String s){this.location = s;}
	public void setTimeOfSERC(String s){this.timeOfSERC = s;}
	public void setWeather(String s){this.weather = s;}
	public void setTimeOfCreation(String s){this.timeOfCreation = s;}
	public void setNotes(String s){this.notes = s;}
	public void setIsWaterBased(boolean b){this.isWaterBased = b;}
	public void setBrief(String b){this.brief = b;}

	private void setGrid(SERCObject[][] input){
		this.grid = input;
		this.width = grid[0].length; // the x values, .length called as part of the primitive array definition
		this.length = grid[1].length; // the y values
		//This is used from the generator to put all the environments in play.
	}


	/*			----------------   PRINTING    --------------------------			*/
	//Print the current grid to the terminal, does not print locations of casualties and aids
	public void printGrid(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < length; j++){
				SERCObject currentCell = grid[i][j];
				System.out.print(currentCell.getIntValue());
				if (j == length -1){
					System.out.print("\n");
				}
			}
		}
	}




} //END of SERC.java