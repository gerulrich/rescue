package net.cloudengine.service;

import org.bson.types.ObjectId;

public interface WorkflowService {
	
	
	boolean activateWorkflow(ObjectId workflowId);
	
//	WorkflowMetadata getActiveWorkflow();
	
	
//	WorkflowMetadata getWorkflow(double version);

}
