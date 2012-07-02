package net.cloudengine.model.map


import javax.persistence.Entity;
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry

@Entity
@SequenceGenerator(name="seq_street", initialValue=1, allocationSize=1)

@NamedNativeQuery(name = 'findStreetByNameAndNumber', 
	query =  """
    SELECT id, name, type, fromLeft, toLeft, fromRight, toRight, geom    
	FROM street  
	WHERE to_tsvector(name) @@ to_tsquery(?) AND  
	( ( ? between fromLeft and  toLeft ) OR ( ? between fromRight and  toRight ) )  
	"""
	, resultClass = Street.class
)

class Street {
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_street")
	@Id Long id;
	String name;
	String type;
	
	int fromLeft;
	int toLeft;
	
	int fromRight;
	int toRight;
	
	
	@Type(type='org.hibernatespatial.GeometryUserType')
//	org.hibernatespatial.GeometryUserType
	Geometry geom;
}
