package pets.ui.mpa.controller;

import static pets.ui.mpa.util.ConstantUtils.FILTER_TRANSACTIONS_OBJECT_NAME;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_ERROR_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_FIRST_OPTION;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_ADD;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_DELETE;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_UPDATE;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static pets.ui.mpa.util.SessionHelper.removeSessionAttribute;
import static pets.ui.mpa.util.SessionHelper.setSessionAttribute;
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
import pets.ui.mpa.model.AccountModel;
import pets.ui.mpa.model.RefCategory;
import pets.ui.mpa.model.RefCategoryType;
import pets.ui.mpa.model.RefMerchant;
import pets.ui.mpa.model.RefTablesModel;
import pets.ui.mpa.model.RefTransactionType;
import pets.ui.mpa.model.Transaction;
import pets.ui.mpa.model.TransactionFilters;
import pets.ui.mpa.model.TransactionModel;
import pets.ui.mpa.service.AccountService;
import pets.ui.mpa.service.RefTablesService;
import pets.ui.mpa.service.TransactionService;
import pets.ui.mpa.validator.TransactionValidator;

@Controller
public class TransactionController {

	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

	private final RefTablesService refTablesService;
	private final AccountService accountService;
	private final TransactionService transactionService;
	private final TransactionValidator transactionValidator;
	
	public TransactionController(RefTablesService refTablesService, AccountService accountService,
			TransactionService transactionService, TransactionValidator transactionValidator) {
		this.refTablesService = refTablesService;
		this.accountService = accountService;
		this.transactionService = transactionService;
		this.transactionValidator = transactionValidator;
	}
	
	@GetMapping(value = { "transactions.pets" })
	public String initializeTransactions(HttpServletRequest httpServletRequest, ModelMap modelMap) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Transactions: {} || {}", httpServletRequest.getRemoteAddr(), username);
		
		TransactionFilters transactionFilters = null;
		try {
			transactionFilters = (TransactionFilters) httpServletRequest.getSession().getAttribute(FILTER_TRANSACTIONS_OBJECT_NAME);
		} catch (Exception ex) {
			logger.error("Initialize Transactions Exception in Transaction Filters: {}", username, ex);
		}
		
		TransactionModel transactionModel = transactionService.getTransactionsByUsername(username, transactionFilters);
		
		if (hasText(transactionModel.getErrMsg())) {
			modelMap.addAttribute("errMsg", transactionModel.getErrMsg());
		} else {
			modelMap.addAttribute("allTransactionsList", transactionModel.getTransactions());
			modelMap.addAttribute("transactionFilters", transactionFilters);
			modelMap.addAttribute("transactionFiltersStr", getTransactionFiltersString(transactionFilters));
		}
		
		return "transactions";
	}
	
	@GetMapping(value = { "transaction.pets" })
	public String initializeTransaction(HttpServletRequest httpServletRequest, ModelMap modelMap,
			@RequestParam(value = "transactionId", required = false) String transactionId,
			@RequestParam(value = "accountId", required = false) String accountId,
			@RequestParam(value = "merchantId", required = false) String merchantId) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Transaction: {} || {} || {} || {} || {}", httpServletRequest.getRemoteAddr(), 
				username, transactionId, accountId, merchantId);
		
		TransactionModel transactionModel;
		
		if (hasText(transactionId)) {
			transactionModel = transactionService.getTransactionById(username, transactionId);
			
			if (hasText(transactionModel.getErrMsg())) {
				modelMap.addAttribute("errMsg", transactionModel.getErrMsg());
			} else {
				Transaction transaction = transactionModel.getTransaction();
				transactionModel = TransactionModel.builder()
						.transaction(transaction)
						.userAction(USER_ACTION_UPDATE)
						.fromAccountPage(hasText(accountId))
						.fromMerchantPage(hasText(merchantId))
						.build();
			}
		} else {
			transactionModel = TransactionModel.builder()
					.transaction(Transaction.builder()
							.account(Account.builder()
									.id(accountId)
									.build())
							.refMerchant(RefMerchant.builder()
									.id(merchantId)
									.build())
							.build())
					.userAction(USER_ACTION_ADD)
					.fromAccountPage(hasText(accountId))
					.fromMerchantPage(hasText(merchantId))
					.build();
		}
		
		modelMap.addAttribute("transactionModel", transactionModel);
		return "transaction";
	}
	
	@PostMapping(value = { "transaction.pets" })
	public String processTransaction(HttpServletRequest httpServletRequest, 
			@ModelAttribute("transactionModel") TransactionModel transactionModel, ModelMap modelMap,
			BindingResult bindingResult, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Process Transaction: {} || {} || {}", httpServletRequest.getRemoteAddr(), 
				username, transactionModel);
		
		try {
			transactionValidator.validate(transactionModel, bindingResult);
			
			if (bindingResult.hasErrors()) {
				return "transaction";
			} else {
				TransactionModel transactionModelResponse = null;
				
				if (transactionModel.getUserAction().equals(USER_ACTION_ADD)) {
					transactionModelResponse = transactionService.saveNewTransaction(username, transactionModel.getTransaction(), 
							transactionModel.getNewMerchant());
				} else if (transactionModel.getUserAction().equals(USER_ACTION_UPDATE)) {
					transactionModelResponse = transactionService.updateTransaction(username, transactionModel.getTransaction().getId(), 
							transactionModel.getTransaction(), transactionModel.getNewMerchant());
				} else if (transactionModel.getUserAction().equals(USER_ACTION_DELETE)) {
					transactionModelResponse = transactionService.deleteTransaction(username, transactionModel.getTransaction().getId());
				} else {
					return "redirect:transaction.pets";
				}
				
				if (transactionModelResponse == null) {
					bindingResult.rejectValue("errMsg", "error.generic.message", new Object[] { " Transaction " }, "errMsg");
				} else if (hasText(transactionModelResponse.getErrMsg())) {
					bindingResult.rejectValue("errMsg", "error.from.response",
							new Object[] { transactionModelResponse.getErrMsg() }, "errMsg");
				} else {
					sessionStatus.setComplete();
					
					if (transactionModel.isFromAccountPage()) {
						if (transactionModel.getUserAction().equals(USER_ACTION_UPDATE)) {
							return String.format("redirect:transaction.pets?transactionId=%s&accountId=%s", transactionModelResponse.getTransaction().getId(), 
													transactionModelResponse.getTransaction().getAccount().getId());
						} else if (transactionModel.getUserAction().equals(USER_ACTION_DELETE)) {
							return String.format("redirect:account.pets?accountId=%s", transactionModel.getTransaction().getAccount().getId());
						} else {
							return String.format("redirect:account.pets?accountId=%s", transactionModelResponse.getTransaction().getAccount().getId());
						}
					} else if (transactionModel.isFromMerchantPage()) {
						if (transactionModel.getUserAction().equals(USER_ACTION_UPDATE)) {
							return String.format("redirect:transaction.pets?transactionId=%s&merchantId=%s", transactionModelResponse.getTransaction().getId(), 
													transactionModelResponse.getTransaction().getRefMerchant().getId());
						} else if (transactionModel.getUserAction().equals(USER_ACTION_DELETE)) {
							return String.format("redirect:merchant.pets?merchantId=%s", transactionModel.getTransaction().getRefMerchant().getId());
						} else {
							return String.format("redirect:merchant.pets?merchantId=%s", transactionModelResponse.getTransaction().getRefMerchant().getId());
						}
					} else {
						if (transactionModel.getUserAction().equals(USER_ACTION_UPDATE)) {
							return String.format("redirect:transaction.pets?transactionId=%s", transactionModelResponse.getTransaction().getId());
						} else {
							return "redirect:transactions.pets";
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Process Transaction: {} || {} || {}", httpServletRequest.getRemoteAddr(), 
					username, transactionModel, ex);
			bindingResult.rejectValue("errMsg", "error.generic.message", new Object[] { " Transaction " }, "errMsg");
		}
		
		return "transaction";
	}
	
	@ModelAttribute("refCategoryTypesList")
	public List<RefCategoryType> modelAttributeRefCategoryTypesList(HttpServletRequest httpServletRequest) {
		List<RefCategoryType> refCategoryTypesList = new ArrayList<>();
		
		boolean usedInTxnsOnly = false;
		if ("/transactions.pets".equals(httpServletRequest.getServletPath())) {
			usedInTxnsOnly = true;
		}
		
		RefTablesModel refTablesModel = refTablesService.getRefCategoryTypes(getCurrentUsername(httpServletRequest), usedInTxnsOnly);
		
		if (hasText(refTablesModel.getErrMsg())) {
			refCategoryTypesList.add(RefCategoryType.builder()
					.id("")
					.description(GENERIC_ERROR_MESSAGE)
					.build());
		} else {
			refCategoryTypesList.add(RefCategoryType.builder()
					.id("")
					.description(GENERIC_FIRST_OPTION)
					.build());
			refCategoryTypesList.addAll(refTablesModel.getRefCategoryTypes());
		}
		
		return refCategoryTypesList;
	}
	
	@ModelAttribute("refCategoriesList")
	public List<RefCategory> modelAttributeRefCategoriesList(HttpServletRequest httpServletRequest) {
		List<RefCategory> refCategoriesList = new ArrayList<>();
		String categoryTypeId = (String) httpServletRequest.getParameter("transaction.refCategory.refCategoryType.id");
		
		boolean usedInTxnsOnly = false;
		if ("/transactions.pets".equals(httpServletRequest.getServletPath())) {
			usedInTxnsOnly = true;
		}
		
		if (!hasText(categoryTypeId)) {
			try {
				TransactionFilters transactionFilters = (TransactionFilters) httpServletRequest.getSession()
						.getAttribute(FILTER_TRANSACTIONS_OBJECT_NAME);
				
				if (transactionFilters != null && hasText(transactionFilters.getCategoryTypeId())) {
					categoryTypeId = transactionFilters.getCategoryTypeId();
				}
			} catch (Exception ex) {
				logger.error("Model Attribute Transactions Exception in Transaction Filters: {}", getCurrentUsername(httpServletRequest), ex);
			}
		}
		
		RefTablesModel refTablesModel = refTablesService.getRefCategories(getCurrentUsername(httpServletRequest), categoryTypeId, usedInTxnsOnly);
		
		if (hasText(refTablesModel.getErrMsg())) {
			refCategoriesList.add(RefCategory.builder()
					.id("")
					.description(GENERIC_ERROR_MESSAGE)
					.build());
		} else {
			refCategoriesList.add(RefCategory.builder()
					.id("")
					.description(GENERIC_FIRST_OPTION)
					.build());
			refCategoriesList.addAll(refTablesModel.getRefCategories());
		}
		
		return refCategoriesList;
	}
	
	@ModelAttribute("refMerchantsList")
	public List<RefMerchant> modelAttributeRefMerchantsList(HttpServletRequest httpServletRequest) {
		List<RefMerchant> refMerchantsList = new ArrayList<>();
		RefTablesModel refTablesModel = refTablesService.getRefMerchants(getCurrentUsername(httpServletRequest), null);
		
		if (hasText(refTablesModel.getErrMsg())) {
			refMerchantsList.add(RefMerchant.builder()
					.id("")
					.description(GENERIC_ERROR_MESSAGE)
					.build());
		} else {
			refMerchantsList.add(RefMerchant.builder()
					.id("")
					.description(GENERIC_FIRST_OPTION)
					.build());
			refMerchantsList.addAll(refTablesModel.getRefMerchants());
		}
		
		return refMerchantsList;
	}
	
	@ModelAttribute("accountsList")
	public List<Account> modelAttributeAccountsList(HttpServletRequest httpServletRequest) {
		List<Account> accountsList = new ArrayList<>();
		AccountModel accountModel = accountService.getAccountsByUsername(getCurrentUsername(httpServletRequest), null);
		
		if (hasText(accountModel.getErrMsg())) {
			accountsList.add(Account.builder()
					.id("")
					.description(GENERIC_ERROR_MESSAGE)
					.build());
		} else {
			accountsList.add(Account.builder()
					.id("")
					.description(GENERIC_FIRST_OPTION)
					.build());
			accountsList.addAll(accountModel.getAccounts());
		}
		
		return accountsList;
	}
	
	@ModelAttribute("refTransactionTypesList")
	public List<RefTransactionType> modelAttributeTransactionTypesList(HttpServletRequest httpServletRequest) {
		List<RefTransactionType> transactionTypesList = new ArrayList<>();
		String transactionTypeId = (String) httpServletRequest.getParameter("transaction.refTransactionType.id");
		RefTablesModel refTablesModel = refTablesService.getRefTransactionTypes(getCurrentUsername(httpServletRequest));
		
		if (hasText(refTablesModel.getErrMsg())) {
			transactionTypesList.add(RefTransactionType.builder()
					.id("")
					.description(GENERIC_ERROR_MESSAGE)
					.build());
		} else {
			transactionTypesList.add(RefTransactionType.builder()
					.id("")
					.description(GENERIC_FIRST_OPTION)
					.build());
			transactionTypesList.addAll(refTablesModel.getRefTransactionTypes());
		}
		
		if (hasText(transactionTypeId)) {
			setSessionAttribute("refTxnTypeId", transactionTypeId, httpServletRequest);
		} else {
			removeSessionAttribute("refTxnTypeId", httpServletRequest);
		}
		
		return transactionTypesList;
	}
	
	private String getTransactionFiltersString(TransactionFilters transactionFilters) {
		if (transactionFilters != null) {
			StringBuilder sb = new StringBuilder("Filters Currently Applied:");
			
			if (hasText(transactionFilters.getTransactionTypeId())) {
				sb.append(" [Transaction Type]");
			}
			
			if (hasText(transactionFilters.getCategoryTypeId())) {
				sb.append(" [Category Type]");
			}
			
			if (hasText(transactionFilters.getCategoryId())) {
				sb.append(" [Category]");
			}
			
			if (hasText(transactionFilters.getAccountId())) {
				sb.append(" [Account]");
			}
			
			if (transactionFilters.getAmountFrom() != null || transactionFilters.getAmountTo() != null) {
				sb.append(" [Txn Amount]");
			}
			
			if (hasText(transactionFilters.getMerchantId())) {
				sb.append(" [Merchant]");
			}
			
			if (hasText(transactionFilters.getDateFrom()) || hasText(transactionFilters.getDateTo())) {
				sb.append(" [Txn Date]");
			}
			
			if (transactionFilters.getNecessary() != null) {
				sb.append(" [Necessary]");
			}
			
			if (transactionFilters.getRegular() != null) {
				sb.append(" [Regular]");
			}
			
			return sb.toString();
		}
		
		return null;
	}
}
