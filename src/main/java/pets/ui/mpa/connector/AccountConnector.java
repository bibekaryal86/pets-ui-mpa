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

import pets.ui.mpa.model.AccountFilters;
import pets.ui.mpa.model.AccountRequest;
import pets.ui.mpa.model.AccountResponse;

@Component
public class AccountConnector {

	private final RestTemplate restTemplate;
	private final String getAccountByIdUrl;
	private final String getAccountsByUsernameUrl;
	private final String editAccountUrl;

	public AccountConnector(@Qualifier("restTemplate") RestTemplate restTemplate, String getAccountByIdUrl,
			String getAccountsByUsernameUrl, String editAccountUrl) {
		this.restTemplate = restTemplate;
		this.getAccountByIdUrl = getAccountByIdUrl;
		this.getAccountsByUsernameUrl = getAccountsByUsernameUrl;
		this.editAccountUrl = editAccountUrl;
	}

	public AccountResponse getAccountById(String username, String id) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getAccountByIdUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<AccountResponse> responseEntity = restTemplate.getForEntity(url, AccountResponse.class);

		return responseEntity.getBody();
	}

	public AccountResponse getAccountsByUsername(String username, AccountFilters accountFilters) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getAccountsByUsernameUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(url, POST,
				new HttpEntity<>(accountFilters, null), AccountResponse.class);

		return responseEntity.getBody();
	}

	public AccountResponse saveNewAccount(String username, AccountRequest accountRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editAccountUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		HttpEntity<AccountRequest> requestEntity = new HttpEntity<>(accountRequest);

		ResponseEntity<AccountResponse> responseEntity = restTemplate.postForEntity(url, requestEntity,
				AccountResponse.class);

		return responseEntity.getBody();
	}

	public AccountResponse updateAccount(String username, String id, AccountRequest accountRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editAccountUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();

		HttpEntity<AccountRequest> requestEntity = new HttpEntity<>(accountRequest);

		ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(url, PUT, requestEntity,
				AccountResponse.class);

		return responseEntity.getBody();
	}

	public AccountResponse deleteAccount(String username, String id) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editAccountUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<AccountResponse> responseEntity = restTemplate.exchange(url, DELETE, null,
				AccountResponse.class);

		return responseEntity.getBody();
	}
}
