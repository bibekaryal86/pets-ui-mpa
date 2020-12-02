package pets.ui.mpa.service;

import static pets.ui.mpa.util.CommonUtils.formatAmountBalance;
import static pets.ui.mpa.util.CommonUtils.objectMapper;
import static pets.ui.mpa.util.CommonUtils.removeApostropheForJavascript;
import static pets.ui.mpa.util.CommonUtils.toUppercase;
import static pets.ui.mpa.util.ConstantUtils.CATEGORY_TYPE_ID_TRANSFER;
import static pets.ui.mpa.util.ConstantUtils.MERCHANT_ID_TRANSFER;
import static pets.ui.mpa.util.ConstantUtils.TRANSACTION_TYPE_ID_TRANSFER;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import pets.ui.mpa.connector.TransactionConnector;
import pets.ui.mpa.decorator.TransactionsDecorator;
import pets.ui.mpa.model.Transaction;
import pets.ui.mpa.model.TransactionFilters;
import pets.ui.mpa.model.TransactionModel;
import pets.ui.mpa.model.TransactionRequest;
import pets.ui.mpa.model.TransactionResponse;

@Service
public class TransactionService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	private final TransactionConnector transactionConnector;

	public TransactionService(TransactionConnector transactionConnector) {
		this.transactionConnector = transactionConnector;
	}

	public TransactionModel getTransactionById(String username, String id) {
		try {
			return TransactionModel.builder()
					.transaction(transactionConnector.getTransactionById(username, id).getTransactions().get(0))
					.build();
		} catch (Exception ex) {
			return TransactionModel.builder()
					.errMsg(errMsg(username, ex, "Get Transaction by Id"))
					.build();
		}
	}

	public TransactionModel getTransactionsByUsername(String username, TransactionFilters transactionFilters) {
		try {
			TransactionResponse transactionResponse = transactionConnector.getTransactionsByUsername(username, transactionFilters);
			
			return TransactionModel.builder()
					.transactions(setAccountFilter(transactionResponse.getTransactions(), transactionFilters))
					.build();
		} catch (Exception ex) {
			return TransactionModel.builder()
					.errMsg(errMsg(username, ex, "Get Transactions by Username"))
					.build();
		}
	}

	public TransactionModel saveNewTransaction(String username, Transaction transaction, String newMerchant) {
		try {
			String trfAccountId = transaction.getTrfAccount() == null ? null 
					: !hasText(transaction.getTrfAccount().getId()) ? null 
							: transaction.getTrfAccount().getId();
			
			String merchantId = isTransactionTypeTransfer(transaction) ? MERCHANT_ID_TRANSFER 
					: transaction.getRefMerchant() == null ? null 
							: transaction.getRefMerchant().getId();
			
			TransactionRequest transactionRequest = TransactionRequest.builder()
					.description(toUppercase(transaction.getDescription()))
					.accountId(transaction.getAccount().getId())
					.trfAccountId(trfAccountId)
					.typeId(transaction.getRefTransactionType().getId())
					.categoryId(transaction.getRefCategory().getId())
					.merchantId(merchantId)
					.newMerchant(toUppercase(removeApostropheForJavascript(newMerchant)))
					.username(username)
					.date(transaction.getDate())
					.amount(formatAmountBalance(transaction.getAmount()))
					.regular(transaction.getRegular())
					.necessary(transaction.getNecessary())
					.build();

			return TransactionModel.builder().transaction(
					transactionConnector.saveNewTransaction(username, transactionRequest).getTransactions().get(0))
					.build();
		} catch (Exception ex) {
			return TransactionModel.builder()
					.errMsg(errMsg(username, ex, "Save New Transaction"))
					.build();
		}
	}

	public TransactionModel updateTransaction(String username, String id, Transaction transaction, String newMerchant) {
		try {
			String trfAccountId = transaction.getTrfAccount() == null ? null 
					: !hasText(transaction.getTrfAccount().getId()) ? null 
							: transaction.getTrfAccount().getId();
			
			String merchantId = isTransactionTypeTransfer(transaction) ? MERCHANT_ID_TRANSFER 
					: transaction.getRefMerchant() == null ? null 
							: transaction.getRefMerchant().getId();

			TransactionRequest transactionRequest = TransactionRequest.builder()
					.description(toUppercase(transaction.getDescription()))
					.accountId(transaction.getAccount().getId())
					.trfAccountId(trfAccountId)
					.typeId(transaction.getRefTransactionType().getId())
					.categoryId(transaction.getRefCategory().getId())
					.merchantId(merchantId)
					.newMerchant(toUppercase(removeApostropheForJavascript(newMerchant)))
					.username(username)
					.date(transaction.getDate())
					.amount(formatAmountBalance(transaction.getAmount()))
					.regular(transaction.getRegular())
					.necessary(transaction.getNecessary())
					.build();

			return TransactionModel.builder().transaction(
					transactionConnector.updateTransaction(username, id, transactionRequest).getTransactions().get(0))
					.build();
		} catch (Exception ex) {
			return TransactionModel.builder()
					.errMsg(errMsg(username, ex, "Update Transaction Request"))
					.build();
		}
	}

	public TransactionModel deleteTransaction(String username, String id) {
		try {
			return TransactionModel.builder()
					.deleteCount(transactionConnector.deleteTransaction(username, id).getDeleteCount().intValue())
					.build();
		} catch (Exception ex) {
			return TransactionModel.builder()
					.errMsg(errMsg(username, ex, "Delete Transaction"))
					.build();
		}
	}
	
	private String errMsg(String username, Exception ex, String methodName) {
		if (ex instanceof HttpStatusCodeException) {
			try {
				logger.error("HttpStatusCodeException in {}: {}", methodName, username, ex);
				return objectMapper()
						.readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(), TransactionResponse.class)
						.getStatus().getErrMsg();
			} catch (Exception ex1) {
				logger.error("Exception Reading Http Exception in {}: {}", methodName, username, ex1);
				return String.format("Error in %s! Please Try Again!!!", methodName);
			}
		} else {
			logger.error("Exception in {}: {}", methodName, username, ex);
			return String.format("Error in %s! Please Try Again!!!", methodName);
		}
	}
	
	/**
	 * This is needed to display TRANSFER transactions properly when applying Account Filter 
	 * (or opening an Account Detail page)
	 * Displaying amount as Postive or Negative is determined when rendering the table
	 * @see TransactionsDecorator.java
	 * @param transactions list
	 * @return transaction list with accountFilter value
	 */
	private List<Transaction> setAccountFilter(List<Transaction> transactions, TransactionFilters transactionFilters) {
		if (transactionFilters == null || !hasText(transactionFilters.getAccountId())) {
			return transactions;
		} else {
			List<Transaction> updatedTransactions = new ArrayList<>();
			
			transactions.forEach(transaction -> {
				updatedTransactions.add(transaction.toBuilder()
						.accountFilter(transactionFilters.getAccountId())
						.build());
			});
			
			return updatedTransactions;
		}
	}
	
	/**
	 * @param transaction
	 * @return check transaction type transfer or not
	 */
	public static boolean isTransactionTypeTransfer(Transaction transaction) {
		return transaction != null
				&& transaction.getRefTransactionType() != null
				&& transaction.getRefTransactionType().getId() != null
				&& transaction.getRefTransactionType().getId().equals(TRANSACTION_TYPE_ID_TRANSFER);
	}
	
	/**
	 * @param transaction
	 * @return accounts to transfer from and to should be different
	 */
	public static boolean isValidTransferTransactionAccount(Transaction transaction) {
		return transaction != null
				&& transaction.getAccount() != null 
				&& transaction.getTrfAccount() != null
				&& transaction.getAccount().getId() != null
				&& transaction.getTrfAccount().getId() != null
				&& !transaction.getAccount().getId().equals(transaction.getTrfAccount().getId());
	}
	
	/**
	 * @param transaction
	 * @return transaction type and category type should be transfer
	 */
	public static boolean isValidTransferTransactionCategory(Transaction transaction) {
		String transactionTypeId = "";
		String categoryTypeId = "";
		
		if (transaction != null 
				&& transaction.getRefTransactionType() != null 
				&& transaction.getRefTransactionType().getId() != null) {
			transactionTypeId = transaction.getRefTransactionType().getId();
		}
		
		if (transaction != null 
				&& transaction.getRefCategory() != null 
				&& transaction.getRefCategory().getRefCategoryType() != null 
				&& transaction.getRefCategory().getRefCategoryType().getId() != null) {
			categoryTypeId = transaction.getRefCategory().getRefCategoryType().getId();
		}
		
		if (transactionTypeId.equals(TRANSACTION_TYPE_ID_TRANSFER)) {
			return categoryTypeId.equals(CATEGORY_TYPE_ID_TRANSFER);
		} else if (categoryTypeId.equals(CATEGORY_TYPE_ID_TRANSFER)) {
			return transactionTypeId.equals(TRANSACTION_TYPE_ID_TRANSFER);
		}
		
		return true;
	}
}
