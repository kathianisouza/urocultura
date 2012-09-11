package core_contagem_atual;
import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.io.Opener;

import java.io.File;

import ij.plugin.filter.*;

public class Execution implements PlugInFilter {
	ImagePlus imp;

	String directoryImages;
	
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_ALL;
	}

	public void run(ImageProcessor ip) {
	
		getParameters();
								
		String[] list = new String[1];
		File file = new File(directoryImages);
				
						    	
		if(file.exists()){
			list = file.list();// Recebe o diretório de cada arquivo dentro da pasta
		}
				
		Opener op = new Opener();
		imp = op.openImage(directoryImages + list[0]);
		
		HoughCircles hc = new HoughCircles(imp);
		
	}

	// Lê entradas do usuário.
	private void getParameters(){
		GenericDialog gd = new GenericDialog("Parameters");
    	
    	gd.addStringField("Images directory: ",directoryImages,100);
    	gd.showDialog();
    	
    	directoryImages = gd.getNextString();
	}
	
	public static void main(String[] args){
		new Execution().run(null);
	}
}