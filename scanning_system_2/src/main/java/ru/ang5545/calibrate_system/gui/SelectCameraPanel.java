package ru.ang5545.calibrate_system.gui;


//TODO
// fix exception in device manager

//package ru.ang5545.scanning_system_2.gui;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import ru.ang5545.scanning_system_2.cam_acces.DeviceManager;
//
//public class SelectCameraPanel extends JFrame {
//
//	private static final String FRAME_NAME = "Select camera";
//	
//	private static final int WINDOW_WIDTH = 500;
//	private static final int WINDOW_HEIGHT = 300;
//	
//	private static final int IMG_PAN_WIDTH  = 300;
//	private static final int IMG_PAN_HEIGHT = 200;
//	
//	private int camIndex = 0;
//	
//	private ImagePanel preview;
//	
//
//	
//	public SelectCameraPanel() {
//		super(FRAME_NAME);
//		
//		// - set window parameters - 
//		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
//		this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
//		this.setResizable(false);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.getContentPane().setLayout(new BorderLayout());
//		
//		// - add components -
//		preview 	= new ImagePanel(IMG_PAN_WIDTH, IMG_PAN_HEIGHT, "Prview");
//		this.add(preview);
//		
//		
//	}
//	
//	public void showFrame() {
//		this.pack();
//		this.setVisible(true);
//	}
//	
//	public int getCamIndex() {
//		return camIndex;
//	}
//}
