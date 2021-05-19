package toolbar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import main.MainClass;
public class SendingByTcpIpThread extends Thread implements Serializable{
	private boolean isSucceeded;
	private AtomicBoolean running;
	private String fileName;
	private MainClass mainC;

	private int portN;
	public SendingByTcpIpThread(String fileName, int portN, MainClass mainC){
		super();
		running = new AtomicBoolean(true);
		isSucceeded = false;
		this.fileName = fileName;
		this.portN = portN;
		this.mainC = mainC;
	}
	@Override
	synchronized public void run() {
		  byte[] buff = new byte[1000000];
		  ServerSocket svSock = null;
		  BufferedInputStream bin = null;
		  BufferedOutputStream bout = null;
		  try {
				svSock = new ServerSocket(15403);
				System.out.println("test1");
				Socket sock = svSock.accept();
				System.out.println("test2");
				if(running.get()) {
					InputStream in = sock.getInputStream();

			        bin = new BufferedInputStream(in);
			        bout = new BufferedOutputStream(
			        		new FileOutputStream(new File(fileName)));
		            try {
		                int n = 0;
		                while ((n = bin.read(buff)) > 0) {
		                	bout.write(buff, 0, n);
		                }
		                bout.flush();
		            } catch (Throwable t) {
		            	isSucceeded = false;
		                t.printStackTrace();
		            }
		            isSucceeded=true;
		  		    mainC.scrollPane.addImage(fileName);
				}
			}catch(IOException e) {
				isSucceeded = false;
				JOptionPane.showMessageDialog(mainC, "エラーが発生しました。");
				e.printStackTrace();
			}finally {
				try {
					if(bout != null){
						bout.close();
					}
					if(bin != null) {
						bin.close();
					}
					if(svSock != null) {
						svSock.close();
					}
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		  mainC.toolbar.endReceiving(isSucceeded);
    }

	public void stopRunning() {
		running.set(false);
		interrupt();
		Socket socket = null;
		OutputStream out = null;
		try {
			socket = new Socket("192.168.10.101", portN);
			out = socket.getOutputStream();
			String shutdown = "Shutdowncommand";
			for(int i = 0; i < shutdown.length(); i++) {
				out.write(shutdown.charAt(i));
			}
			out.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(socket != null) {
					socket.close();
				}
				if(out !=null) {
					out.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean getIsSucceeded() {
		return isSucceeded;
	}

}
