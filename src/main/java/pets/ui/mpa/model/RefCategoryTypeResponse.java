package pets.ui.mpa.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class RefCategoryTypeResponse implements Serializable {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private static final long serialVersionUID = 1L;
	
	private List<RefCategoryType> refCategoryTypes;
	private Long deleteCount;
	private Status status;
}
