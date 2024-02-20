package webserver.servlet;

import java.util.Map;

import model.User;

public class RegisterController  implements Controller {
	@Override
	public String doPost(Map<String, String> request) {
		return "redirect:/user/login.html";
	}

	@Override
	public String doGet(Map<String, String> request) {
		return null;
	}
}
