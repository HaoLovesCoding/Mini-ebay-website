#!/bin/bash
mysql CS144 < drop.sql
mysql CS144 < create.sql
ant
ant run-all
sort -u Item.dat > ItemForLoad.dat
sort -u User.dat > UserForLoad.dat
sort -u Cat.dat > CatForLoad.dat
sort -u Bid.dat  > BidForLoad.dat
mysql CS144 < load.sql
rm *.dat
