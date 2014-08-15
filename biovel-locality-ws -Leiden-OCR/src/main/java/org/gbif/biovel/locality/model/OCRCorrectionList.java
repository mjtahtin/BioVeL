package org.gbif.biovel.locality.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class OCRCorrectionList extends ArrayList<OcrCorrection>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Location> tempList;
    public List<String> locationString = new ArrayList<String>();
    public List<Integer> locationNum = new ArrayList<Integer>();
    public List<String> recorder = new ArrayList<String>();
    public List<String> recorderOCRError = new ArrayList<String>();
    
    public List<String> countries = new ArrayList<String>();
    public List<String> countriesOCRError = new ArrayList<String>();
    public List<String> countriesOCRCorrection = new ArrayList<String>();
    
 //   public static IndexWriter w;
    
   
    
    public OCRCorrectionList() {
		// TODO Auto-generated constructor stub
    //	super(tempLocationList);
    	
    	StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);

	/*    try {
	    	System.out.println("Lucene IndexWriter 0 ");
			w = new IndexWriter(index, config);
			System.out.println("Lucene IndexWriter ok ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Lucene IndexWriter fail ");
			e.printStackTrace();
		}*/
	    
    	
	}

	void LocationList()
    {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

	    // 1. create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);

	/*    try {
	    	System.out.println("Lucene IndexWriter 0 ");
			w = new IndexWriter(index, config);
			System.out.println("Lucene IndexWriter ok ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Lucene IndexWriter fail ");
			e.printStackTrace();
		}*/
    }
	
	protected void finalize()
	{
		
	/*  try {
	/*	//w.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	}
	
	public List<String> getOcrKeyChars(String ocrText)
	{
		
	//	System.out.print("getOcrKeyChars: " + ocrText);
		List<String> tempList = new ArrayList<String>();
		
		for (int loop=0;  loop < ocrText.length(); loop++ ){
	//		System.out.print("getOcrKeyChars: " + loop);
			Character c = ocrText.charAt(loop); 
			if(c.equals('v'))
			{	   
				char[] cArray = new char[ocrText.length()];
				
				ocrText.getChars(0,  ocrText.length(), cArray, 0);
				cArray[loop] = 'x';
				
				String st2 = new String("");
				st2 =  String.copyValueOf(cArray);
				
			   tempList.add(cArray.toString());
			   this.countriesOCRError.add(st2);
			   this.countriesOCRCorrection.add(ocrText);
			   
			   cArray[loop] = 'y';
			   st2 =  String.copyValueOf(cArray);
				
			   tempList.add(cArray.toString());
			   this.countriesOCRError.add(st2);
			   this.countriesOCRCorrection.add(ocrText);
			   
			   System.out.print("OCRTOFIX: " + st2);	
			//	"x"
			//	"y"
			}
			if(c.equals('x'))
			{
				   
				char[] cArray = new char[ocrText.length()];
				
				ocrText.getChars(0,  ocrText.length(), cArray, 0);
				cArray[loop] = 'y';
				
				String st2 = new String("");
				st2 =  String.copyValueOf(cArray);
				
			   tempList.add(cArray.toString());
			   this.countriesOCRError.add(st2);
			   this.countriesOCRCorrection.add(ocrText);
			   
			   System.out.print("OCRTOFIX: " + st2);	
			//	"x"
			//	"y"
			}

			
		}
		
		return tempList;
		
	}
	
	public void AddCountrywithOcrKeys (String loc)
    {
    	if(locationString.contains(loc) == false)
    	{
    		countries.add(loc);
    		CreateOcrCountryKeys(loc);
    		
    		List<String> tempOcrList = getOcrKeyChars(loc);
    		
    		
    		int index = locationString.indexOf(loc);  		
    	}		
    }
	
	public void CreateOcrCountryKeys(String loc)
    {
    	if(countries.contains(loc) == false)
    	{
    		countriesOCRError.add(loc);
    		//CreateOcrCountryKeys(loc);
    		
    		int index = locationString.indexOf(loc);  		
    	}		
    }
	
	
    public void AddLocationString (String loc)
    {
    	if(locationString.contains(loc) == false)
    	{
    		locationString.add(loc);
    		int index = locationString.indexOf(loc);  		
    	}		
    }
    
   
    
    public int findLocationIndex(String location)
    {
    	ListIterator<OcrCorrection> itr = this.listIterator();
    	while(itr.hasNext()) {
    		int index = itr.nextIndex();
    	       OcrCorrection element = itr.next();
    	       if (element.getUnCorrected().equalsIgnoreCase(location))
    	    	   return index;
    	}
    	return -1;
    }
    
   
    
    
    //make Lucene list
    //void addDoc(IndexWriter w, String continent, String country, String county, String stateProvince, String location)
    public int addLuceneLocations(IndexWriter w)
    {
    	//ListIterator<Location> itr = this.listIterator();
    	
    	ListIterator<String> itr = recorder.listIterator();
    	
    	while(itr.hasNext()) {
    		int index = itr.nextIndex();
    	       String element = itr.next();
    	       try {
				addDoc(w, element);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	      // if (element.getLocationString(1).equalsIgnoreCase(location))
    	    //	   return index;
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
    
    // void addDoc(IndexWriter w, String recordedBy, String continent, String country, String county, String stateProvince, String location) throws IOException {
    void addDoc(IndexWriter w, String recordedBy) throws IOException {
    Document doc = new Document();
  //      System.out.println("Lucene A1 ");
       if(recordedBy != null)
            doc.add(new TextField("recordedBy", recordedBy, Field.Store.YES));
      
       /*if(continent != null)
          doc.add(new TextField("continent", continent, Field.Store.YES));
        if(country != null)
          doc.add(new TextField("country", country, Field.Store.YES));
        if(county != null)
          doc.add(new TextField("county", county, Field.Store.YES));
        if(stateProvince != null)
          doc.add(new TextField("stateProvince", stateProvince, Field.Store.YES));
        if(location != null)
          doc.add(new TextField("locality", location, Field.Store.YES));*/
       

        // use a string field for isbn because we don't want it tokenized
       // doc.add(new StringField("isbn", isbn, Field.Store.YES));
  //      System.out.println("Lucene A2 ");
        w.addDocument(doc);
  //      System.out.println("Lucene A3 ");
      }
     
     public void printLucene(String recordedBy, String country, String county, String stateProvince, String locality) throws IOException, ParseException 
     {
    	// 1. create the index
    	 
   // 	 System.out.println("Lucene 1 ");
    	 
    	 Directory index = new RAMDirectory();
    	    
    	 StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
    	 IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);

    	 IndexWriter w = new IndexWriter(index, config);
	 
    	
    	 addLuceneLocations(w);
    	 
    	
    	 Query q;
		//try {
    	 String special = "recordedBy:" +  recordedBy +  "~ OR locality:" + locality + "~0.95";
			q = new QueryParser(Version.LUCENE_46, "recordedBy", analyzer).parse(special);
			
			
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_46,
                    new String[] {"recordedBy", "contry", "county", "stateProvince", "locality"},
                    analyzer);
			
			
			
			//QueryParser temp = new QueryParser(Version.LUCENE_46, "country", analyzer);
			

		//	Hits hits = searcher.Search(queryParser.parse(special));
		
			// TODO Auto-generated catch block
			
//			 System.out.println("Lucene 3 ");
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
    	    
//    	    System.out.println("Lucene 4 ");
    	    // 4. display results
    	    System.out.println("Found " + hits.length + " hits.");
    	    for(int i=0;i<hits.length;++i) {
    	      docId = hits[i].doc;
    	     // Document d;
    	      d = searcher.doc(docId);
    	      System.out.println((i + 1) + ". " + d.get("recordedBy") +  " " + d.get("country") + d.get("county") + d.get("stateProvince") + "\t" + d.get("locality"));
    	    }
    	 //   d = searcher.doc(docId);
    	//    System.out.println((i + 1) + ". " + d.get("country") + "\t" + d.get("location"));
    	    
				
		
		/*	 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	     // System.out.println((i + 1) + ". " + d.get("country") + "\t" + d.get("location"));
 catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    }*/
     }
    	 
     public void ReadCollectors()
     {
     //Get collector list
     String dbURL = "jdbc:mysql://127.0.0.1:3306/uio_assistant";
//	 String dbURL = "jdbc:mysql://localhost:13306/marko";
       // String username ="marko";
       // String password = "QYtjsAuyndUnKMyj";
     String username ="root";
     String password = "";
       
        Connection dbCon = null;
        Statement stmt = null;
        ResultSet rs = null;
       
        String query ="select DISTINCT recordedBy from location";
        String queryCountry = "select DISTINCT country from location order by country";
       
        try {
            //getting database connection to MySQL server
            dbCon = DriverManager.getConnection(dbURL, username, password);
           
            //getting PreparedStatment to execute query
            stmt = dbCon.prepareStatement(query);
           
            //Resultset returned by query
            rs = stmt.executeQuery(query);
           
            while(rs.next()){
             String recordedBy = rs.getString(1);
             //System.out.println("names : " + count);
             recorder.add(recordedBy);
           //  String empty = new String ("");
             // this.addLuceneLocations(w)addLucene(recordedBy);
            }
            
            // country
            //getting PreparedStatment to execute query
            stmt = dbCon.prepareStatement(query);
           
            //Resultset returned by query
            rs = stmt.executeQuery(queryCountry);
           
            while(rs.next()){
             String country = rs.getString(1);
             System.out.println("names : " + country);
             if(country != null) 
               this.AddCountrywithOcrKeys(country);
            // countries.add(country);
           //  String empty = new String ("");
             // this.addLuceneLocations(w)addLucene(recordedBy);
            }
           
           
        } catch (SQLException ex) {
      //      Logger.getLogger(CollectionTest.class.getName()).log(Level.SEVERE, null, ex);
        	System.out.println(ex.getStackTrace());
        	System.out.println("count of stock : " );
        } finally{
           //close connection ,stmt and resultset here
        	try { dbCon.close();
        	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	finally {}
        	
        	}
        }
     
     public boolean isCountry(String text)
     {
    	 boolean c = (countries.contains(text));
    	 System.out.println("country: "  + c);
    	 return c;
     }
     
     public boolean isOCRFixCountry(String text)
     { 
    	 String fixedCountry = new String();
    	 boolean c = ( countriesOCRError.contains(text));
    	 
    	 if(c == true)
    	 {
    		 int index = countriesOCRError.lastIndexOf(text);
    		 fixedCountry = countriesOCRCorrection.get(index);
    		 
    		 System.out.println("countryFixed2: "  + fixedCountry);
    	 }
    	 
    	 System.out.println("countryFixed: "  + fixedCountry);
    	 return c;
     }
     
     public String getOCRFixCountry(String text)
     {
    	 System.out.println("countryFixed4: "  );
    	 String fixedCountry = new String();
    	 boolean c = ( countriesOCRError.contains(text));
    	 
    	 if(c == true)
    	 {
    		 int index = countriesOCRError.lastIndexOf(text);
    		 fixedCountry = countriesOCRCorrection.get(index);
    		 
    		 System.out.println("countryFixed3: "  + fixedCountry);
    		 
    	 }
    	 
    	 return fixedCountry;
    	// System.out.println("countryFixed: "  + fixedCountry);
    	// return c;
     } 
     
     public boolean isCollector(String text)
     {
    	 boolean c = (recorder.contains(text));
    	 System.out.println("collector: "  + c);
    	 return c;
     }
     
     public boolean isOCRFixCollector(String text)
     {
    	 boolean c = (recorderOCRError.contains(text));
    	 System.out.println("collectorFixed: "  + c);
    	 return c;
     }
      
     }




