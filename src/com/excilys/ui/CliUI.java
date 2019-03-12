package com.excilys.ui;

import java.util.ArrayList;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.persistence.*;

public class CliUI {

	public static void main(String[] args) {
		
		DAOFactory daoFactory = DAOFactory.getInstance();	
		CompanyDAO companyDAO = daoFactory.getCompanyDao();
		ComputerDAO computerDAO = daoFactory.getComputerDao();
		
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
