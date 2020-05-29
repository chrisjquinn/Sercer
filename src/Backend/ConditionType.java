package Backend;

/* 			ConditionType.java
*
*	Enum for conditions that casualties can have in SERCs.
*	Along with text descriptions.
*
*/
public enum ConditionType{

	CHOCKFULL("Chocking - Full Blockage"),
	CHOCKPART("Chocking - Partial Blockage"),
	HYPERVENT("Hyperventilating"),
	ASTHMA("Asthmatic"),
	ANAPHYL("Anaphylactic"),
	UB("Unconcious Breathing"),
	UNB("Unconcious Not-Breathing"),
	FAINT("Fainted"),
	SEIZURE("Seizure"),
	BROKEN("Broken"),
	DISLOCATION("Dislocation"),
	SPRAIN("Sprain"),
	MEDICOTHER("Other Medical Condition"),
	EPILEPSY("Epilepsy"),
	ALLERGIES("Allergies"),
	DIABETIC("Diabetic"),
	ANGINA("Angina"),
	BLEED("Bleed"),
	DROWNOTH("Drowning - other"),
	WEAKSWIM("Weak Swimmer"),
	NONSWIM("Non Swimmer"),
	LOCKED("Locked Swimmer"),
	STING("Sting"),
	FINE("Not Suffering"),
	SPINAL("Spinal"),
	CRUSH("Crush"),
	HYPOTHERM("Hypothermic"),
	HEATSTROK("Heatstroke"),
	OTHER("Other");

	private String description;

	ConditionType(String s){
		this.description = s;
	}

	public String getDescription(){
		return this.description;
	}


	
}