package ru.ang5545.scanning_system_2.gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageChannelPanel extends ImagePanel {

	private final static int DEF_VALUE = 100;
	private final static int OFFSET = 100;
	private final static int SLAYDER_HEIGHT = 20;
	private final static int FIELD_HEIGHT = 20;
	private final static int FILED_WEIGHT = 30;
	
	private final static String MIN_TH_LABEL = "Min Th"; 
	private final static String MAX_TH_LABEL = "Max Th";
	
	private JSlider min_slider; 
	private JSlider max_slider;
	
	private JTextField minThField;
	private JTextField maxThField;
	
	private JLabel minThLabel;
	private JLabel maxThLabel;
	
	public int minTh;
	public int maxTh;
	
	
	public ImageChannelPanel(int width, int height, String name) {
		super(width, height, width - OFFSET, height - OFFSET-SLAYDER_HEIGHT);
		this.setBorder(BorderFactory.createTitledBorder(name));

		this.minTh = DEF_VALUE; 
		this.maxTh = DEF_VALUE;
		
		
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
		
		
		this.min_slider =  new JSlider (JSlider.HORIZONTAL, 0, 255, minTh);
		this.min_slider.addChangeListener( new MinListener() );
		this.min_slider.setPreferredSize(new Dimension(width-10, SLAYDER_HEIGHT));
		
		this.max_slider =  new JSlider (JSlider.HORIZONTAL, 0, 255, maxTh);
		this.max_slider.addChangeListener( new MaxListener() );
		this.max_slider.setPreferredSize(new Dimension(width-10, SLAYDER_HEIGHT));
		
		this.add(max_slider);
		this.add(min_slider);
	}
	
	
	class MinListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	minTh = (int)source.getValue();
	        	minThField.setText( String.valueOf( minTh ));
	        }    
	    }
	}
	
	class MaxListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	maxTh = (int)source.getValue();
	        	maxThField.setText( String.valueOf( maxTh ));
	        }    
	    }
	}
}
