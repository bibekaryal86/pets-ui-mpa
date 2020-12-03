package pets.ui.mpa.config;

import static java.util.Arrays.asList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import pets.ui.mpa.util.BasicAuthInterceptorPetsService;
import pets.ui.mpa.util.RestTemplateLoggingInterceptor;

@Configuration
public class RestTemplateConfig {
	
	@Bean("restTemplate")
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(clientHttpRequestFactory()));
		restTemplate.getInterceptors().add(new RestTemplateLoggingInterceptor());
		return restTemplate;
	}
	
	@Bean("restTemplatePetsService")
	public RestTemplate restTemplatePetsService() {
		RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(clientHttpRequestFactory()));
		restTemplate.getInterceptors().addAll(asList(new RestTemplateLoggingInterceptor(), new BasicAuthInterceptorPetsService()));
		return restTemplate;
	}
	
	private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(15000);
        factory.setConnectTimeout(15000);
        return factory;
    }
}
