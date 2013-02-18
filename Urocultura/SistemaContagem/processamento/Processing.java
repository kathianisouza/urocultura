package processamento;

import ij.ImagePlus;

public class Processing {
    HoughCircles hc;
    Plugin_Hough_Linear phl;
    
    public Processing(){
    }

    public void houghCircles(ImagePlus imp){
        hc = new HoughCircles(imp);
    }

    public void houghLine(){
        phl = new Plugin_Hough_Linear(hc.getImagePreProcessed(), hc);
    }
    
    public HoughCircles getHc() {
        return hc;
    }

    public Plugin_Hough_Linear getPhl() {
        return phl;
    }


}
