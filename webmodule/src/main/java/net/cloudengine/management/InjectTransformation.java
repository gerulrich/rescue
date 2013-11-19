package net.cloudengine.management;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.objectweb.asm.Opcodes;

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class InjectTransformation implements ASTTransformation {

	@Override
	public void visit(ASTNode[] nodes, SourceUnit source) {
		if (nodes == null || nodes.length == 0) { 
			return;
		}
		if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof FieldNode)) {
			return;
		}
		FieldNode fileNode = (FieldNode)nodes[1];
		fileNode.setModifiers(Opcodes.ACC_PRIVATE | (fileNode.getModifiers() & (~(Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED))));
		fileNode.setInitialValueExpression(createInjectSts(fileNode.getName(), fileNode.getType()));
	}
	
	
    private Expression createInjectSts(String field, ClassNode type) {
    	return new MethodCallExpression(
    			new ClassExpression(ClassHelper.make(Injector.class)) , 
    			new ConstantExpression("getDependency") , 
    			new ClassExpression(type)
    		);    	
    }
}
