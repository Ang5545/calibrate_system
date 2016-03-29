//package ru.ang5545.scanning_system_2.cam_acces;
//
//import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
//
//import java.awt.List;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.bytedeco.javacpp.opencv_highgui;
//import org.bytedeco.javacpp.opencv_core.IplImage;
//
//public class ImageLoaderForTest {
//
//	private final static String PATH 		= "/Users/fedormurashko/Develop/Java/git/scanning_system_2/scanning_system_2/testImages";
//	
//	private ArrayList<String> files; 
//	private int imgIndex;
//	private IplImage img;
//	
//	public ImageLoaderForTest() {
//		this.imgIndex 	= 0;
//		this.files 		= getFileNames(PATH);
//	}
//	
//	public void snapShoot() {
//		this.imgIndex 	= imgIndex < files.size() ? imgIndex : 0;
//		this.img 		= opencv_highgui.cvLoadImage(PATH + "/" + files.get(imgIndex));
//		this.imgIndex ++;
//	}
//	
//	public IplImage getGrabedImage() {
//		return img;
//	}
//	
//	public void stopGrub(){
//		cvReleaseImage(img);
//	}
//	
//	private ArrayList<String> getFileNames(String importDirectory){	
//		ArrayList<String> result = new ArrayList<String>();
//		File directory = new File( importDirectory );      
//		File [] fileList = directory.listFiles();
//		for(int i = 0; i < fileList.length; i++) {
//			if(fileList[i].isFile()){
//				String fileName = fileList[i].getName();
//				result.add(fileName);
//			}	
//		}
//		return result;
//	}
//	
//}
