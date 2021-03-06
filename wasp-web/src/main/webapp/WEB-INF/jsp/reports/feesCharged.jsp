<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1>
<fmt:message key="reports.feesCharged.label" />
</h1>

<form method="POST" onsubmit="openWaitDialog();  return true;">
	<!--  datepicker -->
	<table class="EditTable ui-widget ui-widget-content">
	<tr class="FormData">
		<td colspan="2" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_JobsCompletedBetweenReportStartDateAndEndDate.label" /></label></td> 
	</tr>
	<tr class="FormData">
		<td  class="CaptionTD"><fmt:message key="reports.feesCharged_ReportStartDate.label" /><sup>*</sup>: <input type="text" id="datepickerStartDate" name="reportStartDateAsString" ></td>		
		<td  class="CaptionTD"><fmt:message key="reports.feesCharged_ReportEndDate.label" /><sup>**</sup>: <input type="text" id="datepickerEndDate" name="reportEndDateAsString" ></td>
	</tr>
	<tr class="FormData">
		<td colspan="2" class="label-centered"><input class="fm-button" type="submit" value="<fmt:message key="reports.feesCharged_runReport.label" />" /></td>
	</tr>
	</table> 
	<span style="font-size:small">
	*<fmt:message key="reports.feesCharged_ifBlankStartDate.label" /> &nbsp; **<fmt:message key="reports.feesCharged_ifBlankEndDate.label" />
	</span>	 
</form>	
<br />	

<c:if test="${not empty reportStartDateAsString }"> <!-- if reportStartDateAsString is not empty, then we're coming from the POST -->
<a id="viewAdditionalJobStatsAndMore" href="javascript:void(0);"><fmt:message key="reports.feesCharged_clickToViewAdditionalJobStats.label" /></a><br /><br />		      	
<div id="jobStatsAndMoreDiv" style="display:none;">
	<fmt:message key="reports.feesCharged_totalJobsInDatabase.label" />: <c:out value="${fn:length(totalJobsInDatabase)}" /><br />
	<fmt:message key="reports.feesCharged_jobsWithdrawn.label" />: <c:out value="${fn:length(jobsNotYetCompletedBecauseWithdrawn)}" /><br />
	<c:if test="${fn:length(jobsMarkedAsCompletedButNoJobCompletionDateRecorded) > 0}" >
		<fmt:message key="reports.feesCharged_jobsCompletedButNoCompletionDateRecordedInDatabase.label" /> <span style="color:red">(<fmt:message key="reports.feesCharged_highlyUnexpectedPleaseReport.label" />)</span>: <c:out value="${fn:length(jobsMarkedAsCompletedButNoJobCompletionDateRecorded)}" /><br />
	</c:if>
	<c:if test="${fn:length(jobsMarkedAsCompletedButQuoteNotFound) > 0}" >
		<fmt:message key="reports.feesCharged_jobsCompletedButNoQuoteFoundInDatabase.label" /> <span style="color:red">(<fmt:message key="reports.feesCharged_highlyUnexpectedPleaseReport.label" />)</span>: <c:out value="${fn:length(jobsMarkedAsCompletedButQuoteNotFound)}" /><br />
	</c:if>
	<c:if test="${fn:length(jobsMarkedAsCompletedButQuoteMetaDateNotFoundOrNotAccessible) > 0}" >
		<fmt:message key="reports.feesCharged_jobsCompletedButUnableToFindOrAccessQuoteMetaDataInDatabase.label" /> <span style="color:red">(<fmt:message key="reports.feesCharged_highlyUnexpectedPleaseReport.label" />)</span>: <c:out value="${fn:length(jobsMarkedAsCompletedButQuoteMetaDateNotFoundOrNotAccessible)}" /><br />
	</c:if>
	<fmt:message key="reports.feesCharged_jobsCompletedButOutsideSelectedReportDates.label" />: <c:out value="${fn:length(jobsMarkedAsCompletedButOutsideOfReportDates)}" /><br />
	<fmt:message key="reports.feesCharged_jobsCompletedAndWithinSelectedReportDates.label" />: <c:out value="${fn:length(jobs)}" /><br />
	<fmt:message key="reports.feesCharged_jobsNotYetCompleted.label" />: <c:out value="${fn:length(jobsNotYetCompleted)}" /><br />
	<br />		
	<c:if test="${not empty jobsNotYetCompleted}">
		<table class="data" style="margin: 0px 0px" >
			<tr class="FormData"><td colspan="7" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
				<tr class="FormData">
			    	<td colspan="7" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_jobsNotYetCompleted.label" /></label></td>
			    </tr>
				<tr class="FormData"><td colspan="7" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
			<tr class="FormData">
		    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_jobId.label" /></label></td> 
		    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_workflow.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_labName.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_dateSubmitted.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_jobStatus.label" /></label></td>
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discountedFacilityCost.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_computationalCost.label" /></label></td> 
			</tr>
			<c:forEach items="${jobsNotYetCompleted}" var="jobNotYetCompleted" >
				<tr class="FormData">
		    		<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label><c:out value="${jobNotYetCompleted.getId()}" /></label></td> 
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label><c:out value="${jobNotYetCompleted.getWorkflow().getName()}" /></label></td> 
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label><c:out value="${jobNotYetCompleted.getLab().getName()}" /></label></td> 
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label><c:out value="${jobNotYetCompletedJobSubmittedDateAsStringMap.get(jobNotYetCompleted)}" /></label></td> 
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><label><c:out value="${jobNotYetCompletedJobStatusAsStringMap.get(jobNotYetCompleted)}" /></label></td> 
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <label><c:out value="${jobNotYetCompletedDiscountedFacilityCostAsStringAsStringMap.get(jobNotYetCompleted)}" /></label></td> 
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <label><c:out value="${jobNotYetCompletedAnalysisCostAsStringAsStringMap.get(jobNotYetCompleted)}" /></label></td> 
				</tr>
			</c:forEach>
			<tr class="FormData"><td colspan="7" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
			<tr class="FormData">
			    	<td colspan="7" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_reportSummaryJobsNotYetCompleted.label" /></label></td>
			    </tr>
			<tr class="FormData">
		    	<td colspan="5" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label></label></td>
		    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discountedFacilityCost.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_computationalCost.label" /></label></td> 
			</tr>	
			<tr class="FormData">
				<td colspan="5"  class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <c:out value="${grandTotalForJobsNotYetCompletedDiscountedFacilityCostAsString}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <c:out value="${grandTotalForJobsNotYetCompletedAnalysisCostAsString}" /></td>
			</tr>
			<tr class="FormData"><td colspan="7" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
		</table>
		<br /><br />
	</c:if>
</div>

	<c:if test="${empty labList }">
		<table class="data" style="margin: 0px 0px" >		
			<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
			<tr class="FormData">
		    	<td colspan="9" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_noJobsCompletedBetween.label" /> <c:out value="${reportStartDateAsString}" /> &amp; <c:out value="${reportEndDateAsString}" /></label></td>
		    </tr>
			<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
		</table>
	</c:if>
	<c:if test="${not empty labList}">
	<table class="data" style="margin: 0px 0px" >	
	
		<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
		<tr class="FormData">
	    	<td colspan="9" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_reportSummaryJobsCompletedBetween.label" /> <c:out value="${reportStartDateAsString}" /> &amp; <c:out value="${reportEndDateAsString}" /></label></td>
		</tr>
		<tr class="FormData">
	    	<td colspan="4" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label></label></td>
	    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_totalCharge.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_initialSeqFacilityCharge.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discount.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discountedSeqFacilityCharge.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_computationalCharge.label" /></label></td> 
		</tr>	
		<tr class="FormData">
			<td colspan="4"  class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(0)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(1) }" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(2)}" />)</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(3)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(4) }" /></td>
		</tr>
		<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
		
	 	<c:forEach items="${labList}" var="lab" >
		
			 <tr class="FormData">
		    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_PI.label" /></label></td>
		    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_submitter.label" /></label></td>
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_jobId.label" /></label></td>
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_workflow.label" /><br /><fmt:message key="reports.feesCharged_submitted.label" /><br /><fmt:message key="reports.feesCharged_completed.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_totalCharge.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_initialSeqFacilityCharge.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discount.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discountedSeqFacilityCharge.label" /></label></td> 
				<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_computationalCharge.label" /></label></td> 
			</tr>
			
			<c:set value="${labJobListMap.get(lab)}" var="jobList"/>
			
			<c:forEach items="${jobList}" var="job" varStatus="counter">
			
				<c:set value="${jobMPSQuoteMap.get(job)}" var="mostRecentMpsQuote"/>
			
				<c:set value="${jobMPSQuoteAsIntegerListMap.get(job)}" var="mPSQuoteAsIntegerList"/>
				<c:set value="${jobPIMap.get(job)}" var="PI"/>
				<tr class="FormData">
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${PI.getNameLstCmFst()}" /></td>
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${job.getUser().getNameLstCmFst()}" /></td>			
					<td class="DataTD"  style="text-align:center; white-space:nowrap;">
						<a href="<wasp:relativeUrl value="job/${job.getId()}/homepage.do" />">J<c:out value="${job.getId()}" /></a>
					</td>
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${job.getWorkflow().getName()}" /><br /><c:out value="${jobStartDateAsStringMap.get(job)}" /><br /><c:out value="${jobCompletionDateAsStringMap.get(job)}" /></td>			
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(0)}" /></td>
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(1) }" /></td>
					<td class="DataTD"  style="text-align:center; white-space:nowrap;">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(2)}" />)</td>
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(3)}" /></td>
					<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(4) }" /></td>
				</tr>
			</c:forEach>
			
			<c:set value="${labGrandTotalsAsIntegerListMap.get(lab)}" var="grandTotalsForLabAsIntegerList"/>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${PI.getNameLstCmFst()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>			
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(0)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(1) }" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(2)}" />)</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(3)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(4) }" /></td>
			</tr>
		</c:forEach>
		
		<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
		<tr class="FormData">
	    	<td colspan="9" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_reportSummaryJobsCompletedBetween.label" /> <c:out value="${reportStartDateAsString}" /> &amp; <c:out value="${reportEndDateAsString}" /></label></td>
		</tr>
		<tr class="FormData">
	    	<td colspan="4" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label></label></td>
	    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_totalCharge.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_initialSeqFacilityCharge.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discount.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_discountedSeqFacilityCharge.label" /></label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label><fmt:message key="reports.feesCharged_computationalCharge.label" /></label></td> 
		</tr>	
		<tr class="FormData">
			<td colspan="4"  class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(0)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(1) }" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(2)}" />)</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(3)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(4) }" /></td>
		</tr>
		<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
		
	</table>
	<br /><br />
	
	</c:if>


</c:if>









