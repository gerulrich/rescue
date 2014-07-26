package net.cloudengine.model.commons

interface Identifiable<PK extends Serializable> {
	
	PK getPK();	

}
