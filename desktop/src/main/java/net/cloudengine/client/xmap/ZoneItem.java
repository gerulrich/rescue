package net.cloudengine.client.xmap;

import com.vividsolutions.jts.geom.Geometry;

import net.cloudengine.mapviewer.layers.Item;
import net.cloudengine.rpc.controller.geo.ZoneModel;

public class ZoneItem implements Item {

	private ZoneModel zone;
	private Geometry geom;
	private boolean selected;
	
	
	public ZoneItem(ZoneModel zone, Geometry geom) {
		super();
		this.zone = zone;
		this.geom = geom;
	}

	@Override
	public Long getId() {
		return zone.getId();
	}

	@Override
	public String getName() {
		return zone.getName();
	}

	@Override
	public double getLon() {
		return geom.getCentroid().getX();
	}

	@Override
	public double getLat() {
		return geom.getCentroid().getY();
	}

	@Override
	public boolean allowSelection() {
		return true;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void selectItem() {
		selected = true;
	}

	@Override
	public void unselectItem() {
		selected = false;
	}

	public Geometry getGeom() {
		return geom;
	}
}
