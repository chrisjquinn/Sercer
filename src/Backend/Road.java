package Backend;

/*	Road.java
*
*	Type of SERCObject to represent roads. 
*	Similar to Water.java, Empty.java (etc)
*/

public class Road extends SERCObject{
	public int intValue = 6;
	
	public Road(Point p){
		super();
	}
	public int getIntValue(){return this.intValue;}

}