package core_contagem_atual;

import hough_circles.HoughCircles;
import hough_line.LinearHT;
import hough_line.LinearHT.HoughLine;
import ij.ImagePlus;
import ij.gui.Line;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Polygon;
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
	int ptx1 = 0, pty1 = 0, ptx2 = 0, pty2 = 0; // pontos calibrados da linha detectada.
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
		} 
	}
	
	void drawLines(List<HoughLine> lines, ImageProcessor ip) {
		ip = hc.getImageOriginal();
		ip.setLineWidth(LINE_WIDTH);
		ip.setColor(LINE_COLOR);
		for (HoughLine hl : lines){
			Line2D.Double lin = hl.makeLine2D();
			int u1 = (int) Math.rint(lin.x1);
			int v1 = (int) Math.rint(lin.y1);
			int u2 = (int) Math.rint(lin.x2);
			int v2 = (int) Math.rint(lin.y2);
			//JOptionPane.showMessageDialog(null, "(" +lin.x1+ "," +lin.y1+ ")" + "(" +lin.x2+ "," +lin.y2+ ")");
			
			lineTo(u1, u2, v1, v2, ip);
			
			//ip.drawLine(ptx1, pty1, ptx2, pty2);
			Line linha = new Line(ptx1, pty1, ptx2, pty2);
			linha.drawPixels(ip);
			new ImagePlus("draw line", ip).show();
			//verifyPixelsLine(lin,hl);
		}
	}

	private void lineTo(int x1, int x2, int y1, int y2, ImageProcessor ip) {
       
		boolean flag1 = true;
		boolean flag2 = true;
        int dx = x2-x1;
        int dy = y2-y1;
        int absdx = dx>=0?dx:-dx;
        int absdy = dy>=0?dy:-dy;
        int n = absdy>absdx?absdy:absdx;
        double xinc = (double)dx/n;
        double yinc = (double)dy/n;
        double x = x1;
        double y = y1;
        n++;
        x1 = x2; y1 = y2;
        if (n>1000000) return;
        do {
            
        	if((int)Math.round(x) <= 0 && flag1 == true){
        		ptx1 = (int)Math.round(x);
        		pty1 = (int)Math.round(y);
        		flag1 = false;
        		//JOptionPane.showMessageDialog(null, "(" +(int)Math.round(x)+ "," +(int)Math.round(y)+ ")");
        	}
           
        	if((int)Math.round(x) <= -(ip.getWidth()) && flag2 == true){
        		ptx2 = (int)Math.round(x);
        		pty2 = (int)Math.round(y);
        		flag2 = false;
        	}
        	
            x += xinc;
            y += yinc;
        } while (--n>0);
        JOptionPane.showMessageDialog(null, "(" +ptx1+ "," +pty1+ ")" + "(" +ptx2+ "," +pty2+ ")");
        
    }
	
	private void verifyPixelsLine(Line2D.Double lin, HoughLine hl){
		int[] xPoints = hc.getXPixelsPetriBound();
		int[] yPoints = hc.getYPixelsPetriBound();
		ImageProcessor ip = hc.getImageOriginal();
		//int x1, x2, y1, y2;
		
		// Ordenação dos vetores xPoints e yPoints.
		int menorY = yPoints[0];
		int X = 0;
		int indice = 0;
		int j = 0;
		
	    do{
	    	for(int i = 0; i < yPoints.length; i++){
	    		if(yPoints[i] < menorY){
	    			menorY = yPoints[i];
	    			indice = i;
	    			X = xPoints[i];
	    		}
	    	}
	    	int aux = yPoints[j];
	    	yPoints[j] = menorY;
	    	yPoints[indice] = aux;
	    	
	    	aux = xPoints[j];
	    	xPoints[j] = X;
	    	xPoints[indice] = aux;
	    	j++;
	    }while(j <= yPoints.length);
		
	    
	    int k = 0;
	    do{
	    	for(int i = 0; i < yPoints.length; i++){
		    	if(yPoints[i] == yPoints[i+1]){
		    		if(xPoints[i] > xPoints[i+1]){
		    			int aux = yPoints[i];
		    			yPoints[i] = yPoints[i+1];
		    			yPoints[i+1] = aux;
		    			
		    			aux = xPoints[i];
		    			xPoints[i] = xPoints[i+1];
		    			xPoints[i+1] = aux;
		    		}
		    	}
	    
	    	}
	    	k++;
	    }while(k < yPoints.length);
	    
	    ip.fillPolygon(new Polygon(xPoints, yPoints, xPoints.length));
	    /*PolygonRoi pr = new PolygonRoi(xPoints, yPoints, xPoints.length, Roi.POLYGON);
	    pr.drawPixels(ip);*/
	    
		//for(int i = 0; i < xPoints.length; i++){
			/*if(lin.contains(xPoints[i], yPoints[i]) == true){
				JOptionPane.showMessageDialog(null, "Encontrou");
			}*/
			
			/*if(yPoints[i] - ym == (int) (angle*(xPoints[i] - xm))){
				JOptionPane.showMessageDialog(null, "Encontrou. X: " +xPoints[i]+ " Y: "+ yPoints[i]);
				ip.fillOval(xPoints[i], yPoints[i], 50, 50);
			}*/
			
			
		//}
		
		//x1 = (int) (lin.x1-(hc.getXC()+hc.getRadius()));
		//y1 = (int) (lin.y1-(hc.getYC()+hc.getRadius()));
		//JOptionPane.showMessageDialog(null, "Encontrou. X: " +x1+ " Y: "+ y1);
		//ip.fillOval(x1, y1, 50, 50);
		new ImagePlus("Poligono circunferencia",ip).show();
	}
	
}