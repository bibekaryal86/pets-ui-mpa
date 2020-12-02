package pets.ui.mpa.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	private final int SESSION_TIMEOUT = 15 * 60; // 15 minutes * 60 seconds

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		httpSessionEvent.getSession().setMaxInactiveInterval(SESSION_TIMEOUT);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		// nothing to do here
	}
}
