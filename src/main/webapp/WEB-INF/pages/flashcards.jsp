<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String sessionName = (String)request.getAttribute("sessionName");
String sessionId = (String)request.getAttribute("sessionId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Flashcards: ${sessionName}</title>
<style>
.content {
  width: 700px;
  margin-left: auto;
  margin-right: auto;
  font-family: arial;
}
.header {
  text-align: center;
  font-size: 40px;
}
.flashcard {
  margin-bottom: 30px;
}
.question {
  margin: 20px;
}
.question_label {
  font-size: 26px;
  color: #BFB8B8;
}
.question_text {
  display: inline-block;
  text-indent: 30px;
}
.answer {
  margin: 20px;
}
.answer_label {
  font-size: 26px;
  color: #BFB8B8;
}
.answer_text {
  display: inline-block;
  text-indent: 30px;
}
.reload_home {
  float: right;
}
</style>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
 
$(document).ready(function(){
 
        $(".slidingDiv").hide();
        $(".show_hide").show();
 
    $('.show_hide').click(function(){
    $(".slidingDiv").slideToggle();
    });
 
});
 
</script>
</head>
<body>
<div class="content">
	<div class="header">
	${sessionName}
	</div>
	<c:forEach items="${flashcards}" var="flashcards">
		<div class="flashcard">
	    <div class="question"><span class="question_label">Question:</span><br />
	    <span class="question_text">${flashcards.key}</span></div>
	    <div class="answer"><span class="answer_label">Answer:</span><br />
	    <span class="answer_text">${flashcards.value}</span></div>
		</div>
	</c:forEach>
	<div class="reload_home">
	<form action="flashcards" method="post">
		<input type="hidden" name="flashcardSession" value="${sessionId}">
		<input type="submit" value="Next question">
	</form>
	<form action="goToResults" method="post">
		<input type="hidden" name="sessionId" value="${sessionId}">
		<input type="submit" value="Back to main page">
	</form>
	</div>
</div>
</body>
</html>