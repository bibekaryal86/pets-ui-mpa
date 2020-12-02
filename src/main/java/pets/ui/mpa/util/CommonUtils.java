package pets.ui.mpa.util;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CommonUtils {

	public static Map<String, String> pathVariables(String username) {
		Map<String, String> pathVariablesMap = new HashMap<>();
		pathVariablesMap.put("username", username);
		return pathVariablesMap;
	}

	public static String toUppercase(String inputString) {
		return inputString == null ? null : inputString.toUpperCase();
	}
	
	public static String removeApostropheForJavascript(String inputString) {
		return inputString == null ? null : inputString.replace("'", "");
	}
	
	public static BigDecimal formatAmountBalance(BigDecimal inputBigDecimal) {
		return inputBigDecimal == null ? null : inputBigDecimal.setScale(2, RoundingMode.HALF_UP);
	}

	public static ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.findAndRegisterModules();
	}
}
