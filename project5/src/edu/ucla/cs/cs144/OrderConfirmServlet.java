package edu.ucla.cs.cs144;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OrderConfirmServlet extends HttpServlet implements Servlet {
	 public OrderConfirmServlet() {}

	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	        // your codes here
	    	response.setContentType("text/html");
//	    	request.setAttribute("result", itemXml);
	    	String cardNumber = request.getParameter("cardNum");
	        HttpSession session = request.getSession(true);
	        request.setAttribute("ItemID", session.getAttribute("ItemID"));
	        request.setAttribute("BuyPrice",session.getAttribute("BuyPrice"));
	        request.setAttribute("ItemName",session.getAttribute("ItemName"));
	        request.setAttribute("CardNumber",cardNumber);
	        request.setAttribute("test", "test");
	        request.getRequestDispatcher("/confirm.jsp").forward(request, response);
	    }
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	doGet(request,response);
	    }
}
