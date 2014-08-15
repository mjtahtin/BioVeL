package org.gbif.biovel.locality.model;

import com.google.common.base.Objects;


/**
 * The locality model.
 */
public class OcrCorrection {

  
  private String unCorrected;
  private String corrected;
  private boolean bCountry = false;
  private boolean bCollector = false;

  public int compare(Location object1, Location object2) {
      //return object1.getnumRecords() - object2.getnumRecords();
	  return 1;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    return false;
  }

  
  public String getUnCorrected() {
	    return unCorrected;
	  }
  
  public String getCorrected() {
	    return corrected;
	  }
  
 
  
  
  @Override
  public int hashCode() {
    return Objects.hashCode(corrected, unCorrected);
  }

  
  public void setCorrected(String corrected) {
	    this.corrected = corrected;
	  }
  
  public void setunCorrected(String uncorrected) {
	    this.unCorrected = uncorrected;
	  }
  
  
  
}
