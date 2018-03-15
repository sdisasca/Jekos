package Stefano_final;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;

import processing.core.PConstants;
import processing.core.PGraphics;



public class AirportMarker extends CommonMarker //implements Comparable <AirportMarker >
{
    public static List<SimpleLinesMarker> routes;
  
    
    public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
    
    }
    
 
    protected static final float kmPerMile = 1.6f;
    public static int TRI_SIZE = 5;
    @Override
    public void drawMarker(PGraphics pg, float x, float y) {
        //pg.fill(11);
        //pg.ellipse(x, y, 5, 5);
        
         pg.pushStyle();
         // pg.noStroke();
          pg.stroke(204, 102, 0);
          pg.ellipse(x, y, 5, 5);
          pg.fill(255, 100);
          pg.ellipse(x, y, 5, 5);
     pg.popStyle();
    
        
    }

    @Override
    public void showTitle(PGraphics pg, float x, float y) {
        String name = getLocation() + " " + getProperties() + " ";
        String pop = "Pop: " + getLocation() + getCity();
        
        pg.pushStyle();
        
       
        pg.fill(255, 255, 255);
        pg.textSize(12);
        pg.rectMode(PConstants.CORNER);
        pg.rect(x, y-TRI_SIZE-35, Math.max(pg.textWidth(name), pg.textWidth(pop)) + 12, 55);
        pg.fill(0, 0, 0);
        pg.textAlign(PConstants.LEFT, PConstants.TOP);
        pg.text(name, x+3, y-TRI_SIZE-33);
        pg.text(pop, x+3, y - TRI_SIZE -18);
        
        pg.popStyle();
    }
    
    private String getCity()
    {
        return getStringProperty("name");
    }
    
    public double threatCircle() {    
    double miles = 20.0f;
    double km = (miles * 20*kmPerMile);
    return km;
}

}



        
    


