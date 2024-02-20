package webserver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import webserver.servlet.Controller;
import webserver.servlet.LogInController;
import webserver.servlet.RegisterController;
import webserver.servlet.RegisterFormController;

public class HandlerMapping {

	protected ConcurrentMap<String, Controller> handlerMap = new ConcurrentHashMap<>();

	public HandlerMapping() {
		handlerMap.put("/user/create", new RegisterController());
		handlerMap.put("/user/form.html", new RegisterFormController());
		handlerMap.put("/user/login", new LogInController());
	}

	public Controller controller(String key){
		return handlerMap.get(key);
	}
}
