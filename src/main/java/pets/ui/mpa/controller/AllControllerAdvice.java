package pets.ui.mpa.controller;

import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_PAGE_NOT_FOUND;
import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_PARAM;
import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_IMPORT_ERROR;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AllControllerAdvice {

	@ExceptionHandler(NoHandlerFoundException.class)
	public String showPageNotFoundMessage(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(HOME_ACTION_PARAM, HOME_ACTION_PAGE_NOT_FOUND);
		return "redirect:main.pets";
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String showFileSizeErrorMessage(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(HOME_ACTION_PARAM, HOME_ACTION_IMPORT_ERROR);
		return "redirect:import.pets";
	}
}
