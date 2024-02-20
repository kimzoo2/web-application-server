package webserver.servlet;

public class RegisterFormController implements Controller {
	@Override
	public String doPost() {
		return null;
	}

	@Override
	public String doGet() {
		return "/user/form.html";
	}
}
