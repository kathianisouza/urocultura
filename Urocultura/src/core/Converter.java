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
import ij.plugin.filter.GaussianBlur;



public class Converter implements PlugInFilter {
        static int heigth_original = 0;
        static int width_original = 0;
        static int height_roi = 0;
        static int width_roi = 0;
        static int left  = 0;
        static int top= 0;
        static String diretorio ;
        ImagePlus imp = null;
        float[][] array_atual;
        float [][] array_original ;
        float [][] array_template ;
        float [][]image_correlation;
        int cont = 0; int test;
        int krad; int x=0;
        static GenericRecallableDialog gd;
        static ArrayDisplay origAd;
        double  max = 0.92;
        static String directoryFile = "C:\\Users\\Kathiani\\Documents\\Iniciação Científica\\Seleção de Imagens\\image4\\";
        String directoryImages = "C:\\Users\\Kathiani\\Documents\\Iniciação Científica\\Seleção de Imagens\\image4\\Samples\\";
         private int nPasses = 1;
         private static boolean sigmaScaled = false;
        
         public int setup(String arg, ImagePlus imp) { 
                this.imp = imp; 
                 return DOES_ALL + NO_CHANGES; 
         } 

        

        public void run(ImageProcessor ip){
               

        ImagePlus implus =  WindowManager.getCurrentImage();
        ImageTools objeto =  new ImageTools();
         array_atual =  objeto.getCurrentImageMatrix(array_atual,implus);
	    String[] list = new String[100];
    	     File file = new File(directoryImages);
		if(file.exists()){
    			list = file.list();// Recebe o diretório de cada arquivo dentro da pasta
    		}

		 //GaussianBlur filtro = new GaussianBlur();
        	             //filtro.blurGaussian(ip, 20,20,20);

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
           					array_template[i][j] = array_atual[roiTLx + i][roiTLy + j];// não era conhecida.
		}
	
	    //int  krad = (int) (gd.getScrollBarValue(0));
        //array_template = ImageTools.calcCircKernel(9);
	    //ImageTools.invertKernel(array_template)
		//for(int n = 0; n < 5;n++){
		//Opener op = new Opener();
		//imp = op.openImage(directoryImages + list[n]);
		String name =    imp.getTitle();
		//JOptionPane.showMessageDialog(null,name);
		array_original = ImageTools.getCurrentImageMatrix(array_original, imp);
		image_correlation = new float[array_original.length][array_original[0].length];
        image_correlation = objeto.statsCorrelation(array_original,array_template);
		           	
		
        for(int k=0;k<9;k++){
                    for(int i=0; i<image_correlation.length ;i++){
                            for(int j=0; j<image_correlation[0].length ;j++){
                                 if(image_correlation[i][j] >= max)
                                             cont++;
                            }
                    }
               	 //JOptionPane.showMessageDialog(null,cont);
        FileResults.fileTemplateMatching(x,name,directoryFile,cont);
                        max = max + 0.01;
                        x = x+1;
                        cont = 0;
              	}
	x = 0;
	max = 0.92;
	//}

	JOptionPane.showMessageDialog(null,"Processo Finalizado");
        }
}