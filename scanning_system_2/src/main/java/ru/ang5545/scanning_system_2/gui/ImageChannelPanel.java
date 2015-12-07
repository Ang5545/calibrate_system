package ru.ang5545.scanning_system_2.gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageChannelPanel extends ImagePanel {

	private final static int DEF_VALUE = 100;
	private final static int OFFSET = 100;
	private final static int SLAYDER_HEIGHT = 20;
	
	private JSlider min_slider; 
	private JSlider max_slider;
	
	public int minTh;
	public int maxTh;
	
	
	public ImageChannelPanel(int width, int height, String name) {
		super(width, height, width - OFFSET, height - OFFSET-SLAYDER_HEIGHT);
		
		this.minTh = DEF_VALUE; 
		this.maxTh = DEF_VALUE;
		
		this.min_slider =  new JSlider (JSlider.HORIZONTAL, 0, 255, minTh);
		this.min_slider.addChangeListener( new MinListener() );
		this.min_slider.setPreferredSize(new Dimension(width-10, SLAYDER_HEIGHT));
		
		this.max_slider =  new JSlider (JSlider.HORIZONTAL, 0, 255, maxTh);
		this.max_slider.addChangeListener( new MaxListener() );
		this.max_slider.setPreferredSize(new Dimension(width-10, SLAYDER_HEIGHT));
		
		this.setBorder(BorderFactory.createTitledBorder(name));
		this.add(max_slider);
		this.add(min_slider);
	}
	
	
	class MinListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	minTh = (int)source.getValue();
	        }    
	    }
	}
	
	class MaxListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	maxTh = (int)source.getValue();
	        }    
	    }
	}
}
