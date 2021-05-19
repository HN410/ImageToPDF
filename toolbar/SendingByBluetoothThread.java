package toolbar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.JOptionPane;

import main.MainClass;

public class SendingByBluetoothThread extends Thread{
	private AtomicBoolean running;
	private boolean isSucceeded;
	private String fileName;
	private MainClass mainC;
	private String UUID;

	public SendingByBluetoothThread(String fileName, String UUID,
			MainClass mainC) {
		super();
		running = new AtomicBoolean(true);
		isSucceeded = false;
		this.fileName = fileName;
		this.UUID = UUID;
		this.mainC = mainC;

	}

	@Override
	synchronized public void run() {
		byte[] buff;
		StreamConnectionNotifier server = null;
		StreamConnection channel = null;

		try {
			server = (StreamConnectionNotifier) Connector.open(
					"btspp://localhost:" + UUID, Connector.READ);
			ServiceRecord record = LocalDevice.getLocalDevice().getRecord(server);
			LocalDevice.getLocalDevice().updateRecord(record);

			channel = server.acceptAndOpen();

			if(running.get()) {
				BufferedInputStream bin = null;
				BufferedOutputStream bout = null;
				try {
					InputStream in = channel.openInputStream();
					bin = new BufferedInputStream(in);
					bout = new BufferedOutputStream(new FileOutputStream(
							new File(fileName)));

					buff = new byte[1000000];
					int n = 0;
					while((n = in.read(buff)) > 0) {
						bout.write(buff, 0, n);
					}


				}catch(IOException e) {
					isSucceeded = false;
					JOptionPane.showMessageDialog(mainC, "エラーが発生しました。");
					e.printStackTrace();

				}finally {
					if(bin!=null) {
						try {bin.close();}catch(IOException e) {e.printStackTrace();}
					}
					if(bout!=null) {
						try {bout.close();}catch(IOException e) {e.printStackTrace();}
					}
				}
	            isSucceeded=true;
		  		mainC.scrollPane.addImage(fileName);
			}

		}catch(IOException e) {
			isSucceeded = false;
			JOptionPane.showMessageDialog(mainC, "エラーが発生しました。");
			e.printStackTrace();
		}finally {
			if(channel!=null) {
				try {channel.close();}catch(IOException e) {e.printStackTrace();}
			}
			if(server!=null){
				try {server.close();}catch(IOException e) {e.printStackTrace();}
			}
		}
		mainC.toolbar.endReceiving(isSucceeded);
	}

	public void stopRunning() {
		running.set(false);
		interrupt();
	}

}
