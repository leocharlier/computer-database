package com.excilys.cdb.ui;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.cdb.config.SpringConfiguration;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.mysql.cj.util.StringUtils;

@Component
public class CliUI {
	@Autowired
	private CompanyDao companyDAO;
	@Autowired
	private ComputerDao computerDAO;
	private Scanner keyboard;
	private DateFormat dateFormat;
	
	private enum Actions {	
		EXIT( "0" ),
		LIST_COMPANIES( "1" ),
		LIST_COMPUTERS( "2" ), 
		SHOW_COMPUTER( "3" ), 
		CREATE_COMPUTER( "4" ),
		UPDATE_COMPUTER( "5" ),
		DELETE_COMPUTER( "6" ),
		DELETE_COMPANY( "7" );
		
		private String code;
		
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
					case DELETE_COMPANY :
						deleteCompany();
						break;
					default :
						
				}
			} catch ( NumberFormatException | ArrayIndexOutOfBoundsException e ) {
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
		System.out.println( "7 : Delete a Company" );
	}
	
	public void showPage(Page<?> page) {
		Pagination pageAction = null;
		
		while( pageAction != Pagination.STOP ) {
			
			if( pageAction != Pagination.INVALID ) {
				for(Object object : page.getPageData())
					System.out.println( object.toString() );
				
			}
			
			System.out.print("Press 'n' for next page, 'p' for previous page or 's' to stop : ");

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
					System.out.print( "Invalid pagination input.\n" );
					break;
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
			Optional<Computer> computer = computerDAO.findById(Integer.parseInt(input));
			if(computer.isPresent()) {
				System.out.println( computer.get().toDetailedString());
			} else {
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
		Optional<Timestamp> introduced = Optional.empty();
		if( !input.equals( "0" ) && !StringUtils.isNullOrEmpty( input ) ) {
			introduced = Optional.ofNullable(convertToTimestamp(input));
		} else if ( input.equals( "0" ) ) {
			System.out.println();
			return;
		}
		
		Optional<Timestamp> discontinued = Optional.empty();
		if( introduced.isPresent() ) {
			System.out.print( "Enter the date (Format 'dd/MM/yyyy' only) of discontinuation (press Enter if it's unknown and 0 to exit the creation) : " );
			input = keyboard.nextLine().trim();
			if( !input.equals( "0" )  && !StringUtils.isNullOrEmpty( input ) ) {
				discontinued = Optional.ofNullable(checkDiscontinued(input, introduced.get()));
			} else if ( input.equals( "0" ) ) {
				System.out.println();
				return;
			}
		}
		
		input = companyIDInput();
		Optional<Company> company = Optional.empty();
		if( !input.equals( "0" ) && !StringUtils.isNullOrEmpty( input ) ) {
			company = companyDAO.findById(Integer.parseInt(input));
			if(!company.isPresent()) {
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
		Optional<Computer> computer = computerDAO.findById( Integer.parseInt( input ) );
		
		if( !input.equals( "0" ) ) {
			if( computer.isPresent() ) {
				Computer computerToUpdate = computer.get();
				
				input = askForUpdate("name", computerToUpdate);
				if( input.equals( "y" ) ) {
					System.out.print( "New computer name : " );
					input = keyboard.nextLine().trim();
					
					while( StringUtils.isNullOrEmpty( input ) ) {
						System.out.print( "The name cannot be null. Enter a valid name : " );
						input = keyboard.nextLine().trim();
					}
					
					computerToUpdate.setName( input );
				}
				
				input = askForUpdate("introduced", computerToUpdate);
				if( input.equals( "y" ) ) {
					System.out.print( "New introduction date (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
					input = keyboard.nextLine().trim();
					
					Optional<Timestamp> introduced = Optional.empty();
					if( !StringUtils.isNullOrEmpty( input ) ) {
						introduced = Optional.ofNullable(convertToTimestamp( input ));
					}
					computerToUpdate.setIntroduced( introduced.orElse(null) );
				}
				
				if( computerToUpdate.getIntroduced().isPresent() ) {
					if( computerToUpdate.getDiscontinued().isPresent() && !computerToUpdate.getDiscontinued().get().after( computerToUpdate.getIntroduced().get() ) ) {
						System.out.println( "The date of discontinuation is now before the date of introduction."
								+ "\nNew date of discontinuation (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
					} else {
						input = askForUpdate("discontinued", computerToUpdate);
						if( input.equals( "y" ) ) {
							System.out.print( "New discontinuation date (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
						}
					}
					
					if( !input.equals( "n" ) ) {
						input = keyboard.nextLine().trim();
						
						Optional<Timestamp> discontinued = Optional.empty();
						if( !StringUtils.isNullOrEmpty( input ) ) {
							discontinued = Optional.ofNullable(checkDiscontinued( input, computerToUpdate.getIntroduced().get() ));
						}
						
						computerToUpdate.setDiscontinued( discontinued.orElse(null) );
					}
				} else {
					computerToUpdate.setDiscontinued( null );
				}
				
				input = askForUpdate("company", computerToUpdate);
				while( !input.equals( "y" ) && !input.equals( "n" ) ) {
					System.out.print( "Enter 'y' to update the manufacturer company or 'n' to continue : " );
					input = keyboard.nextLine().trim();
				}
				if( input.equals( "y" ) ) {
					input = companyIDInput();
					Optional<Company> company = companyDAO.findById( Integer.parseInt( input ) );
					if( !input.equals( "0" ) &&  !StringUtils.isNullOrEmpty( input ) && !company.isPresent() ) {
						System.out.println( "This company doesn't exist. The manufacturer will be null." );
					} else if ( input.equals( "0" ) ) {
						System.out.println();
						return;
					}
					
					computerToUpdate.setCompany( company.orElse(null) );
				}
				
				computerDAO.update( computerToUpdate );
			} else {
				System.out.println( "Sorry, this computer doesn't exist.\n" );
				return;
			}
			
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
			Optional<Computer> computer = computerDAO.findById( Integer.parseInt( input ) );
			if( computer.isPresent() ) {
				Computer computerToDelete = computer.get();
				System.out.println( computerToDelete.toDetailedString() );
				System.out.print( "Are you sure you want to delete this computer ? (y\\n) : " );
				input = keyboard.nextLine().trim();
				while( !input.equals( "y" ) && !input.equals( "n" ) ) {
					System.out.print( "Enter 'y' to confirm the deletion or 'n' to come back to the menu : " );
					input = keyboard.nextLine().trim();
				}
				if( input.equals( "y" ) ) {
					computerDAO.delete( computerToDelete );
					
					System.out.println( "\n||||||||||||||||||||||||" );
					System.out.println( "|| Computer deleted ! ||" );
					System.out.println( "||||||||||||||||||||||||\n\n" );
					
				} else {
					System.out.println();
					return;
				}
			} 
		} else {
			System.out.println( "Sorry, this computer doesn't exist.\n" );
		}
				
	}
	
	public void deleteCompany() {
		System.out.print( "Enter the ID of the company (0 to come back to the menu) : " );
		String input = keyboard.nextLine().trim();
		
		while( ( StringUtils.isNullOrEmpty( input ) || !StringUtils.isStrictlyNumeric( input ) ) && !input.equals( "0" ) ) {
			System.out.print( "Invalid company ID. Enter a valid ID (0 to come back to the menu) : " );
			input = keyboard.nextLine().trim();
		}
		
		if( !input.equals( "0" ) ) {
			Optional<Company> company = companyDAO.findById( Integer.parseInt( input ) );
			if( company.isPresent() ) {
				Company companyToDelete = company.get();
				System.out.println( companyToDelete.toString() );
				System.out.print( "Are you sure you want to delete this company ? (y\\n) : " );
				input = keyboard.nextLine().trim();
				while( !input.equals( "y" ) && !input.equals( "n" ) ) {
					System.out.print( "Enter 'y' to confirm the deletion or 'n' to come back to the menu : " );
					input = keyboard.nextLine().trim();
				}
				if( input.equals( "y" ) ) {
					companyDAO.delete( companyToDelete );
					
					System.out.println( "\n||||||||||||||||||||||||" );
					System.out.println( "|| Company deleted ! ||" );
					System.out.println( "||||||||||||||||||||||||\n\n" );
					
				} else {
					System.out.println();
					return;
				}
			} 
		} else {
			System.out.println( "Sorry, this computer doesn't exist.\n" );
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
		
		if(!discontinued.after(introduced)) {
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
	
	public void createSQLComputer(String name, Optional<Timestamp> introduced, Optional<Timestamp> discontinued, Optional<Company> company) {
		Computer computer = new Computer();
		computer.setName( name );
		computer.setIntroduced( introduced.orElse(null) );
		computer.setDiscontinued( discontinued.orElse(null) );
		computer.setCompany( company.orElse(null) );
		
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
				sbUpdate.append( computerToUpdate.getIntroduced().orElse(null) );
				break;
			case "discontinued " :
				sbUpdate.append( computerToUpdate.getDiscontinued().orElse(null) );
				break;
			case "company" :
				sbUpdate.append( computerToUpdate.getCompany().map( someCompany -> someCompany.getName()).orElse("none") );
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
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
		CliUI cliUI = applicationContext.getBean("cliUI", CliUI.class);
		cliUI.start();
		applicationContext.close();
		System.exit(0);
	}	
}