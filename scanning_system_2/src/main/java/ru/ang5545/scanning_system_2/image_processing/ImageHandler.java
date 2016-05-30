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
	private IplImage middleContours;

	
	private ChannelHandler chanHandler;
	private ContourHandler contHandler;
	private CalibrateHandler calibHandler;

	public ImageHandler(CvSize size) {
		this.chanHandler = new ChannelHandler(size);
		this.contHandler = new ContourHandler(size);
		this.calibHandler = new CalibrateHandler(size);
		this.contours 		= ImageHelper.createImage(size, 3);
		this.middleContours	= ImageHelper.createImage(size, 3);
	}

	public void processImage(IplImage img, ThresholdParameters redThPar, ThresholdParameters greenThPar, ThresholdParameters blueThPar) {
		this.origin		= cvCloneImage(img);
		this.result		= cvCloneImage(img);
		cvSet(contours, CvScalar.WHITE);
		cvSet(middleContours, CvScalar.WHITE);

		chanHandler.processImg(img, redThPar, greenThPar, blueThPar);
		contHandler.processImage(chanHandler.getRgbSumm());
		contHandler.drawContours(contours, CvScalar.BLUE, CvScalar.BLUE, 5);
		contHandler.drawContours(result, CvScalar.GREEN, CvScalar.GREEN, 5);
		
		contHandler.drawPoints(result);
		contHandler.drawCernelLines(result);
		contHandler.drawFindRactangular(result, CvScalar.BLUE);
		
		contHandler.drawContours(middleContours, CvScalar.BLACK, CvScalar.WHITE, 1);
		calibHandler.processImage(middleContours, contHandler.getObjectPoints());
		
		
		
//		contHandler.drawContours(middleContours, CvScalar.BLUE, CvScalar.WHITE, 1);
//		contHandler.drawFindRactangular(rectanContours, CvScalar.BLUE);
//
//		contHandler.drawContours(middleContours, CvScalar.BLUE, CvScalar.WHITE, 1);
//		contHandler.perspictiveCorrection(middleContours, perspectiveTr);
//		contHandler.drawCalibrateRectangular(perspectiveTr, CvScalar.BLACK);
//		contHandler.drawPointsLines(perspectiveTr, CvScalar.BLACK);
	
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
	

	
	public BufferedImage getPerspectiveTr() {
		//return ImageHelper.getBufferedImage(calibHandler.getpPerspectiveWraped());
		return ImageHelper.getBufferedImage(calibHandler.getpResultImage());
	}
	
	public void release() {
		cvReleaseImage(origin);
		cvReleaseImage(result);
		contHandler.release();
		calibHandler.release();
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
		cvSaveImage("/home/fedor-m/Рабочий стол/test.jpg", calibHandler.getpResultImage());
		sleep(2000);
		
//		cvSaveImage("/Users/fedormurashko/Desktop/1/perspective.jpg", perspectiveTr);
		sleep(2000);
		
	}
	
	
	
}
