package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetPerspectiveTransform;
import static org.bytedeco.javacpp.opencv_imgproc.cvWarpPerspective;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgproc.cvDrawCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;

import org.bytedeco.*;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CameraDevice.Settings;
import org.bytedeco.javacv.CameraSettings;
import org.bytedeco.javacv.GeometricCalibrator;
import org.bytedeco.javacv.MarkedPlane;
import org.bytedeco.javacv.Marker;
import org.bytedeco.javacv.MarkerDetector;
import org.bytedeco.javacv.ProCamGeometricCalibrator;
import org.bytedeco.javacv.ProjectorSettings;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import ru.ang5545.model.ThresholdParameters;

public class CalibrateHandler {

	private IplImage object;
	private IplImage perspectiveWrapedObj;
	private IplImage resultRectangular;
	
	private IplImage grayObj;
	private IplImage grayRectan;

	private CvPoint[] objCernelsPoints;
	private CvPoint[] resultRectPoints;

	
	private CvPoint centerPoint;
	
	
	public CalibrateHandler(CvSize resolution) {
		this.perspectiveWrapedObj = ImageHelper.createImage(resolution, 3);
		this.resultRectangular = ImageHelper.createImage(resolution, 3);
		this.grayObj = ImageHelper.createImage(resolution, 1);
		this.grayRectan = ImageHelper.createImage(resolution, 1);
		this.centerPoint = getImageCenter(resolution);
	}
	
	public void processImage(IplImage obj, CvPoint[] objCernelsPoints) {
		this.object	= cvCloneImage(obj);
		this.objCernelsPoints = objCernelsPoints;
		this.resultRectPoints = getResultRectPoints();
		cvSet(perspectiveWrapedObj, CvScalar.WHITE);
		cvSet(resultRectangular, CvScalar.WHITE);
		
		
		perspictiveCorrection(object, perspectiveWrapedObj);
		ImageHelper.drawRactangular(resultRectangular, resultRectPoints, CvScalar.BLUE, 1);
		
		//cvCvtColor(perspectiveWrapedObj, grayObj
		
		cvCvtColor(resultRectangular, grayRectan, CV_BGR2GRAY);
		//drawPointsLines(grayRectan, CvScalar.RED);
		
		List<CvPoint> recPoints = findExtremePoints(grayRectan, 3);
		if (recPoints != null) {
			for (int i = 0; i < recPoints.size(); i++) {
				cvDrawCircle(grayRectan, recPoints.get(i), 10, CvScalar.BLACK, -1, 8, 0);
			}	
		}
	}
	
	public IplImage getpPerspectiveWraped() {
		return this.perspectiveWrapedObj;
	}
	
	public IplImage getpResultRectangular() {
		return this.grayRectan;
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

	
	public List<CvPoint> findExtremePoints(IplImage src, int dimensionCount) {
		List<CvPoint> result = new ArrayList<CvPoint>();
		
		int height = src.height();
		int width = src.width();
		int half_x = width/2;
		int half_y = height/2;
		
		List<CvPoint> verticalPoints_1 = findExtremePoints(src, half_x, half_x+1, 0, height);
		List<CvPoint> gorizontPoints_1 = findExtremePoints(src, 0, width, half_y, half_y+1);
		
		
//		for (int i = 1; i <= dimensionCount; i++) {
//			int x = x_step * i;
//			int y = y_step * i;
//			List<CvPoint> verticalPoints = findExtremePoints(src, x, x+1, 0, height);
//			List<CvPoint> gorizontPoints = findExtremePoints(src, 0, width, y, y+1);
//			if (verticalPoints != null && gorizontPoints!= null) {
//				result.addAll(verticalPoints);
//				result.addAll(verticalPoints);
//				return result;
//			}	
//		}
		return null;
	}
	
	
//	public List<CvPoint> findExtremePoints(IplImage src, int dimensionCount) {
//		List<CvPoint> result = new ArrayList<CvPoint>();
//		int devider = dimensionCount + 2;
//		int height = src.height();
//		int width = src.width();
//		
//		int x_step = width / devider;
//		int y_step = height / devider;
//
//		System.out.println("height = " + height);
//		System.out.println("width  = " + width);
//		
//		for (int i = 1; i <= dimensionCount; i++) {
//			int x = x_step * i;
//			int y = y_step * i;
//			List<CvPoint> verticalPoints = findExtremePoints(src, x, x+1, 0, height);
//			List<CvPoint> gorizontPoints = findExtremePoints(src, 0, width, y, y+1);
//			if (verticalPoints != null && gorizontPoints!= null) {
//				result.addAll(verticalPoints);
//				result.addAll(verticalPoints);
//				return result;
//			}	
//		}
//		return null;
//	}
	
	public List<CvPoint> findExtremePoints(IplImage src, int min_x, int max_x, int min_y, int max_y) {
		ByteBuffer bb_src 	= src.createBuffer();
		List<CvPoint> points = new ArrayList<CvPoint>();
//
//		System.out.println(" min_y = " +  min_y);
//		System.out.println(" max_y = " +  max_y);
//		System.out.println(" min_x = " +  min_x);
//		System.out.println(" max_x = " +  max_x);
//		
		for(int y = min_y; y < max_y; y++) {
		    for(int x = min_x; x < max_x; x++) {
		    	int index = y * src.widthStep() + x * src.nChannels();
		    	cvSet2D(src, y, x, CvScalar.BLACK);
		    	int val = bb_src.get(index) & 0xFF;
			    if (val < 255) {
			    	points.add(new CvPoint(x, y));
			    }
		    }
		}
		if (points.size() > 2) {
			List<CvPoint> result = new ArrayList<CvPoint>();
			result.add(points.get(0));
			result.add(points.get(points.size()-1));	
			return result;
		} 
		return null;
	}
}
