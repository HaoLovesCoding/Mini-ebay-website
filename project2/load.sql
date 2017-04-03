Load DATA LOCAL INFILE 'ItemForLoad.dat' INTO TABLE Item FIELDS TERMINATED BY ' |*| '
(ItemID, ItemName, ItemCurrentprice, ItemBuyprice, ItemFirstBid, ItemNumberOfBid, ItemLocation,@LocationLatitude,@LocationLongitude,Country, Started,Ends, SellerID, Description) SET LocationLatitude=nullif(@LocationLatitude,''),LocationLongitude=nullif(@LocationLongitude,'') ;
Load DATA LOCAL INFILE 'CatForLoad.dat' INTO TABLE Category FIELDS TERMINATED BY ' |*| ';
Load DATA LOCAL INFILE 'UserForLoad.dat' INTO TABLE User FIELDS TERMINATED BY ' |*| ' (UserID,@SellerRating,@BidderRating,Country,Location) SET SellerRating=nullif(@SellerRating,''), BidderRating=nullif(@BidderRating,'');
Load DATA LOCAL INFILE 'BidForLoad.dat' INTO TABLE Bid FIELDS TERMINATED BY ' |*| ';
