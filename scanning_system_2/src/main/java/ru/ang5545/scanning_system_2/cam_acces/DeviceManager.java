package ru.ang5545.scanning_system_2.cam_acces;

import org.bytedeco.javacpp.opencv_core.CvSize;
//import org.bytedeco.javacpp.opencv_highgui.CvCapture;
import org.bytedeco.javacpp.videoInputLib.videoInput;



public class DeviceManager {

	private int camIndex = 1;
	private CvSize resolution = new CvSize(1280, 960);
	
	
	public int getCamIndex() {
		return camIndex;
	}
	
	public CvSize getResolutuon() {
		return this.resolution;
	}
	
	public static int getCamCount() {
		int n = videoInput.listDevices();
		return n;
	}
	
}
