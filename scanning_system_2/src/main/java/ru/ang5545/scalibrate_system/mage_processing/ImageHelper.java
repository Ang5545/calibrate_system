package ru.ang5545.scalibrate_system.mage_processing;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.*;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class ImageHelper {


	public static BufferedImage getBufferedImage(IplImage src) {
	    OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
	    Java2DFrameConverter paintConverter = new Java2DFrameConverter();
	    Frame frame = grabberConverter.convert(src);
	    BufferedImage img = paintConverter.getBufferedImage(frame,1);
	    //cvRelease(src);
	    return img;
	}
	
	public static  BufferedImage getEmptyImage(int height, int width){
		IplImage img = cvCreateImage(cvSize(height, width), IPL_DEPTH_8U, 1); 
		IplImage whiteImg = fillingImage(img, 255, 255, 255);
		BufferedImage bufImg = getBufferedImage(whiteImg);
		cvRelease(img);
	    return bufImg;
	}
	
	public static IplImage fillingImage( IplImage img, int r, int g, int b){
		CvScalar scalar = new CvScalar();
        scalar.setVal(0, r);
        scalar.setVal(1, g);
        scalar.setVal(2, b);
	    cvSet( img , scalar );		
	    return img;
	}
	
	public static IplImage createImage(CvSize size, int channels) {
		IplImage img = cvCreateImage(size, IPL_DEPTH_8U, channels );
		cvSet(img, CvScalar.BLACK);
		return img;
	}
	
	public static void drawRactangular(IplImage src, CvPoint[] points, CvScalar color, int thiknes) {
		if (points != null) {	
			cvLine(src, points[0], points[1], color, thiknes, CV_AA, 0);
			cvLine(src, points[1], points[2], color, thiknes, CV_AA, 0);
			cvLine(src, points[2], points[3], color, thiknes, CV_AA, 0);
			cvLine(src, points[3], points[0], color, thiknes, CV_AA, 0);
		}
	}
	
}
