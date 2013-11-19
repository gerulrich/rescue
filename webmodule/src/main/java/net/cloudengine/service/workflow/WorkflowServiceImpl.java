package net.cloudengine.service.workflow;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import net.cloudengine.api.BlobStore;
import net.cloudengine.api.Datastore;
import net.cloudengine.domain.dsl.workflow.Engine;
import net.cloudengine.domain.dsl.workflow.PersistentWorkflow;
import net.cloudengine.domain.dsl.workflow.State;
import net.cloudengine.domain.dsl.workflow.WorkflowDSLParser;
import net.cloudengine.domain.dsl.workflow.WorkflowMetadata;
import net.cloudengine.model.commons.FileDescriptor;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WorkflowServiceImpl implements WorkflowService, Engine {
	
	private Datastore<PersistentWorkflow, ObjectId> workflowDao;
	private Datastore<FileDescriptor, ObjectId> fileDao;
	private BlobStore blobStore;
	private WorkflowMetadata currentWorkflow = null;
	
	@Autowired
	public WorkflowServiceImpl(
			@Qualifier("wfStore") Datastore<PersistentWorkflow, ObjectId> workflowDao, 
			@Qualifier("fileStore") Datastore<FileDescriptor, ObjectId> fileDao, BlobStore blobStore) {
		super();
		this.workflowDao = workflowDao;
		this.fileDao = fileDao;
		this.blobStore = blobStore;
	}
	
	@Override
	public Collection<FileDescriptor> getWorkflowFiles() {
		return fileDao.createQuery().field("type").eq("wf").list();
	}
	
	@Override
	public PersistentWorkflow createFromFile(ObjectId fileObjectId) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		blobStore.retrieveFile(fileObjectId, stream);
		String workflowScript = new String(stream.toByteArray());
		WorkflowDSLParser parser = new WorkflowDSLParser();
		WorkflowMetadata metadata = parser.parseDSL(workflowScript, "workflow.groovy");
		PersistentWorkflow workflow = new PersistentWorkflow();
		workflow.setVersion(metadata.getVersion());
		workflow.setDefinition(workflowScript);
		workflow.setActive(false);
		workflowDao.save(workflow);
		return workflow;
	}
	
	@Override
	public boolean activateWorkflow(ObjectId workflowId) {
		PersistentWorkflow activeWorkflow = workflowDao.createQuery().field("active").eq(Boolean.TRUE).get();
		if (activeWorkflow != null) {
			activeWorkflow.setActive(Boolean.FALSE);
			workflowDao.update(activeWorkflow);
		}
		
		PersistentWorkflow workflow = workflowDao.get(workflowId);
		workflow.setActive(true);
		workflowDao.update(workflow);
		return false;
	}

	@Override
	public WorkflowMetadata getActiveWorkflow() {
		if (this.currentWorkflow != null) {
			return this.currentWorkflow;
		}
		PersistentWorkflow activeWorkflow = workflowDao.createQuery().field("active").eq(Boolean.TRUE).get();
		if (activeWorkflow != null) {
			WorkflowDSLParser parser = new WorkflowDSLParser();
			this.currentWorkflow = parser.parseDSL(activeWorkflow.getDefinition(), "workflow.groovy");
			return this.currentWorkflow; 
		}
		return new WorkflowMetadata();
	}

	@Override
	public WorkflowMetadata getWorkflow(double version) {
		PersistentWorkflow activeWorkflow = workflowDao.createQuery().field("version").eq(version).get();
		if (activeWorkflow != null) {
			WorkflowDSLParser parser = new WorkflowDSLParser();
			this.currentWorkflow = parser.parseDSL(activeWorkflow.getDefinition(), "workflow.groovy");
		}
		return new WorkflowMetadata();
	}

	@Override
	public State firstState() {
		WorkflowMetadata metadata = this.getActiveWorkflow();
		return metadata.getStates().get(0);
	}

	@Override
	public double getVersion() {
		WorkflowMetadata metadata = this.getActiveWorkflow();
		return metadata.getVersion();
	}
	
	
	
}
