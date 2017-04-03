<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<style type="text/css"> 
  html { height: 100% } 
  body { height: 100%; margin: 0px; padding: 0px } 
  #map_canvas { height: 100% } 
</style> 
<script type="text/javascript" 
    src="http://maps.google.com/maps/api/js?sensor=false"> 
</script>
<% String LTest=(String)request.getAttribute("LocationString");%> 
<script type="text/javascript"> 
  function initialize() {
    var latlng = new google.maps.LatLng(<%=LTest%>);
    var myOptions = { 
      zoom: 14, // default is 8  
      center: latlng, 
      mapTypeId: google.maps.MapTypeId.ROADMAP 
    }; 
    var map = new google.maps.Map(document.getElementById("map_canvas"), 
        myOptions); 
    var marker = new google.maps.Marker({
    position: latlng,
    map: map,
  }); 
 } 

</script> 
</head>
<body>
    <p>Search Results:</p>
	<% String XMLtest=(String)request.getAttribute("XMLString");%>
	<%=XMLtest %>
    <body onload="initialize()"> 
    <div id="map_canvas" style="width:60%; height:60%"></div> 
    <form action="/eBay/item" method="get">
	 	Not Satisfied? Searcg abirger ItemID:
		<input type="text" name="ID"><br>
		<input type="submit" value="Submit">
    </form>
</body> 
</body>
</html>
