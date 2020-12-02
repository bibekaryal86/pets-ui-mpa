package pets.ui.mpa.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@JsonInclude(NON_NULL)
public class RefTablesModel implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private String errMsg;
	private String userAction;

	private List<RefAccountType> refAccountTypes;
	private List<RefBank> refBanks;
	private List<RefCategory> refCategories;
	private List<RefCategoryType> refCategoryTypes;
	private List<RefMerchant> refMerchants;
	private Set<String> refMerchantsFilterList;
	private List<RefTransactionType> refTransactionTypes;
	private int deleteCount;
}
