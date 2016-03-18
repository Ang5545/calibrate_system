package ru.ang5545.scanning_system_2.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;


public class AbstaractPanel extends JPanel{

	private static final int SLAYDER_HEIGHT = 20;
	private static final int SLAYDER_MIN = 0;
	private static final int SLAYDER_MAX = 255;
	
	public AbstaractPanel(){
		super();
	}
	
	public AbstaractPanel(int width, int height){
		super();
		this.setDimension(width, height);
	}
	
	public AbstaractPanel(String name){
		super();
		this.setTitle(name);
	}
	
	public AbstaractPanel(String name, int width, int height){
		super();
		this.setDimension(width, height);
		this.setTitle(name);
	}
	
	public void setTitle(String title){
		this.setBorder(BorderFactory.createTitledBorder(title));
	}
	
	public void setDimension(int width, int height){
		Dimension dim = new Dimension(width, height);
		this.setMaximumSize(dim);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setSize(dim);
	}
	
	public JSlider createSlider( int defVal, int width, ChangeListener act ){
		JSlider slider =  new JSlider (JSlider.HORIZONTAL, SLAYDER_MIN, SLAYDER_MAX, defVal);
		slider.addChangeListener( act );
		slider.setPreferredSize(new Dimension(width, SLAYDER_HEIGHT));
		return slider;
	}
	
	public JComponent createFiled( String name, int width, int height, Object val ){
		JLabel label = new JLabel(name);
		JTextField field = new JTextField();
		field.setPreferredSize( new Dimension( width, height ) );
		field.setText( String.valueOf( val ));
		field.setEditable(false);
		JPanel pan = new JPanel();
		pan.add(label);
		pan.add(field);
		return pan;
	}
	
	
}
