package ru.ang5545.scalibrate_system.mage_processing;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.Graphics2D;
import java.awt.List;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;

import javax.swing.JFrame;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.*;

import ru.ang5545.calibrate_system.utils.Path;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;


public class ImageLoader {

	private static final String IMAGES_DIR 	= "/resources/testImagesFishEye/";
	private static final String IMG_FORMAT 	= ".png";
	
	//private static final String IMG_NAME = "/resources/arnold-frame.JPG";
	
	private int i;
	private int i_max = 5;
	
	private String dirPath;
	private IplImage img;

	public ImageLoader(){
		this.dirPath =  Path.getAppPath()+ IMAGES_DIR;
		this.i = 1;
	}
	
	private void sleep(int milliseconds) {
		try {
		    Thread.sleep(milliseconds);   
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	
	public IplImage grab() {
		//sleep(1000);
		String path = dirPath + "image" + i + IMG_FORMAT;
		//String path = dirPath + IMG_NAME;
		
		this.img	= cvLoadImage(path, 3);
		this.i = i < i_max ? i + 1 :  1;
		return img;
	}

	public IplImage getImg() {
		return img;
	}
	
	public void release() {
		cvRelease(this.img);
	}
	
	public void stopGrub() {
		cvRelease(img);
	}
	
	public CvSize getResolution() {
		String path = dirPath + "image" + i + IMG_FORMAT;
		//String path = dirPath + IMG_NAME;
		IplImage img = cvLoadImage(path, 3);
		CvSize size =  new CvSize(img.width(), img.height());
		cvRelease(img);
		return size;
	}
}
