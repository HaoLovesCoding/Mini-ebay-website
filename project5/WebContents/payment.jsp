<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
	<title>Pay</title>
</head>
<body>
	<p><b>Credit Card Input Page</b></p>
	<p>ItemID: <%=(String)request.getAttribute("ItemID")%> </p>
	<p>ItemName: <%= (String)request.getAttribute("ItemName") %> </p>
	<p>Buy Price: <%= (String)request.getAttribute("BuyPrice") %> </p>
	<% String path = ":8443/eBay/orderConfirm"; %>
	<form action="https://<%= request.getServerName() + path %>" method = "post">
		Credit Card:
		<input type = "text" name="cardNum"> 
		<input type="submit" value="Submit">
	</form>
</body>
</html>
