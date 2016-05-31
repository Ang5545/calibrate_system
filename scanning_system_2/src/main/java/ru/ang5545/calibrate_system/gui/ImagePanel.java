package ru.ang5545.calibrate_system.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.ang5545.calibrate_system.mage_processing.ImageHelper;

public class ImagePanel extends JPanel{

	private static final int X_OFFSET = 15;
	private static final int Y_OFFSET = 40;
	
	protected JLabel label;
	private int imgWidth;
	private int imgHeight;
	private int panWidth;
	private int panHeight;
	
	
	public ImagePanel(int width, int height, String name){
		super();
		this.setBorder(BorderFactory.createTitledBorder(name));
		this.panWidth  	= width;
		this.panHeight 	= height;
		this.imgWidth	= width  - X_OFFSET;
		this.imgHeight	= height - Y_OFFSET;
		
		this.label 		= createLabel();
		this.add(label);
		this.setDimension(panWidth, panHeight);
		this.setImage(  ImageHelper.getEmptyImage(imgWidth, imgHeight) );
	}
	
	public ImagePanel(int panWidth, int panHeight, int imgWidth, int imgHeight){
		super();
		this.panWidth  	= panWidth;
		this.panHeight 	= panHeight;
		this.imgWidth	= imgWidth;
		this.imgHeight	= imgHeight;
		this.label 		= createLabel();
		this.add(label);
		this.setDimension(panWidth, panHeight);
		this.setImage( ImageHelper.getEmptyImage(imgWidth, imgHeight) );
	}
	
	private JLabel createLabel(){
		JLabel label = new JLabel();
		return label;
	} 
	
	public void setImage(BufferedImage img){
		ImageIcon icon = new ImageIcon( resize(img) );
		label.setIcon(icon);
	};
	
	private void setDimension(int width, int height){
		Dimension dim = new Dimension(width, height);
		setMaximumSize(dim);
		setMinimumSize(dim);
		setPreferredSize(dim);
	}
	
	public BufferedImage resize(BufferedImage image) {
	    BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, imgWidth, imgHeight, null);
	    g2d.dispose();
	    return bi;
	}
}
