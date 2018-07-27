package jogoplataforma.personagens;

import jogoplataforma.visual.Animacao;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import jogoplataforma.TileMap;

/**
 * Classe com as características e métodos de um personagem genérico do jogo
 */
public abstract class Personagem {
    
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    
    protected int largura;
    protected int altura;
    
    protected boolean esquerda;
    protected boolean direita;
    protected boolean caindo;
    
    protected double velMov;
    protected double velQuedaMax;
    protected double gravidade;
    
    protected double initScreen;
    protected double screenOffset;
    
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;
    
    protected boolean ativo;
    protected boolean desativando;
    
    protected TileMap tileMap;
    protected Animacao animacao;
    
    protected BufferedImage[] spriteParado;
    protected BufferedImage[] spriteAndando;
    protected BufferedImage[] spriteCaindo;
    protected BufferedImage[] spriteMorrendo;
    
    protected boolean viradoParaEsquerda;
    
    public double getX() {
        
        return x;
        
    }

    public double getY() {
        
        return y;
        
    }
    
    public double getDx() {
        
        return dx;
        
    }

    public double getDy() {
        
        return dy;
        
    }
    
    public int getLargura() {
        
        return largura;
        
    }

    public int getAltura() {
        
        return altura;
        
    }

    public boolean isAtivo() {
        
        return ativo;
        
    }
    
    public boolean isDesativando() {
        
        return desativando;
        
    }
    
    public void setX( double x ) {
        
        this.x = x;
        
    }

    public void setY( double y ) {
        
        this.y = y;
        
    }
    
    public void setDx( double dx ) {
        
        this.dx = dx;
        
    }

    public void setDy( double dy ) {
        
        this.dy = dy;
        
    }

    public void setEsquerda( boolean esquerda ) { 
        
        this.esquerda = esquerda; 
        
    }
    
    public void setDireita( boolean direita ) {
        
        this.direita = direita;
        
    }

    public void setCaindo( boolean caindo ) {
        
        this.caindo = caindo;
        
    }
    
    public void setAtivo( boolean ativo ) {
        
        this.ativo = ativo;
        
    }

    public void setDesativando( boolean desativando ) {
        
        this.desativando = desativando;
        
    }
    
    public void setPosicao( double x, double y ) {
        
        this.setX( x );
        this.setY( y );
        
    }
    
    /**
     * Classe que pega a coordenada de entrada em pixels e retorna a posição 
     * em X ou Y em que essa coordenada está de acordo com o Mapa.
     * 
     * @param valor Coordenada em pixels.
     * @param eixo Eixo em que você quer receber o resultado: X ou Y (o caracter passado precisa estar em letra minúscula)
     * @return Coordenada no Mapa referente a entrada.
     */
    public double coordenadaParaTile( double valor, char eixo ) {
        
        valor += 0.5;
        
        int tamanho = tileMap.getTamanhoTile();
        
        if( eixo == 'x' ) {
            
            double retorno = valor * tamanho - this.largura;
            
            return retorno;
            
        } else if( eixo == 'y' ) {
            
            double retorno = valor * tamanho - this.altura;
            
            return retorno;
            
        }
        
        return 0;
        
    }
    
    /**
     * Método que desenha o personagem no contexto gráfico e faz o controle do 
     * lado em que o personagem está. (Para evitar que o sprite seja desenhado invertido)
     * @param desenhar Objeto de manipulação gráfica 2D
     */
    public void draw( Graphics2D desenhar ) {
        
        int tx = tileMap.getX();
        
        if( !viradoParaEsquerda ) {
            
            desenhar.drawImage( animacao.getImage(), ( int ) ( tx + x - largura/2 ), ( int ) ( y - altura/2 ) , largura, altura, null );
            
        } else {
            
            desenhar.drawImage( animacao.getImage(), ( int ) ( tx + x - largura/2 + largura ), ( int ) ( y - altura/2 ) , -largura, altura, null );
            
        }
        
    }
    
    /**
     * Método que calcula as tiles vizinhas de uma tile e configura quais 
     * estão bloqueadas para movimento.
     * 
     * @param x Posição X em pixels da Tile alvo.
     * @param y Posição Y em pixels.
     */
    protected void calcularCantos( double x, double y ) {
        
        int tileEsquerdo = tileMap.getColTile( ( int ) x - largura/2 );
        int tileDireito = tileMap.getColTile( ( int ) x + largura/2 - 1);
        int tileTopo = tileMap.getRowTile( ( int ) y - altura/2 );
        int tileBaixo = tileMap.getRowTile( ( int ) y + altura/2 - 1);
        
        topLeft = tileMap.isBloqueado( tileTopo , tileEsquerdo );
        topRight = tileMap.isBloqueado( tileTopo , tileDireito );
        bottomLeft = tileMap.isBloqueado( tileBaixo , tileEsquerdo );
        bottomRight = tileMap.isBloqueado( tileBaixo , tileDireito );

    }
    
    /**
     * Método que atualiza a parte lógica de cada classe  que extende Personagem.
     */
    public abstract void update();
    
    /**
     * Método que inicializa os sprites do Personagem.
     * @throws IOException 
     */
    protected abstract void inicializarSprites() throws IOException;
    
    /**
     * Método que faz o controle de movimentação do personagem.
     */
    protected abstract void controleDirecao();
    
    /**
     * Método que define qual Sprite está ativo
     */
    protected abstract void setSprite();

}
