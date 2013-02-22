<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
<script>
	function validate(theform){
		if(!theform.qcStatus[0].checked && !theform.qcStatus[1].checked){
			alert("<fmt:message key="task.cellLibraryqc_validatePassFailAlert.label" />");
			return false;
		}
		var commentObj = theform.comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(theform.qcStatus[1].checked && trimmedCommentValue.length==0){
			alert("<fmt:message key="task.cellLibraryqc_validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
			return false;
		}
		return true;
	}
	function selectedExclude(formId){
		var commentObj = document.getElementById(formId).comment;
		var commentValue = commentObj.value; 
		var trimmedCommentValue = commentValue.replace(/^\s+|\s+$/g, "");
		if(trimmedCommentValue.length==0){
			alert("<fmt:message key="task.cellLibraryqc_validateCommentAlert.label" />");
			if(commentValue.length>0){
				commentObj.value = "";
			}
			commentObj.focus();
		}


	}
</script>
