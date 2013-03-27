package net.cloudengine.model.resource

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Version;
import javax.validation.constraints.NotNull

import org.apache.bval.constraints.NotEmpty

@Entity
class Resource {
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id Long id;
	
	@NotNull
	@NotEmpty
	String name;
	
	String imei;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@NotNull
	ResourceType type;
	
	Date lastModified;
	double lon;
	double lat;
	
	long version;

	/**
	 * Cambia la posión actual del recurso.
	 * Retorna un WayPoint que permite generar
	 * el recorrido historica del recurso.
	 * @param lon
	 * @param lat
	 * @param speed
	 * @return
	 */
	WayPoint trackPoint(double lon, double lat, double speed) {
		this.lastModified = new Date();
		this.lon = lon;
		this.lat = lat;
		
		WayPoint wp = new WayPoint();
		wp.setDate(this.lastModified);
		wp.setResource(getId());
		wp.setSpeed(speed);
		wp.setLat(lat);
		wp.setLon(lon);
		return wp;
	}

}
