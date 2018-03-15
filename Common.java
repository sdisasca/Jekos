package Stefano_final;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static java.util.Arrays.asList;


import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import parsing.ParseFeed;


public class Common {
	private static String cityFile = "city-data.json";
	private static String Airports = "airports.dat";
	private static List<Marker> cityMarkers;
	private static List<Marker> AirportMarker;
	private static List<Marker> airport;
	  public static void main (String [ ] args) {
	
		ArrayList cityMarkers=new ArrayList();
		ArrayList AirportMarker=new ArrayList();
				
	cityMarkers.retainAll(AirportMarker);
		
		System.out.println ( cityMarkers);
	    }
	       
	       
	  
}
