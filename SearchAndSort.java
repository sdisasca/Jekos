package Stefano_final;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import demos.Airport;

// Read the airpoirts in from the file.
public class SearchAndSort {
	private static ArrayList<Airport> readFile(String fname) throws IOException	{
		ArrayList<Airport> airports = new ArrayList<Airport>();
		FileInputStream fis = new FileInputStream(fname);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line;
		while ((line = br.readLine()) != null) 
		{
			String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			for (int i = 0; i < data.length; i++) {
				data[i] = data[i].replace("\"", "");
			}
			int airportID = Integer.parseInt(data[0]);
			String name = data[1];
			String city = data[2];
			String country = data[3];
			String code3 = data[4];
			String code4 = data[5];
			double lat = Double.parseDouble(data[6]);
			double lon = Double.parseDouble(data[7]);
			int alt = Integer.parseInt(data[8]);
			float tz = Float.parseFloat(data[9]);
			char dst = data[10].charAt(0);
			String dbtz = data[11];
			airports.add(new Airport(airportID, name, city, country, 
					code3, code4, lat, lon, alt, tz, dst, dbtz));
		}
		br.close();
		return airports;
	}
	
	

public static String findAirportCodeBS(String toFind, ArrayList<Airport> airports)    {
	int low = 0;
	int high = airports.size()-1;
	int mid;
	while (low <= high) {
		mid = low + ((high-low)/2);
		int compare = toFind.compareTo(airports.get(mid).getCity());
		if (compare < 0) {
			high = mid - 1;
		}
		else if (compare > 0) {
			low = mid+1;
		}
		else return (airports.get(mid)).getCode3();
	}
	return null; 
}
}
