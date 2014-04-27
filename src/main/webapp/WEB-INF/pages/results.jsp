<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Results</title>
<style>
.content {
  width: 700px ;
  margin-left: auto ;
  margin-right: auto ;
  font-family: arial;
}
.result_set {
  padding: 10px;
}
</style>
</head>
<body>
<div class="content">
	<div class="written_header">
	Results written!   
	</div>
	<div class="results_main">
	Previous sessions:
	<form action="flashcards" method="post">
	<%
	HashMap<String, String> tables = (HashMap)request.getAttribute("tables");
	Iterator iterator = tables.entrySet().iterator();
	String sessionId;
	while (iterator.hasNext()) {
		Map.Entry pairs = (Map.Entry)iterator.next();
		sessionId = (String)pairs.getKey();
		out.println("<div class=\"result_set\">");
		out.println("<input type=\"radio\" name=\"flashcardSession\" value=\""+sessionId+"\">"+pairs.getValue()+"<br />");
		out.println("</div>");
	}
	%>
	<input type="hidden" name="sessionName" value="${sessionId}">
	<input type="submit" value="View Flashcards">
	</form>
	</div>
</div>
</body>
</html>