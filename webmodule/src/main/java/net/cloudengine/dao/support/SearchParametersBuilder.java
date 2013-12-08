package net.cloudengine.dao.support;

import com.google.common.base.Preconditions;

public class SearchParametersBuilder {
	
	private static final String INVALID_FIELD_OR_VALUE = "Invalid field or value for class %s. Field: %s, Value: %s.";
	private Class<?> entityClass;
	private SearchParameters result = new SearchParameters();
	
	private SearchParametersBuilder(Class<?> entityClass) {
		this.entityClass = Preconditions.checkNotNull(entityClass);
	}
	
	public static SearchParametersBuilder forClass(Class<?> entityClass) {
		return new SearchParametersBuilder(entityClass);
	}
	
	public SearchParameters build() {
		return result;
	}
	
	public SearchParametersBuilder eq(String field, Object value) {
		addCondition(field, Operator.EQ, value);
		return this;
	}
	
	public SearchParametersBuilder gt(String field, Object value) {
		addCondition(field, Operator.GT, value);
		return this;
	}
	
	public SearchParametersBuilder ge(String field, Object value) {
		addCondition(field, Operator.GE, value);
		return this;
	}
	
	public SearchParametersBuilder lt(String field, Object value) {
		addCondition(field, Operator.LT, value);
		return this;
	}
	
	public SearchParametersBuilder le(String field, Object value) {
		addCondition(field, Operator.LE, value);
		return this;
	}
	
	public void andMode() {
		this.result.setAndMode(true);
	}

	public void orMode() {
		this.result.setAndMode(false);
	}

	private void addCondition(String field, Operator operator, Object value) {
		checkEntityClass();
		if (checkField(entityClass, field, value)) {
			this.result.addCondition(field, operator, value);
		} else {
			String msg = String.format(INVALID_FIELD_OR_VALUE, this.entityClass.getName(), field, value);
			throw new IllegalArgumentException(msg);
		}
	}
	
	private void checkEntityClass() {
		if (this.entityClass == null) {
			throw new IllegalStateException("Please, invoque forClass method after others methods");
		}
	}
	
	private boolean checkField(Class<?> clazz, String name, Object value) {
		return true;
	}	

}
