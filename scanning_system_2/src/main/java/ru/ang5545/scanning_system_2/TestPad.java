//package ru.ang5545.scanning_system_2;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.SwingWorker;
//
//
//
//public class TestPad extends JFrame {
//	
//	
//	public static void createGUI() {
//		JFrame frame = new JFrame();
//        frame.getContentPane().setLayout(new BorderLayout());
//
//        JPanel green = new JPanel();
//        green.setPreferredSize(new Dimension(80, 150));
//        green.setBackground(Color.GREEN);
//
//        JPanel yellow = new JPanel();
//        yellow.setBackground(Color.YELLOW);
//
//        frame.getContentPane().add(green, BorderLayout.CENTER);
//        frame.getContentPane().add(yellow, BorderLayout.PAGE_END);
//
//        JButton b = new JButton("Answer!");
//        b.addActionListener(new StopGrub());
//        
//        frame.pack();
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
//   }
//   
//	public static void main(String[] args) {
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//                  createGUI();
//             }
//        });
//	}
//
//	
//	private class StopGrub implements ActionListener{
//		public void actionPerformed(ActionEvent e) {
//			 new AnswerWorker().execute();
//		}
//	}	
//	
//	
//	
//
//	class AnswerWorker extends SwingWorker<Integer, Integer>{
//	    
//		protected Integer doInBackground() throws Exception{
//			Thread.sleep(1000);             // Do a time-consuming task.
//			return 42;
//		}
//	
//		protected void done(){
//			try {
//				System.out.println("done");
//				//	JOptionPane.showMessageDialog(f, get());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
