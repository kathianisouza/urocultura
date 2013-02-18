package processamento;

import java.io.File;


import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import interfaces.JanelaPrincipal;

public class TesteAreaTemplate {

    private static final int NO_CHANGES = 0;
    private static final int NO_LUT_UPDATE = 0;
    int cont = 0;
    //static ImagePlus imp = null;
    float areaTemplate = 0;
    
    ImagePlus impTemplate; // template.
    ImagePlus impImagem; // imagem de colonias.
    ImageProcessor ip; // processo da imagem de colonias.
    Opener op = new Opener();
    //String[] list = new String[100];
    String fileName;
    String directoryImages = "C:\\Users\\Kathiani\\Documents\\IC 2012-2013\\ImagensTeste\\Imagens Regi�o de Interesse\\Imagens\\";

    public TesteAreaTemplate(ImageProcessor ip) {
        File file = new File(JanelaPrincipal.pathImagem);
       
        if (file.exists()) {
            fileName = file.getName();// Recebe o nome de cada arquivo dentro da pasta
        }

        this.ip = ip;
        impImagem = new ImagePlus(fileName, this.ip);
        contarColoniasWatershedArea();

    }

    private int contarColoniasWatershedArea() {
        int totalColonias = 0;
        double porc = 0.20;
        float divColonias = 0;
        areaTemplate = getAreaTemplate();
        float[] areas = getAreasParticlesWatershed();
        System.out.println("A area do template eh: " + areaTemplate);
        for (int x = 0; x < areas.length; x++) {
            if ((areas[x] > (areaTemplate - (porc * areaTemplate))) && (areas[x] <= (areaTemplate + (porc * areaTemplate)))) {
                totalColonias++;
            } else {
                if (areas[x] > (areaTemplate + (porc * areaTemplate))) {
                    divColonias = divColonias + (areas[x] / (areaTemplate));
                }
            }
        }
        FileResults.fileDivcol(fileName, totalColonias, divColonias, "C:\\Users\\Edneia\\Desktop\\resultados\\");
        System.out.println("Colonias integras: " + totalColonias);
        System.out.println("Divisao pela area: " + divColonias);
        return totalColonias;
    }

    private float[] getAreasParticlesWatershed() {
        /*Opener op = new Opener();
        imp = op.openImage(directoryImages + "image17_1406c.jpg");
        imp.show();
        ImageProcessor ip2 = imp.getProcessor();*/
        float[][] imagem = new float[ip.getWidth()][ip.getHeight()];
        for (int w = 0; w < ip.getWidth(); w++) {
            for (int h = 0; h < ip.getHeight(); h++) {
                imagem[w][h] = (float) ip.getPixel(w, h);
            }
        }
        ArrayDisplay display = new ArrayDisplay(imagem, "original image");
        ImageTools.autoSetWindowLevel();
        //ImageTools.setThreshold(Display,0.4,1); 
        IJ.run("Threshold");
        IJ.run("Invert");
        IJ.run("Watershed");
        float[] areas = ImageTools.getArea(display, 0, 2000);
        return areas;
    }

    public float getAreaTemplate() {
        Opener op = new Opener();
        // sempre passar endereco da template.
        //imp = op.openImage("C:\\Users\\Kathiani\\Documents\\IC 2012-2013\\ImagensTeste\\Template\\template.jpg");
        impTemplate = op.openImage("C:\\Users\\Edneia\\Desktop\\templates\\template.jpg");
        impTemplate.show();
        ImageProcessor ip2 = impTemplate.getProcessor();
        float[][] imagem = new float[ip2.getWidth()][ip2.getHeight()];
        for (int w = 0; w < ip2.getWidth(); w++) {
            for (int h = 0; h < ip2.getHeight(); h++) {
                imagem[w][h] = (float) ip2.getPixel(w, h);
            }
        }
        ArrayDisplay Display = new ArrayDisplay(imagem, "templateResult");
        ImageTools.autoSetWindowLevel();
        ImageConverter convert = new ImageConverter(impTemplate);
        convert.convertToGray8();
        IJ.run("Threshold");
        Point2D[] particles = ImageTools.getResults(Display, 0, 2000);
        float[] area = ImageTools.getArea(Display, 0, 2000);
        return area[0];

    }
}