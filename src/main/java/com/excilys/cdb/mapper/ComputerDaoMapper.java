package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;

@Lazy
@Component
public class ComputerDaoMapper {
  private CompanyDao companyDao;
  
  public ComputerDaoMapper(CompanyDao cd) {
	  companyDao = cd;
  }

  public Computer map( ResultSet resultSet ) throws SQLException {
    Computer computer = new Computer();

    computer.setId(resultSet.getInt("id"));
    computer.setName(resultSet.getString("name"));
    computer.setIntroduced(resultSet.getTimestamp("introduced"));
    computer.setDiscontinued(resultSet.getTimestamp("discontinued"));
    computer.setCompany(companyDao.findById(resultSet.getInt("company_id")).orElse(null));
    
    return computer;
  }

}
