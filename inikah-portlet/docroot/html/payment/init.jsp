<script src='https://www.paypalobjects.com/js/external/dg.js' type='text/javascript'></script>
<script>
	window.onload = function(){
		if(window.opener){
			window.close();
		} 
		else{
			if(top.dg.isOpen() == true){
				top.dg.closeFlow();
				return true;
			}
		}                              
	};                             
</script>