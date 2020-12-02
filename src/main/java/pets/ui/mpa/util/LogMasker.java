package pets.ui.mpa.util;

import static pets.ui.mpa.util.ConstantUtils.LOG_MASKER_PATTERN;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;

import java.util.regex.Matcher;

public class LogMasker {

	public static String maskDetails(String body) {
		if (body == null) {
			return null;
		} else {
			Matcher matcher = compile(quote("\"password\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
			if (matcher.find()) {
				body = matcher.replaceAll("\"password\":\"****\"");
			}

			matcher = compile(quote("\"email\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
			if (matcher.find()) {
				body = matcher.replaceAll("\"email\":\"****@****.***\"");
			}

			matcher = compile(quote("\"phone\":\"") + LOG_MASKER_PATTERN + quote("\"")).matcher(body);
			if (matcher.find()) {
				body = matcher.replaceAll("\"phone\":\"**********\"");
			}

			return body;
		}
	}
}
