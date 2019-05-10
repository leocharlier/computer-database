package com.excilys.cdb.ui;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;

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
		cli = ClientBuilder.newClient().property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);;
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
		System.out.println( "4 : Search Computers" );
		System.out.println( "5 : Create a Computer" );
		System.out.println( "6 : Update a Computer" );
		System.out.println( "7 : Delete a Computer" );
		System.out.println( "8 : Delete a Company" );
	}
	
	public void listComputers() {
		Response response = basePath.path("computers")
				.request(MediaType.APPLICATION_JSON)
				.get();
		
		switch(response.getStatus()) {
			case 200:
				List<ComputerDto> computers = response.readEntity(new GenericType<List<ComputerDto>>() {});
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
				break;
			case 204:
				System.out.println("There's no computer yet, add the first one now !");
				break;
			case 500:
				System.out.println("Error 500 : An error has occured during the database process.\n");
				break;
			default:
				System.out.println("Error " + response.getStatus() + " : An error has occured during the computer listing, please try later.\n");
				break;
		}
	}
	
	public void listCompanies() {
		Response response = basePath.path("companies")
				.request(MediaType.APPLICATION_JSON)
				.get();
		
		switch(response.getStatus()) {
			case 200:
				List<Company> companies = response.readEntity(new GenericType<List<Company>>() {});
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
				break;
			case 204:
				System.out.println("There's no company yet, add the first one now !");
				break;
			case 500:
				System.out.println("Error 500 : An error has occured during the database process.\n");
				break;
			default:
				System.out.println("Error " + response.getStatus() + " : An error has occured during the company listing, please try later.\n");
				break;
		}
	}
	
	public void searchComputers() {
		System.out.print( "Research for a computer and/or company (press Enter if it's unknown and 0 to exit the creation) : " );
		String search = keyboard.nextLine().trim();
		if(!search.equals("0")) {
			Response response = basePath.path("computers")
					.queryParam("search", search)
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			switch(response.getStatus()) {
				case 200:
					List<ComputerDto> computers = response.readEntity(new GenericType<List<ComputerDto>>() {});
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
					break;
				case 204:
					System.out.println("Sorry, no computer found for the research '" + search + "'.\n");
					break;
				case 500:
					System.out.println("Error 500 : An error has occured during the database process.\n");
					break;
				default:
					System.out.println("Error " + response.getStatus() + " : An error has occured during the research, please try later.\n");
					break;
			}
		}
	}
	
	public void showComputer() {
		System.out.print( "Enter the ID of the computer (press 0 to come back to the menu) : " );
		String input = keyboard.nextLine().trim();
		if( !input.equals( "0" ) ) {
			Response response = basePath.path("computers/{id}")
					.resolveTemplate("id", input)
					.request(MediaType.APPLICATION_JSON)
					.get();
			
			switch(response.getStatus()) {
				case 200:
					ComputerDto computer = response.readEntity(ComputerDto.class);
					System.out.println(computer.toDetailedString());
					break;
				case 404:
					System.out.println("Error 404 : Sorry, the computer " + input + " doesn't exist.\n" );
					break;
				case 400:
					System.out.println("Error 400 : Invalid input, the computer ID must be an integer.\n");
					break;
				case 500:
					System.out.println("Error 500 : An error has occured during the database process.\n");
					break;
				default:
					System.out.println("Error " + response.getStatus() + " : An error has occured during the process, please try later.\n");
					break;
			}
		}
	}
	
	public void createComputer() {
		System.out.print( "Enter the name of the new computer (press 0 to exit the creation) : " );
		String name = keyboard.nextLine().trim();
		System.out.print( "Enter the introduction date (press Enter if it's unknown) with pattern 'yyyy-MM'dd' : " );
		String introduced = keyboard.nextLine().trim();
		System.out.print( "Enter the discontinuation date (press Enter if it's unknown) with pattern 'yyyy-MM'dd' : " );
		String discontinued = keyboard.nextLine().trim();
		System.out.print( "Enter the name of the manufacturer (press Enter if it's unknown) : " );
		String company = keyboard.nextLine().trim();
		ComputerDto newComputer = new ComputerDto();
		newComputer.setName(name);
		newComputer.setIntroduced(introduced);
		newComputer.setDiscontinued(discontinued);
		newComputer.setCompany(company);
		Response response = basePath.path("computers")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(newComputer));
		
		switch(response.getStatus()) {
			case 201:
				System.out.println("The computer '" + name + "' has been created.\n");
				break;
			case 400:
				System.out.println("Error 400 : Error in one of the field. Please follow these rules :\n"
						+ " - The name cannot be null or empty.\n"
						+ " - The dates must follow this pattern : yyyy-MM-dd.\n"
						+ " - Introduction date must be SAME or BEFORE discontinuation.\n");
				break;
			case 500:
				System.out.println("Error 500 : An error has occured during the database process.\n");
				break;
			default:
				System.out.println("Error " + response.getStatus() + " : An error has occured during the process, please try later.\n");
				break;
		}
	}
	
	public void updateComputer() {
		System.out.print( "Enter the ID of the computer you want to update (press 0 to come back to the menu) : " );
		String idString = keyboard.nextLine().trim();
		try {
			int id = Integer.parseInt(idString);
			if(id != 0) {
				System.out.print( "New name : " );
				String name = keyboard.nextLine().trim();
				System.out.print( "New introduction date with pattern 'yyyy-MM'dd' : " );
				String introduced = keyboard.nextLine().trim();
				System.out.print( "New discontinuation date with pattern 'yyyy-MM'dd' : " );
				String discontinued = keyboard.nextLine().trim();
				System.out.print( "New manufacturer name : " );
				String company = keyboard.nextLine().trim();
				ComputerDto computerToUpdate = new ComputerDto();
				computerToUpdate.setId(id);
				computerToUpdate.setName(name);
				computerToUpdate.setIntroduced(introduced);
				computerToUpdate.setDiscontinued(discontinued);
				computerToUpdate.setCompany(company);
				Response response = basePath.path("computers/{id}")
						.resolveTemplate("id", id)
						.request(MediaType.APPLICATION_JSON)
						.build("PATCH", Entity.json(computerToUpdate)).invoke();
				
				switch(response.getStatus()) {
					case 200:
						ComputerDto newComputer = response.readEntity(ComputerDto.class);
						System.out.println("The computer has been updated. Here is your new computer :\n");
						System.out.println(newComputer.toDetailedString());
						break;
					case 400:
						System.out.println("Error 400 : Error in one of the field. Please follow these rules :\n"
								+ " - The name cannot be null or empty.\n"
								+ " - The dates must follow this pattern : yyyy-MM-dd.\n"
								+ " - Introduction date must be SAME or BEFORE discontinuation.\n");
						break;
					case 405:
						System.out.println("Error 405 : Sorry, the computer " + id + " doesn't exist.\n");
						break;
					case 500:
						System.out.println("Error 500 : An error has occured during the database process.\n");
						break;
					default:
						System.out.println("Error " + response.getStatus() + " : An error has occured during the process, please try later.\n");
						break;
				}
			}
		} catch(NumberFormatException e) {
			System.out.println("Invalid input, the computer ID must be an integer.\n");
		}	
	}
	
	public void deleteComputer() {
		System.out.print( "Enter the ID of the computer you want to delete (press 0 to come back to the menu) : " );
		String input = keyboard.nextLine().trim();
		if( !input.equals( "0" ) ) {
			Response response = basePath.path("computers/{id}")
					.resolveTemplate("id", input)
					.request(MediaType.APPLICATION_JSON)
					.delete();
			switch(response.getStatus()) {
				case 200:
					System.out.println("The computer " + input + " has been deleted.\n");
					break;
				case 400:
					System.out.println("Error 400 : The computer ID must be an integer.\n");
					break;
				case 405:
					System.out.println("Error 405 : Sorry, the computer " + input + " doesn't exist.\n");
					break;
				case 500:
					System.out.println("Error 500 : An error has occured during the database process.\n");
					break;
				default:
					System.out.println("Error " + response.getStatus() + " : An error has occured during the process, please try later.\n");
					break;
			}
			
		}
	}
	
	public void deleteCompany() {
		System.out.print( "Enter the ID of the company you want to delete (press 0 to come back to the menu) : " );
		String input = keyboard.nextLine().trim();
		if( !input.equals( "0" ) ) {
			Response response = basePath.path("companies/{id}")
					.resolveTemplate("id", input)
					.request(MediaType.APPLICATION_JSON)
					.delete();
			switch(response.getStatus()) {
				case 200:
					System.out.println("The company " + input + " has been deleted.\n");
					break;
				case 400:
					System.out.println("Error 400 : The company ID must be an integer.\n");
					break;
				case 405:
					System.out.println("Error 405 : Sorry, the company " + input + " doesn't exist.\n");
					break;
				case 500:
					System.out.println("Error 500 : An error has occured during the database process.\n");
					break;
				default:
					System.out.println("Error " + response.getStatus() + " : An error has occured during the process, please try later.\n");
					break;
			}
		}
	}
	
	public static void main(String[] args) throws ParseException {
		ClientRS client = new ClientRS();
		client.run();
		System.exit(0);
	}
}
