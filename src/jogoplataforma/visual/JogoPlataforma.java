package jogoplataforma.visual;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import jogoplataforma.PainelJogo;

/**
 * Classe que inicializa a janela.
 */
public class JogoPlataforma {

    public static void main(String[] args) {
        
        //Acesso ao ícone da janela
        ImageIcon img = new ImageIcon( "graficos/icone.png" );

        //Configurações da Janela
        JFrame janela = new JFrame( "Princess Attack" );
        janela.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        janela.setResizable( false );
        
        janela.setIconImage( img.getImage() );
        
        janela.setContentPane( new PainelJogo() );
        janela.pack();
        
        janela.setLocationRelativeTo( null );
        janela.setVisible( true );
        
    }
    
}
