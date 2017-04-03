/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
	private static class CatClass  {
		String ItemID;
		ArrayList<String> list;
		CatClass(String ItemID1,ArrayList<String> listToBeCopy){
			ItemID=ItemID1;
			list=new ArrayList<String>(listToBeCopy.size());
			Iterator CatIterator=listToBeCopy.iterator();
			while(CatIterator.hasNext()){
				//System.out.println((String)CatIterator.next());//Pay Attention to here!!
				list.add((String)CatIterator.next());
				CatIterator.remove();
			}
		}
	}
	private static class ItemClass {
		String ItemID;
		String ItemName;
		String ItemCurrently;
		String ItemBuyPrice;
		String ItemFirstBid;
		String ItemNumberOfBid;
		String ItemLocation;
		String LocationLatitude;
		String LocationLongitude;
		String ItemCountry;
		String ItemStarted;
		String ItemEnds;
		String SellerID;
		String Description;
		ItemClass(String ItemID1,String ItemName1,String ItemCurrently1,String ItemBuyPrice1,String ItemFirstBid1,String ItemNumberOfBid1,String ItemLocation1,String LocationLatitude1,String LocationLongitude1,String ItemCountry1,String ItemStarted1,String ItemEnds1,String SellerID1,String Description1){
		ItemID=ItemID1;
		ItemName=ItemName1;
		ItemCurrently=ItemCurrently1;
		ItemBuyPrice=ItemBuyPrice1;
		ItemFirstBid=ItemFirstBid1;
		ItemNumberOfBid=ItemNumberOfBid1;
		ItemLocation=ItemLocation1;
		LocationLatitude=LocationLatitude1;
		LocationLongitude=LocationLongitude1;
		ItemCountry=ItemCountry1;
		ItemStarted=ItemStarted1;
		ItemEnds=ItemEnds1;
		SellerID=SellerID1;
		Description=Description1;
		}
	}
	private static class BidClass {
		String ItemID;
		String Time;
		String BidderID;
		String Amount;
		BidClass(String ItemID1,String Time1,String BidderID1,String Amount1){
			ItemID=ItemID1;
			Time=Time1;
			BidderID=BidderID1;
			Amount=Amount1;
		}
	}
	private static class UserClass{
		String UserID;
		String SellerRating;
		String BidderRating;
		String Country;
		String Location;
		UserClass(String UserID1,String SellerRating1,String BidderRating1, String Country1, String Location1){
			UserID=UserID1;
			SellerRating=SellerRating1;
			BidderRating=BidderRating1;
			Country=Country1;
			Location=Location1;
		}
		boolean Update(String UserID1,String SellerRating1,String BidderRating1, String Country1, String Location1){
			if (UserID=="" || SellerRating=="" || BidderRating=="" || Country=="" || Location==""){
				if(UserID==""){
					UserID=UserID1;
				}
				if(SellerRating==""){
					SellerRating=SellerRating1;
				}
				if(BidderRating==""){
					BidderRating=BidderRating1;
				}
				if(Country==""){
					Country=Country1;
				}
				if(Location==""){
					Location=Location1;
				}
				return true;
			}
			else{
				return false;
			}
		}
	}

	private static HashMap<String,ItemClass> ItemMap=new  HashMap<String,ItemClass>();
	private static HashMap<String, CatClass> CatMap = new HashMap<String, CatClass>();
        private static HashMap<String,ArrayList<BidClass>> BidMap=new HashMap<String,ArrayList<BidClass>>();
	private static  HashMap<String,UserClass> UserMap=new HashMap<String,UserClass>();

    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */

    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
        static String ConvertDate(String DateOld) throws ParseException {
	        String OldDateFormat = "MMM-dd-yy HH:mm:ss";
        	String NewDateFormat = "yyyy-MM-dd HH:mm:ss";
	        SimpleDateFormat TwoFormat= new SimpleDateFormat(OldDateFormat);
	        String NewDateOut = ""; 
        	Date RawNewDate = TwoFormat.parse(DateOld);
	        TwoFormat.applyPattern(NewDateFormat);
        	NewDateOut=TwoFormat.format(RawNewDate);
       		return NewDateOut;
        }

    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) throws IOException,ParseException{
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        processItem(doc);
	processCategory(doc);
	processBid(doc);
	processUser(doc);
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        /**************************************************************/
        
    }
	static void processUser(Document doc) throws IOException{
		Element root=doc.getDocumentElement();
		Element [] items=getElementsByTagNameNR(root,"Item");
		for(int i=0;i<items.length;i++){
			//System.out.println("Hahahaha");
			Element Seller=getElementByTagNameNR(items[i],"Seller");
			String SellerID=Seller.getAttribute("UserID");
			String SellerRating=Seller.getAttribute("Rating");
//			UserClass SellerObj=new UserClass(SellerID,SellerRating,"","","");
			if(UserMap.containsKey(SellerID)){
				UserClass UserRetrieved=(UserClass)UserMap.get(SellerID);
				//System.out.println("Before:"+UserRetrieved.SellerRating);
				UserRetrieved.Update(SellerID,SellerRating,"","","");
				//System.out.println("After:"+UserRetrieved.SellerRating);
				UserMap.put(SellerID,UserRetrieved);
			}
			else{
				UserClass SellerObj=new UserClass(SellerID,SellerRating,"","","");
				UserMap.put(SellerID,SellerObj);
			}
			Element BidsElement=getElementByTagNameNR(items[i],"Bids");
                        Element [] BidlistElement=getElementsByTagNameNR(BidsElement,"Bid");
			for(int j=0;j<BidlistElement.length;j++){
				Element BidderElement=getElementByTagNameNR(BidlistElement[j],"Bidder");
                                String BidderID=BidderElement.getAttribute("UserID");
				String BidderRating=BidderElement.getAttribute("Rating");
				String BidderLocation=getElementTextByTagNameNR(BidderElement,"Location");
				String BidderCountry=getElementTextByTagNameNR(BidderElement,"Country");
				if(UserMap.containsKey(BidderID)){
					UserClass UserRetrieved2=(UserClass)UserMap.get(BidderID);
					UserRetrieved2.Update(BidderID,"",BidderRating,BidderCountry,BidderLocation);
					UserMap.put(BidderID,UserRetrieved2);
				}
				else{
					UserClass BidderObj=new UserClass(BidderID,"",BidderRating,BidderCountry,BidderLocation);
					UserMap.put(BidderID,BidderObj);
				}
			}
		}
	}		
	static void WriteUser() throws IOException{	
		FileWriter UserFile=new FileWriter("User.dat");
		BufferedWriter UserWriter=new BufferedWriter(UserFile);
		Iterator UserIterator=UserMap.entrySet().iterator();
		while (UserIterator.hasNext()){
			Map.Entry pair=(Map.Entry)UserIterator.next();
			UserClass UserRetrievedWriting=(UserClass)pair.getValue();
			String UserOutput=String.format("%s |*| %s |*| %s |*| %s |*| %s\n",
						UserRetrievedWriting.UserID,
						UserRetrievedWriting.SellerRating,
						UserRetrievedWriting.BidderRating,
						UserRetrievedWriting.Country,
						UserRetrievedWriting.Location
						);
			UserWriter.write(UserOutput);
			UserIterator.remove();
		}
		UserWriter.close();
		UserFile.close();
	}
	static void processBid(Document doc) throws IOException,ParseException{
		Element root=doc.getDocumentElement();
		Element [] items=getElementsByTagNameNR(root,"Item");
		for(int i=0;i<items.length;i++){
			//System.out.println("aaa");
			String ItemID=items[i].getAttribute("ItemID");
			Element BidsElement=getElementByTagNameNR(items[i],"Bids");
			Element [] BidlistElement=getElementsByTagNameNR(BidsElement,"Bid");
			ArrayList<BidClass> BidList=new ArrayList<BidClass>(BidlistElement.length);
			for (int j=0;j<BidlistElement.length;j++){
				String AmountString=strip(getElementTextByTagNameNR(BidlistElement[j],"Amount"));
				String TimeString=ConvertDate(getElementTextByTagNameNR(BidlistElement[j],"Time"));
				Element BidderElement=getElementByTagNameNR(BidlistElement[j],"Bidder");
				String BidderString=BidderElement.getAttribute("UserID");
				BidClass BidObj=new BidClass(ItemID,TimeString,BidderString,AmountString);
				BidList.add(BidObj);
	//			System.out.println("Haha");
			}
			BidMap.put(ItemID,BidList);
		}
	}	
	static void WriteBid() throws IOException{
		FileWriter BidFile=new FileWriter("Bid.dat");
		BufferedWriter BidWriter=new BufferedWriter(BidFile);
		for ( Map.Entry<String, ArrayList<BidClass>> ee : BidMap.entrySet()) {
 			   String key = ee.getKey();
			   ArrayList<BidClass> BidListRetrieved = ee.getValue();
			        for(int i=0;i<BidListRetrieved.size();i++){
                                //System.out.println()
                                String BidOutput=String.format("%s |*| %s |*| %s |*| %s\n",BidListRetrieved.get(i).ItemID,BidListRetrieved.get(i).Time,BidListRetrieved.get(i).BidderID,BidListRetrieved.get(i).Amount);
                                BidWriter.write(BidOutput);
                                } 
    // TODO: Do something.
}
/*		Iterator BidIterator=BidMap.entrySet().iterator();
		while (BidIterator.hasNext()){
			Map.Entry pair=(Map.Entry)BidIterator.next();
			ArrayList<BidClass> BidListRetrieved=(ArrayList<BidClass>)pair.getValue();
			for(int i=0;i<BidListRetrieved.size();i++){
				//System.out.println()
				String BidOutput=String.format("%s |*| %s |*| %s |*| %s\n",BidListRetrieved.get(i).ItemID,BidListRetrieved.get(i).Time,BidListRetrieved.get(i).BidderID,BidListRetrieved.get(i).Amount);
				BidWriter.write(BidOutput);
				}
			BidIterator.remove();
		}*/
		BidWriter.close();
		BidFile.close();
	}

	static void processCategory(Document doc) throws IOException{
		Element root=doc.getDocumentElement();
		Element [] items=getElementsByTagNameNR(root,"Item");
		for (int i=0;i<items.length;i++){
			String ItemID=""+items[i].getAttribute("ItemID");
			Element [] CatsElement=getElementsByTagNameNR(items[i],"Category");
			ArrayList<String> CatList=new ArrayList<String>(CatsElement.length);
			for (int j=0;j<CatsElement.length;j++){
				String CatString=getElementText(CatsElement[j]);
				CatList.add(CatString);
	//			System.out.println(CatString);
			}
			//System.out.println(CatList.get(3));
			CatClass CatForHash=new CatClass(ItemID,CatList);
			//System.out.println(CatForHash.ItemID);
			//System.out.println("list");
			//System.out.println(CatForHash.list);
			CatMap.put(ItemID,CatForHash);//If it has repeat it will just replace!!!!
		}
	}
	static void WriteCat() throws IOException{       
	        FileWriter CatFile=new FileWriter("Cat.dat");
                BufferedWriter CatWriter=new BufferedWriter(CatFile);
                Iterator CatIterator=CatMap.entrySet().iterator();
		while (CatIterator.hasNext()){
			Map.Entry pair=(Map.Entry)CatIterator.next();
			CatClass CatRetrieved=(CatClass)pair.getValue();
			for (int i=0;i<CatRetrieved.list.size();i++){
				String CatOutput=String.format("%s |*| %s\n", CatRetrieved.ItemID, CatRetrieved.list.get(i));
	//			System.out.println(CatOutput);
				CatWriter.write(CatOutput);
			}
			CatIterator.remove();
		}
                CatWriter.close();
                CatFile.close();
	}
	static void processItem(Document doc) throws IOException,ParseException{	
 		Element root=doc.getDocumentElement();//This is a convenience attribute that allows direct access to the child node that is the document element of the document. Here it will get items as root
		//System.out.println(root);      
		Element [] items=getElementsByTagNameNR(root,"Item");//Find all the element by the tag "Item"
	/*	for (int i=0;i<items.length;i++){
			System.out.println(items[i].getAttribute("ItemID"));
	        }//Display*/
		for (int i=0;i<items.length;i++){
			String ItemID=""+items[i].getAttribute("ItemID");
			String ItemName=""+getElementTextByTagNameNR(items[i],"Name");
			String ItemCurrently=""+strip(getElementTextByTagNameNR(items[i],"Currently"));
			String ItemBuyPrice=""+strip(getElementTextByTagNameNR(items[i],"Buy_Price"));
			String ItemFirstBid=""+strip(getElementTextByTagNameNR(items[i],"First_Bid"));
			String ItemNumberOfBid=""+getElementTextByTagNameNR(items[i],"Number_of_Bids");
			String ItemLocation=""+getElementTextByTagNameNR(items[i],"Location");
			Element ItemLocationElement=getElementByTagNameNR(items[i],"Location");
			String LocationLatitude=""+ItemLocationElement.getAttribute("Latitude");
			//System.out.println(LocationLatitude);
	               	String LocationLongitude=""+ItemLocationElement.getAttribute("Longitude");
			String ItemCountry=""+getElementTextByTagNameNR(items[i],"Country");
			String ItemStarted=""+ConvertDate(getElementTextByTagNameNR(items[i],"Started"));
			String ItemEnds=""+ConvertDate(getElementTextByTagNameNR(items[i],"Ends"));
			Element Seller=getElementByTagNameNR(items[i],"Seller");
			String SellerID=""+Seller.getAttribute("UserID");
			String Description=""+getElementTextByTagNameNR(items[i],"Description");
                	if(Description.length()>4000){
				Description=Description.substring(0,4000);
			}	
			ItemClass ItemForHash=new ItemClass(ItemID,ItemName,ItemCurrently,ItemBuyPrice,ItemFirstBid,ItemNumberOfBid,ItemLocation,LocationLatitude,LocationLongitude,ItemCountry,ItemStarted,ItemEnds,SellerID,Description);
			ItemMap.put(ItemID,ItemForHash);
		}
	}
	static void WriteItem() throws IOException{
   		FileWriter ItemFile=new FileWriter("Item.dat");
		BufferedWriter ItemWriter=new BufferedWriter(ItemFile);
	        Iterator ItemIterator=ItemMap.entrySet().iterator();
		while(ItemIterator.hasNext()){
			Map.Entry pair=(Map.Entry)ItemIterator.next();//Use iterator to realize the iteration!!
			ItemClass ItemRetrieved=(ItemClass)pair.getValue();
			String output=ItemRetrieved.ItemID;
			String ItemOutput=String.format("%s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s\n",
				ItemRetrieved.ItemID, 
				ItemRetrieved.ItemName,
				ItemRetrieved.ItemCurrently,
				ItemRetrieved.ItemBuyPrice,
				ItemRetrieved.ItemFirstBid,
				ItemRetrieved.ItemNumberOfBid,
				ItemRetrieved.ItemLocation,
				ItemRetrieved.LocationLatitude,
				ItemRetrieved.LocationLongitude,
				ItemRetrieved.ItemCountry,
				ItemRetrieved.ItemStarted,
				ItemRetrieved.ItemEnds,
				ItemRetrieved.SellerID,
				ItemRetrieved.Description);
	//	System.out.println(ItemOutput);
			ItemWriter.write(ItemOutput);
			ItemIterator.remove();
		}
		ItemWriter.close();
		ItemFile.close();
	}
    public static void main (String[] args) throws IOException,ParseException{
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
	WriteUser();
	WriteBid();
	WriteCat();
	WriteItem();
    }
}
