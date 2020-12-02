package pets.ui.mpa.decorator;

import static pets.ui.mpa.util.ConstantUtils.TRANSACTION_TYPE_ID_EXPENSE;
import static pets.ui.mpa.util.ConstantUtils.TRANSACTION_TYPE_ID_TRANSFER;

import java.math.BigDecimal;

import org.displaytag.decorator.TableDecorator;

import pets.ui.mpa.model.Transaction;

public class TransactionsDecorator extends TableDecorator {

	public BigDecimal getAmount() {
		Transaction transaction = (Transaction) getCurrentRowObject();
		
		if (transaction.getRefTransactionType().getId().equals(TRANSACTION_TYPE_ID_EXPENSE)) {
			return transaction.getAmount().multiply(new BigDecimal("-1"));
		} else if (transaction.getRefTransactionType().getId().equals(TRANSACTION_TYPE_ID_TRANSFER)) {
			String accountFilterId = transaction.getAccountFilter() == null ? "" : transaction.getAccountFilter();
			String trfAccountId = transaction.getTrfAccount() == null ? "" : transaction.getTrfAccount().getId();
			
			if (accountFilterId.equals(trfAccountId)) {
				return transaction.getAmount(); 
			} else {
				return transaction.getAmount().multiply(new BigDecimal("-1"));
			}
		} else {
			return transaction.getAmount();
		}
	}
}
