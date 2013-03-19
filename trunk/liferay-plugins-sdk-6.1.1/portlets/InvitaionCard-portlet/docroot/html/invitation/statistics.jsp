<%@include file="/html/invitation/init.jsp"%>

<% 
	  int sucess=0;
	  int sucessRate=0;
	  long signedUser=0l;
	  int noOfInvitationsSent = 0;
	  int noOfUsersPending = 0;
	  int noOfUsersJoined = 0;
	  int pointCaluclation=0;
	  int rank = 1;
	  int presentCount = 0;
	  long userId=0l;
	  int rankingAlotment=0;
	  int count = 0;
	  String firstRank = null;
	  
	  signedUser=themeDisplay.getUserId();
	  List<InvitationCard> invitationDetails = InvitationUtil.getInvitationByStatus(com.mpower.util.InvitationConstants.STATUS_INVITED);
	  List<InvitationCard> pendingDetails = InvitationUtil.getInvitationByStatus(com.mpower.util.InvitationConstants.STATUS_PENDING);
	  List<InvitationCard> joinedDetails = InvitationUtil.getInvitationByStatus(com.mpower.util.InvitationConstants.STATUS_ACCEPTED);
	
	  if(Validator.isNotNull(invitationDetails) && invitationDetails.size() > 0){
		noOfInvitationsSent = invitationDetails.size();
	  	}
	
	  if(Validator.isNotNull(pendingDetails) && pendingDetails.size() > 0){
		noOfUsersPending = pendingDetails.size();
	    }
	
	  if(Validator.isNotNull(joinedDetails) && joinedDetails.size() > 0){
		noOfUsersJoined = joinedDetails.size();
	    }
	  pointCaluclation=InvitationUtil.caluclatePoints(themeDisplay, renderRequest);
	  List<UserRank> ranks=InvitationCardLocalServiceUtil.findByCount();
		HttpServletRequest requests = PortalUtil.getHttpServletRequest(renderRequest);
		InvitationUtil.getRank(request);
		Map<Integer, String> rankscal=(Map)requests.getAttribute("mapObject");
		for(Map.Entry<Integer, String> ranking : rankscal.entrySet()){
			ranking.getKey();
			firstRank = ranking.getValue();
			List<String> userLists = Arrays.asList(firstRank.split(",")); 
			rankingAlotment=++count;
			
			for(String users:userLists){
				if(signedUser==Long.parseLong(users)){
%>

	<b>User Id :</b><%=signedUser %><br />
	<b>Rank :</b><%=rankingAlotment %><br />
	<b>Points:<%= pointCaluclation%></b>
	
<%
			}
		}
	}

sucessRate=(noOfInvitationsSent/noOfUsersJoined)*100;
		
%>
		
<br/><b>sucessRate=<%=sucessRate %></b>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Pending',       <%=noOfUsersPending %>],
          ['Invited', <%= noOfInvitationsSent%>],
          ['Joined',    <%=noOfUsersJoined%>]

        ]);

        var options = {
          title: 'Invitations'
        };

        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
</script>

<div id="chart_div" style="width: 900px; height: 500px;"></div>

