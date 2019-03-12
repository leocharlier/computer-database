package com.excilys.ui;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.persistence.*;
import com.mysql.cj.util.StringUtils;

public class CliUI {

	public static void main(String[] args) {
		
		DAOFactory daoFactory = DAOFactory.getInstance();	
		CompanyDAO companyDAO = daoFactory.getCompanyDao();
		ComputerDAO computerDAO = daoFactory.getComputerDao();
		
		Scanner keyboard = new Scanner(System.in);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		String action = "-1";
		StringBuilder sbCompanies;
		StringBuilder sbComputers;
		Computer computer;
		String name;
		Date date;
		long time;
		Timestamp introduced;
		Timestamp discontinued;
		Company company;
		String input;
		
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
			
			action = keyboard.nextLine();
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
					sbCompanies = new StringBuilder( "List of companies : \n" );
					for( Company companyEntry : companyDAO.list() ) {
						sbCompanies.append( companyEntry.toString() );
						sbCompanies.append( "\n" );
					}
					System.out.println( sbCompanies );
					break;
				
				case "2" :
					sbComputers = new StringBuilder( "List of computers : \n" );
					for( Computer computerEntry : computerDAO.list() ) {
						sbComputers.append( computerEntry.toString() );
						sbComputers.append( "\n" );
					}
					System.out.println( sbComputers );
					break;
					
				case "3" :
					System.out.print( "Enter the ID of the computer (0 to come back to the menu) : " );
					input = keyboard.nextLine();
					
					while( ( StringUtils.isNullOrEmpty( input ) || !StringUtils.isStrictlyNumeric( input ) ) && !input.equals( "0" ) ) {
						System.out.print( "Invalid computer ID. Enter a valid ID (0 to come back to the menu) : " );
						input = keyboard.nextLine();
					}
					
					if( !input.equals( "0" ) ) {
						try {
							computer = computerDAO.find( Integer.parseInt( input ) );
							System.out.println( computer.toDetailedString() );
						} catch ( NullPointerException e) {
							System.out.println( "Sorry, this computer doesn't exist.\n" );
						}
						
					}
					
					break;
					
				case "4" :
					System.out.println( "|||||||||||||||||||||||" );
					System.out.println( "|| Computer creation ||" );
					System.out.println( "|||||||||||||||||||||||\n\n" );
					
					System.out.print( "Enter the name of the computer (0 to exit the creation) : " );
					input = keyboard.nextLine();
					
					while( StringUtils.isNullOrEmpty( input ) && !input.equals( "0" ) ) {
						System.out.print( "The name cannot be null. Enter a valid name (0 to to exit the creation) : " );
						input = keyboard.nextLine();
					}
					
					if( !input.equals( "0" ) ) {
						name = input;
					} else {
						System.out.println();
						break;
					}
					
					System.out.print( "Enter the date of introduction (press Enter if it's unknown and 0 to exit the creation) : " );
					input = keyboard.nextLine();
					
					if( StringUtils.isNullOrEmpty( input ) ) {
						introduced = null;
					} else if( !input.equals( "0" ) ) {
						try {
							date = dateFormat.parse(input);
							time = date.getTime();
							introduced = new Timestamp(time);
						} catch (Exception e) {
							System.out.println( "Wrong date format. The date of introduction will be null." );
							introduced = null;
						}
					} else {
						System.out.println();
						break;
					}
					
					if( introduced != null) {
						System.out.print( "Enter the date of discontinuation (press Enter if it's unknown  and 0 to exit the creation) : " );
						input = keyboard.nextLine();
						
						if( StringUtils.isNullOrEmpty( input ) ) {
							discontinued = null;
						} else if( !input.equals( "0" ) ) {
							try {
								date = dateFormat.parse(input);
								time = date.getTime();
								discontinued = date.after( introduced ) ? new Timestamp(time) : null;
							} catch (Exception e) {
								System.out.println( "Wrong date format. The date of discontinuation will be null. " );
								discontinued = null;
							}
						} else {
							System.out.println();
							break;
						}
					} else {
						discontinued = null;
					}
					
					System.out.print( "Enter the ID of the manufacturer company (press Enter if it's unknown  and 0 to exit the creation) : " );
					input = keyboard.nextLine();
					
					if( StringUtils.isNullOrEmpty( input ) ) {
						company = null;
					} else if( !input.equals( "0" ) ) {
						try {
							company = companyDAO.find( Integer.parseInt( input ) );
						} catch ( NullPointerException e) {
							System.out.println( "This company doesn't exist. The manufacturer will be null." );
							company = null;
						}
					} else {
						System.out.println();
						break;
					}
					
					computer = new Computer();
					computer.setName( name );
					computer.setIntroduced( introduced );
					computer.setDiscontinued( discontinued );
					computer.setCompany( company );
					
					computerDAO.create( computer );
					
					System.out.println( "\n||||||||||||||||||||||||" );
					System.out.println( "|| Computer created ! ||" );
					System.out.println( "||||||||||||||||||||||||\n\n" );
					
					break;
				
				case "5" :
					computer = computerDAO.find(570);
					System.out.println( computer.toDetailedString() );
					break;
				
				case "6" :
					System.out.print( "Enter the ID of the computer (0 to come back to the menu) : " );
					input = keyboard.nextLine();
					
					while( ( StringUtils.isNullOrEmpty( input ) || !StringUtils.isStrictlyNumeric( input ) ) && !input.equals( "0" ) ) {
						System.out.print( "Invalid computer ID. Enter a valid ID (0 to come back to the menu) : " );
						input = keyboard.nextLine();
					}
					
					if( !input.equals( "0" ) ) {
						try {
							computer = computerDAO.find( Integer.parseInt( input ) );
							System.out.println( computer.toDetailedString() );
							System.out.print( "Are you sure you want to delete this computer ? (y\\n) : " );
							input = keyboard.nextLine();
							while( !input.equals( "y" ) && !input.equals( "n" ) ) {
								System.out.print( "Enter 'y' to confirm the deletion or 'n' to come back to the menu : " );
								input = keyboard.nextLine();
							}
							if( input.equals( "y" ) ) {
								computerDAO.delete( computer );
								
								System.out.println( "\n||||||||||||||||||||||||" );
								System.out.println( "|| Computer deleted ! ||" );
								System.out.println( "||||||||||||||||||||||||\n\n" );
								
							} else {
								System.out.println();
								break;
							}
						} catch ( NullPointerException e) {
							System.out.println( "Sorry, this computer doesn't exist.\n" );
						}
					}
					
					break;

				default :
					System.out.println( "Invalid input. Enter 1, 2, ..., 6 to perform an action or 0 to exit.\n" );
					break;
			}
		}
		
	}

}
