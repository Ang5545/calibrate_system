package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvDrawContours;
//import static org.bytedeco.javacpp.helper.opencv_core.cvDrawContours;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
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
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSize;
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
	private IplImage r_channel;
	private IplImage g_channel;
	private IplImage b_channel;
	private IplImage rgb_summ;
	private IplImage contours;
	private IplImage result;
	private CvSeq innerContour;
	private CvSeq outerContour;
	
	
	private static final int DEF_HEIGHT = 200;
	private static final int DEF_WIDTH  = 300;
	
	private int redMinTh;
	private int redMaxTh;
	private int greenMinTh;
	private int greenMaxTh;
	private int blueMinTh;
	private int blueMaxTh;
	
	
	
	public ImageHandler() {
		this.innerContour = new CvSeq();
		this.outerContour = new CvSeq();
	}

	public void processImage(IplImage img) {

		CvSize size =  new CvSize(img.width(), img.height());
		this.origin		= cvCloneImage(img);
		this.result		= cvCloneImage(img);
		this.r_channel 	= createImage(size, 1);
		this.g_channel 	= createImage(size, 1);
		this.b_channel 	= createImage(size, 1);
		//this.rgb_summ 	= createImage(size, 1);
		this.contours 	= createImage(size, 3);

		// -- channels handling -- 
		cvSplit( origin, b_channel, g_channel, r_channel, null );
		
		threshold(r_channel, redMinTh, redMaxTh);
		threshold(g_channel, greenMinTh, greenMaxTh);
		threshold(b_channel, blueMinTh, blueMaxTh);

		this.rgb_summ = sumChannels(r_channel, g_channel, b_channel);

		// -- contours handling -- 
		
		findContours(rgb_summ); 
		
		this.contours = drawContour(contours, innerContour, CvScalar.BLUE, 6);
		this.contours = drawContour(contours, outerContour, CvScalar.WHITE, 6);
	
		this.result = drawContour(result, innerContour, CvScalar.GREEN, 5);
		this.result = drawContour(result, outerContour, CvScalar.GREEN, 5);

		this.result = drawPoints(result, innerContour, outerContour);		
	} 

	public void setRedThresholdParameters(int minTh, int maxTh) {
		this.redMinTh = minTh;
		this.redMaxTh = maxTh;
	}
	
	public void setGreenThresholdParameters(int minTh, int maxTh) {
		this.greenMinTh = minTh;
		this.greenMaxTh = maxTh;
	}
	
	public void setBlueThresholdParameters(int minTh, int maxTh) {
		this.blueMinTh = minTh;
		this.blueMaxTh = maxTh;
	}
	
	// ///////////////////////////////
	// //// -- image geters --  /////
	// //////////////////////////////
	
	public BufferedImage getOrigin() {
		return ImageHelper.getBufferedImage(origin);
	}
	
	
	public BufferedImage getRedChannel() {
		return ImageHelper.getBufferedImage(r_channel);
	}
	
	public BufferedImage getGreenChannel() {
		return ImageHelper.getBufferedImage(g_channel);
	}
	
	public BufferedImage getBlueChannel() {
		return ImageHelper.getBufferedImage(b_channel);
	}
	
	public BufferedImage getRGBsumm() {
		return ImageHelper.getBufferedImage(rgb_summ);
	}
	
	public BufferedImage get_contour (){
		return ImageHelper.getBufferedImage(contours);
	}
	
	public BufferedImage getResultl() {
		return ImageHelper.getBufferedImage(result);
	}
	
	public IplImage drawContour(IplImage src, CvSeq countour, CvScalar color, int thiknes) {
		//IplImage result = cvCloneImage(src);
		cvDrawContours( 			// — нарисовать заданные контуры
				 src, 				// — изображение на котором будут нарисованы контуры
				 countour, 			// — указатель на первый контур
				 color,				// — цвет внешних контуров
				 color,				// — цвет внутренних контуров(отверстие)
				 0, 				// - максимальный уровень для отображения контуров:
				 					// 	 0 — только данный контур,
				 					// 	 1 — данный и все следующие на данном уровне, 
				 					//   2 — все следующие контуры и все контуры на следующем уровне и т.д. ) 
				 					//   Если величина отрицательная, то будут нарисованы контуры на предыдущем уровне перед contour.
				 thiknes,			// - толщина линии для отображения контуров 
				 					//   Если величина отрицательная, то область заливается выбранным цветом 
				 8 					// — тип линии
		);
		return src;
	}
	
	
	public void drawCircle(IplImage src, CvPoint center, CvScalar color, int radius) {
		 cvDrawCircle(				// — нарисовать круг
				 src, 				// — изображение на котором будет нарисован круг
				 center, 			// - центр круга
				 radius, 			// - радиус круга
				 color, 			// - цвет
				 -1, 				// - толщина (елси значение отрицательное круг заливается выбранным цветом)
				 10, 				// - тип линии
				 0					// - Количество дробных битов в координатах центра и в значении радиуса.
		);
	}
	
	public IplImage drawPoints(IplImage src, CvSeq innerContour, CvSeq outerContour) {
		if (innerContour.total() > 0 && outerContour.total() > 0) {
		
			CvPoint[] innerPoints = getPoints(innerContour);			
			CvPoint[] outerPoints = getPoints(outerContour);	
			
			if (innerPoints != null && outerPoints != null) {	
				System.out.println("Test");
				for (int i = 0; i < 4; i++) {
					CvPoint inP = innerPoints[i];
					CvPoint outP = outerPoints[i];
					cvDrawCircle(src, inP, 5, CvScalar.WHITE, -1, 8, 0);
					cvDrawCircle(src, outP, 5, CvScalar.WHITE, -1, 8, 0);
					cvLine(src, inP, outP, CvScalar.RED, 3, CV_AA, 0);	
				}
				
				CvPoint[] middlePoints = getMiddlePoints(outerPoints, innerPoints);
				for (int i = 0; i < 4; i++) {
					drawCircle(src, middlePoints[i], CvScalar.WHITE, 20);
				}
			}
		}
		return src;
	}

	public IplImage sumChannels(IplImage r_channel, IplImage g_channel, IplImage b_channel) {	
		
		IplImage img = cvCloneImage(r_channel);
		ByteBuffer bb_red 	= r_channel.createBuffer();
		ByteBuffer bb_green = g_channel.createBuffer();
		ByteBuffer bb_blue 	= b_channel.createBuffer();

		for(int y = 0; y < img.height(); y++) {
		    for(int x = 0; x < img.width(); x++) {

		        int index = y * img.widthStep() + x * img.nChannels();
		        int r_val = bb_red.get(index) & 0xFF; // the 0xFF is needed to cast from an unsigned byte to an int.
		        int g_val = bb_green.get(index) & 0xFF; 
		        int b_val = bb_blue.get(index) & 0xFF; 
		        
		        if ( (r_val > 0 && g_val > 0) || (g_val > 0 && b_val > 0) ||  (r_val > 0 && b_val > 0) ) {
		        	cvSet2D(img, y, x, CvScalar.WHITE );	        	
		        }
		        
		    }
		}
		return img;
	}
	

	private void findContours(IplImage img) {
		CvSeq allContours = new CvSeq();
		IplImage src = cvCloneImage(img);
		// -- используется CV_CHAIN_APPROX_NONE так как нужна площадь (по фримену не считает)
		cvFindContours(
				src,							// - исходное 8-битное одноканальное изображение 
												//   (ненулевые пиксели обрабатываются как 1, а нулевые — 0)
				CvMemStorage.create(),			// - хранилище памяти для хранения данных найденных контуров
				allContours,					// - указатель, который будет указывать на первый элемент последовательности,
												//   содержащей данные найденных контуров
				Loader.sizeof(CvContour.class),	//   размер заголовка элемента последовательности
				1,								// - режим поиска
												//   CV_RETR_EXTERNAL 0 // найти только крайние внешние контуры
												//   CV_RETR_LIST     1 // найти все контуры и разместить их списком
												//   CV_RETR_CCOMP    2 // найти все контуры и разместить их в виде 2-уровневой иерархии
												//   RETR_TREE        3 // найти все контуры и разместить их в иерархии вложенных контуров
				1								// - метод аппроксимации:
												//   CV_CHAIN_CODE          	0 // цепной код Фридмана
												//   CV_CHAIN_APPROX_NONE   	1 // все точки цепного кода переводятся в точки
												//   APPROX_SIMPLE      		2 // сжимает горизонтальные, вертикальные и диагональные сегменты и оставляет только их конечные точки
												//   CV_CHAIN_APPROX_TC89_L1    3 // применяется алгоритм
												//   CV_CHAIN_APPROX_TC89_KCOS 	4 // аппроксимации Teh-Chin
												//	 CV_LINK_RUNS				5 // алгоритм только для CV_RETR_LIST
		);
		
		double outArea = 0;
		double innArea = 0;
		
		if(allContours != null && !allContours.isNull()) {	
            for (CvSeq current = allContours; current != null; current = current.h_next()) {
		        double area = cvContourArea(current);
		        if (area > innArea && area < outArea) {
		        	innerContour = current;
		        	innArea = area;
		        } else if (area > innArea && area > outArea) {
		        	innerContour = outerContour;
		        	innArea = outArea;
		        	outerContour = current;
		        	outArea = area;
		        }
            }
		}
	}
	
	public IplImage threshold( IplImage img, int min, int max){
		IplImage src = cvCloneImage(img);
		cvInRangeS( 
			src, 			// - исходный массив
			cvScalar(min),	// - cкаляр с нижней границей (включая)
			cvScalar(max),	// - скаляр с верхней границей (не включая)
			img				// - массив для хранения результата (тип 8S или 8U)
		);
		cvReleaseImage(src);
		return img;
	}

	
	public CvPoint[] getPoints(CvSeq contour) {

		if (contour != null && !contour.isNull() && contour.total()>0) {
			CvSeq poly = cvApproxPoly(						// - ппроксимация контура(кривой) полигонами
					contour, 								// - исходная последовательность или массив точек
					Loader.sizeof(CvContour.class),			// - размер заголовка кривой(контура)
					CvMemStorage.create(), 					// - хранилище контуров. (Если NULL, то используется хранилище входной последовательности)
					CV_POLY_APPROX_DP, 						// - метод аппроксимации
															//   CV_POLY_APPROX_DP 		0 // Douglas-Peucker algorithm
					cvContourPerimeter(contour) * 0.1		// — параметр метода аппроксимации 
															//   (в случае CV_POLY_APPROX_DP — это желаемая точность )
					//4										// — параметр метода аппроксимаци (Не обязательынй)
															//   (Если используется последовательность, то параметр определяет должна ли аппроксимироваться 
															//   только одна последовательность или все последовательности этого уровня и ниже. 
															//   Если массив точек , то параметр определяет закрывается ли кривая
															//   (parameter2!=0) или нет (parameter2=0).
			);

			if (!poly.isNull() && poly.total() == 4) {
				CvPoint[] points = new CvPoint[4];
				for (int i = 0; i < 4; i++){
					CvPoint point = new CvPoint(cvGetSeqElem(poly, i));
					points[i] = (point);
				}
				return sortPoints(points);
				//return points;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static CvPoint[] sortPoints(CvPoint[] points) {
		CvPoint[] result = points;
		// - сортировка  по x - 
		for (int i = result.length-1 ; i > 0 ; i--) {  	// Внешний цикл каждый раз сокращает фрагмент массива, так как внутренний цикл каждый раз ставит в конец фрагмента максимальный элемент
			for (int j = 0 ; j < i ; j++) {
				if (result[j].x() > result[j+1].x()) {  // Сравниваем элементы попарно, если они имеют неправильный порядок, то меняем местами
					CvPoint tmp = result[j];
					result[j] = result[j+1];
					result[j+1] = tmp;
				}
	        }
	    }
		
		// сортировка по  y - 
		CvPoint p1 = result[0];
		CvPoint p2 = result[1];
		if (p1.y() > p2.y()) {
			result[0] = p2;
			result[1] = p1;
		}
		CvPoint p3 = result[2];
		CvPoint p4 = result[3];
		if (p3.y() < p3.y()) {
			result[2] = p4;
			result[3] = p3;
		}
		return result;
	}
	
	public static CvPoint[] getMiddlePoints(CvPoint[] points_1, CvPoint[] points_2) {
		CvPoint[] result = new CvPoint[4];
		for (int i = 0; i < 4; i++) {
			int max_x = 0;
			int min_x = 0;
			int max_y = 0;
			int min_y = 0;
			if (points_1[i].x() > points_2[i].x()) {
				max_x = points_1[i].x();
				min_x = points_2[i].x();
			} else {
				max_x = points_2[i].x();
				min_x = points_1[i].x();
			}
			if (points_1[i].y() > points_2[i].y()) {
				max_y = points_1[i].y();
				min_y = points_2[i].y();
			} else {
				max_y = points_2[i].y();
				min_y = points_1[i].y();
			}
			
			int middle_x = max_x - (max_x - min_x)/2;
			int middle_y = max_y - (max_y - min_y)/2;
			result[i] = new CvPoint(middle_x, middle_y);
		}
		return result;
	}
	
	public IplImage createImage(CvSize size, int channels) {
		IplImage img = cvCreateImage(size, IPL_DEPTH_8U, channels );
		cvSet(img, CvScalar.BLACK);
		return img;
	}
	
	public void release() {
		cvRelease(origin);
		cvRelease(r_channel);
		cvRelease(g_channel);
		cvRelease(b_channel);
		cvRelease(rgb_summ);
		cvRelease(contours);
		cvRelease(result);
	}
}
