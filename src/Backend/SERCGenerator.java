package Backend;

import java.lang.Math;
import java.lang.Double;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;


/* 				SERCGenerator.java
*		Author: Chris Quinn
*		Date: March 2020
* 		Notes: 	This is the largest file in the Backend package,
*				SERC objects are created and mofdified with this.
*		
*		Structure:
*		- Main variables
*		- Constructors
*		- Setters & Getters
*		- The main method to generate a SERC, generateNewSERC()
*		- Further methods which set the bounding box, maps casualties, etc.
*/

public class SERCGenerator {
	
	/* 							MAIN VARIABLES 							
	*	These are (mostly) the same as the SERC class. Additional variables
	*	are for the box that is drawn for in-bounds and out-of-bounds. The 
	*	reason why metadata and variables of the SERC is stored is because
	*	the user may wish to re-load a SERC. So metadata is kept the same, 
	*	but the grid will change.
	*
	*/

	private int numberOfCasualties;
	private int numberOfAids;

	//SERC properties, what type it is
	private boolean isWaterBased;
	private WaterType bodyOfWater;
	private DryType drySERCType;

	//SERC grid, casualties and aids
	private int length; 
	private int width;
	private SERCObject[][] objectGrid;
	private ArrayList<Casualty> casualties;
	private ArrayList<Aid> aids;

	//Useful for sercs with a bounding box of out of bounds
	private Point startPoint;
	private Point endPoint;

	//Metadata of the SERC
	private String weather;
	private String location;
	private String timeOfSERC;
	private String notes;
	private String brief;


	/* 			CLASS CONSTRUCTORS 			*/
	public SERCGenerator(){
		//This is the default constructor for when we want the most random SERC, long as its not too rediculous
		//Note this constructor does not make the SERC itself 

	}
	//This constructor is called when the generate window is making a generator with the properties desired
	public SERCGenerator(int width, int length){
		this.width = width;
		this.length = length;
		this.objectGrid = new SERCObject[width][length];
	}
	//This constructor is called when a SERC is loaded into the software
	public SERCGenerator(SERC serc){
		this.width = serc.getWidth();
		this.length = serc.getLength();
		this.objectGrid = new SERCObject[width][length];
		this.casualties = serc.getCasualties();
		this.aids = serc.getAids();
		this.isWaterBased = serc.getIsWaterBased();
		this.bodyOfWater = serc.getBodyOfWater();
		this.drySERCType = serc.getDrySERCType();
		this.weather = serc.getWeather();
		this.location = serc.getLocation();
		this.timeOfSERC = serc.getTimeOfSERC();
		this.notes = serc.getNotes();
		this.brief = serc.getBrief();
	}

	/* 			SETTERS 		*/

	public void setCasualties(ArrayList<Casualty> casualtiesIn){ this.casualties = casualtiesIn; this.numberOfCasualties = casualtiesIn.size();}
	public void setAids(ArrayList<Aid> aidsIn){ this.aids = aidsIn; this.numberOfAids = aidsIn.size();}
	public void setDrySERCType(DryType type){this.drySERCType = type; this.isWaterBased = false;}
	public void setWetSERCType(WaterType type){this.bodyOfWater = type; this.isWaterBased = true;}
	public void setWeather(String s){this.weather = s;}
	public void setTimeOfSERC(String s){this.timeOfSERC = s;}
	public void setLocation(String s){this.location = s;}
	public void setNotes(String s){this.notes = s;}
	public void setBrief(String b){this.brief = b;}


	/* 			GETTERS 		*/
	public String getLocation(){return this.location;}
	public String getWeather(){return this.weather;}
	public String getTimeOfSERC(){return this.timeOfSERC;}


	/* 	generateNewSERC()
	*	outputs a SERC object, calls all other methods beneath it. 
	*	firstly queries if the SERC in question is water-based or not,
	*	then goes about creating the map and putting the objets in
	*/
	public SERC generateNewSERC(){
		objectGrid = new SERCObject[width][length];
		if(isWaterBased){
			genWetSERC();
		}else{
			genDrySERC();
		}
		addCasualtiesToGrid();
		addAidsToGrid();
		SERC serc = new SERC(objectGrid, casualties, aids);
		if(isWaterBased){
			serc.setIsWaterBased(true);
			serc.setWaterBody(bodyOfWater);
		}
		else {
			serc.setIsWaterBased(false);
			serc.setDrySERCType(drySERCType);
		}
		serc.setWasCreated(false);
		serc.setWeather(weather);
		serc.setLocation(location);
		serc.setTimeOfSERC(timeOfSERC);
		serc.setTimeOfCreation(addTimeStamp());
		serc.setBrief(brief);

		return serc;
	}

	//We create a box with some water, depending on what type of water that is
	public void genWetSERC(){
		setBoundingBoxWithBodyOfWater(bodyOfWater);

	}

	/*	We create a box for each type of dry SERC in question, sometimes we will 
	*	add some additional stuff inside, such as pillars or danger
	*/
	public void genDrySERC(){
		switch(drySERCType){
			case INDOOR:
				setBoundingBoxWithWall();
				if((Math.random() < 0.5)){addInsideWallFeatures();}
				break;
			case MULTIROOM:
				//Multiroom method varies by being recursive, its start and end need to be given
				startPoint = new Point(1,1);
				endPoint = new Point((int) Math.floor(Math.random() * (width/2)) + (width/2), (int) Math.floor(Math.random() * (length/2)) + (length/2)); // Change this to be of a varying size
				setBoundingMultiWall(startPoint, endPoint,(int) Math.floor(Math.random() * 5) + 2 , Math.random() < 0.5);
				//The rest of the out-of-bounds is now rendered
				fillTheGaps();
				break;
			case OUTDOOR:
				setBoundingBox();
				boolean toinCoss = (Math.random() < 0.5);
				if(toinCoss){addDanger();}
				break;
			case ROAD:
				setRoad(randomRoad());
				break;
		}
	}


	/* 	Bounding Box methods:
	*	These methods are called upon by all the types of SERCs, to render an in-bounds area 
	*	and an out-of bounds area. Sometimes additional information is needed for inside. This
	*	could be bodies of water or roads. Their methods are as follows.
	*
	*
	*	setBoundingBox()
	*
	*	The most basic, we choose a randomly sized box inside the width and length of the SERC.
	*	This box then dictates the in-bounds and the out-of-bounds areas.
	*
	*/

	public void setBoundingBox(){
		int dx = 0;
		int dy = 0;

		do{
			startPoint = new Point(1,1);
			dx = (int) Math.floor((width - 1 - startPoint.x) * Math.random()) + 3;
			dy = (int) Math.floor((length - 1 - startPoint.y) * Math.random()) + 3;
		} while (! ((dy + startPoint.y < length) && (dx + startPoint.x < (width))));
		endPoint = new Point(startPoint.x + dx, startPoint.y + dy);

		for(int i = 0; i < width; i++){
			for(int j = 0; j < length; j++){
				if(((i > startPoint.x-1) && (i < startPoint.x + dx -1) && (j > startPoint.y-1) && (j < startPoint.y + dy -1))){
					objectGrid[i][j] = new Empty(new Point(i,j));
				}
				else{
					objectGrid[i][j] = new OutOfBounds(new Point(i,j));
				}
			}
		}
		//Now we choose a random point to be the entry point
		Point[] boundaries = {
				new Point(startPoint.x + (int) Math.floor(Math.random() * dx), startPoint.y),
				new Point(startPoint.x, startPoint.y + (int) Math.floor(Math.random() * dy)),
				new Point(startPoint.x + (int) Math.floor(Math.random() * dx), endPoint.y),
				new Point(endPoint.x, startPoint.y + (int) Math.floor(Math.random() * dy))};
		Point entry = boundaries[(int) Math.floor(Math.random() * 4)];
		objectGrid[entry.x-1][entry.y-1] = new Empty(entry);
	}


	/*	setBoundingBox(int minWidth, int minLength)
	*
	*	Creates a bounding box that is at least a soecific width and length.
	*	Very similar to the method above, but ensures it cannot be smaller
	*	than the width and length passed to it.
	*
	*/
	public void setBoundingBox(int minWidth, int minLength){
		int dx = 0;
		int dy = 0;

		do{
			startPoint = new Point(1,1);
			dx = (int) Math.floor((width - 1 - startPoint.x) * Math.random()) + minWidth;
			dy = (int) Math.floor((length - 1 - startPoint.y) * Math.random()) + minLength;
		} while (! ((dy + startPoint.y < length) && (dx + startPoint.x < (width)) && (dx >= minWidth) && (dy >= minLength)));
		endPoint = new Point(startPoint.x + dx, startPoint.y + dy);

		for(int i = 0; i < width; i++){
			for(int j = 0; j < length; j++){
				if(((i > startPoint.x-1) && (i < startPoint.x + dx) && (j > startPoint.y-1) && (j < startPoint.y + dy))){
					objectGrid[i][j] = new Empty(new Point(i,j));
				}
				else{
					objectGrid[i][j] = new OutOfBounds(new Point(i,j));
				}
			}
		}
		//Now we choose a random point to be the entry point
		Point[] boundaries = {
				new Point(startPoint.x + (int) Math.floor(Math.random() * dx), startPoint.y),
				new Point(startPoint.x, startPoint.y + (int) Math.floor(Math.random() * dy)),
				new Point(startPoint.x + (int) Math.floor(Math.random() * dx), endPoint.y),
				new Point(endPoint.x, startPoint.y + (int) Math.floor(Math.random() * dy))};
		Point entry = boundaries[(int) Math.floor(Math.random() * 4)];
		objectGrid[entry.x-1][entry.y-1] = new Empty(entry);
	}	

	/*	setBoundingBoxWithWall()
	*	
	*	The same principles of the setBoundingBox() is used here. But
	*	a wall is set as a boundary between the in-bounds area and the 
	*	out-of-bounds area. 
	*
	*/
	public void setBoundingBoxWithWall(){
		int dx = 0;
		int dy = 0;

		do{
			startPoint = new Point((int) Math.floor((width-1) * Math.random()) + 1, (int) Math.floor((length-1) * Math.random()) + 1);
			dx = (int) Math.floor((width - 1 - startPoint.x) * Math.random()) + 3;
			dy = (int) Math.floor((length - 1 - startPoint.y) * Math.random()) + 3;
		} while (! ((dy + startPoint.y < length) && (dx + startPoint.x < (width))));
		endPoint = new Point(startPoint.x + dx, startPoint.y + dy);

		for(int i = 0; i < width; i++){
			for(int j = 0; j < length; j++){
				if(((i > startPoint.x) && (i < startPoint.x + dx) && (j > startPoint.y) && (j < startPoint.y + dy))){
					objectGrid[i][j] = new Empty(new Point(i,j));
				}
				else if (((i == startPoint.x && j >= startPoint.y && j<= startPoint.y + dy) || (i == startPoint.x + dx && j>= startPoint.y && j <= startPoint.y + dy)  || (j == startPoint.y && i >= startPoint.x && i <= startPoint.x + dx) || (j == startPoint.y + dy && i>= startPoint.x && i <= startPoint.x + dx))){
					objectGrid[i][j] = new Wall(new Point(i,j));
				}
				else{
					objectGrid[i][j] = new OutOfBounds(new Point(i,j));
				}
			}
		}
		//Now we choose a random point to be the entry point
		Point[] boundaries = {
				new Point(startPoint.x + (int) Math.floor(Math.random() * dx), startPoint.y),
				new Point(startPoint.x, startPoint.y + (int) Math.floor(Math.random() * dy)),
				new Point(startPoint.x + (int) Math.floor(Math.random() * dx), endPoint.y),
				new Point(endPoint.x, startPoint.y + (int) Math.floor(Math.random() * dy))};
		Point entry = boundaries[(int) Math.floor(Math.random() * 4)];
		objectGrid[entry.x][entry.y] = new Empty(entry);
	}


	/*	setBoundingBoxWithWall(Point start, Point end)
	*	
	*	This method now takes co-ordinates of a start and end, meaning
	*	that there is no randomization involved. We simply draw a box
	*	with a wall from the start to the end in the grid.	*
	*
	*/

	public void setBoundingBoxWithWall(Point start, Point end){
		for(int i = start.x; i <= end.x; i++){
			for(int j = start.y; j <= end.y; j++){
				if(((i > start.x) && (i < end.x) && (j > start.y) && (j < end.y))){
					objectGrid[i][j] = new Empty(new Point(i,j));
				}
				else if (((i == start.x && j >= start.y && j<= end.y) || (i == end.x && j>= start.y && j <= end.y)  || (j == start.y && i >= start.x && i <= end.x) || (j == end.y && i>= start.x && i <= end.x))){
					objectGrid[i][j] = new Wall(new Point(i,j));
				}
				else{
					if(!(objectGrid[i][j] instanceof Wall) && !(objectGrid[i][j] instanceof Empty)){
						objectGrid[i][j] = new OutOfBounds(new Point(i,j));
					}
				}
			}
		}
		//Now we choose a random point to be the entry point
		Point[] boundaries = {
				new Point(start.x + (int) Math.floor(Math.random() * (end.x-start.x-1)) +1, start.y),
				new Point(start.x, start.y + (int) Math.floor(Math.random() * (end.y-start.y-1)) +1),
				new Point(start.x + (int) Math.floor(Math.random() * (end.x-start.x-1)) +1, end.y),
				new Point(end.x, start.y + (int) Math.floor(Math.random() * (end.y-start.y-1)) +1)};
		Point entry = boundaries[(int) Math.floor(Math.random() * 4)];
		objectGrid[entry.x][entry.y] = new Empty(entry);
	}


	/*	setBoundingMultiWall(Point start, Point end, int numberOfRooms, boolean recurive)
	*		Point start: the startpoint of the room with walls
	*		Point end: the endpoint of the room with walls
	*		int numberOfRooms: 	how many more rooms are needed to be made
	*		boolean recursive: if the type of multi-wall construction is the spiral-based recursive type or not
	*
	*	This method uses the setBoundingBoxWithWall(Point start, Point end) to use. 
	*	Here, a multi-room is created with two types of construction, spiral based or through the edges and
	*	corners of the room.
	*
	*	If spiral based, the method picks a random horizontal or vertical line for a partition, then calls the method again
	*	If not, then edges and potential points are chosen to be put along the inside of the main room.
	*/

	public void setBoundingMultiWall(Point start, Point end, int numberOfRooms, boolean recursive){
		if(recursive){
			setBoundingBoxWithWall(start, end);
			//Then choose a random point and set it to empty
			if(numberOfRooms > 0) {
				boolean hSplit = Math.random() < 0.5;
				if (hSplit) {
					int vCoord = start.y + (int) Math.floor(Math.random() * (end.y - start.y ) + 1);
					setBoundingMultiWall(new Point(start.x, vCoord), end, numberOfRooms-1, true);
				} else {
					int hCoord = start.x + (int) Math.floor(Math.random() * (end.x - start.x) + 1);
					setBoundingMultiWall(new Point(hCoord, start.y), end, numberOfRooms-1, true);
				}
			}
		}else{
			setBoundingBoxWithWall(start, end);
			int distX = end.x - start.x;
			int distY = end.y - start.y;
			ArrayList<Point> startPotentials = new ArrayList<Point>(Arrays.asList(
				start, 
				new Point(start.x + distX/2 + (int) Math.floor(Math.random() * distX/2), start.y),
				new Point(start.x, start.y + (int) Math.floor(Math.random() * distY/2)) , 
				new Point(start.x +  distX/2 + (int) Math.floor(Math.random() * distX/2), start.y + (int) Math.floor(Math.random() * distY/2)),
				new Point(start.x, start.y + distY/2 + (int)Math.floor(Math.random() * distY/2)) ,
				new Point(start.x + distX/2 + (int)Math.floor(Math.random() * distX/2), start.y + distY/2 + (int)Math.floor(Math.random() * distY/2))
			));

			ArrayList<Point> endPotentials = new ArrayList<Point>(Arrays.asList(
				new Point(start.x + (int) Math.floor(Math.random() * distX/2), start.y + (int) Math.floor(Math.random() * distY/2)), 
				new Point(end.x, start.y + (int) Math.floor(Math.random() * distY/2)), 
				new Point(start.x + (int) Math.floor(Math.random() * distX/2), start.y + distY/2 + (int)Math.floor(Math.random() * distY/2)),
				new Point(end.x, start.y + distY/2 + (int)Math.floor(Math.random() * distY/2)),
				new Point(start.x + (int) Math.floor(Math.random() * distX/2), end.y),
				end
			));

			for(int i = 1; i <= numberOfRooms; i++){
				int indexOfNextRoom = (int) Math.floor(Math.random() * startPotentials.size());
				Point interiorstart = startPotentials.get(indexOfNextRoom);
				Point interiorend = endPotentials.get(indexOfNextRoom);
				startPotentials.remove(indexOfNextRoom);
				endPotentials.remove(indexOfNextRoom);
				setBoundingBoxWithWall(interiorstart, interiorend);
			}
		}

	}

	/*	setBoundingWithBodyOfWater(WaterType wt)
	*		WaterType wt: The body of water that is for the type of SERC used
	*
	*	This method uses the other mbox methods and adds some water in. 
	*	Each body of water defined from either lines or the equation of the ellipse.
	*
	*/

	public void setBoundingBoxWithBodyOfWater(WaterType wt){
		//Make the bounding area with some empty and water, store the variables.
		switch (wt){
			case OCEAN:
				//Set all of the empty to be water, some danger allowed inside (e.g. a boat that has capsized)
				setBoundingBox();
				for(int i = 0; i < width; i++){
					for(int j = 0; j < length; j++){
						if(objectGrid[i][j] instanceof Empty){
							objectGrid[i][j] = new Water(new Point(i,j));
						}
					}
				}
				break;
			case POOL:
				// Pools have walls on the outside by the bounding box
				setBoundingBoxWithWall();
				int dx = endPoint.x - startPoint.x;
				int dy = endPoint.y - startPoint.y;
				Point waterStartPoint = new Point(startPoint.x, startPoint.y);
				int wdx = waterStartPoint.x + 2;
				int wdy = waterStartPoint.y + 2;
				do{
					waterStartPoint = new Point((int) Math.ceil((dx) * Math.random()) + startPoint.x, (int) Math.ceil((dy) * Math.random()) + startPoint.y);
					wdx = (int) Math.floor((dx) * Math.random()) + 2;
					wdy = (int) Math.floor((dy) * Math.random()) + 2;
				} while (!( (wdx + waterStartPoint.x <= startPoint.x + dx) && (wdy + waterStartPoint.y <= startPoint.y + dy) && (wdx <= dx) && (wdy <= dy)));

				for(int i = waterStartPoint.x-1; i < waterStartPoint.x + wdx; i++){
					for(int j = waterStartPoint.y-1; j < waterStartPoint.y + wdy; j++){
						if(objectGrid[i][j] instanceof Empty){
							objectGrid[i][j] = new Water(new Point(i,j));
						}
					}
				}
				break;
			case BEACH:
				//Bit easier, need to pick to points on either side of the bounding and set one half as water
				setBoundingBox();
				int distX = endPoint.x - startPoint.x;
				int distY = endPoint.y - startPoint.y;

				Point[] boundaryChoices = { new Point(startPoint.x, startPoint.y + (int) Math.floor((distY-2) * Math.random()) + 2) , new Point(startPoint.x + (int) Math.floor((distX-2) * Math.random()) + 2, startPoint.y) , 
											new Point(endPoint.x , endPoint.y - (int) Math.floor((distY-2) * Math.random()) - 2)  , new Point(endPoint.x - (int) Math.floor((distX-2) * Math.random()) - 2,endPoint.y) };
				Point p0, p1;
				Double grad;
				do{
					p0 = boundaryChoices[(int) Math.floor(4 * Math.random())];
					p1 = boundaryChoices[(int) Math.floor(4 * Math.random())];
					double ydiff = (p1.y - p0.y);
					double xdiff = (p1.x - p0.x);
					grad = Double.valueOf( ydiff/xdiff );
				} while (p0.equals(p1) && (p1.x >= p0.x));
				
				if(Double.isInfinite(grad)){
					//We have a vertical line
					//Can also say p0.y == p1.y here
					for(int i = startPoint.x-1; i <= endPoint.x; i++){
						for(int j = startPoint.y-1; j <= endPoint.y; j++){
							if((i <= p0.x -1) && (objectGrid[i][j] instanceof Empty)){
								objectGrid[i][j] = new Water(new Point(i,j));
							}
						}
					}
				}
				else{
					// The next part uses the equation of the line to determine which cells become water and which stay as land
					double c = (p0.y - p0.x*(grad.doubleValue()));
					/* 		Determination of being water or not for a new Point object w:
					 * 		y = mx + c 		<==> 	0 = grad*(w.x) + c - w.y 		(if w lies on the line)
					 * 		use < to say it is land and <= to say it is water 
					 */
					for(int i = startPoint.x-1; i <= endPoint.x; i++){
						for(int j = startPoint.y-1; j <= endPoint.y; j++){
							if((((grad.doubleValue() * i) + c - j) <= 0) && (objectGrid[i][j] instanceof Empty)){
								objectGrid[i][j] = new Water(new Point(i,j));
							}
						}
					}
				}
				break;
			case CANAL:
				//Taking the beach approach with two lines, they should be parallel
				setBoundingBox();
				int disX = endPoint.x - startPoint.x;
				int disY = endPoint.y - startPoint.y;

				Point[] boundaryChoic = { new Point(startPoint.x, startPoint.y + (int) Math.floor((disY-5) * Math.random()) + 2) , new Point(startPoint.x + (int) Math.floor((disX-5) * Math.random()) + 2, startPoint.y) , 
											new Point(endPoint.x , endPoint.y - (int) Math.floor((disY-5) * Math.random()) - 2)  , new Point(endPoint.x - (int) Math.floor((disX-5) * Math.random()) - 2,endPoint.y) };
				Point point0, point1, point2, point3;
				Double gradient1, gradient2;
				double xdiff, ydiff;
				do{
					point0 = boundaryChoic[(int) Math.floor(4 * Math.random())];
					point1 = boundaryChoic[(int) Math.floor(4 * Math.random())];
					ydiff = (point1.y - point0.y);
					xdiff = (point1.x - point0.x);
					gradient1 = Double.valueOf( ydiff/xdiff );
				} while (point0.equals(point1) || (xdiff == 1) || (ydiff == 1));
				
				if(Double.isInfinite(gradient1)){
					//We have a vertical line
					do{
						point2 = new Point(point0.x + (int) Math.floor((endPoint.x - point0.x -2) * Math.random() + 1), point0.y);
						point3 = new Point(point1.x + (int) Math.floor((endPoint.x - point1.x -2) * Math.random() + 1), point1.y);
					} while(point2.equals(point0) && point3.equals(point1) && point3.equals(point2));

					for(int i = startPoint.x-1; i <= endPoint.x; i++){
						for(int j = startPoint.y-1; j <= endPoint.y; j++){
							if((i > point0.x -1) && (objectGrid[i][j] instanceof Empty) && (i < point2.x -1)){
								objectGrid[i][j] = new Water(new Point(i,j));
							}
						}
					}
					break;
				}
				else if (point0.y == point1.y){
					//We have a horizontonal line
					//Equivilently, gradient = 0 here.
					//Same procedure to the previous case but we are flipping x and y
					do{
						point2 = new Point(point0.x, point0.y + (int) Math.floor((endPoint.y - point0.y -2) * Math.random() + 1));
						point3 = new Point(point1.x, point1.y + (int) Math.floor((endPoint.y - point1.y -2) * Math.random() + 1));
					} while(point2.equals(point0) || point3.equals(point1) || point3.equals(point2));

					for(int i = startPoint.x-1; i <= endPoint.x; i++){
						for(int j = startPoint.y-1; j <= endPoint.y; j++){
							if((j > point0.y -1) && (objectGrid[i][j] instanceof Empty) && (j < point2.y -1)){
								objectGrid[i][j] = new Water(new Point(i,j));
							}
						}
					}
					break;
				}
				else{
					// The next part uses the equation of the line to determine which cells become water and which stay as land
					double c1 = (point0.y - point0.x*(gradient1.doubleValue()));
					// /* 		Determination of being water or not for a new Point object w:
					//  * 		y = mx + c 		<==> 	0 = grad*(w.x) + c - w.y 		(if w lies on the line)
					//  * 		use < to say it is land and <= to say it is water 
					//  */
					double hyp = Math.sqrt(Math.pow(disX,2) + Math.pow(disY,2));
					double c2;
					boolean testConditionForC2;
					do{
						if (gradient1.doubleValue() > 0){
							//the linear line is increasing
							c2 = c1 - (disY) + (int) (Math.random() * (Math.abs(disY*2)));
							testConditionForC2 = ((gradient1.doubleValue() * (startPoint.x)) + c2 < (endPoint.y)) && ((gradient1.doubleValue() * (endPoint.x)) + c2 > (startPoint.y));
						} else{
							//grad < 0, we have already dealt when it is = 0 or when infinity
							c2 = c1 - (disY) + (int) (Math.random() * (Math.abs(disY*2)));
							testConditionForC2 = ((gradient1.doubleValue() * (endPoint.x)) + c2 < (endPoint.y)) && ((gradient1.doubleValue() * (startPoint.x)) + c2 > (startPoint.y));
						}
					} while ((!testConditionForC2 || Math.abs(Math.abs(c2) - Math.abs(c1))<2));

					if (c1 > c2){
						for(int i = startPoint.x-1; i <= endPoint.x; i++){
							for(int j = startPoint.y-1; j <= endPoint.y; j++){
								if((((gradient1.doubleValue() * i) + c1) > j) && (objectGrid[i][j] instanceof Empty) && (((gradient1.doubleValue() * i) + c2) < j)){
									objectGrid[i][j] = new Water(new Point(i,j));
								}
							}
						}
					}
					else{ // c1 < c2 (cannot be equal)
						for(int i = startPoint.x-1; i <= endPoint.x; i++){
							for(int j = startPoint.y-1; j <= endPoint.y; j++){
								if((((gradient1.doubleValue() * i) + c1) < j) && (objectGrid[i][j] instanceof Empty) && (((gradient1.doubleValue() * i) + c2) > j)){
									objectGrid[i][j] = new Water(new Point(i,j));
								}
							}
						}
					}
					break;
				}

			case LAKE:
				//Need to draw the lake inside the serc and guarantee that land surrounds it
				setBoundingBox(5,5);
				int sizeX = endPoint.x - startPoint.x;
				int sizeY = endPoint.y - startPoint.y;
				Point midPoint;
				double xRadius, yRadius;
				do{
					midPoint = new Point((int) Math.floor((sizeX * Math.random()) + startPoint.x), (int) Math.floor((sizeY * Math.random()) + startPoint.y));
					xRadius = Math.floor(Math.random() * (endPoint.x - midPoint.x)) + 2;
					yRadius = Math.floor(Math.random() * (endPoint.y - midPoint.y)) + 2;
				}while(midPoint.equals(startPoint) || (midPoint.x - xRadius <= 1) || (midPoint.y - yRadius <= 1) || (midPoint.x + xRadius > endPoint.x) || (midPoint.y > endPoint.y));

				for(int i = startPoint.x -1; i < endPoint.x; i++){
					for(int j = startPoint.y-1 ; j < endPoint.y; j++){
						double xFrac = (Math.pow(i - midPoint.x ,2) / Math.pow(xRadius,2));
						double yFrac = (Math.pow(j - midPoint.y ,2) / Math.pow(yRadius,2));
						double inEllipse =  xFrac + yFrac;
						if((inEllipse < 1) && (objectGrid[i][j] instanceof Empty) ){
							objectGrid[i][j] = new Water(new Point(i,j));
						}
					}
				}
				break;
		}
	}


	/*	fillTheGaps()
	*	Only used when multi-wall method is used, some of the points in 
	*	the grid now haven't filled in (they're null) so this will give 
	*	these null points an instance
	*
	*/	
	public void fillTheGaps(){
		for(int i = 0; i < width ;i++){
			for(int j = 0; j < length; j++){
				if(!(objectGrid[i][j] instanceof Wall) && !(objectGrid[i][j] instanceof Empty)){
					objectGrid[i][j] = new OutOfBounds(new Point(i,j));
				}
			}
		}
	}


	/*		END OF THE BOUNDING BOX METHODS 	*/




	/*		INSIDE FEATURES 	*/

	/*	setRoad(RoadType rt)
	*		RoadType rt: Type of road to be put in the SERC
	*
	*	Method creates a bounding box, similar to that of the water methods. 
	*	Then, puts some type of road inside, whether this be giveway, corner 
	*	or a straight.
	*
	*/

	public void setRoad(RoadType rt){
		switch (rt){
			case STRAIGHT:
				setBoundingBoxWithBodyOfWater(WaterType.CANAL);
				for(int i = startPoint.x-1; i < endPoint.x; i++){
					for(int j = startPoint.y-1; j < endPoint.y; j++){
						if(objectGrid[i][j] instanceof Water){
							objectGrid[i][j] = new Road(new Point(i,j));
						}
					}
				}
			break;

			case GIVEWAY:
				setBoundingBox();
				Point p1, p2;
				do{
					p1 = new Point( (int) Math.floor((endPoint.x - startPoint.x - 1) * Math.random()) + 1, (int) Math.floor((endPoint.y - startPoint.y - 1) * Math.random()) + 1);
					p2 = new Point( (int) Math.floor((endPoint.x - startPoint.x - 1) * Math.random()) + 1, (int) Math.floor((endPoint.y - startPoint.y - 1) * Math.random()) + 1);
					// System.out.println("p1: "+p1.toString()+"            p2: "+p2.toString());
				} while (( (p1.equals(p2)) && Math.abs(p1.y - p2.y) < 1 ));
				if(p1.y == p2.y){
					if(p1.x > p2.x){
						Point tmp = new Point(p2);
						p2 = new Point(p1);
						p1 = new Point(tmp);
					}
					int yRoadHeight = (int) Math.floor((endPoint.y - p1.y - 1) * Math.random()) + 1;
					for(int i = startPoint.x-1; i < endPoint.x; i++){
						for(int j = startPoint.y -1; j < endPoint.y; j++){
							if (objectGrid[i][j] instanceof Empty && ( (i >= p1.x && i <= p2.x && j <= p1.y) || (j >= p1.y && j <= p1.y + yRoadHeight))){
								objectGrid[i][j] = new Road(new Point(i,j));
							}
						}

					}
				}
				else{ //this should be the only two options
					if(p1.y > p2.y){
						Point tmp = new Point(p2);
						p2 = new Point(p1);
						p1 = new Point(tmp);
					}
					int xRoadHeight = (int) Math.floor((endPoint.x - p1.x - 1) * Math.random()) + 1;
					for(int i = startPoint.x-1; i < endPoint.x; i++){
						for(int j = startPoint.y -1; j < endPoint.y; j++){
							if (objectGrid[i][j] instanceof Empty && ( (j >= p1.y && j <= p2.y && i <= p1.x) || (i >= p1.x && i <= p1.x + xRoadHeight))){ 
								objectGrid[i][j] = new Road(new Point(i,j));
							}
						}
					}
				}
			break;

			case CORNER:
				setBoundingBox();
				Point c1, c2;
				do{
					c1 = new Point( (int) Math.floor((endPoint.x - startPoint.x - 1) * Math.random()) + 1, (int) Math.floor((endPoint.y - startPoint.y - 1) * Math.random()) + 1);
					c2 = new Point( (int) Math.floor((endPoint.x - startPoint.x - 1) * Math.random()) + 1, (int) Math.floor((endPoint.y - startPoint.y - 1) * Math.random()) + 1);
				} while (!((c1.x != c2.x) && (c1.y != c2.y) && (c2.y < c1.y) && (c2.x > c1.x)));
				// System.out.println("c1: "+c1.toString() + ",       c2: "+c2.toString());
				for(int i = startPoint.x-1; i < endPoint.x; i++){
					for(int j = startPoint.y -1; j < endPoint.y; j++){
						if (objectGrid[i][j] instanceof Empty && ((i <= c2.x && j >= c2.y && j <= c1.y) || (i <= c2.x && i >= c1.x && j >= c2.y))){ 
							objectGrid[i][j] = new Road(new Point(i,j));
						}
					}
				}
			break;

		}		
	}


	/*	addInsideWallFeatures()
	*		For indoor SERC types, this will add some indoor wall features
	*		such as pillars or long walls (e.g. and aisle)
	*
	*	This method takes the indoor SERC that is already been made and then adds 
	*	either of the two.
	*
	*/
	public void addInsideWallFeatures(){
		//These are things such as pillars, inside walls
		boolean toinCoss = (Math.random() < 0.5);
		int distX = endPoint.x - startPoint.x;
		int distY = endPoint.y - startPoint.y;
		if (toinCoss){
			// we will put some random walls in like pillars
			int stepSize = (int) Math.floor(Math.max(distX, distY) * (1/2) * Math.random()) + 2;
			for (int i = startPoint.x +2; i < endPoint.x -1; i = i + stepSize){
				for (int j = startPoint.y + 2; j < endPoint.y -1; j = j + stepSize){
					if (objectGrid[i][j] instanceof Empty){
						objectGrid[i][j] = new Wall(new Point(i,j));
					}
				}
			}
		}
		else{
			int stepSize = (int) Math.floor(distY * Math.random()) + 2;

			for (int i = startPoint.x +2; i < endPoint.x -1; i++){
				for (int j = startPoint.y + 2; j < endPoint.y -1; j = j + stepSize){
					if (objectGrid[i][j] instanceof Empty){
						objectGrid[i][j] = new Wall(new Point(i,j));
					}
				}
			}
		}

	}

	/*	addDanger()
	*		For ouroor SERC types, this will add some danger items that are 
	*		origonally in-bounds. This can be random spots (like salt and 
	*		pepper noise) or a large body of danger.
	*
	*	This method takes the outdoor SERC that is already been made and then adds 
	*	either of the two.
	*
	*/

	public void addDanger(){
		boolean toinCoss = (Math.random() < 0.5);
		int distX = endPoint.x - startPoint.x;
		int distY = endPoint.y - startPoint.y;

		if (toinCoss){
			//Here we will put small specs of danger, like trees 
			int numberOfGrains = (int) Math.floor(Math.random() * (Math.sqrt(distX * distY)));
			Point[] grainLocations = new Point[numberOfGrains];
			for (int i = 0; i < numberOfGrains; i++){grainLocations[i] = new Point((int) Math.floor( 1 + Math.random() * distX), (int) Math.floor(1 + Math.random() * distY));}

			for(Point p : grainLocations){if(objectGrid[p.x][p.y] instanceof Empty){objectGrid[p.x][p.y] = new OutOfBounds(p);}}
		}
		else{
			//Here we will add a more larger, central part
			Point dangerStartPoint = new Point(startPoint.x, startPoint.y);
			int wdx = dangerStartPoint.x;
			int wdy = dangerStartPoint.y;
			do{
				dangerStartPoint = new Point((int) Math.ceil((distX-2) * Math.random()) + startPoint.x, (int) Math.ceil((distY-2) * Math.random()) + startPoint.y);
				wdx = (int) Math.floor((distX) * Math.random()) + 1;
				wdy = (int) Math.floor((distY) * Math.random()) + 1;
			} while (!( (wdx + dangerStartPoint.x < startPoint.x + distX) && (wdy + dangerStartPoint.y < startPoint.y + distY) && (wdx < distX) && (wdy < distY)));

			for(int i = dangerStartPoint.x-1; i < dangerStartPoint.x + wdx; i++){
				for(int j = dangerStartPoint.y-1; j < dangerStartPoint.y + wdy; j++){
					if(objectGrid[i][j] instanceof Empty){
						objectGrid[i][j] = new OutOfBounds(new Point(i,j));
					}
				}
			}

		}
	}

	/*					MAPPING OF OBJECTS 					*/

	/*	addCasualtiesToGrid()
	*		This will add casaulties to the SERC, depending if they are 
	*		needed to be in the water or not. Also, depending if the SERC
	*		is outdoor or ocean types. 
	*
	*		If a casualty is got the locked condition, it makes sure that they 
	*		are put together (next-door).
	*
	*/
	public void addCasualtiesToGrid(){
		int distX = endPoint.x - startPoint.x;
		int distY = endPoint.y - startPoint.y;

		ArrayList<Point> currentCasualtyLocations = new ArrayList<Point>();
		twoDGuassianDistribution distr = new twoDGuassianDistribution(startPoint, endPoint);
		for(Casualty casualty : casualties){
			Point p;
			if(casualty.getIsAquatic()){
				//The casualty needs to be in the water
				do{
					//Need to check if the casualty is locked and put it with the other locked
					if (casualty.hasCondition(ConditionType.LOCKED)  && casualties.indexOf(casualty) > 0) {
						Casualty otherLock = casualties.get(casualties.indexOf(casualty) - 1);
						Point prevPoint = otherLock.getLocation();
						Point[] potentials = {
								new Point(prevPoint.x - 1, prevPoint.y + 1),
								new Point(prevPoint.x, prevPoint.y + 1),
								new Point(prevPoint.x + 1, prevPoint.y + 1),
								new Point(prevPoint.x - 1, prevPoint.y),
								new Point(prevPoint.x + 1, prevPoint.y),
								new Point(prevPoint.x - 1, prevPoint.y - 1),
								new Point(prevPoint.x, prevPoint.y - 1),
								new Point(prevPoint.x + 1, prevPoint.y - 1)
						};
						p = potentials[(int) Math.floor(Math.random() * 8)];
					} else {
						if (bodyOfWater == WaterType.OCEAN) { p = distr.givePoint(); }
						else { p = new Point((int) Math.floor((distX) * Math.random()) + startPoint.x, (int) Math.floor((distY) * Math.random()) + startPoint.y); }
					}
				} while(!itemIsInWater(p) || currentCasualtyLocations.contains(p));
			}
			else{
				do{
					if((bodyOfWater == WaterType.OCEAN) || (drySERCType == DryType.OUTDOOR)){
						p = distr.givePoint();
					}
					else{
						p = new Point((int) Math.floor((distX) * Math.random()) + startPoint.x , (int) Math.floor((distY) * Math.random()) + startPoint.y);
					}
				} while(objectGrid[p.x][p.y] instanceof Wall || currentCasualtyLocations.contains(p));
			}
			currentCasualtyLocations.add(p);
			casualty.setLocation(p);
		}
	}

	/*	addAidsToGrid()
	*		This will add aids to the grid, similar to that of casualties
	*		less constraints need to be checked, only if the Aid is either
	*		in boundds or out-of-bounds.
	*
	*/
	public void addAidsToGrid(){
		int distX = endPoint.x - startPoint.x;
		int distY = endPoint.y - startPoint.y;

		for(Aid aid : aids){
			Point p;
			if(aid.getIsInSERC()){
				do{		
					p = new Point((int) Math.floor((distX) * Math.random()) + startPoint.x , (int) Math.floor((distY) * Math.random()) + startPoint.y);
				} while(objectGrid[p.x][p.y] instanceof Wall || casualtyIsAtPoint(p));
			}
			else{
				do{	
					Point[] potentials = {
						new Point((int) Math.floor((endPoint.x ) * Math.random()), startPoint.y - 1),
						new Point((int) Math.floor((endPoint.x ) * Math.random()), endPoint.y-1),
						new Point(startPoint.x - 1, (int) Math.floor((endPoint.y ) * Math.random())),
						new Point(endPoint.x-1, (int) Math.floor((endPoint.y) * Math.random())),
					};	
					p = potentials[(int) Math.floor(Math.random() * 4)];
				} while(objectGrid[p.x][p.y] instanceof Empty || objectGrid[p.x][p.y] instanceof Wall);
			}

			aid.setLocation(p);
		}
	}



	/*	addTimeStamp()
	*	
	*	Adds a time stamp at the end of generation
	*/
	public String addTimeStamp(){
		LocalDateTime current = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		return "--- SERC Generated on "+current.format(format)+" ---";
	}

	/*	randomRoad()
	*
	*	Method picks a random road type for the three
	*	options that are available
	*/

	public RoadType randomRoad(){
		//There are 3 road types; straight, corner and giveway
		int rand = (int) Math.floor(Math.random() * 4);
		switch(rand){
			case 0: return RoadType.STRAIGHT;
			case 1: return RoadType.CORNER;
			case 2: return RoadType.GIVEWAY;
		}
		return RoadType.STRAIGHT;
	}


	/* 			CHECKERS	*/

	/*	itemIsInWater(Point p)
	*		Point p: location of the start of the ray
	*
	*	This uses the even-odd rule to check an item is in water,
	*	by drawing a horizontal ray to the right. Boundaries are 
	*	classed as when the ray crosses from water to not water
	*	or vice versa.
	*/

	public boolean itemIsInWater(Point p){
		int counter = 0;
		for(int i  = p.x; i < width-1; i++){
			if((objectGrid[i][p.y] instanceof Water && !(objectGrid[i+1][p.y] instanceof Water)) ||
					(!(objectGrid[i][p.y] instanceof Water) && objectGrid[i+1][p.y] instanceof Water)) {counter += 1;}
		}
		return (counter % 2) != 0;
	}


	/*	casualtyIsAtPoint(Point p)
	*		Point p: location in the grid to be checked if a casualty
	*				 is present in the grid
	*
	*	When casualties are being mapped to the grid, some may already
	*	be there, this checks there is not a casualty already at this
	*	location.
	*/
	public boolean casualtyIsAtPoint(Point p){
		for(Casualty casualty : casualties){
			Point casualtyLocation = casualty.getLocation();
			if(casualtyLocation != null && casualtyLocation.equals(p)){return true;}
		}
		return false;
	}







	/* 					PRINTING FUNCTIONS 			*/
	/* 	Used for debugging, looking at the output into terminal before seeing on the UI 		*/


	public void printGrid(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < length; j++){
				SERCObject currentObject = objectGrid[i][j];
				System.out.print(currentObject.getIntValue());
				if (j == length -1){
					System.out.print("\n");
				}
			}
		}
	}

	public void printSERCSummary(){
		System.out.println("-------- SERC  Summary ---------");
		System.out.println("Width: "+ width+", Length: "+length);
		System.out.println("Start: 			("+startPoint.x+","+startPoint.y+")");
		System.out.println("End: 			("+endPoint.x+","+endPoint.y+")");
		System.out.println("Casualties:				"+numberOfCasualties);
		System.out.println("Aids:					"+numberOfAids);
	}

} //END OF SERCGenerator.java