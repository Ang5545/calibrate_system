package ru.ang5545.calibrate_system.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ru.ang5545.calibrate_system.model.ThresholdParameters;

public class ImageChannelPanel extends ImagePanel {

	
	// ---- old size variables ----
	private final static int DEF_VALUE 	= 100;
	
	private final static int MIN_VALUE 	= 0;
	private final static int MAX_VALUE 	= 255;

	
	private final static int SLAYDER_HEIGHT = 20;
	
	private final static int FIELD_HEIGHT 	= 20;
	private final static int FILED_WIDTH  	= 40;
	// ------------------------------------
	
	
	// ---- new size variables ----
	private final static int IMG_WIDTH 	= 185;
	private final static int IMG_HEIGHT = 110;
	// ------------------------------------
	
	
	private final static String MIN_TH_LABEL = "Min threshold "; 
	private final static String MAX_TH_LABEL = "Max threshold ";
	
	private JSlider min_slider; 
	private JSlider max_slider;
	
	private JTextField minThField;
	private JTextField maxThField;
	
	private JLabel minThLabel;
	private JLabel maxThLabel;
	
	private ThresholdParameters thPar;
	
	
	public ImageChannelPanel(int width, int height, String name) {
		super(width, height, IMG_WIDTH, IMG_HEIGHT);
		this.setBorder(BorderFactory.createTitledBorder(name));
		
		this.thPar = new ThresholdParameters(DEF_VALUE, DEF_VALUE);

		//TODO text fields listeners to change slider value
		
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		
		// -- min th --
		this.minThLabel = new JLabel(MIN_TH_LABEL);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(minThLabel, c);
		
		this.minThField = new JTextField();
		this.minThField.setPreferredSize( new Dimension( FILED_WIDTH, FIELD_HEIGHT ) );
		this.minThField.setText( String.valueOf( thPar.getMin() ));
		this.minThField.setHorizontalAlignment(JTextField.CENTER);
		this.minThField.addKeyListener(getMinTecFileldListener());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(minThField, c);
		
		this.min_slider =  new JSlider (JSlider.HORIZONTAL, MIN_VALUE, MAX_VALUE, thPar.getMin());
		this.min_slider.setPreferredSize(new Dimension(width - 30, SLAYDER_HEIGHT));	
		this.min_slider.addChangeListener( new MinSlayderListener() );
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(min_slider, c);
		

		// -- max th --
		this.maxThLabel = new JLabel(MAX_TH_LABEL);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(maxThLabel, c);
		
		this.maxThField = new JTextField();
		this.maxThField.setPreferredSize( new Dimension( FILED_WIDTH, FIELD_HEIGHT ) );
		this.maxThField.setText( String.valueOf( thPar.getMax() ));
		this.maxThField.setHorizontalAlignment(JTextField.CENTER);
		this.maxThField.addKeyListener(getMaxTecFileldListener());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(maxThField, c);
		
		this.max_slider =  new JSlider (JSlider.HORIZONTAL, MIN_VALUE, MAX_VALUE, thPar.getMax());
		this.max_slider.setPreferredSize(new Dimension(width-120, SLAYDER_HEIGHT));	
		this.max_slider.addChangeListener( new MaxSlayderListener() );
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(max_slider, c);
		
		this.add(pane);
		
	}
	
	
	class MinSlayderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	setMinTh((int)source.getValue());
	        }    
	    }
	}
	
	class MaxSlayderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	setMaxTh( (int) source.getValue() );
	        }    
	    }
	}
	
	
	private void setMaxTh(int val) {
    	if (val < thPar.getMin() ) {
    		thPar.setMin(val - 1);
    		minThField.setText( String.valueOf(thPar.getMin()));
    		min_slider.setValue( thPar.getMin() );
    	}
    	maxThField.setText( String.valueOf(val));
    	thPar.setMax(val);
	}

	private void setMinTh(int val) {
    	if (val > thPar.getMax()) {
    		thPar.setMax(val+1);
    		maxThField.setText( String.valueOf( thPar.getMax() ));
    		max_slider.setValue( thPar.getMax() );
    	}
    	minThField.setText( String.valueOf(val));
    	thPar.setMin(val);
	}

	
	private KeyAdapter getMinTecFileldListener(){
		KeyAdapter ka = new KeyAdapter() {
	        public void keyReleased(KeyEvent ke) {
	        	String tfVal = minThField.getText();
	        	if (tfVal.length() > 0) {
		        	try {
		        		int val = Integer.parseInt(tfVal);
		        		if (val >= MIN_VALUE && val <= MAX_VALUE) {
		        			setMinTh(val);
		        			min_slider.setValue( val );
		        		} else {
		        			minThField.setText(String.valueOf(thPar.getMin()));
	        			}
		        	} catch (Exception ex) {
		        		minThField.setText(String.valueOf(thPar.getMax()));
		        	}
	        	}
	        }
	    };
	    return ka;
	} 
	
	private KeyAdapter getMaxTecFileldListener() {
		KeyAdapter ka = new KeyAdapter() {
	        public void keyReleased(KeyEvent ke) {
	        	String tfVal = maxThField.getText();
	        	if (tfVal.length() > 0) {
	        		try {
	        			int val = Integer.parseInt(tfVal);
	        			if (val >= MIN_VALUE && val <= MAX_VALUE) {
	        				setMaxTh(val);
	        				max_slider.setValue( val );
	        			} else {
	        				maxThField.setText(String.valueOf(thPar.getMax()));
	        			}
	        		} catch (Exception ex) {
	        			maxThField.setText(String.valueOf(thPar.getMax()));
	        		}
	        	}
	        }
	    };
	    return ka;
	}
	
	public ThresholdParameters getThresholdParameters() {
		return this.thPar;
	}
	
}
