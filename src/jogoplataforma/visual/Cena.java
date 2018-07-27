package jogoplataforma.visual;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Classe com atributos e métodos referentes a uma cena do jogo.
 * 
 * O jogo é separado em Cenas, que são os diferentes estados que o jogo pode ter.
 * Exemplo: Menu é uma cena, o jogo é um cena, os créditos são uma cena.
 */
public class Cena {
    
    protected int LARGURA;
    protected int ALTURA;
    
    protected BufferedImage imagemFundo;
    
    /**
     * Possui como parâmetro o tamanho dessa cena (geralmente são definidas com base no tamanho da janela do programa)
     * 
     * @param largura
     * @param altura 
     */
    public Cena( int largura, int altura ) {
        
        this.LARGURA = largura;
        this.ALTURA = altura;
        
    }
    
    public void setImagemFundo( BufferedImage imagemFundo ) {
        
        this.imagemFundo = imagemFundo;
        
    }
    
    public void update(){};
    
    public void draw( Graphics2D desenhar ) {
        
        desenhar.drawImage( imagemFundo, 0, 0, LARGURA, ALTURA, null );
        
    }
    
}
