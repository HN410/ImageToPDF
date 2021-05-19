package main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.InetAddress;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import menu.menuBar;
import scrollpane.ScrollPane;
import toolbar.ToolBar;

//送信される画像は12M,3:4リサイズ済を想定

public class MainClass extends JFrame implements ChangeListener,
MouseListener, WindowListener{
	
	private int initX;
	private int initY;
	private int initW;
	private int initH;
	private String selectedB;
	private boolean isSetted;
	
	// zoomMax, Min はパーセント単位
	public static final int zoomRatioMax = 400;
	public static final int zoomRatioMin = 25;
	public static final int zoomRatioStep = 25;
	
	public int zoomRatio;
	public JToggleButton buttonN;
	public Properties properties;
	public String title = "ImageToPdf";
	public String titleNow = title;
	public ScrollPane scrollPane;
	public ToolBar toolbar;
	public menuBar menubar;
	public File file;
	public boolean isEditted;
	public static void main(String args[]) {
		MainClass frame = new MainClass();
		frame.setVisible(true);
	}
	MainClass(){
		super();
		addWindowListener(this);
		properties = PropertiesClass.getProperties();
		getPropertiesData();
		zoomRatio = 100;
		isSetted = false;
		isEditted = false;
		setTitle(title);
		setBounds(initX, initY, initW, initH);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		toolbar = new ToolBar(properties, this);

		menubar = new menuBar(properties, this);
		setJMenuBar(menubar);
		
		scrollPane = new ScrollPane(this);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		buttonN = toolbar.buttonNormal;
		JToggleButton buttonL = toolbar.buttonLine;
		JToggleButton buttonR = toolbar.buttonRectangle;
		JToggleButton buttonT = toolbar.buttonText;
		JToggleButton buttonP = toolbar.buttonPaint;
		JToggleButton buttonI = toolbar.buttonImage;

		buttonN.addChangeListener(this);
		buttonL.addChangeListener(this);
		buttonR.addChangeListener(this);
		buttonT.addChangeListener(this);
		buttonP.addChangeListener(this);
		buttonI.addChangeListener(this);

		selectedB = "・";
		
		ipAddressChecker();
	}

	public void stateChanged(ChangeEvent e) {
		JToggleButton b = (JToggleButton) e.getSource();
		if(b.isSelected()) {
			this.selectedB = b.getText();
			//図形を選択できるように一時的にunvisible
			if(this.selectedB.equals("・")) {
				scrollPane.mousePBox.setVisible(false);
			}else {
				scrollPane.mousePBox.setVisible(true);
			}
		}

	}

	public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
    
    public void windowClosing(WindowEvent e) {
    	Point p = getLocation();
    	Dimension d = getSize();
    	
    	properties.setProperty("initX", String.valueOf(p.x));
    	properties.setProperty("initY", String.valueOf(p.y));
    	properties.setProperty("initW", String.valueOf(d.width));
    	properties.setProperty("initH", String.valueOf(d.height));
    	
    	PropertiesClass.setProperties(properties);
    	
    	if(isEditted) {
			int option = JOptionPane.showConfirmDialog(this, "変更が保存されていませんが、"
					 + "保存しますか？");
			if(option == JOptionPane.YES_OPTION) {
				menubar.fileSaveButtonPushed();
			}else if(option == JOptionPane.NO_OPTION) {
				this.dispose();
			}
    	}else {
    		this.dispose();
    	}
    }

    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    
    private void ipAddressChecker() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ipAddress = addr.getHostAddress();
			if(!properties.getProperty("prevIpAddress").equals(ipAddress)) {
				properties.setProperty("prevIpAddress", ipAddress);
				JOptionPane.showMessageDialog(this, "IPアドレスが変わりました。確認してください。");
			}
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
		}
    }
    
    public void getPropertiesData() {
    	initX = Integer.parseInt(properties.getProperty("initX"));
    	initY = Integer.parseInt(properties.getProperty("initY"));
    	initW = Integer.parseInt(properties.getProperty("initW"));
    	initH = Integer.parseInt(properties.getProperty("initH"));
    }
    
	public String getSelectedB() {
		return this.selectedB;
	}

	public void changeIsSetted() {
		this.isSetted = !this.isSetted;
	}

	public boolean getIsSetted() {
		return this.isSetted;
	}
	
	public void setIsEditted(boolean b) {
		isEditted = b;
		if(isEditted) {
			if(!titleNow.startsWith("*")) setTitleNow("* " + titleNow);
		}else {
			if(titleNow.startsWith("*")) setTitleNow(titleNow.replaceFirst("* ", ""));
		}
	}
	
	public void setTitleNow(String t) {
		this.titleNow = t;
		setTitle(t);
	}
	
	public void setZoomRatio(int ratio) {
		if(ratio > MainClass.zoomRatioMax) {
			ratio = MainClass.zoomRatioMax;
		}else if(ratio < MainClass.zoomRatioMin){
			ratio = MainClass.zoomRatioMin;
		}
		
		this.toolbar.sliderZoom.setValue(ratio);
		this.toolbar.spinnerZoom.setValue(ratio);
		this.zoomRatio = ratio;
	}
}
