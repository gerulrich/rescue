package net.cloudengine.model.geo

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Id;

@Entity(value="tile", noClassnameStored=true)
class Tile {

	@Id
	ObjectId id;
	int x;
	int y;
	int z;
	byte[] image;
	String key;
	transient boolean cacheable = false;

	public Tile(int x, int y, int z, String key, byte[] image) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.key = key;
		this.image = image;
	}
	
	protected Tile() {
		
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
}
