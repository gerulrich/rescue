package net.cloudengine.domain.workflow


import java.util.List;
import java.util.Map;

import net.cloudengine.web.crud.support.CrudProperty;

import org.bson.types.ObjectId;
import org.simple.workflow.entity.Node;
import org.simple.workflow.entity.Transition;
import org.simple.workflow.impl.WorkflowImpl;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="workflow")
class PersistentWorkflow extends WorkflowImpl {
	
	@Id ObjectId id;
	@CrudProperty boolean active;
	@CrudProperty String name;

	@Override
	@Transient
	protected Map<String, Node> getIndexedNodes() {
		return super.getIndexedNodes();
	}

	@Override
	@Transient
	protected Map<String, Map<String, Transition>> getIndexedTransitions() {
		return super.getIndexedTransitions();
	}

	@Override
	@Transient
	public String getVersion() {
		return this.getId().toString();
	}

	@Override
	@Transient
	public Node getNode() {
		return super.getNode();
	}

	@Override
	public List<Node> getNodes() {
		return super.getNodes();
	}

	@Override
	public List<Transition> getTransitions() {
		return super.getTransitions();
	}
	
	public void prepareWorkflow() {
		this.index();
	}
	
	
}