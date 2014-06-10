<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script type="text/javascript" src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script> 
 <script type="text/javascript" src="<wasp:relativeUrl value='scripts/jquery/jquery.table.addrow.js' />"></script>
 
<script type="text/javascript">
	(function($){ 
		$(document).ready(function(){			
				//needed for the addrow and delete row functionality:
				$(".addRow").btnAddRow();
				$(".delRow").btnDelRow();
		});
	})(jQuery);
</script>
	
<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/chipSeq/replicates.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
 
<div class="instructions">
   <fmt:message key="${workflowIName}.replicates_instructions.label"/>
</div>

<%-- 
<div id="container_div_for_adding_rows" >
<table class="data">
	<tr class="row">
		<td class="label" align="center"><fmt:message key="chipSeq.replicates_replicateset.label"/></td>
 		<td class="label" align="center"><fmt:message key="chipSeq.replicates_addreplicatesample.label"/></td>
 	</tr>
	<tr class="row">
		<td class="label">Email</td>
		<td><input type="text" size="24"/></td>
		<td><input type="text" size="24"/></td>
		
		<td><input type="button" class="delRow" value="Delete Row"/></td>
	</tr>
	<tr class="row">
		<td colspan="4"><input type="button" class="addRow" value="Add Row"/></td>
	</tr>
</table>
 </div>
 --%>
 <c:set value="false" var="atLeastOneReplicateSetWithSingleSample"/>
 <table class="data">
	<tr class="row">
		<td class="label" align="center">Set Number</td>
		<td class="label" align="center"><fmt:message key="chipSeq.replicates_replicateset.label"/></td>
		<td class="label" align="center"><fmt:message key="chipSeq.replicates_species.label"/></td>
 		<td class="label" align="center"><fmt:message key="chipSeq.replicates_addreplicatesample.label"/></td>
 	</tr> 
 	 
 	<c:forEach items="${replicatesListOfLists}" var="replicateList" varStatus="replicateStatus">
 		<c:if test="${fn:length(replicateList) ==1}"><c:set value="true" var="atLeastOneReplicateSetWithSingleSample"/></c:if>
		<tr class="row">	
			<td class="label" align="center"><c:out value="${replicateStatus.count}" /></td>
			<td class="label" >
				<c:forEach items="${replicateList}" var="sampleDraft" varStatus="sampleDraftStatus">
					<c:if test="${sampleDraftStatus.first}"><c:set value="${sampleDraftSpeciesNameMap.get(sampleDraft)}" var="speciesForThisReplicateSet"/></c:if>
					<c:if test="${not sampleDraftStatus.first}"><br /></c:if>
					<c:out value="${sampleDraft.name}" /> [<a href="<wasp:relativeUrl value="jobsubmit/chipSeq/replicates/${jobDraft.id}/remove/${sampleDraft.id}.do" />" >remove</a>]
				</c:forEach>
			</td>
			<td class="label" align="center"><c:out value="${speciesForThisReplicateSet}" /></td>
			<td align="center">
			  <c:set value="false" var="speciesForSelectIncludesSpeciesForThisReplicate"/>
			  <c:forEach var="ip" items="${ipSamples}" >
			  	<c:if test="${speciesForThisReplicateSet == sampleDraftSpeciesNameMap.get(ip)}">
			  		<c:set value="true" var="speciesForSelectIncludesSpeciesForThisReplicate"/>
			  	</c:if>
			  </c:forEach>
			  <c:if test='${speciesForSelectIncludesSpeciesForThisReplicate=="true" && ipSamples.size() > 0}'>
				<div>
				<form method="POST">
	  				<select name="ipIdForExistingReplicateSet_${replicateStatus.count}" >
	  					<option value="0">--select sample--</option>
		  				<c:forEach var="ip" items="${ipSamples}" >
		  					<c:set value="${sampleDraftSpeciesNameMap.get(ip)}" var="speciesForSelect"/>
		  					<c:if test="${speciesForThisReplicateSet == speciesForSelect}">
		  						<option value="<c:out value="${ip.id}" />"><c:out value="${ip.name }" /><c:if test="${not empty speciesForSelect }"> [<c:out value="${speciesForSelect}" />]</c:if></option>
		  					</c:if>
		  				</c:forEach>
	  				</select>   				 
	  				&nbsp; 	<input type="submit" onClick="return checkSampleSelected(this);" value="Add Sample To Set" />		
	  			</form>
	  			</div>
	  		  </c:if>
	  		</td>	
		</tr>
	
 	</c:forEach>
 	
 		
 	<c:if test="${ipSamplesForCreateNew.size() > 1}">
		<tr class="row">	
			<td class="label" align="center">Create New<br />Replicate Set</td>
			<td class="label" align="center"></td>
			<td class="label" align="center"></td>
			<td align="center">
				<div>
				<form method="POST">
	  				<select name="ipIdForNewReplicateSet" >
	  					<option value="0">--select sample--</option>
		  				<c:forEach var="ip" items="${ipSamplesForCreateNew}" >
		  					<c:set value="${sampleDraftSpeciesNameMap.get(ip)}" var="speciesForSelect"/>
		  					<option value="<c:out value="${ip.id}" />"><c:out value="${ip.name }" /><c:if test="${not empty speciesForSelect }"> [<c:out value="${speciesForSelect}" />]</c:if></option>
		  				</c:forEach>
	  				</select>   				 
	  				&nbsp; 	<input type="submit" onClick="return checkSampleSelected(this);" value="Add Sample To Set" />		
	  			</form>
	  			</div>
	  		</td>	
		</tr>
	</c:if>
 </table>
<form method="POST">
<div class="submit">
    <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
    <input type="submit" name="continueToNextPage" value="continueToNextPage" onClick="return checksOnContinueToNextPage('<c:out value="${atLeastOneReplicateSetWithSingleSample}" />');" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>
