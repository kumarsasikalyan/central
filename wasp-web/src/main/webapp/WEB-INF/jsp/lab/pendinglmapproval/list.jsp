<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<!--  


      
      <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('su') or hasRole('ga-*')"> 
      
      <h2>PI/Lab Manager Pending Tasks (Lab: <c:out value="${lab.name}" />)</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${newuserspendinglist}" var="up">
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>     
      </div>
    </c:forEach>
     <c:forEach items="${existinguserspendinglist}" var="lu">
      <div>
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
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
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a>     
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

      
      <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('su') or hasRole('ga-*')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${newuserspendinglist}" var="up">
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) <input class="FormElement ui-widget-content ui-corner-all" type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" /> <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>     
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
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) <input class="FormElement ui-widget-content ui-corner-all" type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" /> <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
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
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) <input class="FormElement ui-widget-content ui-corner-all" type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" />  <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a>     
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

  
      
      <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('su') or hasRole('ga-*')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    
    
    <c:forEach items="${newuserspendinglist}" var="up">
   
     
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />)  <button id="robbutton<c:out value='${counter}' />">show</button> <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>   
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
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) <button id="robbutton<c:out value='${counter}' />">show</button> <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a> 
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
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) <button id="robbutton<c:out value='${counter}' />">show</button> <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a> 
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
 
 
-->  
   
 <br />
 
 <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('su') or hasRole('ga-*')"> 
 <title><fmt:message key="pageTitle.lab/pendinglmapproval/list.label"/></title>
  <h2><fmt:message key="lmpendingtask.title.label" /></h2>
 <h3><fmt:message key="lmpendingtask.subtitle1.label" /></h3>
      
<div id="accordion">
      
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    None<br />    
    </c:when>
    <c:otherwise>  
    <c:forEach items="${newuserspendinglist}" var="up">
      
      <h4><a href="#"><c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) </a> </h4> <!--a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a-->    
      
      <div> 
      
      <c:forEach items="${up.userPendingMeta}" var="meta"> 
        
      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
      
      </c:forEach>  
      
      <br /><a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>   
      
      </div> 
      
     </c:forEach>
     
   <c:forEach items="${existinguserspendinglist}" var="lu">
     
      <h4><a href="#"><c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />)</a>  </h4> <!-- a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a-->
      
       <div> 
      
      <c:forEach items="${lu.user.userMeta}" var="meta"> 
         
      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
      
      </c:forEach>
      <br />  <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
      </div>
      
      
    </c:forEach>  
    </c:otherwise>
    </c:choose>

 </div>   
 </br >      
 <h3><fmt:message key="lmpendingtask.subtitle2.label" /></h3>
<div id="accordion2">
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    None<br />    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${jobspendinglist}" var="job">
      
      <h4><a href="#">Job <c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />)</a></h4><!-- a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a-->     
      
      <div>
      <c:forEach items="${job.jobMeta}" var="meta">     
     <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
    </c:forEach>
      <br /> <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a>     
      </div>
      
    </c:forEach> 
    </c:otherwise>
    </c:choose>
</div>
    </sec:authorize>   
    
    
    <br /><br />
