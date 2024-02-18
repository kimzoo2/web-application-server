package util;


import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class CustomHttpRequestUtilTest {

	@Test
	public void extractRequestURL(){
		String line = "GET /index.html HTTP/1.1";
		String reg = " ";
		String result = CustomHttpRequestUtil.parseURL(line, reg);

		assertThat(result).isEqualTo("/index.html");
	}

	@Test
	public void extractRequestHttpMethod(){
		//get
		String getHttpHeader = "GET /index.html HTTP/1.1";
		String reg = " ";
		String getResult = CustomHttpRequestUtil.parseHttpMethod(getHttpHeader, reg);

		//post
		String postHttpHeader = "POST /user/form.html HTTP/1.1";
		String postResult = CustomHttpRequestUtil.parseHttpMethod(postHttpHeader, reg);

		assertThat(getResult).isEqualTo("GET");
		assertThat(postResult).isEqualTo("POST");
	}



	@Test
	public void extractRequestAccessPath(){
		String url = "/user/create?userId=javajigi&password=password&name=JaeSung";
		String[] result = CustomHttpRequestUtil.parseRequestPath(url);

		assertThat(result[0]).isEqualTo("/user/create");
		assertThat(result[1]).isEqualTo("userId=javajigi&password=password&name=JaeSung");

	}

}
