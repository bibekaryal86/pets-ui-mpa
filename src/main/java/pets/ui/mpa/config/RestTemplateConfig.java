package pets.ui.mpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import pets.ui.mpa.util.RestTemplateLoggingInterceptor;

@Configuration
public class RestTemplateConfig {
	
	@Bean("restTemplate")
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
		restTemplate.getInterceptors().add(new RestTemplateLoggingInterceptor());
		return restTemplate;
	}
}
