package com.excilys.cdb.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.excilys.cdb.dto.ComputerDto;

@Component
public class ComputerDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ComputerDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ComputerDto computerDto = (ComputerDto) target;
		Pattern dateFormat = Pattern.compile("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
		
		Matcher matcherIntroduced = dateFormat.matcher(computerDto.getIntroduced());
		if(!"".equals(computerDto.getIntroduced()) && !matcherIntroduced.matches()) {
			errors.rejectValue("introduced", "introduced.wrongDateFormat", "Wrong date format for introduction date.");
		}
	
		if(computerDto.getDiscontinued() != null) {
			Matcher matcherDiscontinued = dateFormat.matcher(computerDto.getDiscontinued());	
			if(!"".equals(computerDto.getDiscontinued())) {
				if(!matcherDiscontinued.matches()) {
					errors.rejectValue("discontinued", "discontinued.wrongDateFormat", "Wrong date format for discontinuation date.");
				} else if("".equals(computerDto.getIntroduced())){
					errors.rejectValue("discontinued", "discontinuedButNoIntroduced", "Discontinuation date cannot be set if introduction date is null.");
				}
			}
		}
	}
}
