package edu.uga.miage.m1.polygons.gui;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *  @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
public class GUIHelper {
	
	public static void showOnFrame(String frameName) {
		JDrawingFrame frame = new JDrawingFrame(frameName);
		WindowAdapter wa = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(wa);
		frame.pack();
		frame.setVisible(true);
	}

}