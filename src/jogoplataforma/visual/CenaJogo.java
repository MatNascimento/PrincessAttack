package jogoplataforma.visual;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import jogoplataforma.personagens.Colisao;
import jogoplataforma.personagens.Inimigo;
import jogoplataforma.personagens.Jogador;
import jogoplataforma.TileMap;
import jogoplataforma.mapas.Mapa;
import jogoplataforma.mapas.MapaController;
import jogoplataforma.personagens.InimigoAtirador;
import jogoplataforma.personagens.Projetil;

/**
 * Cena que possui as características e controles necessários para rodar o jogo.
 */
public class CenaJogo extends Cena {
    
    private int inimigosTotais;
    private int inimigosEliminados;
    
    private int vidas;
    
    private long tempoInicio = 0;
    
    private BufferedImage vida;
    private BufferedImage rosto;
    
    private TileMap tileMap;
    
    private Jogador jogador;
    private Inimigo[] inimigos;
    private Projetil[] projetil;
    
    private Mapa mapaAtual;
    private MapaController controladorDeMapas;
    
    private Colisao colisaoJogadorInimigo;
    private Colisao colisaoJogadorProjetil;
    private Colisao colisaoJogadorArea;
    
    private boolean trocarMapa;
    private boolean venceu;

    public CenaJogo( int largura, int altura ) {
        
        super( largura, altura );
        
    }

    public Jogador getJogador() {
        
        return jogador;
        
    }

    public void setJogador(Jogador jogador) {
        
        this.jogador = jogador;
        
    }
    
    /**
     * Inicializa os elementos necessários para a cena.
     */
    public void init() {
        
        if( !trocarMapa ) {
            
            controladorDeMapas = new MapaController();
            
        }
        
        try {
            
            rosto = ImageIO.read( new File( "graficos/jogador/jogadorrosto.png" ) );
            vida = ImageIO.read( new File( "graficos/vida.png" ) );
            
        } catch ( IOException e ){
        
            System.out.println( "Falha ao encontrar as imagens do HUD" );
            
        }
        
        venceu = false;
        
        mapaAtual = controladorDeMapas.getMapaAtual();
        
        tileMap = mapaAtual.getTileMap();

        inimigos = mapaAtual.getInimigos();
        projetil = mapaAtual.getProjetil();
        
        jogador = new Jogador( tileMap, 0, tileMap.getLarguraMapa() * tileMap.getTamanhoTile() );
        
        //Posição inicial do jogador
        jogador.setPosicao( mapaAtual.getPosicaoJogadorX(), mapaAtual.getPosicaoJogadorY() );
        
        colisaoJogadorInimigo = new Colisao();
        colisaoJogadorProjetil = new Colisao();
        colisaoJogadorArea = new Colisao();
        
        imagemFundo = mapaAtual.getImagemFundo();
        
    }
    
    /**
     * Faz o controle temporal dos disparos realizados por inimigos atiradores.
     */
    public void inimigoDisparaProjetil() {
        
        if( tempoInicio == 0 ) {

            tempoInicio = System.currentTimeMillis();

        }

        long diferenca = System.currentTimeMillis() - tempoInicio;

        if( diferenca > 3000 ) {
            
            int j = 0;
            
            for ( int i = 0; i < inimigos.length; i++ ) {
                
                if ( inimigos[i] instanceof InimigoAtirador ) {
                    
                    try {
                        
                        Clip somAtirador = AudioSystem.getClip();
                        somAtirador.open( AudioSystem.getAudioInputStream( new File( "sons/somInimigoAtirador.wav" ) ) );
                        somAtirador.start();
                        
                    } catch ( IOException | LineUnavailableException | UnsupportedAudioFileException e ){
                    
                        System.out.println( "Falha de execução no arquivo de som 'Inimigo Atirador Tiro'" );
                        
                    }
                    
                    projetil[j].setAtivo( true );
                    projetil[j].disparar( ( int ) inimigos[i].getX() - projetil[j].getLargura(), ( int ) ( inimigos[i].getY() ), true );
                    
                    j++;
                    
                    tempoInicio = 0;
                    
                }
            }
        }
        
    }
    
    public boolean venceu() {
        
        return venceu;
        
    }
    
    /**
     * Desenha a interface com informações sobre o jogo.
     * 
     * Exemplo: quantidade de vidas do jogador, quantidade de inimigos totais e eliminados.
     * 
     * @param desenhar
     * @throws IOException 
     */
    private void desenharGUI( Graphics2D desenhar ) throws IOException {
        
        inimigosTotais = inimigos.length;
        inimigosEliminados = 0;
        
        for( int i = 0; i < inimigosTotais; i++ ) {
            
            if( !inimigos[i].isAtivo() ) {
                
                inimigosEliminados++;

            }
            
        }
        
        BufferedImage numeroTotais = ImageIO.read( new File( "graficos/numeros/" + inimigosTotais + ".png" ) );
        BufferedImage traco = ImageIO.read( new File( "graficos/numeros/traco.png" ) );
        BufferedImage numeroEliminados = ImageIO.read( new File( "graficos/numeros/" + inimigosEliminados + ".png" ) );
        
        int margemDireita = this.LARGURA - numeroTotais.getWidth() - 15;
        
        vidas = jogador.getVidas();
        
        if( vidas != 0 ) {
            
            desenhar.drawImage( numeroTotais, margemDireita, 10, null );
            margemDireita -= traco.getWidth();
            desenhar.drawImage( traco, margemDireita, 10, null );
            margemDireita -= numeroEliminados.getWidth();
            desenhar.drawImage( numeroEliminados, margemDireita, 10, null );
            
            desenhar.drawImage( rosto, 10, 10, null );
            
            for( int i = 0; i < vidas; i++ ) {

                desenhar.drawImage( vida , i * vida.getWidth() + i + 15 + 38, 17, null );

            }
            
        }

    }
    
    /**
     * Atualiza a lógica dos elementos da cena e do mapa atual.
     */
    @Override
    public synchronized void update() {
        
        if( inimigosEliminados >= inimigosTotais ) {
            
            int tamanhoTiles = tileMap.getTamanhoTile();
            
            if( colisaoJogadorArea.isColidindoArea( jogador, mapaAtual.getAreaFimX(), mapaAtual.getAreaFimY(), tamanhoTiles, tamanhoTiles ) ){
                
                controladorDeMapas.trocarMapa();
                trocarMapa = true;
                
                if( trocarMapa && controladorDeMapas.getMapaAtual() != null  ) { 
                    
                    init();
                    jogador.setVidas( vidas, false );
                    trocarMapa = false;

                } else if( controladorDeMapas.getMapaAtual() == null ) {
            
                    venceu = true;

                }
                
            }
            
        }
        
        for( Inimigo inimigo : inimigos ) {
            
            if( inimigo.isAtivo() ) {
                
                inimigo.update();
                
                if ( inimigo instanceof InimigoAtirador && !inimigo.isDesativando() && inimigo.isAtivo() ) {
                    
                    inimigoDisparaProjetil();
                    
                }
                
            }

        }
        
        for( Projetil proj : projetil ) {
            
            if( proj.isAtivo() ) {

                proj.update();

            }

        }
        
        if( jogador.isAtivo() ) {
            
            jogador.update();
            
        }
        
        colisaoJogadorInimigo.colisaoInimigo( jogador, inimigos );
        colisaoJogadorProjetil.colisaoInimigo( jogador, projetil );
        
    }
    
    /**
     * Método que atualiza a parte gráfica da Cena do Jogo.
     * 
     * @param desenhar 
     */
    @Override
    public synchronized void draw( Graphics2D desenhar ) {
        
        desenhar.drawImage( imagemFundo, 0, 0, LARGURA, ALTURA, null );

        tileMap.draw( desenhar );

        for( Inimigo inimigo : inimigos ) {

            if( inimigo.isAtivo() ) {
                
                inimigo.draw( desenhar );
                
            }
            
        }
        
        for( Projetil proj : projetil ) {

            if( proj.isAtivo() ) {
            
                proj.draw( desenhar );

            }

        }

        jogador.render( desenhar );
        
        try {
            
            desenharGUI( desenhar );
            
        } catch (IOException ex) {
            
            Logger.getLogger(CenaJogo.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }

}