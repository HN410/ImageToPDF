package toolbar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.MainClass;
public class ToolBar extends JToolBar implements ActionListener, ChangeListener, MouseListener, Serializable{
	public JToggleButton buttonNormal;
	public JToggleButton buttonLine;
	public JToggleButton buttonRectangle;
	public JToggleButton buttonText;
	public JToggleButton buttonPaint;
	public JToggleButton buttonImage;
	public JButton buttonPreviousPage;
	public JButton buttonNextPage;
	public JButton buttonSetImage;
	public JLabel labelMessageSetted;
	
	public JButton buttonZoomPlus;
	public JButton buttonZoomMinus;
	public JSlider sliderZoom;
	public JSpinner spinnerZoom;
	
	
	public JButton buttonReceive;
	public JButton buttonStop;
	public ButtonGroup buttonGroup;
	public JLabel labelSendingMode;
	public JLabel labelMessageReceiving;

	private String UUID;
	private int portN;
	private boolean isSucceeded;
	private boolean isReceiving;
	private MainClass mainC;
	private Properties properties;
	private SendingByTcpIpThread threadT;
	private SendingByBluetoothThread threadB;
	public ToolBar(Properties properties, MainClass mainC) {
		super();
		this.properties = properties;
		this.mainC = mainC;
		portN = Integer.parseInt(properties.getProperty("port"));
		UUID = properties.getProperty("BluetoothUUID");
		isReceiving = false;

		buttonNormal = new JToggleButton("・", true);
		buttonLine = new JToggleButton("━");
		buttonRectangle = new JToggleButton("□");
		buttonText = new JToggleButton("Ｔ");
		buttonPaint = new JToggleButton("🖌");
		buttonImage = new JToggleButton("📷");
		

		buttonPreviousPage = new JButton("◀");
		buttonNextPage = new JButton("▶");
		buttonSetImage = new JButton("セット");
		labelMessageSetted = new JLabel("未セット");
		labelMessageSetted.setBackground(Color.GREEN);
		labelMessageSetted.setOpaque(true);
		
		buttonZoomPlus = new JButton("＋");
		buttonZoomMinus = new JButton("－");
		sliderZoom = new JSlider(MainClass.zoomRatioMin, MainClass.zoomRatioMax, mainC.zoomRatio);
		SpinnerNumberModel modelSpinnerZoomRatio = new SpinnerNumberModel(mainC.zoomRatio,
				MainClass.zoomRatioMin,
				MainClass.zoomRatioMax,
				MainClass.zoomRatioStep);
		spinnerZoom = new JSpinner(modelSpinnerZoomRatio);
		
		buttonReceive = new JButton("受信");
		buttonStop = new JButton("■");
		labelSendingMode = new JLabel(properties.getProperty("sendingWay"));
		labelMessageReceiving = new JLabel("ここにメッセージを表示します");

		buttonPreviousPage.addMouseListener(this);
		buttonNextPage.addMouseListener(this);
		buttonSetImage.addMouseListener(this);
		buttonReceive.addMouseListener(this);
		buttonStop.addMouseListener(this);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(buttonNormal);
		buttonGroup.add(buttonLine);
		buttonGroup.add(buttonRectangle);
		buttonGroup.add(buttonText);
		buttonGroup.add(buttonPaint);
		buttonGroup.add(buttonImage);
		
		buttonZoomPlus.addActionListener(this);
		buttonZoomPlus.setActionCommand("ZoomPlus");
		buttonZoomMinus.addActionListener(this);
		buttonZoomMinus.setActionCommand("ZoomMinus");

		sliderZoom.setSize(new Dimension(40, 20));
		sliderZoom.setMinorTickSpacing(MainClass.zoomRatioStep);
		sliderZoom.setPaintTicks(true);
		sliderZoom.setSnapToTicks(true);
		sliderZoom.addChangeListener(this);
		
		spinnerZoom.addChangeListener(this);

		
		
		this.add(buttonNormal);
		this.add(buttonLine);
		this.add(buttonRectangle);
		this.add(buttonText);
		this.add(buttonPaint);
		this.add(buttonImage);
		this.addSeparator();
		this.add(buttonPreviousPage);
		this.add(buttonNextPage);
		this.add(buttonSetImage);
		this.add(labelMessageSetted);
		this.addSeparator();
		this.add(buttonZoomMinus);
		this.add(sliderZoom);
		this.add(buttonZoomPlus);
		this.add(spinnerZoom);
		this.addSeparator();
		this.add(buttonReceive);
		this.add(buttonStop);
		this.add(labelSendingMode);
		this.addSeparator();
		this.add(labelMessageReceiving);
		
		buttonImage.setEnabled(false);

		setFloatable(true);
	}

	 public void mouseClicked(MouseEvent e){
		    JButton button = (JButton) e.getSource();
		    if(button.equals(buttonSetImage)) {
		    	if(labelMessageSetted.getText().equals("未セット")) {
		    		labelMessageSetted.setText("セット済");
		    		labelMessageSetted.setBackground(Color.RED);
		    	}else {
		    		labelMessageSetted.setText("未セット");
		    		labelMessageSetted.setBackground(Color.GREEN);
		    	}
		    	mainC.changeIsSetted();
		    }else if(button.equals(buttonPreviousPage)){
		    	imgPageMove(-1);
		    }else if(button.equals(buttonNextPage)){
		    	imgPageMove(1);
		    }else if(button.equals(buttonReceive)) {
				labelMessageReceiving.setText("受信中");
		    	receiveImage();
		    }else if(button.equals(buttonStop) && isReceiving) {
				  if(properties.getProperty("sendingWay").equals("TCPIP")) {
				    	threadT.stopRunning();
				  }else {
					  threadB.stopRunning();
				  }

		    }
	  }

	  public void mouseEntered(MouseEvent e){}
	  public void mouseExited(MouseEvent e){}
	  public void mousePressed(MouseEvent e){}
	  public void mouseReleased(MouseEvent e){}

	  private void receiveImage() {
		  this.isSucceeded = true;
		  String fileDate = new SimpleDateFormat("yyyyMMddHHmmss",
				  Locale.US).format(new Date());
		  String fileName = String.format("img/ImagePdf_%s.jpg", fileDate);

		  isReceiving = true;
		  if(properties.getProperty("sendingWay").equals("TCPIP")) {
			  receivingByTCPIP(fileName);
		  }else {
			  receivingByBluetooth(fileName);
		  }
		  mainC.setIsEditted(true);
     }


	  private void receivingByTCPIP(String fileName) {
		  threadT = new SendingByTcpIpThread(fileName, portN, mainC);
		  threadT.start();
	  }

	  private void receivingByBluetooth(String fileName) {
		  threadB = new SendingByBluetoothThread(fileName, UUID, mainC);
		  threadB.start();
	  }

	  public void endReceiving(boolean isSucceeded) {
		  isReceiving = false;
		  if(isSucceeded) {
			  labelMessageReceiving.setText("受信終了");
		  }else {
			  labelMessageReceiving.setText("受信停止");
		  }
	  }
	  
	  public File[] getImgFileList() {
		  File dir = new File("img/");
		  File[] filesList = dir.listFiles();
		  java.util.Arrays.sort(filesList, new java.util.Comparator<File>() {
			  public int compare(File file1, File file2) {
				  return file1.getName().compareTo(file2.getName());
			  }
		  });
		  return filesList;
	  }
	  
	  public void imgPageMove(int i) {
		  //増加→i=1, 減少→i=-1
	    if(!mainC.getIsSetted()) {
	    	File nowImage = new File(mainC.scrollPane.nowImageUri);
	    	File[] fileList = getImgFileList();
	    	int nowNumber 	= Arrays.asList(fileList).indexOf(nowImage);
	    	while(true) {
	    		nowNumber += i + fileList.length;
	    		nowNumber %= fileList.length;
	    		if(fileList[nowNumber].getName().endsWith(".jpg")) {
		    		System.out.println(nowNumber);
		    		System.out.println(fileList.length);
	    			mainC.scrollPane.addImage("img/" +
		    		fileList[nowNumber].getName());
	    			break;
	    		}
	    	}
	    	mainC.setIsEditted(true);	    	;
	    }
	  }
	  /*
	  private boolean restoreImage(String fileName, byte[] buff) {
		  boolean isSucceeded = true;
		  BufferedOutputStream bout = null;
		  try {
		        bout = new BufferedOutputStream(
		        				new FileOutputStream(new File(fileName)));
		        bout.write(buff);
		        bout.flush();
		  }catch(IOException e) {
			  isSucceeded = false;
			  JOptionPane.showMessageDialog(mainC, "保存エラーが発生しました。");
			  e.printStackTrace();
		  }finally {
			  if(bout != null) {
				  try {
					  bout.close();
				  }catch(IOException e) {
					  e.printStackTrace();
				  }
			  }
		  }
		  return isSucceeded;
	  }
	  */

	@Override
	public void stateChanged(ChangeEvent e) {
		int ratio = mainC.zoomRatio;
		Object obj = e.getSource();
		
		if(obj instanceof JSlider) {
			JSlider slider = (JSlider) obj;
			ratio = slider.getValue();
		}else if(obj instanceof JSpinner) {
			JSpinner spinner = (JSpinner) obj;
			ratio = (Integer) spinner.getValue();
		}
		mainC.setZoomRatio(ratio);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof JButton) {
			JButton button = (JButton) obj;
			String cmd = button.getActionCommand(); 
			int zoomRatio = mainC.zoomRatio;
			if(cmd.equals("ZoomPlus")) {
				zoomRatio += MainClass.zoomRatioStep;
			}else {
				zoomRatio -= MainClass.zoomRatioStep;	
			}
			mainC.setZoomRatio(zoomRatio);
		}
	}

}
