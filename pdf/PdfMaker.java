package pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfFileAttachmentAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfWidgetAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;

import main.MainClass;
import scrollpane.PanelWrapper;
public class PdfMaker {
	private final int height = 756;
	private final PageSize ps = new PageSize(new Rectangle(1008, 756));
	
	private PdfDocument pdf;
	private PanelWrapper pW;
	private Document document;
	private PdfAcroForm form;
	private File file;
	
	public PdfMaker(File file, MainClass mainC) {
		this.file = file;
		try {
			PdfWriter writer = new PdfWriter(file);	
			pdf = new PdfDocument(writer);
			document = new Document(pdf);
	    	form = PdfAcroForm.getAcroForm(pdf, true);
		}catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(mainC,  "ファイルが見つからないか使用中です");
			e.printStackTrace();
		}
	}
	
	public void pdfEdit(PanelWrapper pW) {
		this.pW = pW;
		int pageN = pW.pageN;
		
		for(int i = 0; i < pageN; i++) {
			PdfPage page = pdf.addNewPage(ps);
			PdfCanvas canvas = new PdfCanvas(page);
			canvas.setColor(ColorConstants.RED, false);
			canvas.setLineWidth(4);
		    ElementsBox eBox = new ElementsBox(
		    		pW.graphicsBox.pageList.get(i));
			ImageData imgd;
			try {
				imgd = ImageDataFactory.create(pW.imagesBox.pageList.get(i));
				canvas.addImage(imgd, ps, false);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		    pdfLineEdit(canvas, eBox.linesBox);
			pdfRectEdit(canvas, eBox.rectanglesBox);
			pdfTextEdit(page, eBox.textRectanglesBox, eBox.textsBox);
			pdfPaintsEdit(page, eBox.paintsBox, eBox.urisBox);
		}
		document.close();
		pdf.close();
	}
	
	private void pdfLineEdit(PdfCanvas canvas,
			ArrayList<DoublePointsBox> list) {
		if(!list.isEmpty()) {
			for(DoublePointsBox pB: list) {
				canvas.moveTo(pB.x1, height-pB.y1)
				.lineTo(pB.x2, height-pB.y2).stroke();
			}
		}
	}
	
	private void pdfRectEdit(PdfCanvas canvas,
			ArrayList<DoublePointsBox> list) {
		if(!list.isEmpty()) {
			for(DoublePointsBox pB: list) {
				int W = Math.abs(pB.x1 - pB.x2);
				int H = Math.abs(pB.y1 - pB.y2);
			    canvas.rectangle(
			    		new Rectangle(pB.x1, height-pB.y1-pB.y2, pB.x2, pB.y2));
			    canvas.closePathStroke();
			}
		}
	}
	
	private void pdfTextEdit(PdfPage page,
			ArrayList<DoublePointsBox> listPoints,
			ArrayList<String> listTexts) {
		if(!listPoints.isEmpty()) {
			for(int i = 0; i < listPoints.size();i++) {
				PdfTextFormField field = PdfFormField.createText(pdf);
				field.setMultiline(true);
				field.setFieldName("Memo");
				
				DoublePointsBox pB = listPoints.get(i);
				Rectangle rect = new Rectangle(pB.x1, height - pB.y1 - pB.y2,
						pB.x2, pB.y2);
				PdfWidgetAnnotation widget = new PdfWidgetAnnotation(rect);
				widget.makeIndirect(pdf);
				page.addAnnotation(widget);
				field.addKid(widget);
				form.addField(field, page);
				field.setValue(listTexts.get(i));
				field.setBackgroundColor(ColorConstants.WHITE);
				field.setBorderWidth(2);
				field.setBorderColor(ColorConstants.RED);
			}
		}
		
	}
	
	private void pdfPaintsEdit(PdfPage page, ArrayList<DoublePointsBox> listPoints, 
			ArrayList<String> listUris) {
		if(!listPoints.isEmpty()) {
			String ParentPath = file.getParent();
			File stampDir = new File(ParentPath + "\\stamp\\");
			if(!stampDir.exists() | !stampDir.isDirectory()) {
				stampDir.mkdir();
			}
			for(String fileN: listUris) {
				Path source = Paths.get(fileN);
				Path destination = Paths.get(ParentPath + "\\" + fileN);
				try {
					Files.copy(source, destination);
				}catch(IOException e) {e.printStackTrace();}
			}
			try {
				for(int i = 0; i < listPoints.size();i++) {
					DoublePointsBox pB = listPoints.get(i);
					Rectangle rect = new Rectangle(pB.x1, height - pB.y1 - pB.y2,
							pB.x2, pB.y2);
					String embeddedFileName = ParentPath + "\\" + listUris.get(i);
					
					PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(
							pdf, embeddedFileName, null, embeddedFileName, null, null);
					PdfAnnotation attachment = new PdfFileAttachmentAnnotation(rect, fileSpec);
					PdfFormXObject xObject = new PdfFormXObject(rect);
					ImageData imageData = ImageDataFactory.create(embeddedFileName);
					PdfCanvas canvas = new PdfCanvas(xObject, pdf);
					canvas.addImage(imageData, rect, true);
					attachment.setNormalAppearance(xObject.getPdfObject());
					page.addAnnotation(attachment);
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
