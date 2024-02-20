package webserver.servlet;

import java.util.Map;

import db.DataBase;
import model.User;
import webserver.HttpRequest;

public class LogInController implements Controller {
	@Override
	public String doPost(HttpRequest httpRequest) {
		User model = createModel(httpRequest.getRequestArguments());
		User foundUser = DataBase.findUserById(model.getUserId());
		if(foundUser == null){
			httpRequest.setCookies("logined=false");
			return "/user/login_failed.html";
		}
		httpRequest.setCookies("logined=true");
		return "/index.html";
	}

	@Override
	public String doGet(HttpRequest httpRequest) {
		return null;
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
