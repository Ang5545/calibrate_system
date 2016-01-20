package ru.ang5545.scanning_system_2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;

import ru.ang5545.scanning_system_2.image_processing.Grabber;
import ru.ang5545.scanning_system_2.image_processing.ImageLoader;


public class MainFrame extends JFrame{
	
	private static final String FRAME_NAME = "Scanning system";
	
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 530;
	
	private static final int IMG_PAN_WIDTH  = 300;
	private static final int IMG_PAN_HEIGHT = 200;
	
	private AnswerWorker aw;
	private Grabber grabber;
	
	private CalibPanel calPan;
	
	private ImagePanel origImg;
	private ImagePanel sumChanImg;
	private ImagePanel countImg;
	
	private ImageChannelPanel red_channelsPan;
	private ImageChannelPanel green_channelsPan;
	private ImageChannelPanel blue_channelsPan;

	BufferedImage test;
	
	public MainFrame(){
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
		calPan.addStartAction( new StartGrub() );
		calPan.addStoptAction( new StopGrub()  );
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
	
	
	private class StartGrub implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			grabber = new Grabber();
			aw = new AnswerWorker();
			aw.execute();
		}
	}
	
	private class StopGrub implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			grabber.stopGrub();
			aw.cancel(true);
			
			try {
				File outputfile = new File("/Users/fedormurashko/Desktop/image.jpg");
				ImageIO.write(test, "jpg", outputfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			};
			
		}
	}	
	

	class AnswerWorker extends SwingWorker<String, Object>{
		
		private boolean doIt;
		
		protected String doInBackground() throws Exception{
			doIt = true;
			while(doIt) {

				int redMinTh 	= red_channelsPan.minTh;
				int redMaxTh 	= red_channelsPan.maxTh;
				int greenMinTh 	= green_channelsPan.minTh;
				int greenMaxTh 	= green_channelsPan.maxTh;
				int blueMinTh 	= blue_channelsPan.minTh;
				int blueMaxTh 	= blue_channelsPan.maxTh;

				grabber.snapShoot();
				BufferedImage image			= grabber.getImage();
				BufferedImage redImage 		= grabber.get_r_plane(redMinTh, redMaxTh);
				BufferedImage greenImage 	= grabber.get_g_plane(greenMinTh, greenMaxTh);
				BufferedImage blueImage 	= grabber.get_b_plane(blueMinTh, blueMaxTh);
				
				origImg.setImage(image);
				red_channelsPan.setImage(redImage);
				green_channelsPan.setImage(greenImage);
				blue_channelsPan.setImage(blueImage);

				sumChanImg.setImage( grabber.get_rgb_plane() );
				countImg.setImage(grabber.get_Canny_rgb(calPan.maxTh, calPan.minTh ));

			}
			return "succes";
		}
	
		protected void done(){
			try {
				doIt = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
}
