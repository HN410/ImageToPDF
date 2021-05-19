package pdf;

import java.io.Serializable;
import java.util.ArrayList;

import scrollpane.graphic.GraphicsPage;
import scrollpane.graphic.PaintCanvas;

public class ElementsBox implements Serializable{
	public ArrayList<DoublePointsBox> rectanglesBox;
	public ArrayList<DoublePointsBox> linesBox;
	public ArrayList<DoublePointsBox> textRectanglesBox;
	public ArrayList<DoublePointsBox> paintsBox;
	public ArrayList<String> textsBox;
	public ArrayList<String> urisBox;

	public ElementsBox(GraphicsPage gP) {
		super();
		rectanglesBox = new ArrayList<>();
		linesBox = new ArrayList<>();
		textRectanglesBox = new ArrayList<>();
		textsBox = new ArrayList<>();
		paintsBox = new ArrayList<>();
		urisBox = new ArrayList<>();

		for(PaintCanvas p:  gP.listP) {
			switch(p.gType) {
			case 1:
				DoublePointsBox dp = p.getDoublePoints();
				linesBox.add(dp);
				break;
			case 2:
				rectanglesBox.add(p.getDoublePoints2());
				break;
			case 3:
				textRectanglesBox.add(p.getDoublePoints2());
				textsBox.add(p.textArea.getText());
				break;
			case 5:
				paintsBox.add(p.getDoublePoints2());
				urisBox.add(p.imageUri);
				break;
			}
		}
	}



}
