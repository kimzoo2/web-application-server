package webserver.servlet;

public class RegisterController  implements Controller {
	@Override
	public String doPost() {
		return "redirect:/user/login.html";
	}

	@Override
	public String doGet() {
		return null;
	}
}
