package com.excilys.cdb.model;

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
  
  @Override
  public String toString() {
    return String.format("Company %s : %s", this.getId(), this.getName());
  }

}
