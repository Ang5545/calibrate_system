package ru.ang5545.scanning_system_2;

import ru.ang5545.scanning_system_2.gui.MainFrame;

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
