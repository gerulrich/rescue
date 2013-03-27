package net.cloudengine.mapviewer.pages.selection;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.cloudengine.beans.AbstractGenericPropertyChangeListener;
import net.cloudengine.beans.GenericPropertyChangeEvent;
import net.cloudengine.mapviewer.MapWidgetContext;
import net.cloudengine.mapviewer.layers.Item;
import net.cloudengine.mapviewer.tools.selection.Attribute;
import net.cloudengine.mapviewer.tools.selection.Cast;
import net.cloudengine.mapviewer.tools.selection.Group;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class SelectionContentProvider implements ITreeContentProvider {

	private MapWidgetContext context;
	private List<Group> groups;
	private TreeViewer tree;
	private PropertyChangeListener listener;
	
	public SelectionContentProvider(TreeViewer tree, MapWidgetContext context) {
		super();
		this.tree = tree;
		this.groups = context.getGroups();
		this.context = context;
		this.listener = new AbstractGenericPropertyChangeListener<List<Group>>() {
			@Override
			public void propertyChange(
					GenericPropertyChangeEvent<List<Group>> event) {
				if ("groups".equals(event.getPropertyName())) {
					groups = event.getNewValue();
					updateView();
				}
			}
		};
		this.context.addPropertyChangeListener(listener);
	}
	
	private void updateView() {
		if (!this.tree.getTree().isDisposed()) {
			Display display = this.tree.getTree().getDisplay();
			display.syncExec (new Runnable () {
				public void run () {
					SelectionContentProvider.this.tree.refresh();
					SelectionContentProvider.this.tree.expandAll();
				}
			});
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return groups.toArray();
	}

	@Override
	public void dispose() {
		this.context.removePropertyChangeListener(listener);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Group) {
			sort(((Group)parentElement).getItems());
			return ((Group)parentElement).getItems().toArray();
		} else if (parentElement instanceof Item) {
			Item item = (Item) parentElement;
			return getAttributes(item);
		}
		return null;
	}
	
	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (Cast.isInstance(element, Group.class)) {
			return true;
		} else if (Cast.isInstance(element, Item.class)) {
			return hasAnnotation(Cast.as(Item.class, element));
		}
		return false;
	}
	
	// Metodos utilitarios.
	/**
	 * Verifica si algún campo de la clase tiene
	 * la annotation {@link Attribute}.
	 * @param item
	 * @return
	 */
	private boolean hasAnnotation(Item item) {
		boolean findAnnotation = false;
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(item.getClass(), Object.class);
			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propdesc) {
				String name = pd.getName();
				Attribute annotation = getAnnotation(item, name);
				if (annotation != null) {
					findAnnotation = true;
					break;
				}
			}
		} catch (IntrospectionException e) {
			findAnnotation = false;
		}
		return findAnnotation;
	}
	
	/**
	 * Obtiene la annotation {@link Attribute} de la propiedad especificada. 
	 * Si la proiedad no tiene la annotation retorna null.
	 * @param item
	 * @param property
	 * @return
	 */
	private Attribute getAnnotation(Item item, String property) {
		try {
			Field field = item.getClass().getDeclaredField(property);
			return field.getAnnotation(Attribute.class);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	private Object[] getAttributes(Item item) {
		List<String> list = new ArrayList<String>();
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(item.getClass(), Object.class);
			PropertyDescriptor propdesc[] = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propdesc) {
				String name = pd.getName();
				Attribute annotation = getAnnotation(item, name);
				if (annotation != null) {
					list.add(annotation.label()+": "+pd.getReadMethod().invoke(item, new Object[0]));
				}
			}
		} catch (Exception e) {
			//FIXME
			return null;
		}
		return list.toArray();
	}
	
	private void sort(List<Item> items) {
		Collections.sort(items, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				Integer lenght1 = o1.getName().length();
				Integer lenght2 = o2.getName().length();
				if (lenght1.equals(lenght2)) {
					return o1.getName().compareTo(o2.getName());
				} else {
					return lenght1.compareTo(lenght2);  
				}
				
			}
		});
	}

}
