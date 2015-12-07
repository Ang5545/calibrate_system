package ru.ang5545.scanning_system_2.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	
	private static final String FRAME_NAME = "Scanning system";
	
	public MainFrame(){
		super(FRAME_NAME);
		this.setSize(800, 400);
		this.setMinimumSize(new Dimension(800, 400));
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
