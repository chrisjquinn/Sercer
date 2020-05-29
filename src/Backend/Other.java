package Backend;

/*		Other.java
*
*	Type of SERCObject to represent other items
*	(e.g. tables) along with a description of 
*	what this 'other' item is
*
*/
public class Other extends SERCObject{
	public int intValue = 7;
	public String description;
	
	public Other(Point p){
		super();
	}
	public int getIntValue(){return this.intValue;}
	public void setDescription(String s){this.description = s;}

}