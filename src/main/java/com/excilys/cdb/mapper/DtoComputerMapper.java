package com.excilys.cdb.mapper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.DtoDateParseException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoFactory;

public class DtoComputerMapper {
	
	public Computer map(ComputerDto dtoComputer) {
		DaoFactory daoFactory = DaoFactory.getInstance();
	    CompanyDao companyDao = daoFactory.getCompanyDao();
	    
		Computer computer = new Computer();
		computer.setName(dtoComputer.getName());
		
		if(dtoComputer.getIntroduced().isEmpty()) {
			computer.setIntroduced(null);
		} else {
			String dateString = dtoComputer.getIntroduced();
			try {
				Date date = ComputerDtoMapper.DATE_FORMAT.parse(dateString);
				long time = date.getTime();
				Timestamp introduced = new Timestamp(time);
				computer.setIntroduced(introduced);
			} catch(ParseException e) {
				throw new DtoDateParseException("Introduced date parsing error.");
			}
		}
		
		if(dtoComputer.getDiscontinued().isEmpty()) {
			computer.setDiscontinued(null);
		} else {
			String dateString = dtoComputer.getDiscontinued();
			try {
				Date date = ComputerDtoMapper.DATE_FORMAT.parse(dateString);
				long time = date.getTime();
				Timestamp discontinued = new Timestamp(time);
				computer.setDiscontinued(discontinued);
			} catch(ParseException e) {
				throw new DtoDateParseException("Discontinued date parsing error.");
			}
		}
		
		if(companyDao.findByName(dtoComputer.getCompany()).isPresent()) {
			Company company = companyDao.findByName(dtoComputer.getCompany()).get();
			computer.setCompany(company);
		} else {
			computer.setCompany(null);
		}

		return computer;
	}
	
}
