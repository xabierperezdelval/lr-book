import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.json.JSONObject;

public class RESTTest {
	public static void main(String[] args) {
		String uri = 
				"http://localhost:8080/library-portlet/api/secure/jsonws/lmsbook/insert-book";
		
		HttpClient client = new HttpClient();
		
		// setting the credentials
		String userName = "test@liferay.com";
		String password = "test1";
		
		Credentials credentials = 
				new UsernamePasswordCredentials(userName,password);
		client.getState().setCredentials(AuthScope.ANY, credentials);
		client.getParams().setParameter("http.useragent", "Test Client"); 
		client.getParams().setAuthenticationPreemptive(true);
		
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
			
			try {
				JSONObject object = new JSONObject(method.getResponseBodyAsString());
				System.out.println("ID of the book added ==> " + 
						object.getLong("bookId"));
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				method.releaseConnection();
			}
		} else {
			System.out.println("There is a problem: " + returnCode);
		}
	}
}