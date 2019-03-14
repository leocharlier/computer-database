package com.excilys.model;

public class Company {
	
	private int id;
	private String name;
	
	public int getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("Company ");
		sb.append( this.getId() );
		sb.append( " : " );
		sb.append( this.getName() );
		return sb.toString();
	}
	
}
