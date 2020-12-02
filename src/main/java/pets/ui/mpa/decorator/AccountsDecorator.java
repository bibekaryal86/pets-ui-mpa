package pets.ui.mpa.decorator;

import static pets.ui.mpa.util.ConstantUtils.ACCOUNT_TYPES_LOAN_ACCOUNTS;

import java.math.BigDecimal;

import org.displaytag.decorator.TableDecorator;

import pets.ui.mpa.model.Account;

public class AccountsDecorator extends TableDecorator {

	public BigDecimal getOpeningBalance() {
		Account account = (Account) getCurrentRowObject();
		
		if (ACCOUNT_TYPES_LOAN_ACCOUNTS.contains(account.getRefAccountType().getId())) {
			return account.getOpeningBalance().multiply(new BigDecimal("-1"));
		} else {
			return account.getOpeningBalance();
		}
	}
	
	public BigDecimal getCurrentBalance() {
		Account account = (Account) getCurrentRowObject();
		
		if (ACCOUNT_TYPES_LOAN_ACCOUNTS.contains(account.getRefAccountType().getId())) {
			return account.getCurrentBalance().multiply(new BigDecimal("-1"));
		} else {
			return account.getCurrentBalance();
		}
	}
}
