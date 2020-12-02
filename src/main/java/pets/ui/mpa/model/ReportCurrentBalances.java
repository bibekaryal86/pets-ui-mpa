package pets.ui.mpa.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class ReportCurrentBalances implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private BigDecimal totalCash;
    private String accountTypeCashId;
    private BigDecimal totalCheckingAccounts;
    private String accountTypeCheckingAccountsId;
    private BigDecimal totalSavingsAccounts;
    private String accountTypeSavingsAccountsId;
    private BigDecimal totalInvestmentAccounts;
    private String accountTypeInvestmentAccountsId;
    private BigDecimal totalOtherDepositAccounts;
    private String accountTypeOtherDepositAccountsId;
    private BigDecimal totalCreditCards;
    private String accountTypeCreditCardsId;
    private BigDecimal totalLoansAndMortgages;
    private String accountTypeLoansAndMortgagesId;
    private BigDecimal totalOtherLoanAccounts;
    private String accountTypeOtherLoanAccountsId;
    private BigDecimal totalAssets;
    private BigDecimal totalDebts;
    private BigDecimal netWorth;
}
