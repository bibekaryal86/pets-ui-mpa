package pets.ui.mpa.controller;

import static pets.ui.mpa.util.ConstantUtils.GENERIC_ERROR_MESSAGE;
import static pets.ui.mpa.util.ConstantUtils.GENERIC_FIRST_OPTION;
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

import pets.ui.mpa.model.RefCategoryType;
import pets.ui.mpa.model.RefTablesModel;
import pets.ui.mpa.service.RefTablesService;

@Controller
public class RefCategoriesController {

	private static Logger logger = LoggerFactory.getLogger(RefCategoriesController.class);

	private final RefTablesService refTablesService;

	public RefCategoriesController(RefTablesService refTablesService) {
		this.refTablesService = refTablesService;
	}

	@GetMapping(value = { "categories.pets" })
	public String initializeRefCategories(HttpServletRequest httpServletRequest, ModelMap modelMap,
			@RequestParam(value = "categoryTypeId", required = false) String categoryTypeId) {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Ref Categories: {} || {}", httpServletRequest.getRemoteAddr(), username);

		modelMap.addAttribute("filterCategoryType", hasText(categoryTypeId) ? categoryTypeId : "");

		RefTablesModel refTablesModel = refTablesService.getRefCategories(username, categoryTypeId, false);

		if (hasText(refTablesModel.getErrMsg())) {
			modelMap.addAttribute("errMsg", refTablesModel.getErrMsg());
		} else {
			modelMap.addAttribute("refCategoriesList", refTablesModel.getRefCategories());
		}

		return "categories";
	}
	
	@ModelAttribute("refCategoryTypesList")
	public List<RefCategoryType> modelAttributeRefCategoryTypesList(HttpServletRequest httpServletRequest) {
		List<RefCategoryType> refCategoryTypesList = new ArrayList<>();
		RefTablesModel refTablesModel = refTablesService.getRefCategoryTypes(getCurrentUsername(httpServletRequest), false);
		
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
}
