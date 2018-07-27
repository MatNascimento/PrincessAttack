package jogoplataforma.personagens;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Classe que lida com a lógica de colisão do jogo.
 */
public class Colisao {
    
    private class Extremidade {
    
        private double meiaLargura;
        private double meiaAltura;
        
        private double extremidadeEsquerda;
        private double extremidadeDireita;
        private double extremidadeCima;
        private double extremidadeBaixo;
        
        /**
         * 
         * Subclasse que faz o armazenamento das informações das extremidades
         * para realizar a lógica de colisão.
         * 
         * Lembrete: O ponto de origem de todos os elementos com coordenadas
         * no jogo é o centro desse mesmo elemento.
         * 
         * @param x Posição em X do objeto ou área de colisão.
         * @param y Posição em Y.
         * @param largura Largura do objeto ou área de colisão.
         * @param altura Altura.
         */
        public Extremidade( double x, double y, double largura, double altura ) {
            
            this.meiaLargura = largura/2;
            this.meiaAltura = altura/2;
            
            this.extremidadeEsquerda = x - meiaLargura;
            this.extremidadeDireita = x + meiaLargura;
            this.extremidadeCima = y - meiaAltura;
            this.extremidadeBaixo = y + meiaAltura;
            
        }
        
    }
    
    /**
     * 
     * Método que verifica se existe colisão entre o jogador e uma área que será passada por parâmetro.
     * 
     * Usado para lançar eventos quando o jogador estiver em determinada área do mapa.
     * 
     * @param jogador Objeto do jogador
     * @param x Posição em X da área ( Lembrando que as coordenadas são contadas com base no centro dos elementos )
     * @param y Posição em Y da área
     * @param largura Largura da área
     * @param altura Altura da área
     * @return Booleano informando se a colisão é verdadeira ou falsa
     */
    public boolean isColidindoArea( Jogador jogador, double x, double y, double largura, double altura ) {
        
        Extremidade extremidadeJogador = new Extremidade( jogador.getX(), jogador.getY(), jogador.getLargura(), jogador.getAltura() );
        
        Extremidade extremidadeArea = new Extremidade( x, y, largura, altura );

        return isColidindo( extremidadeJogador, extremidadeArea );
        
    }
    
    /**
     * Método que verifica a colisão entre o jogador e elementos inimigos e 
     * diminui a vida do jogador caso verdadeiro
     * 
     * @param jogador
     * @param inimigos 
     */
    public void colisaoInimigo( Jogador jogador, Personagem[] inimigos ) {
        
        Extremidade extremidadeJogador = new Extremidade( jogador.getX(), jogador.getY(), jogador.getLargura(), jogador.getAltura() );
        
        for ( Personagem inimigo : inimigos ) {
            
            if( inimigo.isAtivo() && !inimigo.isDesativando() && jogador.isAtivo() ) {
                
                Extremidade extremidadeInimigo = new Extremidade( inimigo.getX(), inimigo.getY(), inimigo.getLargura(), inimigo.getAltura() );

                if( isColidindo( extremidadeJogador, extremidadeInimigo ) ) {

                    if( jogador.getDy() > 0 && inimigo instanceof Inimigo ) {

                        if( ( extremidadeInimigo.extremidadeCima - extremidadeJogador.extremidadeBaixo ) < 5 ) {
                            
                            try{
                                
                                Clip morteInimigo = AudioSystem.getClip();
                                morteInimigo.open( AudioSystem.getAudioInputStream( new File( "sons/somMorteInimigo.wav" ) ) );
                                morteInimigo.start();
                                
                            } catch( Exception e ){}

                            jogador.setCaindo( false );
                            jogador.setPulando();
                            inimigo.setDesativando( true );

                        } 

                    } else if( !jogador.isInvencivel() ) {
                        
                        int vidaJogador = jogador.getVidas();
                        jogador.setVidas( --vidaJogador, true );
                        jogador.verificarVidas();

                    }

                }
                
            }

        }
        
    }
    
    /**
     * Método que centraliza a lógica de colisão.
     * 
     * @param extremidadeJogador Objeto com as extremidades do jogador
     * @param extremidadeObjeto Objeto com as extremidades de um inimigo, projétil ou área
     * @return Booleano informando se há ou não colisão
     */
    private boolean isColidindo( Extremidade extremidadeJogador, Extremidade extremidadeObjeto ) {
        
        return     extremidadeJogador.extremidadeEsquerda < extremidadeObjeto.extremidadeDireita 
                && extremidadeJogador.extremidadeDireita  > extremidadeObjeto.extremidadeEsquerda 
                && extremidadeJogador.extremidadeCima     < extremidadeObjeto.extremidadeBaixo
                && extremidadeJogador.extremidadeBaixo    > extremidadeObjeto.extremidadeCima;
        
    }
    
}
