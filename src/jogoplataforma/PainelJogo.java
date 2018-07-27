package jogoplataforma;

import jogoplataforma.personagens.Jogador;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import jogoplataforma.visual.Cena;
import jogoplataforma.visual.CenaJogo;
import jogoplataforma.visual.CenaMenu;

/**
 * Classe onde contém a lógica, o controle e a exibição gráfica geral do jogo.
 * Ela extende o JPanel e implementa as interfaces Runnable e KeyListener.
 * 
 * JPanel: elemento gráfico do JFrame, é um painel em branco usado geralmente 
 * para manipulação gráfica na tela de forma manual.
 * 
 * Runnable: permite o uso de Threads.
 * KeyListener: permite o uso de eventos de teclado.
 */
public class PainelJogo extends JPanel implements Runnable, KeyListener {
    
    public static final int LARGURA = 570;
    public static final int ALTURA = 456;
    
    public static final int FPS = 30;
    private final int tempoAlvo  = 1000 / FPS;
    
    private final int tempoTransicao = 1000;
    private long tempoInicioTransicao;
    
    private Thread thread;
    private boolean jogando;
    
    private boolean reiniciar;
    
    private BufferedImage buffer;
    private Graphics2D desenhar;
    
    private Cena cenaAtual;
    
    private Cena gameover;
    private Cena vitoria;
    private Cena credito;
    private CenaMenu menu;
    private CenaJogo jogo;
    
    private Jogador jogador;
    
    private Clip somMenu;
    private Clip musicaGameover;
    private Clip musicaMenu;
    private Clip musicaVitoria;
    private Clip musicaJogo;
    
    /**
      * Construtor: São inicializadas as configurações de tamanho e interação do Painel.
      */
    public PainelJogo() {
        
        super();
        setPreferredSize( new Dimension( LARGURA, ALTURA ) );
        setFocusable( true );
        requestFocus();
        
    }
    
    /**
      * Inicializador: Inicializa todos os objetos necessários para o funcionamento imediato do jogo.
      */
    private synchronized void init() {
        
        jogando = true;
        
        buffer = new BufferedImage( LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB );
        desenhar = ( Graphics2D ) buffer.getGraphics();
        
        menu = new CenaMenu( LARGURA, ALTURA );
        
        credito = new Cena( LARGURA, ALTURA );
        gameover = new Cena( LARGURA, ALTURA );
        vitoria = new Cena( LARGURA, ALTURA );
        
        try {
            
            credito.setImagemFundo( ImageIO.read( new File( "graficos/cenas/creditos.png" ) ) );
            gameover.setImagemFundo( ImageIO.read( new File( "graficos/cenas/gameover.png" ) ) );
            vitoria.setImagemFundo( ImageIO.read( new File( "graficos/cenas/telaVitoria.png" ) ) );
            
            somMenu = AudioSystem.getClip();
            somMenu.open( AudioSystem.getAudioInputStream( new File( "sons/somMenu.wav" ) ) );
            
            musicaGameover = AudioSystem.getClip();
            musicaGameover.open( AudioSystem.getAudioInputStream( new File( "sons/musica/GameOverMusic.wav" ) ) );
            
            musicaMenu = AudioSystem.getClip();
            musicaMenu.open( AudioSystem.getAudioInputStream( new File( "sons/musica/MenuMusic.wav" ) ) );
            musicaMenu.loop( Clip.LOOP_CONTINUOUSLY );
            
            musicaVitoria = AudioSystem.getClip();
            musicaVitoria.open( AudioSystem.getAudioInputStream( new File( "sons/musica/VictoryMusic.wav" ) ) );
            
            musicaJogo = AudioSystem.getClip();
            musicaJogo.open( AudioSystem.getAudioInputStream( new File( "sons/musica/GameMusic.wav" ) ) );
            
        } catch( Exception e ){
        
            System.out.println( "Falha ao inicializar imagens, sons e musicas do PainelJogo" );
            
        }
        
        jogo = new CenaJogo( LARGURA, ALTURA );
        jogo.init();
       
        jogador = jogo.getJogador();
        
        cenaAtual = menu;

    }
    
    /**
     * Método que faz o controle do tempo de transição entre as cenas do jogo
     * 
     * @param cena Cena para qual se deseja ir
     */
    private void transicao( Cena cena ) {
        
        if( tempoInicioTransicao == 0 ) {
                
            tempoInicioTransicao = System.currentTimeMillis();

        }

        long diferenca = System.currentTimeMillis() - tempoInicioTransicao;

        if( diferenca > tempoTransicao ) {
            
            try {
                
                musicaJogo.stop();
                musicaGameover.start(); 
                
            } catch ( Exception e ){
            
                System.out.println( "Falha no controle de músicas" );
                
            }
            
            cenaAtual = cena;
            tempoInicioTransicao = 0;

        }
        
    }
    
    /**
     * Método que atualiza a parte lógica dos elementos do Painel
     */
    private synchronized void update() {
        
        cenaAtual.update();
        
        if( jogo.venceu() ) {
            
            musicaJogo.stop();
            musicaVitoria.start();

            cenaAtual = vitoria;
            
        }
        
    }
    
    /**
     * Método que atualiza a parte visual dos elementos do painel e os armazena em um buffer
     */
    private synchronized void render(){
        
        if( !jogador.isAtivo() && cenaAtual == jogo ){
            
           transicao( gameover );
           
        }
        
        cenaAtual.draw( desenhar );
        
    }
    
    /**
     * Método que pega o conteúdo do buffer e desenha-os na janela
     */
    private synchronized void draw() {
        
        Graphics2D g2d = ( Graphics2D ) this.getGraphics();
        g2d.drawImage( buffer, 0, 0, this );
        g2d.dispose();
        
    }
    
    /**
     * Método da interface Runnable que configura a Thread
     */
    @Override
    public void addNotify() {
        
        super.addNotify();
        
        if( thread == null ) {
            
            thread = new Thread( this );
            thread.start();
            
        }
        
        addKeyListener( this );
        
    }
    
    /**
     * Método obrigatório da interface Runnable que é executado automaticamente durante a instanciação de um objeto da classe
     */
    @Override
    public void run() {
        
        init();

        long tempoInicio;
        long tempoPercorrido;
        long tempoEspera;
        
        while( jogando ) {
            
            if( reiniciar ) {
                
                musicaVitoria.stop();
                init();
                reiniciar = false;
                
            }
            
            tempoInicio = System.nanoTime();
            
            update();
            render();
            draw();

            //Converte o tempo percorrido de nanosegundos para milisegundos
            tempoPercorrido = ( System.nanoTime() - tempoInicio ) / 1000000; 
            
            tempoEspera = tempoAlvo - tempoPercorrido;
            
            try {
                
                if( tempoEspera > 0 ) {
                    
                    thread.sleep( tempoEspera );
                    
                }
                
            } catch ( InterruptedException ex ) {
                
                Logger.getLogger( PainelJogo.class.getName() ).log( Level.SEVERE, null, ex );
                
            }
            
        }
        
    }
    
    /**
     * Método obrigatório da interface KeyListener que permite a captura das teclas pressionadas pelo usuário
     * 
     * @param e Atributo com informações sobre o evento lançado
     */
    @Override
    public void keyPressed( KeyEvent e ) {
        
        jogador = jogo.getJogador();
        
        int codigoTecla = e.getKeyCode();
        
        if( cenaAtual == jogo ) {

            switch ( codigoTecla ) {

                case KeyEvent.VK_RIGHT:

                    jogador.setEsquerda( false );
                    jogador.setDireita( true );
                    break;

                case KeyEvent.VK_LEFT:

                    jogador.setDireita( false );
                    jogador.setEsquerda( true );
                    break;

                case KeyEvent.VK_UP:

                    jogador.setPulando();
                    break;

                default:
                    break;
            }

            jogo.setJogador( jogador );
        
        }
        
    }
    
    @Override
    public void keyReleased( KeyEvent e ) {
        
        int codigoTecla = e.getKeyCode();

        if( cenaAtual == menu ) {
            
            /////////controle menu////////////////
            
            switch ( codigoTecla ) {

                case KeyEvent.VK_ENTER:
                    
                    try{
                        
                        Clip somMenuSelecao = AudioSystem.getClip();
                        somMenuSelecao.open( AudioSystem.getAudioInputStream( new File( "sons/somSelecaoMenu.wav" ) ) );
                        somMenuSelecao.start();
                        
                    } catch ( Exception ee ){}
                    
                    if( menu.isFocusCredito() ) {

                        cenaAtual = credito;
                        
                    } else {
                        
                        musicaMenu.stop();
                        musicaJogo.loop( Clip.LOOP_CONTINUOUSLY );
                        cenaAtual = jogo;
                        
                    }

                    break;

                case KeyEvent.VK_DOWN:
                    
                    somMenu.setFramePosition(0);
                    somMenu.start();
                    
                    menu.setFocusCredito( true );
                    break;

                case KeyEvent.VK_UP:
                    
                    somMenu.setFramePosition(0);
                    somMenu.start();
                    
                    menu.setFocusCredito( false );
                    break;

                default:
                    break;
            }
            
        } else if( cenaAtual == credito ) {
            
            /////////controle credito///////////
            
            if( codigoTecla == KeyEvent.VK_ENTER ) {
                
                try{
                        
                        Clip somMenuSelecao = AudioSystem.getClip();
                        somMenuSelecao.open( AudioSystem.getAudioInputStream( new File( "sons/somSelecaoMenu.wav" ) ) );
                        somMenuSelecao.start();
                        
                    } catch ( Exception ee ){}
                
                cenaAtual = menu;
                
            }
            
        } else if( cenaAtual == gameover || cenaAtual == vitoria ) {

            /////////controle gameover///////////
            
            if( codigoTecla == KeyEvent.VK_ENTER ) {
                
                musicaGameover.stop();
                
                try{
                        
                        Clip somMenuSelecao = AudioSystem.getClip();
                        somMenuSelecao.open( AudioSystem.getAudioInputStream( new File( "sons/somSelecaoMenu.wav" ) ) );
                        somMenuSelecao.start();
                        
                    } catch ( Exception ee ){}
                
                reiniciar = true;
                
            }
            
        } else if( cenaAtual == jogo ) {
            
            /////////controle movimento jogador////////////
            
            jogador = jogo.getJogador();
            
            if( codigoTecla == KeyEvent.VK_RIGHT ) {

                jogador.setDireita( false );

            } else if( codigoTecla == KeyEvent.VK_LEFT ) {

                jogador.setEsquerda( false );

            }

            jogo.setJogador( jogador );
            
        }
        
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    //Método não utilizado
    @Override
    public void keyTyped( KeyEvent e ) {}
    
}
