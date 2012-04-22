<@layout.main template="admin">

	<h2>Propiedad ${property.key}</h2>

	<form action="<@spring.url '/admin/property/${property.id}'/>" method="post">
		<fieldset id="property">
			<legend>Datos de la propiedad</legend>
        	<table>
        		<tr>
            		<td><label path="value">Valor</label></td>
               		<td><@spring.formInput  "property.value" /></td>
				</tr>
			</table>
		</fieldset>
		
		<input type="submit" value="Guardar"/>		
    </form>	
	
</@layout.main>