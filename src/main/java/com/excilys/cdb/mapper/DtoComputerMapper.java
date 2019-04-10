package com.excilys.cdb.mapper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.DtoDateParseException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;

@Lazy
@Component
public class DtoComputerMapper {
	@Autowired
	private CompanyDao companyDao;
	
	public Computer map(ComputerDto dtoComputer) {
		Computer computer = new Computer();
		
		computer.setId(dtoComputer.getId());
		computer.setName(dtoComputer.getName().trim());
		
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
			
			if(dtoComputer.getDiscontinued().isEmpty()) {
				computer.setDiscontinued(null);
			} else {
				dateString = dtoComputer.getDiscontinued();
				try {
					Date date = ComputerDtoMapper.DATE_FORMAT.parse(dateString);
					long time = date.getTime();
					Timestamp discontinued = new Timestamp(time);
					computer.setDiscontinued(discontinued);
				} catch(ParseException e) {
					throw new DtoDateParseException("Discontinued date parsing error.");
				}
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
