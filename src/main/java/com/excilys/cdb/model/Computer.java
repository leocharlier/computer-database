package com.excilys.cdb.model;

import java.sql.Timestamp;
import java.util.Optional;

public class Computer {
  private int id;
  private String name;
  private Timestamp introduced;
  private Timestamp discontinued;
  private Company company;

  public Computer() {}

  public Computer(String pname) {
    this.name = pname;
  }

  public Computer(String pname, Timestamp pdiscontinued) {
    this.name = pname;
    this.discontinued = pdiscontinued;
  }

  public Computer(String pname, Timestamp pintroduced, Timestamp pdiscontinued) {
    this.name = pname;
    this.introduced = pintroduced;
    this.discontinued = pdiscontinued;
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

  public Optional<Timestamp> getIntroduced() {
    return Optional.ofNullable(introduced);
  }

  public void setIntroduced(Timestamp introduced) {
    this.introduced = introduced;
  }

  public Optional<Timestamp> getDiscontinued() {
    return Optional.ofNullable(discontinued);
  }

  public void setDiscontinued(Timestamp discontinued) {
    this.discontinued = discontinued;
  }

  public Optional<Company> getCompany() {
    return Optional.ofNullable(company);
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public String toString() {
    return String.format("Computer %s : %s", this.getId(), this.getName());
  }

  public String toDetailedString() {
    StringBuilder sb = new StringBuilder(this.toString());
    
    sb.append(this.getIntroduced().map(someDate -> String.format("\n\t| Introduced the %s\n", someDate))
                                  .orElse("\t| Introducing date unknown \n"));
    
    sb.append(this.getDiscontinued().map(someDate -> String.format("\t| Discontinued the %s\n", someDate))
                                  .orElse("\t| Discontinuing date unknown \n"));
    
    sb.append(this.getCompany().map(someCompanyName -> String.format("\t| Manufactured by %s\n", someCompanyName.getName()))
            .orElse("\t| Manufacturer unknown \n"));

    return sb.toString();
  }
}
