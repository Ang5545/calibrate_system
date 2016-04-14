package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvDrawContours;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvRelease;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_POLY_APPROX_DP;
import static org.bytedeco.javacpp.opencv_imgproc.cvApproxPoly;
import static org.bytedeco.javacpp.opencv_imgproc.cvContourArea;
import static org.bytedeco.javacpp.opencv_imgproc.cvContourPerimeter;
import static org.bytedeco.javacpp.opencv_imgproc.cvDrawCircle;
import static org.bytedeco.javacpp.opencv_imgproc.cvLine;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvReleaseFunc;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;

public class ContourHandler {

	private CvSeq innerContour;
	private CvSeq outerContour;

	
	public ContourHandler() {
		this.innerContour = new CvSeq();
		this.outerContour = new CvSeq();	
	}
	
	public void processImage(IplImage img) {
		CvSeq allContours = findContours(img);
		
		double outArea = 0;
		double innArea = 0;
		
		if(allContours != null && !allContours.isNull()) {	
            for (CvSeq current = allContours; current != null; current = current.h_next()) {
		        double area = cvContourArea(current);
		        if (area > innArea && area < outArea) {
		        	this.innerContour = current;
		        	innArea = area;
		        } else if (area > innArea && area > outArea) {
		        	this.innerContour = outerContour;
		        	innArea = outArea;
		        	this.outerContour = current;
		        	outArea = area;
		        }
            }
		}
	}
	
	public IplImage drawContours(IplImage img, CvScalar innColor, CvScalar outColor, int thiknes) {
		IplImage src = cvCloneImage(img);
		drawContour(src, innerContour, innColor, thiknes);
		drawContour(src, outerContour, outColor, thiknes);
		//cvRelease(img);
		return src;
	}
	
	private IplImage drawContour(IplImage src, CvSeq countour, CvScalar color, int thiknes) {
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
	
	private CvSeq findContours(IplImage img) {
		CvSeq contours = new CvSeq();
		cvFindContours(
				img,							// - исходное 8-битное одноканальное изображение 
												//   (ненулевые пиксели обрабатываются как 1, а нулевые — 0)
				CvMemStorage.create(),			// - хранилище памяти для хранения данных найденных контуров
				contours,						// - указатель, который будет указывать на первый элемент последовательности,
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
												//   (используется CV_CHAIN_APPROX_NONE так как нужна площадь (по фримену не считает))
		);
		return contours;
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
	
	public IplImage drawPoints(IplImage src) {
		if (innerContour.total() > 0 && outerContour.total() > 0) {
		
			CvPoint[] innerPoints= getPoints(innerContour);			
			CvPoint[] outerPoints = getPoints(outerContour);	
			
			if (innerPoints != null && outerPoints != null) {	
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
}


