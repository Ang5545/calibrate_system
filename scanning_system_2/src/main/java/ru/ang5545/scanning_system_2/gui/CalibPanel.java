package ru.ang5545.scanning_system_2.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CalibPanel extends JPanel{

	
	private JButton startGrab;
	private JButton stopGrab;
	
	private JLabel grabLabel;
	
	
	public CalibPanel(){
		super();
		
		
//		this.setLayout(new BorderLayout());
//		this.add(createMainImagePanel(), 	BorderLayout.CENTER);
//		this.add(createControlPanel(),  	BorderLayout.EAST);
//		this.add(createControlPanel(),  	BorderLayout.SOUTH);
	}
	
	
	private JPanel createControlPanel(){
		JPanel panel = new JPanel();
		panel.setSize(300, 300);
		this.startGrab = new JButton("Start grab");
		this.startGrab.addActionListener( new StartGrub() );
		
		this.stopGrab = new JButton("StopGrab");
		this.stopGrab.addActionListener( new StopGrub() );
		
		panel.add( this.startGrab) ;
		panel.add( this.stopGrab) ;
		return panel; 
	}
	
	private JPanel createMainImagePanel(){
		JPanel panel = new JPanel();
		ImageIcon img = new ImageIcon("/home/fedor-m/Рабочий стол/2.png");
		
		this.grabLabel = new JLabel();
		this.grabLabel.setIcon(img);
		
		panel.add(grabLabel);
		return panel; 
	}
	
	private class StartGrub implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println("StartGrub");
		}
	}
	
	private class StopGrub implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println("StopGrub");
		}
	}	
	
	
}
