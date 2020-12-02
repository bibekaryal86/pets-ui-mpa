package pets.ui.mpa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class EndpointsConfig {

	@Bean
	public String getAccountByIdUrl(@Value("${account_get_by_id}") String getAccountByIdUrl) {
		return getAccountByIdUrl;
	}

	@Bean
	public String getAccountsByUsernameUrl(@Value("${accounts_get_by_username}") String getAccountsByUsernameUrl) {
		return getAccountsByUsernameUrl;
	}

	@Bean
	public String editAccountUrl(@Value("${account_save_update_delete}") String editAccountUrl) {
		return editAccountUrl;
	}

	@Bean
	public String getRefAccountTypesUrl(@Value("${account_types_get_all}") String getRefAccountTypesUrl) {
		return getRefAccountTypesUrl;
	}

	@Bean
	public String getRefBanksUrl(@Value("${banks_get_all}") String getRefBanksUrl) {
		return getRefBanksUrl;
	}

	@Bean
	public String getRefCategoriesUrl(@Value("${categories_get_all}") String getRefCategoriesUrl) {
		return getRefCategoriesUrl;
	}
	
	@Bean
	public String saveNewCategoryUrl(@Value("${category_save_new}") String saveNewCategoryUrl) {
		return saveNewCategoryUrl;
	}

	@Bean
	public String getRefCategoryTypesUrl(@Value("${category_types_get_all}") String getRefCategoryTypesUrl) {
		return getRefCategoryTypesUrl;
	}
	
	@Bean
	public String getRefMerchantByIdUrl(@Value("${merchant_get_by_id}") String getRefMerchantByIdUrl) {
		return getRefMerchantByIdUrl;
	}

	@Bean
	public String getRefMerchantsByUsernameUrl(@Value("${merchants_get_by_username}") String getRefMerchantsByUsernameUrl) {
		return getRefMerchantsByUsernameUrl;
	}

	@Bean
	public String editMerchantUrl(@Value("${merchant_save_update_delete}") String editMerchantUrl) {
		return editMerchantUrl;
	}
	
	@Bean
	public String pingTestDatabaseUrl(@Value("${ping_test_database}") String pingTestDatabaseUrl) {
		return pingTestDatabaseUrl;
	}
	
	@Bean
	public String pingTestServiceUrl(@Value("${ping_test_service}") String pingTestServiceUrl) {
		return pingTestServiceUrl;
	}
	
	@Bean
	public String getCashFlowsReportUrl(@Value("${report_cash_flows}") String getCashFlowsReportUrl) {
		return getCashFlowsReportUrl;
	}
	
	@Bean
	public String getCategoriesReportUrl(@Value("${report_categories}") String getCategoriesReportUrl) {
		return getCategoriesReportUrl;
	}
	
	@Bean
	public String getCurrentBalancesReportUrl(@Value("${report_current_balances}") String getCurrentBalancesReportUrl) {
		return getCurrentBalancesReportUrl;
	}
	
	@Bean
	public String getTransactionByIdUrl(@Value("${transaction_get_by_id}") String getTransactionByIdUrl) {
		return getTransactionByIdUrl;
	}

	@Bean
	public String getTransactionsByUsernameUrl(@Value("${transactions_get_by_username}") String getTransactionsByUsernameUrl) {
		return getTransactionsByUsernameUrl;
	}

	@Bean
	public String editTransactionUrl(@Value("${transaction_save_update_delete}") String editTransactionUrl) {
		return editTransactionUrl;
	}

	@Bean
	public String getRefTransactionTypesUrl(@Value("${transaction_types_get_all}") String getRefTransactionTypesUrl) {
		return getRefTransactionTypesUrl;
	}

	@Bean
	public String getUserByUsernameUrl(@Value("${user_get_by_username}") String getUserByUsernameUrl) {
		return getUserByUsernameUrl;
	}
}
