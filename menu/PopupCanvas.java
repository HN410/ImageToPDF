package menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import scrollpane.graphic.PaintCanvas;

public class PopupCanvas extends JPopupMenu implements ActionListener{
	
	JMenuItem menuDelete;
	public PopupCanvas(){
		super();
		
		menuDelete = new JMenuItem("削除");
		
		menuDelete.addActionListener(this);
		
		this.add(menuDelete);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem) e.getSource();
		if(item.equals(menuDelete)) {
			PaintCanvas canvas = (PaintCanvas) getInvoker();
			canvas.delete();
		}
	}

}
