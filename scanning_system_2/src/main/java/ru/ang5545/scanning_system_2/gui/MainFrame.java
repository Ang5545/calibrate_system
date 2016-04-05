package ru.ang5545.scanning_system_2.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import ru.ang5545.scanning_system_2.cam_acces.DeviceManager;
import ru.ang5545.scanning_system_2.cam_acces.Grabber;
import ru.ang5545.scanning_system_2.image_processing.ImageHandler;
import ru.ang5545.scanning_system_2.image_processing.ImageLoader;




public class MainFrame extends JFrame{
	
	private static final String FRAME_NAME = "Scanning system";
	
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 400;
	
	private static final int IMG_PAN_WIDTH  = 200;
	private static final int IMG_PAN_HEIGHT = 150;
	private static final int IMG_PAN_WITH_SLIDER_HEIGHT = 240;
	
	private AnswerWorker aw;
	//private Grabber grabber;
	private ImageLoader grabber;
	
	private DeviceManager dm;
	private ImageHandler ih;
	
	private MainPanel mainPan;
	
	
	private ImagePanel origImg;
	private ImagePanel sumChanImg;
	private ImagePanel countImg;
	
	private ImageChannelPanel red_channelsPan;
	private ImageChannelPanel green_channelsPan;
	private ImageChannelPanel blue_channelsPan;

	
	public MainFrame() {
		super(FRAME_NAME);
		
		// - set window parameters - 
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		// - add components -
		JPanel centerPan = new JPanel();
		centerPan.setLayout(new BorderLayout());
		centerPan.add(createImagesPanel(), 		BorderLayout.PAGE_START);
		centerPan.add(createСhannellsPanel(), 	BorderLayout.PAGE_END);
		this.add(centerPan, 			BorderLayout.LINE_START);
		this.add(createCalibPanel(),	BorderLayout.LINE_END);
		
		// - create default components - 
		this.dm = new DeviceManager();
		this.ih = new ImageHandler(dm.getResolutuon());
	}

	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createImagesPanel(){
		JPanel imagesPanel = new JPanel();
		origImg 	= new ImagePanel(IMG_PAN_WIDTH, IMG_PAN_HEIGHT, "Original Image");
		sumChanImg 	= new ImagePanel(IMG_PAN_WIDTH, IMG_PAN_HEIGHT, "Summary Channels");
		countImg 	= new ImagePanel(IMG_PAN_WIDTH, IMG_PAN_HEIGHT, "Contour");
		imagesPanel.add(origImg);
		imagesPanel.add(sumChanImg);
		imagesPanel.add(countImg);
		return imagesPanel;
	}

	private JPanel createCalibPanel(){
		this.mainPan = new MainPanel(400, 400, "Calib panel");
		mainPan.addStartAction(new StartGrub());
		mainPan.addStoptAction(new StopGrub());
		return mainPan;
	}
	
	private JPanel createСhannellsPanel(){
		JPanel channelsPanel = new JPanel();	
		red_channelsPan 	= new ImageChannelPanel(IMG_PAN_WIDTH, IMG_PAN_WITH_SLIDER_HEIGHT, "Red Channel");
		green_channelsPan	= new ImageChannelPanel(IMG_PAN_WIDTH, IMG_PAN_WITH_SLIDER_HEIGHT, "Green Channel");
		blue_channelsPan 	= new ImageChannelPanel(IMG_PAN_WIDTH, IMG_PAN_WITH_SLIDER_HEIGHT, "Blue Channel");		
		channelsPanel.add(red_channelsPan);
		channelsPanel.add(green_channelsPan);
		channelsPanel.add(blue_channelsPan);
		return channelsPanel; 
	}
	
	
	private class StartGrub implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//grabber = new Grabber(dm.getCamIndex());
			grabber = new ImageLoader();
			aw = new AnswerWorker();
			aw.execute();
		}
	}
	
	private class StopGrub implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			grabber.stopGrub();
			aw.cancel(true);
		}
	}	
	

	class AnswerWorker extends SwingWorker<String, Object> {
		
		private boolean doIt;
		
		protected String doInBackground() throws Exception {

			doIt = true;
			
			while(doIt) {
				
				int redMinTh 	= red_channelsPan.minTh;
				int redMaxTh 	= red_channelsPan.maxTh;
				int greenMinTh 	= green_channelsPan.minTh;
				int greenMaxTh 	= green_channelsPan.maxTh;
				int blueMinTh 	= blue_channelsPan.minTh;
				int blueMaxTh 	= blue_channelsPan.maxTh;

				ih.setRedThresholdParameters(redMinTh, redMaxTh);
				ih.setGreenThresholdParameters(greenMinTh, greenMaxTh);
				ih.setBlueThresholdParameters(blueMinTh, blueMaxTh);
				
				ih.processImage(grabber.grab());
//
//				BufferedImage image			= ih.getOrigin();
//				BufferedImage redImage 		= ih.get_r_plane(redMinTh, redMaxTh);
//				BufferedImage greenImage 	= ih.get_g_plane(greenMinTh, greenMaxTh);
//				BufferedImage blueImage 	= ih.get_b_plane(blueMinTh, blueMaxTh);
//				
				origImg.setImage(ih.getOrigin());
				red_channelsPan.setImage(ih.getRedChannel());
				green_channelsPan.setImage(ih.getGreenChannel());
				blue_channelsPan.setImage(ih.getBlueChannel());
				
				sumChanImg.setImage(ih.getRGBsumm());
				countImg.setImage(ih.get_contour());
				mainPan.setImage(ih.getResultl());
				
			}
			return "succes";
		}
	
		protected void done() {
			try {
				doIt = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
}
