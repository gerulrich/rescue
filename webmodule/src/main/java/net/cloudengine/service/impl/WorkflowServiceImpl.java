package net.cloudengine.service.impl;

import static net.cloudengine.dao.support.SearchParametersBuilder.forClass;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.dao.support.SearchParametersBuilder;
import net.cloudengine.domain.workflow.PersistentWorkflow;
import net.cloudengine.domain.workflow.PersistentWorkflowProcess;
import net.cloudengine.service.WorkflowService;

import org.bson.types.ObjectId;
import org.simple.workflow.entity.Workflow;
import org.simple.workflow.entity.WorkflowProcess;
import org.simple.workflow.services.impl.AbstractWorkflowDatasource;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkflowServiceImpl extends AbstractWorkflowDatasource implements WorkflowService {
	
	private Repository<PersistentWorkflow, ObjectId> workflowRepository;
	private Repository<PersistentWorkflowProcess, ObjectId> workflowProcessRepository;
	
	@Autowired
	public WorkflowServiceImpl(RepositoryLocator repositoryLocator) {
		super();
		this.workflowRepository = repositoryLocator.getRepository(PersistentWorkflow.class);
		this.workflowProcessRepository = repositoryLocator.getRepository(PersistentWorkflowProcess.class);
	}
	
	@Override
	public boolean activateWorkflow(ObjectId workflowId) {
		SearchParametersBuilder builder = forClass(PersistentWorkflow.class);
		builder.eq("active", Boolean.TRUE);
		PersistentWorkflow activeWorkflow = workflowRepository.findOne(builder.build());
		if (activeWorkflow != null) {
			activeWorkflow.setActive(Boolean.FALSE);
			workflowRepository.update(activeWorkflow);
		}

		PersistentWorkflow workflow = workflowRepository.get(workflowId);
		workflow.setActive(true);
		workflowRepository.update(workflow);

		
		return false;
	}

	@Override
	public WorkflowProcess getByKey(String key) {
		SearchParametersBuilder builder = forClass(PersistentWorkflow.class);
		builder.eq("key", key);
		return workflowProcessRepository.findOne(builder.build());
	}

	@Override
	public WorkflowProcess newInstance(String key, String version) {
		return new PersistentWorkflowProcess(key, version);
	}

	@Override
	public void save(WorkflowProcess process) {
		workflowProcessRepository.save(process);
	}

	@Override
	protected Workflow getDefaultWorkflow() {
		SearchParametersBuilder builder = forClass(PersistentWorkflow.class);
		builder.eq("active", Boolean.TRUE);
		PersistentWorkflow workflow = workflowRepository.findOne(builder.build());
		workflow.prepareWorkflow();
		return workflow;
	}

	@Override
	protected Workflow getFromRepository(String version) {
		PersistentWorkflow workflow = workflowRepository.get(new ObjectId(version));
		workflow.prepareWorkflow();
		return workflow;
	}
}