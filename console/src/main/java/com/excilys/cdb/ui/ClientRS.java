package com.excilys.cdb.ui;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Company;

public class ClientRS {
	private Scanner keyboard;
	private Client cli;
	private WebTarget basePath;
	
	private enum Actions {	
		EXIT( "0" ),
		LIST_COMPANIES( "1" ),
		LIST_COMPUTERS( "2" ), 
		SHOW_COMPUTER( "3" ),
		SEARCH_COMPUTERS( "4" ),
		CREATE_COMPUTER( "5" ),
		UPDATE_COMPUTER( "6" ),
		DELETE_COMPUTER( "7" ),
		DELETE_COMPANY( "8" );
		
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
	
	public ClientRS() {
		cli = ClientBuilder.newClient();
		basePath = cli.target("http://localhost:8080/webapp/api");
		keyboard = new Scanner(System.in);
	}
	
	public void run() {
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
						cli.close();
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
					case SEARCH_COMPUTERS :
						searchComputers();
						break;
					case CREATE_COMPUTER :

						break;
					case UPDATE_COMPUTER :

						break;
					case DELETE_COMPUTER :

						break;
					case DELETE_COMPANY :

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
		System.out.println( "4 : Search Computers" );
		System.out.println( "5 : Create a Computer" );
		System.out.println( "6 : Update a Computer" );
		System.out.println( "7 : Delete a Computer" );
		System.out.println( "8 : Delete a Company" );
	}
	
	public void listComputers() {
		List<ComputerDto> computers = basePath.path("computers")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<ComputerDto>>() {});
		
		Pagination pageAction = null;
		int page = 1;
		while( pageAction != Pagination.STOP ) {
			if( pageAction != Pagination.INVALID ) {
				computers.stream().forEach(c -> System.out.println(c.toString()));
			}
			
			System.out.print("Press 'n' for next page, 'p' for previous page or 's' to stop : ");

			pageAction = Pagination.codeToAction( keyboard.nextLine().trim() );
			System.out.println();

			switch(pageAction) {
				case NEXT :
					computers = basePath.path("computers")
						.queryParam("page", ++page)
						.request(MediaType.APPLICATION_JSON)
						.get(new GenericType<List<ComputerDto>>() {});
					break;
				case PREVIOUS :
					computers = basePath.path("computers")
					.queryParam("page", --page)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<ComputerDto>>() {});
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
		List<Company> companies = basePath.path("companies")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Company>>() {});
		
		Pagination pageAction = null;
		int page = 1;
		while( pageAction != Pagination.STOP ) {
			if( pageAction != Pagination.INVALID ) {
				companies.stream().forEach(c -> System.out.println(c.toString()));
			}
			
			System.out.print("Press 'n' for next page, 'p' for previous page or 's' to stop : ");

			pageAction = Pagination.codeToAction( keyboard.nextLine().trim() );
			System.out.println();

			switch(pageAction) {
				case NEXT :
					companies = basePath.path("companies")
						.queryParam("page", ++page)
						.request(MediaType.APPLICATION_JSON)
						.get(new GenericType<List<Company>>() {});
					break;
				case PREVIOUS :
					companies = basePath.path("companies")
					.queryParam("page", --page)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Company>>() {});
					break;
				case STOP :
					break;
				case INVALID :
					System.out.print( "Invalid pagination input.\n" );
					break;
			}
		}
	}
	
	public void searchComputers() {
		System.out.print( "Research for a computer and/or company (press Enter if it's unknown and 0 to exit the creation) : " );
		String search = keyboard.nextLine().trim();
		if(!search.equals("0")) {
			try {
				List<ComputerDto> computers = basePath.path("computers")
						.queryParam("search", search)
						.request(MediaType.APPLICATION_JSON)
						.get(new GenericType<List<ComputerDto>>() {});
				Pagination pageAction = null;
				int page = 1;
				while( pageAction != Pagination.STOP ) {
					if( pageAction != Pagination.INVALID ) {
						computers.stream().forEach(c -> System.out.println(c.toString()));
					}
					
					System.out.print("Press 'n' for next page, 'p' for previous page or 's' to stop : ");

					pageAction = Pagination.codeToAction( keyboard.nextLine().trim() );
					System.out.println();

					switch(pageAction) {
						case NEXT :
							computers = basePath.path("computers")
								.queryParam("search", search)
								.queryParam("page", ++page)
								.request(MediaType.APPLICATION_JSON)
								.get(new GenericType<List<ComputerDto>>() {});
							break;
						case PREVIOUS :
							computers = basePath.path("computers")
							.queryParam("search", search)
							.queryParam("page", --page)
							.request(MediaType.APPLICATION_JSON)
							.get(new GenericType<List<ComputerDto>>() {});
							break;
						case STOP :
							break;
						case INVALID :
							System.out.print( "Invalid pagination input.\n" );
							break;
					}
				}
			} catch(NotFoundException e) {
				System.out.println("No result found for the research '" + search + "'.\n");
			}
			
			
			
		}
	}
	
	public void showComputer() {
		System.out.print( "Enter the ID of the manufacturer company (press Enter if it's unknown and 0 to exit the creation) : " );
		String input = keyboard.nextLine().trim();
		if( !input.equals( "0" ) ) {
			try {
				ComputerDto computer = basePath.path("computers/{id}")
						.resolveTemplate("id", input)
						.request(MediaType.APPLICATION_JSON)
						.get(ComputerDto.class);
				System.out.println(computer.toDetailedString());
			} catch(NotFoundException e) {
				System.out.println( "Sorry, the computer " + input + " doesn't exist.\n" );
			} catch(BadRequestException e) {
				System.out.println("Invalid input, the computer ID must be an integer.\n");
			}
		}
	}
	
	public static void main(String[] args) throws ParseException {
		ClientRS client = new ClientRS();
		client.run();
		System.exit(0);
	}
}
