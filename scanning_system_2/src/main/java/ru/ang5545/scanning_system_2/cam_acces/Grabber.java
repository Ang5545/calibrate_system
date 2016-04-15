package ru.ang5545.scanning_system_2.cam_acces;

import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class Grabber {

	private OpenCVFrameGrabber grabber;
	private OpenCVFrameConverter.ToIplImage converter;
	
	public Grabber(int camIndex) {
		this.grabber = new OpenCVFrameGrabber(camIndex);
		this.converter = new OpenCVFrameConverter.ToIplImage();
		startGrub();
	}

	
	public void startGrub(){
		try {
			grabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopGrub(){
		try {
			grabber.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IplImage grab(){
		try {
			return converter.convert(grabber.grab());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public CvSize getResolution() {
		return new CvSize(grabber.getImageWidth(), grabber.getImageHeight());
	}
	
}
