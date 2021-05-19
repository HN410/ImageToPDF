package scrollpane.mouse;

import java.io.Serializable;

import main.MainClass;
import scrollpane.ScrollBox;
import scrollpane.graphic.GraphicsPage;

public class MousePanelsBox extends ScrollBox<String> implements Serializable{
	private MainClass mainC;

	public MousePanelsBox(MainClass c){
		super();
		this.mainC = c;
	}
	public void addPage(GraphicsPage graphicsP) {
		MouseListeningPanel panel = new MouseListeningPanel(size,
				mainC, 	graphicsP);
		this.add(panel);
		expand();
	}

}
