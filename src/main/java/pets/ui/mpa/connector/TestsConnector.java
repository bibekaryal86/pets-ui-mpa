package pets.ui.mpa.connector;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TestsConnector {
	private final RestTemplate restTemplate;
	private final String pingTestDatabaseUrl;
	private final String pingTestServiceUrl;
	
	public TestsConnector(@Qualifier("restTemplate") RestTemplate restTemplate, 
			String pingTestDatabaseUrl, 
			String pingTestServiceUrl) {
		this.restTemplate = restTemplate;
		this.pingTestDatabaseUrl = pingTestDatabaseUrl;
		this.pingTestServiceUrl = pingTestServiceUrl;
	}
	
	public String pingDatabase() {
		try {
			return restTemplate.getForObject(pingTestDatabaseUrl, String.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	public String pingService() {
		try {
			return restTemplate.getForObject(pingTestServiceUrl, String.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
}
