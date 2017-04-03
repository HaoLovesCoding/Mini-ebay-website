package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.StringWriter;
import java.util.*;
//import java.util.Iterator;
//import java.util.ArrayList;
//import java.util.List;
import java.text.SimpleDateFormat;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.OutputKeys;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		SearchResult[] SearchStore=null;
		// TODO: Your code here!
		SearchStore=null;
		try{
			IndexSearcher searcher = null;
			QueryParser parser = null;
			
			int endCount=0;
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/"))));
	        	parser = new QueryParser("content", new StandardAnalyzer());
			Query Luc_query = parser.parse(query);
	        	ScoreDoc[] hits=searcher.search(Luc_query,numResultsToReturn+numResultsToSkip).scoreDocs;
	//		System.out.println("aa");
	//		System.out.println(hits.length);
			if(numResultsToSkip>hits.length){
				return new SearchResult[0];
			}
			else if(hits.length<(numResultsToReturn+numResultsToSkip)){
				endCount=hits.length;
				SearchStore = new SearchResult[hits.length-numResultsToSkip];
			}
			else{
				SearchStore = new SearchResult[numResultsToReturn];
				endCount=numResultsToReturn+numResultsToSkip;
			}
			//System.out.println(endCount);
			for(int Count=0;Count < endCount;Count++){
				if(Count>numResultsToSkip-1){
					Document doc=searcher.doc(hits[Count].doc);
					SearchStore[Count-numResultsToSkip]=new SearchResult(doc.get("ItemID"),doc.get("ItemName"));
					//System.out.println(SearchStore[Count-numResultsToSkip]);
					//System.out.println(Count-numResultsToSkip);
				}
			}
			}catch(Exception e){
				System.err.println(e);
			}
			return SearchStore;

		}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
	//	org.w3c.dom.Document doc = null;
		Connection conn=null;
		SearchResult[] SearchStore=null;
		int endCount=0;
                try{
			conn=DbManager.getConnection(true);
			Statement stmt=conn.createStatement();
			String SetPolyQuery="set @poly=GeomFromText('Polygon(("+region.getLx()+" "+region.getLy()+","
					+region.getRx()+ " " + region.getLy() + ", "
					+region.getRx()+ " " + region.getRy() + ", " 
					+region.getLx()+ " " + region.getRy() + ", "
					+region.getLx()+ " " + region.getLy() +"))');";
			//System.out.println(SetPolyQuery);
			stmt.executeQuery(SetPolyQuery);
			String GetLocationItem="SELECT ItemID, AsText(Coordinate) FROM Locations WHERE MBRContains(@poly,Coordinate);";
			ResultSet rs=stmt.executeQuery(GetLocationItem);
			HashSet<String> ItemRegionMap=new HashSet<String>();
			while(rs.next()){
				ItemRegionMap.add(rs.getString("ItemID"));
				//System.out.println(rs.getString("ItemID"));
			}
			SearchResult[] BasicResult=basicSearch(query, 0, Integer.MAX_VALUE);
			ArrayList<SearchResult> RealResultList=new ArrayList<SearchResult>();
			for(SearchResult temp:BasicResult){
				if(ItemRegionMap.contains(temp.getItemId())){
					RealResultList.add(temp);
				}
			}
			int ResultSize=RealResultList.size();
			//System.out.println("size");
			//System.out.println(ResultSize);
                        if(numResultsToSkip>ResultSize){
                                return new SearchResult[0];
                        }   
                        else if(ResultSize<(numResultsToReturn+numResultsToSkip)){
                                endCount=ResultSize;
                                SearchStore = new SearchResult[ResultSize-numResultsToSkip];
                        }   
                        else{
                                SearchStore = new SearchResult[numResultsToReturn];
                                endCount=numResultsToReturn+numResultsToSkip;
                        }   
                        //System.out.println(endCount);
                        for(int Count=0;Count < endCount;Count++){
                                if(Count>numResultsToSkip-1){
                                        SearchStore[Count-numResultsToSkip]=RealResultList.get(Count);
                                        //System.out.println(SearchStore[Count-numResultsToSkip]);
                                        //System.out.println(Count-numResultsToSkip);
                                }   
                        }   			
                   }catch(Exception e){ 
                                System.err.println(e);
                        }   
                        return SearchStore;

	}
	public class Bid{
		String ItemID;
		String Time;
		String BidderID;
		String Amount;
		public Bid(String ItemID1,String Time1,String BidderID1,String Amount1){
			ItemID=ItemID1;
			Time=Time1;
			BidderID=BidderID1;
			Amount=Amount1;
		}
		public String getBidderID(){
			return BidderID;
		}
		public String getBidTime(){
			return Time;
		}
		public String getBidAmount(){
			return Amount;
		}
	}
	public class Bidder{
		String UserID;
		String SellerRating;
		String BidderRating;
		String Country;
		String Location;
		public Bidder(String UserID1,String SellerRating1,String BidderRating1,String Country1,String Location1){
			UserID=UserID1;
			SellerRating=SellerRating1;
			BidderRating=BidderRating1;
			Country=Country1;
			Location=Location1;
		}
		public String getBidderRating(){
			return BidderRating;
		}
		public String getBidderID(){
			return UserID;
		}
		public String getBidderLocation(){
			return Location;
		}
		public String getBidderCountry(){
			return Country;
		}
	}
	public String getXMLDataForItemId(String itemId) {
		Connection conn=null;
		ResultSet ItemSet,BidSet,SellerSet,BidderSet,CatSet=null;
		PreparedStatement ItemQuery,BidQuery,SellerQuery,CatQuery=null;
		StringWriter TextWriter=null;
		DocumentBuilderFactory docFactory;
		DocumentBuilder docBuilder;
		SimpleDateFormat SQLformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
		SimpleDateFormat XMLformat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		org.w3c.dom.Document doc = null;
		Element ItemRoot=null;
		Element anotherElement=null;
		Element BidsElement=null;
		Element BidElement=null;
		Element BidderElement=null;
		String ItemString=null;
		String ItemNameString=null;
		String ItemCurrentpriceString=null;
		String ItemBuypriceString=null;
		String ItemFirstBidString=null;
		String ItemNumberOfBidString=null;
		String ItemLocationString=null;
		String LocationLatitude=null;
		String LocationLongitude=null;
		String CountryString=null;
		String StartedString=null;
		String EndsString=null;
		String SellerIDString=null;
		String Description=null;
		//Now this is Bid Variable
		String ItemIDString1=null;
		String TimeString1=null;
		String BidderIDString=null;
		String AmountString=null;
		//User Variable
		String SellerSellerIDString=null;
		String SellerRatingString=null;
		String BidderRatingString=null;
		String SellerCountryString=null;
		String SellerLocationString=null;
		try{
			conn=DbManager.getConnection(true);
			ItemQuery=conn.prepareStatement("SELECT * FROM Item WHERE ItemID=?;");

			ItemQuery.setString(1,itemId);
			ItemSet=ItemQuery.executeQuery();
			//System.out.println(ItemSet.getString("ItemID"));
			int i=0;
			while(ItemSet.next()&&i==0){
				i++;
				ItemString=ItemSet.getString("ItemID");
				//System.out.println(ItemString);
				ItemNameString=ItemSet.getString("ItemName");
				//System.out.println(ItemNameString);
				ItemCurrentpriceString=ItemSet.getString("ItemCurrentprice");
				//System.out.println(ItemCurrentpriceString);
				ItemBuypriceString=ItemSet.getString("ItemBuyprice");
				//System.out.println(ItemBuypriceString);
				ItemFirstBidString=ItemSet.getString("ItemFirstBid");
				//System.out.println(ItemFirstBidString);
				ItemNumberOfBidString=ItemSet.getString("ItemNumberOfBid");
				//System.out.println(ItemNumberOfBidString);
				ItemLocationString=ItemSet.getString("ItemLocation");
				//System.out.println(ItemLocationString);
				LocationLatitude=ItemSet.getString("LocationLatitude");
				//System.out.println(LocationLatitude);
				LocationLongitude=ItemSet.getString("LocationLongitude");
				//System.out.println(LocationLongitude);
				CountryString=ItemSet.getString("Country");
				//System.out.println(CountryString);
				StartedString=ItemSet.getString("Started");
				//System.out.println(StartedString);
				EndsString=ItemSet.getString("Ends");
				//System.out.println(EndsString);
				SellerIDString=ItemSet.getString("SellerID");
				//System.out.println(SellerIDString);
				Description=ItemSet.getString("Description");
				//System.out.println(Description);			
			}
			//System.out.println(SellerIDString);
			BidQuery=conn.prepareStatement("SELECT * FROM Bid WHERE ItemID=?;");
			BidQuery.setString(1,itemId);
			//System.out.println(BidQuery);
			BidSet=BidQuery.executeQuery();
			ArrayList<Bid> BidList=new ArrayList<Bid>();
			while(BidSet.next()){
				ItemIDString1=BidSet.getString("ItemID");
			//	System.out.println(ItemIDString1);
				TimeString1=BidSet.getString("Time");
			//	System.out.println(TimeString1);
				BidderIDString=BidSet.getString("BidderID");
			//	System.out.println(BidderIDString);
				AmountString=BidSet.getString("Amount");
			//	System.out.println(AmountString);
				Bid BidTemp=new Bid(ItemIDString1,TimeString1,BidderIDString,AmountString);
				BidList.add(BidTemp);
			}
			//System.out.println(SellerIDString);
			SellerQuery=conn.prepareStatement("SELECT * FROM User WHERE UserID=?;");
			SellerQuery.setString(1,SellerIDString);
			SellerSet=SellerQuery.executeQuery();
			i=0;
			while(SellerSet.next()&&i==0){
				SellerSellerIDString=SellerSet.getString("UserID");
			//	System.out.println(SellerSellerIDString);
				SellerRatingString=SellerSet.getString("SellerRating");
			//	System.out.println(SellerRatingString);
				BidderRatingString=SellerSet.getString("BidderRating");
			//	System.out.println(BidderRatingString);
				SellerCountryString=SellerSet.getString("Country");
			//	System.out.println(SellerCountryString);
				SellerLocationString=SellerSet.getString("Location");
			//	System.out.println(SellerLocationString);
			}
			//Now it
			ArrayList<Bidder> BidderList=new ArrayList<Bidder>(); 
			for(int j=0;j<BidList.size();j++){
				Bid bidtemp=BidList.get(j);
				String Bidder=bidtemp.getBidderID();
				SellerQuery.setString(1,Bidder);
			//	System.out.println(SellerQuery);
				BidderSet=SellerQuery.executeQuery();
				while(BidderSet.next()){                        //BidderList Must correspond to the right sequence in Bid
					String BidderID=BidderSet.getString("UserID");
			//		System.out.println(BidderID);
					String BidderSellerRating=BidderSet.getString("SellerRating");
			//		System.out.println(BidderSellerRating);
					String BidderBidderRating=BidderSet.getString("BidderRating");
			//		System.out.println(BidderBidderRating);
					String BidderCountryString=BidderSet.getString("Country");
			//		System.out.println(BidderCountryString);
					String BidderLocation=BidderSet.getString("Location");
			//		System.out.println(BidderLocation);
					Bidder biddertemp=new Bidder(BidderID,BidderSellerRating,BidderBidderRating,BidderCountryString,BidderLocation);
					BidderList.add(biddertemp);
				}
			}
			CatQuery=conn.prepareStatement("SELECT * FROM Category WHERE ItemID=?;");
			CatQuery.setString(1,itemId);
			//System.out.println(CatQuery);
			CatSet=CatQuery.executeQuery();
			ArrayList<String> CatList=new ArrayList<String>();
			while(CatSet.next()){
				String CatTemp=CatSet.getString("Category");//create a new memory space so arraylist won't be overrite
				CatList.add(CatTemp);
			}
			/*for (int j=0;j<CatList.size();j++){
				System.out.println(CatList.get(j));
			}*/
			docFactory=DocumentBuilderFactory.newInstance();
			docBuilder=docFactory.newDocumentBuilder();
			doc=docBuilder.newDocument();
			
			ItemRoot=doc.createElement("Item");
			ItemRoot.setAttribute("ItemID",itemId);
			doc.appendChild(ItemRoot);
			if(ItemString==null){
				return "";
			}
			anotherElement=doc.createElement("Name");
			anotherElement.setTextContent(ItemNameString);
			ItemRoot.appendChild(anotherElement);
			for(int k=0;k<CatList.size();k++){
				anotherElement=doc.createElement("Category");
				anotherElement.setTextContent(CatList.get(k));
				ItemRoot.appendChild(anotherElement);
			}
			
			anotherElement=doc.createElement("Currently");
			anotherElement.setTextContent("$"+ItemCurrentpriceString);
			ItemRoot.appendChild(anotherElement);

			anotherElement=doc.createElement("First_Bid");
			anotherElement.setTextContent("$"+ItemFirstBidString);
			ItemRoot.appendChild(anotherElement);
			
			anotherElement=doc.createElement("Number_of_Bids");
			anotherElement.setTextContent(ItemNumberOfBidString);
			ItemRoot.appendChild(anotherElement);

			BidsElement=doc.createElement("Bids");
			ItemRoot.appendChild(BidsElement);
			for(int kk=0;kk<BidList.size();kk++){
				BidElement=doc.createElement("Bid");
				BidsElement.appendChild(BidElement);
				BidderElement=doc.createElement("Bidder");
				String BidderRatingTemp=BidderList.get(kk).getBidderRating();
				String BidderIDTemp=BidderList.get(kk).getBidderID();
				String BidderLocationTemp=BidderList.get(kk).getBidderLocation();
				String BidderCountryTemp=BidderList.get(kk).getBidderCountry();

				
				BidderElement.setAttribute("Rating",BidderRatingTemp);
				BidderElement.setAttribute("UserID",BidderIDTemp);
				BidElement.appendChild(BidderElement);

				anotherElement=doc.createElement("Location");
				anotherElement.setTextContent(BidderLocationTemp);
				BidderElement.appendChild(anotherElement);
				
				anotherElement=doc.createElement("Country");
				anotherElement.setTextContent(BidderCountryTemp);
				BidderElement.appendChild(anotherElement);
				
				String BidTimeTemp=BidList.get(kk).getBidTime();				
				anotherElement=doc.createElement("Time");
				anotherElement.setTextContent(XMLformat.format(SQLformat.parse(BidTimeTemp)));
				BidElement.appendChild(anotherElement);
				
				String BidAmountTime=BidList.get(kk).getBidAmount();
				anotherElement=doc.createElement("Amount");
				anotherElement.setTextContent("$"+BidAmountTime);
				BidElement.appendChild(anotherElement);
			}
			
			anotherElement=doc.createElement("Location");
			anotherElement.setAttribute("Latitude",LocationLatitude);
			anotherElement.setAttribute("Longitude",LocationLongitude);
			anotherElement.setTextContent(ItemLocationString);
			ItemRoot.appendChild(anotherElement);		
			
			anotherElement=doc.createElement("Country");
			anotherElement.setTextContent(CountryString);
			ItemRoot.appendChild(anotherElement);

			anotherElement=doc.createElement("Started");
			anotherElement.setTextContent(XMLformat.format(SQLformat.parse(StartedString)));
			ItemRoot.appendChild(anotherElement);

			anotherElement=doc.createElement("Ends");
			anotherElement.setTextContent(XMLformat.format(SQLformat.parse(EndsString)));
			ItemRoot.appendChild(anotherElement);

			anotherElement=doc.createElement("Seller");
			anotherElement.setAttribute("Rating",SellerRatingString);
			anotherElement.setAttribute("UserID",SellerSellerIDString);
			ItemRoot.appendChild(anotherElement);

			anotherElement=doc.createElement("Description");
			anotherElement.setTextContent(Description);
			ItemRoot.appendChild(anotherElement);
			//Write the file
			DOMSource domSource=new DOMSource(doc);
			TextWriter=new StringWriter();
			StreamResult TextResult=new StreamResult(TextWriter);
			TransformerFactory TextTransFac=TransformerFactory.newInstance();
			Transformer TextTransformer=TextTransFac.newTransformer();
			TextTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			TextTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			TextTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			TextTransformer.transform(domSource,TextResult);
			
			//System.out.println(XMLformat.format(SQLformat.parse(EndsString)));
			//System.out.println(SellerQuery);
		}
		catch(Exception e){
			System.err.println(e);
		}
		return TextWriter.toString();
	}
	
	public String echo(String message) {
		return message;
	}

}
