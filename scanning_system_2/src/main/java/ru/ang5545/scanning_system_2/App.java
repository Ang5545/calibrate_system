package ru.ang5545.scanning_system_2;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import ru.ang5545.scanning_system_2.gui.CalibPanel;
import ru.ang5545.scanning_system_2.gui.MainFrame;

public class App {
	
	
	public static void createGUI() {
		MainFrame fr = new MainFrame();	
   }
   
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
                  createGUI();
             }
        });
   }
}
