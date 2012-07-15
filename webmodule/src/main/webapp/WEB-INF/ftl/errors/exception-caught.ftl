<html>
<head>
	<title><@spring.message 'exception.caught.title'/></title>
</head>
<body>
	<p><@spring.message 'exception.caught.description'/></p>
	<div id="errors">
		<pre>${exception}</pre>
	</div>
	<p><@spring.message 'exception.caught.spring.mvc'/></p>
</body>
</html>