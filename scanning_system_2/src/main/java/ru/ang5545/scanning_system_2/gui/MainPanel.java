package ru.ang5545.scanning_system_2.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class MainPanel extends ImagePanel {

	private final static int IMG_WIDTH 	= 370;
	private final static int IMG_HEIGHT = 220;
	
	
//	private JLabel label;
//	private int imgWidth;
//	private int imgHeight;
//	private int panWidth;
//	private int panHeight;

	
	private JButton startGrab;
	private JButton stopGrab;
	
	public MainPanel(int width, int height, String name) {
		super(width, height, IMG_WIDTH, IMG_HEIGHT);
		this.setBorder(BorderFactory.createTitledBorder(name));
	}

}
