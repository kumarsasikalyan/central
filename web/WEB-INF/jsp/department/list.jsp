<%@ include file="/WEB-INF/jsp/include.jsp" %>



<h1>List List</h1>
<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${department}" var="d">
<div>
<tr>
<td>
<a href="/wasp/department/detail/<c:out value="${d.departmentId}" />.do"><c:out value="${d.name}" /></a>
</td>
<td>
</td>
</tr>
</c:forEach>
</table>

