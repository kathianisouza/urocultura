
package core;

import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.*;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter; 
import ij.process.ImageProcessor; 
import java.awt.Rectangle; 
import java.io.File;
import ij.io.FileSaver;
import ij.io.Opener;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

import utils.FileResults;



public class Converter implements PlugInFilter {
	static int height= 0;
	static int width = 0;
	static int height_roi = 0;
	static int width_roi = 0;
	static int left  = 0;
	static int top= 0;
	static String diretorio ;
	ImagePlus imp = null;
	float [][] array_original ;
	float[][]  image_atual;
	float [][] array_template ;
	float [][]image_correlation;
	int cont = 0; int test;
	int krad; int x=0; int c=0;
	static GenericRecallableDialog gd;
	static ArrayDisplay origAd;
	double  max = 0.80;
	static String directoryFile = "C:\\Users\\Kathiani\\Documents\\Iniciação Científica\\Resultados\\";
	 String directoryImages = "C:\\Users\\Kathiani\\Documents\\Iniciação Científica\\Seleção de Imagens\\image4\\Samples\\";
	
	 public int setup(String arg, ImagePlus imp) { 
    		this.imp = imp; 
       		 return DOES_ALL + NO_CHANGES; 
   	 } 

	public void run(ImageProcessor ip) {
		ImagePlus var =  WindowManager.getCurrentImage();
		ImageTools objeto =  new ImageTools();
		image_atual =  objeto.getCurrentImageMatrix(image_atual,c,null,null);
		
		String[] list = new String[100];
    	File file = new File(directoryImages);
    	Opener op = new Opener();
		if(file.exists()){
    			list = file.list();// Recebe o diretório de cada arquivo dentro da pasta
    		}
		
		
		if(ip.getRoi()!= null){
     			Roi curRoi = WindowManager.getCurrentImage().getRoi();
    			Rectangle RoiRect = curRoi.getBounds();
    			int roiTLx = RoiRect.x;
     			int roiTLy = RoiRect.y;
    			int roiWidth = RoiRect.width;
    			int roiHeight = RoiRect.height;
    			if (roiWidth%2==0) roiWidth++;  // make sure it is an odd number
    			if (roiHeight%2==0) roiHeight++;
    			array_template = new float[roiWidth][roiHeight];
     			for (int i=0; i<roiWidth; i++)
        				for (int j=0; j<roiHeight; j++) 
           					array_template[i][j] = image_atual[roiTLx + i][roiTLy + j];// não era conhecida.
		}

		

		for(int n = 10; n <29;n++){
			c = 1;
			array_original = objeto.getCurrentImageMatrix(array_original,c,directoryImages,list[n]);
			image_correlation = new float[array_original.length][array_original[0].length];
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //Obtém o tamanho da tela
			IJ.getInstance().setLocation(screen.width/4, 2);//Move este retângulo para o local especificado. screen.width: largura;
			image_correlation = objeto.statsCorrelation(array_original,array_template);
		
			/*if(n==25){
				ArrayDisplay origAd = new ArrayDisplay(image_correlation,"imagem original");
	        	ImageTools.autoSetWindowLevel();
				JOptionPane.showMessageDialog(null,image_correlation[0].length);}*/
		
				for(int k=0;k<7;k++){   
					for(int i=0; i<image_correlation.length ;i++){
						for(int j=0; j<image_correlation[0].length ;j++){
							if(image_correlation[i][j] >= max)
								cont++;
						}
				
					}
	          
					FileResults.fileTemplateMatching(x,list[n],directoryFile,cont);
				cont = 0;
				max = max + 0.01;      // incrementa a variavel que representa o contador
				x = x+1;        //  variável de controle para escrita em arquivo		
		
				}
		x =0;             // atualizando variável para próxima imagem

	}
	JOptionPane.showMessageDialog(null,"Processo finalizado!");

	}


}


