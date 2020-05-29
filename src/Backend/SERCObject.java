package Backend;

import java.io.Serializable;

/*		SERCObject.java (Abstract)
*
*	SERCs are made from a grid of these SERCObjects.
*	SERCObjects can be walls, water, roads etc.
*	
*	getIntValue() is used such that when rendering in 
*	Frontend, switching colours can be done by the 
*	number they have as their int representation.
*
*/


public abstract class SERCObject implements Serializable{
	//Needed for when the .serc file is being created
	private static final long serialVersionUID = 2L;

	//Two variables, its point location and int representation
	protected Point location;
	public int intValue;

	public SERCObject(){
		//Default constructor
	}
	public SERCObject(Point p){
		this.location = p;
		//More useful constructor when we know where to put the object
	}

	//	GETTERS 
	public Point getLocation(){return this.location;}
	public abstract int getIntValue();

	//	SETTERS
	public void setLocation(Point p){ this.location = p;}
	
}