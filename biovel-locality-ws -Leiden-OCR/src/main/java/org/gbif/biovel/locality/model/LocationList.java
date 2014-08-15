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

public class LocationList extends ArrayList<Location>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Location> tempList;
    public List<String> locationString = new ArrayList<String>();
    public List<Integer> locationNum = new ArrayList<Integer>();
    public List<String> recorder = new ArrayList<String>();
   
    public List<String> countries = new ArrayList<String>();
    public List<String> countriesOCRError = new ArrayList<String>();
    
 //   public static IndexWriter w;
    
    public LocationList(List<Location> tempLocationList) {
		// TODO Auto-generated constructor stub
    	super(tempLocationList);
    	
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
 		  //     loc.setnumRecords(loc.getnumRecords() +1 );
 		       // System.out.print("index: " +firstIndex + "curIndex: " + currentIndex + " ");
 	         }
 	         
 	        
 	   }
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
             //System.out.println("names : " + count);
             countries.add(country);
             
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
     
     public boolean isCollector(String text)
     {
    	 boolean c = (recorder.contains(text));
    	 System.out.println("collector: "  + c);
    	 return c;
     }
      
     }




