package jogoplataforma.personagens;

import jogoplataforma.visual.Animacao;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import jogoplataforma.PainelJogo;
import jogoplataforma.TileMap;

/**
 * Para mais informações sobre essa Classe consulte a documentação da Classe-Pai Personagem
 */
public class Jogador extends Personagem {
    
    private int vidas;
    
    private int tempoInvencibilidade;
    private long tempoInicial;
    private long ultimoTempo;
    
    private boolean pulando;
    private boolean invencivel;

    private double velMovMax;
    private double velParada;
    private double pulo;
    
    private Clip somPulo;
    private Clip somAtacado;
    
    private BufferedImage[] spritePulando;
    
    /**
     * Contrutor que recebe o mapa e as coordenadas dos limites que o jogador 
     * poderá se movimentar.
     * 
     * @param tileMap Objeto com informações do mapa.
     * @param initScreen Coordenada do limite esquerdo.
     * @param screenOffset Coornada do limite direito.
     */
    public Jogador( TileMap tileMap, double initScreen, double screenOffset ) {
        
        this.tileMap = tileMap;
        
        this.initScreen = initScreen;
        this.screenOffset = screenOffset;   
        
        vidas = 3;
        
        largura = 34;
        altura = 37;

        velMov = 0.6;
        velMovMax = 4.2;
        velQuedaMax = 12;
        velParada = 0.7;
        pulo = -12;
        gravidade = 0.64;
        
        ativo = true;
        desativando = false;
        invencivel = false;
        
        tempoInvencibilidade = 2;
        
        try {
            
            inicializarSprites();
            
        } catch ( IOException e ) {
        
            System.out.println( "Não foi possível inicializar os sprites do jogador!" );
            
        }
        
    }

    public int getVidas() {
        
        return vidas;
        
    }

    public long getUltimoTempo() {
        
        return ultimoTempo;
        
    }
    
    public boolean isCaindo() {
        
        return caindo;
        
    }

    public boolean isInvencivel() {
        
        return invencivel;
        
    }
    
    /**
     * Setter do atributos que armazena a quantidade de vidas do jogador.
     * 
     * Caso o booleano 'atacado' seja verdadeiro, o modo invencível é 
     * ativado temporariamente.
     * 
     * @param vidas Quantidade de vidas alvo.
     * @param atacado Booleano verificando se o jogador foi atacado ou não.
     */
    public void setVidas( int vidas, boolean atacado ) {
        
        if( atacado ) {
            
            try {
                
                somAtacado = AudioSystem.getClip();
                somAtacado.open( AudioSystem.getAudioInputStream( new File( "sons/somAtacado.wav" ) ) );
                somAtacado.start();
                
            } catch ( IOException | LineUnavailableException | UnsupportedAudioFileException e ){
            
                System.out.println( "Falha de execução no arquivo de som 'Jogador Atacado'" );
                
            }

            tempoInicial = System.nanoTime();
            ultimoTempo = System.nanoTime();
            invencivel = true;
            
        }
        
        this.vidas = vidas;
        
    }
    
    public void setUltimoTempo( long ultimoTempo ) {
        
        this.ultimoTempo = ultimoTempo;
        
    }
    
    public void setPulando() { 
        
        if( !caindo ) {
            
            pulando = true;
            
        }
        
    }
    
    /**
     * Verifica se o jogador não está morrendo.
     */
    public void verificarVidas() {
        
        if( vidas <= 0 ) {
            
            desativando = true;
            
        }
        
    }
    
    /**
     * Faz o controle do tempo em que o jogador está invencível.
     */
    public void verificarInvencibilidade() {
        
        long diferenca = System.nanoTime() - tempoInicial;
        
        if( diferenca/1000000000 > tempoInvencibilidade ) {
            
            invencivel = false;
            
        }
        
    }
    
    /**
     * Controle da parte gráfica do jogador.
     * Caso o jogador esteja invencível, é nesse método que será feito o efeito
     * de piscar.
     * 
     * @param desenhar 
     */
    public void render( Graphics2D desenhar ) {
        
        if( ativo ) {
            
            if( invencivel && !desativando ) {
                
                long diferenca = System.nanoTime() - ultimoTempo;
                
                if( diferenca/1000000 < 100 ) {
                    
                    draw( desenhar );
                    
                } else if( diferenca/1000000 > 200 ) {
                    
                    ultimoTempo = System.nanoTime();
                    
                }
                
            } else {
                
                draw( desenhar );
                
            }
            
        }
        
    }
    
    /**
     * Método que verifica a colisão entre o jogador e o mapa antes que o jogador
     * se mova, caso exista colisão o movimento será impedido, se não o jogador
     * se moverá normalmente.
     * 
     * @param colunaAtual Tile em X que o jogador está.
     * @param linhaAtual Tile em Y em que o jogador está.
     * @param proximoX Coordenada em X onde o jogador estará depois do movimento.
     * @param proximoY Coordenada em Y onde o jogador estará depois do movimento.
     * @param initX Coordenada em X inicial do limite que o jogador poderá andar (usado para impedir que o jogador ande para fora do mapa)
     * @param offsetX Coordenada em X final do limite que o jogador poderá andar
     */
    private void checarColisao( int colunaAtual, int linhaAtual, double proximoX, double proximoY, double initX, double offsetX ) {

        double tempX = x;
        double tempY = y;
        
        calcularCantos( x, proximoY );
        if( dy < 0 ) {
            
            if( ( topLeft || topRight ) || ( y - altura/2 < 0 )  ) {
                
                dy = 0;
                tempY = linhaAtual * tileMap.getTamanhoTile() + altura/2;
                
            } else {
                
                tempY += dy;
                
            }
            
        }
        if( dy > 0 ) {
            
            if( ( bottomLeft || bottomRight ) ) {
                
                dy = 0;
                caindo = false;
                
                tempY = ( linhaAtual + 1 ) * tileMap.getTamanhoTile() - altura/2;
                
            } else {
                
                tempY += dy;
                
            }
            
        }
        
        calcularCantos( proximoX, y );
        if( dx < 0 ) {
            
            if( ( topLeft || bottomLeft ) || ( x - largura/2 <= initX  ) ) {
                
                dx = 0;
                tempX = colunaAtual * tileMap.getTamanhoTile() + largura/2;
                
            } else {
                
                tempX += dx;
                
            }
            
        }
        if( dx > 0 ) {
            
            if( ( topRight || bottomRight ) || ( x + largura/2 >= offsetX - 7 ) ) {
                
                dx = 0;
                tempX = ( colunaAtual + 1 ) * tileMap.getTamanhoTile() - largura/2;
                
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
    
    /**
     * Update da parte lógica do jogador: atualiza a direção e o sprite do jogador,
     * movimenta, verifica as colisões e os estados.
     */
    @Override
    public void update() {
        
        controleDirecao();
        
        setSprite();
        
        int colunaAtual = tileMap.getColTile( ( int ) x );
        int linhaAtual = tileMap.getRowTile( ( int ) y );
        
        double proximoX = x + dx;
        double proximoY = y + dy;
        
        checarColisao( colunaAtual, linhaAtual, proximoX, proximoY, initScreen, screenOffset );
        
        if( invencivel ) {
            
            verificarInvencibilidade();
            
        }
        
        if( desativando && animacao.getAcabou() ) {
            
            ativo = false;
            
        }
        
        tileMap.setX( ( int ) ( PainelJogo.LARGURA / 3 - x ) );

    }
    
    /**
     * Faz o acesso as imagens dos sprites do jogador e os configura.
     * @throws IOException 
     */
    @Override
    protected void inicializarSprites() throws IOException {
        
        spriteParado = new BufferedImage[1];
        spritePulando = new BufferedImage[1];
        spriteCaindo = new BufferedImage[1];
            
        spriteAndando = new BufferedImage[4];
        spriteMorrendo = new BufferedImage[9];
            
        spriteParado[0] = ImageIO.read( new File( "graficos/jogador/jogadoridle.png" ) );
        spritePulando[0] = ImageIO.read( new File( "graficos/jogador/jogadorjump.png" ) );
        spriteCaindo[0] = ImageIO.read( new File( "graficos/jogador/jogadorfall.png" ) );

        BufferedImage andando = ImageIO.read( new File( "graficos/jogador/spritesheetwalking.png" ) );
        BufferedImage spritemorrendo = ImageIO.read( new File( "graficos/jogador/spritesheetdying.png" ) );

        for(  int i = 0; i < spriteAndando.length; i++ ) {
            
            spriteAndando[i] = andando.getSubimage( i * largura + i, 0, largura, altura );

        }
        
        for( int i = 0; i < spriteMorrendo.length; i++ ) {
            
            spriteMorrendo[i] = spritemorrendo.getSubimage( i * largura + i, 0, largura, altura );
            
        }
        
        animacao = new Animacao();
        viradoParaEsquerda = false;
        
    }
    
    /**
     * Movimenta o jogador com base na posição e na velocidade.
     * Também aplica a gravidade no jogador.
     */
    @Override
    protected void controleDirecao() {
        
        if( !desativando ) {
        
            //Esquerda e direita
            if( esquerda ) {

                dx -= velMov;

                if( dx < -velMovMax ) {

                    dx = -velMovMax;

                }

            } else if( direita ) {

                dx += velMov;

                if( dx > velMovMax ) {

                    dx = velMovMax;

                }

            } else {

                if( dx > 0 ) {

                    dx -= velParada;

                    if( dx < 0 ) {

                        dx = 0;

                    }

                } else if( dx < 0 ) {

                    dx += velParada;

                    if( dx > 0 ) {

                        dx = 0;

                    }

                } 

            }

            //Pulo        
            if( pulando ) {
                
                try {

                    somPulo = AudioSystem.getClip();
                    somPulo.open( AudioSystem.getAudioInputStream( new File( "sons/somPulo.wav" ) ) );
                    somPulo.start();

                } catch ( IOException | LineUnavailableException | UnsupportedAudioFileException e ){
                
                    System.out.println( "Falha de execução no arquivo de som 'Jogador Pulo'" );
                    
                }
                
                dy = pulo;
                caindo = true;
                pulando = false;
                
            }

        } else {
            
            dx = 0;
            
        }
        
        if( caindo ) {

            dy += gravidade;

            if( dy > velQuedaMax ) {

                dy = velQuedaMax;

            }

        } else {

            dy = 0;

        }
        
    }
    
    /**
     * Seta o sprite com base nos estados do jogador e muda o frame da animação
     */
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

            if( dy < 0 ) {

                animacao.setFrames( spritePulando );
                animacao.setDelay( -1 );

            } 

            if( dy > 0 ) {

                animacao.setFrames( spriteCaindo );
                animacao.setDelay( -1 );

            } 
            
        }

        if( esquerda ) {
            
            viradoParaEsquerda = true;
            
        } else if( direita ) {
            
            viradoParaEsquerda = false;
            
        }
        
        animacao.update();

    }

}
