package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
//import static org.bytedeco.javacpp.helper.opencv_core.cvDrawContours;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_core.CV_PI;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAnd;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvInRangeS;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_core.cvSet;
import static org.bytedeco.javacpp.opencv_core.cvSet2D;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_core.cvSplit;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;


public class ImageHandler {
	
	private IplImage origin;
	private IplImage r_plane;
	private IplImage g_plane;
	private IplImage b_plane;
	private IplImage rgb_plane;
	
	private IplImage contours;
	
	private static final int DEF_HEIGHT = 200;
	private static final int DEF_WIDTH  = 300;

	//private CanvasFrame testFrame;
	
	
	public ImageHandler() {
		this.r_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		this.g_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		this.b_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		this.rgb_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		//testFrame = new CanvasFrame("Test");
	}

	public void processImage(IplImage img) {
		this.origin		= cvCloneImage(img);
		this.r_plane 	= cvCreateImage( cvGetSize( img ), IPL_DEPTH_8U, 1 );
		this.g_plane 	= cvCreateImage( cvGetSize( img ), IPL_DEPTH_8U, 1 );
		this.b_plane 	= cvCreateImage( cvGetSize( img ), IPL_DEPTH_8U, 1 );
		this.rgb_plane 	= cvCreateImage( cvGetSize( img ), IPL_DEPTH_8U, 1 );
		cvSplit( origin, b_plane, g_plane, r_plane, null );	
		//cvReleaseImage(img);
		
	} 
	

	// ///////////////////////////////
	// //// -- image geters --  /////
	// //////////////////////////////
	
	public BufferedImage getOrigin (){
		return ImageHelper.getBufferedImage(origin);
	}
	
	
	public BufferedImage get_r_plane (int minTh, int maxTh){
		r_plane = threshold(r_plane, minTh, maxTh);
		return ImageHelper.getBufferedImage(r_plane);
	}
	
	public BufferedImage get_g_plane (int minTh, int maxTh){
		g_plane = threshold(g_plane, minTh, maxTh);
		return ImageHelper.getBufferedImage(g_plane);
	}
	
	public BufferedImage get_b_plane (int minTh, int maxTh){
		b_plane = threshold(b_plane, minTh, maxTh);
		return ImageHelper.getBufferedImage(b_plane);
	}
	
	public BufferedImage get_rgb_plane (){
		rgb_plane = fillingImage( rgb_plane, 0, 0, 0 );
		
		ByteBuffer r_img = r_plane.getByteBuffer();
		ByteBuffer g_img = g_plane.getByteBuffer();
		ByteBuffer b_img = b_plane.getByteBuffer();

		for(int y = 0; y < rgb_plane.height(); y++) {
		    for(int x = 0; x < rgb_plane.width(); x++) {
		        int index = y * rgb_plane.widthStep() + x * rgb_plane.nChannels();
		        int r_val = r_img.get(index) & 0xFF; // the 0xFF is needed to cast from an unsigned byte to an int.
		        int g_val = g_img.get(index) & 0xFF; // the 0xFF is needed to cast from an unsigned byte to an int.
		        int b_val = b_img.get(index) & 0xFF; // the 0xFF is needed to cast from an unsigned byte to an int.
		        
		        if ( (r_val > 0 && g_val > 0) && (g_val > 0 && b_val > 0) ) {
		        //if (r_val > 0 || g_val > 0 || b_val > 0) {
		        //if ( r_val > 0 && g_val > 0 && b_val > 0 ) {
		        	CvScalar white = CV_RGB(255, 255, 255);
		        	cvSet2D(rgb_plane, y, x, white );
		        }
		        
		    }
		}
		return ImageHelper.getBufferedImage(rgb_plane); 
	}
	
	
	
	
	
	
	public BufferedImage get_contour (){
		contours = findCountors(rgb_plane);
		BufferedImage result = ImageHelper.getBufferedImage(contours);
		//cvReleaseImage(contours);
		return result;
	}
	
	public BufferedImage getHoughLines () {
		
		IplImage result = contours.clone();
		IplImage grayImage = cvCreateImage( cvGetSize( contours ), IPL_DEPTH_8U, 1 );
		cvCvtColor(contours, grayImage, CV_RGB2GRAY);
		
		
		//IplImage grayImage = contours.clone();
		
		ByteBuffer img = grayImage.getByteBuffer();
		int y_max_1 = 0;
		int x_max_1 = 0;
		
		int y_max_2 = 0;
		int x_max_2 = 0;
		
		System.out.println("grayImage.height() = " + grayImage.height() );
		System.out.println("grayImage.width() = " + grayImage.width() );
		
		for(int y = 0; y < grayImage.height(); y++) {
		    for(int x = 0; x < grayImage.width(); x++) {
		        int index = y * grayImage.widthStep() + x * grayImage.nChannels();
		        int val = img.get(index);
		        if (val > 0) {
		        	if (y > y_max_1) {
		        		y_max_2 = y_max_1;
		        		x_max_2 = x_max_1;
		        		y_max_1 = y;
		        		x_max_1 = x;
		        	}
		        }
		    }
		}
		
//		cvLine(
//			grayImage, 
//			cvPoint(x_max_1, y_max_1),
//			cvPoint(x_max_2, y_max_2),
//			cvScalarAll(255)
//		);
//
		cvDrawLine(
				result, 
				new CvPoint(1180, 860),
				new CvPoint(100, 900),
				CvScalar.GREEN, 
				3, 
				-1, 
				0);
		
		System.out.println("y_max_1 = " + y_max_1);
		System.out.println("x_max_1 = " + x_max_1);
		
		System.out.println("y_max_2 = " + y_max_2);
		System.out.println("x_max_2 = " + x_max_2);
		
		return ImageHelper.getBufferedImage(result);
	}
	
	
	
	// ////////////////////////////////
	// //// -- image handlers --  /////
	// ////////////////////////////////
	public  IplImage getEmptyImage(int height, int width){
	    IplImage img = cvCreateImage(	// -- создвание пустой картинки для вывода результата 
	    	cvSize(height, width),		//  - размер изображения 
	    	8,							//  - глубина (битность на цвет) 
	    	3							//  - кол-во цветов
	    );
	    return fillingImage( img, 255, 255, 255 );
	}
	
	public IplImage fillingImage( IplImage img, int r, int g, int b){
	    CvScalar color = CV_RGB( r, g, b );	
	    cvSet( img , color );		
	    return img;
	}
	
	public IplImage threshold( IplImage img, int min, int max){
		IplImage resImg = cvCloneImage(img);
		cvInRangeS( 
			img, 			// - исходный массив
			cvScalar(min),	// - cкаляр с нижней границей (включая)
			cvScalar(max),	// - скаляр с верхней границей (не включая)
			resImg			// - массив для хранения результата (тип 8S или 8U)
		);
		cvReleaseImage(img);
	    return resImg;
	}
	
	public IplImage canny( IplImage image, int thMin, int thMax ){
		IplImage resImg = cvCloneImage(image);
		cvCanny( 
			image, 	// - одноканальное изображение для обработки (градации серого) 
			resImg,	// - одноканальное изображение для хранения границ, найденных функцией
			thMin,	// - порог минимума
			thMax,	// - порог максимума
			3		// - размер для оператора Собеля 
		);
		cvReleaseImage(image);
	    return resImg;
	}
		
		
		
	public IplImage findCountors( IplImage image ){
		
		CvSeq contours = new CvSeq();
		int contCount = cvFindContours(
			image,							// - исходное 8-битное одноканальное изображение 
											//   (ненулевые пиксели обрабатываются как 1, а нулевые — 0)
											//   для получения такого изображения из градаций серого можно, 
											// 	 использовать функции cvThreshold() или cvCanny()
			CvMemStorage.create(),			// - хранилище памяти для хранения данных найденных контуров
			contours,						// - указатель, который будет указывать на первый элемент последовательности,
											//   содержащей данные найденных контуров
			Loader.sizeof(CvContour.class),	//   размер заголовка элемента последовательности
			1,								// - режим поиска
											//   CV_RETR_EXTERNAL 0 // найти только крайние внешние контуры
											//   CV_RETR_LIST     1 // найти все контуры и разместить их списком
											//   CV_RETR_CCOMP    2 // найти все контуры и разместить их в виде 2-уровневой иерархии
											//   RETR_TREE        3 // найти все контуры и разместить их в иерархии вложенных контуров
			CV_CHAIN_APPROX_NONE			// - метод аппроксимации:
											//   CV_CHAIN_CODE          	0 // цепной код Фридмана
											//   CV_CHAIN_APPROX_NONE   	1 // все точки цепного кода переводятся в точки
											//   APPROX_SIMPLE      		2 // сжимает горизонтальные, вертикальные и диагональные сегменты и оставляет только их конечные точки
											//   CV_CHAIN_APPROX_TC89_L1    3 // применяется алгоритм
											//   CV_CHAIN_APPROX_TC89_KCOS 	4 // аппроксимации Teh-Chin
											//	 CV_LINK_RUNS				5 // алгоритм только для CV_RETR_LIST
		);

	

		CvSeq outerCount = new CvSeq();
		double maxArea = 0;

		if (contours != null && !contours.isNull()) {
			for(CvSeq cont = contours; cont != null; cont = cont.h_next()) {
				double area = cvContourArea(cont);
				if (area > maxArea) {
					outerCount = cont;
					maxArea = area;
				}
	            
			}
		}
		
		IplImage result = fillingImage(getEmptyImage(image.width(), image.height()), 255, 255, 255);
		cvDrawContours(result, outerCount, CV_RGB(255,0,0), CV_RGB(0,0,255), 0, 4, 8); // рисуем контур


		  
		//cvThreshold(result, grayImage, 150, 255, CV_THRESH_BINARY_INV);
		 
		 
	//	IplImage outPut = cvCreateImage( cvGetSize( result ), IPL_DEPTH_8U, 1 );
		
		//cvDrawContours(result, innerCount, CV_RGB(255,0,0), CV_RGB(0,255,0), 0, 3, 8); // рисуем контур
		
		
//		cvDrawContours( 			// — нарисовать заданные контуры
//				result,				// — изображение на котором будут нарисованы контуры
//				contours,		    // — указатель на первый контур
//				CV_RGB(255, 0,   0),// — цвет внешних контуров
//				CV_RGB(0,   255, 0),// — цвет внутренних контуров(отверстие)
//				2,					// - максимальный уровень для отображения контуров:
//									//   0 — только данный контур,
//									//   1 — данный и все следующие на данном уровне, 
//									//   2 — все следующие контуры и все контуры на следующем уровне и т.д. ) 
//									//   Если величина отрицательная, то будут нарисованы контуры на предыдущем уровне перед contour.
//				2,					// - толщина линии для отображения контуров 
//									//   Если величина отрицательная, то область заливается выбранным цветом 
//				8					//  — тип линии
//			);
				
			
		
//		IplImage result = getEmptyImage(image.width(), image.height());
//		
//		CvSeq current = new CvSeq();
//
//		if(contours != null && !contours.isNull()) {
//	
//            for (current = contours; current != null; current = current.h_next()) { 
//		
//		
//                Random rand = new Random();;
//                int min = 0;
//                int max = 255;
//                int rand1 = rand.nextInt((max - min) + 1) + min;
//                int rand2 = rand.nextInt((max - min) + 1) + min;
//                int rand3 = rand.nextInt((max - min) + 1) + min;
//                
//                cvDrawContours( 			// — нарисовать заданные контуры
//    					result,				// — изображение на котором будут нарисованы контуры
//    					current,		    // — указатель на первый контур
//    					CV_RGB(rand1,rand2,rand3),	// — цвет внешних контуров
//    					CV_RGB(255,0,0),	// — цвет внутренних контуров(отверстие)
//    					1,					// - максимальный уровень для отображения контуров:
//    										//   0 — только данный контур,
//    										//   1 — данный и все следующие на данном уровне, 
//    										//   2 — все следующие контуры и все контуры на следующем уровне и т.д. ) 
//    										//   Если величина отрицательная, то будут нарисованы контуры на предыдущем уровне перед contour.
//    					2,					// - толщина линии для отображения контуров 
//    										//   Если величина отрицательная, то область заливается выбранным цветом 
//    					8					//  — тип линии
//    				);
//            }
//        }    

//			if (biggestCount != null && !biggestCount.isNull()) {
//				cvDrawContours( 			// — нарисовать заданные контуры
//						result,				// — изображение на котором будут нарисованы контуры
//						biggestCount,		// — указатель на первый контур
//						CV_RGB(0,0,255),	// — цвет внешних контуров
//						CV_RGB(255,0,0),	// — цвет внутренних контуров(отверстие)
//						0,					// - максимальный уровень для отображения контуров:
//											//   0 — только данный контур,
//											//   1 — данный и все следующие на данном уровне, 
//											//   2 — все следующие контуры и все контуры на следующем уровне и т.д. ) 
//											//   Если величина отрицательная, то будут нарисованы контуры на предыдущем уровне перед contour.
//						2,					// - толщина линии для отображения контуров 
//											//   Если величина отрицательная, то область заливается выбранным цветом 
//						8					//  — тип линии
//					);
//			}

//		cvReleaseImage(image);
	    return result;
	}
}
