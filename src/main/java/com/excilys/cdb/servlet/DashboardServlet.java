package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.service.ComputerService;

public class DashboardServlet extends HttpServlet {
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String VIEW = "/views/dashboard.jsp";
	public static final int DEFAULT_PAGE_SIZE = 10;

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
		ArrayList<Computer> computers = this.computerService.listService();

		if (request.getParameter("size") != null) {
			this.currentSize = Integer.parseInt(request.getParameter("size"));
		} else {
			this.currentSize = DEFAULT_PAGE_SIZE;
		}

		this.page = new Page<Computer>(computers, this.currentSize);
		request.setAttribute("size", this.currentSize);

		if (request.getParameter("page") != null) {
			int currentPageInt = Integer.parseInt(request.getParameter("page"));
			if (currentPageInt <= this.page.getMaxPages() && currentPageInt >= 1) {
				this.currentPage = currentPageInt;
			}
		} else {
			this.currentPage = 1;
		}

		if (this.currentPage > this.page.getMaxPages()) {
			this.currentPage = this.page.getMaxPages();
		}

		request.setAttribute("page", this.currentPage);

		List<ComputerDto> computerDtos = computerDtoMapper.map(this.page.getPageData(this.currentPage - 1));

		request.setAttribute("computers", computerDtos);
		request.setAttribute("nbOfComputers", computers.size());
		request.setAttribute("nbMaxPages", this.page.getMaxPages());
		this.getServletContext().getRequestDispatcher(VIEW).forward(request, response);
	}
}