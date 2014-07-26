<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/workflow/list'/>">Workflow</a></li>
	<li><a>Workflow ${entity.name}</a></li>
</#macro>

<#macro script>
	<script type="text/javascript" src="<@spring.url '/static/js/graphs/raphael.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/graphs/dracula_graffle.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/graphs/dracula_graph.js'/>"></script>
    
	<script>
	
		$(document).ready(function() {
			
			Raphael.fn.line = function(startX, startY, endX, endY) {
    			return this.path('M' + startX + ' ' + startY + ' L' + endX + ' ' + endY);
			};
			
			var renderFork = function(r, n) {
				/* the Raphael set is obligatory, containing all you want to display */
        		var set = r.set().push(
        			r.circle(n.point[0]-5, n.point[1]-5, 10, 10)
        			.attr({"fill": "#FF8C00"}),
        			r.line(n.point[0]-5, n.point[1]+5, n.point[0]-5, n.point[1]-5),
        			r.line(n.point[0]-15, n.point[1]-5, n.point[0]+5, n.point[1]-5)
        		)
        		return set;
    		};
    		
			var renderUnion = function(r, n) {
				/* the Raphael set is obligatory, containing all you want to display */
        		var set = r.set().push(
        			r.circle(n.point[0]-5, n.point[1]-5, 10, 10)
        			.attr({"fill": "#6495ED"}),
        			r.line(n.point[0]-15, n.point[1]-5, n.point[0]+5, n.point[1]-5),
        			r.line(n.point[0]-3, n.point[1]-1, n.point[0]-3, n.point[1]-8),
        			r.line(n.point[0]-8, n.point[1]-1, n.point[0]-8, n.point[1]-8)
        			
        		)
        		return set;
    		};    		
    		
    		
			var g = new Graph();
			
			<#list entity.nodes as node>
				<#if node.joinNode == true>
					g.addNode("${node.name}", {stroke : "#000" , fill : "#fff", label : "${node.description}" })
				<#else>
					g.addNode("${node.name}", {stroke : "#000" , fill : "#fff", label : "${node.description}" })
				</#if>
			</#list>
			
			<#list entity.transitions as transition>
				<#if transition.forkTransition == true>
					g.addNode("FORK_${transition.from}", {label : "", render : renderFork });
					g.addEdge("${transition.from}", "FORK_${transition.from}", { directed : true, label : ""});
				
					g.addEdge("FORK_${transition.from}", "${transition.to}", { directed : true, label : "${transition.description}"});
					g.addEdge("FORK_${transition.from}", "${transition.forkTo}", { directed : true, label : "${transition.description}"});
				
				<#else>
					g.addEdge("${transition.from}", "${transition.to}", { directed : true, label : "${transition.description}"});
				</#if>
				
			</#list>
			
 			var layouter = new Graph.Layout.Spring(g);
			layouter.layout();
 
			var renderer = new Graph.Renderer.Raphael('canvas', g, 800, 400);
			renderer.draw();
		});
		
	</script>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<@page.section>
			<@page.box title="Workflow ${entity.name}">
				<div id="canvas"></div>
			</@page.box>
	</@page.section>

</#macro>