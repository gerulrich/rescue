package net.cloudengine.dao.mongodb;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import net.cloudengine.dao.support.Condition;
import net.cloudengine.dao.support.SearchParameters;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoUtils {

	public static Query createQuery(SearchParameters parameters) {
		Query query = new Query();
		Criteria criteria = null;
		for(Condition condition : parameters.getConditions()) {
			Criteria currentCriteria = null;
			switch(condition.getOperator()) {
				case EQ: currentCriteria = eq(condition); break;
				case GT: currentCriteria = gt(condition); break;
				case GE: currentCriteria = ge(condition); break;
				case LT: currentCriteria = lt(condition); break;
				case LE: currentCriteria = le(condition); break;
				case LIKE: currentCriteria = like(condition); break;
			}
			if (criteria == null) {
				criteria = currentCriteria;
			} else if (!parameters.isAndMode()) {
				criteria.orOperator(currentCriteria);
			} else {
				criteria.andOperator(currentCriteria);
			}
		}
		query.addCriteria(criteria);
		return query;
	}
	
	private static Criteria eq(Condition condition) {
		return where(condition.getField()).is(condition.getValue());
	}
	
	private static Criteria gt(Condition condition) {
		return where(condition.getField()).gt(condition.getValue());
	}
	
	private static Criteria ge(Condition condition) {
		return where(condition.getField()).gte(condition.getValue());
	}
	
	private static Criteria lt(Condition condition) {
		return where(condition.getField()).lt(condition.getValue());
	}
	
	private static Criteria le(Condition condition) {
		return where(condition.getField()).lte(condition.getValue());
	}
	
	private static Criteria like(Condition condition) {
			return where(condition.getField()).regex("/"+condition.getValue()+"/");
	}
}

