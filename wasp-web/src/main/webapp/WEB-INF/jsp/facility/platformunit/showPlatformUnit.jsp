<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<head>
<script src="/wasp/scripts/jquery/jquery-1.7.1.js" type="text/javascript"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 
<script>

$(document).ready(function(){
	$("#resourceId").change(function(){
		var id = $(this).val();
		//alert('jquery test; resourceId = ' + resourceId);		
		//$(".readType").html('<option value="">---SELECT A READ TYPE---</option><option value="50">---50---</option>');
		if(id != ''){
			$.ajax({
				type: "POST",
				url: "/wasp/facility/platformunit/ajaxReadType.do",
				data: "resourceId=" + id,
				cache: false,
				success: function(response){
					var partsArray = response.split('****');
					$("#readType").html(partsArray[0]);
					$("#readLength").html(partsArray[1]);
					$("#submitButtonCreateNewRun").attr("disabled", false);
				} 
			}); 
		}
	});
});





function toggleDisplayOfCreateNewRunForm(action){
	//alert("in new div; action = " + action);
	var newCreateRunButtonDiv = document.getElementById("newCreateRunButtonDiv");
	var newCreateRunFormDiv = document.getElementById("newCreateRunFormDiv");
	if(action=="create"){
		newCreateRunButtonDiv.style.display = 'none';
		newCreateRunFormDiv.style.display = 'block';
	}
	if(action=="cancel"){
		newCreateRunButtonDiv.style.display = 'block';
		newCreateRunFormDiv.style.display = 'none';
		var runName = document.getElementById("runName");
		var resourceId = document.getElementById("resourceId");
		var readLength = document.getElementById("readLength");
		var readType = document.getElementById("readType");
		var submitButtonCreateNewRun = document.getElementById("submitButtonCreateNewRun");
		runName.value="";
		resourceId.value="";
		readLength.innerHTML="";
		readType.innerHTML="";	
		submitButtonCreateNewRun.disabled = "true";
	}
}
function validateCreateNewRunForm(){
	var runName = document.getElementById("runName");
	var resourceId = document.getElementById("resourceId");
	var readLength = document.getElementById("readLength");
	var readType = document.getElementById("readType");
	if(runName.value==""){
		alert("Please provide a name for this run");
		runName.focus();
		return false;
	}
	if(resourceId.value==""){
		alert("Please select a machine");
		resourceId.focus();
		return false;
	}
	if(readLength.value==""){
		alert("Please select a read length");
		readLength.focus();
		return false;
	}
	if(readType.value==""){
		alert("Please select a read type");
		readType.focus();
		return false;
	}
	if(!confirm("Submit?")){
		return false;
	}
	document.getElementById("newRunForm").submit();
}
function getResourceMeta(obj){//no longer used
	if(obj.value != ""){
		//alert("inside getResourceMeta()");
		document.getElementById("testdiv3").style.display = 'block';
	}
}
function toggleDisplayAddNewControlForm(action, counter){
	//alert("inside toggleTest; counter = " + counter);
	var newControlAnchorName = "newControlAnchor_" + counter;
	var newControlAnchor = document.getElementById(newControlAnchorName);
	var newControlFormDivName = "idNewControlFormDiv_" + counter;
	var newControlFormDiv = document.getElementById(newControlFormDivName);
	if(action == "show_form"){
		newControlAnchor.style.display = 'none';
		newControlFormDiv.style.display = 'block';
	}
	else if(action == "cancel_form"){
		newControlAnchor.style.display = 'block';
		newControlFormDiv.style.display = 'none';
		var newControlIdName = "newControlId_" + counter;
		var newControlId = document.getElementById(newControlIdName);
		var newControlConcInLanePicoMName = "newControlConcInLanePicoM_" + counter;
		var newControlConcInLanePicoM = document.getElementById(newControlConcInLanePicoMName);
		newControlId.value="";
		newControlConcInLanePicoM.value="";
	}
}
function validateAddNewControlToLaneForm(counter){
	//alert("testing alert in validate add new control. counter=" + counter);
	var newControlIdName = "newControlId_" + counter;
	var newControlId = document.getElementById(newControlIdName);
	var newControlConcInLanePicoMName = "newControlConcInLanePicoM_" + counter;
	var newControlConcInLanePicoM = document.getElementById(newControlConcInLanePicoMName);
	if(newControlId.value==""){
		alert("Please select a control");
		newControlId.focus();
		return false;
	}
	var trimmed = newControlConcInLanePicoM.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){//must trim before testing
		alert("Please provide a final concentration for this control");
		newControlConcInLanePicoM.value = "";
		newControlConcInLanePicoM.focus();
		return false;
	}
	else{
		//alert("OK to submit, but not doing it");
		var theFormName = "addNewControlToLaneForm_" + counter;
		var theForm = document.getElementById(theFormName);
		theForm.submit();
	}
}

function toggleDisplayOfUpdateForm(instruction, idCounter){
	//alert ("more testing; id = " + idCounter);
	var formDivName = "updatePicoFormDiv_" + idCounter;
	var formDiv = document.getElementById(formDivName);
	var editAnchorDivName = "editAnchorDiv_" + idCounter;
	var editAnchorDiv = document.getElementById(editAnchorDivName);
	
	if(instruction == 'show'){
		editAnchorDiv.style.display = 'none';
		formDiv.style.display = 'block';
	}
	else if(instruction == 'cancel'){
		editAnchorDiv.style.display = 'block';
		formDiv.style.display = 'none';
		var textInputName = "libConcInLanePicoM_" + idCounter;
		var textInputObject = document.getElementById(textInputName);
		textInputObject.value="";
	}	
}
function validateUpdateForm(idCounter){
	//alert("validateUpdate; id = " + idCounter);
	var textInputName = "libConcInLanePicoM_" + idCounter;
	var textInputObject = document.getElementById(textInputName);
	var trimmed = textInputObject.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){
		alert("Please provide a value");
		textInputObject.value = "";
		textInputObject.focus();
		return false;
	}
	else{
		//alert("submit");
		var theFormName = "updatePicoForm_" + idCounter;
		var theForm = document.getElementById(theFormName);
		theForm.submit();
	}
	
}

</script>
</head>

<c:set var="idCounter" value="0" scope="page" />
<c:set var="idNewControlCounter" value="0" scope="page" />


<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD">PlatformUnit:</td><td class="DataTD"><c:out value="${platformUnit.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Type:</td><td class="DataTD"><c:out value="${platformUnit.sampleSubtype.name}" /></td></tr>
<c:forEach items="${platformUnit.sampleBarcode}" var="sampleBarcodeItem">
	<tr class="FormData"><td class="CaptionTD">Barcode:</td><td class="DataTD"><c:out value="${sampleBarcodeItem.barcode.barcode}" /></td></tr></c:forEach>
<!--  <tr class="FormData"><td class="CaptionTD">Lanes:</td><td class="DataTD"><c:out value="${platformUnit.sampleSource.size()}" /></td></tr> -->
<c:forEach items="${platformUnit.sampleMeta}" var="pusm">
	<c:if test="${fn:substringAfter(pusm.k, '.') != 'comment'}">
		<tr class="FormData"><td class="CaptionTD" style="text-transform: capitalize"><c:out value="${fn:toLowerCase(fn:substringAfter(pusm.k, '.'))}" />:</td><td class="DataTD"><c:out value="${pusm.v}" /></td></tr>
	</c:if>
</c:forEach>
<c:forEach items="${platformUnit.sampleMeta}" var="pusm">
	<c:if test="${fn:substringAfter(pusm.k, '.') == 'comment' && pusm.v != '' }">
		<tr class="FormData"><td class="CaptionTD" style="text-transform: capitalize"><c:out value="${fn:toLowerCase(fn:substringAfter(pusm.k, '.'))}" />:</td><td class="DataTD"><textarea style='font-size:9px' READONLY cols='25' rows='4' wrap='virtual'><c:out value="${pusm.v}" /></textarea></td></tr>
	</c:if>
</c:forEach>
</table>

<c:if test="${runList.size()==0}">
<br />
<div id="newCreateRunButtonDiv">
	<input type="button" value="Create New Run" onclick='toggleDisplayOfCreateNewRunForm("create")' />
</div>
<div id="newCreateRunFormDiv" style="display:none">
		<form id="newRunForm"  method='post' action="<c:url value="/facility/platformunit/createNewRun.do" />" >
		<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
		<table class="EditTable ui-widget ui-widget-content">
			<tr class="FormData"><td class="CaptionTD">Run Name: </td><td class="DataTD"><input type='text' name='runName' id='runName' size='25' maxlength='30' /></td></tr>
			<tr class="FormData"><td class="CaptionTD">Machine: </td><td class="DataTD">
				<select id="resourceId" name="resourceId" >
		  			<option value="">---SELECT A MACHINE---</option>
						<c:forEach items="${resources}" var="resource">
							<option value='<c:out value="${resource.getResourceId()}" />'><c:out value="${resource.getName()}" /> - <c:out value="${resource.getResourceCategory().getName()}" /></option>
						</c:forEach> 
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD">Read Length: </td><td class="DataTD">
				<select id="readLength" name="readLength">
				</select>			
			</td></tr>
			<tr class="FormData"><td class="CaptionTD">Read Type: </td><td class="DataTD">
				<select id="readType" name="readType">
				</select>			
			</td></tr>
			<tr class="FormData"><td colspan="2" class="CaptionTD">
				<input id="submitButtonCreateNewRun" disabled = "disabled" type="button" value="Submit" onclick='validateCreateNewRunForm()' />&nbsp;<input type="button" value="Cancel" onclick='toggleDisplayOfCreateNewRunForm("cancel")' />
			</td></tr>
		</table>
		</form>
</div>
</c:if>
<c:if test="${runList.size()>0}"><table><tr class="FormData"><tr class="FormData"><td colspan="2" class="value-centered">Run Details: <c:out value="${runList.get(0).name}" /></td></tr></table></c:if>


















<c:if test="${platformUnit.sampleSource.size() > 0}">

	<table class="data">
		<tr><td colspan="${platformUnit.sampleSource.size()}" class="label-centered" nowrap><c:out value="${platformUnit.name}" /></td></tr>
		<tr>
			<c:forEach items="${platformUnit.sampleSource}" var="ss1">
				<c:set var="cell" value="${ss1.sampleViaSource}" scope="page" />
					<td class="label-centered" nowrap>Lane: <c:out value="${fn:substringAfter(cell.name, '/')}" /></td>
			</c:forEach>
		</tr>
		<tr>
		  
			<c:forEach items="${platformUnit.sampleSource}" var="ss1">
				<c:set var="numberControlLibrariesPerLane" value="0" scope="page" />
				<c:set var="cell" value="${ss1.sampleViaSource}" scope="page" />
				 <td class="value-centered-small-heavyborder" nowrap>			
					<c:forEach items="${cell.sampleSource}" var="ss2">
				  		<c:set var="controlLibrary" value="${ss2.sampleViaSource}" scope="page" />
				  		<c:if test="${controlLibrary.sampleSubtype.getIName() == 'controlLibrarySample'}">
				  			<c:set var="numberControlLibrariesPerLane" value="${numberControlLibrariesPerLane + 1}" scope="page" />
				  			<c:out value="${controlLibrary.name}" /> 
				  			<c:set var="controlLibraryMeta" value="${controlLibrary.sampleMeta}" scope="page" />
							<c:forEach items="${controlLibraryMeta}" var="controlLibraryMetaItem">
								<c:if test="${fn:substringAfter(controlLibraryMetaItem.k, 'Library.') == 'adaptor'}">
            						<br /><c:out value="${adaptors.get(controlLibraryMetaItem.v).adaptorset.name}"/>
            						<br />Index <c:out value="${adaptors.get(controlLibraryMetaItem.v).barcodenumber}"/>: <c:out value="${adaptors.get(controlLibraryMetaItem.v).barcodesequence}"/>
            					</c:if> 
            				</c:forEach>
            				<c:set var="ss2Meta" value="${ss2.sampleSourceMeta}" scope="page" />
            				<c:forEach items="${ss2Meta}" var="ss2MetaItem">
            					<c:if test="${ss2MetaItem.k=='libConcInLanePicoM'}"><br />Conc. On Lane: <c:out value="${ss2MetaItem.v}"/> pM </c:if>					
            				</c:forEach>
            				
            				<form  name='removeLib' method='post' action="<c:url value="/facility/platformunit/assignRemove.do" />" onsubmit='return confirm("Remove control from this lane?");'>
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='samplesourceid' value='<c:out value="${ss2.sampleSourceId}" />'/>
							<input type='submit' value='Remove Control'/>
							</form>	
            				
            					
            				<hr>
            			</c:if>
					</c:forEach>			  						  		
					<c:if test="${numberControlLibrariesPerLane == 0 }">
						No Control On Lane
						<hr>
					</c:if>
					
					<c:set var="idNewControlCounter" value="${idNewControlCounter + 1}" scope="page" />
					<a href="javascript:void(0)" id="newControlAnchor_<c:out value="${idNewControlCounter}" />" onclick="toggleDisplayAddNewControlForm('show_form',<c:out value="${idNewControlCounter}" />)">Add Control</a>
					<div id="idNewControlFormDiv_<c:out value="${idNewControlCounter}" />" style="display:none">
						<form id="addNewControlToLaneForm_<c:out value="${idNewControlCounter}" />"  method='post' action="<c:url value="/facility/platformunit/addNewControlToLane.do" />" >
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='cellId' value='<c:out value="${cell.sampleId}" />'/>
							<table class="data">
								<tr><td class="value-centered-small">
								<select id="newControlId_<c:out value="${idNewControlCounter}" />" name="newControlId">
									<option value="">---SELECT A CONTROL---</option>
									 <c:forEach items="${controlLibraryList}" var="controlLibrary">
									 	<option value='<c:out value="${controlLibrary.sampleId}" />'><c:out value="${controlLibrary.name}" />
									 	<c:forEach items="${controlLibrary.sampleMeta}" var="controlLibrarySampleMetaItem">
									 		<c:if test="${fn:substringAfter(controlLibrarySampleMetaItem.k, 'Library.') == 'adaptor'}">
            									&nbsp;-&nbsp;<c:out value="${adaptors.get(controlLibrarySampleMetaItem.v).adaptorset.name}"/> [Index <c:out value="${adaptors.get(controlLibrarySampleMetaItem.v).barcodenumber}"/> (<c:out value="${adaptors.get(controlLibrarySampleMetaItem.v).barcodesequence}"/>)]
            								</c:if> 
									 	</c:forEach>
									 	</option>
									 </c:forEach>
								</select>
								
								</td></tr>
								<tr><td class="value-centered-small">Final Conc. (pM): <input type='text' name='newControlConcInLanePicoM' id="newControlConcInLanePicoM_<c:out value="${idNewControlCounter}" />" size='3' maxlength='5' ></td></tr>
								<tr><td class="value-centered-small">
								<input type="button" value="Add" onclick='validateAddNewControlToLaneForm(<c:out value="${idNewControlCounter}" />)' />&nbsp;<input type="button" value="Cancel" onclick='toggleDisplayAddNewControlForm("cancel_form", <c:out value="${idNewControlCounter}" />)' />
								</td></tr>
							</table>
						</form>
					</div>										
				</td>
			</c:forEach>			
		</tr>
		
		<tr>
		<c:forEach items="${platformUnit.sampleSource}" var="ss1">
			<c:set var="cell" value="${ss1.sampleViaSource}" scope="page" />
			<td class="value-centered-top-small-heavyborder" >
				<c:set var="counter" value="1" scope="page" />
				<c:set var="numberRegularLibrariesPerLane" value="0" scope="page" />
				<c:forEach items="${cell.sampleSource}" var="ss2">
				  <c:set var="library" value="${ss2.sampleViaSource}" scope="page" />
				  <c:if test="${counter > 1}"><hr></c:if>
				  
				  <c:if test="${library.sampleSubtype.getIName() != 'controlLibrarySample'}">	
					<c:set var="numberRegularLibrariesPerLane" value="${numberRegularLibrariesPerLane + 1}" scope="page" />						
					<c:out value="${library.name}" />
					<c:set var="ss2Meta" value="${ss2.sampleSourceMeta}" scope="page" />
					<c:forEach items="${ss2Meta}" var="ss2MetaItem">
						<c:if test="${ss2MetaItem.k=='jobId'}"><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${ss2MetaItem.v}.do" />"> (Job: J<c:out value="${ss2MetaItem.v}"/>)</a></c:if>
					</c:forEach>
					<br />				
					<c:set var="libraryMeta" value="${library.sampleMeta}" scope="page" />
					<c:forEach items="${libraryMeta}" var="libraryMetaItem">
						<c:if test="${fn:substringAfter(libraryMetaItem.k, 'Library.') == 'adaptor'}">
            				<c:out value="${adaptors.get(libraryMetaItem.v).adaptorset.name}"/><br />
            				Index <c:out value="${adaptors.get(libraryMetaItem.v).barcodenumber}"/>: <c:out value="${adaptors.get(libraryMetaItem.v).barcodesequence}"/><br />
            			</c:if> 
					</c:forEach>
					<c:forEach items="${ss2Meta}" var="ss2MetaItem">
						<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
						<!--  	<c:if test="${ss2MetaItem.k=='libConcInLanePicoM'}">Conc. On Lane: <c:out value="${ss2MetaItem.v}"/> pM <br /></c:if>  -->
						<c:set var="currentConcentration" value="" scope="page" />
						<div id="editAnchorDiv_<c:out value="${idCounter}" />" >
						<c:if test="${ss2MetaItem.k=='libConcInLanePicoM'}"><c:set var="currentConcentration" value="${ss2MetaItem.v}" scope="page" />Conc. On Lane: <c:out value="${ss2MetaItem.v}"/> pM <a href="javascript:void(0)" onclick='toggleDisplayOfUpdateForm("show", <c:out value="${idCounter}" />);'>[Edit]</a><br />	</c:if>					
						</div>	
						
						<div id="updatePicoFormDiv_<c:out value="${idCounter}" />" style="display:none">
						<form id="updatePicoForm_<c:out value="${idCounter}" />"  method='post' action="<c:url value="/facility/platformunit/updateConcInLane.do" />" >
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='sampleSourceMetaId' value='<c:out value="${ss2MetaItem.sampleSourceMetaId}" />'/>
							<table class="data">
								<tr><td class="value-centered-small">Current Conc. (pM): <c:out value="${currentConcentration}" /></td></tr>
								<tr><td class="value-centered-small">New Conc. (pM): <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5' ></td></tr>
								<tr><td class="value-centered-small">
								<input type="button" value="Update" onclick='validateUpdateForm(<c:out value="${idCounter}" />)' />&nbsp;<input type="button" value="Cancel" onclick='toggleDisplayOfUpdateForm("cancel", <c:out value="${idCounter}" />)' />
								</td></tr>
							</table>
						</form>
						</div>						
					
					</c:forEach>
					<form  name='removeLib' method='post' action="<c:url value="/facility/platformunit/assignRemove.do" />" onsubmit='return confirm("Remove library from this lane?");'>
						<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
						<input type='hidden' name='samplesourceid' value='<c:out value="${ss2.sampleSourceId}" />'/>
						<input type='submit' value='Remove Library'/>
					</form>					
					<c:set var="counter" value="${counter + 1}" scope="page" />
				  </c:if>	
				</c:forEach>
			
			<c:if test="${numberRegularLibrariesPerLane == 0 }">
				No Libraries On Lane
			</c:if>	
			
		</td>
		</c:forEach>		
		</tr>
	</table>
</c:if>


