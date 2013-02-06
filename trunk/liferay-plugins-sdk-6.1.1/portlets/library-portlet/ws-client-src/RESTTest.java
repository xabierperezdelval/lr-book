import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;


public class RESTTest {
	public static void main(String[] args) {
		String uri = 
				"http://localhost:8080/library-portlet/api/jsonws/lmsbook/insert-book";
		
		HttpClient client = new HttpClient();
		
		PostMethod method = new PostMethod(uri);
		
		method.addParameter("bookTitle", "REST in action"); 
		method.addParameter("author", "Ahmed Hasan");
		
		int returnCode = 0;
		try {
			returnCode = client.executeMethod(method);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (returnCode == HttpStatus.SC_OK) {
			System.out.println("Status Code is: " + returnCode);
		} else {
			System.out.println("There is a problem: " + returnCode);
		}
	}
}
