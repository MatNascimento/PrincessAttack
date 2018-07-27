package jogoplataforma.mapas;

import java.awt.image.BufferedImage;
import jogoplataforma.TileMap;
import jogoplataforma.personagens.Inimigo;
import jogoplataforma.personagens.Projetil;

/**
 * Classe que define os atributos e métodos que um Mapa deve possuir no jogo
 */
public abstract class Mapa {
    
    protected BufferedImage imagemFundo;
    
    protected TileMap tileMap;
    
    protected Inimigo[]  inimigos;
    protected Projetil[] projetil;
    
    protected double posicaoJogadorX;
    protected double posicaoJogadorY;
    
    protected double areaFimX;
    protected double areaFimY;
    
    public TileMap getTileMap() {
        
        return tileMap;
        
    }

    public Inimigo[] getInimigos() {
        
        return inimigos;
        
    }

    public Projetil[] getProjetil() {
        
        return projetil;
        
    }

    public double getPosicaoJogadorX() {
        
        return posicaoJogadorX;
        
    }

    public double getPosicaoJogadorY() {
        
        return posicaoJogadorY;
        
    }

    public double getAreaFimX() {
        
        return areaFimX;
        
    }

    public double getAreaFimY() {
        
        return areaFimY;
        
    }

    public BufferedImage getImagemFundo() {
        
        return imagemFundo;
        
    }

}
