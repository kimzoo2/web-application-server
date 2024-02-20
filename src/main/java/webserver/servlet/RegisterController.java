package webserver.servlet;

import java.util.Map;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.HttpRequest;

@Slf4j
public class RegisterController  implements Controller {
	@Override
	public String doPost(HttpRequest httpRequest) {
		User model = createModel(httpRequest.getRequestArguments());
		DataBase.addUser(model);
		User foundUser = DataBase.findUserById(model.getUserId());
		log.info("foundUser ={}", foundUser);
		return "redirect:/user/login.html";
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
