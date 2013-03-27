package net.cloudengine.beans;

import java.beans.PropertyChangeListener;

/**
 * 
 * @author German Ulrich
 *
 * @param <V>
 */
public interface GenericPropertyChangeListener<V> extends PropertyChangeListener {
  
	public void propertyChange(final GenericPropertyChangeEvent<V> genericPropertyChangeEvent);
}