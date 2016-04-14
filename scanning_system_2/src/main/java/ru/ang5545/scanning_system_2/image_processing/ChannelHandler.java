package ru.ang5545.scanning_system_2.image_processing;

import static org.bytedeco.javacpp.opencv_core.cvCloneImage;
import static org.bytedeco.javacpp.opencv_core.cvInRangeS;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_core.cvSet;
import static org.bytedeco.javacpp.opencv_core.cvSet2D;
import static org.bytedeco.javacpp.opencv_core.cvSplit;

import java.nio.ByteBuffer;

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;

import ru.ang5545.model.ThresholdParameters;

public class ChannelHandler {
	
	private IplImage redChannel;
	private IplImage greenChannel;
	private IplImage blueChannel;
	private IplImage rgbSumm;
	
	public ChannelHandler(CvSize size) {
		this.redChannel 	= ImageHelper.createImage(size, 1);
		this.greenChannel 	= ImageHelper.createImage(size, 1);
		this.blueChannel 	= ImageHelper.createImage(size, 1);
		this.rgbSumm 		= ImageHelper.createImage(size, 1);
	}
	
	
	public void processImg(IplImage img, ThresholdParameters redThPar, ThresholdParameters greenThPar, ThresholdParameters blueThPar) {
		cvSet(redChannel, 	CvScalar.BLACK);
		cvSet(greenChannel, CvScalar.BLACK);
		cvSet(blueChannel, 	CvScalar.BLACK);
		cvSet(rgbSumm, 		CvScalar.BLACK);
		
		CvSize size =  new CvSize(img.width(), img.height());
		IplImage red 	= ImageHelper.createImage(size, 1); 
		IplImage green 	= ImageHelper.createImage(size, 1); 
		IplImage blue 	= ImageHelper.createImage(size, 1); 
		
		cvSplit( img, blue, green, red, null );
		
		this.redChannel 	= threshold(red, redThPar.getMin(), redThPar.getMax());
		this.greenChannel 	= threshold(green, greenThPar.getMin(), greenThPar.getMax());
		this.blueChannel 	= threshold(blue, blueThPar.getMin(), blueThPar.getMax());
		
		this.rgbSumm = sumChannels(redChannel, greenChannel, blueChannel);
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
	
	public IplImage sumChannels(IplImage r_channel, IplImage g_channel, IplImage b_channel) {	
		
		IplImage img = cvCloneImage(r_channel);
		cvSet(img, CvScalar.BLACK);
		
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
	
	public IplImage getRedChannel() {
		return redChannel;
	}

	public IplImage getGreenChannel() {
		return greenChannel;
	}

	public IplImage getBlueChannel() {
		return blueChannel;
	}

	public IplImage getRgbSumm() {
		return rgbSumm;
	}
}
