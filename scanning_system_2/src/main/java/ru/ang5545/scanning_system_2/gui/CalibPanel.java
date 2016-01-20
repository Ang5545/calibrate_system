package ru.ang5545.scanning_system_2.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalibPanel extends AbstaractPanel{

	private static final int HEIGHT = 200;
	private static final int WIDTH 	= 300;
	
	private static final String PANEL_TITLE = "Calibrate panel";
	
	private static final String START_BT_NAME = "Start grab";
	private static final String STOP_BT_NAME  = "Stop Grab";
	
	private static final String MIN_TH_LABEL = "Min th";
	private static final String MAX_TH_LABEL = "Max th";
	
	private final static int FIELD_HEIGHT = 20;
	private final static int FILED_WEIGHT = 30;
	
	private final static int DEF_VALUE = 100;
	
	private JButton startGrab;
	private JButton stopGrab;
	
	public int minTh;
	public int maxTh;
	
	private JTextField minThField;
	private JTextField maxThField;
	
	private JLabel minThLabel;
	private JLabel maxThLabel;
	
	private JSlider minThSlider; 
	private JSlider maxThSlider;
	
	
	public CalibPanel(){
		super(PANEL_TITLE, WIDTH, HEIGHT);
		
		this.minTh = DEF_VALUE;
		this.maxTh = DEF_VALUE;
		
		this.startGrab = new JButton(START_BT_NAME);
		this.stopGrab = new JButton(STOP_BT_NAME);
		
		JPanel btPan = new JPanel();
		btPan.add(startGrab) ;
		btPan.add(stopGrab) ;
		this.add(btPan);
		
		this.minThLabel = new JLabel(MIN_TH_LABEL);
		this.minThField = new JTextField();
		this.minThField.setPreferredSize( new Dimension( FILED_WEIGHT, FIELD_HEIGHT ) );
		this.minThField.setText( String.valueOf( minTh ));
		this.minThField.setEditable(false);
		
		JPanel minPan = new JPanel();
		minPan.add(minThLabel);
		minPan.add(minThField);
		this.add(minPan);
		
		this.maxThLabel = new JLabel(MAX_TH_LABEL);
		this.maxThField = new JTextField();
		this.maxThField.setPreferredSize( new Dimension( FILED_WEIGHT, FIELD_HEIGHT ) );
		this.maxThField.setText( String.valueOf( maxTh ));
		this.maxThField.setEditable(false);
		
		JPanel maxPan = new JPanel();
		maxPan.add(maxThLabel);
		maxPan.add(maxThField);
		this.add(maxPan);
		
		
		this.minThSlider = createSlider(minTh, WIDTH-10, new MinThListener() );
		this.maxThSlider = createSlider(maxTh, WIDTH-10, new MaxThListener() );
		
		this.add(minThSlider) ;
		this.add(maxThSlider) ;
	}
	
	public void addStartAction(ActionListener act){
		this.startGrab.addActionListener(act);
	}
	
	public void addStoptAction(ActionListener act){
		this.stopGrab.addActionListener(act);
	}
	
	class MinThListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	minTh = (int)source.getValue();
	        	minThField.setText( String.valueOf( minTh ));
	        }    
	    }
	}
	
	class MaxThListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	maxTh = (int)source.getValue();
	        	maxThField.setText( String.valueOf( maxTh ));
	        }    
	    }
	}
	
	
}
