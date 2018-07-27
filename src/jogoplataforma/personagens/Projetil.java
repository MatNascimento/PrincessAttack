package jogoplataforma.personagens;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import jogoplataforma.visual.Animacao;
import jogoplataforma.TileMap;

/**
 * Para mais informações sobre essa Classe consulte a documentação da Classe-Pai Personagem ou da Classe-Irmã Jogador.
 */
public class Projetil extends Personagem {
    
    public Projetil( TileMap tileMap ) {
        
        this.tileMap = tileMap;
        
        largura = 20;
        altura = 10;
        
        velMov = 7.4;
        velQuedaMax = 0;
        gravidade = 0;
        
        ativo = false;
        
        try {
            
            inicializarSprites();
            
        } catch ( IOException e ){
        
            System.out.println( "Não foi possível inicializar os sprites do projetil!" );
            
        }
        
    }
    
    public void disparar( int x, int y, boolean esquerda ) {
        
        this.x = x;
        this.y = y;
        
        viradoParaEsquerda = !esquerda;
        
        if( esquerda ) {
            
            this.esquerda = true;
            this.direita = false;
            
        } else {
            
            this.esquerda = false;
            this.direita = true;
            
        }
        
    }
    
    private void checarColisao( double proximoX ) {
        
        double tempX = x;
        
        calcularCantos( proximoX, y );
        if( dx < 0 ) {
            
            if( ( topLeft || bottomLeft ) || proximoX - largura/2 < 0 ) {
                
                ativo = false;
                
            } else {
                
                tempX += dx;
                
            }
            
        }
        if( dx >= 0 ) {
            
            if( ( topRight || bottomRight ) ) {
                
                ativo = false;
                
            } else {
                
                tempX += dx;
                
            }
            
        }
        
        x = tempX;
        
    }
    
    @Override
    public void update() {
        
        controleDirecao();
        
        setSprite();

        double proximoX = x + dx;
        
        checarColisao( proximoX );
        
    }
    
    @Override
    protected void inicializarSprites() throws IOException {
        
        spriteParado = new BufferedImage[1];
        spriteMorrendo = new BufferedImage[1];
        
        spriteParado[0] = ImageIO.read( new File( "graficos/projetil.png" ) );
        spriteMorrendo[0] = ImageIO.read( new File( "graficos/projetil.png" ) );
        
        animacao = new Animacao();
        viradoParaEsquerda = false;
        
    }

    @Override
    protected void controleDirecao() {

        if( esquerda ) {

            dx = -velMov;

        } else if( direita ) {

            dx = velMov;

        } 
        
    }

    @Override
    protected void setSprite() {
        
        animacao.setFrames( spriteParado );
        animacao.setDelay( -1 );
        
        animacao.update();
        
    }

}
