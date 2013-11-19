package net.cloudengine.model.ticket

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class PhoneCall {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;

}
