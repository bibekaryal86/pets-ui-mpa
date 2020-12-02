package pets.ui.mpa.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@JsonInclude(NON_NULL)
public class ImportTransaction implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private String description;
	private String accountName;
	private String trfAccountName;
	private String categoryName;
	private String merchantName;
	private String date;
	private String amount;
	private String regular;
	private String necessary;
	private String transactionTypeName;
}
