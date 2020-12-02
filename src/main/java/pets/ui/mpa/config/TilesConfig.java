package pets.ui.mpa.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.request.Request;

public class TilesConfig implements DefinitionsFactory {

	private static final Map<String, Definition> TILES_DEFINITIONS = new HashMap<>();

	@Override
	public Definition getDefinition(String name, Request request) {
		return TILES_DEFINITIONS.get(name);
	}

	public static void addDefinitions() {
		addDefaultDefinition("home", "Personal Expenses Tracking System : Welcome",
				"/WEB-INF/views/jsps/petsHome_body.jsp");
		addDefaultDefinition("main", "Personal Expenses Tracking System : Main Summary",
				"/WEB-INF/views/jsps/mainSummary_body.jsp");
		addDefaultDefinition("closePopupAndRefresh", "Personal Expenses Tracking System : Close Popup",
				"/WEB-INF/views/jsps/closePopupAndRefresh_body.jsp");
		addDefaultDefinition("merchant", "Personal Expenses Tracking System : Merchant Details",
				"/WEB-INF/views/jsps/refMerchant_body.jsp");
		addDefaultDefinition("merchants", "Personal Expenses Tracking System : Merchants List",
				"/WEB-INF/views/jsps/refMerchants_body.jsp");
		addDefaultDefinition("categories", "Personal Expenses Tracking System : Categories List",
				"/WEB-INF/views/jsps/refCategories_body.jsp");
		addDefaultDefinition("accounts", "Personal Expenses Tracking System : Accounts List",
				"/WEB-INF/views/jsps/accounts_body.jsp");
		addDefaultDefinition("account", "Personal Expenses Tracking System : Account Details",
				"/WEB-INF/views/jsps/account_body.jsp");
		addDefaultDefinition("transactions", "Personal Expenses Tracking System : Transactions List", 
				"/WEB-INF/views/jsps/transactions_body.jsp");
		addDefaultDefinition("import", "Personal Expenses Tracking System : Import", 
				"/WEB-INF/views/jsps/import_body.jsp");
		addDefaultDefinition("transaction", "Personal Expenses Tracking System : Transaction Details", 
				"/WEB-INF/views/jsps/transaction_body.jsp");
		addDefaultDefinition("reports", "Personal Expenses Tracking System : Reports", 
				"/WEB-INF/views/jsps/reports_body.jsp");
	}

	private static void addDefaultDefinition(String name, String title, String body) {
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put("title", new Attribute(title));
		attributes.put("header", new Attribute("/WEB-INF/views/tiles/header.jsp"));
		attributes.put("body", new Attribute(body));
		attributes.put("footer", new Attribute("/WEB-INF/views/tiles/footer.jsp"));
		attributes.put("include", new Attribute("/WEB-INF/views/tiles/include.jsp"));

		Attribute baseTemplate = new Attribute("/WEB-INF/views/tiles/template.jsp");

		TILES_DEFINITIONS.put(name, new Definition(name, baseTemplate, attributes));
	}
}
