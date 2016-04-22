package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.opencv_core.*;

import org.bytedeco.*;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CameraDevice.Settings;
import org.bytedeco.javacv.CameraSettings;
import org.bytedeco.javacv.GeometricCalibrator;
import org.bytedeco.javacv.MarkedPlane;
import org.bytedeco.javacv.Marker;
import org.bytedeco.javacv.MarkerDetector;
import org.bytedeco.javacv.ProCamGeometricCalibrator;
import org.bytedeco.javacv.ProjectorSettings;

import ru.ang5545.model.ThresholdParameters;

public class Calibration {

	private IplImage origin;
	
	private Marker[][] markers; 
	private MarkedPlane boardPlane; 
	private CameraSettings cameraSettings = null; 
	private ProjectorSettings projectorSettings = null; 
	private Marker.ArraySettings markerSettings = null; 
	private  MarkerDetector.Settings markerDetectorSettings = null; 
//	private  GeometricSettings geometricCalibratorSettings = null; 
//	private  ColorSettings colorCalibratorSettings = null; 
 
//    CameraDevice[] cameraDevices = null; 
//    CanvasFrame[] cameraCanvasFrames = null; 
//    FrameGrabber[] frameGrabbers = null; 
//    ProjectorDevice[] projectorDevices = null; 
//    CanvasFrame[] projectorCanvasFrames = null; 
//    MarkedPlane[] projectorPlanes = null; 
//    OpenCVFrameConverter.ToIplImage[] cameraFrameConverters = null; 
//    OpenCVFrameConverter.ToIplImage[] projectorFrameConverters = null; 
// 
//    ProCamGeometricCalibrator[] proCamGeometricCalibrators = null; 
//    GeometricCalibrator[] geometricCalibrators = null; 
// 
//    ProCamColorCalibrator[][] proCamColorCalibrators = null; 
    
	public void processImage(IplImage img, ThresholdParameters redThPar, ThresholdParameters greenThPar, ThresholdParameters blueThPar) {
		this.origin	= cvCloneImage(img);
		

	}
}
