<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>New flashcard session</title>
<style>
.content {
  width: 700px;
  margin-left: auto;
  margin-right: auto;
  font-family: arial;
}
</style>
</head>
<body>
<div class="content">
	<form action="createSession" method="post">
	<table border=0>
	<tr>
	<td>Session ID:</td><td><input type="text" name="sessionId"></td>
	</tr><tr>
	<td>Session name:</td><td><input type="text" name="sessionName"></td>
	</tr><tr>	
	<td><input type="submit" value="Submit data"></td>
	</tr>
	</table>
	</form>
</div>
</body>
</html>
