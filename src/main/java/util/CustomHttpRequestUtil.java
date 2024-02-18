package util;

public class CustomHttpRequestUtil {

	public static String parseURL(String line, String regex) {
		String[] tokens = line.split(regex);
		for (String token : tokens) {
			if (token.startsWith("/"))
				return token;
		}
		return tokens[1];
	}

	public static String parseHttpMethod(String line, String regex){
		String[] tokens = line.split(regex);
		return tokens[0];
	}

	public static String[] parseRequestPath(String url) {
		String patternString = "\\?"; // /로 시작하고 ?로 끝난다.
		return url.split(patternString);
	}
}
