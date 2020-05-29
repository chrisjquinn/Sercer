package Backend;

import java.io.Serializable;

	/* 			POINT.java
	* 	This is a primitive class so that the locations of the
		SERC can be represented in a cartesian plane.

		This is used by all SERCObjects, the classes of SERC and SERCGenerator
		also create a 2D Array of SERCObjects, so this is one of the most
		important classes to the SERC
	*/

public class Point implements Serializable {

	private static final long serialVersionUID = 4L;

	public int x;
	public int y;

	//Constructors
	public Point(){ this.x = 0; this.y = 0; }
	public Point(int x, int y){ this.x = x; this.y = y; }
	public Point(Point p){ this.x = p.x; this.y = p.y; }

	//Check if point equal to another point
	public boolean equals(Point p){
		if ((this.x == p.x) && (this.y == p.y)){
			return true;
		}
			return false;
	}

	
	//Used for printing / use in terminal
	@Override
	public String toString(){
		String s = "("+this.x+","+this.y+")";
		return s;
	}

}