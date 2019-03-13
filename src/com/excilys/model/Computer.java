package com.excilys.model;

import java.sql.Timestamp;

public class Computer {
	private int id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
	private Company company;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getIntroduced() {
		return introduced;
	}
	public void setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
	}
	public Timestamp getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(Timestamp discontinued) {
		this.discontinued = discontinued;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Computer ");
		sb.append( this.getId() );
		sb.append( " : " );
		sb.append( this.getName() );
		return sb.toString();
	}
	
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder("Computer ");
		
		sb.append( this.getId() );
		sb.append( " : " );
		sb.append( this.getName() );
		sb.append( "\n" );
		
		if( this.getIntroduced() == null ) {
			sb.append( "\t| Introducing date unknown \n" );
		} else {
			sb.append( "\t| Introduced the " );
			sb.append( this.getIntroduced() );
			sb.append( "\n" );
		}
		
		if( this.getDiscontinued() == null  ) {
			sb.append( "\t| Discontinuing date unknown \n" );
		} else {
			sb.append( "\t| Discontinued the " );
			sb.append( this.getDiscontinued() );
			sb.append( "\n" );
		}
		
		if( this.getCompany() == null ) {
			sb.append( "\t| Manufacturer unknown \n" );
		} else {
			sb.append( "\t| Manufactured by " );
			sb.append( this.getCompany().getName() );
			sb.append( "\n" );
		}
		
		return sb.toString();
	}
	
}
