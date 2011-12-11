<div class="pagetop">
	<div class="head pagesize"><!-- *** head layout *** -->
		<div class="head_top">
			<div class="topbuts">
				<ul class="clear">
					<li><a href="<@spring.url '/j_spring_security_logout' />" class="red"><@spring.message "home.logout"/></a></li>
				</ul>
				<div class="user clear"><@widget.userinfo /></div>
			</div>
			<div class="logo clear"><@widget.logo /></div>
		</div>
		<div class="menu"><@widget.menu selected=template/></div>
	</div>
</div>