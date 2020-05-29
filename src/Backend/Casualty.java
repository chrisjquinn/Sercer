package Backend;

import java.util.ArrayList;
import java.util.Collection;


/*		Casualty.java
*
*	To represent a casualty in a SERC. 
*	Casualty is also of type SERCObject.
*
*	The casualty has a Point like all other SERCObjects
*	but also has two arrays for the conditions they may have
*	along with any other information for that condition
*
*	e.g. 	bleeding 		-		arm
*			medical cond.	-		asthma
*
*/
public class Casualty extends SERCObject{

	private static final long serialVersionUID = 3L;


	public int intValue = 2;

	//If the casualty has their own aid or a personal phone on them
	private boolean hasAid;
	private boolean hasPhone;

	//The conditions the casualty has along with further description of this condition
	private ArrayList<ConditionType> conditions;
	private ArrayList<String> conditionExtra;

	//The 2D point of the location of the casualtys
	private Point location;

	//Default constructor
	public Casualty(){
		this.hasPhone = false;
		this.hasAid = false; 
		this.conditions = new ArrayList<ConditionType>();
		this.conditionExtra = new ArrayList<String>();
	}

	//When we have a Casualty with just their location, used more than the default
	public Casualty(Point p){
		super();
		this.hasPhone = false;
		this.hasAid = false;
		this.conditions = new ArrayList<ConditionType>();
		this.conditionExtra = new ArrayList<String>();
	}

	/*	-----------------		GETTERS		------------------	*/
	public Point getLocation(){ return this.location; }
	public int getIntValue(){ return this.intValue; }

	//Return a description for the casualties conditions
	public String getCasualtyDescription(){
		String fullDescription = new String();
		for(int i = 0; i < conditions.size(); i++){
			fullDescription += conditions.get(i).getDescription();
			if(!conditionExtra.get(i).equals("")){fullDescription += ": "+(conditionExtra.get(i));}
			if(i != conditions.size() -1){
				fullDescription += (", ");
			}
		}
		return fullDescription;
	}

	/*	-----------------		SETTERS		------------------	*/
	public void setLocation(Point p){this.location = p;}

	//Add one condition along with any further info about that condition
	public void addCondition(ConditionType cond, String information){
		conditions.add(cond);
		conditionExtra.add(information);
	}

	//Add multiple conditions along with their correspondong info
	public void addConditions(ArrayList<ConditionType> cond, ArrayList<String> information){
		conditions.addAll(cond);
		conditionExtra.addAll(information);
	}



	/*	-----------------		CHECKERS		------------------	*/

	//Check if a casualty has a specific condition
	public boolean hasCondition(ConditionType cond){
		for(int i = 0; i < conditions.size(); i++){
			if(conditions.get(i) == cond){
				return true;
			}
		} return false;
	}

	/*	Check if a casualty has got a condition that is only permitable
	*	in the water e.g. drowning is an aquatic condition
	*/
	public boolean getIsAquatic(){
		for(ConditionType condition : conditions){
			if((condition == ConditionType.DROWNOTH) || (condition == ConditionType.LOCKED) || (condition == ConditionType.NONSWIM) || (condition == ConditionType.WEAKSWIM)){
				return true;
			}
		}
		return false;
	}

} //END of Casualty.java