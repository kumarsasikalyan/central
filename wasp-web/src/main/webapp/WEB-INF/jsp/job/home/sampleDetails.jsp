<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('fm')"> 
	<br />
	<a class="button" href="<wasp:relativeUrl value="job/${job.getId()}/jobSampleReviewForLabDownload.do" />" ><fmt:message key="sampleDetails.downloadJobSampleSummaryForLab.label" /></a>
	<br /><br />
</sec:authorize>

<div class="fixed-width_scrollable">
<c:if test="${fn:length(submittedMacromoleculeList)>0}">
<h2><fmt:message key="sampleDetails.submittedSamples.label" />: <c:out value="${fn:length(submittedMacromoleculeList)}" /></h2>
  <c:set var="theCounterForAllSubmittedMacromolecules" value = "${0}" />
  <c:forEach items="${sampleTypeListOfSubmittedMacromoleculesThatAreNotLibraries}" var="sampleTypeOfSubmittedMacromolecule" varStatus="statusSampleTypeOfSubmittedMacromolecule">
   <%-- <c:if test="${ not statusSampleTypeOfSubmittedMacromolecule.first }">
   	<br />
   </c:if>
   --%>
   <c:if test="${fn:length(sampleTypeListOfSubmittedMacromoleculesThatAreNotLibraries) > 1 }">
    <c:if test="${ not statusSampleTypeOfSubmittedMacromolecule.first }"><br /></c:if>
   	<span style="font-weight:bold">&nbsp;&nbsp;&nbsp;&nbsp;---Submitted Sample Type <c:out value="${statusSampleTypeOfSubmittedMacromolecule.count}" />: <c:out value="${sampleTypeOfSubmittedMacromolecule.getName()}" /></span>
   </c:if> 
	<table class="data" style="margin: 0px 0px" >	
	    <c:set var="theCounterForAnySpecificTypeOfSubmittedMacromolecule" value = "${0}" /> 
		<c:forEach items="${submittedMacromoleculeList}" var="submittedMacromolecule" varStatus="statusSubmittedMacromolecule">		
		 <c:if test="${submittedMacromolecule.getSampleType().getId() == sampleTypeOfSubmittedMacromolecule.getId()}">
		    <c:set var="theCounterForAllSubmittedMacromolecules" value = "${theCounterForAllSubmittedMacromolecules + 1}" />
		 	<c:set var="theCounterForAnySpecificTypeOfSubmittedMacromolecule" value = "${theCounterForAnySpecificTypeOfSubmittedMacromolecule + 1}" />		 		
			<c:if test="${theCounterForAnySpecificTypeOfSubmittedMacromolecule==1}">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"> </td> 
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.sampleName.label" /><br />(<fmt:message key="sampleDetails.internalID.label" />)</label></td> 
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.type.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.species.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.arrived?.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.qc.label" /></label></td>
					<c:set var="_area" value = "sample" scope="request"/>
					<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedMacromolecule)}" scope="request" />		
				    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">						
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>						
							<c:set var="labelKey" value="${_meta.property.label}" />							
							<c:if test="${fn:contains(labelKey,'Average')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Av.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Fragmentation')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Concentration')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Volume')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Quant. Method')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">								
								<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>${labelKey}</label>
									<c:if test="${not empty _meta.property.tooltip}">
										<wasp:tooltip value="${_meta.property.tooltip}" />
									</c:if>
								</td>
							</c:if>	
						</c:if>
					</c:forEach>
				</tr>
			</c:if>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${theCounterForAllSubmittedMacromolecules}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedMacromolecule.getName()}" /><br />(<fmt:message key="sampleDetails.ID.label" />:<c:out value="${submittedMacromolecule.getId()}" />)</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedMacromolecule.getSampleType().getName()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedObjectOrganismMap.get(submittedMacromolecule)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${receivedStatusMap.get(submittedMacromolecule)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:choose>
						<c:when test='${receivedStatusMap.get(submittedMacromolecule) == "RECEIVED"}'>
							<c:out value="${qcStatusMap.get(submittedMacromolecule)}" />							
							<c:set value="${qcStatusCommentsMap.get(submittedMacromolecule)}" var="metaMessageList" />
							<c:if test="${metaMessageList.size()>0}">
								<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  						<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
							</c:if>							
						</c:when>
						<c:otherwise>
							<fmt:message key="sampleDetails.notApplicable.label" />
						</c:otherwise>
					</c:choose>
				</td>
				<c:set var="_area" value = "sample" scope="request"/>
				<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedMacromolecule)}" scope="request" />		
				<c:forEach items="${_metaList}" var="_meta" varStatus="status">
					<c:if test="${_meta.property.formVisibility != 'ignore'}">						
						<c:set var="_myArea">${_area}.</c:set>
						<c:set var="_myCtxArea">${_area}.</c:set>
						<c:if test="${_metaArea != null}">		
							<c:set var="_myCtxArea">${_metaArea}.</c:set>
						</c:if>						
						<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
						<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">	
							<c:set var="inputVal" value="${_meta.v}" />							
							<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label>${inputVal}</label></td>
						</c:if>	
					</c:if>
				</c:forEach>
			</tr>
		 </c:if>
		</c:forEach>
	</table>
  </c:forEach>	
</c:if>	
</div>





<br />
<div class="fixed-width_scrollable">
<c:if test="${fn:length(submittedLibraryList)>0}">
<h2><fmt:message key="sampleDetails.submittedLibraries.label" />: <c:out value="${fn:length(submittedLibraryList)}" /></h2>
	<table class="data" style="margin: 0px 0px" >	
		<c:forEach items="${submittedLibraryList}" var="submittedLibrary" varStatus="statusSubmittedLibrary">		
			<c:if test="${statusSubmittedLibrary.first}">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"> </td> 					
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.sampleName.label" /><br />(<fmt:message key="sampleDetails.internalID.label" />)</label></td> 
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.type.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.species.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.arrived?.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.qc.label" /></label></td>
					<c:if test="${fn:length(libraryAdaptorsetMap) > 1 }">
						<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.adaptor.label" /></label></td>
					</c:if>
					<c:set var="_area" value = "sample" scope="request"/>
					<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedLibrary)}" scope="request" />		
				    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">						
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>						
							<c:set var="labelKey" value="${_meta.property.label}" />							
							<c:if test="${fn:contains(labelKey,'Average')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Av.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Fragmentation')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Concentration')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Volume')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Quant. Method')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">								
								<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>${labelKey}</label>
									<c:if test="${not empty _meta.property.tooltip}">
										<wasp:tooltip value="${_meta.property.tooltip}" />
									</c:if>
								</td>
							</c:if>	
						</c:if>
					</c:forEach>
				</tr>
			</c:if>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${statusSubmittedLibrary.count}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedLibrary.getName()}" /><br />(<fmt:message key="sampleDetails.ID.label" />:<c:out value="${submittedLibrary.getId()}" />)</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedLibrary.getSampleType().getName()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${submittedObjectOrganismMap.get(submittedLibrary)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${receivedStatusMap.get(submittedLibrary)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:choose>
						<c:when test='${receivedStatusMap.get(submittedLibrary) == "RECEIVED"}'>
							<c:out value="${qcStatusMap.get(submittedLibrary)}" />							
							<c:set value="${qcStatusCommentsMap.get(submittedLibrary)}" var="metaMessageList" />
							<c:if test="${metaMessageList.size()>0}">
								<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  						<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
							</c:if>							
						</c:when>
						<c:otherwise>
							<fmt:message key="sampleDetails.notApplicable.label" />
						</c:otherwise>
					</c:choose>
				</td>
				<c:set value="${libraryAdaptorsetMap.get(submittedLibrary)}" var="adaptorSet"/>
				<c:set value="${libraryAdaptorMap.get(submittedLibrary)}" var="adaptor"/>
				<c:if test="${fn:length(libraryAdaptorsetMap) > 1 }">
				  <td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${adaptorSet.getName()}" /><br />
					<fmt:message key="sampleDetails.index.label" /> <c:out value="${adaptor.getName()}" /> 
				  </td>
				</c:if>
				<c:set var="_area" value = "sample" scope="request"/>
				<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(submittedLibrary)}" scope="request" />		
				<c:forEach items="${_metaList}" var="_meta" varStatus="status">
					<c:if test="${_meta.property.formVisibility != 'ignore'}">						
						<c:set var="_myArea">${_area}.</c:set>
						<c:set var="_myCtxArea">${_area}.</c:set>
						<c:if test="${_metaArea != null}">		
							<c:set var="_myCtxArea">${_metaArea}.</c:set>
						</c:if>						
						<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
						<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">	
							<c:set var="inputVal" value="${_meta.v}" />							
							<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label>${inputVal}</label></td>
						</c:if>	
					</c:if>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
</c:if>	
</div>




<br />
<div class="fixed-width_scrollable">
<c:if test="${fn:length(facilityLibraryList)>0}">
<h2><fmt:message key="sampleDetails.facilityGeneratedLibraries.label" />: <c:out value="${fn:length(facilityLibraryList)}" /></h2>
	<table class="data" style="margin: 0px 0px" >	
		<c:forEach items="${facilityLibraryList}" var="facilityLibrary" varStatus="statusFacilityLibrary">		
			<c:if test="${statusFacilityLibrary.first}">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"> </td> 					
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.sampleName.label" /><br />(<fmt:message key="sampleDetails.internalID.label" />)<br />[<fmt:message key="sampleDetails.derivedFrom.label" />]</label></td> 
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.type.label" /></label></td>
					<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.qc.label" /></label></td>
					<c:if test="${fn:length(libraryAdaptorsetMap) > 1 }">
					  <td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="sampleDetails.adaptor.label" /></label></td>
					</c:if>
					<c:set var="_area" value = "sample" scope="request"/>
					<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(facilityLibrary)}" scope="request" />		
				    <c:forEach items="${_metaList}" var="_meta" varStatus="status">
						<c:if test="${_meta.property.formVisibility != 'ignore'}">						
							<c:set var="_myArea">${_area}.</c:set>
							<c:set var="_myCtxArea">${_area}.</c:set>
							<c:if test="${_metaArea != null}">		
								<c:set var="_myCtxArea">${_metaArea}.</c:set>
							</c:if>						
							<c:set var="labelKey" value="${_meta.property.label}" />							
							<c:if test="${fn:contains(labelKey,'Average')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Av.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Fragmentation')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Concentration')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Volume')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:if test="${fn:contains(labelKey,'Quant. Method')}">
								<c:set var="labelKey" value="${fn:replace(labelKey, ' ', '<br />')}" />
							</c:if>
							<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
							<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">								
								<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>${labelKey}</label>
									<c:if test="${not empty _meta.property.tooltip}">
										<wasp:tooltip value="${_meta.property.tooltip}" />
									</c:if>								
								</td>
							</c:if>	
						</c:if>
					</c:forEach>
				</tr>
			</c:if>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${statusFacilityLibrary.count}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${facilityLibrary.getName()}" /><br />(<fmt:message key="sampleDetails.ID.label" />:<c:out value="${facilityLibrary.getId()}" />)<br />[<fmt:message key="sampleDetails.parent.label" />: <c:out value="${facilityLibrary.getParent().getName()}" /> (<fmt:message key="sampleDetails.ID.label" />:<c:out value="${facilityLibrary.getParent().getId()}" />)]</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${facilityLibrary.getSampleType().getName()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${qcStatusMap.get(facilityLibrary)}" />
					<c:set value="${qcStatusCommentsMap.get(facilityLibrary)}" var="metaMessageList" />
					<c:if test="${metaMessageList.size()>0}">
						<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  				<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
					</c:if>					
				</td>
				<c:set value="${libraryAdaptorsetMap.get(facilityLibrary)}" var="adaptorSet"/>
				<c:set value="${libraryAdaptorMap.get(facilityLibrary)}" var="adaptor"/>
				<c:if test="${fn:length(libraryAdaptorsetMap) > 1 }">
				  <td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${adaptorSet.getName()}" /><br />
					<fmt:message key="sampleDetails.index.label" /> <c:out value="${adaptor.getName()}" /> 
				  </td>
				</c:if>
				<c:set var="_area" value = "sample" scope="request"/>
				<c:set var="_metaList" value = "${sampleNormalizedSampleMetaListMap.get(facilityLibrary)}" scope="request" />		
				<c:forEach items="${_metaList}" var="_meta" varStatus="status">
					<c:if test="${_meta.property.formVisibility != 'ignore'}">						
						<c:set var="_myArea">${_area}.</c:set>
						<c:set var="_myCtxArea">${_area}.</c:set>
						<c:if test="${_metaArea != null}">		
							<c:set var="_myCtxArea">${_metaArea}.</c:set>
						</c:if>						
						<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
						<c:if test="${id!='organism' && id!='genome' && id!='adaptorset' && id!='adaptor'}">	
							<c:set var="inputVal" value="${_meta.v}" />							
							<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label>${inputVal}</label></td>
						</c:if>	
					</c:if>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
</c:if>	
</div>
<br />