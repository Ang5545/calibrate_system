package ru.ang5545.scanning_system_2.cam_acces;

import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;

import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_highgui.CvCapture;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacpp.opencv_highgui.*;

public class Grabber {

	private static final int DEF_HEIGHT = 200;
	private static final int DEF_WIDTH  = 300;
	
	private CvCapture capture;	
	private IplImage grabbedImg;
	
	private int camIndex;
	//private CanvasFrame testFrame;
	
	
	public Grabber(int camIndex) {
		this.camIndex   = camIndex;
		this.capture 	= opencv_highgui.cvCreateCameraCapture( camIndex );
		this.grabbedImg = opencv_highgui.cvQueryFrame( capture );
//		this.grabbedImg = ImageHelper.getEmptyImage(DEF_HEIGHT, DEF_WIDTH );
	//	testFrame = new CanvasFrame("Test");
	}
	
	public void snapShoot(){
		this.grabbedImg = opencv_highgui.cvQueryFrame( capture );
		//return grabbedImg;
	}
	
	public IplImage getGrabedImage() {
		return grabbedImg;
	}
	
	public void stopGrub(){
		cvReleaseImage(grabbedImg);
		//TODO do release capture
		opencv_highgui.cvReleaseCapture(capture);
	}
	
}
