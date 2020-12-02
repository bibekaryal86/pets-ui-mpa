package pets.ui.mpa.service;

import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_ADD;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import pets.ui.mpa.model.Account;
import pets.ui.mpa.model.AccountModel;
import pets.ui.mpa.model.AppUser;
import pets.ui.mpa.model.ImportAccount;
import pets.ui.mpa.model.ImportCategory;
import pets.ui.mpa.model.ImportTransaction;
import pets.ui.mpa.model.RefAccountType;
import pets.ui.mpa.model.RefBank;
import pets.ui.mpa.model.RefCategory;
import pets.ui.mpa.model.RefCategoryType;
import pets.ui.mpa.model.RefMerchant;
import pets.ui.mpa.model.RefTablesModel;
import pets.ui.mpa.model.RefTransactionType;
import pets.ui.mpa.model.Transaction;
import pets.ui.mpa.model.TransactionModel;
import pets.ui.mpa.validator.AccountValidator;
import pets.ui.mpa.validator.TransactionValidator;

@Service
public class ImportService {

	private static final Logger logger = LoggerFactory.getLogger(ImportService.class);
	
	private final String DEFAULT_FORMAT = "uuuu-MM-dd";
	private final String DEFAULT_EXPECTED = "MM/dd/uuuu";
	private final String FORMAT_ONE = "M/d/uuuu";
	private final String FORMAT_TWO = "MM/d/uuuu";
	private final String FORMAT_THREE = "M/dd/uuuu";
	private final List<String> TRANSACTION_TYPES = asList("INCOME", "EXPENSE", "TRANSFER");
	private final List<String> ACCOUNT_TYPES = asList("ACCOUNT CASH", "CHECKING ACCOUNT", "CREDIT CARD", 
			"LOANS AND MORTGAGES", "SAVINGS ACCOUNT", "INVESTMENT ACCOUNT", "OTHER DEPOSIT ACCOUNTS", 
			"OTHER LOAN ACCOUNTS");

	private final AccountService accountService;
	private final TransactionService transactionService;
	private final RefTablesService refTablesService;
	private final TransactionValidator transactionValidator;
	private final AccountValidator accountValidator;

	public ImportService(AccountService accountService, TransactionService transactionService,
			RefTablesService refTablesService, TransactionValidator transactionValidator,
			AccountValidator accountValidator) {
		this.accountService = accountService;
		this.transactionService = transactionService;
		this.refTablesService = refTablesService;
		this.transactionValidator = transactionValidator;
		this.accountValidator = accountValidator;
	}

	private boolean isValidCsvFile(MultipartFile multipartFile) {
		return multipartFile != null && multipartFile.getOriginalFilename() != null
				&& isValidCsvFile(multipartFile.getOriginalFilename());
	}

	private boolean isValidCsvFile(String fileName) {
		String[] fileNameArray = fileName.split("\\.");
		return fileNameArray.length > 1 && fileNameArray[fileNameArray.length - 1].equals("csv");
	}

	/**
	 * Imports Transaction Types, Merchants and then Transactions
	 * @param username
	 * @param multipartFile
	 * @return list of failures
	 */
	public List<String> importTransactions(String username, MultipartFile multipartFile) {
		logger.info("Import Transactions Start: {}", username);
		List<String> errors = new ArrayList<>();

		if (!isValidCsvFile(multipartFile)) {
			errors.add("Import Aborted! File Not CSV!! Please Try Again!!!");
		} else {
			List<ImportTransaction> importTransactions = parseTransactionsCsv(username, multipartFile);
			
			if (isEmpty(importTransactions)) {
				errors.add("Import Aborted! Error Parsing CSV!! Confirm Header Names and Please Try Again!!!");
			} else {
				RefTablesModel refTransactionTypesModel = refTablesService.getRefTransactionTypes(username);
				
				if (hasText(refTransactionTypesModel.getErrMsg())) {
					errors.add("Import Aborted! Error Retrieving Transaction Types!! Please Try Again!!!");
				} else {
					Map<String, String> transactionTypesMap = getTransactionTypesMap(refTransactionTypesModel.getRefTransactionTypes());
					List<String> saveTransactionTypesErrors = saveTransactionTypes(username, transactionTypesMap);
					errors.addAll(saveTransactionTypesErrors);
					
					// retrieve transaction types again to get updated list and map after saving
					refTransactionTypesModel = refTablesService.getRefTransactionTypes(username);
					
					if (hasText(refTransactionTypesModel.getErrMsg())) {
						errors.add("Import Aborted! Error Retrieving Transaction Types After Saving!! Please Try Again!!!");
					} else {
						transactionTypesMap = getTransactionTypesMap(refTransactionTypesModel.getRefTransactionTypes());
						RefTablesModel refMerchantsModel = refTablesService.getRefMerchants(username, null);

						if (hasText(refMerchantsModel.getErrMsg())) {
							errors.add("Import Aborted! Error Retrieving Merchants!! Please Try Again!!!");
						} else {
							Map<String, RefMerchant> merchantsMap = getMerchantsMap(refMerchantsModel.getRefMerchants());
							List<String> saveMerchantErrors = saveMerchants(username, importTransactions, merchantsMap);
							errors.addAll(saveMerchantErrors);

							// retrieve merchants again to get updated list and map after saving
							refMerchantsModel = refTablesService.getRefMerchants(username, null);

							if (hasText(refMerchantsModel.getErrMsg())) {
								errors.add("Import Aborted! Error Retrieving Merchants After Saving!! Please Try Again!!!");
							} else {
								merchantsMap = getMerchantsMap(refMerchantsModel.getRefMerchants());
								AccountModel accountModel = accountService.getAccountsByUsername(username, null);

								if (hasText(accountModel.getErrMsg())) {
									errors.add("Import Aborted! Error Retrieving Accounts!! Please Try Again!!!");
								} else {
									Map<String, Account> accountsMap = getAccountsMap(accountModel.getAccounts());
									RefTablesModel refCategoriesModel = refTablesService.getRefCategories(username, null, false);

									if (hasText(refCategoriesModel.getErrMsg())) {
										errors.add("Import Aborted! Error Retrieving Categories!! Please Try Again!!!");
									} else {
										Map<String, RefCategory> categoriesMap = getCategoriesMap(refCategoriesModel.getRefCategories());
									
										validateImportTransactions(importTransactions, errors);
										List<Transaction> transactions = transactionsToImport(username, importTransactions, 
												transactionTypesMap, merchantsMap, accountsMap, categoriesMap);
										validateTransactions(transactions, errors);
										
										List<String> saveTransactionErrors = saveTransactions(username, transactions);
										errors.addAll(saveTransactionErrors);
									}
								}
							}
						}
					}
				}
			}
		}

		logger.info("Import Transactions Finish: {} || {}", username, errors);
		return errors;
	}
	
	/**
	 * Imports Category Types and then Categories
	 * @param username
	 * @param multipartFile
	 * @return list of failures
	 */
	public List<String> importCategories(String username, MultipartFile multipartFile) {
		logger.info("Import Categories Start: {}", username);
		List<String> errors = new ArrayList<>();
		
		if (!isValidCsvFile(multipartFile)) {
			errors.add("Import Aborted! File Not CSV!! Please Try Again!!!");
		} else {
			List<ImportCategory> importCategories = parseCategoriesCsv(username, multipartFile);
			
			if (isEmpty(importCategories)) {
				errors.add("Import Aborted! Error Parsing CSV!! Confirm Header Names and Please Try Again!!!");
			} else {
				RefTablesModel refCategoryTypesModel = refTablesService.getRefCategoryTypes(username, false);
				
				if (hasText(refCategoryTypesModel.getErrMsg())) {
					errors.add("Import Aborted! Error Retrieving Category Types!! Please Try Again!!!");
				} else {
					Map<String, String> categoryTypesMap = getCategoryTypesMap(refCategoryTypesModel.getRefCategoryTypes());
					List<String> saveCategoryTypesError = saveCategoryTypes(username, importCategories, categoryTypesMap);
					errors.addAll(saveCategoryTypesError);
					
					// retrieve category types again to get updated list and map after saving
					refCategoryTypesModel = refTablesService.getRefCategoryTypes(username, false);
					
					if (hasText(refCategoryTypesModel.getErrMsg())) {
						errors.add("Import Aborted! Error Retrieving Category Types After Saving!! Please Try Again!!!");
					} else {
						categoryTypesMap = getCategoryTypesMap(refCategoryTypesModel.getRefCategoryTypes());
						RefTablesModel refCategoriesModel = refTablesService.getRefCategories(username, null, false);
						
						if (hasText(refCategoriesModel.getErrMsg())) {
							errors.add("Import Aborted! Error Retrieving Categories!! Please Try Again!!!");
						} else {
							Map<String, RefCategory> categoriesMap = getCategoriesMap(refCategoriesModel.getRefCategories());
							List<ImportCategory> categories = categoriesToImport(username, importCategories, categoryTypesMap, categoriesMap);
							validateCategories(categories, errors);
							
							List<String> saveCategoriesErrors = saveCategories(username, categories);
							errors.addAll(saveCategoriesErrors);
						}
					}
				}
			}
		}
		
		return errors;
	}
	
	/**
	 * Imports Account Types, Banks and then Accounts
	 * @param username
	 * @param multipartFile
	 * @return list of failures
	 */
	public List<String> importAccounts(String username, MultipartFile multipartFile) {
		logger.info("Import Accounts Start: {}", username);
		List<String> errors = new ArrayList<>();

		if (!isValidCsvFile(multipartFile)) {
			errors.add("Import Aborted! File Not CSV!! Please Try Again!!!");
		} else {
			List<ImportAccount> importAccounts = parseAccountsCsv(username, multipartFile);
			
			if (isEmpty(importAccounts)) {
				errors.add("Import Aborted! Error Parsing CSV!! Confirm Header Names and Please Try Again!!!");
			} else {
				RefTablesModel refAccountTypesModel = refTablesService.getRefAccountTypes(username);
				
				if (hasText(refAccountTypesModel.getErrMsg())) {
					errors.add("Import Aborted! Error Retrieving Account Types!! Please Try Again!!!");
				} else {
					Map<String, String> accountTypesMap = getAccountTypesMap(refAccountTypesModel.getRefAccountTypes());
					List<String> saveAccountTypesErrors = saveAccountTypes(username, accountTypesMap);
					errors.addAll(saveAccountTypesErrors);
					
					// retrieve account types again to get updated list and map after saving
					refAccountTypesModel = refTablesService.getRefAccountTypes(username);
					
					if (hasText(refAccountTypesModel.getErrMsg())) {
						errors.add("Import Aborted! Error Retrieving Account Types After Saving!! Please Try Again!!!");
					} else {
						accountTypesMap = getAccountTypesMap(refAccountTypesModel.getRefAccountTypes());
						
						RefTablesModel refBanksModel = refTablesService.getRefBanks(username);
						
						if (hasText(refBanksModel.getErrMsg())) {
							errors.add("Import Aborted! Error Retrieving Banks!! Please Try Again!!!");
						} else {
							Map<String, RefBank> banksMap = getBanksMap(refBanksModel.getRefBanks());
							List<String> saveBanksErrors = saveBanks(username, importAccounts, banksMap);
							errors.addAll(saveBanksErrors);
							
							// retrieve banks again to get updated list and map after saving
							refBanksModel = refTablesService.getRefBanks(username);
							if (hasText(refBanksModel.getErrMsg())) {
								errors.add("Import Aborted! Error Retrieving Banks After Saving!! Please Try Again!!!");
							} else {
								banksMap = getBanksMap(refBanksModel.getRefBanks());
								
								validateImportAccounts(importAccounts, errors);
								List<Account> accounts = accountsToImport(username, importAccounts, accountTypesMap, banksMap);
								validateAccounts(accounts, errors);
								
								List<String> saveAccountErrors = saveAccounts(username, accounts);
								errors.addAll(saveAccountErrors);
							}
						}
					}
				}
			}
		}
		
		return errors;
	}
	
	private List<ImportTransaction> parseTransactionsCsv(String username, MultipartFile multipartFile) {
		logger.info("Parse Transactions CSV: {}", username);
		List<ImportTransaction> importTransactions = new ArrayList<>();

		try (CSVParser csvParser = new CSVParser(
				new BufferedReader(new InputStreamReader(multipartFile.getInputStream())),
				CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {
			csvParser.getRecords()
					.forEach(csvRecord -> importTransactions.add(ImportTransaction.builder()
							.description(csvRecord.get("DESCRIPTION"))
							.accountName(csvRecord.get("ACCOUNT_NAME"))
							.trfAccountName(csvRecord.get("TRF_ACCOUNT_NAME"))
							.categoryName(csvRecord.get("CATEGORY_NAME"))
							.merchantName(csvRecord.get("MERCHANT_NAME"))
							.date(csvRecord.get("DATE"))
							.amount(csvRecord.get("AMOUNT"))
							.regular(csvRecord.get("REGULAR"))
							.necessary(csvRecord.get("NECESSARY"))
							.transactionTypeName(csvRecord.get("TRANSACTION_TYPE_NAME"))
							.build()));
		} catch (Exception ex) {
			logger.info("Parse Transactions CSV: {}", username, ex);
		}

		return importTransactions;
	}
	
	private List<ImportCategory> parseCategoriesCsv(String username, MultipartFile multipartFile) {
		logger.info("Parse Categories CSV: {}", username);
		List<ImportCategory> importCategories = new ArrayList<>();
		
		try (CSVParser csvParser = new CSVParser(
				new BufferedReader(new InputStreamReader(multipartFile.getInputStream())),
				CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {
			csvParser.getRecords()
					.forEach(csvRecord -> importCategories.add(ImportCategory.builder()
							.categoryTypeName(csvRecord.get("CATEGORY_TYPE_NAME"))
							.categoryName(csvRecord.get("CATEGORY_NAME"))
							.build()));
		} catch (Exception ex) {
			logger.info("Parse Categories CSV: {}", username, ex);
		}
		
		return importCategories;
	}
	
	private List<ImportAccount> parseAccountsCsv(String username, MultipartFile multipartFile) {
		logger.info("Parse Accounts CSV: {}", username);
		List<ImportAccount> importAccounts = new ArrayList<>();
		
		try (CSVParser csvParser = new CSVParser(
				new BufferedReader(new InputStreamReader(multipartFile.getInputStream())),
				CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {
			csvParser.getRecords()
			.forEach(csvRecord -> importAccounts.add(ImportAccount.builder()
					.bankName(csvRecord.get("BANK_NAME"))
					.accountTypeName(csvRecord.get("ACCOUNT_TYPE_NAME"))
					.accountName(csvRecord.get("ACCOUNT_NAME"))
					.openingBalance(csvRecord.get("OPENING_BALANCE"))
					.status(csvRecord.get("STATUS"))
					.build()));
		} catch (Exception ex) {
			logger.info("Parse Accounts CSV: {}", username, ex);
		}
		
		return importAccounts;
	}
	
	private Map<String, String> getTransactionTypesMap(List<RefTransactionType> refTransactionTypes) {
		return refTransactionTypes.stream()
				.collect(toMap(RefTransactionType::getDescription, RefTransactionType::getId));
	}
	
	private Map<String, RefMerchant> getMerchantsMap(List<RefMerchant> refMerchants) {
		return refMerchants.stream()
				.collect(toMap(RefMerchant::getDescription, refMerchant -> refMerchant));
	}

	private Map<String, Account> getAccountsMap(List<Account> accounts) {
		return accounts.stream()
				.collect(toMap(Account::getDescription, account -> account));
	}

	private Map<String, RefCategory> getCategoriesMap(List<RefCategory> refCategories) {
		return refCategories.stream()
				.collect(toMap(RefCategory::getDescription, refCategory -> refCategory));
	}
	
	private Map<String, String> getCategoryTypesMap(List<RefCategoryType> refCategoryTypes) {
		return refCategoryTypes.stream()
				.collect(toMap(RefCategoryType::getDescription, RefCategoryType::getId));
	}
	
	private Map<String, String> getAccountTypesMap(List<RefAccountType> refAccountTypes) {
		return refAccountTypes.stream()
				.collect(toMap(RefAccountType::getDescription, RefAccountType::getId));
	}
	
	private Map<String, RefBank> getBanksMap(List<RefBank> refBanks) {
		return refBanks.stream()
				.collect(toMap(RefBank::getDescription, refBank -> refBank));
	}
	
	private void validateImportTransactions(List<ImportTransaction> importTransactions, List<String> errors) {
		List<ImportTransaction> filteredImportTransactions = importTransactions.stream()
				.filter(importTransaction -> isAmountValid(importTransaction.getAmount(), importTransaction.getDate(), errors))
				.filter(importTransaction -> isDateValid(importTransaction.getAmount(), importTransaction.getDate(), errors) != null)
				.collect(toList());
		
		importTransactions.clear();
		importTransactions.addAll(filteredImportTransactions);
	}
	
	private boolean isAmountValid(String amount, String date, List<String> errors) {
		try {
			new BigDecimal(amount);
			return true;
		} catch (NumberFormatException | NullPointerException ex) {
			errors.add(String.format("Invalid Amount in Transaction: %s | %s", date, amount));
			return false;
		}
	}
	
	/**
	 * Excel CSV stores date in MM/DD/YYYY format
	 * But it can be M/DD/YYYY or MM/D/YYYY or M/D/YYYY based on date
	 * So, need to check them all
	 * @param amount
	 * @param date
	 * @param errors
	 * @return Date in YYYY-MM-DD format if valid, null if invalid
	 */
	private String isDateValid(String amount, String date, List<String> errors) {
		try {
			return LocalDate.parse(date, ofPattern(DEFAULT_FORMAT)).toString();
		} catch (NullPointerException | DateTimeParseException ex1) {
			try {
				return LocalDate.parse(date, ofPattern(DEFAULT_EXPECTED)).toString();
			} catch (NullPointerException | DateTimeParseException ex2) {
				try {
					return LocalDate.parse(date, ofPattern(FORMAT_ONE)).toString();
				} catch (NullPointerException | DateTimeParseException ex3) {
					try {
						return LocalDate.parse(date, ofPattern(FORMAT_TWO)).toString();
					} catch (NullPointerException | DateTimeParseException ex4) {
						try {
							return LocalDate.parse(date, ofPattern(FORMAT_THREE)).toString();
						} catch (NullPointerException | DateTimeParseException ex5) {
							errors.add(String.format("Invalid Date in Transaction: %s | %s", date, amount));
							return null;
						}
					}
				}
			}
		}
	}
	
	private void validateTransactions(List<Transaction> transactions, List<String> errors) {
		List<Transaction> filteredTransactions = transactions.stream()
				.filter(transaction -> isTransactionValid(transaction, errors))
				.collect(toList());
		
		transactions.clear();
		transactions.addAll(filteredTransactions);
	}
	
	private boolean isTransactionValid(Transaction transaction, List<String> errors) {
		TransactionModel transactionModel = TransactionModel.builder()
				.transaction(transaction)
				.userAction(USER_ACTION_ADD)
				.build();
		
		BindingResult bindingResult = new BeanPropertyBindingResult(transactionModel, "transactionModel");
		transactionValidator.validate(transactionModel, bindingResult);
		
		if (bindingResult.hasErrors()) {
			errors.add(String.format("Transaction Validation Error (Required Filed Missing/Empty/Invalid): %s | %s", transaction.getDate(), transaction.getAmount()));
		}
		
		return !bindingResult.hasErrors();
	}
	
	private void validateCategories(List<ImportCategory> importCategories, List<String> errors) {
		List<ImportCategory> filteredImportCategories = importCategories.stream()
				.filter(importCategory -> isCategoryTypeValid(importCategory.getCategoryTypeId(), 
						importCategory.getCategoryName(), errors))
				.collect(toList());
		
		importCategories.clear();
		importCategories.addAll(filteredImportCategories);
	}
	
	private boolean isCategoryTypeValid(String categoryTypeId, String categoryName, List<String> errors) {
		if (hasText(categoryTypeId)) {
			return true;
		} else {
			errors.add(String.format("Invalid Category Type in Category: %s", categoryName));
			return false;
		}
	}
	
	private void validateImportAccounts(List<ImportAccount> importAccounts, List<String> errors) {
		List<ImportAccount> filteredImportAccounts = importAccounts.stream()
				.filter(importAccount -> isOpeningBalanceValid(importAccount.getOpeningBalance(), importAccount.getAccountName(), errors))
				.collect(toList());
		
		importAccounts.clear();
		importAccounts.addAll(filteredImportAccounts);
	}
	
	private boolean isOpeningBalanceValid(String amount, String accountName, List<String> errors) {
		try {
			new BigDecimal(amount);
			return true;
		} catch (NumberFormatException | NullPointerException ex) {
			errors.add(String.format("Invalid Opening Balance in Account: %s", accountName));
			return false;
		}
	}
	
	private void validateAccounts(List<Account> accounts, List<String> errors) {
		List<Account> filteredAccounts = accounts.stream()
				.filter(account -> isAccountValid(account, errors))
				.collect(toList());
		
		accounts.clear();
		accounts.addAll(filteredAccounts);
	}
	
	private boolean isAccountValid(Account account, List<String> errors) {
		AccountModel accountModel = AccountModel.builder()
				.account(account)
				.userAction(USER_ACTION_ADD)
				.build();
		
		BindingResult bindingResult = new BeanPropertyBindingResult(accountModel, "accountModel");
		accountValidator.validate(accountModel, bindingResult);
		
		if (bindingResult.hasErrors()) {
			errors.add(String.format("Account Validation Error (Required Filed Missing/Empty/Invalid): %s", account.getDescription()));
		}
		
		return !bindingResult.hasErrors();
	}
	
	private List<Transaction> transactionsToImport(String username, List<ImportTransaction> importTransactions, Map<String, String> transactionTypesMap,
			Map<String, RefMerchant> merchantsMap, Map<String, Account> accountsMap, Map<String, RefCategory> categoriesMap) {
		return importTransactions.stream()
				.map(importTransaction -> Transaction.builder()
						.description(importTransaction.getDescription())
						.account(accountsMap.get(importTransaction.getAccountName()))
						.trfAccount(accountsMap.get(importTransaction.getTrfAccountName()))
						.refTransactionType(RefTransactionType.builder()
								.id(transactionTypesMap.get(importTransaction.getTransactionTypeName()))
								.build())
						.refCategory(categoriesMap.get(importTransaction.getCategoryName()))
						.refMerchant(merchantsMap.get(importTransaction.getMerchantName()))
						.user(AppUser.builder()
								.username(username)
								.build())
						.date(isDateValid(importTransaction.getAmount(), importTransaction.getDate(), null))
						.amount(new BigDecimal(importTransaction.getAmount()))
						.regular(Boolean.valueOf(importTransaction.getRegular()))
						.necessary(Boolean.valueOf(importTransaction.getNecessary()))
						.build())
				.collect(toList());
	}
	
	private List<ImportCategory> categoriesToImport(String username, List<ImportCategory> importCategories, 
			Map<String, String> categoryTypesMap, Map<String, RefCategory> categoriesMap) {
		return importCategories.stream()
				.filter(importCategory -> categoriesMap.get(importCategory.getCategoryName()) == null)
				.map(importCategory -> ImportCategory.builder()
						.categoryName(importCategory.getCategoryName())
						.categoryTypeId(categoryTypesMap.get(importCategory.getCategoryTypeName()))
						.build())
				.collect(toList());
	}
	
	private List<Account> accountsToImport(String username, List<ImportAccount> importAccounts, 
			Map<String, String> accountTypesMap, Map<String, RefBank> banksMap) {
		return importAccounts.stream()
				.map(importAccount -> Account.builder()
						.refAccountType(RefAccountType.builder()
								.id(accountTypesMap.get(importAccount.getAccountTypeName()))
								.build())
						.refBank(banksMap.get(importAccount.getBankName()))
						.description(importAccount.getAccountName())
						.user(AppUser.builder()
								.username(username)
								.build())
						.openingBalance(new BigDecimal(importAccount.getOpeningBalance()))
						.status(importAccount.getStatus())
						.build())
				.collect(toList());
	}
	
	private List<String> saveTransactionTypes(String username, Map<String, String> transactionTypesMap) {
		logger.info("Save Transaction Types: {}", username);
		List<String> errors = new ArrayList<>();
		
		TRANSACTION_TYPES.forEach(transactionType -> {
			if (transactionTypesMap.get(transactionType) == null) {
				RefTablesModel refTablesModel = refTablesService.saveRefTransactionType(username, transactionType);
				
				if (hasText(refTablesModel.getErrMsg())) {
					errors.add(String.format("Error Saving Transaction Type: %s", transactionType));
				}
			}
		});
		
		return errors;
	}

	private List<String> saveMerchants(String username, 
			List<ImportTransaction> importTransactions, Map<String, RefMerchant> merchantsMap) {
		logger.info("Save Merchants: {}", username);
		List<String> errors = new ArrayList<>();
		
		Set<String> merchantNames = importTransactions.stream()
				.filter(importTransaction -> merchantsMap.get(importTransaction.getMerchantName()) == null)
				.filter(importTransaction -> hasText(importTransaction.getMerchantName()))
				.map(ImportTransaction::getMerchantName)
				.collect(toSet());
		
		merchantNames.forEach(merchantName -> {
			RefTablesModel refTablesModel = refTablesService.saveNewRefMerchant(username, merchantName);

			if (hasText(refTablesModel.getErrMsg())) {
				errors.add(String.format("Error Saving Merchant: %s", merchantName));
			}
		});
		
		return errors;
	}
	
	private List<String> saveTransactions(String username, List<Transaction> transactions) {
		logger.info("Save Transactions: {}", username);
		List<String> errors = new ArrayList<>();
		
		transactions.forEach(transaction -> {
			TransactionModel transactionModel = transactionService.saveNewTransaction(username, transaction, null);
			
			if (hasText(transactionModel.getErrMsg())) {
				errors.add(String.format("Error Saving Transaction: %s | %s", transaction.getDate(), transaction.getAmount()));
			}
		});
		
		return errors;
	}
	
	private List<String> saveCategoryTypes(String username, List<ImportCategory> importCategories, 
			Map<String, String> categoryTypesMap) {
		logger.info("Save Category Types: {}", username);
		List<String> errors = new ArrayList<>();
		
		Set<String> categoryTypeNames = importCategories.stream()
				.filter(importCategory -> categoryTypesMap.get(importCategory.getCategoryTypeName()) == null)
				.filter(importCategory -> hasText(importCategory.getCategoryTypeName()))
				.map(ImportCategory::getCategoryTypeName)
				.collect(toSet());
		
		categoryTypeNames.forEach(categoryTypeName -> {
			RefTablesModel refTablesModel = refTablesService.saveRefCategoryType(username, categoryTypeName);
			
			if (hasText(refTablesModel.getErrMsg())) {
				errors.add(String.format("Error Saving Category Type: %s", categoryTypeName));
			}
		});
		
		return errors;
	}
	
	private List<String> saveCategories(String username, List<ImportCategory> importCategories) {
		logger.info("Save Categories: {}", username);
		List<String> errors = new ArrayList<>();
		
		importCategories.forEach(category -> {
			RefTablesModel refTablesModel = refTablesService.saveRefCategory(username, 
					category.getCategoryName(), category.getCategoryTypeId());
			
			if (hasText(refTablesModel.getErrMsg())) {
				errors.add(String.format("Error Saving Category: %s", category));
			}
		});
		
		return errors;
	}
	
	private List<String> saveAccountTypes(String username, Map<String, String> accountTypesMap) {
		logger.info("Save Account Types: {}", username);
		List<String> errors = new ArrayList<>();
		
		ACCOUNT_TYPES.forEach(accountType -> {
			if (accountTypesMap.get(accountType) == null) {
				RefTablesModel refTablesModel = refTablesService.saveRefAccountType(username, accountType);
				
				if (hasText(refTablesModel.getErrMsg())) {
					errors.add(String.format("Error Saving Account Type: %s", accountType));
				}
			}
		});
		
		return errors;
	}
	
	private List<String> saveBanks(String username, List<ImportAccount> importAccounts, Map<String, RefBank> refBanksMap) {
		logger.info("Save Banks: {}", username);
		List<String> errors = new ArrayList<>();
		
		Set<String> bankNames = importAccounts.stream()
				.filter(importAccount -> refBanksMap.get(importAccount.getBankName()) == null)
				.filter(importAccount -> hasText(importAccount.getBankName()))
				.map(ImportAccount::getBankName)
				.collect(toSet());
		
		bankNames.forEach(bankName -> {
			RefTablesModel refTablesModel = refTablesService.saveRefBank(username, bankName);
			
			if (hasText(refTablesModel.getErrMsg())) {
				errors.add(String.format("Error Saving Bank: %s", bankName));
			}
		});
		
		return errors;
	}
	
	private List<String> saveAccounts(String username, List<Account> accounts) {
		logger.info("Save Accounts: {}", username);
		List<String> errors = new ArrayList<>();
		
		accounts.forEach(account -> {
			AccountModel accountModel = accountService.saveNewAccount(username, account);
			
			if (hasText(accountModel.getErrMsg())) {
				errors.add(String.format("Error Saving Account: %s", account.getDescription()));
			}
		});
		
		return errors;
	}
}
