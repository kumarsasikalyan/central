<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>	
  <style>
  .ui-autocomplete {
    max-height: 100px;
    overflow-y: auto;
    /* prevent horizontal scrollbar */
    overflow-x: hidden;
  }
  /* IE 6 doesn't support max-height
   * we use height instead, but this forces the menu to always be this tall
   */
  * html .ui-autocomplete {
    height: 100px;
  }
  </style>
  
<script type="text/javascript" src="<wasp:relativeUrl value='scripts/jquery/jquery.table.addrow.js' />"></script>
<script type="text/javascript">
	(function($){ 
		$(document).ready(function(){
			$(".addRow").btnAddRow();
			$(".delRow").btnDelRow();
		    $( "#dialog-form" ).dialog({
		        autoOpen: false,
		        height: 200,
		        width: 420,
		        modal: true,
		        buttons: {
		          "Apply": function() {
		        	  var costToApplyToAllSettableLibraries = $("#costToApplyToAllSettableLibraries").val();
		        	  var regex = /^([0-9])+$/;
		        	  if(regex.test(costToApplyToAllSettableLibraries)){
		        	  	$(".settableLibraryCost").val(costToApplyToAllSettableLibraries);
		        	  	$("#costToApplyToAllSettableLibraries").val("");
		        	  	$("#validateTipForLibraryCostModalDialogForm").text("");
		        	  	$( this ).dialog( "close" );
		        	  }
		        	  else{
		        		  $("#validateTipForLibraryCostModalDialogForm").text('<fmt:message key="jobHomeCreateUpdateQuote.wholeNumbersOnly.label" />');
		        	  }
		          },
		          Cancel: function() {
		        	$("#costToApplyToAllSettableLibraries").val("");
			        $("#validateTipForLibraryCostModalDialogForm").text("");		        	
		            $( this ).dialog( "close" );
		          }
		        },
		        close: function() {
		          	$("#costToApplyToAllSettableLibraries").val("");
		        	$("#validateTipForLibraryCostModalDialogForm").text("");	 
		        	$( this ).dialog( "close" );
		        }
		      });	
		});
	})(jQuery);
	
	function previewQuote(formObjectId, theUrl) {
		$("#wait_dialog-modal").dialog("open");
	   	var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel 
	   	var frm = $("#" + formObjectId);
	   	theUrlWithFormAttached = theUrl+"?"+frm.serialize();
	   	$("#createUpdateQuoteMessageDiv").text("");   
		$.ajax({
	        type: "GET",
	        url: theUrlWithFormAttached,
	        success: function (response) {
	        	//note: here, response is a pdf, which I don't know how to display directly, so call for it again through iframe      	
	        	$("html, body").animate({ scrollTop: 0 }, "fast");
	        	var frm = $("#" + formObjectId);
	        	showModalessDialog(theUrl+"?"+frm.serialize());//frm.serialize() returns, for example, sampleSubtypeId=5&sampleTypeId=2&name=input1 
	        	$("#modalessDialog").scrollTop("0");//bring dialog scrollbar to top of page; see http://stackoverflow.com/questions/10816279/how-to-get-jqueryui-dialog-scrolltop-to-scroll-dialog-content-to-top 
	        	$("#modalessDialog").dialog({        
	                position: { my: "right top", at: "right top", of: $(document).scrollTop("0") } //of used to be of: window 
	            });
	        	$("#wait_dialog-modal").dialog("close");
	        },
	        error: function (response, status, error) {        	
	        	//note: here response contains error text, response.responseText, that is something like: this is one~this is two~this is three 
	        	var returnedResponseArray = response.responseText.split(/~/); 
	        	if(response.status==500){        		
	        		$("#createUpdateQuoteMessageDiv").html("<h2 style='color:red'>Please Address The Following Errors:</h2>");        		
	        	}
	        	else {
	        		$("#createUpdateQuoteMessageDiv").html("<h2 style='color:red'>Unexpected Error:</h2>");
	        	}
	        	for(var i = 0; i < returnedResponseArray.length; i++){
	    			$("#createUpdateQuoteMessageDiv").append("<span style='color:red'>"+(i+1)+") "+ returnedResponseArray[i]+"</span><br />");
	    		}
	        	$("#wait_dialog-modal").dialog("close");
	        }
	    });
		return false; // avoid 
	}
	
	function saveQuoteAndDoNotSendEmail(formObjectId, theUrl){
		saveQuote(formObjectId, theUrl, "no");
	}
	function saveQuoteAndSendEmail(formObjectId, theUrl){
		saveQuote(formObjectId, theUrl, "yes");
	}
	function saveQuote(formObjectId, theUrl, sendEmail) {
		$("#wait_dialog-modal").dialog("open");
	   	var selectedPanel = $('#tabs').find("[aria-expanded=true]");//the div for this selected tabs panel 
	   	var frm = $("#" + formObjectId);
	   	theUrlWithFormAttached = theUrl+"?"+frm.serialize()+"&sendEmail="+sendEmail;
	   	$("#createUpdateQuoteMessageDiv").text("");   
		$.ajax({
	        type: "GET",
	        url: theUrlWithFormAttached,
	        dataType: 'text',
	        success: function (response) {	        	
	        	//oddly enough, the response is in response, not response.responseText. For some reason, response.responseText is undefined here in the success area 
	        	//see https://github.com/jakerella/jquery-mockjax/issues/95 for: 
	        	//Jquery 1.9.1 version returning the response text as 'undefined' on success callback 
	        	$("#createUpdateQuoteMessageDiv").html("<h2 style='color:green'>"+response+"</h2>");	        	
	        	$("#wait_dialog-modal").dialog("close");
	        	//following success, perhaps should disable anchor to save again? 
	        },
	        error: function (response, status, error) {        	
	        	//note: here response contains error text, response.responseText, that is something like: this is one~this is two~this is three 
	        	var returnedResponseArray = response.responseText.split(/~/); 
	        	if(response.status==500){        		
	        		$("#createUpdateQuoteMessageDiv").html("<h2 style='color:red'>Please Address The Following Errors:</h2>");        		
	        	}
	        	else {
	        		$("#createUpdateQuoteMessageDiv").html("<h2 style='color:red'>Unexpected Error:</h2>");
	        	}
	        	for(var i = 0; i < returnedResponseArray.length; i++){
	    			$("#createUpdateQuoteMessageDiv").append("<span style='color:red'>"+(i+1)+") "+ returnedResponseArray[i]+"</span><br />");
	    		}
	        	$("#wait_dialog-modal").dialog("close");
	        }
	    });
		return false; // avoid 
	}
</script>

<sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('da-*')">

	<form  method='post' name='quoteOrInvoiceForm' id="quoteOrInvoiceFormId" action="" >
		<div class="ui-widget">
			<div id="container_div_for_adding_rows" >
				<br />
				
				<c:set value="${mpsQuote}" var="mpsQuote" />
				<c:set value="${mpsQuote.getLocalCurrencyIcon()}" var="localCurrencyIcon" />
				<input type='hidden' name="localCurrencyIcon" value="${localCurrencyIcon}"/>
				
				<c:set value="${mpsQuote.getNumberOfLibrariesExpectedToBeConstructed()}" var="numberOfLibrariesExpectedToBeConstructed" />
				<input type='hidden' name="numberOfLibrariesExpectedToBeConstructed" value="${numberOfLibrariesExpectedToBeConstructed}"/>
				
				<c:set value="${mpsQuote.getNumberOfLanesRequested()}" var="numberOfLanesRequested" />
				<input type='hidden' name="numberOfLanesRequested" value="${numberOfLanesRequested}"/>
							
				<span style="padding:3px; border: 1px solid black;">
				<a <%-- class="button" --%> href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${mpsQuote.getJobId()}/costManager.do" />");' ><fmt:message key="jobHomeCreateUpdateQuote.returnToCostsPage.label" /></a>
				| <a <%-- class="button" --%> href="javascript:void(0);" onclick='showSmallModalessDialog("<wasp:relativeUrl value="job/${mpsQuote.getJobId()}/basic.do" />");' ><fmt:message key="jobHomeCreateUpdateQuote.viewBasicRequest.label" /></a>
				| <a <%-- class="button" --%> href="javascript:void(0);" onclick='showSmallModalessDialog("<wasp:relativeUrl value="job/${mpsQuote.getJobId()}/requests.do?onlyDisplayCellsRequested=true" />");' ><fmt:message key="jobHomeCreateUpdateQuote.viewLaneRequest.label" /></a>
				| <a <%-- class="button" --%> href="javascript:void(0);" onclick='previewQuote("quoteOrInvoiceFormId", "<wasp:relativeUrl value="job/${mpsQuote.getJobId()}/previewQuote.do" />");' ><fmt:message key="jobHomeCreateUpdateQuote.previewQuote.label" /></a>
				| <a <%-- class="button" --%> href="javascript:void(0);" onclick='saveQuoteAndDoNotSendEmail("quoteOrInvoiceFormId", "<wasp:relativeUrl value="job/${mpsQuote.getJobId()}/saveQuote.do" />");' ><fmt:message key="jobHomeCreateUpdateQuote.saveQuote.label" /></a>
				| <a <%-- class="button" --%> href="javascript:void(0);" onclick='saveQuoteAndSendEmail("quoteOrInvoiceFormId", "<wasp:relativeUrl value="job/${mpsQuote.getJobId()}/saveQuote.do" />");' ><fmt:message key="jobHomeCreateUpdateQuote.saveQuoteAndEmailToPI.label" /></a>
				
				</span>
				<br /><br /><div id="createUpdateQuoteMessageDiv"></div><br />				
				
				<span style='font-weight:bold'>1. <fmt:message key="jobHomeCreateUpdateQuote.libraryConstructionsExpected.label" />: <c:out value="${numberOfLibrariesExpectedToBeConstructed}" />
					<c:if test="${numberOfLibrariesExpectedToBeConstructed > 0}">
						&nbsp;&nbsp;[<fmt:message key="jobHomeCreateUpdateQuote.ifNoChargeForALibrary.label" />]
					</c:if>
				</span><br /><br />			
				<div id="dialog-form" title="Apply One Cost To All Library Constructions">
					<p></p>
					<p style="font-weight:bold;color:red" id="validateTipForLibraryCostModalDialogForm"></p>  
				  	<fieldset>
				  		<fmt:message key="jobHomeCreateUpdateQuote.applyOneCostToAllLibraryConstructions.label" />: <c:out value="${localCurrencyIcon}" /> <input id="costToApplyToAllSettableLibraries" style="text-align:right;" name="" type="text" maxlength="4" size="4" value="" />.00
				 	</fieldset>  
				</div>
							
				<table class="data" style="margin: 0px 0px">
					<tr class="FormData">
						<td colspan="5" class="label-centered" style="background-color:#FAF2D6">
							<a href="javascript:void(0);" onclick='$( "#dialog-form" ).dialog( "open" );' ><fmt:message key="jobHomeCreateUpdateQuote.clickHereToApplyOneCostToAllLibraryConstructions.label" /></a>
						</td>
					</tr>
					<tr class="FormData">
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.number.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.submittedSample.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.material.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.libraryCost.label" /><sup>*</sup></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.analysisCost.label" /><sup>*</sup></td>
					</tr>
					<c:forEach items="${mpsQuote.getLibraryCosts()}" var="libraryCost" varStatus="libraryCostStatus">
						<input type='hidden' name="submittedSampleId" value="${libraryCost.getSampleId()}"/>
						<tr>
							<td class="DataTD"  style="text-align:center; white-space:nowrap;">
								<c:out value="${libraryCostStatus.count}" />
							</td>
							<td class="DataTD"  style="text-align:center; white-space:nowrap;">
								<input type='hidden' name="submittedSampleName" value="${libraryCost.getSampleName()}"/>
								<c:out value="${libraryCost.getSampleName()}" />
							</td>
							<td class="DataTD"  style="text-align:center; white-space:nowrap;">
								<input type='hidden' name="submittedSampleMaterial" value="${libraryCost.getMaterial()}"/>
								<c:out value="${libraryCost.getMaterial()}"/>
							</td>
							<td class="DataTD"  style="text-align:center; white-space:nowrap;">				
								<c:choose>					
									<c:when test='${not empty libraryCost.getReasonForNoLibraryCost()}'>
										<c:out value="${libraryCost.getReasonForNoLibraryCost()}" />
										<input type='hidden' name="reasonForNoLibraryCost" value="${libraryCost.getReasonForNoLibraryCost()}"/>
										<input type='hidden' name="submittedSampleCost" value="${libraryCost.getLibraryCost()}"/>							
									</c:when>						
									<c:when test='${empty libraryCost.getLibraryCost()}'>
										<input type='hidden' name="reasonForNoLibraryCost" value="${libraryCost.getReasonForNoLibraryCost()}"/>
										<c:out value="${localCurrencyIcon}" /><input class="settableLibraryCost"  style="text-align:right;" name="submittedSampleCost"  type="text" maxlength="4" size="4" value="" />.00
									</c:when>						
									<c:when test='${not empty libraryCost.getLibraryCost()}'>
										<input type='hidden' name="reasonForNoLibraryCost" value="${libraryCost.getReasonForNoLibraryCost()}"/>
										<c:out value="${localCurrencyIcon}" /><input class="settableLibraryCost" style="text-align:right;" name="submittedSampleCost"  type="text" maxlength="4" size="4" value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${libraryCost.getLibraryCost()}" />"/>.00
									</c:when>						
								</c:choose>											
							</td>
							<td class="DataTD"  style="text-align:center; white-space:nowrap;">				
								<c:out value="${localCurrencyIcon}" /><input style="text-align:right;" name="submittedSampleAnalysisCost"  type="text" maxlength="4" size="4" value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${libraryCost.getAnalysisCost()}" />"/>.00																
							</td>
						</tr>
					</c:forEach>
				</table>
				<sup>*</sup><span style="font-size:small;color:red"><fmt:message key="jobHomeCreateUpdateQuote.forCostsWholeNumbersOnly.label" /></span>
				
				<br /><br />
				
				<span style='font-weight:bold'>2. <fmt:message key="jobHomeCreateUpdateQuote.sequencingLanesExpected.label" />: <c:out value="${numberOfLanesRequested}" /></span><br /><br />
				<table  class="data" style="margin: 0px 0px">
					<tr class="FormData">
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.machine.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.runType.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.readLength.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.readType.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.numberOfLanes.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.costPerLane.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.action.label" /></td>
					</tr>
					<c:choose>
					<c:when test="${not empty mpsQuote.getSequencingCosts()}">
						<c:forEach items="${mpsQuote.getSequencingCosts()}" var="sequencingCost" >
							<tr>
								<td align='center'>	
									<select name='runCostResourceCategoryId' id='runCostResourceCategoryId' size='1'>
										<option value=''><fmt:message key="wasp.default_select.label" />
										<c:forEach items="${sequencingMachines}" var="resourceCategory">
											<c:set value="" var="selected" />
											<c:if test="${resourceCategory.getId() == sequencingCost.getResourceCategory().getId()}">
												<c:set value="SELECTED" var="selected" />
											</c:if>
											<option value='<c:out value="${resourceCategory.getId()}" />' <c:out value="${selected}" /> ><c:out value="${resourceCategory.getName()}" />
										</c:forEach>
									</select>
								</td>
								<td align='center'>	
									<select name='runCostRunType' id='runCostRunType' size='1'>
										<option value=''><fmt:message key="wasp.default_select.label" />
										<option value='high-output'  <c:if test='${sequencingCost.getRunType()=="high-output"}'>selected</c:if>><fmt:message key="jobHomeCreateUpdateQuote.runType_highOutput.label" />
										<option value='rapid-run' <c:if test='${sequencingCost.getRunType()=="rapid-run"}'>selected</c:if>><fmt:message key="jobHomeCreateUpdateQuote.runType_rapidRun.label" />
										<option value='standard' <c:if test='${sequencingCost.getRunType()=="standard"}'>selected</c:if>><fmt:message key="jobHomeCreateUpdateQuote.runType_standard.label" />
									</select>
								</td>				
								<%-- <td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='runCostReadLength' id='runCostReadLength' value="${sequencingCost.getReadLength()}"></td>--%>
								<td align='center'>	
									<select name='runCostReadLength' id='runCostReadLength' size='1'>
										<option value=''><fmt:message key="wasp.default_select.label" />
										<c:forEach items="${allAvailableReadLengths}" var="readLength">						
											<option value='<c:out value="${readLength}" />' <c:if test='${sequencingCost.getReadLength()==readLength}'>selected</c:if>><c:out value="${readLength}" />
										</c:forEach>
									</select>
								</td>
								<%-- <td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostReadType' id='runCostReadType' value="${sequencingCost.getReadType()}"></td>--%>
								<td align='center'>	
									<select name='runCostReadType' id='runCostReadType' size='1'>
										<option value=''><fmt:message key="wasp.default_select.label" />
										<option value='single'  <c:if test='${sequencingCost.getReadType()=="single"}'>selected</c:if>><fmt:message key="jobHomeCreateUpdateQuote.readType_single.label" />
										<option value='paired' <c:if test='${sequencingCost.getReadType()=="paired"}'>selected</c:if>><fmt:message key="jobHomeCreateUpdateQuote.readType_paired.label" />
									</select>
								</td>
								<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostNumberLanes' id='runCostNumberLanes' value="${sequencingCost.getNumberOfLanes()}"></td>
								<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostPricePerLane' id='runCostPricePerLane' value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${sequencingCost.getCostPerLane()}" />">.00</td>
								<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>
							</tr>	
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td align='center'>
								<select name='runCostResourceCategoryId' id='runCostResourceCategoryId' size='1'>
									<option value=''><fmt:message key="wasp.default_select.label" />
									<c:forEach items="${sequencingMachines}" var="resourceCategory">						
										<option value='<c:out value="${resourceCategory.getId()}" />' ><c:out value="${resourceCategory.getName()}" />
									</c:forEach>
								</select>
							</td>
							<td align='center'>	
								<select name='runCostRunType' id='runCostRunType' size='1'>
									<option value=''><fmt:message key="wasp.default_select.label" />
									<option value='high-output'><fmt:message key="jobHomeCreateUpdateQuote.runType_highOutput.label" />
									<option value='rapid-run'><fmt:message key="jobHomeCreateUpdateQuote.runType_rapidRun.label" />
									<option value='standard'><fmt:message key="jobHomeCreateUpdateQuote.runType_standard.label" />
								</select>
							</td>				
							<%--<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='runCostReadLength' id='runCostReadLength' ></td>--%>
							<td align='center'>	
								<select name='runCostReadLength' id='runCostReadLength' size='1'>
									<option value=''><fmt:message key="wasp.default_select.label" />
									<c:forEach items="${allAvailableReadLengths}" var="readLength">						
										<option value='<c:out value="${readLength}" />' ><c:out value="${readLength}" />
									</c:forEach>
								</select>
							</td>
							<%-- <td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostReadType' id='runCostReadType'></td> --%>
							<td align='center'>	
									<select name='runCostReadType' id='runCostReadType' size='1'>
										<option value=''><fmt:message key="wasp.default_select.label" />
										<option value='single' ><fmt:message key="jobHomeCreateUpdateQuote.readType_single.label" />
										<option value='paired'><fmt:message key="jobHomeCreateUpdateQuote.readType_paired.label" />
									</select>
							</td>
							<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostNumberLanes' id='runCostNumberLanes'></td>
							<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostPricePerLane' id='runCostPricePerLane' >.00</td>
							<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>
						</tr>	
					</c:otherwise>
					</c:choose>
					<tr><td colspan="7" align="center"><input style="width:300" type="button" class="addRow" value="<fmt:message key="jobHomeCreateUpdateQuote.addAdditionalRow.label" />"/></td></tr>
				</table>
				
				<br /><br />
				
				<span style='font-weight:bold'>3. <fmt:message key="jobHomeCreateUpdateQuote.additionalCosts.label" />: </span><br /><br />
				<table  class="data" style="margin: 0px 0px">
					<tr class="FormData">
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.reason.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.units.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.costPerUnit.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.action.label" /></td>
					</tr>
					<c:choose>
						<c:when test="${not empty mpsQuote.getAdditionalCosts()}">
							<c:forEach items="${mpsQuote.getAdditionalCosts()}" var="additionalCost">
								<tr>
									<%-- <td align='center'><input type='text' size='20' maxlength='44' name='additionalCostReason' id='additionalCostReason' value="${additionalCost.getReason()}"></td>--%>
									<td align='center'>	
										<select name='additionalCostReason' id='additionalCostReason' size='1'>
											<option value=''><fmt:message key="wasp.default_select.label" />
											<c:forEach items="${additionalCostReasonsList}" var="additionalCostReason">						
												<option value='<c:out value="${additionalCostReason}" />'  <c:if test='${additionalCost.getReason()==additionalCostReason}'>selected</c:if>  ><c:out value="${additionalCostReason}" />
											</c:forEach>
										</select>
									</td>									
									<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalCostUnits' id='additionalCostUnits' value="${additionalCost.getNumberOfUnits()}"></td>
									<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='additionalCostPricePerUnit' id='additionalCostPricePerUnit' value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${additionalCost.getCostPerUnit()}" />">.00</td>
									<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>				
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<%-- <td align='center'><input type='text' size='20' maxlength='44' name='additionalCostReason' id='additionalCostReason'></td>--%>
								<td align='center'>	
									<select name='additionalCostReason' id='additionalCostReason' size='1'>
										<option value=''><fmt:message key="wasp.default_select.label" />
										<c:forEach items="${additionalCostReasonsList}" var="additionalCostReason">						
											<option value='<c:out value="${additionalCostReason}" />' ><c:out value="${additionalCostReason}" />
										</c:forEach>
									</select>
								</td>								
								<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalCostUnits' id='additionalCostUnits'></td>
								<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalCostPricePerUnit' id='additionalCostPricePerUnit' >.00</td>
								<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>
							</tr>
						</c:otherwise>
					</c:choose>
					<tr><td colspan="4" align="center"><input style="width:300" type="button" class="addRow" value="<fmt:message key="jobHomeCreateUpdateQuote.addAdditionalRow.label" />"/></td></tr>
				</table>
				
				<br /><br />
				
				<span style='font-weight:bold'>4. <fmt:message key="jobHomeCreateUpdateQuote.discountsCredits.label" />: </span><br /><br />
				<table  class="data" style="margin: 0px 0px">
					<tr class="FormData">
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.reason.label" /><sup>*</sup></td>
						<td class="label-centered" style="background-color:#FAF2D6"><c:out value="${localCurrencyIcon}" /> Or %</td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.discount.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.action.label" /></td>
					</tr>
					<c:choose>
						<c:when test="${not empty mpsQuote.getDiscounts()}">
							<c:forEach items="${mpsQuote.getDiscounts()}" var="discount">
								<tr>
									<%-- <td align='center'><input onkeydown='robtest_autocomplete(this);'  type='text' size='20' maxlength='44' name='discountReason' id='discountReason'></td>--%>
									<td align='center'>
										<select name='discountReason' id='discountReason' size='1'>
											<option value=''><fmt:message key="wasp.default_select.label" />
											<c:forEach items="${discountReasons}" var="discountReason">
												<c:set value="" var="selected" />
												<c:if test="${discountReason == discount.getReason()}">
													<c:set value="SELECTED" var="selected" />
												</c:if>
												<option value='<c:out value="${discountReason}" />' <c:out value="${selected}" /> ><c:out value="${discountReason}" />
											</c:forEach>
										</select>
									</td>
									<td align='center'>
										<select name='discountType' id='discountType' size='1'>
											<option value=''><fmt:message key="wasp.default_select.label" />
											<c:forEach items="${discountTypes}" var="discountType">
												<c:set value="" var="selected" />
												<c:if test="${discountType == discount.getType()}">
													<c:set value="SELECTED" var="selected" />
												</c:if>
												<option value='<c:out value="${discountType}" />' <c:out value="${selected}" /> ><c:out value="${discountType}" />
											</c:forEach>					
										</select>
									</td>
									<td align='center'><input type='text' style="text-align:right;" size='8' maxlength='8' name='discountValue' id='discountValue' value="${discount.getValue()}" /></td>
									<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<%-- <td align='center'><input onkeydown='robtest_autocomplete(this);'  type='text' size='20' maxlength='44' name='discountReason' id='discountReason'></td>--%>
								<td align='center'>
									<select name='discountReason' id='discountReason' size='1'>
										<option value=''><fmt:message key="wasp.default_select.label" />
											<c:forEach items="${discountReasons}" var="discountReason">
												<option value='<c:out value="${discountReason}" />'  ><c:out value="${discountReason}" />
											</c:forEach>
									</select>
								</td>
								<td align='center'>
									<select name='discountType' id='discountType' size='1'>
									<option value=''><fmt:message key="wasp.default_select.label" />
										<c:forEach items="${discountTypes}" var="discountType">
											<option value='<c:out value="${discountType}" />'  ><c:out value="${discountType}" />
										</c:forEach>
									</select>
								</td>
								<td align='center'><input type='text' style="text-align:right;" size='8' maxlength='8' name='discountValue' id='discountValue' value="" ></td>
								<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>
							</tr>
						</c:otherwise>
					</c:choose>	
					<tr><td colspan="5" align="center"><input style="width:300" type="button" class="addRow" value="<fmt:message key="jobHomeCreateUpdateQuote.addAdditionalRow.label" />"/></td></tr>
				</table>
				<sup>*</sup><span style="font-size:small;color:red"><fmt:message key="jobHomeCreateUpdateQuote.selectDiscountOrCreditOnlyOnce.label" /></span>
				<br /><br />
				<span style='font-weight:bold'>5. <fmt:message key="jobHomeCreateUpdateQuote.comments.label" />: </span><br /><br />
				<table  class="data" style="margin: 0px 0px">
					<tr class="FormData">
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.comment.label" /></td>
						<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeCreateUpdateQuote.action.label" /></td>		
					</tr>
					<c:choose>
						<c:when test="${not empty mpsQuote.getComments()}">
							<c:forEach items="${mpsQuote.getComments()}" var="comment">
								<tr>
									<td align='center'><textarea id="comments" name="comments" cols="60" rows="4" >${comment.getComment()}</textarea><br /></td>
									<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td align='center'><textarea id="comments" name="comments" cols="60" rows="4"></textarea><br /></td>
								<td align='center'><input type="button" class="delRow" value="<fmt:message key="jobHomeCreateUpdateQuote.deleteRow.label" />"/></td>
							</tr>
						</c:otherwise>
					</c:choose>	
					<tr><td colspan="2" align="center"><input style="width:300" type="button" class="addRow" value="<fmt:message key="jobHomeCreateUpdateQuote.addAdditionalRow.label" />"/></td></tr>
				</table>
				
				<br /><br />
				
				<span style="padding:3px; border: 1px solid black;">
					<a href="javascript:void(0);" onclick='$("html, body").animate({ scrollTop: 0 }, "fast");' ><fmt:message key="jobHomeCreateUpdateQuote.returnToTopOfPage.label" /></a>
				</span>
				<br /><br />
			</div>
		</div>
	</form>
</sec:authorize>
