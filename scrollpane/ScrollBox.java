package scrollpane;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;


public abstract class ScrollBox<E> extends JPanel implements Serializable{
	public ArrayList<E> pageList;
	public int pageN;

	protected final Dimension size = new Dimension(1008, 756);
	protected final int sizeW = 1008;
	protected final int sizeH = 756;

	public ScrollBox(){
		super();
		pageList = new ArrayList<E>();
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBounds(0, 0, 0, 0);
		pageN = 0;
	}

	public void expand() {
		pageN++;
		setBounds(0, 0, sizeW, sizeH * pageN);
	}
}
