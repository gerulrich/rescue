package net.cloudengine.dao.support;

public class Condition {
	
	private String field;
	private Operator operator;
	private Object value;
	
	public Condition(String field, Operator operator, Object value) {
		super();
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public Operator getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}
	
}
