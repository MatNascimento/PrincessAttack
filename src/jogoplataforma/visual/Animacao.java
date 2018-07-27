package jogoplataforma.visual;

import java.awt.image.BufferedImage;

/**
 * Classe que faz o controle das Animações.
 * 
 * Armazena e controla as imagens, o frame atual, o tempo de inicio e
 * o delay entre frames.
 * 
 * @author Admin
 */
public class Animacao {
    
    private BufferedImage[] frames;
    private int frameAtual;
    
    private long tempoInicio;
    private long delay;
    
    /**
     * Método que acessa a imagem do sprite na posição atual.
     * 
     * @return a imagem atual em um buffer.
     */
    public BufferedImage getImage() {
        
        return frames[frameAtual];
        
    }

    public boolean getAcabou() {
        
        return frameAtual == frames.length - 1;
        
    }
    
    /**
     * Método que seta os sprites no objeto.
     * 
     * @param frames imagens do sprite.
     */
    public void setFrames( BufferedImage[] frames ) {
        
        this.frames = frames;
        
        if( frameAtual >= frames.length ) {
            
            frameAtual = 0;
            
        }
        
    }
    
    /**
     * Método que seta o tempo de espera entre os frames.
     * 
     * @param delay tempo de espera.
     */
    public void setDelay( long delay ) {
        
        this.delay = delay;
        
    }
    
    /**
     * Método que faz o controle da animação dos sprites.
     */
    public void update() {
        
        //Impede a continuação da execução se o sprite tiver um único frame
        if( delay == -1 ) return; 
        
        long tempoPassado = ( System.nanoTime() - tempoInicio ) / 1000000;
        if( tempoPassado > delay ) {
            
            frameAtual++;
            tempoInicio = System.nanoTime();
            
        }
        
        if( frameAtual == frames.length ) {
            
            frameAtual = 0;
            
        }
        
    }
    
}
