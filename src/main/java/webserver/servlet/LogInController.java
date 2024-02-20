package webserver.servlet;

import java.util.Map;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LogInController implements Controller {
	@Override
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
		User model = createModel(httpRequest.getRequestArguments());
		User foundUser = DataBase.findUserById(model.getUserId());
		if(foundUser == null){
			httpRequest.setCookies("logined=false");
			httpResponse.setResponsePath("/user/login_failed.html");
			return;
		}
		httpRequest.setCookies("logined=true");
		httpResponse.setResponsePath("/index.html");
	}

	@Override
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {

	}

	/**
	 * request를 user 객체로 변경한다
	 * @param requestParams
	 * @return User
	 */
	private User createModel(Map<String, String> requestParams) {
		// 하드코딩?
		return new User(
			requestParams.get("userId")
			, requestParams.get("name")
			, requestParams.get("password")
			, requestParams.getOrDefault("email", null)
		);
	}
}
