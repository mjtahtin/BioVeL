package org.gbif.biovel.locality.persistence;

import org.gbif.biovel.locality.model.Location;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.apache.ibatis.annotations.Param;

public interface LocationMapper {

  /**
   * @return A list of locations ordered by locality, that are recorded by the individual provided, or an empty list.
   */
  List<Location> listLocations(@Param("recordedBy") String recordedBy, @Param("country") String country, @Param("county") String county,
		  @Param("stateProvince") String stateProvince, @Param("continent") String continent, @Param("locality") String locality,
		  @Param("decimalLatitude") String decimalLatitude, @Param("decimalLongitude") String decimalLongitude,
		  @Param("year") int year, @Param("month") int month, @Param("day") int day, @Param("searchMode") String searchMode);
  
  void updateLocation(@Param("occurrenceID") String occurranceID, @Param("recordedBy") String recordedBy, @Param("country") String country, @Param("locality") String locality);
  
  //void createLocation(@Param("recordedBy") String recordedBy, @Param("country") String country, @Param("locality") String locality);
  int createLocation(@Param("occurrenceID") String occurranceID, @Param("continent") String continent, @Param("locality") String locality, @Param("country") String country,
		  @Param("stateProvince") String stateProvince,  @Param("county") String county,
		  @Param("decimalLatitude") String decimalLatitude, @Param("decimalLongitude") String decimalLongitude, 	  
		  @Param("recordedBy") String recordedBy, @Param("year") int year, @Param("month") int month, @Param("day") int day);
 
  void deleteLocation(@Param("occurrenceID") String occurranceID);
  
  /*
   * void updateLocation(@Param("recordedBy") String recordedBy, @Param("continental") String continental, @Param("country") String country,
		  @Param("decimalLatitude") String decimalLatitude, @Param("decimalLongitude") String decimalLongitude, @Param("county") String county, 
		  @Param("locality") String locality, @Param("day") int day, @Param("month") int month, @Param("year") int year);*/
   
 // void updateLocation(Location location);
}
