package com.excilys.cdb.dto;

public class ComputerDto {
	private int id;
	private String name;
	private String introduced;
	private String discontinued;
	private String company;
	
	public ComputerDto(){}
	
	public ComputerDto(int id, String name, String introduced, String discontinued, String company) {
		super();
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company = company;
	}


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
	public String getIntroduced() {
		return introduced;
	}
	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}
	public String getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String toString() {
	    return String.format("Computer %s : %s", this.getId(), this.getName());
	}
	
	public String toDetailedString() {
	    StringBuilder sb = new StringBuilder(this.toString());
	    
	    if(!this.getIntroduced().equals("")) {
	    	sb.append("\n\t| Introduced the " + this.getIntroduced() + "\n");
	    } else {
	    	sb.append("\n\t| Introduction date unknown \n");
	    }
	    
	    if(!this.getDiscontinued().equals("")) {
	    	sb.append("\t| Discontinued the " + this.getDiscontinued() + "\n");
	    } else {
	    	sb.append("\t| Discontinuation date unknown \n");
	    }
	    
	    if(!this.getCompany().equals("")) {
	    	sb.append("\t| Manufactured by " + this.getCompany() + "\n");
	    } else {
	    	sb.append("\t| Manufacturer unknown \n");
	    }

	    return sb.toString();
	  }
}
