<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- template for pages that do NOT have a JQGrid table --%>

<%@ page import="edu.yu.einstein.wasp.dao.impl.DBResourceBundle" %> 
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title> 	 	
    <wasp:pageTitle/> 
  </title>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
  <script>
    var waspOnLoad=function() {
      // re-define the waspOnLoad var 
      // in head-js if you need custom body 
      // onLoad function
    }
  </script>
  <tiles:insertAttribute name="head-js" />
</head>

<body onload='waspOnLoad();'>
  <!-- top -->
  <sec:authorize access="isAuthenticated()">
    <header id="pageHeader">
      <nav>
        <a href="/wasp/j_spring_security_logout" class="button right">Logout</a>
        <a href="/wasp/dashboard.do"><img src="/wasp/css/wasp_logo.jpg" alt="WASP" width="154" height="107" /></a>
      </nav>
    </header>
  </sec:authorize>
  <sec:authorize access="! isAuthenticated()">
    <header id="pageHeader">
      <nav>
        <a href="/wasp/"><img src="/wasp/css/wasp_logo.jpg" alt="WASP" width="154" height="107" /></a>
      </nav>
    </header>
  </sec:authorize>
  <!-- /top -->
 
  <tiles:insertAttribute name="body-content" />
</body>
</html>
