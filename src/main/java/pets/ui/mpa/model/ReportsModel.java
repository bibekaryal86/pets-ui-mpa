package pets.ui.mpa.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(NON_NULL)
public class ReportsModel implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private String errMsg;
	private String userAction;
	
    private List<ReportCurrentBalances> reportCurrentBalances;
    private String selectedYear;
    private List<ReportCashFlows> reportCashFlows;
    private List<ReportCategoryTypes> reportCategoryTypes;
    private Status status;
}
