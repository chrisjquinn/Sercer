package Backend;

/*			Aid.java
*	Type of SERCObject to represent an Aid.
*	Aids can be phones, first aid kits, water,
*	floats etc.
*
*	Aids can also be out of bounds, or in the SERC
*	(or not in either)
*/
public class Aid extends SERCObject{

	public int intValue = 3;

	//If the aid is in the SERC, plus its description
	private boolean isInSerc;
	private PhoneFaType type;
	private String description;

	//Constructor for an Aid only by its type
	public Aid(PhoneFaType type, String s){
		switch (type){
			case INSERC:
				this.isInSerc = true;
				break;
			case OUTOFBOUNDS:
				this.isInSerc = false;
				break;
		}
		this.type = type;
		this.description = s;
	}
	
	//Constructor for an Aid by a Point object
	public Aid(Point p){
		super();
	}

	/*		GETTERS		*/
	public int getIntValue(){return this.intValue;}
	public boolean getIsInSERC(){return this.isInSerc;}

	public String getDescription(){
		String fullDesc = new String();
		fullDesc += description;
		if(isInSerc){fullDesc += ", In SERC";}
		else{fullDesc += ", Out of Bounds";}
		return fullDesc;
	}

} //END of Aid.java