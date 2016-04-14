package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.cvCanny;
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


import javax.swing.JFrame;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.*;


import static org.bytedeco.javacpp.opencv_imgcodecs.*;


public class ImageLoader {

	private static final String IMAGES_DIR 	= "/testImages/";
	private static final String IMG_FORMAT 	= ".png";
	private int i;
	
	private String dirPath;
	private IplImage img;

	public ImageLoader(){
		try {
			this.dirPath =  new File(".").getCanonicalPath() + IMAGES_DIR;
		} catch (IOException e) {
			System.out.println("directory not found");
			e.printStackTrace();
		}
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
		System.out.println("grab");
		String path = dirPath + "image" + i + IMG_FORMAT;
		this.img	= cvLoadImage(path, 3);
		this.i = i < 5 ? i + 1 :  1;
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
}
