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
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		this.add(createMainImagePanel(), c );

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 100;
		c.gridx = 1;
		c.gridy = 0;
		this.add(createControlPanel(), c );

//		button = new JButton("Button 3");
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.weightx = 0.5;
//		c.gridx = 2;
//		c.gridy = 0;
//		pane.add(button, c);
//
//		button = new JButton("Long-Named Button 4");
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.ipady = 40;      //make this component tall
//		c.weightx = 0.0;
//		c.gridwidth = 3;
//		c.gridx = 0;
//		c.gridy = 1;
//		pane.add(button, c);
//
//		button = new JButton("5");
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.ipady = 0;       //reset to default
//		c.weighty = 1.0;   //request any extra vertical space
//		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
//		c.insets = new Insets(10,0,0,0);  //top padding
//		c.gridx = 1;       //aligned with button 2
//		c.gridwidth = 2;   //2 columns wide
//		c.gridy = 2;       //third row
//		pane.add(button, c);
		
		
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
