package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;

import ru.ang5545.model.CalibFrameLine;
import ru.ang5545.model.ThresholdParameters;


public class ImageHandler {
	
	private IplImage origin;
	private IplImage contours;
	private IplImage result;
	private IplImage perspectiveTr;
	
	private ChannelHandler chanHandler;
	private ContourHandler contHandler;

	public ImageHandler(CvSize size) {
		this.chanHandler = new ChannelHandler(size);
		this.contHandler = new ContourHandler(size);
		this.contours 		= ImageHelper.createImage(size, 3);
		this.perspectiveTr 	= ImageHelper.createImage(size, 3);
	}

	public void processImage(IplImage img, ThresholdParameters redThPar, ThresholdParameters greenThPar, ThresholdParameters blueThPar) {
		this.origin		= cvCloneImage(img);
		this.result		= cvCloneImage(img);
		cvSet(contours, CvScalar.WHITE);
		cvSet(perspectiveTr, CvScalar.WHITE);
		
		chanHandler.processImg(img, redThPar, greenThPar, blueThPar);
		contHandler.processImage(chanHandler.getRgbSumm());
		
		contHandler.drawContours(contours, CvScalar.BLUE, CvScalar.BLUE, 5);
		contHandler.drawContours(result, CvScalar.BLACK, CvScalar.BLACK, 5);
		
		contHandler.drawPoints(result);
		contHandler.perspictiveCorrection(origin, perspectiveTr);
	}
	
	// ///////////////////////////////
	// //// -- image geters --  /////
	// //////////////////////////////
	
	public BufferedImage getOrigin() {
		return ImageHelper.getBufferedImage(origin);
	}
	
	
	public BufferedImage getRedChannel() {
		return ImageHelper.getBufferedImage(chanHandler.getRedChannel());
	}
	
	public BufferedImage getGreenChannel() {
		return ImageHelper.getBufferedImage(chanHandler.getGreenChannel());
	}
	
	public BufferedImage getBlueChannel() {
		return ImageHelper.getBufferedImage(chanHandler.getBlueChannel());
	}
	
	public BufferedImage getRGBsumm() {
		return ImageHelper.getBufferedImage(chanHandler.getRgbSumm());
	}
	
	public BufferedImage get_contour (){
		return ImageHelper.getBufferedImage(contours);
	}
	
	public BufferedImage getResultl() {
		return ImageHelper.getBufferedImage(result);
	}
	
	public BufferedImage getPerspetciveTransform() {
		return ImageHelper.getBufferedImage(perspectiveTr);
	}
	
	public void release() {
		cvReleaseImage(origin);
		cvReleaseImage(result);
		contHandler.release();
	}

	private void saveImage(BufferedImage img, String fileName) {
		try {
			File outputfile = new File(fileName);
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
		    System.out.println("connot save image " + fileName);
		    e.printStackTrace();
		}
	}
	
	private void sleep(int milliseconds) {
		try {
		    Thread.sleep(milliseconds);   
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	
	public void saveImages() {
//		cvSaveImage("/Users/fedormurashko/Desktop/1/origin.jpg", origin);
//		cvSaveImage("/Users/fedormurashko/Desktop/1/redChannel.jpg", chanHandler.getRedChannel());
//		cvSaveImage("/Users/fedormurashko/Desktop/1/greenChannel.jpg", chanHandler.getGreenChannel());
//		cvSaveImage("/Users/fedormurashko/Desktop/1/blueChannel.jpg", chanHandler.getBlueChannel());
		sleep(1000);
		
		cvSaveImage("/Users/fedormurashko/Desktop/1/origin.jpg", origin);
		cvSaveImage("/Users/fedormurashko/Desktop/1/redChannel.jpg", chanHandler.getRedChannel());
		cvSaveImage("/Users/fedormurashko/Desktop/1/greenChannel.jpg", chanHandler.getGreenChannel());
		cvSaveImage("/Users/fedormurashko/Desktop/1/blueChannel.jpg", chanHandler.getBlueChannel());
		cvSaveImage("/Users/fedormurashko/Desktop/1/contours.jpg", 	contours);
		cvSaveImage("/Users/fedormurashko/Desktop/1/result.jpg", result);
		cvSaveImage("/Users/fedormurashko/Desktop/1/getRgbSumm.jpg", chanHandler.getRgbSumm());
		sleep(1000);
	}
	
	
	
}
