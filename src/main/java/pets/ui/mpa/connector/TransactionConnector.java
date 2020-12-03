package pets.ui.mpa.connector;

import static pets.ui.mpa.util.CommonUtils.pathVariables;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pets.ui.mpa.model.TransactionFilters;
import pets.ui.mpa.model.TransactionRequest;
import pets.ui.mpa.model.TransactionResponse;

@Component
public class TransactionConnector {

	private final RestTemplate restTemplate;
	private final String getTransactionByIdUrl;
	private final String getTransactionsByUsernameUrl;
	private final String editTransactionUrl;

	public TransactionConnector(@Qualifier("restTemplatePetsService") RestTemplate restTemplate, String getTransactionByIdUrl,
			String getTransactionsByUsernameUrl, String editTransactionUrl) {
		this.restTemplate = restTemplate;
		this.getTransactionByIdUrl = getTransactionByIdUrl;
		this.getTransactionsByUsernameUrl = getTransactionsByUsernameUrl;
		this.editTransactionUrl = editTransactionUrl;
	}

	public TransactionResponse getTransactionById(String username, String id) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getTransactionByIdUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<TransactionResponse> responseEntity = restTemplate.getForEntity(url, TransactionResponse.class);

		return responseEntity.getBody();
	}

	public TransactionResponse getTransactionsByUsername(String username, TransactionFilters transactionFilters) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getTransactionsByUsernameUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<TransactionResponse> responseEntity = restTemplate.exchange(url, POST,
				new HttpEntity<>(transactionFilters, null), TransactionResponse.class);

		return responseEntity.getBody();
	}

	public TransactionResponse saveNewTransaction(String username, TransactionRequest transactionRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editTransactionUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		HttpEntity<TransactionRequest> requestEntity = new HttpEntity<>(transactionRequest);

		ResponseEntity<TransactionResponse> responseEntity = restTemplate.exchange(url, POST, requestEntity,
				TransactionResponse.class);

		return responseEntity.getBody();
	}

	public TransactionResponse updateTransaction(String username, String id, TransactionRequest transactionRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editTransactionUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username)).toString();

		HttpEntity<TransactionRequest> requestEntity = new HttpEntity<>(transactionRequest);

		ResponseEntity<TransactionResponse> responseEntity = restTemplate.exchange(url, PUT, requestEntity,
				TransactionResponse.class);

		return responseEntity.getBody();
	}

	public TransactionResponse deleteTransaction(String username, String id) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editTransactionUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<TransactionResponse> responseEntity = restTemplate.exchange(url, DELETE, null,
				TransactionResponse.class);

		return responseEntity.getBody();
	}
}
