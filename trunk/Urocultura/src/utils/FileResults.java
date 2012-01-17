package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileResults {

	// m�todo que recebe os par�metros a serem arquivados.
	/*No caso do Hough Circles estou arquivando dados como:
	 *  - Nome da imagem que cont�m a qt de colonias contadas manualmente (idImage)
	 *  - Contagem de circulos (countCircles)
	 *  - Valor atual do threshold (threshold) 
	 *  - Valor Pixel de parada de contagem (limitCounter)
	 *  - Se uso ou n�o Watershed (watershed)
	 *  - Se uso filtro de Canny ou Sobel (edges)
	 *  Todos estes parametros citados s�o os que variam e ter� quer ser alterado no template matching, ou seja,
	 *  ser�o substituidos pelos parametros que variam no template matching.
	 *  
	 *  Outros parametros como:
	 *   - index: esta vari�vel recebe o �ndice do vetor que guarda a string com o diret�rio do template.
	 *            Estou utilizando-a para fazer a titula��o do arquivo no excel. Se esta vari�vel tiver como valor zero,
	 *            isto indica que estou salvando no arquivo pela primeira vez, portanto, ser� o momento de titular o arquivo.
	 *            
	 *            A titula��o seria:
	 *            
	 *            IMAGEM | COLONIAS | THRESHOLD | PIXEL | WATERSHED | EDGE FILTER
	 *            
	 *    - directoryFile: String no qual cont�m o diret�rio de onde ser� salvo o arquivo, utilizado justamente 
	 *    					para a cria��o deste arquivo. 
	 *  */
		
	public static void fileHoughCircles(int index, String idImage, String directoryFile, int countCircles, int threshold, int limitCounter, String watershed, String edges){
		FileWriter out;
    	
    	try{
    		out = new FileWriter(new File(directoryFile + "Resultados " + "T(" + threshold + ") " + "P(" + limitCounter + ") "
    							+ "W(" + watershed + ")" + "E(" + edges + ")" + ".xls"),true);
    		if(index == 0){
    			out.write("Imagem" + "\t" + "Col�nias" + "\t" + "Threshold" + "\t" + "Pixel" + "\t" + "Watershed" + "Edge Filter" + "\n");
    		}
    		
    		out.write(idImage + "\t" + countCircles + "\t" + threshold + "\t" + limitCounter + "\t" + watershed + "\t" + edges + "\n");
    		out.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
}
