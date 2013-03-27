package net.cloudengine.mapviewer.pages.selection;

import net.cloudengine.mapviewer.layers.Item;
import net.cloudengine.mapviewer.tools.selection.Group;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class SelectionLabelProvider implements ILabelProvider {

	private Image groupImage;
	private Image itemImage;
	private Image attributeImage;
	
	public SelectionLabelProvider() {
		groupImage = new Image(Display.getDefault(), getClass().getResourceAsStream("group.png"));
		itemImage = new Image(Display.getDefault(), getClass().getResourceAsStream("item.png"));
		attributeImage = new Image(Display.getDefault(), getClass().getResourceAsStream("square.png"));
	}
	
	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
		groupImage.dispose();
		itemImage.dispose();
		attributeImage.dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Group) {
			return groupImage;
		} else if (element instanceof Item) {
			return itemImage;
		} else if (element instanceof String) {
			return attributeImage;
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Group) {
			return ((Group)element).getName();
		} else if (element instanceof Item) {
			return ((Item)element).getName();
		} else if (element instanceof String) {
			return element.toString();
		}
		return null;
	}

}
