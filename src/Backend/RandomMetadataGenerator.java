package Backend;

/*	RandomMetadataGenerator.java
*
*	This will take either a WaterType or a DryType and then create a bank of 
*	metadata for use. Most of this is file is just the text descriptions of
*	locations for various SERCTypes. 
*
*/

import java.util.ArrayList;
import java.util.Arrays;

public class RandomMetadataGenerator{
	/* 			Variables that depend on the type of serc 		*/
	private String location;
	private String time;
	private String weather;

	/* 			Locations 		*/
	public final ArrayList<String> poolLocations = new ArrayList<String>(Arrays.asList(
		"Great Sankey Leisure Centre, WA5 3AA", "Woolston Neighbourhood Hub, WA1 4PN",
		"David Lloyd Leisure, WA5 1HH", "Penketh Community Pool, WA5 2EY",
		"Parr Swimming Baths, WA9 1BQ", "Westpark swimming & fitness on boundary road",
		"Wigan Leisure Centre, WN5 OUL", "Swyn Jones leisure centre at Hope Academy, WA12 OAG",
		"Hinckley Leisure Centre, LE10 1BZ", "Orford Juibilee Neighbourhood Hub, WA2 8HE"
	));

	public final ArrayList<String> lakeLocations = new ArrayList<String>(Arrays.asList(
		"Bassenthwaite lake", "Lake Windermere",
		"Lake Buttermere", "Connistion water",
		"Derwent water", "Lake Tahoe",
		"Lake Warvene", "Lake Ontario",
		"Lock Ern", "Loch Romond"
	));

	public final ArrayList<String> beachLocations = new ArrayList<String>(Arrays.asList(
		"Blackpool beach", "Newquay beach",
		"Southport beach", "Formby beach",
		"Fistral beach", "the beach at St. Agnes",
		"the beach in Barry, Wales", "Phwilli beach",
		"Llandudno beach, north wales", "Miami beach"
	));

	public final ArrayList<String> oceanLocations = new ArrayList<String>(Arrays.asList(
		"a location in the Irish sea", "somewhere in the english channel",
		"Formby beach. You have drifted out to deep water", "the River mersey, you have drifted out to wide water and land is far out of sight",
		"somewhere in the pacific", "somewhere in the Indian ocean",
		"somewhere in the celtic sea","somewhere in the north sea",
		"Miami beach. You have drifed out to deep water.","somewhere in the atlantic ocean"
	));

	public final ArrayList<String> canalLocations = new ArrayList<String>(Arrays.asList(
		"River Goyt", "River Etherow",
		"River Glaze", "Sankey Valley Canal",
		"Manchester ship canal", "River Leam",
		"River Severn", "Leamington canal",
		"River Don", "Dane canal"
	));

	public final ArrayList<String> indoorLocations = new ArrayList<String>(Arrays.asList(
		"Grange Valley Community Centre","Haydock Community Centre",
		"St.Helens town hall", "Latham Hall, Warrington",
		"La Bohemme restaraunt", "Rockery Hall and gardens main hall",
		"Panama Hatty's restaraunt", "Haydock cricket club members room",
		"Hood manor community centre", "Odeon Warrington ticket reception"
	));

	public final ArrayList<String> multiroomLocations = new ArrayList<String>(Arrays.asList(
		"Butchers arms, Great Sankey", "Birmingham Symphony hall atrium",
		"Bridgewater hall level 2 bar", "Blackpool winter gardens cafe",
		"Trafford Centre entrance", "the entrance to the Golden Square shopping centre",
		"Chapelford Farm pub", "Westfield London",
		"The Ferry tavern, Great Sankey", "The Cottage restaraunt"
	));

	public final ArrayList<String> outdoorLocations = new ArrayList<String>(Arrays.asList(
		"Delamere Forest", "Snake pass in the peak district",
		"Bourton on the water", "Sankey valley park",
		"Seqoia national park", "Kinder scout",
		"Pheny Ghent trail", "Kilderwater walk",
		"the standstone trail", "Forst of Bowland"
	));

	public final ArrayList<String> roadLocations = new ArrayList<String>(Arrays.asList(
		"Broad lane","West-end road",
		"Barrow hall lane", "Tachbrook road",
		"Stanhoe drive", "Burtonwood road",
		"Foxcote gardens", "Boston Boulveard",
		"Whittle avenue", "Sankey way"
	));


	/* 			Times 			*/
	public final ArrayList<String> times = new ArrayList<String>(Arrays.asList(
		"", "7pm", "22:00", "Midnight", "3 am",
		"12 noon", "1pm", "15:00", "11 am", "2:45 pm"
	));

	/* 			Weather 		*/
	public final ArrayList<String> weathers = new ArrayList<String>(Arrays.asList(
		"", "Mild", "Breezy", "Cold", "Normal",
		"Warm", "Hot", "Normal", "Mild", "Sunny with occasional clouds"
	));


	public RandomMetadataGenerator(WaterType wt){
		switch(wt){
			case OCEAN: this.location = oceanLocations.get((int) Math.floor(Math.random() * oceanLocations.size())); break;
			case CANAL: this.location = canalLocations.get((int) Math.floor(Math.random() * canalLocations.size())); break;
			case POOL: this.location = poolLocations.get((int) Math.floor(Math.random() * poolLocations.size())); break;
			case BEACH: this.location = beachLocations.get((int) Math.floor(Math.random() * beachLocations.size())); break;
			case LAKE: this.location = lakeLocations.get((int) Math.floor(Math.random() * lakeLocations.size())); break;
		}
		int randomTimeAndWeather = (int) Math.floor(Math.random() * times.size());
		this.time = times.get(randomTimeAndWeather);
		this.weather = weathers.get(randomTimeAndWeather);

	}

	public RandomMetadataGenerator(DryType dt){
		switch(dt){
			case INDOOR: this.location = indoorLocations.get((int) Math.floor(Math.random() * indoorLocations.size())); break;
			case MULTIROOM: this.location = multiroomLocations.get((int) Math.floor(Math.random() * multiroomLocations.size())); break;
			case OUTDOOR: this.location = outdoorLocations.get((int) Math.floor(Math.random() * outdoorLocations.size())); break;
			case ROAD: this.location = roadLocations.get((int) Math.floor(Math.random() * roadLocations.size())); break;
		}
		int randomTimeAndWeather = (int) Math.floor(Math.random() * times.size());
		this.time = times.get(randomTimeAndWeather);
		this.weather = weathers.get(randomTimeAndWeather);

	}

	public String getLocation(){return this.location;}
	public String getTime(){return this.time;}
	public String getWeather(){return this.weather;}

} //END of RandomMetadataGenerator.java