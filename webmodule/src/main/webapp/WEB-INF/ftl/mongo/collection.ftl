<@layout.main template="admin">

<!-- CONTENT BOX - DATATABLE -->
<div class="content-box">
	<div class="box-body">
		<div class="box-header clear">
			<ul class="tabs clear">
				<!--<li><a href="#data-table">JS plugin</a></li>-->
				<!--li><a href="#table">Table only</a></li-->
			</ul>
			<h2>MongoDB ${collection} Collection</h2>
		</div>

		<div class="box-wrap clear">
			<div id="table">
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem.</p> 
				<table class="style1">
					<thead>
						<tr>
							<#list headers as h>
							<th>${h}</th>
							</#list>
						</tr>
					</thead>
					<tbody>
						<#list objects as obj>
						<tr>
							<#list obj.values?keys as key>
							<td>${obj.values[key]}</td>
							</#list>
						</tr>								
						</#list>
					</tbody>
				</table>
			
		</div><!-- end of box-wrap -->
	</div> <!-- end of box-body -->
</div> <!-- end of content-box -->			
			    
</div><!-- end of page -->
</ul>
</@layout.main>