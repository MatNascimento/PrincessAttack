package jogoplataforma;

import java.awt.image.BufferedImage;

/**
 * Classe que seta as imagens correspondentes para cada bloco e
 * faz o controle se o bloco é 'atravessável' pelos personagens ou não.
 */
public class Tile {
    
    private BufferedImage imagem;
    private boolean bloqueado;
    
    /**
     * Contrutor recebe a imagem a ser armazenada pela tile e o estado dessa tile
     * 
     * @param imagem Imagem a ser exibida por essa Tile no Mapa do jogo
     * @param bloqueado Parâmetro que configura o estado. True: não é atravessável | False: atravessável
     */
    public Tile( BufferedImage imagem, boolean bloqueado ) {
        
        this.imagem = imagem;
        this.bloqueado = bloqueado;
        
    }

    public BufferedImage getImagem() {
        
        return imagem;
        
    }

    public boolean isBloqueado() {
        
        return bloqueado;
        
    }
    
}
