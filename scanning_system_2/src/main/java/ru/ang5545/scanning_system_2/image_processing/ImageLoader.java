package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.cvCanny;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.awt.Graphics2D;
import java.awt.List;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;


import javax.swing.JFrame;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;


public class ImageLoader {

	private static final String DIR_PATH 	= "/home/fedor-m/Рабочий стол/1/3/";
	private static final String IMG_FORMAT 	= ".JPG";
	private int i;
	
	private IplImage img;
	private IplImage img_d;
	private IplImage r_pl;
	private IplImage g_pl;
	private IplImage b_pl;
	private IplImage rgb;
	
	public ImageLoader(){
		this.i	 	= 1;
		this.img 	= cvLoadImage(DIR_PATH + i +IMG_FORMAT);
		this.img_d	= cvCreateImage( cvGetSize(img), IPL_DEPTH_8U, 1 );
		this.r_pl	= cvCreateImage( cvGetSize(img), IPL_DEPTH_8U, 1 );
		this.g_pl	= cvCreateImage( cvGetSize(img), IPL_DEPTH_8U, 1 );
		this.b_pl	= cvCreateImage( cvGetSize(img), IPL_DEPTH_8U, 1 );
		this.rgb	= cvCreateImage( cvGetSize(img), IPL_DEPTH_8U, 1 );
	}
	
	
	private void getAllImgArea(){
		
	}
	
	public void loadImage(){
		img = cvLoadImage(DIR_PATH + i +IMG_FORMAT);
		cvSplit(img, r_pl, g_pl, b_pl, null);		
		if (i < 5) 
			i ++;
		else
			i = 1;
	}
	
//	public static BufferedImage IplImageToBufferedImage(IplImage src) {
//	    OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
//	    Java2DFrameConverter paintConverter = new Java2DFrameConverter();
//	    Frame frame = grabberConverter.convert(src);
//	    return paintConverter.getBufferedImage(frame,1);
//	}
//	
//	
//	public BufferedImage getImage(){
//		return IplImageToBufferedImage(img);
//	}
//	
//	public BufferedImage get_r_plImage (int minTh, int maxTh){
//		r_pl = threshold(r_pl, minTh, maxTh);
//		return IplImageToBufferedImage(r_pl);
//	}
//	
//	public BufferedImage get_g_plImage (int minTh, int maxTh){
//		g_pl = threshold(g_pl, minTh, maxTh);
//		return IplImageToBufferedImage(g_pl);
//	}
//	
//	public BufferedImage get_b_plImage (int minTh, int maxTh){
//		b_pl = threshold(b_pl, minTh, maxTh);
//		return IplImageToBufferedImage(b_pl);
//	}
//	
//	public BufferedImage get_RGB_image (){
//		rgb = fillingImage( rgb, 255, 255, 255 );
//		cvAnd(r_pl, g_pl, rgb);
//		cvAnd(rgb, b_pl, rgb);
//		return IplImageToBufferedImage(rgb);
//	}
//	
//	public BufferedImage get_Canny_rgb (int min, int max){
//		IplImage canny = canny(rgb, min, max);
//		IplImage countors = findCountors(canny);
//		IplImage cerners = detectCorners(canny);
//		return IplImageToBufferedImage(countors);
//	}
//	
//	public  BufferedImage getEmptyImage(){
//	    IplImage img = cvCreateImage(	// -- создвание пустой картинки для вывода результата 
//	    		cvSize(200, 300),		//  - размер изображения 
//	    		8,						//  - глубина (битность на цвет) 
//	    		3						//  - кол-во цветов
//	    );
//	    return IplImageToBufferedImage(fillingImage( img, 255, 255, 255 ));
//	}
//	
//	public IplImage fillingImage( IplImage img, int r, int g, int b){
//	    CvScalar color = CV_RGB( r, g, b );	
//	    cvSet( img , color );		
//	    return img;
//	}
//	
//	public IplImage threshold( IplImage img, int min, int max){
//		IplImage resImg = cvCloneImage(img);
//		cvInRangeS( 
//			img, 			// - исходный массив
//			cvScalar(min),	// - cкаляр с нижней границей (включая)
//			cvScalar(max),	// - скаляр с верхней границей (не включая)
//			resImg			// - массив для хранения результата (тип 8S или 8U)
//		);
//	    return resImg;
//	}
//	
//	
//	public IplImage canny( IplImage image, int thMin, int thMax ){
//		
//		IplImage resImg = cvCloneImage(image);
//		cvCanny( 
//			image, 	// - одноканальное изображение для обработки (градации серого) 
//			resImg,	// - одноканальное изображение для хранения границ, найденных функцией
//			thMin,	// - порог минимума
//			thMax,	// - порог максимума
//			3		// - размер для оператора Собеля 
//		);
//	    return resImg;
//	}
//	
//	public IplImage findCountors( IplImage image ){
//		IplImage resImg = cvCloneImage(image);
//		resImg = fillingImage( resImg, 255, 255, 255);
//		
//	    CvSeq contours = new CvSeq();
//	    
//		cvFindContours(
//			image,							// - исходное 8-битное одноканальное изображение 
//											//   (ненулевые пиксели обрабатываются как 1, а нулевые — 0)
//											//   для получения такого изображения из градаций серого можно, 
//											// 	 использовать функции cvThreshold() или cvCanny()
//			CvMemStorage.create(),			// - хранилище памяти для хранения данных найденных контуров
//			contours,						// - указатель, который будет указывать на первый элемент последовательности,
//											//   содержащей данные найденных контуров
//			Loader.sizeof(CvContour.class),	//   размер заголовка элемента последовательности
//			CV_RETR_EXTERNAL,				// - режим поиска
//											//   CV_RETR_EXTERNAL 0 // найти только крайние внешние контуры
//											//   CV_RETR_LIST     1 // найти все контуры и разместить их списком
//											//   CV_RETR_CCOMP    2 // найти все контуры и разместить их в виде 2-уровневой иерархии
//											//   RETR_TREE        3 // найти все контуры и разместить их в иерархии вложенных контуров
//			CV_CHAIN_APPROX_SIMPLE			// - метод аппроксимации:
//											//   CV_CHAIN_CODE          	0 // цепной код Фридмана
//											//   CV_CHAIN_APPROX_NONE   	1 // все точки цепного кода переводятся в точки
//											//   APPROX_SIMPLE      		2 // сжимает горизонтальные, вертикальные и диагональные сегменты и оставляет только их конечные точки
//											//   CV_CHAIN_APPROX_TC89_L1    3 // применяется алгоритм
//											//   CV_CHAIN_APPROX_TC89_KCOS 	4 // аппроксимации Teh-Chin
//											//	 CV_LINK_RUNS				5 // алгоритм только для CV_RETR_LIST
//		);
//
//		// - добавление всех найденных контуров в массив и поиск наибольшего -
//		
//		ArrayList<Map<String, Object>> rects = new ArrayList<Map<String, Object>>();
//		CvSeq zw = new CvSeq();
//	    double maxArcLength = 0; 
//	    
//	    if(contours != null) {
//	        for (zw = contours; zw != null; zw = zw.h_next()) {
//	        	cvDrawContours(resImg, zw, CV_RGB(255,0,0), CV_RGB(0,0,0), 0, 1, 8);
////	        	double arcLength = cvArcLength(zw);
////	            if ( arcLength > maxArcLength ){
////	            	maxArcLength = arcLength;
////	            }
////	            Map<String, Object> mp = new HashMap<String, Object>();
////	            mp.put("arcLength", arcLength);
////	            mp.put("contour", 	zw);
////	            rects.add(mp);
//	        }
//	    }
////	        boolean draw = true;
////	        int i = 0;
////	        while( draw ){
////	        	Map<String, Object> mp = rects.get(i);
////	        	
////	        	if ( (maxArcLength == (Double) mp.get("arcLength")) ){
////	        		cvDrawContours(resImg, (CvSeq) mp.get("contour"), CV_RGB(255,0,0), CV_RGB(0,0,0), 0, 1, 8);
////	        		draw = false;
////	        	}
////	        	if (i < rects.size())
////	        		draw = false;
////	        }
////	        
//////	        for ( int i = 0; i < rects.size(); i++ ){
//////	        	Map<String, Object> mp = rects.get(i);
//////	        	System.out.println("maxArcLength = "+maxArcLength+"; arcLength = "+(Double) mp.get("arcLength")
//////	        		+"; compare =  " + (maxArcLength == (Double) mp.get("arcLength")));
//////	        }
////	    }
//	    
//	    return resImg;
//	}
//
//	public IplImage detectCorners( IplImage image ){
//		IplImage resImg = cvCloneImage(image);
//		
//		/// Detector parameters
//		int blockSize = 2;
//		int apertureSize = 3;
//		double k = 0.04;
//		
//		Mat src_gray = new Mat();
//		Mat imgMat = cvarrToMat(img); 
//		
//		cornerHarris(imgMat, src_gray, blockSize, apertureSize, k);
//		
////		for( int j = 0; j < src_gray.rows() ; j++ ){ 
////			for( int i = 0; i < src_gray.cols(); i++ ){
////				if( (int) src_gray.
////						
////						at<float>(j,i) > thresh )
////		              {
////		               circle( src_gray, Point( i, j ), 5,  Scalar(0), 2, 8, 0 );
////		              }
////		          }
////		     }
//		return resImg;
//	}
	
}
