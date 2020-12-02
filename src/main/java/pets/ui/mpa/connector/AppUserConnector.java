package pets.ui.mpa.connector;

import static pets.ui.mpa.util.CommonUtils.pathVariables;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pets.ui.mpa.model.AppUserResponse;

@Component
public class AppUserConnector {

	private final RestTemplate restTemplate;
	private final String getUserByUsernameUrl;

	public AppUserConnector(@Qualifier("restTemplate") RestTemplate restTemplate, String getUserByUsernameUrl) {
		this.restTemplate = restTemplate;
		this.getUserByUsernameUrl = getUserByUsernameUrl;
	}

	public AppUserResponse getUserByUsername(String username) {
		String url = UriComponentsBuilder
				.fromHttpUrl(getUserByUsernameUrl)
				.buildAndExpand(pathVariables(username))
				.toString();

		ResponseEntity<AppUserResponse> responseEntity = restTemplate.getForEntity(url, AppUserResponse.class);

		return responseEntity.getBody();
	}
}
