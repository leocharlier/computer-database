package com.excilys.ui;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.persistence.*;

public class CliUI {

	public static void main(String[] args) {
		
		DAOFactory daoFactory = DAOFactory.getInstance();	
		CompanyDAO companyDAO = daoFactory.getCompanyDao();
		ComputerDAO computerDAO = daoFactory.getComputerDao();
		
		Computer computer = new Computer();
		computer.setName("Test");
		
//		try {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//			Date parsedDate = dateFormat.parse("2018-05-21 21:05:21.000");
//			Timestamp introduced = new Timestamp(parsedDate.getTime());
//		} catch (Exception e) {
//		
//		}
		
		computer.setIntroduced(null);
		computer.setDiscontinued(null);
		computer.setCompany(companyDAO.find(2));
		
		computerDAO.create(computer);
		
		System.out.println("Computer added : " + computer.getId());
		
		computerDAO.delete(computer);
		
//		ArrayList<Company> companies = companyDAO.list();
//		ArrayList<Computer> computers = computerDAO.list();
		
//		for(Company company : companies) {
//			System.out.println( "Company " + company.getId() + " : " + company.getName() );
//		}
		
//		for(Computer computer : computers) {
//			System.out.println( "Computer " + computer.getId() + " : " + computer.getName() );
//		}
		
//		Computer computer = computerDAO.find(5546);
//		System.out.println( "Computer " + computer.getId() + " : " + computer.getName() );
//		System.out.println( "\t| Introduced at : " + computer.getIntroduced() );
//		System.out.println( "\t| Discontinued at : " + computer.getDiscontinued() );
//		System.out.println( "\t| Manufacturer : " + computer.getCompany().getName() );
	}

}
