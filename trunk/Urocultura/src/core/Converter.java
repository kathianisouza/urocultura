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
	static int heigth_original = 0;
	static int width_original = 0;
	static int height_roi = 0;
	static int width_roi = 0;
	static int left  = 0;
	static int top= 0;
	static String diretorio ;
	ImagePlus imp = null;
	float [][] array_original ;
	float [][] array_template ;
	float [][]image_correlation;
	int cont = 0; int test;
	int krad; int x=0;
	static GenericRecallableDialog gd;
	static ArrayDisplay origAd;
	double  max = 0.94;
    String directoryFile = "C:\\Users\\Kathiani\\Documents\\Iniciação Científica\\Resultados\\";

	
	 public int setup(String arg, ImagePlus imp) { 
    		this.imp = imp; 
       		 return DOES_ALL + NO_CHANGES; 
   	 } 

	public void run(ImageProcessor ip){
		
		ImagePlus var =  WindowManager.getCurrentImage();
		String name =	 var.getTitle();
		//JOptionPane.showMessageDialog(null,name);
		
		ImageTools objeto =  new ImageTools();
		array_original =  objeto.getCurrentImageMatrix(array_original);

		/*ImageWindow curImgWindow = WindowManager.getCurrentWindow();
		WindowManager.removeWindow(curImgWindow);//Remove a janela especificada a partir do menu Window.
	   	curImgWindow.setVisible(false);//Mostra ou oculta este componente, dependendo do valor do parâmetro b.
	   	curImgWindow.dispose();*/
		
		if(ip.getRoi()!= null){
			Create_A_Template_ crop = new  Create_A_Template_();
			array_template = crop.cropForKernel(array_original);
			crop.displayKernel(array_template);
		}
		
	    ImageTools.filterGaussian(array_original);	
	    ArrayDisplay origAd = new ArrayDisplay(array_original,"imagem original");
	    ImageTools.autoSetWindowLevel();
		image_correlation = new float[array_original.length][array_original[0].length];
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //Obtém o tamanho da tela
		//gd.setLocation(screen.width -10 -gd.getSize().width, screen.height/8);
		IJ.getInstance().setLocation(screen.width/4, 2);//Move este retângulo para o local especificado. screen.width: largura;
		//IJ.getTextPanel().getParent().setLocation(screen.width -10- gd.getSize().width,screen.height/8+ gd.getSize().height);//Abre o "Results" janela se de momento não é aberto.setlocation: Move-se este componente para um novo local. 
		//origAd.setScreenLocation(screen.width -20- gd.getSize().width - origAd.getScreenSize().width,screen.height/8);
		image_correlation = objeto.statsCorrelation(array_original,array_template);
		
		
			for(int k=0;k<7;k++){
				for(int i=0; i<image_correlation.length ;i++){
					for(int j=0; j<image_correlation[0].length ;j++){
						if(image_correlation[i][j] >= max)
							cont++;
					}
				
				}
	        // JOptionPane.showMessageDialog(null,cont);
	        FileResults.fileTemplateMatching(x,name,directoryFile,cont);
			max = max + 0.01;
			x = x+1;
			
		}
	}
}
	