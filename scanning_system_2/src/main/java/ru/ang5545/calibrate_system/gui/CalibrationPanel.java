package ru.ang5545.calibrate_system.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CalibrationPanel extends JPanel {
	
	private static final String START_BT_NAME = "Start grab";
	private static final String STOP_BT_NAME  = "Stop Grab";
	private static final String CALIBRATE     = "Calibrate";
	
	private JButton startGrab;
	private JButton stopGrab;
	private JButton calibrate;
	
	private ImagePanel findObject;
	private ImagePanel calibrationResult;
	
	public CalibrationPanel() {
		super();
		this.findObject 		= new ImagePanel(300, 200, "Find Object");
		this.calibrationResult 	= new ImagePanel(300, 200, "Calibration");
		this.startGrab 			= new JButton(START_BT_NAME);
		this.stopGrab 			= new JButton(STOP_BT_NAME);
		this.calibrate 		    = new JButton(CALIBRATE);
		
		this.setLayout(new BorderLayout());
		
		JPanel images = new JPanel();
		images.add(findObject);
		images.add(calibrationResult);
		this.add(images, BorderLayout.PAGE_START);
		
		JPanel buttons = new JPanel();
		buttons.add(startGrab);
		buttons.add(stopGrab);
		buttons.add(calibrate);
		this.add(buttons, BorderLayout.PAGE_END);
	}
	
	public void addStartAction(ActionListener as) {
		startGrab.addActionListener(as);
	}
	
	public void addStoptAction(ActionListener as) {
		stopGrab.addActionListener(as);
	}
	
	public void addCalibrateAction(ActionListener as) {
		calibrate.addActionListener(as);
	}

	public void setFinfImage(BufferedImage img) {
		findObject.setImage(img);
	};

	public void setCalibrationResult(BufferedImage img) {
		calibrationResult.setImage(img);
	};
}
