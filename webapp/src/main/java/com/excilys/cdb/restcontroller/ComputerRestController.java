package com.excilys.cdb.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.DiscontinuedBeforeIntroducedException;
import com.excilys.cdb.exception.DiscontinuedButNoIntroducedException;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.mapper.DtoComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ComputerService;

@RestController
@RequestMapping(path="/api/computers", produces = "application/json")
public class ComputerRestController {
	private ComputerService computerService;
	private ComputerDtoMapper computerDtoMapper;
	private DtoComputerMapper dtoComputerMapper;
	
	public ComputerRestController(ComputerService sComputer, 
			ComputerDtoMapper mComputerDto,
			DtoComputerMapper mDtoComputer) {
		computerService = sComputer;
		computerDtoMapper = mComputerDto;
		dtoComputerMapper = mDtoComputer;
	}
	
	@GetMapping
	public List<ComputerDto> getPageableComputers(@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "sort", defaultValue = "nameAsc") String sort,
			@RequestParam(name = "size", defaultValue = "10") String size,
			@RequestParam(name = "page", defaultValue = "1") String page)
					throws ResponseStatusException {
		ArrayList<Computer> computers;
		if(search != null && !search.isEmpty()) {
			computers = computerService.searchService(search);
		} else {
			computers = computerService.listService();
		}
		
		if(!computers.isEmpty()) {
			switch(sort) {
				case "nameAsc" :
					computerService.sortByNameAscService(computers);
					break;
				case "nameDesc" :
					computerService.sortByNameDescService(computers);
					break;
				case "introducedAsc" :
					computerService.sortByIntroducedAscService(computers);
					break;
				case "introducedDesc" :
					computerService.sortByIntroducedDescService(computers);
					break;
				case "discontinuedAsc" :
					computerService.sortByDiscontinuedAscService(computers);
					break;
				case "discontinuedDesc" :
					computerService.sortByDiscontinuedDescService(computers);
					break;
				case "companyAsc" :
					computerService.sortByCompanyNameAscService(computers);
					break;
				case "companyDesc" :
					computerService.sortByCompanyNameDescService(computers);
					break;
			}
			
			Page<Computer >currentPage = new Page<Computer>(computers, Integer.parseInt(size));
			int numPage = Integer.parseInt(page);
			if(numPage < 1) {
				numPage = 1;
			} else if(numPage > currentPage.getMaxPages()) {
				numPage = currentPage.getMaxPages();
			}
				
			return computerDtoMapper.map(currentPage.getPageData(numPage - 1));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Computer's list is empty.");
		}
	}
	
	@GetMapping(path = "/{id}")
	public ComputerDto getUniqueComputer(@PathVariable("id") int id) throws ResponseStatusException {
		Optional<Computer> computerToFind = computerService.findService(id);
		if(computerToFind.isPresent()) {
			return computerDtoMapper.map(computerToFind.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Computer " + id + " doesn't exist.");
		}
	}
	
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addComputer(@RequestBody ComputerDto computerToAdd) {
        try {
        	Computer computer = dtoComputerMapper.map(computerToAdd);
        	computerService.createService(computer);
        } catch(DaoException e) {
        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Creation : An error has occured during the creation...", e);
        } catch(ComputerNullNameException e) {
        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Creation : The name needs to be set.", e);
        } catch(DiscontinuedButNoIntroducedException e) {
        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Creation : The discontinuation date cannot be set if introduction date is not.", e);
        } catch(DiscontinuedBeforeIntroducedException e) {
        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Creation : The discontinuation date must be same or after the introduction date.", e);
        }
    }
	
	@PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ComputerDto updateComputer(@PathVariable( "id" ) int id, @RequestBody Computer computerToUpdate) {
		if(computerService.findService(id).isPresent()) {
			try {
	        	computerService.updateService(computerToUpdate);
	        	return computerDtoMapper.map(computerService.findService(id).get());
	        } catch(DaoException e) {
	        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Update : An error has occured during the update...", e);
	        } catch(ComputerNullNameException e) {
	        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Update : The name needs to be set.", e);
	        } catch(DiscontinuedButNoIntroducedException e) {
	        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Update : The discontinuation date cannot be set if introduction date is not.", e);
	        } catch(DiscontinuedBeforeIntroducedException e) {
	        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Update : The discontinuation date must be same or after the introduction date.", e);
	        }
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Computer " + id + " doesn't exist.");
		}
    }
	
	@DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
		Optional<Computer> computerToDelete = computerService.findService(id);
		if(computerToDelete.isPresent()) {
			try {
	        	computerService.deleteService(computerToDelete.get());
	        } catch(DaoException e) {
	        	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , "Deletion : An error has occured during the deletion...", e);
	        }
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , "Computer " + id + " doesn't exist.");
		}
    }
}
