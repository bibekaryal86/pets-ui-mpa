package pets.ui.mpa.servlet;

import static pets.ui.mpa.util.ConstantUtils.GENERIC_ERROR_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_SUCCESS_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_DELETE;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_UPDATE;
import static pets.ui.mpa.util.ContextProvider.getApplicationContext;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static java.lang.Character.isLetterOrDigit;
import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pets.ui.mpa.model.RefTablesModel;
import pets.ui.mpa.service.RefTablesService;

@WebServlet(name = "RefMerchantServlet", urlPatterns = { "/servlet/RefMerchantServlet" })
public class RefMerchantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(RefMerchantServlet.class);

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		String resp = GENERIC_ERROR_MESSAGE;
		String username = getCurrentUsername(httpServletRequest);

		String merchantAction = (String) httpServletRequest.getParameter("merchantAction");
		String merchantId = (String) httpServletRequest.getParameter("merchantId");
		String merchantNameOld = (String) httpServletRequest.getParameter("merchantNameOld");
		String merchantNameNew = (String) httpServletRequest.getParameter("merchantNameNew");

		logger.info("Ref Merchant Servlet: {} | {} | {} | {} | {}", username, merchantAction, merchantId,
				merchantNameOld, merchantNameNew);

		RefTablesModel refTablesModel;
		RefTablesService refTablesService = getApplicationContext().getBean(RefTablesService.class);

		if (merchantAction.equals(USER_ACTION_UPDATE)) {
			if (hasText(merchantNameNew)) {
				if (isLetterOrDigit(merchantNameNew.charAt(0))) {
					refTablesModel = refTablesService.updateRefMerchant(username, merchantId, merchantNameNew);

					if (hasText(refTablesModel.getErrMsg())) {
						resp = refTablesModel.getErrMsg();
					} else {
						resp = GENERIC_SUCCESS_MESSAGE;
					}
				} else {
					resp = "Merchant Name Should Begin With Alpha-Numeric Character!!!";
				}
			} else {
				resp = "Merchant Name Cannot be Empty!!!";
			}
		} else if (merchantAction.equals(USER_ACTION_DELETE)) {
			refTablesModel = refTablesService.deleteRefMerchant(username, merchantId);

			if (hasText(refTablesModel.getErrMsg())) {
				resp = refTablesModel.getErrMsg();
			} else {
				resp = GENERIC_SUCCESS_MESSAGE;
			}
		}

		httpServletResponse.getWriter().write(resp);
	}
}
