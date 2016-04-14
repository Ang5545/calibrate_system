package ru.ang5545.scanning_system_2.cam_acces;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;


public class Grabber {

	//private int camIndex;
	private OpenCVFrameGrabber grabber;
	private OpenCVFrameConverter.ToIplImage converter;
	
	private CanvasFrame frame;
	private OpenCVFrameConverter.ToIplImage sourceConverter;
	
	public Grabber(int camIndex) {
		//this.camIndex  = camIndex;
		this.grabber = new OpenCVFrameGrabber(camIndex);
		this.converter = new OpenCVFrameConverter.ToIplImage();
		startGrub();
		
//		frame = new CanvasFrame("TEST");
//		sourceConverter = new OpenCVFrameConverter.ToIplImage();
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
			//IplImage img = converter.convert(grabber.grab());
			//frame.showImage(sourceConverter.convert(img));
			return converter.convert(grabber.grab());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
// -------------------------
// ------ OLD GRABBER ------
// ----- (javaCv 0.8 ) -----
// -------------------------
	
	
//	private CvCapture capture;	
//	private IplImage grabbedImg;	
	
//	public Grabber(int camIndex) {
//		this.camIndex   = camIndex;
//		this.capture 	= opencv_highgui.cvCreateCameraCapture( camIndex );
////		this.grabbedImg = opencv_highgui.cvQueryFrame( capture );
////		this.grabbedImg = ImageHelper.getEmptyImage(DEF_HEIGHT, DEF_WIDTH );
//	//	testFrame = new CanvasFrame("Test");
//	}
//	
//	public void snapShoot(){
//		this.grabbedImg = opencv_highgui.cvQueryFrame( capture );
//		//return grabbedImg;
//	}
//	
//	public IplImage getGrabedImage() {
//		return grabbedImg;
//	}
//	
//	public void stopGrub(){
//		//cvReleaseImage(grabbedImg);
//		//TODO do release capture
//		opencv_highgui.cvReleaseCapture(capture);
//		//cvReleaseImage(grabbedImg);
//	}
	
}
