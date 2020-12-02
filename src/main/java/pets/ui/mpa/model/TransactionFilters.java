package pets.ui.mpa.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@JsonInclude(NON_NULL)
public class TransactionFilters implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private String accountId;
	private String accountTypeId;
	private BigDecimal amountFrom;
	private BigDecimal amountTo;
	private String bankId;
	private String categoryId;
	private String categoryTypeId;
	private String dateFrom;
	private String dateTo;
	private String merchantId;
	private Boolean necessary;
	private Boolean regular;
	private String transactionTypeId;
}
