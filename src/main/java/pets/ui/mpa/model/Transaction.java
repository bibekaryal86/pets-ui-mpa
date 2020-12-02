package pets.ui.mpa.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Transaction implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String description;
	private Account account;
	private Account trfAccount;
	private RefTransactionType refTransactionType;
	private RefCategory refCategory;
	private RefMerchant refMerchant;
	private AppUser user;
	private String date;
	private BigDecimal amount;
	private Boolean regular;
	private Boolean necessary;
	private String creationDate;
	private String lastModified;
	
	private String accountFilter;
}
