package webserver.servlet;

import java.util.Map;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

@Slf4j
public class RegisterController  implements Controller {

	@Override
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
		User model = createModel(httpRequest.getRequestArguments());
		DataBase.addUser(model);
		User foundUser = DataBase.findUserById(model.getUserId());
		log.info("foundUser ={}", foundUser);
		httpResponse.setResponsePath("redirect:/user/login.html");
	}

	@Override
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {}

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
