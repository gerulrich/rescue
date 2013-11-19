package net.cloudengine.service.workflow;

import java.util.Collection;

import org.bson.types.ObjectId;

import net.cloudengine.domain.dsl.workflow.PersistentWorkflow;
import net.cloudengine.domain.dsl.workflow.WorkflowMetadata;
import net.cloudengine.model.commons.FileDescriptor;

public interface WorkflowService {
	
	
	Collection<FileDescriptor> getWorkflowFiles();
	
	PersistentWorkflow createFromFile(ObjectId fileObjectId);
	
	boolean activateWorkflow(ObjectId workflowId);
	
	WorkflowMetadata getActiveWorkflow();
	
	
	WorkflowMetadata getWorkflow(double version);

}
