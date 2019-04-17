package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;

@Lazy
@Component
public class CompanyDaoMapper implements RowMapper<Company> {

  @Override
  public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
	  Company company = new Company();
	  company.setId(rs.getInt("id"));
	  company.setName(rs.getString("name"));
	  return company;
  }

}
