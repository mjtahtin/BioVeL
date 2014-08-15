package org.gbif.biovel.locality.model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
 





import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LocationList extends ArrayList<Location>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int YEAR_RANGE = 20; //fuzzy search for +-20 years range outside given year(s)
	public static final int LUCENE_MAX_SCORE = 100; //fuzzy search for +-20 years range outside given year(s)
	public static final int YEAR_MATCH_SCORE = 20; //points for year matching given range
	public static final int MONTH_MATCH_SCORE = 15; //points for month matching given range
	public static final int DAY_MATCH_SCORE = 50; //points day matching given range

	public List<Location> tempList;
    public List<String> locationString = new ArrayList<String>();
    public List<Integer> locationNum = new ArrayList<Integer>();
    public List<String> recorder;
    
    public LocationList(List<Location> tempLocationList) {
		// TODO Auto-generated constructor stub
    	super(tempLocationList);
    	
    	StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
	    
	}

	void LocationList()
    {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

	    // create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);

    }
	
	protected void finalize()
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
    
    public void addLuceneIndex(String country, String stateProvince, String county, String locality, int year, int month, int day, int addNum)
    {
    	ListIterator<Location> itr = this.listIterator();
    	while(itr.hasNext()) {
    		int index = itr.nextIndex();
    	       Location element = itr.next();
    	       
    	       if (  ( (element.getLocality() != null && element.getLocality().equalsIgnoreCase(locality)) || (element.getLocality()== null && locality == null)) &&
    	    		   ((element.getStateProvince() != null && element.getStateProvince().equalsIgnoreCase(stateProvince)) || (element.getStateProvince()== null && stateProvince == null))  &&
    	    		   ((element.getCountry() != null && element.getCountry().equalsIgnoreCase(country)) || (element.getCountry()== null && country == null))  &&
    	    		   ((element.getCounty() != null && element.getCounty().equalsIgnoreCase(county)) || (element.getCounty()== null && county == null))
    	    	 /*  year == element.getYear()*/)
    	       {
    	    	
    	    	   element.setnumRecords(addNum);
    	    	   
    	       }   	 
    	}   	
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
    
    public void scoreDate(int year, int month, int day, int endDay, int endMonth, int endYear)
    {
    	 int dateMatch = 0;
    	
    	ListIterator<Location> itr = this.listIterator();
    	while(itr.hasNext()) {
    		int currentIndex = itr.nextIndex();
    		 Location element = itr.next();
    		 int year2 = element.getYear();
    		 int month2 = element.getMonth();
    		 int day2 = element.getDay();
   	    	 dateMatch = 0;
    	
    	if(year != 0)
   	      {
    		if((year2 >= year) && (year2 <= endYear)) 
    		{
   	    	  dateMatch = dateMatch + YEAR_MATCH_SCORE;	  
    		}
   	        else if( ((year-year2) <= YEAR_RANGE) || ((endYear-year2) >= -YEAR_RANGE) )   //year is close to range (20 years)
   	        {        	
   	        	int diff1 = Math.abs(year-year2);
   	        	int diff2 = Math.abs(endYear-year2);
   	 
   	        	if(diff1 < diff2 && diff1 < YEAR_RANGE)
   	        	{
   	        	  dateMatch = dateMatch + (YEAR_MATCH_SCORE-diff1); 
   	        	}
   	        	else if(diff2 <= diff1 && diff2 < YEAR_RANGE)
   	        	{
   	        	  dateMatch = dateMatch + (YEAR_MATCH_SCORE-diff2); 
   	        	}
   	        }
   	        	
    		if((year2 == year) && (year2 == endYear))
   	        {
   	       //Month is exact match or in range 
   	        	if(element.getMonth() == month && month != 0 && endMonth == 0  && dateMatch == YEAR_MATCH_SCORE)
   	   	    	  dateMatch = dateMatch + MONTH_MATCH_SCORE;
   	        	else if(month2 >= month &&  month2<= endMonth &&  month != 0 && endMonth !=0 && dateMatch == YEAR_MATCH_SCORE)
     	   	    	  dateMatch = dateMatch + MONTH_MATCH_SCORE;
   	        	
   	           //Day is exact match or in range 
   	        	if(day2 == day && day != 0 && endDay == 0 && dateMatch == (YEAR_MATCH_SCORE + MONTH_MATCH_SCORE))
     	   	    	  dateMatch = dateMatch + DAY_MATCH_SCORE;
   	        	else if(day2 >= day &&  day2 <= endDay &&  day != 0 && endDay !=0 && dateMatch == (YEAR_MATCH_SCORE + MONTH_MATCH_SCORE) && month==endMonth)
   	   	    	  dateMatch = dateMatch + DAY_MATCH_SCORE;
   	        	
   	      // System.out.print("scoreDate51 " + dateMatch + " " + year2 +element.getMonth() + day2 +  "\n"); 
   	        }    	
   	      }
    		addLuceneIndex( element.getCountry(),  element.getStateProvince(), element.getCounty(),  element.getLocality(),  year2, month2, day2, element.getnumRecords()+dateMatch);
   	   }  		 
 	        
    }
    
    //make Lucene list
    public int addLuceneLocations(IndexWriter w)
    {
    	ListIterator<Location> itr = this.listIterator();
    	while(itr.hasNext()) {
    		int index = itr.nextIndex();
    	       Location element = itr.next();
    	       try {
				addDoc(w, element.getContinent(), element.getCountry(), element.getCounty(), element.getStateProvince(), element.getLocality(), element.getYear(), element.getMonth(), element.getDay());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	      
    	}
    	try {
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return -1;
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
    
     void addDoc(IndexWriter w, String continent, String country, String county, String stateProvince, String location, int year, int month, int day) throws IOException {
        Document doc = new Document();
        
        int year2 = year;

        if(continent != null)
          doc.add(new TextField("continent", continent, Field.Store.YES));
        if(country != null)
          doc.add(new TextField("country", country, Field.Store.YES));
        if(county != null)
          doc.add(new TextField("county", county, Field.Store.YES));
        if(stateProvince != null)
          doc.add(new TextField("stateProvince", stateProvince, Field.Store.YES));
        if(location != null)
          doc.add(new TextField("locality", location, Field.Store.YES));
        if(year != 0)
        {
        	 if(month != 0 && day == 0)
        	 {
        	    year2 = year*10000 + month*100 + 01;
        	 }
        	 else if(month != 0 && day != 0)
        	 {
        	    year2 = year*10000 + month*100+day;
        	 }
        	 else
            	 {
            	    year2 = year*10000 + 1*100+1;
            	 }
        	 
        	 doc.add(new TextField("year", String.valueOf(year2), Field.Store.YES));
        	 System.out.println("y2 " + year2 );
        }
        if(month != 0)
        	doc.add(new TextField("month", String.valueOf(month), Field.Store.YES));
        if(day != 0)
        	doc.add(new TextField("day", String.valueOf(day), Field.Store.YES));
        	//doc.add(new IntField("year", year, Field.Store.YES));
        
        w.addDocument(doc);
    
      }
     
     public void LuceneSort(String country, String county, String stateProvince, String locality, int year, int month, int day,  int endYear, int endMonth, int endDay) throws IOException, ParseException 
     {
    	// 1. create the index
    	 System.out.println("Input1 " + year +  + month + day);
    	 System.out.println(" End " + endYear +  + endMonth + endDay);
    	 
    	 Directory index = new RAMDirectory();	    
    	 StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
    	 IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer); 

    	 IndexWriter w = new IndexWriter(index, config);
    	 addLuceneLocations(w);
    	 Query q;
    	// String special = "country:" + country + "~ OR stateProvince:" + stateProvince + "~ OR county:" + county +  "~ OR locality:" + locality  + "~ OR year:" + year + "~";
    
    	 //NEW multifield daterange
    	// NumericRangeFilter<Long> longNumericRangeFilter = NumericRangeFilter.newLongRange(rangeQueryField, rangeValue, Long.MAX_VALUE, false, false);
      //   MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_46, fieldSet.toArray(new String[0]), analyzer);
       //  queryParser.setAllowLeadingWildcard(true);

    	 
    	 boolean bLocality = false;
    	 String localityString = "";
    	 if(locality != null || stateProvince != null || county != null)
    		 bLocality = true;
    	 
    	 if(locality != null)
    	 {
    	    if(county == null)	 
    		 county = locality;
    	    
    	    if (stateProvince == null)
    	    	stateProvince = locality;
    	 }
    	 
    	 String searchString = "";
    	 if(country != null)
    	 {
    		 searchString =  searchString +  "country:" + country + "~0.8 ";
    	   if(bLocality)
    		   searchString =  searchString + "AND (";
    	 }
    	 if(county != null)
    	 {
    		 localityString =  localityString +  "county:" + county + "~";
    	 }
    	 
    	 if( stateProvince != null)
    	 {
    		 if(localityString.isEmpty() == false)
    			 localityString =  localityString + " OR ";
    	 
    		 localityString =  localityString +  "stateProvince:" +  stateProvince + "~";
    	 }
    	 
    	 if( locality != null)
    	 {
    		 if(localityString.isEmpty() == false)
    			 localityString =  localityString + " OR ";
    		 
    		 localityString =  localityString +  "locality:" +  locality + "~0.4";
    	 }
    	 searchString = searchString + localityString;
    	 if(bLocality && country != null)
    	   searchString =  searchString + ")";
    	 
    	 
    	 //Set defaults for unset dates to use format [yyyymmdd TO yyyymmdd]
    	 if(year !=0 && endYear ==0)
    		 endYear = year;
    	 
    	 if(month != 0 && endMonth ==0)
    		 endMonth = month;
    	 
    	 if(day != 0 && endDay ==0)
    		 endDay = day;
    	 
    	 if(year != 0 && month==0)
    		 month = 1;
    	 
    	 if(endYear !=0 && endMonth==0)
    		 endMonth=12;
    	 
    	 if(year != 0 && day==0)
    		 day = 1;
    	 
    	 if(endYear !=0 && endDay==0)
    		 endDay=31;
    	 
    	 if(year != 0)
    	 {
    		 String strYear = String.format("%04d", year);
    		 String strEndYear = String.format("%04d", endYear);
    		 String strDay = String.format("%02d", day);
	    	 String strMonth = String.format("%02d", month);
	    	 String strEndDay = String.format("%02d", endDay);
	    	 String strEndMonth = String.format("%02d", endMonth); 
	    	 
	    	 String strRange = "year:[" + strYear + strMonth + strDay + " TO " + strEndYear + strEndMonth + strEndDay + "]";
	    	 
	    	 if(searchString.isEmpty())
	    	 {
	    	     searchString = strRange;
	    	 }
	    	 else
	    	 {
	    		 searchString = searchString + " AND " + strRange;
	    	 }
    	 }
    	 
    	 System.out.println("special " + searchString);
    	 
    	 
			q = new QueryParser(Version.LUCENE_46, "country", analyzer).parse(searchString);
			
			System.out.println("special" + searchString);
	
			
    	    // 3. search
    	    int hitsPerPage = 10;
    	    IndexReader reader;
			
			reader = DirectoryReader.open(index);
			
    	    IndexSearcher searcher = new IndexSearcher(reader);
    	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
    	   searcher.search(q, collector);
      	    
    	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
    	    
    	    Document d;
    	    int docId =0;
    	    
    	    System.out.println("Lucene 4 ");
    	    // 4. display results
    	    System.out.println("Found " + hits.length + " hits.");
    	    
    	    //Rank how close date is to given search
    	    for(int i=0;i<hits.length;++i) {
    	      docId = hits[i].doc;
    	      d = searcher.doc(docId);
    	      System.out.println((i + 1) + ". " + d.get("country") + " "  + d.get("stateProvince") + " " + d.get("county") + " "  + d.get("year") + " " + "\t" + d.get("locality"));
    	      
    	      int addnum = LUCENE_MAX_SCORE-i*10;
    	      int year2 = 0;
    	      int month2 = 0;
    	      int day2 = 0;	      
    	      int dateMatch = 0;	      
    	   
    	      addLuceneIndex( d.get("country"),  d.get("stateProvince"), d.get("county"),  d.get("locality"),  year2, month2, day2, addnum+dateMatch);
    	    }
    	    System.out.println("Input1 " + year  + month + day);
         	System.out.println(" End " + endYear +  + endMonth + endDay + "\n");
    	    scoreDate(year, month, day, endDay, endMonth, endYear);		
		}
		
     }
     