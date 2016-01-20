package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.helper.opencv_core.cvDrawContours;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAnd;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvInRangeS;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_core.cvSet;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_core.cvSplit;
import static org.bytedeco.javacpp.opencv_core.cvarrToMat;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_EXTERNAL;
import static org.bytedeco.javacpp.opencv_imgproc.cornerHarris;
import static org.bytedeco.javacpp.opencv_imgproc.cvCanny;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui.CvCapture;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;

public class Grabber {

	private static final int DEF_HEIGHT = 200;
	private static final int DEF_WIDTH  = 300;
	
	private static final int CAM_INDEX = 1;
	private CvCapture capture;	
	
	private IplImage grabbedImg;
	private IplImage r_plane;
	private IplImage g_plane;
	private IplImage b_plane;
	private IplImage rgb_plane;
	
	//private IplImage img_d;
	
	private CanvasFrame testFrame;
	private CanvasFrame testFrameCanny;
	
	public Grabber(){
		this.capture 	= opencv_highgui.cvCreateCameraCapture( CAM_INDEX );
		this.grabbedImg = getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		this.r_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		this.g_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		this.b_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		this.rgb_plane 	= getEmptyImage( DEF_HEIGHT, DEF_WIDTH );
		
//		testFrame = new CanvasFrame("Test");
//		testFrameCanny = new CanvasFrame("Tes Canny");
		
	}
	
	public void snapShoot(){
		this.grabbedImg = opencv_highgui.cvQueryFrame( capture );
		this.r_plane 	= cvCreateImage( cvGetSize( grabbedImg ), IPL_DEPTH_8U, 1 );
		this.g_plane 	= cvCreateImage( cvGetSize( grabbedImg ), IPL_DEPTH_8U, 1 );
		this.b_plane 	= cvCreateImage( cvGetSize( grabbedImg ), IPL_DEPTH_8U, 1 );
		this.rgb_plane 	= cvCreateImage( cvGetSize( grabbedImg ), IPL_DEPTH_8U, 1 );
		cvSplit( grabbedImg, b_plane, g_plane, r_plane, null );	
	} 
	
	public void stopGrub(){
		opencv_highgui.cvReleaseCapture(capture);
	}
	
	// ///////////////////////////////
	// //// -- image geters --  /////
	// //////////////////////////////
	public BufferedImage getImage() {
		return grabbedImg.getBufferedImage();
	}
	
	public BufferedImage get_r_plane (int minTh, int maxTh){
		r_plane = threshold(r_plane, minTh, maxTh);
		return r_plane.getBufferedImage();
	}
	
	public BufferedImage get_g_plane (int minTh, int maxTh){
		g_plane = threshold(g_plane, minTh, maxTh);
		return g_plane.getBufferedImage();
	}
	
	public BufferedImage get_b_plane (int minTh, int maxTh){
		b_plane = threshold(b_plane, minTh, maxTh);
		return b_plane.getBufferedImage();
	}
	
	public BufferedImage get_rgb_plane (){
		rgb_plane = fillingImage( rgb_plane, 0, 0, 0 );
		cvAnd(r_plane, g_plane, rgb_plane);
		cvAnd(rgb_plane, b_plane, rgb_plane);
		return rgb_plane.getBufferedImage();
	}
	
	public BufferedImage get_Canny_rgb (int min, int max){
		IplImage canny = canny(rgb_plane, min, max);
		//testFrameCanny.showImage(canny);
		
		IplImage countors = findCountors(canny);
		//testFrame.showImage(countors);
		
		//IplImage cerners = detectCorners(canny);
		
		BufferedImage result = countors.getBufferedImage();
		cvReleaseImage(canny);
		cvReleaseImage(countors);
		return result;
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
		
//	    IplImage grayImage    = IplImage.create(grabbedImg.width(), grabbedImg.height(), IPL_DEPTH_8U, 1);
//	    cvCvtColor(grabbedImg, grayImage, CV_RGB2GRAY);
	    
	    
//	    IplImage grayImage    = IplImage.create(image.width(), image.height(), IPL_DEPTH_8U, 1);
//	    cvCvtColor(image, grayImage, CV_RGB2GRAY);
	    
	    
//	    cvThreshold(grayImage, grayImage, 127, 255, CV_THRESH_BINARY);
		
		CvSeq contours = new CvSeq();
	    //CvMemStorage memory = CvMemStorage.create();
	    
		int contCount = cvFindContours(
			image,							// - исходное 8-битное одноканальное изображение 
											//   (ненулевые пиксели обрабатываются как 1, а нулевые — 0)
											//   для получения такого изображения из градаций серого можно, 
											// 	 использовать функции cvThreshold() или cvCanny()
			CvMemStorage.create(),			// - хранилище памяти для хранения данных найденных контуров
			contours,						// - указатель, который будет указывать на первый элемент последовательности,
											//   содержащей данные найденных контуров
			Loader.sizeof(CvContour.class),	//   размер заголовка элемента последовательности
			CV_RETR_EXTERNAL,				// - режим поиска
											//   CV_RETR_EXTERNAL 0 // найти только крайние внешние контуры
											//   CV_RETR_LIST     1 // найти все контуры и разместить их списком
											//   CV_RETR_CCOMP    2 // найти все контуры и разместить их в виде 2-уровневой иерархии
											//   RETR_TREE        3 // найти все контуры и разместить их в иерархии вложенных контуров
			CV_CHAIN_APPROX_SIMPLE			// - метод аппроксимации:
											//   CV_CHAIN_CODE          	0 // цепной код Фридмана
											//   CV_CHAIN_APPROX_NONE   	1 // все точки цепного кода переводятся в точки
											//   APPROX_SIMPLE      		2 // сжимает горизонтальные, вертикальные и диагональные сегменты и оставляет только их конечные точки
											//   CV_CHAIN_APPROX_TC89_L1    3 // применяется алгоритм
											//   CV_CHAIN_APPROX_TC89_KCOS 	4 // аппроксимации Teh-Chin
											//	 CV_LINK_RUNS				5 // алгоритм только для CV_RETR_LIST
		);
		
		System.out.println("contCount = "+contCount);
		
		IplImage result = getEmptyImage(grabbedImg.width(), grabbedImg.height());
		
		CvSeq current = new CvSeq();
		CvSeq biggestCount = new CvSeq();
		double maxPerim = 0;
		double maxArea = 0;
		
		if(contours != null && !contours.isNull()) {
            for (current = contours; current != null; current = current.h_next()) { 
            	double perim = cvContourPerimeter(current);
            	double area = cvContourArea(current);
            	if (area > maxArea) {
            		biggestCount = current;
            		maxArea = area;
            	}
                //cvDrawContours( resultImage, ptr, CvScalar.BLUE, CvScalar.RED, -1, 3, 8, cvPoint(0,0) );
            }
        }    
		
		if(contours != null && !contours.isNull()) {
            for (current = contours; current != null; current = current.h_next()) { 
            	double perim = cvContourPerimeter(current);
            	double area = cvContourArea(current);
            	if (area > maxArea) {
            		biggestCount = current;
            		maxArea = area;
            	}
                //cvDrawContours( resultImage, ptr, CvScalar.BLUE, CvScalar.RED, -1, 3, 8, cvPoint(0,0) );
            }
        } 
		
		System.out.println("maxPerim = "+maxPerim);
		
		if (biggestCount != null && !biggestCount.isNull()) {
			cvDrawContours( 			// — нарисовать заданные контуры
					result,				// — изображение на котором будут нарисованы контуры
					biggestCount,		// — указатель на первый контур
					CV_RGB(0,0,255),	// — цвет внешних контуров
					CV_RGB(255,0,0),	// — цвет внутренних контуров(отверстие)
					1,					// - максимальный уровень для отображения контуров:
										//   0 — только данный контур,
										//   1 — данный и все следующие на данном уровне, 
										//   2 — все следующие контуры и все контуры на следующем уровне и т.д. ) 
										//   Если величина отрицательная, то будут нарисованы контуры на предыдущем уровне перед contour.
					2,					// - толщина линии для отображения контуров 
										//   Если величина отрицательная, то область заливается выбранным цветом 
					8					//  — тип линии
				);
		}

		cvReleaseImage(image);
	    return result;
	}
}