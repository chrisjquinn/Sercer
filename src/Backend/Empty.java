package Backend;

/*		Empty.java
*
*	Type of SERCObject to represent empty 
*	spaces, used in all SERCs to distinguish
*	between land and other items.
*
*/
public class Empty extends SERCObject{
	public int intValue = 0;
	
	public Empty(Point p){
		super();
	}
	public int getIntValue(){return this.intValue;}

}