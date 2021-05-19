package menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import imgpdf.ImgPdf;
import imgpdf.ImgPdfBox;
import main.MainClass;
import main.PropertiesClass;
import pdf.PdfMaker;

public class menuBar extends JMenuBar implements ChangeListener,
ActionListener, Serializable{

	private JMenuItem menuitem1_1;
	private JMenuItem menuitem1_2;
	private JMenuItem menuitem1_3;
	private JMenuItem menuitem1_4;
	private JRadioButtonMenuItem menuitem2_1_1;
	private JRadioButtonMenuItem menuitem2_1_2;
	private JMenuItem menuitem3_1;
	private JMenuItem menuitem3_2;
	private Properties properties;
	private MainClass mainC;
	private File file;
	public menuBar(Properties properties, MainClass mainC){
		super();
		this.properties = properties;
		this.mainC = mainC;

		file = null;

		JMenu menuFile = new JMenu("ファイル");
		JMenu menuSetting = new JMenu("設定");
		JMenu menuTool = new JMenu("ツール");

		this.add(menuFile);
		this.add(menuSetting);
		this.add(menuTool);

		menuitem1_1 = new JMenuItem("独自ファイルを開く");
		menuitem1_2 = new JMenuItem("上書き保存");
		menuitem1_3 = new JMenuItem("名前を付けて保存");
		menuitem1_4 = new JMenuItem("PDFで保存");

		menuitem1_1.addActionListener(this);
		menuitem1_2.addActionListener(this);
		menuitem1_3.addActionListener(this);
		menuitem1_4.addActionListener(this);

		JMenu menuitem2_1 = new JMenu("通信方法");
		menuitem2_1_1 = new JRadioButtonMenuItem("TCPIP");
		menuitem2_1_2 = new JRadioButtonMenuItem("Bluetooth");
		ButtonGroup group2_1 = new ButtonGroup();
		group2_1.add(menuitem2_1_1);
		group2_1.add(menuitem2_1_2);
		menuitem2_1_1.addChangeListener(this);
		menuitem2_1_2.addChangeListener(this);

		menuitem3_1 = new JMenuItem("画像キャッシュの一括削除");
		menuitem3_2 = new JMenuItem("現在のIPアドレス");
		menuitem3_1.addActionListener(this);
		menuitem3_2.addActionListener(this);


		menuFile.add(menuitem1_1);
		menuFile.add(menuitem1_2);
		menuFile.add(menuitem1_3);
		menuFile.add(menuitem1_4);
		menuSetting.add(menuitem2_1);
		menuitem2_1.add(menuitem2_1_1);
		menuitem2_1.add(menuitem2_1_2);
		menuTool.add(menuitem3_1);
		menuTool.add(menuitem3_2);
		checkProperties();
	}

	private void checkProperties() {
		if(properties.getProperty("sendingWay").equals("TCPIP")) {
			menuitem2_1_1.setSelected(true);
		}else {
			menuitem2_1_2.setSelected(true);
		}

	}

	public void stateChanged(ChangeEvent e) {
		Object o = e.getSource();
		if(o instanceof JRadioButtonMenuItem) {
			JRadioButtonMenuItem button = (JRadioButtonMenuItem) e.getSource();
			if(button.isSelected()) {
				if(button.equals(menuitem2_1_1)) {
					properties.setProperty("sendingWay", "TCPIP");
					mainC.toolbar.labelSendingMode.setText("TCPIP");
				}else if(button.equals(menuitem2_1_2)){
					properties.setProperty("sendingWay", "Bluetooth");
					mainC.toolbar.labelSendingMode.setText("Bluetooth");
				}
				PropertiesClass.setProperties(properties);
		}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) o;

			if(menuItem.equals(menuitem1_1)) {
				JFileChooser chooser = new JFileChooser(properties.getProperty("defaultDir"));
				FileFilter filter =
						new FileNameExtensionFilter("独自ファイル imgpdf", "imgpdf");
				chooser.addChoosableFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				int selected = chooser.showOpenDialog(null);
				if(selected == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					ImgPdfBox imgPdfBox = ImgPdf.open(file);
					if(imgPdfBox == null) {
						JOptionPane.showMessageDialog(this, "エラーが発生しました");
					}else {
						mainC.scrollPane.setNew(imgPdfBox);
						setFile(file);
					}
				}

			}else if(menuItem.equals(menuitem1_2)) {
				fileSaveButtonPushed();
			}else if(menuItem.equals(menuitem1_3)) {
				buttonFileSave();

			}else if(menuItem.equals(menuitem1_4)) {
				JFileChooser chooser = new JFileChooser(properties.getProperty("defaultDir"));
				FileFilter filter =
						new FileNameExtensionFilter("PDFファイル", "pdf");
				chooser.addChoosableFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				int selected = chooser.showSaveDialog(null);
				if(selected == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String fileName = file.getName();
					if(!fileName.endsWith(".pdf")) {
						fileName = file.getParent() + File.separator +
								fileName + ".pdf";
						System.out.println(fileName);
						file = new File(fileName);
					}
					PdfMaker pdfMaker = new PdfMaker(file, mainC);
					pdfMaker.pdfEdit(mainC.scrollPane.panelWrapper);
				}

			}else if(menuItem.equals(menuitem3_1)) {
				int option = JOptionPane.showConfirmDialog(this, "画像をすべて削除しますか？");
				if(option == JOptionPane.YES_OPTION) {
					deleteAllImg();
				}

			}else if(menuItem.equals(menuitem3_2)) {
				try {
					String address = InetAddress.getLocalHost().getHostAddress();
					JOptionPane.showMessageDialog(mainC, address);
				} catch (java.net.UnknownHostException e1) {
					e1.printStackTrace();

				}
			}
		}
	}

	public void fileSaveButtonPushed() {
		if(file == null) {
			buttonFileSave();
		}else {
			fileSaver(file);
		}
	}

	public void fileSaver(File file) {
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	    if(!ImgPdf.save(file, mainC.scrollPane.panelWrapper)) {
	    	JOptionPane.showMessageDialog(this, "エラーが発生しました");
	    }else {
			setFile(file);
		    mainC.setIsEditted(false);
	    }
	}

	public void buttonFileSave() {
		JFileChooser chooser = new JFileChooser(properties.getProperty("defaultDir"));
		FileFilter filter =
				new FileNameExtensionFilter("独自ファイル imgpdf", "imgpdf");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		int selected = chooser.showSaveDialog(null);
		if(selected == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			String fileName = file.getName();
			if(!fileName.endsWith(".imgpdf")) {
				fileName = file.getParent() + File.separator +
						fileName + ".imgpdf";
				file = new File(fileName);
			}
			fileSaver(file);
			setFile(file);
		}
	}

	public void deleteAllImg() {
		File imgFolder = new File("img");
		if(imgFolder.exists() && imgFolder.isDirectory()) {
			File[] files = imgFolder.listFiles();
			for(File file: files) {
				if(file.exists() && file.getName().endsWith(".jpg")) {
					file.delete();
				}
			}
		}
	}

	public void setFile(File file) {
		this.file = file;
		mainC.setTitleNow(mainC.title + " - " + file.getName());
		mainC.file = file;
	}

}
