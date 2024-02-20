package webserver;

import java.util.Map;

import lombok.Getter;

@Getter
public class HttpRequest {

	private final Map<String, String> requestArguments;
	private String cookie;

	public HttpRequest(Map<String, String> requestArguments, String cookie) {
		this.requestArguments = requestArguments;
		this.cookie = cookie;
	}

	public void setCookies(String cookies){
		this.cookie = cookies;
	}

	public boolean hasCookies(){
		return !cookie.equals("");
	}

}
