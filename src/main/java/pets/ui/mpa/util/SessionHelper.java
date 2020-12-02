package pets.ui.mpa.util;

import static java.time.LocalDate.now;
import static org.springframework.util.StringUtils.hasText;

import java.security.Principal;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

public class SessionHelper {
	private static ResourceBundle rb = ResourceBundle.getBundle("application");
	private static String version = rb.getString("pets.version");

	public static void setFooterDisplay(HttpServletRequest httpServletRequest) {
		httpServletRequest.getSession().setAttribute("petsVersion", version);
		httpServletRequest.getSession().setAttribute("petsCopyright",
				"Copyright (c). " + "" + now().getYear() + " ABibek. All rights reserved.");
	}

	public static String getCurrentUsername(HttpServletRequest httpServletRequest) {
		Principal userPrincipal = httpServletRequest.getUserPrincipal();
		if (userPrincipal == null) {
			return "ERROR_NULL_USER_PRINCIPAL";
		} else {
			if (hasText(userPrincipal.getName())) {
				return userPrincipal.getName();
			} else {
				return "ERROR_NULL_USER_NAME";
			}
		}
	}

	public static Object getSessionAttribute(String attributeName, HttpServletRequest httpServletRequest) {
		return httpServletRequest.getSession().getAttribute(attributeName);
	}

	public static void setSessionAttribute(String attributeName, Object attributeValue,
			HttpServletRequest httpServletRequest) {
		httpServletRequest.getSession().setAttribute(attributeName, attributeValue);
	}

	public static void removeSessionAttribute(String attributeName, HttpServletRequest httpServletRequest) {
		httpServletRequest.getSession().removeAttribute(attributeName);
	}
}
