package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvRelease;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;

import ru.ang5545.model.ThresholdParameters;
//import static org.bytedeco.javacpp.helper.opencv_core.cvDrawContours;


public class ImageHandler {
	
	private IplImage origin;
	private IplImage contours;
	private IplImage result;

	private ChannelHandler chanHandler;
	private ContourHandler contHandler;

	public ImageHandler(CvSize size) {
		this.chanHandler = new ChannelHandler(size);
		this.contHandler =  new ContourHandler();
	}

	public void processImage(IplImage img, ThresholdParameters redThPar, ThresholdParameters greenThPar, ThresholdParameters blueThPar) {
//		CvSize size =  new CvSize(img.width(), img.height());
		this.origin		= cvCloneImage(img);

		chanHandler.processImg(img, redThPar, greenThPar, blueThPar);
//		contHandler.processImage(chanHandler.getRgbSumm());
		
	//	this.contours = contHandler.drawContours(ImageHelper.createImage(size, 3), CvScalar.BLUE, CvScalar.WHITE, 6);
	//	this.result = contHandler.drawContours(img, CvScalar.BLUE, CvScalar.WHITE, 6);
		
		//IplImage imgWithCOntour =  contHandler.drawContours(img, CvScalar.GREEN, CvScalar.GREEN, 6);
		//this.result = contHandler.drawPoints(imgWithCOntour);
		//cvRelease(imgWithCOntour);
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
	
	public void release() {
		cvRelease(origin);
//		cvRelease(contours);
//		cvRelease(result);
	}
}
