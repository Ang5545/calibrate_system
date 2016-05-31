package ru.ang5545.calibrate_system.image_processing;

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

import ru.ang5545.calibrate_system.model.ThresholdParameters;

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

		cvSplit( img, blueChannel, greenChannel, redChannel, null );
		
		threshold(blueChannel, blueThPar.getMin(), blueThPar.getMax());
		threshold(greenChannel, greenThPar.getMin(), greenThPar.getMax());
		threshold(redChannel, redThPar.getMin(), redThPar.getMax());

		sumChannels(rgbSumm, redChannel, greenChannel, blueChannel);
	}
	
	public void threshold( IplImage img, int min, int max){
		IplImage src = cvCloneImage(img);
		cvInRangeS( 
			src, 			// - исходный массив
			cvScalar(min),	// - cкаляр с нижней границей (включая)
			cvScalar(max),	// - скаляр с верхней границей (не включая)
			img				// - массив для хранения результата (тип 8S или 8U)
		);
		cvReleaseImage(src);
		//return img;
	}
	
	public void sumChannels(IplImage rgb, IplImage r_channel, IplImage g_channel, IplImage b_channel) {	

		ByteBuffer bb_red 	= r_channel.createBuffer();
		ByteBuffer bb_green = g_channel.createBuffer();
		ByteBuffer bb_blue 	= b_channel.createBuffer();

		for(int y = 0; y < rgb.height(); y++) {
		    for(int x = 0; x < rgb.width(); x++) {

		        int index = y * rgb.widthStep() + x * rgb.nChannels();
		        int r_val = bb_red.get(index) & 0xFF; // the 0xFF is needed to cast from an unsigned byte to an int.
		        int g_val = bb_green.get(index) & 0xFF; 
		        int b_val = bb_blue.get(index) & 0xFF; 
		        
		        if ( (r_val > 0 && g_val > 0) || (g_val > 0 && b_val > 0) ||  (r_val > 0 && b_val > 0) ) {
		        	cvSet2D(rgb, y, x, CvScalar.WHITE );	        	
		        }
		        
		    }
		}
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
