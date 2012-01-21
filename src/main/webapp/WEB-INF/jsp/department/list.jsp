<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<font color="red"><wasp:message /></font>

<sec:authorize access="hasRole('su') or hasRole('ga-*')">
	<div>
		<h1><fmt:message key="department.create.label" /></h1>
		<div class="instructions">
			<%@ include file="/WEB-INF/jsp/lorem.jsp" %>
		</div>

		<form name="f" action="<c:url value='/department/create.do'/>" method="POST" onsubmit='return validate();'>
			<table class="data">
				<tr><td class="label"><fmt:message key="department.list_department.label" />:</td><td class="input"><input type='text' name='departmentName' value=''/></td></tr>
				<tr><td class="label"><fmt:message key="department.detail_administrator_name.label" />:</td><td class="input"><input id="adminName" name='adminName' value='' /></td></tr>
		 
				</table>
				<div class="submit">
					<input type="submit" value="<fmt:message key="department.list.data" />" /></td></tr>
 				</div>
			</form>
	</div>
</sec:authorize>

<div>
<h1><fmt:message key="department.list.label" /></h1>
<div class="instructions">
  <%@ include file="/WEB-INF/jsp/lorem.jsp" %>
</div>

<div class="action">
 	<c:choose>
		<c:when test='${departmentAdminPendingTasks == 0}'>
			No Pending Department Administrator Tasks
	 	</c:when>
	 	<c:otherwise>
		 	<a style="color:red" href="<c:url value="/department/dapendingtasklist.do"/>"><c:out value="${departmentAdminPendingTasks}" /> Pending Department Administrator Task<c:if test='${departmentAdminPendingTasks != 1}'>s</c:if></a>
		</c:otherwise>
	</c:choose>
</div>

<table class="data list">
	<c:forEach items="${department}" var="d">
		<tr>
		<td class="value"><a href="/wasp/department/detail/<c:out value="${d.departmentId}" />.do"><c:out value="${d.name}" /></a></td>
		<td class="action"><c:choose><c:when test="${d.isActive == 1}"> active</c:when><c:otherwise> inactive</c:otherwise></c:choose></td>
		</tr>
	</c:forEach>
</table>
