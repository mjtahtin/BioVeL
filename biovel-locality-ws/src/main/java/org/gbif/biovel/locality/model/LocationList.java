package org.gbif.biovel.locality.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
 

public class LocationList extends ArrayList<Location>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public List<Location> tempList;
    public List<String> locationString = new ArrayList<String>();
    public List<Integer> locationNum = new ArrayList<Integer>();
    public List<String> recorder;
    
    public LocationList(List<Location> tempLocationList) {
		// TODO Auto-generated constructor stub
    	super(tempLocationList);
    	
	    
	}

	void LocationList()
    {
		
    }
	
		
    public void AddLocationString (String loc)
    {
    	if(locationString.contains(loc) == false)
    	{
    		locationString.add(loc);
    		int index = locationString.indexOf(loc);  		
    	}		
    }
    
    public void removeEmptyLocations()
    {
    	ListIterator<Location> itr = this.listIterator();
    	while(itr.hasNext()) {
    	       Location element = itr.next();
    	       if (element.isLocationNull(1))
    	    	   itr.remove();
    	}
    }
    
    public int findLocationIndex(String location)
    {
    	ListIterator<Location> itr = this.listIterator();
    	while(itr.hasNext()) {
    		int index = itr.nextIndex();
    	       Location element = itr.next();
    	       if (element.getLocationString(1).equalsIgnoreCase(location))
    	    	   return index;
    	}
    	return -1;
    }
    
    
    
    public void removeDublicateLocations()
    {
    	ListIterator<Location> itr = this.listIterator();
    	while(itr.hasNext()) {
    		int currentIndex = itr.nextIndex();
    		 Location element = itr.next();
    	
    	    int  firstIndex = this.findLocationIndex(element.getLocationString(1));
 	         if(currentIndex > firstIndex)
 	         {
 		       itr.remove();
 		       Location loc = this.get(firstIndex);
 		       loc.setnumRecords(loc.getnumRecords() +1 );
 	         }       
 	        
 	   }
    }
    
 
    
    public List<String> getLocationString()
    {
    	if(locationString != null)
          return locationString;
    	else {
    		System.out.print("locationString NULL");
    	
		    return null;	
    	}
    }
    
    public List<String> getLocationNum()
    {
    	if(locationString != null)
          return locationString;
    	else {
    		System.out.print("locationString NULL");
    	
		    return null;	
    	}
    }
		
  }
     