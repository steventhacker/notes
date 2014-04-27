<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert New Question</title>
<style>
.content {
  width: 700px ;
  margin-left: auto ;
  margin-right: auto ;
  font-family: arial;
}
.finalize {
  float: right;
}
</style>
</head>
<body>
<div class="content">
	<div class="finalize">
	<form action="finalize" method="post">
	<input type="hidden" name="sessionId" value="${sessionId}">
	<input type="submit" value="Finalize">
	</form>
	</div>
	<form action="question" method="post">
	<%
	String acceptedQuestion = request.getParameter("question");
	String acceptedAnswer = request.getParameter("answer");
	if (acceptedQuestion != null) {
		out.print("Question and answer submitted:<br />");
		out.print(acceptedQuestion+"<br />");
		out.print(acceptedAnswer);
	}
	%>
	<input type="hidden" name="sessionId" value="${sessionId}">
		<div class="title">
		Question:
		</div>
		<div class="content">
		<textarea name="question" cols=70 rows=10></textarea>
		</div>
		<div class="title">
		Answer:
		</div>
		<div class="content">
		<textarea name="answer" cols=70 rows=10></textarea>
		</div>
	<input type="submit" value="Submit data">
	</form>
</div>
</body>
</html>