<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Logger Setup</title>
  </head>
  
  <body>
    Please choose the logger and the level:
    
    <form action="${pageContext.request.contextPath}/admin/logger" method="post">
    	<table CELLPADDING="5" CELLSPACING="0" BORDER="1">
    		<tr>
    			<td COLSPAN="2"><h2>Enable Disable Logger</h2></td>
    		</tr>
    		<tr>
    			<td>Choose Logger:<br>Format: LoggerClass [Current Level] -> Parent Logger<br>
    			
    				<select name='loggerName' MULTIPLE SIZE='8'><c:forEach var="logger" items="${loggerList}">
    					<option VALUE='${logger.name}'>${logger.name} [${logger.level}] -> ${logger.parent}</option>
    				</c:forEach></select>    			
    			
    			</td>
    			<td>Choose Level:<br>
    				<select name='loggerLevel'>
    					<option value="ALL">All</option>
    					<option value="DEBUG">Debug</option>
    					<option value="ERROR">Error</option>
    					<option value="FATAL">Fatal</option>
    					<option value="INFO">Info</option>
    					<option value="OFF">Off</option>
    					<option value="WARN">Warn</option>
    				</select>
    			</td>
    		</tr>
    		<tr>
    			<td COLSPAN="2"><INPUT TYPE="Submit" NAME='Submit' VALUE='Apply Changes'><BR>
    				(If you wish to disable all logging then find "root" in the list below and apply a level)
    			</td>
    		</tr>
    	</table>
    </form>
  </body>
</html>