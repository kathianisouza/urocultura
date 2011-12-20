/*C�digo importado de Hough_Circles.java (Hemerson Pistori),
 * com algumas adapta��es para contagem de col�nias bacterianas
 * em exames de urina. 
 */
package core;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ij.gui.GenericDialog;
import ij.io.Opener;
import ij.plugin.filter.PlugInFilter; 
import ij.ImagePlus; 
import ij.process.ByteProcessor;
import ij.process.ImageProcessor; 
import ij.IJ;
import javax.swing.JOptionPane;

public class Counter_HC implements PlugInFilter {
	public int radiusMin = 10;  // Find circles with radius grater or equal radiusMin
    public int radiusMax = 20;  // Find circles with radius less or equal radiusMax
    public int radiusInc = 2;  // Increment used to go from radiusMin to radiusMax
	
    byte imageValues[]; // Raw image (returned by ip.getPixels())
    double houghValues[][][]; // Hough Space Values
    public int width; // Hough Space width (depends on image width)
    public int height;  // Hough Space heigh (depends on image height)
    public int depth;  // Hough Space depth (depends on radius interval)
    public int offset; // Image Width
    public int offx;   // ROI x offset
    public int offy;   // ROI y offset
    int lut[][][]; // LookUp Table for rsin e rcos values

    int countCircles;
    String directoryImages = "D:\\Documentos\\SI - UFGD\\Projeto Automatiza��o na Contagem de Col�nias\\Templates\\image1\\Samples\\";
    String directoryFile = "D:\\Documentos\\SI - UFGD\\Projeto Automatiza��o na Contagem de Col�nias\\Templates\\image1\\";
    
    ImagePlus imp = null;
    
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL + NO_CHANGES;
	}

	public void run(ImageProcessor ip) {
		// L� entradas do usu�rio. Diret�rio das templates para contagem, diret�rio para salvar arquivo .xls
		getParameters();
		
		String[] list = new String[70];
    	File file = new File(directoryImages);
    	Opener op = new Opener();
    	
    	if(file.exists()){
    		list = file.list();
    	}
    	
    	for(int i = 0; i < list.length; i++){
    		
    		// Referencia imagem aberta.
    		imp = op.openImage(directoryImages + list[i]);
    		ip = imp.getProcessor();
    		
    		//Pre processamento
    		ip = ip.convertToByte(true);
    		ip.smooth();
    		ip.findEdges();
    		ip.threshold(80);
    		IJ.run("Convert to Mask");
    	
    		imageValues = (byte[])ip.getPixels();
            Rectangle r = ip.getRoi();
    		
            offx = r.x;
            offy = r.y;
            width = r.width;
            height = r.height;
            offset = ip.getWidth();
            depth = ((radiusMax-radiusMin)/radiusInc)+1;
            
    		houghTransform();

            // Create image View for Hough Transform.
            ImageProcessor newip = new ByteProcessor(width, height);
            byte[] newpixels = (byte[])newip.getPixels();
            createHoughPixels(newpixels);
    	
            getCenterPoints();
            
            //JOptionPane.showMessageDialog(null,"Col�nias: " + countCircles);
            
            // Arquiva os resultados em .xls
            FileResults.fileHoughCircles(list[i],directoryFile,countCircles);
            //fileResults(list[i]);
            
            countCircles = 0;
    	}
		
	}

	private int buildLookUpTable() {

        int i = 0;
        int incDen = Math.round (8F * radiusMin);  // increment denominator

        lut = new int[2][incDen][depth];

        for(int radius = radiusMin;radius <= radiusMax;radius = radius+radiusInc) {
            i = 0;
            for(int incNun = 0; incNun < incDen; incNun++) {
                double angle = (2*Math.PI * (double)incNun) / (double)incDen;
                int indexR = (radius-radiusMin)/radiusInc;
                int rcos = (int)Math.round ((double)radius * Math.cos (angle));
                int rsin = (int)Math.round ((double)radius * Math.sin (angle));
                if((i == 0) | (rcos != lut[0][i][indexR]) & (rsin != lut[1][i][indexR])) {
                    lut[0][i][indexR] = rcos;
                    lut[1][i][indexR] = rsin;
                    i++;
                }
            }
        }

        return i;
    }

    private void houghTransform () {

        int lutSize = buildLookUpTable();

        houghValues = new double[width][height][depth];

        int k = width - 1;
        int l = height - 1;

        for(int y = 1; y < l; y++) {
            for(int x = 1; x < k; x++) {
                for(int radius = radiusMin;radius <= radiusMax;radius = radius+radiusInc) {
                    if( imageValues[(x+offx)+(y+offy)*offset] != 0 )  {// Edge pixel found
                        int indexR=(radius-radiusMin)/radiusInc;
                        for(int i = 0; i < lutSize; i++) {

                            int a = x + lut[1][i][indexR]; // x + rsin
                            int b = y + lut[0][i][indexR]; // y + rcos
                            if((b >= 0) & (b < height) & (a >= 0) & (a < width)) {
                                houghValues[a][b][indexR] += 1;
                            }
                        }

                    }
                }
            }

        }

    }
    
    private void createHoughPixels (byte houghPixels[]) {
        double d = -1D;
        for(int j = 0; j < height; j++) {
            for(int k = 0; k < width; k++)
                if(houghValues[k][j][0] > d) {
                    d = houghValues[k][j][0];
                }

        }

        for(int l = 0; l < height; l++) {
            for(int i = 0; i < width; i++) {
                houghPixels[i + l * width] = (byte) Math.round ((houghValues[i][l][0] * 255D) / d);
            }

        }
    }

    private void getCenterPoints () {


        int xMax = 0;
        int yMax = 0;
        int rMax = 0;
        
        double counterMax = -1;

        do{
            counterMax = -1;
            for(int radius = radiusMin;radius <= radiusMax;radius = radius+radiusInc) {


                int indexR = (radius-radiusMin)/radiusInc;
                for(int y = 0; y < height; y++) {
                    for(int x = 0; x < width; x++) {
                        if(houghValues[x][y][indexR] > counterMax) {
                            counterMax = houghValues[x][y][indexR]; 
                            
                            xMax = x;
                            yMax = y;
                            rMax = radius;
                        }
                    }
                }
            }
            
            countCircles += 1;
            clearNeighbours(xMax,yMax,rMax); 
            
        }while(counterMax > 55);   // ajustar condi��o de parada
    }

    private void clearNeighbours(int x,int y, int radius) {


        // The following code just clean the points around the center of the circle found.


        double halfRadius = radius / 2.0F;
        double halfSquared = halfRadius*halfRadius;


        int y1 = (int)Math.floor ((double)y - halfRadius);
        int y2 = (int)Math.ceil ((double)y + halfRadius) + 1;
        int x1 = (int)Math.floor ((double)x - halfRadius);
        int x2 = (int)Math.ceil ((double)x + halfRadius) + 1;



        if(y1 < 0)
            y1 = 0;
        if(y2 > height)
            y2 = height;
        if(x1 < 0)
            x1 = 0;
        if(x2 > width)
            x2 = width;



        for(int r = radiusMin;r <= radiusMax;r = r+radiusInc) {
            int indexR = (r-radiusMin)/radiusInc;
            for(int i = y1; i < y2; i++) {
                for(int j = x1; j < x2; j++) {	      	     
                    if(Math.pow (j - x, 2D) + Math.pow (i - y, 2D) < halfSquared) {
                        houghValues[j][i][indexR] = 0.0D;
                    }
                }
            }
        }

    }

    /*void fileResults(String idImage){
    	FileWriter out;
    	
    	try{
    		out = new FileWriter(new File(directoryFile + "Resultados.xls"),true);
    		out.write(idImage + "\t" + countCircles + "\n");
    		out.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }*/

    void getParameters(){
    	
    	GenericDialog gd = new GenericDialog("Parameters");
    	
    	gd.addStringField("Templates directory: ",directoryImages,10);
    	gd.addStringField("Save file (.xls) directory : ",directoryFile,10);
    	gd.showDialog();
    	directoryImages = gd.getNextString();
    	directoryFile = gd.getNextString();
    }
}