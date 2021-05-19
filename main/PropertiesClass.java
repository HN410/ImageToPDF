package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesClass {
	private static final String address = "MainSettings.properties";

	public static void setProperties(Properties properties) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(address);
			properties.store(out, "MainSettings");
		}catch(IOException ex) {
			ex.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				}catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static Properties getProperties(){
		Properties properties = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(address);
			properties.load(in);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(in != null) {
				try {
					in.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}

}
