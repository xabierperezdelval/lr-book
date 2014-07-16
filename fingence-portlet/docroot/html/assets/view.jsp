<%@ include file="/html/init.jsp"%>

<div id="myTab">
	<ul class="nav nav-tabs">
    	<li><a href="#tab-1">Data Upload</a></li>
    	<li><a href="#tab-2">Report</a></li>
  	</ul>

  	<div class="tab-content">
    	<div id="tab-1" class="tab-pane">
    		<%@ include file="/html/assets/data-upload.jspf"%>
    	</div>
    	<div id="tab-2">
			<%@ include file="/html/assets/report-config.jspf"%>    	
		</div>
  	</div>
</div>

<aui:script>
	YUI().use(
		'aui-tabview',
	  	function(Y) {
	    	new Y.TabView({
	        	srcNode: '#myTab'
	      	}).render();
	  	}
	);
</aui:script>