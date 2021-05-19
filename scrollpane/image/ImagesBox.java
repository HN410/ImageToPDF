package scrollpane.image;

import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import scrollpane.ScrollBox;
import scrollpane.ScrollPane;


public class ImagesBox extends ScrollBox<String> implements Serializable{
	public ImagesBox() {
		super();
	}

	public void addPage(String uri, ScrollPane sP) {
		ImageIcon icon = new ImageIcon(uri);
		JLabel label = new JLabel(icon);
		this.add(label);
		pageList.add(uri);
		expand();
		sP.nowImageUri = uri;
	}

	public void changeLast(String uri, ScrollPane sP) {
		ImageIcon icon = new ImageIcon(uri);
		JLabel label = new JLabel(icon);
		remove(getComponentCount()-1);
		pageList.set(pageN - 1, uri);
		this.add(label);
		sP.nowImageUri = uri;
	}
}
