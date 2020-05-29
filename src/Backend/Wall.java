package Backend;

/*	Wall.java
*
*	Type of SERCObject to represent a wall for indoor rooms & pool SERCs
*
*/

public class Wall extends SERCObject{
	public int intValue = 1;
	
	public Wall(Point p){
		super();
	}
	public int getIntValue(){return this.intValue;}

}