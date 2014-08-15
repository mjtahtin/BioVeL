package org.gbif.biovel.locality.model;

import com.google.common.base.Objects;


/**
 * The locality model.
 */
public class Location implements Comparable<Location>{

  private String recordedBy;
  private String occurrenceID;
  private String continent;
  private String locality;
  private String country;
  private String county;
  private String stateProvince;
  private String decimalLatitude;
  private String decimalLongitude;
  private String ocrCorrectedString;
  private int year;
  private int month;
  private int day;
  private int numRecords = 1;

  public int compare(Location object1, Location object2) {
      //return object1.getnumRecords() - object2.getnumRecords();
	  return 1;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object instanceof Location) {
      Location that = (Location) object;
      return Objects.equal(this.recordedBy, that.recordedBy) && Objects.equal(this.occurrenceID, that.occurrenceID) && Objects.equal(this.continent, that.continent) && Objects.equal(this.country, that.country) && Objects.equal(this.locality, that.locality)
    		  && Objects.equal(this.county, that.county) && Objects.equal(this.stateProvince, that.stateProvince)
    		  && Objects.equal(this.decimalLatitude, that.decimalLatitude) && Objects.equal(this.decimalLongitude, that.decimalLongitude) 
    		 && Objects.equal(this.day, that.day) && Objects.equal(this.month, that.month) && Objects.equal(this.year, that.year);
    }
    return false;
  }

  
  public String getRecordedBy() {
	    return recordedBy;
	  }
  
  public String getOccurrenceID() {
	    return occurrenceID;
	  }
  
 // public int getYear() {
	//    return year;
	 // }
  
  public String getContinent() {
	    return continent;
	  }
  
  public String getCountry() {
    return country;
  }

  public String getCounty() {
	    return county;
	  }
  
  
  public String getStateProvince() {
	    return stateProvince;
	  }
  
  public String getDecimalLatitude() {
	    return decimalLatitude;
	  }
  
  public String getDecimalLongitude() {
	    return decimalLongitude;
	  }
  
  public String getLocality() {
    return locality;
  }
  
  public String getOcrCorrectedString() {
	    return ocrCorrectedString;
	  }
  /*
  public int getMonth() {
	    return month;
	  }
  
  public int getDay() {
	    return day;
	  }
  
  public int getnumRecords() {
	    return numRecords;
	  }
*/
  @Override
  public int hashCode() {
    return Objects.hashCode(locality, country);
  }

  
  public void setRecordedBy(String recordedBy) {
	    this.recordedBy = recordedBy;
	  }
  
  public void setOccurrenceID(String occurrenceID) {
	    this.occurrenceID = occurrenceID;
	  }
  
  public void setContinent(String contient) {
	    this.continent = contient;
	  }
  
  public void setCountry(String country) {
    this.country = country;
  }
  
  public void setStateProvince(String stateProvince) {
	    this.stateProvince = stateProvince;
	  }
  
  public void setDecimalLongitude(String decimalLongitude) {
	    this.decimalLongitude = decimalLongitude;
	  }
  
  public void setDecimalLatitude(String decimalLatitude) {
	    this.decimalLatitude = decimalLatitude;
	  }
  
  public void setCounty(String county) {
	    this.county = county;
	  }

  public void setLocality(String locality) {
    this.locality = locality;
  }
  
  public void setOcrCorrectedString(String ocrCorrectedString) {
	    this.ocrCorrectedString = ocrCorrectedString;
	  }
  
  public void setYear(int year) {
	  if (year > 0) this.year = year;
	  }
  
  public void setMonth(int month) {
	  if (month > 0) this.month = month;
	  }
  
  public void setDay(int day) {
	    if (day > 0) this.day = day;
	  }
  
  public void setnumRecords(int numRec) {
	    numRecords=numRec;
	  }
  
  public boolean isLocationNull(int x)
  {
    if(getCountry() != null || getStateProvince() != null || getCounty() != null || getLocality() != null ||
    		((getDecimalLatitude() != null) && (getDecimalLongitude() != null)))
    	return false;
    else return true;
  }
  
  public boolean isLocationSame(String continent, String locality, String country, String county, String stateProvince)
  {
    if(getCountry().equals(country) && getStateProvince().equals(stateProvince) && getCounty().equals(county) && getLocality().equals(locality))
    	return true;
    else return false;
  }
  
  public String getLocationString(int i)
  {
	  String locationStr = getContinent() + getCountry() + getStateProvince() + getCounty() + getLocality();   
      return locationStr;
  }

  @Override
  public String toString() {
	  
    //    return Objects.toStringHelper(this.getClass()).add("recordedBy", recordedBy).add("occurrenceID", occurrenceID).add("continent", continent).add("locality", locality).add("country", country).add("county", county).add("stateProvince", stateProvince).add("decimalLatitude", decimalLatitude).add("decimalLongitude", decimalLongitude).
    	//	add("year", year).add("month", month).add("day", day).toString();	
	  return "xx";
  }

@Override
public int compareTo(Location arg0) {
	// TODO Auto-generated method stub
	return  0; // arg0.getnumRecords() - this.getnumRecords();
	//return 0;
}
}
