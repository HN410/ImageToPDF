package scrollpane.graphic;

import java.io.Serializable;
import java.util.ArrayList;

import main.MainClass;
import scrollpane.ScrollBox;

public class GraphicsBox extends ScrollBox<GraphicsPage> implements Serializable{
	private MainClass mainC;

	public GraphicsBox(MainClass c) {
		super();
		pageList = new ArrayList<GraphicsPage>();
		this.mainC = c;
	}

	public void addPage() {
		GraphicsPage panel = new GraphicsPage(size, mainC);
		pageList.add(panel);
		this.add(panel);
		expand();
	}

}
