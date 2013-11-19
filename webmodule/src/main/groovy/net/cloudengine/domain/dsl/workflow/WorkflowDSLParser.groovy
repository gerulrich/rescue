package net.cloudengine.domain.dsl.workflow

import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;

import java.io.File;

import net.cloudengine.reports.ParameterMetadata;
import net.cloudengine.reports.ReportMetadata;

class WorkflowDSLParser {
	
	WorkflowMetadata parseDSL(File dsl) {
		parseDSL(dsl.text, dsl.getName());
	}
	
	
	WorkflowMetadata parseDSL(String text, String name) {
		
		WorkflowMetadata workflowMetada = new WorkflowMetadata()
		Script dslScript = new GroovyShell().parse(text, name)
		
		String.metaClass.or = {String value ->
			return delegate+','+value;
		}
		dslScript.metaClass = createEMC(dslScript.class, {
			ExpandoMetaClass emc ->
			
			emc.workflow = {
				Closure cl ->
				
				cl.delegate = new WorkflowDelegate(workflowMetada)
				cl.resolveStrategy = Closure.DELEGATE_FIRST
				
				cl()
			}
		})
		dslScript.run()
		workflowMetada.validate();
		return workflowMetada;
	}
	
	static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
		ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
		cl(emc)
		emc.initialize()
		return emc
	}
}

class WorkflowDelegate {
	
	private WorkflowMetadata workflowMetadata;
	
	public WorkflowDelegate(WorkflowMetadata workflowMetadata) {
		this.workflowMetadata = workflowMetadata;
	}

	void version(BigDecimal version) {
		workflowMetadata.setVersion(version.doubleValue());
	}
	
	void start(int start) {
		
	}
	
	void states(Closure cl) {
		cl.delegate = new StatesDelegate(workflowMetadata)
		cl.resolveStrategy = Closure.DELEGATE_FIRST
		cl()
	}
	
	void transitions(Closure cl) {
		cl.delegate = new TransitionsDelegate(workflowMetadata)
		cl.resolveStrategy = Closure.DELEGATE_FIRST
		cl()
	}
}

class StatesDelegate {
	
	private WorkflowMetadata workflowMetadata;
	
	public StatesDelegate(WorkflowMetadata workflowMetadata) {
		super();
		this.workflowMetadata = workflowMetadata;
	}

	def methodMissing(String name, Object args) {
		if (args.length == 1) {
			if (args[0] instanceof Closure) {
				State state = new State();
				state.setName(name);
				args[0].delegate = new StateDelegate(state)
				args[0].resolveStrategy = Closure.DELEGATE_FIRST
				
				args[0]()
				
				this.workflowMetadata.addState(state);

			} else {
				throw new MissingMethodException(name, this.class, args as Object[])
			}
		} else {
			throw new MissingMethodException(name, this.class, args as Object[])
		}
	}
}

class StateDelegate {
	
	private State state;
	
	StateDelegate(State state) {
		this.state = state
	}
	
	void description(String description) {
		state.setDescription(description)
	}
	
	void methodMissing(String name, Object args) {
		throw new MissingMethodException(name, this.class, args as Object[])
	}
}

class TransitionsDelegate {
	
	private WorkflowMetadata workflowMetadata;
	
	public TransitionsDelegate(WorkflowMetadata workflowMetadata) {
		super();
		this.workflowMetadata = workflowMetadata;
	}

	def methodMissing(String name, Object args) {
		if (args.length == 1) {
			if (args[0] instanceof Closure) {
				Transition transition = new Transition()
				transition.setName(name)
				args[0].delegate = new TransitionDelegate(transition)
				args[0].resolveStrategy = Closure.DELEGATE_FIRST
				
				args[0]()
				
				this.workflowMetadata.addTransition(transition);

			} else {
				throw new MissingMethodException(name, this.class, args as Object[])
			}
		} else {
			throw new MissingMethodException(name, this.class, args as Object[])
		}
	}
}

class TransitionDelegate {
	
	private Transition transition;
	
	TransitionDelegate(Transition transition) {
		this.transition = transition
	}
	
	void description(String description) {
		transition.setDescription(description)
	}
	
	void from(String from) {
		transition.setFrom(from);
	}
	
	void to(String to) {
		transition.setTo(to);
	}
	
	def allowTo(String roles) {
		transition.setAllowTo(roles.split(",") as List)
	}
	
	void methodMissing(String name, Object args) {
		throw new MissingMethodException(name, this.class, args as Object[])
	}
}