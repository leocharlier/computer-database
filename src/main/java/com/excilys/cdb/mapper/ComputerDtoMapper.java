package com.excilys.cdb.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;

@Lazy
@Component
public class ComputerDtoMapper {
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public ComputerDto map(Computer computer) {
		return new ComputerDto(computer.getId(),
				               computer.getName(),
				               computer.getIntroduced()
								.map(someDate -> DATE_FORMAT.format(new Date(someDate.getTime())))
								.orElse(""),
							   computer.getDiscontinued()
								.map(someDate -> DATE_FORMAT.format(new Date(someDate.getTime())))
								.orElse(""),
							   computer.getCompany()
								.map(someCompany -> someCompany.getName())
								.orElse(""));
	}

	public List<ComputerDto> map(List<Computer> computers){
		return computers.stream().map( computer -> map(computer)).collect(Collectors.toList());
	}
}
