package interfaces;

import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.Line;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import processamento.Processing;

public class JanelaImagem {

    Processing pr = new Processing();
    ImagePlus imp; // imagem com rois.
    ImageCanvas ic;
    OvalRoi placa;
    PolygonRoi poligonoPlaca;
    Line linha;
    PolygonRoi poligonoLinha;
    Overlay listaRoi = new Overlay();
    ImageProcessor ipPlaca;
    ImageProcessor ipPlacaL1;
    ImageProcessor ipPlacaL2;

    public JanelaImagem() {
        JanelaPrincipal.impOriginal.show();
        imp = JanelaPrincipal.impOriginal;
    }

    public void houghCircles() {  
        pr.houghCircles(imp);
        imp.setProcessor(pr.getHc().getImageOriginal());
        imp.updateImage();

        placa = new OvalRoi((int) (pr.getHc().getXC() - pr.getHc().getRadius()),
                (int) (pr.getHc().getYC() - pr.getHc().getRadius()),
                (int) (2 * pr.getHc().getRadius()), (int) (2 * pr.getHc().getRadius()));

        placa.setStrokeWidth(10);
        placa.setStrokeColor(Color.BLUE);
        listaRoi.add(placa);

        poligonoPlaca = new PolygonRoi(placa.getPolygon(), PolygonRoi.FREEROI);
    }

    public void houghLine() {
        pr.houghLine();
        Line2D.Double line = pr.getPhl().getLine();

        linha = new Line((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);

        linha.setStrokeWidth(10);
        linha.setStrokeColor(Color.GREEN);

        listaRoi.add(linha);

        poligonoLinha = new PolygonRoi(linha.getPolygon(), PolygonRoi.FREEROI);

        imp.setOverlay(listaRoi);
        listeners();
    }

    private void listeners() {
        ic = imp.getCanvas();
        
        ic.addMouseListener(new MouseAdapter() {

            // seleciona Roi ao clicar.
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                x = ic.offScreenX(x);
                y = ic.offScreenY(y);

                if (linha.contains(x, y)) {
                    imp.setRoi(linha);
                    imp.updateImage();
                } else if (placa.contains(x, y)) {
                    imp.setRoi(placa);
                    imp.updateImage();
                }
            }
        });

        WindowManager.getCurrentWindow().getCanvas().addMouseMotionListener(new MouseMotionAdapter() {

            // Ao arrastar, remove a roi da lista de rois, configura a roi modificada, e adiciona novamente a roi na lista de rois.
            @Override
            public void mouseDragged(MouseEvent e) {
                if (imp.getRoi() instanceof Line) {
                    listaRoi.remove(linha);
                    imp.setOverlay(listaRoi);
                    imp.updateImage();

                    linha = (Line) imp.getRoi();
                    listaRoi.add(linha);
                    imp.setOverlay(listaRoi);
                    imp.updateImage();
                } else if (imp.getRoi() instanceof OvalRoi) {
                    listaRoi.remove(placa);
                    imp.setOverlay(listaRoi);
                    imp.updateImage();

                    placa = (OvalRoi) imp.getRoi();
                    listaRoi.add(placa);
                    imp.setOverlay(listaRoi);
                    imp.updateImage();
                }
            }
        });
    }

    public void createImage() {
        int[] pixelsImagemOriginal = (int[]) pr.getHc().getImageOriginal().getPixels();
        int[] pixelsImagemPlaca = new int[pixelsImagemOriginal.length];

        Rectangle r = pr.getHc().getImageOriginal().getRoi();

        int offx = r.x;
        int offy = r.y;
        int width = r.width;
        int height = r.height;
        int offset = pr.getHc().getImageOriginal().getWidth();

        // inicializa vetor.
        for (int i = 0; i < pixelsImagemPlaca.length; i++) {
            pixelsImagemPlaca[i] = 0;
        }

        // adiciona ao vetor os pixels que contém na placa.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (placa.contains(x, y)) {
                    pixelsImagemPlaca[(x + offx) + (y + offy) * offset] = pixelsImagemOriginal[(x + offx)
                            + (y + offy) * offset];
                }
            }
        }

        ipPlaca = new ColorProcessor(width, height);
        ipPlaca.setPixels(pixelsImagemPlaca);
    }

    // Imagem Lado 1 foi definido como a meia lua da placa do lado esquerdo ou abaixo na imagem.
    public void createImageLado1() {

        double angle = linha.getAngle(linha.x1, linha.y1, linha.x2, linha.y2);

        int[] pixelsImagemOriginal = (int[]) pr.getHc().getImageOriginal().getPixels();
        int[] pixelsImagemPlacaL1 = new int[pixelsImagemOriginal.length];

        Rectangle r = pr.getHc().getImageOriginal().getRoi();

        int offx = r.x;
        int offy = r.y;
        int width = r.width;
        int height = r.height;
        int offset = pr.getHc().getImageOriginal().getWidth();

        // inicializa vetor.
        for (int i = 0; i < pixelsImagemPlacaL1.length; i++) {
            pixelsImagemPlacaL1[i] = 0;
        }

        System.out.println("Angulo: " + angle);

        // detecta linha vertical, meia lua esquerda na imagem.        
        if ((angle > 45 && angle < 135) || (angle < -45 && angle > -135)) {
            System.out.println("Vertical");

            int[] linhaX = getMenoresXLinhaImagemLE(linha);
            int[] linhaY = getYLinhaVertical(linha);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (placa.contains(x, y)) {
                        for (int i = 0; i < linhaY.length; i++) {
                            if (y == linhaY[i]) {
                                if (x <= linhaX[i]) {
                                    pixelsImagemPlacaL1[(x + offx) + (y + offy) * offset] = pixelsImagemOriginal[(x + offx)
                                            + (y + offy) * offset];
                                }
                            }
                        }
                    }
                }
            }
        } // detecta linha horizontal, meia lua abaixo na imagem.
        else /*if((angle >= 135 || angle <= -135 ) || (angle <= 45 || angle >= -45))*/ {
            System.out.println("Horizontal");

            int[] linhaX = getXLinhaHorizontal(linha);
            int[] linhaY = getMaioresYLinhaImagemAbaixo(linha);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (placa.contains(x, y)) {
                        for (int i = 0; i < linhaX.length; i++) {
                            if (x == linhaX[i]) {
                                if (y >= linhaY[i]) {
                                    pixelsImagemPlacaL1[(x + offx) + (y + offy) * offset] = pixelsImagemOriginal[(x + offx)
                                            + (y + offy) * offset];
                                }
                            }
                        }
                    }
                }
            }
        }

        ipPlacaL1 = new ColorProcessor(width, height);
        ipPlacaL1.setPixels(pixelsImagemPlacaL1);
    }

    // Imagem Lado 2 foi definido como a meia lua da placa do lado direto ou acima na imagem.
    public void createImageLado2() {

        double angle = linha.getAngle(linha.x1, linha.y1, linha.x2, linha.y2);

        int[] pixelsImagemOriginal = (int[]) pr.getHc().getImageOriginal().getPixels();
        int[] pixelsImagemPlacaL2 = new int[pixelsImagemOriginal.length];

        Rectangle r = pr.getHc().getImageOriginal().getRoi();

        int offx = r.x;
        int offy = r.y;
        int width = r.width;
        int height = r.height;
        int offset = pr.getHc().getImageOriginal().getWidth();

        // inicializa vetor.
        for (int i = 0; i < pixelsImagemPlacaL2.length; i++) {
            pixelsImagemPlacaL2[i] = 0;
        }

        System.out.println("Angulo: " + angle);

        // detecta linha vertical, meia lua direita na imagem.        
        if ((angle > 45 && angle < 135) || (angle < -45 && angle > -135)) {
            System.out.println("Vertical");

            int[] linhaX = getMaioresXLinhaImagemLD(linha);
            int[] linhaY = getYLinhaVertical(linha);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (placa.contains(x, y)) {
                        for (int i = 0; i < linhaY.length; i++) {
                            if (y == linhaY[i]) {
                                if (x >= linhaX[i]) {
                                    pixelsImagemPlacaL2[(x + offx) + (y + offy) * offset] = pixelsImagemOriginal[(x + offx)
                                            + (y + offy) * offset];
                                }
                            }
                        }
                    }
                }
            }
        } // detecta linha horizontal, meia lua abaixo na imagem.
        else /*if((angle >= 135 || angle <= -135 ) || (angle <= 45 || angle >= -45))*/ {
            System.out.println("Horizontal");

            int[] linhaX = getXLinhaHorizontal(linha);
            int[] linhaY = getMenoresYLinhaImagemAcima(linha);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (placa.contains(x, y)) {
                        for (int i = 0; i < linhaX.length; i++) {
                            if (x == linhaX[i]) {
                                if (y <= linhaY[i]) {
                                    pixelsImagemPlacaL2[(x + offx) + (y + offy) * offset] = pixelsImagemOriginal[(x + offx)
                                            + (y + offy) * offset];
                                }
                            }
                        }
                    }
                }
            }
        }


        ipPlacaL2 = new ColorProcessor(width, height);
        ipPlacaL2.setPixels(pixelsImagemPlacaL2);
    }

    /********** outros métodos ************/
    private int[] getXLinhaHorizontal(Line linha) {
        int[] pontosX;

        if (linha.x1 < linha.x2) {
            pontosX = new int[(linha.x2 - linha.x1) + 1];
            int index = 0;
            for (int x = linha.x1; x <= linha.x2; x++) {
                pontosX[index] = x;
                index++;
            }
            return pontosX;
        } else {
            pontosX = new int[(linha.x1 - linha.x2) + 1];
            int index = 0;
            for (int x = linha.x2; x <= linha.x1; x++) {
                pontosX[index] = x;
                index++;
            }
            return pontosX;
        }
    }

    private int[] getYLinhaVertical(Line linha) {
        int[] pontosY;

        if (linha.y1 < linha.y2) {
            pontosY = new int[(linha.y2 - linha.y1) + 1];
            int index = 0;
            for (int y = linha.y1; y <= linha.y2; y++) {
                pontosY[index] = y;
                index++;
            }
            return pontosY;
        } else {
            pontosY = new int[(linha.y1 - linha.y2) + 1];
            int index = 0;
            for (int y = linha.y2; y <= linha.y1; y++) {
                pontosY[index] = y;
                index++;
            }
            return pontosY;
        }
    }

    // retorna os menores valores X que contem na linha.
    private int[] getMenoresXLinhaImagemLE(Line linha) {
        int index = -1;

        if (linha.y1 < linha.y2) {
            int[] menoresX = new int[(linha.y2 - linha.y1) + 1];
            boolean continua;

            for (int i = linha.y1; i <= linha.y2; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o menor X.
                if (linha.x1 < linha.x2) {
                    for (int j = linha.x1; j <= linha.x2; j++) { // começa a iteração pelo menor X.
                        if (linha.contains(j, i) && continua) {
                            menoresX[index] = j; // recebe o menor X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o menor X, então para.
                        }
                    }
                } else {
                    for (int j = linha.x2; j <= linha.x1; j++) { // começa a iteração pelo menor X.
                        if (linha.contains(j, i) && continua) {
                            menoresX[index] = j; // recebe o menor X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o menor X, então para.
                        }
                    }
                }
            }
            return menoresX;
        } else {
            int[] menoresX = new int[(linha.y1 - linha.y2) + 1];
            boolean continua;

            for (int i = linha.y2; i <= linha.y1; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o menor X.
                if (linha.x1 < linha.x2) {
                    for (int j = linha.x1; j <= linha.x2; j++) { // começa a iteração pelo menor X.
                        if (linha.contains(j, i) && continua) {
                            menoresX[index] = j; // recebe o menor X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o menor X, então para.
                        }
                    }
                } else {
                    for (int j = linha.x2; j <= linha.x1; j++) { // começa a iteração pelo menor X.
                        if (linha.contains(j, i) && continua) {
                            menoresX[index] = j; // recebe o menor X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o menor X, então para.
                        }
                    }
                }
            }
            return menoresX;
        }
    }

    // retorna os maiores valores X que contem na linha.
    private int[] getMaioresXLinhaImagemLD(Line linha) {
        int index = -1;

        if (linha.y1 < linha.y2) {
            int[] maioresX = new int[(linha.y2 - linha.y1) + 1];
            boolean continua;

            for (int i = linha.y1; i <= linha.y2; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o maior X.
                if (linha.x1 < linha.x2) {
                    for (int j = linha.x2; j >= linha.x1; j--) { // começa a iteração pelo maior X.
                        if (linha.contains(j, i) && continua) {
                            maioresX[index] = j; // recebe o maior X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o maior X, então para.
                        }
                    }
                } else {
                    for (int j = linha.x1; j >= linha.x2; j--) { // começa a iteração pelo maior X.
                        if (linha.contains(j, i) && continua) {
                            maioresX[index] = j; // recebe o maior X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o maior X, então para.
                        }
                    }
                }
            }
            return maioresX;
        } else {
            int[] maioresX = new int[(linha.y1 - linha.y2) + 1];
            boolean continua;

            for (int i = linha.y2; i <= linha.y1; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o maior X.
                if (linha.x1 < linha.x2) {
                    for (int j = linha.x2; j >= linha.x1; j--) { // começa a iteração pelo maior X.
                        if (linha.contains(j, i) && continua) {
                            maioresX[index] = j; // recebe o maior X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o maior X, então para.
                        }
                    }
                } else {
                    for (int j = linha.x1; j >= linha.x2; j--) { // começa a iteração pelo menor X.
                        if (linha.contains(j, i) && continua) {
                            maioresX[index] = j; // recebe o maior X dentre os possiveis X para aquele Y (i).
                            continua = false; // recebeu o maior X, então para.
                        }
                    }
                }
            }
            return maioresX;
        }
    }

    // retorna os maiores valores Y que contem na linha.
    private int[] getMaioresYLinhaImagemAbaixo(Line linha) {
        int index = -1;

        if (linha.x1 < linha.x2) {
            int[] maioresY = new int[(linha.x2 - linha.x1) + 1];
            boolean continua;

            for (int i = linha.x1; i <= linha.x2; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o maior Y.
                if (linha.y1 < linha.y2) {
                    for (int j = linha.y2; j >= linha.y1; j--) { // começa a iteração pelo maior Y.
                        if (linha.contains(i, j) && continua) {
                            maioresY[index] = j; // recebe o maior Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o maior Y, então para.
                        }
                    }
                } else {
                    for (int j = linha.y1; j >= linha.y2; j--) { // começa a iteração pelo maior Y.
                        if (linha.contains(i, j) && continua) {
                            maioresY[index] = j; // recebe o maior Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o maior Y, então para.
                        }
                    }
                }
            }
            return maioresY;
        } else {
            int[] maioresY = new int[(linha.x1 - linha.x2) + 1];
            boolean continua;

            for (int i = linha.x2; i <= linha.x1; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o maior Y.
                if (linha.y1 < linha.y2) {
                    for (int j = linha.y2; j >= linha.y1; j--) { // começa a iteração pelo maior Y.
                        if (linha.contains(i, j) && continua) {
                            maioresY[index] = j; // recebe o maior Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o maior Y, então para.
                        }
                    }
                } else {
                    for (int j = linha.y1; j >= linha.y2; j--) { // começa a iteração pelo maior Y.
                        if (linha.contains(i, j) && continua) {
                            maioresY[index] = j; // recebe o maior Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o maior Y, então para.
                        }
                    }
                }
            }
            return maioresY;
        }
    }

    // retorna os menores valores Y que contem na linha.
    private int[] getMenoresYLinhaImagemAcima(Line linha) {
        int index = -1;

        if (linha.x1 < linha.x2) {
            int[] menoresY = new int[(linha.x2 - linha.x1) + 1];
            boolean continua;

            for (int i = linha.x1; i <= linha.x2; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o menor Y.
                if (linha.y1 < linha.y2) {
                    for (int j = linha.y1; j <= linha.y2; j++) { // começa a iteração pelo menor Y.
                        if (linha.contains(i, j) && continua) {
                            menoresY[index] = j; // recebe o menor Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o menor Y, então para.
                        }
                    }
                } else {
                    for (int j = linha.y2; j <= linha.y1; j++) { // começa a iteração pelo menor Y.
                        if (linha.contains(i, j) && continua) {
                            menoresY[index] = j; // recebe o menor Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o menor Y, então para.
                        }
                    }
                }
            }
            return menoresY;
        } else {
            int[] menoresY = new int[(linha.x1 - linha.x2) + 1];
            boolean continua;

            for (int i = linha.x2; i <= linha.x1; i++) {
                index++;
                continua = true; // inicia com verdadeiro. Passará a ser falso quando encontrar o menor Y.
                if (linha.y1 < linha.y2) {
                    for (int j = linha.y1; j <= linha.y2; j++) { // começa a iteração pelo menor Y.
                        if (linha.contains(i, j) && continua) {
                            menoresY[index] = j; // recebe o menor Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o menor Y, então para.
                        }
                    }
                } else {
                    for (int j = linha.y2; j <= linha.y1; j++) { // começa a iteração pelo menor Y.
                        if (linha.contains(i, j) && continua) {
                            menoresY[index] = j; // recebe o menor Y dentre os possiveis Y para aquele X (i).
                            continua = false; // recebeu o menor Y, então para.
                        }
                    }
                }
            }
            return menoresY;
        }
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

    // retorna pilha de imagens geradas (inteira, lado1, lado2)
    public ImageStack criarPilhaImagensGeradas() {
        ImageStack is = new ImageStack(imp.getWidth(), imp.getHeight());
        
        if(ipPlaca != null){
            is.addSlice(ipPlaca);
        }
        
        if(ipPlacaL1 != null){
            is.addSlice(ipPlacaL1);
        }
        
        if(ipPlacaL2 != null){
            is.addSlice(ipPlacaL2);
        }
        
        return is;
    }
    
}