package org.gbif.biovel.locality.ws;

import org.apache.ibatis.annotations.Param;
import org.apache.lucene.queryparser.classic.ParseException;
import org.gbif.biovel.locality.model.Location;
import org.gbif.biovel.locality.model.LocationList;
import org.gbif.biovel.locality.model.OCRCorrectionList;
import org.gbif.biovel.locality.persistence.LocationMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

import org.gbif.biovel.locality.guice.Trim;
//import org.gbif.registry.ws.security.EditorAuthorizationService;
//import org.gbif.registry.ws.security.UserRoles;
import org.gbif.ws.server.interceptor.NullToNotFound;
import org.gbif.ws.util.ExtraMediaTypes;
import org.mybatis.guice.transactional.Transactional;

import javax.validation.constraints.NotNull;

import java.util.UUID;

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
		  @QueryParam("year") int year, @QueryParam("month") int month, @QueryParam("day") int day) {
    
	 try{ 
	 Preconditions.checkNotNull(recordedBy, "recordedBy is a mandatory parameter\n"
	 		+ "syntax: locality?recordedBy= { recordedBy }         ");
	 }
	   catch (NullPointerException exc)
	 {
		   
		 return null;
	 }
	 
    List<Location> tempLocationList = locationMapper.listLocations(recordedBy, country, county, stateProvince, continent,locality, decimalLatitude, decimalLongitude, year, month, day);
    
    		
    LocationList locList = new LocationList(tempLocationList);
    locList.removeEmptyLocations();
    locList.removeDublicateLocations();
    locList.ReadCollectors();
    
    OCRCorrectionList ocrList = new OCRCorrectionList();
    ocrList.ReadCollectors();
    
    boolean bCountry = locList.isCountry(recordedBy);
    boolean bCollector = locList.isCollector(recordedBy);
    boolean bFixedCountry = ocrList.isOCRFixCountry(recordedBy);
    
    if( ((bCountry) || bCollector || bFixedCountry))
    {
      locList.clear();
      Location tempLocation = new Location();
      if(bCountry)
        tempLocation.setOcrCorrectedString("country: " + recordedBy);
      else if(bFixedCountry)
      {
    	  String fixCountry = ocrList.getOCRFixCountry(recordedBy);
          tempLocation.setOcrCorrectedString("countryFixed: " + fixCountry);
          
      }
      else if(bCollector)
          tempLocation.setOcrCorrectedString("collector: " + recordedBy);
      
      locList.add(tempLocation);
   
    }
    
    
    Collections.sort(locList);
    try {
		locList.printLucene(recordedBy, country, county, stateProvince, locality);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
/*  public void update(@QueryParam("recordedBy") String recordedBy, @QueryParam("continental") String continental, @QueryParam("country") String country,
		  @QueryParam("decimalLatitude") String decimalLatitude, @QueryParam("decimalLongitude") String decimalLongitude, @QueryParam("county") String county, 
		  @QueryParam("locality") String locality, @QueryParam("day") int day, @QueryParam("month") int month, @QueryParam("year") int year) {
//    checkArgument(key.equals(entity.getKey()), "Provided entity must have the same key as the resource URL");
    //entity.setModifiedBy(security.getUserPrincipal().getName());
    Location lm = new Location();
    //update(lm);
  }*/
  
//  @PUT
//  @Path("{key}")
  //@Trim
 
  
 /* public void update(@NotNull @Trim Location entity) {
//    checkArgument(key.equals(entity.getKey()), "Provided entity must have the same key as the resource URL");
    //entity.setModifiedBy(security.getUserPrincipal().getName());
    Location lm = new Location();
    //update(lm);
  }*/

}
