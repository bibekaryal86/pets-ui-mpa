package pets.ui.mpa.service;

import static pets.ui.mpa.util.CommonUtils.objectMapper;
import static pets.ui.mpa.util.CommonUtils.toUppercase;
import static pets.ui.mpa.util.CommonUtils.removeApostropheForJavascript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import pets.ui.mpa.connector.RefTablesConnector;
import pets.ui.mpa.model.RefAccountTypeRequest;
import pets.ui.mpa.model.RefAccountTypeResponse;
import pets.ui.mpa.model.RefBankRequest;
import pets.ui.mpa.model.RefBankResponse;
import pets.ui.mpa.model.RefCategoryFilters;
import pets.ui.mpa.model.RefCategoryRequest;
import pets.ui.mpa.model.RefCategoryResponse;
import pets.ui.mpa.model.RefCategoryTypeRequest;
import pets.ui.mpa.model.RefCategoryTypeResponse;
import pets.ui.mpa.model.RefMerchantFilters;
import pets.ui.mpa.model.RefMerchantRequest;
import pets.ui.mpa.model.RefMerchantResponse;
import pets.ui.mpa.model.RefTablesModel;
import pets.ui.mpa.model.RefTransactionTypeRequest;
import pets.ui.mpa.model.RefTransactionTypeResponse;

@Service
public class RefTablesService {

	private static final Logger logger = LoggerFactory.getLogger(RefTablesService.class);

	private final RefTablesConnector refTablesConnector;

	public RefTablesService(RefTablesConnector refTablesConnector) {
		this.refTablesConnector = refTablesConnector;
	}

	public RefTablesModel getRefAccountTypes(String username) {
		try {
			return RefTablesModel.builder()
					.refAccountTypes(refTablesConnector.getRefAccountTypes(username).getRefAccountTypes())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Get Ref Account Types"))
					.build();
		}
	}
	
	/**
	 * Used for Import only
	 */
	public RefTablesModel saveRefAccountType(String username, String description) {
		try {
			RefAccountTypeRequest refAccountTypeRequest = RefAccountTypeRequest.builder()
					.description(toUppercase(removeApostropheForJavascript(description)))
					.build();
			
			return RefTablesModel.builder()
					.refAccountTypes(refTablesConnector.saveRefAccountType(username, refAccountTypeRequest).getRefAccountTypes())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Save Ref Account Type"))
					.build();
		}
	}

	public RefTablesModel getRefBanks(String username) {
		try {
			return RefTablesModel.builder()
					.refBanks(refTablesConnector.getRefBanks(username).getRefBanks())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Get Ref Banks"))
					.build();
		}
	}
	
	/**
	 * Used for Import only
	 */
	public RefTablesModel saveRefBank(String username, String description) {
		try {
			RefBankRequest refBankRequest = RefBankRequest.builder()
					.description(toUppercase(removeApostropheForJavascript(description)))
					.build();
			
			return RefTablesModel.builder()
					.refBanks(refTablesConnector.saveRefBank(username, refBankRequest).getRefBanks())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Save Ref Bank"))
					.build();
		}
	}

	public RefTablesModel getRefCategories(String username, String refCategoryTypeId, boolean usedInTxnsOnly) {
		try {
			RefCategoryFilters refCategoryFilters = null;
			if (!"".equals(refCategoryTypeId)) {
				refCategoryFilters = RefCategoryFilters.builder()
						.categoryTypeId(refCategoryTypeId)
						.usedInTxnsOnly(usedInTxnsOnly)
						.build();
			} else if (usedInTxnsOnly) {
				refCategoryFilters = RefCategoryFilters.builder()
						.usedInTxnsOnly(usedInTxnsOnly)
						.build();
			}

			return RefTablesModel.builder()
					.refCategories(refTablesConnector.getRefCategories(username, refCategoryFilters).getRefCategories())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Get Ref Categories"))
					.build();
		}
	}
	
	/**
	 * Used for Import only
	 */
	public RefTablesModel saveRefCategory(String username, String description, String categoryTypeId) {
		try {
			RefCategoryRequest refCategoryRequest = RefCategoryRequest.builder()
					.description(toUppercase(removeApostropheForJavascript(description)))
					.categoryTypeId(categoryTypeId)
					.build();
			
			return RefTablesModel.builder()
					.refCategories(refTablesConnector.saveRefCategory(username, refCategoryRequest).getRefCategories())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Save Ref Category"))
					.build();
		}
	}

	public RefTablesModel getRefCategoryTypes(String username, boolean usedInTxnsOnly) {
		try {
			return RefTablesModel.builder()
					.refCategoryTypes(refTablesConnector.getRefCategoryTypes(username, String.valueOf(usedInTxnsOnly))
							.getRefCategoryTypes())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Get Ref Category Types"))
					.build();
		}
	}
	
	/**
	 * Used for Import only
	 */
	public RefTablesModel saveRefCategoryType(String username, String description) {
		try {
			RefCategoryTypeRequest refCategoryTypeRequest = RefCategoryTypeRequest.builder()
					.description(toUppercase(removeApostropheForJavascript(description)))
					.build();
			
			return RefTablesModel.builder()
					.refCategoryTypes(refTablesConnector.saveRefCategoryType(username, refCategoryTypeRequest).getRefCategoryTypes())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Save Ref Category Type"))
					.build();
		}
	}
	
	public RefTablesModel getRefMerchantById(String username, String merchantId) {
		try {
			return RefTablesModel.builder()
					.refMerchants(refTablesConnector.getRefMerchantById(username, merchantId).getRefMerchants())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Get Ref Merchant By Id"))
					.build();
		}
	}

	public RefTablesModel getRefMerchants(String username, String merchantNameBeginsWith) {
		try {
			RefMerchantFilters refMerchantFilters = null;
			if (!"ALL".equals(merchantNameBeginsWith)) {
				refMerchantFilters = RefMerchantFilters.builder()
						.firstChar(merchantNameBeginsWith)
						.notUsedInTransactionsOnly("0".equals(merchantNameBeginsWith))
						.build();
			}

			RefMerchantResponse refMerchantResponse = refTablesConnector.getRefMerchants(username, refMerchantFilters);

			return RefTablesModel.builder()
					.refMerchants(refMerchantResponse.getRefMerchants())
					.refMerchantsFilterList(refMerchantResponse.getRefMerchantsFilterList())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Get Ref Merchants"))
					.build();
		}
	}
	
	/**
	 * Used for Import only
	 */
	public RefTablesModel saveNewRefMerchant(String username, String description) {
		try {
			RefMerchantRequest refMerchantRequest = RefMerchantRequest.builder()
					.username(username)
					.description(toUppercase(removeApostropheForJavascript(description)))
					.build();
			
			return RefTablesModel.builder()
					.refMerchants(
							refTablesConnector.saveNewRefMerchant(username, refMerchantRequest).getRefMerchants())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Save Ref Merchant"))
					.build();
		}
	}
	
	public RefTablesModel updateRefMerchant(String username, String id, String description) {
		try {
			RefMerchantRequest refMerchantRequest = RefMerchantRequest.builder()
					.username(username)
					.description(toUppercase(removeApostropheForJavascript(description)))
					.build();
			
			return RefTablesModel.builder()
					.refMerchants(
							refTablesConnector.updateRefMerchant(username, id, refMerchantRequest).getRefMerchants())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Update Ref Merchant"))
					.build();
		}
	}

	public RefTablesModel deleteRefMerchant(String username, String id) {
		try {
			return RefTablesModel.builder()
					.deleteCount(refTablesConnector.deleteRefMerchant(username, id).getDeleteCount().intValue())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Delete Ref Merchant"))
					.build();
		}
	}

	public RefTablesModel getRefTransactionTypes(String username) {
		try {
			return RefTablesModel.builder()
					.refTransactionTypes(refTablesConnector.getRefTransactionTypes(username).getRefTransactionTypes())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Get Ref Transaction Types"))
					.build();
		}
	}
	
	/**
	 * Used for Import only
	 */
	public RefTablesModel saveRefTransactionType(String username, String description) {
		try {
			RefTransactionTypeRequest refTransactionTypeRequest = RefTransactionTypeRequest.builder()
					.description(toUppercase(removeApostropheForJavascript(description)))
					.build();
			
			return RefTablesModel.builder()
					.refTransactionTypes(refTablesConnector.saveRefTransactionType(username, refTransactionTypeRequest).getRefTransactionTypes())
					.build();
		} catch (Exception ex) {
			return RefTablesModel.builder()
					.errMsg(errMsg(username, ex, "Save Ref Transaction Type"))
					.build();
		}
	}
	
	private String errMsg(String username, Exception ex, String methodName) {
		String errMsg;

		if (ex instanceof HttpStatusCodeException) {
			try {
				logger.error("HttpStatusCodeException in {}: {}", methodName, username, ex);
				
				switch (methodName) {
				case "Get Ref Account Types":
				case "Save Ref Account Type":
					errMsg = objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
							RefAccountTypeResponse.class).getStatus().getErrMsg();
					break;
				case "Get Ref Banks":
				case "Save Ref Bank":
					errMsg = objectMapper()
							.readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(), RefBankResponse.class)
							.getStatus().getErrMsg();
					break;
				case "Get Ref Categories":
				case "Save Ref Category":
					errMsg = objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
							RefCategoryResponse.class).getStatus().getErrMsg();
					break;
				case "Get Ref Category Types":
				case "Save Ref Category Type":
					errMsg = objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
							RefCategoryTypeResponse.class).getStatus().getErrMsg();
					break;
				case "Get Ref Merchants":
				case "Get Ref Merchant By Id":
				case "Save Ref Merchant":
				case "Update Ref Merchant":
				case "Delete Ref Merchant":
					errMsg = objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
							RefMerchantResponse.class).getStatus().getErrMsg();
					break;
				case "Get Ref Transaction Types":
				case "Save Ref Transaction Type":
					errMsg = objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
							RefTransactionTypeResponse.class).getStatus().getErrMsg();
					break;
				default:
					errMsg = String.format("Error in %s! Please Try Again!!!", methodName);
				}
			} catch (Exception ex1) {
				logger.error("Exception Reading Http Exception in {}: {} | {}", methodName, username, ex1.toString(), ex);
				errMsg = String.format("Error in %s! Please Try Again!!!", methodName);
			}
		} else {
			logger.error("Exception in {}: {}", methodName, username, ex);
			errMsg = String.format("Error in %s! Please Try Again!!!", methodName);
		}

		return errMsg;
	}
}
