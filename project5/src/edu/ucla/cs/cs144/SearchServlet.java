package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	response.setContentType("text/html");
    	String keyword = request.getParameter("q");
    	int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
    	int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
    	SearchResult[] res = AuctionSearchClient.basicSearch(keyword, numResultsToSkip, numResultsToReturn);
        request.setAttribute("keyword", request.getParameter("q"));
        request.setAttribute("numResultsToSkip", numResultsToSkip);
        request.setAttribute("numResultsToReturn", numResultsToReturn);
    	request.setAttribute("result", res);
        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
}
