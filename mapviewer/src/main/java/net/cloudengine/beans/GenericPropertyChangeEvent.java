package net.cloudengine.beans;

import java.beans.PropertyChangeEvent;

/**
 * 
 * @author German Ulrich
 *
 * @param <V>
 */
public class GenericPropertyChangeEvent<V> extends PropertyChangeEvent {


	private static final long serialVersionUID = -6882212346566830306L;

	public GenericPropertyChangeEvent(
		  final Object source, final String propertyName, 
		  final V oldValue, V newValue) {
		super(source, propertyName, oldValue, newValue);
	}

	@SuppressWarnings("unchecked")
	public GenericPropertyChangeEvent(final PropertyChangeEvent propertyChangeEvent) {
		this(propertyChangeEvent.getSource(),
				propertyChangeEvent.getPropertyName(),
				(V)propertyChangeEvent.getOldValue(),
				(V)propertyChangeEvent.getNewValue());
		setPropagationId(propertyChangeEvent.getPropagationId());
	}

	@SuppressWarnings("unchecked")
	public V getOldValue() {
		return (V)super.getOldValue();
	}

	@SuppressWarnings("unchecked")
	public V getNewValue() {
		return (V)super.getNewValue();
	}
}