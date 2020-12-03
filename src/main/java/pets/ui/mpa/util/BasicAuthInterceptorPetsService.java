package pets.ui.mpa.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import static pets.ui.mpa.util.ConstantUtils.BASIC_AUTH_USR_PETSSERVICE;
import static pets.ui.mpa.util.ConstantUtils.BASIC_AUTH_PWD_PETSSERVICE;

public class BasicAuthInterceptorPetsService implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		Map<String, String> authConfig = getAuthConfig();
        request.getHeaders().setBasicAuth(authConfig.get(BASIC_AUTH_USR_PETSSERVICE), authConfig.get(BASIC_AUTH_PWD_PETSSERVICE));
        return execution.execute(request, body);
	}
	
	 private Map<String, String> getAuthConfig() {
        Map<String, String> authConfigMap = new HashMap<>();

        if (System.getProperty(BASIC_AUTH_USR_PETSSERVICE) != null) {
            // for running locally
            authConfigMap.put(BASIC_AUTH_USR_PETSSERVICE, System.getProperty(BASIC_AUTH_USR_PETSSERVICE));
            authConfigMap.put(BASIC_AUTH_PWD_PETSSERVICE, System.getProperty(BASIC_AUTH_PWD_PETSSERVICE));
        } else if (System.getenv(BASIC_AUTH_USR_PETSSERVICE) != null) {
            // for GCP
            authConfigMap.put(BASIC_AUTH_USR_PETSSERVICE, System.getenv(BASIC_AUTH_USR_PETSSERVICE));
            authConfigMap.put(BASIC_AUTH_PWD_PETSSERVICE, System.getenv(BASIC_AUTH_PWD_PETSSERVICE));
        }

        return authConfigMap;
    }
}
