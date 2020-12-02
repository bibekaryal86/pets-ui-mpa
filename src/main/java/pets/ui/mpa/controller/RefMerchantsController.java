package pets.ui.mpa.controller;

import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import pets.ui.mpa.model.RefTablesModel;
import pets.ui.mpa.model.Transaction;
import pets.ui.mpa.model.TransactionFilters;
import pets.ui.mpa.model.TransactionModel;
import pets.ui.mpa.service.RefTablesService;
import pets.ui.mpa.service.TransactionService;

@Controller
public class RefMerchantsController {

	private static Logger logger = LoggerFactory.getLogger(RefMerchantsController.class);

	private final RefTablesService refTablesService;
	private final TransactionService transactionService;

	public RefMerchantsController(RefTablesService refTablesService, TransactionService transactionService) {
		this.refTablesService = refTablesService;
		this.transactionService = transactionService;
	}

	@GetMapping(value = { "merchants.pets" })
	public String initializeRefMerchants(HttpServletRequest httpServletRequest, ModelMap modelMap,
			@RequestParam(value = "merchantNameBeginsWith", required = false) String merchantNameBeginsWith) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Ref Merchants: {} || {}", httpServletRequest.getRemoteAddr(), username);

		RefTablesModel refTablesModel = refTablesService.getRefMerchants(username, merchantNameBeginsWith);

		if (hasText(refTablesModel.getErrMsg())) {
			modelMap.addAttribute("errMsg", refTablesModel.getErrMsg());
		} else {
			modelMap.addAttribute("allMerchantsList", refTablesModel.getRefMerchants());
			modelMap.addAttribute("allMerchantsFilterList", refTablesModel.getRefMerchantsFilterList());
			modelMap.addAttribute("merchantFiltersStr", getMerchantFiltersString(merchantNameBeginsWith, refTablesModel.getRefMerchants().size()));
		}

		return "merchants";
	}
	
	@GetMapping(value = { "merchant.pets" })
	public String initializeRefMerchant(HttpServletRequest httpServletRequest, ModelMap modelMap,
			@RequestParam(value = "merchantId") String merchantId) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Ref Merchant: {} || {} || {}", httpServletRequest.getRemoteAddr(), 
				username, merchantId);
		
		RefTablesModel refTablesModel = refTablesService.getRefMerchantById(username, merchantId);

		if (hasText(refTablesModel.getErrMsg())) {
			modelMap.addAttribute("errMsg", refTablesModel.getErrMsg());
		} else {
			modelMap.addAttribute("merchant", refTablesModel.getRefMerchants().get(0));
		}

		return "merchant";
	}
	
	@ModelAttribute("allTransactionsList")
	public List<Transaction> modelAttributeAllTransactionsList(HttpServletRequest httpServletRequest) {
		List<Transaction> allTransactionsList = new ArrayList<>();
		
		if ("/merchants.pets".equals(httpServletRequest.getServletPath())) {
			return allTransactionsList;
		} else if ("/merchant.pets".equals(httpServletRequest.getServletPath())) {
			String merchantId = (String) httpServletRequest.getParameter("merchantId");
			
			if (hasText(merchantId)) {
				String username = getCurrentUsername(httpServletRequest);
				
				TransactionModel transactionModel = transactionService.getTransactionsByUsername(username, 
						TransactionFilters.builder()
						.merchantId(merchantId)
						.build());
				
				allTransactionsList.addAll(transactionModel.getTransactions());
			}
		}
		
		return allTransactionsList;
	}
	
	private String getMerchantFiltersString(String merchantNameBeginsWith, int listSize) {
		if (hasText(merchantNameBeginsWith) && !merchantNameBeginsWith.equals("ALL")) {
			StringBuilder sb = new StringBuilder("Filters Currently Applied:");
			
			if ("0".equals(merchantNameBeginsWith)) {
				sb.append(" [Merchant Not Used Transactions]");
				
				if (listSize > 0) {
					sb.append(" Below Listed Merchants Can Be Deleted Safely!");
				}
			} else {
				sb.append(String.format(" [Merchant Name Begins With: %s]", merchantNameBeginsWith));
			}
			
			return sb.toString();
		}
		
		return null;
	}
}
