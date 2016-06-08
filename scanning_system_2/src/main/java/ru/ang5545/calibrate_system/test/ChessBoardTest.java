package ru.ang5545.calibrate_system.test;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import javax.swing.WindowConstants;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacpp.opencv_calib3d.*;
import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetPerspectiveTransform;
import static org.bytedeco.javacpp.opencv_imgproc.cvInitUndistortMap;
import static org.bytedeco.javacpp.opencv_imgproc.cvRemap;
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
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
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

import ru.ang5545.calibrate_system.utils.Path;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_calib3d.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class ChessBoardTest {

    public static void main( String[] args ) {
    	String path = Path.getAppPath() + "/resources/chessboard07.jpg";
    	
    	IplImage img = cvLoadImage(path, 3);
    	IplImage undistImg = cvCloneImage(img);
    	
    	OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
       
        int board_width = 6;
        int board_height = 4;
        int count = board_width * board_height;
        
        CvSize boardSize = new CvSize(board_width, board_height);
        CvPoint2D32f imageCorners = new CvPoint2D32f();
        
        int patternFound = cvFindChessboardCorners(img, boardSize, imageCorners);
        cvDrawChessboardCorners(img, boardSize, imageCorners, 0, patternFound);
        
        CanvasFrame canvas = new CanvasFrame("Test", 1);
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.showImage(converter.convert(img));
        
        CvMat objectPoints = CvMat.create(count,3);
        int idx = 0;
		for(int i = boardSize.height()-1; i>=0; i--){
			for(int j=0; j < boardSize.width(); j++){
				//objectPoints.position(f);
				objectPoints.put(idx, 0,(double)(j));
				objectPoints.put(idx, 1,(double)(i));
				objectPoints.put(idx, 2,(double)(0));
				idx++;
			}
		}
		
		CvSize resolution =  new CvSize(img.width(), img.height());
		
		CvMat cameraMatrix = CvMat.create(3, 3);
		CvMat distCoeffs = CvMat.create(5, 1);
	
		
		CvMat imagePoints = CvMat.create(count, 2);
		for(int p = 0; p < count; p++) {
			imagePoints.put(p, 0, imageCorners.position(p).x());
			imagePoints.put(p, 1, imageCorners.position(p).y());
		}
		
		CvMat pointCount = cvCreateMat(1, 1, CV_32SC1);
		pointCount.put(0, count);
		
		double error = cvCalibrateCamera2(objectPoints, imagePoints, pointCount, resolution, cameraMatrix, distCoeffs);
		
		
		CvMat mapx = CvMat.create(resolution.height(), resolution.width(), CV_32FC1);
		CvMat mapy = CvMat.create(resolution.height(), resolution.width(), CV_32FC1);
		
		
		
		
		cvInitUndistortMap(cameraMatrix, distCoeffs, mapx, mapy);
		cvRemap(undistImg, undistImg, mapx, mapy, CV_INTER_LINEAR, CvScalar.ZERO);
		
		
        CanvasFrame resCanv = new CanvasFrame("Result", 1);
        resCanv.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        resCanv.showImage(converter.convert(undistImg));
		
		
		System.out.println("error = " + error);
        System.out.println("patternFound = " + patternFound);
        
    }
}
