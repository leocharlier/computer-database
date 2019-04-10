package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;

@Component
public class CompanyDaoMapper {

  public Company map(ResultSet resultSet) throws SQLException {
    Company company = new Company();
    company.setId(resultSet.getInt("id"));
    company.setName(resultSet.getString("name"));
    return company;
  }

}
