package scrollpane;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JScrollPane;

import imgpdf.ImgPdfBox;
import main.MainClass;
import pdf.ElementsBox;
import scrollpane.graphic.GraphicsBox;
import scrollpane.image.ImagesBox;
import scrollpane.mouse.MousePanelsBox;


public class ScrollPane extends JScrollPane implements Serializable{
	public Dimension size;
	public int sizeW;
	public int sizeH;
	public MousePanelsBox mousePBox;
	public ImagesBox imagesBox;
	public GraphicsBox graphicsBox;
	public PanelWrapper panelWrapper;
	public String nowImageUri;

	
	private MainClass mainC;

	public ScrollPane(MainClass main){
		super();
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.getVerticalScrollBar().setUnitIncrement(10);
		this.getHorizontalScrollBar().setUnitIncrement(10);
		size = new Dimension(1008, 756);
		sizeW = (int) size.getWidth();
		sizeH = (int) size.getHeight();

		this.mainC = main;

		setNew();

		imagesBox.addPage("img/ImagePdf_20200514000556.jpg", this);
		graphicsBox.addPage();
		mousePBox.addPage(graphicsBox.pageList.get(0));
		panelWrapper.expand();

		this.setViewportView(panelWrapper);

	}

	public void addImage(String uri) {
		int pageN = graphicsBox.pageN;
		if(pageN == 0 | mainC.getIsSetted()) {
			imagesBox.addPage(uri, this);
			graphicsBox.addPage();
			mousePBox.addPage(graphicsBox.pageList.get(pageN));
			panelWrapper.expand();
		}else {
			imagesBox.changeLast(uri, this);
		}

	}

	public void setNew() {
		this.panelWrapper = new PanelWrapper();
		imagesBox = new ImagesBox();
		graphicsBox = new GraphicsBox(mainC);
		mousePBox = new MousePanelsBox(mainC);

		panelWrapper.addMBox(mousePBox);
		panelWrapper.addGBox(graphicsBox);
		panelWrapper.addIBox(imagesBox);
	}

	public void setNew(ImgPdfBox imgPdfBox) {
		setNew();
		int pageN = imgPdfBox.pageN;
		ArrayList<ElementsBox> eBList = imgPdfBox.eBList;
		ArrayList<String> uriList = imgPdfBox.uriList;
		
		for(int i = 0; i < pageN; i++) {
			imagesBox.addPage(uriList.get(i), this);
			graphicsBox.addPage();
			graphicsBox.pageList.get(i).setGraphics(eBList.get(i));
			
			mousePBox.addPage(graphicsBox.pageList.get(i));
			panelWrapper.expand();
		}
		this.setViewportView(panelWrapper);
	}


	public PanelWrapper getPanelWrapper() {
		return panelWrapper;
	}
}
