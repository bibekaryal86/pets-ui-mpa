package pets.ui.mpa.servlet;

import static pets.ui.mpa.util.ConstantUtils.GENERIC_ERROR_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_FIRST_OPTION;
import static pets.ui.mpa.util.ContextProvider.getApplicationContext;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
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

@WebServlet(name = "LookupRefCategoriesByType", urlPatterns = { "/servlet/LookupRefCategoriesByType" })
public class LookupRefCategoriesByType extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(LookupRefCategoriesByType.class);

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		StringBuffer resp = new StringBuffer("");
		String username = getCurrentUsername(httpServletRequest);
		String categoryTypeId = (String) httpServletRequest.getParameter("categoryTypeId");
		String usedInTxnsOnlyStr = (String) httpServletRequest.getParameter("usedInTxnsOnly");
		boolean usedInTxnsOnly = Boolean.parseBoolean(usedInTxnsOnlyStr);
		
		logger.info("Lookup Ref Categories by Type: {} | {} | {}", username, categoryTypeId, usedInTxnsOnly);
		
		try {
			RefTablesService refTablesService = getApplicationContext().getBean(RefTablesService.class);
			RefTablesModel refTablesModel = refTablesService.getRefCategories(username, categoryTypeId, usedInTxnsOnly);
			
			if (hasText(refTablesModel.getErrMsg())) {
				resp.append("<option value=\" \">" + GENERIC_ERROR_MESSAGE + "</option>");
			} else {
				resp.append("<option value=\" \">" + GENERIC_FIRST_OPTION + "</option>");
				refTablesModel.getRefCategories()
				.forEach(refCategory -> 
				resp.append("<option value=\"" + refCategory.getId() + "\">" + refCategory.getDescription() + "</option>"));
			}
		} catch (Exception ex) {
			logger.info("Lookup Ref Categories by Type: {} | {}", username, categoryTypeId, ex);
		}

		httpServletResponse.getWriter().write(resp.toString());
	}
}
