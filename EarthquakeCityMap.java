package Stefano_final;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;


public class EarthquakeCityMap extends PApplet {
    
    private static final long serialVersionUID = 1L;
    // IF YOU ARE WORKING OFFILINE, change the value of this variable to true
    private static final boolean offline = true;
        /** This is where to find the local tiles, for working without an Internet connection */
    public static String mbTilesString = "blankLight-1-3.mbtiles";
            //feed with magnitude 2.5+ Earthquakes
    private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
        // The files containing city names and info and country names and info
    private String cityFile = "city-data.json";
    private String countryFile = "countries.geo.json";
       
    //EXTENSION
    private PGraphics buffer;   
    //EXTENSION
    // The map
    
  ////EXTENSION - BUT NOT USED
  	    private AbstractMapProvider provider1;
        private AbstractMapProvider provider2;
    	
        private UnfoldingMap map;
        // Markers for each city
    	private List<Marker> cityMarkers;
    	// Markers for each earthquake
    	private List<Marker> quakeMarkers;
    	// A List of country markers
    	private List<Marker> countryMarkers;
    	// NEW IN MODULE 5
    	private CommonMarker lastSelected;
    	private CommonMarker lastClicked;
        ///EXTENSION
        //private List<Marker> airportList; // 	AIRPORT
        private List<Marker> AirportMarker;
        //private List<Marker> routeList;
      
        ///EXTENSION

        private int countNearByQuakes=0;
    	private double avgMag=0.0;
    	private double sumMag = 0.0;
    	private int countRecentQuakes=0;
    	private PFont bold = createFont("Thaoma",10);
        private PFont regular = createFont("Thaoma",10);
        ///EXTENSION

        public void setup() {        
        // (1) Initializing canvas and map tiles
        size(900, 700, OPENGL);
        //new new new
        buffer = createGraphics(900, 700);
      //new new new
        
        
        if (offline) {
            map = new UnfoldingMap(this, 200, 50,900, 600, new MBTilesMapProvider(mbTilesString));
       earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
        }
        else {
           // map = new UnfoldingMap(this, 200, 50, 900, 600, new Google.GoogleMapProvider());
            // IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
        	map = new UnfoldingMap(this, 200, 50, 900, 600, provider1);
        	//new change provider
            provider1 = new Microsoft.HybridProvider();
            provider2 = new Google.GoogleMapProvider();
            
            earthquakesURL = "2.5_week.atom";
        }
        
    	MapUtils.createDefaultEventDispatcher(this, map);
     
        //earthquakesURL = "test1.atom";
        //earthquakesURL = "test2.atom";
    
     
        // Uncomment this line to take the quiz
        //earthquakesURL = "quiz2.atom";
        
        // (2) Reading in earthquake data and geometric properties
        //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
        
        //     STEP 2: read in city data
        List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
        cityMarkers = new ArrayList<Marker>();
        for(Feature city : cities) {
          cityMarkers.add(new CityMarker(city));
        }
        
        //     STEP 3: read in earthquake RSS feed
        List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
        
        for(PointFeature feature : earthquakes) {
          //check if LandQuake
          if(isLand(feature)) {
            quakeMarkers.add(new LandQuakeMarker(feature));
          }
          // OceanQuakes
          else {
            quakeMarkers.add(new OceanQuakeMarker(feature));
          }
        }
        
        //EXTENSION //EXTENSION//EXTENSION//EXTENSION//EXTENSION//EXTENSION//EXTENSION//EXTENSION
        // get features from airport data
                List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
                
                // list for markers, hashmap for quicker access when matching with routes
                AirportMarker = new ArrayList<Marker>();
                          HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
                // create markers from features
                for(PointFeature feature : features) {
                //airportList.retainAll(cityMarkers);
                	//if (AirportMarker<> cityMarkers){
                		//result.remove (city);
                	//}
                    AirportMarker m = new AirportMarker(feature);
                    m.setRadius(2);
                    AirportMarker.add(m);
              // put airport in hashmap with OpenFlights unique id for key
               airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
              
                }
   
        
        //sort Earthquake excercise                   
        //EarthquakeMarker quakers[] = quakeMarkers.toArray(new EarthquakeMarker[quakeMarkers.size()]);
        //List<EarthquakeMarker> quakelist = Arrays.asList(quakers);
        //Collections.sort (quakelist);        
        
        // could be used for debugging
        // printQuakes();
        // sortAndPrint();
             
        // (3) Add markers to map
        //     NOTE: Country markers are not added to the map.  They are used for their geometric properties
        map.addMarkers(quakeMarkers);
        map.addMarkers(cityMarkers);
      //EXTENSION
        map.addMarkers(AirportMarker);
   
		
      //  ArrayList cityMarkers=new ArrayList();
	//	ArrayList AirportMarker=new ArrayList();
			
	   // cityMarkers.retainAll(AirportMarker);
	
	//	System.out.println ( " " +AirportMarker);
        
        }
        

    
    public void draw() {
    	 //EXTENSION
    	buffer.beginDraw();
		background(255,255,255);
		map.draw();
		buffer.endDraw();
		image(buffer, 0, 0);
		addKey();
		buffer.clear();

		//Draw latitude and longitude in a rectangle below the map
		//EXTENSION
		addLocation();

		if( lastClicked != null && lastClicked instanceof CityMarker ) {
			popUpCityClicked(countNearByQuakes, avgMag, countRecentQuakes);
		}

	
             }
    
    

     //private void sortAndPrint() {
      //            EarthquakeMarker quakers[] = quakeMarkers.toArray(new EarthquakeMarker[quakeMarkers.size()]);
        //    List<EarthquakeMarker> quakelist = Arrays.asList(quakers);
          //  Collections.sort (quakelist);    
           
            //System.out.println("order quakes: " + quakelist);
    // }
    
          // and then call that method from setUp
    
    /** Event handler that gets called automatically when the 
     * mouse moves.
     */
    @Override
    public void mouseMoved()
    {
        // clear the last selection
        if (lastSelected != null) {
            lastSelected.setSelected(false);
            lastSelected = null;
        
        }
        selectMarkerIfHover(quakeMarkers);
        selectMarkerIfHover(cityMarkers);
        selectMarkerIfHover(AirportMarker);
        //loop();
    }
    
    // If there is a marker selected 
    private void selectMarkerIfHover(List<Marker> markers)
    {
        // Abort if there's already a marker selected
        if (lastSelected != null) {
            return;
        }
        
        for (Marker m : markers) 
        {
            CommonMarker marker = (CommonMarker)m;
            if (marker.isInside(map,  mouseX, mouseY)) {
                lastSelected = marker;
                marker.setSelected(true);
                return;
            }
        }
    }
    
    /** The event handler for mouse clicks
     * It will display an earthquake and its threat circle of cities
     * Or if a city is clicked, it will display all the earthquakes 
     * where the city is in the threat circle
     */
    @Override
    public void mouseClicked()
    {
        if (lastClicked != null) {
            unhideMarkers();
            lastClicked = null;
        }
        else if (lastClicked == null) 
        {
            checkEarthquakesForClick();
           
            if (lastClicked == null) {
                checkCitiesForClick();
                checkAirportForClick();
            }
        }  
    }
    
  

    
    
    // Helper method that will check if a city marker was clicked on
    // and respond appropriately
    private void checkCitiesForClick()
    {
    	//New
    	countNearByQuakes = 0;
		sumMag = 0.0;
		avgMag = 0.0;
        countRecentQuakes = 0;
    	//
        
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker marker : cityMarkers) {
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = (CommonMarker)marker;
                // Hide all the other earthquakes and hide
                for (Marker mhide : cityMarkers) {
                    if (mhide != lastClicked) {
                        mhide.setHidden(true);
                    }
                }
                for (Marker mhide : quakeMarkers) {
                    EarthquakeMarker quakeMarker = (EarthquakeMarker)mhide;
                    if (quakeMarker.getDistanceTo(marker.getLocation()) 
                            > quakeMarker.threatCircle()) {
                        quakeMarker.setHidden(true);
                    }
                    else {
                    	countNearByQuakes++;
                    	sumMag += quakeMarker.getMagnitude();
                    	String age = quakeMarker.getStringProperty("age");
						//System.out.println("title: " + quakeMarker.getTitle());
						//System.out.println("age: " + age);
						if( "Past Hour".equals(age) || "Past Day".equals(age) ) {
							countRecentQuakes++;
						}

					}
				}

				avgMag = sumMag / (double)countNearByQuakes;
				/*
				if( lastClicked != null ) {
					popUpCityClicked( countNearByQuakes, avgMag );
				}
				*/
				return;
			}
		}
}
    
    // Helper method that will check if an earthquake marker was clicked on
    // and respond appropriately
    private void checkEarthquakesForClick()
    {
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker m : quakeMarkers) {
            EarthquakeMarker marker = (EarthquakeMarker)m;
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = marker;
                // Hide all the other earthquakes and hide
                for (Marker mhide : quakeMarkers) {
                    if (mhide != lastClicked) {
                        mhide.setHidden(true);
                    }
                }
                for (Marker mhide : cityMarkers) {
                    if (mhide.getDistanceTo(marker.getLocation()) 
                            > marker.threatCircle()) {
                        mhide.setHidden(true);
                    }
                }
                return;
            }
        }
    }
   
  //EXTENSION
    private void checkAirportForClick()
    {
        if (lastClicked != null) return;
        // Loop over the earthquake markers to see if one of them is selected
        for (Marker m : AirportMarker) {
            AirportMarker marker = (AirportMarker)m;
            if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
                lastClicked = marker;
                // Hide all the other earthquakes and hide
                for (Marker mhide : AirportMarker) {
                    if (mhide != lastClicked) {
                        mhide.setHidden(true);
                    }
                }
                for (Marker mhide : AirportMarker) {
                    if (mhide.getDistanceTo(marker.getLocation()) 
                            > marker.threatCircle()) {
                        mhide.setHidden(true);
                    }
                }
                return;
            }
        }
    }
    
    //EXTENSION
    // loop over and unhide all markers
    private void unhideMarkers() {
        for(Marker marker : quakeMarkers) {
            marker.setHidden(false);
        }
            
        for(Marker marker : cityMarkers) {
            marker.setHidden(false);
        }
      //EXTENSION
        for(Marker marker : AirportMarker) {
            marker.setHidden(false);
        }
      //extension
    }
    
    
    
    // helper method to draw key in GUI
    private void addKey() {    
        // Remember you can use Processing's graphics methods here
        fill(255, 250, 240);
        
        int xbase = 25;
        int ybase = 50;
        
        rect(xbase, ybase, 150, 250);
        
       
        
        fill(0);
        textAlign(LEFT, CENTER);
        textSize(12);
        text("Earthquake Key", xbase+25, ybase+25);
        
        fill(150, 30, 30);
        int tri_xbase = xbase + 35;
        int tri_ybase = ybase + 50;
        triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
                tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
                tri_ybase+CityMarker.TRI_SIZE);

        fill(0, 0, 0);
        textAlign(LEFT, CENTER);
        text("City Marker", tri_xbase + 15, tri_ybase);
        text("Land Quake", xbase+50, ybase+70);
        text("Ocean Quake", xbase+50, ybase+90);
        text("Airport", xbase+50, ybase+110);
        
        text("Size ~ Magnitude", xbase+25, ybase+135);
        
        fill(255, 255, 255);
        
        ellipse(xbase+35,ybase+70,10,10);
        
        rect(xbase+35-5, ybase+90-5, 10, 10);
              
        ellipse(xbase+35,ybase+110,5,5);
        fill(color(255, 255, 0));
        
        ellipse (xbase+35-3,ybase+150,5,5);
        fill(color(255, 255, 0));
        
        ellipse(xbase+35, ybase+170, 12, 12);
        fill(color(0, 0, 255));
        
        ellipse(xbase+35, ybase+190, 12, 12);
        fill(color(255, 0, 0));
     
        ellipse(xbase+35, ybase+210, 12, 12);
        fill(color(0, 0, 255));
        
        textAlign(LEFT, CENTER);
        fill(0, 0, 0);
        text("Shallow", xbase+50, ybase+150);
        text("Intermediate", xbase+50, ybase+170);
        text("Deep", xbase+50, ybase+190);
        text("Past hour", xbase+50, ybase+210);
        
        fill(255, 255, 255);
        int centerx = xbase+35;
        int centery = ybase+210;
        ellipse(centerx, centery, 12, 12);

        
        
      

        textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		
		text("Press 1 to hide Airport marker..", xbase+20, ybase+600);

    }

    
    
    // Checks whether this quake occurred on land.  If it did, it sets the 
    // "country" property of its PointFeature to the country where it occurred
    // and returns true.  Notice that the helper method isInCountry will
    // set this "country" property already.  Otherwise it returns false.
    private boolean isLand(PointFeature earthquake) {
        
        // IMPLEMENT THIS: loop over all countries to check if location is in any of them
        // If it is, add 1 to the entry in countryQuakes corresponding to this country.
        for (Marker country : countryMarkers) {
            if (isInCountry(earthquake, country)) {
                return true;
            }
        }
        
        // not inside any country
        return false;
    }
    
    // prints countries with number of earthquakes
    // You will want to loop through the country markers or country features
    // (either will work) and then for each country, loop through
    // the quakes to count how many occurred in that country.
    // Recall that the country markers have a "name" property, 
    // And LandQuakeMarkers have a "country" property set.
   
   // private void printQuakes() {
     //   int totalWaterQuakes = quakeMarkers.size();
       // for (Marker country : countryMarkers) {
         //   String countryName = country.getStringProperty("name");
           // int numQuakes = 0;
            //for (Marker marker : quakeMarkers)
            //{
             //   EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
              //  if (eqMarker.isOnLand()) {
                //    if (countryName.equals(eqMarker.getStringProperty("country"))) {
                  //      numQuakes++;
                   // }
                //}
            //}
            //if (numQuakes > 0) {
              //  totalWaterQuakes -= numQuakes;
               // System.out.println(countryName + ": " + numQuakes);
            //}
       // }
       // System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
    //}
    
    
    
    // helper method to test whether a given earthquake is in a given country
    // This will also add the country property to the properties of the earthquake feature if 
    // it's in one of the countries.
    // You should not have to modify this code
    private boolean isInCountry(PointFeature earthquake, Marker country) {
        // getting location of feature
        Location checkLoc = earthquake.getLocation();

        // some countries represented it as MultiMarker
        // looping over SimplePolygonMarkers which make them up to use isInsideByLoc
        if(country.getClass() == MultiMarker.class) {
                
            // looping over markers making up MultiMarker
            for(Marker marker : ((MultiMarker)country).getMarkers()) {
                    
                // checking if inside
                if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
                    earthquake.addProperty("country", country.getProperty("name"));
                        
                    // return if is inside one
                    return true;
                }
            }
        }
            
        // check if inside country represented by SimplePolygonMarker
        else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
            earthquake.addProperty("country", country.getProperty("name"));
            
            return true;
        }
        return false;
    }



//EXTENSION
  //Draw latitude and longitude on top of the map
  	private void addLocation() {
  		int xbase = 25;
  		int ybase = 50;
  		fill(255, 255, 0);
  		//rect(xbase+65, ybase+61, 175, 30);
  		Location location = map.getLocation(mouseX, mouseY);
  		fill(0);
  		text(location.getLat()+", "+location.getLon(), 20, 22);
  }



  //EXTENSION
//pop up window to show city information when clicked

private void popUpCityClicked( int countNearByQuakes, double avgMag, int countRecentQuakes ) {
	pushStyle();

	fill(255, 250, 240);

	int rectWidth = 225;
	int rectHeight;
	int xbase = 25;
	int ybase = 460;
	String message;
	//float msgWidth;

	String cityName = lastClicked.getStringProperty("name");
	String countryName = lastClicked.getStringProperty("country");
	String cityName1 = lastClicked.getStringProperty("population");
	//rect(xbase, ybase, 150, 250);
	/*
	 * to calculate text width:
	 * String s = "Tokyo";
	 * float sw = textWidth(s);
	 */

	if( countNearByQuakes == 0 ) {
		rectHeight = 125;
		rect(xbase, ybase, rectWidth, rectHeight);
		fill(0);
		//textAlign(CENTER);
		textSize(20);
		textFont(bold);
		message = "CITY INFORMATION";
		//msgWidth = textWidth(message);
		text(message, xbase+22, ybase+25);

		textFont(regular);
		message = "City: " + cityName;
		text(message, xbase+22, ybase+50);

		message = "Country: " + countryName;
		text(message, xbase+22, ybase+65);

		message = "Population: " + cityName1;
		text(message, xbase+22, ybase+80);
		
		//textAlign(LEFT);
		fill(0);
		textSize(15);
		textFont(regular);
		message = "NO QUAKES nearby";
		text(message, xbase+22, ybase+100);

	}
	else {
		rectHeight = 150;
		rect(xbase, ybase, rectWidth, rectHeight);
		fill(0);
		//textAlign(CENTER);
		textSize(20);
		textFont(bold);
		message = "City Information";
		//msgWidth = textWidth(message);
		text(message, xbase+30, ybase+25);

		textFont(regular);
		message = "City: " + cityName1;
		text(message, xbase+22, ybase+50);

		message = "Country: " + countryName;
		text(message, xbase+22, ybase+65);
		
		message = "Population: " + cityName1;
		text(message, xbase+22, ybase+80);

		message = "Nearby Quakes: " + countNearByQuakes;
		text(message, xbase+22, ybase+95);

		avgMag = Math.round(avgMag * 100.0)/100.0;
		message = "Avg Magnitue: " + avgMag;
		text(message, xbase+22, ybase+110);

		message = "Recent Quakes: " + countRecentQuakes;
		text(message, xbase+22, ybase+125);
	}

	popStyle();
}

//EXTENSION
//Key press to hide airport marker as they are too many
public void keyPressed() {
	if(key == '1') {
			hideAirportMarkers();
		}
}
private void hideAirportMarkers() {
	 for (Marker mhide : AirportMarker) {
                     mhide.setHidden(true);
	}
}
}





