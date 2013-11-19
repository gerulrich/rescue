package net.cloudengine.domain.dsl.workflow

interface Engine {
	
	State firstState();
	double getVersion();

}
