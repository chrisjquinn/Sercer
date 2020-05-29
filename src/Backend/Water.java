package Backend;

/*	Water.java
*	
*	Type of SERCObject representing water, used in all 'wet' SERCs
*
*/

public class Water extends SERCObject{
	public int intValue = 5;
	
	public Water(Point p){
		super();
	}
	public int getIntValue(){return this.intValue;}

}