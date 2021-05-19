package scrollpane.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.MainClass;
import menu.PopupCanvas;
import pdf.DoublePointsBox;

public class PaintCanvas extends JPanel implements MouseListener, MouseMotionListener, Serializable {
	private final int imgM = 10;
	private Graphics2D g2D;
	private PopupCanvas popup;
	private MainClass mainC;
	private int x1;
	private int x2;
	private int y1;
	private int y2;

	private int initX;
	private int initY;
	private int X;
	private int Y;
	private int minX;
	private int minY;
	private int W;
	private int H;

	private int mouseX1;
	private int mouseY1;
	private int mouseX2;
	private int mouseY2;

	private int clickedPoint;
	private boolean transformEnabled = false;

	private final int blink = 2;
	public JTextArea textArea;
	public JLabel labelImage;
	public String imageUri;
	public int gType;
	//gtype : 1 line , 2 rectangle , 3 textbox, 4 , 5 paint, 6 image

	public PaintCanvas(MainClass mainC, int[] points, int graphicType) {
		super();
		this.mainC = mainC;
		this.popup = new PopupCanvas();
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.gType = graphicType;
		moveG(points);
		switch (graphicType) {
		case 3:
			textMake(W, H);
			break;
		}
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println(e);
		Point p = e.getLocationOnScreen();
		this.mouseX2 = p.x;
		this.mouseY2 = p.y;
		if (transformEnabled) {
//			 transformCanvas();
		} else {
			moveCanvas();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(gType >=5 && e.getClickCount() >= 2) {
			imagePaintOpen();

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(e);
		Point pA = e.getLocationOnScreen();
		this.mouseX1 = pA.x;
		this.mouseY1 = pA.y;
		judgeWhereClicked(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point p = e.getLocationOnScreen();
		this.mouseX2 = p.x;
		this.mouseY2 = p.y;
		int xD = mouseX2 - mouseX1;
		int yD = mouseY2 - mouseY1;

		if (transformEnabled) {
			transformCanvas(xD, yD);
		} else {
			int[] points = { initX + xD, initY + yD, X + xD, Y + yD };
			moveG(points);
		}
		this.getParent().repaint();
		showPopup(e);
		mainC.setIsEditted(true);
	}

	void moveG(int[] points) {
		initX = points[0];
		initY = points[1];
		X = points[2];
		Y = points[3];
		minX = Math.min(initX, X);
		minY = Math.min(initY, Y);
		W = Math.abs(initX - X);
		H = Math.abs(initY - Y);

		switch (gType) {
		case 1:
			if ((initX - X) * (initY - Y) < 0) {
				this.x1 = blink;
				this.y1 = H - blink;
				this.x2 = W - blink;
				this.y2 = blink;

			} else {
				this.x1 = blink;
				this.y1 = blink;
				this.x2 = W - blink;
				this.y2 = H - blink;
			}
			break;

		case 5:
		case 6:
		case 3:
		case 2:

			this.x1 = blink;
			this.x2 = W + blink;
			this.y1 = blink;
			this.y2 = H + blink;
			break;
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		BasicStroke stroke = new BasicStroke(4);
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(Color.RED);
		g2D.setStroke(stroke);
		switch (gType) {
		case 1:
			g2D.drawLine(x1, y1, x2, y2);
			break;

		case 3:
		case 2:
			g2D.drawRect(x1, y1, x2, y2);
			break;

		}
	}

	public void textMake(int W, int H) {
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(W, H));
		textArea.setLineWrap(true);
		textArea.setBounds(0, 0, W, H);
		this.add(textArea);

	}

	public void imgMake() {
		BufferedImage img = new BufferedImage((W+blink) * imgM, (H+blink) * imgM, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, (W+blink) * imgM, (H+blink) * imgM);
		g.dispose();
		String fileDate = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.US).format(new Date());
		String fileName = String.format("stamp\\ImagePdfStamp_%s.jpg", fileDate);
		this.imageUri = fileName;
		try {
			ImageIO.write(img, "jpg", new File(fileName));
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		labelImage = new JLabel();
		labelImage.setBounds(0, 0, W+blink, H+blink);
		this.add(labelImage);
	}
	
	public void imgNewSet() {
		labelImage = new JLabel();
		labelImage.setBounds(0, 0, W+blink, H+blink);
		this.add(labelImage);
		imgIconResize();
	}

	public void moveCanvas() {
		int xD = mouseX2 - mouseX1;
		int yD = mouseY2 - mouseY1;
		setBounds(minX + xD, minY + yD, W + blink*4, H + blink*4);
		repaint();

	}

	public void transformCanvas(int xD, int yD) {
		switch (clickedPoint) {
		case 1:
			int[] points1 = { minX + W, minY + H, minX + xD, minY + yD };
			resetBounds(points1);
			moveG(points1);
			break;
		case 2:
			int[] points2 = { minX, minY + H, minX + W + xD, minY + yD };
			resetBounds(points2);
			moveG(points2);
			break;
		case 3:
			int[] points3 = { minX + W, minY, minX + xD, minY + H + yD };
			resetBounds(points3);
			moveG(points3);
			break;
		case 4:
			int[] points4 = { minX, minY, minX + W + xD, minY + H + yD };
			resetBounds(points4);
			moveG(points4);
			break;
		}

		switch(gType) {
		case 3:
			textArea.setPreferredSize(new Dimension(W, H));
			textArea.setLocation(0, 0);
			textArea.repaint();
			break;
		case 5:
			imgIconResize();
			labelImage.setPreferredSize(new Dimension(W, H));
			labelImage.setLocation(0, 0);
			labelImage.repaint();
		}
		
	}

	public void resetBounds(int[] points) {
		int minX_ = Math.min(points[0], points[2]);
		int minY_ = Math.min(points[1], points[3]);
		int W_ = Math.abs(points[0] - points[2]);
		int H_ = Math.abs(points[1] - points[3]);
		setBounds(minX_, minY_, W_ + blink * 4, H_ + blink * 4);
	}

	private void judgeWhereClicked(Point p) {
		float proportion = 0.125f;
		float fW = (float) W;
		float fH = (float) H;
		clickedPoint = 0;
		if (p.x / fW < proportion | p.x <= 5) {
			if (p.y / fH < proportion | p.y <= 5) {
				clickedPoint = 1;
			} else if (p.y / fH > 1 - proportion | H - p.y <= 5) {
				clickedPoint = 3;
			}
		} else if (p.x / fW > 1 - proportion | W - p.x <= 5) {
			if (p.y / fH < proportion | p.y <= 5) {
				clickedPoint = 2;
			} else if (p.y / fH > 1 - proportion | H - p.y <= 5) {
				clickedPoint = 4;
			}
		}
		transformEnabled = false;
		if (clickedPoint > 0) {
			switch (gType) {
			case 5:
			case 2:
			case 3:
				transformEnabled = true;
				break;
			case 1:
				if ((initX - X) * (initY - Y) < 0) {
					if (clickedPoint == 2 | clickedPoint == 3) {
						transformEnabled = true;
					}
				} else {
					if (clickedPoint == 1 | clickedPoint == 4) {
						transformEnabled = true;
					}
				}
			}
		}

	}
	
	public void imagePaintOpen() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process p = runtime.exec("mspaint.exe .\\" +imageUri);
			System.out.println(p.waitFor());
			imgIconResize();

		}catch(IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void imgIconResize() {
		ImageIcon icon = new ImageIcon(imageUri);
		MediaTracker tracker = new MediaTracker(this);
		Image resizedIcon = icon.getImage().getScaledInstance(W+blink, H+blink, Image.SCALE_SMOOTH	);
		tracker.addImage(resizedIcon, 2);
		try {
			tracker.waitForAll();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		ImageIcon icon2 = new ImageIcon(resizedIcon);
		labelImage.setIcon(icon2);
		repaint();
	}
	public void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void delete() {

		GraphicsPage gp = (GraphicsPage) getParent();
		gp.listP.remove(gp.listP.indexOf(this));
		gp.remove(this);
		gp.repaint();

	}

	public DoublePointsBox getDoublePoints() {
		return new DoublePointsBox(initX, initY, X, Y);
	}

	public DoublePointsBox getDoublePoints2() {
		return new DoublePointsBox(minX, minY, W, H);
	}

}
