package net.cloudengine.domain.dsl.workflow

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="workflow")
class PersistentWorkflow {

	@Id ObjectId id;
	double version;
	boolean active;
	String definition;
}
