package pets.ui.mpa.servlet;

import static pets.ui.mpa.util.ConstantUtils.FILTER_ACTION_GET;
import static pets.ui.mpa.util.ConstantUtils.FILTER_ACTION_SET;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static pets.ui.mpa.util.SessionHelper.getSessionAttribute;
import static pets.ui.mpa.util.SessionHelper.removeSessionAttribute;
import static pets.ui.mpa.util.SessionHelper.setSessionAttribute;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "SessionHelperServlet", urlPatterns = { "/servlet/SessionHelperServlet" })
public class SessionHelperServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SessionHelperServlet.class);

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {

		String getOrSetOrRemove = (String) httpServletRequest.getParameter("getOrSetOrReset");
		String attributeName = (String) httpServletRequest.getParameter("attributeName");
		String attributeValue = (String) httpServletRequest.getParameter("attributeValue");

		logger.info("Session Helper Servlet: {} | {} | {} | {}", getCurrentUsername(httpServletRequest),
				getOrSetOrRemove, attributeName, attributeValue);

		if (getOrSetOrRemove != null && attributeName != null) {
			if (getOrSetOrRemove.equals(FILTER_ACTION_GET)) {
				getSessionAttribute(attributeName, httpServletRequest);
			} else if (getOrSetOrRemove.equals(FILTER_ACTION_SET)) {
				setSessionAttribute(attributeName, attributeValue, httpServletRequest);
			} else {
				removeSessionAttribute(attributeName, httpServletRequest);
			}
		}
	}
}
