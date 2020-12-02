package pets.ui.mpa.service;

import static pets.ui.mpa.util.CommonUtils.objectMapper;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import pets.ui.mpa.connector.ReportsConnector;
import pets.ui.mpa.model.ReportsModel;
import pets.ui.mpa.model.ReportsResponse;

@Service
public class ReportsService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportsService.class);
	private final ReportsConnector reportsConnector;
	
	public ReportsService(ReportsConnector reportsConnector) {
		this.reportsConnector = reportsConnector;
	}
	
	public ReportsModel getCurrentBalancesReport(String username) {
		try {
			return ReportsModel.builder()
					.reportCurrentBalances(reportsConnector.getCurrentBalancesReport(username).getReportCurrentBalances())
					.build();
		} catch (Exception ex) {
			return ReportsModel.builder()
					.errMsg(errMsg(username, ex, "Current Balances Report"))
					.build();
		}
	}
	
	public CompletableFuture<ReportsModel> getCurrentBalancesReportFuture(String username) {
		return CompletableFuture.supplyAsync(() -> getCurrentBalancesReport(username));
	}
	
	public ReportsModel getCashFlowsReport(String username, int selectedYear) {
		try {
			return ReportsModel.builder()
					.selectedYear(String.valueOf(selectedYear))
					.reportCashFlows(reportsConnector.getCashFlowsReport(username, selectedYear).getReportCashFlows())
					.build();
		} catch (Exception ex) {
			return ReportsModel.builder()
					.errMsg(errMsg(username, ex, "Cash Flows Report"))
					.build();
		}
	}
	
	public CompletableFuture<ReportsModel> getCashFlowsReportFuture(String username, int selectedYear) {
		return CompletableFuture.supplyAsync(() -> getCashFlowsReport(username, selectedYear));
	}
	
	public ReportsModel getCategoriesReport(String username, int selectedYear) {
		try {
			return ReportsModel.builder()
					.selectedYear(String.valueOf(selectedYear))
					.reportCategoryTypes(reportsConnector.getCategoriesReport(username, selectedYear).getReportCategoryTypes())
					.build();
		} catch (Exception ex) {
			return ReportsModel.builder()
					.errMsg(errMsg(username, ex, "Categories Report"))
					.build();
		}
	}
	
	public CompletableFuture<ReportsModel> getCategoriesReportFuture(String username, int selectedYear) {
		return CompletableFuture.supplyAsync(() -> getCategoriesReport(username, selectedYear));
	}
	
	private String errMsg(String username, Exception ex, String methodName) {
		if (ex instanceof HttpStatusCodeException) {
			try {
				logger.error("HttpStatusCodeException in {}: {}", methodName, username, ex);
				return objectMapper()
						.readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(), ReportsResponse.class)
						.getStatus().getErrMsg();
			} catch (Exception ex1) {
				logger.error("Exception Reading Http Exception in {}: {}", methodName, username, ex1);
				return String.format("Error in %s! Please Try Again!!!", methodName);
			}
		} else {
			logger.error("Exception in {}: {}", methodName, username, ex);
			return String.format("Error in %s! Please Try Again!!!", methodName);
		}
	}

}
