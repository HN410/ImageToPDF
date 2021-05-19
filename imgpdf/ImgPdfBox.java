package imgpdf;

import java.io.Serializable;
import java.util.ArrayList;

import pdf.ElementsBox;
import scrollpane.PanelWrapper;


public class ImgPdfBox implements Serializable{
	public ArrayList<ElementsBox> eBList;
	public ArrayList<String> uriList;
	public int pageN;

	public ImgPdfBox(PanelWrapper pW){
		this.pageN = pW.pageN;
		eBList = new ArrayList<>();
		uriList = new ArrayList<>();

		for(int i= 0; i < pageN; i++) {
			eBList.add(
					new ElementsBox(pW.graphicsBox.pageList.get(i)));
			uriList.add(pW.imagesBox.pageList.get(i));
		}
	}

}
