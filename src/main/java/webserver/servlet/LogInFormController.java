package webserver.servlet;

public class LogInFormController implements Controller{
	@Override
	public String doPost() {
		return null;
	}

	@Override
	public String doGet() {
		return "/user/login.html";
	}
}
