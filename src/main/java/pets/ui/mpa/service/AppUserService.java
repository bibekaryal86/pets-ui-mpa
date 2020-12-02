package pets.ui.mpa.service;

import static pets.ui.mpa.util.CommonUtils.objectMapper;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import pets.ui.mpa.connector.AppUserConnector;
import pets.ui.mpa.model.AppUser;
import pets.ui.mpa.model.AppUserResponse;
import pets.ui.mpa.model.Status;;

@Service
public class AppUserService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

	private final AppUserConnector userConnector;

	public AppUserService(AppUserConnector userConnector) {
		this.userConnector = userConnector;
	}

	public AppUser getUserByUsername(String username) {
		try {
			return userConnector.getUserByUsername(username).getUsers().get(0);
		} catch (Exception ex) {
			logUserRetrieveError(username, ex);
			return null;
		}
	}
	
	public CompletableFuture<AppUser> getUserByUsernameFuture(String username) {
		return CompletableFuture.supplyAsync(() -> getUserByUsername(username));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = getUserByUsername(username);
		UserBuilder userBuilder = null;

		if (user == null) {
			throw new UsernameNotFoundException("User Not Found! Please Try Again!!!");
		} else {
			userBuilder = User.withUsername(username);
			userBuilder.password(user.getPassword());
			userBuilder.authorities("APP_USER");
		}

		return userBuilder.build();
	}

	private void logUserRetrieveError(String username, Exception ex) {
		if (ex instanceof HttpStatusCodeException) {
			try {
				AppUserResponse appUserResponse = objectMapper()
						.readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(), AppUserResponse.class);
				Status status = appUserResponse.getStatus();

				logger.error("Exception in Get User By Username: {} | {}", username, status, ex);
			} catch (Exception ex1) {
				logger.error("Exception Reading Http Exception in Get User By Username: {}", username, ex1);
			}
		} else {
			logger.error("Exception in Get User By Username: {}", username, ex);
		}
	}
}
