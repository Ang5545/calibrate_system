package ru.ang5545.calibrate_system.image_processing;


import static org.bytedeco.javacpp.opencv_core.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.*;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CameraDevice.Settings;

import ru.ang5545.calibrate_system.model.ThresholdParameters;

import org.bytedeco.javacv.CameraSettings;
import org.bytedeco.javacv.GeometricCalibrator;
import org.bytedeco.javacv.MarkedPlane;
import org.bytedeco.javacv.Marker;
import org.bytedeco.javacv.MarkerDetector;
import org.bytedeco.javacv.ProCamGeometricCalibrator;
import org.bytedeco.javacv.ProjectorSettings;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_calib3d.*;

import static org.bytedeco.javacpp.opencv_imgproc.*;




public class CalibrateHandler {

	private int deviderCount;
	private CvSize resolution;
	
	private IplImage object;
	private IplImage perspectiveWrapedObj;
	private IplImage resultRectangular;
	
	private IplImage grayObj;
	private IplImage grayRectan;

	private IplImage resultImage;
	
	private CvPoint[] objCernelsPoints;
	private CvPoint[] resultRectPoints;

	private CvPoint centerPoint;
	
	private List<CvPoint2D32f> objVerticalPoints;
	private List<CvPoint2D32f> objGorizontalPoints;
	
	private int maxShoot;
	private int shootCount;
	private double minError;
	private CvMat optimalCamMatrix;
	private CvMat optimalDistCoeffs; 
	
	public CalibrateHandler(CvSize resolution, int deviderCount, int maxShoot) {
		this.resolution 		  = resolution;
		this.deviderCount		  = deviderCount;
		this.perspectiveWrapedObj = ImageHelper.createImage(resolution, 3);
		this.resultRectangular 	  = ImageHelper.createImage(resolution, 3);
		this.grayObj 			  = ImageHelper.createImage(resolution, 1);
		this.grayRectan 		  = ImageHelper.createImage(resolution, 1);
		this.resultImage 		  = ImageHelper.createImage(resolution, 3);
		this.centerPoint 		  = getImageCenter(resolution);
		this.shootCount			  = 0;
		this.maxShoot 			  = maxShoot;
		this.optimalCamMatrix 	  = CvMat.create(3, 3);
		this.optimalDistCoeffs 	  = CvMat.create(5, 1);
	}
	
	public void processImage(IplImage obj, CvPoint[] objCernelsPoints) {
		this.object	= cvCloneImage(obj);
		this.objCernelsPoints = objCernelsPoints;
		this.resultRectPoints = getResultRectPoints();
		cvSet(perspectiveWrapedObj, CvScalar.WHITE);
		cvSet(resultRectangular, CvScalar.WHITE);
		cvSet(resultImage, CvScalar.WHITE);
		
		perspictiveCorrection(object, perspectiveWrapedObj);
		cvCvtColor(perspectiveWrapedObj, grayObj, CV_BGR2GRAY);
		
//		ImageHelper.drawRactangular(resultRectangular, resultRectPoints, CvScalar.BLACK, 1);		
//		cvCvtColor(resultRectangular, grayRectan, CV_BGR2GRAY);
		
		objVerticalPoints = findVeticalCalibratePPoints(grayObj, deviderCount);
		objGorizontalPoints = findGorizontalCalibratePoints(grayObj, deviderCount);
		
		if (shootCount >= maxShoot) {
			shootCount = 0;
			minError = 20;
		} else {
			shootCount++;
		}
		
		calibrateCamera(objVerticalPoints, objGorizontalPoints, deviderCount);
		
//		int board_width = deviderCount;
//		int board_height = deviderCount;
//		int count = board_width * board_height;
		
//		if (objVerticalPoints.size() == count) {
//			if (shootCount >= maxShoot) {
//				shootCount = 0;
//				minError = 20;
//			} else {
//				shootCount++;
//			}
//			calibrateCamera(objVerticalPoints, count, board_width, board_height);
//		}
		
		
		//drawPoints(objCernelsPoints, resultImage);
		//drawPoints(objVerticalPoints, resultImage);
		//drawPoints(recPoints, resultImage);
		
	}
	
	public IplImage getpPerspectiveWraped() {
		return this.perspectiveWrapedObj;
	}
	
	public IplImage getpResultRectangular() {
		return this.grayRectan;
	}
	
	public IplImage getpResultImage() {
		return this.resultImage;
	}
	
	
	public void calibrate(IplImage src, IplImage dst) {
		if (!optimalCamMatrix.isNull() && !optimalDistCoeffs.isNull()) {
			perspictiveCorrection(src, dst);
			CvMat mapx = CvMat.create(resolution.height(), resolution.width(), CV_32FC1);
			CvMat mapy = CvMat.create(resolution.height(), resolution.width(), CV_32FC1);
			cvInitUndistortMap(optimalCamMatrix, optimalDistCoeffs, mapx, mapy);
			cvRemap(dst, dst, mapx, mapy, CV_INTER_LINEAR, CvScalar.ZERO);			
			cvRelease(mapx);
			cvRelease(mapy);
		}
	}
	
	private void calibrateCamera(List<CvPoint2D32f> objVerticalPoints, List<CvPoint2D32f> objGorizontalPoints, int deviderCount) {
		int board_width = deviderCount;
		int board_height = deviderCount;
		int count = board_width * board_height;
		
		if (objVerticalPoints.size() == count && objGorizontalPoints.size() == count) {
			
			CvMat imagePoints = CvMat.create(count, 2);
			for(int p = 0; p < count; p++) {
				imagePoints.put(0+p, 0, objGorizontalPoints.get(p).x());
				imagePoints.put(0+p, 1, objGorizontalPoints.get(p).y());	
			}

			
			CvMat objectPoints = CvMat.create(count, 3);
			int idx = 0;
			for (int j = 0; j < board_width; j++) {
				for (int i = board_height-1; i >= 0; i--) {		
					objectPoints.put(idx, 0, (double) (i));
					objectPoints.put(idx, 1, (double) (j));
					objectPoints.put(idx, 2, (double) (0));
					idx++;
				}
			}

			CvMat pointCount = cvCreateMat(1, 1, CV_32SC1);
			pointCount.put(0, count);
			
			CvMat cameraMatrix = CvMat.create(3, 3);
			CvMat distCoeffs = CvMat.create(5, 1);
			System.out.println("calibration");
			double error = cvCalibrateCamera2(objectPoints, imagePoints, pointCount, resolution, cameraMatrix, distCoeffs);

			if (error < minError) {
				optimalCamMatrix = cameraMatrix.clone();
				optimalDistCoeffs = distCoeffs.clone();
				minError = error;
			} 
			
			cvRelease(pointCount);
			cvRelease(imagePoints);
			cvRelease(objectPoints);
			cvRelease(cameraMatrix);
			cvRelease(distCoeffs);
		}
	}
	
	public void perspictiveCorrection(IplImage src, IplImage dst) {
		if (objCernelsPoints != null && src != null && !src.isNull()) {
			
			CvPoint objTopRightP 	= new CvPoint(objCernelsPoints[2].x(), 	objCernelsPoints[2].y());
			CvPoint objTopLeftP 	= new CvPoint(objCernelsPoints[1].x(), 	objCernelsPoints[1].y());
			CvPoint objBttLeftP  	= new CvPoint(objCernelsPoints[0].x(),	objCernelsPoints[0].y());
			CvPoint objBttRightP 	= new CvPoint(objCernelsPoints[3].x(), 	objCernelsPoints[3].y());
			
			CvPoint imgTopRightP 	= new CvPoint(resultRectPoints[2].x(), 	resultRectPoints[2].y());
			CvPoint imgTopLeftP 	= new CvPoint(resultRectPoints[1].x(), 	resultRectPoints[1].y());
			CvPoint imgBttLeftP  	= new CvPoint(resultRectPoints[0].x(),	resultRectPoints[0].y());
			CvPoint imgBttRightP 	= new CvPoint(resultRectPoints[3].x(), 	resultRectPoints[3].y());
			
			float[] sourcePoints = {
					objTopLeftP.x(), 	objTopLeftP.y(),
					objTopRightP.x(),	objTopRightP.y(),
					objBttLeftP.x(),	objBttLeftP.y(),
					objBttRightP.x(),	objBttRightP.y()
			};
			
			float[] distinPoints = {
					imgTopLeftP.x(), 	imgTopLeftP.y(),
					imgTopRightP.x(),	imgTopRightP.y(),
					imgBttLeftP.x(),	imgBttLeftP.y(),
					imgBttRightP.x(),	imgBttRightP.y()
			};
			 
			CvMat perspectiveTransform = cvCreateMat(3,3,CV_32FC1);
			cvGetPerspectiveTransform(sourcePoints, distinPoints, perspectiveTransform);
			cvWarpPerspective(src, dst, perspectiveTransform, INTER_LINEAR, CvScalar.WHITE);
		}
	}
	
	private CvPoint getImageCenter(CvSize size) {
		int x = size.width() / 2;
		int y = size.height() / 2;
		return new CvPoint(x, y);
	}
	
	public void release() {
		cvRelease(object);
		//cvRelease(resultImage);
	}
	
	private CvPoint[] getResultRectPoints() {

		if (objCernelsPoints != null) {
		
			double a_x = objCernelsPoints[1].x() - objCernelsPoints[0].x();
			double a_y = objCernelsPoints[1].y() - objCernelsPoints[0].y();
		
			double b_x = objCernelsPoints[2].x() - objCernelsPoints[1].x();
			double b_y = objCernelsPoints[2].y() - objCernelsPoints[1].y();
		
			double rectALength = Math.sqrt( Math.pow(a_x, 2) + Math.pow(a_y, 2));
			double rectBLength = Math.sqrt( Math.pow(b_x, 2) + Math.pow(b_y, 2));
		
			int minX = (int) (centerPoint.x() - (rectBLength / 2));
			int maxX = (int) (centerPoint.x() + (rectBLength / 2));
			int minY = (int) (centerPoint.y() - (rectALength / 2));
			int maxY = (int) (centerPoint.y() + (rectALength / 2));
			
			CvPoint[] result = new CvPoint[4];	
			result[0] = new CvPoint(minX, minY);
			result[1] = new CvPoint(minX, maxY);
			result[2] = new CvPoint(maxX, maxY);
			result[3] = new CvPoint(maxX, minY);
			return result;
		} else {
			return null;
		}
	}

	public List<CvPoint2D32f> findVeticalCalibratePPoints(IplImage src, int dimensionCount) {
		List<CvPoint2D32f> result = new ArrayList<CvPoint2D32f>();
		int height = src.height();
		int width = src.width();
		int devider = dimensionCount + 1;
		int y_step = height / devider;
		
		for (int i = 1; i < devider; i++) {
			int y = y_step * i;			
			List<CvPoint2D32f> verticalPoints = findVerticalExtremePoints(src, y);
			CvPoint2D32f verticalMiddlePoint = new CvPoint2D32f((width/2), y);
			if (verticalPoints != null && verticalPoints.size() == 2) {
				result.add(verticalPoints.get(0));
				result.add(verticalMiddlePoint);
				result.add(verticalPoints.get(1));
			}	
		}
		
		return result;
	}
	
	public List<CvPoint2D32f> findGorizontalCalibratePoints(IplImage src, int dimensionCount) {
		List<CvPoint2D32f> points = new ArrayList<CvPoint2D32f>();
		int height = src.height();
		int width = src.width();
		int devider = dimensionCount + 1;
		int x_step = width / devider;
		int y_step = height / devider;

		for (int i = 1; i < devider; i++) {
			int x = x_step * i;
			int y = y_step * i;
			List<CvPoint2D32f> gorizontalPoints = findGorizontalExtremePoints(src, x);
			CvPoint2D32f gorizontalMiddlePoint = new CvPoint2D32f(x, (height/2));			
			if (gorizontalPoints != null && gorizontalPoints.size() == 2) {
				points.add(gorizontalPoints.get(0));
				points.add(gorizontalMiddlePoint);
				points.add(gorizontalPoints.get(1));
			}
		}
		
		// TODO сделать автоматом без привязки к dimCount
		List<CvPoint2D32f> result = new ArrayList<CvPoint2D32f>();
		if (points.size() == 9) {
			result.add(points.get(0));
			result.add(points.get(3));
			result.add(points.get(6));
			result.add(points.get(1));
			result.add(points.get(4));
			result.add(points.get(7));
			result.add(points.get(2));
			result.add(points.get(5));
			result.add(points.get(8));
		}
		return result;
	}
	
	
	public List<CvPoint2D32f> findGorizontalExtremePoints(IplImage src, int x) {
		List<CvPoint2D32f> allPoints = new ArrayList<CvPoint2D32f>();
		ByteBuffer bb_src 	= src.createBuffer();
		for(int y = 0; y < src.height(); y++) {
			int index = y * src.widthStep() + x * src.nChannels();
	    	int val = bb_src.get(index) & 0xFF;
		    if (val < 255) {
		    	allPoints.add(new CvPoint2D32f(x, y));
		    }
		}
		List<CvPoint2D32f> result = new ArrayList<CvPoint2D32f>();
		if (allPoints.size() >= 2) {
			result.add(allPoints.get(0));
			result.add(allPoints.get(allPoints.size()-1));
		}
		return result;
	}
	
	public List<CvPoint2D32f> findVerticalExtremePoints(IplImage src, int y) {
		List<CvPoint2D32f> allPoints = new ArrayList<CvPoint2D32f>();
		ByteBuffer bb_src 	= src.createBuffer();
		for(int x = 0; x < src.width(); x++) {
			int index = y * src.widthStep() + x * src.nChannels();
	    	int val = bb_src.get(index) & 0xFF;
		    if (val < 255) {
		    	allPoints.add(new CvPoint2D32f(x, y));
		    }
		}
		List<CvPoint2D32f> result = new ArrayList<CvPoint2D32f>();
		if (allPoints.size() >= 2) {
			result.add(allPoints.get(0));
			result.add(allPoints.get(allPoints.size()-1));
		}
		return result;
	}
	
	
	private void drawPoints(List<CvPoint2D32f> points, IplImage img) {
		CvFont font = new CvFont();
        double hScale = 3;
        double vScale = 3;
        int    lineWidth = 3;
        
        cvInitFont(font,CV_FONT_HERSHEY_SIMPLEX, hScale,vScale,0,lineWidth, 8);
        
		for(int i = 0; i < points.size(); i++){
			CvPoint2D32f point = points.get(i);
			int x = (int) point.x();
			int y = (int) point.y(); 
			cvDrawCircle(img, new CvPoint(x, y), 10, CvScalar.BLACK, -1, 8, 0);
			cvPutText(img, String.valueOf(i), new CvPoint(x, y), font, CvScalar.BLACK);
		}
	}
	
	
	private void drawPoints(CvPoint[] points, IplImage img) {
		CvFont font = new CvFont();
        double hScale = 3;
        double vScale = 3;
        int    lineWidth = 3;
        
        cvInitFont(font,CV_FONT_HERSHEY_SIMPLEX, hScale,vScale,0,lineWidth, 8);
        if (points != null && points.length == 4) { 
			for (int i = 0; i < points.length; i++) {
				CvPoint point = points[i];
				int x = (int) point.x();
				int y = (int) point.y(); 
				cvDrawCircle(img, new CvPoint(x, y), 10, CvScalar.BLACK, -1, 8, 0);
				cvPutText(img, String.valueOf(i), new CvPoint(x, y), font, CvScalar.BLACK);
			}
        }
	}
}
