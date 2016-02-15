package ru.ang5545.scanning_system_2.cam_acces;

import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;

import java.awt.List;
import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_core.IplImage;


public class ImageLoaderForTest {

	private final static String PATH 		= "/Users/fedormurashko/Desktop/imageFromPaint/image";
	private final static String FORMAT 		= ".png";
	private final static int IMAGE_COUNT 	= 5;
	
	private ArrayList paths; 
	private int imgIndex;
	private IplImage img;
	
	public ImageLoaderForTest() {
		this.imgIndex 	= 1;
		this.paths 		= new ArrayList();
		for (int i = 1; i <- IMAGE_COUNT; i++ ) {
			paths.add(PATH + i + FORMAT);
		}
	}
	
	public void snapShoot() {
		this.img = opencv_highgui.cvLoadImage(PATH + imgIndex + FORMAT);
		if ( imgIndex < IMAGE_COUNT) {
			this.imgIndex++;
		} else {
			this.imgIndex = 1;
		}
	}
	
	public IplImage getGrabedImage() {
		return img;
	}
	
	public void stopGrub(){
	}
	
}
