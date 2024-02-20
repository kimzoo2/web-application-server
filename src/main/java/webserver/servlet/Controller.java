package webserver.servlet;

import java.util.Map;

public interface Controller {

	String doPost(Map<String, String> request);
	String doGet(Map<String, String> request);

}
