package com.excilys.ui;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.persistence.*;
import com.mysql.cj.util.StringUtils;

public class CliUI {
	
	private DAOFactory daoFactory;
	private CompanyDAO companyDAO;
	private ComputerDAO computerDAO;
	private Scanner keyboard;
	private DateFormat dateFormat;
	
	public CliUI() {
		daoFactory = DAOFactory.getInstance();
		companyDAO = daoFactory.getCompanyDao();
		computerDAO = daoFactory.getComputerDao();
		keyboard = new Scanner(System.in);
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public void start() {
		String action = "-1";
		System.out.println( "|||||||||||||||||||||||||||||||||||||||||||||||||||||||" );
		System.out.println( "||                                                   ||" );
		System.out.println( "||     Welcome in the Computer Database Manager !    ||" );
		System.out.println( "||                                                   ||" );
		System.out.println( "|||||||||||||||||||||||||||||||||||||||||||||||||||||||\n" );
		
		while( !action.equals( "0" ) ) {
			System.out.println( "What do you want to do ?" );
			System.out.println( "0 : Exit" );
			System.out.println( "1 : List Companies" );
			System.out.println( "2 : List Computers" );
			System.out.println( "3 : Show Computer Details" );
			System.out.println( "4 : Create a Computer" );
			System.out.println( "5 : Update a Computer" );
			System.out.println( "6 : Delete a Computer" );
			
			action = keyboard.nextLine().trim();
			System.out.println();
			
			switch(action) {
				case "0" :
					System.out.println( "|||||||||||||||||||||||||||||||||||||||||" );
					System.out.println( "||                                     ||" );
					System.out.println( "||     Goodbye, see you next time !    ||" );
					System.out.println( "||                                     ||" );
					System.out.println( "|||||||||||||||||||||||||||||||||||||||||\n" );
					keyboard.close();
					break;
				case "1" :
					listCompanies();
					break;
				case "2" :
					listComputers();
					break;
				case "3" :
					showComputer();
					break;
				case "4" :
					createComputer();
					break;
				case "5" :
					updateComputer();
					break;
				case "6" :
					deleteComputer();
					break;
				default :
					System.out.println( "Invalid input. Enter 1, 2, ..., 6 to perform an action or 0 to exit.\n" );
					break;
			}
		}	
	}
	
	public void listCompanies() {
		StringBuilder sbCompanies = new StringBuilder( "List of companies : \n" );
		for( Company companyEntry : companyDAO.list() ) {
			sbCompanies.append( companyEntry.toString() );
			sbCompanies.append( "\n" );
		}
		System.out.println( sbCompanies );
	}
	
	public void listComputers() {
		StringBuilder sbComputers = new StringBuilder( "List of computers : \n" );
		for( Computer computerEntry : computerDAO.list() ) {
			sbComputers.append( computerEntry.toString() );
			sbComputers.append( "\n" );
		}
		System.out.println( sbComputers );
	}
	
	public void showComputer() {
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
			} catch ( DAOException e) {
				System.out.println( "Sorry, this computer doesn't exist.\n" );
			}
			
		}
	}
	
	public void createComputer() {
		System.out.println( "|||||||||||||||||||||||" );
		System.out.println( "|| Computer creation ||" );
		System.out.println( "|||||||||||||||||||||||\n\n" );
		
		System.out.print( "Enter the name of the computer (0 to exit the creation) : " );
		String input = keyboard.nextLine().trim();
		
		while( StringUtils.isNullOrEmpty( input ) && !input.equals( "0" ) ) {
			System.out.print( "The name cannot be null. Enter a valid name (0 to to exit the creation) : " );
			input = keyboard.nextLine().trim();
		}
		
		String name;
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
			try {
				Date date = dateFormat.parse(input);
				long time = date.getTime();
				introduced = new Timestamp(time);
			} catch (Exception e) {
				System.out.println( "Wrong date format. The date of introduction will be null." );
			}
		} else if ( input.equals( "0" ) ) {
			System.out.println();
			return;
		}
		
		Timestamp discontinued = null;
		
		if( introduced != null) {
			System.out.print( "Enter the date (Format 'dd/MM/yyyy' only) of discontinuation (press Enter if it's unknown and 0 to exit the creation) : " );
			input = keyboard.nextLine().trim();
			
			if( !input.equals( "0" )  && !StringUtils.isNullOrEmpty( input ) ) {
				try {
					Date date = dateFormat.parse(input);
					long time = date.getTime();
					if( date.after( introduced ) ) {
						discontinued = new Timestamp(time);
					} else {
						System.out.println( "The date of discontinuation must be after the date of introduction. It will be null." );
					}
				} catch (Exception e) {
					System.out.println( "Wrong date format. The date of discontinuation will be null." );
				}
			} else if ( input.equals( "0" ) ) {
				System.out.println();
				return;
			}
		}
		
		System.out.print( "Enter the ID of the manufacturer company (press Enter if it's unknown and 0 to exit the creation) : " );
		input = keyboard.nextLine().trim();
		
		while( !StringUtils.isStrictlyNumeric( input ) && !StringUtils.isNullOrEmpty( input ) && !input.equals( "0" ) ) {
			System.out.print( "Invalid company ID. Enter a valid ID (0 to come back to the menu) : " );
			input = keyboard.nextLine().trim();
		}
		
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
		
		Computer computer = new Computer();
		computer.setName( name );
		computer.setIntroduced( introduced );
		computer.setDiscontinued( discontinued );
		computer.setCompany( company );
		
		computerDAO.create( computer );
		
		System.out.println( "\n||||||||||||||||||||||||" );
		System.out.println( "|| Computer created ! ||" );
		System.out.println( "||||||||||||||||||||||||\n\n" );
	}
	
	public void updateComputer() {
		System.out.print( "Enter the ID of the computer (0 to come back to the menu) : " );
		String input = keyboard.nextLine().trim();
		
		while( ( StringUtils.isNullOrEmpty( input ) || !StringUtils.isStrictlyNumeric( input ) ) && !input.equals( "0" ) ) {
			System.out.print( "Invalid computer ID. Enter a valid ID (0 to come back to the menu) : " );
			input = keyboard.nextLine().trim();
		}
		
		Computer computer;
		if( !input.equals( "0" ) ) {
			try {
				computer = computerDAO.find( Integer.parseInt( input ) );
			} catch ( DAOException e) {
				System.out.println( "Sorry, this computer doesn't exist.\n" );
				return;
			}
			
			StringBuilder sbUpdate = new StringBuilder( "Actual computer name : " );
			sbUpdate.append( computer.getName() );
			sbUpdate.append( ". Do you want to update it ? (y/n)");
			System.out.print( sbUpdate );
			
			input = keyboard.nextLine().trim();
			while( !input.equals( "y" ) && !input.equals( "n" ) ) {
				System.out.print( "Enter 'y' to update the name or 'n' to continue : " );
				input = keyboard.nextLine().trim();
			}
			if( input.equals( "y" ) ) {
				System.out.print( "New computer name : " );
				input = keyboard.nextLine().trim();
				
				while( StringUtils.isNullOrEmpty( input ) ) {
					System.out.print( "The name cannot be null. Enter a valid name : " );
					input = keyboard.nextLine().trim();
				}
				
				computer.setName( input );
			}
			
			sbUpdate = new StringBuilder( "Actual introduction date : " );
			sbUpdate.append( computer.getIntroduced() );
			sbUpdate.append( ". Do you want to update it ? (y/n)");
			System.out.print( sbUpdate );
			
			input = keyboard.nextLine().trim();
			while( !input.equals( "y" ) && !input.equals( "n" ) ) {
				System.out.print( "Enter 'y' to update the introduction or 'n' to continue : " );
				input = keyboard.nextLine().trim();
			}
			if( input.equals( "y" ) ) {
				System.out.print( "New introduction date (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
				input = keyboard.nextLine().trim();
				
				Timestamp introduced = null;
				if( !StringUtils.isNullOrEmpty( input ) ) {
					try {
						Date date = dateFormat.parse( input );
						long time = date.getTime();
						introduced = new Timestamp( time );
					} catch (Exception e) {
						System.out.println( "Wrong date format. The date of introduction will be null." );
					}
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
					sbUpdate = new StringBuilder( "Actual discontinuation date : " );
					sbUpdate.append( computer.getDiscontinued() );
					sbUpdate.append( ". Do you want to update it ? (y/n)");
					System.out.print( sbUpdate );
					
					input = keyboard.nextLine().trim();
					while( !input.equals( "y" ) && !input.equals( "n" ) ) {
						System.out.print( "Enter 'y' to update the discontinuation date or 'n' to continue : " );
						input = keyboard.nextLine().trim();
					}
					
					if( input.equals( "y" ) ) {
						System.out.print( "New discontinuation date (Format 'dd/MM/yyyy' or press Enter to make it null) : " );
					}
				}
				
				if( !input.equals( "n" ) ) {
					input = keyboard.nextLine().trim();
					
					Timestamp discontinued = null;
					if( !StringUtils.isNullOrEmpty( input ) ) {
						try {
							Date date = dateFormat.parse(input);
							long time = date.getTime();
							if( date.after( computer.getIntroduced() ) ) {
								discontinued = new Timestamp(time);
							} else {
								System.out.println( "The new date of discontinuation must be after the date of introduction. It will be null." );
							}
						} catch (Exception e) {
							System.out.println( "Wrong date format. The new date of discontinuation will be null." );
						}
					}
					
					computer.setDiscontinued( discontinued );
				}
			}
			
			sbUpdate = new StringBuilder( "Actual manufacturer company : " );
			try {
				sbUpdate.append( computer.getCompany().getName() );
			} catch ( NullPointerException e) {
				sbUpdate.append( "none" );
			}
			sbUpdate.append( ". Do you want to update it ? (y/n)");
			System.out.print( sbUpdate );
			
			input = keyboard.nextLine().trim();
			while( !input.equals( "y" ) && !input.equals( "n" ) ) {
				System.out.print( "Enter 'y' to update the manufacturer company or 'n' to continue : " );
				input = keyboard.nextLine().trim();
			}
			if( input.equals( "y" ) ) {
				System.out.print( "New ID of the manufacturer company (press Enter make it null) : " );
				input = keyboard.nextLine().trim();
				
				while( !StringUtils.isStrictlyNumeric( input ) && !StringUtils.isNullOrEmpty( input ) ) {
					System.out.print( "Invalid company ID. Enter a valid ID (0 to come back to the menu) : " );
					input = keyboard.nextLine().trim();
				}
				
				Company company = null;
				if( !StringUtils.isNullOrEmpty( input ) ) {
					try {
						company = companyDAO.find( Integer.parseInt( input ) );
					} catch (DAOException e){
						System.out.println( "This company doesn't exist. The new manufacturer will be null." );
					}
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

	public static void main(String[] args) throws ParseException {
		CliUI cliUI = new CliUI();
		cliUI.start();
		System.exit(0);
	}	
}
