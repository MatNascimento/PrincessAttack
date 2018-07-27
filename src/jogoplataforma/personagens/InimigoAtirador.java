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
public class InimigoAtirador extends Inimigo {

    public InimigoAtirador(TileMap tileMap) {
        
        super( tileMap, 0, 0 );
        
        largura = 36;
        altura = 37;
        
        velMov = 0;
        velQuedaMax = 12;
        gravidade = 0.64;
        
        ativo = true;
        desativando = false;
        
        try {
            
            inicializarSprites();
            
        } catch ( IOException e ){
        
            System.out.println( "Não foi possível inicializar os sprites do inimigo!" );
            
        }
        
    }
    
    private void checarColisao( int linhaAtual, double proximoY ) {

        double tempX = x;
        double tempY = y;
        
        calcularCantos( x, proximoY );
        if( dy > 0 ) {
            
            if( ( bottomLeft || bottomRight ) ) {
                
                dy = 0;
                caindo = false;
                tempY = ( linhaAtual + 1 ) * tileMap.getTamanhoTile() - altura/2;
                
            } else {
                
                tempY += dy;
                
            }
            
        }
        
        if( !caindo ) {
            
            calcularCantos( x, y + 1 );
            if( !bottomLeft && !bottomRight ) {
                
                caindo = true;
                
            }
            
        }
        
        x = tempX;
        y = tempY;
        
    }
    
    @Override
    public void update() {
        
        direita = false;
        esquerda = false;
        viradoParaEsquerda = false;
        
        controleDirecao();
        
        setSprite();
        
        int linhaAtual = tileMap.getRowTile( ( int ) y );
        
        double proximoY = y + dy;
        
        checarColisao( linhaAtual, proximoY );
        
        if( desativando && animacao.getAcabou() ) {
            
            ativo = false;
            
        }
        
    }
    
    @Override
    protected void inicializarSprites() throws IOException {

        spriteParado = new BufferedImage[1];
        spriteCaindo = new BufferedImage[1];
        
        spriteMorrendo = new BufferedImage[7];

        spriteParado[0] = ImageIO.read( new File( "graficos/inimigoAtirador/inimigoAtiradorIdle.png" ) );
        spriteCaindo[0] = ImageIO.read( new File( "graficos/inimigoAtirador/inimigoAtiradorFall.png" ) );
        
        BufferedImage spritemorrendo = ImageIO.read( new File( "graficos/inimigoAtirador/spritesheetdying.png" ) );
        
        for( int i = 0; i < spriteMorrendo.length; i++ ) {
            
            spriteMorrendo[i] = spritemorrendo.getSubimage( i * 72 + i + 1, 0, 72, 75 );
            
        }
        
        animacao = new Animacao();
        viradoParaEsquerda = false;
        
    }
    
    @Override
    protected void setSprite() {
        
        if( desativando ) {
            
            animacao.setFrames( spriteMorrendo );
            animacao.setDelay( 100 );
            
        } else {
            
            if( esquerda || direita ) {

                animacao.setFrames( spriteAndando );
                animacao.setDelay( 100 );

            } else {

                animacao.setFrames( spriteParado );
                animacao.setDelay( -1 );

            }

            if( dy > 0 ) {

                animacao.setFrames( spriteCaindo );
                animacao.setDelay( -1 );

            } 
            
        }
        
        if( dx < 0 ) {
            
            viradoParaEsquerda = true;
            
        } else if( dx > 0 ) {
            
            viradoParaEsquerda = false;
            
        }
        
        animacao.update();

    }
    
}
