<@layout.main template="admin">			

<!-- CONTENT BOX - DATATABLE -->
<div class="content-box">
	<div class="box-body">
		<div class="box-header clear">
			<ul class="tabs clear">
				<!--<li><a href="#data-table">JS plugin</a></li>-->
				<!--li><a href="#table">Table only</a></li-->
			</ul>
			
			<h2>Usuarios</h2>
		</div>

		<div class="box-wrap clear">
			<div id="table">
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem.</p> 
				<table class="style1">
					<thead>
						<tr>
							<th>OID</th>
							<th>Nombre</th>
							<th>E-mail</th>
							<th>Habilitado</th>
							<th>Acciones</th>
						</tr>
					</thead>
					<tbody>
						<#list users.list as user>
						<tr>
							<td>${user.id}</td>
							<td>${user.displayName}</td>
							<td>${user.username}</td>
							<td><#if user.account.enabled>Yes<#else>No</#if></td>
							<td>
								<@widget.detail "/admin/user/show/${user.id}"/>
								<@widget.edit "/admin/user/${user.id}"/>
								<@widget.delete "/admin/user/${user.id}"/>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
			<@widget.tablePaging users "/admin/users" />
		</div><!-- end of box-wrap -->
	</div> <!-- end of box-body -->
</div> <!-- end of content-box -->			
			    
</div><!-- end of page -->
</@layout.main>