package org.gbif.biovel.locality.ws;

import org.gbif.biovel.locality.model.Location;
import org.gbif.biovel.locality.model.LocationList;
import org.gbif.biovel.locality.persistence.LocationMapper;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

//import org.gbif.biovel.locality.guice.Trim;
//import org.gbif.registry.ws.security.EditorAuthorizationService;
//import org.gbif.registry.ws.security.UserRoles;
import org.gbif.ws.server.interceptor.NullToNotFound;
import javax.validation.constraints.NotNull;


//import org.json.simple.JSONObject;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("locality")
@Singleton
public class LocalityResource {

  private final LocationMapper locationMapper;

  @Inject
  public LocalityResource(LocationMapper locationMapper) {
    this.locationMapper = locationMapper;
  }

  
  public String returnHelp(String x)
  {
	  return new String("recordedBy is a mandatory parameter\n syntax: locality?recordedBy= { recordedBy ");
  }
  
  
  /**
   * Returns the list of locations where the recordedBy is present.
   * 
   * @param recordedBy The scopes the list of locations (Mandatory)
   * @return The list ordered by locality, or an empty list
   */
  @GET
  public List<Location> listByRecordedBy(@QueryParam("recordedBy") String recordedBy, @QueryParam("country") String country, @QueryParam("county") String county,
		  @QueryParam("stateProvince") String stateProvince, @QueryParam("continent") String continent, @QueryParam("locality") String locality,
		  @QueryParam("decimalLatitude") String decimalLatitude, @QueryParam("decimalLongitude") String decimalLongitude,
		  @QueryParam("year") int year, @QueryParam("month") int month, @QueryParam("day") int day, 
		  @QueryParam("endYear") int endYear, @QueryParam("endMonth") int endMonth, @QueryParam("endDay") int endDay,
		  @QueryParam("searchMode") String searchMode) {
    
	 try{ 
	 Preconditions.checkNotNull(recordedBy, "recordedBy is a mandatory parameter\n"
	 		+ "syntax: locality?recordedBy= { recordedBy }         ");
	 }
	   catch (NullPointerException exc)
	 {
		   
		 return null;
	 }	
	
    List<Location> tempLocationList = locationMapper.listLocations(recordedBy, country, county, stateProvince, continent,locality, decimalLatitude, decimalLongitude, year, month, day, searchMode);
    		
    LocationList locList = new LocationList(tempLocationList);
    locList.removeEmptyLocations();
    locList.removeDublicateLocations();
    	
   	Collections.sort(locList);
	
    
   return locList; 
}
  
  
  
  /**
   * Returns the list of locations where the recordedBy is present.
   * @param recordedBy The scopes the list of locations (Mandatory)
   * @return The list ordered by locality, or an empty list
   */
  @PUT
  //public List<Location> listByRecordedBy(@QueryParam("recordedBy") String recordedBy, @QueryParam("country") String country) {
   
  public void updateLocationById(@QueryParam("OccurrenceID") String occurrenceID, @QueryParam("recordedBy") String recordedBy, @QueryParam("country") String country, 
		  @QueryParam("locality") String locality) {
  Preconditions.checkNotNull(recordedBy, "recordedBy is a mandatory parameter");
    //return locationMapper.listLocations(recordedBy, country);
    locationMapper.updateLocation(occurrenceID, recordedBy, country, locality); 
  }
  
  /**
   * This method ensures that the path variable for the key matches the entity's key, ensures that the caller is
   * authorized to perform the action and then adds the server controlled field modifiedBy.
   *
   * @param key key of entity to update
   * @param entity entity that extends NetworkEntity
   * @param security SecurityContext (security related information)
   */
  
 
  
  @POST
  //@Path("locality")
  //@Trim
  //@Transactional
  //@RolesAllowed({ADMIN_ROLE, EDITOR_ROLE})
  public void addLocation(@QueryParam("occurrenceID") String occurrenceID, @QueryParam("continent") String continent, @QueryParam("locality") String locality, @QueryParam("country") String country,
		  @QueryParam("stateProvince") String stateProvince,  @QueryParam("county") String county,
		  @QueryParam("decimalLatitude") String decimalLatitude, @QueryParam("decimalLongitude") String decimalLongitude, 	  
		  @QueryParam("recordedBy") String recordedBy,  @QueryParam("year") int year, @QueryParam("month") int month, @QueryParam("day") int day) {
//    checkArgument(key.equals(entity.getKey()), "Provided entity must have the same key as the resource URL");
    //entity.setModifiedBy(security.getUserPrincipal().getName());
	  locationMapper.createLocation(occurrenceID, continent, locality, country, stateProvince, county,
			   decimalLatitude, decimalLongitude, recordedBy, year,  month, day);
   // Location lm = new Location();
    //update(lm);
  }
  
  /**
   * This method ensures that the path variable for the key matches the entity's key, ensures that the caller is
   * authorized to perform the action and then adds the server controlled field modifiedBy.
   *
   * @param key key of entity to delete
   * @param security SecurityContext (security related information)
   */
  
  @DELETE
  public void deleteLocation(@QueryParam("occurrenceID") String occurrenceID)
  {
	  locationMapper.deleteLocation(occurrenceID);
  
  }


}
