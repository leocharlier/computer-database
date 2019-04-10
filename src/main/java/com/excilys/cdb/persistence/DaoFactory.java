package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class DaoFactory {
  @Autowired
  private HikariDataSource dataSource;

  protected Connection getConnection() throws SQLException {
	  return dataSource.getConnection();
  }
}
