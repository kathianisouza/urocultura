package principal;

import interfaces.JanelaPrincipal;

public class Execution {
    //String pathImagem;
    
    public Execution(){
        JanelaPrincipal jpPrincipal = new JanelaPrincipal();
        jpPrincipal.setVisible(true);
        
        //JOptionPane.showMessageDialog(null, "PROCESSO FINALIZADO.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Execution();
            }
        });
    }
}
