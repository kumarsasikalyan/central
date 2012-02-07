<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

    <h1>
      <c:out value="${run.name}"/>
    </h1>

<p>
	<font color="blue"><wasp:message /> </font>
</p>

    <div>
      Resource: <a href="/resource/detail_ro/<c:out value="${run.resourceId}"/>.do"><c:out value="${run.resource.name}"/></a>
    </div>

<table>

	<c:set var="_area" value="run" scope="request" />
	<c:set var="_metaList" value="${run.runMeta}" scope="request" />
	<c:import url="/WEB-INF/jsp/meta_ro.jsp" />

    <p>
      Sample 
      <a href="/wasp/sample/detail/<c:out value="${run.sampleId}"/>.do">
        <c:out value="${run.sample.name}"/>
      </a>
    </p>

    <p>
    Run Lanes
    <c:forEach items="${runlane}" var="rl">
      <div>
      -- Samples:
      <a href="/wasp/sample/detail/<c:out value="${rl.sampleId}"/>.do">
        <c:out value="${rl.sample.name}"/>
      </a>
      -- ResourceLanes:
      (<a href="/wasp/run/lane/detail/<c:out value="${rl.runId}"/>/<c:out value="${rl.runLaneId}"/>.do">
        <c:out value="${rl.resourceLane.name}"/>
      </a>)
      </div>
    </c:forEach>
    </p>

    <h2>FILES</h2>
    <c:forEach items="${runfile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="/wasp/run/file/<c:out value="${f.runId}"/>/<c:out value="${f.fileId}"/>.do">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>
