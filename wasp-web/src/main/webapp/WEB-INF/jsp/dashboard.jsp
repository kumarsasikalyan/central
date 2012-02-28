<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<wasp:message />
<div>
	<h1>Dashboard - ${me.firstName} ${me.lastName}</h1>
</div>
<div id="tabs">
	<ul>
		<li><a href="#tabs-home">Home</a></li>
		<sec:authorize access="hasRole('su')">
			<li><a href="#tabs-suUtils">Superuser Utils</a></li>
		</sec:authorize>
		<sec:authorize
			access="hasRole('da-*') or hasRole('su') or hasRole('ga')">
			<li><a href="#tabs-daAdmin">Department Admin</a></li>
		</sec:authorize>
		<sec:authorize
			access="hasRole('lu-*') or hasRole('su') or hasRole('ga')">
			<li><a href="#tabs-labUtils">Lab Utils</a></li>
		</sec:authorize>
		<sec:authorize
			access="hasRole('jv-*') or hasRole('jd-*') or hasRole('su') or hasRole('ga') or hasRole('lu-*') or hasRole('fm') or hasRole('ft')">
			<li><a href="#tabs-jobUtils">Job Utils</a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('fm')">
			<li><a href="#tabs-facilityUtils">Facility Utils</a></li>
		</sec:authorize>
		<li><a href="#tabs-taskList">Task List (<c:out value="${fn:length(tasks)}" /> tasks)</a></li>
	</ul>
	<div id="tabs-home">
		<ul class="navTabs">
			<li>
				<a href='<c:url value="/user/me_ro.do"/>'>My Profile</a>
			</li>
			<sec:authorize access="not hasRole('ldap')">
				<li>
					<a href='<c:url value="/user/mypassword.do"/>'>My Password</a>
				</li>
			</sec:authorize>
			<li>
				<a href='<c:url value="/auth/reauth.do"/>'>Refresh Auth</a>
			</li>
			<li>
				<a href='<c:url value="/lab/newrequest.do"/>'>Request Access to a Lab</a> (Note: requests subject to verification)
			</li>
		</ul>
	</div>


	<sec:authorize access="hasRole('su')">
		<div id="tabs-suUtils">
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/user/list.do"/>'>User Utils</a>
				</li>
				<li>
					<a href='<c:url value="/sysrole/list.do"/>'>System Users</a>
				</li>
				<!-- <li><a href="<c:url value="/department/list.do"/>">Department Utils</a></li> -->
				<li>
					<a href='<c:url value="/lab/list.do"/>'>Lab Utils</a>
				</li>
				<li>
					<a href='<c:url value="/sample/list.do"/>'>Sample Utils</a>
				</li>
				<li>
					<a href='<c:url value="/resource/list.do"/>'>Resource Utils</a>
				</li>
				<li>
					<a href='<c:url value="/run/list.do"/>'>Run Utils</a>
				</li>
				<li>
					<a href='<c:url value="/workflow/list.do"/>'>Workflow Utils</a>
				</li>
			</ul>
		</div>
	</sec:authorize>

	<sec:authorize	access="hasRole('da-*') or hasRole('su') or hasRole('ga')">
		<div id="tabs-daAdmin">
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/department/list.do"/>'>Department Management</a>&nbsp;
					<c:choose>
						<c:when test='${departmentAdminPendingTasks == 0}'>(No Pending Departmental Tasks)</c:when>
						<c:otherwise>
							<span style="color: red"> (<c:out value="${departmentAdminPendingTasks}" /> Pending Department Administrator Task<c:if
									test='${departmentAdminPendingTasks != 1}'>s</c:if>)
							</span>
						</c:otherwise>
					</c:choose>
				</li>
				<c:forEach items="${departments}" var="d">
					<b><c:out value="${d.name}" /> </b>
					<c:set var="departmentId" value="${d.departmentId}" />
					<li>
						<a href='<c:url value="/department/detail/${departmentId}.do"/>'>Department	Detail</a>
					</li>
					<li>
						<a href='<c:url value="/department/pendinglab/list/${departmentId}.do"/>'>PendingLab Approval</a>
					</li>
					<li>
						<a href='<c:url value="/task/daapproval/list/${departmentId}.do"/>'>Pending Department Admin Job Approval</a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</sec:authorize>



	<sec:authorize access="hasRole('lu-*') or hasRole('su') or hasRole('ga')">
		<div id="tabs-labUtils">
			<ul class="navTabs">
				<sec:authorize access="hasRole('su') or hasRole('ga')">
					<li>
						<a href='<c:url value="/lab/allpendinglmapproval/list.do"/>'>All Labs Management</a>&nbsp;
						<c:choose>
							<c:when test='${allLabManagerPendingTasks == 0}'>(No Pending PI/Lab Manager Tasks)</c:when>
							<c:otherwise>
								<span style="color: red"> (<c:out value="${allLabManagerPendingTasks}" /> Pending PI/Lab Manager Task<c:if test='${allLabManagerPendingTasks != 1}'>s</c:if>)
								</span>
							</c:otherwise>
						</c:choose>
					</li>
				</ul>
				</sec:authorize>
				<ul class="navTabs">
					<c:forEach items="${labs}" var="l">
						<br /><b><c:out value="${l.name}" /> </b>
						<c:set var="labId" value="${l.labId}" />
						<li>
							<a href='<c:url value="/lab/detail_ro/${l.departmentId}/${l.labId}.do"/>'>Lab Details</a>
						</li>
						<sec:authorize access="not hasRole('lm-${l.labId}' )">
							<li>
								<a href='<c:url value="/lab/user_list/${l.labId}.do"/>'>Lab	Members</a>
							</li>
						</sec:authorize>
						<sec:authorize access="hasRole('lm-${l.labId}' )">
							<!--     <li><a href="<c:url value="/lab/pendinguser/list/${l.labId}.do"/>">Pending User Approval</a></li> -->
							<li>
								<a href='<c:url value="/lab/user_manager/${l.labId}.do"/>'>User Manager</a>
							</li>
							<!-- li><a href="<c:url value="/task/lmapproval/list/${l.labId}.do"/>">Pending Lab Manager Approval</a></div-->
							<!--  <li><a href="<c:url value="/task/lmapproval/list/${l.labId}.do"/>">Tasks Pending PI/Lab Manager Approval</a> -->
							<li>
								<a href='<c:url value="/lab/pendinglmapproval/list/${l.labId}.do"/>'>Tasks Pending PI/Lab Manager Approval</a>
								<c:choose>
									<c:when test='${labmap.get(l.labId) == 0}'>(No Pending PI/Lab Manager Tasks)</c:when>
									<c:otherwise>
										<span style="color: red"> (<c:out value="${labmap.get(l.labId)}" /> Pending PI/Lab Manager Task<c:if test='${labmap.get(l.labId) != 1}'>s</c:if>)
										</span>
									</c:otherwise>
								</c:choose>
							</li>
						</sec:authorize>
					</c:forEach>
			</ul>
		</div>
	</sec:authorize>

	<div>
		<sec:authorize access="hasRole('jv-*') or hasRole('jd-*') or hasRole('su') or hasRole('ga') or hasRole('lu-*') or hasRole('fm') or hasRole('ft')">
			<div id="tabs-jobUtils">
				<ul class="navTabs">
					<sec:authorize access="hasRole('jv-*')">
						<li>
							<a href='<c:url value="/job/list.do?userId=${me.getUserId()}"/>'>My	Viewable Jobs (<c:out value="${jobViewableCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('jd-*')">
						<li>
							<a href='<c:url value="/jobsubmit/list.do?userId=${me.getUserId()}"/>'>Drafted Jobs (<c:out value="${jobDraftCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize
						access="hasRole('su') or hasRole('ga') or hasRole('fm') or hasRole('ft')">
						<li>
							<a href='<c:url value="/job/list.do"/>'>View All Jobs (<c:out value="${jobsAllCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('lu-*')">
						<li>
							<a href='<c:url value="/jobsubmit/create.do" />'>Submit a Job</a>
						</li>
					</sec:authorize>
				</ul>
			</div>
		</sec:authorize>

	</div>

	<br />
	<sec:authorize access="hasRole('fm') or hasRole('ft')">
		<div id="tabs-facilityUtils">
			<h2>Job Quoting</h2>
			<ul class="navTabs">
				<sec:authorize access="hasRole('fm')">
					<li>
						<a href='<c:url value="/task/fmrequote/list.do"/>'>Requote Pending Jobs</a>
					</li>
					<li>
						<a href='<c:url value="/task/fmpayment/list.do"/>'>Receive Payment for Jobs</a>
					</li>
				</sec:authorize>
			</ul>
			<br />
			<h2>Sample Utils</h2>
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/task/samplereceive/list.do"/>'>Sample Receiver</a>
				</li>
		
		
				<li>
					<a href='<c:url value="/sampleDnaToLibrary/listJobSamples/2.do"/>'>List	Samples for Job 2</a>
				</li>
				<li>
					<a href='<c:url value="/sampleDnaToLibrary/listJobSamples/3.do"/>'>List Samples for Job 3</a>
				</li>
				<li>
					<a href='<c:url value="/sampleDnaToLibrary/listJobSamples/4.do"/>'>List Samples for New Job 4</a>
				</li>
				<li>
					<a href='<c:url value="/sampleDnaToLibrary/listJobSamples/5.do"/>'>List	Samples for New Job 5</a>
				</li>
				<li>
					<a href='<c:url value="/sampleDnaToLibrary/listJobSamples/6.do"/>'>List	Samples for Job 6</a>
				</li>
				<li>
					<a href='<c:url value="/sampleDnaToLibrary/listJobSamples/7.do"/>'>List	Samples for Job 7</a>
				</li>
			</ul>
			<br />
			<h2>Platform Unit Utils</h2>
			<ul class="navTabs">
				<li>
					<a
						href='<c:url value="/facility/platformunit/limitPriorToPlatUnitAssign.do" />'>List / Create</a>
				</li>
				<li>
					<a href='<c:url value="/facility/platformunit/assign.do" />'>Platform Unit assignment (must remove this)</a>
				</li>
				<li>
					<a href='<c:url value="/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0" />'>Platform Unit assignment</a>
				</li>
			</ul>
		</div>
	</sec:authorize>
	<div id="tabs-taskList">
		<ul class="navTabs">
			<c:forEach items="${tasks}" var="task">
				<li>
					<a href='<c:url value="${task.listMap}"/>'>${task.task.name}/${task.status}</a>	(${task.stateCount})
				</li>
	
			</c:forEach>
		</ul>
	</div>
</div>








