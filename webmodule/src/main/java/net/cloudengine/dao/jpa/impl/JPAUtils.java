package net.cloudengine.dao.jpa.impl;

import net.cloudengine.dao.support.Condition;
import net.cloudengine.dao.support.SearchParameters;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;


public class JPAUtils {
	
	public static <E> Criteria createQuery(SearchParameters parameters, Session session, Class<E> clazz) {
		
		
		Criteria criteria = session.createCriteria(clazz);
		Junction junction = null;
		if (parameters.isAndMode()) {
			junction = Restrictions.conjunction();
		} else {
			junction = Restrictions.disjunction();
		}
		
		for(Condition condition : parameters.getConditions()) {
			
			switch(condition.getOperator()) {
				case EQ: eq(condition, junction); break;
				case GT: gt(condition, junction); break;
				case GE: ge(condition, junction); break;
				case LT: lt(condition, junction); break;
				case LE: le(condition, junction); break;
				case LIKE: like(condition, junction); break;
			}
		}
		criteria.add(junction);
		return criteria;
	}
	
	private static void eq(Condition condition, Junction junction) {
		junction.add(Restrictions.eq(condition.getField(), condition.getValue()));
		return ;
	}
	
	private static void gt(Condition condition, Junction junction) {
		junction.add(Restrictions.gt(condition.getField(), condition.getValue()));
		return ;
	}
	
	private static void ge(Condition condition, Junction junction) {
		junction.add(Restrictions.ge(condition.getField(), condition.getValue()));
		return ;
	}
	
	private static void lt(Condition condition, Junction junction) {
		junction.add(Restrictions.lt(condition.getField(), condition.getValue()));
		return ;
	}
	
	private static void le(Condition condition, Junction junction) {
		junction.add(Restrictions.le(condition.getField(), condition.getValue()));
		return ;
	}
	
	private static void like(Condition condition, Junction junction) {
		junction.add(Restrictions.like(condition.getField(), condition.getValue()));
		return ;
	}
}
