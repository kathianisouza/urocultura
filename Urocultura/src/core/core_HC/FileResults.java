import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileResults {
		
	// pre processamento 2
	public static void fileHoughCircles(int index, String idImage, String directoryFile, int countCircles, float sigma, float alpha, int threshold){
		FileWriter out;
    	
    	try{
    		out = new FileWriter(new File(directoryFile + "Pre processamento 2 - " + "S(" + sigma + ") " + "A(" + alpha + ") " + "T(" + threshold + ") " + ".xls"),true);
    		if(index == 0){
    			out.write("Imagem" + "\t" + "Colônias" + "\t" + "\n");
    		}
    		
    		out.write(idImage + "\t" + countCircles + "\t" + "\n");
    		out.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
}