package ru.ang5545.calibrate_system;

import ru.ang5545.calibrate_system.gui.MainFrame;

public class App {

	public static void createGUI() {
		
		MainFrame fr = new MainFrame();	
		fr.showFrame();
   }
   
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
                  createGUI();
             }
        });
   }
}
