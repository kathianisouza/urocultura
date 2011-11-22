import ij.ImagePlus; 
import ij.gui.*;
import ij.plugin.filter.PlugInFilter; 
import ij.process.ImageProcessor; 
import java.awt.Rectangle; 
import ij.io.FileSaver;

public class Cut_Image implements PlugInFilter { 
        static int ht = 100; 
        static int wt = 100; 
        static int width = 0;
		static int height = 0;
		static int left = 0;
		static int top = 0;
        static String diretorio  = "C:\\";
		ImagePlus imp = null; 
        
    public int setup(String arg, ImagePlus imp) { 
    this.imp = imp; 
        return DOES_ALL + NO_CHANGES; 
    } 

    public void run(ImageProcessor ip) { 
		width = ip.getWidth();     
        height = ip.getHeight();
		

		if(!getParameters()) 
			return; 

       	if (ip.getRoi() != null) { 
               	Rectangle rect = ip.getRoi(); 
               	width = rect.width; 
               	height = rect.height; 
				left = rect.x;
				top = rect.y;
				width += left;
				height += top;
		} 
		
		int count = 1;
		
		for(int i = left;i < width;i += wt) {
			for(int j = top;j < height;j += ht){
				
				ip.setRoi(i,j,wt,ht); 
				if(j + ht > height){
					ip.setRoi(i,j-((j+ht)-height),wt,ht);
				}
				
				if(i + wt > width){
					ip.setRoi(i-((i+wt)-width),j,wt,ht);
				}
				
				if((j + ht > height) && (i + wt > width)){
					ip.setRoi(i-((i+wt)-width),j-((j+ht)-height),wt,ht);
				}
		
				ImageProcessor ip2 = ip.crop(); 
				ImagePlus imp2 = new ImagePlus(null,ip2); 
				FileSaver file = new FileSaver(imp2);	
				file.saveAsJpeg(diretorio+"sample"+count+"_"+(imp.getTitle())+".jpg");
				count++;
			}	
		}
	}
    
    boolean getParameters() { 
        GenericDialog gd = new GenericDialog("Cut Image"); 
        gd.addNumericField("Width Template", wt, 0); 
        gd.addNumericField("Height Template", ht, 0); 
        gd.addStringField("Directory",diretorio);
		gd.showDialog(); 
        if(gd.wasCanceled()) return false; 
	    wt = (int)gd.getNextNumber(); 
        ht = (int)gd.getNextNumber(); 
        diretorio = gd.getNextString();
		return true; 
    } 
} 
	
	
        
