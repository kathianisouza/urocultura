package core_contagem_atual;

import hough_circles.HoughCircles;
import hough_line.LinearHT.HoughLine;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.List;

import javax.swing.JOptionPane;


/** 
 * This plugin implements a simple Hough Transform for straight lines.
 * It expects an 8-bit binary (edge) image, with background = 0 and
 * edge pixels > 0.
 * Draws the resulting lines destructively into a new result image.
 * Last update: 2010-07-24
*/

public class Plugin_Hough_Linear{
	static int N_ANGLE = 256;			// resolution of angle
	static int N_RADIUS = 256;			// resolution of radius
	static int MAX_LINES = 1;			// max. number of lines to be detected
	static int MIN_PNTSONLINE = 150;	// min. number of points on each line
	static boolean SHOW_ACCUMULATOR = false;
	static boolean SHOW_LOCALMAXIMA = false;
	static boolean LIST_LINES = false;
	static boolean DRAW_LINES = true;
	static int LINE_WIDTH = 1;
	static Color LINE_COLOR = Color.GREEN;

	HoughCircles hc = null;
	ImagePlus imp = null;		// input image
	
	public Plugin_Hough_Linear(ImageProcessor ip, HoughCircles hc) {
		this.hc = hc;
		LinearHT ht = new LinearHT(ip, N_ANGLE, N_RADIUS);
		List<HoughLine> lines = ht.getMaxLines(MAX_LINES, MIN_PNTSONLINE);
		
		// plot the lines in a new image
		if (DRAW_LINES) {
			ImageProcessor resultIp = ip.convertToRGB();
			drawLines(lines, resultIp);
			ImagePlus resultIm = new ImagePlus("line detected", resultIp);
			resultIm.show();
		} 
	}
	
	void drawLines(List<HoughLine> lines, ImageProcessor ip) {
		ip.setLineWidth(LINE_WIDTH);
		ip.setColor(LINE_COLOR);
		for (HoughLine hl : lines){
			Line2D.Double lin = hl.makeLine2D();
			int u1 = (int) Math.rint(lin.x1);
			int v1 = (int) Math.rint(lin.y1);
			int u2 = (int) Math.rint(lin.x2);
			int v2 = (int) Math.rint(lin.y2);
			//JOptionPane.showMessageDialog(null, "(" +u1+ "," +v1+ ")" + "(" +u2+ "," +v2+ ")");

			ip.drawLine(u1, v1, u2, v2);
			
			verifyPixelsLine(lin);
		}
	}

	private void verifyPixelsLine(Line2D.Double lin){
		float[] xPoints = hc.getXPixelsPetriBound();
		float[] yPoints = hc.getYPixelsPetriBound();
		
		
		for(int i = 0; i < xPoints.length; i++){
			for(int j = 0; j < yPoints.length; j++){
				if(lin.contains(xPoints[i], yPoints[j])){
					JOptionPane.showMessageDialog(null, "Encontrou");
				}
				
			}
		}
	}
	
}
