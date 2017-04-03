package edu.ucla.cs.cs144;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	response.setContentType("text/xml");
    	String keyword = request.getParameter("q");
    	String url = "http://google.com/complete/search?output=toolbar&q=" + keyword;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
//		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer googleResponse = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			googleResponse.append(inputLine);
		}
		in.close();
		PrintWriter out = response.getWriter();
		out.println(googleResponse.toString());
		out.close();
//		request.setAttribute("response", googleResponse.toString());
//		request.getRequestDispatcher("/proxyResult.jsp").forward(request, response);
		//print result
//		System.out.println(response.toString());
    }
}
