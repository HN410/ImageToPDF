package scrollpane.graphic;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.MainClass;
import pdf.DoublePointsBox;
import pdf.ElementsBox;

public class GraphicsPage extends JPanel implements Serializable{

	public ArrayList<PaintCanvas> listP;
	private int x, y, xx, yy;
	private MainClass mainC;
	

	private final int blink = 4;
	public GraphicsPage(Dimension d,MainClass main) {
		super();

		setOpaque(false);
		setLayout(null);
		x = -1;
		y = -1;
		xx = -1;
		yy = -1;

		listP = new ArrayList<PaintCanvas>();
		this.mainC = main;
		setPreferredSize(d);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		switch(mainC.getSelectedB()) {
		case "━":
			paintLine(g);
			break;
		case "□":
		    paintRectangle(g);
		    break;
		default:
		}
	}
	void paintLine(Graphics g) {
		BasicStroke stroke = new BasicStroke(5);
		if(xx >= 0 && yy >= 0 && x >=0 && y >= 0) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setStroke(stroke);
			g2D.drawLine(xx, yy, x, y);
		}
	}

	void paintRectangle(Graphics g) {
		BasicStroke stroke = new BasicStroke(5);
		if(xx >= 0 && yy >= 0 && x >=0 && y >= 0) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setStroke(stroke);
			int minX = Math.min(xx, x);
			int minY = Math.min(y, yy);
			int lenX = Math.abs(xx - x);
			int lenY = Math.abs(yy - y);
			g2D.drawRect(minX, minY, lenX, lenY);
		}
	}
	
	public void setGraphics(ElementsBox eB) {
		for(DoublePointsBox dp: eB.linesBox) {
			int[] points = {dp.x1, dp.y1, dp.x2, dp.y2};
			PaintCanvas p = new PaintCanvas(mainC, points, 1);
			listP.add(p);
			canvasSetBounds(p, points);
			this.add(p);
			
		}
		
		for(DoublePointsBox dp: eB.rectanglesBox) {
			int[] points = {dp.x1, dp.y1, dp.x1 + dp.x2, dp.y1 + dp.y2};
			PaintCanvas p = new PaintCanvas(mainC, points, 2);
			listP.add(p);
			canvasSetBounds(p, points);
			this.add(p);

		}
		
		for(int i = 0; i < eB.textsBox.size(); i++) {
			DoublePointsBox dp = eB.textRectanglesBox.get(i);
			int[] points = {dp.x1, dp.y1, dp.x1 + dp.x2, dp.y1 + dp.y2};
			PaintCanvas p = new PaintCanvas(mainC, points, 3);
			p.textArea.setText(eB.textsBox.get(i));
			listP.add(p);
			canvasSetBounds(p, points);
			this.add(p);
		}
		
		for(int i = 0; i < eB.paintsBox.size(); i++) {
			DoublePointsBox dp = eB.paintsBox.get(i);
			int[] points = {dp.x1, dp.y1, dp.x1 + dp.x2, dp.y1 + dp.y2};
			PaintCanvas p = new PaintCanvas(mainC, points, 5);
			p.imageUri = eB.urisBox.get(i);
			p.imgNewSet();
			listP.add(p);
			canvasSetBounds(p, points);
			this.add(p);
		}
	}
	
	public void canvasSetBounds(PaintCanvas p, int[] points) {
		int x1 = points[0];
		int y1 = points[1];
		int x2 = points[2];
		int y2 = points[3];
		
		p.setBounds(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2) + blink*2,
				Math.abs(y1 - y2) + blink * 2);
	}




}
