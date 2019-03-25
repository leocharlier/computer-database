package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoFactory;

public class ComputerDaoMapper {

  public Computer map( ResultSet resultSet ) throws SQLException {
    Computer computer = new Computer();
    DaoFactory daoFactory = DaoFactory.getInstance();
    CompanyDao companyDao = daoFactory.getCompanyDao();

    computer.setId(resultSet.getInt("id"));
    computer.setName(resultSet.getString("name"));
    computer.setIntroduced(resultSet.getTimestamp("introduced"));
    computer.setDiscontinued(resultSet.getTimestamp("discontinued"));
    computer.setCompany(companyDao.findById(resultSet.getInt("company_id")).orElse(null));
    
    return computer;
  }

}
