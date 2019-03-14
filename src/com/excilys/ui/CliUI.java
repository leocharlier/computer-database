package com.excilys.ui;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.persistence.*;
import com.mysql.cj.util.StringUtils;

public class CliUI {
	
	private DAOFactory daoFactory;
	private CompanyDAO companyDAO;
	private ComputerDAO computerDAO;
	private Scanner keyboard;
	private DateFormat dateFormat;
	
	private enum Actions {	
		EXIT( "0" ),
		LIST_COMPANIES( "1" ),
		LIST_COMPUTERS( "2" ), 
		SHOW_COMPUTER( "3" ), 
		CREATE_COMPUTER( "4" ),
		UPDATE_COMPUTER( "5" ),
		DELETE_COMPUTER( "6" );
		
		private final String code;
		
		private Actions( String pCode) {
			this.code = pCode;
		}
		
		public static Actions codeToAction(String pCode) {
			return Actions.values()[Integer.parseInt( pCode )];
		}
	}
	
	private enum Pagination {
		NEXT( "n" ),
		PREVIOUS( "p" ),
		STOP( "s" ),
		INVALID;
		
		private String code;
		
		private Pagination() {}
		
		private Pagination( String pCode) {
			this.code = pCode;
		}
		
		public static Pagination codeToAction(String pCode) {
			int index = 3;
			switch( pCode ) {
				case "n" :
					index = 0;
					break;
				case "p" :
					index = 1;
					break;
				case "s" :
					index = 2;
					break;
			}
			return Pagination.values()[index];
		}
	}
	
	public CliUI() {
		this.daoFactory = DAOFactory.getInstance();
		this.companyDAO = daoFactory.getCompanyDao();
		this.computerDAO = daoFactory.getComputerDao();
		this.keyboard = new Scanner(System.in);
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public void start() {
		Actions action = null;
		
		System.out.println( "|||||||||||||||||||||||||||||||||||||||||||||||||||||||" );
		System.out.println( "||                                                   ||" );
		System.out.println( "||     Welcome in the Computer Database Manager !    ||" );
		System.out.println( "||                                                   ||" );
		System.out.println( "|||||||||||||||||||||||||||||||||||||||||||||||||||||||\n" );
		
		while( action != Actions.EXIT ) {
			menu();
	
			try {
				action = Actions.codeToAction( keyboard.nextLine().trim() );
				System.out.println();
				
				switch(action) {
					case EXIT :
						System.out.println( "|||||||||||||||||||||||||||||||||||||||||" );
						System.out.println( "||                                     ||" );
						System.out.println( "||     Goodbye, see you next time !    ||" );
						System.out.println( "||                                     ||" );
						System.out.println( "|||||||||||||||||||||||||||||||||||||||||\n" );
						keyboard.close();
						break;
					case LIST_COMPANIES :
						listCompanies();
						break;
					case LIST_COMPUTERS :
						listComputers();
						break;
					case SHOW_COMPUTER :
						showComputer();
						break;
					case CREATE_COMPUTER :
						createComputer();
						break;
					case UPDATE_COMPUTER :
						updateComputer();
						break;
					case DELETE_COMPUTER :
						deleteComputer();
						break;
				}
			} catch (NumberFormatException e) {
				System.out.println( "Invalid input. Enter 1, 2, ..., 6 to perform an action or 0 to exit.\n" );
			}
		}	
	}
	
	public void menu() {
		System.out.println( "What do you want to do ?" );
		System.out.println( "0 : Exit" );
		System.out.println( "1 : List Companies" );
		System.out.println( "2 : List Computers" );
		System.out.println( "3 : Show Computer Details" );
		System.out.println( "4 : Create a Computer" );
		System.out.println( "5 : Update a Computer" );
		System.out.println( "6 : Delete a Computer" );
	}
	
	public void showPage(Page<?> page) {
		Pagination pageAction = null;
		
		while( pageAction != Pagination.STOP ) {
			
			for(Object object : page.getPageData())
				System.out.println( object.toString() );
			
			System.out.print("Press 'n' for next page, 'p' for previous page or 's' to stop : ");

			try {
				pageAction = Pagination.codeToAction( keyboard.nextLine().trim() );
				System.out.println();

				switch(pageAction) {
					case NEXT :
						page.next();
						break;
					case PREVIOUS :
						page.previous();
						break;
					case STOP :
						break;
					case INVALID :
						System.out.println( "Invalid pagination input ('n' for Next Page, 'p' for Previous Page and 's' to Stop.\n" );
						break;
				}
			} catch (NumberFormatException e) {
				System.out.println( "Invalid pagination input ('n' for Next Page, 'p' for Previous Page and 's' to Stop.\n" );
			}
		}
		
		
	}
	
	public void listCompanies() {
		ArrayList<Company> companies = companyDAO.list();
		Page<Company> pageCompany = new Page<Company>(companies);
		showPage(pageCompany);
	}
	
	public void listComputers() {
		ArrayList<Computer> computers = computerDAO.list();
		Page<Computer> pageComputer = new Page<Computer>(computers);
		showPage(pageComputer);
	}
	
	public void showComputer() {
		String input = computerIDInput();
		
		if( !input.equals( "0" ) ) {
			try {
				Computer computer = computerDAO.find( Integer.parseInt( input ) );
				System.out.println( computer.toDetailedString() );
			} catch ( DAOException e) {
				System.out.println( "Sorry, this computer doesn't exist.\n" );
			}
		}
	} 
	
	public void createComputer() {
		System.out.println( "|||||||||||||||||||||||" );
		System.out.println( "|| Computer creation ||" );
		System.out.println( "|||||||||||||||||||||||\n\n" );

		String input = nameInput();
		String name = null;
		if( !input.equals( "0" ) ) {
			name = input;
		} else {
			System.out.println();
			return;
		}
		
		System.out.print( "Enter the date (Format 'dd/MM/yyyy' only) of introduction (press Enter if it's unknown and 0 to exit the creation) : " );
		input = keyboard.nextLine().trim();
		Timestamp introduced = null;
		if( !input.equals( "0" ) && !StringUtils.isNullOrEmpty( input ) ) {
			introduced = convertToTimestamp(input);
		} else if ( input.equals( "0" ) ) {
			System.out.println();
			return;
		}
		
		Timestamp discontinued = null;
		if( introduced != null) {
			System.out.print( "Enter the date (Format 'dd/MM/yyyy' only) of discontinuation (press Enter if it's unknown and 0 to exit the creation) : " );
			input = keyboard.nextLine().trim();
			if( !input.equals( "0" )  && !StringUtils.isNullOrEmpty( input ) ) {
				discontinued = checkDiscontinued(input, introduced);
			} else if ( input.equals( "0" ) ) {
				System.out.println();
				return;
			}
		}
		
		input = companyIDInput();
		Company company = null;
		if( !input.equals( "0" ) && !StringUtils.isNullOrEmpty( input ) ) {
			try {
				company = companyDAO.find( Integer.parseInt( input ) );
			} catch (DAOException e){
				System.out.println( "This company doesn't exist. The manufacturer will be null." );
			}
		} else if ( input.equals( "0" ) ){
			System.out.println();
			return;
		}
		
		createSQLComputer(name, introduced, discontinued, company);
		
		System.out.println( "\n||||||||||||||||||||||||" );
		System.out.println( "|| Computer created ! ||" );
		System.out.println( "||||||||||||||||||||||||\n\n" );
	}
	
	public void updateComputer() {
		String input = computerIDInput();
		
		Computer computer;
		if( !input.equals( "0" ) ) {
			try {
				computer = computerDAO.find( Integer.parseInt( input ) );
			} catch ( DAOException e) {
				System.out.println( "Sorry, this computer doesn't exist.\n" );
				return;
			}
			
			input = askForUpdate("name", computer);
			if( input.equals( "y" ) ) {
				System.out.print( "New computer name : " );
				input = keyboard.nextLine().trim();
				
				while( StringUtils.isNullOrEmpty( input ) ) {
					System.out.print( "The name cannot be null. Enter a valid name : " );
					input = keyboard.nextLine().trim();
				}
				
				computer.setName( input );
			}
			
			input = askForUpdate("introduced", computer);
			if( input.equals( "y" ) ) {
				System.out.print( "New introduction date (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
				input = keyboard.nextLine().trim();
				
				Timestamp introduced = null;
				if( !StringUtils.isNullOrEmpty( input ) ) {
					introduced = convertToTimestamp( input );
				} else {
					computer.setDiscontinued( null );
				}
				computer.setIntroduced( introduced );
			}
			
			if( computer.getIntroduced() != null ) {
				if( computer.getDiscontinued() != null && !computer.getDiscontinued().after( computer.getIntroduced() ) ) {
					System.out.println( "The date of discontinuation is now before the date of introduction."
							+ "\nNew date of discontinuation (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
				} else {
					input = askForUpdate("discontinued", computer);
					if( input.equals( "y" ) ) {
						System.out.print( "New discontinuation date (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
					}
				}
				
				if( !input.equals( "n" ) ) {
					input = keyboard.nextLine().trim();
					
					Timestamp discontinued = null;
					if( !StringUtils.isNullOrEmpty( input ) ) {
						discontinued = checkDiscontinued( input, computer.getIntroduced() );
					}
					
					computer.setDiscontinued( discontinued );
				}
			}
			
			input = askForUpdate("company", computer);
			while( !input.equals( "y" ) && !input.equals( "n" ) ) {
				System.out.print( "Enter 'y' to update the manufacturer company or 'n' to continue : " );
				input = keyboard.nextLine().trim();
			}
			if( input.equals( "y" ) ) {
				input = companyIDInput();
				Company company = null;
				if( !input.equals( "0" ) &&  !StringUtils.isNullOrEmpty( input ) ) {
					try {
						company = companyDAO.find( Integer.parseInt( input ) );
					} catch (DAOException e){
						System.out.println( "This company doesn't exist. The new manufacturer will be null." );
					}
				} else if ( input.equals( "0" ) ) {
					System.out.println();
					return;
				}
				
				computer.setCompany( company );
			}
			
			computerDAO.update( computer );
			
			System.out.println( "\n||||||||||||||||||||||||" );
			System.out.println( "|| Computer updated ! ||" );
			System.out.println( "||||||||||||||||||||||||\n\n" );
			
		}
	}
	
	public void deleteComputer() {
		System.out.print( "Enter the ID of the computer (0 to come back to the menu) : " );
		String input = keyboard.nextLine().trim();
		
		while( ( StringUtils.isNullOrEmpty( input ) || !StringUtils.isStrictlyNumeric( input ) ) && !input.equals( "0" ) ) {
			System.out.print( "Invalid computer ID. Enter a valid ID (0 to come back to the menu) : " );
			input = keyboard.nextLine().trim();
		}
		
		if( !input.equals( "0" ) ) {
			try {
				Computer computer = computerDAO.find( Integer.parseInt( input ) );
				System.out.println( computer.toDetailedString() );
				System.out.print( "Are you sure you want to delete this computer ? (y\\n) : " );
				input = keyboard.nextLine().trim();
				while( !input.equals( "y" ) && !input.equals( "n" ) ) {
					System.out.print( "Enter 'y' to confirm the deletion or 'n' to come back to the menu : " );
					input = keyboard.nextLine().trim();
				}
				if( input.equals( "y" ) ) {
					computerDAO.delete( computer );
					
					System.out.println( "\n||||||||||||||||||||||||" );
					System.out.println( "|| Computer deleted ! ||" );
					System.out.println( "||||||||||||||||||||||||\n\n" );
					
				} else {
					System.out.println();
					return;
				}
			} catch ( DAOException e) {
				System.out.println( "Sorry, this computer doesn't exist.\n" );
			}
		}
	}
	
	public String nameInput() {
		System.out.print( "Enter the name of the computer (0 to exit the creation) : " );
		String input = keyboard.nextLine().trim();
		
		while( StringUtils.isNullOrEmpty( input ) && !input.equals( "0" ) ) {
			System.out.print( "The name cannot be null. Enter a valid name (0 to to exit the creation) : " );
			input = keyboard.nextLine().trim();
		}
		
		return input;
	}
	
	public String computerIDInput() {
		System.out.print( "Enter the ID of the computer (0 to come back to the menu) : " );
		String input = keyboard.nextLine().trim();
		
		while( ( StringUtils.isNullOrEmpty( input ) || !StringUtils.isStrictlyNumeric( input ) ) && !input.equals( "0" ) ) {
			System.out.print( "Invalid computer ID. Enter a valid ID (0 to come back to the menu) : " );
			input = keyboard.nextLine().trim();
		}
		
		return input;
	}
	
	public Timestamp convertToTimestamp(String dateInput) {
		Timestamp sqlDate = null;
		
		try {
			Date date = dateFormat.parse( dateInput );
			long time = date.getTime();
			sqlDate = new Timestamp(time);
			return sqlDate;
		} catch( ParseException e) {
			System.out.println( "Wrong date format. The date will be null." );
			return sqlDate;
		}	
	}
	
	public Timestamp checkDiscontinued(String input, Timestamp introduced) {
		Timestamp discontinued = convertToTimestamp(input);
		
		if(discontinued.after(introduced)) {
			System.out.println( "The date of discontinuation must be after the date of introduction. It will be null." );
			discontinued = null;
		}
		
		return discontinued;
	}
	
	public String companyIDInput() {
		System.out.print( "Enter the ID of the manufacturer company (press Enter if it's unknown and 0 to exit the creation) : " );
		String input = keyboard.nextLine().trim();
		
		while( !StringUtils.isStrictlyNumeric( input ) && !StringUtils.isNullOrEmpty( input ) && !input.equals( "0" ) ) {
			System.out.print( "Invalid company ID. Enter a valid ID (0 to come back to the menu) : " );
			input = keyboard.nextLine().trim();
		}
		
		return input;
	}
	
	public void createSQLComputer(String name, Timestamp introduced, Timestamp discontinued, Company company) {
		Computer computer = new Computer();
		computer.setName( name );
		computer.setIntroduced( introduced );
		computer.setDiscontinued( discontinued );
		computer.setCompany( company );
		
		computerDAO.create( computer );
	}
	
	public String askForUpdate(String fieldToUpdate, Computer computerToUpdate) {
		StringBuilder sbUpdate = new StringBuilder( "Actual computer \"");
		sbUpdate.append(fieldToUpdate);
		sbUpdate.append("\" field : ");
		switch( fieldToUpdate ) {
			case "name" :
				sbUpdate.append( computerToUpdate.getName() );
				break;
			case "introduced" :
				sbUpdate.append( computerToUpdate.getIntroduced() );
				break;
			case "discontinued " :
				sbUpdate.append( computerToUpdate.getDiscontinued() );
				break;
			case "company" :
				try {
					sbUpdate.append( computerToUpdate.getCompany().getName() );
				} catch ( NullPointerException e) {
					sbUpdate.append( "none" );
				}
				break;
		}
		
		sbUpdate.append( ". Do you want to update it ? (y/n)");
		System.out.print( sbUpdate );
		
		String input = keyboard.nextLine().trim();
		while( !input.equals( "y" ) && !input.equals( "n" ) ) {
			System.out.print( "Enter 'y' to update the computer \"" + fieldToUpdate + "\" field or 'n' to continue : " );
			input = keyboard.nextLine().trim();
		}
		
		return input;
	}

	public static void main(String[] args) throws ParseException {
		CliUI cliUI = new CliUI();
		cliUI.start();
		System.exit(0);
	}	
}
