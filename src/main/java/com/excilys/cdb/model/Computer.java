package com.excilys.cdb.model;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="computer")
public class Computer {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  
  @Column(name="name")
  private String name;
  
  @Column(name="introduced")
  private Date introduced;

  @Column(name="discontinued")
  private Date discontinued;
  
  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  public Computer() {}

  public Computer(String pname) {
    this.name = pname;
  }

  public Computer(String pname, Date pdiscontinued) {
    this.name = pname;
    this.discontinued = pdiscontinued;
  }

  public Computer(String pname, Date pintroduced, Date pdiscontinued) {
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

  public Optional<Date> getIntroduced() {
    return Optional.ofNullable(introduced);
  }

  public void setIntroduced(Date introduced) {
    this.introduced = introduced;
  }

  public Optional<Date> getDiscontinued() {
    return Optional.ofNullable(discontinued);
  }

  public void setDiscontinued(Date discontinued) {
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
                                  .orElse("\n\t| Introducing date unknown \n"));
    
    sb.append(this.getDiscontinued().map(someDate -> String.format("\t| Discontinued the %s\n", someDate))
                                  .orElse("\t| Discontinuing date unknown \n"));
    
    sb.append(this.getCompany().map(someCompanyName -> String.format("\t| Manufactured by %s\n", someCompanyName.getName()))
            .orElse("\t| Manufacturer unknown \n"));

    return sb.toString();
  }
}
