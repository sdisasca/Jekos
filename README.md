# Jekos
Extension
1.Add airport markers to the Map and on mouse over display Airport Information and hide all the other airport marker
2.Add a key press to hide all the airport marker if needed 
3.Show latitude and longitude (as coordinates) of the location at the current cursor position on the top of the screen
4.Display a a box info off the map if a city is clicked: 1) If city has any nearby quake (city falls within threat circle of any quake), then following info shown in pop up: city name, city country, count of nearby quakes, avg. magnitude of nearby quakes, count of recent quakes (past day or past hour) if not 2) will just display city name country and population

1. Added airport markers ArrayList and draw Airport Marker in the Airport Marker class 
2. Added method checkAirportForClick/Unhide Markers to hide all Airport marker on click once one airport is selected, add ShowTitle on the Airport Marker class.
3.Added a method to hide all airport markers by pressing 1 on the keyboard
3.Added a method to show calculate location on the map based on the mouse position and display it on top of the map, call the method in the Map drawing
4.Create a method to display info about a city and changed the display info based on the fact that there are earthquake threat nearby; this is done by counting earthquake around the city and get the average magnitude
