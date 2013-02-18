package processamento;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class Image_Petri {

    ImageProcessor ipPlaca;
    ImageProcessor ipPlacaL1;
    ImageProcessor ipPlacaL2;

    public Image_Petri() {
    }

    public void execute(HoughCircles hc, Plugin_Hough_Linear phl){
        createImage(hc);
        new ImagePlus("Imagem Placa Inteira", ipPlaca).show();
    }
    
    private void createImage(HoughCircles hc) {
        int[] pixelsImagemOriginal = (int[]) hc.getImageOriginal().getPixels();
        int[] pixelsImagemPlaca = new int[pixelsImagemOriginal.length];
        
        // inicializa vetor.
        for(int i = 0; i < pixelsImagemPlaca.length; i++){
            pixelsImagemPlaca[i] = 0;
        }
        
        
        
    }
    
    
    /*private void createImageLado1() {
        // detecta linha horizontal.
        if (angle >= 1 && angle <= 2) {
            for (int i = 1; i < length; i++) {
                x1 = xs - (dy * i);
                y1 = ys + (dx * i);
                if (x1 >= (hc.getXC() + hc.getRadius()) || x1 <= (hc.getXC() - hc.getRadius())) {
                    break;
                }
            }

            for (int i = 1; i < length; i++) {
                x2 = xs + (dy * i);
                y2 = ys - (dx * i);
                if (x2 <= (hc.getXC() - hc.getRadius()) || x2 >= (hc.getXC() + hc.getRadius())) {
                    break;
                }
            }
        }

        // detecta linha vertical.
        if (angle < 1 || angle > 2) {
            for (int i = 1; i < length; i++) {
                x1 = xs - (dy * i);
                y1 = ys + (dx * i);
                if (y1 >= (hc.getYC() + hc.getRadius()) || y1 <= (hc.getYC() - hc.getRadius())) {
                    break;
                }
            }

            for (int i = 1; i < length; i++) {
                x2 = xs + (dy * i);
                y2 = ys - (dx * i);
                if (y2 <= (hc.getYC() - hc.getRadius()) || y2 >= (hc.getYC() + hc.getRadius())) {
                    break;
                }
            }
        }
    }*/

    private void createImageLado2() {
    }

    public ImageProcessor getIpPlaca() {
        return ipPlaca;
    }

    public ImageProcessor getIpPlacaL1() {
        return ipPlacaL1;
    }

    public ImageProcessor getIpPlacaL2() {
        return ipPlacaL2;
    }
}
