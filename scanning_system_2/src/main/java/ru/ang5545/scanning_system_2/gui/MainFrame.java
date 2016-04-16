package ru.ang5545.scanning_system_2.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import ru.ang5545.model.ThresholdParameters;
import ru.ang5545.scanning_system_2.cam_acces.DeviceManager;
import ru.ang5545.scanning_system_2.cam_acces.Grabber;
import ru.ang5545.scanning_system_2.image_processing.ImageHandler;
import ru.ang5545.scanning_system_2.image_processing.ImageLoader;
import ru.ang5545.utils.Path;

//import com.apple.eawt.Application;

public class MainFrame extends JFrame{
	
	private static final String FRAME_NAME = "Scanning system";
	private static final String FRAME_ICON = "/resources/appIcon.png";
	
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 400;
	
	private static final int IMG_PAN_WIDTH  = 200;
	private static final int IMG_PAN_HEIGHT = 150;
	private static final int IMG_PAN_WITH_SLIDER_HEIGHT = 240;
	
	private AnswerWorker aw;
	private Grabber grabber;
	//private ImageLoader grabber;
	
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
		
		Image appIcon = new ImageIcon(Path.getAppPath() + FRAME_ICON).getImage();
		this.setIconImage(appIcon);									// for other OS
		
		// TODO change to work this on PC 
		//Application.getApplication().setDockIconImage(appIcon);		// for Mac OS X
		
		
		// - add components -
		JPanel centerPan = new JPanel();
		centerPan.setLayout(new BorderLayout());
		centerPan.add(createImagesPanel(), 		BorderLayout.PAGE_START);
		centerPan.add(createСhannellsPanel(), 	BorderLayout.PAGE_END);
		this.add(centerPan, 			BorderLayout.LINE_START);
		this.add(createCalibPanel(),	BorderLayout.LINE_END);
		
		// - create default components - 
		this.dm = new DeviceManager();
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
			grabber = new Grabber(dm.getCamIndex());
			//grabber = new ImageLoader();
			ih = new ImageHandler(grabber.getResolution());
			aw = new AnswerWorker();
			aw.execute();
		}
	}
	
	private class StopGrub implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			aw.cancel(true);
			grabber.stopGrub();
			ih.release();
		}
	}	
	

	class AnswerWorker extends SwingWorker<String, Object> {
		
		private boolean doIt;
		
		protected String doInBackground() throws Exception {

			doIt = true;
			
			while(doIt) {
				
				ThresholdParameters redThPar = red_channelsPan.getThresholdParameters();
				ThresholdParameters greenThPar = green_channelsPan.getThresholdParameters();
				ThresholdParameters blueThPar = blue_channelsPan.getThresholdParameters();

				ih.processImage(grabber.grab(), redThPar, greenThPar, blueThPar);

				origImg.setImage(ih.getOrigin());
				red_channelsPan.setImage(ih.getRedChannel());
				green_channelsPan.setImage(ih.getGreenChannel());
				blue_channelsPan.setImage(ih.getBlueChannel());
				
				sumChanImg.setImage(ih.getRGBsumm());
				countImg.setImage(ih.get_contour());
				mainPan.setImage(ih.getResultl());
				
				//grabber.release();
				ih.release();
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
