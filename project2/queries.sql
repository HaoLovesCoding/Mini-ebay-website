SELECT COUNT(*) FROM User;
SELECT COUNT(*) FROM Item WHERE Binary ItemLocation = 'New York';
SELECT COUNT(*) FROM (select ItemID, count(distinct Category) as count_num from Category group by ItemID Having count_num=4) T;
SELECT ItemID FROM Item WHERE ItemCurrentprice=(SELECT MAX(ItemCurrentprice) FROM Item WHERE Ends>"2001-12-20 00:00:01" AND ItemNumberOfBid>0) AND Ends>"2001-12-20 00:00:01" AND ItemNumberOfBid>0;
SELECT COUNT(*) FROM User WHERE SellerRating > 1000;
SELECT COUNT(*) FROM User Where SellerRating IS NOT NULL AND BidderRating IS NOT NULL;
SELECT COUNT(DISTINCT Category) FROM Category INNER JOIN Item ON Category.ItemID=Item.ItemID WHERE ItemCurrentprice>100 AND ItemNumberOfBid>0;
