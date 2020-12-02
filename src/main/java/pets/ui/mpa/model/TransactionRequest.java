package pets.ui.mpa.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Data
@JsonInclude(NON_NULL)
public class TransactionRequest implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private String description;
	@NonNull
	private String accountId;
	private String trfAccountId;
	//@NonNull - only reqd in service and data layers
	private String typeId;
	@NonNull
	private String categoryId;
	private String merchantId;
	private String newMerchant;
	@NonNull
	private String username;
	@NonNull
	private String date;
	@NonNull
	private BigDecimal amount;
	@NonNull
	private Boolean regular;
	@NonNull
	private Boolean necessary;
}
