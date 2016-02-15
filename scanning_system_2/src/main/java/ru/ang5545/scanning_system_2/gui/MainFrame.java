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
import ru.ang5545.scanning_system_2.cam_acces.ImageLoaderForTest;
import ru.ang5545.scanning_system_2.image_processing.ImageHandler;




public class MainFrame extends JFrame{
	
	private static final String FRAME_NAME = "Scanning system";
	
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 530;
	
	private static final int IMG_PAN_WIDTH  = 300;
	private static final int IMG_PAN_HEIGHT = 200;
	
	private AnswerWorker aw;
	private Grabber grabber;
	//private ImageLoaderForTest grabber;
	private DeviceManager dm;
	private ImageHandler ih;
	
	private CalibPanel calPan;
	
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
		this.ih = new ImageHandler();
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
		calPan = new CalibPanel();
		calPan.addStartAction(new StartGrub());
		calPan.addStoptAction(new StopGrub());
		return calPan;
	}
	
	private JPanel createСhannellsPanel(){
		JPanel channelsPanel = new JPanel();	
		red_channelsPan 	= new ImageChannelPanel(IMG_PAN_WIDTH, 300, "Red Channel");
		green_channelsPan	= new ImageChannelPanel(IMG_PAN_WIDTH, 300, "Green Channel");
		blue_channelsPan 	= new ImageChannelPanel(IMG_PAN_WIDTH, 300, "Blue Channel");		
		channelsPanel.add(red_channelsPan);
		channelsPanel.add(green_channelsPan);
		channelsPanel.add(blue_channelsPan);
		return channelsPanel; 
	}
	
	
	private class StartGrub implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//grabber = new Grabber(dm.getCamIndex());
			grabber = new Grabber(dm.getCamIndex());
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
				
				try {
				    Thread.sleep(1000);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				
				int redMinTh 	= red_channelsPan.minTh;
				int redMaxTh 	= red_channelsPan.maxTh;
				int greenMinTh 	= green_channelsPan.minTh;
				int greenMaxTh 	= green_channelsPan.maxTh;
				int blueMinTh 	= blue_channelsPan.minTh;
				int blueMaxTh 	= blue_channelsPan.maxTh;

				grabber.snapShoot();
				ih.processImage(grabber.getGrabedImage());

				BufferedImage image			= ih.getOrigin();
				BufferedImage redImage 		= ih.get_r_plane(redMinTh, redMaxTh);
				BufferedImage greenImage 	= ih.get_g_plane(greenMinTh, greenMaxTh);
				BufferedImage blueImage 	= ih.get_b_plane(blueMinTh, blueMaxTh);
				
				origImg.setImage(image);
				red_channelsPan.setImage(redImage);
				green_channelsPan.setImage(greenImage);
				blue_channelsPan.setImage(blueImage);
				
				sumChanImg.setImage(ih.get_rgb_plane());
				countImg.setImage(ih.get_Canny_rgb(calPan.maxTh, calPan.minTh ));

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
