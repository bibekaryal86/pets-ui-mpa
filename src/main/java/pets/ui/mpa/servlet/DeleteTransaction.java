package pets.ui.mpa.servlet;

import static pets.ui.mpa.util.ConstantUtils.GENERIC_ERROR_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_SUCCESS_MESSAGE;
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

import pets.ui.mpa.model.TransactionModel;
import pets.ui.mpa.service.TransactionService;

@WebServlet(name = "DeleteTransaction", urlPatterns = { "/servlet/DeleteTransaction" })
public class DeleteTransaction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DeleteTransaction.class);

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		String resp = GENERIC_ERROR_MESSAGE;
		String username = getCurrentUsername(httpServletRequest);

		String transactionId = (String) httpServletRequest.getParameter("transactionId");
		String transactionDate = (String) httpServletRequest.getParameter("transactionDate");

		logger.info("Delete Transaction: {} | {} | {}", username, transactionId, transactionDate);
		
		TransactionService transactionService = getApplicationContext().getBean(TransactionService.class);
		TransactionModel transactionModel = transactionService.deleteTransaction(username, transactionId);
		
		if (hasText(transactionModel.getErrMsg())) {
			resp = transactionModel.getErrMsg();
		} else {
			resp = GENERIC_SUCCESS_MESSAGE;
		}

		httpServletResponse.getWriter().write(resp);
	}
}
