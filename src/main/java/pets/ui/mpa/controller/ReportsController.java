package pets.ui.mpa.controller;

import static pets.ui.mpa.util.ConstantUtils.YEAR_ZERO;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pets.ui.mpa.model.ReportCategories;
import pets.ui.mpa.model.ReportCategoryTypes;
import pets.ui.mpa.model.ReportsModel;
import pets.ui.mpa.service.ReportsService;

@Controller
public class ReportsController {

	private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);
	private final ReportsService reportsService;

	public ReportsController(ReportsService reportsService) {
		this.reportsService = reportsService;
	}
	
	@GetMapping(value = { "report_all.pets" })
	public String initializeReportAll(HttpServletRequest httpServletRequest, ModelMap model) throws Exception {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize All Report: {} || {}", httpServletRequest.getRemoteAddr(), username);
		
		int selectedYear = LocalDate.now().getYear();
		CompletableFuture<ReportsModel> currentBalancesReportFuture = reportsService.getCurrentBalancesReportFuture(username);
		CompletableFuture<ReportsModel> cashFlowsReportFuture = reportsService.getCashFlowsReportFuture(username, selectedYear);
		CompletableFuture<ReportsModel> categoriesReportFuture = reportsService.getCategoriesReportFuture(username, selectedYear);
		
		ReportsModel currentBalancesReport = currentBalancesReportFuture.join();
		
		if (hasText(currentBalancesReport.getErrMsg())) {
			model.addAttribute("errMsg", currentBalancesReport.getErrMsg());
		} else {
			model.addAttribute("currentBalances", currentBalancesReport.getReportCurrentBalances());
		}
		
		ReportsModel cashFlowsReport = cashFlowsReportFuture.join();
		
		if (hasText(cashFlowsReport.getErrMsg())) {
			model.addAttribute("errMsg", cashFlowsReport.getErrMsg());
		} else {
			model.addAttribute("cashFlows", cashFlowsReport.getReportCashFlows());
			
		}
		
		ReportsModel categoriesReport = categoriesReportFuture.join();
		
		if (hasText(categoriesReport.getErrMsg())) {
			model.addAttribute("errMsg", categoriesReport.getErrMsg());
		} else {
			model.addAttribute("categoryTypes", categoriesReport.getReportCategoryTypes());
		}

		model.addAttribute("selectedYear", selectedYear);
		model.addAttribute("showAllReports", true);
		return "reports";
	}

	@GetMapping(value = { "report_currentbalances.pets" })
	public String initializeCurrentBalancesReport(HttpServletRequest httpServletRequest, ModelMap model) throws Exception {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Current Balances Report: {} || {}", httpServletRequest.getRemoteAddr(), username);

		ReportsModel reportsModel = reportsService.getCurrentBalancesReport(username);
		
		if (hasText(reportsModel.getErrMsg())) {
			model.addAttribute("errMsg", reportsModel.getErrMsg());
		} else {
			model.addAttribute("currentBalances", reportsModel.getReportCurrentBalances());
		}
		
		model.addAttribute("showCurrentBalancesReport", true);

		return "reports";
	}
	
	@GetMapping(value = { "report_cashflows.pets" })
	public String initializeCashFlowsReport(HttpServletRequest httpServletRequest, ModelMap model,
			@RequestParam(value = "selectedYear", required = false) Integer selectedYear) throws Exception {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Cash Flows Report: {} || {} || {}", httpServletRequest.getRemoteAddr(), 
				username, selectedYear);
		
		int currentYear = LocalDate.now().getYear();
		if (selectedYear == null) {
			selectedYear = currentYear;
		}
		
		ReportsModel reportsModel = reportsService.getCashFlowsReport(username, selectedYear);
		
		if (hasText(reportsModel.getErrMsg())) {
			model.addAttribute("errMsg", reportsModel.getErrMsg());
		} else {
			model.addAttribute("showYearSelection", true);
			model.addAttribute("showClearSelection", currentYear != selectedYear.intValue());
			model.addAttribute("selectedYear", selectedYear);
			model.addAttribute("years", yearsDropdown(currentYear));
			
			model.addAttribute("cashFlows", reportsModel.getReportCashFlows());
		}
		
		model.addAttribute("showCashFlowsReport", true);

		return "reports";
	}
	
	@GetMapping(value = { "report_categories.pets" })
	public String initializeCategoriesReport(HttpServletRequest httpServletRequest, ModelMap model,
			@RequestParam(value = "selectedYear", required = false) Integer selectedYear) throws Exception {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Categories Report: {} || {} || {}", httpServletRequest.getRemoteAddr(), 
				username, selectedYear);
		
		int currentYear = LocalDate.now().getYear();
		if (selectedYear == null) {
			selectedYear = currentYear;
		}
		ReportsModel reportsModel = reportsService.getCategoriesReport(username, selectedYear);
		
		if (hasText(reportsModel.getErrMsg())) {
			model.addAttribute("errMsg", reportsModel.getErrMsg());
		} else {
			model.addAttribute("showYearSelection", true);
			model.addAttribute("showClearSelection", currentYear != selectedYear.intValue());
			model.addAttribute("selectedYear", selectedYear);
			model.addAttribute("years", yearsDropdown(currentYear));
			
			model.addAttribute("categoryTypes", reportsModel.getReportCategoryTypes());
			model.addAttribute("categories", getReportCategories(reportsModel));
		}
		
		model.addAttribute("showCategoriesReport", true);

		return "reports";
	}
	
	private List<Integer> yearsDropdown(int currentYear) {
		List<Integer> years = new ArrayList<>();
		
		int startYear = YEAR_ZERO.intValue();
		
		while (startYear <= currentYear) {
			years.add(startYear);
			startYear++;
		}
		
		return years;
	}
	
	private List<ReportCategories> getReportCategories(ReportsModel reportsModel) {
		List<List<ReportCategories>> reportCategoriesList = reportsModel.getReportCategoryTypes()
				.stream()
				.map(ReportCategoryTypes::getReportCategories)
				.collect(toList());
		List<ReportCategories> reportCategories = reportCategoriesList.stream()
				.flatMap(List::stream)
				.collect(toList());
		
		reportCategories.sort(comparing(reportCategory -> reportCategory.getRefCategory().getDescription()));
		
		return reportCategories;
	}
}
