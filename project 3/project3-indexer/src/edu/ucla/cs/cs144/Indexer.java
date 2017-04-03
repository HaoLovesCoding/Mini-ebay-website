package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;
import java.util.*; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    public void closeIndexWriter() {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch(IOException e) {
                System.err.println(e);
            }
        }
    }
    public IndexWriter writer = null;//getIndexWriter(false);
    public void indexItem(int ItemID,String ItemName, String description,String category) throws IOException{
//    	IndexWriter writer=getIndexWriter(false);
	Document doc=new Document();
    	doc.add(new StringField("ItemID",Integer.toString(ItemID),Field.Store.YES));
	doc.add(new StringField("ItemName",ItemName,Field.Store.YES));
	doc.add(new StringField("description",description,Field.Store.YES));
	doc.add(new StringField("category",category,Field.Store.YES));
	String fullSearchText=ItemID+" "+ItemName+" "+description+" "+category;
	doc.add(new TextField("content",fullSearchText,Field.Store.YES));
	writer.addDocument(doc);
    }
    public boolean deleteDirectory(File path) {
    if( path.exists() ) { 
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
         if(files[i].isDirectory()) {
           deleteDirectory(files[i]);
         }   
         else {
           files[i].delete();
         }   
      }   
    }   
    return( path.delete() );
    }
    private IndexWriter indexWriter =null;
    public IndexWriter getIndexWriter(boolean create)  {//true to create the index or overwrite the existing one; false to append to the existing index
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
	    try{
	//	deleteDirectory(new File("/var/lib/lucene/index1/"));
	    	Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/"));
//		indexWriter = new IndexWriter(System.getenv("LUCENE_INDEX")+ "/ebayIndex", new StandardAnalyzer(), create);
                indexWriter = new IndexWriter(indexDir, config); 
            } catch(IOException e) {
                System.err.println(e);
            } 
        return indexWriter;
    }
    public static class ItemToBeStored{
	String ItemID;
	String ItemName;
	String ItemCategory;
	String ItemDescription;
	String SearchContent;
	ItemToBeStored(String ItemID1,String ItemName1,String ItemCategory1,String ItemDescription1){
		ItemID=ItemID1;
		ItemName=ItemName1;
		ItemCategory=ItemCategory1;
		ItemDescription=ItemDescription1;
		SearchContent=ItemID+" "+ItemName+" "+ItemCategory+" "+ItemDescription;
	}
/*	boolean ItemContainsCategory(String AnotherCat){
		
	} this is unneccessay as the category in Cat is different*/
	boolean ItemUpdateCategory(String AnotherCat){
		ItemCategory=ItemCategory+" "+AnotherCat;
		SearchContent=ItemID+" "+ItemName+" "+ItemCategory+" "+ItemDescription;
		return true;
	}
	public String getCat(){
		return ItemCategory;
	}
	public String getItemID(){
		return ItemID;
	}
	public String getName(){
		return ItemName;
	}
	public String getDescription(){
		return ItemDescription;
	}
	public String getSearchContent(){
		return SearchContent;
	}
    }
    private static HashMap<String,ItemToBeStored> ItemToBeStoredMap=new  HashMap<String,ItemToBeStored>();
    public void rebuildIndexes() throws IOException{

        Connection conn = null;
	HashMap<String,ItemToBeStored> ItemMap=new  HashMap<String,ItemToBeStored>();
        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.err.println(ex);
	}
/*	try{
		Directory indexDir=FSDirectory.open(new File("directory"));
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
		IndexWriter indexWriter=new IndexWriter(indexDir,config);
	}catch (IOException ex){
	    System.err.println(ex);
	}*/
	
	try{
	deleteDirectory(new File("/var/lib/lucene/"));//To delete the index directory
	Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT Item.ItemID, ItemName, Description,Category From Item JOIN Category ON Item.ItemID=Category.ItemID;");
	
	//PreparedStatement getItemCategories=conn.prepareStatement("SELECT Category FROM Category WHERE ItemID=?");
	while (rs.next()) {
		    String ItemIDForStorage = rs.getString("ItemID");
		    String ItemNameForStorage = rs.getString("ItemName");
		    String ItemDescriptionForStorage= rs.getString("Description");
		    String ItemCatForStorage=rs.getString("Category");
		    if(ItemMap.containsKey(ItemIDForStorage)){
			ItemToBeStored ItemRetrieved=(ItemToBeStored)ItemMap.get(ItemIDForStorage);
			ItemRetrieved.ItemUpdateCategory(ItemCatForStorage);
			ItemMap.put(ItemIDForStorage,ItemRetrieved);
			 //System.out.println(ItemRetrieved.getCat());
		    }
		    else{
		    ItemToBeStored ItemNew=new ItemToBeStored(ItemIDForStorage,ItemNameForStorage,ItemCatForStorage,ItemDescriptionForStorage);
		    ItemMap.put(ItemIDForStorage,ItemNew);
		    }		    
		}
	//* Now write the index*/
	Iterator ItemIterator=ItemMap.entrySet().iterator();
	int j=0;
	writer = getIndexWriter(false);
	while(ItemIterator.hasNext()){
		Map.Entry pair=(Map.Entry)ItemIterator.next();
		ItemToBeStored ItemForIndex=(ItemToBeStored)pair.getValue();
		indexItem(Integer.parseInt(ItemForIndex.getItemID()),ItemForIndex.getName(),ItemForIndex.getDescription(),ItemForIndex.getCat());
		if(j==1000){
			j=0;
			closeIndexWriter();
			writer = getIndexWriter(false);
		}
		//System.out.println(j);
		j++;
	}
	closeIndexWriter();
	}
	catch (SQLException ex){
		System.err.println(ex.getMessage());
	}
	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */


        // close the database connection
	try {
	//    closeIndexWriter();
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) throws IOException{
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
