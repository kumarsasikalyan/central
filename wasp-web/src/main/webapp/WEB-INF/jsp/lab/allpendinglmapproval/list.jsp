<%@ include file="/WEB-INF/jsp/taglib.jsp" %>



<!--
<br /><br />
      
      <sec:authorize access="hasRole('su')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${newuserspendinglist}" var="up">
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) [Lab: <c:out value="${up.lab.name}" />] <a href="<c:url value="/lab/userpending/approve/${up.lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${up.lab.labId}/${up.userPendingId}.do"/>">REJECT</a>     
      </div>
    </c:forEach>
     <c:forEach items="${existinguserspendinglist}" var="lu">
      <div>
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) [Lab: <c:out value="${lu.lab.name}" />] <a href="<c:url value="/lab/labuserpending/approve/${lu.lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lu.lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
      </div>
    </c:forEach>  
    </c:otherwise>
    </c:choose>
      <h3>Pending Jobs</h3>
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${jobspendinglist}" var="job">
      <div>
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) [Lab: <c:out value="${job.lab.name}" />] <a href="<c:url value="/job/allpendinglmapproval/approve/${job.lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/allpendinglmapproval/reject/${job.lab.labId}/${job.jobId}.do"/>">REJECT</a>     
      </div>
        
      <c:forEach items="${job.jobMeta}" var="meta">     
      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" />
      <br />
    </c:forEach>     
    </c:forEach> 
    </c:otherwise>
    </c:choose>
  
    </sec:authorize> 
   
 <br />

<c:set var="counter" scope="page" value="1"/>

      
      <sec:authorize access="hasRole('su')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${newuserspendinglist}" var="up">
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) [Lab: <c:out value="${up.lab.name}" />] <input class="FormElement ui-widget-content ui-corner-all" type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" /> <a href="<c:url value="/lab/userpending/approve/${up.lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${up.lab.labId}/${up.userPendingId}.do"/>">REJECT</a>     
      </div>
      
      
      
      <div id='robdiv<c:out value='${counter}' />' style="display:none">       
      <c:forEach items="${up.userPendingMeta}" var="meta">        
       <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />      
      </c:forEach>       
      <br /><br />
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      
      
      
    </c:forEach>
     <c:forEach items="${existinguserspendinglist}" var="lu">
      <div>
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) [Lab: <c:out value="${lu.lab.name}" />] <input class="FormElement ui-widget-content ui-corner-all" type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" /> <a href="<c:url value="/lab/labuserpending/approve/${lu.lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lu.lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
      </div>
      
      
       <div id='robdiv<c:out value='${counter}' />' style="display:none">       
      <c:forEach items="${lu.user.userMeta}" var="meta">        
       <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />      
      </c:forEach>       
      <br /><br />
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      
      
    </c:forEach>  
    </c:otherwise>
    </c:choose>
      <h3>Pending Jobs</h3>
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${jobspendinglist}" var="job">
      <div>
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) [Lab: <c:out value="${job.lab.name}" />] <input class="FormElement ui-widget-content ui-corner-all" type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" />  <a href="<c:url value="/job/allpendinglmapproval/approve/${job.lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/allpendinglmapproval/reject/${job.lab.labId}/${job.jobId}.do"/>">REJECT</a>     
      </div>
       
       <div id='robdiv<c:out value='${counter}' />' style="display:none">  
      <c:forEach items="${job.jobMeta}" var="meta">     
      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
      </c:forEach>
      <br /><br />
      </div>
      <c:set var="counter" scope="page" value="${counter + 1}"/>
       
    </c:forEach> 
    </c:otherwise>
    </c:choose>
  
    </sec:authorize> 
  
  
  <br />


  
      
      <sec:authorize access="hasRole('su')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    
    <table class="EditTable ui-widget ui-widget-content">
    <c:forEach items="${newuserspendinglist}" var="up">
   
     <tr class="FormData"><td>
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) </td><td> <button id="robbutton<c:out value='${counter}' />">show</button></td><td> <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a></td><td> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>   
       </div> 
     </td></tr>  
      
      <tr class="FormData"><td colspan = '4'>
      <div id='robdiv<c:out value='${counter}' />' style="display:none"> 
            
      <c:forEach items="${up.userPendingMeta}" var="meta"> 
            
       <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />     
      </c:forEach>       
      <br /><br />
      
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      </td></tr> 
      
      
    </c:forEach>
    
    
     <c:forEach items="${existinguserspendinglist}" var="lu">
    <tr class="FormData"><td>  <div>
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) </td><td> <button id="robbutton<c:out value='${counter}' />">show</button></td><td> <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> </td><td> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a> 
      </div>
    </td></tr>  
      <tr class="FormData"><td colspan = '4'>
       <div id='robdiv<c:out value='${counter}' />' style="display:none">       
      <c:forEach items="${lu.user.userMeta}" var="meta">        
       <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />      
      </c:forEach>       
      <br /><br />
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      </td></tr>
      
    </c:forEach> 
    </table> 
    </c:otherwise>
    </c:choose>
      <h3>Pending Jobs</h3>
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise> 
    <table class="EditTable ui-widget ui-widget-content">   
    <c:forEach items="${jobspendinglist}" var="job">
    <tr class="FormData"><td>
      <div>
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) </td><td> <button id="robbutton<c:out value='${counter}' />">show</button> <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a> 
      </div>
     </td></tr>  
     <tr class="FormData"><td colspan = '2'>
       <div id='robdiv<c:out value='${counter}' />' style="display:none">
         
      <c:forEach items="${job.jobMeta}" var="meta">     
      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
      </c:forEach>
      <br /><br />
      </div>
      <c:set var="counter" scope="page" value="${counter + 1}"/>
       </td></tr>
    </c:forEach>
    </table> 
    </c:otherwise>
    </c:choose>
  
    </sec:authorize>   
  -->
   

      
<sec:authorize access="hasRole('su') or hasRole('lm-*}') or hasRole('pi-*') or hasRole('fm-*') or hasRole('ft-*') or hasRole('ga-*')"> 
<h1><fmt:message key="pageTitle.lab/pendinglmapproval/list.label"/></h1>
 
<c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    	<h2><fmt:message key="lmpendingtask.subtitle1_none.label" /></h2>
    </c:when>
    <c:otherwise>   
       <h2><fmt:message key="lmpendingtask.subtitle1.label" /></h2> 
	   <div id="accordion">
      		<c:forEach items="${newuserspendinglist}" var="up">
      		 	<h4><a href="#"><c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) [Lab: <c:out value="${up.lab.name}" />] </a> </h4> <!--a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a-->    
            	<div> 
      	      		<c:forEach items="${up.userPendingMeta}" var="meta"> 
        	 			<c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
      				</c:forEach>  
      				<br /><div class="submit"><a href="<c:url value="/lab/userpending/approve/${up.lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${up.lab.labId}/${up.userPendingId}.do"/>">REJECT</a></div>    
      			</div>       
     		</c:forEach>
     		<c:forEach items="${existinguserspendinglist}" var="lu">
     			<h4><a href="#"><c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) [Lab: <c:out value="${lu.lab.name}" />] </a>  </h4> <!-- a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a-->
      		    <div> 
      				<c:forEach items="${lu.user.userMeta}" var="meta"> 
         			      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
            		</c:forEach>
      				<br />  <div class="submit"><a href="<c:url value="/lab/labuserpending/approve/${lu.lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lu.lab.labId}/${lu.labUserId}.do"/>">REJECT</a></div>
      			</div>      
    		</c:forEach> 
    	</div>   
    </c:otherwise>
</c:choose>
   
<c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    	<h2><fmt:message key="lmpendingtask.subtitle2_none.label" /></h2>    
    </c:when>
    <c:otherwise>
    	<h2><fmt:message key="lmpendingtask.subtitle2.label" /></h2>  
		<div id="accordion2">         
    	<c:forEach items="${jobspendinglist}" var="job">      
      		<h4><a href="#">Job <c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />)</a></h4><!-- a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a-->     
      		<div>
      			Resource: 
      			<c:forEach items="${job.jobResourcecategory}" var="jrc">
      				<c:out value="${jrc.resourceCategory.name}" />&nbsp;
      			</c:forEach>
      			<br />
      			<c:forEach items="${job.jobMeta}" var="meta">     
     				<c:set var="optionName" value="${meta.k}.label" />
     				 <c:if test="${fn:substringAfter(meta.k, '.') == 'readLength' || fn:substringAfter(meta.k, '.') == 'readType'}">
     					<fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
     				</c:if>
    			</c:forEach>
    			Workflow: <c:out value="${job.workflow.name}" /><br />
    			Sample(s):<br />	
    			<c:forEach items="${job.sample}" var="sample">
      				--<c:out value="${sample.name}" /> (<c:out value="${sample.sampleType.name}" /> 
      				<c:forEach items="${sample.sampleMeta}" var="samplemeta"> 
      					<c:if test="${fn:substringAfter(samplemeta.k, '.') == 'species'}">
     						; <c:out value="${samplemeta.v}" />)
     					</c:if>
     				</c:forEach>
     				<br />
      			</c:forEach>  
      			<br /> <div class="submit"><a href="<c:url value="/job/allpendinglmapproval/approve/${job.lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/allpendinglmapproval/reject/${job.lab.labId}/${job.jobId}.do"/>">REJECT</a></div>    
      		</div>      
   		 </c:forEach>
    	</div> 
	</c:otherwise>
</c:choose>

</sec:authorize>   
    
    
  
