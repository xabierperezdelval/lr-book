package com.fingence.hook;

import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.util.portlet.PortletProps;

public class CreateReportTypes extends SimpleAction {
	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.SimpleAction#SimpleAction()
	 */
	public CreateReportTypes() {
		super();
	}

	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.SimpleAction#run(String[] arg0)
	 */
	public void run(String[] arg0) throws ActionException {
		// TODO Auto-generated method stub
		
		// create vocabulary
		String vocabularyName = PortletProps.get("report.types.vocabulary.name");
	
		// create vocabulary if it does NOT exist
		
		//============
		// create parent and children, recursively
		
		String[] mainReports = PortletProps.getArray("report.types.parent.categories");
		
		for (int i=0; i<mainReports.length; i++) {
			// create the parent if it does NOT exist
			
			// for each parent create it's children by reading from the properties file
		}
	}

}