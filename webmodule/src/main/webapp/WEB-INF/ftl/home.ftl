<@layout.main template="dashboard">

	<!-- ICONBAR -->
	<div class="content-box clear">
		<div class="box-body iconbar">
			<div class="box-wrap">
				<div class="main-icons" id="iconbar">
					<ul class="clear">
                        <!--<li><a href="#"><img src="images/ico_folder_64.png" class="icon" alt="" /><span class="text">Create an Article</span></a></li>
                        <li><a href="#"><img src="images/ico_page_64.png" class="icon" alt="" /><span class="text">Add Page</span></a></li>
           		        <li><a href="#"><img src="images/ico_picture_64.png" class="icon" alt="" /><span class="text">Add Picture</span></a></li>
                        <li><a href="#"><img src="images/ico_clock_64.png" class="icon" alt="" /><span class="text">Add Event</span></a></li>-->
				        <li><a href="<@spring.url '/admin/users'/>"><img src="<@spring.url '/static/images/icon_users_64.png'/>" class="icon" alt="" /><span class="text">Usuarios</span></a></li>
		                <li><a href="<@spring.url '/admin/mongo/list'/>"><img src="<@spring.url '/static/images/icon_db_64.png'/>" class="icon" alt="" /><span class="text">MongoDB</span></a></li>
		                <li><a href="<@spring.url '/admin/properties'/>"><img src="<@spring.url '/static/images/ico_settings_64.png'/>" class="icon" alt="" /><span class="text">Configuración</span></a></li>
		                <!--<li><a href="#modal" class="modal-link"><img src="images/ico_chat_64.png" class="icon" alt="" /><span class="text">Open Modal</span></a></li>
           		        <li><a href="#"><img src="images/ico_folder_64.png" class="icon" alt="" /><span class="text">Create an Article</span></a></li>
                   		<li class="active"><a href="#"><img src="images/ico_page_64.png" class="icon" alt="" /><span class="text">Active icon</span></a></li>
		                <li><a href="#"><img src="images/ico_picture_64.png" class="icon" alt="" /><span class="text">Add Picture</span></a></li>
				        <li><a href="#"><img src="images/ico_clock_64.png" class="icon" alt="" /><span class="text">Add Event</span></a></li-->
					</ul>
				</div>
			</div>
		</div>
	</div>



<!--h1>Manage Articles <a href="#modal-label" class="label modal-link">HELP</a></h1>
	<p>
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus rhoncus lorem vitae sem ultrices commodo. 
		Phasellus aliquam nunc sit amet mi euismod non vulputate erat iaculis. Sed convallis condimentum tortor vitae lacinia. 
		Cras non neque quis orci placerat mattis at in orci. Maecenas id mollis lacus. Proin non orci ut nisi congue egestas vel vitae enim. 
		Pellentesque massa felis, rhoncus a suscipit eu, posuere et odio. Morbi viverra dolor ac est ultrices fermentum.
		Nullam euismod tempor porta. Fusce eget arcu neque. Quisque vitae urna urna. Sed viverra hendrerit convallis. 
		Mauris viverra interdum dolor, quis laoreet nisl tincidunt in. Pellentesque fermentum hendrerit risus, at porta purus bibendum et. 
		Ut aliquam nibh sed nunc vestibulum vehicula. Sed fringilla dui eu lectus varius mattis. Nulla ut justo nulla, nec luctus neque. 
		Donec ullamcorper tellus nec arcu tincidunt at feugiat erat dignissim. Mauris in mattis lacus. 
		Suspendisse nisi magna, auctor a imperdiet in, egestas vulputate velit. Suspendisse ac metus purus. 
		Pellentesque sodales purus id sapien pretium congue. Mauris mattis blandit elit, pretium porttitor erat tempus id. 
		Donec in elit a velit aliquam interdum at sed tellus. Integer commodo commodo augue at venenatis. Sed varius laoreet odio ut rutrum. 
		Donec viverra, nunc eget vestibulum faucibus, lectus sem euismod nibh, quis tincidunt purus justo ac felis. 
		Vivamus adipiscing mauris nec neque porta varius. Nunc convallis, neque nec aliquet fermentum, eros augue dignissim nulla, a varius turpis turpis nec metus. 
		Aliquam erat volutpat. In scelerisque porttitor sem eget vestibulum.
	</p>    
	
	< !-- MODAL WINDOW -- >
	<div id="modal-label" class="modal-window modal-600">
		<div class="notification note-attention">
			<a href="#" class="close" title="Close notification"><span>close</span></a>
			<span class="icon"></span>
			<p><strong>Attention:</strong> More about settings of modal windows is described in Dashboard - Open Modal icon.</p>
		</div>
				
		<h2>Modal Window : size 600</h2>
		<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tristique, lorem id hendrerit sodales, nisl felis sollicitudin lacus, et facilisis felis quam at quam. Nullam vel nunc at sapien sagittis feugiat. Vestibulum est eros, condimentum ac sodales vel, iaculis vitae neque.</p>
		<p>Nam nisl odio, scelerisque non venenatis quis, venenatis a leo. Cras non vehicula justo. Nam vel arcu sem. Suspendisse quam enim, dictum quis lacinia sed, lobortis eget libero. Suspendisse potenti. Suspendisse et ante vitae turpis vestibulum fermentum nec nec elit. Suspendisse ullamcorper lacus in arcu mollis fringilla porta mi placerat. Ut at elit non diam tristique scelerisque. </p>
	</div>
</div-->


<div class="columns clear bt-space20">
	<!-- DASHBOARD - LEFT COLUMN -->
	<div class="col2-3">
		<h1>Dashboard Example <a href="#modal-label" class="label modal-link">INFO</a></h1>
		<p>Nam posuere, felis sed feugiat viverra, quam felis dapibus eros, vitae pulvinar nisl quam ut eros. Curabitur eget fringilla mi. Vivamus sed justo sit amet elit malesuada bibendum. Pellentesque consectetur blandit nisl, a eleifend arcu adipiscing eu. In et neque nec urna mollis fermentum gravida at turpis. </p>
		<!-- MODAL WINDOW -->
		<div id="modal-label" class="modal-window modal-400">
			<div class="notification note-attention">
				<a href="#" class="close" title="Close notification"><span>close</span></a>
				<span class="icon"></span>
				<p><strong>Attention:</strong> More about settings of modal windows is described in Dashboard - Open Modal icon.</p>
			</div>
			<h2>Modal Window : size 400</h2>
			<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tristique, lorem id hendrerit sodales, nisl felis sollicitudin lacus, et facilisis felis quam at quam. Nullam vel nunc at sapien sagittis feugiat. Vestibulum est eros, condimentum ac sodales vel, iaculis vitae neque.</p>
			<p>Nam nisl odio, scelerisque non venenatis quis, venenatis a leo. Cras non vehicula justo. Nam vel arcu sem. Suspendisse quam enim, dictum quis lacinia sed, lobortis eget libero. Suspendisse potenti. Suspendisse et ante vitae turpis vestibulum fermentum nec nec elit. Suspendisse ullamcorper lacus in arcu mollis fringilla porta mi placerat. Ut at elit non diam tristique scelerisque. </p>
		</div>


		<div class="columns clear">
			<div class="col1-2">
				<!-- OVERVIEW - BASIC TABLE -->
				<h2>Configuración</h2>
				<table class="basic" cellspacing="0">
					<tbody>
						<#list options as option>
						<tr>
							<td><img src="<@spring.url '/static/images/ball_green_16.png'/>" class="block" alt="" /></td>
							<th class="full">${option.name}</th>
							<td class="value right">${option.value}</td>
							<!--td><a href="#">more</a></td-->
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>	
	</div>

</div>

</@layout.main>