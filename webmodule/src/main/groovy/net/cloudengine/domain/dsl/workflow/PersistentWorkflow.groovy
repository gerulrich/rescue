package net.cloudengine.domain.dsl.workflow

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("workflow")
class PersistentWorkflow {

	@Id ObjectId id;
	double version;
	boolean active;
	String definition;
}
