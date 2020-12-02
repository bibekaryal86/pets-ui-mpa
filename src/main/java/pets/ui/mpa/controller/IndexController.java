package pets.ui.mpa.controller;

import static pets.ui.mpa.util.ConstantUtils.GET_HOME_ACTION_MAP;
import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_LOG_OFF;
import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_PARAM;
import static pets.ui.mpa.util.SessionHelper.getCurrentUsername;
import static pets.ui.mpa.util.SessionHelper.setFooterDisplay;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.util.StringUtils.hasText;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pets.ui.mpa.model.IndexModel;

@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@GetMapping(value = { "/" })
	public String initializeIndex(HttpServletRequest httpServletRequest) {
		logger.info("Initialize Index: {} || {}", httpServletRequest.getRemoteAddr(),
				getCurrentUsername(httpServletRequest));

		setFooterDisplay(httpServletRequest);

		return "redirect:main.pets";
	}

	@GetMapping(value = { "home.pets" })
	public String initializePetsHome(HttpServletRequest httpServletRequest, ModelMap model,
			@ModelAttribute(value = HOME_ACTION_PARAM) String homeAction) throws Exception {
		logger.info("Initialize Pets Home: {} || {}", httpServletRequest.getRemoteAddr(),
				getCurrentUsername(httpServletRequest));

		setFooterDisplay(httpServletRequest);
		
		Authentication authentication = getContext().getAuthentication();
		if (authentication != null && !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
			return "main";
		}

		if (hasText(homeAction)) {
			model.addAttribute("errMsg", GET_HOME_ACTION_MAP().get(homeAction));
		}
		
		return "home";
	}

	@GetMapping(value = { "logoff.pets" })
	public String initializePetsUserLogOff(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, RedirectAttributes redirectAttributes) throws Exception {
		logger.info("Initialize Pets User Log Off: {} || {}", httpServletRequest.getRemoteAddr(),
				getCurrentUsername(httpServletRequest));

		Authentication authentication = getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
		}

		redirectAttributes.addFlashAttribute(HOME_ACTION_PARAM, HOME_ACTION_LOG_OFF);
		return "redirect:home.pets";
	}

	@GetMapping(value = { "closePopupAndRefresh.pets" })
	public String initializeClosePopupAndRefresh(HttpServletRequest httpServletRequest, ModelMap model,
			@ModelAttribute(value = "redirectFrom") String redirectFrom) throws Exception {
		logger.info("Initialize Close Popup And Refresh: {} || {} ||", httpServletRequest.getRemoteAddr(),
				getCurrentUsername(httpServletRequest), redirectFrom);
		model.addAttribute("closePopupAndRefresh", new IndexModel());
		return "closePopupAndRefresh";
	}

	@GetMapping(value = { "main.pets" })
	public String initializeMainSummary(HttpServletRequest httpServletRequest, ModelMap model,
			@ModelAttribute(value = HOME_ACTION_PARAM) String homeAction) throws Exception {
		String username = getCurrentUsername(httpServletRequest);
		logger.info("Initialize Main Summary: {} || {} ||", httpServletRequest.getRemoteAddr(), username, homeAction);

		if (hasText(homeAction)) {
			model.addAttribute("errMsg", GET_HOME_ACTION_MAP().get(homeAction));
		}
		
		model.addAttribute("main", new IndexModel());
		return "main";
	}
}
