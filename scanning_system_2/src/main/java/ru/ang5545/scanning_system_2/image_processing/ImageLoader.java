package ru.ang5545.scanning_system_2.image_processing;

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

import ru.ang5545.utils.Path;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;


public class ImageLoader {

	private static final String IMAGES_DIR 	= "/resources/testImages/";
	private static final String IMG_FORMAT 	= ".png";
	private int i;
	
	private String dirPath;
	private IplImage img;

	public ImageLoader(){
		this.dirPath =  Path.getAppPath() + IMAGES_DIR;
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
		this.img	= cvLoadImage(path, 3);
		this.i = i < 14 ? i + 1 :  1;
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
		IplImage img = cvLoadImage(path, 3);
		CvSize size =  new CvSize(img.width(), img.height());
		cvRelease(img);
		return size;
	}
}
