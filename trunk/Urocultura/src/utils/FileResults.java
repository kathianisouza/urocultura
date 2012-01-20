package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileResults {

	// método que recebe os parâmetros a serem arquivados.
	/*No caso do Hough Circles estou arquivando dados como:
	 *  - Nome da imagem que contém a qt de colonias contadas manualmente (idImage)
	 *  - Contagem de circulos (countCircles)
	 *  - Valor atual do threshold (threshold) 
	 *  - Valor Pixel de parada de contagem (limitCounter)
	 *  - Se uso ou não Watershed (watershed)
	 *  - Se uso filtro de Canny ou Sobel (edges)
	 *  Todos estes parametros citados são os que variam e terá quer ser alterado no template matching, ou seja,
	 *  serão substituidos pelos parametros que variam no template matching.
	 *  
	 *  Outros parametros como:
	 *   - index: esta variável recebe o índice do vetor que guarda a string com o diretório do template.
	 *            Estou utilizando-a para fazer a titulação do arquivo no excel. Se esta variável tiver como valor zero,
	 *            isto indica que estou salvando no arquivo pela primeira vez, portanto, será o momento de titular o arquivo.
	 *            
	 *            A titulação seria:
	 *            
	 *            IMAGEM | COLONIAS | THRESHOLD | PIXEL | WATERSHED | EDGE FILTER
	 *            
	 *    - directoryFile: String no qual contém o diretório de onde será salvo o arquivo, utilizado justamente 
	 *    					para a criação deste arquivo. 
	 *  */
		
	public static void fileHoughCircles(int index, String idImage, String directoryFile, int countCircles, int threshold, int limitCounter, String watershed, String edges){
		FileWriter out;
    	
    	try{
    		out = new FileWriter(new File(directoryFile + "Resultados " + "T(" + threshold + ") " + "P(" + limitCounter + ") "
    							+ "W(" + watershed + ")" + "E(" + edges + ")" + ".xls"),true);
    		if(index == 0){
    			out.write("Imagem" + "\t" + "Colônias" + "\t" + "Threshold" + "\t" + "Pixel" + "\t" + "Watershed" + "Edge Filter" + "\n");
    		}
    		
    		out.write(idImage + "\t" + countCircles + "\t" + threshold + "\t" + limitCounter + "\t" + watershed + "\t" + edges + "\n");
    		out.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	public static void fileTemplateMatching(int index,String name,String directoryFile, int cont){
		File file = new File(directoryFile+ "Resultado_contador.xls");
		FileWriter out;
		
		try{
			 out =  new FileWriter(new File(directoryFile + "Resultado_contador" + ".xls"),true);
			if(file.length()==0){
				out.write("Nome" + "\t"  + "cont 0,94" + "\t" + "cont 0,95" + "\t" + "cont 0,96" + "\t"+  "cont 0,97" + "\t"+ "cont 0,98" + "\t" + "cont 0,99" + "\t" + "cont 1" + "\n");
			}
		if(index==0)
			out.write(name + "\t" + cont +"\t");
		else
			out.write(  cont +"\t");
		if(index==6)  
			 out.write("\n");

		out.close();
		}catch(IOException e){
    		e.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	
	}
}