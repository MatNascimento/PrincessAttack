package jogoplataforma.visual;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CenaMenu extends Cena {
    
    private BufferedImage menuJogar;
    private BufferedImage menuCreditos;
    
    private boolean focusCredito;
    
    /**
     * Cena do Menu: Funciona como uma cena genérica, porém, com estados a mais,
     * que representam a mudança de posição do focus do menu.
     * 
     * @param largura
     * @param altura 
     */
    public CenaMenu( int largura, int altura ) {
        
        super( largura, altura );
        
        try {
            
            menuJogar = ImageIO.read( new File( "graficos/cenas/menuJogar.png" ) );
            menuCreditos = ImageIO.read( new File( "graficos/cenas/menuCreditos.png" ) );
            
        } catch( IOException e ){
        
            System.out.println( "Falha ao inicializar as imagens do Menu" );
            
        }
        
    }

    public boolean isFocusCredito() {
        
        return focusCredito;
        
    }

    public void setFocusCredito( boolean focusCredito ) {
        
        this.focusCredito = focusCredito;
        
    }

    @Override
    public void draw(Graphics2D desenhar) {
        
        if( focusCredito ) {
            
            desenhar.drawImage( menuCreditos, 0, 0, LARGURA, ALTURA, null );
            
        } else {
            
            desenhar.drawImage( menuJogar, 0, 0, LARGURA, ALTURA, null );
            
        }
        
    }

}
