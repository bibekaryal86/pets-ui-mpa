package pets.ui.mpa.validator;

import static pets.ui.mpa.service.AccountService.isAccountTypeCompatibleWithBankType;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_DELETE;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pets.ui.mpa.model.Account;
import pets.ui.mpa.model.AccountModel;

@Component
public class AccountValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Account.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AccountModel accountModel = (AccountModel) target;

		if (accountModel == null || accountModel.getAccount() == null) {
			errors.rejectValue("errMsg", "error.null.update", new Object[] { " Account " }, "");
		} else if (!accountModel.getUserAction().equals(USER_ACTION_DELETE)) {
			rejectIfEmptyOrWhitespace(errors, "account.refAccountType.id", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "account.refBank.id", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "account.description", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "account.status", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "account.openingBalance", "error.required.input");

			if (!isAccountTypeCompatibleWithBankType(accountModel.getAccount())) {
				errors.rejectValue("errMsg", "error.account.type");
			}
		}
	}
}
