package scrollpane.mouse;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import javax.swing.JPanel;

import main.MainClass;
import scrollpane.graphic.GraphicsPage;
import scrollpane.graphic.PaintCanvas;

public class MouseListeningPanel extends JPanel implements MouseListener, MouseMotionListener, Serializable {
	private int x, y, xx, yy;
	private MainClass mainC;
	private final int blink = 4;
	private GraphicsPage gPage;
	private Dimension size;

	public MouseListeningPanel(Dimension d, MainClass main, GraphicsPage gPage) {
		super();
		setOpaque(false);
		setLayout(null);
		x = -1;
		y = -1;
		xx = -1;
		yy = -1;
		addMouseListener(this);
		addMouseMotionListener(this);
		this.mainC = main;
		setPreferredSize(d);
		size = d;
		this.gPage = gPage;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.RED);
		switch (mainC.getSelectedB()) {
		case "━":
			paintLine(g);
			break;
		case "🖌":
		case "📷":
		case "Ｔ":
		case "□":
			paintRectangle(g);
			break;
		default:
		}
	}

	void paintLine(Graphics g) {
		BasicStroke stroke = new BasicStroke(4);
		if (xx >= 0 && yy >= 0 && x >= 0 && y >= 0) {
			Graphics2D g2D = (Graphics2D) g;

			g2D.setStroke(stroke);
			g2D.drawLine(xx, yy, x, y);
		}
	}

	void paintRectangle(Graphics g) {
		BasicStroke stroke = new BasicStroke(4);
		if (xx >= 0 && yy >= 0 && x >= 0 && y >= 0) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setStroke(stroke);
			int minX = Math.min(xx, x);
			int minY = Math.min(y, yy);
			int lenX = Math.abs(xx - x);
			int lenY = Math.abs(yy - y);
			g2D.drawRect(minX, minY, lenX, lenY);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point point = e.getPoint();
		x = point.x;
		y = point.y;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point point = e.getPoint();
		xx = point.x;
		yy = point.y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(javax.swing.SwingUtilities.isLeftMouseButton(e)) {
			
			Point point = e.getPoint();
			this.x = (int) Math.min(point.x, size.getWidth());
			this.y = (int) Math.min(point.y, size.getHeight());
			
			int[] points = { xx, yy, this.x, this.y };
			if (!mainC.getSelectedB().equals("・")) {
				PaintCanvas p = null;
				switch (mainC.getSelectedB()) {
				case "━":
					p = new PaintCanvas(mainC, points, 1);
					break;
				case "□":
					p = new PaintCanvas(mainC, points, 2);
					break;
				case "Ｔ":
					p = new PaintCanvas(mainC, points, 3);
					break;
				case "🖌":
					p = new PaintCanvas(mainC, points, 5);
					break;
				case "📷":
					p = new PaintCanvas(mainC, points, 6);
					break;
				}
				gPage.listP.add(p);
				p.setBounds(Math.min(xx, point.x), Math.min(yy, point.y),
						Math.abs(xx - point.x) + blink * 2, Math.abs(yy - point.y) + blink * 2);
				gPage.add(p);
				
				x = -1;
				xx = -1;
				y = -1;
				yy = -1;
				repaint();
				
				switch (mainC.getSelectedB()) {
				case "Ｔ":
					mainC.buttonN.doClick();
					p.textArea.requestFocus();
					break;
				case "🖌":
					p.imgMake();
					p.imagePaintOpen();
					break;
				}
				mainC.setIsEditted(true);
			}
		}
		

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
