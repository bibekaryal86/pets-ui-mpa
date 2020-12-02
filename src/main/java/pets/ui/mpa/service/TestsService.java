package pets.ui.mpa.service;

import static pets.ui.mpa.util.ConstantUtils.GENERIC_ERROR_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_SUCCESS_MESSAGE;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import pets.ui.mpa.connector.TestsConnector;

@Service
public class TestsService {
	
	private final TestsConnector testsConnector;
	
	public TestsService(TestsConnector testsConnector) {
		this.testsConnector = testsConnector;
	}
	
	public String pingTest() {
		String response = GENERIC_ERROR_MESSAGE;
		CompletableFuture<String> pingDatabaseFuture = CompletableFuture.supplyAsync(() -> testsConnector.pingDatabase());
		CompletableFuture<String> pingServiceFuture = CompletableFuture.supplyAsync(() -> testsConnector.pingService());
		
		if (pingDatabaseFuture.join().equals("Ping Successful")) {
			if (pingServiceFuture.join().equals("Ping Successful")) {
				response = GENERIC_SUCCESS_MESSAGE;
			} else {
				response = "Service Initialization Error!";
			}
		} else {
			response = "Database Initialization Error!";
		}
		
		return response;
	}
}
