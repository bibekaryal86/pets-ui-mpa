package pets.ui.mpa.service;

import static pets.ui.mpa.util.CommonUtils.objectMapper;
import static pets.ui.mpa.util.CommonUtils.toUppercase;
import static pets.ui.mpa.util.CommonUtils.formatAmountBalance;
import static pets.ui.mpa.util.ConstantUtils.ACCOUNT_TYPE_ID_CASH;
import static pets.ui.mpa.util.ConstantUtils.BANK_ID_CASH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import pets.ui.mpa.connector.AccountConnector;
import pets.ui.mpa.model.Account;
import pets.ui.mpa.model.AccountFilters;
import pets.ui.mpa.model.AccountModel;
import pets.ui.mpa.model.AccountRequest;
import pets.ui.mpa.model.AccountResponse;

@Service
public class AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

	private final AccountConnector accountConnector;

	public AccountService(AccountConnector accountConnector) {
		this.accountConnector = accountConnector;
	}

	public AccountModel getAccountById(String username, String id) {
		try {
			return AccountModel.builder()
					.account(accountConnector.getAccountById(username, id).getAccounts().get(0))
					.build();
		} catch (Exception ex) {
			return AccountModel.builder()
					.errMsg(errMsg(username, ex, "Get Account by Id"))
					.build();
		}
	}

	public AccountModel getAccountsByUsername(String username, AccountFilters accountFilters) {
		try {
			return AccountModel.builder()
					.accounts(accountConnector.getAccountsByUsername(username, accountFilters).getAccounts())
					.build();
		} catch (Exception ex) {
			return AccountModel.builder()
					.errMsg(errMsg(username, ex, "Get Accounts by Username"))
					.build();
		}
	}
	
	public AccountModel saveNewAccount(String username, Account account) {
		try {
			AccountRequest accountRequest = AccountRequest.builder()
					.bankId(account.getRefBank().getId())
					.description(toUppercase(account.getDescription()))
					.openingBalance(formatAmountBalance(account.getOpeningBalance()))
					.status(account.getStatus())
					.typeId(account.getRefAccountType().getId())
					.username(username)
					.build();

			return AccountModel.builder()
					.account(accountConnector.saveNewAccount(username, accountRequest).getAccounts().get(0)).build();
		} catch (Exception ex) {
			return AccountModel.builder()
					.errMsg(errMsg(username, ex, "Save New Account"))
					.build();
		}
	}

	public AccountModel updateAccount(String username, String id, Account account) {
		try {
			AccountRequest accountRequest = AccountRequest.builder()
					.bankId(account.getRefBank().getId())
					.description(toUppercase(account.getDescription()))
					.openingBalance(account.getOpeningBalance())
					.status(account.getStatus())
					.typeId(account.getRefAccountType().getId())
					.username(username)
					.build();

			return AccountModel.builder()
					.account(accountConnector.updateAccount(username, id, accountRequest).getAccounts().get(0))
					.build();
		} catch (Exception ex) {
			return AccountModel.builder()
					.errMsg(errMsg(username, ex, "Update Account Request"))
					.build();
		}
	}

	public AccountModel deleteAccount(String username, String id) {
		try {
			return AccountModel.builder()
					.deleteCount(accountConnector.deleteAccount(username, id).getDeleteCount().intValue())
					.build();
		} catch (Exception ex) {
			return AccountModel.builder()
					.errMsg(errMsg(username, ex, "Delete Account"))
					.build();
		}
	}

	private String errMsg(String username, Exception ex, String methodName) {
		if (ex instanceof HttpStatusCodeException) {
			try {
				logger.error("HttpStatusCodeException in {}: {}", methodName, username, ex);
				return objectMapper()
						.readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(), AccountResponse.class)
						.getStatus().getErrMsg();
			} catch (Exception ex1) {
				logger.error("Exception Reading Http Exception in {}: {} | {}", methodName, username, ex1.toString(), ex);
				return String.format("Error in %s! Please Try Again!!!", methodName);
			}
		} else {
			logger.error("Exception in {}: {}", methodName, username, ex);
			return String.format("Error in %s! Please Try Again!!!", methodName);
		}
	}

	/**
	 * @param account
	 * @return account type cash can only be linked to bank type cash
	 */
	public static boolean isAccountTypeCompatibleWithBankType(Account account) {
		String accountTypeId = "";
		String bankId = "";

		if (account != null && account.getRefAccountType() != null && account.getRefAccountType().getId() != null) {
			accountTypeId = account.getRefAccountType().getId();
		}

		if (account != null && account.getRefBank() != null && account.getRefBank().getId() != null) {
			bankId = account.getRefBank().getId();
		}
		
		if (accountTypeId.equals(ACCOUNT_TYPE_ID_CASH)) {
			return bankId.equals(BANK_ID_CASH);
		} else if (bankId.equals(BANK_ID_CASH)) {
			return accountTypeId.equals(ACCOUNT_TYPE_ID_CASH);
		}
		
		return true;
	}
}
