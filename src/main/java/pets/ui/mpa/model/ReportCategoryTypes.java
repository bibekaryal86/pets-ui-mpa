package pets.ui.mpa.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class ReportCategoryTypes implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
    private RefCategoryType refCategoryType;
    private BigDecimal totalRefCategoryType;
    private List<ReportCategories> reportCategories;
}
