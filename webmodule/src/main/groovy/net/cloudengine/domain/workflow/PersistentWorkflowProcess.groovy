package net.cloudengine.domain.workflow

import org.bson.types.ObjectId;
import org.simple.workflow.impl.WorkflowProcessImpl
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="workflow_process")
class PersistentWorkflowProcess extends WorkflowProcessImpl {
	
	@Id ObjectId id;

	public PersistentWorkflowProcess(String key, String version) {
		super(key, version);
	}
	
	protected PersistentWorkflowProcess() {
		super();
	}	

}
