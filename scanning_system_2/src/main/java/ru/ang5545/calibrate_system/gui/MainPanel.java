package ru.ang5545.calibrate_system.gui;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class MainPanel extends ImagePanel {

	private final static int IMG_WIDTH 	= 370;
	private final static int IMG_HEIGHT = 220;
	
	private static final String START_BT_NAME = "Start grab";
	private static final String STOP_BT_NAME  = "Stop Grab";
	private static final String SAVE_IMGS_BT_NAME  = "Save images";
	
//	private JLabel label;
//	private int imgWidth;
//	private int imgHeight;
//	private int panWidth;
//	private int panHeight;

	public void setImage(BufferedImage img){
		ImageIcon icon = new ImageIcon( resize(img) );
		label.setIcon(icon);
	};
	
	
	private JButton startGrab;
	private JButton stopGrab;
	private JButton saveImages;
	
	
	public MainPanel(int width, int height, String name) {
		super(width, height, width - 20, height-20);
		this.setBorder(BorderFactory.createTitledBorder(name));
		this.startGrab = new JButton(START_BT_NAME);
		this.stopGrab = new JButton(STOP_BT_NAME);
		this.saveImages = new JButton(SAVE_IMGS_BT_NAME);
//		this.add(startGrab);
//		this.add(stopGrab);
//		this.add(saveImages);
	}

	public void addStartAction(ActionListener as) {
		startGrab.addActionListener(as);
	}
	
	public void addStoptAction(ActionListener as) {
		stopGrab.addActionListener(as);
	}
	
	public void addSaveAction(ActionListener as) {
		saveImages.addActionListener(as);
	}
}
