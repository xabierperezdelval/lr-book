import java.net.URL;

import com.liferay.portal.model.OrganizationSoap;
import com.liferay.portal.service.http.OrganizationServiceSoap;
import com.liferay.portal.service.http.OrganizationServiceSoapServiceLocator;

public class WSTest1 {
	public static void main(String[] args) {
		System.out.println("Inside the client program!!");
	
		try {
			OrganizationServiceSoapServiceLocator locator = 
					new OrganizationServiceSoapServiceLocator();
	
			OrganizationServiceSoap soap = 
					locator.getPortal_OrganizationService(
							_getURL("Portal_OrganizationService"));
	
			OrganizationSoap[] organizations = soap.getUserOrganizations(10195l);
	
			for (int i = 0; i < organizations.length; i++) {
				OrganizationSoap organization = organizations[i];
				System.out.println(organization.getName());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static URL _getURL(String serviceName) 
			throws Exception {
	
		String remoteUser = "10195";
		String password = "test1";
	
		String url = "http://" + remoteUser + ":" + password + 
				"@localhost:8080/api/secure/axis/" + serviceName;
	
		return new URL(url);
	}
}