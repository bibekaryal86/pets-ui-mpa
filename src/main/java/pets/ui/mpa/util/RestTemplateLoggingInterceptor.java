package pets.ui.mpa.util;

import static pets.ui.mpa.util.LogMasker.maskDetails;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {
	private final Logger requestLogger = LoggerFactory.getLogger("spring.web.client.MessageTracing.sent");
	private final Logger responseLogger = LoggerFactory.getLogger("spring.web.client.MessageTracing.received");

	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		this.logRequest(httpRequest, body);
		long startTime = System.currentTimeMillis();
		
		ClientHttpResponse clientHttpResponse = execution.execute(httpRequest, body);
		
		long endTime = System.currentTimeMillis();
		this.logResponse(httpRequest, clientHttpResponse, endTime - startTime);
		
		return clientHttpResponse;
	}

	private void logRequest(HttpRequest httpRequest, byte[] body) {
		String maskedUri = maskDetails(httpRequest.getURI().toString());
		StringBuilder stringBuilder = new StringBuilder("Sending [").append(httpRequest.getMethod())
				.append("] Request [").append(maskedUri).append("]");
		
		if (this.hasTextBody(httpRequest.getHeaders())) {
			stringBuilder.append(" [Headers] ").append(httpRequest.getHeaders());
		}
		
		if (body.length > 0) {
			//String bodyText = new String(body, this.determineCharset(httpRequest.getHeaders()));
			//stringBuilder.append(" ::: ").append(LogMasker.maskDetails(bodyText));
			stringBuilder.append(" [Body] ").append("[EXCLUDED]");
		}
		
		String requestLog = stringBuilder.toString();
		this.requestLogger.info(requestLog);
	}

	private void logResponse(HttpRequest httpRequest, ClientHttpResponse clientHttpResponse, long durationInMs) {
		String maskedUri = maskDetails(httpRequest.getURI().toString());
		
		try {
			StringBuilder stringBuilder = new StringBuilder("Received [")
					.append(clientHttpResponse.getRawStatusCode()).append("] Response [")
					.append(maskedUri).append("]");
			HttpHeaders httpHeaders = clientHttpResponse.getHeaders();
			long contentLength = httpHeaders.getContentLength();
			
			if (contentLength != 0L) {
				if (this.hasTextBody(httpHeaders)) {
					stringBuilder.append(" [Headers] ").append(httpHeaders);
					//String bodyText = org.springframework.util.StreamUtils.copyToString(clientHttpResponse.getBody(), this.determineCharset(httpHeaders));
					//stringBuilder.append(" [::] ").append(LogMasker.maskDetails(bodyText));
					stringBuilder.append(" [Body] ").append("[EXCLUDED]");
				} else {
					MediaType mediaType = httpHeaders.getContentType();
					stringBuilder.append(" [Content Type] [ ").append(mediaType==null?"Unknown":mediaType).append(" ]");
					stringBuilder.append(" [Content Length] [ ").append(contentLength).append(" ]");
				}
			}
			
			stringBuilder.append(" [After] [ ").append(durationInMs).append(" ms]");
            String responseLog = stringBuilder.toString();
            this.responseLogger.info(responseLog);
		} catch (IOException ex) {
			this.responseLogger.error("Failed to Log Response for {} Request to {}", httpRequest.getMethod(), maskedUri, ex);
		}
	}

	private boolean hasTextBody(HttpHeaders httpHeaders) {
		MediaType mediaType = httpHeaders.getContentType();
		if (mediaType == null) {
			return false;
		} else {
			return "text".equals(mediaType.getType()) || "xml".equals(mediaType.getSubtype())
					|| "json".equals(mediaType.getSubtype());
		}
	}

	@SuppressWarnings("unused")
	private Charset determineCharset(HttpHeaders httpHeaders) {
		MediaType mediaType = httpHeaders.getContentType();
		
		if (mediaType != null) {
			try {
				Charset charset = mediaType.getCharset();
				assert charset != null;
			} catch (UnsupportedCharsetException | NullPointerException ex) {
				// do nothing
			}
		}
		
		return StandardCharsets.UTF_8;
	}
}
