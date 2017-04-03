package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ItemServlet extends HttpServlet implements Servlet {
       
	public class Item {
		private String ItemID;
		private String userID;
		private String name;
		private String rating;
		private String currently;
		private String buy_price;
		private String first_bid;
		private String number_of_bids;
		private String country;
		private String description;
		private String started;
		private String ends;
		private String location;
		private String latitude;
		private String longitude;
		private ArrayList<String> category = new ArrayList<>();
		private ArrayList<BidClass> bids = new ArrayList<>();
		public String getItemID() {
			return ItemID;
		}
		public void setItemID(String itemID) {
			ItemID = itemID;
		}
		public String getUserID() {
			return userID;
		}
		public void setUserID(String userID) {
			this.userID = userID;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}
		public String getCurrently() {
			return currently;
		}
		public void setCurrently(String currently) {
			this.currently = currently;
		}
		public String getBuy_price() {
			return buy_price;
		}
		public void setBuy_price(String buy_price) {
			this.buy_price = buy_price;
		}
		public String getFirst_bid() {
			return first_bid;
		}
		public void setFirst_bid(String first_bid) {
			this.first_bid = first_bid;
		}
		public String getNumber_of_bids() {
			return number_of_bids;
		}
		public void setNumber_of_bids(String number_of_bids) {
			this.number_of_bids = number_of_bids;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getStarted() {
			return started;
		}
		public void setStarted(String started) {
			this.started = started;
		}
		public String getEnds() {
			return ends;
		}
		public void setEnds(String ends) {
			this.ends = ends;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public ArrayList<String> getCategory() {
			return category;
		}
		public void setCategory(ArrayList<String> category) {
			this.category = category;
		}
		public ArrayList<BidClass> getBids() {
			return bids;
		}
		public void setBids(ArrayList<BidClass> bids) {
			this.bids = bids;
		}
		public String toString() {
			String newline = "<br>";
			String item = "ItemID: " + this.ItemID + newline;
			item = item + "UserID: " + this.userID + newline;
			item = item + "Name: " + this.name + newline;
			item = item + "Rating: " + this.rating + newline;
			item = item + "Currently: " + this.currently + newline;
			item = item + "Buy_Price: " + this.buy_price + newline;
			item = item + "First_Bid: " + this.first_bid + newline;
			item = item + "Number_of_Bids:" + this.number_of_bids + newline;
			item = item + "Country: " + this.country + newline;
			item = item + "Description: " + this.description + newline;
			item = item + "Started: " + this.started + newline;
			item = item + "Ends: " + this.ends + newline;
			item = item + "Location: " + this.location + newline;
			item = item + "Latitude: " + this.latitude + newline;
			item = item + "Lontitide: " + this.longitude + newline;
			String cat = "";
			for(String c : this.category) {
				cat += c;
				cat += ", ";
			}
			item = item + "Category:" + newline+ cat + newline+ "\n";
			item = item + "Bids:" + newline;
			int count = 1;
			for(BidClass b : this.bids) {
				item = item + "Bid" + count + "\n";
				item = item + b + "\n"+newline;// Why add them directly?
				count++;
			}
			if(!this.buy_price.equals("")) {
				//item = item + "<a href = \"/eBay/pay" + this.ItemID + "\">PayNow</a>";
				item = item + "<a href = \"/eBay/pay" + "\">PayNow</a>";
			}
			return item;
		}
	}
	
	public class BidClass {
		private String userID;
		private String rating;
		private String location;
		private String country;
		private String time;
		private String amount;
		public String getUserID() {
			return userID;
		}
		public void setUserID(String userID) {
			this.userID = userID;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String toString() {
			String bid = "UserID: " + this.userID + "\n";
			bid = bid + "Rating: " + this.rating + "\n";
			bid = bid + "Location: " + this.location +"\n";
			bid = bid + "Country: " + this.country + "\n";
			bid = bid + "Time: " + this.time +"\n";
			bid = bid + "Amount: " + this.amount +"\n";
			return bid;
		}
	}
	
	
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	final String query=request.getParameter("ID");
    	String XMLString=null;
    	String XMLParsed=null;
    	String LocationString=null;
    	String ItemIDString=null;
    	String BuyPriceString=null;
    	String NameString=null;
    	try{
    		if(query!=null){
    		XMLString=AuctionSearchClient.getXMLDataForItemId(query);
    		XMLParsed=processXMLString(XMLString);
    		LocationString=getLocationString(XMLString);
    		ItemIDString=getItemIDString(XMLString);
    		BuyPriceString=getBuyPriceString(XMLString);
    		NameString=getItemNameString(XMLString);
    		}
    	}
    	catch(final Exception ex) {
            ex.printStackTrace();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("ItemID", ItemIDString); 
        session.setAttribute("BuyPrice",BuyPriceString);
        session.setAttribute("ItemName",NameString);
    	request.setAttribute("XMLString", XMLParsed);
    	request.setAttribute("LocationString", LocationString);
    	request.getRequestDispatcher("/searchItem.jsp").forward(request, response);
    }
    
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
    
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
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
    
    static String ConvertDate(String DateOld) throws ParseException {
        String OldDateFormat = "MMM-dd-yy HH:mm:ss";
        String NewDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat TwoFormat= new SimpleDateFormat(OldDateFormat);
        String NewDateOut = "";
        Date RawNewDate = (Date) TwoFormat.parse(DateOld);
        TwoFormat.applyPattern(NewDateFormat);
        NewDateOut=TwoFormat.format(RawNewDate);
        return NewDateOut;
}
 
    protected String processXMLString(String xml) {//This function would new an item and set all the parameters and call the toString method to convert this item to String
    	Item item = new Item();
    	String test="No results found";
    	try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = builder.parse(is);
			Element e = doc.getDocumentElement();
			Element location = getElementByTagNameNR(e, "Location");//
        	Element seller = getElementByTagNameNR(e, "Seller");//
        	Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(e, "Bids"), "Bid");
        	Element[] categories = getElementsByTagNameNR(e, "Category");
        	ArrayList<BidClass> bidList = new ArrayList<>();
        	ArrayList<String> categoryList = new ArrayList<>();
        	//construct the item object.
        	item.setItemID(e.getAttribute("ItemID"));//
        	test=e.getAttribute("ItemID");
        	item.setUserID(seller.getAttribute("UserID"));//
        	item.setName(getElementTextByTagNameNR(e, "Name"));//
        	item.setCurrently(strip(getElementTextByTagNameNR(e, "Currently")));//
        	item.setBuy_price(strip(getElementTextByTagNameNR(e, "Buy_Price")));//
        	item.setFirst_bid(strip(getElementTextByTagNameNR(e, "First_Bid"))); //
        	item.setNumber_of_bids(getElementTextByTagNameNR(e, "Number_of_Bids"));//
        	item.setCountry(getElementTextByTagNameNR(e, "Country"));//
        	item.setDescription((getElementTextByTagNameNR(e, "Description"))); //
        	item.setStarted((getElementTextByTagNameNR(e, "Started")));//
        	item.setEnds((getElementTextByTagNameNR(e, "Ends")));//
        	item.setLocation(getElementTextByTagNameNR(e, "Location"));//
        	item.setLatitude(location.getAttribute("Latitude")); //
        	item.setLongitude(location.getAttribute("Longitude")); //
        	item.setRating(seller.getAttribute("Rating")); //
        	//construct the bids of the item.
        	for(Element bid : bids) {
        		BidClass b = new BidClass();
        		Element bidder = getElementByTagNameNR(bid, "Bidder");
        		if(bidder != null)
        			b.setUserID((bidder.getAttribute("UserID")));//
        		b.setTime((getElementTextByTagNameNR(bid, "Time"))); //
        		b.setAmount(strip(getElementTextByTagNameNR(bid, "Amount"))); //
        		b.setRating(bidder.getAttribute("Rating")); 
        		b.setLocation(getElementTextByTagNameNR(bidder, "Location")); //
        		b.setCountry(getElementTextByTagNameNR(bidder, "Country"));//
        		bidList.add(b);
        	}
        	item.setBids(bidList);
        	for(Element category : categories) {
        		categoryList.add(category.getTextContent()); 
        	}
        	item.setCategory(categoryList);
        	test=item.toString();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} 
    	return test;
    }
   
    protected String getLocationString(String xml){
    	String LocationString=null;
    	String Latitude=null;
    	String Longitude=null;
    	try{
    			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    			InputSource is = new InputSource();
    			is.setCharacterStream(new StringReader(xml));
    			Document doc = builder.parse(is);
    			Element e = doc.getDocumentElement();
    			Element location = getElementByTagNameNR(e, "Location");
            	Latitude=location.getAttribute("Latitude"); //
            	Longitude=location.getAttribute("Longitude");
            	//if(Latitude!=null && Longitude !=null){
            		LocationString=Latitude+","+Longitude;
            	//}
    	}
    	catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} 
		return LocationString;	
    }
    
    protected String getItemNameString(String xml) {//This function would new an item and set all the parameters and call the toString method to convert this item to String
    	String ItemNameString="test";
    	try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = builder.parse(is);
			Element e = doc.getDocumentElement();
			ItemNameString=getElementTextByTagNameNR(e, "Name");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} 
    	return ItemNameString;
    }
    
    protected String getBuyPriceString(String xml){
    	String BuyPriceString="test";
    	try{
    			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    			InputSource is = new InputSource();
    			is.setCharacterStream(new StringReader(xml));
    			Document doc = builder.parse(is);
    			Element e = doc.getDocumentElement();
    			BuyPriceString=strip(getElementTextByTagNameNR(e, "Buy_Price"));
    	}
    	catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} 
		return BuyPriceString;	
    }
    
    protected String getItemIDString(String xml) {//This function would new an item and set all the parameters and call the toString method to convert this item to String
    	String ItemIDString=null;
    	try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = builder.parse(is);
			Element e = doc.getDocumentElement();
			ItemIDString=e.getAttribute("ItemID");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} 
    	return ItemIDString;
    }
    
    protected String processXML(String XMLRaw) throws ParserConfigurationException, SAXException, IOException, ParseException{
        String test="aaa";
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(XMLRaw));
        Document XMLDoc=builder.parse(is);
        Element root=XMLDoc.getDocumentElement();
        Element item=root;
        //Element item=getElementByTagNameNR(root,"Item");
        Item ItemObj = new Item();
        String ItemID=""+item.getAttribute("ItemID");
        ItemObj.setItemID(ItemID);
        String ItemName=""+getElementTextByTagNameNR(item,"Name");
        ItemObj.setName(ItemName);
        String ItemCurrently=""+strip(getElementTextByTagNameNR(item,"Currently"));
        ItemObj.setCurrently(ItemCurrently);
        String ItemBuyPrice=""+strip(getElementTextByTagNameNR(item,"Buy_Price"));
        ItemObj.setBuy_price(ItemBuyPrice);
        String ItemFirstBid=""+strip(getElementTextByTagNameNR(item,"First_Bid"));
        ItemObj.setFirst_bid(ItemFirstBid);
        String ItemNumberOfBid=""+getElementTextByTagNameNR(item,"Number_of_Bids");
        ItemObj.setNumber_of_bids(ItemNumberOfBid);
        String ItemLocation=""+getElementTextByTagNameNR(item,"Location");
        ItemObj.setLocation(ItemLocation);
        Element ItemLocationElement=getElementByTagNameNR(item,"Location");
        String LocationLatitude=""+ItemLocationElement.getAttribute("Latitude");
        ItemObj.setLatitude(LocationLatitude);
        //System.out.println(LocationLatitude);
        String LocationLongitude=""+ItemLocationElement.getAttribute("Longitude");
        ItemObj.setLongitude(LocationLongitude);
        String ItemCountry=""+getElementTextByTagNameNR(item,"Country");
        ItemObj.setCountry(ItemCountry);
        String ItemStarted=""+getElementTextByTagNameNR(item,"Started");
        ItemObj.setStarted(ItemStarted);
        String ItemEnds=""+getElementTextByTagNameNR(item,"Ends");
        ItemObj.setEnds(ItemEnds);
        Element Seller=getElementByTagNameNR(item,"Seller");
        String SellerID=""+Seller.getAttribute("UserID");
        ItemObj.setUserID(SellerID);
        String SellerRating=""+Seller.getAttribute("Rating");
        ItemObj.setRating(SellerRating);
        String Description=""+getElementTextByTagNameNR(item,"Description");
        if(Description.length()>4000){
                Description=Description.substring(0,4000);
        }
    	Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
        ArrayList<BidClass> BidList=new ArrayList<BidClass>(bids.length);
        for (int j=0;j<bids.length;j++){
                String AmountString=strip(getElementTextByTagNameNR(bids[j],"Amount"));
                String TimeString=getElementTextByTagNameNR(bids[j],"Time");
                Element BidderElement=getElementByTagNameNR(bids[j],"Bidder");
                String BidderString=BidderElement.getAttribute("UserID");
                String BidderLocation=getElementTextByTagNameNR(BidderElement,"Location");
                String BidderCountry=getElementTextByTagNameNR(BidderElement,"Country");
                String BidderRating=getElementTextByTagNameNR(BidderElement,"Rating");
                BidClass BidObj=new BidClass();
                BidObj.setAmount(AmountString);
                BidObj.setTime(TimeString);
                BidObj.setUserID(BidderString);
                BidObj.setLocation(BidderLocation);
                BidObj.setCountry(BidderCountry);
                BidObj.setRating(BidderRating);
                BidList.add(BidObj);
//                      System.out.println("Haha");
        }
        ItemObj.setBids(BidList);
        Element [] CatsElement=getElementsByTagNameNR(item,"Category");
        ArrayList<String> CatList=new ArrayList<String>(CatsElement.length);
        for (int j=0;j<CatsElement.length;j++){
                String CatString=getElementText(CatsElement[j]);
                CatList.add(CatString);
        }
        ItemObj.setCategory(CatList);
        String lie="";
        test=ItemObj.toString();
        return test;
    }
}
