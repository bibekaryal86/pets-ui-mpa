package pets.ui.mpa.connector;

import static pets.ui.mpa.util.CommonUtils.pathVariables;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pets.ui.mpa.model.ReportsResponse;

@Component
public class ReportsConnector {
	
	private final RestTemplate restTemplate;
	private final String getCurrentBalancesReportUrl;
	private final String getCashFlowsReportUrl;
	private final String getCategoriesReportUrl;
	
	public ReportsConnector(@Qualifier("restTemplate") RestTemplate restTemplate, 
			String getCurrentBalancesReportUrl, String getCashFlowsReportUrl,
			String getCategoriesReportUrl) {
		this.restTemplate = restTemplate;
		this.getCurrentBalancesReportUrl = getCurrentBalancesReportUrl;
		this.getCashFlowsReportUrl = getCashFlowsReportUrl;
		this.getCategoriesReportUrl = getCategoriesReportUrl;
	}
	
	public ReportsResponse getCurrentBalancesReport(String username) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getCurrentBalancesReportUrl)
				.buildAndExpand(pathVariables(username))
				.toString();
		
		ResponseEntity<ReportsResponse> responseEntity = restTemplate.getForEntity(url, ReportsResponse.class);
		
		return responseEntity.getBody();
	}
	
	public ReportsResponse getCashFlowsReport(String username, int selectedYear) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getCashFlowsReportUrl)
				.queryParam("selectedyear", selectedYear)
				.buildAndExpand(pathVariables(username))
				.toString();
		
		ResponseEntity<ReportsResponse> responseEntity = restTemplate.getForEntity(url, ReportsResponse.class);
		
		return responseEntity.getBody();
	}
	
	public ReportsResponse getCategoriesReport(String username, int selectedYear) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getCategoriesReportUrl)
				.queryParam("selectedyear", selectedYear)
				.buildAndExpand(pathVariables(username))
				.toString();
		
		ResponseEntity<ReportsResponse> responseEntity = restTemplate.getForEntity(url, ReportsResponse.class);
		
		return responseEntity.getBody();
	}
}
