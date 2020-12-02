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
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Data
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
public class AccountRequest implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	@NonNull
	private String typeId;
	@NonNull
	private String bankId;
	@NonNull
	private String description;
	@NonNull
	private BigDecimal openingBalance;
	@NonNull
	private String status;
	@NonNull
	private String username;
}
