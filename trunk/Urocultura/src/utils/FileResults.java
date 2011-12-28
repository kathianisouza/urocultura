package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileResults {

	
	public static void fileHoughCircles(int index, String idImage, String directoryFile, int countCircles, int threshold, int limitCounter, String watershed){
		FileWriter out;
    	
    	try{
    		out = new FileWriter(new File(directoryFile + "Resultados " + "T(" + threshold + ") " + "P(" + limitCounter + ") "
    							+ "W(" + watershed + ")" + ".xls"),true);
    		if(index == 0){
    			out.write("Imagem" + "\t" + "Col�nias" + "\t" + "Threshold" + "\t" + "Pixel" + "\t" + "Watershed" + "\n");
    		}
    		
    		out.write(idImage + "\t" + countCircles + "\t" + threshold + "\t" + limitCounter + "\t" + watershed + "\n");
    		out.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
}
