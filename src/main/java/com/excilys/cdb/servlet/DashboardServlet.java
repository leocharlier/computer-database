package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.service.ComputerService;

public class DashboardServlet extends HttpServlet {
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String VIEW             = "/views/dashboard.jsp";
	public static final String EXCEPTION_VIEW     = "/views/500.jsp";
	public static final String NOT_FOUND_VIEW   = "/views/404.jsp";
	public static final int DEFAULT_PAGE_SIZE   = 10;

	private ComputerService computerService;
	private ComputerDtoMapper computerDtoMapper;
	private Page<Computer> page;
	private int currentPage;
	private int currentSize;

	public void init() throws ServletException {
		this.computerService = new ComputerService(((DaoFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)));
		this.computerDtoMapper = new ComputerDtoMapper();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Computer> computers;
		if(request.getParameterMap().containsKey("search") && !request.getParameter("search").equals("")) {
			computers = this.computerService.searchService(request.getParameter("search").trim());
			request.setAttribute("search", request.getParameter("search"));
		} else {
			computers = this.computerService.listService();
		}
		
		if(computers.isEmpty()) {
			request.setAttribute("nbOfComputers", 0);
			request.setAttribute("noComputersFound", true);
		} else {
			if (request.getParameterMap().containsKey("size")) {
				this.currentSize = Integer.parseInt(request.getParameter("size"));
			} else {
				this.currentSize = DEFAULT_PAGE_SIZE;
			}

			this.page = new Page<Computer>(computers, this.currentSize);
			request.setAttribute("size", this.currentSize);

			if (request.getParameterMap().containsKey("page") && Integer.parseInt(request.getParameter("page")) >= 1) {
				int currentPageInt = Integer.parseInt(request.getParameter("page"));
				if(currentPageInt <= this.page.getMaxPages()) {
					this.currentPage = currentPageInt;
				} else {
					this.currentPage = this.page.getMaxPages();
				}
			} else {
				this.currentPage = 1;
			}

			request.setAttribute("page", this.currentPage);

			List<ComputerDto> computerDtos = computerDtoMapper.map(this.page.getPageData(this.currentPage - 1));

			request.setAttribute("computers", computerDtos);
			request.setAttribute("nbOfComputers", computers.size());
			request.setAttribute("nbMaxPages", this.page.getMaxPages());
		}

		this.getServletContext().getRequestDispatcher(VIEW).forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] computersIdToDelete = request.getParameter("selection").split("\\,");
		boolean allGood = true;
		
		for(String computerId : computersIdToDelete) {
			Optional<Computer> computerToDelete = computerService.findService(Integer.parseInt(computerId));
			if(computerToDelete.isPresent()) {
				try {
					computerService.deleteService(computerToDelete.get());
				} catch(DaoException e) {
					allGood = false;
					request.setAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the deletion...");
					this.getServletContext().getRequestDispatcher(EXCEPTION_VIEW).forward(request, response);
					break;
				}
			} else {
				allGood = false;
				request.setAttribute("errorMessage", "Sorry, the computer <strong>" + computerId + "</strong> doesn't exist.");
				this.getServletContext().getRequestDispatcher(NOT_FOUND_VIEW).forward(request, response);
				break;
			}
		}
		
		if(allGood) {
			response.sendRedirect("dashboard?page=" + request.getParameter("page") + "&size=" + request.getParameter("size"));
		}
	}
}