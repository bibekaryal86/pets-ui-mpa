package pets.ui.mpa.validator;

import static pets.ui.mpa.service.TransactionService.isTransactionTypeTransfer;
import static pets.ui.mpa.service.TransactionService.isValidTransferTransactionAccount;
import static pets.ui.mpa.service.TransactionService.isValidTransferTransactionCategory;
import static pets.ui.mpa.util.ConstantUtils.DATE_FORMATTER_PATTERN;
import static pets.ui.mpa.util.ConstantUtils.USER_ACTION_DELETE;
import static java.lang.Character.isLetterOrDigit;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.StringUtils.hasText;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pets.ui.mpa.model.Transaction;
import pets.ui.mpa.model.TransactionModel;

@Component
public class TransactionValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Transaction.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TransactionModel transactionModel = (TransactionModel) target;

		if (transactionModel == null || transactionModel.getTransaction() == null) {
			errors.rejectValue("errMsg", "error.null.update", new Object[] { " Transaction " }, "");
		} else if (!transactionModel.getUserAction().equals(USER_ACTION_DELETE)) {
			rejectIfEmptyOrWhitespace(errors, "transaction.account.id", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "transaction.refCategory.refCategoryType.id", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "transaction.refCategory.id", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "transaction.refTransactionType.id", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "transaction.date", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "transaction.amount", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "transaction.regular", "error.required.input");
			rejectIfEmptyOrWhitespace(errors, "transaction.necessary", "error.required.input");
			
			boolean isTransferTransaction = isTransactionTypeTransfer(transactionModel.getTransaction());
			
			if (isTransferTransaction) {
				rejectIfEmptyOrWhitespace(errors, "transaction.trfAccount.id", "error.required.input");
				
				if (!isValidTransferTransactionAccount(transactionModel.getTransaction())) {
					errors.rejectValue("errMsg", "error.transaction.transfer.accounts");
				}
				
				if (!isValidTransferTransactionCategory(transactionModel.getTransaction())) {
					errors.rejectValue("errMsg", "error.transaction.transfer.category");
				}
			} else {
				if (!hasText(transactionModel.getNewMerchant())) {
					rejectIfEmptyOrWhitespace(errors, "transaction.refMerchant.id", "error.required.input");
				} else {
					if (!isLetterOrDigit(transactionModel.getNewMerchant().charAt(0))) {
						errors.rejectValue("newMerchant", "error.transaction.merchant");
					}
				}
			}

			if (hasText(transactionModel.getTransaction().getDate())) {
				try {
					parse(transactionModel.getTransaction().getDate(), ofPattern(DATE_FORMATTER_PATTERN));
				} catch (DateTimeParseException ex) {
					errors.rejectValue("transaction.date", "error.transaction.date.format");
				}
			}
		}
	}
}
