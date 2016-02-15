package ru.ang5545.scanning_system_2.cam_acces;

import org.bytedeco.javacpp.opencv_highgui.CvCapture;
import org.bytedeco.javacpp.videoInputLib.videoInput;



public class DeviceManager {

	private int camIndex = 0;
	
	public int getCamIndex() {
		return camIndex;
	}
	
	public static int getCamCount() {
		//String cameraInformation = "";
		int n = videoInput.listDevices();
		
		//TODO fix exceprion
		//Exception in thread "AWT-EventQueue-0" java.lang.UnsatisfiedLinkError: no jnivideoInputLib in java.library.path
		
//		for (int i = 0; i < n; i++) {
//		            String info = videoInputLib.videoInput.getDeviceName(i);
//		            //cameraInformation = info + " Device id:" + i + "\n";
//		            System.out.println("Your information for camera:"+info+" and device index is="+i);
//		}
		return n;
	}
	
}
