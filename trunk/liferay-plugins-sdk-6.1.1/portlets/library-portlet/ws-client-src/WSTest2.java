import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.slayer.model.LMSBookSoap;
import com.slayer.service.http.LMSBookServiceSoap;
import com.slayer.service.http.LMSBookServiceSoapServiceLocator;


public class WSTest2 {
	public static void main(String[] args) {
	
		LMSBookServiceSoapServiceLocator locator = 
				new LMSBookServiceSoapServiceLocator();
		
		URL portAddress = null;
		try {
			portAddress = _getURL("Plugin_LMS_LMSBookService");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LMSBookServiceSoap soap = null;
		try {
			soap = locator.getPlugin_LMS_LMSBookService(portAddress);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		if (soap != null) {
			String bookTitle = "Web Services in action";
			String author = "Ahmed Hasan";
			
			LMSBookSoap lmsBook = null;
			try {
				lmsBook = soap.insertBook(bookTitle, author);
				System.out.println("book Id ==> " + lmsBook.getBookId());
			} catch (RemoteException e) {
				e.printStackTrace();
			}						
		}
	}
	
	private static URL _getURL(String serviceName) throws Exception {
	
		// Unathenticated url
		String url = "http://localhost:8080/library-portlet/api/axis/" + serviceName;
	
		return new URL(url);
	}
}