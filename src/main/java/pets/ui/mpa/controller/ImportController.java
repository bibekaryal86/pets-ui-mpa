package pets.ui.mpa.controller;

import static pets.ui.mpa.util.ConstantUtils.GET_HOME_ACTION_MAP;
import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_PARAM;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static java.util.Arrays.asList;
import static org.springframework.util.CollectionUtils.isEmpty;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pets.ui.mpa.service.ImportService;

@Controller
public class ImportController {

	private static final Logger logger = LoggerFactory.getLogger(ImportController.class);
	
	private static final String CATEGORY = "CATEGORY";
	private static final String ACCOUNT = "ACCOUNT";
	private static final String TRANSACTION = "TRANSACTION";
	private static final List<String> IMPORT_NAMES = asList(CATEGORY, ACCOUNT, TRANSACTION);

	private final ImportService importService;

	public ImportController(ImportService importService) {
		this.importService = importService;
	}

	@GetMapping(value = { "import.pets" })
	public String initializeImport(HttpServletRequest httpServletRequest, ModelMap modelMap,
			@ModelAttribute(value = HOME_ACTION_PARAM) String homeAction) throws Exception {
		logger.info("Initialize Import: {} || {} || {}", httpServletRequest.getRemoteAddr(),
				getCurrentUsername(httpServletRequest), homeAction);

		if (hasText(homeAction)) {
			modelMap.addAttribute("errMsg", GET_HOME_ACTION_MAP().get(homeAction));
		}

		return "import";
	}

	@PostMapping(value = { "import.pets" })
	public String processImport(HttpServletRequest httpServletRequest, ModelMap modelMap,
			@RequestParam final MultipartFile file, @RequestParam String name) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Process Import: {} || {} || {}", httpServletRequest.getRemoteAddr(), username, name);
		
		if (!hasText(name) || !IMPORT_NAMES.contains(name)) {
			modelMap.addAttribute("errMsg", "Invalid Selection! Please Try Again!!!");
		} else {
			List<String> errors = new ArrayList<>();
			
			if (name.equals(TRANSACTION)) {
				errors = importService.importTransactions(username, file);
			} else if (name.equals(CATEGORY)) {
				errors = importService.importCategories(username, file);
			} else if (name.equals(ACCOUNT)) {
				errors = importService.importAccounts(username, file);
			}

			if (!isEmpty(errors)) {
				modelMap.addAttribute("selectedName", name);
				modelMap.addAttribute("errors", errors);
			}
		}

		return "import";
	}
	
	@ModelAttribute("namesList")
	public List<String> modelAttributeNamesList() {
		return IMPORT_NAMES;
	}
}
