package net.cloudengine.domain.dsl.workflow

class Transition {
	String name
	String from
	String to
	String description
	List<String> allowTo
}
