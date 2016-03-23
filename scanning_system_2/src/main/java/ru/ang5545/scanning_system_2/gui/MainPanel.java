package ru.ang5545.scanning_system_2.gui;

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
	
	public MainPanel(int width, int height, String name) {
		super(width, height, IMG_WIDTH, IMG_HEIGHT);
		this.setBorder(BorderFactory.createTitledBorder(name));
		startGrab = new JButton(START_BT_NAME);
		stopGrab = new JButton(STOP_BT_NAME);
		this.add(startGrab);
		this.add(stopGrab);
	}

	public void addStartAction(ActionListener ls) {
		startGrab.addActionListener(ls);
	}
	
	public void addStoptAction(ActionListener ls) {
		stopGrab.addActionListener(ls);
	}
}
