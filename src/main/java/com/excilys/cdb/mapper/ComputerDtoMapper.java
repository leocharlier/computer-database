package com.excilys.cdb.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;

public class ComputerDtoMapper {
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public ComputerDto map(Computer computer) {
		ComputerDto computerDto = new ComputerDto();
		computerDto.setName(computer.getName());
		computerDto.setIntroduced( computer.getIntroduced()
				.map(someDate -> dateFormat.format(new Date(someDate.getTime())))
				.orElse(""));
		computerDto.setDiscontinued( computer.getDiscontinued()
				.map(someDate -> dateFormat.format(new Date(someDate.getTime())))
				.orElse(""));
		computerDto.setCompany( computer.getCompany()
				.map(someCompany -> someCompany.getName())
				.orElse(""));
		
		return computerDto;
	}
	
	public List<ComputerDto> map(List<Computer> computers){
		return computers.stream().map( computer -> map(computer)).collect(Collectors.toList());
	}

}
