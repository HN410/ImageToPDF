package imgpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import scrollpane.PanelWrapper; 

public class ImgPdf {
	public static boolean save(File file, PanelWrapper pW) {
		boolean isSuccessed = true;
		ImgPdfBox box = new ImgPdfBox(pW);
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fis = new FileOutputStream(file);
			oos = new ObjectOutputStream(fis);
			oos.writeObject(box);
			oos.flush();
		}catch(IOException e) {
			e.printStackTrace();
			isSuccessed = false;
		}finally {
			if(oos!=null) {
				try {
					oos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return isSuccessed;
	}
	
	public static ImgPdfBox open(File file) {
		ObjectInputStream ois = null;
		ImgPdfBox imgpdfBox = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			imgpdfBox = (ImgPdfBox) ois.readObject();
			
		}catch(IOException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}finally {
			if(ois != null) {
				try {
					ois.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return imgpdfBox;
	}
}
