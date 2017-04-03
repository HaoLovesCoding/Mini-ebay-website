CREATE TABLE Locations(ItemID INT NOT NULL, Coordinate GEOMETRY NOT NULL, PRIMARY KEY (ItemID)) ENGINE=MyISAM;
INSERT INTO Locations (ItemID,Coordinate) SELECT ItemID,POINT(LocationLatitude,LocationLongitude) FROM Item WHERE LocationLatitude IS NOT NULL AND LocationLongitude IS NOT NULL;
CREATE SPATIAL INDEX ItemSpIndex ON Locations (Coordinate);
