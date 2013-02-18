package interfaces;

import ij.ImagePlus;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import processamento.TesteAreaTemplate;

public class JanelaPrincipal extends javax.swing.JFrame {

    public JanelaPrincipal() {
        initComponents();
        initComponents2();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        tbBarraFerramentas = new javax.swing.JToolBar();
        pbBarraProgresso = new javax.swing.JProgressBar();
        pPainelEsquerdo = new javax.swing.JPanel();
        bDetectarPlaca = new javax.swing.JButton();
        bGerarImagens = new javax.swing.JButton();
        bContagem = new javax.swing.JButton();
        cbPlacaInteira = new javax.swing.JCheckBox();
        cbPlacaLado1 = new javax.swing.JCheckBox();
        cbPlacaLado2 = new javax.swing.JCheckBox();
        rbInteira = new javax.swing.JRadioButton();
        rbLado1 = new javax.swing.JRadioButton();
        rbLado2 = new javax.swing.JRadioButton();
        mbBarraMenu = new javax.swing.JMenuBar();
        mFile = new javax.swing.JMenu();
        miOpen = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Contagem Colônias");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tbBarraFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbBarraFerramentas.setRollover(true);

        pPainelEsquerdo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bDetectarPlaca.setText("Detectar Placa e Linha Central");
        bDetectarPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDetectarPlacaActionPerformed(evt);
            }
        });

        bGerarImagens.setText("Gerar Imagens");
        bGerarImagens.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGerarImagensActionPerformed(evt);
            }
        });

        bContagem.setText("Contagem");
        bContagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bContagemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pPainelEsquerdoLayout = new javax.swing.GroupLayout(pPainelEsquerdo);
        pPainelEsquerdo.setLayout(pPainelEsquerdoLayout);
        pPainelEsquerdoLayout.setHorizontalGroup(
            pPainelEsquerdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPainelEsquerdoLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(bDetectarPlaca)
                .addGap(40, 40, 40)
                .addComponent(bGerarImagens)
                .addGap(43, 43, 43)
                .addComponent(bContagem)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        pPainelEsquerdoLayout.setVerticalGroup(
            pPainelEsquerdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPainelEsquerdoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pPainelEsquerdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bDetectarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bGerarImagens, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bContagem, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cbPlacaInteira.setText("Inteira");

        cbPlacaLado1.setText("Lado 1");

        cbPlacaLado2.setText("Lado 2");

        rbInteira.setText("Inteira");

        rbLado1.setText("Lado 1");

        rbLado2.setText("Lado 2");

        mFile.setMnemonic('q');
        mFile.setText("Arquivo");

        miOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        miOpen.setMnemonic('b');
        miOpen.setText("Abrir...");
        miOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenActionPerformed(evt);
            }
        });
        mFile.add(miOpen);

        mbBarraMenu.add(mFile);

        setJMenuBar(mbBarraMenu);
        mbBarraMenu.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbBarraFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
            .addComponent(pPainelEsquerdo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pbBarraProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbPlacaInteira)
                .addGap(18, 18, 18)
                .addComponent(cbPlacaLado1)
                .addGap(18, 18, 18)
                .addComponent(cbPlacaLado2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(rbInteira)
                .addGap(18, 18, 18)
                .addComponent(rbLado1)
                .addGap(18, 18, 18)
                .addComponent(rbLado2)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tbBarraFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pPainelEsquerdo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbBarraProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbPlacaInteira)
                    .addComponent(cbPlacaLado1)
                    .addComponent(cbPlacaLado2)
                    .addComponent(rbLado2)
                    .addComponent(rbLado1)
                    .addComponent(rbInteira))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initComponents2() {
        bg.add(rbInteira);
        bg.add(rbLado1);
        bg.add(rbLado2);
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void bDetectarPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDetectarPlacaActionPerformed
        ji.houghCircles();
        ji.houghLine();
    }//GEN-LAST:event_bDetectarPlacaActionPerformed

    private void miOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenActionPerformed
        fcImagem.setDialogTitle("Abrir Imagem:");
        fcImagem.setDialogType(JFileChooser.OPEN_DIALOG);
        fcImagem.setMultiSelectionEnabled(false);
        fcImagem.showOpenDialog(this);

        pathImagem = fcImagem.getSelectedFile().getAbsolutePath();
        if ((pathImagem != null)) {
            if (pathImagem.contains(".jpg") || pathImagem.contains(".JPG")) {
                impOriginal = new ImagePlus(pathImagem);
                ji = new JanelaImagem();
            } else {
                JOptionPane.showMessageDialog(this, "Imagem não encontrada", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_miOpenActionPerformed

    private void bGerarImagensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGerarImagensActionPerformed

        if(cbPlacaInteira.isSelected()){
            ji.createImage();
           // new ImagePlus("Imagem Placa Inteira", ji.getIpPlaca()).show(); // exibe imagem da placa selecionada.
        }
        
        if(cbPlacaLado1.isSelected()){
            ji.createImageLado1();
            //new ImagePlus("Imagem Placa Lado 1", ji.getIpPlacaL1()).show(); // exibe imagem de um dos lados da placa (esquerdo, em baixo).
        }
        
        if(cbPlacaLado2.isSelected()){
            ji.createImageLado2();
            //new ImagePlus("Imagem Placa Lado 2", ji.getIpPlacaL2()).show(); // exibe imagem de um dos lados da placa (direito, em cima).
        }
        
        new ImagePlus(null, ji.criarPilhaImagensGeradas()).show();
    }//GEN-LAST:event_bGerarImagensActionPerformed

    private void bContagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bContagemActionPerformed
        if(rbInteira.isSelected()){
            new TesteAreaTemplate(ji.getIpPlaca());
        }
        else if(rbLado1.isSelected()){
            new TesteAreaTemplate(ji.getIpPlacaL1()); 
        }
        else if(rbLado2.isSelected()){
            new TesteAreaTemplate(ji.getIpPlacaL2());
        }
    }//GEN-LAST:event_bContagemActionPerformed

    public String getPathImagem() {
        return pathImagem;
    }

    public ImagePlus getImagemOriginal() {
        return impOriginal;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bContagem;
    private javax.swing.JButton bDetectarPlaca;
    private javax.swing.JButton bGerarImagens;
    private javax.swing.JCheckBox cbPlacaInteira;
    private javax.swing.JCheckBox cbPlacaLado1;
    private javax.swing.JCheckBox cbPlacaLado2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JMenu mFile;
    private javax.swing.JMenuBar mbBarraMenu;
    private javax.swing.JMenuItem miOpen;
    private javax.swing.JPanel pPainelEsquerdo;
    public static javax.swing.JProgressBar pbBarraProgresso;
    private javax.swing.JRadioButton rbInteira;
    private javax.swing.JRadioButton rbLado1;
    private javax.swing.JRadioButton rbLado2;
    private javax.swing.JToolBar tbBarraFerramentas;
    // End of variables declaration//GEN-END:variables
    private JFileChooser fcImagem = new JFileChooser();
    public static String pathImagem;
    public static ImagePlus impOriginal;
    JanelaImagem ji;
    ButtonGroup bg = new ButtonGroup();
}
