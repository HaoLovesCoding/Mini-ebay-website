<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
    <title>Item Search Results</title>
</head>
<body>
<form action="/eBay/item" method="get">
	 	ItemID:
		<input type="text" name="id"><br>
		<input type="submit" value="Submit">
	</form>
    <p>The results are as follows:</p>
    <%= (String)request.getAttribute("response") %>
</body>
</html>