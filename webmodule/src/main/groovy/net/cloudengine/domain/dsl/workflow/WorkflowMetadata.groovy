package net.cloudengine.domain.dsl.workflow

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class WorkflowMetadata {
	
	double version
	List<State> states = new ArrayList<State>();
	List<Transition> transitions = new ArrayList<State>();
	private Map<String,State> statesByname =  new HashMap<String,State>();
	private Map<String,Transition> transitionsByname =  new HashMap<String,Transition>();
	private ConcurrentMap<String,List<Transition>> trasitionsByState = new ConcurrentHashMap<String,List<Transition>>();
	
	void addState(State state) {
		states.add(state);
		statesByname.put(state.getName(), state);
	}
	
	void addTransition(Transition transition) {
		transitions.add(transition);
		transitionsByname.put(transition.getName(), transition);
	}
	
	void validate() {
		transitionsByname.each() { key, value -> 
			 if (!statesByname.containsKey(value.getFrom()) || !statesByname.containsKey(value.getTo())) {
				 throw new RuntimeException("Las transiciones contienen estados invalidos"); 
			 }
			 if (!trasitionsByState.containsKey(value.getFrom())) {
				 trasitionsByState.put(value.getFrom(), new ArrayList<Transition>());
			 }
			 trasitionsByState.get(value.getFrom()).add(value);
		};
	}

}
