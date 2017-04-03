<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
	<title>Confirmation Page</title>
</head>
<body>
	<p><b>Confirmation Page</b></p>
	<p>ItemID: <%=(String)request.getAttribute("ItemID")%> </p>
	<p>ItemName: <%= (String)request.getAttribute("ItemName") %> </p>
	<p>Buy Price: <%= (String)request.getAttribute("BuyPrice") %> </p>
	<p>Credit Card: <%=(String)request.getAttribute("CardNumber")%></p>
</body>
</html>
