package pets.ui.mpa.servlet;

import static pets.ui.mpa.util.ConstantUtils.DATE_FORMATTER_PATTERN;
import static pets.ui.mpa.util.ConstantUtils.FILTER_ACTION_RESET;
import static pets.ui.mpa.util.ConstantUtils.FILTER_ACTION_SET;
import static pets.ui.mpa.util.ConstantUtils.FILTER_TRANSACTIONS_OBJECT_NAME;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pets.ui.mpa.model.TransactionFilters;

@WebServlet(name = "FilterTransactions", urlPatterns = { "/servlet/FilterTransactions" })
public class FilterTransactions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FilterTransactions.class);

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		doPost(httpServletRequest, httpServletResponse);
	}

	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		String username = getCurrentUsername(httpServletRequest);
		String action = (String) httpServletRequest.getParameter("setOrReset");
		String filterTransactionType = (String) httpServletRequest.getParameter("fTxnType");
		String filterCategoryType = (String) httpServletRequest.getParameter("fCatType");
		String filterCategory = (String) httpServletRequest.getParameter("fCat");
		String filterUserAccount = (String) httpServletRequest.getParameter("fAcc");
		String filterTransactionDateFrom = (String) httpServletRequest.getParameter("fTxnDateFrom");
		String filterTransactionDateTo = (String) httpServletRequest.getParameter("fTxnDateTo");
		String filterTransactionAmountFrom = (String) httpServletRequest.getParameter("fTxnAmtFrom");
		String filterTransactionAmountTo = (String) httpServletRequest.getParameter("fTxnAmtTo");
		String filterNecessaryTransaction = (String) httpServletRequest.getParameter("fNecTxn");
		String filterRegularTransaction = (String) httpServletRequest.getParameter("fRegTxn");
		String filterTransactionMerchant = (String) httpServletRequest.getParameter("fTxnMerchant");

		logger.info("Filter Transactions: {} | {} | {} | {} | {} | {} | {} | {} | {} | {} | {} | {} | {}", 
				username, action, filterTransactionType, filterCategoryType, filterCategory, filterUserAccount, 
				filterTransactionDateFrom, filterTransactionDateTo, filterTransactionAmountFrom, filterTransactionAmountTo, 
				filterNecessaryTransaction, filterRegularTransaction, filterTransactionMerchant);

		try {
			HttpSession httpSession = httpServletRequest.getSession();
			
			if (action.equals(FILTER_ACTION_RESET)) {
				httpSession.removeAttribute(FILTER_TRANSACTIONS_OBJECT_NAME);
			} else if (action.equals(FILTER_ACTION_SET)) {
				Boolean necessary = null;
				if (hasText(filterNecessaryTransaction)) {
					String[] tempArray = filterNecessaryTransaction.split("_");
					if (tempArray[0].equals(String.valueOf(true))) {
						necessary = true;
					} else if (tempArray[1].equals(String.valueOf(true))) {
						necessary = false;
					}
				}
				
				Boolean regular = null;
				if (hasText(filterRegularTransaction)) {
					String[] tempArray = filterRegularTransaction.split("_");
					if (tempArray[0].equals(String.valueOf(true))) {
						regular = true;
					} else if (tempArray[1].equals(String.valueOf(true))) {
						regular = false;
					}
				}
				
				BigDecimal amountFrom = null;
				try {
					amountFrom = new BigDecimal(filterTransactionAmountFrom);
				} catch (Exception ex) {
					// do nothing
				}
				
				BigDecimal amountTo = null;
				try {
					amountTo = new BigDecimal(filterTransactionAmountTo);
				} catch (Exception ex) {
					// do nothing
				}
				
				String dateFrom = null;
				try {
					dateFrom = parse(filterTransactionDateFrom, ofPattern(DATE_FORMATTER_PATTERN)).toString();
				} catch (Exception ex) {
					// do nothing
				}
				
				String dateTo = null;
				try {
					dateTo = parse(filterTransactionDateTo, ofPattern(DATE_FORMATTER_PATTERN)).toString();
				} catch (Exception ex) {
					// do nothing
				}
				
				TransactionFilters transactionFilters = TransactionFilters.builder()
						.accountId(filterUserAccount)
						.amountFrom(amountFrom)
						.amountTo(amountTo)
						.categoryId(filterCategory)
						.categoryTypeId(filterCategoryType)
						.dateFrom(dateFrom)
						.dateTo(dateTo)
						.merchantId(filterTransactionMerchant)
						.necessary(necessary)
						.regular(regular)
						.transactionTypeId(filterTransactionType)
						.build();

				httpSession.setAttribute(FILTER_TRANSACTIONS_OBJECT_NAME, transactionFilters);
			}
		} catch (Exception ex) {
			logger.error("Filter Transactions: {} | {} | {} | {} | {} | {} | {} | {} | {} | {} | {} | {}", 
					username, action, filterCategoryType, filterCategory, filterUserAccount, filterTransactionDateFrom,
					filterTransactionDateTo, filterTransactionAmountFrom, filterTransactionAmountTo, filterNecessaryTransaction,
					filterRegularTransaction, filterTransactionMerchant, ex);
		}
	}
}
