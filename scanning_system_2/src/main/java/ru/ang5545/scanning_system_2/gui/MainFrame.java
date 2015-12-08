package ru.ang5545.scanning_system_2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;

import ru.ang5545.scanning_system_2.image_processing.ImageLoader;


public class MainFrame extends JFrame{
	
	private static final String FRAME_NAME = "Scanning system";
	
	private static final int WEIGHT = 800;
	private static final int HEIGHT = 600;
	
	private ImageLoader imgLoad;
	
	private CalibPanel calPan;
	
	private ImagePanel imgPan;
	private ImagePanel rgb;
	private ImagePanel canny;
	
	private ImageChannelPanel red_imgChPan;
	private ImageChannelPanel green_imgChPan;
	private ImageChannelPanel blue_imgChPan;
	private AnswerWorker aw;
	
	public MainFrame(){
		super(FRAME_NAME);
		this.setSize(WEIGHT, HEIGHT);
		this.setMinimumSize(new Dimension(WEIGHT, HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		this.imgLoad = new ImageLoader();
		
		this.calPan = new CalibPanel();
		this.calPan.addStartAction( new StartGrub() );
		this.calPan.addStoptAction( new StopGrub()  );
		this.add(calPan, BorderLayout.LINE_END);
		
		this.getContentPane().add(createMainImagePanel(), 	BorderLayout.LINE_START);
		this.getContentPane().add(create_rgb_Panel(), 		BorderLayout.PAGE_END);
		
		this.pack();
		this.setVisible(true);
	}

	
	
	public void add(Component comp, BorderFactory layFact){
		this.getContentPane().add(comp, layFact);
	}
	
	
	
	
	
//	private JPanel createControlPanel(){
//		JPanel panel = new JPanel();
//		
//		this.startGrab = new JButton("Start grab");
//		this.startGrab.addActionListener( new StartGrub() );
//		
//		this.stopGrab = new JButton("StopGrab");
//		this.stopGrab.addActionListener( new StopGrub() );
//		
//		panel.add( this.startGrab) ;
//		panel.add( this.stopGrab) ;
//
//		panel = setDimension(panel, 220, 100);
//		return panel; 
//	}
	
	private JPanel createMainImagePanel(){
		
		imgPan = new ImagePanel(300, 300);
		imgPan.setImage( imgLoad.getEmptyImage());
		
		rgb = new ImagePanel(300, 300);
		rgb.setImage( imgLoad.getEmptyImage());
		
		canny = new ImagePanel(300, 300);
		canny.setImage( imgLoad.getEmptyImage());
		
		JPanel jp = new JPanel();
		jp.add(imgPan);
		jp.add(rgb);
		jp.add(canny);
		return jp; 
	}
	
	
	private JPanel create_rgb_Panel(){
		JPanel pn = new JPanel();
		
		red_imgChPan = new ImageChannelPanel(250, 300, "Red Channel");
		red_imgChPan.setImage( imgLoad.getEmptyImage());
		pn.add( red_imgChPan );
		
		green_imgChPan = new ImageChannelPanel(250, 300, "Green Channel");
		green_imgChPan.setImage( imgLoad.getEmptyImage());
		pn.add( green_imgChPan );
		
		blue_imgChPan = new ImageChannelPanel(250, 300, "Blue Channel");
		blue_imgChPan.setImage( imgLoad.getEmptyImage());
		pn.add( blue_imgChPan );
		
		return pn; 
	}
	
	
	private class StartGrub implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			aw = new AnswerWorker();
			aw.execute();
		}
	}
	
	private class StopGrub implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			aw.cancel(true);
			
		}
	}	
	
	private JPanel setDimension(JPanel pane, int width, int height){
		Dimension dim = new Dimension(width, height);
		pane.setMaximumSize(dim);
		pane.setMinimumSize(dim);
		pane.setPreferredSize(dim);
		return pane;
	}
	

	class AnswerWorker extends SwingWorker<String, Object>{
	    
		private boolean doIt;
		
		protected String doInBackground() throws Exception{
			doIt = true;
			while(doIt) {
				Thread.sleep(1000);
				imgLoad.loadImage();
				
				imgPan.setImage( imgLoad.getImage() );
				
				red_imgChPan.setImage( 
						imgLoad.get_r_plImage(red_imgChPan.minTh, red_imgChPan.maxTh) );
				
				green_imgChPan.setImage(
						imgLoad.get_g_plImage(green_imgChPan.minTh, green_imgChPan.maxTh) );
				
				blue_imgChPan.setImage( 
						imgLoad.get_b_plImage(blue_imgChPan.minTh, blue_imgChPan.maxTh) );
				
				rgb.setImage( imgLoad.get_RGB_image());
				
				canny.setImage( 
						imgLoad.get_Canny_rgb(calPan.maxTh, calPan.minTh ) );
			}
			return "test";
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
