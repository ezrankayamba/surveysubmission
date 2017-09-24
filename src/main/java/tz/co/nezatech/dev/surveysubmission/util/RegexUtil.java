package tz.co.nezatech.dev.surveysubmission.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	public static final String value(String str, String regex) {
		String val = null;
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			if (m.find()) {
				val = m.group(1);
			}
		} catch (Exception e) {

		}

		return val;
	}
}
