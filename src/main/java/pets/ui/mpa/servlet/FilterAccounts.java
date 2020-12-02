package pets.ui.mpa.servlet;

import static pets.ui.mpa.util.ConstantUtils.FILTER_ACCOUNTS_OBJECT_NAME;
import static pets.ui.mpa.util.ConstantUtils.FILTER_ACTION_RESET;
import static pets.ui.mpa.util.ConstantUtils.FILTER_ACTION_SET;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pets.ui.mpa.model.AccountFilters;

@WebServlet(name = "FilterAccounts", urlPatterns = { "/servlet/FilterAccounts" })
public class FilterAccounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FilterAccounts.class);

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		String username = getCurrentUsername(httpServletRequest);
		String action = (String) httpServletRequest.getParameter("setOrReset");
		String refAccountTypeId = (String) httpServletRequest.getParameter("fAccType");
		String refBankId = (String) httpServletRequest.getParameter("fBank");
		String status = (String) httpServletRequest.getParameter("fAccSts");

		logger.info("Filter Accounts: {} | {} | {} | {} | {}", username, action, refAccountTypeId, refBankId, status);

		try {
			HttpSession httpSession = httpServletRequest.getSession();

			if (action.equals(FILTER_ACTION_SET)) {
				AccountFilters accountFilters = AccountFilters.builder()
						.accountTypeId(refAccountTypeId)
						.bankId(refBankId)
						.status(status)
						.build();

				httpSession.setAttribute(FILTER_ACCOUNTS_OBJECT_NAME, accountFilters);
			} else if (action.equals(FILTER_ACTION_RESET)) {
				httpSession.removeAttribute(FILTER_ACCOUNTS_OBJECT_NAME);
			}
		} catch (Exception ex) {
			logger.error("Filter Accounts: {} | {} | {} | {} | {}", username, action, refAccountTypeId, refBankId,
					status, ex);
		}
	}
}
