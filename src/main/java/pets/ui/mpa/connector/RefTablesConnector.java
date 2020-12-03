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

import pets.ui.mpa.model.RefAccountTypeRequest;
import pets.ui.mpa.model.RefAccountTypeResponse;
import pets.ui.mpa.model.RefBankRequest;
import pets.ui.mpa.model.RefBankResponse;
import pets.ui.mpa.model.RefCategoryFilters;
import pets.ui.mpa.model.RefCategoryRequest;
import pets.ui.mpa.model.RefCategoryResponse;
import pets.ui.mpa.model.RefCategoryTypeRequest;
import pets.ui.mpa.model.RefCategoryTypeResponse;
import pets.ui.mpa.model.RefMerchantFilters;
import pets.ui.mpa.model.RefMerchantRequest;
import pets.ui.mpa.model.RefMerchantResponse;
import pets.ui.mpa.model.RefTransactionTypeRequest;
import pets.ui.mpa.model.RefTransactionTypeResponse;

@Component
public class RefTablesConnector {

	private final RestTemplate restTemplate;
	private final String getRefAccountTypesUrl;
	private final String getRefBanksUrl;
	private final String getRefCategoriesUrl;
	private final String getRefCategoryTypesUrl;
	private final String getRefMerchantByIdUrl;
	private final String getRefMerchantsByUsernameUrl;
	private final String editMerchantUrl;
	private final String getRefTransactionTypesUrl;
	private final String saveNewCategoryUrl;

	public RefTablesConnector(@Qualifier("restTemplatePetsService") RestTemplate restTemplate, String getRefAccountTypesUrl,
			String getRefBanksUrl, String getRefCategoriesUrl, String getRefCategoryTypesUrl,
			String getRefMerchantByIdUrl, String getRefMerchantsByUsernameUrl, String editMerchantUrl,
			String getRefTransactionTypesUrl, String saveNewCategoryUrl) {
		this.restTemplate = restTemplate;
		this.getRefAccountTypesUrl = getRefAccountTypesUrl;
		this.getRefBanksUrl = getRefBanksUrl;
		this.getRefCategoriesUrl = getRefCategoriesUrl;
		this.getRefCategoryTypesUrl = getRefCategoryTypesUrl;
		this.getRefMerchantByIdUrl = getRefMerchantByIdUrl;
		this.getRefMerchantsByUsernameUrl = getRefMerchantsByUsernameUrl;
		this.editMerchantUrl = editMerchantUrl;
		this.getRefTransactionTypesUrl = getRefTransactionTypesUrl;
		this.saveNewCategoryUrl = saveNewCategoryUrl;
	}

	public RefAccountTypeResponse getRefAccountTypes(String username) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefAccountTypesUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefAccountTypeResponse> responseEntity = restTemplate.getForEntity(url,
				RefAccountTypeResponse.class);

		return responseEntity.getBody();
	}
	
	/**
	 * Used for Import only
	 */
	public RefAccountTypeResponse saveRefAccountType(String username, RefAccountTypeRequest refAccountTypeRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefAccountTypesUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefAccountTypeResponse> responseEntity = restTemplate.postForEntity(url, 
				new HttpEntity<>(refAccountTypeRequest, null), RefAccountTypeResponse.class);

		return responseEntity.getBody();
	}
	
	public RefBankResponse getRefBanks(String username) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefBanksUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefBankResponse> responseEntity = restTemplate.getForEntity(url, RefBankResponse.class);

		return responseEntity.getBody();
	}
	
	/**
	 * Used for Import only
	 */
	public RefBankResponse saveRefBank(String username, RefBankRequest refBankRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefBanksUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefBankResponse> responseEntity = restTemplate.postForEntity(url, 
				new HttpEntity<>(refBankRequest, null), RefBankResponse.class);

		return responseEntity.getBody();
	}

	public RefCategoryResponse getRefCategories(String username, RefCategoryFilters refCategoryFilters) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefCategoriesUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefCategoryResponse> responseEntity = restTemplate.exchange(url, POST,
				new HttpEntity<>(refCategoryFilters, null), RefCategoryResponse.class);

		return responseEntity.getBody();
	}
	
	/**
	 * Used for Import only
	 */
	public RefCategoryResponse saveRefCategory(String username, RefCategoryRequest refCategoryRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(saveNewCategoryUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefCategoryResponse> responseEntity = restTemplate.postForEntity(url, 
				new HttpEntity<>(refCategoryRequest, null), RefCategoryResponse.class);

		return responseEntity.getBody();
	}

	public RefCategoryTypeResponse getRefCategoryTypes(String username, String usedInTxnsOnly) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefCategoryTypesUrl)
				.queryParam("usedInTxnsOnly", usedInTxnsOnly)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefCategoryTypeResponse> responseEntity = restTemplate.getForEntity(url,
				RefCategoryTypeResponse.class);

		return responseEntity.getBody();
	}
	
	/**
	 * Used for Import only
	 */
	public RefCategoryTypeResponse saveRefCategoryType(String username, RefCategoryTypeRequest refCategoryTypeRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefCategoryTypesUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefCategoryTypeResponse> responseEntity = restTemplate.postForEntity(url, 
				new HttpEntity<>(refCategoryTypeRequest, null), RefCategoryTypeResponse.class);

		return responseEntity.getBody();
	}
	
	public RefMerchantResponse getRefMerchantById(String username, String id) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefMerchantByIdUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();
		
		ResponseEntity<RefMerchantResponse> responseEntity = restTemplate.getForEntity(url, RefMerchantResponse.class);
		
		return responseEntity.getBody();
	}

	public RefMerchantResponse getRefMerchants(String username, RefMerchantFilters refMerchantFilters) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefMerchantsByUsernameUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefMerchantResponse> responseEntity = restTemplate.exchange(url, POST,
				new HttpEntity<>(refMerchantFilters, null), RefMerchantResponse.class);

		return responseEntity.getBody();
	}
	
	/**
	 * Used for Import only
	 */
	public RefMerchantResponse saveNewRefMerchant(String username, RefMerchantRequest refMerchantRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editMerchantUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		HttpEntity<RefMerchantRequest> requestEntity = new HttpEntity<>(refMerchantRequest);

		ResponseEntity<RefMerchantResponse> responseEntity = restTemplate.exchange(url, POST, requestEntity,
				RefMerchantResponse.class);

		return responseEntity.getBody();
	}
	
	public RefMerchantResponse updateRefMerchant(String username, String id, RefMerchantRequest refMerchantRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editMerchantUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();

		HttpEntity<RefMerchantRequest> requestEntity = new HttpEntity<>(refMerchantRequest);

		ResponseEntity<RefMerchantResponse> responseEntity = restTemplate.exchange(url, PUT, requestEntity,
				RefMerchantResponse.class);

		return responseEntity.getBody();
	}

	public RefMerchantResponse deleteRefMerchant(String username, String id) {
		String url = UriComponentsBuilder
				.fromHttpUrl(editMerchantUrl)
				.queryParam("id", id)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefMerchantResponse> responseEntity = restTemplate.exchange(url, DELETE, null,
				RefMerchantResponse.class);

		return responseEntity.getBody();
	}

	public RefTransactionTypeResponse getRefTransactionTypes(String username) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefTransactionTypesUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefTransactionTypeResponse> responseEntity = restTemplate.getForEntity(url,
				RefTransactionTypeResponse.class);

		return responseEntity.getBody();
	}
	
	/**
	 * Used for Import only
	 */
	public RefTransactionTypeResponse saveRefTransactionType(String username, RefTransactionTypeRequest refTransactionTypeRequest) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getRefTransactionTypesUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<RefTransactionTypeResponse> responseEntity = restTemplate.postForEntity(url, 
				new HttpEntity<>(refTransactionTypeRequest, null), RefTransactionTypeResponse.class);

		return responseEntity.getBody();
	}
}
