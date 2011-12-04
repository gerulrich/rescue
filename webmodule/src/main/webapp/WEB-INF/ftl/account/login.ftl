<!DOCTYPE html>
<html>
<head>
	<link rel="shortcut icon" href="<@spring.url '/img/favicon.ico'/>" type="image/x-icon" />
	<title><@spring.message "signin.title"/></title>
	<link rel='stylesheet' id='wp-admin-css'  href="<@spring.url '/static/css/wp-admin.css'/>" type='text/css' media='all' />
</head>
<body class="login">

	<div id="login">
		<h1><a href="#" title="rescue.cloudfoundry.com"><span class="hide">rescue.cloudfoundry.com</span></a></h1>
	<@widget.loginError/>
	<form name="loginform" id="loginform" action="<@spring.url '/j_spring_security_check'/>" method="post">
		<p>
			<label for="user_login"><@spring.message "signin.username"/><br />
				<input type="text" name="j_username" id="user_login" class="input" value="" size="20" tabindex="1" />
			</label>
		</p>
		<p>
			<label for="user_pass"><@spring.message "signin.password"/>:<br />
				<input type="password" name="j_password" id="user_pass" class="input" value="" size="20" tabindex="2" />
			</label>
		</p>
		<p class="forgetmenot">
			<label for="rememberme">
				<input type='checkbox' name='remember_me' id="rememberme" tabindex="3"/><@spring.message "signin.rememberme"/>
			</label>
		</p>
		<p class="submit">
			<input type="submit" name="wp-submit" id="wp-submit" class="button-primary" value="Log In" tabindex="4" />
		</p>
	</form>

	<p id="nav">
		<!--a href="http://wordpress.com/signup/?ref=wplogin">Register</a> |-->
		<a href="#" title="Password Lost and Found">Lost your password?</a>
	</p>

	<script type="text/javascript">
		function wp_attempt_focus(){setTimeout( function(){ 
			try{d = document.getElementById('user_login');
				d.focus();
				d.select();
			} catch(e){}
		}, 200);}
		wp_attempt_focus();
		if(typeof wpOnload=='function')wpOnload();
	</script>

</div>
<div class="clear"/>
</body>
</html>