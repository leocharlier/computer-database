package com.excilys.cdb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.exception.ComputerNullNameException;
import com.excilys.cdb.exception.DaoException;
import com.excilys.cdb.exception.DiscontinuedBeforeIntroducedException;
import com.excilys.cdb.exception.DiscontinuedButNoIntroducedException;
import com.excilys.cdb.mapper.ComputerDtoMapper;
import com.excilys.cdb.mapper.DtoComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.ComputerDtoValidator;

@Controller
public class ComputerController {
	private static final String DASHBOARD = "dashboard";
	private static final String EDIT_COMPUTER = "editComputer";
	private static final String ADD_COMPUTER = "addComputer";
	private static final String EXCEPTION_VIEW = "500";
	private static final String NOT_FOUND_VIEW = "404";
	
	private static final int DEFAULT_PAGE_SIZE   = 10;
	
	private ComputerService computerService;
	private CompanyService companyService;
	
	private ComputerDtoMapper computerDtoMapper;
	private DtoComputerMapper dtoComputerMapper;
	
	private ComputerDtoValidator computerDtoValidator;
	
	private Page<Computer> page;
	private int currentPage;
	private int currentSize;
	
	public ComputerController(ComputerService sComputer, 
			CompanyService sCompany, 
			ComputerDtoMapper mComputerDto,
			DtoComputerMapper mDtoComputer,
			ComputerDtoValidator vComputerDto) {
		computerService = sComputer;
		companyService = sCompany;
		computerDtoMapper = mComputerDto;
		dtoComputerMapper = mDtoComputer;
		computerDtoValidator = vComputerDto;
	}
	
	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		binder.addValidators(computerDtoValidator);
	}
	
	
	@ModelAttribute
	public ComputerDto initComputerDto() {
		return new ComputerDto();
	}
	
	@GetMapping({"/", "/dashboard"})
	public String getDashBoard(@RequestParam(required = false) Map<String, String> paths, Model model) {
		ArrayList<Computer> computers;
		if(paths.containsKey("search") && !paths.get("search").equals("")) {
			computers = this.computerService.searchService(paths.get("search").trim());
			model.addAttribute("search", paths.get("search"));
		} else {
			computers = this.computerService.listService();
		}
		
		if(computers.isEmpty()) {
			model.addAttribute("nbOfComputers", 0);
			model.addAttribute("noComputersFound", true);
		} else {
			if(paths.containsKey("sort")) {
				switch(paths.get("sort")) {
					case "nameDesc" :
						model.addAttribute("sort", paths.get("sort"));
						computerService.sortByNameDescService(computers);
						break;
					case "introducedAsc" :
						model.addAttribute("sort", paths.get("sort"));
						computerService.sortByIntroducedAscService(computers);
						break;
					case "introducedDesc" :
						model.addAttribute("sort", paths.get("sort"));
						computerService.sortByIntroducedDescService(computers);
						break;
					case "discontinuedAsc" :
						model.addAttribute("sort", paths.get("sort"));
						computerService.sortByDiscontinuedAscService(computers);
						break;
					case "discontinuedDesc" :
						model.addAttribute("sort", paths.get("sort"));
						computerService.sortByDiscontinuedDescService(computers);
						break;
					case "companyAsc" :
						model.addAttribute("sort", paths.get("sort"));
						computerService.sortByCompanyNameAscService(computers);
						break;
					case "companyDesc" :
						model.addAttribute("sort", paths.get("sort"));
						computerService.sortByCompanyNameDescService(computers);
						break;
					default :
						computerService.sortByNameAscService(computers);
						break;
				}
			} else {
				computerService.sortByNameAscService(computers);
			}
			
			if (paths.containsKey("size")) {
				this.currentSize = Integer.parseInt(paths.get("size"));
			} else {
				this.currentSize = DEFAULT_PAGE_SIZE;
			}

			this.page = new Page<Computer>(computers, this.currentSize);
			model.addAttribute("size", this.currentSize);

			if (paths.containsKey("page") && Integer.parseInt(paths.get("page")) >= 1) {
				int currentPageInt = Integer.parseInt(paths.get("page"));
				if(currentPageInt <= this.page.getMaxPages()) {
					this.currentPage = currentPageInt;
				} else {
					this.currentPage = this.page.getMaxPages();
				}
			} else {
				this.currentPage = 1;
			}

			model.addAttribute("page", this.currentPage);

			List<ComputerDto> pageComputerDtos = computerDtoMapper.map(this.page.getPageData(this.currentPage - 1));

			model.addAttribute("computers", pageComputerDtos);
			model.addAttribute("nbOfComputers", computers.size());
			model.addAttribute("nbMaxPages", this.page.getMaxPages());
		}
		
		return DASHBOARD;
	}
	
	@PostMapping({"/", "/dashboard"})
	public String postDeleteComputers(@RequestParam(required = true) Map<String, String> paths, Model model) {
		String[] computersIdToDelete = paths.get("selection").split("\\,");
		
		for(String computerId : computersIdToDelete) {
			Optional<Computer> computerToDelete = computerService.findService(Integer.parseInt(computerId));
			if(computerToDelete.isPresent()) {
				try {
					computerService.deleteService(computerToDelete.get());
				} catch(DaoException e) {
					model.addAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the deletion...");
					return get500(model);
				}
			} else {
				model.addAttribute("errorMessage", "Sorry, the computer <strong>" + computerId + "</strong> doesn't exist.");
				return get404(model);
			}
		}
		
		return getDashBoard(paths, model);
	}
	
	@GetMapping("/addComputer")
	public String getAddComputer(Model model) {
		model.addAttribute("companies", companyService.listService());
		return ADD_COMPUTER;
	}
	
	@PostMapping("/addComputer")
	public String postAddComputer(@Validated @ModelAttribute("computerDto")ComputerDto dtoComputer, BindingResult result, Model model) {
		if(result.hasErrors()) {
			StringBuilder sb = new StringBuilder("Error(s) : \n");
			for(ObjectError error : result.getAllErrors()) {
				sb.append("<strong>");
				sb.append(error.getDefaultMessage());
				sb.append("</strong>");
				sb.append("\n");
			}
			model.addAttribute("errorMessage", "An error has occured during the <strong>form validation</strong>... " + sb);
			return get500(model);
		}
		
		Computer computer = dtoComputerMapper.map(dtoComputer);

		try {
			this.computerService.createService(computer);
			model.addAttribute("resultMessage", "The computer <strong>" + computer.getName() + "</strong> has been created !");
			model.addAttribute("computerDto", new ComputerDto());
			return getAddComputer(model);
		} catch (DaoException e) {
			model.addAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the creation...");
			return get500(model);
		} catch (ComputerNullNameException e) {
			model.addAttribute("errorMessage", "An error has occurred <strong>due to the name</strong> of the computer...");
			return get500(model);
		} catch (DiscontinuedButNoIntroducedException e) {
			model.addAttribute("errorMessage", "An error has occurred <strong>due to the discontinuation and introduction date</strong> of the computer...");
			return get500(model);
		} catch (DiscontinuedBeforeIntroducedException e) {
			model.addAttribute("errorMessage", "An error has occurred <strong>due to the discontinuation date</strong> (it must be after the introduction date)...");
			return get500(model);
		}
	}
	
	@GetMapping("/editComputer")
	public String getEditComputer(@RequestParam(value = "computerId", required = true) String pComputerId, Model model) {
		int computerId = Integer.parseInt(pComputerId);
		Optional<Computer> computer = computerService.findService(computerId);
		if (computer.isPresent()) {
			Computer computerToEdit = computer.get();
			ComputerDto computerDto = computerDtoMapper.map(computerToEdit);
			model.addAttribute("computer", computerDto);
			model.addAttribute("companies", companyService.listService());
			return EDIT_COMPUTER;
		} else {
			model.addAttribute("errorMessage", "Sorry, the computer <strong>" + computerId + "</strong> doesn't exist.");
			return get404(model);
		}
	}
	
	@PostMapping("/editComputer")
	public String postEditComputer(@Validated @ModelAttribute("computerDto") ComputerDto dtoComputer, BindingResult result, Model model) {
		if(result.hasErrors()) {
			StringBuilder sb = new StringBuilder("Error(s) : \n");
			for(ObjectError error : result.getAllErrors()) {
				sb.append("<strong>");
				sb.append(error.getDefaultMessage());
				sb.append("</strong>");
				sb.append("\n");
			}
			model.addAttribute("errorMessage", "An error has occured during the <strong>form validation</strong>... " + sb);
			return get500(model);
		}
		
		Computer computer = dtoComputerMapper.map(dtoComputer);

		try {
			this.computerService.updateService(computer);
			model.addAttribute("resultMessage", "The computer <strong>" + computer.getName() + "</strong> has been updated !");
			return getEditComputer(String.valueOf(computer.getId()), model);
		} catch (DaoException e) {
			model.addAttribute("errorMessage", "An <strong>SQL error</strong> has occured during the update...");
			return get500(model);
		} catch (ComputerNullNameException e) {
			model.addAttribute("errorMessage", "An error has occurred <strong>due to the name</strong> of the computer...");
			return get500(model);
		} catch (DiscontinuedButNoIntroducedException e) {
			model.addAttribute("errorMessage", "An error has occurred <strong>due to the discontinuation and introduction date</strong> of the computer...");
			return get500(model);
		} catch (DiscontinuedBeforeIntroducedException e) {
			model.addAttribute("errorMessage", "An error has occurred <strong>due to the discontinuation date</strong> (it must be after the introduction date)...");
			return get500(model);
		}
	}
	
	@GetMapping("/404")
	public String get404(Model model) {
		return NOT_FOUND_VIEW;
	}
	
	@GetMapping("/500")
	public String get500(Model model) {
		return EXCEPTION_VIEW;
	}
}