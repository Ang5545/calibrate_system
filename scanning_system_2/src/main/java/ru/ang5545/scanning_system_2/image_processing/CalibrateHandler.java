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
import static org.bytedeco.javacpp.opencv_calib3d.*;

import static org.bytedeco.javacpp.opencv_imgproc.*;
import ru.ang5545.model.ThresholdParameters;




public class CalibrateHandler {

	private IplImage object;
	private IplImage perspectiveWrapedObj;
	private IplImage resultRectangular;
	
	private IplImage grayObj;
	private IplImage grayRectan;

	private IplImage resultImage;
	
	private CvPoint[] objCernelsPoints;
	private CvPoint[] resultRectPoints;

	
	private CvPoint centerPoint;
	
	
	public CalibrateHandler(CvSize resolution) {
		this.perspectiveWrapedObj = ImageHelper.createImage(resolution, 3);
		this.resultRectangular 	  = ImageHelper.createImage(resolution, 3);
		this.grayObj 			  = ImageHelper.createImage(resolution, 1);
		this.grayRectan 		  = ImageHelper.createImage(resolution, 1);
		this.resultImage 		  = ImageHelper.createImage(resolution, 3);
		this.centerPoint 		  = getImageCenter(resolution);
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
		List<CvPoint2D32f> objPoints = findExtremePoints(grayObj, 3);
		
		ImageHelper.drawRactangular(resultRectangular, resultRectPoints, CvScalar.BLACK, 1);		
		cvCvtColor(resultRectangular, grayRectan, CV_BGR2GRAY);
		List<CvPoint2D32f> recPoints = findExtremePoints(grayRectan, 3);
		
//		System.out.println("objPoints.size() = " + objPoints.size());
//		System.out.println("recPoints.size() = " + recPoints.size());
		
		
//		resultImage = cvCloneImage(grayObj);
//		ImageHelper.drawRactangular(resultImage, resultRectPoints, CvScalar.BLACK, 1);
		
//		for (int i = 0; i < recPoints.size(); i++) {
//			int x = (int) recPoints.get(i).x();
//			int y = (int) recPoints.get(i).y();
//			CvPoint p = new CvPoint(x, y);
//			cvDrawCircle(resultImage, p, 10, CvScalar.BLACK, -1, 8, 0);
//		}
//		for (int i = 0; i < objPoints.size(); i++) {
//			int x = (int) objPoints.get(i).x();
//			int y = (int) objPoints.get(i).y();
//			CvPoint p = new CvPoint(x, y);
//			cvDrawCircle(resultImage, p, 10, CvScalar.BLACK, -1, 8, 0);
//		}
		
		
//		CvMat srcPoints = cvCreateMat(3,3,CV_32FC1);
//		CvMat dstPoints = cvCreateMat(3,3,CV_32FC1);    
//		CvMat homography = cvCreateMat(3,3,CV_32FC1);
//		
//		cvFindHomography(srcPoints, dstPoints, homography);
		
//		Mat distorted_src = new Mat();
//		Mat undistort_dst = new Mat();
//		
//		
//		
//		Mat h = findHomography(distorted_src,undistort_dst);
		
//		MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();  
//        objMatOfPoint2f.fromList(objectPoints);  
//        
//        
//        
//		CvMat homography = cvCreateMat(3,3,CV_32FC1);
//		cvFindHomography(perspectiveWrapedObj, resultRectangular, homography);
				//findHomography(perspectiveWrapedObj, resultRectangular);
		
		
		// ~ ==========================================================================
		
		CvMat distorted_src = cvCreateMat(recPoints.size(), 2, CV_32FC1);
		for(int s = 0; s < recPoints.size(); s++) {
			CvPoint2D32f p = recPoints.get(s);                 
			distorted_src.put(s, 0, p.x());
			distorted_src.put(s, 1, p.y());
		}
		
		CvMat undistort_dst = cvCreateMat(objPoints.size(), 2, CV_32FC1);
		for(int s = 0; s < objPoints.size(); s++){
			CvPoint2D32f p = objPoints.get(s);                         
			undistort_dst.put(s, 0, p.x());
			undistort_dst.put(s, 1, p.y());
		}
		
		
//		Mat drawtransform = getOptimalNewCameraMatrix(cameraMatrix, distCoeffs, size, 1.0, size * 2);
//		undistort(inputimage, undistorted, cameraMatrix, distCoeffs, drawtransform);
		
		
//		CvMat mat = cvCreateMat(3, 3, CV_32FC1);
//		
//		
//		cvUndistortPoints(src, dst, camera_matrix, dist_coeffs);
//		cvInitUndistortRectifyMap(undistort_dst, distorted_src, R, new_camera_matrix, mapx, mapy);
//		
//		cvFindHomography(distorted_src, undistort_dst, homography); 
//		
//		cvRemap(perspectiveWrapedObj, resultImage, homography, homography);
		//cvWarpPerspective(perspectiveWrapedObj, resultImage, homography, INTER_LINEAR, CvScalar.WHITE);
		
		// ~ ==========================================================================
		
//		List<CvPoint2D32f> points = new ArrayList<CvPoint2D32f>();
//		List<CvPoint2D32f> known = new ArrayList<CvPoint2D32f>();
//		// points and known should be filled with valid values
//		// here are just some ad-hoc numbers that do not result a singular (unsolvable) configuration 
//		for(int i = 0; i < 2; i++) {
//			points.add(cvPoint2D32f((double)i, 10 - 2 * (double)i));
//			known.add(cvPoint2D32f((double)i, 10 - 2 * (double)i));
//		}
//		
//		for(int i = 2; i < 5; i++) {
//			points.add(cvPoint2D32f((double)i,(double)i));
//		    known.add(cvPoint2D32f((double)i,(double)i));
//		}

		
		
		// ~ ======================================================================
		
//		CvMat matsrc = cvCreateMat(objPoints.size(), 2, CV_32FC1);
//		CvMat matdst = cvCreateMat(recPoints.size(), 2, CV_32FC1);
//
//		
//		// filling the matrices with the point coordinates
//		for(int s = 0; s < objPoints.size(); s++) {
//			CvPoint2D32f p = objPoints.get(s);//.get("Point");
//		    //Add this point to matsrc                         
//			matsrc.put(s, 0, p.x());
//		    matsrc.put(s, 1, p.y());
//		}
//
//		for(int s = 0; s < objPoints.size(); s++) {
//			CvPoint2D32f p = objPoints.get(s);//.get("Point");
//		    //Add this point to matdst                         
//		    matdst.put(s, 0, p.x());
//		    matdst.put(s, 1, p.y());
//		}

		
//		CvMat mat = cvCreateMat(3, 3, CV_32FC1);
//		cvFindHomography(matsrc, matdst, mat); //Here the matrices created are used to find the 3x3 Homography transform Matrix
//		
//		// displaying the resulting matrix
//		for( int i = 0; i < 3; ++i) {
//			for( int j = 0; j < 3; ++j) {
//				System.out.print(mat.get(i,j) + ",      ");
//			}
//			System.out.println();
//		}
//		System.out.println("----------------------------------------------");
//		
//		//warpPerspective(im_src, im_undistort, h, size);
//		cvWarpPerspective(perspectiveWrapedObj, resultImage, mat, INTER_LINEAR, CvScalar.WHITE);
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

	
	public List<CvPoint2D32f> findExtremePoints(IplImage src, int dimensionCount) {
		List<CvPoint2D32f> result = new ArrayList<CvPoint2D32f>();
		int height = src.height();
		int width = src.width();
		int devider = dimensionCount + 1;
		int x_step = width / devider;
		int y_step = height / devider;

		for (int i = 1; i < devider; i++) {
			int x = x_step * i;
			int y = y_step * i;
			List<CvPoint2D32f> verticalPoints = findVerticalExtremePoints(src, x);
			List<CvPoint2D32f> gorizontPoints = findGorizontalExtremePoints(src, y);
			if (verticalPoints != null && gorizontPoints!= null) {
				result.addAll(verticalPoints);
				result.addAll(gorizontPoints);
			}	
		}
		return result;
	}
	
	
	public List<CvPoint2D32f> findVerticalExtremePoints(IplImage src, int x) {
		List<CvPoint2D32f> allPoints = new ArrayList<CvPoint2D32f>();
		ByteBuffer bb_src 	= src.createBuffer();
		for(int y = 0; y < src.height(); y++) {
			int index = y * src.widthStep() + x * src.nChannels();
	    	int val = bb_src.get(index) & 0xFF;
		    if (val < 255) {
		    	allPoints.add(new CvPoint2D32f(x, y));
		    }
//		   	cvDrawCircle(grayRectan, new CvPoint(x, y), 2, CvScalar.BLACK, -1, 8, 0);
		}
		List<CvPoint2D32f> result = new ArrayList<CvPoint2D32f>();
		if (allPoints.size() >= 2) {
			result.add(allPoints.get(0));
			result.add(allPoints.get(allPoints.size()-1));
		}
		return result;
	}
	
	public List<CvPoint2D32f> findGorizontalExtremePoints(IplImage src, int y) {
		List<CvPoint2D32f> allPoints = new ArrayList<CvPoint2D32f>();
		ByteBuffer bb_src 	= src.createBuffer();
		for(int x = 0; x < src.width(); x++) {
			int index = y * src.widthStep() + x * src.nChannels();
	    	int val = bb_src.get(index) & 0xFF;
		    if (val < 255) {
		    	allPoints.add(new CvPoint2D32f(x, y));
		    }
//		   	cvDrawCircle(grayRectan, new CvPoint(x, y), 2, CvScalar.BLACK, -1, 8, 0);
		}
		List<CvPoint2D32f> result = new ArrayList<CvPoint2D32f>();
		if (allPoints.size() >= 2) {
			result.add(allPoints.get(0));
			result.add(allPoints.get(allPoints.size()-1));
		}
		return result;
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
	
//	public List<CvPoint> findExtremePoints(IplImage src, int min_x, int max_x, int min_y, int max_y) {
//		ByteBuffer bb_src 	= src.createBuffer();
//		List<CvPoint> points = new ArrayList<CvPoint>();
////
////		System.out.println(" min_y = " +  min_y);
////		System.out.println(" max_y = " +  max_y);
////		System.out.println(" min_x = " +  min_x);
////		System.out.println(" max_x = " +  max_x);
////		
//		for(int y = min_y; y < max_y; y++) {
//		    for(int x = min_x; x < max_x; x++) {
//		    	int index = y * src.widthStep() + x * src.nChannels();
//		    	cvSet2D(src, y, x, CvScalar.BLACK);
//		    	int val = bb_src.get(index) & 0xFF;
//			    if (val < 255) {
//			    	points.add(new CvPoint(x, y));
//			    }
//		    }
//		}
//		if (points.size() > 2) {
//			List<CvPoint> result = new ArrayList<CvPoint>();
//			result.add(points.get(0));
//			result.add(points.get(points.size()-1));	
//			return result;
//		} 
//		return null;
//	}
}
