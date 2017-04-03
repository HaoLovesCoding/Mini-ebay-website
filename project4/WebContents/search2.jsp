<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
    <title>Search Results</title>
    <script type="text/javascript">
		// var count = 0;
		// var xmlHttp = new XMLHttpRequest();
		// function sendAjaxRequest(op) {
		// 	if(op.localeCompare("Next")) count++; else count--;
		// 	alert(count);
		// 	var numResultsToSkip = 20 * count;
		// 	var numResultsToReturn = 20;
		// 	var keyword = document.getElementById("keyword");
		// 	// var func = (op == "Next" ? next() : previous());
		// 	var request = "/eBay/search?q=" + keyword + "&numResultsToSkip=" + numResultsToSkip + "&numResultsToReturn=" + numResultsToReturn;
		// 	xmlHttp.open("GET", request);
		// 	// xmlHttp.onreadystatechange = next();
		// 	xmlHttp.send(null);
		// }
		// function next() {
		// 	if(xmlHttp.responseState == 4) {
		// 		response = xmlHttp.responseText;
		// 		alert(response);
		// 	}
		// }
	</script>
	<style type="text/css">
	.buttons {
		float: right;
	}
	</style>
</head>
</head>
<body>
	<form action="/eBay/search" method="get">
	 	keywords:
		<input type="text" name="q"><br>
		<input type="hidden" name="numResultsToSkip" value="0"><br>
		<input type="hidden" name="numResultsToReturn" value="20"><br>
		<input type="submit" value="Submit">
	</form>
    <p>The results are as follows of keyword <span id="keyword"><%= (String)request.getAttribute("keyword") %></span>:</p>
    <% 	SearchResult[] res = (SearchResult[])request.getAttribute("result");
    	String keyword = (String) request.getAttribute("keyword");
    	int numResultsToSkip = Integer.parseInt(request.getAttribute("numResultsToSkip").toString());
    	// int numResultsToReturn = Integer.parseInt(request.getAttribute("numResultsToReturn"));
    	if(numResultsToSkip > 0) { %>
    	<a href="/eBay/search?q= <%= keyword %>&numResultsToSkip=<%= numResultsToSkip - 20 %>&numResultsToReturn=20"> Previous</a>
   	 <% }
   	 	if(res.length == 20) { %>
   	 		<a href="/eBay/search?q=<%= keyword %>&numResultsToSkip=<%= numResultsToSkip + 20 %>&numResultsToReturn=20"> Next</a>
   	 <%	}
    	if(res.length > 0) {
    		for (SearchResult sr: res) { %>
    			<p><a href=/eBay/item?ID="<%= sr.getItemId() %>"><%= sr.getItemId() %> </a>: <%= sr.getName() %></p>
     <%		}
		} %>
    
</body>
</html>
