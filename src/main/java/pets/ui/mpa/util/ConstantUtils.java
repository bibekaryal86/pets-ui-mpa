package pets.ui.mpa.util;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantUtils {

	public static final String LOG_MASKER_PATTERN = "(.*?)"; 
	public static final String DATE_FORMATTER_PATTERN = "yyyy-MM-dd";
	public static final Integer YEAR_ZERO = 2020;

	public static final String GENERIC_ERROR_MESSAGE = "ERROR";
	public static final String GENERIC_SUCCESS_MESSAGE = "SUCCESS";
	public static final String GENERIC_FIRST_OPTION = "PLEASE SELECT";

	public static final String HOME_ACTION_PARAM = "HOME_ACTION";
	public static final String HOME_ACTION_LOG_OFF = "LOG_OFF";
	public static final String HOME_ACTION_INVALID = "INVALID";
	public static final String HOME_ACTION_PAGE_NOT_FOUND = "NOT_FOUND";
	public static final String HOME_ACTION_IMPORT_ERROR = "IMPORT_FILE_SIZE";

	public static final String USER_ACTION_UPDATE = "UPDATE";
	public static final String USER_ACTION_DELETE = "DELETE";
	public static final String USER_ACTION_ADD = "ADD";

	public static final String FILTER_ACCOUNTS_OBJECT_NAME = "FILTER_ACCOUNTS";
	public static final String FILTER_TRANSACTIONS_OBJECT_NAME = "FILTER_TRANSACTIONS";
	public static final String FILTER_ACTION_GET = "GET";
	public static final String FILTER_ACTION_SET = "SET";
	public static final String FILTER_ACTION_RESET = "RESET";

	public static final Map<String, String> GET_HOME_ACTION_MAP() {
		Map<String, String> HOME_ACTION_MAP = new HashMap<>();
		HOME_ACTION_MAP.put(HOME_ACTION_LOG_OFF, "Successfully Logged Off, Please Log In Again to Continue!");
		HOME_ACTION_MAP.put(HOME_ACTION_INVALID, "Invalid Username and/or Password, Please Try Again!");
		HOME_ACTION_MAP.put(HOME_ACTION_PAGE_NOT_FOUND, "The Requested Page is Under Construction and Not Available");
		HOME_ACTION_MAP.put(HOME_ACTION_IMPORT_ERROR, "Only Upto 1MB File Size is Allowed to Import! Please Try Again!!!");
		return HOME_ACTION_MAP;
	}

	public static final String ACCOUNT_TYPE_ID_CASH = "5ede4cbb0525eb78290332e4";
	public static final String ACCOUNT_TYPE_ID_CREDIT_CARD = "5ede4cf30525eb78290332e7";
    public static final String ACCOUNT_TYPE_ID_LOANS_MORTGAGES = "5ede4d080525eb78290332e8";
    public static final String ACCOUNT_TYPE_ID_OTHER_LOANS = "5ede4d1d0525eb78290332ea";
	public static final String BANK_ID_CASH = "5ede4d510525eb78290332eb";
	public static final String CATEGORY_TYPE_ID_TRANSFER = "5ede589097efcd0315ea06e6";
	public static final String MERCHANT_ID_TRANSFER = "5f9f861c083c2023ef009a9a";
	public static final String TRANSACTION_TYPE_ID_EXPENSE = "5ede664746fa58038df1b422";
    public static final String TRANSACTION_TYPE_ID_TRANSFER = "5ede664e46fa58038df1b423";

	public static final List<String> ACCOUNT_STATUSES = asList("ACTIVE", "CLOSED");
	public static final List<String> ACCOUNT_TYPES_LOAN_ACCOUNTS = asList(ACCOUNT_TYPE_ID_CREDIT_CARD,
            ACCOUNT_TYPE_ID_LOANS_MORTGAGES, ACCOUNT_TYPE_ID_OTHER_LOANS);
}
