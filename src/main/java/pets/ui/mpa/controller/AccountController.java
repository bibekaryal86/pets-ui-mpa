package pets.ui.mpa.controller;

import static pets.ui.mpa.util.ConstantUtils.ACCOUNT_STATUSES;
import static pets.ui.mpa.util.ConstantUtils.FILTER_ACCOUNTS_OBJECT_NAME;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_ERROR_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_FIRST_OPTION;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_ADD;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_DELETE;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_UPDATE;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pets.ui.mpa.model.Account;
import pets.ui.mpa.model.AccountFilters;
import pets.ui.mpa.model.AccountModel;
import pets.ui.mpa.model.RefAccountType;
import pets.ui.mpa.model.RefBank;
import pets.ui.mpa.model.RefTablesModel;
import pets.ui.mpa.model.Transaction;
import pets.ui.mpa.model.TransactionFilters;
import pets.ui.mpa.model.TransactionModel;
import pets.ui.mpa.service.AccountService;
import pets.ui.mpa.service.RefTablesService;
import pets.ui.mpa.service.TransactionService;
import pets.ui.mpa.validator.AccountValidator;

@Controller
public class AccountController {

	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	private final AccountService accountService;
	private final RefTablesService refTablesService;
	private final AccountValidator accountValidator;
	private final TransactionService transactionService;

	public AccountController(AccountService accountService, RefTablesService refTablesService,
			AccountValidator accountValidator, TransactionService transactionService) {
		this.accountService = accountService;
		this.refTablesService = refTablesService;
		this.accountValidator = accountValidator;
		this.transactionService = transactionService;
	}

	@GetMapping(value = { "accounts.pets" })
	public String initializeAccounts(HttpServletRequest httpServletRequest, ModelMap modelMap) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Accounts: {} || {}", httpServletRequest.getRemoteAddr(), username);

		AccountFilters accountFilters = null;
		try {
			accountFilters = (AccountFilters) httpServletRequest.getSession().getAttribute(FILTER_ACCOUNTS_OBJECT_NAME);
		} catch (Exception ex) {
			logger.error("Initialize Accounts Exception in Account Filters: {}", username, ex);
		}

		AccountModel accountModel = accountService.getAccountsByUsername(username, accountFilters);

		if (hasText(accountModel.getErrMsg())) {
			modelMap.addAttribute("errMsg", accountModel.getErrMsg());
		} else {
			modelMap.addAttribute("allAccountsList", accountModel.getAccounts());
			modelMap.addAttribute("accountFilters", accountFilters);
			modelMap.addAttribute("accountFiltersStr", getAccountFiltersString(accountFilters));
		}

		return "accounts";
	}
	
	@GetMapping(value = { "account.pets" })
	public String initializeAccount(HttpServletRequest httpServletRequest, ModelMap modelMap,
			@RequestParam(value = "accountId", required = false) String accountId) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Account: {} || {} || {}", httpServletRequest.getRemoteAddr(), username, accountId);

		AccountModel accountModel;

		if (hasText(accountId)) {
			accountModel = accountService.getAccountById(username, accountId);

			if (hasText(accountModel.getErrMsg())) {
				modelMap.addAttribute("errMsg", accountModel.getErrMsg());
			} else {
				Account account = accountModel.getAccount();
				accountModel = AccountModel.builder()
						.account(account)
						.userAction(USER_ACTION_UPDATE)
						.build();
			}
		} else {
			accountModel = AccountModel.builder()
					.userAction(USER_ACTION_ADD)
					.build();
		}

		modelMap.addAttribute("accountModel", accountModel);
		return "account";
	}

	@PostMapping(value = { "account.pets" })
	public String processAccount(HttpServletRequest httpServletRequest,
			@ModelAttribute("accountModel") AccountModel accountModel, BindingResult bindingResult,
			SessionStatus sessionStatus, RedirectAttributes redirectAttributes, ModelMap modelMap) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Process Account: {} || {} || {}", httpServletRequest.getRemoteAddr(), 
				username, accountModel);

		try {
			accountValidator.validate(accountModel, bindingResult);

			if (bindingResult.hasErrors()) {
				return "account";
			} else {
				AccountModel accountModelResponse = null;

				if (accountModel.getUserAction().equals(USER_ACTION_ADD)) {
					accountModelResponse = accountService.saveNewAccount(username, accountModel.getAccount());
				} else if (accountModel.getUserAction().equals(USER_ACTION_UPDATE)) {
					accountModelResponse = accountService.updateAccount(username, accountModel.getAccount().getId(),
							accountModel.getAccount());
				} else if (accountModel.getUserAction().equals(USER_ACTION_DELETE)) {
					accountModelResponse = accountService.deleteAccount(username, accountModel.getAccount().getId());
				} else {
					return "redirect:account.pets";
				}

				if (accountModelResponse == null) {
					bindingResult.rejectValue("errMsg", "error.generic.message", new Object[] { " Account " }, "errMsg");
				} else if (hasText(accountModelResponse.getErrMsg())) {
					bindingResult.rejectValue("errMsg", "error.from.response",
							new Object[] { accountModelResponse.getErrMsg() }, "errMsg");
				} else {
					sessionStatus.setComplete();
					
					if (accountModel.getUserAction().equals(USER_ACTION_DELETE)) {
						return "redirect:accounts.pets";
					} else {
						return String.format("redirect:account.pets?accountId=%s", accountModelResponse.getAccount().getId());
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Process Account: {} || {} || {}", httpServletRequest.getRemoteAddr(), username,
					accountModel, ex);
			bindingResult.rejectValue("errMsg", "error.generic.message", new Object[] { " Account " }, "errMsg");
		}

		return "account";
	}
	
	@ModelAttribute("refAccountTypesList")
	private List<RefAccountType> modelAttributeRefAccountTypesList(HttpServletRequest httpServletRequest) {
		List<RefAccountType> refAccountTypesList = new ArrayList<>();
		RefTablesModel refTablesModel = refTablesService.getRefAccountTypes(getCurrentUsername(httpServletRequest));

		if (hasText(refTablesModel.getErrMsg())) {
			refAccountTypesList.add(RefAccountType.builder()
					.id("")
					.description(GENERIC_ERROR_MESSAGE)
					.build());
		} else {
			refAccountTypesList.add(RefAccountType.builder()
					.id("")
					.description(GENERIC_FIRST_OPTION)
					.build());
			refAccountTypesList.addAll(refTablesModel.getRefAccountTypes());
		}

		return refAccountTypesList;
	}

	@ModelAttribute("refBanksList")
	public List<RefBank> modelAttributeRefBanksList(HttpServletRequest httpServletRequest) {
		List<RefBank> refBanksList = new ArrayList<>();
		RefTablesModel refTablesModel = refTablesService.getRefBanks(getCurrentUsername(httpServletRequest));

		if (hasText(refTablesModel.getErrMsg())) {
			refBanksList.add(RefBank.builder()
					.id("")
					.description(GENERIC_ERROR_MESSAGE)
					.build());
		} else {
			refBanksList.add(RefBank.builder()
					.id("")
					.description(GENERIC_FIRST_OPTION)
					.build());
			refBanksList.addAll(refTablesModel.getRefBanks());
		}

		return refBanksList;
	}

	@ModelAttribute("accountStatusList")
	public List<String> modelAttributeAccountStatusList() {
		return ACCOUNT_STATUSES;
	}
	
	@ModelAttribute("allTransactionsList")
	public List<Transaction> modelAttributeAllTransactionsList(HttpServletRequest httpServletRequest) {
		List<Transaction> allTransactionsList = new ArrayList<>();
		
		if ("/accounts.pets".equals(httpServletRequest.getServletPath())) {
			return allTransactionsList;
		} else if ("/account.pets".equals(httpServletRequest.getServletPath())) {
			String accountId = (String) httpServletRequest.getParameter("accountId");
			
			if (hasText(accountId)) {
				String username = getCurrentUsername(httpServletRequest);
				
				TransactionModel transactionModel = transactionService.getTransactionsByUsername(username, 
						TransactionFilters.builder()
						.accountId(accountId)
						.build());
				
				allTransactionsList.addAll(transactionModel.getTransactions());
			}
		}
		
		return allTransactionsList;
	}
	
	private String getAccountFiltersString(AccountFilters accountFilters) {
		if (accountFilters != null) {
			StringBuilder sb = new StringBuilder("Filters Currently Applied:");
			
			if (hasText(accountFilters.getAccountTypeId())) {
				sb.append(" [Account Type]");
			}
			
			if (hasText(accountFilters.getBankId())) {
				sb.append(" [Bank]");
			}
			
			if (hasText(accountFilters.getStatus())) {
				sb.append(" [Status]");
			}
			
			return sb.toString();
		}
		
		return null;
	}
}
