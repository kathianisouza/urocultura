package processamento;

import processamento.LinearHT.HoughLine;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import interfaces.JanelaPrincipal;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.List;


/**
 * This plugin implements a simple Hough Transform for straight lines. It
 * expects an 8-bit binary (edge) image, with background = 0 and edge pixels >
 * 0. Draws the resulting lines destructively into a new result image. Last
 * update: 2010-07-24
 */
public class Plugin_Hough_Linear {

    static int N_ANGLE = 256; // resolution of angle
    static int N_RADIUS = 256; // resolution of radius
    static int MAX_LINES = 1; // max. number of lines to be detected
    static int MIN_PNTSONLINE = 150; // min. number of points on each line
    static boolean SHOW_ACCUMULATOR = false;
    static boolean SHOW_LOCALMAXIMA = false;
    static boolean LIST_LINES = false;
    static boolean DRAW_LINES = true;
    static int LINE_WIDTH = 1;
    static Color LINE_COLOR = Color.GREEN;
    int ptx1 = 0, pty1 = 0, ptx2 = 0, pty2 = 0; // pontos calibrados da linha detectada.
    HoughCircles hc = null;
    ImagePlus imp = null; // input image
    ImageProcessor ip = null;
    Line2D.Double lin = null;
    HoughLine hl;
    
    public Plugin_Hough_Linear(ImageProcessor ip1, HoughCircles hc) {
        this.hc = hc;
        LinearHT ht = new LinearHT(ip1, hc, N_ANGLE, N_RADIUS);
     
        List<HoughLine> lines = ht.getMaxLines(MAX_LINES, MIN_PNTSONLINE);
      
        hl = lines.get(0);
        lin = hl.makeLine2D();
    }

    /*private void drawLines(List<HoughLine> lines) {
        ip = hc.getImagePetri();
        ip.setLineWidth(LINE_WIDTH);
        ip.setColor(LINE_COLOR);
        for (HoughLine hl : lines) {
            Line2D.Double lin = hl.makeLine2D();
            int u1 = (int) Math.rint(lin.x1);
            int v1 = (int) Math.rint(lin.y1);
            int u2 = (int) Math.rint(lin.x2);
            int v2 = (int) Math.rint(lin.y2);
            JOptionPane.showMessageDialog(null, "(" + u1 + "," + v1 + ")" + "("+ u2 + "," + v2 + ")");
            
            ip.drawLine(u1, v1, u2, v2);
            ip.setColor(Color.BLUE);
            ip.fillOval(hc.getXC(), hc.getYC(), 15, 15);      
        }
    }*/
    
    public Line2D.Double getLine(){
        return lin;
    }
    
    public HoughLine getHl() {
        return hl;
    }
    
    // verifica se tr�s pontos s�o colineares
	/*
     * private void pointsLineDetection(int x1, int y1, int x2, int y2, int
     * width, int height){
     * 
     * boolean[][] line = new boolean[width][height];
     * 
     * // inicializa vetor for(int i = 0; i < width; i++){ for(int j = 0; j <
     * height; j++){ line[i][j] = false; } }
     * 
     * // encontra o determinante pela regra de sarrus, se igual a 0, os pontos
     * s�o colineares. for(int i = 0; i < width; i++){ for(int j = 0; j <
     * height; j++){ int dp = (i*y1) + (j*x2) + (x1*y2); int ds = (j*x1) +
     * (i*y2) + (x2*y1);
     * 
     * if(dp - ds == 0){ JOptionPane.showMessageDialog(null, "Entrou" + i
     * +" - "+ j); line[i][j] = true; } } }
     * 
     * ImageProcessor ip = hc.getImageOriginal();
     * 
     * for(int i = 0; i < width; i++){ for(int j = 0; j < height; j++){
     * if(line[i][j]){ ip.fillOval(i, j, 5, 5); } } }
     * 
     * new ImagePlus("Linha desenhada", ip).show(); }
     */

    /*
     * private void lineTo(int x1, int x2, int y1, int y2, int width, int
     * height) {
     * 
     * boolean flag1 = true; boolean flag2 = true; int dx = x2-x1; int dy =
     * y2-y1; int absdx = dx>=0?dx:-dx; int absdy = dy>=0?dy:-dy; int n =
     * absdy>absdx?absdy:absdx; double xinc = (double)dx/n; double yinc =
     * (double)dy/n; double x = x1; double y = y1; n++; x1 = x2; y1 = y2; if
     * (n>1000000) return; do { // encontra x e y do ponto 1.
     * if((int)Math.round(x) >= 0 && (int)Math.round(y) >= 0 &&
     * (int)Math.round(x) < width && (int)Math.round(y) < height && flag1){ ptx1
     * = (int)Math.round(x); pty1 = (int)Math.round(y); flag1 = false; }
     * 
     * // encontra x e y do ponto 2. if((int)Math.round(x) >= width &&
     * (int)Math.round(y) > height && flag2){ ptx2 = (int)Math.round(x); pty2 =
     * (int)Math.round(y); flag2 = false; }
     * 
     * x += xinc; y += yinc; } while (--n>0);
     * JOptionPane.showMessageDialog(null, "(" +ptx1+ "," +pty1+ ")" + "("
     * +ptx2+ "," +pty2+ ")");
     * 
     * }
     */
    /*
     * private void lineTo(int x1, int x2, int y1, int y2, int width, int
     * height) {
     * 
     * boolean flag1 = true; boolean flag2 = true; int dx = x2-x1; int dy =
     * y2-y1; int absdx = dx>=0?dx:-dx; int absdy = dy>=0?dy:-dy; int n =
     * absdy>absdx?absdy:absdx; double xinc = (double)dx/n; double yinc =
     * (double)dy/n; double x = x1; double y = y1; n++; x1 = x2; y1 = y2; if
     * (n>1000000) return; do { // condi��es v�lidas caso a linha esteja
     * iniciando pelas laterais da imagem, ou seja, na horizontal. // encontra x
     * e y do ponto 1. if((int)Math.round(x) <= 0 && flag1){ // altera para
     * positivo o sinal da coordenada. if((int)Math.round(x) < 0){ ptx1 =
     * (int)Math.round(x) * (-1); } else{ ptx1 = (int)Math.round(x); }
     * 
     * if((int)Math.round(y) < 0){ pty1 = (int)Math.round(y) * (-1); } else{
     * pty1 = (int)Math.round(y); } flag1 = false; }
     * 
     * // encontra x e y do ponto 2. if((int)Math.round(x) <= -(width -1) &&
     * flag2){ if((int)Math.round(x) < 0){ ptx2 = (int)Math.round(x) * (-1); }
     * else{ ptx2 = (int)Math.round(x); }
     * 
     * if((int)Math.round(y) < 0){ pty2 = (int)Math.round(y) * (-1); } else{
     * pty2 = (int)Math.round(y); } flag2 = false; }
     * 
     * x += xinc; y += yinc; } while (--n>0);
     * JOptionPane.showMessageDialog(null, "(" +ptx1+ "," +pty1+ ")" + "("
     * +ptx2+ "," +pty2+ ")");
     * 
     * }
     */
    
}
