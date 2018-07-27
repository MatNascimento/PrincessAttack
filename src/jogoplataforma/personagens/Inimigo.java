package jogoplataforma.personagens;

import jogoplataforma.visual.Animacao;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import jogoplataforma.TileMap;

/**
 * Para mais informações sobre essa Classe consulte a documentação da Classe-Pai Personagem ou da Classe-Irmã Jogador.
 */
public class Inimigo extends Personagem {
    
    public Inimigo( TileMap tileMap, double initScreen, double screenOffset ) {
        
        this.tileMap = tileMap;
        
        this.initScreen = initScreen;
        this.screenOffset = screenOffset;
        
        largura = 31;
        altura = 37;
        
        velMov = 2.5;
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
    
    private void checarColisao( int linhaAtual, double proximoX, double proximoY, double initX, double offsetX ) {

        double tempX = x;
        double tempY = y;
        
        calcularCantos( x, proximoY );
        if( dy > 0 ) {
            
            if( ( bottomLeft || bottomRight ) ) {
                
                dy = 0;
                caindo = false;
                direita = true;
                tempY = ( linhaAtual + 1 ) * tileMap.getTamanhoTile() - altura/2;
                
            } else {
                
                tempY += dy;
                
            }
            
        }
        
        calcularCantos( proximoX, y );
        if( dx < 0 ) {
            
            if( ( topLeft || bottomLeft )  || ( x - largura/2 <= initX  ) ) {
                
                esquerda = false;
                direita = true;
                
            } else {
                
                tempX += dx;
                
            }
            
        }
        if( dx >= 0 ) {
            
            if( ( topRight || bottomRight ) || ( x + largura/2 >= offsetX - 7 ) ) {
                
                direita = false;
                esquerda = true;
                
            } else {
                
                tempX += dx;
                
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
        
        controleDirecao();
        
        setSprite();
        
        int linhaAtual = tileMap.getRowTile( ( int ) y );
        
        double proximoX = x + dx;
        double proximoY = y + dy;
        
        checarColisao( linhaAtual, proximoX, proximoY, initScreen, screenOffset );
        
        if( desativando && animacao.getAcabou() ) {
            
            ativo = false;
            
        }
        
    }
    
    @Override
    protected void inicializarSprites() throws IOException {

        spriteParado = new BufferedImage[1];
        spriteCaindo = new BufferedImage[1];
        spriteAndando = new BufferedImage[3];
        
        spriteMorrendo = new BufferedImage[6];

        spriteParado[0] = ImageIO.read( new File( "graficos/inimigo/inimigoidle.png" ) );
        spriteCaindo[0] = ImageIO.read( new File( "graficos/inimigo/inimigoidle.png" ) );
        
        BufferedImage andando = ImageIO.read( new File( "graficos/inimigo/inimigowalking2.png" ) );
        BufferedImage spritemorrendo = ImageIO.read( new File( "graficos/inimigo/spritesheetdying.png" ) );
        
        for( int i = 0; i < spriteAndando.length; i++ ) {
            
            spriteAndando[i] = andando.getSubimage( i * 37 + i, 0, 37, 44 );

        }
        
        for( int i = 0; i < spriteMorrendo.length; i++ ) {
            
            spriteMorrendo[i] = spritemorrendo.getSubimage( i * 37 + i, 0, 37, 44 );
            
        }
        
        animacao = new Animacao();
        viradoParaEsquerda = true;
        
    }
    
    @Override
    protected void controleDirecao() {
        
        if( !desativando ) {
            
            if( esquerda ) {
            
                dx = -velMov;

            } else if( direita ) {

                dx = velMov;

            } 

            if( caindo ) {
                
                dy += gravidade;

                if( dy > velQuedaMax ) {

                    dy = velQuedaMax;

                }

            } else {

                dy = 0;

            }
            
        } else {
            
            dx = 0;
            
        }

    }
    
    @Override
    protected void setSprite() {
        
        if( desativando ) {
            
            animacao.setFrames( spriteMorrendo );
            animacao.setDelay( 200 );
            
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
            
            viradoParaEsquerda = false;
            
        } else if( dx > 0 ) {
            
            viradoParaEsquerda = true;
            
        }
        
        animacao.update();

    }

}
