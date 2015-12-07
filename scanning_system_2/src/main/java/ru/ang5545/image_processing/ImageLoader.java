package ru.ang5545.image_processing;

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;

public class ImageLoader {

	private static final String DIR_PATH 	= "/home/fedor-m/Рабочий стол/1/2/";
	private static final String IMG_FORMAT 	= ".jpeg";
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
	
	
	public void loadImage(){
		img = cvLoadImage(DIR_PATH + i +IMG_FORMAT);
		cvSplit(img, r_pl, g_pl, b_pl, null);		
		if (i < 5) 
			i ++;
		else
			i = 1;
	}
	
	
	public BufferedImage getImage(){
		return img.getBufferedImage();
	}
	
	public BufferedImage get_r_plImage (int minTh, int maxTh){
		r_pl = threshold(r_pl, minTh, maxTh);
		return r_pl.getBufferedImage();
	}
	
	public BufferedImage get_g_plImage (int minTh, int maxTh){
		g_pl = threshold(g_pl, minTh, maxTh);
		return g_pl.getBufferedImage();
	}
	
	public BufferedImage get_b_plImage (int minTh, int maxTh){
		b_pl = threshold(b_pl, minTh, maxTh);
		return b_pl.getBufferedImage();
	}
	
	public BufferedImage get_RGB_image (){
		rgb = fillingImage( rgb, 255, 255, 255 );
		cvAnd(r_pl, g_pl, rgb);
		cvAnd(rgb, b_pl, rgb);
		return rgb.getBufferedImage();
	}
	
	public  BufferedImage getEmptyImage(){
	    IplImage img = cvCreateImage(	// -- создвание пустой картинки для вывода результата 
	    		cvSize(200, 300),		//  - размер изображения 
	    		8,						//  - глубина (битность на цвет) 
	    		3						//  - кол-во цветов
	    );
	    return fillingImage( img, 255, 255, 255 ).getBufferedImage();
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
	    return resImg;
	}
	
}
