package com.excilys.cdb.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Error404Servlet  extends HttpServlet {
	public static final String VIEW     = "/views/404.jsp";

	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
	  //request.setAttribute("errorMessage", this.companies);
	  this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
	}
}
