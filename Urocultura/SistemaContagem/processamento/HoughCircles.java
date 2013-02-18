package processamento;

import java.awt.Point;
import java.awt.Rectangle;


import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import interfaces.JanelaPrincipal;

public class HoughCircles {

    ImageProcessor ipImageOriginal;
    ImageProcessor ipImagePreProcessed;
    ImageProcessor ipImagePetri;
    ImageProcessor ipImagePetriPreProcessed;
    int imageOriginalValues[];
    byte imagePreProcessingValues[];
    int imagePetriValues[];
    byte imagePetriPreProcessingValues[];
    static final float ALPHA = (float) 1.1; // utilizado no filtro de Canny.
    static final float SIGMA = (float) 1.5; // utilizado no filtro Gaussiano.
    public int radiusMin = 725; // Find circles with radius grater or equal radiusMin
    public int radiusMax = 725; // Find circles with radius less or equal radiusMax
    public int radiusInc = 1; // Increment used to go from radiusMin to radiusMax
    double houghValues[][][]; // Hough Space Values
    double houghValuesPetri[][][]; // Hough Space Values from Petri dishes.
    public int width; // Hough Space width (depends on image width)
    public int height; // Hough Space heigh (depends on image height)
    public int depth; // Hough Space depth (depends on radius interval)
    public int offset; // Image Width
    public int offx; // ROI x offset
    public int offy; // ROI y offset
    int xc; // Abscissa do pixel central da circunfer�ncia.
    int yc; // Ordenada do pixel central da circunfer�ncia.
    int radius; // Raio da circunfer�ncia localizada.
    int lut[][][]; // LookUp Table for rsin e rcos values
    int[] xPoints; // Coordenadas da borda da circunfer�ncia detectada.
    int[] yPoints; // *************************************************
    Point centerPoint[];
    int dadosHough[];
    int countCircles;
    boolean useThreshold = false;
    
    public HoughCircles(ImagePlus imp) {

        WindowManager.setTempCurrentImage(imp);

        ipImageOriginal = imp.getProcessor(); // processo da imagem original.
        imageOriginalValues = (int[]) ipImageOriginal.getPixels(); // captura valores da imagem original.
        
        imp = pre_processing(imp, HoughCircles.SIGMA, HoughCircles.ALPHA);
        
        ipImagePreProcessed = imp.getProcessor(); // captura imagem pré-processada.
        imagePreProcessingValues = (byte[]) ipImagePreProcessed.getPixels(); // captura valores da imagem pré-processada.

        houghProcess(ipImagePreProcessed);
    }

    private ImagePlus pre_processing(ImagePlus imp, float sigma, float alpha) {

        Image_Edge canny = new Image_Edge();
        
        IJ.run("Enhance Contrast", "saturated=5");
        IJ.run("Gaussian Blur...", "sigma=" + sigma);
        IJ.run("8-bit");

        canny.setAlpha(alpha);
        return canny.EdgeDetection(imp);
    }

    private void houghProcess(ImageProcessor ip) {
        Rectangle r = ip.getRoi();

        offx = r.x;
        offy = r.y;
        width = r.width;
        height = r.height;
        offset = ip.getWidth();
        depth = ((radiusMax - radiusMin) / radiusInc) + 1;

        houghTransform();

        getCenterDishesPetri();

        createPetriImage();
        //createPetriImagePreProcessed();
     }

    // Cria o espa�o de hough.
    private void houghTransform() {

        int lutSize = buildLookUpTable();

        houghValues = new double[width][height][depth];

        int k = width - 1;
        int l = height - 1;

        for (int y = 1; y < l; y++) {
            for (int x = 1; x < k; x++) {
                for (int radius1 = radiusMin; radius1 <= radiusMax; radius1 = radius1
                                + radiusInc) {
                    if (imagePreProcessingValues[(x + offx) + (y + offy)
                            * offset] != 0) {// Edge pixel found
                        int indexR = (radius1 - radiusMin) / radiusInc;
                        for (int i = 0; i < lutSize; i++) {

                            int a = x + lut[1][i][indexR]; // x + rsin
                            int b = y + lut[0][i][indexR]; // y + rcos
                            if ((b >= 0) & (b < height) & (a >= 0)
                                    & (a < width)) {
                                houghValues[a][b][indexR] += 1;
                            }
                        }

                    }
                }
            }

        }

    }

    private int buildLookUpTable() {

        int i = 0;
        int incDen = Math.round(8F * radiusMin); // increment denominator

        lut = new int[2][incDen][depth];

        for (int radius1 = radiusMin; radius1 <= radiusMax; radius1 = radius1
                        + radiusInc) {
            i = 0;
            for (int incNun = 0; incNun < incDen; incNun++) {
                double angle = (2 * Math.PI * (double) incNun)
                        / (double) incDen;
                int indexR = (radius1 - radiusMin) / radiusInc;
                int rcos = (int) Math.round((double) radius1 * Math.cos(angle));
                int rsin = (int) Math.round((double) radius1 * Math.sin(angle));
                if ((i == 0) | (rcos != lut[0][i][indexR])
                        & (rsin != lut[1][i][indexR])) {
                    lut[0][i][indexR] = rcos;
                    lut[1][i][indexR] = rsin;
                    i++;
                }
            }
        }

        return i;
    }

    // Detecta a circunfer�ncia atrav�s do espa�o de hough. Salva os valores de
    // xc, yc e radius.
    void getCenterDishesPetri() {

        int xMax = 0;
        int yMax = 0;
        int rMax = 0;

        double counterMax = -1;
        for (int radius1 = radiusMin; radius1 <= radiusMax; radius1 = radius1
                        + radiusInc) {

            int indexR = (radius1 - radiusMin) / radiusInc;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (houghValues[x][y][indexR] > counterMax) {
                        counterMax = houghValues[x][y][indexR];
                        xMax = x;
                        yMax = y;
                        rMax = radius1;
                    }
                }
            }
        }

        xc = xMax;
        yc = yMax;
        radius = rMax;

        getPixelsDishesPetri(xMax, yMax, rMax);
    }

    // Captura os valores do espa�o de hough pertencentes a placa de Petri.
    private void getPixelsDishesPetri(int x, int y, int radius) {
        houghValuesPetri = new double[width][height][depth];

        double Squared = radius * radius;

        int y1 = (int) Math.floor((double) y - radius);
        int y2 = (int) Math.ceil((double) y + radius) + 1;
        int x1 = (int) Math.floor((double) x - radius);
        int x2 = (int) Math.ceil((double) x + radius) + 1;

        if (y1 < 0) {
            y1 = 0;
        }
        if (y2 > height) {
            y2 = height;
        }
        if (x1 < 0) {
            x1 = 0;
        }
        if (x2 > width) {
            x2 = width;
        }

        for (int r = radiusMin; r <= radiusMax; r = r + radiusInc) {
            int indexR = (r - radiusMin) / radiusInc;
            for (int i = y1; i < y2; i++) {
                for (int j = x1; j < x2; j++) {
                    if (Math.pow(j - x, 2D) + Math.pow(i - y, 2D) < Squared) {
                        houghValuesPetri[j][i][indexR] = houghValues[j][i][indexR];
                    }
                }
            }
        }
    }

    private void createPetriImage() {
        ipImagePetri = new ColorProcessor(width, height);
        imagePetriValues = (int[]) ipImagePetri.getPixels();

        for (int radius1 = radiusMin; radius1 <= radiusMax; radius1 = radius1
                        + radiusInc) {
            int indexR = (radius1 - radiusMin) / radiusInc;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (houghValuesPetri[x][y][indexR] > 0) {

                        imagePetriValues[(x + offx) + (y + offy) * offset] = imageOriginalValues[(x + offx)
                                + (y + offy) * offset];
                    }
                }
            }
        }

        ipImagePetri.setPixels(imagePetriValues);
    }

    private void createPetriImagePreProcessed() {
        ipImagePetriPreProcessed = new ByteProcessor(width, height);
        imagePetriPreProcessingValues = (byte[]) ipImagePetriPreProcessed.getPixels();

        for (int radius1 = radiusMin; radius1 <= radiusMax; radius1 = radius1
                        + radiusInc) {
            int indexR = (radius1 - radiusMin) / radiusInc;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (houghValuesPetri[x][y][indexR] > 0) {

                        imagePetriPreProcessingValues[(x + offx) + (y + offy) * offset] = imagePreProcessingValues[(x + offx)
                                + (y + offy) * offset];
                    }
                }
            }
        }

        ipImagePetriPreProcessed.setPixels(imagePetriPreProcessingValues);
    }
    
    // verifica quais os pixels pertencem a borda da circunfer�ncia
    private void verifyPixelsBound(int[] imagePetriValues) {
        xPoints = new int[imagePetriValues.length];
        yPoints = new int[imagePetriValues.length];
        int i = 0;
        ImageProcessor ip = ipImageOriginal;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (imagePetriValues[(x + offx) + (y + offy) * offset] != 0) {
                    int a = (x - xc) * (x - xc);
                    int b = (y - yc) * (y - yc);
                    int c = radius * radius;
                    // JOptionPane.showMessageDialog(null, "A+B: " +(a+b)+
                    // " C: "+c);
                    if (a + b >= c - (c * 0.01)) {
                        // ip.fillOval(x, y, 1, 1);
                        xPoints[i] = x;
                        yPoints[i] = y;
                        i++;
                        // JOptionPane.showMessageDialog(null, "X: " +x+
                        // " Y: "+y);
                    }
                }
            }
        }
        // new ImagePlus("pixels desenhado",ip).show();
    }

    // GETS
    public ImageProcessor getImageOriginal() {
        return ipImageOriginal;
    }

    public ImageProcessor getImagePreProcessed() {
        return ipImagePreProcessed;
    }

    public ImageProcessor getImagePetri() {
        return ipImagePetri;
    }

    public ImageProcessor getImagePetriPreProcessed() {
        return ipImagePetriPreProcessed;
    }

    public int getXC() {
        return xc;
    }

    public int getYC() {
        return yc;
    }

    public double getRadius() {
        return radius;
    }

    public int[] getXPixelsPetriBound() {
        return xPoints;
    }

    public int[] getYPixelsPetriBound() {
        return yPoints;
    }
}
