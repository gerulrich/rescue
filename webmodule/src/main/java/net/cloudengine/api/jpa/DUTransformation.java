package net.cloudengine.api.jpa;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.hibernate.annotations.Entity;

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class DUTransformation implements ASTTransformation {

	@Override
	public void visit(ASTNode[] nodes, SourceUnit source) {
		if (nodes == null || nodes.length == 0){ 
			return;
		}
				
		if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
			return;
		}
				
		AnnotatedNode parent = (AnnotatedNode) nodes[1];
		AnnotationNode node = (AnnotationNode) nodes[0];
		
		if (node.getClassNode().getName().equals(DynamicUpdate.class.getName()) && parent instanceof ClassNode) {
			AnnotationNode a = new AnnotationNode(ClassHelper.makeWithoutCaching(Entity.class));
			a.setMember("dynamicUpdate", new ConstantExpression(Boolean.TRUE));
			ClassNode classNode = (ClassNode) parent;
			classNode.addAnnotation(a);
		}
	}
}
