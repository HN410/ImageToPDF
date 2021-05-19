package scrollpane;
import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.JPanel;

import scrollpane.graphic.GraphicsBox;
import scrollpane.image.ImagesBox;
import scrollpane.mouse.MousePanelsBox;

public class PanelWrapper extends JPanel implements Serializable{
	
	public int pageN;
	public ImagesBox imagesBox;
	public GraphicsBox graphicsBox;
	public MousePanelsBox mousePBox;
	private int sizeW = 1008;
	private int sizeH = 756;
	private JPanel innerPanel;
	public PanelWrapper(){
		super();
		pageN = 0;
		innerPanel = new JPanel();
		this.add(innerPanel);
		innerPanel.setLayout(null);
		innerPanel.setPreferredSize(new Dimension(0, 0));
	}
	
	public void expand() {
		pageN ++;
		innerPanel.setPreferredSize(new Dimension(sizeW, sizeH * pageN));
	}
	
	public void addIBox(ImagesBox box) {
		innerPanel.add(box);
		imagesBox = box;
	}
	public void addGBox(GraphicsBox box) {
		innerPanel.add(box);
		graphicsBox = box;
	}
	public void addMBox(MousePanelsBox box) {
		innerPanel.add(box);
		mousePBox=box;
	}

}
