package ru.ang5545.scanning_system_2.image_processing;

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

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class ImageHelper {

	
	
	public static BufferedImage getBufferedImage(IplImage src) {
	    OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
	    Java2DFrameConverter paintConverter = new Java2DFrameConverter();
	    Frame frame = grabberConverter.convert(src);
	    return paintConverter.getBufferedImage(frame,1);
	}
	
	public static  BufferedImage getEmptyImage(int height, int width){
//	    IplImage img = cvCreateImage(	// -- создвание пустой картинки для вывода результата 
//	    	cvSize(height, width),		//  - размер изображения 
//	    	8,							//  - глубина (битность на цвет) 
//	    	3							//  - кол-во цветов
//	    );
//		System.out.println(" -- create img --");
		
		IplImage img = cvCreateImage(cvSize(height, width), IPL_DEPTH_8U, 1); 
		IplImage whiteImg = fillingImage(img, 255, 255, 255);
		BufferedImage bufImg = getBufferedImage(whiteImg);
	    return bufImg;
	}
	
	public static IplImage fillingImage( IplImage img, int r, int g, int b){
	//    CvScalar color = CV_RGB( r, g, b );
		CvScalar scalar = new CvScalar();
        scalar.setVal(0, r);
        scalar.setVal(1, g);
        scalar.setVal(2, b);
	    cvSet( img , scalar );		
	    return img;
	}
}
