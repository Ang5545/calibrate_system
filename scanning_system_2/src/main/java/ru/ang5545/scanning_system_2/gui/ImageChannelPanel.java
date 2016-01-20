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

	private final static int DEF_VALUE 	= 100;
	
	private final static int MIN_VALUE 	= 0;
	private final static int MAX_VALUE 	= 255;
	
	private final static int Y_OFFSET 	= 100;
	private final static int X_OFFSET 	= 30;
	
	private final static int SLAYDER_HEIGHT = 20;
	
	private final static int FIELD_HEIGHT 	= 20;
	private final static int FILED_WIDTH  	= 40;
	
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
		super(width, height, width - X_OFFSET, height - Y_OFFSET-SLAYDER_HEIGHT);
		this.setBorder(BorderFactory.createTitledBorder(name));

		this.minTh = DEF_VALUE; 
		this.maxTh = DEF_VALUE;

		//TODO text fields listeners to change slider value
		
		JPanel minSlayderPan = new JPanel();
		this.minThLabel = new JLabel(MIN_TH_LABEL);
		this.minThField = new JTextField();
		this.minThField.setPreferredSize( new Dimension( FILED_WIDTH, FIELD_HEIGHT ) );
		this.minThField.setText( String.valueOf( minTh ));
		this.minThField.setEditable(false);
		this.min_slider =  new JSlider (JSlider.HORIZONTAL, MIN_VALUE, MAX_VALUE, minTh);
		this.min_slider.setPreferredSize(new Dimension(width-120, SLAYDER_HEIGHT));	
		this.min_slider.addChangeListener( new MinListener() );
		minSlayderPan.add(minThLabel);
		minSlayderPan.add(minThField);
		minSlayderPan.add(min_slider);
		this.add(minSlayderPan);
		
		JPanel maxSlayderPan = new JPanel();
		this.maxThLabel = new JLabel(MAX_TH_LABEL);
		this.maxThField = new JTextField();
		this.maxThField.setPreferredSize( new Dimension( FILED_WIDTH, FIELD_HEIGHT ) );
		this.maxThField.setText( String.valueOf( maxTh ));
		this.maxThField.setEditable(false);
		this.max_slider =  new JSlider (JSlider.HORIZONTAL, MIN_VALUE, MAX_VALUE, maxTh);
		this.max_slider.setPreferredSize(new Dimension(width-120, SLAYDER_HEIGHT));	
		this.max_slider.addChangeListener( new MaxListener() );
		maxSlayderPan.add(maxThLabel);
		maxSlayderPan.add(maxThField);
		maxSlayderPan.add(max_slider);
		this.add(maxSlayderPan);
	}
	
	
	class MinListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	minTh = (int)source.getValue();
	        	minThField.setText( String.valueOf( minTh ));
	        	if ( minTh > maxTh ){
	        		maxTh = minTh + 1;
	        		maxThField.setText( String.valueOf( maxTh ));
	        		max_slider.setValue( maxTh );
	        	}
	        }    
	    }
	}
	
	class MaxListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	maxTh = (int)source.getValue();
	        	maxThField.setText( String.valueOf( maxTh ));
	        	if ( maxTh < minTh ){
	        		minTh = maxTh - 1;
	        		minThField.setText( String.valueOf( minTh ));
	        		min_slider.setValue( minTh );
	        	}
	        }    
	    }
	}
}
