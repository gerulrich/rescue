package net.cloudengine.dao.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.objectweb.asm.Opcodes;

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class QuerySupportTransformation implements ASTTransformation {

	
	@Override
	public void visit(ASTNode[] nodes, SourceUnit source) {
		if (nodes == null || nodes.length == 0) { 
			return;
		}
				
		if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
			return;
		}
				
		AnnotatedNode parent = (AnnotatedNode) nodes[1];
		AnnotationNode node = (AnnotationNode) nodes[0];
		
		// TODO agregar field
		List<FieldNode> fieldsToAdd = new ArrayList<FieldNode>();
		if (node.getClassNode().getName().equals(QuerySupport.class.getName()) && parent instanceof ClassNode) {
			ClassNode classNode = (ClassNode) parent;
			List<FieldNode> fields = classNode.getFields();
			fieldsToAdd.addAll(fields);
		}
		
		
		for(FieldNode fieldNode : fieldsToAdd) {
			ClassNode classNode = (ClassNode) parent;
			
			int modifiers = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL; 
			classNode.addFieldFirst("FIELD_"+fieldNode.getName().toUpperCase(), modifiers, new ClassNode(String.class), new ConstantExpression(fieldNode.getName()));
		}

		
	}

}
